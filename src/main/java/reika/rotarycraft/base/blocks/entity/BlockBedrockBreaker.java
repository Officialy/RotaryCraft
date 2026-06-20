package reika.rotarycraft.base.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.blockentities.production.BlockEntityBedrockBreaker;

public class BlockBedrockBreaker extends BlockBasicMachine {

    public BlockBedrockBreaker(Properties properties) {
        // The whole body is drawn by the BER (block model is INVISIBLE). Without noOcclusion the
        // block still occludes its neighbours, so their faces are culled and nothing is drawn in
        // their place — an X-ray hole through the world (most obvious down at bedrock level).
        super(properties.noOcclusion());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BlockEntityBedrockBreaker(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        // Client uses the shared phi ticker (driven by the synced omega) so the head animates;
        // running the full updateEntity client-side left omega at 0 → no spin when powered.
        if (pLevel.isClientSide()) {
            @SuppressWarnings("unchecked")
            BlockEntityTicker<T> t = (BlockEntityTicker<T>) clientPhiTicker(BlockEntityBedrockBreaker.class);
            return t;
        }
        return (lvl, pos, st, be) -> ((BlockEntityBedrockBreaker) be).updateEntity(lvl, pos);
    }

    // rendered in-world by RenderBedrockBreaker (BER); block model itself is invisible
    @Override
    protected boolean isCustomRendered() {
        return true;
    }
}
