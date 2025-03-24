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
//import net.minecraft.world.level.material.Fluid;
//import net.minecraftforge.client.gui.ScreenUtils;
//import org.lwjgl.opengl.GL11;
//
//import reika.rotarycraft.base.GuiNonPoweredMachine;
//
//public class GuiDryer extends GuiNonPoweredMachine {
//    private final BlockEntityDryingBed te;
//
//    public GuiDryer(Inventory p5ep, BlockEntityDryingBed DryingBed) {
//        super(new ContainerDryingBed(p5ep, DryingBed), DryingBed);
//        te = DryingBed;
//        ep = p5ep;
//        imageWidth = 176;
//        imageHeight = 166;
//    }
//
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
//        }
//        if (api.isMouseInBox(j + 7, j + 24, k + 6, k + 79)) {
//            int mx = api.getMouseRealX();
//            int my = api.getMouseRealY();
//            api.drawTooltipAt(font, String.format("%d/%d", te.getLevel(), te.getCapacity()), mx - j, my - k);
//        }
//    }
//
//    @Override
//    protected void renderBg(PoseStack poseStack, float par1, int par2, int par3) {
//        super.renderBg(PoseStack poseStack, par1, par2, par3);
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//
//        int i1 = te.getProgressScaled(91);
//        ScreenUtils.drawTexturedModalRect(j + 29, k + 41, 1, 169, i1, 4);
//    }
//
//    @Override
//    protected ResourceLocation getGuiTexture() {
//        return ResourceLocation.parse("drygui");
//    }
//}
