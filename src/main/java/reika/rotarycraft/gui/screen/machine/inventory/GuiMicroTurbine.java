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
import reika.rotarycraft.blockentities.engine.BlockEntityMicroturbine;
import reika.rotarycraft.gui.container.machine.inventory.ContainerMicroTurbine;

/**
 * 26.1 port. Pairs with {@link ContainerMicroTurbine}. Draws the jet-fuel bar using the same
 * pattern as {@link GuiEthanol}.
 */
public class GuiMicroTurbine extends EngineScreen<BlockEntityMicroturbine, ContainerMicroTurbine> {
    private final BlockEntityMicroturbine engine;

    public GuiMicroTurbine(ContainerMicroTurbine container, Inventory inv, Component title) {
        super(container, inv, title);
        engine = (BlockEntityMicroturbine) inv.player.level().getBlockEntity(container.tile.getBlockPos());
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor poseStack, int pX, int pY, float pPartialTick) {
        super.extractBackground(poseStack, pX, pY, pPartialTick);

        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;
        int i1 = engine.getFuelScaled(54);
        poseStack.blit(RenderPipelines.GUI_TEXTURED, getTextureIdentifier(),
                j + 85, k + 71 - i1, 200, 55 - i1, 5, i1, 256, 256);
    }

    // Reuse the ethanol GUI background until a microturbine-specific texture is ported.
    @Override
    protected String getGuiTexture() {
        return "ethanolgui";
    }

    @Override protected int getFuelBarXPos()  { return 84; }
    @Override protected int getFuelBarYPos()  { return 16; }
    @Override protected int getFuelBarXSize() { return 6; }
    @Override protected int getFuelBarYSize() { return 55; }
}
