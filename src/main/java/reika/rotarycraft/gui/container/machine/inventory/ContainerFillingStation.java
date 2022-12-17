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
//import net.minecraft.entity.player.Player;
//
//import reika.dragonapi.instantiable.gui.ArmorSlot;
//import reika.dragonapi.libraries.io.ReikaPacketHelper;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.IOMachineMenu;
//import reika.rotarycraft.blockentities.auxiliary.BlockEntityFillingStation;
//
//public class ContainerFillingStation extends IOMachineMenu {
//
//    private final BlockEntityFillingStation tile;
//
//    public ContainerFillingStation(Player player, BlockEntityFillingStation te) {
//        super(player, te);
//        tile = te;
//
//        this.addSlotNoClick(0, 106, 71);
//        this.addSlot(1, 54, 21);
//        this.addSlotNoClick(2, 134, 71);
//        this.addSlot(3, 106, 21);
//
//        this.addPlayerInventoryWithOffset(player, 0, 21);
//
//        for (int i = 0; i < 4; i++) {
//            this.addSlot(new ArmorSlot(player, player.inventory.getContainerSize() - 1 - i, 20, 21 + i * 18, i));
//        }
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
