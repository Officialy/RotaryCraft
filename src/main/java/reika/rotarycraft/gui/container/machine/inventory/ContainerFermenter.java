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
//import net.minecraft.inventory.Slot;
//import net.minecraft.inventory.SlotFurnace;
//import net.minecraft.world.item.ItemStack;
//
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.rotarycraft.base.IOMachineMenu;
//
//import reika.rotarycraft.registry.RotaryAchievements;
//import reika.rotarycraft.blockentities.production.BlockEntityFermenter;
//
//public class ContainerFermenter extends IOMachineMenu {
//    private final BlockEntityFermenter fermenter;
//    private final int lastFermenterBurnTime;
//    private final int lastFermenterItemBurnTime;
//    private int lastFermenterCookTime;
//
//    public ContainerFermenter(Player player, BlockEntityFermenter par2BlockEntityFermenter) {
//        super(player, par2BlockEntityFermenter);
//        lastFermenterCookTime = 0;
//        lastFermenterBurnTime = 0;
//        lastFermenterItemBurnTime = 0;
//        fermenter = par2BlockEntityFermenter;
//        this.addSlot(new Slot(par2BlockEntityFermenter, 0, 55, 17));
//        //this.addSlot(new Slot(par2BlockEntityFermenter, 1, 55, 35));
//        //if (tile.level.isBlockIndirectlyGettingPowered(tile.xCoord, tile.yCoord, tile.zCoord))
//        //this.addSlot(new Slot(par2BlockEntityFermenter, 1, 55, 35));
//        //else
//        this.addSlot(new Slot(par2BlockEntityFermenter, 1, 55, 53));
//        this.addSlot(new SlotFurnace(player, par2BlockEntityFermenter, 2, 116, 35));
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
//            if (lastFermenterCookTime != fermenter.fermenterCookTime) {
//                icrafting.sendProgressBarUpdate(this, 0, fermenter.fermenterCookTime);
//                icrafting.sendProgressBarUpdate(this, 1, fermenter.getLevel());
//            }
//        }
//
//        lastFermenterCookTime = fermenter.fermenterCookTime;
//    }
//
//    @Override
//    public void setData(int par1, int par2) {
//        if (par1 == 0) {
//            fermenter.fermenterCookTime = par2;
//        }
//        if (par1 == 1) {
//            fermenter.setLiquid(par2);
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
