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

import reika.dragonapi.instantiable.gui.slot.ResultSlotItemHandler;
import reika.rotarycraft.blockentities.processing.BlockEntityAutoCrafter;
import reika.rotarycraft.blockentities.processing.BlockEntityAutoCrafter.CraftingMode;
import reika.rotarycraft.base.IOMachineContainer;
import reika.rotarycraft.registry.RotaryMenus;

/**
 * V33a {@code ContainerAutoCrafter}: two rows of nine pattern slots, each with its output slot below it (hidden in
 * Sustain mode, where outputs go straight to the ME network). The per-slot "just crafted" flash timers are synced as
 * container data.
 */
public class ContainerAutoCrafter extends IOMachineContainer<BlockEntityAutoCrafter> {

    private final BlockEntityAutoCrafter crafter;

    public ContainerAutoCrafter(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityAutoCrafter) inv.player.level().getBlockEntity(data.readBlockPos()));
    }

    public ContainerAutoCrafter(int id, Inventory inv, BlockEntityAutoCrafter te) {
        super(RotaryMenus.CRAFTER.get(), id, inv, te);
        crafter = te;
        for (int i = 0; i < BlockEntityAutoCrafter.SIZE; i++) {
            int dx = 8 + (i % 9) * 18;
            int dy = i < 9 ? 19 : 81;
            this.addSlot(new net.neoforged.neoforge.transfer.item.ResourceHandlerSlot(ii, ii::set, i, dx, dy) {
                @Override
                public boolean mayPlace(net.minecraft.world.item.ItemStack stack) {
                    return crafter.isItemValidForSlot(this.getSlotIndex(), stack); //V33a: programmed crafting patterns only
                }
            });
            if (te.getMode() != CraftingMode.SUSTAIN)
                this.addSlot(new ResultSlotItemHandler(ii, i + BlockEntityAutoCrafter.SIZE, dx, dy + 27));
        }

        this.addPlayerInventoryWithOffset(inv, 0, 56);

        this.addDataSlots(te.getCraftingData());
    }
}
