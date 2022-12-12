///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.guis.Machine;
//
//import java.util.List;
//
//import org.lwjgl.opengl.GL11;
//
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.entity.player.Player;
//import net.minecraft.util.Mth;
//
//import reika.dragonapi.base.CoreMenu;
//import reika.dragonapi.libraries.ReikaEntityHelper;
//import reika.dragonapi.libraries.io.ReikaTextureHelper;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.GuiPowerOnlyMachine;
//import reika.rotarycraft.blockentities.surveying.BlockEntityMobRadar;
//
//public class GuiMobRadar extends GuiPowerOnlyMachine {
//
//    public static final int UNIT = 4;
//    private final BlockEntityMobRadar radar;
//
//    public GuiMobRadar(Player p5ep, BlockEntityMobRadar te) {
//        super(new CoreMenu(p5ep, te), te);
//        radar = te;
//        imageHeight = 223;
//        imageWidth = 214;
//        ep = p5ep;
//    }
//
//    @Override
//    protected void drawGuiContainerForegroundLayer(int a, int b) {
//        super.drawGuiContainerForegroundLayer(a, b);
//
//        if (radar.isJammed()) {
//            api.renderStatic(7, 16, 206, 215);
//        }
//    }
//
//    @Override
//    protected void renderBg(PoseStack poseStack, float par1, int par2, int par3) {
//        super.renderBg(PoseStack poseStack, par1, par2, par3);
//
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2 + 1;
//
//        //this.drawRect(j+imageWidth/2-1-radar.getRange()*UNIT, k+imageHeight/2+5+radar.getRange()*UNIT, j+imageWidth/2+1+radar.getRange()*UNIT, k+imageHeight/2+6+radar.getRange()*UNIT, 0xffffffff);
//        //this.drawRect(j+imageWidth/2-1-radar.getRange()*UNIT, k+imageHeight/2+4-radar.getRange()*UNIT, j+imageWidth/2+1+radar.getRange()*UNIT, k+imageHeight/2+5-radar.getRange()*UNIT, 0xffffffff);
//        //this.drawRect(j+imageWidth/2-1-radar.getRange()*UNIT, k+imageHeight/2+4-radar.getRange()*UNIT, j+imageWidth/2-radar.getRange()*UNIT, k+imageHeight/2+6+radar.getRange()*UNIT, 0xffffffff);
//        //this.drawRect(j+imageWidth/2+radar.getRange()*UNIT, k+imageHeight/2+4-radar.getRange()*UNIT, j+imageWidth/2+1+radar.getRange()*UNIT, k+imageHeight/2+6+radar.getRange()*UNIT, 0xffffffff);
//
//        int a = j + 7;
//        int b = k + 16;
//
//        this.drawRadar(j, k);
//        font.draw(radar.getRange() + "m", a + 102, b, 0xaaffaa);
//        font.draw((int) (0.63 * radar.getRange()) + "m", a + 102, b + 37, 0xaaffaa);
//        font.draw(Mth.ceil(0.31 * radar.getRange()) + "m", a + 102, b + 69, 0xaaffaa);
//    }
//
//    private void drawRadar(int a, int b) {
//        List<LivingEntity> li = radar.getEntities();
//        for (LivingEntity e : li) {
//            int dx = 100 + Mth.floor(100 * (e.getY - radar.xCoord - 0.5) / radar.getRange());
//            int dz = 100 + Mth.floor(100 * (e.posZ - radar.zCoord - 0.5) / radar.getRange());
//            this.drawMobIcon(a + 7, b + 16, dx, dz, ReikaEntityHelper.getEntityID(e), dx, dz);
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
//        ScreenUtils.drawTexturedModalRect(a + px - UNIT / 2, b + py - UNIT / 2, u, v, UNIT * 2, UNIT * 2);
//    }
//
//
//    @Override
//    protected String getGuiTexture() {
//        return "mobradargui";
//    }
//}
