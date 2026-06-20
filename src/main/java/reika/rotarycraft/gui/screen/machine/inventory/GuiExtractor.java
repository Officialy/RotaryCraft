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
import reika.rotarycraft.blockentities.processing.BlockEntityExtractor;
import reika.rotarycraft.gui.container.machine.inventory.ContainerExtractor;
import reika.rotarycraft.registry.ConfigRegistry;

public class GuiExtractor extends MachineScreen<BlockEntityExtractor, ContainerExtractor> {

    private final BlockEntityExtractor ext;

    public GuiExtractor(ContainerExtractor container, Inventory inv, Component title) {
        super(container, inv, title);
        ext = (BlockEntityExtractor) inv.player.level().getBlockEntity(container.tile.getBlockPos());
        inventory = inv;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor poseStack, int mouseX, int mouseY, float par1) {
        super.extractBackground(poseStack, mouseX, mouseY, par1);
        int scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int scaleHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        int j = (scaledWidth - imageWidth) / 2;
        int k = (scaleHeight - imageHeight) / 2;

        // four stage arrows, each filling downward as the stage cooks (28px arrows for the
        // middle stages, 32px for the drill/dry stages — matches extractorgui.png)
        int i1 = Math.min(32, ext.getCookProgressScaled(32, 0));
        int i2 = Math.min(28, ext.getCookProgressScaled(28, 1));
        int i3 = Math.min(28, ext.getCookProgressScaled(28, 2));
        int i4 = Math.min(32, ext.getCookProgressScaled(32, 3));
        poseStack.blit(RenderPipelines.GUI_TEXTURED, getTextureIdentifier(), j + 29, k + 34, 176, 48, 10, i1, 256, 256);
        poseStack.blit(RenderPipelines.GUI_TEXTURED, getTextureIdentifier(), j + 65, k + 34, 176, 48, 10, i2, 256, 256);
        poseStack.blit(RenderPipelines.GUI_TEXTURED, getTextureIdentifier(), j + 101, k + 34, 176, 48, 10, i3, 256, 256);
        poseStack.blit(RenderPipelines.GUI_TEXTURED, getTextureIdentifier(), j + 137, k + 34, 176, 48, 10, i4, 256, 256);

        if (ConfigRegistry.EXTRACTORMAINTAIN.getState() && api.isMouseInBox(j + 25, j + 43, k + 33, k + 51, mouseX, mouseY)) {
            api.drawTooltipAt(poseStack, font, String.format("Drill Status: %d%%", ext.getDrillLifeScaled(100)), mouseX, mouseY);
        }
    }

    @Override
    protected void drawPowerTab(GuiGraphicsExtractor stack, int j, int k) {
        var loc = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/powertab.png");
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j, k + 4, 0, 4, 42, imageHeight - 4, 256, 256);

        long frac = (ext.power * 29L) / Math.max(1, ext.MINPOWER);
        if (frac > 29)
            frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 144, 0, 0, (int) frac, 4, 256, 256);

        frac = (ext.omega * 29L) / Math.max(1, ext.MINSPEED);
        if (frac > 29)
            frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 84, 0, 0, (int) frac, 4, 256, 256);

        frac = (ext.torque * 29L) / Math.max(1, ext.MINTORQUE);
        if (frac > 29)
            frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 24, 0, 0, (int) frac, 4, 256, 256);

        api.drawCenteredStringNoShadow(stack, font, "Power:", imageWidth + j + 20, k + 9, 0xff000000);
        api.drawCenteredStringNoShadow(stack, font, "Speed:", imageWidth + j + 20, k + 69, 0xff000000);
        api.drawCenteredStringNoShadow(stack, font, "Torque:", imageWidth + j + 20, k + 129, 0xff000000);
    }

    @Override
    protected String getGuiTexture() {
        return "extractorgui";
    }
}
