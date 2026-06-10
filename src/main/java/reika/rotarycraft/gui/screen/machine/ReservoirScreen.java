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
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import reika.rotarycraft.base.NonPoweredMachineScreen;
import reika.rotarycraft.blockentities.storage.BlockEntityReservoir;
import reika.rotarycraft.gui.container.machine.ReservoirContainer;

public class ReservoirScreen extends NonPoweredMachineScreen<BlockEntityReservoir, ReservoirContainer> {

    private final BlockEntityReservoir reservoir;

    public ReservoirScreen(ReservoirContainer container, Inventory inv, Component title) {
        super(container, inv, title, 176, 96);
        reservoir = (BlockEntityReservoir) inv.player.level().getBlockEntity(container.tile.getBlockPos());
        inventory = inv;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor pPoseStack, int pX, int pY, float pPartialTick) {
        super.extractBackground(pPoseStack, pX, pY, pPartialTick);
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;

        if (api.isMouseInBox(j + 83, j + 92, k + 25, k + 70, pX, pY)) {
            api.drawTooltipAt(pPoseStack, font, String.format("%d/%d", reservoir.getFluidLevel(), BlockEntityReservoir.CAPACITY), pX, pY);
        }

        // 1.21.5: IClientFluidTypeExtensions.getStillTexture() was removed; fluid GUI
        // overlays now come from the fluid's still-render-type texture atlas sprite.
        // TODO: rebuild via FluidStack.getFluid().getFluidType() + the new still-sprite API.
    }

    @Override
    protected String getGuiTexture() {
        return "reservoirgui";
    }

}
