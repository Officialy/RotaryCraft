package reika.rotarycraft.auxiliary.recipemanagers;

import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import reika.rotarycraft.RotaryCraft;

public class RotaryRecipeTypes {

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, RotaryCraft.MODID);
    public static final RecipeType<SmeltingRecipe> RECYCLING = RecipeType.register("recycling");

    public static final RecipeType<SmeltingRecipe> WORKTABLE = RecipeType.register("worktable");
    public static final RecipeType<SmeltingRecipe> FRICTION = RecipeType.register("friction");


}
