/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.auxiliary;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import reika.rotarycraft.auxiliary.interfaces.PipeRenderConnector;
import reika.rotarycraft.auxiliary.interfaces.PumpablePipe;
import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

/**
 * Forces fluid along a pipe run against gravity or a pressure gradient: sitting inline between two
 * {@link PumpablePipe}s along its facing axis, it moves fluid from the pipe behind it to the pipe in
 * front at {@code omega/4} mB/tick. Power is summed from every side. Port of the legacy
 * {@code TileEntityPipePump}; the 1.7 {@code read}-based axis is replaced by the block's FACING.
 */
public class BlockEntityPipePump extends BlockEntityPowerReceiver implements PipeRenderConnector {

    public BlockEntityPipePump(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.PIPEPUMP.get(), pos, state);
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.PIPEPUMP;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.PIPEPUMP.get();
    }

    @Override
    protected String getTEName() {
        return "pipepump";
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    private Direction facing() {
        return this.getBlockState().getValue(BlockRotaryCraftMachine.FACING);
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        this.getSummativeSidedPower();

        if (power < MINPOWER || omega < MINSPEED)
            return;

        Direction facing = this.facing();
        // Pull from the pipe behind, push into the pipe ahead.
        BlockEntity behind = this.getAdjacentBlockEntity(facing.getOpposite());
        BlockEntity ahead = this.getAdjacentBlockEntity(facing);
        if (behind instanceof PumpablePipe src && ahead instanceof PumpablePipe dst) {
            if (src.canTransferTo(dst, facing))
                dst.transferFrom(src, this.getTransferrableAmount(src.getFluidLevel()));
        }
    }

    private int getTransferrableAmount(int amt) {
        return Math.min(amt, omega / 4);
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    public boolean canConnectToPipeOnSide(Direction dir) {
        Direction facing = this.facing();
        return dir == facing || dir == facing.getOpposite();
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
