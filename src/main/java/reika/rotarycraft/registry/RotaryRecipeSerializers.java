package reika.rotarycraft.registry;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.recipemanagers.CentrifugeRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.CompactorRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.CrystallizerRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.DryingBedRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.WetterRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.LavaMakerRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.ExtractorRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.FermenterRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.FrictionHeaterRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.GrinderRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.PulseFurnaceRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.ShapedBlastFurnaceRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.ShapelessBlastFurnaceRecipe;

public class RotaryRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, RotaryCraft.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<GrinderRecipe>> GRINDER =
            RECIPE_SERIALIZERS.register("grinder",
                    () -> new RecipeSerializer<>(GrinderRecipe.CODEC, GrinderRecipe.STREAM_CODEC));

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CentrifugeRecipe>> CENTRIFUGE =
            RECIPE_SERIALIZERS.register("centrifuge",
                    () -> new RecipeSerializer<>(CentrifugeRecipe.CODEC, CentrifugeRecipe.STREAM_CODEC));

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<LavaMakerRecipe>> LAVA_MAKER =
            RECIPE_SERIALIZERS.register("lava_maker",
                    () -> new RecipeSerializer<>(LavaMakerRecipe.CODEC, LavaMakerRecipe.STREAM_CODEC));

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CompactorRecipe>> COMPACTOR =
            RECIPE_SERIALIZERS.register("compactor",
                    () -> new RecipeSerializer<>(CompactorRecipe.CODEC, CompactorRecipe.STREAM_CODEC));

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<WetterRecipe>> WETTER =
            RECIPE_SERIALIZERS.register("wetter",
                    () -> new RecipeSerializer<>(WetterRecipe.CODEC, WetterRecipe.STREAM_CODEC));

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<DryingBedRecipe>> DRYING_BED =
            RECIPE_SERIALIZERS.register("drying_bed",
                    () -> new RecipeSerializer<>(DryingBedRecipe.CODEC, DryingBedRecipe.STREAM_CODEC));

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CrystallizerRecipe>> CRYSTALLIZER =
            RECIPE_SERIALIZERS.register("crystallizer",
                    () -> new RecipeSerializer<>(CrystallizerRecipe.CODEC, CrystallizerRecipe.STREAM_CODEC));

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FrictionHeaterRecipe>> FRICTION_HEATER =
            RECIPE_SERIALIZERS.register("friction_heater",
                    () -> new RecipeSerializer<>(FrictionHeaterRecipe.CODEC, FrictionHeaterRecipe.STREAM_CODEC));

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ExtractorRecipe>> EXTRACTOR =
            RECIPE_SERIALIZERS.register("extractor",
                    () -> new RecipeSerializer<>(ExtractorRecipe.CODEC, ExtractorRecipe.STREAM_CODEC));

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FermenterRecipe>> FERMENTER =
            RECIPE_SERIALIZERS.register("fermenter",
                    () -> new RecipeSerializer<>(FermenterRecipe.CODEC, FermenterRecipe.STREAM_CODEC));

    // 1.21.5: RecipeSerializer is now a record(MapCodec, StreamCodec); inner Serializer classes are gone.
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ShapelessBlastFurnaceRecipe>> BLAST_FURNACE_SHAPELESS =
            RECIPE_SERIALIZERS.register("blast_furnace_shapeless",
                    () -> new RecipeSerializer<>(ShapelessBlastFurnaceRecipe.CODEC, ShapelessBlastFurnaceRecipe.STREAM_CODEC));

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ShapedBlastFurnaceRecipe>> BLAST_FURNACE_SHAPED =
            RECIPE_SERIALIZERS.register("blast_furnace_shaped",
                    () -> new RecipeSerializer<>(ShapedBlastFurnaceRecipe.CODEC, ShapedBlastFurnaceRecipe.STREAM_CODEC));

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<PulseFurnaceRecipe>> PULSE_FURNACE =
            RECIPE_SERIALIZERS.register("pulse_furnace",
                    () -> new RecipeSerializer<>(PulseFurnaceRecipe.CODEC, PulseFurnaceRecipe.STREAM_CODEC));
}