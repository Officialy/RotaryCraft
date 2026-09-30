package reika.rotarycraft.data;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import reika.rotarycraft.RotaryCraft;

/** Legacy parent/hill/mutated families expressed as extensible biome tags using surviving 26.3 equivalents. */
public class TerraformerBiomeTagsProvider extends BiomeTagsProvider {
    public TerraformerBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) { super(output, lookup, RotaryCraft.MODID); }
    @Override protected void addTags(HolderLookup.Provider lookup) {
        family(Biomes.DESERT, Biomes.DESERT);
        family(Biomes.SAVANNA, Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.WINDSWEPT_SAVANNA);
        family(Biomes.PLAINS, Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS);
        family(Biomes.FOREST, Biomes.FOREST, Biomes.FLOWER_FOREST);
        family(Biomes.JUNGLE, Biomes.JUNGLE, Biomes.SPARSE_JUNGLE, Biomes.BAMBOO_JUNGLE);
        family(Biomes.SWAMP, Biomes.SWAMP);
        family(Biomes.OCEAN, Biomes.OCEAN);
        family(Biomes.FROZEN_OCEAN, Biomes.FROZEN_OCEAN);
        family(Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_GRAVELLY_HILLS);
        family(Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS, Biomes.ICE_SPIKES);
        family(Biomes.MUSHROOM_FIELDS, Biomes.MUSHROOM_FIELDS);
        family(Biomes.TAIGA, Biomes.TAIGA);
        family(Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA);
        family(Biomes.DARK_FOREST, Biomes.DARK_FOREST);
        family(Biomes.BIRCH_FOREST, Biomes.BIRCH_FOREST, Biomes.OLD_GROWTH_BIRCH_FOREST);
        family(Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA);
        family(Biomes.BADLANDS, Biomes.BADLANDS, Biomes.WOODED_BADLANDS, Biomes.ERODED_BADLANDS);
        family(Biomes.DEEP_OCEAN, Biomes.DEEP_OCEAN);
    }
    @SafeVarargs private void family(ResourceKey<Biome> base, ResourceKey<Biome>... biomes) {
        tag(TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "terraformer/" + base.identifier().getPath()))).add(biomes);
    }
}
