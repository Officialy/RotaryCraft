package reika.rotarycraft.base.blocks.entity.engine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.blockentities.engine.BlockEntityHydroEngine;

public class BlockHydroEngine extends BlockBasicMachine {

    public BlockHydroEngine(Properties properties) {
        super(properties.noOcclusion());
        registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BlockEntityHydroEngine(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            @SuppressWarnings("unchecked")
            BlockEntityTicker<T> t = (BlockEntityTicker<T>) clientPhiTicker(BlockEntityHydroEngine.class);
            return t;
        }
        return (pLevel1, pPos, pState1, pBlockEntity) -> {
            ((BlockEntityHydroEngine) pBlockEntity).updateEntity(pLevel1, pPos);
        };
    }

    @Override
    protected boolean isCustomRendered() { return true; }
}
