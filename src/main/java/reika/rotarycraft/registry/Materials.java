package reika.rotarycraft.registry;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import reika.rotarycraft.RotaryCraft;

public class Materials {

    public static ArmorMaterial HSLA_STEEL = new ArmorMaterial() {

        @Override
        public int getDurabilityForType(ArmorItem.Type p_266807_) {
            return 37;
        }

        @Override
        public int getDefenseForType(ArmorItem.Type p_267168_) {
            return 3;
        }

        @Override
        public int getEnchantmentValue() {
            return 6;
        }

        @Override
        public SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_IRON;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(RotaryItems.HSLA_STEEL_INGOT.get());
        }

        @Override
        public String getName() {
            return RotaryCraft.MODID + ":" + "hsla_steel";
        }

        @Override
        public float getToughness() {
            return 6;
        }

        @Override
        public float getKnockbackResistance() {
            return 3;
        }
    };

    public static ArmorMaterial BEDROCK_ALLOY = new ArmorMaterial() {
        @Override
        public int getDurabilityForType(ArmorItem.Type p_40410_) {
            return 37;
        }

        @Override
        public int getDefenseForType(ArmorItem.Type p_40411_) {
            return 15;
        }

        @Override
        public int getEnchantmentValue() {
            return 12;
        }

        @Override
        public SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_IRON;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(RotaryItems.HSLA_STEEL_INGOT.get());
        }

        @Override
        public String getName() {
            return RotaryCraft.MODID + ":" + "bedrock_alloy";
        }

        @Override
        public float getToughness() {
            return 14;
        }

        @Override
        public float getKnockbackResistance() {
            return 8;
        }
    };


}
