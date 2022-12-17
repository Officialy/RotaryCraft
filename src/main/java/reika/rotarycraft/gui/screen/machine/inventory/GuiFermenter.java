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
//import net.minecraftforge.fluids.Fluids;
//
//import reika.dragonapi.libraries.io.ReikaTextureHelper;
//import reika.dragonapi.libraries.rendering.ReikaLiquidRenderer;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.auxiliary.RotaryItems;
//import reika.rotarycraft.base.GuiMachine;
//import reika.rotarycraft.gui.container.Machine.Inventory.ContainerFermenter;
//
//import reika.rotarycraft.blockentities.production.BlockEntityFermenter;
//
//public class GuiFermenter extends GuiMachine {
//    private final BlockEntityFermenter ferm;
//
//    public GuiFermenter(Player p5ep, BlockEntityFermenter Fermenter) {
//        super(new ContainerFermenter(p5ep, Fermenter), Fermenter);
//        ferm = Fermenter;
//        ep = p5ep;
//    }
//
//    @Override
//    protected void drawGuiContainerForegroundLayer(int a, int b) {
//        super.drawGuiContainerForegroundLayer(a, b);
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//        boolean red = ferm.hasRedstoneSignal();
//        int sx = 154;
//        int sy = 6;
//        if (red) {
//            api.drawItemStack(itemRender, font, RotaryItems.sludge, sx, sy);
//        } else {
//            api.drawItemStack(itemRender, font, RotaryItems.YEAST.get(), sx, sy);
//        }
//        font.draw("Target", 119, 10, 0);
//
//        if (api.isMouseInBox(sx + j, sx + 16 + j, sy + k, sy + 16 + k)) {
//            int dy = 13;
//            api.drawTooltipAt(font, "This controls automation.", api.getMouseRealX() - j, api.getMouseRealY() - k);
//        }
//
//        sx = 55;
//        sy = 35;
//        if (api.isMouseInBox(sx + j - 1, sx + 16 + j + 1, sy + k - 1, sy + 16 + k + 1)) {
//            int dy = 13;
//            api.drawTooltipAt(font, String.format("Water: %d/%d mB", ferm.getLevel(), BlockEntityFermenter.CAPACITY), api.getMouseRealX() - j, api.getMouseRealY() - k);
//        }
//
//        GL11.glColor4f(1, 1, 1, 1);
//        ReikaLiquidRenderer.bindFluidTexture(Fluids.WATER);
//        int h = 16 * ferm.getLevel() / BlockEntityFermenter.CAPACITY;
//        int dy = red ? 18 : 0;
//        dy = 0;
//        this.drawTexturedModelRectFromIcon(sx, sy + 16 - h + dy, Fluids.WATER.getIcon(), 16, h);
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
//        int i1 = ferm.getCookProgressScaled(48);
//        if (i1 > 48)
//            i1 = 48;
//        ScreenUtils.drawTexturedModalRect(j + 79, k + 34, 176, 14, 1 * (i1) + 1, 16);
//
//        int i2 = ferm.getTemperatureScaled(54);
//        if (i2 > 54)
//            i2 = 54;
//        ScreenUtils.drawTexturedModalRect(j + 24, k + 70 - i2, 177, 86 - i2, 9, i2);
//    }
//
//    @Override
//    protected void drawPowerTab(int var5, int var6) {
//        String var4 = "/Reika/RotaryCraft/Textures/GUI/powertab.png";
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        ReikaTextureHelper.bindTexture(RotaryCraft.class, var4);
//        this.drawTexturedModalRect(imageWidth + var5, var6 + 4, 0, 4, 42, imageHeight - 4);
//
//        long frac = (ferm.power * 29L) / ferm.MINPOWER;
//        if (frac > 29)
//            frac = 29;
//        this.drawTexturedModalRect(imageWidth + var5 + 5, imageHeight + var6 - 144, 0, 0, (int) frac, 4);
//
//        frac = (int) (ferm.omega * 29L) / ferm.MINSPEED;
//        if (frac > 29)
//            frac = 29;
//        this.drawTexturedModalRect(imageWidth + var5 + 5, imageHeight + var6 - 84, 0, 0, (int) frac, 4);
//
//        frac = (int) (ferm.torque * 29L) / ferm.MINTORQUE;
//        if (frac > 29)
//            frac = 29;
//        this.drawTexturedModalRect(imageWidth + var5 + 5, imageHeight + var6 - 24, 0, 0, (int) frac, 4);
//
//        api.drawCenteredStringNoShadow(font, "Power:", imageWidth + var5 + 20, var6 + 9, 0xff000000);
//        api.drawCenteredStringNoShadow(font, "Speed:", imageWidth + var5 + 20, var6 + 69, 0xff000000);
//        api.drawCenteredStringNoShadow(font, "Torque:", imageWidth + var5 + 20, var6 + 129, 0xff000000);
//        //this.drawCenteredStringNoShadow(font, String.format("%d/%d", ferm.power, ferm.MINPOWER), imageWidth+var5+16, var6+16, 0xff000000);
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return "fermentergui";
//    }
//}
