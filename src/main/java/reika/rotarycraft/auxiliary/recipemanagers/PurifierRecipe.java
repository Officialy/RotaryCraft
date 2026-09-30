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
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import reika.rotarycraft.registry.RotaryRecipeSerializers;
import reika.rotarycraft.registry.RotaryRecipeTypes;

/** V33a steel purification: one ingot from each occupied matching input slot. */
public record PurifierRecipe(Ingredient input, Ingredient gunpowder, Ingredient sand, Holder<Item> output,
        int temperature, int gunpowderConsumption, int sandConsumption) implements Recipe<SingleRecipeInput> {
    public static final MapCodec<PurifierRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("input").forGetter(PurifierRecipe::input),
            Ingredient.CODEC.fieldOf("gunpowder").forGetter(PurifierRecipe::gunpowder),
            Ingredient.CODEC.fieldOf("sand").forGetter(PurifierRecipe::sand),
            BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("output").forGetter(PurifierRecipe::output),
            Codec.intRange(0, 1000).fieldOf("temperature").forGetter(PurifierRecipe::temperature),
            Codec.intRange(1, Integer.MAX_VALUE).fieldOf("gunpowder_consumption").forGetter(PurifierRecipe::gunpowderConsumption),
            Codec.intRange(1, Integer.MAX_VALUE).fieldOf("sand_consumption").forGetter(PurifierRecipe::sandConsumption)
    ).apply(instance, PurifierRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PurifierRecipe> STREAM_CODEC = StreamCodec.of(
            (buf, recipe) -> {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.input);
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.gunpowder);
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.sand);
                ByteBufCodecs.holderRegistry(Registries.ITEM).encode(buf, recipe.output);
                buf.writeVarInt(recipe.temperature);
                buf.writeVarInt(recipe.gunpowderConsumption);
                buf.writeVarInt(recipe.sandConsumption);
            }, buf -> new PurifierRecipe(Ingredient.CONTENTS_STREAM_CODEC.decode(buf),
                    Ingredient.CONTENTS_STREAM_CODEC.decode(buf), Ingredient.CONTENTS_STREAM_CODEC.decode(buf),
                    ByteBufCodecs.holderRegistry(Registries.ITEM).decode(buf), buf.readVarInt(), buf.readVarInt(), buf.readVarInt()));

    @Override public boolean matches(SingleRecipeInput in, Level level) { return input.test(in.item()); }
    @Override public ItemStack assemble(SingleRecipeInput in) { return getResult(); }
    public ItemStack getResult() { return new ItemStack(output.value()); }
    @Override public boolean showNotification() { return false; }
    @Override public String group() { return ""; }
    @Override public PlacementInfo placementInfo() { return PlacementInfo.create(input); }
    @Override public RecipeBookCategory recipeBookCategory() { return RecipeBookCategories.CRAFTING_MISC; }
    @Override public RecipeSerializer<? extends Recipe<SingleRecipeInput>> getSerializer() { return RotaryRecipeSerializers.PURIFIER.get(); }
    @Override public RecipeType<? extends Recipe<SingleRecipeInput>> getType() { return RotaryRecipeTypes.PURIFIER.get(); }
}
