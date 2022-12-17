///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.gui.container;
//
//
//import net.minecraft.world.Container;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.inventory.AbstractContainerMenu;
//import net.minecraft.world.inventory.CraftingMenu;
//import net.minecraft.world.inventory.Slot;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//
//public class ContainerMatchFilter extends AbstractContainerMenu {
//    private final Level level;
//    /**
//     * The crafting matrix inventory (3x3).
//     */
//    public CraftingMenu craftMatrix = new CraftingMenu(this, 1, 1);
//
//    public ContainerMatchFilter(Player player, Level par2World) {
//        super(null, 0);
//        level = par2World;
//        int var6;
//        int var7;
//
//        this.addSlot(new Slot(craftMatrix, 0, 80, 35));
//
//        for (var6 = 0; var6 < 3; ++var6)
//            for (var7 = 0; var7 < 9; ++var7)
//                this.addSlot(new Slot(player.inventory, var7 + var6 * 9 + 9, 8 + var7 * 18, 84 + var6 * 18));
//        for (var6 = 0; var6 < 9; ++var6)
//            this.addSlot(new Slot(player.inventory, var6, 8 + var6 * 18, 142));
//        this.onCraftMatrixChanged(craftMatrix);
//    }
//
//    @Override
//    public ItemStack quickMoveStack(Player p_38941_, int p_38942_) {
//        return null;
//    }
//
//    @Override
//    public boolean stillValid(Player p_38874_) {
//        return true;
//    }
//
///*
//    @Override
//    public ItemStack transferStackInSlot(Player par1Player, int par2) {
//        return this.getSlot(0).getStack();
//    }*/
//}
