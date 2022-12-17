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

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import reika.rotarycraft.base.NonPoweredMachineScreen;
import reika.rotarycraft.blockentities.weaponry.BlockEntityLandmine;
import reika.rotarycraft.gui.container.machine.ContainerSorter;
import reika.rotarycraft.gui.container.machine.inventory.LandmineMenu;

public class GuiLandmine extends NonPoweredMachineScreen<BlockEntityLandmine, LandmineMenu> {

    public GuiLandmine(LandmineMenu container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected String getGuiTexture() {
        return "landminegui";
    }
}
