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
import reika.rotarycraft.blockentities.farming.BlockEntityAutoBreeder;
import reika.rotarycraft.registry.RotaryMenus;

/** Auto breeder: 18-slot (2x9) feed grid on the shared storage layout. */
public class ContainerAutoBreeder extends IOMachineContainer<BlockEntityAutoBreeder> {

    public ContainerAutoBreeder(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityAutoBreeder) inv.player.level().getBlockEntity(data.readBlockPos()));
    }

    public ContainerAutoBreeder(int id, Inventory inv, BlockEntityAutoBreeder te) {
        super(RotaryMenus.AUTOBREEDER.get(), id, inv, te);
        for (int i = 0; i < te.getContainerSize(); i++) {
            int col = i % 9;
            int row = i / 9;
            this.addSlot(te.getFeedHandler().slot(i, 8 + col * 18, 18 + row * 18));
        }
        this.addPlayerInventoryWithOffset(inv, 0, 14);
    }
}
