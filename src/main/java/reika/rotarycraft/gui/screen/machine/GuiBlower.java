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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import reika.dragonapi.instantiable.gui.ImagedGuiButton;
import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.GuiPowerOnlyMachine;
import reika.rotarycraft.blockentities.BlockEntityBlower;
import reika.rotarycraft.gui.container.machine.BlowerContainer;
import reika.rotarycraft.registry.PacketRegistry;

public class GuiBlower extends GuiPowerOnlyMachine<BlockEntityBlower, BlowerContainer> {

    private final BlockEntityBlower tile;
    private final boolean[] controls;

    // Item metadata no longer exists in modern Minecraft, so the legacy "check metadata"
    // toggle is dropped; the surviving filters keep their original buttons.png texture rows.
    private static final PacketRegistry[] PACKETS = {PacketRegistry.BLOWERWHITELIST, PacketRegistry.BLOWERNBT, PacketRegistry.BLOWEROREDICT};
    private static final int[] TEX_V = {54, 90, 108};

    public GuiBlower(BlowerContainer container, Inventory inv, Component title) {
        super(container, inv, title, 176, 192);
        tile = (BlockEntityBlower) inv.player.level().getBlockEntity(container.tile.getBlockPos());
        inventory = inv;

        controls = new boolean[3];
        controls[0] = tile.isWhitelist;
        controls[1] = tile.checkNBT;
        controls[2] = !tile.useOreDict;
    }

    @Override
    public void init() {
        super.init();
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;

        Identifier s = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/blowergui.png");

        for (int i = 0; i < controls.length; i++) {
            int u = controls[i] ? 194 : 176;
            final int finalI = i;
            addRenderableWidget(new ImagedGuiButton(i, j + 25 + 36 * i, k + 64, 18, 18, u, TEX_V[i], s, b -> this.actionPerformed(finalI)));
        }
    }

    protected void actionPerformed(int id) {
        if (id >= 0 && id < controls.length) {
            ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PACKETS[id].ordinal(), tile);
            controls[id] = !controls[id];
        }
        this.init();
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor stack, int pX, int pY, float pPartialTick) {
        super.extractBackground(stack, pX, pY, pPartialTick);
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;

        int dy = 18;
        int x = 8;
        int y = 21;
        for (int i = 0; i < tile.matchingItems.length; i++) {
            ItemStack is = tile.matchingItems[i];
            if (is != null) {
                api.drawItemStack(stack, font, is, x + i % 9 * 18, y + i / 9 * dy);
            }
        }

        if (api.isMouseInBox(j + 25, j + 43, k + 64, k + 82, pX, pY)) {
            api.drawTooltipAt(stack, font, controls[0] ? "Whitelist" : "Blacklist", pX - j + 50, pY - k);
        }
        if (api.isMouseInBox(j + 25 + 36, j + 43 + 36, k + 64, k + 82, pX, pY)) {
            api.drawTooltipAt(stack, font, controls[1] ? "Use NBT" : "Ignore NBT", pX - j + 80, pY - k);
        }
        if (api.isMouseInBox(j + 25 + 36 * 2, j + 43 + 36 * 2, k + 64, k + 82, pX, pY)) {
            api.drawTooltipAt(stack, font, controls[2] ? "Match Exact" : "Use Ore Dictionary", pX - j, pY - k);
        }
    }

    @Override
    protected String getGuiTexture() {
        return "blowergui";
    }

}
