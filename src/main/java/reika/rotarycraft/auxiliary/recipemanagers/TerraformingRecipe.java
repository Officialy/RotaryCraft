package reika.rotarycraft.auxiliary.recipemanagers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import reika.rotarycraft.registry.RotaryRecipeSerializers;
import reika.rotarycraft.registry.RotaryRecipeTypes;

/** Original directed biome steps, resource costs and terrain profiles, all reloadable. */
public record TerraformingRecipe(TagKey<Biome> source, ResourceKey<Biome> target, int minPower, int water,
        List<ItemCost> items, Block sourceTop, Block sourceFiller, Block targetTop, Block targetFiller, int icon)
        implements Recipe<TerraformingRecipe.Input> {
    public TerraformingRecipe { items = List.copyOf(items); }
    public record ItemCost(Ingredient ingredient, float chance) {
        public static final Codec<ItemCost> CODEC = RecordCodecBuilder.create(i -> i.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(ItemCost::ingredient),
                Codec.floatRange(0, 1).fieldOf("chance").forGetter(ItemCost::chance)).apply(i, ItemCost::new));
        /** ItemReq used nextInt((int)(1/chance)), including its original quantization. */
        public boolean consume(RandomSource random) { return chance > 0 && random.nextInt(Math.max(1, (int)(1F / chance))) == 0; }
    }
    public static final MapCodec<TerraformingRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            TagKey.codec(Registries.BIOME).fieldOf("source").forGetter(TerraformingRecipe::source),
            ResourceKey.codec(Registries.BIOME).fieldOf("target").forGetter(TerraformingRecipe::target),
            Codec.intRange(1, Integer.MAX_VALUE).fieldOf("min_power").forGetter(TerraformingRecipe::minPower),
            Codec.intRange(0, 1500).fieldOf("water").forGetter(TerraformingRecipe::water),
            ItemCost.CODEC.listOf().fieldOf("items").forGetter(TerraformingRecipe::items),
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("source_top").forGetter(TerraformingRecipe::sourceTop),
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("source_filler").forGetter(TerraformingRecipe::sourceFiller),
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("target_top").forGetter(TerraformingRecipe::targetTop),
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("target_filler").forGetter(TerraformingRecipe::targetFiller),
            Codec.intRange(0, 255).optionalFieldOf("icon", 0).forGetter(TerraformingRecipe::icon)
    ).apply(i, TerraformingRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, TerraformingRecipe> STREAM_CODEC = StreamCodec.of(
            (buf, recipe) -> {
                buf.writeIdentifier(recipe.source.location()); buf.writeIdentifier(recipe.target.identifier());
                buf.writeVarInt(recipe.minPower); buf.writeVarInt(recipe.water); buf.writeVarInt(recipe.items.size());
                for (var cost : recipe.items) { Ingredient.CONTENTS_STREAM_CODEC.encode(buf, cost.ingredient); buf.writeFloat(cost.chance); }
                for (var block : List.of(recipe.sourceTop, recipe.sourceFiller, recipe.targetTop, recipe.targetFiller))
                    ByteBufCodecs.registry(Registries.BLOCK).encode(buf, block);
                buf.writeVarInt(recipe.icon);
            }, buf -> {
                var from = TagKey.create(Registries.BIOME, buf.readIdentifier());
                var to = ResourceKey.create(Registries.BIOME, buf.readIdentifier());
                int power = buf.readVarInt(), water = buf.readVarInt(), count = buf.readVarInt();
                if (count < 0 || count > 54) throw new IllegalArgumentException("Invalid Terraformer item count");
                var costs = new java.util.ArrayList<ItemCost>();
                for (int n = 0; n < count; n++) costs.add(new ItemCost(Ingredient.CONTENTS_STREAM_CODEC.decode(buf), buf.readFloat()));
                return new TerraformingRecipe(from, to, power, water, costs,
                        ByteBufCodecs.registry(Registries.BLOCK).decode(buf), ByteBufCodecs.registry(Registries.BLOCK).decode(buf),
                        ByteBufCodecs.registry(Registries.BLOCK).decode(buf), ByteBufCodecs.registry(Registries.BLOCK).decode(buf), buf.readVarInt());
            });
    @Override public boolean matches(Input in, Level level) { return in.biome.is(source); }
    // A biome operation has no item result and is not a vanilla crafting-book recipe.
    @Override public ItemStack assemble(Input in) { return ItemStack.EMPTY; }
    @Override public PlacementInfo placementInfo() { return PlacementInfo.NOT_PLACEABLE; }
    @Override public boolean showNotification() { return false; }
    @Override public String group() { return ""; }
    @Override public RecipeBookCategory recipeBookCategory() { return RecipeBookCategories.CRAFTING_MISC; }
    @Override public RecipeSerializer<TerraformingRecipe> getSerializer() { return RotaryRecipeSerializers.TERRAFORMER.get(); }
    @Override public RecipeType<TerraformingRecipe> getType() { return RotaryRecipeTypes.TERRAFORMER.get(); }
    public record Input(Holder<Biome> biome) implements RecipeInput {
        @Override public ItemStack getItem(int slot) { throw new IndexOutOfBoundsException("Terraforming has no crafting slots"); }
        @Override public int size() { return 0; }
        @Override public boolean isEmpty() { return false; }
    }
}
