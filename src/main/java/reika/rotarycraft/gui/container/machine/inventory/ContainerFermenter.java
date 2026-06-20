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
import reika.rotarycraft.blockentities.production.BlockEntityFermenter;
import reika.rotarycraft.registry.RotaryMenus;

public class ContainerFermenter extends IOMachineContainer<BlockEntityFermenter> {

    private final BlockEntityFermenter fermenter;

    //Client
    public ContainerFermenter(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityFermenter) inv.player.level().getBlockEntity(data.readBlockPos()));
    }

    public ContainerFermenter(int id, Inventory inv, BlockEntityFermenter te) {
        super(RotaryMenus.FERMENTER.get(), id, inv, te);
        fermenter = te;

        // catalyst over feedstock on the left, product on the right — coordinates from the
        // original 1.7 ContainerFermenter / fermentergui.png.
        this.addSlot(te.itemHandler.slot(0, 55, 17));
        this.addSlot(te.itemHandler.slot(1, 55, 53));
        this.addSlot(new ResultSlotItemHandler(te.itemHandler, 2, 116, 35));

        this.addPlayerInventory(inv);
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, fermenter, "tank");
    }

    @Override
    public void setData(int par1, int par2) {
        switch (par1) {
            case 0 -> fermenter.fermenterCookTime = par2;
            case 1 -> fermenter.temperature = par2;
        }
    }
}
