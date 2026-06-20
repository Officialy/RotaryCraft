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
import reika.dragonapi.interfaces.IHeatRecipe;
import reika.rotarycraft.registry.RotaryRecipeSerializers;
import reika.rotarycraft.registry.RotaryRecipeTypes;

import java.util.List;

/**
 * A single-input smelt/recycle recipe for the pulse-jet furnace. Each recipe has a minimum
 * {@link #requiredTemperature() temperature} the furnace must reach before it will process the
 * input. Ported from the 1.7.10 {@code RecipesPulseFurnace.PulseJetRecipe}; the output is stored
 * as a deferred {@link ItemStackTemplate} so the recipe can be built during datagen before item
 * components are bound (matching the blast-furnace recipes).
 */
public class PulseFurnaceRecipe implements Recipe<RecipeInput>, IHeatRecipe {

    private final Ingredient input;
    private final ItemStackTemplate output;
    private final float operatingTemperature;

    public PulseFurnaceRecipe(Ingredient input, ItemStackTemplate output, float temperature) {
        this.input = input;
        this.output = output;
        this.operatingTemperature = temperature;
    }

    public Ingredient getInput() {
        return input;
    }

    @Override
    public boolean matches(RecipeInput in, Level lvl) {
        if (lvl.isClientSide()) return false;
        ItemStack slot = in.getItem(0);
        return !slot.isEmpty() && input.test(slot);
    }

    @Override
    public ItemStack assemble(RecipeInput in) {
        return output.create();
    }

    public ItemStack getOutput() {
        return output.create();
    }

    @Override
    public float requiredTemperature() {
        return operatingTemperature;
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
        return PlacementInfo.create(List.of(input));
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.BLAST_FURNACE_MISC;
    }

    @Override
    public RecipeSerializer<? extends Recipe<RecipeInput>> getSerializer() {
        return RotaryRecipeSerializers.PULSE_FURNACE.get();
    }

    @Override
    public RecipeType<? extends Recipe<RecipeInput>> getType() {
        return RotaryRecipeTypes.PULSE_FURNACE.get();
    }

    public static final MapCodec<PulseFurnaceRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.fieldOf("input").forGetter(r -> r.input),
            ItemStackTemplate.CODEC.fieldOf("output").forGetter(r -> r.output),
            Codec.FLOAT.fieldOf("temperature").forGetter(r -> r.operatingTemperature)
    ).apply(inst, PulseFurnaceRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PulseFurnaceRecipe> STREAM_CODEC = StreamCodec.of(
            (buf, r) -> {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, r.input);
                ItemStackTemplate.STREAM_CODEC.encode(buf, r.output);
                buf.writeFloat(r.operatingTemperature);
            },
            buf -> {
                Ingredient in = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
                ItemStackTemplate out = ItemStackTemplate.STREAM_CODEC.decode(buf);
                float temp = buf.readFloat();
                return new PulseFurnaceRecipe(in, out, temp);
            }
    );
}
