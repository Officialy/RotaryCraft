package reika.rotarycraft.base.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.blockentities.BlockEntityChunkLoader;
import reika.rotarycraft.registry.RotaryBlockEntities;

public class BlockChunkLoader extends BlockBasicMachine {
    public BlockChunkLoader(Properties properties) { super(properties.noOcclusion()); }
    @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new BlockEntityChunkLoader(pos, state); }
    @Override public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type != RotaryBlockEntities.CHUNK_LOADER.get() ? null : (world, pos, blockState, entity) -> {
            var tile = (BlockEntityChunkLoader)entity;
            tile.updateEntity(world, pos);
            if (world.isClientSide() && tile.isActive()) reika.rotarycraft.client.ChunkLoaderParticles.spawn(world, pos, tile.phi);
        };
    }
    @Override protected boolean isCustomRendered() { return true; }
}
