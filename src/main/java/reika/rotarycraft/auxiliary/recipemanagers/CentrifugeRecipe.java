package reika.rotarycraft.auxiliary.recipemanagers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

import reika.rotarycraft.registry.RotaryRecipeSerializers;
import reika.rotarycraft.registry.RotaryRecipeTypes;

/**
 * Single input item spun into a set of chanced item outputs plus an optional chanced fluid —
 * the data-driven port of the legacy {@code RecipesCentrifuge.CentrifugeRecipe}. Chances are
 * normalized (0..1); values above 1 yield {@code floor(chance)} guaranteed copies plus one
 * more at the fractional chance, matching the legacy {@code rollItems} semantics.
 */
public class CentrifugeRecipe implements Recipe<SingleRecipeInput> {

    public record ChancedOutput(ItemStackTemplate stack, float chance) {
        public static final Codec<ChancedOutput> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                ItemStackTemplate.CODEC.fieldOf("item").forGetter(ChancedOutput::stack),
                Codec.FLOAT.optionalFieldOf("chance", 1F).forGetter(ChancedOutput::chance)
        ).apply(inst, ChancedOutput::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, ChancedOutput> STREAM_CODEC = StreamCodec.composite(
                ItemStackTemplate.STREAM_CODEC, ChancedOutput::stack,
                ByteBufCodecs.FLOAT, ChancedOutput::chance,
                ChancedOutput::new);
    }

    /**
     * Fluid byproduct as holder + amount rather than a FluidStack — FluidStack construction needs
     * bound data components, which don't exist during datagen ("Components not bound yet").
     */
    public record FluidOutput(Holder<Fluid> fluid, int amount, float chance) {
        public static final Codec<FluidOutput> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                BuiltInRegistries.FLUID.holderByNameCodec().fieldOf("fluid").forGetter(FluidOutput::fluid),
                Codec.INT.fieldOf("amount").forGetter(FluidOutput::amount),
                Codec.FLOAT.optionalFieldOf("chance", 1F).forGetter(FluidOutput::chance)
        ).apply(inst, FluidOutput::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, FluidOutput> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.holderRegistry(Registries.FLUID), FluidOutput::fluid,
                ByteBufCodecs.VAR_INT, FluidOutput::amount,
                ByteBufCodecs.FLOAT, FluidOutput::chance,
                FluidOutput::new);

        public FluidStack createStack() {
            return new FluidStack(fluid.value(), amount);
        }
    }

    private final Ingredient input;
    private final List<ChancedOutput> outputs;
    private final Optional<FluidOutput> fluid;

    public CentrifugeRecipe(Ingredient input, List<ChancedOutput> outputs, Optional<FluidOutput> fluid) {
        this.input = input;
        this.outputs = outputs;
        this.fluid = fluid;
    }

    @Override
    public boolean matches(SingleRecipeInput in, Level level) {
        return input.test(in.item());
    }

    @Override
    public ItemStack assemble(SingleRecipeInput in) {
        return outputs.isEmpty() ? ItemStack.EMPTY : outputs.get(0).stack().create();
    }

    public Ingredient getInput() {
        return input;
    }

    public List<ChancedOutput> getOutputs() {
        return outputs;
    }

    public Optional<FluidOutput> getFluidOutput() {
        return fluid;
    }

    /** Roll every chanced output; chance >= 1 gives guaranteed copies plus a fractional roll. */
    public List<ItemStack> rollItems(RandomSource rand) {
        List<ItemStack> out = new ArrayList<>();
        for (ChancedOutput c : outputs) {
            float ch = c.chance();
            while (ch >= 1) {
                out.add(c.stack().create());
                ch -= 1;
            }
            if (ch > 0 && rand.nextFloat() < ch)
                out.add(c.stack().create());
        }
        return out;
    }

    /** Roll the fluid output; empty stack when absent or the roll fails. */
    public FluidStack rollFluid(RandomSource rand) {
        if (fluid.isEmpty())
            return FluidStack.EMPTY;
        FluidOutput f = fluid.get();
        return rand.nextFloat() < f.chance() ? f.createStack() : FluidStack.EMPTY;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(input);
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    @Override
    public RecipeSerializer<? extends Recipe<SingleRecipeInput>> getSerializer() {
        return RotaryRecipeSerializers.CENTRIFUGE.get();
    }

    @Override
    public RecipeType<? extends Recipe<SingleRecipeInput>> getType() {
        return RotaryRecipeTypes.CENTRIFUGE.get();
    }

    public static final MapCodec<CentrifugeRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.fieldOf("input").forGetter(r -> r.input),
            ChancedOutput.CODEC.listOf().fieldOf("outputs").forGetter(r -> r.outputs),
            FluidOutput.CODEC.optionalFieldOf("fluid_output").forGetter(r -> r.fluid)
    ).apply(inst, CentrifugeRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CentrifugeRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, r -> r.input,
            ChancedOutput.STREAM_CODEC.apply(ByteBufCodecs.list()), r -> r.outputs,
            ByteBufCodecs.optional(FluidOutput.STREAM_CODEC), r -> r.fluid,
            CentrifugeRecipe::new);
}
