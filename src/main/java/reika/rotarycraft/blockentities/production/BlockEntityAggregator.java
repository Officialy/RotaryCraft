///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.production;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.biome.Biome;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.material.Fluids;
//import net.minecraftforge.fluids.capability.IFluidHandler;
//import reika.dragonapi.instantiable.StepTimer;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.rotarycraft.api.Interfaces.BasicTemperatureMachine;
//import reika.rotarycraft.auxiliary.interfaces.TemperatureTE;
//import reika.rotarycraft.blockentities.piping.BlockEntityPipe;
//import reika.rotarycraft.registry.MachineRegistry;
//
//public class BlockEntityAggregator extends PoweredLiquidProducer implements BasicTemperatureMachine, TemperatureTE {
//
//    public static final int CAPACITY = 128000;
//
//    private final StepTimer timer = new StepTimer(20);
//
//    private int temperature;
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        this.getPowerBelow();
//
//        timer.update();
//        if (timer.checkCap())
//            this.updateTemperature(world, pos);
//
//        if (!tank.isEmpty())
//            this.dumpLiquid(world, pos);
//
//        if (power < MINPOWER || omega < MINSPEED)
//            return;
//
//        if (tank.isFull())
//            return;
//
//        if (temperature >= this.getMaxTemperature())
//            return;
//
//        if (AtmosphereHandler.isNoAtmo(world, x, y + 1, z, blockType, false))
//            return;
//
//        int Tamb = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);
//        if (temperature < Tamb) {
//            Biome biome = world.getBiomeGenForCoords(x, z);
//            //float h = biome.rainfall; //Not used by any biome
//            int amt = this.getProductionPerTick(biome);
//            tank.addLiquid(amt, Fluids.WATER);
//        }
//    }
//
//    private void dumpLiquid(Level world, BlockPos pos) {
//        for (int i = 2; i < 6; i++) {
//            Direction dir = Direction.values()[i];
//            BlockEntity te = getAdjacentBlockEntity(dir);
//            if (te instanceof BlockEntityPipe) {
//                BlockEntityPipe p = (BlockEntityPipe) te;
//                int dL = (tank.getLevel() - p.getFluidLevel()) / 4;
//                if (dL > 0 && (p.getAttributes() == null || tank.getActualFluid().equals(p.getFluidType()))) {
//                    p.setFluid(tank.getActualFluid());
//                    p.addFluid(dL);
//                    tank.removeLiquid(dL);
//                }
//            } else if (te instanceof IFluidHandler) {
//                IFluidHandler ifl = (IFluidHandler) te;
//                int added = ifl.fill(dir.getOpposite(), tank.getFluid(), true);
//                if (added > 0) {
//                    tank.removeLiquid(added);
//                    if (tank.isEmpty())
//                        return;
//                }
//            }
//        }
//    }
//
//    public int getProductionPerTick(Biome biome) {
//        if (omega < MINSPEED || power < MINPOWER)
//            return 0;
//        int n = Math.max(1, this.getOriginalOperationTime());
//        return this.getOriginalWaterProduced(biome) / n;
//    }
//
//    private int getOriginalOperationTime() {
//        return Math.max(0, (int) (80 - 5 * ReikaMathLibrary.logbase(omega + 1/*-MINSPEED*/, 2)));
//    }
//
//    private int getOriginalWaterProduced(Biome biome) {
//        return Math.max(2, (int) (torque * torque * ReikaBiomeHelper.getBiomeHumidity(biome)));
//    }
//
////    @Override
////    protected void animateWithTick(Level world, BlockPos pos) {
////        if (!this.isInWorld()) {
////            phi = 0;
////            return;
////        }
////        phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
////    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.AGGREGATOR;
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        return 15 * tank.getLevel() / tank.getCapacity();
//    }
//
//    public int getWater() {
//        return tank.getLevel();
//    }
//
//    @Override
//    public boolean canConnectToPipe(MachineRegistry m) {
//        return m.isStandardPipe();
//    }
//
//    @Override
//    public boolean canOutputTo(Direction to) {
//        return to.getStepY() == 0;
//    }
//
//    @Override
//    public int getCapacity() {
//        return CAPACITY;
//    }
//
//    @Override
//    public void updateTemperature(Level world, BlockPos pos) {
//        int Tamb = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);
//        int dT = Tamb - temperature;
//        temperature += dT / 4;
//    }
//
//    @Override
//    public void addTemperature(int temp) {
//        temperature += temp;
//    }
//
//    @Override
//    public int getTemperature() {
//        return temperature;
//    }
//
//    public void setTemperature(int temp) {
//        temperature = Math.max(1, temp);
//    }
//
//    @Override
//    public int getThermalDamage() {
//        return 0;
//    }
//
//    @Override
//    public void overheat(Level world, BlockPos pos) {
//
//    }
//
//    @Override
//    public boolean canBeCooledWithFins() {
//        return true;
//    }
//
//    @Override
//    public boolean allowHeatExtraction() {
//        return false;
//    }
//
//    @Override
//    public int getMaxTemperature() {
//        return 100;
//    }
//
//    @Override
//    public boolean allowExternalHeating() {
//        return true;
//    }
//
//    @Override
//    public void resetAmbientTemperatureTimer() {
//        timer.reset();
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//
//        temperature = NBT.getInt("temp");
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//
//        NBT.putInt("temp", temperature);
//    }
//}
