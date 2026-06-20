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
import reika.rotarycraft.blockentities.level.BlockEntityAerosolizer;
import reika.rotarycraft.gui.container.machine.inventory.ContainerAerosolizer;

public class GuiAerosolizer extends GuiPowerOnlyMachine<BlockEntityAerosolizer, ContainerAerosolizer> {

    private final BlockEntityAerosolizer aero;

    public GuiAerosolizer(ContainerAerosolizer container, Inventory inv, Component title) {
        super(container, inv, title);
        aero = container.tile;
        inventory = inv;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;

        // 3x3 grid of potion tanks: a coloured fill bar (scaled by stored level) with the level
        // printed over it, matching the original aerosolizergui.png layout.
        for (int i = 0; i < 3; i++) {
            for (int x = 0; x < 3; x++) {
                int slot = 3 * i + x;
                int amount = aero.getPotionLevel(slot) / 4;
                int bx = j + 62 + 18 * x;
                int by = k + 17 + 18 * i;
                api.fillBar(graphics, bx, by, 16, by + 16, aero.getPotionColor(slot), amount, 16, true);
                api.drawCenteredStringNoShadow(graphics, font, String.format("%d", aero.getPotionLevel(slot)), bx + 8, by + 5, 0x000000);
            }
        }
    }

    @Override
    protected String getGuiTexture() {
        return "aerosolizergui";
    }
}
