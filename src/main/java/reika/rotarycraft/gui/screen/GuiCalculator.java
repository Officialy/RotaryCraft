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
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.client.gui.ScreenUtils;

public class GuiCalculator extends Screen {

    private static final int imageWidth = 194;
    private static final int imageHeight = 161;
    private final Player ep;
    public Level level;

    public GuiCalculator(Player p5ep, Level world) {
        super(Component.literal("Calculator"));
        ep = p5ep;
        level = world;
    }
	/*
    @Override
    public void initGui() {
    	super.initGui();
    	this.renderables.clear();

        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2 - 8;
    }*/

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    @Override
    public void render(GuiGraphics stack, int p_98419_, int x, float z) {
        super.render(stack, p_98419_, x, z);
        int posX = (width - imageWidth) / 2;
        int posY = (height - imageHeight) / 2 - 8;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, ResourceLocation.fromNamespaceAndPath("rotarycraft", "textures/screen/calcgui.png"));
        ScreenUtils.drawTexturedModalRect(stack, posX, posY, 0, 0, imageWidth, imageHeight, 0);
        this.drawKeys();
    }
    private void drawKeys() {
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2 - 8;
        int color = 0x000000;/*
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "π", j+16, k+141-18, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "Int", j+16, k+141-54, color);

    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "Exp", j+34, k+141, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "tanh", j+34, k+141-18, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "cosh", j+34, k+141-36, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "sinh", j+34, k+141-54, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "e^x", j+34, k+141-72, color);

    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "Mod", j+52, k+141, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "tan", j+52, k+141-18, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "cos", j+52, k+141-36, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "sin", j+52, k+141-54, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "ln", j+52, k+141-72, color);

    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "log", j+70, k+141, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "x^3", j+70, k+141-18, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "x^y", j+70, k+141-36, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "x^2", j+70, k+141-54, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "(", j+70, k+141-72, color);

    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "10^x", j+88, k+141, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "x^0.3", j+88, k+141-18, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "x^.y", j+88, k+141-36, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "n!", j+88, k+141-54, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, ")", j+88, k+141-72, color);

    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "0", j+106+9, k+141, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "1", j+106, k+141-18, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "4", j+106, k+141-36, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "7", j+106, k+141-54, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "Bk", j+106, k+141-72, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "MC", j+106, k+141-90, color);

    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "2", j+124, k+141-18, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "5", j+124, k+141-36, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "8", j+124, k+141-54, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "CE", j+124, k+141-72, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "MR", j+124, k+141-90, color);

    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, ".", j+142, k+141, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "3", j+142, k+141-18, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "6", j+142, k+141-36, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "9", j+142, k+141-54, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "C", j+142, k+141-72, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "MS", j+142, k+141-90, color);

    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "+", j+160, k+141, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "-", j+160, k+141-18, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "*", j+160, k+141-36, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "÷", j+160, k+141-54, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "±", j+160, k+141-72, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "M-", j+160, k+141-90, color);

    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "=", j+178, k+131, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "1/x", j+178, k+131-26, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "%", j+178, k+131-26-18, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "√", j+178, k+131-26-36, color);
    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "M-", j+178, k+131-26-54, color);*/
    }
}
