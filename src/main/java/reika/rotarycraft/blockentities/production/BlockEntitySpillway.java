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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import reika.dragonapi.instantiable.HybridTank;
import reika.dragonapi.instantiable.data.blockstruct.BlockArray;
import reika.rotarycraft.auxiliary.interfaces.PipeConnector;
import reika.rotarycraft.base.blockentity.BlockEntityPiping.Flow;
import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;

import java.util.ArrayList;
import java.util.Collection;


public class BlockEntitySpillway extends RotaryCraftBlockEntity implements PipeConnector, IFluidHandler {

    public static final int CAPACITY = 8000;

    private final HybridTank tank = new HybridTank("spillway", CAPACITY);
    private final Collection<BlockPos> forcedEmpty = new ArrayList<>();
    private BlockArray liquidPool;
    private int activeTick;

    public BlockEntitySpillway(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.SPILLWAY.get(), pos, state);
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.SPILLWAY;
    }

    @Override
    public boolean hasModelTransparency() {
        return true;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return null;
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        Direction dir = this.getDrainSide();
        int dx = pos.getX() + dir.getStepX();
        int dy = pos.getY() + dir.getStepY();
        int dz = pos.getZ() + dir.getStepZ();
        Block b = world.getBlockState(new BlockPos(dx, dy, dz)).getBlock();

        Block b2 = world.getBlockState(new BlockPos(dx, dy, dz).above()).getBlock();
//        Fluid f = ReikaFluidHelper.lookupFluidForBlock(b);
//        if ((InterfaceCache.STREAM.instanceOf(b)) || InterfaceCache.STREAM.instanceOf(b2)) {
//            liquidPool = null;
//            this.handleStream(world, pos, dx, dy + 1, dz);
//        } else if (f == Fluids.WATER) {
//            if (ReikaWorldHelper.isLiquidAColumn(world, dx, dy + 1, dz)) {
//                liquidPool = null;
//                tank.addLiquid((int) (50 * RotaryConfig.COMMON.getFreeWaterProduction()), Fluids.WATER);
//                this.setActive();
//            } else
//                this.formAndDrainPool(world, pos, dx, dy, dz, b);
//        } else {
//            liquidPool = null;
//        }
        if (activeTick > 0)
            activeTick--;

        Block ab = world.getBlockState(pos.above()).getBlock();
//        if (ReikaFluidHelper.lookupFluidForBlock(ab) == Fluids.WATER)
//            world.setBlock(pos.above(), Blocks.AIR.defaultBlockState(), 1);
    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    private void formAndDrainPool(Level world, BlockPos pos, int dx, int dy, int dz, Block id) {
        if (liquidPool == null || liquidPool.isEmpty()) {
            liquidPool = new BlockArray();
            liquidPool.maxDepth = 240;
//            liquidPool.recursiveAddWithBoundsMetadata(world, dx, dy, dz, id, 0, pos.getX() - 64, pos.getY(), pos.getZ() - 64, pos.getX() + 64, pos.getY() + 24, pos.getZ() + 64);
            liquidPool.sortBlocksByDistance(worldPosition);
            liquidPool.sortBlocksByHeight(true);
            forcedEmpty.clear();
        }
        if (liquidPool.isEmpty()) {
            liquidPool = null;
            return;
        }
        if (tank.canTakeIn(1000)) {
            BlockPos c = liquidPool.getNextAndMoveOn();
            forcedEmpty.add(c);
            tank.addLiquid(1000, Fluids.WATER);
            this.setActive();
        }
        for (BlockPos c2 : forcedEmpty) {
            world.setBlock(c2, Blocks.AIR.defaultBlockState(), 0, 2);
        }
    }

    private void handleStream(Level world, BlockPos pos, int dx, int dy, int dz) {
        //ensure perpendicular?
        boolean act = this.isActive();
        if (tank.canTakeIn(250)) {
            this.setActive();
            tank.addLiquid(250, Fluids.WATER);
//            if (this.tickcount % 8 == 0)
//                this.syncAllData(false);
        }
//        if (act != this.isActive())
//            this.syncAllData(false);
    }

    private void setActive() {
        boolean lastActive = activeTick > 0;
        activeTick = 4;
//        if (!lastActive)
//            this.syncAllData(false);
    }

    public boolean isActive() {
        return activeTick > 0;
    }

    private Direction getDrainSide(Direction dir) {
        return switch (dir) {
            case EAST -> Direction.EAST;
            case WEST -> Direction.WEST;
            case NORTH -> Direction.NORTH;
            case SOUTH -> Direction.SOUTH;
            default -> Direction.NORTH;
        };
    }

    public Direction getDrainSide() {
        return this.getDrainSide(dirs[1]); //todo s
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    public int getFluidLevel() {
        return tank.getFluidLevel();
    }

    public Fluid getFluid() {
        return tank.getActualFluid().getFluid();
    }

    @Override
    public boolean canConnectToPipe(MachineRegistry m) {
        return m.isStandardPipe();
    }

    @Override
    public boolean canConnectToPipeOnSide(MachineRegistry p, Direction side) {
        return this.canConnectToPipe(p);
    }

    @Override
    public int fill(Direction from, FluidStack resource, FluidAction action) {
        return 0;
    }

    @Override
    public FluidStack drain(Direction from, int maxDrain, FluidAction doDrain) {
        return from == Direction.DOWN ? tank.drain(maxDrain, doDrain) : null;
    }
    //    @Override
    public boolean canDrain(Direction from, Fluid fluid) {
        return from == Direction.DOWN;
    }

    @Override
    public Flow getFlowForSide(Direction side) {
        return side == Direction.DOWN ? Flow.OUTPUT : Flow.NONE;
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        tank.writeToNBT(NBT);

        NBT.putInt("active", activeTick);
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        tank.readFromNBT(NBT);

        activeTick = NBT.getInt("active");
    }

    @Override
    protected String getTEName() {
        return "spillway";
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
        return 0;
    }

    @NotNull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return /*from == Direction.DOWN && */resource.getFluid() == Fluids.WATER ? tank.drain(resource.getAmount(), FluidAction.EXECUTE) : null;

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
