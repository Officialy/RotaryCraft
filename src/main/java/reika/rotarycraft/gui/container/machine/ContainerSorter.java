/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.gui.container.machine;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import reika.dragonapi.instantiable.gui.Slot.GhostSlot;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.base.IOMachineMenu;
import reika.rotarycraft.blockentities.BlockEntitySorting;
import reika.rotarycraft.registry.RotaryMenus;

public class ContainerSorter extends IOMachineMenu<BlockEntitySorting> {

    private final BlockEntitySorting sorter;
    //Client
    public ContainerSorter(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntitySorting) inv.player.level.getBlockEntity(data.readBlockPos()));
    }

    public ContainerSorter(int id, Inventory inventory, BlockEntitySorting te) {
        super(RotaryMenus.SORTER.get(),id, inventory, te);
        sorter = te;

        int l = BlockEntitySorting.LENGTH;
        int dy = 22;
        int x = 8;
        int y = 18;
        for (int k = 0; k < 3; k++) {
            for (int i = 0; i < l; i++) {
                this.addSlot(new GhostSlot(i + k * l, x + i * 18, y + dy * k));
            }
        }

        this.addPlayerInventoryWithOffset(inventory, 0, 14);
    }

    @Override
    public void clicked(int slot, int button, ClickType type, Player ep) {
        boolean inGUI = slot < BlockEntitySorting.LENGTH * 3 && slot >= 0;
        if (inGUI) {
            ItemStack held = ep.getMainHandItem();
            sorter.setMapping(slot, ReikaItemHelper.getSizedItemStack(held, 1));
//         todo   return held;
        }
    }

}
