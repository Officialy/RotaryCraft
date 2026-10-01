package reika.rotarycraft.base.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.blockentities.processing.BlockEntityFuelConverter;
import reika.rotarycraft.registry.RotaryBlockEntities;

public class BlockFuelConverter extends BlockBasicMachine {
    public BlockFuelConverter(Properties properties) { super(properties.noOcclusion()); }
    @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new BlockEntityFuelConverter(pos, state); }
    @Override public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type != RotaryBlockEntities.FUEL_ENHANCER.get() ? null
                : (world, pos, blockState, entity) -> ((BlockEntityFuelConverter) entity).updateEntity(world, pos);
    }
    @Override protected boolean isCustomRendered() { return true; }
    @Override protected boolean hasAnalogOutputSignal(BlockState state) { return true; }
    @Override protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, net.minecraft.core.Direction direction) {
        return level.getBlockEntity(pos) instanceof BlockEntityFuelConverter tile ? tile.getRedstoneOverride() : 0;
    }
}
