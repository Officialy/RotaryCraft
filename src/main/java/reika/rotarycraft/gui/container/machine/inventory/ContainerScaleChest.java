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

import reika.rotarycraft.base.IOMachineContainer;
import reika.rotarycraft.blockentities.storage.BlockEntityScaleableChest;
import reika.rotarycraft.registry.RotaryMenus;

/**
 * Paged container for the Scaleable Chest. All {@value BlockEntityScaleableChest#MAXSIZE} backing
 * slots are registered so their container indices line up with the tile inventory (which is what
 * {@code CoreContainer.quickMoveStack}'s {@link reika.dragonapi.interfaces.blockentity.MultiPageInventory}
 * path assumes); only the current page's 54 slots are positioned on-screen, the rest sit off-screen.
 * Switching pages closes and re-opens the menu with a new page (see the CHEST packet handler).
 */
public class ContainerScaleChest extends IOMachineContainer<BlockEntityScaleableChest> {

    public final int page;

    // Client-side
    public ContainerScaleChest(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityScaleableChest) inv.player.level().getBlockEntity(data.readBlockPos()), data.readInt());
    }

    // Server-side
    public ContainerScaleChest(int id, Inventory inv, BlockEntityScaleableChest te, int page) {
        super(RotaryMenus.SCALECHEST.get(), id, inv, te);
        this.page = page;

        int offset = page * BlockEntityScaleableChest.SLOTS_PER_PAGE;
        for (int i = 0; i < te.getContainerSize(); i++) {
            int rel = i - offset;
            if (rel >= 0 && rel < BlockEntityScaleableChest.SLOTS_PER_PAGE) {
                int x = 8 + 18 * (rel % 9);
                int y = 18 + 18 * (rel / 9);
                this.addSlot(i, x, y);
            } else {
                this.addSlot(i, -9000, -9000); //off-screen: kept only so indices align with the tile
            }
        }

        // 6-row chest grid ends at y=126; player inventory sits below (rows at dy+84, hotbar at dy+142).
        this.addPlayerInventoryWithOffset(inv, 0, 56);
    }
}
