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
import reika.dragonapi.instantiable.gui.slot.ResultSlotItemHandler;
import reika.rotarycraft.base.IOMachineContainer;
import reika.rotarycraft.blockentities.processing.BlockEntityCentrifuge;
import reika.rotarycraft.registry.RotaryMenus;

public class ContainerCentrifuge extends IOMachineContainer<BlockEntityCentrifuge> {

    //Client
    public ContainerCentrifuge(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityCentrifuge) inv.player.level().getBlockEntity(data.readBlockPos()));
    }

    public ContainerCentrifuge(int id, Inventory inv, BlockEntityCentrifuge te) {
        super(RotaryMenus.CENTRIFUGE.get(), id, inv, te);
        // Original 1.7 layout: input at (26, 38), 3x3 output grid starting at (85, 20).
        this.addSlot(te.itemHandler.slot(0, 26, 38));
        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 3; k++) {
                this.addSlot(new ResultSlotItemHandler(te.itemHandler, 1 + i * 3 + k, 85 + k * 18, 20 + i * 18));
            }
        }
        this.addPlayerInventory(inv);
    }
}
