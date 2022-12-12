///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.guis.Machine;
//
//import org.lwjgl.input.Keyboard;
//import org.lwjgl.opengl.GL11;
//
//import net.minecraft.entity.player.Player;
//
//import reika.dragonapi.base.CoreMenu;
//import reika.dragonapi.libraries.io.ReikaPacketHelper;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.GuiPowerOnlyMachine;
//import reika.rotarycraft.registry.PacketRegistry;
//import reika.rotarycraft.blockentities.surveying.BlockEntityGPR;
//
//public class GuiGPR extends GuiPowerOnlyMachine {
//
//    public static final int UNIT = 2;
//    private final BlockEntityGPR gpr;
//    private boolean pressL;
//    private boolean pressR;
//    private boolean pressC;
//
//    public GuiGPR(Player p5ep, BlockEntityGPR GPR) {
//        super(new CoreMenu(p5ep, GPR), GPR);
//        gpr = GPR;
//        imageHeight = 215;
//        ep = p5ep;
//    }
//
//    @Override
//    public void updateScreen() {
//        super.updateScreen();
//
//        boolean keyL = Keyboard.isKeyDown(Keyboard.KEY_LBRACKET);
//        boolean keyR = Keyboard.isKeyDown(Keyboard.KEY_RBRACKET);
//        boolean keyC = Keyboard.isKeyDown(Keyboard.KEY_BACKSLASH);
//
//        if (keyL && !pressL) {
//            ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.GPR.ordinal(), gpr, 1);
//            gpr.shift(gpr.getGuiDirection(), 1);
//            pressL = true;
//        } else if (keyL && pressL) {
//            pressL = false;
//        }
//
//        if (keyR && !pressR) {
//            ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.GPR.ordinal(), gpr, -1);
//            gpr.shift(gpr.getGuiDirection(), -1);
//            pressR = true;
//        } else if (keyR && pressR) {
//            pressR = false;
//        }
//
//        if (keyC && !pressC) {
//            ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.GPR.ordinal(), gpr, 0);
//            gpr.shift(gpr.getGuiDirection(), 0);
//            pressC = true;
//        } else if (keyC && pressC) {
//            pressC = false;
//        }
//    }
//
//    @Override
//    protected void renderBg(PoseStack poseStack, float par1, int par2, int par3) {
//        super.renderBg(PoseStack poseStack, par1, par2, par3);
//
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2 + 1;
//
//        this.drawRadar(j, k);
//    }
//
//    private void drawRadar(int a, int b) {
//        int r = gpr.getRange();
//        for (int x = -r; x <= r; x++) {
//            for (int dd = 1; dd <= BlockEntityGPR.MAX_HEIGHT; dd++) {
//                int color = 0xff000000 | gpr.getColor(x, dd);
//                int x0 = a + 7 + UNIT * (x + BlockEntityGPR.MAX_WIDTH / 2);
//                int y0 = b + 16 + UNIT * dd - 2;
//                this.drawRect(x0, y0, x0 + UNIT, y0 + UNIT, color);
//            }
//        }
//
//        stack.pushPose();
//        double d = 0.5;
//        stack.scale(d, d, d);
//        String s = gpr.getLookDirection().toString();
//        int w = font.width(s);
//        font.drawStringWithShadow(s, (int) ((a + w / 2) / d), (int) ((b + 16) / d), 0xffffff);
//        stack.popPose();
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return "gprgui";
//    }
//}
