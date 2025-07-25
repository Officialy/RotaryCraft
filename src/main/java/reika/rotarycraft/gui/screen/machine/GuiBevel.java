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

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.apache.commons.lang3.tuple.ImmutablePair;
import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.NonPoweredMachineScreen;
import reika.rotarycraft.blockentities.transmission.BlockEntityBevelGear;
import reika.rotarycraft.gui.container.machine.BlankContainer;
import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.PacketRegistry;

public class GuiBevel extends NonPoweredMachineScreen<BlockEntityBevelGear, BlankContainer<BlockEntityBevelGear>> {
    private final BlockEntityBevelGear bevel;
    /**
     * Side colors:
     * <p>
     * Cyan y-1; blue y+1; yellow -z; black +z; orange -x; magenta +x;<br>
     * 0 y-1; 1 y+1; 2 -z; 3 +z; 4 -x; 5 +x;
     */
    private int posn;
    private Direction in;
    private Direction out;

    public GuiBevel(BlankContainer<BlockEntityBevelGear> id, Inventory inventory, Component title) {
        super(id, inventory, title);
        bevel = (BlockEntityBevelGear) inventory.player.level().getBlockEntity(id.tile.getBlockPos());
        imageHeight = 192;
        this.inventory = inventory;
        posn = bevel.direction;
        this.getIOFromDirection();
    }

    @Override
    public void init() {
        super.init();
        int j = (width - imageWidth) / 2 - 2;
        int k = (height - imageHeight) / 2 - 12;

        ResourceLocation file = ResourceLocation.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/bevelgui2.png");
        int px = 176;
        for (int i = 0; i < 6; i++) {
            String s = Direction.values()[i].name().substring(0, 1);
            if (true) {
                if (in.ordinal() == i)
                //id is i
                //pX, pY, pWidth, pHeight, pXTexStart, pYTexStart, pYDiffTex, pResourceLocation, pTextureWidth, pTextureHeight, pOnPress, pMessage
                //x,  y,  width,  height,    u,           v,           display    string,          color,          shadow?,    filepath
                {
                    int finalI = i;
                    addRenderableWidget(new ImageButton(j + 40, k + 8 + 48 + i * 22, 18, 18, px + 18, i * 18, 0,
                            file, 256, 256, (button) -> actionPerformed(button, finalI), Component.literal(s)));
                } else {
                    int finalI1 = i;
                    addRenderableWidget(new ImageButton(j + 40, k + 8 + 48 + i * 22, 18, 18, px, i * 18, 0,
                            file, 256, 256, (button) -> actionPerformed(button, finalI1), Component.literal(s)));
                }
            } else {
                int finalI2 = i;
                addRenderableWidget(new ImageButton(j + 40, k + 8 + 48 + i * 22, 18, 18, 212, 0, 0,
                        file, 256, 256, (button) -> actionPerformed(button, finalI2), Component.literal(s)));
            }
        }
        for (int i = 0; i < 6; i++) {
            String s = Direction.values()[i].name().substring(0, 1);
            if (BlockEntityBevelGear.isValid(in, Direction.values()[i])) {
                if (out.ordinal() == i) {
                    // Output side button - ID should be i + 6
                    int finalI2 = i;
                    addRenderableWidget(new ImageButton(j + imageWidth - 40 - 18, k + 8 + 48 + i * 22, 18, 18, px + 18, i * 18, 0,
                            file, 256, 256, (button) -> actionPerformed(button, finalI2 + 6), Component.literal(s)));
                } else {
                    int finalI1 = i;
                    addRenderableWidget(new ImageButton(j + imageWidth - 40 - 18, k + 8 + 48 + i * 22, 18, 18, px, i * 18, 0,
                            file, 256, 256, (button) -> actionPerformed(button, finalI1 + 6), Component.literal(s)));
                }
            } else {
                int finalI = i;
                addRenderableWidget(new ImageButton(j + imageWidth - 40 - 18, k + 8 + 48 + i * 22, 18, 18, 212, 0, 0,
                        file, 256, 256, (button) -> actionPerformed(button, finalI + 6), Component.literal(s)));
            }
        }
    }

    public void getIOFromDirection() {
        ImmutablePair<Direction, Direction> dirs = BlockEntityBevelGear.getDirectionMap().get(posn);
        if (dirs == null) {
            RotaryCraft.LOGGER.error("Bevel was set to invalid direction value " + posn + "!");
            return;
        }
        in = dirs.left;
        out = dirs.right;
    }

    public void getDirectionFromIO() {
        if (!BlockEntityBevelGear.isValid(in, out)) {
            RotaryCraft.LOGGER.error("Bevel was set to invalid state " + in + " > " + out + "!");
            return;
        }
        posn = BlockEntityBevelGear.getDirectionMap().inverse().get(new ImmutablePair<>(in, out));
    }

    @Override
    protected void actionPerformed(Button button, int id) {
        super.actionPerformed(button, id);
        RotaryCraft.LOGGER.info("Button " + id + " pressed!");
        if (id < 6) {
            // Input side button pressed
            in = Direction.values()[id];
            if (!BlockEntityBevelGear.isValid(in, out))
                out = in.getStepY() != 0 ? Direction.EAST : Direction.DOWN;
        } else if (id < 12) {
            // Output side button pressed (id 6-11 map to directions 0-5)
            if (!BlockEntityBevelGear.isValid(in, Direction.values()[id - 6]))
                return;
            out = Direction.values()[id - 6];
        }
        this.getDirectionFromIO();
        this.init();
        bevel.direction = posn;
        ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.BEVEL.ordinal(), bevel, posn);
        bevel.setChanged();
    }

    @Override
    public void render(GuiGraphics stack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(stack, pMouseX, pMouseY, pPartialTick);

        int j = (width - imageWidth) / 2 - 2;
        int k = (height - imageHeight) / 2 - 12;

        stack.drawString(font,  "Input Side", j + 24, k + 32, 4210752);
        stack.drawString(font,  "Output Side", j + 99, k + 32, 4210752);

        if (ConfigRegistry.COLORBLIND.getState()) {
            for (int i = 0; i < 6; i++) {
                stack.drawString(font,  String.valueOf(i), 30, 49 + i * 22, 0);
            }

            for (int i = 0; i < 6; i++) {
                stack.drawString(font,  String.valueOf(i), imageWidth - 68, 49 + i * 22, 0);
            }
        }
    }

    @Override
    protected String getGuiTexture() {
        return "bevelgui2";
    }
}