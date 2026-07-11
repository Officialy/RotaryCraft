package reika.rotarycraft.base.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.blockentities.farming.BlockEntityLawnSprinkler;

public class BlockLawnSprinkler extends BlockBasicMachine {

    public BlockLawnSprinkler(Properties properties) {
        super(properties.noOcclusion());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BlockEntityLawnSprinkler(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        // Ticks BOTH sides: server effects, client spray particles + head spin (legacy behaviour).
        return (pLevel1, pPos, pState1, pBlockEntity) ->
                ((BlockEntityLawnSprinkler) pBlockEntity).updateEntity(pLevel1, pPos);
    }

    @Override
    protected boolean isCustomRendered() {
        return true;
    }
}
