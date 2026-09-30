package reika.rotarycraft.auxiliary.recipemanagers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import reika.rotarycraft.registry.RotaryRecipeSerializers;

import java.util.List;

/** Keeps V33a's multi-item yields for items that must remain individually stored. */
public final class BulkShapedRecipe extends ShapedRecipe {
    public static final MapCodec<ShapedRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Recipe.CommonInfo.MAP_CODEC.forGetter(recipe -> ((BulkShapedRecipe) recipe).commonInfo),
            CraftingRecipe.CraftingBookInfo.MAP_CODEC.forGetter(recipe -> ((BulkShapedRecipe) recipe).bookInfo),
            ShapedRecipePattern.MAP_CODEC.forGetter(recipe -> recipe.pattern),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(recipe -> ((BulkShapedRecipe) recipe).singleResult),
            ExtraCodecs.intRange(2, 99).fieldOf("output_count").forGetter(recipe -> ((BulkShapedRecipe) recipe).outputCount)
    ).apply(instance, BulkShapedRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ShapedRecipe> STREAM_CODEC = StreamCodec.composite(
            Recipe.CommonInfo.STREAM_CODEC, recipe -> ((BulkShapedRecipe) recipe).commonInfo,
            CraftingRecipe.CraftingBookInfo.STREAM_CODEC, recipe -> ((BulkShapedRecipe) recipe).bookInfo,
            ShapedRecipePattern.STREAM_CODEC, recipe -> recipe.pattern,
            ItemStackTemplate.STREAM_CODEC, recipe -> ((BulkShapedRecipe) recipe).singleResult,
            ByteBufCodecs.VAR_INT, recipe -> ((BulkShapedRecipe) recipe).outputCount,
            BulkShapedRecipe::new);

    private final ItemStackTemplate singleResult;
    private final int outputCount;

    public BulkShapedRecipe(Recipe.CommonInfo common, CraftingRecipe.CraftingBookInfo book,
            ShapedRecipePattern pattern, ItemStackTemplate result, int count) {
        super(common, book, pattern, result.withCount(1));
        singleResult = result.withCount(1);
        outputCount = count;
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        ItemStack result = singleResult.create();
        // A crafting result can contain several non-stackable items. Vanilla pickup divides them
        // among inventory slots; do not enlarge the items' actual stack limit or change their data.
        if (!result.isEmpty()) result.setCount(outputCount);
        return result;
    }

    @Override
    public RecipeSerializer<ShapedRecipe> getSerializer() {
        return RotaryRecipeSerializers.BULK_SHAPED.get();
    }

    @Override
    public List<RecipeDisplay> display() {
        // Display templates validate counts against stack limits. Only the recipe-book icon gets
        // a larger display limit; assemble returns the original item components and stack limit.
        ItemStack icon = singleResult.create();
        icon.setCount(outputCount);
        if (icon.getMaxStackSize() < outputCount) icon.set(DataComponents.MAX_STACK_SIZE, outputCount);
        return List.of(new ShapedCraftingRecipeDisplay(pattern.width(), pattern.height(),
                pattern.ingredients().stream().map(ingredient -> ingredient.map(Ingredient::display)
                        .orElse(SlotDisplay.Empty.INSTANCE)).toList(),
                new SlotDisplay.ItemStackSlotDisplay(ItemStackTemplate.fromNonEmptyStack(icon)),
                new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)));
    }
}
