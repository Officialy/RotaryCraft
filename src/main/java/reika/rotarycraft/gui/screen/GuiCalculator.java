///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.gui.screen;
//
//import net.minecraft.client.gui.screens.inventory.ContainerScreen;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.level.Level;
//
//import org.lwjgl.opengl.GL11;
//
//import reika.rotarycraft.RotaryCraft;
//
//public class GuiCalculator extends ContainerScreen {
//
//    private static final int imageWidth = 194;
//    private static final int imageHeight = 161;
//    private final Player ep;
//    public Level level;
//    private int mx;
//    private int my;
//
//    public GuiCalculator(Player p5ep, Level world) {
//        super();
//        ep = p5ep;
//        level = world;
//    }
//	/*
//    @Override
//    public void initGui() {
//    	super.initGui();
//    	this.renderables.clear();
//
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2 - 8;
//    }*/
//
//    @Override
//    public boolean doesGuiPauseGame() {
//        return true;
//    }
//
//
//    public void refreshScreen() {
//        int lastx = mx;
//        int lasty = my;
//        mc.thePlayer.closeScreen();
//        //ModLoader.openGUI(ep, new GuiCalculator(ep, level));
//        Mouse.setCursorPosition(lastx, lasty);
//    }
//
//    @Override
//    public void updateScreen() {
//        super.updateScreen();
//        mx = Mouse.getX();
//        my = Mouse.getY();
//    }
//
//    @Override
//    public void drawScreen(int x, int y, float f) {
//        String var4 = "/Reika/RotaryCraft/Textures/GUI/calcgui.png";
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        ReikaTextureHelper.bindTexture(RotaryCraft.class, var4);
//
//        int getY = (width - imageWidth) / 2;
//        int posY = (height - imageHeight) / 2 - 8;
//
//        ScreenUtils.drawTexturedModalRect(getY, getY(), 0, 0, imageWidth, imageHeight);
//        this.drawKeys();
//        super.drawScreen(x, y, f);
//    }
//
//    private void drawKeys() {
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2 - 8;
//        int color = 0x000000;/*
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "π", j+16, k+141-18, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "Int", j+16, k+141-54, color);
//
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "Exp", j+34, k+141, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "tanh", j+34, k+141-18, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "cosh", j+34, k+141-36, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "sinh", j+34, k+141-54, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "e^x", j+34, k+141-72, color);
//
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "Mod", j+52, k+141, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "tan", j+52, k+141-18, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "cos", j+52, k+141-36, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "sin", j+52, k+141-54, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "ln", j+52, k+141-72, color);
//
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "log", j+70, k+141, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "x^3", j+70, k+141-18, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "x^y", j+70, k+141-36, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "x^2", j+70, k+141-54, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "(", j+70, k+141-72, color);
//
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "10^x", j+88, k+141, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "x^0.3", j+88, k+141-18, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "x^.y", j+88, k+141-36, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "n!", j+88, k+141-54, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, ")", j+88, k+141-72, color);
//
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "0", j+106+9, k+141, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "1", j+106, k+141-18, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "4", j+106, k+141-36, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "7", j+106, k+141-54, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "Bk", j+106, k+141-72, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "MC", j+106, k+141-90, color);
//
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "2", j+124, k+141-18, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "5", j+124, k+141-36, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "8", j+124, k+141-54, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "CE", j+124, k+141-72, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "MR", j+124, k+141-90, color);
//
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, ".", j+142, k+141, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "3", j+142, k+141-18, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "6", j+142, k+141-36, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "9", j+142, k+141-54, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "C", j+142, k+141-72, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "MS", j+142, k+141-90, color);
//
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "+", j+160, k+141, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "-", j+160, k+141-18, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "*", j+160, k+141-36, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "÷", j+160, k+141-54, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "±", j+160, k+141-72, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "M-", j+160, k+141-90, color);
//
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "=", j+178, k+131, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "1/x", j+178, k+131-26, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "%", j+178, k+131-26-18, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "√", j+178, k+131-26-36, color);
//    	ImageButton.drawCenteredStringNoShadow(this.fontRenderer, "M-", j+178, k+131-26-54, color);*/
//    }
//}
