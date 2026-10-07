package reika.rotarycraft.auxiliary.recipemanagers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.Level;
import reika.dragonapi.interfaces.IBonusYield;
import reika.dragonapi.interfaces.IHasXP;
import reika.dragonapi.interfaces.IHeatRecipe;
import reika.rotarycraft.registry.RotaryRecipeSerializers;
import reika.rotarycraft.registry.RotaryRecipeTypes;

import java.util.ArrayList;
import java.util.List;

public class ShapedBlastFurnaceRecipe implements Recipe<RecipeInput>, IBonusYield, IHasXP, IHeatRecipe {
    private final ShapedRecipePattern pattern;
    // 1.21.5: stored as ItemStackTemplate (deferred Holder<Item>) so the recipe can be built during
    // datagen before Item components are bound; materialized via output.create() at runtime.
    private final ItemStackTemplate output;
    private final float operatingTemperature;
    private final float experience;
    private final float timeMultiplier;
    private final boolean needsAdditives;
    private final int bonusChance;
    private final int bonusMin;
    private final int bonusMax;

    public ShapedBlastFurnaceRecipe(ShapedRecipePattern pattern, ItemStackTemplate output, float temperature, float experience, float timeMultiplier, boolean needsAdditives, int chance, int min, int max) {
        this.pattern = pattern;
        this.output = output;
        this.operatingTemperature = temperature;
        this.experience = experience;
        this.timeMultiplier = timeMultiplier;
        this.needsAdditives = needsAdditives;
        this.bonusChance = chance;
        this.bonusMin = min;
        this.bonusMax = max;
    }

    public float getOperatingTemperature() {
        return operatingTemperature;
    }

    @Override
    public boolean matches(RecipeInput input, Level world) {
        if (input.size() < 10 || needsAdditives)
            return false;
        List<ItemStack> grid = new ArrayList<>(9);
        for (int i = 1; i <= 9; i++)
            grid.add(input.getItem(i));
        return pattern.matches(CraftingInput.of(3, 3, grid));
    }

    @Override
    public ItemStack assemble(RecipeInput input) {
        return output.create();
    }

    public ItemStack getOutput() {
        return output.create();
    }

    public List<Ingredient> getIngredients() {
        return pattern.ingredients().stream().flatMap(java.util.Optional::stream).toList();
    }

    /** The grid with its empty cells, for displays that must place each ingredient where it goes. */
    public net.minecraft.world.item.crafting.ShapedRecipePattern getPattern() {
        return pattern;
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
    public boolean showNotification() {
        return true;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.createFromOptionals(pattern.ingredients());
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.BLAST_FURNACE_MISC;
    }

    @Override
    public RecipeSerializer<? extends Recipe<RecipeInput>> getSerializer() {
        return RotaryRecipeSerializers.BLAST_FURNACE_SHAPED.get();
    }

    @Override
    public RecipeType<? extends Recipe<RecipeInput>> getType() {
        return RotaryRecipeTypes.BLAST_FURNACE_SHAPED.get();
    }

    /* IBonusYield */
    @Override public int bonusChance() { return bonusChance; }
    @Override public int bonusMin()    { return bonusMin; }
    @Override public int bonusMax()    { return bonusMax; }
    /* IHasXP */
    @Override public float xpPerItem() { return experience; }
    @Override public float requiredTemperature() { return operatingTemperature; }

    public static final MapCodec<ShapedBlastFurnaceRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ShapedRecipePattern.MAP_CODEC.forGetter(r -> r.pattern),
            ItemStackTemplate.CODEC.fieldOf("output").forGetter(r -> r.output),
            Codec.FLOAT.fieldOf("temperature").forGetter(r -> r.operatingTemperature),
            Codec.FLOAT.fieldOf("experience").forGetter(r -> r.experience),
            Codec.FLOAT.fieldOf("timeMultiplier").forGetter(r -> r.timeMultiplier),
            Codec.BOOL.optionalFieldOf("needsAdditives", false).forGetter(r -> r.needsAdditives),
            Codec.INT.optionalFieldOf("bonusChance", 0).forGetter(r -> r.bonusChance),
            Codec.INT.optionalFieldOf("bonusMin", 0).forGetter(r -> r.bonusMin),
            Codec.INT.optionalFieldOf("bonusMax", 0).forGetter(r -> r.bonusMax)
    ).apply(inst, ShapedBlastFurnaceRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ShapedBlastFurnaceRecipe> STREAM_CODEC = StreamCodec.of(
            (buf, r) -> {
                ShapedRecipePattern.STREAM_CODEC.encode(buf, r.pattern);
                ItemStackTemplate.STREAM_CODEC.encode(buf, r.output);
                buf.writeFloat(r.operatingTemperature);
                buf.writeFloat(r.experience);
                buf.writeFloat(r.timeMultiplier);
                buf.writeBoolean(r.needsAdditives);
                buf.writeVarInt(r.bonusChance);
                buf.writeVarInt(r.bonusMin);
                buf.writeVarInt(r.bonusMax);
            },
            buf -> {
                ShapedRecipePattern pattern = ShapedRecipePattern.STREAM_CODEC.decode(buf);
                ItemStackTemplate out = ItemStackTemplate.STREAM_CODEC.decode(buf);
                float temp = buf.readFloat();
                float xp = buf.readFloat();
                float tm = buf.readFloat();
                boolean na = buf.readBoolean();
                int bc = buf.readVarInt();
                int bmin = buf.readVarInt();
                int bmax = buf.readVarInt();
                return new ShapedBlastFurnaceRecipe(pattern, out, temp, xp, tm, na, bc, bmin, bmax);
            }
    );
}
