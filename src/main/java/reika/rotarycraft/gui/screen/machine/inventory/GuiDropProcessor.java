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
import reika.rotarycraft.blockentities.processing.BlockEntityDropProcessor;
import reika.rotarycraft.gui.container.machine.inventory.ContainerDropProcessor;

/** Drop-processor GUI: input -> progress arrow -> output, + the overflow-buffer count. */
public class GuiDropProcessor extends MachineScreen<BlockEntityDropProcessor, ContainerDropProcessor> {

    private final BlockEntityDropProcessor drops;

    public GuiDropProcessor(ContainerDropProcessor container, Inventory inv, Component title) {
        super(container, inv, title, 176, 166);
        drops = (BlockEntityDropProcessor) inv.player.level().getBlockEntity(container.tile.getBlockPos());
        inventory = inv;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partial) {
        super.extractBackground(graphics, mouseX, mouseY, partial);
        int j = (Minecraft.getInstance().getWindow().getGuiScaledWidth() - imageWidth) / 2;
        int k = (Minecraft.getInstance().getWindow().getGuiScaledHeight() - imageHeight) / 2;

        // Progress arrow between the slots (legacy blit from the texture margin -> tint fill).
        int w = drops.getCookScaled(24);
        if (w > 0)
            graphics.fill(j + 75, k + 34, j + 75 + w, k + 50, 0xFF3fbf3f);

        if (drops.overflowCount > 0)
            api.drawCenteredStringNoShadow(graphics, font, "Buffered: " + drops.overflowCount, j + 88, k + 60, 0xff404040);
    }

    @Override
    protected void drawPowerTab(GuiGraphicsExtractor stack, int j, int k) {
        var loc = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/powertab.png");
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j, k + 4, 0, 4, 42, imageHeight - 4, 256, 256);
        long frac = drops.MINPOWER > 0 ? (drops.power * 29L) / drops.MINPOWER : (drops.power > 0 ? 29 : 0);
        if (frac > 29) frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 144, 0, 0, (int) frac, 4, 256, 256);
        frac = drops.MINSPEED > 0 ? (drops.omega * 29L) / drops.MINSPEED : (drops.omega > 0 ? 29 : 0);
        if (frac > 29) frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 84, 0, 0, (int) frac, 4, 256, 256);
        frac = drops.MINTORQUE > 0 ? (drops.torque * 29L) / drops.MINTORQUE : (drops.torque > 0 ? 29 : 0);
        if (frac > 29) frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 24, 0, 0, (int) frac, 4, 256, 256);
        api.drawCenteredStringNoShadow(stack, font, "Power:", imageWidth + j + 20, k + 9, 0xff000000);
        api.drawCenteredStringNoShadow(stack, font, "Speed:", imageWidth + j + 20, k + 69, 0xff000000);
        api.drawCenteredStringNoShadow(stack, font, "Torque:", imageWidth + j + 20, k + 129, 0xff000000);
    }

    @Override
    protected String getGuiTexture() {
        return "dropgui";
    }
}
