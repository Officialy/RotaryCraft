/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.processing;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.tags.FluidTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;

import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.RotaryAux;
import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.RotaryItems;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
import reika.rotarycraft.auxiliary.interfaces.FrictionHeatable;
import reika.rotarycraft.auxiliary.interfaces.MultiOperational;
import reika.rotarycraft.auxiliary.interfaces.PressureTE;
import reika.rotarycraft.auxiliary.interfaces.TemperatureTE;
import reika.rotarycraft.auxiliary.recipemanagers.CompactorRecipe;
import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
import reika.rotarycraft.registry.DurationRegistry;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryRecipeTypes;

/**
 * Compactor: squeezes 4x of an item (slots 0-3, all matching) into a compressed product (slot 4)
 * once BOTH sufficient pressure and temperature are reached. Pressure builds from input torque;
 * temperature from friction (heatable externally with a friction heater). The flagship chain is
 * coal -> anthracite -> prismane -> lonsdaleite -> diamond (each step needs 550 MPa / 800 C);
 * peripherals: blaze powder -> glowstone, ice -> packed ice (cold!).
 *
 * <p>Recipes are data-driven. Environmental heating, cooling, pressure decay and machine
 * failures follow V33a, using the shared DragonAPI world helpers.</p>
 */
public class BlockEntityCompactor extends InventoriedPowerReceiver implements TemperatureTE,
        PressureTE, FrictionHeatable, MultiOperational, ConditionalOperation {

    public static final int MAXTEMP = 1000;
    /** All pressures in kPa; steel yield strength = 250 MPa. */
    public static final int MAXPRESSURE = 600000;
    public static final int REQTEMP = 800;
    public static final int REQPRESS = 550000;
    private static final int AMBIENT_TEMP = 20;
    private static final int AMBIENT_PRESS = 101;

    public int compactorCookTime;
    private int pressure = AMBIENT_PRESS;
    public int temperature = AMBIENT_TEMP;
    public boolean idle = false;
    private boolean animdir = false;
    private int envirotick = 0;
    private int tempTick;

    public BlockEntityCompactor(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.COMPACTOR.get(), pos, state);
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        read = world.getBlockState(pos).getValue(BlockRotaryCraftMachine.FACING).getOpposite();
        this.getPower(false);

        envirotick++;
        if (envirotick >= 20) {
            this.updatePressure(world, pos);
            if (tempTick == 0)
                this.updateTemperature(world, pos);
            envirotick = 0;
        }
        if (tempTick > 0)
            tempTick--;

        this.testIdle();

        tickcount++;

        if (world.isClientSide())
            return;

        boolean flag1 = false;
        int n = this.getNumberConsecutiveOperations();
        for (int i = 0; i < n; i++)
            flag1 |= this.doOperation(n > 1);

        if (flag1)
            this.setChanged();
    }

    private boolean doOperation(boolean multiple) {
        if (this.canSmelt()) {
            compactorCookTime++;
            if (multiple || compactorCookTime >= this.getOperationTime()) {
                compactorCookTime = 0;
                this.smeltItem();
            }
            return true;
        }
        else {
            compactorCookTime = 0;
            return false;
        }
    }

    public void testIdle() {
        boolean ingred = false;
        boolean invalid = false;
        for (int i = 0; i < 4; i++) {
            if (itemHandler.getStackInSlot(i).isEmpty())
                invalid = true;
        }
        if (!invalid) {
            ItemStack first = itemHandler.getStackInSlot(0);
            for (int i = 1; i < 4; i++) {
                if (!ItemStack.isSameItemSameComponents(first, itemHandler.getStackInSlot(i)))
                    invalid = true;
            }
        }
        if (!invalid && this.getRecipe(itemHandler.getStackInSlot(0)) != null)
            ingred = true;
        ItemStack out = itemHandler.getStackInSlot(4);
        boolean full = !out.isEmpty() && out.getCount() >= out.getMaxStackSize();
        idle = !ingred || full;
    }

    private CompactorRecipe getRecipe(ItemStack in) {
        if (in.isEmpty() || level == null || level.getServer() == null)
            return null;
        return level.getServer().getRecipeManager()
                .getRecipeFor(RotaryRecipeTypes.COMPACTOR.get(), new SingleRecipeInput(in), level)
                .map(h -> h.value()).orElse(null);
    }

    private boolean canSmelt() {
        if (power < MINPOWER || torque < MINTORQUE)
            return false;
        ItemStack first = itemHandler.getStackInSlot(0);
        for (int i = 0; i < 4; i++) {
            if (itemHandler.getStackInSlot(i).isEmpty())
                return false;
        }
        for (int i = 1; i < 4; i++) {
            if (!ItemStack.isSameItemSameComponents(first, itemHandler.getStackInSlot(i)))
                return false;
        }
        CompactorRecipe r = this.getRecipe(first);
        if (r == null)
            return false;
        if (pressure < r.getReqPressure() || temperature < r.getReqTemperature())
            return false;
        ItemStack result = r.getResult();
        ItemStack out = itemHandler.getStackInSlot(4);
        if (out.isEmpty())
            return true;
        return ItemStack.isSameItemSameComponents(out, result)
                && out.getCount() + result.getCount() <= Math.min(this.getInventoryStackLimit(), result.getMaxStackSize());
    }

    private void smeltItem() {
        if (!this.canSmelt())
            return;
        CompactorRecipe r = this.getRecipe(itemHandler.getStackInSlot(0));
        ItemStack result = r.getResult();
        ItemStack out = itemHandler.getStackInSlot(4);
        if (out.isEmpty())
            itemHandler.setStackInSlot(4, result);
        else {
            out.grow(result.getCount());
            itemHandler.setStackInSlot(4, out);
        }
        for (int i = 0; i < 4; i++)
            itemHandler.extractItem(i, 1, false);
    }

    /** 1-indexed stage from the current input (drives the staged operation time). */
    public int getStage() {
        ItemStack in = itemHandler.getStackInSlot(0);
        if (in.isEmpty())
            return 1;
        if (in.getItem() == reika.rotarycraft.registry.RotaryItems.ANTHRACITE.get())
            return 2;
        if (in.getItem() == reika.rotarycraft.registry.RotaryItems.PRISMANE.get())
            return 3;
        if (in.getItem() == reika.rotarycraft.registry.RotaryItems.LONSDALEITE.get())
            return 4;
        return 1;
    }

    // --- Pressure (builds from torque; decays toward ambient) ---------------

    @Override
    public void updatePressure(Level world, BlockPos pos) {
        int ambient = (int) ReikaWorldHelper.getAmbientPressureAt(world, pos, true);
        if (pressure > ambient)
            pressure -= Math.max((pressure - ambient) / (world.dimension() == Level.NETHER ? 600 : 200), 1);
        if (pressure < ambient)
            pressure += Math.max((ambient - pressure) / 40, 1);
        if (omega > 0 && torque > 0)
            pressure += (int) (128 * ReikaMathLibrary.logbase(torque, 2));
        if (pressure >= 0.8 * MAXPRESSURE)
            RotaryCraft.LOGGER.warn("WARNING: {} is reaching very high pressure!", this);
        if (pressure > MAXPRESSURE)
            this.overpressure(world, pos);
    }

    @Override
    public void addPressure(int press) {
        pressure += press;
    }

    @Override
    public int getPressure() {
        return pressure;
    }

    @Override
    public int getMaxPressure() {
        return MAXPRESSURE;
    }

    @Override
    public void overpressure(Level world, BlockPos pos) {
        world.explode(null, pos.getX(), pos.getY(), pos.getZ(), 4F,
                ConfigRegistry.BLOCKDAMAGE.getState() ? Level.ExplosionInteraction.BLOCK : Level.ExplosionInteraction.NONE);
        pressure = MAXPRESSURE;
    }

    // --- Temperature (V33a environmental heating and cooling) ----------------

    @Override
    public void updateTemperature(Level world, BlockPos pos) {
        int ambient = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);
        if (temperature > ambient)
            temperature -= Math.max((temperature - ambient) / 200, 1);
        if (temperature < ambient)
            temperature += Math.max((ambient - temperature) / 40, 1);
        if (RotaryAux.isNextToLava(world, pos)) temperature += 4;
        if (RotaryAux.isNextToFire(world, pos)) temperature += 2;
        if (ambient == 300) temperature++;
        for (Direction side : Direction.values()) {
            if (world.getFluidState(pos.relative(side)).is(FluidTags.WATER) && temperature > 600) {
                temperature--;
                if (rand.nextInt(4000) == 0) world.setBlockAndUpdate(pos.relative(side), Blocks.AIR.defaultBlockState());
                break;
            }
        }
        Direction ice = ReikaWorldHelper.checkForAdjBlock(world, pos, Blocks.ICE);
        if (ice != null && temperature > 0) {
            temperature -= 2;
            if (rand.nextInt(200) == 0) world.setBlockAndUpdate(pos.relative(ice), Blocks.WATER.defaultBlockState());
        }
        Direction snow = ReikaWorldHelper.checkForAdjBlock(world, pos, Blocks.SNOW_BLOCK);
        if (snow != null && temperature > -5) {
            temperature -= 2;
            // V33a accidentally used the ice direction here; melt the snow that cooled us.
            if (rand.nextInt(100) == 0) world.setBlockAndUpdate(pos.relative(snow), Blocks.WATER.defaultBlockState());
        }
        ReikaWorldHelper.temperatureEnvironment(world, pos, temperature);
        if (temperature >= 0.9 * MAXTEMP)
            RotaryCraft.LOGGER.warn("WARNING: {} is reaching very high temperature!", this);
        if (temperature > MAXTEMP) this.overheat(world, pos);
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
    public void setTemperature(int T) {
        temperature = T;
    }

    @Override
    public int getThermalDamage() {
        return temperature / 100;
    }

    @Override
    public int getMaxTemperature() {
        return MAXTEMP;
    }

    @Override
    public int getAmbientTemperature() {
        return AMBIENT_TEMP;
    }

    @Override
    public boolean canBeCooledWithFins() {
        return true;
    }

    @Override
    public boolean allowExternalHeating() {
        return true;
    }

    @Override
    public boolean allowHeatExtraction() {
        return false;
    }

    @Override
    public void overheat(Level world, BlockPos pos) {
        temperature = MAXTEMP;
        ReikaWorldHelper.overheat(world, pos.getX(), pos.getY(), pos.getZ(),
                RotaryItems.HSLA_STEEL_SCRAP.get().getDefaultInstance(), 0, 17, true, 1F, false,
                ConfigRegistry.BLOCKDAMAGE.getState(), ConfigRegistry.BLOCKDAMAGE.getState() ? 2F : 0F);
    }

    @Override
    public void resetAmbientTemperatureTimer() {
        tempTick = 5;
    }

    @Override
    public float getMultiplier() {
        return 0.75F;
    }

    @Override
    public boolean canBeFrictionHeated() {
        return true;
    }

    @Override
    public void onOverheat(Level world, BlockPos pos) {
        // As in V33a, updateTemperature handles this machine's own failure threshold.
    }

    // --- GUI scaling ---------------------------------------------------------

    public int getCookProgressScaled(int par1) {
        int time = this.getOperationTime();
        return time > 0 ? Math.min(par1, compactorCookTime * par1 / time) : 0;
    }

    public int getPressureScaled(int par1) {
        return pressure * par1 / MAXPRESSURE;
    }

    public int getTemperatureScaled(int par1) {
        return temperature * par1 / MAXTEMP;
    }

    // --- Boilerplate ----------------------------------------------------------

    @Override
    public int getContainerSize() {
        return 5;
    }

    public boolean isItemValidForSlot(int slot, ItemStack is) {
        return slot != 4 && this.getRecipe(is) != null;
    }

    public boolean canExtractItem(int i, ItemStack is, int j) {
        return i == 4;
    }

    /** Exposes the inventory for the container (base handler is protected). */
    public reika.dragonapi.instantiable.storage.ManagedItemHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
        // Press piston oscillation between 0.5 and 1.5, mostly resting at the ends (legacy).
        if (phi < 0.5F)
            phi = 1F;
        if (!this.isInWorld())
            return;
        if (power < MINPOWER || torque < MINTORQUE)
            return;
        if (phi >= 1.5F || phi <= 0.5F)
            if (rand.nextInt(40) > 0)
                return;
        if (animdir)
            phi += 0.03125F;
        else
            phi -= 0.03125F;
        if (phi >= 1.5F || phi <= 0.5F)
            animdir = !animdir;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.COMPACTOR;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.COMPACTOR.get();
    }

    @Override
    protected String getTEName() {
        return "compactor";
    }

    @Override
    public boolean hasAnInventory() {
        return true;
    }

    @Override
    public boolean hasATank() {
        return false;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public int getRedstoneOverride() {
        return canSmelt() ? 0 : 15;
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        NBT.putShort("CookTime", (short) compactorCookTime);
        NBT.putInt("temperature", temperature);
        NBT.putInt("pressure", pressure);
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        compactorCookTime = NBT.getShortOr("CookTime", (short) 0);
        temperature = NBT.getIntOr("temperature", AMBIENT_TEMP);
        pressure = NBT.getIntOr("pressure", AMBIENT_PRESS);
    }

    @Override
    public int getOperationTime() {
        return DurationRegistry.COMPACTOR.getOperationTime(omega, this.getStage() - 1);
    }

    @Override
    public int getNumberConsecutiveOperations() {
        return DurationRegistry.COMPACTOR.getNumberOperations(omega, this.getStage() - 1);
    }

    @Override
    public boolean areConditionsMet() {
        return canSmelt();
    }

    @Override
    public String getOperationalStatus() {
        ItemStack input = itemHandler.getStackInSlot(0);
        if (input.isEmpty()) return "Missing Items";
        CompactorRecipe recipe = getRecipe(input);
        if (recipe != null && temperature < recipe.getReqTemperature()) return "Insufficient Temperature";
        if (recipe != null && pressure < recipe.getReqPressure()) return "Insufficient Pressure";
        return this.areConditionsMet() ? "Operational" : "Invalid or Missing Items";
    }

}
