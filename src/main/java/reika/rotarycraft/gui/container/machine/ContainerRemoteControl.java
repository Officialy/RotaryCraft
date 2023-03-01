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
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.items.SlotItemHandler;
import reika.dragonapi.base.CoreContainer;
import reika.rotarycraft.base.blockentity.RemoteControlMachine;

public class ContainerRemoteControl extends CoreContainer<RemoteControlMachine> {

    public ContainerRemoteControl(MenuType<?> type, int id, Inventory playerInv, FriendlyByteBuf byteBuf) {
        super(type, id, playerInv, (RemoteControlMachine) playerInv.player.level.getBlockEntity(byteBuf.readBlockPos()));

        this.addSlot(new SlotItemHandler(ii, 0, 80, 17));

        this.addSlot(new SlotItemHandler(ii, 1, 62, 53));
        this.addSlot(new SlotItemHandler(ii, 2, 80, 53));
        this.addSlot(new SlotItemHandler(ii, 3, 98, 53));

        this.addPlayerInventory(playerInv);
    }

}
