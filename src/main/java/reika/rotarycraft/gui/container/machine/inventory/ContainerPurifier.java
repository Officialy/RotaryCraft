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
//import reika.rotarycraft.base.IOMachineMenu;
//import reika.rotarycraft.blockentities.processing.BlockEntityPurifier;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.inventory.Slot;
//
//public class ContainerPurifier extends IOMachineMenu {
//    private final BlockEntityPurifier purifier;
//    private int lastPurifierCookTime;
//
//    public ContainerPurifier(Player player, BlockEntityPurifier par2BlockEntityPurifier) {
//        super(player, par2BlockEntityPurifier);
//        lastPurifierCookTime = 0;
//        purifier = par2BlockEntityPurifier;
//        this.addSlot(new Slot(par2BlockEntityPurifier, 0, 35, 16));
//        this.addSlot(new Slot(par2BlockEntityPurifier, 7, 53, 16));
//
//        for (int i = 0; i < 5; i++)
//            this.addSlot(new Slot(par2BlockEntityPurifier, i + 1, 8 + i * 18, 52));
//
//        this.addSlot(new SlotFurnace(player, par2BlockEntityPurifier, 6, 134, 34));
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
//            if (lastPurifierCookTime != purifier.cookTime) {
//                icrafting.sendProgressBarUpdate(this, 0, purifier.cookTime);
//            }
//        }
//
//        lastPurifierCookTime = purifier.cookTime;
//    }
//
//    @Override
//    public void setData(int par1, int par2) {
//        if (par1 == 0) {
//            purifier.cookTime = par2;
//        }
//    }
//}
