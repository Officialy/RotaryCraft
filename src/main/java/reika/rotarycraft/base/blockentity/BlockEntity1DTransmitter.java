/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.base.blockentity;

import reika.rotarycraft.api.power.ShaftMerger;
import reika.rotarycraft.auxiliary.PowerSourceList;
import reika.rotarycraft.auxiliary.interfaces.PowerSourceTracker;
import reika.rotarycraft.auxiliary.interfaces.SimpleProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;

import java.util.Collection;

public abstract class BlockEntity1DTransmitter extends BlockEntityTransmissionMachine implements SimpleProvider {

    protected int ratio;
    protected boolean performRatio = true;

    public BlockEntity1DTransmitter(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public final int getRatio() {
        return ratio;
    }

    public void getIOSides(Level world, BlockPos pos, boolean hasVertical) {
        if (world.getBlockState(pos).getBlock() instanceof BlockRotaryCraftMachine) {
            switch (world.getBlockState(pos).getValue(BlockRotaryCraftMachine.FACING)) {
                case EAST -> read = Direction.WEST;
                case WEST -> read = Direction.EAST;
                case SOUTH -> read = Direction.NORTH;
                case NORTH -> read = Direction.SOUTH;
                case DOWN -> {
                    if (hasVertical) {
                        read = Direction.DOWN;
                    }
                }
                case UP -> {
                    if (hasVertical) {
                        read = Direction.UP;
                    }
                }
            }
        }
        write = read.getOpposite();
    }

    protected abstract void transferPower(Level world, BlockPos pos);

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public PowerSourceList getPowerSources(PowerSourceTracker io, ShaftMerger caller) {
        if (read == null)
            return new PowerSourceList();
        return PowerSourceList.getAllFrom(level, read, new BlockPos(worldPosition.getX() + read.getStepX(), worldPosition.getY() + read.getStepY(), worldPosition.getZ() + read.getStepZ()), this, caller);
    }

    @Override
    public void getAllOutputs(Collection<BlockEntity> c, Direction dir) {
        if (dir == read)
            c.add(getAdjacentBlockEntity(write));
    }

}
