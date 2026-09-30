package reika.rotarycraft.base.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.blockentities.processing.BlockEntityDistillery;
import reika.rotarycraft.registry.RotaryBlockEntities;

public class BlockDistillery extends BlockBasicMachine {
    public BlockDistillery(Properties properties) { super(properties.noOcclusion()); }
    @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new BlockEntityDistillery(pos, state); }
    @Override public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide() || type != RotaryBlockEntities.DISTILLER.get() ? null
                : (world, pos, blockState, entity) -> ((BlockEntityDistillery) entity).updateEntity(world, pos);
    }
    @Override protected boolean isCustomRendered() { return true; }
}
