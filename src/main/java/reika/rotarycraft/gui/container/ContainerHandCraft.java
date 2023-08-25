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
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import reika.rotarycraft.registry.RotaryMenus;

import java.util.Optional;

public class ContainerHandCraft extends AbstractContainerMenu {

    private final Level level;
    /**
     * The crafting matrix inventory (3x3).
     */
    public CraftingContainer craftMatrix = new TransientCraftingContainer(this, 3, 3);
    public ResultContainer craftResult = new ResultContainer();
    public Player player;

    //Client
    public ContainerHandCraft(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, inv.player);
    }

    //Server
    public ContainerHandCraft(final int id, Inventory inventory, Player player) {
        super(RotaryMenus.HAND_CRAFT.get(), id);
        level = player.level();
        this.player = player;
        this.addSlot(new ResultSlot(player, craftMatrix, craftResult, 0, 124, 35));

        int var6;
        int var7;
        for (var6 = 0; var6 < 3; ++var6)
            for (var7 = 0; var7 < 3; ++var7)
                this.addSlot(new Slot(craftMatrix, var7 + var6 * 3, 30 + var7 * 18, 17 + var6 * 18));
        for (var6 = 0; var6 < 3; ++var6)
            for (var7 = 0; var7 < 9; ++var7)
                this.addSlot(new Slot(inventory, var7 + var6 * 9 + 9, 8 + var7 * 18, 84 + var6 * 18));
        for (var6 = 0; var6 < 9; ++var6)
            this.addSlot(new Slot(inventory, var6, 8 + var6 * 18, 142));
//   todo     this.onCraftMatrixChanged(craftMatrix);
    }


    @Override
    public void slotsChanged(Container pInventory) {
        slotChangedCraftingGrid(this, level, player, this.craftMatrix, this.craftResult);
    }

    protected static void slotChangedCraftingGrid(AbstractContainerMenu p_150547_, Level level, Player p_150549_, CraftingContainer container, ResultContainer p_150551_) {
        if (!level.isClientSide) {
            ServerPlayer serverplayer = (ServerPlayer) p_150549_;
            ItemStack itemstack = ItemStack.EMPTY;
            Optional<CraftingRecipe> optional = level.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, container, level);
            if (optional.isPresent()) {
                CraftingRecipe craftingrecipe = optional.get();
                if (p_150551_.setRecipeUsed(level, serverplayer, craftingrecipe)) {
                    ItemStack itemstack1 = craftingrecipe.assemble(container, level.registryAccess());
                    if (itemstack1.isItemEnabled(level.enabledFeatures())) {
                        itemstack = itemstack1;
                    }
                }
            }

            p_150551_.setItem(0, itemstack);
            p_150547_.setRemoteSlot(0, itemstack);
            serverplayer.connection.send(new ClientboundContainerSetSlotPacket(p_150547_.containerId, p_150547_.incrementStateId(), 0, itemstack));
        }
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
//        craftResult.setItem(0, CraftingManager.getInstance().findMatchingRecipe(craftMatrix, level));
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
        return true;
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

}
