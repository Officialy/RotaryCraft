///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.items.tools.steel;
//
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.rotarycraft.registry.RotaryItems;
//
//public class ItemSteelSickle extends ItemSickleBase {
//
//    public ItemSteelSickle(Item.Properties properties) {
//        super(properties);
//        this.setMaxDamage(600);
//
//        damageVsEntity = 4;
//    }
//
//    @Override
//    public int getLeafRange() {
//        return 4;
//    }
//
//    @Override
//    public int getCropRange() {
//        return 4;
//    }
//
//    @Override
//    public int getPlantRange() {
//        return 4;
//    }
//
//    @Override
//    public boolean canActAsShears() {
//        return false;
//    }
//
//    @Override
//    public boolean isBreakable() {
//        return true;
//    }
//
//    @Override
//    public boolean getIsRepairable(ItemStack tool, ItemStack item) {
//        return tool.getItem() == this && ReikaItemHelper.matchStacks(item, RotaryItems.HSLA_STEEL_INGOT);
//    }
//
//    @Override
//    public int getItemEnchantability(ItemStack is) {
//        return Items.iron_pickaxe.getItemEnchantability(is);
//    }
//
//}
