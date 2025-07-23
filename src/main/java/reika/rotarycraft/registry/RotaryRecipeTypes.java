package reika.rotarycraft.registry;

import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.recipemanagers.ShapedBlastFurnaceRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.ShapelessBlastFurnaceRecipe;

public class RotaryRecipeTypes {

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, RotaryCraft.MODID);

    
    public static final RegistryObject<RecipeType<ShapelessBlastFurnaceRecipe>> BLAST_FURNACE_SHAPELESS = RECIPE_TYPES.register("blast_furnace_shapeless", () -> new RecipeType<ShapelessBlastFurnaceRecipe>() {});

    public static final RegistryObject<RecipeType<ShapedBlastFurnaceRecipe>> BLAST_FURNACE_SHAPED = RECIPE_TYPES.register("blast_furnace_shaped", () -> new RecipeType<ShapedBlastFurnaceRecipe>() {});
}
