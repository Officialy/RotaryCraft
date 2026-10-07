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

    // 1.7.10 order and texture rows (54 + 18*i). "Metadata" compares the damage value, where this port keeps item variants.
    private static final PacketRegistry[] PACKETS = {PacketRegistry.BLOWERWHITELIST, PacketRegistry.BLOWERMETA, PacketRegistry.BLOWERNBT, PacketRegistry.BLOWEROREDICT};
    private static final int[] TEX_V = {54, 72, 90, 108};

    public GuiBlower(BlowerContainer container, Inventory inv, Component title) {
        super(container, inv, title, 176, 192);
        tile = (BlockEntityBlower) inv.player.level().getBlockEntity(container.tile.getBlockPos());
        inventory = inv;

        controls = new boolean[4];
        controls[0] = tile.isWhitelist;
        controls[1] = tile.checkMeta;
        controls[2] = tile.checkNBT;
        controls[3] = !tile.useOreDict;
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

    // 1.7.10 drew these in drawGuiContainerForegroundLayer, whose origin is the panel corner, so the
    // icon positions and the "- j / - k" tooltip offsets are panel-relative; the background pass is not.
    @Override
    protected void extractLabels(GuiGraphicsExtractor stack, int pX, int pY) {
        super.extractLabels(stack, pX, pY);
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
            api.drawTooltipAt(stack, font, controls[1] ? "Use Metadata" : "Ignore Metadata", pX - j + 80, pY - k);
        }
        if (api.isMouseInBox(j + 25 + 36 * 2, j + 43 + 36 * 2, k + 64, k + 82, pX, pY)) {
            api.drawTooltipAt(stack, font, controls[2] ? "Use NBT" : "Ignore NBT", pX - j, pY - k);
        }
        if (api.isMouseInBox(j + 25 + 36 * 3, j + 43 + 36 * 3, k + 64, k + 82, pX, pY)) {
            api.drawTooltipAt(stack, font, controls[3] ? "Match Exact" : "Use Ore Dictionary", pX - j, pY - k);
        }
    }

    @Override
    protected String getGuiTexture() {
        return "blowergui";
    }

}
