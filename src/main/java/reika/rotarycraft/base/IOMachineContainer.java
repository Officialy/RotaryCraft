/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.base;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import reika.dragonapi.base.CoreContainer;
import reika.rotarycraft.base.blockentity.BlockEntityIOMachine;

public abstract class IOMachineContainer<T extends BlockEntityIOMachine> extends CoreContainer<T> {

    private final T ioTile;

    public IOMachineContainer(MenuType<?> type, final int id, Inventory inv, FriendlyByteBuf buf) {
        this(type, id, inv, (T) inv.player.level.getBlockEntity(buf.readBlockPos()));
    }

    public IOMachineContainer(MenuType<?> type, final int id, Inventory inv, T tile) {
        super(type, id, inv, tile);
        ioTile = tile;
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

        /*todo for (Object ic : this.containerListeners) {
            if (ic instanceof ServerPlayer)
                PacketHandlerCore.sendPowerSyncPacket(ioTile, (ServerPlayer) ic);
        }*/
    }

    @Override
    public void setData(int id, int val) {
        super.setData(id, val);
    }

}
