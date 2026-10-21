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
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.dragonapi.libraries.java.ReikaJavaLibrary;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.MachineScreen;
import reika.rotarycraft.blockentities.auxiliary.BlockEntityHeater;
import reika.rotarycraft.gui.container.machine.inventory.ContainerHeater;
import reika.rotarycraft.registry.PacketRegistry;

/** 1.7.10 GuiHeater: the fuel grid, a typed target temperature sent to the server, and the power tab. */
public class GuiHeater extends MachineScreen<BlockEntityHeater, ContainerHeater> {

    private static final Identifier POWER_TAB = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/powertab.png");

    private final BlockEntityHeater heater;
    private EditBox input;
    public int temperature;

    public GuiHeater(ContainerHeater container, Inventory inv, Component title) {
        super(container, inv, title, 176, 167);
        heater = container.tile;
        temperature = heater.setTemperature;
        inventory = inv;
    }

    @Override
    protected void init() {
        super.init();
        int j = (width - imageWidth) / 2 + 8;
        int k = (height - imageHeight) / 2 - 12;
        input = new EditBox(font, j + imageWidth / 2 + 40, k + 67, 32, 16, Component.literal("Temperature"));
        input.setFocused(false);
        input.setMaxLength(4);
        addRenderableWidget(input);
    }

    /** 1.7.10 updateScreen: every tick a non-empty field is parsed and sent; a non-digit is dropped and zeroes the target. */
    @Override
    protected void containerTick() {
        super.containerTick();
        String text = input.getValue();
        if (text.isEmpty())
            return;
        if (!text.matches("^[0-9 ]+$")) {
            temperature = 0;
            input.setValue(text.substring(0, text.length() - 1));
            ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.HEATER.wireId(), heater, temperature);
            return;
        }
        temperature = ReikaJavaLibrary.safeIntParse(text);
        if (temperature >= 0)
            ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.HEATER.wireId(), heater, temperature);
        heater.setTemperature = temperature;
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractLabels(graphics, mouseX, mouseY);
        graphics.text(font, "Temperature Control:", 26, 59, 0xFF404040, false);
        if (!input.isFocused())
            graphics.text(font, String.format("%d", heater.setTemperature), 140, 59, 0xFFFFFFFF, false);
    }

    /** GuiHeater labelled the player inventory at x=8. */
    @Override
    protected boolean inventoryLabelLeft() {
        return true;
    }

    @Override
    protected void drawPowerTab(GuiGraphicsExtractor stack, int j, int k) {
        stack.blit(RenderPipelines.GUI_TEXTURED, POWER_TAB, imageWidth + j, k + 4, 0, 4, 42, imageHeight - 20, 256, 256);
        stack.blit(RenderPipelines.GUI_TEXTURED, POWER_TAB, imageWidth + j, k + 4 + imageHeight - 20, 0, 157, 42, 6, 256, 256);

        long frac = Math.min(29, powerFrac(heater.power, heater.MINPOWER));
        stack.blit(RenderPipelines.GUI_TEXTURED, POWER_TAB, imageWidth + j + 5, imageHeight + k - 145, 0, 0, (int) frac, 4, 256, 256);
        frac = Math.min(29, powerFrac(heater.omega, heater.MINSPEED));
        stack.blit(RenderPipelines.GUI_TEXTURED, POWER_TAB, imageWidth + j + 5, imageHeight + k - 85, 0, 0, (int) frac, 4, 256, 256);
        frac = Math.min(29, powerFrac(heater.torque, heater.MINTORQUE));
        stack.blit(RenderPipelines.GUI_TEXTURED, POWER_TAB, imageWidth + j + 5, imageHeight + k - 25, 0, 0, (int) frac, 4, 256, 256);

        api.drawCenteredStringNoShadow(stack, font, "Power:", imageWidth + j + 20, k + 9, 0xff000000);
        api.drawCenteredStringNoShadow(stack, font, "Speed:", imageWidth + j + 20, k + 69, 0xff000000);
        api.drawCenteredStringNoShadow(stack, font, "Torque:", imageWidth + j + 20, k + 129, 0xff000000);
    }

    @Override
    protected Rect2i getPowerTabArea() {
        return new Rect2i(leftPos + imageWidth, topPos + 4, 42, imageHeight - 14);
    }

    @Override
    protected String getGuiTexture() {
        return "heatergui";
    }
}
