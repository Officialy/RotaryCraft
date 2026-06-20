package reika.rotarycraft.base.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.blockentities.auxiliary.BlockEntityFurnaceHeater;

public class BlockFrictionHeater extends BlockBasicMachine {

    public BlockFrictionHeater(Properties properties) {
        super(properties.noOcclusion());
    }


    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BlockEntityFurnaceHeater(pPos, pState);
    }


    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return (pLevel1, pPos, pState1, pBlockEntity) -> ((BlockEntityFurnaceHeater) pBlockEntity).updateEntity(pLevel1, pPos);
    }

    // rendered in-world by RenderFriction (BER); the block model itself is invisible
    @Override
    protected boolean isCustomRendered() {
        return true;
    }
}
