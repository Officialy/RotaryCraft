package reika.rotarycraft.data;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.data.tags.FluidTagsProvider;
import reika.rotarycraft.RotaryCraft;

/** Common integration tags preserve V33a's oil/bioethanol/biofuel names without requiring those mods. */
public class RoCFluidTagsProvider extends FluidTagsProvider {
    public static final TagKey<Fluid> OIL = common("oil");
    public static final TagKey<Fluid> BIOETHANOL = common("bioethanol");
    public static final TagKey<Fluid> BIOFUEL = common("biofuel");
    private static TagKey<Fluid> common(String name) { return TagKey.create(Registries.FLUID, Identifier.fromNamespaceAndPath("c", name)); }
    public RoCFluidTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) { super(output, lookup, RotaryCraft.MODID); }
    private static net.minecraft.resources.ResourceKey<Fluid> key(String namespace, String name) { return net.minecraft.resources.ResourceKey.create(Registries.FLUID, Identifier.fromNamespaceAndPath(namespace, name)); }
    @Override protected void addTags(HolderLookup.Provider provider) {
        tag(OIL).addOptional(key("buildcraftenergy", "oil")).addOptional(key("buildcraft", "oil"));
        tag(BIOETHANOL).addOptional(key("forestry", "bioethanol"));
        tag(BIOFUEL).addOptional(key("minefactoryreloaded", "biofuel")).addOptional(key("industrialforegoing", "biofuel"));
    }
}
