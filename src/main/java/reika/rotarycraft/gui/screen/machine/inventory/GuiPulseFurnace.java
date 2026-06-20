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
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.MachineScreen;
import reika.rotarycraft.blockentities.processing.BlockEntityPulseFurnace;
import reika.rotarycraft.gui.container.machine.inventory.ContainerPulseFurnace;

public class GuiPulseFurnace extends MachineScreen<BlockEntityPulseFurnace, ContainerPulseFurnace> {

    private final BlockEntityPulseFurnace puls;

    public GuiPulseFurnace(ContainerPulseFurnace container, Inventory inv, Component title) {
        super(container, inv, title);
        puls = container.tile;
        inventory = inv;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;
        Identifier tex = getTextureIdentifier();

        int i1 = puls.getCookProgressScaled(10);   // smelt arrow
        int i2 = puls.getFuelScaled(52);            // fuel column
        int i3 = puls.getWaterScaled(52);           // water column
        int i4 = puls.getTempScaled(54);            // temperature bar
        if (i4 < 9)
            i4 = 9;
        int i5 = puls.getFireScaled(38);            // twin flame bars
        int i6 = puls.getAccelerantScaled(52);      // oxygen column

        graphics.blit(RenderPipelines.GUI_TEXTURED, tex, j + 131, k + 36, 215, 55, 4, i1, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, tex, j + 91, k + 68 - i2, 248, 53 - i2, 5, i2, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, tex, j + 59, k + 68 - i3, 199, 53 - i3, 5, i3, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, tex, j + 20, k + 70 - i4, 176, 55 - i4, 11, i4, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, tex, j + 115, k + 61 - i5, 177, 95 - i5, 9, i5, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, tex, j + 142, k + 61 - i5, 204, 95 - i5, 9, i5, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, tex, j + 160, k + 68 - i6, 227, 53 - i6, 5, i6, 256, 256);

        // Tooltips: fuel / temperature / oxygen columns.
        if (api.isMouseInBox(j + 90, j + 96, k + 15, k + 68, mouseX, mouseY))
            api.drawTooltipAt(graphics, font, String.format("%d/%d", puls.getFuel(), BlockEntityPulseFurnace.MAXFUEL), mouseX, mouseY);
        if (api.isMouseInBox(j + 20, j + 30, k + 15, k + 70, mouseX, mouseY))
            api.drawTooltipAt(graphics, font, String.format("%dC", puls.temperature), mouseX, mouseY);
        if (api.isMouseInBox(j + 159, j + 165, k + 15, k + 68, mouseX, mouseY))
            api.drawTooltipAt(graphics, font, String.format("%d/%d", puls.getAccelerant(), puls.getAccelerantCapacity()), mouseX, mouseY);
    }

    @Override
    protected void drawPowerTab(GuiGraphicsExtractor stack, int j, int k) {
        var loc = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/powertab.png");
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j, k + 4, 0, 4, 42, imageHeight - 4, 256, 256);

        long frac = (puls.power * 29L) / puls.MINPOWER;
        if (frac > 29)
            frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 144, 0, 0, (int) frac, 4, 256, 256);

        frac = (puls.omega * 29L) / puls.MINSPEED;
        if (frac > 29)
            frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 84, 0, 0, (int) frac, 4, 256, 256);

        frac = (puls.torque * 29L) / puls.MINTORQUE;
        if (frac > 29)
            frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 24, 0, 0, (int) frac, 4, 256, 256);

        api.drawCenteredStringNoShadow(stack, font, "Power:", imageWidth + j + 20, k + 9, 0xff000000);
        api.drawCenteredStringNoShadow(stack, font, "Speed:", imageWidth + j + 20, k + 69, 0xff000000);
        api.drawCenteredStringNoShadow(stack, font, "Torque:", imageWidth + j + 20, k + 129, 0xff000000);
    }

    @Override
    protected String getGuiTexture() {
        return "pulsejetgui";
    }
}
