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
//import net.minecraft.world.inventory.Slot;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.IOMachineMenu;
//
//public class ContainerFractionator extends IOMachineMenu {
//    private final BlockEntityFractionator Fraction;
//
//    public ContainerFractionator(Inventory player, BlockEntityFractionator par2BlockEntityFractionator) {
//        super(player, par2BlockEntityFractionator);
//        Fraction = par2BlockEntityFractionator;
//        int getY = Fraction.xCoord;
//        int posY = Fraction.yCoord;
//        int posZ = Fraction.zCoord;
//
//        this.addSlot(new Slot(par2BlockEntityFractionator, 0, 17, 16));
//        this.addSlot(new Slot(par2BlockEntityFractionator, 1, 17, 35));
//        this.addSlot(new Slot(par2BlockEntityFractionator, 2, 17, 54));
//        this.addSlot(new Slot(par2BlockEntityFractionator, 3, 53, 16));
//        this.addSlot(new Slot(par2BlockEntityFractionator, 4, 53, 35));
//        this.addSlot(new Slot(par2BlockEntityFractionator, 5, 53, 54));
//
//        this.addSlot(new Slot(par2BlockEntityFractionator, 6, 103, 54));
//
//        this.addPlayerInventory(player);
//    }
//
//    /**
//     * Updates crafting matrix; called from onCraftMatrixChanged. Args: none
//     */
//    @Override
//    public void broadcastChanges() {
//        super.broadcastChanges();
//
//        for (int i = 0; i < crafters.size(); i++) {
//            ICrafting icrafting = (ICrafting) crafters.get(i);
//
//            //icrafting.sendProgressBarUpdate(this, 1, Fraction.getFuelLevel());
//            icrafting.sendProgressBarUpdate(this, 2, Fraction.mixTime);
//            icrafting.sendProgressBarUpdate(this, 3, Fraction.getPressure());
//        }
//
//        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, Fraction, "input");
//        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, Fraction, "output");
//    }
//
//    @Override
//    public void setData(int par1, int par2) {
//        switch (par1) {
//            //case 1: Fraction.setFuelLevel(par2); break;
//            case 2:
//                Fraction.mixTime = par2;
//                break;
//            case 3:
//                Fraction.addPressure(-Fraction.getPressure());
//                Fraction.addPressure(par2);
//                break;
//        }
//    }
//}
