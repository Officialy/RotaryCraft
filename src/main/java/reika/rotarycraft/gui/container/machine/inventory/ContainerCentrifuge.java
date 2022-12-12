///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.containers.machine.inventory;
//
//import net.minecraft.world.entity.player.Inventory;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.IOMachineMenu;
//
//public class ContainerCentrifuge extends IOMachineMenu {
//    private final BlockEntityCentrifuge te;
//
//    public ContainerCentrifuge(Inventory player, BlockEntityCentrifuge Centrifuge) {
//        super(player, Centrifuge);
//        te = Centrifuge;
//        int getY = te.xCoord;
//        int posY = te.yCoord;
//        int posZ = te.zCoord;
//
//        this.addSlot(0, 26, 38);
//
//        int dx = 85;
//        int dy = 20;
//        for (int i = 0; i < 3; i++) {
//            for (int k = 0; k < 3; k++) {
//                int id = 1 + i * 3 + k;
//                int x = dx + k * 18;
//                int y = dy + i * 18;
//                this.addSlot(id, x, y);
//            }
//        }
//
//        this.addPlayerInventory(player);
//    }
//
//    @Override
//    public void broadcastChanges() {
//        super.broadcastChanges();
//
//        for (int i = 0; i < crafters.size(); i++) {
//            ICrafting icrafting = (ICrafting) crafters.get(i);
//
//            icrafting.sendProgressBarUpdate(this, 0, te.getProgress());
//            //icrafting.sendProgressBarUpdate(this, 2, compactor.pressure);
//        }
//
//        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, te, "tank");
//    }
//
//    @Override
//    public void setData(int par1, int par2) {
//        switch (par1) {
//            case 0:
//                te.syncProgress(par2);
//                //case 2: compactor.pressure = par2; break;
//        }
//    }
//}
