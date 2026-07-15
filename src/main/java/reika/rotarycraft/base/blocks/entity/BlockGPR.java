/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.base.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.blockentities.surveying.BlockEntityGPR;

public class BlockGPR extends BlockBasicMachine {

    public BlockGPR(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BlockEntityGPR(pPos, pState);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity e, ItemStack stack) {
        super.setPlacedBy(world, pos, state, e, stack);
        // Faithful to 1.7.10: the scan plane runs along the axis the placer is facing.
        if (e != null && world.getBlockEntity(pos) instanceof BlockEntityGPR gpr)
            gpr.setDirection(e.getDirection().getAxis() == Direction.Axis.Z);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        // Both sides tick: the server scan carries the side effects, the client scan fills the GUI's
        // display array from the client's own loaded world copy (the array is never synced).
        if (pLevel.isClientSide())
            return (lvl, pos, state, be) -> ((BlockEntityGPR) be).clientTick(lvl, pos);
        return (lvl, pos, state, be) -> ((BlockEntityGPR) be).updateEntity(lvl, pos);
    }

    @Override
    protected boolean isCustomRendered() {
        return false;
    }
}
