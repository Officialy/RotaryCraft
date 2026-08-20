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
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.MachineScreen;
import reika.rotarycraft.blockentities.storage.BlockEntityScaleableChest;
import reika.rotarycraft.gui.container.machine.inventory.ContainerScaleChest;
import reika.rotarycraft.registry.PacketRegistry;

/**
 * Paged GUI for the Scaleable Chest. Drawn procedurally (panel + slot cells via
 * {@link GuiGraphicsExtractor#fill}) — no dedicated background texture ships. The Next/Back buttons
 * ask the server to re-open the menu on the neighbouring page.
 */
public class GuiScaleChest extends MachineScreen<BlockEntityScaleableChest, ContainerScaleChest> {

    private final BlockEntityScaleableChest chest;
    private final int page;

    public GuiScaleChest(ContainerScaleChest container, Inventory inv, Component title) {
        super(container, inv, title, 176, 216);
        chest = (BlockEntityScaleableChest) tile;
        page = container.page;
    }

    @Override
    protected void init() {
        super.init();
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;
        addRenderableWidget(Button.builder(Component.literal(">"), b -> this.shiftPage(1))
                .bounds(j + imageWidth - 22, k + 2, 20, 14).build());
        addRenderableWidget(Button.builder(Component.literal("<"), b -> this.shiftPage(-1))
                .bounds(j + 2, k + 2, 20, 14).build());
    }

    private void shiftPage(int dir) {
        int target = page + dir;
        if (target < 0 || target >= chest.getMaxPage() || target == page)
            return;
        ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.CHEST.ordinal(), chest, target);
        //the server re-opens the menu on the new page, which rebuilds this screen
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int pX, int pY, float pPartialTick) {
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;
        // Panel body + a lighter top strip for the title/buttons.
        graphics.fill(j, k, j + imageWidth, k + imageHeight, 0xFFC6C6C6);
        graphics.fill(j, k, j + imageWidth, k + 16, 0xFFB0B0B0);
        // Slot cells for every on-screen slot (off-screen hidden slots have negative x).
        for (Slot s : menu.slots) {
            if (s.x < 0)
                continue;
            int x = j + s.x;
            int y = k + s.y;
            graphics.fill(x - 1, y - 1, x + 17, y + 17, 0xFF373737);
            graphics.fill(x, y, x + 16, y + 16, 0xFF8B8B8B);
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int pMouseX, int pMouseY) {
        graphics.text(font, "Page " + (page + 1) + "/" + Math.max(1, chest.getMaxPage()), 26, 4, 0xFF404040);
        graphics.text(font, chest.getNumberSlots() + " slots", imageWidth - 70, 4, 0xFF404040);
    }

    @Override
    protected void drawPowerTab(GuiGraphicsExtractor stack, int j, int k) {
        //procedural GUI: no power tab
    }

    @Override
    protected String getGuiTexture() {
        return "scalechest"; //unused: extractBackground is fully procedural
    }
}
