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

import reika.rotarycraft.base.GuiPowerOnlyMachine;
import reika.rotarycraft.blockentities.processing.BlockEntityLavaSmeltery;
import reika.rotarycraft.gui.container.machine.inventory.ContainerBigFurnace;

public class GuiBigFurnace extends GuiPowerOnlyMachine<BlockEntityLavaSmeltery, ContainerBigFurnace> {
    private final BlockEntityLavaSmeltery te;

    public GuiBigFurnace(ContainerBigFurnace container, Inventory inv, Component title) {
        super(container, inv, title, 190, 207);
        te = (BlockEntityLavaSmeltery) inventory.player.level().getBlockEntity(container.tile.getBlockPos());
        inventory = inv;
    }

    /**
     * Draw the foreground layer (temperature read-out) in gui-relative space, matching the
     * legacy drawGuiContainerForegroundLayer. 26.1 routes this through extractLabels which is
     * already translated by (leftPos, topPos).
     */
    @Override
    protected void extractLabels(GuiGraphicsExtractor stack, int pMouseX, int pMouseY) {
        super.extractLabels(stack, pMouseX, pMouseY);

        int c = 0;
        if (te.getTemperature() >= 1000)
            c = 4;
        else if (te.getTemperature() >= 100)
            c = 2;
        api.drawCenteredStringNoShadow(stack, font, te.getTemperature() + "C", imageWidth - 13 - c, 6, 0xFF404040);

        // Panel-relative like 1.7.10's foreground layer (it sat in the untranslated background pass).
        int j = leftPos;
        int k = topPos;
        if (api.isMouseInBox(j + 172, j + 183, k + 17, k + 109, pMouseX, pMouseY))
            api.drawTooltipAt(stack, font, String.format("%d/%d", te.getLevel(), te.getCapacity()), pMouseX - j, pMouseY - k);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    @Override
    public void extractBackground(GuiGraphicsExtractor poseStack, int pX, int pY, float pPartialTick) {
        super.extractBackground(poseStack, pX, pY, pPartialTick);

        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;

        int i1 = te.getCookScaled(17);
        poseStack.blit(RenderPipelines.GUI_TEXTURED, getTextureIdentifier(), j + 7, k + 55, 0, 208, 162, i1, 256, 256);

        if (!te.isEmpty()) {
            int i2 = te.getLavaScaled(91);
            int x = 173;
            int y = 108 - i2 + 1;
            // 1.7.10 drew this in the foreground layer (panel-relative); here the panel origin is added.
            reika.dragonapi.libraries.rendering.ReikaLiquidRenderer.drawFluidIcon(poseStack,
                    net.minecraft.world.level.material.Fluids.LAVA, j + x, k + y, 10, i2);
        }
    }

    @Override
    protected String getGuiTexture() {
        return "bigfurngui";
    }
}
