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

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.NonPoweredMachineScreen;
import reika.rotarycraft.blockentities.surveying.BlockEntityGPR;
import reika.rotarycraft.gui.container.machine.BlankContainer;
import reika.rotarycraft.registry.PacketRegistry;

/**
 * The GPR readout: a colored cross-section of the scanned underground painted one 2×2 cell per
 * block via {@link GuiGraphicsExtractor#fill}. Pressing {@code [} / {@code ]} shifts the scan plane
 * forward/back along the view direction, and {@code \} recenters it; each keypress both nudges the
 * client's copy optimistically and tells the server (which re-runs the authoritative scan).
 */
public class GuiGPR extends NonPoweredMachineScreen<BlockEntityGPR, BlankContainer<BlockEntityGPR>> {

    public static final int UNIT = 2;

    private final BlockEntityGPR gpr;

    public GuiGPR(BlankContainer<BlockEntityGPR> menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 215);
        gpr = menu.tile;
        this.inventory = inventory;
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (super.keyPressed(event))
            return true;
        switch (event.key()) {
            case GLFW.GLFW_KEY_LEFT_BRACKET -> {
                this.sendShift(1);
                return true;
            }
            case GLFW.GLFW_KEY_RIGHT_BRACKET -> {
                this.sendShift(-1);
                return true;
            }
            case GLFW.GLFW_KEY_BACKSLASH -> {
                this.sendShift(0);
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    private void sendShift(int amt) {
        ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.GPR.ordinal(), gpr, amt);
        gpr.shift(gpr.getGuiDirection(), amt); //optimistic local update; the server sync corrects it
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor stack, int pX, int pY, float pPartialTick) {
        super.extractBackground(stack, pX, pY, pPartialTick);
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2 + 1;
        this.drawRadar(stack, j, k);
    }

    private void drawRadar(GuiGraphicsExtractor stack, int a, int b) {
        int r = gpr.getRange();
        for (int x = -r; x <= r; x++) {
            for (int dd = 1; dd <= BlockEntityGPR.MAX_HEIGHT; dd++) {
                int color = 0xff000000 | gpr.getColor(x, dd);
                int x0 = a + 7 + UNIT * (x + BlockEntityGPR.MAX_WIDTH / 2);
                int y0 = b + 16 + UNIT * dd - 2;
                stack.fill(x0, y0, x0 + UNIT, y0 + UNIT, color);
            }
        }
        String s = gpr.getLookDirection().toString();
        int w = font.width(s);
        stack.text(font, s, a + (imageWidth - w) / 2, b + 6, 0xFFFFFFFF);
    }

    @Override
    protected String getGuiTexture() {
        return "gprgui";
    }
}
