package reika.rotarycraft.auxiliary.recipemanagers;

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
import reika.rotarycraft.registry.RotaryRecipeSerializers;
import reika.rotarycraft.registry.RotaryRecipeTypes;

import java.util.List;

/**
 * Fermenter recipe: catalyst (top slot) + input (middle slot) + water → output.
 * Sugar + dirt → yeast; yeast + plant matter → sludge, with the output count
 * carrying the legacy MulchMaterials plant value.
 */
public class FermenterRecipe implements Recipe<FermenterRecipe.FermenterInput> {

    private final Ingredient catalyst;
    private final Ingredient input;
    private final ItemStackTemplate output;

    public FermenterRecipe(Ingredient catalyst, Ingredient input, ItemStackTemplate output) {
        this.catalyst = catalyst;
        this.input = input;
        this.output = output;
    }

    public record FermenterInput(ItemStack catalyst, ItemStack input) implements RecipeInput {
        @Override
        public ItemStack getItem(int slot) {
            return switch (slot) {
                case 0 -> catalyst;
                case 1 -> input;
                default -> throw new IllegalArgumentException("No slot " + slot);
            };
        }

        @Override
        public int size() {
            return 2;
        }
    }

    @Override
    public boolean matches(FermenterInput in, Level level) {
        return catalyst.test(in.catalyst()) && input.test(in.input());
    }

    @Override
    public ItemStack assemble(FermenterInput in) {
        return output.create();
    }

    public ItemStack getOutput() {
        return output.create();
    }

    public Ingredient getCatalyst() {
        return catalyst;
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
        return PlacementInfo.create(List.of(catalyst, input));
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    @Override
    public RecipeSerializer<? extends Recipe<FermenterInput>> getSerializer() {
        return RotaryRecipeSerializers.FERMENTER.get();
    }

    @Override
    public RecipeType<? extends Recipe<FermenterInput>> getType() {
        return RotaryRecipeTypes.FERMENTER.get();
    }

    public static final MapCodec<FermenterRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.fieldOf("catalyst").forGetter(r -> r.catalyst),
            Ingredient.CODEC.fieldOf("input").forGetter(r -> r.input),
            ItemStackTemplate.CODEC.fieldOf("output").forGetter(r -> r.output)
    ).apply(inst, FermenterRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, FermenterRecipe> STREAM_CODEC = StreamCodec.of(
            (buf, r) -> {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, r.catalyst);
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, r.input);
                ItemStackTemplate.STREAM_CODEC.encode(buf, r.output);
            },
            buf -> {
                Ingredient cat = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
                Ingredient in = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
                ItemStackTemplate out = ItemStackTemplate.STREAM_CODEC.decode(buf);
                return new FermenterRecipe(cat, in, out);
            }
    );
}
