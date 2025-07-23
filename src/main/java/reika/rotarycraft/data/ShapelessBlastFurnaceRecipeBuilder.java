package reika.rotarycraft.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.registry.RotaryRecipeSerializers;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class ShapelessBlastFurnaceRecipeBuilder extends BlastFurnaceRecipeBuilderBase {
    public ShapelessBlastFurnaceRecipeBuilder(Item result, int count) {
        super(result, count);
    }

    public ShapelessBlastFurnaceRecipeBuilder(Item result) {
        super(result, 1);
    }

    public ShapelessBlastFurnaceRecipeBuilder addIngredient(Ingredient ingredient, int quantity) {
        for (int i = 0; i < quantity; i++) {
            ingredients.add(ingredient);
        }
        return this;
    }

    public ShapelessBlastFurnaceRecipeBuilder addIngredient(Ingredient ingredient) {
        return this.addIngredient(ingredient, 1);
    }

    @Override
    public void build(Consumer<FinishedRecipe> consumer) {
        consumer.accept(new Result(ResourceLocation.fromNamespaceAndPath(RotaryCraft.MODID, "blast_furnace/" + ForgeRegistries.ITEMS.getKey(result).getPath()), this));
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final ShapelessBlastFurnaceRecipeBuilder builder;

        public Result(ResourceLocation id, ShapelessBlastFurnaceRecipeBuilder builder) {
            this.id = id;
            this.builder = builder;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            JsonArray ingredients = new JsonArray();
            for (Ingredient ingredient : builder.ingredients) {
                ingredients.add(ingredient.toJson());
            }
            json.add("ingredients", ingredients);

            JsonArray additives = new JsonArray();
            for (Ingredient additive : builder.additives) {
                additives.add(additive.toJson());
            }
            json.add("additives", additives);

            JsonObject result = new JsonObject();
            result.addProperty("item", ForgeRegistries.ITEMS.getKey(builder.result).toString());
            if (builder.count > 1) {
                result.addProperty("count", builder.count);
            }
            json.add("output", result);

            json.addProperty("temperature", builder.temperature);
            json.addProperty("experience", builder.experience);
            json.addProperty("timeMultiplier", builder.timeMultiplier);
            json.addProperty("bonusChance", builder.bonusChance);
            json.addProperty("bonusMin",   builder.bonusMin);
            json.addProperty("bonusMax",   builder.bonusMax);
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return RotaryRecipeSerializers.BLAST_FURNACE_SHAPELESS.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}