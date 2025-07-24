package reika.rotarycraft.registry;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.registries.DeferredRegister;
import net.neoforged.registries.ForgeRegistries;
import net.neoforged.registries.RegistryObject;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.recipemanagers.ShapedBlastFurnaceRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.ShapelessBlastFurnaceRecipe;

public class RotaryRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, RotaryCraft.MODID);

    public static final RegistryObject<RecipeSerializer<ShapelessBlastFurnaceRecipe>> BLAST_FURNACE_SHAPELESS = RECIPE_SERIALIZERS.register("blast_furnace_shapeless", ShapelessBlastFurnaceRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<ShapedBlastFurnaceRecipe>> BLAST_FURNACE_SHAPED = RECIPE_SERIALIZERS.register("blast_furnace_shaped", ShapedBlastFurnaceRecipe.Serializer::new);
}