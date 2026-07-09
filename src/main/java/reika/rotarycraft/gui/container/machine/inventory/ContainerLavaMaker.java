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
import reika.rotarycraft.blockentities.processing.BlockEntityLavaMaker;
import reika.rotarycraft.registry.RotaryMenus;

/** 9-slot feed grid (3x3) for the rock melter. */
public class ContainerLavaMaker extends IOMachineContainer<BlockEntityLavaMaker> {

    public ContainerLavaMaker(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityLavaMaker) inv.player.level().getBlockEntity(data.readBlockPos()));
    }

    public ContainerLavaMaker(int id, Inventory inv, BlockEntityLavaMaker te) {
        super(RotaryMenus.LAVAMAKER.get(), id, inv, te);
        for (int i = 0; i < 9; i++) {
            int col = i % 3;
            int row = i / 3;
            this.addSlot(te.getFeedHandler().slot(i, 44 + col * 18, 17 + row * 18));
        }
        this.addPlayerInventory(inv);
    }
}
