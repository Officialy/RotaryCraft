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
//import reika.rotarycraft.containers.Machine.Inventory.ContainerCompactor;
//import reika.rotarycraft.blockentities.processing.BlockEntityCompactor;
//
//public class GuiCompactor extends GuiMachine {
//    private final BlockEntityCompactor comp;
//
//    public GuiCompactor(Player p5ep, BlockEntityCompactor Compactor) {
//        super(new ContainerCompactor(p5ep, Compactor), Compactor);
//        comp = Compactor;
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
//        if (api.isMouseInBox(j + 146, j + 151, k + 15, k + 70)) {
//            int mx = api.getMouseRealX();
//            int my = api.getMouseRealY();
//            api.drawTooltipAt(font, String.format("%d kPa", comp.getPressure()), mx - j, my - k);
//        }
//        if (api.isMouseInBox(j + 117, j + 127, k + 15, k + 70)) {
//            int mx = api.getMouseRealX();
//            int my = api.getMouseRealY();
//            api.drawTooltipAt(font, String.format("%dC", comp.temperature), mx - j, my - k);
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
//        int i1 = comp.getCookProgressScaled(30);
//        int i2 = comp.getTemperatureScaled(54);
//        int i3 = comp.getPressureScaled(54);
//        if (i1 != 0)
//            i1++;
//        if (i2 < 9)
//            i2 = 9;
//        ScreenUtils.drawTexturedModalRect(j + 46, k + 14, 193, 32, 1 * (i1), 58);
//        ScreenUtils.drawTexturedModalRect(j + 147, k + 70 - i3, 176, 82 - i3, 4, i3);
//        ScreenUtils.drawTexturedModalRect(j + 118, k + 70 - i2, 182, 86 - i2, 9, i2);
//    }
//
//    @Override
//    protected void drawPowerTab(int var5, int var6) {
//        String var4 = "/Reika/RotaryCraft/Textures/GUI/powertab.png";
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        ReikaTextureHelper.bindTexture(RotaryCraft.class, var4);
//        ScreenUtils.drawTexturedModalRect(imageWidth + var5, var6 + 4, 0, 4, 42, imageHeight - 4);
//
//        long frac = (comp.power * 29L) / comp.MINPOWER;
//        if (frac > 29)
//            frac = 29;
//        this.drawTexturedModalRect(imageWidth + var5 + 5, imageHeight + var6 - 144, 0, 0, (int) frac, 4);
//
//        frac = (int) (comp.omega * 29L) / comp.MINSPEED;
//        if (frac > 29)
//            frac = 29;
//        this.drawTexturedModalRect(imageWidth + var5 + 5, imageHeight + var6 - 84, 0, 0, (int) frac, 4);
//
//        frac = (int) (comp.torque * 29L) / comp.MINTORQUE;
//        if (frac > 29)
//            frac = 29;
//        this.drawTexturedModalRect(imageWidth + var5 + 5, imageHeight + var6 - 24, 0, 0, (int) frac, 4);
//
//        api.drawCenteredStringNoShadow(font, "Power:", imageWidth + var5 + 20, var6 + 9, 0xff000000);
//        api.drawCenteredStringNoShadow(font, "Speed:", imageWidth + var5 + 20, var6 + 69, 0xff000000);
//        api.drawCenteredStringNoShadow(font, "Torque:", imageWidth + var5 + 20, var6 + 129, 0xff000000);
//        //this.drawCenteredStringNoShadow(font, String.format("%d/%d", comp.power, comp.MINPOWER), imageWidth+var5+16, var6+16, 0xff000000);
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return "compactorgui2";
//    }
//}
