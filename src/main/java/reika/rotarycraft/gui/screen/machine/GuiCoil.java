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
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.dragonapi.libraries.java.ReikaJavaLibrary;
import reika.dragonapi.libraries.mathsci.ReikaEngLibrary;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.NonPoweredMachineScreen;
import reika.rotarycraft.blockentities.transmission.BlockEntityAdvancedGear;
import reika.rotarycraft.gui.container.machine.BlankContainer;
import reika.rotarycraft.registry.PacketRegistry;

public class GuiCoil extends NonPoweredMachineScreen<BlockEntityAdvancedGear, BlankContainer<BlockEntityAdvancedGear>> {
    private final BlockEntityAdvancedGear coil;
    private EditBox inputOmega;
    private EditBox inputTorque;
    private int omega;
    private int torque;

    /**
     * This screen expects the menu to be opened with a BlockPos in the buffer, and the container to provide the tile entity.
     */
    public GuiCoil(BlankContainer<BlockEntityAdvancedGear> container, Inventory inv, Component title) {
        super(container, inv, title);
        coil = container.tile;
        imageHeight = coil.isCreative() ? 72 : 105;
        imageWidth = 176;
    }

    @Override
    protected void init() {
        super.init();
        int j = (width - imageWidth) / 2 + 8;
        int k = (height - imageHeight) / 2 - 12;
        inputOmega = new EditBox(font, j + imageWidth / 2 - 15, k + 30, 56, 16, Component.literal("Omega"));
        inputOmega.setMaxLength(8);
        inputOmega.setValue("");
        inputOmega.setFocused(false);
        addRenderableWidget(inputOmega);
        inputTorque = new EditBox(font, j + imageWidth / 2 - 15, k + 60, 56, 16, Component.literal("Torque"));
        inputTorque.setMaxLength(8);
        inputTorque.setValue("");
        inputTorque.setFocused(false);
        addRenderableWidget(inputTorque);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean handled = super.keyPressed(keyCode, scanCode, modifiers);
        handled |= inputOmega.keyPressed(keyCode, scanCode, modifiers);
        handled |= inputTorque.keyPressed(keyCode, scanCode, modifiers);
        return handled;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        boolean handled = super.charTyped(codePoint, modifiers);
        handled |= inputOmega.charTyped(codePoint, modifiers);
        handled |= inputTorque.charTyped(codePoint, modifiers);
        return handled;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean handled = super.mouseClicked(mouseX, mouseY, button);
        handled |= inputOmega.mouseClicked(mouseX, mouseY, button);
        handled |= inputTorque.mouseClicked(mouseX, mouseY, button);
        return handled;
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        boolean valid1 = true;
        boolean valid2 = true;
        String omegaText = inputOmega.getValue();
        String torqueText = inputTorque.getValue();
        if (omegaText.isEmpty() && torqueText.isEmpty()) {
            return;
        }
        if (omegaText.isEmpty())
            valid1 = false;
        if (torqueText.isEmpty())
            valid2 = false;
        if (!omegaText.isEmpty() && !(omegaText.matches("^[0-9 ]+$"))) {
            omega = 0;
            inputOmega.setValue(omegaText.substring(0, Math.max(0, omegaText.length() - 1)));
            ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.COILSPEED.ordinal(), coil, omega);
            valid1 = false;
        }
        if (!torqueText.isEmpty() && !(torqueText.matches("^[0-9 ]+$"))) {
            torque = 0;
            inputTorque.setValue(torqueText.substring(0, Math.max(0, torqueText.length() - 1)));
            ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.COILTORQUE.ordinal(), coil, torque);
            valid2 = false;
        }
        if (!valid1 && !valid2)
            return;
        if (valid1) {
            omega = ReikaJavaLibrary.safeIntParse(omegaText);
            if (omega >= 0) {
                int max = Math.min(coil.getMaximumEmission(), /*RotaryConfig.omegalimit*/Integer.MAX_VALUE);
                if (omega > max) {
                    omega = max;
                    inputOmega.setValue(String.valueOf(max));
                }
                ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.COILSPEED.ordinal(), coil, omega);
            }
        }
        if (valid2) {
            torque = ReikaJavaLibrary.safeIntParse(torqueText);
            if (torque >= 0) {
                int max = Math.min(coil.getMaximumEmission(), /*RotaryConfig.torquelimit*/Integer.MAX_VALUE);
                if (torque > max) {
                    torque = max;
                    inputTorque.setValue(String.valueOf(max));
                }
                ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.COILTORQUE.ordinal(), coil, torque);
            }
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;
        graphics.drawString(font, "Output Speed", imageWidth / 2 - 82, 22, 4210752, false);
        if (!coil.isCreative())
            graphics.drawString(font, String.format("(Max %d)", coil.getMaximumEmission()), imageWidth / 2 - 82, 37, 4210752, false);
        graphics.drawString(font, "Output Torque", imageWidth / 2 - 82, 52, 4210752, false);
        graphics.drawString(font, "rad/s", imageWidth / 2 + 53, 22, 4210752, false);
        graphics.drawString(font, "Nm", imageWidth / 2 + 53, 52, 4210752, false);
        if (!coil.isCreative()) {
            double e = coil.getEnergy() / 20D;
            String s = String.format("Stored Energy: %.3f%sJ", ReikaMathLibrary.getThousandBase(e), ReikaEngLibrary.getSIPrefix(e));
            graphics.drawString(font, s, imageWidth / 2 - 82, 80 - 8, 4210752, false);
            long max = coil.getMaxStorageCapacity();
            s = String.format("Max Energy: %.3f%sJ", ReikaMathLibrary.getThousandBase(max), ReikaEngLibrary.getSIPrefix(max));
            graphics.drawString(font, s, imageWidth / 2 - 82, 80 - 8 + 14, 4210752, false);
        }
        if (!inputOmega.isFocused())
            graphics.drawString(font, String.format("%d", coil.getReleaseOmega()), imageWidth / 2 - 3, 22, 0xffffffff, false);
        if (!inputTorque.isFocused())
            graphics.drawString(font, String.format("%d", coil.getReleaseTorque()), imageWidth / 2 - 3, 52, 0xffffffff, false);
        super.renderLabels(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(graphics, partialTick, mouseX, mouseY);
        inputOmega.render(graphics, mouseX, mouseY, partialTick);
        inputTorque.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    protected String getGuiTexture() {
        return coil.isCreative() ? "coilgui" : "coilgui2";
    }
}
