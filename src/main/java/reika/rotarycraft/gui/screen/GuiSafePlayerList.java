///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.gui.screen;
//
//import java.util.List;
//
//import net.minecraft.world.entity.player.Player;
//import org.lwjgl.opengl.GL11;
//import reika.dragonapi.instantiable.gui.SubviewableList;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.blockentity.BlockEntityAimedCannon;
//import reika.rotarycraft.registry.PacketRegistry;
//
//public class GuiSafePlayerList extends GuiScreen {
//
//    private static final int colsize = 8;
//    private final int imageWidth = 226;
//    private final int imageHeight = 204;
//    private final BlockEntityAimedCannon te;
//    private final List<String> rawData;
//    private final SubviewableList<String> playerList;
//    private final Player ep;
//    protected int buttontimer = 0;
//    private String playerName;
//    private String activePlayer;
//    private long buttontime;
//
//    public GuiSafePlayerList(Player e, BlockEntityAimedCannon tile) {
//        ep = e;
//        te = tile;
//        rawData = te.getCopyOfSafePlayerList();
//        playerList = new SubviewableList(rawData, colsize);
//    }
//
//    @Override
//    public void initGui() {
//        super.initGui();
//        renderables.clear();
//
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//
//        int width = 180;
//
//        int dx = 10;//imageWidth/2-width/2;//(i/colsize)*width;
//        for (int i = 0; i < playerList.clampedSize(); i++) {
//            int dy = 12 + i * 22;
//            addRenderableWidget(new Button(i, j + dx, k + dy, width, 20, playerList.getEntryAtRelativeIndex(i)));
//        }
//
//        addRenderableWidget(new Button(1000000, j + dx + width + 6, 11 + k, 20, 20, "^"));
//        addRenderableWidget(new Button(1000001, j + dx + width + 6, 11 + k + colsize * 20 - 5, 20, 20, "v"));
//    }
//
//    /**
//     * Returns true if this GUI should pause the game when it is displayed in single-player
//     */
//    @Override
//    public boolean doesGuiPauseGame() {
//        return true;
//    }
//
//    @Override
//    protected void actionPerformed(Button button) {
//        if (buttontimer > 0)
//            return;
//        buttontimer = 20;
//        if (button.id >= 1000000) {
//            if (button.id == 1000000) {
//                playerList.stepOffset(-1);
//            } else {
//                playerList.stepOffset(1);
//            }
//            this.initGui();
//            return;
//        }
//        activePlayer = playerList.getEntryAtRelativeIndex(button.id);
//        ReikaPacketHelper.sendStringPacket(RotaryCraft.packetChannel, PacketRegistry.SAFEPLAYER.ordinal(), activePlayer, te);
//        rawData.remove(button.id);
//        this.initGui();
//    }
//
//    @Override
//    public void drawScreen(int x, int y, float f) {
//        if (System.nanoTime() - buttontime > 100000000) {
//            buttontime = System.nanoTime();
//            buttontimer = 0;
//        }
//        String title = te.getPlacerName() + "'s " + te.getName() + " Whitelist";
//
//        String var4 = "/Reika/RotaryCraft/Textures/GUI/safeplayergui.png";
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        ReikaTextureHelper.bindTexture(RotaryCraft.class, var4);
//
//        int getY = (width - imageWidth) / 2;
//        int posY = (height - imageHeight) / 2 - 8;
//
//        ScreenUtils.drawTexturedModalRect(getY, getY(), 0, 0, imageWidth, imageHeight);
//
//        ReikaGuiAPI.instance.drawCenteredStringNoShadow(font, title, getY + imageWidth / 2, getY() + 6, 4210752);
//        super.drawScreen(x, y, f);
//    }
//
//}
