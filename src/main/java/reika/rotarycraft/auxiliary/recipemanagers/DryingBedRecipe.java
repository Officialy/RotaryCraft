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
 * Drying Bed evaporation: {@code amount} mB of a fluid dries into an item (water -> salt,
 * lava -> gold nugget). Data-driven port of the legacy {@code RecipesDryingBed}. The input is a
 * fluid, so matches() is fluid-driven (the SingleRecipeInput item is unused).
 */
public class DryingBedRecipe implements Recipe<SingleRecipeInput> {

    private final Holder<Fluid> fluid;
    private final int amount;
    private final Holder<Item> output;
    private final int count;

    public DryingBedRecipe(Holder<Fluid> fluid, int amount, Holder<Item> output, int count) {
        this.fluid = fluid;
        this.amount = amount;
        this.output = output;
        this.count = count;
    }

    @Override
    public boolean matches(SingleRecipeInput in, Level level) {
        return false;
    }

    public boolean matchesFluid(Fluid f) {
        return fluid.value().isSame(f);
    }

    @Override
    public ItemStack assemble(SingleRecipeInput in) {
        return this.getResult();
    }

    public ItemStack getResult() {
        return new ItemStack(output.value(), count);
    }

    public Fluid getFluid() {
        return fluid.value();
    }

    /** mB consumed per item produced. */
    public int getConsumption() {
        return amount;
    }

    @Override public boolean isSpecial() { return true; }

    @Override public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    @Override
    public RecipeSerializer<? extends Recipe<SingleRecipeInput>> getSerializer() {
        return RotaryRecipeSerializers.DRYING_BED.get();
    }

    @Override
    public RecipeType<? extends Recipe<SingleRecipeInput>> getType() {
        return RotaryRecipeTypes.DRYING_BED.get();
    }

    public static final MapCodec<DryingBedRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            BuiltInRegistries.FLUID.holderByNameCodec().fieldOf("fluid").forGetter(r -> r.fluid),
            Codec.INT.fieldOf("amount").forGetter(r -> r.amount),
            BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("output").forGetter(r -> r.output),
            Codec.INT.optionalFieldOf("count", 1).forGetter(r -> r.count)
    ).apply(inst, DryingBedRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, DryingBedRecipe> STREAM_CODEC = StreamCodec.of(
            (buf, r) -> {
                ByteBufCodecs.holderRegistry(Registries.FLUID).encode(buf, r.fluid);
                buf.writeVarInt(r.amount);
                ByteBufCodecs.holderRegistry(Registries.ITEM).encode(buf, r.output);
                buf.writeVarInt(r.count);
            },
            buf -> new DryingBedRecipe(
                    ByteBufCodecs.holderRegistry(Registries.FLUID).decode(buf),
                    buf.readVarInt(),
                    ByteBufCodecs.holderRegistry(Registries.ITEM).decode(buf),
                    buf.readVarInt()));
}
