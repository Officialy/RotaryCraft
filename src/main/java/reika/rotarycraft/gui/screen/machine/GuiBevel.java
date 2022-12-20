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

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.apache.commons.lang3.tuple.ImmutablePair;
import reika.dragonapi.base.CoreContainer;
import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.NonPoweredMachineScreen;
import reika.rotarycraft.blockentities.transmission.BlockEntityBevelGear;
import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.PacketRegistry;

public class GuiBevel extends NonPoweredMachineScreen<BlockEntityBevelGear, CoreContainer<BlockEntityBevelGear>> {
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
    //private Level level = ModLoader.getMinecraftInstance().theWorld;

    public GuiBevel(CoreContainer<BlockEntityBevelGear> id, Inventory inventory, Component title) {
        super(id, inventory, title);
        bevel = (BlockEntityBevelGear) inventory.player.level.getBlockEntity(id.tile.getBlockPos());
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

        ResourceLocation file = new ResourceLocation(RotaryCraft.MODID, "textures/screen/bevelgui2.png");
        int px = 176;
       for (int i = 0; i < 6; i++) {
            String s = Direction.values()[i].name().substring(0, 1);
            if (true || BlockEntityBevelGear.isValid(Direction.values()[i], out)) {
                if (in.ordinal() == i)
                    //id is i
                    //pX, pY, pWidth, pHeight, pXTexStart, pYTexStart, pYDiffTex, pResourceLocation, pTextureWidth, pTextureHeight, pOnPress, pMessage
                    //                                  x,               y,                 width,       height, u, v, display string, color, shadow?, filepath
                    renderables.add(new ImageButton(j + 40, k + 8 + 48 + i * 22, 18, 18, px + 18, i * 18, file, this::actionPerformed));
                else
                    renderables.add(new ImageButton(j + 40, k + 8 + 48 + i * 22, 18, 18, px, i * 18, file, this::actionPerformed)); //todo s is not used rn
            } else {
                renderables.add(new ImageButton(j + 40, k + 8 + 48 + i * 22, 18, 18, 212, 0,file, this::actionPerformed));
            }
        }
        for (int i = 0; i < 6; i++) {
            String s = Direction.values()[i].name().substring(0, 1);
            if (BlockEntityBevelGear.isValid(in, Direction.values()[i])) {
                if (out.ordinal() == i)
                    //i + 6
                    renderables.add(new ImageButton(j + imageWidth - 40 - 18, k + 8 + 48 + i * 22, 18, 18, px + 18, i * 18, file, this::actionPerformed));
                else
                    renderables.add(new ImageButton(j + imageWidth - 40 - 18, k + 8 + 48 + i * 22, 18, 18, px, i * 18, file, this::actionPerformed)); //todo s is not used rn
            } else
                renderables.add(new ImageButton(j + imageWidth - 40 - 18, k + 8 + 48 + i * 22, 18, 18, 212, 0,file, this::actionPerformed));
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


    protected void actionPerformed(Button button) {
       /* if (button.id < 6) {
            in = Direction.values()[button.id];
            if (!BlockEntityBevelGear.isValid(in, out))
                out = in.getStepY() != 0 ? Direction.EAST : Direction.DOWN;
        } else if (button.id < 24000) {
            if (!BlockEntityBevelGear.isValid(in, Direction.values()[button.id - 6]))
                return;
            out = Direction.values()[button.id - 6];
        }*/
        this.getDirectionFromIO();
        this.init();
        bevel.direction = posn;
        ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.BEVEL.ordinal(), bevel, posn);
    }

    /**
     * todo Draw the foreground layer for the GuiContainer (everything in front of the items)
     */

    protected void drawGuiContainerForegroundLayer(PoseStack poseStack, int a, int b) {
        font.draw(poseStack, "Input Side", 24, 32, 4210752);
        font.draw(poseStack, "Output Side", 99, 32, 4210752);

        int j = (width - imageWidth) / 2 - 2;
        int k = (height - imageHeight) / 2 - 12;

        if (ConfigRegistry.COLORBLIND.getState()) {
            for (int i = 0; i < 6; i++) {
                font.draw(poseStack, String.valueOf(i), 30, 49 + i * 22, 0);
            }

            for (int i = 0; i < 6; i++) {
                font.draw(poseStack, String.valueOf(i), imageWidth - 68, 49 + i * 22, 0);
            }
        }
    }

    @Override
    protected String getGuiTexture() {
        return "textures/screen/bevelgui2";
    }
}
