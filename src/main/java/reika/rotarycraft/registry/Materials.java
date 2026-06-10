package reika.rotarycraft.registry;

import java.util.EnumMap;
import java.util.Map;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAssets;

public class Materials {

    private static Map<ArmorType, Integer> defense(int value) {
        EnumMap<ArmorType, Integer> map = new EnumMap<>(ArmorType.class);
        for (ArmorType type : ArmorType.values())
            map.put(type, value);
        return map;
    }

    // 26.1: ArmorMaterial is a record. Custom armor textures need a registered EquipmentAsset +
    // client model JSON; we reuse vanilla IRON/DIAMOND equipment assets so the armor renders with
    // the right silhouette. The defense/durability values match the 1.7.10 originals. Wire custom
    // assets later if you want bespoke HSLA / bedrock armor textures.
    public static final ArmorMaterial HSLA_STEEL = new ArmorMaterial(
            37, defense(3), 6, SoundEvents.ARMOR_EQUIP_IRON, 6F, 3F, ItemTags.REPAIRS_IRON_ARMOR, EquipmentAssets.IRON);

    public static final ArmorMaterial BEDROCK_ALLOY = new ArmorMaterial(
            37, defense(15), 12, SoundEvents.ARMOR_EQUIP_IRON, 14F, 8F, ItemTags.REPAIRS_DIAMOND_ARMOR, EquipmentAssets.DIAMOND);

}
