package reika.rotarycraft.registry;

import java.util.Map;
import java.util.Optional;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

/** Reloadable V33a impact conversions and hit counts, including optional rock integrations. */
public final class PileDriverRules {
    private PileDriverRules() {}
    public record Rule(int hits, boolean keep, Optional<Identifier> product, Map<String, String> properties) {
        public static final Codec<Rule> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.INT.optionalFieldOf("hits", 0).forGetter(Rule::hits),
                Codec.BOOL.optionalFieldOf("keep", false).forGetter(Rule::keep),
                Identifier.CODEC.optionalFieldOf("product").forGetter(Rule::product),
                Codec.unboundedMap(Codec.STRING, Codec.STRING).optionalFieldOf("properties", Map.of()).forGetter(Rule::properties)
        ).apply(i, Rule::new));
        public BlockState result(BlockState state) {
            for (var entry : properties.entrySet()) {
                var property = state.getBlock().getStateDefinition().getProperty(entry.getKey());
                if (property == null || !value(state, property).equals(entry.getValue())) return Blocks.AIR.defaultBlockState();
            }
            if (keep) return state;
            return product.map(id -> BuiltInRegistries.BLOCK.getOptional(id).orElseThrow(
                    () -> new IllegalStateException("Missing pile driver product " + id)).defaultBlockState()).orElse(Blocks.AIR.defaultBlockState());
        }
        private static <T extends Comparable<T>> String value(BlockState state, Property<T> property) { return property.getName(state.getValue(property)); }
        public static Rule hits(int hits) { return new Rule(hits, false, Optional.empty(), Map.of()); }
        public static Rule keepState() { return new Rule(0, true, Optional.empty(), Map.of()); }
        public static Rule product(int hits, Identifier product) { return new Rule(hits, false, Optional.of(product), Map.of()); }
    }
    public static final DataMapType<Block, Rule> IMPACT = DataMapType.builder(
            Identifier.fromNamespaceAndPath("rotarycraft", "pile_driver_impact"), Registries.BLOCK, Rule.CODEC).build();
    public static final DataMapType<net.minecraft.world.entity.EntityType<?>, Identifier> SPAWNER_DIMENSIONS = DataMapType.builder(
            Identifier.fromNamespaceAndPath("rotarycraft", "spawner_dimensions"), Registries.ENTITY_TYPE, Identifier.CODEC).synced(Identifier.CODEC, false).build();
    public static void register(RegisterDataMapTypesEvent event) { event.register(IMPACT); event.register(SPAWNER_DIMENSIONS); }
    public static Rule get(BlockState state) { return state.getBlock().builtInRegistryHolder().getData(IMPACT); }
    public static int hits(BlockState state) { Rule rule = get(state); return rule == null ? 0 : rule.hits(); }
    public static BlockState product(BlockState state) {
        if (state.getBlock() instanceof net.minecraft.world.level.block.LiquidBlock) return state;
        Rule rule = get(state); return rule == null ? Blocks.AIR.defaultBlockState() : rule.result(state);
    }
}
