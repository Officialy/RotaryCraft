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
//import net.minecraft.world.entity.player.Inventory;
//
//import net.minecraft.world.inventory.Slot;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.IOMachineMenu;
//
//public class ContainerCompactor extends IOMachineMenu {
//    private final BlockEntityCompactor compactor;
//    private final int lastCompactorBurnTime;
//    private final int lastCompactorItemBurnTime;
//    private int lastCompactorCookTime;
//
//    public ContainerCompactor(Inventory player, BlockEntityCompactor par2BlockEntityCompactor) {
//        super(player, par2BlockEntityCompactor);
//        lastCompactorCookTime = 0;
//        lastCompactorBurnTime = 0;
//        lastCompactorItemBurnTime = 0;
//        compactor = par2BlockEntityCompactor;
//        this.addSlot(new Slot(par2BlockEntityCompactor, 0, 26, 8));
//        this.addSlot(new Slot(par2BlockEntityCompactor, 1, 26, 26));
//        this.addSlot(new Slot(par2BlockEntityCompactor, 2, 26, 44));
//        this.addSlot(new Slot(par2BlockEntityCompactor, 3, 26, 62));
//        this.addSlot(new SlotMachineOut(player, par2BlockEntityCompactor, 4, 80, 35));
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
//            if (lastCompactorCookTime != compactor.compactorCookTime) {
//                icrafting.sendProgressBarUpdate(this, 0, compactor.compactorCookTime);
//            }
//            icrafting.sendProgressBarUpdate(this, 1, compactor.temperature);
//            //icrafting.sendProgressBarUpdate(this, 2, compactor.pressure);
//        }
//
//        lastCompactorCookTime = compactor.compactorCookTime;
//
//        ReikaPacketHelper.sendSyncPacket(RotaryCraft.packetChannel, compactor, "pressure");
//    }
//
//    @Override
//    public void setData(int par1, int par2) {
//        switch (par1) {
//            case 0:
//                compactor.compactorCookTime = par2;
//                break;
//            case 1:
//                compactor.temperature = par2;
//                break;
//            //case 2: compactor.pressure = par2; break;
//        }
//    }
//}
