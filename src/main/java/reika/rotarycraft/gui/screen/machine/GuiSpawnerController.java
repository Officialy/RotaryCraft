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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.GuiPowerOnlyMachine;
import reika.rotarycraft.blockentities.farming.BlockEntitySpawnerController;
import reika.rotarycraft.gui.container.machine.ContainerSpawnerController;
import reika.rotarycraft.registry.PacketRegistry;

/** Spawner-controller GUI: delay entry box + enable/disable toggle (legacy layout). */
public class GuiSpawnerController extends GuiPowerOnlyMachine<BlockEntitySpawnerController, ContainerSpawnerController> {

    private final BlockEntitySpawnerController spawner;
    private EditBox input;
    private boolean disabled;

    public GuiSpawnerController(ContainerSpawnerController container, Inventory inv, Component title) {
        super(container, inv, title, 176, 75);
        spawner = (BlockEntitySpawnerController) inv.player.level().getBlockEntity(container.tile.getBlockPos());
        disabled = spawner.disable;
    }

    @Override
    public void init() {
        super.init();
        int j = (width - imageWidth) / 2 + 8;
        int k = (height - imageHeight) / 2 - 12;
        addRenderableWidget(Button.builder(Component.literal("Disable/Enable"), b -> this.toggle())
                .bounds(j + imageWidth / 2 - 48, k + 31, 80, 20).build());
        input = new EditBox(font, j + imageWidth / 2 - 7, k + 59, 26, 16, Component.literal("delay"));
        input.setMaxLength(3);
        input.setResponder(this::onDelayTyped);
        addRenderableWidget(input);
    }

    private void toggle() {
        disabled = !disabled;
        this.send();
    }

    private void onDelayTyped(String text) {
        if (text.isEmpty())
            return;
        this.send();
    }

    /** Legacy protocol: -1 = disabled, else the delay in ticks. */
    private void send() {
        int dat;
        if (disabled) {
            dat = -1;
        }
        else {
            int delay = BlockEntitySpawnerController.BASEDELAY;
            try {
                delay = 20 * Integer.parseInt(input.getValue());
            }
            catch (NumberFormatException ignored) {
            }
            dat = Math.max(0, delay);
        }
        ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.SPAWNERTIMER.ordinal(), spawner, dat);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partial) {
        super.extractBackground(graphics, mouseX, mouseY, partial);
        int j = (Minecraft.getInstance().getWindow().getGuiScaledWidth() - imageWidth) / 2;
        int k = (Minecraft.getInstance().getWindow().getGuiScaledHeight() - imageHeight) / 2;
        String status = spawner.disable ? "Disabled" : "Delay: " + spawner.getDelay() / 20 + "s";
        api.drawCenteredStringNoShadow(graphics, font, status, j + imageWidth / 2, k + 14, 0xff404040);
    }

    @Override
    protected String getGuiTexture() {
        return "spawnercontrollergui";
    }
}
