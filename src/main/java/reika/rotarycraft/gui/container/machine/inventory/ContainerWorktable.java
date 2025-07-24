/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.gui.container.machine.inventory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.items.SlotItemHandler;
import reika.dragonapi.instantiable.gui.slot.ResultSlotItemHandler;
import reika.dragonapi.interfaces.ReikaCraftingContainer;
import reika.rotarycraft.blockentities.production.BlockEntityWorktable;
import reika.rotarycraft.registry.RotaryMenus;

public class ContainerWorktable extends ReikaCraftingContainer<BlockEntityWorktable> {

    private static BlockEntityWorktable table;
    //Client
    public ContainerWorktable(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityWorktable) inv.player.level().getBlockEntity(data.readBlockPos()), inv.player.level(), true);
        table = (BlockEntityWorktable) inv.player.level().getBlockEntity(data.readBlockPos());
    }

    public ContainerWorktable(int id, Inventory inv, BlockEntityWorktable te, Level world, boolean gui) {
        super(RotaryMenus.WORKTABLE.get(), id, inv, null/*todo null for now*/, world, gui);
        int dx = 0;
        table = te;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.addSlot(new SlotItemHandler(ii, i * 3 + j, dx + 26 + j * 18, 17 + i * 18));
            }
        }
        dx += 96 - 28 + 4;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.addSlot(new ResultSlotItemHandler(ii, 9 + i * 3 + j, dx + 26 + j * 18, 17 + i * 18));
            }
        }

//        todo this.addSlot(new SlotApprovedItems(te, 18, 6, 53).addItem(RotaryItems.CRAFTPATTERN.get()));

		/*
		dx = 153;
		int dy = 84;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				//this.addSlot(new GhostSlot(te, 18+i*3+j, dx+26+j*18, dy+i*18));
				this.addSlot(new SlotXItems(te, 18+i*3+j, dx+26+j*18, dy+i*18, 1));
			}
		}
		 */

        this.updateCraftMatrix();

        this.addPlayerInventory(inv);
//        this.onCraftMatrixChanged();
    }

    @Override
    protected BlockEntityWorktable getRecipe(CraftingContainer craftMatrix, Level world) {
        return null;//WorktableRecipes.getInstance().findMatchingRecipe(craftMatrix, world);
    }

    @Override
    protected ItemStack getOutput(BlockEntityWorktable wr) {
        return ItemStack.EMPTY;//todo wr.isRecycling() ? wr.getRecycling().getResultItem() : wr.getOutput();
    }

    @Override
    public void clearContent() {

    }

    @Override
    public int getContainerSize() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getItem(int p_18941_) {
        return null;
    }

    @Override
    public ItemStack removeItem(int p_18942_, int p_18943_) {
        return null;
    }

    @Override
    public ItemStack removeItemNoUpdate(int p_18951_) {
        return null;
    }

    @Override
    public void setItem(int p_18944_, ItemStack p_18945_) {

    }

    @Override
    public void setChanged() {

    }

    @Override
    public boolean stillValid(Player player) {
        return !tile.isRemoved();
    }
}
