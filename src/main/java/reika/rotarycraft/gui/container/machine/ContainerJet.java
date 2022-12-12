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
//import net.minecraft.world.entity.player.Inventory;
//import reika.rotarycraft.base.IOMachineMenu;
//import reika.rotarycraft.base.blockentity.BlockEntityEngine;
//
//public class ContainerJet extends IOMachineMenu {
//
//    private final BlockEntityEngine engine;
//
//    public ContainerJet(int id, Inventory player, BlockEntityEngine te) {
//        super(null, id, player, te); //todo jet engine
//        engine = te;
//        this.addPlayerInventory(player);
//    }
//
//    @Override
//    public void broadcastChanges() {
//        super.broadcastChanges();
//
////        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, engine, "fuel");
//    }
//
//}
