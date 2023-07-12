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
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
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
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.base.ItemRotaryArmor;

import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.Materials;
import reika.rotarycraft.registry.RotaryItems;

import java.util.HashMap;
import java.util.Locale;

//@Strippable(value = {"forestry.api.apiculture.IArmorApiarist"})
public class ItemBedrockArmor extends ItemRotaryArmor {//implements IArmorApiarist {

    public ItemBedrockArmor(Type slot, Properties properties) {
        super(Materials.BEDROCK_ALLOY, slot, properties);
    }

//    public static boolean isWearingFullSuitOf(LivingEntity e) {
//        return ReikaEntityHelper.isEntityWearingFullSuitOf(e, (ItemStack is) -> isValidBedrockArmorItem(is));
//    }
//
//    @ModDependent(ModList.CHROMATICRAFT)
//    private static boolean checkFloatstoneBoots(ItemStack is) {
//        if (ChromaItems.FLOATBOOTS.matchWith(is)) {
//            ItemStack in = ItemFloatstoneBoots.getSpecialItem(is);
//            if (in != null) {
//                return isValidBedrockArmorItem(in);
//            }
//        }
//        return false;
//    }
//
//    public static boolean isValidBedrockArmorItem(ItemStack is) {
//        if (is == null)
//            return false;
//        if (ModList.CHROMATICRAFT.isLoaded()) {
//            if (checkFloatstoneBoots(is))
//                return true;
//        }
//        RotaryItems ir = RotaryItems.getEntry(is);
//        if (ir == null)
//            return false;
//        if (!ir.isBedrockTypeArmor())
//            return false;
//        return true;
//    }


    @Override
    public Component getDescription() {
        super.getDescription();
        for (int i = 0; i < HelmetUpgrades.list.length; i++) {
            HelmetUpgrades g = HelmetUpgrades.list[i];
            if (g.isAvailable && g.existsOn(this.getDefaultInstance())) {
                return Component.literal("Upgraded: " + g.name());
            }
        }
        return (Component) Component.EMPTY;
    }

    @Override
    public void onUseTick(Level level, LivingEntity ep, ItemStack is, int p_41431_) {
        if (RotaryItems.getArmorType(this) == 0 && HelmetUpgrades.NIGHTVISION.existsOn(is)) {
            ep.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 3, 0, false, true));
        }
        ep.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(Double.MAX_VALUE);
    }

    public HashMap<Enchantment, Integer> getDefaultEnchantments() {
        HashMap<Enchantment, Integer> map = new HashMap<>();
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
                    map.put(Enchantments.FALL_PROTECTION, 4);
                    map.put(Enchantments.DEPTH_STRIDER, 4);
                }
            }
        }
        return map;
    }

    @Override
    public void inventoryTick(ItemStack is, Level world, Entity entity, int par4, boolean p_41408_) {
        this.forceEnchantments(is, world, entity, par4);
    }

    /**
     * Called by the default implemetation of EntityItem's onUpdate method, allowing
     * for cleaner control over the update of the item without having to write a
     * subclass.
     *
     * @param stack
     * @param entity The entity Item
     * @return Return true to skip any further update code.
     */
    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        ItemStack is = entity.getItem();
        HashMap<Enchantment, Integer> map = this.getDefaultEnchantments();
        for (Enchantment e : map.keySet()) {
            if (!ReikaEnchantmentHelper.hasEnchantment(e, is)) {
                entity.playSound(SoundEvents.ITEM_BREAK, 1, 1);
                entity.kill();
            }
        }
        return false;
    }

    private void forceEnchantments(ItemStack is, Level world, Entity entity, int slot) {
        HashMap<Enchantment, Integer> map = this.getDefaultEnchantments();
        for (Enchantment e : map.keySet()) {
            if (!ReikaEnchantmentHelper.hasEnchantment(e, is)) {
                entity.playSound(SoundEvents.ITEM_BREAK, 1, 1);
                if (entity instanceof Player ep) {
                    ep.getInventory().setItem(slot, ItemStack.EMPTY);
                    ep.hurt(ep.damageSources().generic(), 10);
                    ReikaChatHelper.sendChatToPlayer(ep, "The damaged tool has broken.");
                    is = ItemStack.EMPTY;
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

//    @Override
//    public double getDamageMultiplier(DamageSource src) { todo damagemultiplier
//        return src.isUnblockable() ? 0.75 : 0.35;
//    }


    @Override
    public int getEnchantmentValue() {
        return ConfigRegistry.PREENCHANT.getState() ? 0 : Items.IRON_PICKAXE.getEnchantmentValue();//(Items.IRON_PICKAXE.getDefaultInstance());
    }
//    @Override
//    public boolean protectEntity(LivingEntity entity, ItemStack armor, String cause, boolean doProtect) {
//        ItemStack head = entity.getEquipmentInSlot(4);
//        RotaryItems ir = head != null ? RotaryItems.getEntry(head) : null;
//        return ir != null && ir.isBedrockArmor() && HelmetUpgrades.APIARIST.existsOn(head);
//    }

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
            return is.getTag() != null && is.getTag().getBoolean(this.getNBT());
        }

        private String getNBT() {
            return this.name().toLowerCase(Locale.ENGLISH);
        }

        public void enable(ItemStack is, boolean set) {
            is.getOrCreateTag().putBoolean(this.getNBT(), set);
        }

        public ItemStack[] getUpgradeItems() {
            return switch (this) {
                case NIGHTVISION -> new ItemStack[]{RotaryItems.NVG.get().getDefaultInstance()};
                case VISOR ->
                        new ItemStack[]{new ItemStack(Blocks.GREEN_STAINED_GLASS, 1), new ItemStack(Items.DIAMOND), new ItemStack(Blocks.GREEN_STAINED_GLASS, 1)};
                case APIARIST ->
                        ReikaArrayHelper.getArrayOf(/*ForestryHandler.CraftingMaterials.WOVENSILK.getItem()*/new ItemStack(Items.PINK_CARPET), 8);
            };
        }
    }

}
