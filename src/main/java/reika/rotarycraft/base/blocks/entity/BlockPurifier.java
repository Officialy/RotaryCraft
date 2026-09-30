package reika.rotarycraft.base.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.blockentities.processing.BlockEntityPurifier;
import reika.rotarycraft.registry.RotaryBlockEntities;

/** The V33a purifier uses its original full-cube texture and accepts power on all six faces. */
public class BlockPurifier extends BlockBasicMachine {
    public BlockPurifier(Properties properties) { super(properties); }
    @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new BlockEntityPurifier(pos, state); }
    @Override public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide() || type != RotaryBlockEntities.PURIFIER.get() ? null
                : (world, pos, blockState, entity) -> ((BlockEntityPurifier) entity).updateEntity(world, pos);
    }
}
