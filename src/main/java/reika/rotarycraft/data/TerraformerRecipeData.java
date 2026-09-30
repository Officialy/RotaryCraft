package reika.rotarycraft.data;

import java.util.List;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.recipemanagers.TerraformingRecipe;

/** All 36 V33a steps, transcribed from TileEntityTerraformer; chances retain ItemReq semantics. */
final class TerraformerRecipeData {
    private TerraformerRecipeData() {}
    static void bootstrap(BootstrapContext<Recipe<?>> context) {
        step(context, Biomes.DESERT, Biomes.SAVANNA, 65536, 30, Blocks.SAND, Blocks.SAND, Blocks.GRASS_BLOCK, Blocks.DIRT, 35, cost(Items.SHORT_GRASS, 0.5F), cost(Items.ACACIA_SAPLING, 0.05F));
        step(context, Biomes.SAVANNA, Biomes.PLAINS, 32768, 20, Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.DIRT, 1, cost(Items.SHORT_GRASS, 0.3F));
        step(context, Biomes.PLAINS, Biomes.FOREST, 131072, 10, Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.DIRT, 4, cost(Items.OAK_SAPLING, 0.5F), cost(Items.BIRCH_SAPLING, 0.2F));
        step(context, Biomes.FOREST, Biomes.JUNGLE, 262144, 50, Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.DIRT, 21, cost(Items.OAK_SAPLING, 0.4F), cost(Items.OAK_SAPLING, 0.6F), cost(Items.FERN, 0.3F));
        step(context, Biomes.PLAINS, Biomes.SWAMP, 32768, 100, Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.DIRT, 6, cost(Items.OAK_SAPLING, 0.1F), cost(Items.RED_MUSHROOM, 0.05F), cost(Items.BROWN_MUSHROOM, 0.15F));
        step(context, Biomes.SWAMP, Biomes.OCEAN, 131072, 500, Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.DIRT, 0);
        step(context, Biomes.OCEAN, Biomes.FROZEN_OCEAN, 1024, 0, Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.DIRT, 10, cost(Items.ICE, 1));
        step(context, Biomes.PLAINS, Biomes.WINDSWEPT_HILLS, 65536, 0, Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.DIRT, 3, cost(Items.OAK_SAPLING, 0.05F));
        step(context, Biomes.PLAINS, Biomes.SNOWY_PLAINS, 8192, 0, Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.DIRT, 12, cost(Items.SNOW_BLOCK, 1), cost(Items.OAK_SAPLING, 0.05F));
        step(context, Biomes.SNOWY_PLAINS, Biomes.PLAINS, 524288, 0, Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.DIRT, 1, cost(Items.SHORT_GRASS, 0.7F));
        step(context, Biomes.OCEAN, Biomes.MUSHROOM_FIELDS, 1048576, 0, Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.MYCELIUM, Blocks.DIRT, 14, cost(Items.DIRT, 1), cost(Items.MYCELIUM, 1), cost(Items.RED_MUSHROOM, 0.9F), cost(Items.BROWN_MUSHROOM, 0.9F));
        step(context, Biomes.MUSHROOM_FIELDS, Biomes.WINDSWEPT_HILLS, 262144, 0, Blocks.MYCELIUM, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.DIRT, 3, cost(Items.GRASS_BLOCK, 0.125F), cost(Items.OAK_SAPLING, 0.05F), cost(Items.SHORT_GRASS, 0.25F));
        step(context, Biomes.FOREST, Biomes.TAIGA, 131072, 0, Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.DIRT, 5, cost(Items.SPRUCE_SAPLING, 0.25F));
        step(context, Biomes.FOREST, Biomes.SNOWY_TAIGA, 131072, 0, Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.DIRT, 30, cost(Items.SNOW_BLOCK, 0.3F), cost(Items.SPRUCE_SAPLING, 0.25F));
        step(context, Biomes.FOREST, Biomes.DARK_FOREST, 65536, 40, Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.DIRT, 29, cost(Items.DARK_OAK_SAPLING, 0.5F));
        step(context, Biomes.FOREST, Biomes.BIRCH_FOREST, 32768, 0, Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.DIRT, 27, cost(Items.BIRCH_SAPLING, 0.25F));
        step(context, Biomes.TAIGA, Biomes.SNOWY_TAIGA, 32768, 0, Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.DIRT, 30, cost(Items.SNOW_BLOCK, 0.3F));
        step(context, Biomes.TAIGA, Biomes.SNOWY_PLAINS, 65536, 0, Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.DIRT, 12, cost(Items.SNOW_BLOCK, 1), cost(Items.OAK_SAPLING, 0.05F));
        step(context, Biomes.TAIGA, Biomes.FOREST, 131072, 0, Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.DIRT, 4, cost(Items.OAK_SAPLING, 0.4F), cost(Items.BIRCH_SAPLING, 0.1F));
        step(context, Biomes.TAIGA, Biomes.OLD_GROWTH_PINE_TAIGA, 32768, 20, Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.DIRT, 32, cost(Items.SPRUCE_SAPLING, 0.1F));
        step(context, Biomes.SNOWY_PLAINS, Biomes.FROZEN_OCEAN, 32768, 100, Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.DIRT, 10, cost(Items.ICE, 1));
        step(context, Biomes.PLAINS, Biomes.SAVANNA, 65536, 0, Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.DIRT, 35, cost(Items.ACACIA_SAPLING, 0.05F));
        step(context, Biomes.SAVANNA, Biomes.DESERT, 65536, 0, Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.SAND, Blocks.SAND, 2, cost(Items.SAND, 1), cost(Items.SANDSTONE, 0.5F), cost(Items.CACTUS, 0.1F));
        step(context, Biomes.FOREST, Biomes.PLAINS, 262144, 0, Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.DIRT, 1, cost(Items.SHORT_GRASS, 0.8F));
        step(context, Biomes.JUNGLE, Biomes.FOREST, 65536, 0, Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.DIRT, 4, cost(Items.OAK_SAPLING, 0.5F), cost(Items.BIRCH_SAPLING, 0.2F));
        step(context, Biomes.SWAMP, Biomes.PLAINS, 262144, 0, Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.DIRT, 1, cost(Items.SHORT_GRASS, 0.8F), cost(Items.DIRT, 0.8F));
        step(context, Biomes.OCEAN, Biomes.SWAMP, 524288, 0, Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.DIRT, 6, cost(Items.OAK_SAPLING, 0.1F), cost(Items.RED_MUSHROOM, 0.05F), cost(Items.BROWN_MUSHROOM, 0.15F), cost(Items.GRASS_BLOCK, 0.125F));
        step(context, Biomes.FROZEN_OCEAN, Biomes.OCEAN, 524288, 0, Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.DIRT, 0);
        step(context, Biomes.WINDSWEPT_HILLS, Biomes.PLAINS, 262144, 0, Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.DIRT, 1, cost(Items.SHORT_GRASS, 0.6F));
        step(context, Biomes.SNOWY_PLAINS, Biomes.TAIGA, 65536, 0, Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.DIRT, 5, cost(Items.SPRUCE_SAPLING, 0.4F));
        step(context, Biomes.FROZEN_OCEAN, Biomes.SNOWY_PLAINS, 65536, 0, Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.DIRT, 12, cost(Items.OAK_SAPLING, 0.05F), cost(Items.DIRT, 1), cost(Items.GRASS_BLOCK, 0.125F));
        step(context, Biomes.DESERT, Biomes.BADLANDS, 32768, 0, Blocks.SAND, Blocks.SAND, Blocks.RED_SAND, Blocks.TERRACOTTA, 37, cost(Items.CLAY, 0.2F));
        step(context, Biomes.OCEAN, Biomes.DEEP_OCEAN, 1024, 200, Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.DIRT, 24);
        step(context, Biomes.BADLANDS, Biomes.DESERT, 16384, 0, Blocks.RED_SAND, Blocks.TERRACOTTA, Blocks.SAND, Blocks.SAND, 2, cost(Items.SAND, 0.5F), cost(Items.SANDSTONE, 0.1F));
        step(context, Biomes.DARK_FOREST, Biomes.FOREST, 32768, 0, Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.DIRT, 4, cost(Items.OAK_SAPLING, 0.5F), cost(Items.BIRCH_SAPLING, 0.2F));
        step(context, Biomes.BIRCH_FOREST, Biomes.FOREST, 32768, 0, Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.DIRT, 4, cost(Items.OAK_SAPLING, 0.5F));
    }
    private static TerraformingRecipe.ItemCost cost(Item item, float chance) { return new TerraformingRecipe.ItemCost(Ingredient.of(item), chance); }
    private static void step(BootstrapContext<Recipe<?>> context, ResourceKey<Biome> source, ResourceKey<Biome> target,
            int power, int water, Block sourceTop, Block sourceFiller, Block top, Block filler, int icon, TerraformingRecipe.ItemCost... costs) {
        var tag = TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "terraformer/" + source.identifier().getPath()));
        context.register(ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(RotaryCraft.MODID,
                "terraformer/" + source.identifier().getPath() + "_to_" + target.identifier().getPath())),
                new TerraformingRecipe(tag, target, power, water, List.of(costs), sourceTop, sourceFiller, top, filler, icon));
    }
}
