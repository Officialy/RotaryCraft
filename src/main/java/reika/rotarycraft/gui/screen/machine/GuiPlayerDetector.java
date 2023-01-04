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
//import reika.rotarycraft.base.GuiNonPoweredMachine;
//import reika.rotarycraft.registry.PacketRegistry;
//import reika.rotarycraft.blockentities.BlockPlayerDetector;
//
//public class GuiPlayerDetector extends GuiNonPoweredMachine {
//    private final BlockPlayerDetector playerdetector;
//    public int range;
//    //private Level level = ModLoader.getMinecraftInstance().theWorld;
//
//    int x;
//    int y;
//    private GuiTextField input;
//
//    public GuiPlayerDetector(Player p5ep, BlockPlayerDetector PlayerDetector) {
//        super(new CoreMenu(p5ep, PlayerDetector), PlayerDetector);
//        playerdetector = PlayerDetector;
//        imageHeight = 46;
//        ep = p5ep;
//        range = playerdetector.selectedrange;
//        //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d", this.range));
//    }
//
//    @Override
//    public void init() {
//        super.init();
//        int j = (width - imageWidth) / 2 + 8;
//        int k = (height - imageHeight) / 2 - 12;
//        input = new GuiTextField(font, j + imageWidth / 2 - 6, k + 33, 26, 16);
//        input.setFocused(false);
//        input.setMaxStringLength(3);
//    }
//
//    @Override
//    protected void keyTyped(char c, int i) {
//        super.keyTyped(c, i);
//        input.textboxKeyTyped(c, i);
//    }
//
//    @Override
//    protected void mouseClicked(int i, int j, int k) {
//        super.mouseClicked(i, j, k);
//        input.mouseClicked(i, j, k);
//    }
//
//    @Override
//    public void updateScreen() {
//        super.updateScreen();
//        x = Mouse.getX();
//        y = Mouse.getY();
//        if (input.getText().isEmpty()) {
//            return;
//        }
//        if (!(input.getText().matches("^[0-9 ]+$"))) {
//            range = 0;
//            input.deleteFromCursor(-1);
//            ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.DETECTOR.ordinal(), playerdetector, range);
//            return;
//        }
//        range = ReikaJavaLibrary.safeIntParse(input.getText());
//        if (range >= 0)
//            ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.DETECTOR.ordinal(), playerdetector, range);
//    }
//
//    @Override
//    protected void drawGuiContainerForegroundLayer(int a, int b) {
//        super.drawGuiContainerForegroundLayer(a, b);
//
//        font.draw("Detection Range:", imageWidth / 2 - 82, 25, 4210752);
//        if (!input.isFocused()) {
//            font.draw(String.format("%d", playerdetector.selectedrange), imageWidth / 2 + 6, 25, 0xffffffff);
//        }
//    }
//
//    @Override
//    protected void renderBg(PoseStack poseStack, float par1, int par2, int par3) {
//        super.renderBg(PoseStack poseStack, par1, par2, par3);
//
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//        input.drawTextBox();
//        int color = 4210752;
//        if (range > playerdetector.getMaxRange())
//            color = 0xff0000;
//        api.drawCenteredStringNoShadow(font, String.format("(%d)", playerdetector.getRange()), j + imageWidth / 2 + 58, k + 25, color);
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return "rangegui";
//    }
//
//}
