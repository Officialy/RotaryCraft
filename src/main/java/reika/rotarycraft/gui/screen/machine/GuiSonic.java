///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.gui.screen.machine;
//
//import net.minecraft.world.entity.player.Player;
//import reika.dragonapi.base.CoreMenu;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.GuiPowerOnlyMachine;
//import reika.rotarycraft.registry.PacketRegistry;
//
//public class GuiSonic extends GuiPowerOnlyMachine {
//    private final BlockEntitySonicWeapon sonic;
//    //private Level level = ModLoader.getMinecraftInstance().theWorld;
//
//    int x;
//    int y;
//    private GuiTextField input;
//    private GuiTextField input2;
//
//    private long freq;
//    private long vol;
//
//    private int dB;
//
//    public GuiSonic(Player p5ep, BlockEntitySonicWeapon SonicWeapon) {
//        super(new CoreMenu(p5ep, SonicWeapon), SonicWeapon);
//        sonic = SonicWeapon;
//        imageHeight = 92;
//        if (!BlockEntitySonicWeapon.DECIBELMODE)
//            imageWidth = 235;
//        if (!BlockEntitySonicWeapon.ENABLEFREQ)
//            imageHeight = 56;
//        ep = p5ep;
//        vol = SonicWeapon.setvolume;
//        freq = SonicWeapon.setpitch;
//
//        dB = (int) (10 * Math.log10(vol));
//        if (dB < 0)
//            dB = 0;
//
//        //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d", this.freq));
//    }
//
//    @Override
//    public void initGui() {
//        super.initGui();
//        int j = (width - imageWidth) / 2 + 8;
//        int k = (height - imageHeight) / 2 - 12;
//        int xo = 0;
//        if (BlockEntitySonicWeapon.DECIBELMODE)
//            xo = 48;
//        input = new GuiTextField(font, j + imageWidth / 2 - 62, k + 73, 86, 16);
//        input.setFocused(false);
//        input.setMaxStringLength(12);
//        input2 = new GuiTextField(font, j + imageWidth / 2 - 62 + xo, k + 43, 86 - xo, 16);
//        input2.setFocused(false);
//        if (BlockEntitySonicWeapon.DECIBELMODE)
//            input2.setMaxStringLength(3);
//        else
//            input2.setMaxStringLength(12);
//    }
//
//    @Override
//    protected void keyTyped(char c, int i) {
//        super.keyTyped(c, i);
//        if (BlockEntitySonicWeapon.ENABLEFREQ)
//            input.textboxKeyTyped(c, i);
//        input2.textboxKeyTyped(c, i);
//    }
//
//    @Override
//    protected void mouseClicked(int i, int j, int k) {
//        super.mouseClicked(i, j, k);
//        if (BlockEntitySonicWeapon.ENABLEFREQ)
//            input.mouseClicked(i, j, k);
//        input2.mouseClicked(i, j, k);
//    }
//
//    @Override
//    public void updateScreen() {
//        super.updateScreen();
//        boolean valid1 = true;
//        boolean valid2 = true;
//        x = Mouse.getX();
//        y = Mouse.getY();
//        if (input.getText().isEmpty() && input2.getText().isEmpty()) {
//            return;
//        }
//        if (input.getText().isEmpty())
//            valid1 = false;
//        if (input2.getText().isEmpty())
//            valid2 = false;
//        if (!input.getText().isEmpty() && !(input.getText().matches("^[0-9 ]+$"))) {
//            freq = 0;
//            input.deleteFromCursor(-1);
//            ReikaPacketHelper.sendLongDataPacket(RotaryCraft.packetChannel, PacketRegistry.SONICPITCH.ordinal(), sonic, freq);
//            valid1 = false;
//        }
//        if (!input2.getText().isEmpty() && !(input2.getText().matches("^[0-9 ]+$"))) {
//            dB = 0;
//            this.getVolFromdB();
//            input2.deleteFromCursor(-1);
//            ReikaPacketHelper.sendLongDataPacket(RotaryCraft.packetChannel, PacketRegistry.SONICVOLUME.ordinal(), sonic, vol);
//            valid2 = false;
//        }
//        if (!valid1 && !valid2)
//            return;
//        //ModLoader.getMinecraftInstance().thePlayer.addChatMessage("435");
//        //System.out.println(input.getText());
//        if (valid1) {
//            freq = Long.parseLong(input.getText());
//            if (freq >= 0)
//                ReikaPacketHelper.sendLongDataPacket(RotaryCraft.packetChannel, PacketRegistry.SONICPITCH.ordinal(), sonic, freq);
//        }
//        if (valid2) {
//            dB = ReikaJavaLibrary.safeIntParse(input2.getText());
//            if (dB >= 0) {
//                this.getVolFromdB();
//                ReikaPacketHelper.sendLongDataPacket(RotaryCraft.packetChannel, PacketRegistry.SONICVOLUME.ordinal(), sonic, vol);
//            }
//        }
//    }
//
//    public void getVolFromdB() {
//        double d = dB / 10D;
//        d = ReikaMathLibrary.doubpow(10, d);
//        //ReikaJavaLibrary.pConsole(d);
//        vol = (long) (d);
//    }
//
//    /**
//     * Draw the foreground layer for the GuiContainer (everything in front of the items)
//     */
//    @Override
//    protected void drawGuiContainerForegroundLayer(int a, int b) {
//        int xo = 0;
//        if (BlockEntitySonicWeapon.DECIBELMODE)
//            xo = 48;
//
//        super.drawGuiContainerForegroundLayer(a, b);
//
//        font.draw("Volume:", imageWidth / 2 - 92 + xo, 35, 4210752);
//        if (BlockEntitySonicWeapon.ENABLEFREQ)
//            font.draw("Frequency:", imageWidth / 2 - 113, 65, 4210752);
//        if (BlockEntitySonicWeapon.ENABLEFREQ) {
//            if (!input.isFocused())
//                font.draw(String.format("%d", sonic.setpitch), imageWidth / 2 - 50, 65, 0xffffffff);
//        }
//        if (!input2.isFocused()) {
//            if (BlockEntitySonicWeapon.DECIBELMODE)
//                font.draw(String.format("%d", dB), imageWidth / 2 - 50 + xo, 35, 0xffffffff);
//            else
//                font.draw(String.format("%d", sonic.setvolume), imageWidth / 2 - 50 + xo, 35, 0xffffffff);
//        }
//
//
//        if (BlockEntitySonicWeapon.DECIBELMODE)
//            font.draw("dB", imageWidth / 2 + 38, 35, 4210752);
//        else
//            font.draw("W/m^2", imageWidth / 2 + 38, 35, 4210752);
//        if (BlockEntitySonicWeapon.ENABLEFREQ)
//            font.draw("Hz", imageWidth / 2 + 38, 65, 4210752);
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
//        if (BlockEntitySonicWeapon.ENABLEFREQ)
//            input.drawTextBox();
//        input2.drawTextBox();
//
//        int color = 4210752;
//        if (vol > sonic.getMaxVolume())
//            color = 0xff0000;
//        //ImageButton.drawCenteredStringNoShadow(font, String.format("(%d)", sonic.getVolume()), j+imageWidth/2+85, k+65, color);
//        color = 4210752;
//        this.drawPowerTab(j, k);
//        if (!BlockEntitySonicWeapon.ENABLEFREQ)
//            return;
//        if (freq > sonic.getMaxPitch())
//            color = 0xff0000;
//        //ImageButton.drawCenteredStringNoShadow(font, String.format("(%d)", sonic.getPitch()), j+imageWidth/2+85, k+35, color);
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return "sonicgui3";
//    }
//}
