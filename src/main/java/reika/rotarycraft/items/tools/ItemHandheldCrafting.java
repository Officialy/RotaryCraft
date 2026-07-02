/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.items.tools;

import net.minecraft.network.chat.Component;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import reika.rotarycraft.base.ItemRotaryTool;
import reika.rotarycraft.gui.container.ContainerHandCraft;
import reika.rotarycraft.registry.RotaryItems;

public class ItemHandheldCrafting extends ItemRotaryTool implements MenuProvider {

    public ItemStack[] items = new ItemStack[9];

    public ItemHandheldCrafting() {
        super(RotaryItems.itemProperties());
    }

    @Override
    public InteractionResult use(Level world, Player ep, InteractionHand hand) {
        if (!world.isClientSide()) ep.openMenu(this);
        return InteractionResult.PASS;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("item.handheldcrafting.name");
    }

    
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ContainerHandCraft(id, inventory, player);
    }
}
