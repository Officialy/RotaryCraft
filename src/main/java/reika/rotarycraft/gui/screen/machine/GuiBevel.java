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
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.apache.commons.lang3.tuple.ImmutablePair;
import reika.dragonapi.instantiable.gui.ImagedGuiButton;
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
        super(id, inventory, title, 176, 192);
        bevel = (BlockEntityBevelGear) inventory.player.level().getBlockEntity(id.tile.getBlockPos());
        this.inventory = inventory;
        posn = bevel.direction;
        this.getIOFromDirection();
    }

    @Override
    public void init() {
        super.init();
        int j = (width - imageWidth) / 2 - 2;
        int k = (height - imageHeight) / 2 - 12;

        Identifier file = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/bevelgui2.png");
        int px = 176;
        // Input-side selectors (ids 0-5). Legacy always took the px path here (the validity
        // check was short-circuited with "true ||"); selected side uses the lit column (px+18).
        for (int i = 0; i < 6; i++) {
            String s = Direction.values()[i].name().substring(0, 1);
            int finalI = i;
            int u = in.ordinal() == i ? px + 18 : px;
            addRenderableWidget(new ImagedGuiButton(i, j + 40, k + 8 + 48 + i * 22, 18, 18, u, i * 18, s, 0, false, file, b -> actionPerformed(b, finalI)));
        }
        // Output-side selectors (ids 6-11). Invalid combos render the disabled icon at 212,0.
        for (int i = 0; i < 6; i++) {
            String s = Direction.values()[i].name().substring(0, 1);
            int finalI = i;
            int u, v;
            if (BlockEntityBevelGear.isValid(in, Direction.values()[i])) {
                u = out.ordinal() == i ? px + 18 : px;
                v = i * 18;
            } else {
                u = 212;
                v = 0;
            }
            addRenderableWidget(new ImagedGuiButton(i + 6, j + imageWidth - 40 - 18, k + 8 + 48 + i * 22, 18, 18, u, v, s, 0, false, file, b -> actionPerformed(b, finalI + 6)));
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
    public void extractRenderState(GuiGraphicsExtractor stack, int pMouseX, int pMouseY, float pPartialTick) {
        super.extractRenderState(stack, pMouseX, pMouseY, pPartialTick);

        int j = (width - imageWidth) / 2 - 2;
        int k = (height - imageHeight) / 2 - 12;

        stack.text(font,  "Input Side", j + 24, k + 32, 4210752);
        stack.text(font,  "Output Side", j + 99, k + 32, 4210752);

        if (ConfigRegistry.COLORBLIND.getState()) {
            for (int i = 0; i < 6; i++) {
                stack.text(font,  String.valueOf(i), j + 30, k + 49 + i * 22, 0);
            }

            for (int i = 0; i < 6; i++) {
                stack.text(font,  String.valueOf(i), j + imageWidth - 68, k + 49 + i * 22, 0);
            }
        }
    }

    @Override
    protected String getGuiTexture() {
        return "bevelgui2";
    }
}