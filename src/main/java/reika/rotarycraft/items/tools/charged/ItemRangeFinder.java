/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.items.tools.charged;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.HitResult;
import reika.dragonapi.libraries.ReikaPlayerAPI;
import reika.dragonapi.libraries.io.ReikaChatHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.base.ItemChargedTool;

public class ItemRangeFinder extends ItemChargedTool {

    public ItemRangeFinder() {
        super();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player ep, InteractionHand hand) {
        if (hand.equals(InteractionHand.MAIN_HAND)) {
            if (this.getDefaultInstance().getOrCreateTag().getInt("charge") <= 0) {
                this.noCharge();
                return InteractionResultHolder.fail(this.getDefaultInstance());
            }
            this.warnCharge(this.getDefaultInstance());

            HitResult mov = ReikaPlayerAPI.getLookedAtBlock(ep, 512, true);
            if (mov != null) {
                double d = ReikaMathLibrary.py3d(mov.getLocation().x() - ep.getY(), mov.getLocation().y() - ep.getY(), mov.getLocation().z() - ep.getZ());
                Block b = level.getBlockState(new BlockPos(mov.getLocation().x(), mov.getLocation().y(), mov.getLocation().z())).getBlock();
                String s = Item.BY_BLOCK.get(b).getName(new ItemStack(b, 1)).toString();
                if (s == null || s.isEmpty())
                    s = "[No Name]";
                ReikaChatHelper.write(String.format("Block '%s' is %.3fm away.", s, d));
                this.getDefaultInstance().setDamageValue(this.getDefaultInstance().getDamageValue() - 1);
            }
        }
        return InteractionResultHolder.pass(this.getDefaultInstance());

    }
}
