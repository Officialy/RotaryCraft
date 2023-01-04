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

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.gui.ScreenUtils;
import reika.dragonapi.instantiable.gui.ColorButton;
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
        super(container, inv, title);
        music = (BlockEntityMusicBox) inv.player.level.getBlockEntity(container.tile.getBlockPos());
        imageHeight = 217;
        imageWidth = 256;
    }

    @Override
    public void init() {
        super.init();
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;
        ResourceLocation note = new ResourceLocation(RotaryCraft.MODID,"textures/screen/musicbuttons.png");
        ResourceLocation put =  new ResourceLocation(RotaryCraft.MODID, "textures/screen/buttons.png");
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
            addRenderableWidget(new ImageButton(j + 10 + 16 * i, k + 53, 16, 16, i * 16 + offset[i], 32, note, (button) -> actionPerformed(button, 300 + finalI)));
        }

        ItemStack[] items = {
                new ItemStack(Blocks.GRASS),
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
    public boolean mouseClicked(double i, double j, int k) { //delete note on right-click
		/*
		if (k == 0) {
			for (int l = 0; l < renderables.size(); l++) {
				Button guibutton = (Button)renderables.get(l);
				if (guibutton.mousePressed(mc, i, j)) {
					this.actionPerformed(guibutton);
					if (guibutton.id >= 100)
						mc.sndManager.playSoundFX("DragonAPI.rand.click", 1.0F, 1.0F);
					return; //to avoid double presses
				}
			}
		}*/
        super.mouseClicked(i, j, k);
        input.mouseClicked(i, j, k);
        return super.mouseClicked(i, j, k);
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
    @Override
    protected void renderBg(PoseStack stack, float par1, int par2, int par3) {
        super.renderBg(stack, par1, par2, par3);
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;

        RenderSystem.setShaderTexture(0, new ResourceLocation(RotaryCraft.MODID,"textures/screen/musicbuttons.png"));
        ScreenUtils.drawTexturedModalRect(stack, j+imageWidth/2-232/2, k+150, 0, 64, 232, 37, 0);

        input.drawKeys(stack);

        //this was in foreground
        ReikaGuiAPI.instance.drawCenteredStringNoShadow(stack, font, "Note Length", 51, 42, 0);
        ReikaGuiAPI.instance.drawCenteredStringNoShadow(stack, font, "Instrument", 200, 42, 0);
        ReikaGuiAPI.instance.drawCenteredStringNoShadow(stack, font, "Channel Select", imageWidth / 2, 85, 0);

        int dx = (activeVoice.ordinal() - 1) * 16;
        int color = BlockEntityMusicBox.getColorForChannel(activeChannel);
        ReikaGuiAPI.instance.drawLine(stack, 152 + dx, 53, 152 + dx, 69, 0xff000000);
        ReikaGuiAPI.instance.drawLine(stack, 152 + dx, 53, 168 + dx, 53, 0xff000000);
        ReikaGuiAPI.instance.drawLine(stack, 168 + dx, 53, 168 + dx, 69, 0xff000000);
        ReikaGuiAPI.instance.drawLine(stack, 152 + dx, 69, 168 + dx, 69, 0xff000000);
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
    public void bindKeyboardTexture() {
        RenderSystem.setShaderTexture(0, new ResourceLocation(RotaryCraft.MODID, "textures/screen/musicbuttons.png"));
    }

}