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
//
//import reika.dragonapi.instantiable.gui.Slot.SlotXItems;
//import reika.rotarycraft.auxiliary.SlotExtractor1;
//import reika.rotarycraft.auxiliary.SlotExtractor2;
//import reika.rotarycraft.auxiliary.SlotExtractor3;
//import reika.rotarycraft.auxiliary.SlotExtractor4;
//import reika.rotarycraft.auxiliary.SlotMachineOut;
//import reika.rotarycraft.base.IOMachineMenu;
//
//import reika.rotarycraft.blockentities.processing.BlockEntityExtractor;
//
//public class ContainerExtractor extends IOMachineMenu {
//    private final BlockEntityExtractor extractor;
//    private final int[] lastExtractorCookTime;
//
//    public ContainerExtractor(Player player, BlockEntityExtractor te) {
//        super(player, te);
//        lastExtractorCookTime = new int[4];
//        extractor = te;
//        this.addSlot(new SlotExtractor1(te, 0, 26, 13));
//        this.addSlot(new SlotMachineOut(player, te, 4, 26, 55));
//        this.addSlot(new SlotExtractor2(te, 1, 62, 13));
//        this.addSlot(new SlotMachineOut(player, te, 5, 62, 55));
//        this.addSlot(new SlotExtractor3(te, 2, 98, 13));
//        this.addSlot(new SlotMachineOut(player, te, 6, 98, 55));
//        this.addSlot(new SlotExtractor4(te, 3, 134, 13));
//        this.addSlot(new SlotMachineOut(player, te, 7, 134, 55));
//        this.addSlot(new SlotMachineOut(player, te, 8, 152, 55));
//
//        if (RotaryConfig.COMMON.EXTRACTORMAINTAIN.getState()) {
//            this.addSlot(new SlotXItems(te, 9, 26, 34, 1));
//        }
//
//        this.addPlayerInventory(player);
//    }
//
//    /**
//     * Updates crafting matrix; called from onCraftMatrixChanged. Args: none
//     */
//    public void broadcastChanges(int i) {
//        super.broadcastChanges();
//
//        for (int j = 0; j < crafters.size(); j++) {
//            ICrafting icrafting = (ICrafting) crafters.get(i);
//
//            if (lastExtractorCookTime[i] != extractor.getCookTime(i)) {
//                icrafting.sendProgressBarUpdate(this, 0, extractor.getCookTime(i));
//            }
//        }
//
//        lastExtractorCookTime[i] = extractor.getCookTime(i);
//    }
//
//    public void setData(int par1, int par2, int i) {
//        switch (par1) {
//            case 0:
//                extractor.setCookTime(i, par2);
//                break;
//        }
//    }
//}
