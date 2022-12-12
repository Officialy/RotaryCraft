package reika.rotarycraft.registry;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public enum PlantMaterials {

    SUGARCANE(Items.SUGAR_CANE, 1),
    TALLGRASS(Blocks.TALL_GRASS, 2),
    LILYPAD(Blocks.LILY_PAD, 1),
    SAPLING(Blocks.OAK_SAPLING, 1),
    ROSE(Blocks.POPPY, 1),
    FLOWER(Blocks.DANDELION, 1),
    VINES(Blocks.VINE, 2),
    LEAVES(Blocks.OAK_LEAVES, 2), //todo all leaves
    LEAVES2(Blocks.SPRUCE_LEAVES, 2), //todo all leaves
    POTATO(Items.POTATO, 1);

    private final ItemStack item;
    private final int multiplier;

    public static final PlantMaterials[] plantList = PlantMaterials.values();

    PlantMaterials(Item i, int num) {
        this(new ItemStack(i), num);
    }

    PlantMaterials(Block i, int num) {
        this(new ItemStack(i), num);
    }

    PlantMaterials(ItemStack is, int num) {
        item = is;
        multiplier = num;
    }

    public static boolean isValidPlant(ItemStack is) {
        if (is == null)
            return false;
        for (int i = 1; i < plantList.length; i++) {
            if (plantList[i].item.getItem() == is.getItem())
                return true;
        }
        return false;
    }

    public static PlantMaterials getPlantEntry(ItemStack is) {
        if (is == null)
            return null;
        for (PlantMaterials plantMaterials : plantList) {
            if (plantMaterials.item.getItem() == is.getItem())
                return plantMaterials;
        }
        return null;
    }

    public int getPlantValue() {
        return multiplier;
    }

    public ItemStack getPlantItem() {
        return item.copy();
    }

    public ItemStack getPlantItemForIcon() {
        if (this == TALLGRASS)
            return new ItemStack(item.getItem(), 1);
        return item.copy();
    }

}
