package reika.rotarycraft.data;

import com.google.common.collect.Maps;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ShapedBlastFurnaceRecipeBuilder extends BlastFurnaceRecipeBuilderBase {
    private final List<String> rows;
    private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();

    public ShapedBlastFurnaceRecipeBuilder(Item result, int count) {
        super(result, count);
        this.rows = new ArrayList<>();
    }

    public ShapedBlastFurnaceRecipeBuilder define(Character c, Ingredient ingredient) {
        if (this.key.containsKey(c)) {
            throw new IllegalArgumentException("Symbol '" + c + "' is already defined!");
        } else if (c == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        } else {
            this.key.put(c, ingredient);
            return this;
        }
    }

    public ShapedBlastFurnaceRecipeBuilder pattern(String pattern) {
        if (!this.rows.isEmpty() && pattern.length() != this.rows.get(0).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        } else {
            this.rows.add(pattern);
            return this;
        }
    }

    @Override
    public void build(Consumer<FinishedRecipe> consumer) {
        if (rows.isEmpty())
            throw new IllegalStateException("No pattern defined for shaped Blast-Furnace recipe!");
        if (key.isEmpty())
            throw new IllegalStateException("No keys defined for shaped Blast-Furnace recipe!");

        consumer.accept(new Result(this));
    }

    /* --------------------------------------------------------------------- */
    /*  Inner record holding the finished recipe                             */
    /* --------------------------------------------------------------------- */
    private static final class Result implements FinishedRecipe {

        private final ShapedBlastFurnaceRecipeBuilder b;

        private Result(ShapedBlastFurnaceRecipeBuilder builder) {
            this.b = builder;
        }

        /* ----------------  json ---------------- */

        @Override
        public void serializeRecipeData(com.google.gson.JsonObject json) {
            // pattern
            com.google.gson.JsonArray pattern = new com.google.gson.JsonArray();
            for (String row : b.rows)
                pattern.add(row);
            json.add("pattern", pattern);

            // key
            com.google.gson.JsonObject keyObj = new com.google.gson.JsonObject();
            for (Map.Entry<Character, Ingredient> e : b.key.entrySet())
                keyObj.add(String.valueOf(e.getKey()), e.getValue().toJson());
            json.add("key", keyObj);

            // additives
            com.google.gson.JsonArray adds = new com.google.gson.JsonArray();
            for (Ingredient in : b.additives)
                adds.add(in.toJson());
            json.add("additives", adds);

            // result
            com.google.gson.JsonObject result = new com.google.gson.JsonObject();
            result.addProperty("item",
                    net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(b.result).toString());
            if (b.count > 1)
                result.addProperty("count", b.count);
            json.add("output", result);

            // blast-furnace metadata
            json.addProperty("temperature",  b.temperature);
            json.addProperty("experience",   b.experience);
            json.addProperty("timeMultiplier", b.timeMultiplier);
            json.addProperty("bonusChance",  b.bonusChance);   // % chance
            json.addProperty("bonusMin",     b.bonusMin);
            json.addProperty("bonusMax",     b.bonusMax);
        }

        /* ---------------- identifiers & serializer ---------------- */

        @Override
        public net.minecraft.resources.ResourceLocation getId() {
            return net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(
                    reika.rotarycraft.RotaryCraft.MODID,
                    "blast_furnace/" +
                            net.minecraftforge.registries.ForgeRegistries.ITEMS
                                    .getKey(b.result).getPath() +
                            "_shaped");
        }

        @Override
        public net.minecraft.world.item.crafting.RecipeSerializer<?> getType() {
            return reika.rotarycraft.registry.RotaryRecipeSerializers
                    .BLAST_FURNACE_SHAPED.get();
        }

        /* ---------------- advancements (none) ---------------- */
        @Override
        public com.google.gson.JsonObject serializeAdvancement() { return null; }

        @Override
        public net.minecraft.resources.ResourceLocation getAdvancementId() { return null; }
    }

}