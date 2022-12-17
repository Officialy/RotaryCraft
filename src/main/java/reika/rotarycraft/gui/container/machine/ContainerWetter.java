/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************//*

package reika.rotarycraft.gui.container.machine;

import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.rotarycraft.RotaryCraft;
import net.minecraft.world.entity.player.Player;
import reika.rotarycraft.base.OneSlotContainer;

public class ContainerWetter extends OneSlotContainer<BlockEntityWetter> {

    public ContainerWetter(Player player, BlockEntityWetter te) {
        super(player, te);
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, tile, "tank");
    }

}
*/
