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
//import reika.rotarycraft.gui.container.Machine.Inventory.ContainerCrystallizer;
//import reika.rotarycraft.blockentities.processing.BlockEntityCrystallizer;
//
//public class GuiCrystallizer extends GuiMachine {
//    private final BlockEntityCrystallizer te;
//
//    public GuiCrystallizer(Player p5ep, BlockEntityCrystallizer Crystallizer) {
//        super(new ContainerCrystallizer(p5ep, Crystallizer), Crystallizer);
//        te = Crystallizer;
//        ep = p5ep;
//        imageWidth = 176;
//        imageHeight = 166;
//    }
//
//    /**
//     * Draw the foreground layer for the GuiContainer (everything in front of the items)
//     */
//    @Override
//    protected void drawGuiContainerForegroundLayer(int a, int b) {
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//
//        super.drawGuiContainerForegroundLayer(a, b);
//
//        if (!te.isEmpty()) {
//            int i2 = te.getLiquidScaled(72);
//            int x = 8;
//            int y = 78 - i2 + 1;
//            GL11.glColor4f(1, 1, 1, 1);
//            Fluid f = te.getContainedFluid();
//            IIcon ico = ReikaLiquidRenderer.getFluidIconSafe(f);
//            ReikaLiquidRenderer.bindFluidTexture(f);
//            this.drawTexturedModelRectFromIcon(x, y, ico, 16, i2);
//
//            String s = String.format("%d C", te.getFreezingPoint());
//            api.drawCenteredStringNoShadow(font, s, imageWidth / 2, 56, 0);
//        }
//        if (api.isMouseInBox(j + 7, j + 24, k + 6, k + 79)) {
//            int mx = api.getMouseRealX();
//            int my = api.getMouseRealY();
//            api.drawTooltipAt(font, String.format("%d/%d", te.getLevel(), te.getCapacity()), mx - j, my - k);
//        }
//        String s = String.format("%d C", te.getTemperature());
//        api.drawCenteredStringNoShadow(font, s, 50, 30, 0);
//    }
//
//    /**
//     * Draw the background layer for the GuiContainer (everything behind the items)
//     */
//    @Override
//    protected void renderBg(PoseStack poseStack, float par1, int par2, int par3) {
//        super.renderBg(PoseStack poseStack, par1, par2, par3);
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//
//        int i1 = Math.min(44, te.getProgressScaled(44));
//        ScreenUtils.drawTexturedModalRect(j + 29, k + 41, 178, 1, i1, 4);
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return "crystalgui";
//    }
//
//    @Override
//    protected void drawPowerTab(int j, int k) {
//        String var4 = "/Reika/RotaryCraft/Textures/GUI/powertab.png";
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        ReikaTextureHelper.bindTexture(RotaryCraft.class, var4);
//        ScreenUtils.drawTexturedModalRect(imageWidth + j, k + 4, 0, 4, 42, imageHeight - 4);
//
//        long frac = (te.power * 29L) / te.MINPOWER;
//        if (frac > 29)
//            frac = 29;
//        ScreenUtils.drawTexturedModalRect(imageWidth + j + 5, imageHeight + k - 144, 0, 0, (int) frac, 4);
//
//        frac = (int) (te.omega * 29L) / te.MINSPEED;
//        if (frac > 29)
//            frac = 29;
//        ScreenUtils.drawTexturedModalRect(imageWidth + j + 5, imageHeight + k - 84, 0, 0, (int) frac, 4);
//
//        frac = (int) (te.torque * 29L) / te.MINTORQUE;
//        if (frac > 29)
//            frac = 29;
//        this.drawTexturedModalRect(imageWidth + j + 5, imageHeight + k - 24, 0, 0, (int) frac, 4);
//
//        api.drawCenteredStringNoShadow(font, "Power:", imageWidth + j + 20, k + 9, 0xff000000);
//        api.drawCenteredStringNoShadow(font, "Speed:", imageWidth + j + 20, k + 69, 0xff000000);
//        api.drawCenteredStringNoShadow(font, "Torque:", imageWidth + j + 20, k + 129, 0xff000000);
//        //this.drawCenteredStringNoShadow(font, String.format("%d/%d", te.power, te.MINPOWER), imageWidth+j+16, k+16, 0xff000000);
//    }
//}
