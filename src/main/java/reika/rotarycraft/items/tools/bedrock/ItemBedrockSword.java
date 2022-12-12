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

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import reika.dragonapi.libraries.ReikaEnchantmentHelper;
import reika.dragonapi.libraries.ReikaPlayerAPI;
import reika.dragonapi.libraries.io.ReikaChatHelper;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.registry.ConfigRegistry;


public class ItemBedrockSword extends SwordItem {

    public ItemBedrockSword() {
        super(Tiers.NETHERITE, 5, 12, new Item.Properties().stacksTo(1).setNoRepair());
    }

    @Override
    public void onCraftedBy(ItemStack p_41447_, Level p_41448_, Player p_41449_) {
        //RotaryAchievements.BEDROCKTOOLS.triggerAchievement(ep);
    }

    @Override
    public boolean hurtEnemy(ItemStack is, LivingEntity target, LivingEntity player) {
        //ReikaEntityHelper.damageArmor(target, 100);
        if (player instanceof Player && (ConfigRegistry.FAKEBEDROCK.getState() || !ReikaPlayerAPI.isFake((Player) player))) {
            if (target.isAlive() || target.getHealth() <= 0) {
                if (player.getRandom().nextInt(5) == 0) {
                    //ReikaEntityHelper.dropHead(target);
                }
                if (target instanceof LivingEntity) {
                    //int xp = ((LivingEntity) target).experienceValue * ReikaRandomHelper.getRandomBetween(1, 10);
                    //ReikaWorldHelper.splitAndSpawnXP(target.getLevel(), target.getX(), target.getY() + player.getRandom().nextDouble() * target.getEyeHeight(), target.getZ(), xp); //target.height
                }
            }
        }
        return true;
    }

    /**
     * Called before a block is broken. Return true to prevent default block
     * harvesting.
     * <p>
     * Note: In SMP, this is called on both client and server sides!
     *
     * @param itemstack The current ItemStack
     * @param pos       Block's position in world
     * @param player    The Player that is wielding the item
     * @return True to prevent harvesting, false to continue as normal
     */
    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
        return true;
    }

    @Override
    public int getEnchantmentValue() {
        return ConfigRegistry.PREENCHANT.getState() ? 0 : Tiers.IRON.getEnchantmentValue();
    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return false;
    }

    // To make un-unenchantable
    @Override
    public void inventoryTick(ItemStack is, Level level, Entity entity, int par4, boolean p_41408_) {
        this.forceEnchants(is, level, entity, par4);
    }

    private void forceEnchants(ItemStack is, Level Level, Entity entity, int slot) {
        if (!ReikaEnchantmentHelper.hasEnchantment(Enchantments.MOB_LOOTING, is) || !ReikaEnchantmentHelper.hasEnchantment(Enchantments.SHARPNESS, is)) {
            entity.playSound(SoundEvents.ITEM_BREAK, 1, 1);
            if (entity instanceof Player) {
                Player ep = (Player) entity;
                ep.getInventory().setItem(slot, ItemStack.EMPTY);
                ep.hurt(DamageSource.GENERIC, 10);
                ReikaChatHelper.sendChatToPlayer(ep, "The dulled tool has broken.");
                is = null;
            }
        }
    }

    /**
     * Called by the default implemetation of EntityItem's onUpdate method, allowing
     * for cleaner control over the update of the item without having to write a
     * subclass.
     *
     * @param stack
     * @param ei    The entity Item
     * @return Return true to skip any further update code.
     */
    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity ei) {
        ItemStack is = ei.getItem();
//        if (!ReikaEnchantmentHelper.hasEnchantment(Enchantments.MOB_LOOTING, is) || !ReikaEnchantmentHelper.hasEnchantment(Enchantments.SHARPNESS, is)) {
//            ei.playSound(SoundEvents.ITEM_BREAK, 1, 1);
//            ei.kill();
//        }
        return false;
    }
}
