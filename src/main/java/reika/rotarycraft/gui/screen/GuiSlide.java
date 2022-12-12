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
//import com.mojang.blaze3d.vertex.PoseStack;
//import net.minecraft.client.gui.screens.inventory.ContainerScreen;
//import net.minecraft.network.chat.Component;
//import net.minecraft.world.entity.player.Inventory;
//import net.minecraft.world.item.ItemStack;
//import org.lwjgl.opengl.GL11;
//
//import reika.dragonapi.libraries.rendering.ReikaGuiAPI;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.registry.PacketRegistry;
//
//public class GuiSlide extends ContainerScreen {
//
//    private final int imageWidth = 225;
//    private final int imageHeight = 48;
//    private GuiTextField input;
//    private String file;
//
//    public GuiSlide(SlideContainer, Inventory inventory, TextComponent title, ItemStack in) {
//        super();
//        if (in.getTag() != null)
//            file = in.getTag().getString("file");
//    }
//
//    @Override
//    public void initGui() {
//        super.initGui();
//        int j = (width - imageWidth) / 2 + 8;
//        int k = (height - imageHeight) / 2 - 12;
//        input = new GuiTextField(font, j - 2, k + 31, imageWidth - 16, 16);
//        input.setMaxStringLength(128);
//        input.setFocused(false);
//        input.setText(file);
//    }
//
//    @Override
//    protected void keyTyped(char c, int i) {
//        super.keyTyped(c, i);
//        input.textboxKeyTyped(c, i);
//    }
//
//    @Override
//    public void mouseClicked(int i, int j, int k) {
//        super.mouseClicked(i, j, k);
//        input.mouseClicked(i, j, k);
//    }
//
//    @Override
//    public void updateScreen() {
//        super.updateScreen();
//        int x = Mouse.getX();
//        int y = Mouse.getY();
//        if (input.getText().isEmpty()) {
//            return;
//        } else
//            file = input.getText();
//        ReikaPacketHelper.sendStringPacket(RotaryCraft.packetChannel, PacketRegistry.SLIDE.ordinal(), file);
//    }
//
//    @Override
//    public boolean isPauseScreen() {
//        return true;
//    }
//
//    @Override
//    public void drawScreen(int x, int y, float f) {
//
//    }
//
//    @Override
//    public void render(PoseStack poseStack, int x, int y, float f) {
//        String var4 = "/Reika/RotaryCraft/Textures/GUI/slidegui.png";
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        ReikaTextureHelper.bindTexture(RotaryCraft.class, var4);
//
//        int posX = (width - imageWidth) / 2;
//        int posY = (height - imageHeight) / 2 - 8;
//
//        ScreenUtils.drawTexturedModalRect(posX, posY, 0, 0, imageWidth, imageHeight);
//
//        input.drawTextBox();
//
//        if (!input.isFocused()) {
//            int d = input.getCursorPosition();
//            //font.drawStringWithShadow(file.substring(d, Math.min(file.length(), 37+d)), getY+10, getY()+imageHeight-15, 0xaaaaaa);
//        }
//        ReikaGuiAPI.instance.drawCenteredStringNoShadow(font, "Select an image file. Be sure to include", posX + imageWidth / 2 + 1, posY + 4, 4210752);
//        ReikaGuiAPI.instance.drawCenteredStringNoShadow(font, "C:/ and file extension and use \"/\", not \"\\\".", posX + imageWidth / 2 + 1, posY + 14, 4210752);
//        super.render(poseStack, x, y, f);
//    }
//}
