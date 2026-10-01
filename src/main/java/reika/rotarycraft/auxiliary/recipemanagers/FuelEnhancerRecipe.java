package reika.rotarycraft.auxiliary.recipemanagers;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.*;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.*;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import reika.rotarycraft.blockentities.processing.BlockEntityFuelConverter;
import reika.rotarycraft.registry.*;

/** V33a's speed, fluid ratio and independently consumed catalysts, supplied by datapacks. */
public final class FuelEnhancerRecipe implements Recipe<FuelEnhancerRecipe.Input> {
    private final TagKey<Fluid> input;
    private final Holder<Fluid> output;
    private final int speedFactor, fluidRatio;
    private final double consumptionMultiplier;
    private final List<Ingredient> ingredients;
    private final String description;
    private UsabilityCondition condition;
    public FuelEnhancerRecipe(TagKey<Fluid> input, Holder<Fluid> output, int speedFactor, int fluidRatio,
            double consumptionMultiplier, List<Ingredient> ingredients, String description) {
        if (speedFactor < 1 || speedFactor > 5000 || fluidRatio < 1 || fluidRatio > 5000 || ingredients.size() > 9
                || !Double.isFinite(consumptionMultiplier) || consumptionMultiplier < 0 || consumptionMultiplier > 32)
            throw new IllegalArgumentException("Invalid fuel conversion cost, rate or ingredient count");
        this.input = input; this.output = output; this.speedFactor = speedFactor; this.fluidRatio = fluidRatio;
        this.consumptionMultiplier = consumptionMultiplier; this.ingredients = List.copyOf(ingredients); this.description = description;
    }
    public TagKey<Fluid> input() { return input; }
    public Holder<Fluid> output() { return output; }
    public int speedFactor() { return speedFactor; }
    public int fluidRatio() { return fluidRatio; }
    public double consumptionMultiplier() { return consumptionMultiplier; }
    public List<Ingredient> ingredients() { return ingredients; }
    public String getCondition() { return condition == null ? description : condition.getDescription(); }
    public double itemConsumptionChance() { return Math.min(1, consumptionMultiplier * DifficultyEffects.CONSUMEFRAC.getChance()); }
    public boolean accepts(Fluid fluid) { return BuiltInRegistries.FLUID.wrapAsHolder(fluid).is(input); }
    public boolean isValidItem(ItemStack item) { return ingredients.stream().anyMatch(i -> i.test(item)); }
    public FuelEnhancerRecipe setUsability(UsabilityCondition condition) { this.condition = condition; return this; }
    public boolean isUsable(BlockEntityFuelConverter machine) { return condition == null || condition.isUsable(machine); }
    /** Keeps V33a's optional integration condition without registering recipes in runtime maps. */
    public interface UsabilityCondition {
        boolean isUsable(BlockEntityFuelConverter machine);
        String getDescription();
    }
    public static final MapCodec<FuelEnhancerRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            TagKey.codec(Registries.FLUID).fieldOf("input").forGetter(FuelEnhancerRecipe::input),
            BuiltInRegistries.FLUID.holderByNameCodec().fieldOf("output").forGetter(FuelEnhancerRecipe::output),
            Codec.intRange(1, 5000).fieldOf("speed_factor").forGetter(FuelEnhancerRecipe::speedFactor),
            Codec.intRange(1, 5000).fieldOf("fluid_ratio").forGetter(FuelEnhancerRecipe::fluidRatio),
            Codec.doubleRange(0, 32).fieldOf("consumption_multiplier").forGetter(FuelEnhancerRecipe::consumptionMultiplier),
            Ingredient.CODEC.listOf().validate(list -> list.size() > 9 ? DataResult.error(() -> "Expected at most 9 catalysts") : DataResult.success(list))
                    .fieldOf("ingredients").forGetter(FuelEnhancerRecipe::ingredients),
            Codec.STRING.optionalFieldOf("condition_description", "").forGetter(FuelEnhancerRecipe::getCondition)
    ).apply(i, FuelEnhancerRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, FuelEnhancerRecipe> STREAM_CODEC = StreamCodec.of((buf, recipe) -> {
        buf.writeIdentifier(recipe.input.location()); ByteBufCodecs.holderRegistry(Registries.FLUID).encode(buf, recipe.output);
        buf.writeVarInt(recipe.speedFactor); buf.writeVarInt(recipe.fluidRatio); buf.writeDouble(recipe.consumptionMultiplier);
        buf.writeVarInt(recipe.ingredients.size());
        for (var item : recipe.ingredients) Ingredient.CONTENTS_STREAM_CODEC.encode(buf, item);
        buf.writeUtf(recipe.getCondition());
    }, buf -> {
        var tag = TagKey.create(Registries.FLUID, buf.readIdentifier());
        var output = ByteBufCodecs.holderRegistry(Registries.FLUID).decode(buf);
        int speed = buf.readVarInt(), ratio = buf.readVarInt(); double chance = buf.readDouble(); int count = buf.readVarInt();
        if (count < 0 || count > 9) throw new IllegalArgumentException("Expected at most 9 catalysts");
        var ingredients = new ArrayList<Ingredient>();
        for (int n = 0; n < count; n++) ingredients.add(Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
        return new FuelEnhancerRecipe(tag, output, speed, ratio, chance, ingredients, buf.readUtf());
    });
    /** Assigns one unit to each catalyst, including overlapping ingredient tags and repeated costs. */
    public int[] findIngredients(List<ItemStack> inventory) {
        int[] slots = new int[ingredients.size()]; Arrays.fill(slots, -1);
        for (int item = 0; item < slots.length; item++)
            if (!assign(item, inventory, slots, new boolean[slots.length], new boolean[inventory.size()])) return null;
        return slots;
    }
    private boolean assign(int item, List<ItemStack> inventory, int[] slots, boolean[] seenItems, boolean[] seenSlots) {
        if (seenItems[item]) return false;
        seenItems[item] = true;
        for (int slot = 0; slot < inventory.size(); slot++) {
            if (seenSlots[slot] || !ingredients.get(item).test(inventory.get(slot))) continue;
            seenSlots[slot] = true;
            int used = 0; for (int assigned : slots) if (assigned == slot) used++;
            if (used < inventory.get(slot).getCount()) { slots[item] = slot; return true; }
            for (int previous = 0; previous < slots.length; previous++)
                if (slots[previous] == slot && assign(previous, inventory, slots, seenItems, seenSlots)) { slots[item] = slot; return true; }
        }
        return false;
    }
    @Override public boolean matches(Input in, Level level) { return accepts(in.fluid) && in.amount >= (long)speedFactor * fluidRatio && findIngredients(in.items) != null; }
    @Override public ItemStack assemble(Input in) { return ItemStack.EMPTY; }
    @Override public PlacementInfo placementInfo() { return PlacementInfo.NOT_PLACEABLE; }
    @Override public boolean showNotification() { return false; }
    @Override public String group() { return ""; }
    @Override public RecipeBookCategory recipeBookCategory() { return RecipeBookCategories.CRAFTING_MISC; }
    @Override public RecipeSerializer<FuelEnhancerRecipe> getSerializer() { return RotaryRecipeSerializers.FUEL_ENHANCER.get(); }
    @Override public RecipeType<FuelEnhancerRecipe> getType() { return RotaryRecipeTypes.FUEL_ENHANCER.get(); }
    public record Input(Fluid fluid, int amount, List<ItemStack> items) implements RecipeInput {
        @Override public ItemStack getItem(int slot) { return items.get(slot); }
        @Override public int size() { return items.size(); }
        @Override public boolean isEmpty() { return amount == 0; }
    }
}
