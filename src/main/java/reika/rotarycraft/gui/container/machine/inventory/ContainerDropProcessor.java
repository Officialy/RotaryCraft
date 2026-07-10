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
import reika.rotarycraft.blockentities.processing.BlockEntityDropProcessor;
import reika.rotarycraft.registry.RotaryMenus;

/** Drop processor: block-item input (slot 0) + drops output (slot 1), legacy furnace-style layout. */
public class ContainerDropProcessor extends IOMachineContainer<BlockEntityDropProcessor> {

    public ContainerDropProcessor(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityDropProcessor) inv.player.level().getBlockEntity(data.readBlockPos()));
    }

    public ContainerDropProcessor(int id, Inventory inv, BlockEntityDropProcessor te) {
        super(RotaryMenus.DROPS.get(), id, inv, te);
        this.addSlot(te.getItemHandler().slot(0, 52, 35));
        this.addSlot(te.getItemHandler().slot(1, 112, 35));
        this.addPlayerInventory(inv);
    }
}
