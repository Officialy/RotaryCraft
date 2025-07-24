///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.auxiliary;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.block.entity.BlockEntityType;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.material.Fluid;
//import net.neoforged.fluids.FluidStack;
//import net.neoforged.fluids.capability.IFluidHandler;
//
//import reika.dragonapi.instantiable.HybridTank;
//import reika.dragonapi.libraries.java.ReikaStringParser;
//import reika.rotarycraft.auxiliary.interfaces.PipeConnector;
//import reika.rotarycraft.base.blockentity.BlockEntityEngine;
//import reika.rotarycraft.base.blockentity.BlockEntityPiping.Flow;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.registry.EngineType;
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.RotaryFluids;
//
//public class BlockEntityEngineController extends RotaryCraftBlockEntity implements PipeConnector, IFluidHandler {
//
//    public static final int FUELCAP = 3000;
//
//    private final HybridTank tank = new HybridTank("ecu", FUELCAP);
//
//    public boolean redstoneMode;
//    private int redstoneTick = 0;
//    private int prevRedstone;
//
//    private EngineSettings setting = EngineSettings.FULL;
//
//    public BlockEntityEngineController(BlockEntityType<?> type, BlockPos pos, BlockState state) {
//        super(type, pos, state);
//    }
//
//    public static EngineSettings[] getSettingList() {
//        EngineSettings[] arr = new EngineSettings[EngineSettings.list.length];
//        System.arraycopy(EngineSettings.list, 0, arr, 0, arr.length);
//        return arr;
//    }
//
//    public static String getSettingsAsString() {
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < EngineSettings.list.length; i++) {
//            EngineSettings set = EngineSettings.list[i];
//            sb.append(String.format("%s: %.2f%% Speed, %dx Fuel Efficiency", ReikaStringParser.capFirstChar(set.name()), set.getSpeedDecimal(), set.getEfficiencyFactor()));
//            if (i < EngineSettings.list.length - 1)
//                sb.append("\n");
//        }
//        return sb.toString();
//    }
//
//    public boolean consumeFuel() {
//        return setting.fuelFactor != 0;
//    }
//
//    public boolean canProducePower() {
//        return setting.speedFactor != 0;
//    }
//
//    public boolean playSound() {
//        return this.canProducePower();
//    }
//
//    public float getSpeedMultiplier() {
//        if (this.canProducePower())
//            return 1F / setting.speedFactor;
//        return 0;
//    }
//
//    public int getSpeedFactor() {
//        if (this.canProducePower())
//            return setting.speedFactor;
//        return 0;
//    }
//
//    public int getFuelMultiplier(EngineType.EngineClass e) {
//        int base = setting.fuelFactor;
//        if (e == EngineType.EngineClass.TURBINE)
//            base /= 8;
//        return Math.max(1, base);
//    }
//
//    public float getSoundStretch() {
//        switch (setting) {
//            case FULL:
//                return 1F;
//            case LOW:
//                return 0.6F;
//            case MEDIUM:
//                return 0.8F;
//            case SHUTDOWN:
//                return 0F;
//            case STANDBY:
//                return 0.4F;
//            default:
//                return 1F;
//        }
//    }
//
//    public void increment() {
//        int l = EngineSettings.list.length;
//        int o = setting.ordinal();
//        o++;
//        if (o >= l)
//            o = 0;
//        setting = EngineSettings.list[o];
//    }
//
//    public void setSetting(int ordinal) {
//        int o = Math.max(0, Math.min(ordinal, EngineSettings.list.length - 1));
//        setting = EngineSettings.list[o];
//    }
//
//    //@Override
//    public void updateEntity(Level world, BlockPos pos) {
//
//        if (redstoneTick > 0)
//            redstoneTick--;
//        int power = redstoneTick == 0 ? world.getBlockPowerInput(pos) : prevRedstone;
//        if (prevRedstone != power)
//            redstoneTick = 60;
//        prevRedstone = power;
//        //ReikaJavaLibrary.pConsole(prevRedstone+":"+this.canProducePower(), Dist.DEDICATED_SERVER);
//
//        if (redstoneMode) {
//            setting = power == 15 ? EngineSettings.SHUTDOWN : EngineSettings.list[4 - power / 3];
//        }
//        //ReikaJavaLibrary.pConsole(tank);
//        if (tank.isEmpty())
//            return;
//
//        if (MachineRegistry.getMachine(world, pos.above()) == MachineRegistry.ENGINE)
//            if (this.transferToEngine((BlockEntityEngine) world.getBlockEntity(pos.above()), false))
//                return;
////        if (MachineRegistry.getMachine(world, x, y + 1, z) == MachineRegistry.FUELENGINE)
////            if (this.transferToFuelEngine((BlockEntityFuelEngine) world.getBlockEntity(x, y + 1, z), false))
////                return;
//
//        if (MachineRegistry.getMachine(world, pos.below()) == MachineRegistry.ENGINE)
//            if (this.transferToEngine((BlockEntityEngine) world.getBlockEntity(pos.below()), true))
//                return;
////        if (MachineRegistry.getMachine(world, x, y - 1, z) == MachineRegistry.FUELENGINE)
////            if (this.transferToFuelEngine((BlockEntityFuelEngine) world.getBlockEntity(x, y - 1, z), true))
////                return;
//    }
//
////    private boolean transferToFuelEngine(BlockEntityFuelEngine te, boolean flip) {
////        if (te.isFlipped != flip)
////            return false;
////        FluidStack liq = tank.getFluid();
////        int toadd = Math.min(liq.amount / 4 + 1, BlockEntityFuelEngine.CAPACITY - te.getFuelLevel());
////        if (toadd > 0) {
////            te.addFuel(toadd);
////            tank.removeLiquid(toadd);
////            return true;
////        }
////        return false;
////    }
//
//    private boolean transferToEngine(BlockEntityEngine te, boolean flip) {
//        if (te.isFlipped != flip)
//            return false;
//        FluidStack liq = tank.getFluid();
//        if (BlockEntityEngine.isAirFluid(liq.getFluid())) {
//            FluidStack move = liq.copy();
//            move.amount = move.amount / 4 + 1;
//            int added = te.fill(flip ? Direction.UP : Direction.DOWN, move, IFluidHandler.FluidAction.EXECUTE);
//            tank.removeLiquid(added);
//            return added > 0;
//        } else {
//            Fluid f = te.getEngineType().getFuelType();
//            if (f == null || liq == null || !f.equals(liq.getFluid()))
//                return false;
//            if (te.getFuelLevel() + liq.amount > BlockEntityEngine.FUELCAP)
//                return false;
//            te.addFuel(liq.amount / 4 + 1);
//            tank.removeLiquid(liq.amount / 4 + 1);
//        }
//        return true;
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//
//        tank.saveAdditional(NBT);
//
//        NBT.putInt("lvl", setting.ordinal());
//
//        NBT.putBoolean("redstone", redstoneMode);
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//
//        tank.load(NBT);
//
//        setting = EngineSettings.list[NBT.getInt("lvl")];
//
//        redstoneMode = NBT.getBoolean("redstone");
//    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.ECU;
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        return 0;
//    }
//
//    @Override
//    public boolean canConnectToPipe(MachineRegistry m) {
//        return m == MachineRegistry.FUELLINE || m == MachineRegistry.BEDPIPE;
//    }
//
//    @Override
//    public boolean canConnectToPipeOnSide(MachineRegistry p, Direction side) {
//        return true;
//    }
//
//    @Override
//    public int fill(Direction from, FluidStack resource, FluidAction action) {
//        return 0;
//    }
//
//    @Override
//    public int fill(Direction from, FluidStack resource, boolean doFill) {
//        if (this.canFill(from, resource.getFluid()))
//            return tank.fill(resource, doFill);
//        return 0;
//    }
//
//    
//    @Override
//    public FluidStack drain(FluidStack resource, FluidAction action) {
//        return this.canDrain(resource.getFluid()) ? tank.drain(resource.getAmount(), action) : null;
//    }
//
//    @Override
//    public FluidStack drain(Direction from, int maxDrain, boolean doDrain) {
//        return tank.drain(maxDrain, doDrain);
//    }
//
//    @Override
//    public boolean canFill(Direction from, Fluid fluid) {
//        //if (fluid.equals(Fluids.LAVA)) Why was THIS here???
//        //	return true;
//        BlockEntity te = getAdjacentBlockEntity(from);
//        if (te instanceof BlockEntityEngine) {
//            BlockEntityEngine eng = (BlockEntityEngine) te;
//            return eng.getEngineType() != EngineType.STEAM && eng.getEngineType().burnsFuel() && fluid.equals(eng.getEngineType().getFuelType());
//        } //else if (te instanceof BlockEntityFuelEngine) {
//        //return fluid.equals(Fluids.getFluid("fuel"));
//        //}
//        if (fluid.equals(RotaryFluids.JET_FUEL))
//            return true;
//        if (fluid.equals(RotaryFluids.ETHANOL))
//            return true;
////        if (fluid.equals(Fluids.getFluid("fuel")))
////            return true;
//        if (fluid.equals(RotaryFluids.OXYGEN))
//            return true;
//        return false;//fluid.equals(Fluids.getFluid("oxygen"));
//    }
//
//    //    @Override
//    public boolean canDrain(Direction from, Fluid fluid) {
//        return ReikaFluidHelper.isFluidDrainableFromTank(fluid, tank);
//    }
//
////    @Override
////    public FluidTankInfo[] getTankInfo(Direction from) {
////        return new FluidTankInfo[]{tank.getInfo()};
////    }
//
//    @Override
//    public Flow getFlowForSide(Direction side) {
//        return Flow.DUAL;
//    }
//
//    @Override
//    public int getTanks() {
//        return 0;
//    }
//
//    
//    @Override
//    public FluidStack getFluidInTank(int tank) {
//        return null;
//    }
//
//    @Override
//    public int getTankCapacity(int tank) {
//        return 0;
//    }
//
//    @Override
//    public boolean isFluidValid(int tank,  FluidStack stack) {
//        return false;
//    }
//
//    @Override
//    public int fill(FluidStack resource, FluidAction action) {
//        return 0;
//    }
//
//    
//    @Override
//    public FluidStack drain(int maxDrain, FluidAction action) {
//        return null;
//    }
//
//    @Override
//    public String getName() {
//        return null;
//    }
//
//    @Override
//    public boolean hasAnInventory() {
//        return false;
//    }
//
//    @Override
//    public boolean hasATank() {
//        return false;
//    }
//
//    private enum EngineSettings {
//        SHUTDOWN(0, 0),
//        STANDBY(16, 64),
//        LOW(4, 8),
//        MEDIUM(2, 2),
//        FULL(1, 1);
//
//        public static final EngineSettings[] list = values();
//        public final int speedFactor;
//        public final int fuelFactor;
//
//        EngineSettings(int speed, int fuel) {
//            speedFactor = speed;
//            fuelFactor = fuel;
//        }
//
//        public double getSpeedDecimal() {
//            if (this == SHUTDOWN)
//                return 0;
//            return 100D / speedFactor;
//        }
//
//        public int getEfficiencyFactor() {
//            if (this == SHUTDOWN)
//                return 0;
//            return fuelFactor / speedFactor;
//        }
//    }
//}
