/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.gui.container.machine.inventory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import reika.dragonapi.instantiable.gui.ArmorSlot;
import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.IOMachineContainer;
import reika.rotarycraft.blockentities.auxiliary.BlockEntityFillingStation;
import reika.rotarycraft.registry.RotaryMenus;

public class ContainerFillingStation extends IOMachineContainer<BlockEntityFillingStation> {

    public final BlockEntityFillingStation tile;

    //Client
    public ContainerFillingStation(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityFillingStation) inv.player.level.getBlockEntity(data.readBlockPos()));
    }
    public ContainerFillingStation(int id, Inventory inv, BlockEntityFillingStation te) {
        super(RotaryMenus.FILLING_STATION.get(), id, inv, te);
        tile = te;

        this.addSlotNoClick(0, 106, 71);
        this.addSlot(1, 54, 21);
        this.addSlotNoClick(2, 134, 71);
        this.addSlot(3, 106, 21);

        this.addPlayerInventoryWithOffset(inv, 0, 21);

        for (int i = 0; i < 4; i++) {
            this.addSlot(new ArmorSlot(inv, inv.getContainerSize() - 1 - i, 20, 21 + i * 18, i));
        }
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, tile, "tank");
    }

}
