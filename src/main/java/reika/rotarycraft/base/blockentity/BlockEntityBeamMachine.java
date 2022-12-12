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

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BlockEntityBeamMachine extends BlockEntityPowerReceiver {

    protected Direction facing = Direction.NORTH;
    protected int pipemeta;

    public BlockEntityBeamMachine(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    protected abstract void makeBeam(Level world, BlockPos pos);

    public final void getIOSides(Level world, BlockPos pos, Direction direction) {
        switch (direction) {
            case WEST -> {
                read = Direction.EAST;
                facing = read.getOpposite();
                pipemeta = 0;
            }
            case EAST -> {
                read = Direction.WEST;
                facing = read.getOpposite();
                pipemeta = 0;
            }
            case SOUTH -> {
                read = Direction.NORTH;
                facing = read.getOpposite();
                pipemeta = 2;
            }
            case NORTH -> {
                read = Direction.SOUTH;
                facing = read.getOpposite();
                pipemeta = 2;
            }
            case UP -> {    //moving up
                read = Direction.DOWN;
                facing = read.getOpposite();
            }
            case DOWN -> {    //moving down
                read = Direction.UP;
                facing = read.getOpposite();
            }
        }
    }

    public final Direction getFacing() {
        return facing;
    }

}
