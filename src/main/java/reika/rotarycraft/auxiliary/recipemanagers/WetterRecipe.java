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
import net.minecraft.world.level.material.Fluid;

import reika.rotarycraft.registry.RotaryRecipeSerializers;
import reika.rotarycraft.registry.RotaryRecipeTypes;

/**
 * Wetter soaking: an item bathed in {@code amount} mB of a specific fluid for {@code duration}
 * ticks becomes another item (sand + lubricant -> soul sand, cobblestone + jet fuel ->
 * netherrack). Data-driven port of the legacy {@code RecipesWetter}.
 */
public class WetterRecipe implements Recipe<SingleRecipeInput> {

    private final Ingredient input;
    private final Holder<Fluid> fluid;
    private final int amount;
    private final Holder<Item> output;
    private final int duration;

    public WetterRecipe(Ingredient input, Holder<Fluid> fluid, int amount, Holder<Item> output, int duration) {
        this.input = input;
        this.fluid = fluid;
        this.amount = amount;
        this.output = output;
        this.duration = duration;
    }

    @Override
    public boolean matches(SingleRecipeInput in, Level level) {
        return input.test(in.item());
    }

    public boolean matchesWith(ItemStack is, Fluid f) {
        return input.test(is) && fluid.value().isSame(f);
    }

    @Override
    public ItemStack assemble(SingleRecipeInput in) {
        return this.getResult();
    }

    public ItemStack getResult() {
        return new ItemStack(output.value());
    }

    public Ingredient getInput() {
        return input;
    }

    public Fluid getFluid() {
        return fluid.value();
    }

    /** mB consumed per conversion. */
    public int getAmount() {
        return amount;
    }

    /** Base soak time in ticks (shrinks with speed). */
    public int getDuration() {
        return duration;
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
        return RotaryRecipeSerializers.WETTER.get();
    }

    @Override
    public RecipeType<? extends Recipe<SingleRecipeInput>> getType() {
        return RotaryRecipeTypes.WETTER.get();
    }

    public static final MapCodec<WetterRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.fieldOf("input").forGetter(r -> r.input),
            BuiltInRegistries.FLUID.holderByNameCodec().fieldOf("fluid").forGetter(r -> r.fluid),
            Codec.INT.fieldOf("amount").forGetter(r -> r.amount),
            BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("output").forGetter(r -> r.output),
            Codec.INT.fieldOf("duration").forGetter(r -> r.duration)
    ).apply(inst, WetterRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, WetterRecipe> STREAM_CODEC = StreamCodec.of(
            (buf, r) -> {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, r.input);
                ByteBufCodecs.holderRegistry(Registries.FLUID).encode(buf, r.fluid);
                buf.writeVarInt(r.amount);
                ByteBufCodecs.holderRegistry(Registries.ITEM).encode(buf, r.output);
                buf.writeVarInt(r.duration);
            },
            buf -> new WetterRecipe(
                    Ingredient.CONTENTS_STREAM_CODEC.decode(buf),
                    ByteBufCodecs.holderRegistry(Registries.FLUID).decode(buf),
                    buf.readVarInt(),
                    ByteBufCodecs.holderRegistry(Registries.ITEM).decode(buf),
                    buf.readVarInt()));
}
