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

import reika.dragonapi.instantiable.gui.slot.ResultSlotItemHandler;
import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.IOMachineContainer;
import reika.rotarycraft.blockentities.processing.BlockEntityPulseFurnace;
import reika.rotarycraft.registry.RotaryMenus;

public class ContainerPulseFurnace extends IOMachineContainer<BlockEntityPulseFurnace> {

    private final BlockEntityPulseFurnace pulseFurnace;

    //Client
    public ContainerPulseFurnace(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityPulseFurnace) inv.player.level().getBlockEntity(data.readBlockPos()));
    }

    public ContainerPulseFurnace(int id, Inventory inv, BlockEntityPulseFurnace te) {
        super(RotaryMenus.PULSE_FURNACE.get(), id, inv, te);
        pulseFurnace = te;

        // Slot 0 = smelt input (top), slot 2 = result (bottom); coords from pulsejetgui.png.
        this.addSlot(te.itemHandler.slot(0, 125, 16));
        this.addSlot(new ResultSlotItemHandler(te.itemHandler, 2, 125, 52));
        this.addPlayerInventory(inv);
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, pulseFurnace, "water");
        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, pulseFurnace, "fuel");
        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, pulseFurnace, "accel");
    }

    @Override
    public void setData(int par1, int par2) {
        switch (par1) {
            case 0 -> pulseFurnace.pulseFurnaceCookTime = par2;
            case 1 -> pulseFurnace.temperature = par2;
            case 2 -> pulseFurnace.smelttick = par2;
            case 4 -> pulseFurnace.omega = par2;
        }
    }
}
