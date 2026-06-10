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

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import reika.dragonapi.ModList;
import reika.dragonapi.libraries.ReikaEnchantmentHelper;
import reika.dragonapi.libraries.io.ReikaChatHelper;
import reika.dragonapi.libraries.java.ReikaArrayHelper;
import reika.rotarycraft.base.ItemRotaryArmor;
import reika.rotarycraft.registry.Materials;
import reika.rotarycraft.registry.RotaryItems;

import java.util.HashMap;
import java.util.Locale;

// 1.21.5 NOTE: Enchantments constants are now ResourceKey<Enchantment>; legacy
// HashMap<Enchantment, Integer> APIs and getEnchantmentValue / onEntityItemUpdate have
// been removed. ItemBedrockArmor is trimmed to the bits that still compile while we
// rebuild the rest against the new APIs.
public class ItemBedrockArmor extends ItemRotaryArmor {

    public ItemBedrockArmor(net.minecraft.world.item.equipment.ArmorType slot, Properties properties) {
        super(Materials.BEDROCK_ALLOY, slot, properties);
    }

    @Override
    public void onUseTick(Level level, LivingEntity ep, ItemStack is, int p_41431_) {
        if (RotaryItems.getArmorType(this) == 0 && HelmetUpgrades.NIGHTVISION.existsOn(is)) {
            ep.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 3, 0, false, true));
        }
        ep.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(Double.MAX_VALUE);
    }

    public HashMap<ResourceKey<Enchantment>, Integer> getDefaultEnchantments() {
        HashMap<ResourceKey<Enchantment>, Integer> map = new HashMap<>();
        if (this == RotaryItems.BEDROCK_ALLOY_HELMET.get()) {
            map.put(Enchantments.PROJECTILE_PROTECTION, 4);
            map.put(Enchantments.RESPIRATION, 3);
        }
        if (this == RotaryItems.BEDROCK_ALLOY_CHESTPLATE.get() || this == RotaryItems.BEDROCK_ALLOY_LEGGINGS.get() || this == RotaryItems.BEDROCK_ALLOY_BOOTS.get()) {
            switch (RotaryItems.getArmorType(this)) {
                case 0 -> {
                    map.put(Enchantments.PROJECTILE_PROTECTION, 4);
                    map.put(Enchantments.RESPIRATION, 3);
                }
                case 1 -> map.put(Enchantments.BLAST_PROTECTION, 4);
                case 2 -> map.put(Enchantments.FIRE_PROTECTION, 4);
                case 3 -> {
                    map.put(Enchantments.FEATHER_FALLING, 4);
                    map.put(Enchantments.DEPTH_STRIDER, 4);
                }
            }
        }
        return map;
    }

    // 1.21.5: Item#inventoryTick signature is now (ItemStack, ServerLevel, Entity, EquipmentSlot).
    @Override
    public void inventoryTick(ItemStack is, net.minecraft.server.level.ServerLevel world, Entity entity, net.minecraft.world.entity.EquipmentSlot slot) {
        this.forceEnchantments(is, world, entity, slot == null ? 0 : slot.getIndex(0));
    }

    private void forceEnchantments(ItemStack is, Level world, Entity entity, int slot) {
        HashMap<ResourceKey<Enchantment>, Integer> map = this.getDefaultEnchantments();
        for (ResourceKey<Enchantment> e : map.keySet()) {
            if (!ReikaEnchantmentHelper.hasEnchantment(e, is)) {
                entity.playSound(SoundEvents.ITEM_BREAK.value(), 1, 1);
                if (entity instanceof Player ep) {
                    ep.getInventory().setItem(slot, ItemStack.EMPTY);
                    ep.hurt(ep.damageSources().generic(), 10);
                    ReikaChatHelper.sendChatToPlayer(ep, "The damaged tool has broken.");
                    break;
                }
            }
        }
    }

    @Override
    public boolean providesProtection() {
        return true;
    }

    @Override
    public boolean canBeDamaged() {
        return false;
    }

    public enum HelmetUpgrades {
        NIGHTVISION(),
        VISOR(),
        APIARIST(ModList.FORESTRY);

        public static final HelmetUpgrades[] list = values();

        public final boolean isAvailable;

        HelmetUpgrades() {
            this(true);
        }

        HelmetUpgrades(ModList mod) {
            this(mod.isLoaded());
        }

        HelmetUpgrades(boolean b) {
            isAvailable = b;
        }

        public boolean existsOn(ItemStack is) {
            net.minecraft.nbt.CompoundTag tag = is.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag();
            return tag != null && tag.getBooleanOr(this.getNBT(), false);
        }

        private String getNBT() {
            return this.name().toLowerCase(Locale.ENGLISH);
        }

        public void enable(ItemStack is, boolean set) {
            reika.dragonapi.libraries.registry.ReikaItemHelper.updateStackTag(is, __T__ -> __T__.putBoolean(this.getNBT(), set));
        }

        public ItemStack[] getUpgradeItems() {
            return switch (this) {
                case NIGHTVISION -> new ItemStack[]{RotaryItems.NVG.get().getDefaultInstance()};
                case VISOR ->
                        new ItemStack[]{new ItemStack(Blocks.GREEN_STAINED_GLASS, 1), new ItemStack(Items.DIAMOND), new ItemStack(Blocks.GREEN_STAINED_GLASS, 1)};
                case APIARIST ->
                        ReikaArrayHelper.getArrayOf(new ItemStack(Items.PINK_CARPET), 8);
            };
        }
    }
}
