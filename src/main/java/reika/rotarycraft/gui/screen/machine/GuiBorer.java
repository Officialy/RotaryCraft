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
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.MachineScreen;
import reika.rotarycraft.blockentities.production.BlockEntityBorer;
import reika.rotarycraft.gui.container.machine.ContainerBorer;

/**
 * Cut-shape editor for the Boring Machine. Draws the 7x5 grid of toggle cells over the borer GUI
 * background; clicking a cell flips whether the borer bores it, sent to the server as a
 * {@code clickMenuButton} (button id {@code col*ROWS+row}). The centre cell is the borer's own line.
 * A drops-on/off toggle and select-all / clear buttons sit below the grid.
 */
public class GuiBorer extends MachineScreen<BlockEntityBorer, ContainerBorer> {

    private static final int GRID_X = 25;
    private static final int GRID_Y = 16;
    private static final int CELL = 18;

    private final BlockEntityBorer borer;

    public GuiBorer(ContainerBorer container, Inventory inv, Component title) {
        super(container, inv, title, 176, 169);
        borer = container.getBorer();
        inventory = inv;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partial) {
        super.extractBackground(graphics, mouseX, mouseY, partial);
        int j = (Minecraft.getInstance().getWindow().getGuiScaledWidth() - imageWidth) / 2;
        int k = (Minecraft.getInstance().getWindow().getGuiScaledHeight() - imageHeight) / 2;

        for (int col = 0; col < BlockEntityBorer.COLS; col++) {
            for (int row = 0; row < BlockEntityBorer.ROWS; row++) {
                int x = j + GRID_X + CELL * col;
                int y = k + GRID_Y + CELL * row;
                boolean on = borer.cutShape[col][row];
                boolean centre = col == 3 && row == 4;
                int fill = centre ? 0xFF3060C0 : (on ? 0xFF30A030 : 0xFF303030);
                graphics.fill(x + 1, y + 1, x + CELL - 1, y + CELL - 1, fill);
                graphics.fill(x, y, x + CELL, y + 1, 0xFF101010);
                graphics.fill(x, y + CELL - 1, x + CELL, y + CELL, 0xFF101010);
                graphics.fill(x, y, x + 1, y + CELL, 0xFF101010);
                graphics.fill(x + CELL - 1, y, x + CELL, y + CELL, 0xFF101010);
            }
        }

        String dropStatus = "Drops: " + (borer.drops ? "On" : "Off");
        graphics.text(font, dropStatus, j + GRID_X, k + GRID_Y + CELL * BlockEntityBorer.ROWS + 4, 0x404040);
        graphics.text(font, borer.isJammed() ? "JAMMED" : (borer.drops ? "Ready" : "Ready"),
                j + GRID_X + 90, k + GRID_Y + CELL * BlockEntityBorer.ROWS + 4, borer.isJammed() ? 0xC03030 : 0x309030);
    }

    @Override
    public boolean mouseClicked(net.minecraft.client.input.MouseButtonEvent event, boolean doubleClick) {
        double mx = event.x();
        double my = event.y();
        int j = (Minecraft.getInstance().getWindow().getGuiScaledWidth() - imageWidth) / 2;
        int k = (Minecraft.getInstance().getWindow().getGuiScaledHeight() - imageHeight) / 2;
        // Grid cells
        for (int col = 0; col < BlockEntityBorer.COLS; col++) {
            for (int row = 0; row < BlockEntityBorer.ROWS; row++) {
                int x = j + GRID_X + CELL * col;
                int y = k + GRID_Y + CELL * row;
                if (mx >= x && mx < x + CELL && my >= y && my < y + CELL) {
                    int id = col * BlockEntityBorer.ROWS + row;
                    // Optimistic local flip for instant feedback; server is authoritative + re-syncs.
                    borer.cutShape[col][row] = !borer.cutShape[col][row];
                    this.sendButton(id);
                    return true;
                }
            }
        }
        // Drops toggle row
        int dy = k + GRID_Y + CELL * BlockEntityBorer.ROWS + 4;
        if (my >= dy && my < dy + 10 && mx >= j + GRID_X && mx < j + GRID_X + 80) {
            borer.drops = !borer.drops;
            this.sendButton(ContainerBorer.BUTTON_DROPS);
            return true;
        }
        return super.mouseClicked(event, doubleClick);
    }

    private void sendButton(int id) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.gameMode != null)
            mc.gameMode.handleInventoryButtonClick(this.menu.containerId, id);
    }

    @Override
    protected void drawPowerTab(GuiGraphicsExtractor stack, int j, int k) {
        var loc = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/powertab.png");
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j, k + 4, 0, 4, 42, imageHeight - 4, 256, 256);

        long frac = borer.MINPOWER > 0 ? (borer.power * 29L) / borer.MINPOWER : 29;
        if (frac > 29) frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 144, 0, 0, (int) frac, 4, 256, 256);

        frac = borer.MINSPEED > 0 ? (borer.omega * 29L) / borer.MINSPEED : (borer.omega > 0 ? 29 : 0);
        if (frac > 29) frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 84, 0, 0, (int) frac, 4, 256, 256);

        frac = borer.MINTORQUE > 0 ? (borer.torque * 29L) / borer.MINTORQUE : (borer.torque > 0 ? 29 : 0);
        if (frac > 29) frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, loc, imageWidth + j + 5, imageHeight + k - 24, 0, 0, (int) frac, 4, 256, 256);

        api.drawCenteredStringNoShadow(stack, font, "Power:", imageWidth + j + 20, k + 9, 0xff000000);
        api.drawCenteredStringNoShadow(stack, font, "Speed:", imageWidth + j + 20, k + 69, 0xff000000);
        api.drawCenteredStringNoShadow(stack, font, "Torque:", imageWidth + j + 20, k + 129, 0xff000000);
    }

    @Override
    protected String getGuiTexture() {
        return "borergui";
    }
}
