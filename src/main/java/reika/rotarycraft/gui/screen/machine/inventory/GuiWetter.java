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
import reika.rotarycraft.blockentities.processing.BlockEntityWetter;
import reika.rotarycraft.gui.container.machine.ContainerWetter;

/** Wetter GUI: soak slot + tank level bar. */
public class GuiWetter extends MachineScreen<BlockEntityWetter, ContainerWetter> {

    private final BlockEntityWetter wetter;

    public GuiWetter(ContainerWetter container, Inventory inv, Component title) {
        super(container, inv, title, 176, 166);
        wetter = (BlockEntityWetter) inv.player.level().getBlockEntity(container.tile.getBlockPos());
        inventory = inv;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partial) {
        super.extractBackground(graphics, mouseX, mouseY, partial);
        int j = (Minecraft.getInstance().getWindow().getGuiScaledWidth() - imageWidth) / 2;
        int k = (Minecraft.getInstance().getWindow().getGuiScaledHeight() - imageHeight) / 2;

        // Tank level bar on the left (blue fill).
        int h = wetter.getCapacity() > 0 ? wetter.getTankLevel() * 54 / wetter.getCapacity() : 0;
        if (h > 0)
            graphics.fill(j + 18, k + 70 - h, j + 26, k + 70, 0xFF3060d0);
        if (api.isMouseInBox(j + 18, j + 26, k + 16, k + 70, mouseX, mouseY))
            api.drawTooltipAt(graphics, font, wetter.getTankLevel() + "/" + wetter.getCapacity() + " mB", mouseX, mouseY);
    }

    @Override
    protected void drawPowerTab(GuiGraphicsExtractor stack, int j, int k) {
        var loc = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/powertab.png");
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j, k + 4, 0, 4, 42, imageHeight - 4, 256, 256);
        long frac = wetter.MINPOWER > 0 ? (wetter.power * 29L) / wetter.MINPOWER : (wetter.power > 0 ? 29 : 0);
        if (frac > 29) frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 144, 0, 0, (int) frac, 4, 256, 256);
        frac = wetter.MINSPEED > 0 ? (wetter.omega * 29L) / wetter.MINSPEED : (wetter.omega > 0 ? 29 : 0);
        if (frac > 29) frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 84, 0, 0, (int) frac, 4, 256, 256);
        frac = wetter.MINTORQUE > 0 ? (wetter.torque * 29L) / wetter.MINTORQUE : (wetter.torque > 0 ? 29 : 0);
        if (frac > 29) frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 24, 0, 0, (int) frac, 4, 256, 256);
        api.drawCenteredStringNoShadow(stack, font, "Power:", imageWidth + j + 20, k + 9, 0xff000000);
        api.drawCenteredStringNoShadow(stack, font, "Speed:", imageWidth + j + 20, k + 69, 0xff000000);
        api.drawCenteredStringNoShadow(stack, font, "Torque:", imageWidth + j + 20, k + 129, 0xff000000);
    }

    @Override
    protected String getGuiTexture() {
        return "wettergui";
    }
}
