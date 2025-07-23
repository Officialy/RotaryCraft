package reika.rotarycraft.auxiliary.recipemanagers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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

public class ShapelessBlastFurnaceRecipe implements Recipe<SimpleContainer>, IBonusYield, IHasXP, IHeatRecipe {
    private final ResourceLocation id;
    protected final NonNullList<Ingredient> ingredients;
    protected final NonNullList<Ingredient> additives;
    private final ItemStack output;
    private final float operatingTemperature;
    private final float experience;
    private final float timeMultiplier;
    private final int bonusChance;
    private final int bonusMin;
    private final int bonusMax;

    public ShapelessBlastFurnaceRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, NonNullList<Ingredient> additives, ItemStack output, float temperature, float experience, float timeMultiplier, int bonusChance, int bonusMin, int bonusMax) {
        this.id = id;
        this.ingredients = ingredients;
        this.additives = additives;
        this.output = output;
        this.operatingTemperature = temperature;
        this.experience = experience;
        this.timeMultiplier = timeMultiplier;
        this.bonusChance = bonusChance;
        this.bonusMin = bonusMin;
        this.bonusMax = bonusMax;
    }

    public float getOperatingTemperature() {
        return operatingTemperature;
    }

    public float getExperience() {
        return experience;
    }

    public float getTimeMultiplier() {
        return timeMultiplier;
    }

    @Override
    public boolean matches(SimpleContainer inv, Level lvl) {
        if (lvl.isClientSide) return false;

        // Build a working copy of the inventory so we can mark slots “used”
        ItemStack[] remaining = new ItemStack[inv.getContainerSize()];
        for (int i = 0; i < remaining.length; i++)
            remaining[i] = inv.getItem(i).copy();

        /* ------- check main ingredients (order-agnostic) -------- */
        for (Ingredient ing : ingredients) {
            boolean matched = false;
            for (ItemStack itemStack : remaining) {
                if (!itemStack.isEmpty() && ing.test(itemStack)) {
                    itemStack.shrink(1);   // mark as used
                    matched = true;
                    break;
                }
            }
            if (!matched) return false;       // missing one ingredient
        }

        /* ------- check additives (each must appear at least once) -------- */
        for (Ingredient add : additives) {
            boolean matched = false;
            for (ItemStack stack : remaining) {
                if (!stack.isEmpty() && add.test(stack)) {
                    matched = true;
                    break;
                }
            }
            if (!matched) return false;       // additive missing
        }

        return true;
    }


    @Override
    public ItemStack assemble(SimpleContainer p_44001_, RegistryAccess p_267165_) {
        return output.copy();
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

    public NonNullList<Ingredient> getAdditives() {
        return additives;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RotaryRecipeSerializers.BLAST_FURNACE_SHAPELESS.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RotaryRecipeTypes.BLAST_FURNACE_SHAPELESS.get();
    }


    /* IBonusYield */
    @Override public int bonusChance() { return bonusChance; }
    @Override public int bonusMin()    { return bonusMin; }
    @Override public int bonusMax()    { return bonusMax; }
    /* IHasXP */
    @Override public float xpPerItem() { return experience; }
    @Override public float requiredTemperature() { return operatingTemperature; }

    public static class Serializer implements RecipeSerializer<ShapelessBlastFurnaceRecipe> {

        @Override
        public ShapelessBlastFurnaceRecipe fromJson(ResourceLocation id, JsonObject json) {
            NonNullList<Ingredient> ingredients = NonNullList.create();
            JsonArray ia = GsonHelper.getAsJsonArray(json, "ingredients");
            for (int i = 0; i < ia.size(); i++)
                ingredients.add(Ingredient.fromJson(ia.get(i)));

            NonNullList<Ingredient> additives = NonNullList.create();
            JsonArray aa = GsonHelper.getAsJsonArray(json, "additives");
            for (int i = 0; i < aa.size(); i++)
                additives.add(Ingredient.fromJson(aa.get(i)));

            ItemStack output      = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            float     temp        = GsonHelper.getAsFloat(json, "temperature");
            float     xp          = GsonHelper.getAsFloat(json, "experience");
            float     timeMul     = GsonHelper.getAsFloat(json, "timeMultiplier");
            int       chance      = GsonHelper.getAsInt  (json, "bonusChance", 0);
            int       min         = GsonHelper.getAsInt  (json, "bonusMin",    0);
            int       max         = GsonHelper.getAsInt  (json, "bonusMax",    0);

            return new ShapelessBlastFurnaceRecipe(
                    id, ingredients, additives, output,
                    temp, xp, timeMul,
                    chance, min, max);
        }

        @Override
        public ShapelessBlastFurnaceRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {

            /* main ingredients */
            int ingCount = buf.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(ingCount, Ingredient.EMPTY);
            for (int i = 0; i < ingCount; i++)
                ingredients.set(i, Ingredient.fromNetwork(buf));

            /* additives */
            int addCount = buf.readVarInt();
            NonNullList<Ingredient> additives = NonNullList.withSize(addCount, Ingredient.EMPTY);
            for (int i = 0; i < addCount; i++)
                additives.set(i, Ingredient.fromNetwork(buf));

            /* scalar data */
            ItemStack output      = buf.readItem();
            float     temp        = buf.readFloat();
            float     xp          = buf.readFloat();
            float     timeMul     = buf.readFloat();
            int       chance      = buf.readInt();
            int       min         = buf.readInt();
            int       max         = buf.readInt();

            return new ShapelessBlastFurnaceRecipe(
                    id, ingredients, additives, output,
                    temp, xp, timeMul,
                    chance, min, max);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ShapelessBlastFurnaceRecipe recipe) {
            /* main ingredients */
            buf.writeVarInt(recipe.ingredients.size());
            for (Ingredient ing : recipe.ingredients)
                ing.toNetwork(buf);

            /* additives */
            buf.writeVarInt(recipe.additives.size());
            for (Ingredient add : recipe.additives)
                add.toNetwork(buf);

            /* scalar data */
            buf.writeItem(recipe.output);
            buf.writeFloat(recipe.operatingTemperature);
            buf.writeFloat(recipe.experience);
            buf.writeFloat(recipe.timeMultiplier);
            buf.writeInt (recipe.bonusChance);
            buf.writeInt (recipe.bonusMin);
            buf.writeInt (recipe.bonusMax);
        }
    }
}