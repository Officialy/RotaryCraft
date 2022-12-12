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
//import reika.rotarycraft.containers.Machine.Inventory.ContainerPulseFurnace;
//import reika.rotarycraft.blockentities.processing.BlockEntityPulseFurnace;
//
//import cpw.mods.fml.relauncher.Side;
//import cpw.mods.fml.relauncher.OnlyIn;
//
//
//public class GuiPulseFurnace extends GuiMachine {
//
//    private final BlockEntityPulseFurnace puls;
//
//    public GuiPulseFurnace(Player p5ep, BlockEntityPulseFurnace pulseFurnace) {
//        super(new ContainerPulseFurnace(p5ep, pulseFurnace), pulseFurnace);
//        puls = pulseFurnace;
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
//        if (api.isMouseInBox(j + 90, j + 96, k + 15, k + 68)) {
//            int mx = api.getMouseRealX();
//            int my = api.getMouseRealY();
//            api.drawTooltipAt(font, String.format("%d/%d", puls.getFuel(), BlockEntityPulseFurnace.MAXFUEL), mx - j, my - k);
//        }
//        if (api.isMouseInBox(j + 20, j + 30, k + 15, k + 70)) {
//            int mx = api.getMouseRealX();
//            int my = api.getMouseRealY();
//            api.drawTooltipAt(font, String.format("%dC", puls.temperature), mx - j, my - k);
//        }
//        if (api.isMouseInBox(j + 159, j + 165, k + 15, k + 68)) {
//            int mx = api.getMouseRealX();
//            int my = api.getMouseRealY();
//            api.drawTooltipAt(font, String.format("%d/%d", puls.getAccelerant(), puls.getAccelerantCapacity()), mx - j, my - k);
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
//        int i1 = puls.getCookProgressScaled(10);
//        int i2 = puls.getFuelScaled(52);
//        int i3 = puls.getWaterScaled(52);
//        int i4 = puls.getTempScaled(54);
//        if (i4 < 9)
//            i4 = 9;
//        int i5 = puls.getFireScaled(38);
//        int i6 = puls.getAccelerantScaled(52);
//        ScreenUtils.drawTexturedModalRect(j + 131, k + 36, 215, 55, 4, i1);
//        ScreenUtils.drawTexturedModalRect(j + 91, k + 68 - i2, 248, 53 - i2, 5, i2);
//        ScreenUtils.drawTexturedModalRect(j + 59, k + 68 - i3, 199, 53 - i3, 5, i3);
//        ScreenUtils.drawTexturedModalRect(j + 20, k + 70 - i4, 176, 55 - i4, 11, i4);
//        this.drawTexturedModalRect(j + 115, k + 61 - i5, 177, 95 - i5, 9, i5);
//        this.drawTexturedModalRect(j + 142, k + 61 - i5, 204, 95 - i5, 9, i5);
//        this.drawTexturedModalRect(j + 160, k + 68 - i6, 227, 53 - i6, 5, i6);
//    }
//
//    @Override
//    protected void drawPowerTab(int var5, int var6) {
//        String var4 = "/Reika/RotaryCraft/Textures/GUI/powertab.png";
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        ReikaTextureHelper.bindTexture(RotaryCraft.class, var4);
//        this.drawTexturedModalRect(imageWidth + var5, var6 + 4, 0, 4, 42, imageHeight - 4);
//
//        long frac = (puls.power * 29L) / puls.MINPOWER;
//        if (frac > 29)
//            frac = 29;
//        this.drawTexturedModalRect(imageWidth + var5 + 5, imageHeight + var6 - 144, 0, 0, (int) frac, 4);
//
//        frac = (int) (puls.omega * 29L) / puls.MINSPEED;
//        if (frac > 29)
//            frac = 29;
//        this.drawTexturedModalRect(imageWidth + var5 + 5, imageHeight + var6 - 84, 0, 0, (int) frac, 4);
//
//        frac = (int) (puls.torque * 29L) / puls.MINTORQUE;
//        if (frac > 29)
//            frac = 29;
//        this.drawTexturedModalRect(imageWidth + var5 + 5, imageHeight + var6 - 24, 0, 0, (int) frac, 4);
//
//        api.drawCenteredStringNoShadow(font, "Power:", imageWidth + var5 + 20, var6 + 9, 0xff000000);
//        api.drawCenteredStringNoShadow(font, "Speed:", imageWidth + var5 + 20, var6 + 69, 0xff000000);
//        api.drawCenteredStringNoShadow(font, "Torque:", imageWidth + var5 + 20, var6 + 129, 0xff000000);
//        //this.drawCenteredStringNoShadow(font, String.format("%d/%d", puls.power, puls.MINPOWER), imageWidth+var5+16, var6+16, 0xff000000);
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return "pulsejetgui";
//    }
//
//}
