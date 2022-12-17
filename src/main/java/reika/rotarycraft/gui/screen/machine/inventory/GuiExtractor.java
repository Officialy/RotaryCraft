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
//import reika.rotarycraft.gui.container.Machine.Inventory.ContainerExtractor;
//
//import reika.rotarycraft.blockentities.processing.BlockEntityExtractor;
//
//public class GuiExtractor extends GuiMachine {
//    private final BlockEntityExtractor ext;
//
//    public GuiExtractor(Player p5ep, BlockEntityExtractor Extractor) {
//        super(new ContainerExtractor(p5ep, Extractor), Extractor);
//        ext = Extractor;
//        ep = p5ep;
//    }
//
//    @Override
//    protected void drawGuiContainerForegroundLayer(int a, int b) {
//        super.drawGuiContainerForegroundLayer(a, b);
//
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//        if (RotaryConfig.COMMON.EXTRACTORMAINTAIN.getState()) {
//            int dx = j + 19;
//            int dy = k + 33;
//            if (api.isMouseInBox(dx, dx + 4, dy, dy + 18)) {
//                String s = String.format("Drill Status: %d%%", ext.getDrillLifeScaled(100));
//                api.drawTooltipAt(font, s, api.getMouseRealX() - 35, api.getMouseRealY() - 15);
//            }
//
//            String var4 = "/Reika/RotaryCraft/Textures/GUI/extractorgui2.png";
//            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//            ReikaTextureHelper.bindTexture(RotaryCraft.class, var4);
//            stack.pushPose();
//            stack.translate(0, 0, 200);
//            RenderSystem.enableBlend();
//            GL11.glColor4f(1, 1, 1, 0.5F);
//            ScreenUtils.drawTexturedModalRect(25, 33, 25, 33, 18, 18);
//            RenderSystem.disableBlend();
//
//            int i1 = Math.min(32, ext.getCookProgressScaled(32, 0));
//            ScreenUtils.drawTexturedModalRect(29, 34, 176, 48, 10, i1);
//            stack.popPose();
//        }
//
//        String var4 = RotaryConfig.COMMON.EXTRACTORMAINTAIN.get() ? "/Reika/RotaryCraft/Textures/GUI/extractorgui2.png" : "/Reika/RotaryCraft/Textures/GUI/extractorgui.png";
//        ReikaTextureHelper.bindTexture(RotaryCraft.class, var4);
//        int u = ext.isBedrock() ? 190 : 182;
//        int v = 142;
//        int dx = 10;
//        int dy = 33;
//        ScreenUtils.drawTexturedModalRect(dx, dy, u, v, 7, 18);
//        if (api.isMouseInBox(j + dx, j + dx + 7, k + dy, k + dy + 18)) {
//            String s = ext.isBedrock() ? "Bedrock Drill" : "Steel Drill";
//            api.drawTooltipAt(font, s, api.getMouseRealX() - font.width(s), api.getMouseRealY() - 12);
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
//        int i2 = Math.min(28, ext.getCookProgressScaled(28, 1));
//        int i3 = Math.min(28, ext.getCookProgressScaled(28, 2));
//        int i4 = Math.min(32, ext.getCookProgressScaled(32, 3));
//        if (!RotaryConfig.COMMON.EXTRACTORMAINTAIN.getState()) {
//            int i1 = Math.min(32, ext.getCookProgressScaled(32, 0));
//            ScreenUtils.drawTexturedModalRect(j + 29, k + 34, 176, 48, 10, i1);
//        }
//        ScreenUtils.drawTexturedModalRect(j + 63, k + 35, 186, 48, 14, i2);
//        this.drawTexturedModalRect(j + 99, k + 35, 200, 48, 14, i3);
//        this.drawTexturedModalRect(j + 133, k + 49 - i4, 176, 79 - i4, 17, i4);
//
//        if (RotaryConfig.COMMON.EXTRACTORMAINTAIN.getState()) {
//            int i5 = ext.getDrillLifeScaled(16);
//            this.drawTexturedModalRect(j + 20, k + 50 - i5, 178, 159 - i5, 2, i5);
//        }
//    }
//
//    @Override
//    protected void drawPowerTab(int var5, int var6) {
//        String var4 = "/Reika/RotaryCraft/Textures/GUI/powertab.png";
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        ReikaTextureHelper.bindTexture(RotaryCraft.class, var4);
//        this.drawTexturedModalRect(imageWidth + var5, var6 + 4, 42, 4, 42, imageHeight - 4);
//
//        for (int i = 0; i < 4; i++) {
//            int frac = (int) ((ext.power * 31L) / ext.machine.getMinPower(i));
//            if (frac > 31)
//                frac = 31;
//            this.drawTexturedModalRect(imageWidth + var5 + 7 + 7 * i, imageHeight + var6 - 144 + 31 - frac, 0, 200 - frac, 5, frac);
//        }
//
//        for (int i = 0; i < 4; i++) {
//            int frac = ext.omega * 31 / ext.machine.getMinSpeed(i);
//            if (frac > 31)
//                frac = 31;
//            this.drawTexturedModalRect(imageWidth + var5 + 7 + 7 * i, imageHeight + var6 - 93 + 31 - frac, 0, 200 - frac, 5, frac);
//        }
//
//        for (int i = 0; i < 4; i++) {
//            int frac = ext.torque * 31 / ext.machine.getMinTorque(i);
//            if (frac > 31)
//                frac = 31;
//            this.drawTexturedModalRect(imageWidth + var5 + 7 + 7 * i, imageHeight + var6 - 42 + 31 - frac, 0, 200 - frac, 5, frac);
//        }
//
//        api.drawCenteredStringNoShadow(font, "Power:", imageWidth + var5 + 20, var6 + 9, 0xff000000);
//        api.drawCenteredStringNoShadow(font, "Speed:", imageWidth + var5 + 20, var6 + 60, 0xff000000);
//        api.drawCenteredStringNoShadow(font, "Torque:", imageWidth + var5 + 20, var6 + 111, 0xff000000);
//        //this.drawCenteredStringNoShadow(font, String.format("%d/%d", ext.power, ext.MINPOWER), imageWidth+var5+16, var6+16, 0xff000000);
//    }
//
//    @Override
//    protected void mouseClicked(int x, int y, int button) {
//        super.mouseClicked(x, y, button);
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;/*
//		int slot = (x-inventorySlots.getSlot(0).xDisplayPosition-j)/36;
//		boolean isSlot = ((x-inventorySlots.getSlot(0).xDisplayPosition-j)/18)%2 == 0;
//		int dy = y-k;
//		//ReikaJavaLibrary.pConsole(x+":"+y+":"+slot+":"+isSlot+":"+dy);
//		if (ReikaMathLibrary.isValueInsideBoundsIncl(2, 13, dy)) {
//			ext.extractableSlots[slot] = !ext.extractableSlots[slot];
//		}*/
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return RotaryConfig.COMMON.EXTRACTORMAINTAIN.get() ? "extractorgui2" : "extractorgui";
//    }
//}
