package reika.rotarycraft.base.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.blockentities.transmission.BlockEntityBeltHub;
import reika.rotarycraft.blockentities.transmission.BlockEntityChainDrive;

/**
 * Belt/chain hub block. Right-clicking a hub with its belt/chain item stores the first end in the
 * item NBT; clicking the second hub consumes (distance-1) items and links the pair (legacy
 * ItemMulti.tryBeltConnection flow).
 */
public class BlockBeltHub extends BlockBasicMachine {

    private final boolean chain;

    public BlockBeltHub(boolean chain, Properties properties) {
        super(properties.noOcclusion());
        this.chain = chain;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return chain ? new BlockEntityChainDrive(pPos, pState) : new BlockEntityBeltHub(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            @SuppressWarnings("unchecked")
            BlockEntityTicker<T> t = (BlockEntityTicker<T>) clientPhiTicker(BlockEntityBeltHub.class);
            return t;
        }
        return (pLevel1, pPos, pState1, pBlockEntity) ->
                ((BlockEntityBeltHub) pBlockEntity).updateEntity(pLevel1, pPos);
    }

    @Override
    protected boolean isCustomRendered() {
        return true;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack is, BlockState state, Level level, BlockPos pos, Player ep, InteractionHand hand, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof BlockEntityBeltHub hub))
            return super.useItemOn(is, state, level, pos, ep, hand, hit);
        if (is.isEmpty() || is.getItem() != hub.getBeltItem().getItem())
            return super.useItemOn(is, state, level, pos, ep, hand, hit);
        if (level.isClientSide())
            return InteractionResult.SUCCESS;

        var tag = is.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (!tag.contains("end1")) {
            tag.putLong("end1", pos.asLong());
            CustomData.set(DataComponents.CUSTOM_DATA, is, tag);
            ep.sendOverlayMessage(Component.literal("Belt end set; click the far hub"));
            return InteractionResult.SUCCESS;
        }
        BlockPos c1 = BlockPos.of(tag.getLongOr("end1", 0L));
        tag.remove("end1");
        CustomData.set(DataComponents.CUSTOM_DATA, is, tag);
        if (!(level.getBlockEntity(c1) instanceof BlockEntityBeltHub other)) {
            ep.sendOverlayMessage(Component.literal("Tile at other end is invalid"));
            return InteractionResult.SUCCESS;
        }
        int dl = c1.distManhattan(pos) - 1;
        if (is.getCount() >= dl || ep.isCreative()) {
            other.resetOther();
            hub.resetOther();
            other.reset();
            hub.reset();
            if (other.tryConnect(level, pos) && hub.tryConnect(level, c1)) {
                if (!ep.isCreative())
                    is.shrink(dl);
                other.setChanged();
                hub.setChanged();
                ep.sendOverlayMessage(Component.literal("Connected (" + (dl + 1) + " blocks)"));
            } else {
                ep.sendOverlayMessage(Component.literal("Connection is invalid"));
            }
        } else {
            ep.sendOverlayMessage(Component.literal("Not enough " + (chain ? "chain" : "belt") + " items (" + dl + " needed)"));
        }
        return InteractionResult.SUCCESS;
    }
}
