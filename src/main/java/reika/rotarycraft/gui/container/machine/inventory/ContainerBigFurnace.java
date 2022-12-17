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
import reika.rotarycraft.base.IOMachineMenu;
import reika.rotarycraft.blockentities.BlockEntitySorting;
import reika.rotarycraft.blockentities.processing.BlockEntityBigFurnace;
import reika.rotarycraft.registry.RotaryMenus;

public class ContainerBigFurnace extends IOMachineMenu<BlockEntityBigFurnace> {
    private final BlockEntityBigFurnace te;

    //Client
    public ContainerBigFurnace(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityBigFurnace) inv.player.level.getBlockEntity(data.readBlockPos()));
    }

    public ContainerBigFurnace(int id, Inventory player, BlockEntityBigFurnace par2BlockEntityBigFurnace) {
        super(RotaryMenus.BIG_FURNACE.get(), id, player, par2BlockEntityBigFurnace);
        te = par2BlockEntityBigFurnace;

        int dx = 18;
        int dy = 21;
        for (int i = 0; i < te.getNumberInputSlots(); i++) {
            int row = i % 9;
            int col = i / 9;
            Slot slot = new Slot(te, i, 8 + row * dx, 18 + col * dy);
            this.addSlot(slot);
        }

        for (int i = 0; i < te.getNumberInputSlots(); i++) {
            int row = i % 9;
            int col = i / 9;
            Slot slot = new Slot(player,i + te.getNumberInputSlots(), 8 + row * dx, 72 + col * dy);
            this.addSlot(slot);
        }


        this.addPlayerInventoryWithOffset(player, 0, 41);
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

    @Override
    public void setData(int par1, int par2) {
        switch (par1) {
            //case 1: te.setLevel(par2); break;
            case 0:
                te.smeltTick = par2;
                break;
        }
    }
}
