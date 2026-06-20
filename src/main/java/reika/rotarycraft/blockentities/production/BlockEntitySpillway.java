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
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import reika.dragonapi.instantiable.HybridTank;
import reika.dragonapi.instantiable.data.blockstruct.BlockArray;
import reika.dragonapi.libraries.ReikaFluidHelper;
import reika.rotarycraft.auxiliary.interfaces.PipeConnector;
import reika.rotarycraft.base.blockentity.BlockEntityPiping.Flow;
import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.registry.ConfigRegistry;
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
        super.updateEntity(); // drive BlockEntityBase lifecycle
        Direction dir = this.getDrainSide();
        BlockPos frontPos = pos.relative(dir);
        BlockState frontState = world.getBlockState(frontPos);
        Fluid f = ReikaFluidHelper.lookupFluidForBlock(frontState);

        if (f == Fluids.WATER) {
            // "Column" = source water above means we have a continual waterfall/stream feeding us
            boolean isColumn = ReikaFluidHelper.lookupFluidForBlock(world.getBlockState(frontPos.above())) == Fluids.WATER;
            if (isColumn) {
                liquidPool = null;
                tank.addLiquid((int)(50 * ConfigRegistry.getFreeWaterProduction()), Fluids.WATER);
                this.setActive();
            } else {
                this.formAndDrainPool(world, pos, frontPos, frontState.getBlock());
            }
        } else {
            liquidPool = null;
        }

        if (activeTick > 0)
            activeTick--;

        // Clear any stray water that accumulated above us
        if (ReikaFluidHelper.lookupFluidForBlock(world.getBlockState(pos.above())) == Fluids.WATER)
            world.setBlock(pos.above(), Blocks.AIR.defaultBlockState(), 1);
    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    private void formAndDrainPool(Level world, BlockPos pos, BlockPos frontPos, Block id) {
        if (liquidPool == null || liquidPool.isEmpty()) {
            liquidPool = new BlockArray();
            liquidPool.maxDepth = 240;
            liquidPool.clampToChunkLoad = true;
            int x = pos.getX(), y = pos.getY(), z = pos.getZ();
            liquidPool.recursiveAddWithBounds(world, frontPos.getX(), frontPos.getY(), frontPos.getZ(), id,
                    x - 64, y, z - 64, x + 64, y + 24, z + 64);
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
            world.setBlock(c2, Blocks.AIR.defaultBlockState(), 2);
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

    public Direction getDrainSide() {
        Direction d = getBlockState().getValue(BlockRotaryCraftMachine.FACING);
        // Spillway drains horizontally in the direction it faces
        return d.getAxis().isHorizontal() ? d : Direction.NORTH;
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
    public int fillPipe(Direction from, FluidStack resource, FluidAction action) {
        return 0;
    }

    @Override
    public FluidStack drainPipe(Direction from, int maxDrain, FluidAction doDrain) {
        return from == Direction.DOWN ? tank.drain(maxDrain, doDrain) : FluidStack.EMPTY;
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

        activeTick = NBT.getIntOr("active", 0);
    }

    @Override
    protected String getTEName() {
        return "spillway";
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public FluidStack getFluidInTank(int slot) {
        return tank.getFluid();
    }

    @Override
    public int getTankCapacity(int slot) {
        return CAPACITY;
    }

    @Override
    public boolean isFluidValid(int slot, FluidStack stack) {
        return false; // no external fill; water only comes from the environment
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return 0; // not fillable externally
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (resource.getFluid() != Fluids.WATER) return FluidStack.EMPTY;
        return tank.drain(resource.getAmount(), action);
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return tank.drain(maxDrain, action);
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
