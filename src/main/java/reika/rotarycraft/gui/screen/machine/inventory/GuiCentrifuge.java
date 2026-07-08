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
import net.minecraft.world.level.material.Fluid;

import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.MachineScreen;
import reika.rotarycraft.blockentities.processing.BlockEntityCentrifuge;
import reika.rotarycraft.gui.container.machine.inventory.ContainerCentrifuge;
import reika.rotarycraft.registry.RotaryFluids;

public class GuiCentrifuge extends MachineScreen<BlockEntityCentrifuge, ContainerCentrifuge> {

    private final BlockEntityCentrifuge cent;

    public GuiCentrifuge(ContainerCentrifuge container, Inventory inv, Component title) {
        super(container, inv, title);
        cent = (BlockEntityCentrifuge) inv.player.level().getBlockEntity(container.tile.getBlockPos());
        inventory = inv;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor poseStack, int mouseX, int mouseY, float partial) {
        super.extractBackground(poseStack, mouseX, mouseY, partial);
        int scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int scaledHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        int j = (scaledWidth - imageWidth) / 2;
        int k = (scaledHeight - imageHeight) / 2;

        // Progress bar: widening sweep at (45, 27), texture region (178, 1).
        int i3 = Math.min(37, cent.getProgressScaled(37));
        poseStack.blit(RenderPipelines.GUI_TEXTURED, getTextureIdentifier(), j + 45, k + 27, 178, 1, i3, 37, 256, 256);

        // Byproduct tank column at (152, 7..77), drawn as a tinted fill (the legacy GUI drew the
        // fluid's atlas icon; the 26.x extractor has no sprite blit, so tint per fluid instead).
        int i2 = cent.getLiquidScaled(70);
        if (i2 > 0)
            poseStack.fill(j + 152, k + 77 - i2, j + 152 + 16, k + 77, fluidColor(cent.getFluid()));

        if (api.isMouseInBox(j + 151, j + 168, k + 7, k + 78, mouseX, mouseY)) {
            if (cent.getFluidLevel() > 0)
                api.drawTooltipAt(poseStack, font, String.format("%d/%d mB", cent.getFluidLevel(), BlockEntityCentrifuge.CAPACITY), mouseX, mouseY);
            else
                api.drawTooltipAt(poseStack, font, String.format("0/%d mB", BlockEntityCentrifuge.CAPACITY), mouseX, mouseY);
        }
    }

    private static int fluidColor(Fluid f) {
        if (f == RotaryFluids.LUBRICANT.get())
            return 0xFFC08020;
        return 0xFF20A0C0;
    }

    @Override
    protected void drawPowerTab(GuiGraphicsExtractor stack, int j, int k) {
        var loc = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/powertab.png");
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j, k + 4, 0, 4, 42, imageHeight - 4, 256, 256);

        long frac = (cent.power * 29L) / cent.MINPOWER;
        if (frac > 29)
            frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 144, 0, 0, (int) frac, 4, 256, 256);

        frac = (cent.omega * 29L) / cent.MINSPEED;
        if (frac > 29)
            frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 84, 0, 0, (int) frac, 4, 256, 256);

        frac = (cent.torque * 29L) / cent.MINTORQUE;
        if (frac > 29)
            frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 24, 0, 0, (int) frac, 4, 256, 256);

        api.drawCenteredStringNoShadow(stack, font, "Power:", imageWidth + j + 20, k + 9, 0xff000000);
        api.drawCenteredStringNoShadow(stack, font, "Speed:", imageWidth + j + 20, k + 69, 0xff000000);
        api.drawCenteredStringNoShadow(stack, font, "Torque:", imageWidth + j + 20, k + 129, 0xff000000);
    }

    @Override
    protected String getGuiTexture() {
        return "centrifugegui";
    }
}
