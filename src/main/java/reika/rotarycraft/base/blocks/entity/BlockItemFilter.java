package reika.rotarycraft.base.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.blockentities.BlockEntityItemFilter;

/** V33a BlockIMachine ITEMFILTER: a plain textured cube (filter_top / steel / steel_dark). Ticks both sides (the GUI reads the client's match data). */
public class BlockItemFilter extends BlockBasicMachine {

    public BlockItemFilter(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BlockEntityItemFilter(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return (pLevel1, pPos, pState1, pBlockEntity) ->
                ((BlockEntityItemFilter) pBlockEntity).updateEntity(pLevel1, pPos);
    }
}
