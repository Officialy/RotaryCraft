package reika.rotarycraft.auxiliary.recipemanagers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import reika.rotarycraft.registry.RotaryRecipeSerializers;

/** Furnace recipe that prefers an installed mod's tagged product, with a V33a RotaryCraft fallback. */
public final class TaggedSmeltingRecipe extends SmeltingRecipe {
    private final TagKey<Item> resultTag;
    public TaggedSmeltingRecipe(CommonInfo common, CookingBookInfo book, Ingredient input, ItemStackTemplate fallback,
                                float xp, int time, TagKey<Item> resultTag) {
        super(common, book, input, fallback, xp, time);
        this.resultTag = resultTag;
    }
    public TagKey<Item> resultTag() { return resultTag; }
    public ItemStackTemplate fallback() { return super.result(); }
    @Override protected ItemStackTemplate result() {
        var fallback = fallback();
        // Stable ordering avoids changes when registries or tag members are enumerated differently.
        return java.util.stream.StreamSupport.stream(BuiltInRegistries.ITEM.getTagOrEmpty(resultTag).spliterator(), false)
                .filter(holder -> !BuiltInRegistries.ITEM.getKey(holder.value()).getNamespace().equals("rotarycraft"))
                .sorted(java.util.Comparator.comparing(holder -> BuiltInRegistries.ITEM.getKey(holder.value()).toString()))
                .map(holder -> new ItemStackTemplate(holder, fallback.count(), fallback.components()))
                .findFirst().orElse(fallback);
    }
    @Override public ItemStack assemble(SingleRecipeInput input) { return result().create(); }
    @Override public RecipeSerializer<SmeltingRecipe> getSerializer() { return RotaryRecipeSerializers.TAGGED_SMELTING.get(); }

    private static TaggedSmeltingRecipe tagged(SmeltingRecipe recipe) { return (TaggedSmeltingRecipe)recipe; }
    public static final MapCodec<SmeltingRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Recipe.CommonInfo.MAP_CODEC.forGetter(r -> tagged(r).commonInfo),
            CookingBookInfo.MAP_CODEC.forGetter(r -> tagged(r).bookInfo),
            Ingredient.CODEC.fieldOf("ingredient").forGetter(SmeltingRecipe::input),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(r -> tagged(r).fallback()),
            Codec.FLOAT.optionalFieldOf("experience", 0F).forGetter(SmeltingRecipe::experience),
            Codec.intRange(1, Integer.MAX_VALUE).fieldOf("cookingtime").forGetter(SmeltingRecipe::cookingTime),
            TagKey.codec(Registries.ITEM).fieldOf("result_tag").forGetter(r -> tagged(r).resultTag)
    ).apply(i, TaggedSmeltingRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, SmeltingRecipe> STREAM_CODEC = StreamCodec.of(
            (buf, recipe) -> {
                var r = tagged(recipe);
                Recipe.CommonInfo.STREAM_CODEC.encode(buf, r.commonInfo);
                CookingBookInfo.STREAM_CODEC.encode(buf, r.bookInfo);
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, r.input());
                ItemStackTemplate.STREAM_CODEC.encode(buf, r.fallback());
                buf.writeFloat(r.experience()); buf.writeVarInt(r.cookingTime());
                buf.writeIdentifier(r.resultTag.location());
            }, buf -> new TaggedSmeltingRecipe(Recipe.CommonInfo.STREAM_CODEC.decode(buf), CookingBookInfo.STREAM_CODEC.decode(buf),
                    Ingredient.CONTENTS_STREAM_CODEC.decode(buf), ItemStackTemplate.STREAM_CODEC.decode(buf),
                    buf.readFloat(), buf.readVarInt(), TagKey.create(Registries.ITEM, buf.readIdentifier())));
}
