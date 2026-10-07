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
import net.minecraft.world.inventory.Slot;
import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.IOMachineContainer;
import reika.rotarycraft.blockentities.processing.BlockEntityLavaSmeltery;
import reika.rotarycraft.registry.RotaryMenus;

public class ContainerBigFurnace extends IOMachineContainer<BlockEntityLavaSmeltery> {
    private final BlockEntityLavaSmeltery te;

    //Client
    public ContainerBigFurnace(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityLavaSmeltery) inv.player.level().getBlockEntity(data.readBlockPos()));
    }

    public ContainerBigFurnace(int id, Inventory player, BlockEntityLavaSmeltery par2BlockEntityLavaSmeltery) {
        super(RotaryMenus.BIG_FURNACE.get(), id, player, par2BlockEntityLavaSmeltery);
        te = par2BlockEntityLavaSmeltery;

        int dx = 18;
        int dy = 21;
        // 1.21.5: wire slots directly against the BE's ManagedItemHandler (InventoriedPowerLiquidReceiver
        // exposes `itemHandler` publicly). Replaces the old ForgeCapabilities.ITEM_HANDLER lookup.
        for (int i = 0; i < te.getNumberInputSlots(); i++) {
            int row = i % 9;
            int col = i / 9;
            this.addSlot(te.itemHandler.slot(i, 8 + row * dx, 18 + col * dy));
        }

        for (int i = 0; i < te.getNumberInputSlots(); i++) {
            int row = i % 9;
            int col = i / 9;
            this.addSlot(new reika.dragonapi.instantiable.gui.slot.ResultSlotItemHandler(
                    te.itemHandler, i + te.getNumberInputSlots(), 8 + row * dx, 72 + col * dy));
        }


        this.addPlayerInventoryWithOffset(player, 0, 41);

        // 1.7.10 sent smeltTick as progress bar 0; the cook bar reads it on the client.
        this.addDataSlot(new net.minecraft.world.inventory.DataSlot() {
            @Override public int get() { return te.smeltTick; }
            @Override public void set(int value) { te.smeltTick = value; }
        });
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

    /*    for (int i = 0; i < crafters.size(); i++) {
            ICrafting icrafting = (ICrafting) crafters.get(i);

            icrafting.sendProgressBarUpdate(this, 0, te.smeltTick);
            //icrafting.sendProgressBarUpdate(this, 1, te.getLevel());
        }*/

        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, te, "tank");
    }

}
