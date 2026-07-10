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

import reika.dragonapi.base.CoreContainer;
import reika.rotarycraft.blockentities.processing.BlockEntityDryingBed;
import reika.rotarycraft.registry.RotaryMenus;

/** Drying bed: single (output-only) dried-product slot. */
public class ContainerDryingBed extends CoreContainer<BlockEntityDryingBed> {

    public ContainerDryingBed(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityDryingBed) inv.player.level().getBlockEntity(data.readBlockPos()));
    }

    public ContainerDryingBed(int id, Inventory inv, BlockEntityDryingBed te) {
        super(RotaryMenus.DRYING.get(), id, inv, te);
        this.addSlot(te.itemHandler.slot(0, 80, 35));
        this.addPlayerInventory(inv);
    }
}
