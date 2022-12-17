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
//import net.minecraft.inventory.ICrafting;
//import net.minecraft.inventory.SlotFurnace;
//
//import reika.rotarycraft.base.IOMachineMenu;
//import reika.rotarycraft.blockentities.production.BlockEntityRefrigerator;
//
//public class ContainerFridge extends IOMachineMenu {
//    private final BlockEntityRefrigerator fridge;
//    private int time;
//
//    public ContainerFridge(Player player, BlockEntityRefrigerator par2BlockEntityRefrigerator) {
//        super(player, par2BlockEntityRefrigerator);
//        time = 0;
//        fridge = par2BlockEntityRefrigerator;
//
//        this.addSlot(0, 99, 58);
//        this.addSlot(new SlotFurnace(player, fridge, 1, 132, 72));
//
//        this.addPlayerInventoryWithOffset(player, 0, 22);
//    }
//
//    @Override
//    public void broadcastChanges() {
//        super.broadcastChanges();
//
//        for (int i = 0; i < crafters.size(); i++) {
//            ICrafting icrafting = (ICrafting) crafters.get(i);
//
//            icrafting.sendProgressBarUpdate(this, 0, fridge.time);
//            icrafting.sendProgressBarUpdate(this, 1, fridge.getLevel());
//        }
//
//        time = fridge.time;
//    }
//
//    @Override
//    public void setData(int par1, int par2) {
//        if (par1 == 0) {
//            fridge.time = par2;
//        }
//        if (par1 == 1) {
//            fridge.setLevel(par2);
//        }
//    }
//}
