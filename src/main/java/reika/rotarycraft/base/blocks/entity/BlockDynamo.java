package reika.rotarycraft.base.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.modinterface.conversion.TileEntityDynamo;

/** Shaft power in from the back; energy out from the facing side. */
public final class BlockDynamo extends BlockBasicMachine {
    public BlockDynamo(Properties properties) {
        super(properties.noOcclusion());
        hasVerticalPlacement = true;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityDynamo(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) {
            @SuppressWarnings("unchecked") BlockEntityTicker<T> ticker = (BlockEntityTicker<T>) clientPhiTicker(TileEntityDynamo.class);
            return ticker;
        }
        return (lvl, pos, st, be) -> ((TileEntityDynamo) be).updateEntity(lvl, pos);
    }

    @Override
    protected boolean isCustomRendered() { return true; }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                           Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof TileEntityDynamo dynamo && dynamo.canUpgradeWith(stack)) {
            if (!level.isClientSide()) {
                dynamo.upgrade(stack);
                if (!player.isCreative()) stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hit);
    }
}
