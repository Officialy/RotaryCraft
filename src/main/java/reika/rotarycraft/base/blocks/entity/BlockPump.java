package reika.rotarycraft.base.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.blockentities.production.BlockEntityPump;

public class BlockPump extends BlockBasicMachine {

    public BlockPump(Properties properties) {
        super(properties.noOcclusion());
    }


    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BlockEntityPump(pPos, pState);
    }


    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            // 26.1: drive the pump's shaft animation client-side from the synced omega so the
            // BER's phi-based piston motion is visible (phi itself is not part of the sync tag).
            @SuppressWarnings("unchecked")
            BlockEntityTicker<T> t = (BlockEntityTicker<T>) clientPhiTicker(BlockEntityPump.class);
            return t;
        }
        return (pLevel1, pPos, pState1, pBlockEntity) -> {
            ((BlockEntityPump) pBlockEntity).updateEntity(pLevel1, pPos);
        };
    }

    @Override
    protected boolean isCustomRendered() { return true; }
}
