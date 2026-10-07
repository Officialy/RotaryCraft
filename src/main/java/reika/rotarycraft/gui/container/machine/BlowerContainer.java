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
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.ItemStack;
import reika.dragonapi.instantiable.gui.slot.GhostSlot;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.base.IOMachineContainer;
import reika.rotarycraft.blockentities.BlockEntityBlower;
import reika.rotarycraft.registry.RotaryMenus;

public class BlowerContainer extends IOMachineContainer<BlockEntityBlower> {

    private final BlockEntityBlower blower;

    public BlowerContainer(int id, Inventory playerInv, FriendlyByteBuf byteBuf) {
        super(RotaryMenus.BLOWER.get(), id, playerInv, byteBuf);
        blower = this.tile;

        int dy = 18;
        int x = 8;
        int y = 21;
        for (int k = 0; k < 2; k++) {
            for (int i = 0; i < 9; i++) {
                this.addSlot(new GhostSlot(i + k * 9, x + i * 18, y + dy * k));
            }
        }

        this.addPlayerInventoryWithOffset(playerInv, 0, 26);
    }

    @Override
    public void clicked(int id, int button, ContainerInput type, Player ep) {
        boolean inGUI = id < blower.matchingItems.length && id >= 0;
        if (inGUI) {
            // 1.7.10: the stack on the cursor (inventory.getItemStack), an empty cursor clearing it.
            ItemStack held = this.getCarried();
            blower.matchingItems[id] = held.isEmpty() ? null : ReikaItemHelper.getSizedItemStack(held, 1);
        } else {
            super.clicked(id, button, type, ep);
        }
    }

}

