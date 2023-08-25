/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.gui.screen.machine;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.gui.ScreenUtils;
import reika.rotarycraft.base.EngineScreen;
import reika.rotarycraft.blockentities.engine.BlockEntitySteamEngine;
import reika.rotarycraft.gui.container.machine.SteamContainer;

public class SteamScreen extends EngineScreen<BlockEntitySteamEngine, SteamContainer> {

    private final BlockEntitySteamEngine steam;

    public SteamScreen(SteamContainer container, Inventory inv, Component text) { //BlockEntitySteamEngine te
        super(container, inv, text);
        steam = (BlockEntitySteamEngine) inv.player.level().getBlockEntity(container.tile.getBlockPos());
        imageWidth = 176;
        imageHeight = 79;
        inventory = inv;
    }

    @Override
    protected void renderBg(GuiGraphics poseStack, float pPartialTick, int pX, int pY) {
        super.renderBg(poseStack, pPartialTick, pX, pY);
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;

        int i2 = steam.getWaterScaled(54);
        int i3 = steam.getTempScaled(54);

        ScreenUtils.drawTexturedModalRect(poseStack, j + 49, k + 71 - i2, 193, 55 - i2, 5, i2, 0);
        ScreenUtils.drawTexturedModalRect(poseStack, j + 119, k + 71 - i3, 177, 99 - i3, 9, i3, 0); //todo texture for these two??
    }

    @Override
    public boolean labelInventory() {
        return false;
    }

    @Override
    protected String getGuiTexture() {
        return "steamgui";
    }

    @Override
    protected int getFuelBarXPos() {
        return 48;
    }

    @Override
    protected int getFuelBarYPos() {
        return 16;
    }

    @Override
    protected int getFuelBarXSize() {
        return 6;
    }

    @Override
    protected int getFuelBarYSize() {
        return 55;
    }

}
