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
import reika.rotarycraft.blockentities.processing.BlockEntityExtractor;
import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.RotaryMenus;

public class ContainerExtractor extends IOMachineContainer<BlockEntityExtractor> {

    private final BlockEntityExtractor extractor;

    //Client
    public ContainerExtractor(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityExtractor) inv.player.level().getBlockEntity(data.readBlockPos()));
    }

    public ContainerExtractor(int id, Inventory inv, BlockEntityExtractor te) {
        super(RotaryMenus.EXTRACTOR.get(), id, inv, te);
        extractor = te;

        // 4 processing stages: input slots (0-3) top row, output slots (4-7) bottom row,
        // bonus slot (8) at the far right, optional drill slot (9) under the first stage.
        // Coordinates from the original 1.7 ContainerExtractor / extractorgui.png.
        this.addSlot(te.itemHandler.slot(0, 26, 13));
        this.addSlot(new ResultSlotItemHandler(te.itemHandler, 4, 26, 55));
        this.addSlot(te.itemHandler.slot(1, 62, 13));
        this.addSlot(new ResultSlotItemHandler(te.itemHandler, 5, 62, 55));
        this.addSlot(te.itemHandler.slot(2, 98, 13));
        this.addSlot(new ResultSlotItemHandler(te.itemHandler, 6, 98, 55));
        this.addSlot(te.itemHandler.slot(3, 134, 13));
        this.addSlot(new ResultSlotItemHandler(te.itemHandler, 7, 134, 55));
        this.addSlot(new ResultSlotItemHandler(te.itemHandler, 8, 152, 55));

        if (ConfigRegistry.EXTRACTORMAINTAIN.getState())
            this.addSlot(te.itemHandler.slot(9, 26, 34));

        // Original used the standard player-inventory placement; the earlier +12 offset pushed
        // it 12px below where extractorgui.png draws the inventory grid.
        this.addPlayerInventory(inv);
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, extractor, "tank");
    }

    @Override
    public void setData(int par1, int par2) {
        // stages 0-3 cook times are pushed by index; client renders the four arrows
        if (par1 >= 0 && par1 < 4)
            extractor.setCookTime(par1, par2);
    }
}
