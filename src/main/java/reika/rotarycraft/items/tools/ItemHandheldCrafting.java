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
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import reika.rotarycraft.base.ItemRotaryTool;
import reika.rotarycraft.gui.container.ContainerHandCraft;

public class ItemHandheldCrafting extends ItemRotaryTool implements MenuProvider {

    public ItemStack[] items = new ItemStack[9];

    public ItemHandheldCrafting() {
        super(new Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player ep, InteractionHand hand) {
        if (!world.isClientSide()) ep.openMenu(this);
        return InteractionResultHolder.pass(this.getDefaultInstance());
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("item.handheldcrafting.name");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ContainerHandCraft(id, inventory, player);
    }
}
