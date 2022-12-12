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
//public class ContainerCrystallizer extends IOMachineMenu {
//    private final BlockEntityCrystallizer te;
//
//    public ContainerCrystallizer(Inventory player, BlockEntityCrystallizer par2BlockEntityCrystallizer) {
//        super(player, par2BlockEntityCrystallizer);
//        te = par2BlockEntityCrystallizer;
//        int getY = te.xCoord;
//        int posY = te.yCoord;
//        int posZ = te.zCoord;
//
//        this.addSlot(0, 80, 35);
//        this.addSlot(1, 125, 35);
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
//            icrafting.sendProgressBarUpdate(this, 0, te.freezeTick);
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
//                te.freezeTick = par2;
//                break;
//        }
//    }
//}
