package reika.rotarycraft.registry;

import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.recipemanagers.FrictionHeaterRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.GrinderRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.ShapedBlastFurnaceRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.ShapelessBlastFurnaceRecipe;

public class RotaryRecipeTypes {

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, RotaryCraft.MODID);


    public static final DeferredHolder<RecipeType<?>, RecipeType<ShapelessBlastFurnaceRecipe>> BLAST_FURNACE_SHAPELESS = RECIPE_TYPES.register("blast_furnace_shapeless", () -> new RecipeType<ShapelessBlastFurnaceRecipe>() {});

    public static final DeferredHolder<RecipeType<?>, RecipeType<ShapedBlastFurnaceRecipe>> BLAST_FURNACE_SHAPED = RECIPE_TYPES.register("blast_furnace_shaped", () -> new RecipeType<ShapedBlastFurnaceRecipe>() {});

    public static final DeferredHolder<RecipeType<?>, RecipeType<GrinderRecipe>> GRINDER = RECIPE_TYPES.register("grinder", () -> new RecipeType<GrinderRecipe>() {});

    public static final DeferredHolder<RecipeType<?>, RecipeType<FrictionHeaterRecipe>> FRICTION_HEATER = RECIPE_TYPES.register("friction_heater", () -> new RecipeType<FrictionHeaterRecipe>() {});
}
