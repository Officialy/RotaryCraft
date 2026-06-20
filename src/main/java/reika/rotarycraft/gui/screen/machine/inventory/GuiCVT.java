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
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.dragonapi.libraries.java.ReikaJavaLibrary;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.NonPoweredMachineScreen;
import reika.rotarycraft.blockentities.transmission.BlockEntityAdvancedGear;
import reika.rotarycraft.blockentities.transmission.BlockEntityAdvancedGear.CVTMode;
import reika.rotarycraft.gui.container.machine.inventory.ContainerCVT;
import reika.rotarycraft.registry.PacketRegistry;

public class GuiCVT extends NonPoweredMachineScreen<BlockEntityAdvancedGear, ContainerCVT> {

    private static final int LUBE_CAPACITY = 20000;
    /** Lubricant fill colour (opaque amber), used to draw the right-hand tank bar. */
    private static final int LUBE_COLOR = 0xFFD9A441;

    private final BlockEntityAdvancedGear cvt;
    private CVTMode mode;
    private boolean reduction;
    private EditBox input;
    private int buttonTimer;

    public GuiCVT(ContainerCVT container, Inventory inv, Component title) {
        super(container, inv, title, 240, 237);
        cvt = container.tile;
        mode = cvt.getMode();
        reduction = cvt.getRatio() < 0;
    }

    @Override
    protected void init() {
        super.init();
        input = null;
        // Mode-cycle button: always present (top-right of the gui).
        addRenderableWidget(Button.builder(Component.literal("Mode"), b -> this.cycleMode())
                .bounds(leftPos + 200, topPos + 6, 36, 20).build());

        switch (mode) {
            case MANUAL -> {
                input = new EditBox(font, leftPos + 152, topPos + 27, 26, 16, Component.literal("Ratio"));
                input.setMaxLength(3);
                input.setValue("");
                input.setFocused(false);
                addRenderableWidget(input);
                addRenderableWidget(Button.builder(Component.literal(reduction ? "Torque" : "Speed"), b -> this.toggleReduction())
                        .bounds(leftPos + 122, topPos + 51, 80, 20).build());
            }
            case AUTO -> {
                input = new EditBox(font, leftPos + 152, topPos + 36, 76, 16, Component.literal("Target"));
                input.setMaxLength(9);
                input.setValue("");
                input.setFocused(false);
                addRenderableWidget(input);
            }
            case REDSTONE -> {
                addRenderableWidget(Button.builder(Component.literal(cvt.getCVTString(true)), b -> this.incrementRedstoneState(true))
                        .bounds(leftPos + 153, topPos + 31, 71, 20).build());
                addRenderableWidget(Button.builder(Component.literal(cvt.getCVTString(false)), b -> this.incrementRedstoneState(false))
                        .bounds(leftPos + 153, topPos + 54, 71, 20).build());
            }
        }
    }

    private void cycleMode() {
        if (buttonTimer > 0)
            return;
        buttonTimer = 8;
        ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.CVTMODE.ordinal(), cvt);
        mode = mode.next();
        this.rebuildWidgets();
    }

    private void toggleReduction() {
        if (buttonTimer > 0)
            return;
        buttonTimer = 8;
        reduction = !reduction;
        int v = Math.abs(cvt.getRatio());
        if (v == 0)
            v = 1;
        ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.CVTRATIO.ordinal(), cvt, reduction ? -v : v);
        this.rebuildWidgets();
    }

    private void incrementRedstoneState(boolean on) {
        if (buttonTimer > 0)
            return;
        buttonTimer = 8;
        ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.CVTREDSTONESTATE.ordinal(), cvt, on ? 1 : 0);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (buttonTimer > 0)
            buttonTimer--;
        // A server-side mode change (redstone toggle, etc.) needs the widget set rebuilt.
        if (mode != cvt.getMode()) {
            mode = cvt.getMode();
            reduction = cvt.getRatio() < 0;
            this.rebuildWidgets();
            return;
        }
        if (input == null)
            return;
        String text = input.getValue();
        if (text.isEmpty())
            return;
        if (!text.matches("^[0-9 ]+$")) {
            input.setValue(text.substring(0, Math.max(0, text.length() - 1)));
            return;
        }
        int val = ReikaJavaLibrary.safeIntParse(text);
        if (val == 0)
            return;
        if (mode == CVTMode.AUTO) {
            ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.CVTTARGET.ordinal(), cvt, val);
        } else if (mode == CVTMode.MANUAL) {
            ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.CVTRATIO.ordinal(), cvt, reduction ? -val : val);
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        switch (mode) {
            case MANUAL -> {
                graphics.text(font, "Belt Ratio:", imageWidth / 2 - 32, 31, 4210752, false);
                if (input == null || !input.isFocused())
                    graphics.text(font, String.format("%d", Math.abs(cvt.getRatio())), imageWidth / 2 + 36, 31, 0xffffffff, false);
            }
            case AUTO -> {
                graphics.text(font, "Target Torque:", imageWidth / 2 - 48, 40, 4210752, false);
                if (input == null || !input.isFocused())
                    graphics.text(font, String.format("%d", Math.abs(cvt.getTargetTorque())), imageWidth / 2 + 36, 40, 0xffffffff, false);
                graphics.text(font, String.format("Current Input: %d Nm", cvt.getTorqueIn()), imageWidth / 2 - 30, 60, 4210752, false);
                int r = cvt.getRatio();
                graphics.text(font, String.format("Current Ratio: %dx (%s)", Math.abs(r), r < 0 ? "Torque" : "Speed"), imageWidth / 2 - 30, 72, 4210752, false);
            }
            case REDSTONE -> {
                graphics.text(font, "Belt Ratio:", imageWidth / 2 - 46, 48, 4210752, false);
                api.drawItemStack(graphics, font, new ItemStack(Blocks.REDSTONE_TORCH), 129, 31);
                api.drawItemStack(graphics, font, new ItemStack(Blocks.REDSTONE_TORCH), 129, 54);
            }
        }
        super.extractLabels(graphics, mouseX, mouseY);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        int j = leftPos;
        int k = topPos;

        // Lubricant tank bar (right edge), filling upward from the bottom of the 48px column.
        if (cvt.hasLubricant()) {
            int h = Math.min(48, 48 * cvt.getLubricant() / LUBE_CAPACITY);
            graphics.fill(j + 186, k + 137 - h, j + 202, k + 137, LUBE_COLOR);
        }
        if (api.isMouseInBox(j + 185, j + 202, k + 88, k + 149, mouseX, mouseY))
            api.drawTooltipAt(graphics, font, String.format("Lubricant: %d/%d", cvt.getLubricant(), LUBE_CAPACITY), mouseX, mouseY);

        // Manual mode: show the belt-limited maximum ratio (red when the requested ratio exceeds it).
        if (mode == CVTMode.MANUAL) {
            int max = cvt.getMaxRatio();
            int r = Math.abs(cvt.getRatio());
            if (r > max)
                api.drawCenteredStringNoShadow(graphics, font, String.format("(%d)", max), j + imageWidth / 2 + 88, k + 31, 0xff0000);
            else
                api.drawCenteredStringNoShadow(graphics, font, String.format("(%d)", r == 0 ? 1 : r), j + imageWidth / 2 + 88, k + 31, 4210752);
        }

        if (input != null)
            input.extractRenderState(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    protected String getGuiTexture() {
        return mode == CVTMode.REDSTONE ? "cvtgui2" : "cvtgui";
    }
}
