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
import net.minecraft.world.entity.player.Player;
import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.IOMachineContainer;
import reika.rotarycraft.base.blockentity.BlockEntityEngine;
import reika.rotarycraft.blockentities.engine.BlockEntitySteamEngine;
import reika.rotarycraft.registry.RotaryMenus;

public class SteamContainer extends IOMachineContainer<BlockEntitySteamEngine> {

    public final BlockEntitySteamEngine steam;

    //Client
    public SteamContainer(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntitySteamEngine) inv.player.level.getBlockEntity(data.readBlockPos()));
    }

    public SteamContainer(int id, Inventory inv, BlockEntitySteamEngine te) {
        super(RotaryMenus.STEAM_ENGINE.get(), id, inv, te);
        steam = te;
    }

    /**
     * Updates crafting matrix; called from onCraftMatrixChanged. Args: none
     */
    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

       /* for (int i = 0; i < crafters.size(); i++) {
            CraftingRecipe icrafting = (CraftingRecipe) crafters.get(i);

            icrafting.sendProgressBarUpdate(this, 1, steam.temperature);
        }*/
        if (steam == null) {
            RotaryCraft.LOGGER.error("STEAM ENGINE IS NULL....how?");
        }

    }

    @Override
    public void sendAllDataToRemote() {
        super.sendAllDataToRemote();
        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, steam, "water");
    }

    @Override
    public void setData(int pId, int pData) {
        switch (pId) {
            case 1 -> BlockEntityEngine.temperature = pData;
        }
    }

}
