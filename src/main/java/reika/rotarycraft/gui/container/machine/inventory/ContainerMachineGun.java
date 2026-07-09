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
import reika.rotarycraft.blockentities.weaponry.BlockEntityMachineGun;
import reika.rotarycraft.registry.RotaryMenus;

/** 27-slot arrow magazine (9x3) over the player inventory. */
public class ContainerMachineGun extends IOMachineContainer<BlockEntityMachineGun> {

    public ContainerMachineGun(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityMachineGun) inv.player.level().getBlockEntity(data.readBlockPos()));
    }

    public ContainerMachineGun(int id, Inventory inv, BlockEntityMachineGun te) {
        super(RotaryMenus.MACHINEGUN.get(), id, inv, te);
        for (int i = 0; i < te.getContainerSize(); i++) {
            int col = i % 9;
            int row = i / 9;
            this.addSlot(te.itemHandler.slot(i, 8 + col * 18, 18 + row * 18));
        }
        this.addPlayerInventoryWithOffset(inv, 0, 14);
    }
}
