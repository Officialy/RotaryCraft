/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.fluids.FluidStack;
import net.neoforged.fluids.capability.IFluidHandler;
import reika.dragonapi.instantiable.HybridTank;
import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
import reika.rotarycraft.auxiliary.interfaces.PipeConnector;
import reika.rotarycraft.base.blockentity.BlockEntityPiping;
import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
import reika.rotarycraft.registry.DurationRegistry;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryFluids;

public class BlockEntityBucketFiller extends InventoriedPowerReceiver implements PipeConnector, DiscreteFunction, ConditionalOperation {

    public static final int CAPACITY = 24000;
    public static final Fluid WATER = Fluids.WATER;
    public static final Fluid LAVA = Fluids.LAVA;
    public static final Fluid JETFUEL = RotaryFluids.JET_FUEL.get();
    public static final Fluid LUBRICANT = RotaryFluids.LUBRICANT.get();
    private final HybridTank tank = new HybridTank("bucketfiller", CAPACITY);
    public boolean filling = true;

    public BlockEntityBucketFiller(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.BUCKET_FILLER.get(), pos, state);
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.BUCKETFILLER;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return null;
    }

    //@Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        tickcount++;
        this.getSummativeSidedPower();
        if (power < MINPOWER)
            return;
        if (omega < MINSPEED)
            return;
        if (filling) {
            //ReikaJavaLibrary.pConsole(fuelLevel);
            if (tickcount <= this.getOperationTime())
                return;
            tickcount = 0;
//            this.fillBuckets();
        } else {
            if (tickcount <= this.getOperationTime())
                return;
            tickcount = 0;
//            this.emptyBuckets();
        }
    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

//    private void emptyBuckets() {
//        for (int i = 0; i < itemHandler.getSlots(); i++) {
//            ItemStack slot = itemHandler.getStackInSlot(i);
//            if (slot != null) {
//                FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(slot);
//                if (fluid != null) {
//                    if (this.canAccept(fluid.getFluid())) {
//                        if (tank.getCapacity() >= fluid.amount + tank.getLevel()) {
//                            ItemStack is = FluidContainerRegistry.drainFluidContainer(slot);
//                            ReikaInventoryHelper.decrStack(i, inv);
//                            if (is != null)
//                                if (!ReikaInventoryHelper.addToIInv(is, this))
//                                    ReikaItemHelper.dropItem(level, worldPosition.above().offset(0.5, 0, 0.5), is);
//                            tank.addLiquid(fluid.getAmount(), fluid.getFluid());
//                            return; //uncomment to only allow 1 bucket at a time
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private void fillBuckets() {
//        for (int i = 0; i < itemHandler.getSlots(); i++) {
//            ItemStack slot = itemHandler.getStackInSlot(i);
//            if (slot != null && FluidContainerRegistry.isEmptyContainer(slot)) {
//                ItemStack is = FluidContainerRegistry.fillFluidContainer(tank.getFluid(), slot);
//                if (is != null) {
//                    tank.removeLiquid(FluidContainerRegistry.getFluidForFilledItem(is).amount);
//                    ReikaInventoryHelper.decrStack(i, inv);
//                    if (!ReikaInventoryHelper.addToIInv(is, this))
//                        ReikaItemHelper.dropItem(level, worldPosition.offset(0.5, 0.5, 0.5), is);
//                }
//            }
//        }
//    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag);
        tank.readFromNBT(tag);
    }

    @Override
    protected String getTEName() {
        return "bucketfiller";
    }

    @Override
    protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag);
        tank.writeToNBT(tag);
    }

//    @Override
//    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
//        if (filling)
//            return j == 0 && FluidContainerRegistry.isFilledContainer(itemstack);
//        return j == 0 && FluidContainerRegistry.isEmptyContainer(itemstack);
//    }

    @Override
    public boolean canConnectToPipe(MachineRegistry m) {
//        return m == MachineRegistry.HOSE || m.isStandardPipe() || m == MachineRegistry.FUELLINE;
        return true;
    }


    @Override
    public boolean canConnectToPipeOnSide(MachineRegistry p, Direction side) {
        return side.getStepY() == 0;
    }

    @Override
    public int fillPipe(Direction from, FluidStack resource, IFluidHandler.FluidAction action) {
        return 0;
    }

    @Override
    public FluidStack drainPipe(Direction from, int maxDrain, IFluidHandler.FluidAction doDrain) {
        return null;
    }
    public boolean canAccept(Fluid f) {
        return tank.isEmpty() || f.equals(tank.getActualFluid());
    }

    //    @Override
    public boolean canFill(Direction from, Fluid fluid) {
        return this.canReceiveFrom(from) && filling;
    }

    private boolean canReceiveFrom(Direction from) {
        return from.getStepY() == 0;
    }

    public void setEmpty() {
        tank.empty();
    }

    public boolean canDrain(Direction from, Fluid fluid) {
        return !filling && from.getStepY() == 0;// && ReikaFluidHelper.isFluidDrainableFromTank(fluid, tank);
    }

//    @Override
//    public FluidTankInfo[] getTankInfo(Direction from) {
//        return new FluidTankInfo[]{tank.getInfo()};
//    }

    @Override
    public BlockEntityPiping.Flow getFlowForSide(Direction side) {
        return side.getStepY() == 0 ? (filling ? BlockEntityPiping.Flow.INPUT : BlockEntityPiping.Flow.OUTPUT) : BlockEntityPiping.Flow.NONE;
    }

    public int getFluidLevel() {
        return tank.getFluidLevel();
    }

    public Fluid getContainedFluid() {
        return tank.getActualFluid().getFluid();
    }

    @Override
    public int getOperationTime() {
        return DurationRegistry.BUCKETFILLER.getOperationTime(omega);
    }

    @Override
    public boolean areConditionsMet() {
        return !ReikaInventoryHelper.isEmpty(itemHandler);
    }

    @Override
    public String getOperationalStatus() {
        return this.areConditionsMet() ? "Operational" : "No Buckets";
    }

    @Override
    public boolean hasAnInventory() {
        return true;
    }

    @Override
    public boolean hasATank() {
        return true;
    }

    @Override
    public int getContainerSize() {
        return 18;
    }

}
