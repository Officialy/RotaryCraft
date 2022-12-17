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
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.IContainerFactory;
import reika.rotarycraft.registry.RotaryMenus;

public class ContainerHandCraft extends AbstractContainerMenu {

    private final Level level;
    /**
     * The crafting matrix inventory (3x3).
     */
    public CraftingContainer craftMatrix = new CraftingContainer(this, 3, 3);
//    public Container craftResult = new InventoryCraftResult();


    //Client
    public ContainerHandCraft(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, inv.player.level);
    }
    //Server
    public ContainerHandCraft(final int id, Inventory player, Level par2World) {
        super(RotaryMenus.HAND_CRAFT.get(), id);
        level = par2World;
//   todo     this.addSlot(new SlotCrafting(player, craftMatrix, craftResult, 0, 124, 35));
        int var6;
        int var7;
        for (var6 = 0; var6 < 3; ++var6)
            for (var7 = 0; var7 < 3; ++var7)
                this.addSlot(new Slot(craftMatrix, var7 + var6 * 3, 30 + var7 * 18, 17 + var6 * 18));
        for (var6 = 0; var6 < 3; ++var6)
            for (var7 = 0; var7 < 9; ++var7)
                this.addSlot(new Slot(player, var7 + var6 * 9 + 9, 8 + var7 * 18, 84 + var6 * 18));
        for (var6 = 0; var6 < 9; ++var6)
            this.addSlot(new Slot(player, var6, 8 + var6 * 18, 142));
//   todo     this.onCraftMatrixChanged(craftMatrix);
    }


    @Override
    public void slotsChanged(Container pInventory) {
        super.slotsChanged(pInventory);
    }

//    @Override
//todo    public void onCraftMatrixChanged(Container par1IInventory) {
//    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        //        craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(craftMatrix, level));
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (!level.isClientSide) {
            for (int var2 = 0; var2 < 9; ++var2) {
                ItemStack var3 = craftMatrix.getItem(var2);
                if (var3 != null)
                    player.drop(var3, true);
            }
        }
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return false;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack var3 = null;
        Slot var4 = slots.get(index);

        if (var4 != null && var4.hasItem()) {
            ItemStack var5 = var4.getItem();
            var3 = var5.copy();

            if (index == 0) {
                if (!this.moveItemStackTo(var5, 10, 46, true)) {
                    return null;
                }

                var4.onQuickCraft(var5, var3);
            } else if (index >= 10 && index < 37) {
                if (!this.moveItemStackTo(var5, 37, 46, false)) {
                    return null;
                }
            } else if (index >= 37 && index < 46) {
                if (!this.moveItemStackTo(var5, 10, 37, false)) {
                    return null;
                }
            } else if (!this.moveItemStackTo(var5, 10, 46, false)) {
                return null;
            }

            if (var5.getCount() == 0) {
                var4.set(ItemStack.EMPTY);
            } else {
                var4.setChanged();
            }

            if (var5.getCount() == var3.getCount()) {
                return null;
            }

            var4.onTake(player, var5);
        }

        return var3;
    }

    public static class Factory implements IContainerFactory<ContainerHandCraft> {
        @Override
        public ContainerHandCraft create(final int id, final Inventory inv, final FriendlyByteBuf data) {
            final Level world = inv.player.getCommandSenderWorld();

            return new ContainerHandCraft(id, inv, world);
        }
    }
}
