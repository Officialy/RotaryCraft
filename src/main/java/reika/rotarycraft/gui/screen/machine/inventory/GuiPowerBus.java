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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.MachineScreen;
import reika.rotarycraft.blockentities.transmission.BlockEntityPowerBus;
import reika.rotarycraft.gui.container.machine.inventory.ContainerPowerBus;
import reika.rotarycraft.registry.PacketRegistry;

/**
 * Power bus GUI: one gear slot per horizontal side with a speed/torque mode toggle next to each
 * (legacy POWERBUS packet), plus the per-side output readout.
 */
public class GuiPowerBus extends MachineScreen<BlockEntityPowerBus, ContainerPowerBus> {

    private static final Direction[] SIDES = {Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};
    private static final int[] SLOT_X = {80, 80, 44, 116};
    private static final int[] SLOT_Y = {13, 57, 35, 35};

    private final BlockEntityPowerBus bus;

    public GuiPowerBus(ContainerPowerBus container, Inventory inv, Component title) {
        super(container, inv, title, 176, 166);
        bus = (BlockEntityPowerBus) inv.player.level().getBlockEntity(container.tile.getBlockPos());
        inventory = inv;
    }

    @Override
    public void init() {
        super.init();
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;
        for (int i = 0; i < 4; i++) {
            final int id = i;
            // Mode toggle beside each side slot: speed mode multiplies speed, torque mode torque.
            addRenderableWidget(Button.builder(Component.literal("x"), b -> this.toggleMode(id))
                    .bounds(j + SLOT_X[i] + 18, k + SLOT_Y[i], 12, 12).build());
        }
    }

    private void toggleMode(int side) {
        ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.POWERBUS.ordinal(), bus, side);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partial) {
        super.extractBackground(graphics, mouseX, mouseY, partial);
        int j = (Minecraft.getInstance().getWindow().getGuiScaledWidth() - imageWidth) / 2;
        int k = (Minecraft.getInstance().getWindow().getGuiScaledHeight() - imageHeight) / 2;

        for (int i = 0; i < 4; i++) {
            Direction dir = SIDES[i];
            String mode = bus.isSideSpeedMode(dir) ? "Spd" : "Trq";
            api.drawCenteredStringNoShadow(graphics, font, mode, j + SLOT_X[i] + 24, k + SLOT_Y[i] + 13, 0xff404040);
        }
        api.drawCenteredStringNoShadow(graphics, font,
                bus.getInputPower() + "W over " + bus.getTotalOutputSidesDisplay() + " sides",
                j + imageWidth / 2, k + 76, 0xff404040);
    }

    // The bus is not a standard shaft receiver; the input readout replaces the power tab.
    @Override
    protected void drawPowerTab(GuiGraphicsExtractor stack, int j, int k) {
    }

    @Override
    protected String getGuiTexture() {
        return "bus";
    }
}
