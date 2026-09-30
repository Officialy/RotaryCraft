package reika.rotarycraft.base.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.blockentities.level.BlockEntityTerraformer;
import reika.rotarycraft.registry.RotaryBlockEntities;

public class BlockTerraformer extends BlockBasicMachine {
    public BlockTerraformer(Properties properties) { super(properties); }
    @Override protected net.minecraft.world.InteractionResult useItemOn(net.minecraft.world.item.ItemStack stack, BlockState state, Level level, BlockPos pos, net.minecraft.world.entity.player.Player player, net.minecraft.world.InteractionHand hand, net.minecraft.world.phys.BlockHitResult hit) {
        return stack.is(reika.rotarycraft.registry.RotaryItems.TILE_SELECTOR.get()) ? net.minecraft.world.InteractionResult.PASS : super.useItemOn(stack, state, level, pos, player, hand, hit);
    }
    @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new BlockEntityTerraformer(pos, state); }
    @Override public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide() || type != RotaryBlockEntities.TERRAFORMER.get() ? null
                : (world, pos, blockState, entity) -> ((BlockEntityTerraformer)entity).updateEntity(world, pos);
    }
}
