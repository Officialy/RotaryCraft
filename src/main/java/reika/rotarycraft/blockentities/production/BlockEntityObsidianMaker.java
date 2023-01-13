/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.production;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import reika.dragonapi.instantiable.HybridTank;
import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.dragonapi.libraries.java.ReikaRandomHelper;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
import reika.rotarycraft.auxiliary.interfaces.MultiOperational;
import reika.rotarycraft.auxiliary.interfaces.PipeConnector;
import reika.rotarycraft.auxiliary.interfaces.TemperatureTE;
import reika.rotarycraft.base.blockentity.BlockEntityPiping.Flow;
import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;

import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.DurationRegistry;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;

public class BlockEntityObsidianMaker extends InventoriedPowerReceiver implements TemperatureTE, PipeConnector, IFluidHandler, MultiOperational, ConditionalOperation {

    public static final int CAPACITY = 320 * 1000;
    public static final int MAXTEMP = 1000;
    private static final int MIN_OBSIDIAN_TEMP_0 = 500;
    private static final int MIN_OBSIDIAN_TEMP_100 = 550;
    private static final int MAX_OBSIDIAN_TEMP_100 = 750;
    private static final int MAX_OBSIDIAN_TEMP_0 = 900;
    private final HybridTank lava = new HybridTank("lavamix", CAPACITY);
    private final HybridTank water = new HybridTank("watermix", CAPACITY);
    public int mixTime;
    public int temperature;
    public float overred;
    public float overgreen;
    public float overblue;
    public boolean idle = false;
    private int temptick = 0;

    public BlockEntityObsidianMaker(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.OBSIDIAN_MAKER.get(), pos, state);
    }

    public static boolean func_52005_b(ItemStack par0ItemStack) {
        return true;
    }

    private ItemStack getProducedItem() {
        if (ReikaMathLibrary.isValueInsideBoundsIncl(MIN_OBSIDIAN_TEMP_100, MAX_OBSIDIAN_TEMP_100, temperature))
            return new ItemStack(Blocks.OBSIDIAN);
        else if (temperature > MAX_OBSIDIAN_TEMP_100) {
            if (temperature > MAX_OBSIDIAN_TEMP_0) {
                return new ItemStack(Blocks.COBBLESTONE);
            } else {
                float f = (temperature - MAX_OBSIDIAN_TEMP_100) / (float) (MAX_OBSIDIAN_TEMP_0 - MAX_OBSIDIAN_TEMP_100);
                return ReikaRandomHelper.doWithChance(1 - f) ? new ItemStack(Blocks.OBSIDIAN) : new ItemStack(Blocks.COBBLESTONE);
            }
        } else if (temperature < MIN_OBSIDIAN_TEMP_100) {
            if (temperature < MIN_OBSIDIAN_TEMP_0) {
                return new ItemStack(Blocks.COBBLESTONE);
            } else {
                float f = (MIN_OBSIDIAN_TEMP_100 - temperature) / (float) (MIN_OBSIDIAN_TEMP_100 - MIN_OBSIDIAN_TEMP_0);
                return ReikaRandomHelper.doWithChance(1 - f) ? new ItemStack(Blocks.OBSIDIAN) : new ItemStack(Blocks.COBBLESTONE);
            }
        } else {
            return new ItemStack(Blocks.BEDROCK); //never happens
        }
    }

    @Override
    public Block getBlockEntityBlockID() {
        return null;
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        tickcount++;
        temptick++;
        this.getPowerBelow();
        if (temptick >= 20 && !world.isClientSide) {
            this.updateTemperature(world, pos);
            temptick = 0;
        }
        if (power < MINPOWER || omega < MINSPEED || water.isEmpty() || lava.isEmpty())
            return;
        this.testIdle();

        if (!world.isClientSide) {
            int n = this.getNumberConsecutiveOperations();
            for (int i = 0; i < n; i++)
                this.doOperation(n > 1);
        }
    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    private void doOperation(boolean multiple) {
        if (multiple || tickcount >= this.getOperationTime()) {
            tickcount = 0;
            this.mix();
        }
    }

    public void testIdle() {
        boolean noliq = water.isEmpty() || lava.isEmpty();
        boolean full = this.getNonFullStack(this.getProducedItem()) == -1;
        idle = full || noliq;
    }

    public void updateTemperature(Level world, BlockPos pos) {
        overblue = 0;
        overgreen = 0;
        overred = 0;

        int Tamb = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);
        int max = MAXTEMP;

        //if (DragonAPI.rand.nextInt(20/20) == 0) {
        if (temperature > Tamb) {
            temperature--;
        }
        if (temperature < Tamb) {
            temperature++;
        }

        if (!water.isEmpty()) {
            max = 600;
            if (lava.isEmpty())
                temperature = Math.max(temperature - 2, Tamb);
        }

        if (!lava.isEmpty())
            temperature = Math.min(temperature + 3, max);
        //}

        if (temperature > 600 + (MAXTEMP - 600) / 2) {
            overred = 0.25F;
        }
        if (temperature > 600 + (MAXTEMP - 600) / 1.375) {
            overred = 0.4F;
            overgreen = 0.1F;
            RotaryCraft.LOGGER.warn("WARNING: " + this + " is reaching very high temperature!");
        }
        if (temperature > 600 + (MAXTEMP - 600) / 1.1) {
            overred = 0.55F;
            overgreen = 0.2F;
            RotaryCraft.LOGGER.warn("WARNING: " + this + " is reaching very high temperature!");
        }
        if (temperature >= MAXTEMP && ConfigRegistry.BLOCKDAMAGE.getState()) {
            this.overheat(world, pos);
        }
    }

    public void overheat(Level world, BlockPos pos) {
//        world.playLocalSound(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, "DragonAPI.rand.fizz", 3F, 1F);
        world.setBlock(pos, Fluids.FLOWING_LAVA.defaultFluidState().createLegacyBlock(), 1);
    }

    public void mix() {
        ItemStack is = this.getProducedItem();
        if (is == null)
            return;
        int slot = this.getNonFullStack(is);
        if (slot == -1)
            return;
        //ReikaJavaLibrary.pConsole(is);
        boolean obsid = ReikaItemHelper.matchStackWithBlock(is, Blocks.OBSIDIAN.defaultBlockState());
        int lavaamt = obsid ? 1000 : 50;
        if (lava.getFluidLevel() < lavaamt || water.getFluidLevel() < 1000)
            return;
        ReikaInventoryHelper.addOrSetStack(is, inv, slot);
        if (!ReikaItemHelper.matchStackWithBlock(is, Blocks.COBBLESTONE.defaultBlockState()))
            lava.removeLiquid(lavaamt);
        water.removeLiquid(obsid ? 2500 : 1000);
//        level.playLocalSound(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, "DragonAPI.rand.fizz", 0.5F + 0.5F * DragonAPI.rand.nextFloat(), 0.7F + 0.3F * DragonAPI.rand.nextFloat());
        for (double x = worldPosition.getX() + 0.25; x <= worldPosition.getX() + 0.75; x += 0.25) {
            for (double z = worldPosition.getZ() + 0.25; z <= worldPosition.getZ() + 0.75; z += 0.25) {
                level.addParticle(ParticleTypes.SMOKE, x, worldPosition.getY() + 0.75, z, 0, 0, 0);
            }
        }
    }

    public int getNonFullStack(ItemStack is) {
        for (int k = 0; k < inv.length; k++) {
            if (inv[k] == null)
                return k;
            else if (ReikaItemHelper.matchStacks(inv[k], is) && inv[k].getCount() < this.getInventoryStackLimit())
                return k;
        }
        return -1;
    }

    public int getWaterScaled(int par1) {
        return (water.getFluidLevel() * par1) / CAPACITY;
    }

    public int getLavaScaled(int par1) {
        return (lava.getFluidLevel() * par1) / CAPACITY;
    }

    /**
     * Returns the number of slots in the inv.
     */
    public int getContainerSize() {
        return 9;
    }

    public int getCookProgressScaled(int par1) {
        return (mixTime * par1) / (600 - (int) (40 * ReikaMathLibrary.logbase(omega, 2)));
    }

    @Override
    protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag);

        lava.writeToNBT(tag);
        water.writeToNBT(tag);

        tag.putInt("mix", mixTime);

        tag.putInt("temp", temperature);

        tag.putFloat("red", overred);
        tag.putFloat("green", overgreen);
        tag.putFloat("blue", overblue);
    }

    @Override
    protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag);

        water.readFromNBT(tag);
        lava.readFromNBT(tag);

        mixTime = tag.getInt("mix");

        temperature = tag.getInt("temp");

        overred = tag.getFloat("red");
        overgreen = tag.getFloat("green");
        overblue = tag.getFloat("blue");
    }

    @Override
    protected String getTEName() {
        return "obsidianmaker";
    }

    @Override
    public boolean hasModelTransparency() {
        return true;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.OBSIDIAN;
    }

    @Override
    public int getThermalDamage() {
        return 0;
    }

    @Override
    public int getRedstoneOverride() {
        return idle ? 15 : 0;
    }

    @Override
    public boolean canConnectToPipe(MachineRegistry m) {
        return m.isStandardPipe();
    }

    @Override
    public boolean canConnectToPipeOnSide(MachineRegistry p, Direction side) {
        return side.getStepY() == 0;
    }

    @Override
    public int fill(Direction from, FluidStack resource, FluidAction action) {
        return 0;
    }

    @Override
    public FluidStack drain(Direction from, int maxDrain, FluidAction doDrain) {
        return null;
    }

    @Override
    public void addTemperature(int temp) {
        temperature += temp;
    }

    @Override
    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temp) {
        temperature = temp;
    }

    //    @Override
    public boolean canFill(Fluid fluid) {
//        if (from.getStepY() != 0)
//            return false;
        return fluid.equals(Fluids.WATER) || fluid.equals(Fluids.LAVA);
    }

    public int getWater() {
        return water.getFluidLevel();
    }

    public void setWater(int amt) {
        water.setContents(amt, Fluids.WATER);
    }

    public int getLava() {
        return lava.getFluidLevel();
    }

    public void setLava(int amt) {
        lava.setContents(amt, Fluids.LAVA);
    }

    public void addWater(int amt) {
        water.addLiquid(amt, Fluids.WATER);
    }

    public void addLava(int amt) {
        lava.addLiquid(amt, Fluids.LAVA);
    }

    @Override
    public Flow getFlowForSide(Direction side) {
        return side.getStepY() == 0 ? Flow.INPUT : Flow.NONE;
    }

    @Override
    public int getOperationTime() {
        return DurationRegistry.OBSIDIAN.getOperationTime(omega);
    }

    @Override
    public int getNumberConsecutiveOperations() {
        return DurationRegistry.OBSIDIAN.getNumberOperations(omega);
    }

    @Override
    public boolean areConditionsMet() {
        return !lava.isEmpty() && !water.isEmpty();
    }

    @Override
    public String getOperationalStatus() {
        return lava.isEmpty() ? "No Lava" : water.isEmpty() ? "No Water" : "Operational";
    }

    @Override
    public boolean canBeCooledWithFins() {
        return true;
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
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getItem(int pIndex) {
        return null;
    }

    @Override
    public ItemStack removeItem(int pIndex, int pCount) {
        return null;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pIndex) {
        return null;
    }

    @Override
    public void setItem(int pIndex, ItemStack pStack) {

    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return false;
    }

    @Override
    public void clearContent() {

    }

    @Override
    public int getTanks() {
        return 0;
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return null;
    }

    @Override
    public int getTankCapacity(int tank) {
        return 0;
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return false;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        Fluid f = resource.getFluid();
        if (!this.canFill(f))
            return 0;
        if (f.equals(Fluids.WATER))
            return water.fill(resource, action);
        if (f.equals(Fluids.LAVA))
            return lava.fill(resource, action);
        return 0;
    }

    @NotNull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return null;
    }

    @NotNull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return null;
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
