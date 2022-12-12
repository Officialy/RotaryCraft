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
//import java.io.ByteArrayOutputStream;
//import java.io.DataOutputStream;
//
//import org.lwjgl.input.Mouse;
//
//import net.minecraft.client.gui.GuiTextField;
//import net.minecraft.entity.player.Player;
//
//import reika.dragonapi.libraries.io.ReikaPacketHelper;
//import reika.dragonapi.libraries.java.ReikaJavaLibrary;
//import reika.dragonapi.libraries.mathsci.ReikaPhysicsHelper;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.GuiPowerOnlyMachine;
//import reika.rotarycraft.base.BlockEntity.BlockEntityLaunchCannon;
//import reika.rotarycraft.containers.Machine.Inventory.ContainerCannon;
//import reika.rotarycraft.registry.PacketRegistry;
//import reika.rotarycraft.blockentities.BlockEntityItemCannon;
//import reika.rotarycraft.blockentities.weaponry.BlockEntityTNTCannon;
//
//public class GuiCannon extends GuiPowerOnlyMachine {
//    private final BlockEntityLaunchCannon tnt;
//    private final boolean targetMode;
//    int x;
//    int y;
//    Player player;
//    private GuiTextField input;
//    private GuiTextField input2;
//    private GuiTextField input3;
//    private GuiTextField input4;
//    private double phi;
//    private double theta;
//    private double phid;
//    private double thetad;
//    private int velocity;
//    private int[] target = new int[3];
//    private int fuse;
//
//    public GuiCannon(Player p5ep, BlockEntityLaunchCannon Cannon) {
//        super(new ContainerCannon(p5ep, Cannon), Cannon);
//        tnt = Cannon;
//        phi = tnt.phi;
//        theta = tnt.theta;
//        velocity = tnt.velocity;
//        targetMode = tnt.targetMode;
//        if (targetMode) {
//            imageHeight = 170;
//            target = tnt.target;
//        } else {
//            imageWidth = 212;
//            imageHeight = 236;
//        }
//        thetad = theta;
//        phid = phi;
//        theta = ReikaPhysicsHelper.degToRad(theta);
//        phi = ReikaPhysicsHelper.degToRad(phi);
//        ep = p5ep;
//        fuse = tnt instanceof BlockEntityTNTCannon ? ((BlockEntityTNTCannon) tnt).selectedFuse : 0;
//    }
//
//    @Override
//    public void initGui() {
//        super.initGui();
//        int j = (width - imageWidth) / 2 + 8;
//        int k = (height - imageHeight) / 2 - 12;
//        //this.renderables.add(new Button(0, j+imageWidth/2-48, -1+k+32, 80, 20, "Trajectory"));
//        if (targetMode) {
//            input = new GuiTextField(font, j + imageWidth / 2, k + 26, 46, 16);
//            input.setFocused(false);
//            input.setMaxStringLength(6);
//            input2 = new GuiTextField(font, j + imageWidth / 2, k + 42, 46, 16);
//            input2.setFocused(false);
//            input2.setMaxStringLength(6);
//            input3 = new GuiTextField(font, j + imageWidth / 2, k + 58, 46, 16);
//            input3.setFocused(false);
//            input3.setMaxStringLength(6);
//
//            //offscreen
//            input4 = new GuiTextField(font, -100, -100, 0, 0);
//            input4.setFocused(false);
//            input4.setMaxStringLength(0);
//        } else {
//            input = new GuiTextField(font, j + imageWidth / 2 + 22 + 18, k + 104, 26, 16);
//            input.setFocused(false);
//            input.setMaxStringLength(3);
//            input2 = new GuiTextField(font, j + imageWidth / 2 - 65 - 18, k + 104, 26, 16);
//            input2.setFocused(false);
//            input2.setMaxStringLength(3);
//            input3 = new GuiTextField(font, j + imageWidth / 2 + 22 + 18, k + 124, 26, 16);
//            input3.setFocused(false);
//            input3.setMaxStringLength(3);
//            input4 = new GuiTextField(font, j + imageWidth / 2 - 49, k + 124, 26, 16);
//            input4.setFocused(false);
//            input4.setMaxStringLength(3);
//        }
//    }
//
//    @Override
//    protected void keyTyped(char c, int i) {
//        super.keyTyped(c, i);
//        input.textboxKeyTyped(c, i);
//        input2.textboxKeyTyped(c, i);
//        input3.textboxKeyTyped(c, i);
//        input4.textboxKeyTyped(c, i);
//    }
//
//    @Override
//    protected void mouseClicked(int i, int j, int k) {
//        super.mouseClicked(i, j, k);
//        input.mouseClicked(i, j, k);
//        input2.mouseClicked(i, j, k);
//        input3.mouseClicked(i, j, k);
//        input4.mouseClicked(i, j, k);
//    }
//
//    public void sendPacket() {
//        ByteArrayOutputStream bos = new ByteArrayOutputStream(32); // 8 ints
//        DataOutputStream outputStream = new DataOutputStream(bos);
//        try {
//            //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.valueOf(drops));
//            int b = 0;
//            if (tile instanceof BlockEntityLaunchCannon)
//                b = PacketRegistry.CANNONFIRINGVALS.ordinal();
//            if (tile instanceof BlockEntityItemCannon)
//                b = PacketRegistry.ITEMCANNON.ordinal();
//            //ReikaJavaLibrary.pConsole("Sending packet number "+b+" Type "+PacketRegistry.getEnum(b));
//            outputStream.writeInt(b);
//            if (targetMode) {
//                outputStream.writeInt(1);
//                outputStream.writeInt(target[0]);
//                outputStream.writeInt(target[1]);
//                outputStream.writeInt(target[2]);
//                outputStream.writeInt(fuse);
//            } else {
//                outputStream.writeInt(0);
//                outputStream.writeInt((int) phid);
//                outputStream.writeInt((int) thetad);
//                outputStream.writeInt(velocity);
//                outputStream.writeInt(fuse);
//            }
//            outputStream.writeInt(tile.xCoord);
//            outputStream.writeInt(tile.yCoord);
//            outputStream.writeInt(tile.zCoord);
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        ReikaPacketHelper.sendDataPacket(RotaryCraft.packetChannel, bos);
//    }
//
//    @Override
//    public void updateScreen() {
//        super.updateScreen();
//        boolean valid1 = true;
//        boolean valid2 = true;
//        boolean valid3 = true;
//        boolean valid4 = true;
//        x = Mouse.getX();
//        y = Mouse.getY();
//        if (input.getText().isEmpty() && input2.getText().isEmpty() && input3.getText().isEmpty() && input4.getText().isEmpty()) {
//            return;
//        }
//        if (input.getText().isEmpty())
//            valid1 = false;
//        if (input2.getText().isEmpty())
//            valid2 = false;
//        if (input3.getText().isEmpty())
//            valid3 = false;
//        if (input4.getText().isEmpty())
//            valid4 = false;
//        if (!input.getText().isEmpty() && !ReikaJavaLibrary.isValidInteger(input.getText())) {
//            if (targetMode)
//                target[0] = 0;
//            else
//                phid = 0;
//            input.deleteFromCursor(-1);
//            valid1 = false;
//        }
//        if (!input2.getText().isEmpty() && !ReikaJavaLibrary.isValidInteger(input2.getText())) {
//            if (targetMode)
//                target[1] = 0;
//            else
//                thetad = 0;
//            input2.deleteFromCursor(-1);
//            valid2 = false;
//        }
//        if (!input3.getText().isEmpty() && !ReikaJavaLibrary.isValidInteger(input3.getText())) {
//            if (targetMode)
//                target[2] = 0;
//            else
//                velocity = 0;
//            input3.deleteFromCursor(-1);
//            valid3 = false;
//        }
//        if (!input4.getText().isEmpty() && !ReikaJavaLibrary.isValidInteger(input4.getText())) {
//            fuse = 0;
//            input4.deleteFromCursor(-1);
//            valid4 = false;
//        }
//        this.sendPacket();
//        if (!valid1 && !valid2 && !valid3 && !valid4)
//            return;
//        if (input.getText().contentEquals("-"))
//            valid1 = false;
//        if (input2.getText().contentEquals("-"))
//            valid2 = false;
//        if (input3.getText().contentEquals("-"))
//            valid3 = false;
//        if (input4.getText().contentEquals("-"))
//            valid4 = false;
//        //ModLoader.getMinecraftInstance().thePlayer.addChatMessage("435");
//        //System.out.println(input.getText());
//        if (valid1) {
//            if (targetMode) {
//                target[0] = ReikaJavaLibrary.safeIntParse(input.getText());
//            } else {
//                phid = ReikaJavaLibrary.safeIntParse(input.getText());
//                while (phid > 360) {
//                    phid -= 360;
//                }
//            }
//        }
//        if (valid2) {
//            if (targetMode) {
//                target[1] = ReikaJavaLibrary.safeIntParse(input2.getText());
//            } else {
//                thetad = ReikaJavaLibrary.safeIntParse(input2.getText());
//                if (thetad > 90) {
//                    thetad = 90;
//                }
//            }
//        }
//        if (valid3) {
//            if (targetMode) {
//                target[2] = ReikaJavaLibrary.safeIntParse(input3.getText());
//            } else {
//                velocity = ReikaJavaLibrary.safeIntParse(input3.getText());
//                if (velocity < 0) {
//                    velocity = 0;
//                }
//            }
//        }
//        if (valid4) {
//            fuse = ReikaJavaLibrary.safeIntParse(input4.getText());
//        }
//        this.sendPacket();
//        if (targetMode)
//            return;
//        theta = ReikaPhysicsHelper.degToRad(thetad);
//        phi = ReikaPhysicsHelper.degToRad(phid);
//    }
//
//    @Override
//    public boolean labelInventory() {
//        return false;
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
//        if (targetMode) {
//            font.draw("Target X", imageWidth / 3 - 20, 18, 4210752);
//            font.draw("Target Y", imageWidth / 3 - 20, 34, 4210752);
//            font.draw("Target Z", imageWidth / 3 - 20, 51, 4210752);
//        } else {
//            font.draw("Launch Angle", imageWidth / 3 - 46 - 12, 80, 4210752);
//            font.draw("Compass Angle", imageWidth / 3 + 36 + 24, 80, 4210752);
//            font.draw("Velocity", imageWidth / 3 - 26 + 24 + 44, 116, 4210752);
//            if (tnt instanceof BlockEntityTNTCannon)
//                font.draw("Fuse Time", imageWidth / 3 - 26 - 32, 116, 4210752);
//        }
//
//        if (targetMode) {
//            if (!input.isFocused())
//                font.draw(String.format("%d", target[0]), 100, 18, 0xffffffff);
//            if (!input2.isFocused())
//                font.draw(String.format("%d", target[1]), 100, 34, 0xffffffff);
//            if (!input3.isFocused())
//                font.draw(String.format("%d", target[2]), 100, 50, 0xffffffff);
//        } else {
//            if (!input.isFocused())
//                font.draw(String.format("%d", tnt.phi), 122 + 36, 96, 0xffffffff);
//            if (!input2.isFocused())
//                font.draw(String.format("%d", tnt.theta), 35, 96, 0xffffffff);
//            if (!input3.isFocused())
//                font.draw(String.format("%d", tnt.velocity), 122 + 36, 116, 0xffffffff);
//            if (tnt instanceof BlockEntityTNTCannon)
//                if (!input4.isFocused())
//                    font.draw(String.format("%d", fuse), 122 - 54, 116, 0xffffffff);
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
//
//        input.drawTextBox();
//        input2.drawTextBox();
//        input3.drawTextBox();
//        if (tnt instanceof BlockEntityTNTCannon)
//            input4.drawTextBox();
//
//        if (!targetMode) {
//            this.drawGrid(j, k);
//            this.drawAngles(j, k);
//        }
//    }
//
//    private void drawAngles(int j, int k) {
//        int basex1 = 16 + j;
//        int basey1 = 73 + k;
//        int x2 = basex1 + (int) (57 * Math.cos(theta));
//        int y2 = basey1 - (int) (57 * Math.sin(theta));
//        int basex3 = 131 + j + 36;
//        int basey3 = 45 + k;/*
//    	if (phid >= 90 && phid <= 270) {
//    		basey3--;
//    	}
//       	if (phid >= 180 && phid <= 360) {
//    		basex3++;
//    	}*/
//        int x4 = basex3 + (int) (30 * Math.cos(theta) * Math.cos(phi));
//        int y4 = basey3 + (int) (30 * Math.cos(theta) * Math.sin(phi));
//
//        api.drawLine(basex1, basey1, x2, y2, 0xffffffff);
//        api.drawLine(basex3, basey3, x4, y4, 0xffffffff);
//    }
//
//    private void drawGrid(int j, int k) {
//        int color = 0x008800;
//        int basex1 = 16 + j;
//        int basey1 = 73 + k;
//        int basex3 = 131 + j + 36;
//        int basey3 = 45 + k;
//        for (int i = 0; i < 7; i++) {
//            int size = 57;
//            if (i == 1 || i == 5)
//                size = 60;
//            if (i == 2 || i == 4)
//                size = 66;
//            if (i == 3)
//                size = 80;
//            int x2 = basex1 + (int) (size * Math.cos(ReikaPhysicsHelper.degToRad(i * 15)));
//            int y2 = basey1 - (int) (size * Math.sin(ReikaPhysicsHelper.degToRad(i * 15)));
//            api.drawLine(basex1, basey1, x2, y2, color);
//        }
//        for (int i = 0; i < 8; i++) {
//            int size = 30;
//            int x4 = basex3 - 18 - 88 + imageWidth / 2 + (int) (size * Math.cos(ReikaPhysicsHelper.degToRad(i * 45)));
//            int y4 = basey3 - 118 + imageHeight / 2 - (int) (size * Math.sin(ReikaPhysicsHelper.degToRad(i * 45)));
//            api.drawLine(basex3, basey3, x4, y4, color);
//        }
//        for (int i = 0; i < 3; i++) {
//            int size = 30;
//            api.drawCircle(basex3, basey3, (int) (size * Math.cos(ReikaPhysicsHelper.degToRad(i * 30))), color);
//        }
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        if (targetMode)
//            return "targetgui";
//        else
//            return "cannongui";
//    }
//}
