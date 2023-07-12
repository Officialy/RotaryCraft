/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.items.tools.bedrock;

import net.minecraft.world.entity.EquipmentSlot;

//@Strippable(value = {"thaumcraft.api.nodes.IRevealer", "thaumcraft.api.IGoggles", "thaumcraft.api.IVisDiscountGear"})
public class ItemBedReveal extends ItemBedrockArmor {//implements IRevealer, IGoggles, IVisDiscountGear {

    public ItemBedReveal(Properties properties) {
        super(Type.HELMET, properties);
    }

//    @Override
//    public boolean showIngamePopups(ItemStack is, LivingEntity player) {
//        return true;
//    }
//
//    @Override
//    public boolean showNodes(ItemStack is, LivingEntity elb) {
//        return true;
//    }
//
//    @Override
//    public HashMap<Enchantment, Integer> getDefaultEnchantments() {
//        return ((ItemBedrockArmor) RotaryItems.BEDROCK_HELMET.get()).getDefaultEnchantments();
//    }
//
//    @Override
//    @ModDependent(ModList.THAUMCRAFT)
//    public int getVisDiscount(ItemStack is, Player ep, Aspect a) {
//        return a == Aspect.ORDER || a == Aspect.ENTROPY ? 10 : 5;
//    }

}
