/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.gui.screen.machine.inventory;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import reika.rotarycraft.base.BasicStorageScreen;
import reika.rotarycraft.blockentities.farming.BlockEntityAutoBreeder;
import reika.rotarycraft.gui.container.machine.inventory.ContainerAutoBreeder;

/** Auto breeder feed inventory on the shared storage background. */
/** 1.7.10 had no breeder screen: it fell through to GuiBasicStorage, ported as {@link BasicStorageScreen}. */
public class GuiAutoBreeder extends BasicStorageScreen<BlockEntityAutoBreeder, ContainerAutoBreeder> {

    public GuiAutoBreeder(ContainerAutoBreeder container, Inventory inv, Component title) {
        super(container, inv, title, container.tile.getContainerSize());
    }
}
