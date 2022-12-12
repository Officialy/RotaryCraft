///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.items.tools.bedrock;
//
//import net.minecraft.sounds.SoundEvents;
//import net.minecraft.world.damagesource.DamageSource;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.item.ItemEntity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.item.enchantment.Enchantments;
//import net.minecraft.world.level.Level;
//import reika.dragonapi.libraries.ReikaEnchantmentHelper;
//import reika.dragonapi.libraries.io.ReikaChatHelper;
//import reika.rotarycraft.base.ItemSickleBase;
//
//
//public class ItemBedrockSickle extends ItemSickleBase {
//
//    public ItemBedrockSickle(int index) {
//        super(index);
//
//        damageVsEntity = 6;
//    }
//
//    @Override
//    public int getLeafRange() {
//        return 6;
//    }
//
//    @Override
//    public int getCropRange() {
//        return 8;
//    }
//
//    @Override
//    public int getPlantRange() {
//        return 7;
//    }
//
//    @Override
//    public boolean canActAsShears() {
//        return true;
//    }
//
//    @Override
//    public boolean isBreakable() {
//        return false;
//    }
//
//    @Override
//    public void onCreated(ItemStack is, Level Level, Player ep) {
////        RotaryAchievements.BEDROCKTOOLS.triggerAchievement(ep);
//    }
//
//    public void onUpdate(ItemStack is, Level Level, Entity entity, int slot) {
//        this.forceFortune(is, Level, entity, slot);
//    }
//
//    private void forceFortune(ItemStack is, Level Level, Entity entity, int slot) {
//        if (!ReikaEnchantmentHelper.hasEnchantment(Enchantments.BLOCK_FORTUNE, is)) {
//            if (entity instanceof Player) {
//                entity.playSound(SoundEvents.ITEM_BREAK, 1, 1);
//                Player ep = (Player) entity;
//                ep.getInventory().canPlaceItem(slot, null);
//                ep.hurt(DamageSource.GENERIC, 10);
//                ReikaChatHelper.sendChatToPlayer(ep, "The dulled tool has broken.");
//                is = null;
//            }
//        }
//    }
//
//    @Override
//    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity itemEntity) {
//        ItemStack is = itemEntity.getItem();
//        if (!ReikaEnchantmentHelper.hasEnchantment(Enchantments.BLOCK_FORTUNE, is)) {
//            itemEntity.playSound(SoundEvents.ITEM_BREAK, 1, 1);
//            itemEntity.kill();
//        }
//        return false;
//    }
//
//    @Override
//    public ItemStack getEnchantabilityReference() {
//        return RotaryConfig.COMMON.PREENCHANT.get() ? null : new ItemStack(Items.IRON_PICKAXE);
//    }
//
//}
