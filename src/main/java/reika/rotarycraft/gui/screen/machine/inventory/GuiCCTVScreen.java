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
//
//import reika.dragonapi.libraries.io.ReikaTextureHelper;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.GuiMachine;
//import reika.rotarycraft.gui.container.Machine.ContainerScreen;
//import reika.rotarycraft.blockentities.auxiliary.BlockEntityScreen;
//
//public class GuiCCTVScreen extends GuiMachine {
//
//    public GuiCCTVScreen(Player p5ep, BlockEntityScreen te) {
//        super(new ContainerScreen(p5ep, te), te);
//        ep = p5ep;
//        imageHeight = 166;
//        imageWidth = 176;
//    }
//
//    @Override
//    protected void drawGuiContainerForegroundLayer(int a, int b) {
//        super.drawGuiContainerForegroundLayer(a, b);
//
//        api.drawCenteredStringNoShadow(font, "Camera Select", imageWidth / 2, 54, 4210752);
//    }
//
//    @Override
//    protected void drawPowerTab(int var5, int var6) {
//        int a = 2;
//        String var4 = "/Reika/RotaryCraft/Textures/GUI/powertab.png";
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        ReikaTextureHelper.bindTexture(RotaryCraft.class, var4);
//        ScreenUtils.drawTexturedModalRect(imageWidth + var5, var6 + 0 + a, 85, 4, 42, imageHeight - 1);
//
//        long frac = (recv.power * 29L) / recv.MINPOWER;
//        if (frac > 29)
//            frac = 29;
//        ScreenUtils.drawTexturedModalRect(imageWidth + var5 + 5, imageHeight + var6 - 149 + a, 0, 0, (int) frac, 4);
//
//        frac = recv.omega * 29L / recv.MINSPEED;
//        if (frac > 29)
//            frac = 29;
//        ScreenUtils.drawTexturedModalRect(imageWidth + var5 + 5, imageHeight + var6 - 90 + a, 0, 0, (int) frac, 4);
//
//        frac = recv.torque * 29L / recv.MINTORQUE;
//        if (frac > 29)
//            frac = 29;
//        ScreenUtils.drawTexturedModalRect(imageWidth + var5 + 5, imageHeight + var6 - 31 + a, 0, 0, (int) frac, 4);
//
//        api.drawCenteredStringNoShadow(font, "Power:", imageWidth + var5 + 20, var6 + 4 + a, 0xff000000);
//        api.drawCenteredStringNoShadow(font, "Speed:", imageWidth + var5 + 20, var6 + 63 + a, 0xff000000);
//        api.drawCenteredStringNoShadow(font, "Torque:", imageWidth + var5 + 20, var6 + 122 + a, 0xff000000);
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return "screengui";
//    }
//
//}
