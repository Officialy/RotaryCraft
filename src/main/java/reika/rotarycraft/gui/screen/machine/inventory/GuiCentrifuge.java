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
//import net.neoforged.fluids.Fluid;
//
//import reika.dragonapi.libraries.io.ReikaTextureHelper;
//import reika.dragonapi.libraries.rendering.ReikaLiquidRenderer;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.GuiMachine;
//import reika.rotarycraft.gui.container.Machine.Inventory.ContainerCentrifuge;
//import reika.rotarycraft.blockentities.processing.BlockEntityCentrifuge;
//
//public class GuiCentrifuge extends GuiMachine {
//    private final BlockEntityCentrifuge cent;
//
//    int x;
//    int y;
//
//    public GuiCentrifuge(Player p5ep, BlockEntityCentrifuge tilef) {
//        super(new ContainerCentrifuge(p5ep, tilef), tilef);
//        cent = tilef;
//        imageWidth = 176;
//        imageHeight = 166;
//        ep = p5ep;
//    }
//
//    @Override
//    protected boolean inventoryLabelLeft() {
//        return true;
//    }
//
//    @Override
//    protected void drawGuiContainerForegroundLayer(int a, int b) {
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//
//        super.drawGuiContainerForegroundLayer(a, b);
//
//        if (api.isMouseInBox(j + 151, j + 168, k + 7, k + 78)) {
//            int mx = api.getMouseRealX();
//            int my = api.getMouseRealY();
//            int level = cent.getLevel();
//            if (level > 0) {
//                String name = cent.getFluid().getLocalizedName();
//                api.drawTooltipAt(font, String.format("%s: %d/%d", name, cent.getLevel(), BlockEntityCentrifuge.CAPACITY), mx - j, my - k);
//            } else {
//                api.drawTooltipAt(font, String.format("0/%d mB", BlockEntityCentrifuge.CAPACITY), mx - j, my - k);
//            }
//        }
//        if (cent.getLevel() > 0) {
//            int i2 = cent.getLiquidScaled(70);
//            int x = 152;
//            int y = 77 - i2 + 1;
//            GL11.glColor4f(1, 1, 1, 1);
//            Fluid f = cent.getFluid();
//            IIcon ico = ReikaLiquidRenderer.getFluidIconSafe(f);
//            ReikaLiquidRenderer.bindFluidTexture(f);
//            this.drawTexturedModelRectFromIcon(x, y, ico, 16, i2);
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
//        int i3 = Math.min(37, cent.getProgressScaled(37));
//        ScreenUtils.drawTexturedModalRect(j + 45, k + 27, 178, 1, i3, 37);
//    }
//
//    @Override
//    protected void drawPowerTab(int var5, int var6) {
//        String var4 = "/Reika/RotaryCraft/Textures/GUI/powertab.png";
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        ReikaTextureHelper.bindTexture(RotaryCraft.class, var4);
//        ScreenUtils.drawTexturedModalRect(imageWidth + var5, var6 + 4, 0, 4, 42, imageHeight - 4);
//
//        long frac = (cent.power * 29L) / cent.MINPOWER;
//        if (frac > 29)
//            frac = 29;
//        ScreenUtils.drawTexturedModalRect(imageWidth + var5 + 5, imageHeight + var6 - 144, 0, 0, (int) frac, 4);
//
//        frac = cent.omega * 29L / cent.MINSPEED;
//        if (frac > 29)
//            frac = 29;
//        ScreenUtils.drawTexturedModalRect(imageWidth + var5 + 5, imageHeight + var6 - 84, 0, 0, (int) frac, 4);
//
//        frac = cent.torque * 29L / cent.MINTORQUE;
//        if (frac > 29)
//            frac = 29;
//        this.drawTexturedModalRect(imageWidth + var5 + 5, imageHeight + var6 - 24, 0, 0, (int) frac, 4);
//
//        api.drawCenteredStringNoShadow(font, "Power:", imageWidth + var5 + 20, var6 + 9, 0xff000000);
//        api.drawCenteredStringNoShadow(font, "Speed:", imageWidth + var5 + 20, var6 + 69, 0xff000000);
//        api.drawCenteredStringNoShadow(font, "Torque:", imageWidth + var5 + 20, var6 + 129, 0xff000000);
//        //this.drawCenteredStringNoShadow(font, String.format("%d/%d", fct.power, fct.MINPOWER), imageWidth+var5+16, var6+16, 0xff000000);
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return "centrifugegui";
//    }
//}
