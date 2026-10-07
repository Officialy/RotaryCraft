package reika.rotarycraft.data;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.data.DataMapProvider;
import reika.rotarycraft.registry.PileDriverRules;
import reika.rotarycraft.registry.PileDriverRules.Rule;
import reika.rotarycraft.registry.RotaryBlocks;

/** Original TileEntityPileDriver tables, generated into NeoForge block data maps. */
public final class PileDriverDataProvider extends DataMapProvider {
    public PileDriverDataProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) { super(output, lookup); }
    @Override protected void gather(HolderLookup.Provider provider) {
        var dimensions = builder(PileDriverRules.SPAWNER_DIMENSIONS);
        dimensions.add(net.minecraft.world.entity.EntityTypes.ENDER_DRAGON.builtInRegistryHolder(), Identifier.fromNamespaceAndPath("minecraft", "the_end"), false);
        for (String boss : new String[]{"ur_ghast", "hydra", "naga"}) dimensions.add(Identifier.fromNamespaceAndPath("twilightforest", boss),
                Identifier.fromNamespaceAndPath("twilightforest", "twilight_forest"), false, new ModLoadedCondition("twilightforest"));
        var map = builder(PileDriverRules.IMPACT);
        map.add(Blocks.OBSIDIAN.builtInRegistryHolder(), Rule.hits(5), false);
        map.add(Blocks.NETHERRACK.builtInRegistryHolder(), Rule.hits(-2), false);
        map.add(Blocks.GLASS.builtInRegistryHolder(), Rule.hits(-4), false);
        map.add(Blocks.GLOWSTONE.builtInRegistryHolder(), Rule.hits(-3), false);
        map.add(BlockTags.WOOL, Rule.hits(-1), false);
        map.add(Blocks.BEDROCK.builtInRegistryHolder(), Rule.keepState(), false);
        map.add(RotaryBlocks.SHIELD, Rule.keepState(), false);
        map.add(RotaryBlocks.MININGPIPE, new Rule(0, true, Optional.empty(), Map.of("shape", "junction")), false);
        map.add(Blocks.STONE.builtInRegistryHolder(), Rule.product(0, BuiltInRegistries.BLOCK.getKey(Blocks.COBBLESTONE)), false);
        map.add(Blocks.STONE_BRICKS.builtInRegistryHolder(), Rule.product(0, BuiltInRegistries.BLOCK.getKey(Blocks.CRACKED_STONE_BRICKS)), false);
        // V33a RockTypes x RockShapes. Connected shapes have their modern GeoStrata suffix.
        String[] rocks = {"granite", "basalt", "marble", "limestone", "shale", "sandstone", "pumice", "slate", "gneiss", "peridotite", "quartz", "granulite", "hornfel", "migmatite", "schist", "onyx", "opal"};
        String[] shapes = {"smooth", "cobble", "brick", "round", "fitted", "tile", "engraved", "inscribed", "cubed", "lined", "embossed", "centered", "raised", "etched", "spiral", "fan", "mossy", "connected", "connected2", "pillar"};
        for (String rock : rocks) for (String shape : shapes) {
            int hits = switch (rock) { case "granite", "hornfel" -> 3; case "peridotite", "gneiss", "schist" -> 2; case "shale", "limestone" -> -1; default -> 0; };
            Rule rule = shape.equals("cobble") ? Rule.hits(hits) : Rule.product(hits, Identifier.fromNamespaceAndPath("geostrata", rock + "_cobble"));
            String suffix = shape.equals("connected") || shape.equals("connected2") ? "_connected" : "";
            map.add(Identifier.fromNamespaceAndPath("geostrata", rock + "_" + shape + suffix), rule, false, new ModLoadedCondition("geostrata"));
        }
    }
}
