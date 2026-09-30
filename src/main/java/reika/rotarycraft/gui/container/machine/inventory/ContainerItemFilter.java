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
import net.minecraft.world.inventory.ContainerInput;

import reika.rotarycraft.base.IOMachineContainer;
import reika.rotarycraft.blockentities.BlockEntityItemFilter;
import reika.rotarycraft.registry.RotaryMenus;

/** V33a {@code ContainerItemFilter}: template slot, the (take-only) filtered slot, and the 4x4 blacklist. */
public class ContainerItemFilter extends IOMachineContainer<BlockEntityItemFilter> {

    private final BlockEntityItemFilter filter;

    public ContainerItemFilter(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityItemFilter) inv.player.level().getBlockEntity(data.readBlockPos()));
    }

    public ContainerItemFilter(int id, Inventory inv, BlockEntityItemFilter te) {
        super(RotaryMenus.ITEMFILTER.get(), id, inv, te);

        filter = te;

        this.addSlot(0, 8, 8);
        this.addSlotNoClick(1, 8, 104);

        int s = (int) Math.sqrt(BlockEntityItemFilter.BLACKLIST_SLOTS);
        for (int i = 0; i < BlockEntityItemFilter.BLACKLIST_SLOTS; i++) {
            int dx = 176 + i % s * 18;
            int dy = 135 + i / s * 18;
            this.addSlot(2 + i, dx, dy);
        }

        this.addPlayerInventoryWithOffset(inv, 0, 51);
    }

    @Override
    public void clicked(int id, int button, ContainerInput type, Player ep) {
        super.clicked(id, button, type, ep);
        filter.onSlotsChanged();
    }

}
