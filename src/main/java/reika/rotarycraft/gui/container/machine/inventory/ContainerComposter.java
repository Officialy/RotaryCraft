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
//
//import net.minecraft.world.entity.player.Inventory;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.inventory.Slot;
//import net.minecraft.world.item.ItemStack;
//import reika.dragonapi.base.CoreMenu;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.rotarycraft.blockentities.farming.BlockEntityComposter;
//import reika.rotarycraft.registry.RotaryItems;
//
//public class ContainerComposter extends CoreContainer<> {
//    private final BlockEntityComposter composter;
//    private int lastComposterCookTime;
//
//    public ContainerComposter(Inventory player, BlockEntityComposter par2BlockEntityComposter) {
//        super(player, par2BlockEntityComposter);
//        lastComposterCookTime = 0;
//        composter = par2BlockEntityComposter;
//        this.addSlot(new Slot(par2BlockEntityComposter, 0, 55, 26));
//        this.addSlot(new Slot(par2BlockEntityComposter, 1, 55, 44));
//        this.addSlot(new SlotFurnace(player, par2BlockEntityComposter, 2, 116, 35));
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
//            if (lastComposterCookTime != composter.composterCookTime) {
//                icrafting.sendProgressBarUpdate(this, 0, composter.composterCookTime);
//            }
//        }
//
//        lastComposterCookTime = composter.composterCookTime;
//    }
//
//    @Override
//    public void setData(int par1, int par2) {
//        if (par1 == 0) {
//            composter.composterCookTime = par2;
//        }
//    }
//
//    @Override
//    public ItemStack slotClick(int par1, int par2, int par3, Player ep) {
//        ItemStack is = super.slotClick(par1, par2, par3, ep);
//        if (ReikaItemHelper.matchStacks(RotaryItems.YEAST.get(), is))
//            RotaryAchievements.MAKEYEAST.triggerAchievement(ep);
//        return is;
//    }
//}
