/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

import reika.rotarycraft.registry.RotaryRecipeSerializers;
import reika.rotarycraft.registry.RotaryRecipeTypes;

/**
 * Compactor compression step: 4x an input item squeeze into {@code count} of an output item, gated
 * on the machine holding {@code pressure} (kPa) and {@code temperature} (C). The carbon chain
 * (coal -> anthracite -> prismane -> lonsdaleite -> diamond) is the flagship use. Output stored as
 * item-holder + count (an ItemStack field trips "Components not bound" during datagen). Data-driven
 * port of the legacy {@code RecipesCompactor}.
 *
 * <p>{@code inputDamage} / {@code outputDamage} carry the {@code getDamageValue()}-variant charge used
 * by ReactorCraft's magnet chain (lodestone -> charge 0, then charge i -> charge i+1). {@code inputDamage
 * < 0} means "match any damage" (the carbon chain leaves both absent); a non-zero {@code outputDamage}
 * is stamped onto the result stack. Storing the damage as an int rather than a full output ItemStack
 * keeps clear of the datagen "Components not bound" trap.</p>
 */
public class CompactorRecipe implements Recipe<SingleRecipeInput> {

    private final Ingredient input;
    private final Holder<Item> output;
    private final int count;
    private final int pressure;
    private final int temperature;
    private final int inputDamage;
    private final int outputDamage;

    public CompactorRecipe(Ingredient input, Holder<Item> output, int count, int pressure, int temperature) {
        this(input, output, count, pressure, temperature, -1, 0);
    }

    public CompactorRecipe(Ingredient input, Holder<Item> output, int count, int pressure, int temperature,
                           int inputDamage, int outputDamage) {
        this.input = input;
        this.output = output;
        this.count = count;
        this.pressure = pressure;
        this.temperature = temperature;
        this.inputDamage = inputDamage;
        this.outputDamage = outputDamage;
    }

    @Override
    public boolean matches(SingleRecipeInput in, Level level) {
        return input.test(in.item()) && (inputDamage < 0 || in.item().getDamageValue() == inputDamage);
    }

    @Override
    public ItemStack assemble(SingleRecipeInput in) {
        return this.getResult();
    }

    public Ingredient getInput() {
        return input;
    }

    public ItemStack getResult() {
        ItemStack s = new ItemStack(output.value(), count);
        if (outputDamage != 0)
            s.setDamageValue(outputDamage);
        return s;
    }

    /** Required machine pressure in kPa. */
    public int getReqPressure() {
        return pressure;
    }

    /** Required machine temperature in C (may be negative, e.g. packing ice). */
    public int getReqTemperature() {
        return temperature;
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
        return RotaryRecipeSerializers.COMPACTOR.get();
    }

    @Override
    public RecipeType<? extends Recipe<SingleRecipeInput>> getType() {
        return RotaryRecipeTypes.COMPACTOR.get();
    }

    public static final MapCodec<CompactorRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.fieldOf("input").forGetter(r -> r.input),
            BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("output").forGetter(r -> r.output),
            Codec.INT.optionalFieldOf("count", 1).forGetter(r -> r.count),
            Codec.INT.fieldOf("pressure").forGetter(r -> r.pressure),
            Codec.INT.fieldOf("temperature").forGetter(r -> r.temperature),
            Codec.INT.optionalFieldOf("input_damage", -1).forGetter(r -> r.inputDamage),
            Codec.INT.optionalFieldOf("output_damage", 0).forGetter(r -> r.outputDamage)
    ).apply(inst, CompactorRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CompactorRecipe> STREAM_CODEC = StreamCodec.of(
            (buf, r) -> {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, r.input);
                ByteBufCodecs.holderRegistry(Registries.ITEM).encode(buf, r.output);
                buf.writeVarInt(r.count);
                buf.writeVarInt(r.pressure);
                buf.writeVarInt(r.temperature);
                buf.writeVarInt(r.inputDamage);
                buf.writeVarInt(r.outputDamage);
            },
            buf -> new CompactorRecipe(
                    Ingredient.CONTENTS_STREAM_CODEC.decode(buf),
                    ByteBufCodecs.holderRegistry(Registries.ITEM).decode(buf),
                    buf.readVarInt(),
                    buf.readVarInt(),
                    buf.readVarInt(),
                    buf.readVarInt(),
                    buf.readVarInt()));
}
