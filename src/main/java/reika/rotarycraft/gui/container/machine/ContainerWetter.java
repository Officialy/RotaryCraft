///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.gui.container.machine;
//
//import reika.dragonapi.base.OneSlotContainer;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.blockentities.processing.BlockEntityWetter;
//import net.minecraft.world.entity.player.Player;
//
//public class ContainerWetter extends OneSlotContainer {
//
//    public ContainerWetter(Player player, BlockEntityWetter te) {
//        super(player, te);
//    }
//
//    @Override
//    public void broadcastChanges() {
//        super.broadcastChanges();
//
//        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, tile, "tank");
//    }
//
//}
