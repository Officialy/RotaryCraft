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

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import reika.rotarycraft.base.EngineScreen;
import reika.rotarycraft.blockentities.engine.BlockEntitySteamEngine;
import reika.rotarycraft.gui.container.machine.SteamContainer;

public class SteamScreen extends EngineScreen<BlockEntitySteamEngine, SteamContainer> {

    private final BlockEntitySteamEngine steam;

    public SteamScreen(SteamContainer container, Inventory inv, Component text) { //BlockEntitySteamEngine te
        super(container, inv, text, 176, 79);
        steam = (BlockEntitySteamEngine) inv.player.level().getBlockEntity(container.tile.getBlockPos());
        inventory = inv;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor poseStack, int pX, int pY, float pPartialTick) {
        super.extractBackground(poseStack, pX, pY, pPartialTick);
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;

        int i2 = steam.getWaterScaled(54);
        int i3 = steam.getTempScaled(54);

        poseStack.blit(RenderPipelines.GUI_TEXTURED, getTextureIdentifier(), j + 49, k + 71 - i2, 193, 55 - i2, 5, i2, 256, 256);
        poseStack.blit(RenderPipelines.GUI_TEXTURED, getTextureIdentifier(), j + 119, k + 71 - i3, 177, 99 - i3, 9, i3, 256, 256);
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
