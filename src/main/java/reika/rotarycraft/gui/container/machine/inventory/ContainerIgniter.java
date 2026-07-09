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
import reika.rotarycraft.blockentities.level.BlockEntityIgniter;
import reika.rotarycraft.registry.RotaryMenus;

/** 18-slot fuel grid (9x2) for the firestarter. */
public class ContainerIgniter extends IOMachineContainer<BlockEntityIgniter> {

    public ContainerIgniter(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityIgniter) inv.player.level().getBlockEntity(data.readBlockPos()));
    }

    public ContainerIgniter(int id, Inventory inv, BlockEntityIgniter te) {
        super(RotaryMenus.IGNITER.get(), id, inv, te);
        for (int i = 0; i < 18; i++) {
            int col = i % 9;
            int row = i / 9;
            this.addSlot(te.itemHandler.slot(i, 8 + col * 18, 22 + row * 18));
        }
        this.addPlayerInventoryWithOffset(inv, 0, 18);
    }
}
