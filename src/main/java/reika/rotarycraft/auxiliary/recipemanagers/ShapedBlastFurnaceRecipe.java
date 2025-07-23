package reika.rotarycraft.auxiliary.recipemanagers;


import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import reika.dragonapi.interfaces.IBonusYield;
import reika.dragonapi.interfaces.IHasXP;
import reika.dragonapi.interfaces.IHeatRecipe;
import reika.rotarycraft.registry.RotaryRecipeSerializers;
import reika.rotarycraft.registry.RotaryRecipeTypes;

import java.util.Map;

public class ShapedBlastFurnaceRecipe implements Recipe<SimpleContainer>, IBonusYield, IHasXP, IHeatRecipe {
    private final ResourceLocation id;
    private final NonNullList<Ingredient> ingredients;
    private final ItemStack output;
    //The temperature required to craft
    private final float operatingTemperature;
    //The experience points you get from crafting
    private final float experience;
    //The higher the tier, the longer it takes to craft
    private final float timeMultiplier;
    private boolean needsAdditives = false;
    private final int bonusChance;
    private final int bonusMin;
    private final int bonusMax;

    public ShapedBlastFurnaceRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, ItemStack output, float temperature, float experience, float timeMultiplier, boolean needsAdditives, int chance, int min, int max) {
        this.id = id;
        this.ingredients = ingredients;
        this.output = output;
        this.operatingTemperature = temperature;
        this.experience = experience;
        this.timeMultiplier = timeMultiplier;
        this.needsAdditives = needsAdditives;
        this.bonusChance = chance;
        this.bonusMin    = min;
        this.bonusMax    = max;
    }

    public float getOperatingTemperature() {
        return operatingTemperature;
    }

    @Override
    public boolean matches(SimpleContainer inv, Level world) {
        // Check if all additives match - Ignores if needsAdditives is false
        boolean satisfied = !needsAdditives;

        for (int i = 0; i < 3; i++) {
            ItemStack additiveStack = inv.getItem(8 + i); // last three slots
            if (!additiveStack.isEmpty() && i < this.ingredients.size() && this.ingredients.get(i).test(additiveStack)) {
                satisfied = true;
            }
        }

        // Check if all recipe items match
        if (satisfied) {
            for (int i = 0; i < 9; i++) {
                ItemStack recipeStack = inv.getItem(i); // first nine slots
                if (!recipeStack.isEmpty() && i < this.ingredients.size() && this.ingredients.get(i).test(recipeStack)) {
                    continue;
                }
                return false;
            }
            return true;
        }

        return false;
    }

    /* IBonusYield */
    @Override public int bonusChance() { return bonusChance; }
    @Override public int bonusMin()    { return bonusMin; }
    @Override public int bonusMax()    { return bonusMax; }
    /* IHasXP */
    @Override public float xpPerItem() { return experience; }

    @Override public float requiredTemperature() { return operatingTemperature; }

    @Override
    public ItemStack assemble(SimpleContainer p_44001_, RegistryAccess p_267165_) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess p_267052_) {
        return output.copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }
   @Override
    public ResourceLocation getId() {
        return id;
    }
    public float getExperience() {
        return experience;
    }

    public float getTimeMultiplier() {
        return timeMultiplier;
    }

    public boolean needsAdditives() {
        return needsAdditives;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RotaryRecipeSerializers.BLAST_FURNACE_SHAPED.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RotaryRecipeTypes.BLAST_FURNACE_SHAPED.get();
    }

    public static class Serializer implements RecipeSerializer<ShapedBlastFurnaceRecipe> {

        @Override
        public ShapedBlastFurnaceRecipe fromJson(ResourceLocation id, JsonObject json) {
            Map<String, Ingredient> map = ShapedRecipe.keyFromJson(GsonHelper.getAsJsonObject(json, "key"));

            String[] pattern = ShapedRecipe.shrink(ShapedRecipe.patternFromJson(GsonHelper.getAsJsonArray(json, "pattern")));
            int i = pattern[0].length();
            int j = pattern.length;
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);
            for (int k = 0; k < i * j; ++k) {
                char c = pattern[k / i].charAt(k % i);
                Ingredient ingredient = map.get(c);
                if (ingredient == null) {
                    throw new JsonSyntaxException("Pattern references undefined symbol '" + c + "'");
                }
                nonnulllist.set(k, ingredient);
            }
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));

            float temperature = GsonHelper.getAsFloat(json, "temperature");
            float experience = GsonHelper.getAsFloat(json, "experience");
            float timeMultiplier = GsonHelper.getAsFloat(json, "timeMultiplier");
            boolean needsAdditives = GsonHelper.getAsBoolean(json, "needsAdditives");
            int bonusChance = GsonHelper.getAsInt(json, "bonusChance", 0);
            int bonusMin = GsonHelper.getAsInt(json, "bonusMin", 0);
            int bonusMax = GsonHelper.getAsInt(json, "bonusMax", 0);

            return new ShapedBlastFurnaceRecipe(id, nonnulllist, output, temperature, experience, timeMultiplier, needsAdditives, bonusChance, bonusMin, bonusMax);
        }

        @Override
        public ShapedBlastFurnaceRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf byteBuf) {
            int ingredientCount = byteBuf.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(ingredientCount, Ingredient.EMPTY);
            for (int i = 0; i < ingredientCount; ++i) {
                ingredients.set(i, Ingredient.fromNetwork(byteBuf));
            }

            ItemStack output = byteBuf.readItem();
            float temperature = byteBuf.readFloat();
            float experience = byteBuf.readFloat();
            float timeMultiplier = byteBuf.readFloat();
            boolean needsAdditives = byteBuf.readBoolean();
            int bonusChance = byteBuf.readVarInt();
            int bonusMin = byteBuf.readVarInt();
            int bonusMax = byteBuf.readVarInt();

            return new ShapedBlastFurnaceRecipe(id, ingredients, output, temperature, experience, timeMultiplier, needsAdditives, bonusChance, bonusMin, bonusMax);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ShapedBlastFurnaceRecipe recipe) {
            buffer.writeVarInt(recipe.ingredients.size());
            for (Ingredient ingredient : recipe.ingredients) {
                ingredient.toNetwork(buffer);
            }
            buffer.writeItemStack(recipe.output, true);
            buffer.writeFloat(recipe.operatingTemperature);
            buffer.writeFloat(recipe.experience);
            buffer.writeFloat(recipe.timeMultiplier);
            buffer.writeBoolean(recipe.needsAdditives());
            buffer.writeVarInt(recipe.bonusChance);
            buffer.writeVarInt(recipe.bonusMin);
            buffer.writeVarInt(recipe.bonusMax);
        }
    }
}
