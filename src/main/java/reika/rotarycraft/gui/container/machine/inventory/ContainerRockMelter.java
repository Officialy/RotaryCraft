///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.gui.container.machine.inventory;
//
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.IOMachineMenu;
//import net.minecraft.world.entity.player.Player;
//
//public class ContainerRockMelter extends IOMachineMenu {
//
//    public ContainerRockMelter(Player player, BlockEntityLavaMaker te) {
//        super(player, te);
//
//        for (int i = 0; i < 9; i++) {
//            int dx = 26 + 18 * (i % 3);
//            int dy = 17 + 18 * (i / 3);
//            this.addSlot(i, dx, dy);
//        }
//
//        this.addPlayerInventory(ep);
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
