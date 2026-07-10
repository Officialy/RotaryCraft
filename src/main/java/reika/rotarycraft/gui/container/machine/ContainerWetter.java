/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.gui.container.machine;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

import reika.rotarycraft.base.IOMachineContainer;
import reika.rotarycraft.blockentities.processing.BlockEntityWetter;
import reika.rotarycraft.registry.RotaryMenus;

/** Wetter: single soak slot, centred (legacy layout). */
public class ContainerWetter extends IOMachineContainer<BlockEntityWetter> {

    public ContainerWetter(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityWetter) inv.player.level().getBlockEntity(data.readBlockPos()));
    }

    public ContainerWetter(int id, Inventory inv, BlockEntityWetter te) {
        super(RotaryMenus.WETTER.get(), id, inv, te);
        this.addSlot(te.itemHandler.slot(0, 80, 35));
        this.addPlayerInventory(inv);
    }
}
