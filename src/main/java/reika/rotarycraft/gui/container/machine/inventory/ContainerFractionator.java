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
import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.IOMachineContainer;
import reika.rotarycraft.blockentities.production.BlockEntityFractionator;
import reika.rotarycraft.registry.RotaryMenus;

/**
 * 26.1 menu for the Fractionator.
 * <p>Slot layout (legacy 1.7 mirrored):
 * <pre>
 *   0..2 — left column ingredients  (x=17, y=16/35/54)
 *   3..5 — second column ingredients (x=53, y=16/35/54)
 *   6    — ghast-tear catalyst       (x=103, y=54)
 * </pre>
 * Liquid tanks (ethanol input / jet-fuel output) are not slots — they're synced via the
 * {@code tank} sync-packet so the screen's fuel bars stay live.</p>
 */
public class ContainerFractionator extends IOMachineContainer<BlockEntityFractionator> {

    private final BlockEntityFractionator fct;

    // Client (factory)
    public ContainerFractionator(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityFractionator) inv.player.level().getBlockEntity(data.readBlockPos()));
    }

    public ContainerFractionator(int id, Inventory inv, BlockEntityFractionator te) {
        super(RotaryMenus.FRACTIONATOR.get(), id, inv, te);
        this.fct = te;

        // Two columns of 3 ingredient slots, then the ghast-tear catalyst slot.
        this.addSlot(te.itemHandler.slot(0, 17, 16));
        this.addSlot(te.itemHandler.slot(1, 17, 35));
        this.addSlot(te.itemHandler.slot(2, 17, 54));
        this.addSlot(te.itemHandler.slot(3, 53, 16));
        this.addSlot(te.itemHandler.slot(4, 53, 35));
        this.addSlot(te.itemHandler.slot(5, 53, 54));
        this.addSlot(te.itemHandler.slot(6, 103, 54));

        this.addPlayerInventory(inv);
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        // Sync both tanks so the GUI's input/output fuel bars stay live with the server.
        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, fct, "input");
        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, fct, "output");
    }

    @Override
    public void setData(int par1, int par2) {
        if (par1 == 0) {
            fct.mixTime = par2;
        }
    }
}
