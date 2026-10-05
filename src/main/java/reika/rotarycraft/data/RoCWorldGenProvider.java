package reika.rotarycraft.data;

import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.registries.DeferredRegister;
import reika.rotarycraft.ExtraIronGenerator;

public final class RoCWorldGenProvider implements DataProvider {
    public static final DeferredRegister<MapCodec<? extends Feature>> TYPES=DeferredRegister.create(Registries.FEATURE_TYPE,"rotarycraft");
    static { TYPES.register("extra_iron", () -> ExtraIronGenerator.CODEC); }
    public static final Identifier ID=Identifier.fromNamespaceAndPath("rotarycraft","extra_iron");
    private final PackOutput.PathProvider paths;
    public RoCWorldGenProvider(PackOutput output) { paths=output.createPathProvider(PackOutput.Target.DATA_PACK,"neoforge/biome_modifier"); }
    public static RegistrySetBuilder buildRegistrySet() {
        return new RegistrySetBuilder().add(Registries.FEATURE,c -> c.register(ResourceKey.create(Registries.FEATURE,ID),new ExtraIronGenerator(10,10,4,67)))
                .add(Registries.PLACED_FEATURE,c -> c.register(ResourceKey.create(Registries.PLACED_FEATURE,ID),new PlacedFeature(c.lookup(Registries.FEATURE).getOrThrow(ResourceKey.create(Registries.FEATURE,ID)),List.of())));
    }
    @Override public CompletableFuture<?> run(CachedOutput cache) {
        JsonObject json=new JsonObject();json.addProperty("type","neoforge:add_features");json.addProperty("biomes","#minecraft:is_overworld");
        json.addProperty("features",ID.toString());json.addProperty("step","underground_ores");
        return DataProvider.saveStable(cache,json,paths.json(ID));
    }
    @Override public String getName() { return "RotaryCraft extra iron biome modifier"; }
}
