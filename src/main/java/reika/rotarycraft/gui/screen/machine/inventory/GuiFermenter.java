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
import reika.rotarycraft.blockentities.production.BlockEntityFermenter;
import reika.rotarycraft.gui.container.machine.inventory.ContainerFermenter;

public class GuiFermenter extends MachineScreen<BlockEntityFermenter, ContainerFermenter> {

    private final BlockEntityFermenter ferm;

    public GuiFermenter(ContainerFermenter container, Inventory inv, Component title) {
        super(container, inv, title);
        ferm = (BlockEntityFermenter) inv.player.level().getBlockEntity(container.tile.getBlockPos());
        inventory = inv;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor poseStack, int mouseX, int mouseY, float par1) {
        super.extractBackground(poseStack, mouseX, mouseY, par1);
        int scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int scaleHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        int j = (scaledWidth - imageWidth) / 2;
        int k = (scaleHeight - imageHeight) / 2;

        int i1 = ferm.getCookProgressScaled(48);
        poseStack.blit(RenderPipelines.GUI_TEXTURED, getTextureIdentifier(), j + 79, k + 34, 176, 14, i1 + 1, 16, 256, 256);

        int i2 = ferm.getTemperatureScaled(54);
        poseStack.blit(RenderPipelines.GUI_TEXTURED, getTextureIdentifier(), j + 24, k + 70 - i2, 177, 86 - i2, 9, i2, 256, 256);

        if (api.isMouseInBox(j + 23, j + 33, k + 16, k + 70, mouseX, mouseY)) {
            api.drawTooltipAt(poseStack, font, String.format("%dC", ferm.getTemperature()), mouseX, mouseY);
        }
    }

    @Override
    protected void drawPowerTab(GuiGraphicsExtractor stack, int j, int k) {
        var loc = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/powertab.png");
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j, k + 4, 0, 4, 42, imageHeight - 4, 256, 256);

        long frac = (ferm.power * 29L) / Math.max(1, ferm.MINPOWER);
        if (frac > 29)
            frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 144, 0, 0, (int) frac, 4, 256, 256);

        frac = (ferm.omega * 29L) / Math.max(1, ferm.MINSPEED);
        if (frac > 29)
            frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 84, 0, 0, (int) frac, 4, 256, 256);

        frac = (ferm.torque * 29L) / Math.max(1, ferm.MINTORQUE);
        if (frac > 29)
            frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 24, 0, 0, (int) frac, 4, 256, 256);

        api.drawCenteredStringNoShadow(stack, font, "Power:", imageWidth + j + 20, k + 9, 0xff000000);
        api.drawCenteredStringNoShadow(stack, font, "Speed:", imageWidth + j + 20, k + 69, 0xff000000);
        api.drawCenteredStringNoShadow(stack, font, "Torque:", imageWidth + j + 20, k + 129, 0xff000000);
    }

    @Override
    protected String getGuiTexture() {
        return "fermentergui";
    }
}
