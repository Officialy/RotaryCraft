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
import reika.rotarycraft.blockentities.engine.BlockEntityMicroturbine;
import reika.rotarycraft.registry.RotaryMenus;

/**
 * 26.1 port: the legacy 1.7 microturbine had its own fuel-bar GUI; until now no menu/container
 * had been written for the 26.1 port, so right-clicking the engine did nothing. This mirrors
 * {@link ContainerEthanol} — no inventory, just a tank-sync packet on broadcastChanges so the
 * client-side fuel bar stays in step with the server.
 */
public class ContainerMicroTurbine extends IOMachineContainer<BlockEntityMicroturbine> {
    private final BlockEntityMicroturbine engine;

    // Client (factory)
    public ContainerMicroTurbine(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityMicroturbine) inv.player.level().getBlockEntity(data.readBlockPos()));
    }

    public ContainerMicroTurbine(int id, Inventory player, BlockEntityMicroturbine engine) {
        super(RotaryMenus.MICRO_TURBINE.get(), id, player, engine);
        this.engine = engine;
        this.addPlayerInventory(player);
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, engine, "fuel");
    }
}
