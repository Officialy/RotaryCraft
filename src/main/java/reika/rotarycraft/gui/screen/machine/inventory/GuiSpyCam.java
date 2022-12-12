///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.guis.Machine.Inventory;
//
//import org.lwjgl.opengl.GL11;
//
//import net.minecraft.entity.player.Player;
//import net.minecraft.util.Mth;
//
//import reika.dragonapi.base.CoreMenu;
//import reika.dragonapi.libraries.io.ReikaTextureHelper;
//import reika.dragonapi.libraries.rendering.ReikaColorAPI;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.GuiNonPoweredMachine;
//import reika.rotarycraft.blockentities.surveying.BlockEntitySpyCam;
//
//public class GuiSpyCam extends GuiNonPoweredMachine {
//
//    public static final int UNIT = 4;
//    //private Level level = ModLoader.getMinecraftInstance().theWorld;
//    private final BlockEntitySpyCam cam;
//    int x;
//    int y;
//    private int direction;
//
//    public GuiSpyCam(Player p5ep, BlockEntitySpyCam spycam) {
//        super(new CoreMenu(p5ep, spycam), spycam);
//        cam = spycam;
//        imageHeight = 222;
//        imageWidth = 222;
//        ep = p5ep;
//        direction = Mth.floor((ep.rotationYaw * 4F) / 360F + 0.5D);
//        while (direction > 3)
//            direction -= 4;
//        while (direction < 0)
//            direction += 4;
//    }
//
//    @Override
//    public boolean labelInventory() {
//        return false;
//    }
//
//    /**
//     * Draw the background layer for the GuiContainer (everything behind the items)
//     */
//    @Override
//    protected void renderBg(PoseStack poseStack, float par1, int par2, int par3) {
//        super.renderBg(PoseStack poseStack, par1, par2, par3);
//
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2 + 1;
//
//        //this.drawRect(j+imageWidth/2-1-cam.getRange()*UNIT, k+imageHeight/2+5+cam.getRange()*UNIT, j+imageWidth/2+1+cam.getRange()*UNIT, k+imageHeight/2+6+cam.getRange()*UNIT, 0xffffffff);
//        //this.drawRect(j+imageWidth/2-1-cam.getRange()*UNIT, k+imageHeight/2+4-cam.getRange()*UNIT, j+imageWidth/2+1+cam.getRange()*UNIT, k+imageHeight/2+5-cam.getRange()*UNIT, 0xffffffff);
//        //this.drawRect(j+imageWidth/2-1-cam.getRange()*UNIT, k+imageHeight/2+4-cam.getRange()*UNIT, j+imageWidth/2-cam.getRange()*UNIT, k+imageHeight/2+6+cam.getRange()*UNIT, 0xffffffff);
//        //this.drawRect(j+imageWidth/2+cam.getRange()*UNIT, k+imageHeight/2+4-cam.getRange()*UNIT, j+imageWidth/2+1+cam.getRange()*UNIT, k+imageHeight/2+6+cam.getRange()*UNIT, 0xffffffff);
//
//        this.drawRadar(j, k);
//    }
//
//    private void drawRadar(int a, int b) {
//        int max = cam.getBounds()[1] * UNIT;
//        for (int i = cam.getBounds()[0]; i <= cam.getBounds()[1]; i++) {
//            for (int j = cam.getBounds()[0]; j <= cam.getBounds()[1]; j++) {
//                float br = 1 - (cam.yCoord - cam.getHeightAt(i, j)) / (float) cam.yCoord * 1.25F;
//                if (br < 0)
//                    br = 0;
//                this.drawRect(a + 17 + max - (UNIT * j), b + 19 + UNIT * i, a + 17 + max - (UNIT + UNIT * j), b + 19 + UNIT * i + UNIT, ReikaColorAPI.getColorWithBrightnessMultiplier(cam.getTopBlockColorAt(i, j), br));
//            }
//        }
//        for (int i = cam.getBounds()[0]; i <= cam.getBounds()[1]; i++) {
//            for (int j = cam.getBounds()[0]; j <= cam.getBounds()[1]; j++) {
//                this.drawMobIcon(a + 13, b + 19, max - (UNIT * i), UNIT * j, cam.getMobAt(i, j), i, j);
//            }
//        }
//    }
//
//    private void drawMobIcon(int a, int b, int px, int py, int id, int i, int j) {
//        String var4 = "/Reika/RotaryCraft/Textures/GUI/mobicons.png";
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        ReikaTextureHelper.bindTexture(RotaryCraft.class, var4);
//        int v = 2 * UNIT * (id / 16);
//        int u = 2 * UNIT * (id - (v / UNIT / 2) * 16);
//        if (id == -1) {
//            u = 2 * UNIT;
//            v = 0;
//        }
//        this.drawTexturedModalRect(a + px - UNIT / 2, b + py - UNIT / 2, u, v, UNIT * 2, UNIT * 2);
//    }
//
//
//    @Override
//    protected String getGuiTexture() {
//        return "spycamgui";
//    }
//}
