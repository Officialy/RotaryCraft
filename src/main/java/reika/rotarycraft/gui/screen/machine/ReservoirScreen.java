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

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.client.gui.ScreenUtils;
import reika.rotarycraft.base.NonPoweredMachineScreen;
import reika.rotarycraft.blockentities.storage.BlockEntityReservoir;
import reika.rotarycraft.gui.container.machine.ReservoirContainer;

public class ReservoirScreen extends NonPoweredMachineScreen<BlockEntityReservoir, ReservoirContainer> {

    private final BlockEntityReservoir reservoir;

    public ReservoirScreen(ReservoirContainer container, Inventory inv, Component title) {
        super(container, inv, title);
        reservoir = (BlockEntityReservoir) inv.player.level.getBlockEntity(container.tile.getBlockPos());
        imageWidth = 176;
        imageHeight = 96;
        inventory = inv;
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pX, int pY) {
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;

        super.renderBg(pPoseStack, pPartialTick, pX, pY);

        if (api.isMouseInBox(j + 83, j + 92, k + 25, k + 70, pX, pY)) {
            int mx = pX;
            int my = pY;
            api.drawTooltipAt(pPoseStack, font, String.format("%d/%d", reservoir.getFluidLevel(), BlockEntityReservoir.CAPACITY), mx, my);
        }

        if (!reservoir.isEmpty()) {
            int i2 = reservoir.getLiquidScaled(44);
            int x = imageWidth / 2 - 4;
            int y = imageHeight / 2 - 13 - i2 + 35;

            IClientFluidTypeExtensions props = IClientFluidTypeExtensions.of(reservoir.getFluid().getFluid().defaultFluidState());
            ResourceLocation tex = props.getStillTexture();
            RenderSystem.setShaderTexture(0, tex);
            ScreenUtils.drawTexturedModalRect(pPoseStack, j + x, k + y, 16, i2, 8, 44 - i2, 0);
//            this.drawTexturedModelRectFromIcon(x, y, tex, 8, i2);
        }
    }

    @Override
    protected String getGuiTexture() {
        return "reservoirgui";
    }

}
