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
//import reika.rotarycraft.gui.container.Machine.Inventory.ContainerGrinder;
//import reika.rotarycraft.blockentities.processing.BlockEntityGrinder;
//
//public class GuiGrinder extends GuiMachine {
//    private final BlockEntityGrinder grin;
//
//    public GuiGrinder(Player p5ep, BlockEntityGrinder Grinder) {
//        super(new ContainerGrinder(p5ep, Grinder), Grinder);
//        grin = Grinder;
//        ep = p5ep;
//    }
//
//    @Override
//    protected void drawGuiContainerForegroundLayer(int a, int b) {
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//
//        super.drawGuiContainerForegroundLayer(a, b);
//
//        font.draw("Lubricant", 5, 11, 4210752);
//
//        if (api.isMouseInBox(j + 23, j + 32, k + 20, k + 76)) {
//            int mx = api.getMouseRealX();
//            int my = api.getMouseRealY();
//            api.drawTooltipAt(font, String.format("%d/%d", grin.getLevel(), BlockEntityGrinder.MAXLUBE), mx - j, my - k);
//        }
//    }
//
//    @Override
//    protected void renderBg(PoseStack poseStack, float par1, int par2, int par3) {
//        super.renderBg(PoseStack poseStack, par1, par2, par3);
//
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//
//        int i1 = Math.min(48, grin.getCookProgressScaled(48));
//        ScreenUtils.drawTexturedModalRect(j + 99, k + 34, 176, 14, 1 * (i1) + 1, 16);
//
//        int i2 = grin.getLubricantScaled(55);
//        int i3 = 0;
//        if (i2 != 0)
//            i3 = 1;
//        ScreenUtils.drawTexturedModalRect(j + 24, imageHeight / 2 + k - 7 - i2, 176, 126 - i2, 8, i2);
//    }
//
//    @Override
//    protected void drawPowerTab(int var5, int var6) {
//        String var4 = "/Reika/RotaryCraft/Textures/GUI/powertab.png";
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        ReikaTextureHelper.bindTexture(RotaryCraft.class, var4);
//        ScreenUtils.drawTexturedModalRect(imageWidth + var5, var6 + 4, 0, 4, 42, imageHeight - 4);
//
//        long frac = (grin.power * 29L) / grin.MINPOWER;
//        if (frac > 29)
//            frac = 29;
//        ScreenUtils.drawTexturedModalRect(imageWidth + var5 + 5, imageHeight + var6 - 144, 0, 0, (int) frac, 4);
//
//        frac = (int) (grin.omega * 29L) / grin.MINSPEED;
//        if (frac > 29)
//            frac = 29;
//        this.drawTexturedModalRect(imageWidth + var5 + 5, imageHeight + var6 - 84, 0, 0, (int) frac, 4);
//
//        frac = (int) (grin.torque * 29L) / grin.MINTORQUE;
//        if (frac > 29)
//            frac = 29;
//        this.drawTexturedModalRect(imageWidth + var5 + 5, imageHeight + var6 - 24, 0, 0, (int) frac, 4);
//
//        api.drawCenteredStringNoShadow(font, "Power:", imageWidth + var5 + 20, var6 + 9, 0xff000000);
//        api.drawCenteredStringNoShadow(font, "Speed:", imageWidth + var5 + 20, var6 + 69, 0xff000000);
//        api.drawCenteredStringNoShadow(font, "Torque:", imageWidth + var5 + 20, var6 + 129, 0xff000000);
//        //this.drawCenteredStringNoShadow(font, String.format("%d/%d", grin.power, grin.MINPOWER), imageWidth+var5+16, var6+16, 0xff000000);
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return "grindergui";
//    }
//}
