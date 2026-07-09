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
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

import reika.rotarycraft.registry.RotaryRecipeSerializers;
import reika.rotarycraft.registry.RotaryRecipeTypes;

/**
 * Melts a solid into a fluid in the Lava Maker: an input item becomes {@code amount} mB of a fluid,
 * gated on the machine reaching {@code meltTemperature} (C) and having accumulated {@code meltEnergy}
 * joules of shaft work. Fluid stored as holder+amount (a bare FluidStack trips "Components not bound"
 * during datagen). Data-driven port of the legacy {@code RecipesLavaMaker}.
 */
public class LavaMakerRecipe implements Recipe<SingleRecipeInput> {

    private final Ingredient input;
    private final Holder<Fluid> fluid;
    private final int amount;
    private final int meltTemperature;
    private final long meltEnergy;

    public LavaMakerRecipe(Ingredient input, Holder<Fluid> fluid, int amount, int meltTemperature, long meltEnergy) {
        this.input = input;
        this.fluid = fluid;
        this.amount = amount;
        this.meltTemperature = meltTemperature;
        this.meltEnergy = meltEnergy;
    }

    @Override
    public boolean matches(SingleRecipeInput in, Level level) {
        return input.test(in.item());
    }

    @Override
    public ItemStack assemble(SingleRecipeInput in) {
        return ItemStack.EMPTY;
    }

    public Ingredient getInput() {
        return input;
    }

    public FluidStack getFluid() {
        return new FluidStack(fluid.value(), amount);
    }

    public int getMeltTemperature() {
        return meltTemperature;
    }

    public long getMeltEnergy() {
        return meltEnergy;
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
        return RotaryRecipeSerializers.LAVA_MAKER.get();
    }

    @Override
    public RecipeType<? extends Recipe<SingleRecipeInput>> getType() {
        return RotaryRecipeTypes.LAVA_MAKER.get();
    }

    public static final MapCodec<LavaMakerRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.fieldOf("input").forGetter(r -> r.input),
            BuiltInRegistries.FLUID.holderByNameCodec().fieldOf("fluid").forGetter(r -> r.fluid),
            Codec.INT.fieldOf("amount").forGetter(r -> r.amount),
            Codec.INT.fieldOf("temperature").forGetter(r -> r.meltTemperature),
            Codec.LONG.fieldOf("energy").forGetter(r -> r.meltEnergy)
    ).apply(inst, LavaMakerRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, LavaMakerRecipe> STREAM_CODEC = StreamCodec.of(
            (buf, r) -> {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, r.input);
                ByteBufCodecs.holderRegistry(Registries.FLUID).encode(buf, r.fluid);
                buf.writeVarInt(r.amount);
                buf.writeVarInt(r.meltTemperature);
                buf.writeVarLong(r.meltEnergy);
            },
            buf -> new LavaMakerRecipe(
                    Ingredient.CONTENTS_STREAM_CODEC.decode(buf),
                    ByteBufCodecs.holderRegistry(Registries.FLUID).decode(buf),
                    buf.readVarInt(),
                    buf.readVarInt(),
                    buf.readVarLong()));
}
