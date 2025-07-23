package reika.rotarycraft.data;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class BlastFurnaceRecipeBuilderBase {
    protected final Item result;
    protected final int count;
    protected final List<Ingredient> ingredients = new ArrayList<>();
    protected final List<Ingredient> additives = new ArrayList<>();
    protected float temperature;
    protected float experience;
    protected float timeMultiplier;
    protected int bonusChance;   // % probability, 0-100
    protected int bonusMin = 0;  // inclusive
    protected int bonusMax = 0;  // inclusive

    public BlastFurnaceRecipeBuilderBase(Item result, int count) {
        this.result = result;
        this.count = count;
    }

    public BlastFurnaceRecipeBuilderBase addAdditive(Ingredient ingredient) {
        additives.add(ingredient);
        return this;
    }

    public BlastFurnaceRecipeBuilderBase setTemperature(float temperature) {
        this.temperature = temperature;
        return this;
    }

    public BlastFurnaceRecipeBuilderBase setExperience(float experience) {
        this.experience = experience;
        return this;
    }

    public BlastFurnaceRecipeBuilderBase setTimeMultiplier(float timeMultiplier) {
        this.timeMultiplier = timeMultiplier;
        return this;
    }

    public BlastFurnaceRecipeBuilderBase setBonus(int chance, int min, int max) {
        this.bonusChance = chance;
        this.bonusMin    = min;
        this.bonusMax    = max;
        return this;
    }

    public abstract void build(Consumer<FinishedRecipe> consumer);
}