package reika.rotarycraft.auxiliary.recipemanagers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import reika.rotarycraft.registry.RotaryRecipeSerializers;
import reika.rotarycraft.registry.RotaryRecipeTypes;

import java.util.ArrayList;
import java.util.List;

/** The six unordered solids, solvent, and fluids used by the original jet-fuel fractionator. */
public class FractionatorRecipe implements Recipe<FractionatorRecipe.FractionatorInput> {

    public record WeightedIngredient(Ingredient ingredient, float weight) {
        public static final Codec<WeightedIngredient> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(WeightedIngredient::ingredient),
                Codec.FLOAT.fieldOf("weight").forGetter(WeightedIngredient::weight)
        ).apply(inst, WeightedIngredient::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, WeightedIngredient> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, WeightedIngredient::ingredient,
                        ByteBufCodecs.FLOAT, WeightedIngredient::weight,
                        WeightedIngredient::new);
    }

    public record FractionatorInput(List<ItemStack> solids, ItemStack solvent, FluidStack fluid)
            implements RecipeInput {
        @Override
        public ItemStack getItem(int slot) {
            if (slot >= 0 && slot < solids.size()) return solids.get(slot);
            if (slot == solids.size()) return solvent;
            throw new IllegalArgumentException("No fractionator slot " + slot);
        }

        @Override
        public int size() {
            return solids.size() + 1;
        }
    }

    private final List<WeightedIngredient> ingredients;
    private final Ingredient solvent;
    private final Holder<Fluid> inputFluid;
    private final Holder<Fluid> outputFluid;
    private final int inputAmount;
    private final int nominalOutput;

    public FractionatorRecipe(List<WeightedIngredient> ingredients, Ingredient solvent,
                              Holder<Fluid> inputFluid, Holder<Fluid> outputFluid,
                              int inputAmount, int nominalOutput) {
        if (ingredients.size() != 6 || ingredients.stream().anyMatch(entry -> !Float.isFinite(entry.weight()) || entry.weight() <= 0)
                || inputAmount <= 0 || nominalOutput <= 0)
            throw new IllegalArgumentException("Fractionator needs six positive-weight solids and positive fluid amounts");
        this.ingredients = List.copyOf(ingredients);
        this.solvent = solvent;
        this.inputFluid = inputFluid;
        this.outputFluid = outputFluid;
        this.inputAmount = inputAmount;
        this.nominalOutput = nominalOutput;
    }

    @Override
    public boolean matches(FractionatorInput input, Level level) {
        if (input.solids().size() != ingredients.size() || !solvent.test(input.solvent())
                || input.fluid().isEmpty() || !input.fluid().getFluid().isSame(inputFluid.value()))
            return false;
        boolean[] used = new boolean[ingredients.size()];
        for (ItemStack stack : input.solids()) {
            if (stack.isEmpty()) return false;
            int match = -1;
            for (int i = 0; i < ingredients.size(); i++) {
                if (!used[i] && ingredients.get(i).ingredient().test(stack)) {
                    match = i;
                    break;
                }
            }
            if (match < 0) return false;
            used[match] = true;
        }
        return true;
    }

    @Override
    public ItemStack assemble(FractionatorInput input) {
        return ItemStack.EMPTY;
    }

    public List<WeightedIngredient> getIngredients() {
        return ingredients;
    }

    public Ingredient getSolvent() {
        return solvent;
    }

    public Fluid getInputFluid() {
        return inputFluid.value();
    }

    public Fluid getOutputFluid() {
        return outputFluid.value();
    }

    public int getInputAmount() {
        return inputAmount;
    }

    public int getNominalOutput() {
        return nominalOutput;
    }

    public float weightFor(ItemStack stack) {
        for (WeightedIngredient entry : ingredients)
            if (entry.ingredient().test(stack)) return entry.weight();
        return 0;
    }

    @Override public boolean showNotification() { return false; }
    @Override public String group() { return ""; }

    @Override
    public PlacementInfo placementInfo() {
        List<Ingredient> slots = new ArrayList<>(ingredients.size() + 1);
        for (WeightedIngredient entry : ingredients) slots.add(entry.ingredient());
        slots.add(solvent);
        return PlacementInfo.create(slots);
    }

    @Override public RecipeBookCategory recipeBookCategory() { return RecipeBookCategories.CRAFTING_MISC; }
    @Override public RecipeSerializer<? extends Recipe<FractionatorInput>> getSerializer() { return RotaryRecipeSerializers.FRACTIONATOR.get(); }
    @Override public RecipeType<? extends Recipe<FractionatorInput>> getType() { return RotaryRecipeTypes.FRACTIONATOR.get(); }

    public static final MapCodec<FractionatorRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            WeightedIngredient.CODEC.listOf().fieldOf("ingredients").forGetter(r -> r.ingredients),
            Ingredient.CODEC.fieldOf("solvent").forGetter(r -> r.solvent),
            BuiltInRegistries.FLUID.holderByNameCodec().fieldOf("input_fluid").forGetter(r -> r.inputFluid),
            BuiltInRegistries.FLUID.holderByNameCodec().fieldOf("output_fluid").forGetter(r -> r.outputFluid),
            Codec.INT.fieldOf("input_amount").forGetter(r -> r.inputAmount),
            Codec.INT.fieldOf("nominal_output").forGetter(r -> r.nominalOutput)
    ).apply(inst, FractionatorRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, FractionatorRecipe> STREAM_CODEC = StreamCodec.of(
            (buf, recipe) -> {
                WeightedIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buf, recipe.ingredients);
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.solvent);
                ByteBufCodecs.holderRegistry(Registries.FLUID).encode(buf, recipe.inputFluid);
                ByteBufCodecs.holderRegistry(Registries.FLUID).encode(buf, recipe.outputFluid);
                buf.writeVarInt(recipe.inputAmount);
                buf.writeVarInt(recipe.nominalOutput);
            },
            buf -> new FractionatorRecipe(
                    WeightedIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buf),
                    Ingredient.CONTENTS_STREAM_CODEC.decode(buf),
                    ByteBufCodecs.holderRegistry(Registries.FLUID).decode(buf),
                    ByteBufCodecs.holderRegistry(Registries.FLUID).decode(buf),
                    buf.readVarInt(), buf.readVarInt()));
}
