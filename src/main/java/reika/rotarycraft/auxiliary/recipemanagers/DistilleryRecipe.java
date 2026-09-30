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
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import reika.rotarycraft.registry.RotaryRecipeSerializers;
import reika.rotarycraft.registry.RotaryRecipeTypes;

/** V33a fluid conversions, represented as reloadable recipes with common-tag integration inputs. */
public record DistilleryRecipe(TagKey<Fluid> input, int inputAmount, Holder<Fluid> output, int outputAmount,
        int minTorque, long minPower) implements Recipe<DistilleryRecipe.Input> {
    public static final MapCodec<DistilleryRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            TagKey.codec(Registries.FLUID).fieldOf("input").forGetter(DistilleryRecipe::input),
            Codec.intRange(1, 6000).fieldOf("input_amount").forGetter(DistilleryRecipe::inputAmount),
            BuiltInRegistries.FLUID.holderByNameCodec().fieldOf("output").forGetter(DistilleryRecipe::output),
            Codec.intRange(1, 6000).fieldOf("output_amount").forGetter(DistilleryRecipe::outputAmount),
            Codec.intRange(1, Integer.MAX_VALUE).fieldOf("min_torque").forGetter(DistilleryRecipe::minTorque),
            Codec.LONG.validate(value -> value > 0 ? com.mojang.serialization.DataResult.success(value) : com.mojang.serialization.DataResult.error(() -> "Power must be positive")).fieldOf("min_power").forGetter(DistilleryRecipe::minPower)
    ).apply(i, DistilleryRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, DistilleryRecipe> STREAM_CODEC = StreamCodec.of(
            (data, recipe) -> {
                data.writeIdentifier(recipe.input.location());
                data.writeVarInt(recipe.inputAmount);
                ByteBufCodecs.holderRegistry(Registries.FLUID).encode(data, recipe.output);
                data.writeVarInt(recipe.outputAmount);
                data.writeVarInt(recipe.minTorque);
                data.writeVarLong(recipe.minPower);
            }, data -> new DistilleryRecipe(TagKey.create(Registries.FLUID, data.readIdentifier()), data.readVarInt(),
                    ByteBufCodecs.holderRegistry(Registries.FLUID).decode(data), data.readVarInt(), data.readVarInt(), data.readVarLong()));

    public boolean accepts(Fluid fluid) { return BuiltInRegistries.FLUID.wrapAsHolder(fluid).is(input); }
    @Override public boolean matches(Input in, Level level) { return !in.fluid.isEmpty() && accepts(in.fluid.getFluid()) && in.fluid.getAmount() >= inputAmount; }
    public FluidStack getFluidOutput() { return new FluidStack(output.value(), outputAmount); }
    // Fluid-only recipes have no item result and never appear in the vanilla crafting book.
    @Override public ItemStack assemble(Input in) { return ItemStack.EMPTY; }
    @Override public PlacementInfo placementInfo() { return PlacementInfo.NOT_PLACEABLE; }
    @Override public boolean showNotification() { return false; }
    @Override public String group() { return ""; }
    @Override public RecipeBookCategory recipeBookCategory() { return RecipeBookCategories.CRAFTING_MISC; }
    @Override public RecipeSerializer<DistilleryRecipe> getSerializer() { return RotaryRecipeSerializers.DISTILLER.get(); }
    @Override public RecipeType<DistilleryRecipe> getType() { return RotaryRecipeTypes.DISTILLER.get(); }

    public record Input(FluidStack fluid) implements RecipeInput {
        @Override public ItemStack getItem(int slot) { throw new IndexOutOfBoundsException("Distillery recipes have no item slots"); }
        @Override public int size() { return 0; }
        @Override public boolean isEmpty() { return fluid.isEmpty(); }
    }
}
