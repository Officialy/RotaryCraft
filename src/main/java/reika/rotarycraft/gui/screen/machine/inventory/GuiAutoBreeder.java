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

import reika.rotarycraft.base.NonPoweredMachineScreen;
import reika.rotarycraft.blockentities.farming.BlockEntityAutoBreeder;
import reika.rotarycraft.gui.container.machine.inventory.ContainerAutoBreeder;

/** Auto breeder feed inventory on the shared storage background. */
public class GuiAutoBreeder extends NonPoweredMachineScreen<BlockEntityAutoBreeder, ContainerAutoBreeder> {

    public GuiAutoBreeder(ContainerAutoBreeder container, Inventory inv, Component title) {
        super(container, inv, title, 176, 180);
        inventory = inv;
    }

    @Override
    protected String getGuiTexture() {
        return "basicstorage";
    }
}
