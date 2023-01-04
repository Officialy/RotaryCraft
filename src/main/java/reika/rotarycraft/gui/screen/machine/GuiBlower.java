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

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.GuiPowerOnlyMachine;
import reika.rotarycraft.blockentities.BlockEntityBlower;
import reika.rotarycraft.gui.container.machine.BlowerContainer;

public class GuiBlower extends GuiPowerOnlyMachine<BlockEntityBlower, BlowerContainer> {

    private final BlockEntityBlower tile;
    private final boolean[] controls;

    public GuiBlower(BlowerContainer container, Inventory inv, Component title) {
        super(container, inv, title);
        tile = (BlockEntityBlower) inv.player.level.getBlockEntity(container.tile.getBlockPos());
        imageWidth = 176;
        imageHeight = 192;
        inventory = inv;

        controls = new boolean[4];
        controls[0] = tile.isWhitelist;
        controls[1] = tile.checkNBT;
        controls[2] = !tile.useOreDict;
    }

    @Override
    public void init() {
        super.init();
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;

        ResourceLocation s = new ResourceLocation(RotaryCraft.MODID, "textures/gui/blowergui.png");

        for (int i = 0; i < 3; i++) {
            int u = 176;
            if (controls[i])
                u += 18;
            addRenderableWidget(new ImageButton(i, j + 25 + 36 * i, k + 64, 18, 18, u, 54 + 18 * i, s, this::actionPerformed));
        }
    }

    protected void actionPerformed(Button b) {
    /*todo    if (b.id < 24000) {
            PacketRegistry id = null;
            switch (b.id) {
                case 0:
                    id = PacketRegistry.BLOWERWHITELIST;
                    break;
                case 1:
                    id = PacketRegistry.BLOWERNBT;
                    break;
                case 2:
                    id = PacketRegistry.BLOWEROREDICT;
                    break;
            }
            if (id != null)
                ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, id.ordinal(), tile);
            controls[b.id] = !controls[b.id];
        }*/

        this.init();
    }

    @Override
    protected void renderBg(PoseStack stack, float pPartialTick, int pX, int pY) {
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;

        int dy = 18;
        int x = 8;
        int y = 21;
        for (int i = 0; i < tile.matchingItems.length; i++) {
            ItemStack is = tile.matchingItems[i];
            if (is != null) {
                api.drawItemStack(stack, itemRenderer, font, is, x + i % 9 * 18, y + i / 9 * dy);
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
