/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.gui.container;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import reika.rotarycraft.items.tools.ItemCraftPattern;
import reika.rotarycraft.registry.RotaryItems;
import reika.rotarycraft.registry.RotaryMenus;

/**
 * V33a {@code ContainerCraftingPattern}: programs the held Craft Pattern. The 3x3 grid is a ghost grid - clicking a
 * cell with an item copies one of it in (nothing leaves the player's inventory), clicking with an empty cursor
 * clears it - the result slot shows what the pattern's recipe mode makes, and closing the screen writes the grid
 * and its output onto the pattern.
 */
public class ContainerCraftingPattern extends AbstractContainerMenu {

    private static final int width = 3;
    private static final int height = 3;

    private final CraftingContainer craftMatrix = new TransientCraftingContainer(this, width, height);
    private final SimpleContainer craftResult = new SimpleContainer(1);
    private final Level level;
    private final Player player;

    //Client
    public ContainerCraftingPattern(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, inv.player);
    }

    //Server
    public ContainerCraftingPattern(int id, Inventory inv, Player player) {
        super(RotaryMenus.CRAFTING_PATTERN.get(), id);
        level = player.level();
        this.player = player;

        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                this.addSlot(new Slot(craftMatrix, i * width + k, 30 + k * 18, 17 + i * 18));
            }
        }

        this.addSlot(new Slot(craftResult, 0, 124, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public boolean mayPickup(Player player) {
                return false;
            }
        });

        for (int i = 0; i < 3; ++i)
            for (int k = 0; k < 9; ++k)
                this.addSlot(new Slot(inv, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
        for (int i = 0; i < 9; ++i)
            this.addSlot(new Slot(inv, i, 8 + i * 18, 142));

        ItemStack tool = player.getMainHandItem();
        ItemStack[] items = ItemCraftPattern.getItems(tool, level.registryAccess());
        for (int i = 0; i < 9; i++) {
            craftMatrix.setItem(i, items != null && items[i] != null ? items[i] : ItemStack.EMPTY);
        }

        this.slotsChanged(craftMatrix);
    }

    public void clearRecipe() {
        for (int i = 0; i < 9; i++) {
            craftMatrix.setItem(i, ItemStack.EMPTY);
        }
        craftResult.setItem(0, ItemStack.EMPTY);
    }

    @Override
    public void slotsChanged(Container ii) {
        super.slotsChanged(ii);

        ItemStack is = player.getMainHandItem();
        ItemStack out = is.is(RotaryItems.CRAFT_PATTERN.get()) ? ItemCraftPattern.getMode(is).getRecipe(craftMatrix, level) : null;
        craftResult.setItem(0, out != null ? out : ItemStack.EMPTY);
    }

    @Override
    public void clicked(int slot, int button, ContainerInput input, Player ep) {
        boolean inGUI = slot < width * height && slot >= 0;
        if (inGUI) {
            ItemStack held = this.getCarried();
            craftMatrix.setItem(slot, held.isEmpty() ? ItemStack.EMPTY : held.copyWithCount(1));
            this.slotsChanged(craftMatrix);
        }
        else if (slot == width * height) {
            //the result slot is display-only
        }
        else {
            super.clicked(slot, button, input, ep);
        }
    }

    @Override
    public void removed(Player ep) {
        super.removed(ep);

        ItemStack is = ep.getMainHandItem();
        if (!ep.level().isClientSide() && is.is(RotaryItems.CRAFT_PATTERN.get()))
            ItemCraftPattern.setRecipe(is, craftMatrix, ep.level());
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return pPlayer.getMainHandItem().is(RotaryItems.CRAFT_PATTERN.get());
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }

}
