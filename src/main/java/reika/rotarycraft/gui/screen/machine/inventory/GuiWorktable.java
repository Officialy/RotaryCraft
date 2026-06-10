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

import reika.rotarycraft.base.GuiNonPoweredMachine;
import reika.rotarycraft.blockentities.production.BlockEntityWorktable;
import reika.rotarycraft.gui.container.machine.inventory.ContainerWorktable;

/**
 * 26.1 port of the legacy GuiWorktable. The fancy rollout/pattern features from 1.7 are
 * deferred until the recipe pipeline is back; this just gives the menu a visible background
 * so right-clicking the worktable doesn't crash with "no screen registered for menu type".
 */
public class GuiWorktable extends GuiNonPoweredMachine<BlockEntityWorktable, ContainerWorktable> {

    private final BlockEntityWorktable table;

    public GuiWorktable(ContainerWorktable container, Inventory inv, Component title) {
        super(container, inv, title);
        table = (BlockEntityWorktable) inventory.player.level().getBlockEntity(container.tile.getBlockPos());
        inventory = inv;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor poseStack, int pX, int pY, float pPartialTick) {
        super.extractBackground(poseStack, pX, pY, pPartialTick);

        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;

        // The legacy GUI drew an arrow indicating the recipe was ready to craft. Once the
        // worktable recipe pipeline ports we can re-enable this; for now the background image
        // already shows the empty arrow slot, which is enough to make the GUI usable.
        if (table != null && table.isReadyToCraft()) {
            poseStack.blit(RenderPipelines.GUI_TEXTURED, getTextureIdentifier(),
                    j + 79, k + 35, 176, 35, 18, 15, 256, 256);
        }
    }

    @Override
    protected String getGuiTexture() {
        return "worktablegui2";
    }
}
