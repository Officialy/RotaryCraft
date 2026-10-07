package reika.rotarycraft.base.blocks.entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.blockentities.level.BlockEntityPileDriver;
public final class BlockPileDriver extends BlockBasicMachine {
    public BlockPileDriver(Properties properties) { super(properties.noOcclusion()); }
    @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new BlockEntityPileDriver(pos, state); }
    @Override public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return (world, pos, blockState, tile) -> ((BlockEntityPileDriver)tile).updateEntity(world, pos);
    }
    @Override protected boolean isCustomRendered() { return true; }
}
