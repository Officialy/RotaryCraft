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
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.GuiNonPoweredMachine;
//import reika.rotarycraft.registry.PacketRegistry;
//
//public class GuiMusic extends GuiNonPoweredMachine implements MusicGui {
//
//    private final BlockEntityMusicBox music;
//    //private Level level = ModLoader.getMinecraftInstance().theWorld;
//    int x;
//    int y;
//
//    private int activeChannel = 0;
//    private NoteLength activeType = NoteLength.WHOLE;
//    private Instrument activeVoice = Instrument.GUITAR;
//
//    private PianoKeyboard input;
//
//    public GuiMusic(Player p5ep, BlockEntityMusicBox MusicBox) {
//        super(new CoreMenu(p5ep, MusicBox), MusicBox);
//        music = MusicBox;
//        imageHeight = 217;
//        imageWidth = 256;
//        ep = p5ep;
//    }
//
//    @Override
//    public void initGui() {
//        super.initGui();
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//        String note = "/Reika/RotaryCraft/Textures/GUI/musicbuttons.png";
//        String put = "/Reika/RotaryCraft/Textures/GUI/buttons.png";
//
//        renderables.add(new Button(100, j + 10, k + 6, 40, 20, "Save"));
//        renderables.add(new Button(101, j + 50, k + 6, 40, 20, "Load"));
//        renderables.add(new Button(102, j + imageWidth / 2 + 40, k + 6, 80, 20, "Load Demo"));
//
//        renderables.add(new Button(103, j + 20, k + 160, imageWidth - 40, 20, "Add Rest"));
//
//        renderables.add(new Button(104, j + 20, k + 185, 64, 20, "Backspace"));
//        renderables.add(new Button(105, j + 84, k + 185, 88, 20, "Clear Channel"));
//        renderables.add(new Button(106, j + 172, k + 185, 64, 20, "Clear Music"));
//
//        for (int i = 0; i < 16; i++) {
//            ColorButton b = new ColorButton(200 + i, j + 9 + i * 15, k + 95, 12, 12, BlockEntityMusicBox.getColorForChannel(i));
//            if (activeChannel == i)
//                b.isSelected = true;
//            renderables.add(b);
//        }
//
//        input = new PianoKeyboard(j + imageWidth / 2 - 116, k + 120, this);
//
//        int[] offset = new int[5];
//        offset[activeType.ordinal()] = 80;
//        for (int i = 0; i < 5; i++) {
//            renderables.add(new ImageButton(300 + i, j + 10 + 16 * i, k + 53, 16, 16, i * 16 + offset[i], 32, note, RotaryCraft.class));
//        }
//
//        ItemStack[] items = {
//                new ItemStack(Blocks.GRASS),
//                new ItemStack(Blocks.planks),
//                new ItemStack(Blocks.portal),
//                new ItemStack(Blocks.STONE),
//                new ItemStack(Blocks.SAND),
//                new ItemStack(Blocks.GLASS)
//        };
//        for (int i = 0; i < 6; i++)
//            renderables.add(new ItemIconButton(400 + i, j + 152 + 16 * i, k + 53, 0, items[i]));
//    }
//
//    @Override
//    protected void keyTyped(char c, int i) {
//        super.keyTyped(c, i);
//
//    }
//
//    @Override
//    protected void mouseClicked(int i, int j, int k) { //delete note on right-click
//		/*
//		if (k == 0) {
//			for (int l = 0; l < renderables.size(); l++) {
//				Button guibutton = (Button)renderables.get(l);
//				if (guibutton.mousePressed(mc, i, j)) {
//					this.actionPerformed(guibutton);
//					if (guibutton.id >= 100)
//						mc.sndManager.playSoundFX("DragonAPI.rand.click", 1.0F, 1.0F);
//					return; //to avoid double presses
//				}
//			}
//		}*/
//        super.mouseClicked(i, j, k);
//        input.mouseClicked(i, j, k);
//    }
//
//    @Override
//    public void updateScreen() {
//        super.updateScreen();
//    }
//
//    @Override
//    protected void actionPerformed(Button button) {
//        super.actionPerformed(button);
//        boolean flag = true;
//        if (button.id < 24000) {
//            if (button.id == 100) {
//                ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.MUSICSAVE.ordinal() + 1, music);
//            } else if (button.id == 101) {
//                ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.MUSICREAD.ordinal() + 2, music);
//            } else if (button.id == 102) {
//                ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.MUSICDEMO.ordinal(), music);
//            } else if (button.id == 103) {
//                ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.MUSICREST.ordinal(), music, activeChannel, activeType.ordinal(), 0, 0); //rest
//            } else if (button.id == 104) {
//                ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.MUSICBKSP.ordinal(), music, activeChannel); //bksp
//            } else if (button.id == 105) {
//                ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.MUSICCLEARCH.ordinal(), music, activeChannel); //ch clr
//            } else if (button.id == 106) {
//                ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.MUSICCLEAR.ordinal(), music); //clrall
//            } else if (button.id >= 400) {
//                int i = button.id - 400 + 1;
//                activeVoice = Instrument.values()[i];
//            } else if (button.id >= 300) {
//                int i = button.id - 300;
//                activeType = NoteLength.values()[i];
//            } else if (button.id >= 200) {
//                activeChannel = button.id - 200;
//            } else {
//                //ReikaJavaLibrary.pConsole(button.id);
//                flag = false;
//                music.sendNote(button.id, activeChannel, activeType, activeVoice);
//            }
//        }
//        if (flag) {
//            this.initGui();
//        }
//    }
//
//    /**
//     * Draw the foreground layer for the GuiContainer (everything in front of the items)
//     */
//    @Override
//    protected void drawGuiContainerForegroundLayer(int a, int b) {
//        super.drawGuiContainerForegroundLayer(a, b);
//
//        ReikaGuiAPI.instance.drawCenteredStringNoShadow(font, "Note Length", 51, 42, 0);
//        ReikaGuiAPI.instance.drawCenteredStringNoShadow(font, "Instrument", 200, 42, 0);
//        ReikaGuiAPI.instance.drawCenteredStringNoShadow(font, "Channel Select", imageWidth / 2, 85, 0);
//
//        int dx = (activeVoice.ordinal() - 1) * 16;
//        int color = BlockEntityMusicBox.getColorForChannel(activeChannel);
//        ReikaGuiAPI.instance.drawLine(152 + dx, 53, 152 + dx, 69, 0xff000000);
//        ReikaGuiAPI.instance.drawLine(152 + dx, 53, 168 + dx, 53, 0xff000000);
//        ReikaGuiAPI.instance.drawLine(168 + dx, 53, 168 + dx, 69, 0xff000000);
//        ReikaGuiAPI.instance.drawLine(152 + dx, 69, 168 + dx, 69, 0xff000000);
//
//    }
//
//    /**
//     * Draw the background layer for the GuiContainer (everything behind the items)
//     */
//    @Override
//    protected void renderBg(PoseStack poseStack, float par1, int par2, int par3) {
//        super.renderBg(PoseStack poseStack, par1, par2, par3);
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//
//        //ReikaTextureHelper.bindTexture(RotaryCraft.class, "/Reika/RotaryCraft/Textures/GUI/musicbuttons.png");
//        //ScreenUtils.drawTexturedModalRect(j+imageWidth/2-232/2, k+150, 0, 64, 232, 37);
//
//        input.drawKeys();
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return "musicgui";
//    }
//
//    @Override
//    public int getActiveChannel() {
//        return activeChannel;
//    }
//
//    @Override
//    public void onKeyPressed(PianoKey key) {
//        this.actionPerformed(key);
//    }
//
//    @Override
//    public int getColorForChannel(int channel) {
//        return BlockEntityMusicBox.getColorForChannel(channel);
//    }
//
//    @Override
//    public void bindKeyboardTexture() {
//        ReikaTextureHelper.bindTexture(RotaryCraft.class, "/Reika/RotaryCraft/Textures/GUI/musicbuttons.png");
//    }
//
//}
