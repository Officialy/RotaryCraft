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
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.player.Inventory;
//import net.minecraft.world.level.material.Fluids;
//import net.minecraftforge.client.gui.ScreenUtils;
//import org.lwjgl.opengl.GL11;
//
//import reika.rotarycraft.base.GuiPowerOnlyMachine;
//import reika.rotarycraft.blockentities.processing.BlockEntityBigFurnace;
//
//public class GuiBigFurnace extends GuiPowerOnlyMachine {
//    private final BlockEntityBigFurnace te;
//
//    public GuiBigFurnace(Inventory p5ep, BlockEntityBigFurnace BigFurnace) {
//        super(new ContainerBigFurnace(p5ep, BigFurnace), BigFurnace);
//        te = BigFurnace;
//        ep = p5ep;
//        imageWidth = 190;
//        imageHeight = 207;
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
//        int c = 0;
//        if (te.getTemperature() >= 1000)
//            c = 4;
//        else if (te.getTemperature() >= 100)
//            c = 2;
//        api.drawCenteredStringNoShadow(font, te.getTemperature() + "C", imageWidth - 13 - c, 6, 4210752);
//
//        if (!te.isEmpty()) {
//            int i2 = te.getLavaScaled(91);
//            int x = 173;
//            int y = 108 - i2 + 1;
//            GL11.glColor4f(1, 1, 1, 1);
//            IIcon ico = Fluids.LAVA.getStillIcon();
//            ReikaLiquidRenderer.bindFluidTexture(Fluids.LAVA);
//            this.drawTexturedModelRectFromIcon(x, y, ico, 10, i2);
//        }
//        if (api.isMouseInBox(j + 172, j + 183, k + 17, k + 109)) {
//            int mx = api.getMouseRealX();
//            int my = api.getMouseRealY();
//            api.drawTooltipAt(font, String.format("%d/%d", te.getLevel(), te.getCapacity()), mx - j, my - k);
//        }
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
//        int i1 = te.getCookScaled(17);
//        ScreenUtils.drawTexturedModalRect(poseStack,j + 7, k + 55, 0, 208, 162, i1, 0);
//    }
//
//    @Override
//    protected ResourceLocation getGuiTexture() {
//        return new ResourceLocation("bigfurngui");
//    }
//}
