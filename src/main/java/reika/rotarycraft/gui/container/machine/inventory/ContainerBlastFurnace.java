/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2025
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.gui.container.machine.inventory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import reika.dragonapi.base.CoreContainer;
import reika.dragonapi.instantiable.gui.slot.ResultSlotItemHandler;
import reika.dragonapi.instantiable.gui.slot.SlotApprovedItems;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.blockentities.production.BlockEntityBlastFurnace;
import reika.rotarycraft.registry.RotaryItems;
import reika.rotarycraft.registry.RotaryMenus;

/**
 * 1.20.1-ported container for the RotaryCraft Blast Furnace.
 */
public class ContainerBlastFurnace extends CoreContainer<BlockEntityBlastFurnace> {

    private final BlockEntityBlastFurnace blast;

    /* --------------------------------------------------------------------- */
    /*  Constructors                                                          */
    /* --------------------------------------------------------------------- */

    // --- Client side
    public ContainerBlastFurnace(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityBlastFurnace) inv.player.level().getBlockEntity(data.readBlockPos()));
    }

    // --- Server side
    public ContainerBlastFurnace(int id, Inventory playerInv, BlockEntityBlastFurnace te) {
        super(RotaryMenus.BLAST_FURNACE.get(), id, playerInv, te);
        this.blast = te;

        int slot = 0;

        // single input (slot 0 - center additive)
        this.addSlot(new SlotItemHandler(ii, slot++, 26, 35));

        // 3 × 3 grid (slots 1-9)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                this.addSlot(new SlotItemHandler(ii, slot++, 62 + col * 18, 17 + row * 18));
            }
        }

        // OUTPUT SLOTS - FIXED: Use the output inventory instead of internal inventory
        // Slot 10: center output - use outputInv slot 0
        this.addSlot(new ResultSlotItemHandler(blast.getOutputInventory(), 0, 148, 35));

        // Slot 11: lower additive (still internal inventory)
        this.addSlot(new SlotItemHandler(ii, slot++, 26, 54));

        // Slot 12: upper output - use outputInv slot 1
        this.addSlot(new ResultSlotItemHandler(blast.getOutputInventory(), 1, 148, 17));

        // Slot 13: lower output - use outputInv slot 2
        this.addSlot(new ResultSlotItemHandler(blast.getOutputInventory(), 2, 148, 53));

        // Slot 14: upper additive (still internal inventory)
        this.addSlot(new SlotItemHandler(ii, slot++, 26, 16));

        // recipe pattern (accepts only blank Craft Pattern item) - slot 15
        this.addSlot(new SlotApprovedItems(te, BlockEntityBlastFurnace.PATTERN_SLOT, 123, 53)
                .addItem(RotaryItems.CRAFT_PATTERN.get()));

        // player inventory & hot-bar
        this.addPlayerInventory(playerInv);
    }

    /* --------------------------------------------------------------------- */
    /*  Click handling                                                        */
    /* --------------------------------------------------------------------- */

    /**
     * Middle-click on an internal slot toggles its "locked" state
     * (creative-only, prevents duping).
     * Afterwards falls through to the normal click pipeline so regular
     * behaviour is preserved.
     */
    @Override
    public void clicked(int slotId, int button, ClickType clickType, Player player) {
        if (slotId >= 0 && slotId < blast.getContainerSize()) {
            Slot slot = this.slots.get(slotId);
            if (slot instanceof Slot && clickType == ClickType.CLONE && button == 2) { // middle-click
                //blast.lockedSlots[slotId] = !blast.lockedSlots[slotId];
                blast.syncAllData(false);
                return; // cancel further handling – avoids creative-mode dupes
            }
        }
        super.clicked(slotId, button, clickType, player);

        /* Achievement hook – kept for parity, commented until achievements re-added
        if (slotId == 10 || slotId == 12 || slotId == 13) {
            ItemStack stack = this.slots.get(slotId).getItem();
            if (ReikaItemHelper.matchStacks(RotaryItems.HSLA_STEEL_INGOT.get().getDefaultInstance(), stack)) {
                // RotaryAchievements.MAKESTEEL.triggerAchievement(player);
            }
        }
        */
    }

    /* --------------------------------------------------------------------- */
    /*  Shift-click                                                           */
    /* --------------------------------------------------------------------- */

    /**
     * Handles quick-move between player inventory and machine inventory.
     * Also taps in to the old HSLA-Steel achievement trigger.
     */
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack original = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack current = slot.getItem();
            original = current.copy();

            int machineSlots = blast.getContainerSize();

            if (index < machineSlots) { // from machine → player
                if (!this.moveItemStackTo(current, machineSlots, this.slots.size(), true))
                    return ItemStack.EMPTY;
            } else {                    // from player → machine
                if (!this.moveItemStackTo(current, 0, machineSlots, false))
                    return ItemStack.EMPTY;
            }

            if (current.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            // achievement check on successful extraction
            if ((index == 10 || index == 12 || index == 13) &&
                    ReikaItemHelper.matchStacks(RotaryItems.HSLA_STEEL_INGOT.get().getDefaultInstance(), original)) {
                // RotaryAchievements.MAKESTEEL.triggerAchievement(player);
            }
        }
        return original;
    }
}