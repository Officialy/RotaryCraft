package reika.rotarycraft;

import net.minecraft.world.item.ItemStack;
import reika.dragonapi.auxiliary.EnumDifficulty;
import reika.dragonapi.base.DragonAPIMod;
import reika.dragonapi.instantiable.io.ControlledConfig;
import reika.dragonapi.interfaces.configuration.ConfigList;
import reika.dragonapi.interfaces.registry.IDRegistry;
import reika.dragonapi.libraries.java.ReikaJavaLibrary;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.auxiliary.BlastGate;
import reika.rotarycraft.registry.RotaryAdvancements;

import java.util.ArrayList;

public class RotaryConfig extends ControlledConfig {

    private static final ArrayList<String> entries = ReikaJavaLibrary.getEnumEntriesWithoutInitializing(RotaryAdvancements.class);
    private final DataElement<String[]> blastGate;
    private final DataElement<String> bedrockGate;
    private final DataElement<String> gravelGate;

    /**
     * Non-config-file control data used by the machines
     */

    public static final int friction = 0;
    public static final int torquelimit = (Integer.MAX_VALUE - 1) / 2;    // ~1 billion
    public static final int omegalimit = (Integer.MAX_VALUE - 1) / 2;
    public static final boolean debugmode = false;

    public static final EnumDifficulty EASIEST = EnumDifficulty.EASY;
    public static final EnumDifficulty HARDEST = EnumDifficulty.HARD;

    public RotaryConfig(DragonAPIMod mod, ConfigList[] option, IDRegistry[] id) {
        super(mod, option, id);

        blastGate = this.registerAdditionalOption("Other Options", "Alternate Blast Furnace Materials", new String[0]);
        bedrockGate = this.registerAdditionalOption("Other Options", "Bedrock Armor Gating Material", "");
        gravelGate = this.registerAdditionalOption("Other Options", "Gravel Gun Gating Material", "");
    }

    @Override
    protected void onInit() {

    }

    public ItemStack getBedrockArmorGatingMaterial(boolean check, ItemStack obj) {
        String item = bedrockGate.getData();
        if (!check || item == null || item.length() == 0)
            return obj;
        return this.getGatedMaterial(item, obj);
    }

    public ItemStack getGravelGunGatingMaterial(boolean check, ItemStack obj) {
        String item = gravelGate.getData();
        if (!check || item == null || item.length() == 0)
            return obj;
        return this.getGatedMaterial(item, obj);
    }

    /** Resolves the recipe gate without constructing stacks before 26.3 components are bound. */
    public net.minecraft.world.item.crafting.Ingredient getGravelGunGatingIngredient(
            net.minecraft.core.HolderGetter<net.minecraft.world.item.Item> items,
            net.minecraft.world.level.ItemLike fallback) {
        String configured = gravelGate.getData();
        if (configured == null || configured.isBlank())
            return net.minecraft.world.item.crafting.Ingredient.of(fallback);
        try {
            Object material = BlastGate.valueOf(configured.toUpperCase(java.util.Locale.ROOT)).getItem();
            if (material instanceof net.minecraft.world.level.ItemLike item)
                return net.minecraft.world.item.crafting.Ingredient.of(item);
            if (material instanceof String ore)
                return net.minecraft.world.item.crafting.Ingredient.of(items.getOrThrow(ReikaItemHelper.getOreTag(ore)));
            throw new IllegalArgumentException("Unsupported gate material: " + material);
        } catch (IllegalArgumentException exception) {
            RotaryCraft.LOGGER.error("Invalid Gravel Gun gating material '{}'; using {}", configured, fallback, exception);
            return net.minecraft.world.item.crafting.Ingredient.of(fallback);
        }
    }

    private ItemStack getGatedMaterial(String item, ItemStack obj) {
        BlastGate g = null;
        try {
            g = BlastGate.valueOf(item.toUpperCase());
        } catch (IllegalArgumentException ignored) {

        }
        if (g == null) {
            RotaryCraft.LOGGER.error("Gating material '" + item + "' is invalid.");
            return obj;
        } else {
            Object material = g.getItem();
            if (material instanceof net.minecraft.world.level.ItemLike selected)
                return new ItemStack(selected);
            if (material instanceof String ore) {
                var tag = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(ReikaItemHelper.getOreTag(ore));
                var selected = tag.stream().flatMap(net.minecraft.core.HolderSet.Named::stream)
                        .map(net.minecraft.core.Holder::value)
                        .sorted(java.util.Comparator.comparing(value -> net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(value).toString()))
                        .findFirst();
                if (selected.isPresent()) return new ItemStack(selected.get());
            }
            RotaryCraft.LOGGER.error("Selected gating material {} could not be found; its item tag may be empty or its mod absent.", g);
        }
        return obj;
    }

    public Object[] getBlastFurnaceGatingMaterials(boolean check, Object obj1, Object obj2, Object obj3, Object obj4) {
        String[] arr = blastGate.getData();
        if (!check || arr == null || arr.length == 0)
            return new Object[]{obj1, obj2, obj3, obj4};
        ArrayList<Object> c = new ArrayList();
        boolean invalid = false;
        for (String s : arr) {
            String idx = s.toUpperCase();
            BlastGate g = null;
            try {
                g = BlastGate.valueOf(idx);
            } catch (IllegalArgumentException ignored) {

            }
            if (g == null) {
                RotaryCraft.LOGGER.error("Gating material '" + idx + "' is invalid.");
                invalid = true;
            } else {
                Object item = g.getItem();
                if (item == null) {
                    RotaryCraft.LOGGER.error("Selected gating material " + g + " could not be found; either the item does not exist or its mods have not yet loaded.");
                } else {
                    c.add(item);
                }
            }
        }
        if (invalid) {
            RotaryCraft.LOGGER.info("Valid materials (case insensitive):");
            StringBuilder sb = new StringBuilder();
            for (BlastGate g : BlastGate.values())
                sb.append(g.name() + "; ");
            RotaryCraft.LOGGER.info(sb.toString());
        }

        switch (c.size()) {
            case 1 -> obj1 = obj2 = obj3 = obj4 = c.get(0);
            case 2 -> {
                obj1 = obj4 = c.get(0);
                obj2 = obj3 = c.get(1);
            }
            case 3 -> {
                obj1 = obj4 = c.get(0);
                obj2 = c.get(1);
                obj3 = c.get(2);
            }
            case 4 -> {
                obj1 = c.get(0);
                obj2 = c.get(1);
                obj3 = c.get(2);
                obj4 = c.get(3);
            }
            default -> {
            }
        }

        return new Object[]{obj1, obj2, obj3, obj4};
    }
}
