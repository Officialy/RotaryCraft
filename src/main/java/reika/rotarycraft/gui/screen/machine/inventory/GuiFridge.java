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
//import com.mojang.blaze3d.vertex.PoseStack;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.level.material.Fluid;
//import net.minecraftforge.client.gui.ScreenUtils;
//import org.lwjgl.opengl.GL11;
//
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.GuiMachine;
//
//public class GuiFridge extends GuiMachine {
//    private final BlockEntityRefrigerator te;
//
//    public GuiFridge(Player p5ep, BlockEntityRefrigerator Fridge) {
//        super(new ContainerFridge(p5ep, Fridge), Fridge);
//        te = Fridge;
//        ep = p5ep;
//        imageHeight = 188;
//    }
//
//    @Override
//    protected void renderBg(PoseStack poseStack, float par1, int par2, int par3) {
//        super.renderBg(PoseStack poseStack, par1, par2, par3);
//
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//
//        int i1 = Math.min(145, te.getProgressScaled(145));
//        ScreenUtils.drawTexturedModalRect(j + 7, k + 17, 0, 189, i1, 67);
//    }
//
//    @Override
//    protected void drawGuiContainerForegroundLayer(int a, int b) {
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//
//        super.drawGuiContainerForegroundLayer(a, b);
//
//        if (te.getLevel() > 0) {
//            int i2 = te.getLiquidScaled(72);
//            int x = 152;
//            int y = 89 - i2 + 1;
//            GL11.glColor4f(1, 1, 1, 1);
//            Fluid f = te.getContainedFluid();
//            IIcon ico = ReikaLiquidRenderer.getFluidIconSafe(f);
//            ReikaLiquidRenderer.bindFluidTexture(f);
//            this.drawTexturedModelRectFromIcon(x, y, ico, 16, i2);
//        }
//        if (api.isMouseInBox(j + 152, j + 167, k + 18, k + 89)) {
//            int mx = api.getMouseRealX();
//            int my = api.getMouseRealY();
//            api.drawTooltipAt(font, String.format("%d/%d", te.getLevel(), te.getCapacity()), mx - j, my - k);
//            if (te.getLevel() > 0) {
//                Fluid f = te.getContainedFluid();
//                String s = f.getLocalizedName();
//                api.drawTooltipAt(font, s, mx - j, my - k + 12);
//            }
//        }
//    }
//
//    @Override
//    protected void drawPowerTab(int var5, int var6) {
//        String var4 = "/Reika/RotaryCraft/Textures/GUI/powertab.png";
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        ReikaTextureHelper.bindTexture(RotaryCraft.class, var4);
//        ScreenUtils.drawTexturedModalRect(imageWidth + var5, var6 + 4, 0, 4, 42, imageHeight - 24);
//
//        long frac = (te.power * 29L) / te.MINPOWER;
//        if (frac > 29)
//            frac = 29;
//        ScreenUtils.drawTexturedModalRect(imageWidth + var5 + 5, imageHeight + var6 - 166, 0, 0, (int) frac, 4);
//
//        frac = (int) (te.omega * 29L) / te.MINSPEED;
//        if (frac > 29)
//            frac = 29;
//        this.drawTexturedModalRect(imageWidth + var5 + 5, imageHeight + var6 - 106, 0, 0, (int) frac, 4);
//
//        frac = (int) (te.torque * 29L) / te.MINTORQUE;
//        if (frac > 29)
//            frac = 29;
//        this.drawTexturedModalRect(imageWidth + var5 + 5, imageHeight + var6 - 46, 0, 0, (int) frac, 4);
//
//        api.drawCenteredStringNoShadow(font, "Power:", imageWidth + var5 + 20, var6 + 9, 0xff000000);
//        api.drawCenteredStringNoShadow(font, "Speed:", imageWidth + var5 + 20, var6 + 69, 0xff000000);
//        api.drawCenteredStringNoShadow(font, "Torque:", imageWidth + var5 + 20, var6 + 129, 0xff000000);
//        //this.drawCenteredStringNoShadow(font, String.format("%d/%d", te.power, te.MINPOWER), imageWidth+var5+16, var6+16, 0xff000000);
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return "fridgegui";
//    }
//}
