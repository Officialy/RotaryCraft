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
import reika.rotarycraft.blockentities.processing.BlockEntityCrystallizer;
import reika.rotarycraft.gui.container.machine.inventory.ContainerCrystallizer;

/** Crystallizer GUI: fluid level + freeze progress + temperature vs freezing point. */
public class GuiCrystallizer extends MachineScreen<BlockEntityCrystallizer, ContainerCrystallizer> {

    private final BlockEntityCrystallizer crystal;

    public GuiCrystallizer(ContainerCrystallizer container, Inventory inv, Component title) {
        super(container, inv, title, 176, 166);
        crystal = (BlockEntityCrystallizer) inv.player.level().getBlockEntity(container.tile.getBlockPos());
        inventory = inv;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partial) {
        super.extractBackground(graphics, mouseX, mouseY, partial);
        int j = (Minecraft.getInstance().getWindow().getGuiScaledWidth() - imageWidth) / 2;
        int k = (Minecraft.getInstance().getWindow().getGuiScaledHeight() - imageHeight) / 2;

        // Tank level (blue) on the left.
        int h = crystal.getLiquidScaled(54);
        if (h > 0)
            graphics.fill(j + 18, k + 70 - h, j + 26, k + 70, 0xFF3060d0);
        // Freeze progress (cyan) between the slots.
        int w = crystal.getProgressScaled(24);
        if (w > 0)
            graphics.fill(j + 46, k + 34, j + 46 + w, k + 42, 0xFF60d0e0);
        // Temperature readout vs required freezing point.
        api.drawCenteredStringNoShadow(graphics, font,
                crystal.getTemperature() + "C / need <= " + crystal.getFreezingPoint() + "C",
                j + imageWidth / 2, k + 60, 0xff404040);
    }

    @Override
    protected void drawPowerTab(GuiGraphicsExtractor stack, int j, int k) {
        var loc = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/powertab.png");
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j, k + 4, 0, 4, 42, imageHeight - 4, 256, 256);
        long frac = crystal.MINPOWER > 0 ? (crystal.power * 29L) / crystal.MINPOWER : (crystal.power > 0 ? 29 : 0);
        if (frac > 29) frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 144, 0, 0, (int) frac, 4, 256, 256);
        frac = crystal.MINSPEED > 0 ? (crystal.omega * 29L) / crystal.MINSPEED : (crystal.omega > 0 ? 29 : 0);
        if (frac > 29) frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 84, 0, 0, (int) frac, 4, 256, 256);
        frac = crystal.MINTORQUE > 0 ? (crystal.torque * 29L) / crystal.MINTORQUE : (crystal.torque > 0 ? 29 : 0);
        if (frac > 29) frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 24, 0, 0, (int) frac, 4, 256, 256);
        api.drawCenteredStringNoShadow(stack, font, "Power:", imageWidth + j + 20, k + 9, 0xff000000);
        api.drawCenteredStringNoShadow(stack, font, "Speed:", imageWidth + j + 20, k + 69, 0xff000000);
        api.drawCenteredStringNoShadow(stack, font, "Torque:", imageWidth + j + 20, k + 129, 0xff000000);
    }

    @Override
    protected String getGuiTexture() {
        return "crystalgui";
    }
}
