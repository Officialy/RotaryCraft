/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.gui.container.machine;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.IOMachineContainer;
import reika.rotarycraft.base.blockentity.BlockEntityEngine;
import reika.rotarycraft.registry.RotaryMenus;

public class ContainerJet extends IOMachineContainer<BlockEntityEngine> {

    private final BlockEntityEngine engine;

    public ContainerJet(int id, Inventory inv, FriendlyByteBuf buf) {
        super(RotaryMenus.JET.get(), id, inv, buf);
        engine = (BlockEntityEngine) inv.player.level.getBlockEntity(buf.readBlockPos());
        this.addPlayerInventory(inv);
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, engine, "fuel");
    }

}
