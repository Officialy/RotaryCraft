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
//import java.io.ByteArrayOutputStream;
//import java.io.DataOutputStream;
//
//import net.minecraft.client.gui.components.Button;
//import net.minecraft.network.chat.Component;
//import org.lwjgl.opengl.GL11;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.GuiMachine;
//import reika.rotarycraft.registry.PacketRegistry;
//
//public class GuiBorer extends GuiMachine {
//    private final BlockEntityBorer borer;
//    private final boolean[][] dig = new boolean[7][5];
//    public String dropstatus;
//    public boolean drops;
//    int x;
//    int y;
//    private int packetID;
//
//    public GuiBorer(Player p5ep, BlockEntityBorer borer) {
//        super(new CoreMenu(p5ep, borer), borer);
//        this.borer = borer;
//        imageHeight = 169;
//        imageWidth = 176;
//        dropstatus = "Drops On";
//        ep = p5ep;
//        drops = borer.drops;
//        for (int i = 0; i < 7; i++)
//            for (int l = 0; l < 5; l++)
//                dig[i][l] = borer.cutShape[i][l];
//    }
//
//    @Override
//    public void initGui() {
//        super.initGui();
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//
//        String file = "/Reika/RotaryCraft/Textures/GUI/buttons.png";
//        for (int i = 0; i < 7; i++)
//            for (int l = 0; l < 5; l++) {
//                int u = 0;
//                if (i == 3 && l == 4)
//                    u = 36;
//                if (dig[i][l])
//                    renderables.add(new ImageButton(50 + i + 7 * l, j + 25 + 18 * i, k + 16 + 18 * l, 18, 18, u, 0, file, RotaryCraft.class));
//                else
//                    renderables.add(new ImageButton(10 + i + 7 * l, j + 25 + 18 * i, k + 16 + 18 * l, 18, 18, u + 18, 0, file, RotaryCraft.class));
//            }
//
//        renderables.add(new Button(j + 14, -1 + k + 116, 72, 20, Component.literal("Reset Pos'n"), (a) -> this.sendPacket(PacketRegistry.BORERRESET.ordinal()))); //8
//        renderables.add(new Button(j + 14, k + 140, 148, 20, Component.literal("Toggle All"), (a) -> {
//            this.sendPacket(PacketRegistry.BORERTOGGLEALL.ordinal());
//            for (int i = 0; i < 5; i++) {
//                for (int j = 0; j < 7; j++) {
//                    dig[j][i] = !dig[j][i];
//                }
//            }
//        })); //6
//
//        if (drops)
//            renderables.add(new Button(j + 90, -1 + k + 116, 72, 20, Component.literal("Drops On"), (a) -> this.toggleDrops())); //7 toggle
//        else
//            renderables.add(new Button(j + 90, -1 + k + 116, 72, 20, Component.literal("Drops Off"), (a) -> this.toggleDrops())); //7 toggle
//    }
//
//    public void toggleDrops() {
//        if (drops) {
//            dropstatus = "Drops Off";
//            drops = false;
//        } else {
//            dropstatus = "Drops On";
//            drops = true;
//        }
//
//        this.sendPacket(PacketRegistry.BORERDROPS.ordinal());
//    }
//
//    protected void actionPerformed(Button button) {
//
//        if (button.id >= 10 && button.id < 50) {
//            int rows = (button.id - 10) / 7;
//            int cols = (button.id - 10) - rows * 7;
//            //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d -> row %d col %d", button.id, rows, cols));
//            dig[cols][rows] = true;
//            packetID = (button.id - 10);
//            if (button.id == 10)
//                packetID = 100;
//            this.sendPacket(PacketRegistry.BORER.ordinal());
//        }
//        if (button.id >= 50 && button.id < 24000) {
//            int rows = (button.id - 50) / 7;
//            int cols = (button.id - 50) - rows * 7;
//            //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d -> row %d col %d", button.id, rows, cols));
//            dig[cols][rows] = false;
//            packetID = (button.id - 50);
//            if (button.id == 50)
//                packetID = 100;
//            this.sendPacket(PacketRegistry.BORER.ordinal());
//        }
//        this.updateScreen();
//
//    }
//
//    public void sendPacket(int a) {
//        ByteArrayOutputStream bos = new ByteArrayOutputStream(20); // 5 ints
//        DataOutputStream outputStream = new DataOutputStream(bos);
//        try {
//            //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.valueOf(drops));
//            outputStream.writeInt(a);
//            if (a == PacketRegistry.BORERDROPS.ordinal()) {
//                if (drops)
//                    outputStream.writeInt(1); //set drops to 0 (false)
//                else
//                    outputStream.writeInt(0);
//                //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.valueOf(drops));
//            }
//            if (a == PacketRegistry.BORERTOGGLEALL.ordinal())
//                outputStream.writeInt(-1);
//            if (a > PacketRegistry.BORERTOGGLEALL.ordinal())
//                outputStream.writeInt(-1);
//            if (a == PacketRegistry.BORER.ordinal()) {
//                //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.valueOf(3434));
//                int rows = packetID / 7;
//                int cols = packetID - rows * 7;
//                if (packetID == 100) {
//                    rows = cols = 0;
//                }
//                //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d -> row %d col %d", this.packetID, rows, cols));
//                if (dig[cols][rows])
//                    outputStream.writeInt(-1 * packetID);
//                else
//                    outputStream.writeInt(packetID);
//            }
//            outputStream.writeInt(borer.xCoord);
//            outputStream.writeInt(borer.yCoord);
//            outputStream.writeInt(borer.zCoord);
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        ReikaPacketHelper.sendDataPacket(RotaryCraft.packetChannel, bos);
//        this.updateScreen();
//    }
//
//    @Override
//    public void updateScreen() {
//        super.updateScreen();
//        x = Mouse.getX();
//        y = Mouse.getY();
//        this.initGui();
//    }
//
//    @Override
//    protected void drawPowerTab(int var5, int var6) {
//        String var4 = "/Reika/RotaryCraft/Textures/GUI/powertab.png";
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        ReikaTextureHelper.bindTexture(RotaryCraft.class, var4);
//        ScreenUtils.drawTexturedModalRect(imageWidth + var5, var6 + 5, 0, 4, 42, 159);
//
//        long frac = ((borer.power * 29L) / borer.MINPOWER);
//        if (frac > 29)
//            frac = 29;
//        ScreenUtils.drawTexturedModalRect(imageWidth + var5 + 5, imageHeight + var6 - 146, 0, 0, (int) frac, 4);
//
//        frac = borer.omega * 29L / borer.MINSPEED;
//        if (frac > 29)
//            frac = 29;
//        ScreenUtils.drawTexturedModalRect(imageWidth + var5 + 5, imageHeight + var6 - 86, 0, 0, (int) frac, 4);
//
//        frac = borer.torque * 29L / borer.MINTORQUE;
//        if (frac > 29)
//            frac = 29;
//        ScreenUtils.drawTexturedModalRect(imageWidth + var5 + 5, imageHeight + var6 - 26, 0, 0, (int) frac, 4);
//
//        api.drawCenteredStringNoShadow(font, "Power:", imageWidth + var5 + 20, var6 + 12, 0xff000000);
//        api.drawCenteredStringNoShadow(font, "Speed:", imageWidth + var5 + 20, var6 + 71, 0xff000000);
//        api.drawCenteredStringNoShadow(font, "Torque:", imageWidth + var5 + 20, var6 + 130, 0xff000000);
//        //this.drawCenteredStringNoShadow(font, String.format("%d/%d", borer.power, borer.MINPOWER), imageWidth+var5+16, var6+16, 0xff000000);
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return "borergui2";
//    }
//
//}
