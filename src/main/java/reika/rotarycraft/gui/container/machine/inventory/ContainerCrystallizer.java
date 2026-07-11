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
import reika.rotarycraft.blockentities.processing.BlockEntityCrystallizer;
import reika.rotarycraft.registry.RotaryMenus;

/** Crystallizer: frozen-product output (slot 0) + dry-ice coolant (slot 1), legacy layout. */
public class ContainerCrystallizer extends IOMachineContainer<BlockEntityCrystallizer> {

    public ContainerCrystallizer(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityCrystallizer) inv.player.level().getBlockEntity(data.readBlockPos()));
    }

    public ContainerCrystallizer(int id, Inventory inv, BlockEntityCrystallizer te) {
        super(RotaryMenus.CRYSTALLIZER.get(), id, inv, te);
        this.addSlot(te.getCrystallizerHandler().slot(0, 80, 35));
        this.addSlot(te.getCrystallizerHandler().slot(1, 125, 35));
        this.addPlayerInventory(inv);
    }
}
