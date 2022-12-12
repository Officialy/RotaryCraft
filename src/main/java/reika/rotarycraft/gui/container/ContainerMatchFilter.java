///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.containers;
//
//
//import net.minecraft.world.Container;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.inventory.Slot;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//
//public class ContainerMatchFilter extends Container {
//    private final Level level;
//    /**
//     * The crafting matrix inventory (3x3).
//     */
//    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 1, 1);
//
//    public ContainerMatchFilter(Player player, Level par2World) {
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
//    public void onContainerClosed(Player par1Player) {
//        super.onContainerClosed(par1Player);
//    }
//
//    @Override
//    public boolean canInteractWith(Player par1Player) {
//        return true;
//    }
//
//    @Override
//    public ItemStack transferStackInSlot(Player par1Player, int par2) {
//        return this.getSlot(0).getStack();
//    }
//}
