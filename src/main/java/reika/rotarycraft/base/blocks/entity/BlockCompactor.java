package reika.rotarycraft.base.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.blockentities.processing.BlockEntityCompactor;

public class BlockCompactor extends BlockBasicMachine {

    public BlockCompactor(Properties properties) {
        super(properties.noOcclusion());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BlockEntityCompactor(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            @SuppressWarnings("unchecked")
            BlockEntityTicker<T> t = (BlockEntityTicker<T>) clientPhiTicker(BlockEntityCompactor.class);
            return t;
        }
        return (pLevel1, pPos, pState1, pBlockEntity) ->
                ((BlockEntityCompactor) pBlockEntity).updateEntity(pLevel1, pPos);
    }

    @Override
    protected boolean isCustomRendered() {
        return true;
    }
}
