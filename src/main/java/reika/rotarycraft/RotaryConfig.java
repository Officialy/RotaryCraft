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
    private DataElement<String[]> blastGate;
    private DataElement<String> bedrockGate;
    private DataElement<String> gravelGate;

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
            ItemStack ret = ReikaItemHelper.parseItem(g.getItem());
            if (ret == null) {
                RotaryCraft.LOGGER.error("Selected gating material " + g + " could not be found; either the item does not exist or its mods have not yet loaded.");
            }
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
