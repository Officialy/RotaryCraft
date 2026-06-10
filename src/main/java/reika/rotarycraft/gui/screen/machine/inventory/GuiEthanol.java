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
import reika.rotarycraft.base.EngineScreen;
import reika.rotarycraft.blockentities.engine.BlockEntityGasEngine;
import reika.rotarycraft.gui.container.machine.inventory.ContainerEthanol;

public class GuiEthanol extends EngineScreen<BlockEntityGasEngine, ContainerEthanol> {
    private final BlockEntityGasEngine engine;

    public GuiEthanol(ContainerEthanol container, Inventory inv, Component component) {
        super(container, inv, component);
        engine = (BlockEntityGasEngine) inv.player.level().getBlockEntity(container.tile.getBlockPos());
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor poseStack, int par2, int par3, float par1) {
        super.extractBackground(poseStack, par2, par3, par1);

        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;
        int i1 = engine.getFuelScaled(54);
        poseStack.blit(RenderPipelines.GUI_TEXTURED, getTextureIdentifier(), j + 85, k + 71 - i1, 200, 55 - i1, 5, i1, 256, 256);

    }

    @Override
    protected String getGuiTexture() {
        return "ethanolgui";
    }

    @Override
    protected int getFuelBarXPos() {
        return 84;
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
