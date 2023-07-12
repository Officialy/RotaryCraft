/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.items.tools;


import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.ItemRotaryArmor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemIOGoggles extends ItemRotaryArmor {

    public ItemIOGoggles() {
        super(ArmorMaterials.IRON, Type.HELMET, new Properties().stacksTo(1));
    }

    @Override
    public void onArmorTick(ItemStack stack, Level world, Player player) {
        super.onArmorTick(stack, world, player);
    }

    @Override
    public void onUseTick(Level p_41428_, LivingEntity p_41429_, ItemStack p_41430_, int p_41431_) {
        super.onUseTick(p_41428_, p_41429_, p_41430_, p_41431_);
    }
    /*
	@Override
	public String getArmorTexture(ItemStack itemstack, Entity e, int slot, String nulll) {
		return "/Reika/RotaryCraft/Textures/Misc/IOGoggles.png";
	}*/

    @Override
    public boolean providesProtection() {
        return false;
    }

    @Override
    public boolean canBeDamaged() {
        return false;
    }

}
