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
//import net.minecraft.entity.player.Player;
//
//import reika.rotarycraft.base.GuiPowerOnlyMachine;
//import reika.rotarycraft.containers.Machine.Inventory.ContainerDefoliator;
//import reika.rotarycraft.blockentities.level.BlockEntityDefoliator;
//
//public class GuiDefoliator extends GuiPowerOnlyMachine {
//    private final BlockEntityDefoliator tile;
//    int x;
//    int y;
//
//    public GuiDefoliator(Player p5ep, BlockEntityDefoliator te) {
//        super(new ContainerDefoliator(p5ep, te), te);
//        tile = te;
//        imageWidth = 176;
//        imageHeight = 166;
//        ep = p5ep;
//    }
//
//    @Override
//    protected void drawGuiContainerForegroundLayer(int a, int b) {
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//        super.drawGuiContainerForegroundLayer(a, b);
//        int x = api.getMouseRealX();
//        int y = api.getMouseRealY();
//        if (api.isMouseInBox(j + 133, j + 150, k + 16, k + 69)) {
//            int lvl = tile.getLevel();
//            String sg = String.format("Poison: %d/%d", lvl, BlockEntityDefoliator.CAPACITY);
//            api.drawTooltipAt(font, sg, x - j, y - k);
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
//        int i1 = tile.getPoisonScaled(52);
//        ScreenUtils.drawTexturedModalRect(j + 134, k + 69 - i1, 177, 69 - i1, 16, i1);
//
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return "defoliatorgui";
//    }
//}
