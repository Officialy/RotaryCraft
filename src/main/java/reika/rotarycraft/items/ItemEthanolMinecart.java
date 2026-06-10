/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.items;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.gameevent.GameEvent;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.ItemRotaryTool;

public class ItemEthanolMinecart extends ItemRotaryTool {

    public ItemEthanolMinecart() {
        super(reika.rotarycraft.registry.RotaryItems.itemProperties());
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        //EntityGasMinecart cart = new EntityGasMinecart(level, player.blockPosition().getX() + 0.5, player.blockPosition().getY() + 0.5, player.blockPosition().getZ() + 0.5);
        //level.addFreshEntity(cart);
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        Level level = useOnContext.getLevel();
        BlockPos blockpos = useOnContext.getClickedPos();
        BlockState blockstate = level.getBlockState(blockpos);
        if (!blockstate.is(BlockTags.RAILS)) {
            return InteractionResult.FAIL;
        } else {
            ItemStack itemstack = useOnContext.getItemInHand();
            if (!level.isClientSide()) {
                RailShape railshape = blockstate.getBlock() instanceof BaseRailBlock ? ((BaseRailBlock) blockstate.getBlock()).getRailDirection(blockstate, level, blockpos, null) : RailShape.NORTH_SOUTH;
                double d0 = 0.0D;
                // 1.21.5: RailShape.isAscending was renamed to isSlope().
                if (railshape.isSlope()) {
                    d0 = 0.5D;
                }

                //EntityGasMinecart cart = new EntityGasMinecart(level, (double) blockpos.getX() + 0.5D, (double) blockpos.getY() + 0.0625D + d0, (double) blockpos.getZ() + 0.5D);
                if (itemstack.has(net.minecraft.core.component.DataComponents.CUSTOM_NAME)) {
                    //    cart.setCustomName(itemstack.getHoverName());
                }

                //level.addFreshEntity(cart);
                level.gameEvent(useOnContext.getPlayer(), GameEvent.ENTITY_PLACE, blockpos);
            }

            itemstack.shrink(1);
            // 1.21.5: InteractionResult.sidedSuccess(boolean) was removed; return SUCCESS for both.
            return InteractionResult.SUCCESS;
        }
    }
}
