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
//public class GuiSpawnerController extends GuiPowerOnlyMachine {
//
//    private final BlockEntitySpawnerController spawnercontroller;
//    //private Level level = ModLoader.getMinecraftInstance().theWorld;
//    int x;
//    int y;
//    boolean hasPower;
//    private int timer;
//    private boolean disabled;
//    private GuiTextField input;
//
//    public GuiSpawnerController(Player p5ep, BlockEntitySpawnerController spw) {
//        super(new CoreMenu(p5ep, spw), spw);
//        spawnercontroller = spw;
//        imageHeight = 75;
//        ep = p5ep;
//        timer = spawnercontroller.getDelay();
//        disabled = spawnercontroller.disable;
//        hasPower = (spawnercontroller.power >= spawnercontroller.machine.getMinPower());
//    }
//
//    @Override
//    public void initGui() {
//        super.initGui();
//        int j = (width - imageWidth) / 2 + 8;
//        int k = (height - imageHeight) / 2 - 12;
//        if (hasPower) {
//            addRenderableWidget(new Button(0, j + imageWidth / 2 - 48, -1 + k + 32, 80, 20, "Disable/Enable"));
//            input = new GuiTextField(font, j + imageWidth / 2 - 7, k + 59, 26, 16);
//            input.setFocused(false);
//            input.setMaxStringLength(3);
//        }
//    }
//
//    @Override
//    protected void keyTyped(char c, int i) {
//        super.keyTyped(c, i);
//        if (hasPower)
//            input.textboxKeyTyped(c, i);
//    }
//
//    @Override
//    protected void mouseClicked(int i, int j, int k) {
//        super.mouseClicked(i, j, k);
//        if (hasPower)
//            input.mouseClicked(i, j, k);
//    }
//
//    @Override
//    protected void actionPerformed(Button button) {
//        super.actionPerformed(button);
//        if (button.id == 0) {
//            disabled = !spawnercontroller.disable;
//        }
//        int dat;
//        if (disabled)
//            dat = -1;
//        else
//            dat = timer;
//        ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.SPAWNERTIMER.ordinal(), spawnercontroller, dat);
//    }
//
//    @Override
//    public void updateScreen() {
//        super.updateScreen();
//        x = Mouse.getX();
//        y = Mouse.getY();
//        if (hasPower) {
//            if (input.getText().isEmpty()) {
//                return;
//            }
//            if (!(input.getText().matches("^[0-9 ]+$"))) {
//                timer = BlockEntitySpawnerController.BASEDELAY;
//                input.deleteFromCursor(-1);
//                int dat;
//                if (disabled)
//                    dat = -1;
//                else
//                    dat = timer;
//                ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.SPAWNERTIMER.ordinal(), spawnercontroller, dat);
//                return;
//            }
//            //ModLoader.getMinecraftInstance().thePlayer.addChatMessage("435");
//            //System.out.println(input.getText());
//            timer = ReikaJavaLibrary.safeIntParse(input.getText());
//            int dat;
//            if (disabled)
//                dat = -1;
//            else
//                dat = timer;
//            if (timer >= 0)
//                ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.SPAWNERTIMER.ordinal(), spawnercontroller, dat);
//        }
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
//        if (hasPower) {
//            int color = 4210752;
//            if (disabled)
//                color = 0xcccccc;
//            font.draw("Spawn Delay:", imageWidth / 2 - 64, 51, color);
//            if (!input.isFocused() && !disabled) {
//                font.draw(String.format("%d", spawnercontroller.getDelay()), imageWidth / 2 + 5, 51, 0xffffffff);
//            }
//        }
//    }
//
//    /**
//     * Draw the background layer for the GuiContainer (everything behind the items)
//     */
//    @Override
//    protected void renderBg(PoseStack poseStack,float par1, int par2, int par3) {
//        super.renderBg(PoseStack poseStack,par1, par2, par3);
//
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//        if (!hasPower) {
//            String i = "/Reika/RotaryCraft/Textures/GUI/spawnercontrollergui.png";
//            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//            ReikaTextureHelper.bindTexture(RotaryCraft.class, i);
//            ScreenUtils.drawTexturedModalRect(j, k, 0, imageHeight, imageWidth, imageHeight);
//        }
//
//        if (hasPower) {
//            if (!disabled)
//                input.drawTextBox();
//            int color = 4210752;
//            if (timer < spawnercontroller.getOperationTime())
//                color = 0xff0000;
//            if (disabled) {
//                color = 0xaaaaaa;
//                api.drawCenteredStringNoShadow(font, "Infinity", j + imageWidth / 2 + 28, k + 51, color);
//            } else
//                api.drawCenteredStringNoShadow(font, String.format("(%d)", spawnercontroller.getDelay()), j + imageWidth / 2 + 58, k + 51, color);
//        }
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return "spawnercontrollergui";
//    }
//
//}
