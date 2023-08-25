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

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.gui.ScreenUtils;
import reika.rotarycraft.gui.container.ContainerHandCraft;

public class GuiHandCraft extends AbstractContainerScreen<ContainerHandCraft> implements RecipeUpdateListener {
    private static final ResourceLocation textures = new ResourceLocation("textures/gui/container/crafting_table.png");
    private final RecipeBookComponent recipeBookComponent = new RecipeBookComponent();

    public GuiHandCraft(ContainerHandCraft container, Inventory inventory, Component title) {
        super(container, inventory, title);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    @Override
    public void render(GuiGraphics stack, int p_98419_, int p_98420_, float p_98421_) {
        stack.drawString(font, I18n.get("container.crafting"), 28, 6, 4210752);
        stack.drawString(font, I18n.get("container.inventory"), 8, imageHeight - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    @Override
    protected void renderBg(GuiGraphics poseStack, float par1, int par2, int par3) {
        int var5 = (width - imageWidth) / 2;
        int var6 = (height - imageHeight) / 2;
        poseStack.blit(textures, var5, var6, 1, 2, imageWidth, imageHeight);
    }

    @Override
    public void recipesUpdated() {
        this.recipeBookComponent.recipesUpdated();
    }

    @Override
    public RecipeBookComponent getRecipeBookComponent() {
        return this.recipeBookComponent;
    }
}
