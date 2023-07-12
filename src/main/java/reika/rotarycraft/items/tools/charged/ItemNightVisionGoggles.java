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

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.ItemChargedArmor;

public class ItemNightVisionGoggles extends ItemChargedArmor {

    public ItemNightVisionGoggles() {
        super(ArmorMaterials.IRON, Type.HELMET, new Properties());
    }

    @Override
    public void onArmorTick(ItemStack stack, Level world, Player player) {
        if (stack.getTag().getInt("power") < 5) {
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 30, 1, true, true));
        }
    }

    @Override
    public boolean providesProtection() {
        return false;
    }

    @Override
    public boolean canBeDamaged() {
        return false;
    }

}
