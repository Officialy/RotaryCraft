package reika.rotarycraft.base.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.blockentities.processing.BlockEntityMagnetizer;

public class BlockMagnetizer extends BlockBasicMachine {

    public BlockMagnetizer(Properties properties) {
        super(properties.noOcclusion());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BlockEntityMagnetizer(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            @SuppressWarnings("unchecked")
            BlockEntityTicker<T> t = (BlockEntityTicker<T>) clientPhiTicker(BlockEntityMagnetizer.class);
            return t;
        }
        return (pLevel1, pPos, pState1, pBlockEntity) ->
                ((BlockEntityMagnetizer) pBlockEntity).updateEntity(pLevel1, pPos);
    }
}
