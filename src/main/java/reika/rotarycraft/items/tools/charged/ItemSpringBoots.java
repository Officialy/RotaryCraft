/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.items.tools.charged;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import reika.rotarycraft.base.ItemChargedArmor;
import reika.rotarycraft.registry.RotaryItems;

//@Strippable(value = {"forestry.api.apiculture.IArmorApiarist"})
public class ItemSpringBoots extends ItemChargedArmor //implements IArmorApiarist {
{
    public final int JUMP_LEVEL = 3;
    public final int SPEED_LEVEL = 2;

    public ItemSpringBoots(ArmorMaterial mat, Item.Properties properties) {
        super(mat, EquipmentSlot.FEET, properties);
    }

//    public static boolean isSpringBoots(ItemStack is) {
//        return is.getItem() instanceof ItemSpringBoots || (ModList.CHROMATICRAFT.isLoaded() && checkFloatstoneBoots(is));
//    }

    /* @ModDependent(ModList.CHROMATICRAFT)
     private static boolean checkFloatstoneBoots(ItemStack is) {
         if (ChromaItems.FLOATBOOTS.matchWith(is)) {
             ItemStack in = ItemFloatstoneBoots.getSpecialItem(is);
             if (in != null) {
                 return in.getItem() instanceof ItemSpringBoots;
             }
         }
         return false;
     }
 */
    @Override
    public boolean providesProtection() {
        return this == RotaryItems.BEDROCK_ALLOY_JUMP_BOOTS.get();
    }

    @Override
    public boolean canBeDamaged() {
        return false;
    }

    /*
    @SubscribeEvent
    public void undoStepHeight(ScheduledTickEvent evt) {
        if (evt.type == TickType.PLAYER) {
            Player ep = (Player)evt.getData(0);
            ep.stepHeight = 0.5F;
        }
    }

    @Override
    public double getDamageMultiplier(DamageSource src) {
        ItemBedrockArmor arm = (ItemBedrockArmor) RotaryItems.BEDBOOTS.get();
        return this == RotaryItems.BEDJUMP.get() ? arm.getDamageMultiplier(src) : 1;
    }

*/
//    @Override
//    public void onArmorTick(ItemStack is, Level world, Player ep) {
//        if (is.getItem() == RotaryItems.BEDJUMP.get() || is.getDamageValue() > 0) {
//            MobEffect pot = ep.getActiveEffects(new MobEffectInstance(MobEffects.JUMP));
//            if (pot == null || pot.getAmplifier() < JUMP_LEVEL) {
//                ep.addEffect(new MobEffectInstance(MobEffects.JUMP, 1, JUMP_LEVEL));
//            }
//            pot = ep.getActiveMobEffect(MobEffects.MOVEMENT_SPEED);
//            if (pot == null || pot.getAmplifier() < SPEED_LEVEL) {
//                ep.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1, SPEED_LEVEL));
//            }
//            ep.maxUpStep = ep.isCrouching() ? 0.5F : Math.max(ep.maxUpStep, 1.45F); //1.25F
//            if (new Random().nextInt(3200) == 0) {
//                if (is.getItem() != RotaryItems.BEDJUMP.get()) {
//                    ep.setCurrentItemOrArmor(1, new ItemStack(is.getItem(), is.getCount()));
//                    //this.warnCharge(is);
//                }
//            }
//            //ReikaPlayerAPI.schedulePlayerTick(ep, 5);
//        } else {
//            ep.maxUpStep = 0.5F;
//        }
//    }

//    @Override
//    public boolean protectEntity(LivingEntity entity, ItemStack armor, String cause, boolean doProtect) {
//        ItemStack head = entity.getEquipmentInSlot(4);
//        RotaryItems ir = head != null ? RotaryItems.getEntry(head) : null;
//        return ir != null && ir.isBedrockArmor() && HelmetUpgrades.APIARIST.existsOn(head);
//    }
//
//    @Override
//    @Deprecated
//    public boolean protectPlayer(Player player, ItemStack armor, String cause, boolean doProtect) {
//        return this.protectEntity(player, armor, cause, doProtect);
//    }

    @Override
    public final void setDamage(ItemStack stack, int damage) {

    }
}
