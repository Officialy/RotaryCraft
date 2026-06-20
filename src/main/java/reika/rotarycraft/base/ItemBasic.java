/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.base;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.dragonapi.base.BlockEntityBase;
import reika.rotarycraft.auxiliary.interfaces.UpgradeableMachine;

import java.util.Random;

public class ItemBasic extends Item {

    protected Random par5Random = new Random();

    public ItemBasic(Properties properties, int max) {
        super(properties.stacksTo(max));
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level world = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        Player ep = ctx.getPlayer();
        ItemStack is = ctx.getItemInHand();
        BlockEntity te = world.getBlockEntity(pos);
        if (te instanceof UpgradeableMachine u) {
            if (u.canUpgradeWith(is)) {
                if (!world.isClientSide()) {
                    u.upgrade(is);
                    if (te instanceof BlockEntityBase base)
                        base.syncAllData(true);
                    if (ep != null && !ep.isCreative())
                        is.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return super.useOn(ctx);
    }

}