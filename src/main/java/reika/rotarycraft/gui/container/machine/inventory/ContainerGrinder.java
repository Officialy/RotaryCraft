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
//import net.minecraft.world.entity.player.Player;
//import reika.rotarycraft.base.IOMachineMenu;
//
//
//public class ContainerGrinder extends IOMachineMenu {
//    private final BlockEntityGrinder grinder;
//    private int lastGrinderCookTime;
//
//    public ContainerGrinder(Player player, BlockEntityGrinder te) {
//        super(player, te);
//        lastGrinderCookTime = 0;
//        grinder = te;
//        this.addSlot(new Slot(te, 0, 76, 35));
//        this.addSlot(new SlotFurnace(player, te, 1, 136, 35));
//        this.addSlot(new Slot(te, 2, 35, 60));
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
//            if (lastGrinderCookTime != grinder.grinderCookTime) {
//                icrafting.sendProgressBarUpdate(this, 0, grinder.grinderCookTime);
//            }
//            icrafting.sendProgressBarUpdate(this, 1, grinder.getLevel());
//        }
//
//        lastGrinderCookTime = grinder.grinderCookTime;
//    }
//
//    @Override
//    public void setData(int par1, int par2) {
//        switch (par1) {
//            case 1:
//                grinder.setLevel(par2);
//                break;
//            case 0:
//                grinder.grinderCookTime = par2;
//                break;
//        }
//    }
//}
