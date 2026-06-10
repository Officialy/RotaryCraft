package reika.rotarycraft.auxiliary.recipemanagers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import reika.dragonapi.interfaces.IBonusYield;
import reika.dragonapi.interfaces.IHasXP;
import reika.dragonapi.interfaces.IHeatRecipe;
import reika.rotarycraft.registry.RotaryRecipeSerializers;
import reika.rotarycraft.registry.RotaryRecipeTypes;

import java.util.ArrayList;
import java.util.List;

public class ShapelessBlastFurnaceRecipe implements Recipe<RecipeInput>, IBonusYield, IHasXP, IHeatRecipe {

    protected final List<Ingredient> ingredients;
    protected final List<Ingredient> additives;
    // 1.21.5: stored as ItemStackTemplate (deferred Holder<Item>) so the recipe can be built
    // during datagen before Item components are bound. Convert to ItemStack on demand in
    // {@link #assemble} / {@link #getOutput}, by which time the registry is frozen.
    private final ItemStackTemplate output;
    private final float operatingTemperature;
    private final float experience;
    private final float timeMultiplier;
    private final int bonusChance;
    private final int bonusMin;
    private final int bonusMax;

    public ShapelessBlastFurnaceRecipe(List<Ingredient> ingredients, List<Ingredient> additives, ItemStackTemplate output, float temperature, float experience, float timeMultiplier, int bonusChance, int bonusMin, int bonusMax) {
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
    public boolean matches(RecipeInput input, Level lvl) {
        if (lvl.isClientSide()) return false;

        ItemStack[] remaining = new ItemStack[input.size()];
        for (int i = 0; i < remaining.length; i++)
            remaining[i] = input.getItem(i).copy();

        for (Ingredient ing : ingredients) {
            boolean matched = false;
            for (ItemStack itemStack : remaining) {
                if (!itemStack.isEmpty() && ing.test(itemStack)) {
                    itemStack.shrink(1);
                    matched = true;
                    break;
                }
            }
            if (!matched) return false;
        }

        for (Ingredient add : additives) {
            boolean matched = false;
            for (ItemStack stack : remaining) {
                if (!stack.isEmpty() && add.test(stack)) {
                    matched = true;
                    break;
                }
            }
            if (!matched) return false;
        }

        return true;
    }

    @Override
    public ItemStack assemble(RecipeInput input) {
        return output.create();
    }

    public ItemStack getOutput() {
        return output.create();
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Ingredient> getAdditives() {
        return additives;
    }

    @Override
    public boolean showNotification() {
        return true;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(ingredients);
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.BLAST_FURNACE_MISC;
    }

    @Override
    public RecipeSerializer<? extends Recipe<RecipeInput>> getSerializer() {
        return RotaryRecipeSerializers.BLAST_FURNACE_SHAPELESS.get();
    }

    @Override
    public RecipeType<? extends Recipe<RecipeInput>> getType() {
        return RotaryRecipeTypes.BLAST_FURNACE_SHAPELESS.get();
    }

    /* IBonusYield */
    @Override public int bonusChance() { return bonusChance; }
    @Override public int bonusMin()    { return bonusMin; }
    @Override public int bonusMax()    { return bonusMax; }
    /* IHasXP */
    @Override public float xpPerItem() { return experience; }
    @Override public float requiredTemperature() { return operatingTemperature; }

    public static final MapCodec<ShapelessBlastFurnaceRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(r -> r.ingredients),
            Ingredient.CODEC.listOf().fieldOf("additives").forGetter(r -> r.additives),
            ItemStackTemplate.CODEC.fieldOf("output").forGetter(r -> r.output),
            Codec.FLOAT.fieldOf("temperature").forGetter(r -> r.operatingTemperature),
            Codec.FLOAT.fieldOf("experience").forGetter(r -> r.experience),
            Codec.FLOAT.fieldOf("timeMultiplier").forGetter(r -> r.timeMultiplier),
            Codec.INT.optionalFieldOf("bonusChance", 0).forGetter(r -> r.bonusChance),
            Codec.INT.optionalFieldOf("bonusMin", 0).forGetter(r -> r.bonusMin),
            Codec.INT.optionalFieldOf("bonusMax", 0).forGetter(r -> r.bonusMax)
    ).apply(inst, ShapelessBlastFurnaceRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ShapelessBlastFurnaceRecipe> STREAM_CODEC = StreamCodec.of(
            (buf, r) -> {
                buf.writeVarInt(r.ingredients.size());
                for (Ingredient ing : r.ingredients) Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ing);
                buf.writeVarInt(r.additives.size());
                for (Ingredient ing : r.additives) Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ing);
                ItemStackTemplate.STREAM_CODEC.encode(buf, r.output);
                buf.writeFloat(r.operatingTemperature);
                buf.writeFloat(r.experience);
                buf.writeFloat(r.timeMultiplier);
                buf.writeVarInt(r.bonusChance);
                buf.writeVarInt(r.bonusMin);
                buf.writeVarInt(r.bonusMax);
            },
            buf -> {
                int ic = buf.readVarInt();
                List<Ingredient> ings = new ArrayList<>();
                for (int i = 0; i < ic; i++) ings.add(Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
                int ac = buf.readVarInt();
                List<Ingredient> adds = new ArrayList<>();
                for (int i = 0; i < ac; i++) adds.add(Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
                ItemStackTemplate out = ItemStackTemplate.STREAM_CODEC.decode(buf);
                float temp = buf.readFloat();
                float xp = buf.readFloat();
                float tm = buf.readFloat();
                int bc = buf.readVarInt();
                int bmin = buf.readVarInt();
                int bmax = buf.readVarInt();
                return new ShapelessBlastFurnaceRecipe(ings, adds, out, temp, xp, tm, bc, bmin, bmax);
            }
    );
}
