package reika.rotarycraft.base.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.blockentities.processing.BlockEntityAutoCrafter;

/** V33a BlockIMachine CRAFTER: a plain textured cube (crafter_top / steel / steel_dark), no model renderer. */
public class BlockAutoCrafter extends BlockBasicMachine {

    public BlockAutoCrafter(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BlockEntityAutoCrafter(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide())
            return null;
        return (pLevel1, pPos, pState1, pBlockEntity) ->
                ((BlockEntityAutoCrafter) pBlockEntity).updateEntity(pLevel1, pPos);
    }
}
