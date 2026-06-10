/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.auxiliary;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import reika.dragonapi.DragonAPI;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.api.interfaces.ThermalMachine;
import reika.rotarycraft.auxiliary.RotaryAux;
import reika.rotarycraft.auxiliary.recipemanagers.FrictionHeaterRecipe;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
import reika.rotarycraft.auxiliary.interfaces.TemperatureTE;
import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.DifficultyEffects;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryRecipeTypes;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.SoundRegistry;

import java.util.Optional;

public class BlockEntityFurnaceHeater extends BlockEntityPowerReceiver implements TemperatureTE, ConditionalOperation {

    public static final int MAXTEMP = 2000;

    private BlockPos furnaceLocation;

    private int temperature;
    private int smeltTime = 0;
    private int soundtick = 0;

    public BlockEntityFurnaceHeater(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.FRICTION_HEATER.get(), pos, state);
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.FRICTION_HEATER.get();
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();

        Direction facing = getBlockState().getValue(BlockRotaryCraftMachine.FACING);
        read = facing.getOpposite();
        this.getPower(false);
        this.getFurnaceCoordinates(facing);

        tickcount++;
        if (tickcount >= 20) {
            tickcount = 0;
            this.updateTemperature(world, pos);
        }

        if (world.isClientSide())
            return;
        if (!this.isActive())
            return;

        BlockEntity te = world.getBlockEntity(furnaceLocation);
        if (te instanceof AbstractFurnaceBlockEntity furn) {
            this.heatFurnace(world, pos, furn);
        } else if (te instanceof ThermalMachine tm) {
            this.heatMachine(world, pos, tm);
        }
    }

    private void getFurnaceCoordinates(Direction facing) {
        furnaceLocation = worldPosition.relative(facing);
    }

    public boolean isActive() {
        return power >= MINPOWER && torque >= MINTORQUE;
    }

    private boolean hasHeatableMachine(Level world) {
        if (furnaceLocation == null)
            return false;
        BlockEntity te = world.getBlockEntity(furnaceLocation);
        if (te instanceof AbstractFurnaceBlockEntity)
            return true;
        return te instanceof ThermalMachine tm && tm.canBeFrictionHeated();
    }

    // Drives a vanilla furnace via its public Container slots (input 0, output 2) on a
    // temperature-scaled timer; the furnace's private burn-time fields are inaccessible in 1.21.5.
    // High-temperature friction recipes (data-driven, RotaryRecipeTypes.FRICTION_HEATER) take
    // priority over vanilla smelting when the heater is hot enough.
    private void heatFurnace(Level world, BlockPos pos, AbstractFurnaceBlockEntity furn) {
        ItemStack in = furn.getItem(0);
        if (in.isEmpty()) {
            smeltTime = 0;
            return;
        }
        SingleRecipeInput input = new SingleRecipeInput(in);

        ItemStack result;
        int duration = 200;
        Optional<RecipeHolder<FrictionHeaterRecipe>> friction =
                world.getServer().getRecipeManager().getRecipeFor(RotaryRecipeTypes.FRICTION_HEATER.get(), input, world);
        if (friction.isPresent() && temperature >= friction.get().value().requiredTemperature()) {
            result = friction.get().value().assemble(input).copy();
            duration = friction.get().value().duration();
        } else {
            Optional<RecipeHolder<SmeltingRecipe>> smelt =
                    world.getServer().getRecipeManager().getRecipeFor(RecipeType.SMELTING, input, world);
            if (smelt.isEmpty()) {
                smeltTime = 0;
                return;
            }
            result = smelt.get().value().assemble(input).copy();
        }

        ItemStack out = furn.getItem(2);
        if (!this.canMerge(out, result)) {
            return;
        }

        smeltTime += 1 + this.getSpeedFactorFromTemperature();
        if (smeltTime >= duration) {
            smeltTime = 0;
            in.shrink(1);
            furn.setItem(0, in.isEmpty() ? ItemStack.EMPTY : in);
            if (out.isEmpty()) {
                furn.setItem(2, result);
            } else {
                out.grow(result.getCount());
                furn.setItem(2, out);
            }
            furn.setChanged();
            if (ConfigRegistry.FRICTIONXP.getState())
                ReikaWorldHelper.splitAndSpawnXP(world, furnaceLocation.getX() + 0.5, furnaceLocation.getY() + 0.6, furnaceLocation.getZ() + 0.5, 1, 600);
        }

        this.playEffects(world, pos);
    }

    private boolean canMerge(ItemStack out, ItemStack result) {
        if (out.isEmpty())
            return true;
        if (!ReikaItemHelper.matchStacks(out, result))
            return false;
        return out.getCount() + result.getCount() <= Math.min(out.getMaxStackSize(), 64);
    }

    private void heatMachine(Level world, BlockPos pos, ThermalMachine te) {
        if (!te.canBeFrictionHeated())
            return;
        int tdiff = Math.min(te.getMaxTemperature(), temperature) - te.getTemperature();
        if (tdiff > 0 || (tdiff == 0 && temperature == te.getMaxTemperature())) {
            te.addTemperature(Math.max(1, (int) (tdiff * te.getMultiplier())));
        }
        if (te.getTemperature() > te.getMaxTemperature()) {
            te.onOverheat(world, furnaceLocation);
        }
        this.playEffects(world, pos);
    }

    private void playEffects(Level world, BlockPos pos) {
        soundtick++;
        if (soundtick > 49) {
            SoundRegistry.FRICTION.playSoundAtBlock(world, pos, RotaryAux.isMuffled(this) ? 0.1F : 0.5F, 1);
            soundtick = 0;
        }
    }

    private int getSpeedFactorFromTemperature() {
        if (temperature < 500)
            return 1;
        if (temperature >= MAXTEMP)
            return 2000;
        return 1 + (int) Math.sqrt(Math.pow(2, ((temperature - 500) / 100F)));
    }

    @Override
    public void updateTemperature(Level world, BlockPos pos) {
        if (this.isActive() && omega > 0 && this.hasHeatableMachine(world)) {
            temperature += 3 * ReikaMathLibrary.logbase(omega, 2) * ReikaMathLibrary.logbase(torque, 2);
        }
        int Tamb = power > MINPOWER && torque > MINTORQUE ? 30 : ReikaWorldHelper.getAmbientTemperatureAt(world, pos); //prevent nether exploit
        if (temperature > Tamb) {
            temperature -= (temperature - Tamb) / 5;
        } else {
            temperature += (temperature - Tamb) / 5;
        }
        if (temperature - Tamb <= 4 && temperature > Tamb)
            temperature--;
        if (temperature > MAXTEMP)
            temperature = MAXTEMP;
        if (temperature >= MAXTEMP && !world.isClientSide() && ConfigRegistry.BLOCKDAMAGE.getState()
                && DragonAPI.rand.nextInt(DifficultyEffects.FURNACEMELT.getInt()) == 0)
            this.meltFurnace(world);
        if (temperature < Tamb)
            temperature = Tamb;
    }

    private void meltFurnace(Level world) {
        if (furnaceLocation == null)
            return;
        Block id = world.getBlockState(furnaceLocation).getBlock();
        if (id != Blocks.FURNACE)
            return;
        world.explode(null, furnaceLocation.getX() + 0.5, furnaceLocation.getY() + 0.5, furnaceLocation.getZ() + 0.5, 1F, Level.ExplosionInteraction.BLOCK);
        world.setBlock(furnaceLocation, Blocks.AIR.defaultBlockState(), 3);
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
        if (!this.isInWorld()) {
            phi = 0;
            return;
        }
        if (power < MINPOWER || torque < MINTORQUE)
            return;
        phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.FRICTION;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public int getRedstoneOverride() {
        return temperature / 100;
    }

    @Override
    public int getThermalDamage() {
        return temperature * 5 / 1200;
    }

    @Override
    public void addTemperature(int temp) {
        temperature += temp;
    }

    @Override
    public int getTemperature() {
        return temperature;
    }

    @Override
    public void setTemperature(int temp) {
        temperature = temp;
    }

    @Override
    public void overheat(Level world, BlockPos pos) {

    }

    @Override
    public void onEMP() {
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        NBT.putInt("temp", temperature);
        if (furnaceLocation != null)
            NBT.putLong("furnLoc", furnaceLocation.asLong());
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        temperature = NBT.getIntOr("temp", 0);
        if (NBT.contains("furnLoc"))
            furnaceLocation = BlockPos.of(NBT.getLongOr("furnLoc", 0L));
    }

    @Override
    protected String getTEName() {
        return null;
    }

    @Override
    public boolean areConditionsMet() {
        return this.hasHeatableMachine(level);
    }

    @Override
    public String getOperationalStatus() {
        return this.areConditionsMet() ? "Operational" : "No Heatable Machine";
    }

    @Override
    public boolean canBeCooledWithFins() {
        return false;
    }

    @Override
    public boolean allowHeatExtraction() {
        return false;
    }

    @Override
    public int getAmbientTemperature() {
        return 0;
    }

    @Override
    public boolean allowExternalHeating() {
        return false;
    }

    @Override
    public int getMaxTemperature() {
        return MAXTEMP;
    }

    @Override
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    public boolean hasATank() {
        return false;
    }
}
