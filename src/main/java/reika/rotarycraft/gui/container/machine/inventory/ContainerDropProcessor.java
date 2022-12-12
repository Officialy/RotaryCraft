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
//import reika.rotarycraft.base.IOMachineMenu;
//
//public class ContainerDropProcessor extends IOMachineMenu {
//    private final BlockEntityDropProcessor drops;
//    private int lastdropProcessTime;
//
//    public ContainerDropProcessor(Inventory player, BlockEntityDropProcessor te) {
//        super(player, te);
//        lastdropProcessTime = 0;
//        drops = te;
//        this.addSlot(new Slot(te, 0, 52, 35));
//        this.addSlot(new SlotFurnace(player, te, 1, 112, 35));
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
//            if (lastdropProcessTime != drops.dropProcessTime) {
//                icrafting.sendProgressBarUpdate(this, 0, drops.dropProcessTime);
//            }
//        }
//
//        lastdropProcessTime = drops.dropProcessTime;
//    }
//
//    @Override
//    public void setData(int par1, int par2) {
//        switch (par1) {
//            case 0:
//                drops.dropProcessTime = par2;
//                break;
//        }
//    }
//}
