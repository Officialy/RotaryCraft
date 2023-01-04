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
//import net.minecraft.client.gui.GuiTextField;
//import net.minecraft.entity.player.Player;
//
//import reika.dragonapi.instantiable.data.immutable.WorldLocation;
//import reika.dragonapi.libraries.io.ReikaPacketHelper;
//import reika.dragonapi.libraries.java.ReikaJavaLibrary;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.GuiPowerOnlyMachine;
//import reika.rotarycraft.gui.container.Machine.Inventory.ContainerItemCannon;
//import reika.rotarycraft.registry.PacketRegistry;
//import reika.rotarycraft.blockentities.BlockEntityItemCannon;
//
//public class GuiItemCannon extends GuiPowerOnlyMachine {
//    private final BlockEntityItemCannon ica;
//    private GuiTextField input;
//    private GuiTextField input2;
//    private GuiTextField input3;
//    private GuiTextField input4; //dim
//
//    private WorldLocation target;
//
//    private int targetDim;
//    private int targetX;
//    private int targetY;
//    private int targetZ;
//
//    public GuiItemCannon(Player p5ep, BlockEntityItemCannon ItemCannon) {
//        super(new ContainerItemCannon(p5ep, ItemCannon), ItemCannon);
//        ica = ItemCannon;
//        imageHeight = 236;
//        imageHeight = 170;
//        target = ica.getTarget();
//        ep = p5ep;
//    }
//
//    @Override
//    public void initGui() {
//        super.initGui();
//        int j = (width - imageWidth) / 2 + 8;
//        int k = (height - imageHeight) / 2 - 12;
//        //this.addRenderableWidget(new Button(0, j+imageWidth/2-48, -1+k+32, 80, 20, "Trajectory"));
//        input = new GuiTextField(font, j + imageWidth / 2 + 25, k + 26, 46, 16);
//        input.setFocused(false);
//        input.setMaxStringLength(6);
//        if (target != null)
//            input.setText(String.valueOf(target.xCoord));
//        input2 = new GuiTextField(font, j + imageWidth / 2 + 25, k + 42, 46, 16);
//        input2.setFocused(false);
//        input2.setMaxStringLength(6);
//        if (target != null)
//            input2.setText(String.valueOf(target.yCoord));
//        input3 = new GuiTextField(font, j + imageWidth / 2 + 25, k + 58, 46, 16);
//        input3.setFocused(false);
//        input3.setMaxStringLength(6);
//        if (target != null)
//            input3.setText(String.valueOf(target.zCoord));
//        input4 = new GuiTextField(font, j + imageWidth / 4 - 30 - 6, k + 50, 26, 16);
//        input4.setFocused(false);
//        input4.setMaxStringLength(6);
//        if (target != null)
//            input4.setText(String.valueOf(target.dimensionID));
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
//    private void sendPacket() {
//        target = new WorldLocation(targetDim, targetX, targetY, targetZ);
//        ByteArrayOutputStream bos = new ByteArrayOutputStream(24); // 6 ints
//        DataOutputStream outputStream = new DataOutputStream(bos);
//        try {
//            //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.valueOf(drops));
//            outputStream.writeInt(PacketRegistry.ITEMCANNON.ordinal());
//            outputStream.writeInt(target.dimensionID);
//            outputStream.writeInt(target.xCoord);
//            outputStream.writeInt(target.yCoord);
//            outputStream.writeInt(target.zCoord);
//            outputStream.writeInt(ica.xCoord);
//            outputStream.writeInt(ica.yCoord);
//            outputStream.writeInt(ica.zCoord);
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
//            targetX = 0;
//            input.deleteFromCursor(-1);
//            this.sendPacket();
//            valid1 = false;
//        }
//        if (!input2.getText().isEmpty() && !ReikaJavaLibrary.isValidInteger(input2.getText())) {
//            targetY = 0;
//            input2.deleteFromCursor(-1);
//            this.sendPacket();
//            valid2 = false;
//        }
//        if (!input3.getText().isEmpty() && !ReikaJavaLibrary.isValidInteger(input3.getText())) {
//            targetZ = 0;
//            input3.deleteFromCursor(-1);
//            this.sendPacket();
//            valid3 = false;
//        }
//        if (!input4.getText().isEmpty() && !ReikaJavaLibrary.isValidInteger(input4.getText())) {
//            targetDim = 0;
//            input4.deleteFromCursor(-1);
//            this.sendPacket();
//            valid4 = false;
//        }
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
//        if (valid1) {
//            targetX = ReikaJavaLibrary.safeIntParse(input.getText());
//            this.sendPacket();
//        }
//        if (valid2) {
//            targetY = ReikaJavaLibrary.safeIntParse(input2.getText());
//            this.sendPacket();
//        }
//        if (valid3) {
//            targetZ = ReikaJavaLibrary.safeIntParse(input3.getText());
//            this.sendPacket();
//        }
//        if (valid4) {
//            targetDim = ReikaJavaLibrary.safeIntParse(input4.getText());
//            this.sendPacket();
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
//        font.draw("Target X", imageWidth / 3 + 10, 18, 4210752);
//        font.draw("Target Y", imageWidth / 3 + 10, 34, 4210752);
//        font.draw("Target Z", imageWidth / 3 + 10, 51, 4210752);
//        font.draw("Target Dim", imageWidth / 4 - 32 - 6, 26, 4210752);
//
//        if (!input.isFocused())
//            font.draw(String.format("%d", targetX), 125, 18, 0xffffffff);
//        if (!input2.isFocused())
//            font.draw(String.format("%d", targetY), 125, 34, 0xffffffff);
//        if (!input3.isFocused())
//            font.draw(String.format("%d", targetZ), 125, 50, 0xffffffff);
//        if (!input4.isFocused())
//            font.draw(String.format("%d", targetDim), 20, 42, 0xffffffff);
//    }
//
//    /**
//     * Draw the background layer for the GuiContainer (everything behind the items)
//     */
//    @Override
//    protected void renderBg(PoseStack poseStack, float par1, int par2, int par3) {
//        super.renderBg(PoseStack poseStack, par1, par2, par3);
//
//        input.drawTextBox();
//        input2.drawTextBox();
//        input3.drawTextBox();
//        input4.drawTextBox();
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return "targetgui";
//    }
//}
