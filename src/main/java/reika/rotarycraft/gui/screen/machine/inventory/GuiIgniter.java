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
import reika.rotarycraft.blockentities.level.BlockEntityIgniter;
import reika.rotarycraft.gui.container.machine.inventory.ContainerIgniter;

/** Firestarter GUI: 9x2 fuel grid + a temperature readout, on the shared basic-storage background. */
public class GuiIgniter extends MachineScreen<BlockEntityIgniter, ContainerIgniter> {

    private final BlockEntityIgniter ign;

    public GuiIgniter(ContainerIgniter container, Inventory inv, Component title) {
        super(container, inv, title, 176, 176);
        ign = (BlockEntityIgniter) inv.player.level().getBlockEntity(container.tile.getBlockPos());
        inventory = inv;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partial) {
        super.extractBackground(graphics, mouseX, mouseY, partial);
        int j = (Minecraft.getInstance().getWindow().getGuiScaledWidth() - imageWidth) / 2;
        int k = (Minecraft.getInstance().getWindow().getGuiScaledHeight() - imageHeight) / 2;
        int h = ign.getTemperatureScaled(40);
        int col = 0xFF000000 | (Math.min(255, 60 + h * 4) << 16);
        if (h > 0)
            graphics.fill(j + 6, k + 62 - h, j + 12, k + 62, col);
        if (api.isMouseInBox(j + 6, j + 12, k + 22, k + 62, mouseX, mouseY))
            api.drawTooltipAt(graphics, font, ign.getTemperature() + "C", mouseX, mouseY);
    }

    @Override
    protected void drawPowerTab(GuiGraphicsExtractor stack, int j, int k) {
        var loc = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/powertab.png");
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j, k + 4, 0, 4, 42, imageHeight - 4, 256, 256);

        long frac = ign.MINPOWER > 0 ? (ign.power * 29L) / ign.MINPOWER : (ign.power > 0 ? 29 : 0);
        if (frac > 29) frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 144, 0, 0, (int) frac, 4, 256, 256);

        frac = ign.MINSPEED > 0 ? (ign.omega * 29L) / ign.MINSPEED : (ign.omega > 0 ? 29 : 0);
        if (frac > 29) frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 84, 0, 0, (int) frac, 4, 256, 256);

        frac = ign.MINTORQUE > 0 ? (ign.torque * 29L) / ign.MINTORQUE : (ign.torque > 0 ? 29 : 0);
        if (frac > 29) frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 24, 0, 0, (int) frac, 4, 256, 256);

        api.drawCenteredStringNoShadow(stack, font, "Power:", imageWidth + j + 20, k + 9, 0xff000000);
        api.drawCenteredStringNoShadow(stack, font, "Speed:", imageWidth + j + 20, k + 69, 0xff000000);
        api.drawCenteredStringNoShadow(stack, font, "Torque:", imageWidth + j + 20, k + 129, 0xff000000);
    }

    @Override
    protected String getGuiTexture() {
        return "basicstorage";
    }
}
