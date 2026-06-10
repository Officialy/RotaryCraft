package reika.rotarycraft.base.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.blockentities.auxiliary.BlockEntityCoolingFin;

public class BlockCoolingFin extends BlockBasicMachine {

    public BlockCoolingFin(Properties properties) {
        super(properties.noOcclusion());
        this.hasVerticalPlacement = true;
    }

    /**
     * Original 1.7 placement used {@code isSidePlaced()} — the meta encoded the direction
     * FROM the fin TO the cooling target, which is the opposite of the clicked face.
     * Clicking the top of a block → fin placed above → FACING = DOWN (cools block below).
     */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Direction clicked = pContext.getClickedFace();    // face the player clicked on
        Direction target  = clicked.getOpposite();        // direction from fin toward the target block
        return this.defaultBlockState().setValue(FACING, target);
    }

    
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BlockEntityCoolingFin(pPos, pState);
    }

    
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide() ? null : ((pLevel1, pPos, pState1, pBlockEntity) -> {
            ((BlockEntityCoolingFin) pBlockEntity).updateEntity(pLevel1, pPos);
        });
    }

    @Override
    protected boolean isCustomRendered() { return true; }
}
