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
//import net.minecraft.world.Container;
//import net.minecraft.world.level.material.Fluid;
//import reika.rotarycraft.auxiliary.WorldEditHelper;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.inventory.Slot;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//
//public class ContainerWorldEdit extends Container {
//    private final Level level;
//    /**
//     * The crafting matrix inventory (3x3).
//     */
//    public CraftingMenu craftMatrix = new CraftingMenu(this, 1, 1);
//
//    public ContainerWorldEdit(Player player, Level par2World) {
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
//        ItemStack var3 = craftMatrix.getStackInSlotOnClosing(0);
//        if (var3 != null) {
//            if (!ReikaInventoryHelper.addToIInv(var3, par1Player.inventory)) {
//                if (!level.isClientSide)
//                    par1Player.dropPlayerItemWithRandomChoice(var3, true);
//            }
//            if (FluidContainerRegistry.isFilledContainer(var3)) {
//                Fluid liq = FluidContainerRegistry.getFluidForFilledItem(var3).getFluid();
//                if (liq.canBePlacedInWorld())
//                    WorldEditHelper.addCommand(par1Player, liq.getBlock(), 0);
//                return;
//            }
//            if (!(var3.getItem() instanceof BlockItem))
//                return;
//            WorldEditHelper.addCommand(par1Player, var3.getItem(), var3.getItemDamage());
//        } else {
//            WorldEditHelper.addCommand(par1Player, Blocks.AIR, 0);
//        }
//    }
//
//    @Override
//    public boolean canInteractWith(Player par1Player) {
//        return true;
//    }
//
//    /**
//     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
//     */
//    @Override
//    public ItemStack transferStackInSlot(Player par1Player, int par2) {
//        return this.getSlot(0).getStack();
//    }
//}
