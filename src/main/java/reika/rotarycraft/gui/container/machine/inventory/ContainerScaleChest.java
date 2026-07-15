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

    // Client-side. Only the pos is in the buffer (the vanilla openMenu(MenuProvider, BlockPos) form,
    // which is how the block's right-click opens it too); the page is read from the synced tile.
    public ContainerScaleChest(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityScaleableChest) inv.player.level().getBlockEntity(data.readBlockPos()));
    }

    public ContainerScaleChest(int id, Inventory inv, BlockEntityScaleableChest te) {
        this(id, inv, te, te.page);
    }

    // Server-side (explicit page).
    public ContainerScaleChest(int id, Inventory inv, BlockEntityScaleableChest te, int page) {
        super(RotaryMenus.SCALECHEST.get(), id, inv, te);
        this.page = page;

        // Match the original layout so CoreContainer.quickMoveStack's MultiPageInventory boundary
        // (invsize+base = player inventory start) lands correctly: only [0, offset) hidden slots +
        // this page's visible slots, NOT the later pages' slots.
        int offset = page * BlockEntityScaleableChest.SLOTS_PER_PAGE;
        for (int i = 0; i < offset; i++)
            this.addSlot(i, -9000, -9000); //off-screen: kept only so indices align with the tile
        int visible = te.getSlotsOnPage(page);
        for (int rel = 0; rel < visible; rel++) {
            int x = 8 + 18 * (rel % 9);
            int y = 18 + 18 * (rel / 9);
            this.addSlot(offset + rel, x, y);
        }

        // Player inventory at a fixed position below the (up to 6-row) chest grid.
        this.addPlayerInventoryWithOffset(inv, 0, 56);
    }

    @Override
    public boolean stillValid(net.minecraft.world.entity.player.Player player) {
        return tile.isUseableByPlayer(player) && super.stillValid(player);
    }
}
