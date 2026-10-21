package reika.rotarycraft.base.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.blockentities.processing.BlockEntityGrinder;

public class BlockGrinder extends BlockBasicMachine {

    /**
     * 1.7.10 MachineRegistry.getMaxY gave the grinder a 0.8125 top for both its selection and
     * collision box; standing on the grinding wheels is what puts an entity inside the block and
     * lets {@code entityInside} grind it.
     */
    private static final VoxelShape SHAPE = box(0, 0, 0, 16, 13, 16);

    public BlockGrinder(Properties properties) {
        super(properties.noOcclusion());
    }

    
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BlockEntityGrinder(pPos, pState);
    }

    
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            // 26.1: client-side phi integration so the grinder wheel actually spins visually.
            @SuppressWarnings("unchecked")
            BlockEntityTicker<T> t = (BlockEntityTicker<T>) clientPhiTicker(BlockEntityGrinder.class);
            return t;
        }
        return (pLevel1, pPos, pState1, pBlockEntity) -> {
            ((BlockEntityGrinder) pBlockEntity).updateEntity(pLevel1, pPos);
        };
    }

    @Override
    protected boolean isCustomRendered() { return true; }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
