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

    //Client
    public ContainerFillingStation(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityFillingStation) inv.player.level().getBlockEntity(data.readBlockPos()));
    }

    public ContainerFillingStation(int id, Inventory inv, BlockEntityFillingStation te) {
        super(RotaryMenus.FILLING_STATION.get(), id, inv, te);

        // 0 = item being filled (display), 1 = fuel bucket, 2 = filled output, 3 = fillable input.
        this.addSlotNoClick(0, 106, 71);
        this.addSlot(1, 54, 21);
        this.addSlotNoClick(2, 134, 71);
        this.addSlot(3, 106, 21);

        this.addPlayerInventoryWithOffset(inv, 0, 21);

        // The four worn-armour slots, so a worn jetpack can be topped up directly. ArmorSlot type
        // i is 0=HEAD,1=CHEST,2=LEGS,3=FEET. In 1.21.5 the worn-armour inventory indices are
        // FEET=36, LEGS=37, CHEST=38, HEAD=39, then OFFHAND=40, BODY=41, SADDLE=42 — so
        // getContainerSize() (43) is no longer the right base. HEAD sits directly below the offhand
        // slot, so the correct index is SLOT_OFFHAND-1-i (= 39-i). The old getContainerSize()-1-i
        // landed on SADDLE/BODY/OFFHAND/HEAD instead of the four armour slots.
        for (int i = 0; i < 4; i++) {
            this.addSlot(new ArmorSlot(inv, Inventory.SLOT_OFFHAND - 1 - i, 20, 21 + i * 18, i));
        }
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, tile, "tank");
    }
}
