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
import reika.rotarycraft.blockentities.farming.BlockEntitySpawnerController;
import reika.rotarycraft.registry.RotaryMenus;

/** Spawner controller: no slots -- delay input + disable toggle live in the screen. */
public class ContainerSpawnerController extends IOMachineContainer<BlockEntitySpawnerController> {

    public ContainerSpawnerController(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntitySpawnerController) inv.player.level().getBlockEntity(data.readBlockPos()));
    }

    public ContainerSpawnerController(int id, Inventory inv, BlockEntitySpawnerController te) {
        super(RotaryMenus.SPAWNERCONTROLLER.get(), id, inv, te);
    }
}
