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
import net.neoforged.fml.loading.FMLLoader;
import reika.dragonapi.base.CoreContainer;
import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.blockentities.storage.BlockEntityReservoir;
import reika.rotarycraft.registry.RotaryMenus;

public class ReservoirContainer extends CoreContainer<BlockEntityReservoir> {

    private final BlockEntityReservoir reservoir;

    //Client
    public ReservoirContainer(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityReservoir) inv.player.level().getBlockEntity(data.readBlockPos()));
    }

    public ReservoirContainer(int id, Inventory inv, BlockEntityReservoir te) {
        super(RotaryMenus.RESERVOIR.get(), id, inv, te);
        this.reservoir = te;
    }

    /**
     * Updates crafting matrix; called from onCraftMatrixChanged. Args: none
     */
    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        if (FMLLoader.getDist().isDedicatedServer()) {
            ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, reservoir, "tank");
        }

    }

}
