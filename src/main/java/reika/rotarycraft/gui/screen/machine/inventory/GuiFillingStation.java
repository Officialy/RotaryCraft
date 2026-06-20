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

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import reika.rotarycraft.base.GuiPowerOnlyMachine;
import reika.rotarycraft.blockentities.auxiliary.BlockEntityFillingStation;
import reika.rotarycraft.gui.container.machine.inventory.ContainerFillingStation;

public class GuiFillingStation extends GuiPowerOnlyMachine<BlockEntityFillingStation, ContainerFillingStation> {

    /** Liquid fill colour (opaque amber) for the tank bar. */
    private static final int FUEL_COLOR = 0xFFD9A441;

    private final BlockEntityFillingStation fillingStation;

    public GuiFillingStation(ContainerFillingStation container, Inventory inv, Component title) {
        super(container, inv, title, 176, 187);
        fillingStation = container.tile;
        inventory = inv;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;

        if (!fillingStation.isEmpty()) {
            int h = fillingStation.getLiquidScaled(66);
            graphics.fill(j + 82, k + 87 - h, j + 94, k + 87, FUEL_COLOR);
        }
        if (api.isMouseInBox(j + 81, j + 94, k + 20, k + 87, mouseX, mouseY))
            api.drawTooltipAt(graphics, font, String.format("%d/%d mB", fillingStation.getFluidLevel(), BlockEntityFillingStation.CAPACITY), mouseX, mouseY);
    }

    @Override
    protected String getGuiTexture() {
        return "fillingstationgui";
    }
}
