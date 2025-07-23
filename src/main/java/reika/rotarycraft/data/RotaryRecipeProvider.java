package reika.rotarycraft.data;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import reika.rotarycraft.registry.RotaryItems;

import java.util.function.Consumer;

public class RotaryRecipeProvider extends RecipeProvider {
    public RotaryRecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        new ShapelessBlastFurnaceRecipeBuilder(RotaryItems.HSLA_STEEL_INGOT.get())
                .addIngredient(Ingredient.of(Items.IRON_INGOT))
                .addAdditive(Ingredient.of(Blocks.SAND.asItem()))
                .addAdditive(Ingredient.of(Items.GUNPOWDER))
                .addAdditive(Ingredient.of(RotaryItems.COKE.get()))
                .setTemperature(600)
                .setExperience(1.5f)
                .setTimeMultiplier(2.0f)
                .setBonus(10,1, 9) //todo work out bonus chance and max range
                .build(consumer);

    }
}