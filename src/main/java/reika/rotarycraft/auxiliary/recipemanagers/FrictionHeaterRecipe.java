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
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import reika.rotarycraft.registry.RotaryRecipeSerializers;
import reika.rotarycraft.registry.RotaryRecipeTypes;

/** High-temperature smelt performed by the friction heater on an adjacent furnace's input. */
public class FrictionHeaterRecipe implements Recipe<SingleRecipeInput> {

    private final Ingredient input;
    private final ItemStackTemplate output;
    private final float temperature;
    private final int duration;

    public FrictionHeaterRecipe(Ingredient input, ItemStackTemplate output, float temperature, int duration) {
        this.input = input;
        this.output = output;
        this.temperature = temperature;
        this.duration = duration;
    }

    @Override
    public boolean matches(SingleRecipeInput in, Level level) {
        return input.test(in.item());
    }

    @Override
    public ItemStack assemble(SingleRecipeInput in) {
        return output.create();
    }

    public ItemStack getOutput() {
        return output.create();
    }

    public float requiredTemperature() {
        return temperature;
    }

    public int duration() {
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
        return RecipeBookCategories.FURNACE_MISC;
    }

    @Override
    public RecipeSerializer<? extends Recipe<SingleRecipeInput>> getSerializer() {
        return RotaryRecipeSerializers.FRICTION_HEATER.get();
    }

    @Override
    public RecipeType<? extends Recipe<SingleRecipeInput>> getType() {
        return RotaryRecipeTypes.FRICTION_HEATER.get();
    }

    public static final MapCodec<FrictionHeaterRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.fieldOf("input").forGetter(r -> r.input),
            ItemStackTemplate.CODEC.fieldOf("output").forGetter(r -> r.output),
            Codec.FLOAT.fieldOf("temperature").forGetter(r -> r.temperature),
            Codec.INT.optionalFieldOf("duration", 200).forGetter(r -> r.duration)
    ).apply(inst, FrictionHeaterRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, FrictionHeaterRecipe> STREAM_CODEC = StreamCodec.of(
            (buf, r) -> {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, r.input);
                ItemStackTemplate.STREAM_CODEC.encode(buf, r.output);
                buf.writeFloat(r.temperature);
                buf.writeVarInt(r.duration);
            },
            buf -> {
                Ingredient in = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
                ItemStackTemplate out = ItemStackTemplate.STREAM_CODEC.decode(buf);
                float temp = buf.readFloat();
                int dur = buf.readVarInt();
                return new FrictionHeaterRecipe(in, out, temp, dur);
            }
    );
}
