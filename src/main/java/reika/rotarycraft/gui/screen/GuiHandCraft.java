/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.gui.screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import reika.rotarycraft.gui.container.ContainerHandCraft;

// 1.21.5 NOTE: RecipeBookComponent became abstract and RecipeUpdateListener was rewritten
// around the new RecipeDisplay system. The recipe book integration is dropped pending a
// rewrite against the new RecipeUpdateListener/SlotDisplayContext APIs.
public class GuiHandCraft extends AbstractContainerScreen<ContainerHandCraft> {
    private static final Identifier textures = Identifier.parse("textures/gui/container/crafting_table.png");

    public GuiHandCraft(ContainerHandCraft container, Inventory inventory, Component title) {
        super(container, inventory, title);
    }

    // Legacy drawGuiContainerForegroundLayer: the crafting + inventory titles, in gui-relative
    // space (extractLabels is translated by leftPos/topPos under 26.1).
    @Override
    protected void extractLabels(GuiGraphicsExtractor stack, int pMouseX, int pMouseY) {
        stack.text(font, I18n.get("container.crafting"), 28, 6, 4210752, false);
        stack.text(font, I18n.get("container.inventory"), 8, imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor poseStack, int par2, int par3, float par1) {
        super.extractBackground(poseStack, par2, par3, par1);
        int var5 = (width - imageWidth) / 2;
        int var6 = (height - imageHeight) / 2;
        poseStack.blit(RenderPipelines.GUI_TEXTURED, textures, var5, var6, 0, 0, imageWidth, imageHeight, 256, 256);
    }
}
