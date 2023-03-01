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
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.gui.ScreenUtils;
import reika.rotarycraft.base.EngineScreen;
import reika.rotarycraft.blockentities.engine.BlockEntityGasEngine;
import reika.rotarycraft.gui.container.machine.inventory.ContainerEthanol;

public class GuiEthanol extends EngineScreen<BlockEntityGasEngine, ContainerEthanol> {
    private final BlockEntityGasEngine engine;

    public GuiEthanol(ContainerEthanol container, Inventory inv, Component component) {
        super(container, inv, component);
        engine = (BlockEntityGasEngine) inv.player.level.getBlockEntity(container.tile.getBlockPos());
        imageWidth = 176;
        imageHeight = 166;
    }

    @Override
    protected void renderBg(PoseStack poseStack, float par1, int par2, int par3) {
        super.renderBg(poseStack, par1, par2, par3);

        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;
        int i1 = engine.getFuelScaled(54);
        ScreenUtils.drawTexturedModalRect(poseStack, j + 85, k + 71 - i1, 200, 55 - i1, 5, i1, 0);

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
