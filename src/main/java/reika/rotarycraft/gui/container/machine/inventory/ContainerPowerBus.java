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

import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

import reika.rotarycraft.base.IOMachineContainer;
import reika.rotarycraft.blockentities.transmission.BlockEntityPowerBus;
import reika.rotarycraft.registry.RotaryMenus;

/** Power bus: one gear-part slot per horizontal side (N/S/W/E), laid out as a compass rose. */
public class ContainerPowerBus extends IOMachineContainer<BlockEntityPowerBus> {

    public ContainerPowerBus(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityPowerBus) inv.player.level().getBlockEntity(data.readBlockPos()));
    }

    /**
     * 1.7.10 ContainerPowerBus: one gear slot per horizontal side (index = Direction ordinal - 2),
     * only on sides that do not face another bus block or the hub, around the 220x220 "bus" panel;
     * the player inventory sits 22/57 px further in to centre it on that wider texture.
     */
    public ContainerPowerBus(int id, Inventory inv, BlockEntityPowerBus te) {
        super(RotaryMenus.POWERBUS.get(), id, inv, te);
        int[] x = {102, 102, 66, 138};
        int[] y = {33, 105, 69, 69};
        for (int i = 0; i < 4; i++) {
            if (te.canHaveItemInSlot(Direction.values()[i + 2]))
                this.addSlot(te.itemHandler.slot(i, x[i], y[i]));
        }
        this.addPlayerInventoryWithOffset(inv, 22, 57);
    }
}
