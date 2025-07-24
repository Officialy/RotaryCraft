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

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.client.gui.ScreenUtils;

import reika.rotarycraft.base.GuiPowerOnlyMachine;
import reika.rotarycraft.blockentities.processing.BlockEntityLavaSmeltery;
import reika.rotarycraft.gui.container.machine.inventory.ContainerBigFurnace;

public class GuiBigFurnace extends GuiPowerOnlyMachine<BlockEntityLavaSmeltery, ContainerBigFurnace> {
    private final BlockEntityLavaSmeltery te;

    public GuiBigFurnace(ContainerBigFurnace container, Inventory inv, Component title) {
        super(container, inv, title);
        te = (BlockEntityLavaSmeltery) inventory.player.level().getBlockEntity(container.tile.getBlockPos());
        inventory = inv;
        imageWidth = 190;
        imageHeight = 207;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(PoseStack stack, int a, int b) {

    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    @Override
    protected void renderBg(GuiGraphics poseStack, float pPartialTick, int pX, int pY) {
        super.renderBg(poseStack, pPartialTick, pX, pY);

        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;

        int i1 = te.getCookScaled(17);
        ScreenUtils.drawTexturedModalRect(poseStack, j + 7, k + 55, 0, 208, 162, i1, 0);

//drawGuiContainerForegroundLayer
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;

        int c = 0;
        if (te.getTemperature() >= 1000)
            c = 4;
        else if (te.getTemperature() >= 100)
            c = 2;
        api.drawCenteredStringNoShadow(poseStack, font, te.getTemperature() + "C", imageWidth - 13 - c, 6, 4210752);

        if (!te.isEmpty()) {
            int i2 = te.getLavaScaled(91);
            int x = 173;
            int y = 108 - i2 + 1;
//       todo     GL11.glColor4f(1, 1, 1, 1);
//            IIcon ico = Fluids.LAVA.getStillIcon();
//            ReikaLiquidRenderer.bindFluidTexture(Fluids.LAVA);
//            this.drawTexturedModelRectFromIcon(x, y, ico, 10, i2);
        }
        if (api.isMouseInBox(j + 172, j + 183, k + 17, k + 109, pX, pY)) {
            int mx = pX;
            int my = pY;
            api.drawTooltipAt(poseStack, font, String.format("%d/%d", te.getLevel(), te.getCapacity()), mx - j, my - k);
        }


    }

    @Override
    protected String getGuiTexture() {
        return "bigfurngui";
    }
}
