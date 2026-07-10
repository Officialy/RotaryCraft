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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import reika.rotarycraft.base.NonPoweredMachineScreen;
import reika.rotarycraft.blockentities.processing.BlockEntityDryingBed;
import reika.rotarycraft.gui.container.machine.inventory.ContainerDryingBed;

/** Drying bed GUI: dried-product slot + tank level bar (unpowered machine). */
public class GuiDryingBed extends NonPoweredMachineScreen<BlockEntityDryingBed, ContainerDryingBed> {

    private final BlockEntityDryingBed bed;

    public GuiDryingBed(ContainerDryingBed container, Inventory inv, Component title) {
        super(container, inv, title, 176, 166);
        bed = (BlockEntityDryingBed) inv.player.level().getBlockEntity(container.tile.getBlockPos());
        inventory = inv;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partial) {
        super.extractBackground(graphics, mouseX, mouseY, partial);
        int j = (Minecraft.getInstance().getWindow().getGuiScaledWidth() - imageWidth) / 2;
        int k = (Minecraft.getInstance().getWindow().getGuiScaledHeight() - imageHeight) / 2;

        int h = bed.getCapacity() > 0 ? bed.getFluidLevel() * 54 / bed.getCapacity() : 0;
        if (h > 0)
            graphics.fill(j + 18, k + 70 - h, j + 26, k + 70, 0xFF3060d0);
        if (api.isMouseInBox(j + 18, j + 26, k + 16, k + 70, mouseX, mouseY))
            api.drawTooltipAt(graphics, font, bed.getFluidLevel() + "/" + bed.getCapacity() + " mB", mouseX, mouseY);
    }

    @Override
    protected String getGuiTexture() {
        return "basic_gui_oneslot";
    }
}
