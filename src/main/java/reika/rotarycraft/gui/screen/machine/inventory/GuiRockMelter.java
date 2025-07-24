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
//import reika.dragonapi.libraries.rendering.ReikaLiquidRenderer;
//import reika.rotarycraft.base.GuiPowerOnlyMachine;
//import reika.rotarycraft.gui.container.Machine.Inventory.ContainerRockMelter;
//import reika.rotarycraft.blockentities.production.BlockEntityLavaMaker;
//
//public class GuiRockMelter extends GuiPowerOnlyMachine {
//
//    private final BlockEntityLavaMaker tile;
//
//    public GuiRockMelter(Player ep, BlockEntityLavaMaker te) {
//        super(new ContainerRockMelter(ep, te), te);
//        tile = te;
//        this.ep = ep;
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return "lavamakergui";
//    }
//
//    @Override
//    protected void drawGuiContainerForegroundLayer(int a, int b) {
//        super.drawGuiContainerForegroundLayer(a, b);
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//        int minx = j + 133;
//        int maxx = minx + 18;
//        int maxy = k + 75;
//        int miny = maxy - 66;
//        if (!tile.isEmpty()) {
//            if (api.isMouseInBox(minx, maxx, miny, maxy)) {
//                String sg = String.format("%s: %d/%d mB", tile.getContainedFluid().getLocalizedName(), tile.getLevel(), tile.getCapacity());
//                int mx = api.getMouseRealX();
//                int my = api.getMouseRealY();
//                api.drawTooltipAt(font, sg, mx - font.width(sg) - 8, my - 40);
//            }
//        }
//    }
//
//    @Override
//    protected void renderBg(PoseStack poseStack, float par1, int par2, int par3) {
//        super.renderBg(PoseStack poseStack, par1, par2, par3);
//
//        if (!tile.isEmpty()) {
//            int amt = tile.getLevel();
//            int cap = tile.getCapacity();
//            int frac = amt * 64 / cap;
//            int j = (width - imageWidth) / 2;
//            int k = (height - imageHeight) / 2;
//            GL11.glColor4f(1, 1, 1, 1);
//            Fluid f = tile.getContainedFluid();
//            IIcon ico = ReikaLiquidRenderer.getFluidIconSafe(f);
//            ReikaLiquidRenderer.bindFluidTexture(f);
//            int x = j + 134;
//            int y = k + 73 - frac + 1;
//            this.drawTexturedModelRectFromIcon(x, y, ico, 16, frac);
//        }
//
//    }
//
//    @Override
//    protected boolean inventoryLabelLeft() {
//        return true;
//    }
//
//}
