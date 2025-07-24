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
import reika.rotarycraft.base.EngineScreen;
import reika.rotarycraft.blockentities.engine.BlockEntityPerformanceEngine;
import reika.rotarycraft.gui.container.machine.inventory.PerformanceContainer;

public class GuiPerformance extends EngineScreen<BlockEntityPerformanceEngine, PerformanceContainer> {
    private final BlockEntityPerformanceEngine engine;

    public GuiPerformance(PerformanceContainer container, Inventory p5ep, Component title) {
        super(container, p5ep, title);
        engine = (BlockEntityPerformanceEngine) inventory.player.level().getBlockEntity(container.tile.getBlockPos());
        imageWidth = 176;
        imageHeight = 166;
    }

    @Override
    protected int getFuelBarXPos() {
        return 81;
    }

    @Override
    protected int getFuelBarYPos() {
        return 16;
    }

    @Override
    protected int getFuelBarXSize() {
        return 7;
    }

    @Override
    protected int getFuelBarYSize() {
        return 55;
    }

    @Override
    protected void renderBg(GuiGraphics poseStack, float par1, int par2, int par3) {
        super.renderBg(poseStack, par1, par2, par3);

        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;

        int i2 = engine.getWaterScaled(54);
        int i3 = engine.getTempScaled(54);
        ScreenUtils.drawTexturedModalRect(poseStack, j + 41, k + 71 - i2, 193, 55 - i2, 5, i2, 0);
        ScreenUtils.drawTexturedModalRect(poseStack, j + 128, k + 71 - i3, 177, 99 - i3, 9, i3, 0);

        int i1 = engine.getFuelScaled(54);
        ScreenUtils.drawTexturedModalRect(poseStack, j + 82, k + 71 - i1, 200, 55 - i1, 6, i1, 0);
        int i4 = engine.getAdditivesScaled(54);
        ScreenUtils.drawTexturedModalRect(poseStack, j + 89, k + 71 - i4, 207, 55 - i4, 6, i4, 0);
    }

    @Override
    protected String getGuiTexture() {
        return "perfgui";
    }
}
