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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;

import reika.dragonapi.instantiable.StepTimer;
import reika.dragonapi.libraries.level.ReikaBiomeHelper;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.api.interfaces.BasicTemperatureMachine;
import reika.rotarycraft.auxiliary.interfaces.TemperatureTE;
import reika.rotarycraft.base.blockentity.PoweredLiquidProducer;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;

/**
 * The Aggregator condenses water out of the air: while cooled below the ambient temperature it
 * produces vanilla water at a rate scaled by the biome's humidity and the input torque (and faster
 * with higher speed). Pipes pull the water out of the side tank through the standard fluid
 * capability. 1.7.10-faithful; the no-atmosphere check (modded airless dimensions) is gated out
 * (DRAGONAPI-PORT: AtmosphereHandler is not ported).
 */
public class BlockEntityAggregator extends PoweredLiquidProducer implements BasicTemperatureMachine, TemperatureTE {

    public static final int CAPACITY = 128000;

    private final StepTimer timer = new StepTimer(20);

    private int temperature;

    public BlockEntityAggregator(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.AGGREGATOR.get(), pos, state);
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        /* 26.1-lifecycle */ super.updateEntity(); // drive BlockEntityBase lifecycle (ticksExisted++, onFirstTick → recompute/sync).
        this.getPowerBelow();

        timer.update();
        if (timer.checkCap())
            this.updateTemperature(world, pos);

        if (power < MINPOWER || omega < MINSPEED)
            return;
        if (tank.isFull())
            return;
        if (temperature >= this.getMaxTemperature())
            return;
        // DRAGONAPI-PORT: legacy also skipped in no-atmosphere (modded airless) dimensions here.

        int Tamb = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);
        if (temperature < Tamb) {
            int amt = this.getProductionPerTick(world, pos);
            if (amt > 0)
                tank.addLiquid(amt, Fluids.WATER);
        }
    }

    public int getProductionPerTick(Level world, BlockPos pos) {
        if (omega < MINSPEED || power < MINPOWER)
            return 0;
        int n = Math.max(1, this.getOperationTime());
        return this.getWaterProduced(world, pos) / n;
    }

    private int getOperationTime() {
        return Math.max(0, (int) (80 - 5 * ReikaMathLibrary.logbase(omega + 1, 2)));
    }

    private int getWaterProduced(Level world, BlockPos pos) {
        float humidity = ReikaBiomeHelper.getBiomeHumidity(world, pos);
        return Math.max(2, (int) (torque * torque * humidity));
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
        if (!this.isInWorld()) {
            phi = 0;
            return;
        }
        phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
    }

    @Override
    protected String getTEName() {
        return "aggregator";
    }

    @Override
    public net.minecraft.world.level.block.Block getBlockEntityBlockID() {
        return reika.rotarycraft.registry.RotaryBlocks.AGGREGATOR.get();
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.AGGREGATOR;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public int getRedstoneOverride() {
        return 15 * tank.getFluidLevel() / tank.getCapacity();
    }

    public int getWater() {
        return tank.getFluidLevel();
    }

    @Override
    public boolean canConnectToPipe(MachineRegistry m) {
        return m.isStandardPipe();
    }

    @Override
    public boolean canOutputTo(Direction to) {
        return to.getStepY() == 0;
    }

    @Override
    public FluidStack drainPipe(Direction from, int maxDrain, FluidAction doDrain) {
        return this.canOutputTo(from) ? tank.drain(maxDrain, doDrain) : FluidStack.EMPTY;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return tank.drain(maxDrain, action);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return 0; //a producer never accepts fluid
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public FluidStack getFluidInTank(int t) {
        return tank.getFluid();
    }

    @Override
    public int getTankCapacity(int t) {
        return tank.getCapacity();
    }

    @Override
    public boolean isFluidValid(int t, FluidStack stack) {
        return false;
    }

    @Override
    public int fillPipe(Direction from, FluidStack resource, FluidAction action) {
        return 0; //a producer never accepts fluid from pipes
    }

    @Override
    public int getCapacity() {
        return CAPACITY;
    }

    @Override
    public boolean hasATank() {
        return true;
    }

    @Override
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    public void updateTemperature(Level world, BlockPos pos) {
        int Tamb = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);
        int dT = Tamb - temperature;
        temperature += dT / 4;
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
        temperature = Math.max(1, temp);
    }

    @Override
    public int getThermalDamage() {
        return 0;
    }

    @Override
    public void overheat(Level world, BlockPos pos) {
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
    public int getMaxTemperature() {
        return 100;
    }

    @Override
    public int getAmbientTemperature() {
        return level != null ? ReikaWorldHelper.getAmbientTemperatureAt(level, worldPosition) : 25;
    }

    @Override
    public boolean allowExternalHeating() {
        return true;
    }

    @Override
    public void resetAmbientTemperatureTimer() {
        timer.reset();
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        temperature = NBT.getIntOr("temp", 0);
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        NBT.putInt("temp", temperature);
    }
}
