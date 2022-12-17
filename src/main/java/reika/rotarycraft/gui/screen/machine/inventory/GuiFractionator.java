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
//import reika.dragonapi.instantiable.rendering.SubdividedProgressBar;
//import reika.dragonapi.instantiable.rendering.SubdividedProgressBar;
//import reika.dragonapi.libraries.io.ReikaTextureHelper;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.GuiMachine;
//import reika.rotarycraft.gui.container.Machine.Inventory.ContainerFractionator;
//import reika.rotarycraft.blockentities.production.BlockEntityFractionator;
//
//public class GuiFractionator extends GuiMachine {
//
//    private final BlockEntityFractionator fct;
//    //private Level level = ModLoader.getMinecraftInstance().theWorld;
//
//    private final SubdividedProgressBar bar = new SubdividedProgressBar().addSection(18).addSection(37).addSection(38, 0.4F).addSection(8).addSection(9, 0.1F);
//
//    public GuiFractionator(Player p5ep, BlockEntityFractionator tilef) {
//        super(new ContainerFractionator(p5ep, tilef), tilef);
//        fct = tilef;
//        imageWidth = 176;
//        imageHeight = 166;
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
//        if (api.isMouseInBox(j + 150, j + 159, k + 15, k + 70)) {
//            int mx = api.getMouseRealX();
//            int my = api.getMouseRealY();
//            api.drawTooltipAt(font, String.format("%d/%d", fct.getOutputLevel(), fct.getOutputCapacity()), mx - j, my - k);
//        }
//        if (api.isMouseInBox(j + 123, j + 132, k + 15, k + 70)) {
//            int mx = api.getMouseRealX();
//            int my = api.getMouseRealY();
//            api.drawTooltipAt(font, String.format("%d/%d", fct.getInputLevel(), fct.getInputCapacity()), mx - j, my - k);
//        }
//        if (api.isMouseInBox(j + 133, j + 136, k + 15, k + 70)) {
//            int mx = api.getMouseRealX();
//            int my = api.getMouseRealY();
//            api.drawTooltipAt(font, String.format("%dkPa", fct.getPressure()), mx - j, my - k);
//        }
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
//        int k = (height - imageHeight) / 2;
//
//        int i2 = fct.getFuelScaled(54);
//        ScreenUtils.drawTexturedModalRect(j + 151, k + 70 - i2, 179, 55 - i2, 8, i2);
//
//        int i1 = fct.getEthanolScaled(54);
//        ScreenUtils.drawTexturedModalRect(j + 124, k + 70 - i1, 189, 55 - i1, 8, i1);
//
//        int i0 = fct.getPressureScaled(54);
//        ScreenUtils.drawTexturedModalRect(j + 134, k + 70 - i0, 224, 55 - i0, 2, i0);
//
//        bar.setTick(fct.mixTime, fct.getOperationTime());
//        int v1 = bar.getScaled(0);
//        int v2 = bar.getScaled(1);
//        int v3 = bar.getScaled(2);
//        int v4 = bar.getScaled(3);
//        int v5 = bar.getScaled(4);
//        ScreenUtils.drawTexturedModalRect(j + 34, k + 17, 178, 59, v1, 47);
//        this.drawTexturedModalRect(j + 70, k + 17, 197, 59, v2, 47);
//        this.drawTexturedModalRect(j + 107, k + 53 - v3, 212, 39 - v3, 8, v3);
//        this.drawTexturedModalRect(j + 115, k + 17, 235, 59, v4, 34);
//        this.drawTexturedModalRect(j + 139, k + 18, 200, 1, v5, 52);
//    }
//
//    @Override
//    protected void drawPowerTab(int var5, int var6) {
//        String var4 = "/Reika/RotaryCraft/Textures/GUI/powertab.png";
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        ReikaTextureHelper.bindTexture(RotaryCraft.class, var4);
//        this.drawTexturedModalRect(imageWidth + var5, var6 + 4, 0, 4, 42, imageHeight - 4);
//
//        long frac = (fct.power * 29L) / fct.MINPOWER;
//        if (frac > 29)
//            frac = 29;
//        this.drawTexturedModalRect(imageWidth + var5 + 5, imageHeight + var6 - 144, 0, 0, (int) frac, 4);
//
//        frac = fct.omega * 29L / fct.MINSPEED;
//        if (frac > 29)
//            frac = 29;
//        this.drawTexturedModalRect(imageWidth + var5 + 5, imageHeight + var6 - 84, 0, 0, (int) frac, 4);
//
//        frac = fct.torque * 29L / fct.MINTORQUE;
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
//        return "fractiongui3b";
//    }
//}
