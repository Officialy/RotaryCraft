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

/**
 * One stage of the extractor's four-stage ore chain. {@code stage} selects which of
 * the machine's four processing slots the recipe runs in: 0 = ore→dust (drill),
 * 1 = dust→slurry (wash), 2 = slurry→solution (leach), 3 = solution→flakes (dry).
 */
public class ExtractorRecipe implements Recipe<SingleRecipeInput> {

    private final int stage;
    private final Ingredient input;
    private final ItemStackTemplate output;

    public ExtractorRecipe(int stage, Ingredient input, ItemStackTemplate output) {
        this.stage = stage;
        this.input = input;
        this.output = output;
    }

    @Override
    public boolean matches(SingleRecipeInput in, Level level) {
        return input.test(in.item());
    }

    public boolean matches(int stage, ItemStack in) {
        return this.stage == stage && input.test(in);
    }

    @Override
    public ItemStack assemble(SingleRecipeInput in) {
        return output.create();
    }

    public int getStage() {
        return stage;
    }

    public ItemStack getOutput() {
        return output.create();
    }

    public Ingredient getInput() {
        return input;
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
        return RotaryRecipeSerializers.EXTRACTOR.get();
    }

    @Override
    public RecipeType<? extends Recipe<SingleRecipeInput>> getType() {
        return RotaryRecipeTypes.EXTRACTOR.get();
    }

    public static final MapCodec<ExtractorRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.intRange(0, 3).fieldOf("stage").forGetter(r -> r.stage),
            Ingredient.CODEC.fieldOf("input").forGetter(r -> r.input),
            ItemStackTemplate.CODEC.fieldOf("output").forGetter(r -> r.output)
    ).apply(inst, ExtractorRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ExtractorRecipe> STREAM_CODEC = StreamCodec.of(
            (buf, r) -> {
                buf.writeVarInt(r.stage);
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, r.input);
                ItemStackTemplate.STREAM_CODEC.encode(buf, r.output);
            },
            buf -> {
                int stage = buf.readVarInt();
                Ingredient in = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
                ItemStackTemplate out = ItemStackTemplate.STREAM_CODEC.decode(buf);
                return new ExtractorRecipe(stage, in, out);
            }
    );
}
