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
import reika.rotarycraft.blockentities.auxiliary.BlockEntityHeater;
import reika.rotarycraft.registry.RotaryMenus;

/**
 * 1.7.10 ContainerHeater: the fuel inventory in rows of nine from (8, 18), and the player rows at
 * 103 + (rows - 4) * 18 + 18 - one pixel below the standard 84 for the heater's two rows.
 */
public class ContainerHeater extends IOMachineContainer<BlockEntityHeater> {

    public ContainerHeater(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityHeater) inv.player.level().getBlockEntity(data.readBlockPos()));
    }

    public ContainerHeater(int id, Inventory inv, BlockEntityHeater te) {
        super(RotaryMenus.HEATER.get(), id, inv, te);
        int rows = te.getContainerSize() / 9;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < 9; col++)
                this.addSlot(te.itemHandler.slot(col + row * 9, 8 + col * 18, 18 + row * 18));
        }
        this.addPlayerInventoryWithOffset(inv, 0, 19 + (rows - 4) * 18 + 18);
    }
}
