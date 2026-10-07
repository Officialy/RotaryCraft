package reika.rotarycraft.base.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.modinterface.TileEntityFuelEngine;
import reika.rotarycraft.registry.RotaryBlockEntities;

public final class BlockFuelEngine extends BlockBasicMachine {
    public BlockFuelEngine(Properties properties) { super(properties.noOcclusion()); }
    @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new TileEntityFuelEngine(pos, state); }
    @Override public void setPlacedBy(Level level, BlockPos pos, BlockState state, net.minecraft.world.entity.LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (level.getBlockEntity(pos) instanceof TileEntityFuelEngine tile) {
            tile.isFlipped = reika.rotarycraft.auxiliary.RotaryAux.shouldSetFlipped(level, pos);
            tile.setChanged();
        }
    }
    @Override public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == RotaryBlockEntities.FUEL_ENGINE.get() ? (world, pos, blockState, tile) -> ((TileEntityFuelEngine)tile).updateEntity(world, pos) : null;
    }
    @Override protected boolean isCustomRendered() { return true; }
    @Override protected boolean hasAnalogOutputSignal(BlockState state) { return true; }
    @Override protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
        return level.getBlockEntity(pos) instanceof TileEntityFuelEngine tile ? tile.getRedstoneOverride() : 0;
    }
    @Override public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext context,
            java.util.List<net.minecraft.network.chat.Component> lines, net.minecraft.world.item.TooltipFlag flag) {
        super.appendHoverText(stack, context, lines, flag);
        if (flag.hasShiftDown() || flag.shouldDisplayAllInformation()) {
            long[] values = {(long)TileEntityFuelEngine.GEN_OMEGA * TileEntityFuelEngine.GEN_TORQUE,
                    TileEntityFuelEngine.GEN_TORQUE, TileEntityFuelEngine.GEN_OMEGA};
            String[] names = {"power", "torque", "speed"};
            for (int i = 0; i < values.length; i++) lines.add(net.minecraft.network.chat.Component.translatable(
                    "tooltip.rotarycraft.fuel_engine." + names[i], String.format(java.util.Locale.ROOT, "%.3f",
                            reika.dragonapi.libraries.mathsci.ReikaMathLibrary.getThousandBase(values[i])),
                    reika.dragonapi.libraries.mathsci.ReikaEngLibrary.getSIPrefix(values[i])));
        } else lines.add(net.minecraft.network.chat.Component.translatable("tooltip.rotarycraft.fuel_engine.shift"));
    }
    @Override protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hit) {
        if (!player.isCrouching() && stack.getCount() == 1 && stack.getItem() instanceof BucketItem bucket && bucket.content != net.minecraft.world.level.material.Fluids.EMPTY
                && level.getBlockEntity(pos) instanceof TileEntityFuelEngine tile) {
            try (var tx = Transaction.openRoot()) {
                if (tile.getFluidHandler(null).insert(FluidResource.of(bucket.content), 1000, tx) == 1000) {
                    if (!level.isClientSide()) { tx.commit(); if (!player.isCreative()) player.setItemInHand(hand, new ItemStack(Items.BUCKET)); }
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hit);
    }
}
