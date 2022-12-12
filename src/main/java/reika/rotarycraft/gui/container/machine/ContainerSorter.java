///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.gui.container.machine;
//
//import reika.dragonapi.instantiable.gui.Slot.GhostSlot;
//import reika.rotarycraft.base.IOMachineMenu;
//import reika.rotarycraft.blockentities.BlockEntitySorting;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//
//public class ContainerSorter extends IOMachineMenu {
//
//    private final BlockEntitySorting tile;
//
//    public ContainerSorter(Player player, BlockEntitySorting te) {
//        super(player, te);
//        tile = te;
//
//        int l = BlockEntitySorting.LENGTH;
//        int dy = 22;
//        int x = 8;
//        int y = 18;
//        for (int k = 0; k < 3; k++) {
//            for (int i = 0; i < l; i++) {
//                this.addSlot(new GhostSlot(i + k * l, x + i * 18, y + dy * k));
//            }
//        }
//
//        this.addPlayerInventoryWithOffset(player, 0, 14);
//    }
//
//    @Override
//    public ItemStack slotClick(int slot, int par2, int par3, Player ep) {
//        boolean inGUI = slot < BlockEntitySorting.LENGTH * 3 && slot >= 0;
//        if (inGUI) {
//            ItemStack held = ep.inventory.getItemStack();
//            tile.setMapping(slot, ReikaItemHelper.getSizedItemStack(held, 1));
//            return held;
//        } else
//            return super.slotClick(slot, par2, par3, ep);
//    }
//
//}
