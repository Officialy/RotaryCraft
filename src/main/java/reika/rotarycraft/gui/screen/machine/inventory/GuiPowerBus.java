/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.gui.screen.machine.inventory;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import reika.dragonapi.instantiable.gui.ImagedGuiButton;
import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.NonPoweredMachineScreen;
import reika.rotarycraft.blockentities.transmission.BlockEntityPowerBus;
import reika.rotarycraft.gui.container.machine.inventory.ContainerPowerBus;
import reika.rotarycraft.registry.PacketRegistry;

/**
 * 1.7.10 GuiPowerBus: the 220x220 "bus" panel with a gear slot on each usable side, a tinted square
 * marking each side's slot (sides facing another bus block or the hub are painted over with panel
 * texture), and a speed/torque toggle button beside each slot.
 */
public class GuiPowerBus extends NonPoweredMachineScreen<BlockEntityPowerBus, ContainerPowerBus> {

    private static final int[] SLOT_X = {101, 101, 65, 137};
    private static final int[] SLOT_Y = {32, 104, 68, 68};

    private final BlockEntityPowerBus bus;

    public GuiPowerBus(ContainerPowerBus container, Inventory inv, Component title) {
        super(container, inv, title, 220, 220);
        bus = container.tile;
        inventory = inv;
    }

    @Override
    protected void init() {
        super.init();
        int j = (width - imageWidth) / 2 - 2;
        int k = (height - imageHeight) / 2 - 12;
        Identifier file = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/buttons.png");
        int[] dx = {j + 103, j + 103, j + 49, j + 139 + 18};
        int[] dy = {k + 26, k + 134, k + 66 + 18 - 4, k + 66 + 18 - 4};
        for (int i = 0; i < 4; i++) {
            Direction dir = Direction.values()[i + 2];
            int u = bus.isSideSpeedMode(dir) ? 54 : 36;
            final int id = i;
            if (bus.canHaveItemInSlot(dir))
                addRenderableWidget(new ImagedGuiButton(i, dx[i], dy[i], 18, 18, u, 36, file, b -> this.toggleMode(id)));
        }
    }

    /** 1.7.10 flipped the side locally for the button art and sent the same toggle to the server. */
    private void toggleMode(int side) {
        Direction dir = Direction.values()[side + 2];
        bus.setMode(dir, !bus.isSideSpeedMode(dir));
        ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.POWERBUS.wireId(), bus, side);
        this.rebuildWidgets();
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractLabels(graphics, mouseX, mouseY);
        for (int i = 0; i < 4; i++) {
            Direction dir = Direction.values()[i + 2];
            int dx = SLOT_X[i];
            int dy = SLOT_Y[i];
            if (bus.canHaveItemInSlot(dir))
                graphics.fill(dx, dy, dx + 18, dy + 18, getColorForSide(dir));
            else
                graphics.blit(RenderPipelines.GUI_TEXTURED, this.getTextureIdentifier(), dx, dy, 8, 8, 18, 18, 256, 256);
        }
    }

    private static int getColorForSide(Direction dir) {
        return switch (dir) {
            case EAST -> 0x44ffff00;
            case NORTH -> 0x440000ff;
            case SOUTH -> 0x44ff0000;
            case WEST -> 0x4400ff00;
            default -> 0x44ffffff;
        };
    }

    @Override
    protected String getGuiTexture() {
        return "bus";
    }
}
