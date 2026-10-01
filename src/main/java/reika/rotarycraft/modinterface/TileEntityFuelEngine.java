/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.modinterface;

import java.util.Collection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import reika.dragonapi.instantiable.HybridTank;
import reika.dragonapi.instantiable.StepTimer;
import reika.dragonapi.instantiable.storage.FilteredFluidResourceHandler;
import reika.dragonapi.instantiable.storage.HybridTankResourceHandler;
import reika.dragonapi.interfaces.blockentity.HasFluidResourceHandler;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.dragonapi.modinteract.AtmosphereHandler;
import reika.rotarycraft.api.power.PowerGenerator;
import reika.rotarycraft.api.power.ShaftMerger;
import reika.rotarycraft.auxiliary.PowerSourceList;
import reika.rotarycraft.auxiliary.RotaryAux;
import reika.rotarycraft.auxiliary.interfaces.*;
import reika.rotarycraft.base.blockentity.BlockEntityIOMachine;
import reika.rotarycraft.base.blockentity.BlockEntityPiping.Flow;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.auxiliary.BlockEntityEngineController;
import reika.rotarycraft.data.RoCFluidTagsProvider;
import reika.rotarycraft.gui.container.machine.inventory.ContainerFuelEngine;
import reika.rotarycraft.registry.*;

/** V33a's three-tank petroleum engine, including ECU throttling, inertia and overheating. */
public final class TileEntityFuelEngine extends BlockEntityIOMachine implements HasFluidResourceHandler,
        PipeConnector, SimpleProvider, PowerGenerator, TemperatureTE {
    public static final int GEN_OMEGA = 256, GEN_TORQUE = 2048, CAPACITY = 24000, MAXTEMP = 750;
    private int temperature;
    private final HybridTank tank = new HybridTank("fuelengine", CAPACITY);
    private final HybridTank water = new HybridTank("waterfuelengine", CAPACITY);
    private final HybridTank lube = new HybridTank("lubefuelengine", CAPACITY);
    private final StepTimer fuelTimer = new StepTimer(36), soundTimer = new StepTimer(40), tempTimer = new StepTimer(20);
    private final ResourceHandler<FluidResource> fluids = new HybridTankResourceHandler(new HybridTank[]{tank, water, lube},
            (index, fluid) -> switch (index) {
                case 0 -> isValidFuel(fluid.getFluid());
                case 1 -> fluid.getFluid() == Fluids.WATER;
                default -> fluid.getFluid() == RotaryFluids.LUBRICANT.get();
            }, (index, fluid) -> false, this::setChanged);
    public TileEntityFuelEngine(BlockPos pos, BlockState state) { super(RotaryBlockEntities.FUEL_ENGINE.get(), pos, state); }
    @Override public ResourceHandler<FluidResource> getFluidHandler(Direction side) {
        if (side == null) return fluids;
        return new FilteredFluidResourceHandler(fluids, index -> true,
                (index, fluid) -> index == 0 ? side == fuelSide() : side.getAxis().isHorizontal(), (index, fluid) -> false);
    }
    private Direction fuelSide() { return isFlipped ? Direction.UP : Direction.DOWN; }
    public static boolean isValidFuel(Fluid fluid) {
        return fluid != null && (BuiltInRegistries.FLUID.wrapAsHolder(fluid).is(RoCFluidTagsProvider.FUEL)
                || BuiltInRegistries.FLUID.wrapAsHolder(fluid).is(RoCFluidTagsProvider.TURBOFUEL));
    }
    public boolean isUsingTurbofuel() { return !tank.isEmpty() && BuiltInRegistries.FLUID.wrapAsHolder(tank.getActualFluid().getFluid()).is(RoCFluidTagsProvider.TURBOFUEL); }
    public BlockEntityEngineController getController() {
        return level != null && getAdjacentBlockEntity(fuelSide()) instanceof BlockEntityEngineController ecu ? ecu : null;
    }
    private boolean canEmitPower(Level world, BlockPos pos) {
        var ecu = getController();
        return !tank.isEmpty() && !lube.isEmpty() && !AtmosphereHandler.isNoAtmo(world, pos.above(), getBlockState().getBlock(), true)
                && (ecu == null || ecu.canProducePower());
    }
    public int getFuelInterval() {
        var ecu = getController();
        int interval = 36 * (ecu == null ? 1 : ecu.getFuelMultiplier(EngineType.EngineClass.PISTON));
        // Preserve V33a's integer expression: 5/2 is evaluated before multiplying.
        return isUsingTurbofuel() ? interval * (5 / 2) : interval;
    }
    public int getGenOmega() { return temperature <= 450 ? GEN_OMEGA : Math.max(16, GEN_OMEGA + 450 - temperature); }
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        write = getBlockState().getValue(BlockRotaryCraftMachine.FACING);
        if (world.isClientSide()) { if (power > 0) smoke(world, pos); return; }
        fuelTimer.setCap(getFuelInterval());
        int target = getGenOmega();
        tempTimer.update();
        if (tempTimer.checkCap()) { updateTemperature(world, pos); if (isRemoved()) return; }
        if (canEmitPower(world, pos)) {
            fuelTimer.update();
            if (fuelTimer.checkCap()) tank.removeLiquid(4);
            torque = GEN_TORQUE;
            var ecu = getController();
            if (ecu != null) target *= ecu.getSpeedMultiplier();
        } else { target = 0; if (omega == 0) torque = 0; }
        if (target >= omega && omega < target) {
            omega = Math.min(target, omega + 4 * (int)ReikaMathLibrary.logbase(target, 2));
            tank.removeLiquid(1);
        } else if (target < omega && omega > 0) omega -= omega / 256 + 1;
        power = (long)omega * torque;
        soundTimer.update();
        if (power > 0) {
            if (soundTimer.checkCap()) SoundRegistry.DIESEL.playSoundAtBlock(world, pos, RotaryAux.isMuffled(this) ? .3F : 1F, .4F);
            if (world.getGameTime() % 32 == 0) lube.removeLiquid(1);
        }
        setChanged();
    }
    private void smoke(Level world, BlockPos pos) {
        Direction facing = getBlockState().getValue(BlockRotaryCraftMachine.FACING);
        double y = pos.getY() + .9375 - (isFlipped ? .5 : 0);
        for (double across : new double[]{.0625, .9375}) {
            double x = facing.getAxis() == Direction.Axis.X ? (facing == Direction.WEST ? .6875 : .3175) : across;
            double z = facing.getAxis() == Direction.Axis.Z ? (facing == Direction.NORTH ? .6875 : .3175) : across;
            world.addParticle(ParticleTypes.SMOKE, pos.getX() + x, y, pos.getZ() + z, 0, 0, 0);
        }
    }
    @Override protected void animateWithTick(Level world, BlockPos pos) {
        if (!isInWorld()) { phi = 0; return; }
        phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
    }
    @Override public void updateTemperature(Level world, BlockPos pos) {
        if (world.isClientSide()) return;
        int ambient = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);
        if (temperature > ambient) {
            temperature--;
            if (!water.isEmpty()) { temperature -= (temperature - ambient) / 100; water.removeLiquid(20); }
        }
        if (power > 0) temperature += 5;
        if (temperature > MAXTEMP) overheat(world, pos);
        setChanged();
    }
    @Override public void overheat(Level world, BlockPos pos) {
        if (world.isClientSide()) return;
        world.removeBlock(pos, false);
        world.explode(null, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, 4, true, Level.ExplosionInteraction.BLOCK);
        world.explode(null, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, 8, true, Level.ExplosionInteraction.BLOCK);
    }
    public int getFuelLevel() { return tank.getFluidLevel(); }
    public int getWaterLevel() { return water.getFluidLevel(); }
    public int getLubeLevel() { return lube.getFluidLevel(); }
    public void addFuel(int amount, Fluid fluid) { if (isValidFuel(fluid)) { tank.addLiquid(amount, fluid); setChanged(); } }
    public void removeFuel(int amount) { tank.removeLiquid(amount); setChanged(); }
    public void addWater(int amount) { water.addLiquid(amount, Fluids.WATER); setChanged(); }
    public void addLube(int amount) { lube.addLiquid(amount, RotaryFluids.LUBRICANT.get()); setChanged(); }
    public int getFuelDuration() { return getFuelLevel() * getFuelInterval() / 4 / 20; }
    public int getFuelScaled(int height) { return getFuelLevel() * height / CAPACITY; }
    public int getWaterScaled(int height) { return getWaterLevel() * height / CAPACITY; }
    public int getLubricantScaled(int height) { return getLubeLevel() * height / CAPACITY; }
    public int getTemperatureScaled(int height) { return temperature * height / MAXTEMP; }
    @Override public int getTemperature() { return temperature; }
    @Override public void addTemperature(int amount) { setTemperature(temperature + amount); }
    @Override public void setTemperature(int value) { temperature = value; setChanged(); }
    @Override public int getMaxTemperature() { return MAXTEMP; }
    @Override public int getThermalDamage() { return 0; }
    @Override public boolean canBeCooledWithFins() { return false; }
    @Override public boolean allowExternalHeating() { return false; }
    @Override public boolean allowHeatExtraction() { return false; }
    @Override public int getAmbientTemperature() { return level == null ? 0 : ReikaWorldHelper.getAmbientTemperatureAt(level, worldPosition); }
    @Override public long getMaxPower() { return power; }
    @Override public long getCurrentPower() { return power; }
    @Override public BlockPos getEmittingPos(BlockPos pos) { return pos.relative(getBlockState().getValue(BlockRotaryCraftMachine.FACING)); }
    @Override public boolean canProvidePower() { return !tank.isEmpty(); }
    @Override public PowerSourceList getPowerSources(PowerSourceTracker tracker, ShaftMerger caller) { return new PowerSourceList().addSource(this); }
    @Override public void getAllOutputs(Collection<BlockEntity> outputs, Direction side) { outputs.add(getAdjacentBlockEntity(getBlockState().getValue(BlockRotaryCraftMachine.FACING))); }
    @Override public boolean canConnectToPipe(MachineRegistry pipe) { return pipe.isStandardPipe() || pipe == MachineRegistry.HOSE || pipe == MachineRegistry.FUELLINE; }
    @Override public boolean canConnectToPipeOnSide(MachineRegistry pipe, Direction side) { return canConnectToPipe(pipe) && getFlowForSide(side) == Flow.INPUT; }
    @Override public Flow getFlowForSide(Direction side) { return side == fuelSide().getOpposite() ? Flow.NONE : Flow.INPUT; }
    // BUILDCRAFT-PORT: modern BuildCraft pipes use the sided fluid capability above; the legacy
    // IPipeConnection FLUID override is restored by its adapter when that API is available.
    @Override public MachineRegistry getMachine() { return MachineRegistry.FUELENGINE; }
    @Override public Block getBlockEntityBlockID() { return RotaryBlocks.FUEL_ENGINE.get(); }
    @Override protected String getTEName() { return "Fuel Engine"; }
    @Override public boolean hasAnInventory() { return false; }
    @Override public boolean hasATank() { return true; }
    @Override public boolean hasModelTransparency() { return false; }
    @Override public int getRedstoneOverride() { return 15 * getFuelLevel() / CAPACITY; }
    @Override public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) { return new ContainerFuelEngine(id, inv, this); }
    @Override protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag); tank.writeToNBT(tag); water.writeToNBT(tag); lube.writeToNBT(tag);
        tag.putInt("temp", temperature); tag.putInt("fuelTick", fuelTimer.getTick());
        tag.putInt("soundTick", soundTimer.getTick()); tag.putInt("tempTick", tempTimer.getTick());
    }
    @Override protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag); tank.readFromNBT(tag); water.readFromNBT(tag); lube.readFromNBT(tag);
        temperature = tag.getIntOr("temp", 0); fuelTimer.setTick(Math.max(0, tag.getIntOr("fuelTick", 0)));
        soundTimer.setTick(Math.max(0, tag.getIntOr("soundTick", 0))); tempTimer.setTick(Math.max(0, tag.getIntOr("tempTick", 0)));
    }
}
