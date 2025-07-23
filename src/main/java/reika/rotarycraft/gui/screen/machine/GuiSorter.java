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
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import reika.rotarycraft.base.GuiPowerOnlyMachine;
import reika.rotarycraft.blockentities.BlockEntitySorting;
import reika.rotarycraft.gui.container.machine.ContainerSorter;

public class GuiSorter extends GuiPowerOnlyMachine<BlockEntitySorting, ContainerSorter> {

    private final BlockEntitySorting sorter;

    public GuiSorter(ContainerSorter container, Inventory inv, Component title) {
        super(container, inv, title);
        sorter = (BlockEntitySorting) inventory.player.level().getBlockEntity(container.tile.getBlockPos());
        imageWidth = 176;
        imageHeight = 180;
        inventory = inv;
    }

    @Override
    public void renderBackground(GuiGraphics stack) {
        int dy = 22;
        int x = 8;
        int y = 18;
        int l = BlockEntitySorting.LENGTH;
        for (int k = 0; k < l * 3; k++) {
            ItemStack is = sorter.getMapping(k);
            if (is != null) {
                api.drawItemStack(stack, font, is, x + k % l * 18, y + k / l * dy);
            }
        }
    }

    @Override
    protected String getGuiTexture() {
        return "sortergui";
    }

}