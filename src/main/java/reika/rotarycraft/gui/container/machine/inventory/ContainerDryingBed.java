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
//import reika.dragonapi.base.CoreMenu;
//import reika.rotarycraft.RotaryCraft;
//
//public class ContainerDryingBed extends CoreMenu {
//    private final BlockEntityDryingBed te;
//
//    public ContainerDryingBed(Inventory playerInv, BlockEntityDryingBed par2BlockEntityDryingBed) {
//        super(playerInv, par2BlockEntityDryingBed);
//        te = par2BlockEntityDryingBed;
//        int getY = te.xCoord;
//        int posY = te.yCoord;
//        int posZ = te.zCoord;
//
//        this.addSlot(0, 125, 35);
//
//        this.addPlayerInventory(playerInv);
//    }
//
//    @Override
//    public void broadcastChanges() {
//        super.broadcastChanges();
//
//        for (int i = 0; i < crafters.size(); i++) {
//            ICrafting icrafting = (ICrafting) crafters.get(i);
//
//            icrafting.sendProgressBarUpdate(this, 0, te.progress);
//            //icrafting.sendProgressBarUpdate(this, 1, te.getLevel());
//        }
//
//        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, te, "tank");
//    }
//
//    @Override
//    public void setData(int par1, int par2) {
//        switch (par1) {
//            //case 1: te.setLevel(par2); break;
//            case 0:
//                te.progress = par2;
//                break;
//        }
//    }
//}
