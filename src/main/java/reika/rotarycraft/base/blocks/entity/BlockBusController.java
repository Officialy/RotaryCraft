package reika.rotarycraft.base.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.blockentities.transmission.BlockEntityBusController;

public class BlockBusController extends BlockBasicMachine {

    public BlockBusController(Properties properties) {
        super(properties.noOcclusion());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BlockEntityBusController(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide())
            return null;
        return (pLevel1, pPos, pState1, pBlockEntity) ->
                ((BlockEntityBusController) pBlockEntity).updateEntity(pLevel1, pPos);
    }
}
