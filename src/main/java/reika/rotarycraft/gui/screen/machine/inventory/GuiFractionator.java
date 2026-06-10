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
import net.minecraft.world.entity.player.Inventory;
import reika.dragonapi.instantiable.rendering.SubdividedProgressBar;
import reika.rotarycraft.base.GuiPowerOnlyMachine;
import reika.rotarycraft.blockentities.production.BlockEntityFractionator;
import reika.rotarycraft.gui.container.machine.inventory.ContainerFractionator;

/**
 * 26.1 port of the 1.7 GuiFractionator. Faithful to the legacy layout:
 *   - Output fluid bar (jet fuel) on the far right, atlas region {@code (179, 55-y, 8, y)}.
 *   - Input fluid bar (ethanol) right of centre, atlas region {@code (189, 55-y, 8, y)}.
 *   - Pressure bar (2 px wide), atlas region {@code (224, 55-y, 2, y)}.
 *   - Mix-progress made up of 5 {@link SubdividedProgressBar} sections that snake across the
 *     top half of the GUI in the order they fill (ethanol intake → mix chamber → riser →
 *     condenser → outlet).
 *   - Foreground tooltips on each fluid bar + the pressure bar.
 */
public class GuiFractionator extends GuiPowerOnlyMachine<BlockEntityFractionator, ContainerFractionator> {

    private final BlockEntityFractionator fct;

    /**
     * Five-section progress bar matching the legacy implementation:
     *   section 0: 18 px wide (intake)
     *   section 1: 37 px wide (mix chamber)
     *   section 2:  8 px wide, with a 0.4 fill-shift (riser — fills early at low pressure)
     *   section 3:  8 px wide (condenser)
     *   section 4:  9 px wide, 0.1 fill-shift (outlet — finishes the cycle just before
     *               the jet-fuel level ticks up)
     */
    private final SubdividedProgressBar bar = new SubdividedProgressBar()
            .addSection(18)
            .addSection(37)
            .addSection(8, 0.4F)
            .addSection(8)
            .addSection(9, 0.1F);

    public GuiFractionator(ContainerFractionator container, Inventory inv, Component title) {
        super(container, inv, title);
        fct = (BlockEntityFractionator) inv.player.level().getBlockEntity(container.tile.getBlockPos());
        inventory = inv;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor poseStack, int pX, int pY, float pPartialTick) {
        super.extractBackground(poseStack, pX, pY, pPartialTick);

        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;

        // Jet-fuel output bar (rightmost, 8 px wide, fills from bottom).
        int i2 = fct.getFuelScaled(54);
        poseStack.blit(RenderPipelines.GUI_TEXTURED, getTextureIdentifier(),
                j + 151, k + 70 - i2, 179, 55 - i2, 8, i2, 256, 256);

        // Ethanol input bar.
        int i1 = fct.getEthanolScaled(54);
        poseStack.blit(RenderPipelines.GUI_TEXTURED, getTextureIdentifier(),
                j + 124, k + 70 - i1, 189, 55 - i1, 8, i1, 256, 256);

        // Pressure bar (2 px wide).
        int i0 = fct.getPressureScaled(54);
        poseStack.blit(RenderPipelines.GUI_TEXTURED, getTextureIdentifier(),
                j + 134, k + 70 - i0, 224, 55 - i0, 2, i0, 256, 256);

        // 5-section mix-progress strip.
        bar.setTick(fct.mixTime, BlockEntityFractionator.OPERATION_TIME);
        int v1 = bar.getScaled(0);
        int v2 = bar.getScaled(1);
        int v3 = bar.getScaled(2);
        int v4 = bar.getScaled(3);
        int v5 = bar.getScaled(4);

        poseStack.blit(RenderPipelines.GUI_TEXTURED, getTextureIdentifier(),
                j + 34, k + 17, 178, 59, v1, 47, 256, 256);
        poseStack.blit(RenderPipelines.GUI_TEXTURED, getTextureIdentifier(),
                j + 70, k + 17, 197, 59, v2, 47, 256, 256);
        // Section 2 fills vertically (legacy: drawTexturedModalRect(j+107, k+53-v3, 212, 39-v3, 8, v3)).
        poseStack.blit(RenderPipelines.GUI_TEXTURED, getTextureIdentifier(),
                j + 107, k + 53 - v3, 212, 39 - v3, 8, v3, 256, 256);
        poseStack.blit(RenderPipelines.GUI_TEXTURED, getTextureIdentifier(),
                j + 115, k + 17, 235, 59, v4, 34, 256, 256);
        poseStack.blit(RenderPipelines.GUI_TEXTURED, getTextureIdentifier(),
                j + 139, k + 18, 200, 1, v5, 52, 256, 256);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor stack, int mouseX, int mouseY) {
        super.extractLabels(stack, mouseX, mouseY);
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;

        // Tooltip on the jet-fuel output bar — shows current / max.
        if (api.isMouseInBox(j + 150, j + 159, k + 15, k + 70, mouseX, mouseY)) {
            api.drawTooltipAt(stack, font,
                    String.format("Jet Fuel: %d / %d mB", fct.getOutputLevel(), BlockEntityFractionator.CAPACITY),
                    mouseX - j, mouseY - k);
        }
        // Tooltip on the ethanol input bar.
        if (api.isMouseInBox(j + 123, j + 132, k + 15, k + 70, mouseX, mouseY)) {
            api.drawTooltipAt(stack, font,
                    String.format("Ethanol: %d / %d mB", fct.getInputLevel(), fct.getInputCapacity()),
                    mouseX - j, mouseY - k);
        }
        // Tooltip on the pressure bar.
        if (api.isMouseInBox(j + 133, j + 136, k + 15, k + 70, mouseX, mouseY)) {
            api.drawTooltipAt(stack, font,
                    String.format("Pressure: %d / %d kPa  (yield ×%.2f)", fct.getPressure(),
                            BlockEntityFractionator.MAX_PRESSURE, fct.getYieldRatio()),
                    mouseX - j, mouseY - k);
        }
    }

    @Override
    protected String getGuiTexture() {
        return "fractiongui3b";
    }
}
