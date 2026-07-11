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
import reika.rotarycraft.blockentities.transmission.BlockEntityPowerBus;
import reika.rotarycraft.registry.RotaryMenus;

/**
 * Power bus: one gear-part slot per horizontal side (N/S/W/E), laid out as a compass rose
 * (legacy coords, shifted into the standard 176-wide frame).
 */
public class ContainerPowerBus extends IOMachineContainer<BlockEntityPowerBus> {

    public ContainerPowerBus(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityPowerBus) inv.player.level().getBlockEntity(data.readBlockPos()));
    }

    public ContainerPowerBus(int id, Inventory inv, BlockEntityPowerBus te) {
        super(RotaryMenus.POWERBUS.get(), id, inv, te);
        // Slot indices 0-3 = Direction ordinal-2 (N/S/W/E); compass-rose layout.
        int[] x = {80, 80, 44, 116};
        int[] y = {13, 57, 35, 35};
        for (int i = 0; i < 4; i++)
            this.addSlot(te.itemHandler.slot(i, x[i], y[i]));
        this.addPlayerInventory(inv);
    }
}
