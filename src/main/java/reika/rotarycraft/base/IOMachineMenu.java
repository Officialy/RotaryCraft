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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import reika.dragonapi.base.CoreMenu;
import reika.rotarycraft.PacketHandlerCore;
import reika.rotarycraft.base.blockentity.BlockEntityIOMachine;

public abstract class IOMachineMenu<T extends BlockEntityIOMachine> extends CoreMenu<T> {

    private final T ioTile;

    public IOMachineMenu(MenuType<?> type, final int id, Inventory inv, FriendlyByteBuf buf) {
        this(type, id, inv, (T) inv.player.level.getBlockEntity(buf.readBlockPos()));
    }

    public IOMachineMenu(MenuType<?> type, final int id, Inventory inv, T tile) {
        super(type, id, inv, tile, inv);
        ioTile = tile;
    }

    @Override
    public void broadcastChanges() {
//        super.broadcastChanges();

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
