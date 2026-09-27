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
    protected final List<Additive> additives;
    private final int mainCount;
    private final boolean exactCount;
    private final boolean requiresEmptyOutput;
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

    public ShapelessBlastFurnaceRecipe(List<Ingredient> ingredients, List<Additive> additives, ItemStackTemplate output, float temperature, float experience, float timeMultiplier, int bonusChance, int bonusMin, int bonusMax, int mainCount, boolean exactCount, boolean requiresEmptyOutput) {
        this.ingredients = ingredients;
        this.additives = additives;
        this.output = output;
        this.operatingTemperature = temperature;
        this.experience = experience;
        this.timeMultiplier = timeMultiplier;
        this.bonusChance = bonusChance;
        this.bonusMin = bonusMin;
        this.bonusMax = bonusMax;
        this.mainCount = mainCount;
        this.exactCount = exactCount;
        this.requiresEmptyOutput = requiresEmptyOutput;
    }

    /** Slot 0/11/14 correspond to centre/lower/upper additive. */
    public record Additive(int slot, Ingredient ingredient, int count, float chance) {
        public static final Codec<Additive> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                Codec.INT.fieldOf("slot").forGetter(Additive::slot),
                Ingredient.CODEC.fieldOf("ingredient").forGetter(Additive::ingredient),
                Codec.INT.fieldOf("count").forGetter(Additive::count),
                Codec.FLOAT.fieldOf("chance").forGetter(Additive::chance)
        ).apply(inst, Additive::new));
    }

    public int getMainCount() { return mainCount; }
    public boolean requiresEmptyOutput() { return requiresEmptyOutput; }

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

        if (input.size() < 15 || ingredients.isEmpty() || mainCount < 1) return false;
        if (ingredients.size() > 1) {
            List<ItemStack> present = new ArrayList<>();
            for (int slot = 1; slot <= 9; slot++)
                if (!input.getItem(slot).isEmpty()) present.add(input.getItem(slot));
            if (present.size() != ingredients.size() || mainCount != ingredients.size()
                    || !matchDistinct(present, new boolean[ingredients.size()], 0)) return false;
        } else {
        int occupied = 0;
        for (int slot = 1; slot <= 9; slot++) {
            ItemStack stack = input.getItem(slot);
            if (stack.isEmpty()) continue;
            if (!ingredients.getFirst().test(stack)) return false;
            occupied++;
        }
        if (occupied < mainCount || (exactCount && occupied != mainCount)) return false;
        }
        for (Additive additive : additives) {
            if (additive.slot != 0 && additive.slot != 11 && additive.slot != 14) return false;
            if (!additive.ingredient.test(input.getItem(additive.slot))
                    || input.getItem(additive.slot).getCount() < additive.count) return false;
        }
        return true;
    }

    private boolean matchDistinct(List<ItemStack> grid, boolean[] used, int at) {
        if (at == grid.size()) return true;
        for (int i = 0; i < ingredients.size(); i++) {
            if (used[i] || !ingredients.get(i).test(grid.get(at))) continue;
            used[i] = true;
            if (matchDistinct(grid, used, at + 1)) return true;
            used[i] = false;
        }
        return false;
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

    public List<Additive> getAdditives() {
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
            Additive.CODEC.listOf().fieldOf("additives").forGetter(r -> r.additives),
            ItemStackTemplate.CODEC.fieldOf("output").forGetter(r -> r.output),
            Codec.FLOAT.fieldOf("temperature").forGetter(r -> r.operatingTemperature),
            Codec.FLOAT.fieldOf("experience").forGetter(r -> r.experience),
            Codec.FLOAT.fieldOf("timeMultiplier").forGetter(r -> r.timeMultiplier),
            Codec.INT.optionalFieldOf("bonusChance", 0).forGetter(r -> r.bonusChance),
            Codec.INT.optionalFieldOf("bonusMin", 0).forGetter(r -> r.bonusMin),
            Codec.INT.optionalFieldOf("bonusMax", 0).forGetter(r -> r.bonusMax),
            Codec.INT.fieldOf("mainCount").forGetter(r -> r.mainCount),
            Codec.BOOL.fieldOf("exactCount").forGetter(r -> r.exactCount),
            Codec.BOOL.fieldOf("requiresEmptyOutput").forGetter(r -> r.requiresEmptyOutput)
    ).apply(inst, ShapelessBlastFurnaceRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ShapelessBlastFurnaceRecipe> STREAM_CODEC = StreamCodec.of(
            (buf, r) -> {
                buf.writeVarInt(r.ingredients.size());
                for (Ingredient ing : r.ingredients) Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ing);
                buf.writeVarInt(r.additives.size());
                for (Additive add : r.additives) {
                    buf.writeVarInt(add.slot);
                    Ingredient.CONTENTS_STREAM_CODEC.encode(buf, add.ingredient);
                    buf.writeVarInt(add.count);
                    buf.writeFloat(add.chance);
                }
                ItemStackTemplate.STREAM_CODEC.encode(buf, r.output);
                buf.writeFloat(r.operatingTemperature);
                buf.writeFloat(r.experience);
                buf.writeFloat(r.timeMultiplier);
                buf.writeVarInt(r.bonusChance);
                buf.writeVarInt(r.bonusMin);
                buf.writeVarInt(r.bonusMax);
                buf.writeVarInt(r.mainCount);
                buf.writeBoolean(r.exactCount);
                buf.writeBoolean(r.requiresEmptyOutput);
            },
            buf -> {
                int ic = buf.readVarInt();
                List<Ingredient> ings = new ArrayList<>();
                for (int i = 0; i < ic; i++) ings.add(Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
                int ac = buf.readVarInt();
                List<Additive> adds = new ArrayList<>();
                for (int i = 0; i < ac; i++) adds.add(new Additive(buf.readVarInt(), Ingredient.CONTENTS_STREAM_CODEC.decode(buf), buf.readVarInt(), buf.readFloat()));
                ItemStackTemplate out = ItemStackTemplate.STREAM_CODEC.decode(buf);
                float temp = buf.readFloat();
                float xp = buf.readFloat();
                float tm = buf.readFloat();
                int bc = buf.readVarInt();
                int bmin = buf.readVarInt();
                int bmax = buf.readVarInt();
                int main = buf.readVarInt();
                boolean exact = buf.readBoolean();
                boolean empty = buf.readBoolean();
                return new ShapelessBlastFurnaceRecipe(ings, adds, out, temp, xp, tm, bc, bmin, bmax, main, exact, empty);
            }
    );
}
