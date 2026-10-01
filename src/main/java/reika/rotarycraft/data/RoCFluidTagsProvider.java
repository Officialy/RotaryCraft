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
    public static final TagKey<Fluid> ROCKET_FUEL = common("rocket_fuel");
    public static final TagKey<Fluid> FUEL = common("fuel");
    public static final TagKey<Fluid> TURBOFUEL = common("turbofuel");
    public static final TagKey<Fluid> KEROSENE = common("kerosene");
    public static final TagKey<Fluid> OIL = common("oil");
    public static final TagKey<Fluid> BIOETHANOL = common("bioethanol");
    public static final TagKey<Fluid> BIOFUEL = common("biofuel");
    private static TagKey<Fluid> common(String name) { return TagKey.create(Registries.FLUID, Identifier.fromNamespaceAndPath("c", name)); }
    public RoCFluidTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) { super(output, lookup, RotaryCraft.MODID); }
    private static net.minecraft.resources.ResourceKey<Fluid> key(String namespace, String name) { return net.minecraft.resources.ResourceKey.create(Registries.FLUID, Identifier.fromNamespaceAndPath(namespace, name)); }
    @Override protected void addTags(HolderLookup.Provider provider) {
        tag(FUEL).addOptional(key("buildcraftenergy", "fuel")).addOptional(key("buildcraft", "fuel"));
        // SATISFORESTRY-PORT: its modern fluid registration opts into this tag; preserve the V33a turbofuel bonus.
        tag(TURBOFUEL);
        tag(ROCKET_FUEL); // Optional rocket-fuel integrations opt in through this tag.
        tag(KEROSENE); // Optional integrations supply their registered kerosene fluids through this common tag.
        tag(OIL).addOptional(key("buildcraftenergy", "oil")).addOptional(key("buildcraft", "oil"));
        tag(BIOETHANOL).addOptional(key("forestry", "bioethanol"));
        tag(BIOFUEL).addOptional(key("minefactoryreloaded", "biofuel")).addOptional(key("industrialforegoing", "biofuel"));
    }
}
