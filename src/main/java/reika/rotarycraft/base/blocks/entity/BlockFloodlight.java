package reika.rotarycraft.base.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.blockentities.level.BlockEntityFloodlight;

public class BlockFloodlight extends BlockBasicMachine {
    public BlockFloodlight(Properties properties) {
        super(properties.noOcclusion());
        hasVerticalPlacement = true;
    }


    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BlockEntityFloodlight(pPos, pState);
    }

    @Override
    @Deprecated
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return Shapes.empty();
    }

    // Inherited BlockBasicMachine#getRenderShape consults isCustomRendered (true → INVISIBLE);
    // the previous explicit MODEL override here masked it and double-drew the missing-texture cube.
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide() ? null : ((pLevel1, pPos, pState1, pBlockEntity) -> {
            ((BlockEntityFloodlight) pBlockEntity).updateEntity(pLevel1, pPos);
        });
    }

    @Override
    protected boolean isCustomRendered() { return true; }
}
