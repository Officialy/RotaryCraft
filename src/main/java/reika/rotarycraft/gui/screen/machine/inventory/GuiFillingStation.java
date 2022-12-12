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
//
//import reika.dragonapi.libraries.rendering.ReikaColorAPI;
//import reika.dragonapi.libraries.rendering.ReikaLiquidRenderer;
//import reika.rotarycraft.base.GuiPowerOnlyMachine;
//import reika.rotarycraft.containers.Machine.Inventory.ContainerFillingStation;
//import reika.rotarycraft.blockentities.auxiliary.BlockEntityFillingStation;
//
//public class GuiFillingStation extends GuiPowerOnlyMachine {
//
//    private final BlockEntityFillingStation fillingStation;
//    //private Level level = ModLoader.getMinecraftInstance().theWorld;
//
//    public GuiFillingStation(Player p5ep, BlockEntityFillingStation te) {
//        super(new ContainerFillingStation(p5ep, te), te);
//        fillingStation = te;
//        imageWidth = 176;
//        imageHeight = 187;
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
//        if (api.isMouseInBox(j + 81, j + 94, k + 20, k + 87)) {
//            int mx = api.getMouseRealX();
//            int my = api.getMouseRealY();
//            api.drawTooltipAt(font, String.format("%d/%d mB", fillingStation.getLevel(), BlockEntityFillingStation.CAPACITY), mx - j, my - k);
//        }
//
//        if (!fillingStation.isEmpty()) {
//            int i2 = fillingStation.getLiquidScaled(66);
//            int x = 82;
//            int y = 87 - i2;
//            IIcon ico = ReikaLiquidRenderer.getFluidIconSafe(fillingStation.getFluid());
//            ReikaLiquidRenderer.bindFluidTexture(fillingStation.getFluid());
//            int clr = 0xffffffff;
//            if (fillingStation.getFluid().canBePlacedInWorld()) {
//                clr = fillingStation.getFluid().getBlock().colorMultiplier(fillingStation.level, fillingStation.xCoord * 2, fillingStation.yCoord * 2, fillingStation.zCoord * 2);
//            }
//            GL11.glColor4f(ReikaColorAPI.HextoColorMultiplier(clr, 0), ReikaColorAPI.HextoColorMultiplier(clr, 1), ReikaColorAPI.HextoColorMultiplier(clr, 2), 1);
//            this.drawTexturedModelRectFromIcon(x, y, ico, 12, i2);
//        }
//    }
//
//    /**
//     * Draw the background layer for the GuiContainer (everything behind the items)
//     */
//    @Override
//    protected void renderBg(PoseStack poseStack, float par1, int par2, int par3) {
//        super.renderBg(PoseStack poseStack, par1, par2, par3);
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return "fillingstationgui";
//    }
//
//}
