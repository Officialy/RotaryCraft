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

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import reika.dragonapi.libraries.ReikaEnchantmentHelper;
import reika.rotarycraft.registry.RotaryItems;

// 26.1: legacy 1.7.10 features still missing:
//   - Whole-tree felling via TreeReader / ReikaTreeHelper. Those helpers don't exist in the
//     current DragonAPI port; add them and hook into NeoForge BlockEvent.BreakEvent here to
//     restore the chain-chop behaviour.
//   - onBlockStartBreak / canAttackBlock / getEnchantmentValue overrides — those methods
//     were removed from Item; the same behaviour is now event-driven (BlockEvent listeners,
//     ToolMaterial / enchantment data components).
// The standard axe behaviours (high destroy speed for wood, anti-silk-touch on use, special
// hurt against EntityEnt) are functional below.
public class ItemBedrockAxe extends AxeItem {

    public ItemBedrockAxe() {
        super(ToolMaterial.NETHERITE, 6F, -3.0F, RotaryItems.itemProperties().stacksTo(1));
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack is, int pRemainingUseDuration) {
        this.forceNoSilkTouch(is);
    }

    private void forceNoSilkTouch(ItemStack is) {
        if (ReikaEnchantmentHelper.hasEnchantment(Enchantments.SILK_TOUCH, is)) {
            ReikaEnchantmentHelper.removeEnchantment(is, Enchantments.SILK_TOUCH);
        }
    }

    @Override
    public float getDestroySpeed(ItemStack is, BlockState b) {
        if (b == null)
            return 0;
        if (b.getBlock().defaultMapColor() == MapColor.WOOD) {
            return 20F;
        }
        return 1F;
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity player) {
        if (target.getClass().getSimpleName().equals("EntityEnt")) {
            DamageSource src = player instanceof Player ? player.damageSources().playerAttack((Player) player) : player.damageSources().generic();
            target.setHealth(1);
            target.hurt(src, Integer.MAX_VALUE);
        } else {
            super.hurtEnemy(stack, target, player);
        }
    }
}
