///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.transmission;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.instantiable.StepTimer;
//import reika.dragonapi.interfaces.blockentity.BreakAction;
//import reika.rotarycraft.auxiliary.interfaces.TransmissionReceiver;
//import reika.rotarycraft.base.blockentity.PoweredLiquidReceiver;
//import reika.rotarycraft.registry.DifficultyEffects;
//import reika.rotarycraft.registry.MachineRegistry;
//
//import java.util.Collection;
//
//public class BlockEntityBusController extends PoweredLiquidReceiver implements TransmissionReceiver, BreakAction {
//
//    private ShaftPowerBus bus = new ShaftPowerBus(this);
//
//    private final StepTimer timer = new StepTimer(100);
//
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.BUSCONTROLLER;
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        return 0;
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        this.getIOSides(world, pos, meta);
//        this.getPower(false);
//
//        timer.update();
//
//        if (DragonAPI.debugtest)
//            tank.addLiquid(5, Fluids.getFluid("rc lubricant"));
//
//        if (tank.isEmpty()) {
//            torque = 0;
//            omega = 0;
//        } else {
//            if (power > 0 && timer.checkCap())
//                tank.removeLiquid(this.getLubricantUsed());
//        }
//
//        power = (long) torque * (long) omega;
//        if (tickcount % 10 == 0)
//            bus.update();
//        //ReikaJavaLibrary.pConsole(bus.getInputPower()+":"+bus.getTotalOutputSides(), Dist.DEDICATED_SERVER);
//    }
//
//    private int getLubricantUsed() {
//        return Math.max(1, (int) (DifficultyEffects.LUBEUSAGE.getChance() * 2 * bus.getSize() + bus.getTotalOutputSides()));
//    }
//
//    public void getIOSides(Level world, BlockPos pos, int metadata) {
//        switch (metadata) {
//            case 0:
//                read = Direction.EAST;
//                break;
//            case 1:
//                read = Direction.WEST;
//                break;
//            case 2:
//                read = Direction.NORTH;
//                break;
//            case 3:
//                read = Direction.SOUTH;
//                break;
//        }
//    }
//
//    public ShaftPowerBus getBus() {
//        return bus;
//    }
//
//    private void clear() {
//        bus.clear();
//        bus = null;
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//
//        tank.load(NBT);
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//
//        tank.saveAdditional(NBT);
//    }
//
//    @Override
//    public boolean canConnectToPipe(MachineRegistry m) {
//        return m == MachineRegistry.HOSE || m == MachineRegistry.BEDPIPE;
//    }
//
//    @Override
//    public Fluid getInputFluid() {
//        return RotaryFluids.LUBRICANT.get();
//    }
//
//    @Override
//    public boolean canReceiveFrom(Direction from) {
//        return from.getStepY() != 0;
//    }
//
//    @Override
//    public int getCapacity() {
//        return 8000;
//    }
//
//    @Override
//    public void breakBlock() {
//        this.clear();
//    }
//
//    @Override
//    public void getOutputs(Collection<BlockEntity> c, Direction dir) {
//        Collection<BlockEntityPowerBus> c2 = bus.getBlocks();
//        for (BlockEntityPowerBus te : c2) {
//            te.getAllOutputs(c, dir);
//        }
//    }
//
//}
