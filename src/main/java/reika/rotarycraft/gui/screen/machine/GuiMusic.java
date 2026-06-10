/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.gui.screen.machine;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import reika.dragonapi.instantiable.gui.ColorButton;
import reika.dragonapi.instantiable.gui.ImagedGuiButton;
import reika.dragonapi.instantiable.gui.PianoKeyboard;
import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.dragonapi.libraries.rendering.ReikaGuiAPI;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.GuiNonPoweredMachine;
import reika.rotarycraft.blockentities.decorative.BlockEntityMusicBox;
import reika.rotarycraft.blockentities.decorative.BlockEntityMusicBox.Instrument;
import reika.rotarycraft.blockentities.decorative.BlockEntityMusicBox.NoteLength;
import reika.rotarycraft.gui.container.machine.MusicContainer;
import reika.rotarycraft.registry.PacketRegistry;

public class GuiMusic extends GuiNonPoweredMachine<BlockEntityMusicBox, MusicContainer> implements PianoKeyboard.MusicGui {

    private final BlockEntityMusicBox music;
    int x;
    int y;

    private int activeChannel = 0;
    private NoteLength activeType = NoteLength.WHOLE;
    private Instrument activeVoice = Instrument.GUITAR;

    private PianoKeyboard input;

    public GuiMusic(MusicContainer container, Inventory inv, Component title) {
        super(container, inv, title, 256, 217);
        music = (BlockEntityMusicBox) inv.player.level().getBlockEntity(container.tile.getBlockPos());
    }

    @Override
    public void init() {
        super.init();
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;
        Identifier note = Identifier.fromNamespaceAndPath(RotaryCraft.MODID,"textures/screen/musicbuttons.png");
        Identifier put =  Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/buttons.png");
        addRenderableWidget(new Button.Builder(Component.nullToEmpty("Save"), (button) -> actionPerformed(button, 100)).pos(j + 10, k + 6).size(40, 20).build());// j + 10, k + 6, 40, 20,
        addRenderableWidget(new Button.Builder(Component.nullToEmpty("Load"), (button) -> actionPerformed(button, 101)).pos(j + 50, k + 6).size( 40, 20).build());
        addRenderableWidget(new Button.Builder(Component.nullToEmpty("Load Demo"), (button) -> actionPerformed(button, 102)).pos(j + imageWidth / 2 + 40, k + 6).size( 80, 20).build());

        addRenderableWidget(new Button.Builder(Component.nullToEmpty("Add Rest"), (button) -> actionPerformed(button, 103)).pos(j + 20, k + 160).size( imageWidth - 40, 20).build());

        addRenderableWidget(new Button.Builder(Component.nullToEmpty("Backspace"), (button) -> actionPerformed(button, 104)).pos(j + 20, k + 185).size( 64, 20).build());
        addRenderableWidget(new Button.Builder(Component.nullToEmpty("Clear Channel"), (button) -> actionPerformed(button, 105)).pos(j + 84, k + 185).size( 88, 20).build());
        addRenderableWidget(new Button.Builder(Component.nullToEmpty("Clear Music"), (button) -> actionPerformed(button, 106)).pos(j + 172, k + 185).size( 64, 20).build());

        for (int i = 0; i < 16; i++) {
            ColorButton b = new ColorButton(200 + i, j + 9 + i * 15, k + 95, 12, 12, BlockEntityMusicBox.getColorForChannel(i));
            if (activeChannel == i)
                b.isSelected = true;
            addRenderableWidget(b);
        }

        input = new PianoKeyboard(j + imageWidth / 2 - 116, k + 120, this);

        int[] offset = new int[5];
        offset[activeType.ordinal()] = 80;
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            addRenderableWidget(new ImagedGuiButton(300 + i, j + 10 + 16 * i, k + 53, 16, 16, i * 16 + offset[i], 32, note, b -> actionPerformed(b, 300 + finalI)));
        }

        ItemStack[] items = {
                new ItemStack(Blocks.GRASS_BLOCK),
//          todo      new ItemStack(Blocks.planks),
                new ItemStack(Blocks.NETHER_PORTAL),
                new ItemStack(Blocks.STONE),
                new ItemStack(Blocks.SAND),
                new ItemStack(Blocks.GLASS)
        };
//      todo  for (int i = 0; i < 6; i++)
//            addRenderableWidget(new ItemIconButton(400 + i, j + 152 + 16 * i, k + 53, 0, items[i]));
    }

    @Override
    public boolean mouseClicked(net.minecraft.client.input.MouseButtonEvent event, boolean doubleClick) {
        super.mouseClicked(event, doubleClick);
        input.mouseClicked(event.x(), event.y(), event.button());
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    protected void actionPerformed(Button button, int id) {
        super.actionPerformed(button, id);
        boolean flag = true;
        if (id < 24000) {
            if (id == 100) {
                ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.MUSICSAVE.ordinal() + 1, music);
            } else if (id == 101) {
                ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.MUSICREAD.ordinal() + 2, music);
            } else if (id == 102) {
                ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.MUSICDEMO.ordinal(), music);
            } else if (id == 103) {
                ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.MUSICREST.ordinal(), music, activeChannel, activeType.ordinal(), 0, 0); //rest
            } else if (id == 104) {
                ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.MUSICBKSP.ordinal(), music, activeChannel); //bksp
            } else if (id == 105) {
                ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.MUSICCLEARCH.ordinal(), music, activeChannel); //ch clr
            } else if (id == 106) {
                ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.MUSICCLEAR.ordinal(), music); //clrall
            } else if (id >= 400) {
                int i = id - 400 + 1;
                activeVoice = Instrument.values()[i];
            } else if (id >= 300) {
                int i = id - 300;
                activeType = NoteLength.values()[i];
            } else if (id >= 200) {
                activeChannel = id - 200;
            } else {
                //ReikaJavaLibrary.pConsole(id);
                flag = false;
                music.sendNote(id, activeChannel, activeType, activeVoice);
            }
        }
        if (flag) {
            this.init();
        }
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    // 1.21.5: Screen.extractBackground is public; GuiGraphicsExtractor.pose() returns
    // Matrix3x2fStack rather than PoseStack so the line-drawing helpers need a port.
    @Override
    public void extractBackground(GuiGraphicsExtractor stack, int par2, int par3, float par1) {
        super.extractBackground(stack, par2, par3, par1);
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;

        stack.blit(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/musicbuttons.png"), j+imageWidth/2-232/2, k+150, 0, 64, 232, 37, 256, 256);

        input.drawKeys(stack);

        ReikaGuiAPI.instance.drawCenteredStringNoShadow(stack, font, "Note Length", 51, 42, 0);
        ReikaGuiAPI.instance.drawCenteredStringNoShadow(stack, font, "Instrument", 200, 42, 0);
        ReikaGuiAPI.instance.drawCenteredStringNoShadow(stack, font, "Channel Select", imageWidth / 2, 85, 0);

        // 26.1: divider line under the "Channel Select" label. Original 1.7 used Tesselator
        // primitives via stack.pose(); the new pipeline gives us a clean rectangle-fill API
        // (GuiGraphicsExtractor.fill) which is the standard way to draw 1-px rules in 1.21+.
        int dividerY = 85 + font.lineHeight + 1;
        stack.fill(imageWidth / 2 - 60, dividerY, imageWidth / 2 + 60, dividerY + 1, 0xFF202020);
    }

    @Override
    protected String getGuiTexture() {
        return "musicgui";
    }

    @Override
    public int getActiveChannel() {
        return activeChannel;
    }

    @Override
    public void onKeyPressed(PianoKeyboard.PianoKey key) {
        this.actionPerformed(key, 0); //todo id
    }

    @Override
    public int getColorForChannel(int channel) {
        return BlockEntityMusicBox.getColorForChannel(channel);
    }

    @Override
    public Identifier bindKeyboardTexture() {
        return Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/musicbuttons.png");
    }

}