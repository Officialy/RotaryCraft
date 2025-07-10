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

import net.minecraft.network.chat.Component;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import reika.rotarycraft.base.ItemRotaryTool;

public class ItemHandBook extends ItemRotaryTool implements MenuProvider {

    public ItemHandBook() {
        super(new Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.openMenu(this);
        //RotaryAdvancements.RCUSEBOOK.triggerAchievement(player);
        return InteractionResultHolder.pass(this.getDefaultInstance()); //todo interactionresult stuff
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("item.handbook.name");
    }


    @Override
    public AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
        return null;//new HandBookMenu(p_39954_, p_39955_, p_39956_);
    }
}
