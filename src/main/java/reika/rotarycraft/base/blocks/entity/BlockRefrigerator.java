package reika.rotarycraft.base.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.blockentities.production.BlockEntityRefrigerator;

public class BlockRefrigerator extends BlockBasicMachine {

    public BlockRefrigerator(Properties properties) {
        super(properties.noOcclusion());
    }

    
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityRefrigerator(pos, state);
    }

    
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            @SuppressWarnings("unchecked")
            BlockEntityTicker<T> t = (BlockEntityTicker<T>) clientPhiTicker(BlockEntityRefrigerator.class);
            return t;
        }
        return (pLevel1, pPos, pState1, pBlockEntity) -> {
            ((BlockEntityRefrigerator) pBlockEntity).updateEntity(pLevel1, pPos);
        };
    }
}
