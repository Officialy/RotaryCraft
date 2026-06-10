package reika.rotarycraft.base.blocks.entity.engine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.blockentities.engine.BlockEntityDCEngine;

public class BlockDCEngine extends BlockBasicMachine {

    public BlockDCEngine(Properties properties) {
        super(properties.noOcclusion());
    }

    @Override
    @Deprecated
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return Shapes.empty();
    }

    // Note: previously had {@code getRenderShape -> RenderShape.MODEL} hard-coded here, which
    // bypassed BlockBasicMachine's {@link #isCustomRendered}-driven override and forced the
    // missing-texture cube_all model to render on top of the BER. Removed so the inherited
    // {@code getRenderShape -> isCustomRendered ? INVISIBLE : MODEL} kicks in.
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BlockEntityDCEngine(pPos, pState);
    }


    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            // 26.1: drive the spinning-shaft animation locally from the synced omega so the
            // engine's BER sees a non-stale phi value (phi itself is not sync'd).
            @SuppressWarnings("unchecked")
            BlockEntityTicker<T> t = (BlockEntityTicker<T>) clientPhiTicker(BlockEntityDCEngine.class);
            return t;
        }
        return (pLevel1, pPos, pState1, pBlockEntity) -> {
            ((BlockEntityDCEngine) pBlockEntity).updateEntity(pLevel1, pPos);
        };
    }

    @Override
    protected boolean isCustomRendered() { return true; }
}
