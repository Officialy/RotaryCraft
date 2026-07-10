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
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.MachineScreen;
import reika.rotarycraft.blockentities.processing.BlockEntityCompactor;
import reika.rotarycraft.gui.container.machine.inventory.ContainerCompactor;

/** Compactor GUI: 4-input column -> progress -> output, with temperature + pressure bars. */
public class GuiCompactor extends MachineScreen<BlockEntityCompactor, ContainerCompactor> {

    private final BlockEntityCompactor comp;

    public GuiCompactor(ContainerCompactor container, Inventory inv, Component title) {
        super(container, inv, title, 176, 166);
        comp = (BlockEntityCompactor) inv.player.level().getBlockEntity(container.tile.getBlockPos());
        inventory = inv;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partial) {
        super.extractBackground(graphics, mouseX, mouseY, partial);
        int j = (Minecraft.getInstance().getWindow().getGuiScaledWidth() - imageWidth) / 2;
        int k = (Minecraft.getInstance().getWindow().getGuiScaledHeight() - imageHeight) / 2;

        // Progress (legacy vertical-ish strip at x=46 -> tint fill toward the output slot).
        int i1 = comp.getCookProgressScaled(30);
        if (i1 > 0)
            graphics.fill(j + 46, k + 32, j + 46 + i1, k + 40, 0xFF3fbf3f);

        // Temperature bar (fills toward red) and pressure bar (fills toward blue), legacy right side.
        int i2 = comp.getTemperatureScaled(54);
        int col = 0xFF000000 | (Math.min(255, 60 + i2 * 4) << 16);
        if (i2 > 0)
            graphics.fill(j + 118, k + 70 - i2, j + 127, k + 70, col);
        if (api.isMouseInBox(j + 118, j + 127, k + 16, k + 70, mouseX, mouseY))
            api.drawTooltipAt(graphics, font, comp.getTemperature() + "C / " + BlockEntityCompactor.REQTEMP + "C", mouseX, mouseY);

        int i3 = comp.getPressureScaled(54);
        int col2 = 0xFF000000 | Math.min(255, 60 + i3 * 4);
        if (i3 > 0)
            graphics.fill(j + 147, k + 70 - i3, j + 151, k + 70, col2);
        if (api.isMouseInBox(j + 147, j + 151, k + 16, k + 70, mouseX, mouseY))
            api.drawTooltipAt(graphics, font, comp.getPressure() + " kPa / " + BlockEntityCompactor.REQPRESS + " kPa", mouseX, mouseY);
    }

    @Override
    protected void drawPowerTab(GuiGraphicsExtractor stack, int j, int k) {
        var loc = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/powertab.png");
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j, k + 4, 0, 4, 42, imageHeight - 4, 256, 256);
        long frac = comp.MINPOWER > 0 ? (comp.power * 29L) / comp.MINPOWER : (comp.power > 0 ? 29 : 0);
        if (frac > 29) frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 144, 0, 0, (int) frac, 4, 256, 256);
        frac = comp.MINSPEED > 0 ? (comp.omega * 29L) / comp.MINSPEED : (comp.omega > 0 ? 29 : 0);
        if (frac > 29) frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 84, 0, 0, (int) frac, 4, 256, 256);
        frac = comp.MINTORQUE > 0 ? (comp.torque * 29L) / comp.MINTORQUE : (comp.torque > 0 ? 29 : 0);
        if (frac > 29) frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 24, 0, 0, (int) frac, 4, 256, 256);
        api.drawCenteredStringNoShadow(stack, font, "Power:", imageWidth + j + 20, k + 9, 0xff000000);
        api.drawCenteredStringNoShadow(stack, font, "Speed:", imageWidth + j + 20, k + 69, 0xff000000);
        api.drawCenteredStringNoShadow(stack, font, "Torque:", imageWidth + j + 20, k + 129, 0xff000000);
    }

    @Override
    protected String getGuiTexture() {
        return "compactorgui";
    }
}
