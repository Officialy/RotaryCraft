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
import reika.rotarycraft.blockentities.processing.BlockEntityCompactor;
import reika.rotarycraft.registry.RotaryMenus;

/** Compactor: 4 stacked matching-input slots (legacy column at x=26) + output (slot 4). */
public class ContainerCompactor extends IOMachineContainer<BlockEntityCompactor> {

    public ContainerCompactor(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityCompactor) inv.player.level().getBlockEntity(data.readBlockPos()));
    }

    public ContainerCompactor(int id, Inventory inv, BlockEntityCompactor te) {
        super(RotaryMenus.COMPACTOR.get(), id, inv, te);
        for (int i = 0; i < 4; i++)
            this.addSlot(te.getItemHandler().slot(i, 26, 8 + i * 18));
        this.addSlot(te.getItemHandler().slot(4, 80, 35));
        this.addPlayerInventory(inv);
    }
}
