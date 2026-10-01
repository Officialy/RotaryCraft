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
import java.util.Optional;
import java.util.List;

/**
 * One stage of the extractor's four-stage ore chain. {@code stage} selects which of
 * the machine's four processing slots the recipe runs in: 0 = ore→dust (drill),
 * 1 = dust→slurry (wash), 2 = slurry→solution (leach), 3 = solution→flakes (dry).
 */
public class ExtractorRecipe implements Recipe<SingleRecipeInput> {

    private final int stage;
    private final Ingredient input;
    private final ItemStackTemplate output;
    private final Optional<Double> duplicationChance;
    private final Optional<Double> oreDuplicationChance;
    private final int priority;
    private final List<ExtractorBonusOutput> bonuses;

    public ExtractorRecipe(int stage, Ingredient input, ItemStackTemplate output) {
        this(stage, input, output, Optional.empty());
    }

    public ExtractorRecipe(int stage, Ingredient input, ItemStackTemplate output, Optional<Double> duplicationChance) {
        this(stage, input, output, duplicationChance, Optional.empty(), 0, List.of());
    }

    public ExtractorRecipe(int stage, Ingredient input, ItemStackTemplate output, Optional<Double> duplicationChance,
                           Optional<Double> oreDuplicationChance, int priority, List<ExtractorBonusOutput> bonuses) {
        if (stage < 0 || stage > 3) throw new IllegalArgumentException("Extractor stage must be between zero and three");
        if (java.util.stream.Stream.of(duplicationChance, oreDuplicationChance).flatMap(Optional::stream)
                .anyMatch(chance -> !Double.isFinite(chance) || chance < 0 || chance > 1))
            throw new IllegalArgumentException("Extractor duplication chance must be between zero and one");
        this.stage = stage;
        this.input = input;
        this.output = output;
        this.duplicationChance = duplicationChance;
        this.oreDuplicationChance = oreDuplicationChance;
        this.priority = priority;
        this.bonuses = List.copyOf(bonuses);
    }

    /** Empty preserves the original ore/rarity/bedrock behavior; an explicit rate overrides it. */
    public Optional<Double> getDuplicationChance() { return duplicationChance; }
    /** Applies after the bedrock stage-zero guarantee, preserving rare/Nether rates across the whole chain. */
    public Optional<Double> getOreDuplicationChance() { return oreDuplicationChance; }
    public int getPriority() { return priority; }
    public List<ExtractorBonusOutput> getBonuses() { return bonuses; }
    public Optional<ExtractorBonusOutput> getAvailableBonus() { return bonuses.stream().filter(ExtractorBonusOutput::isAvailable).findFirst(); }

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
            ItemStackTemplate.CODEC.fieldOf("output").forGetter(r -> r.output),
            Codec.doubleRange(0, 1).optionalFieldOf("duplication_chance").forGetter(r -> r.duplicationChance),
            Codec.doubleRange(0, 1).optionalFieldOf("ore_duplication_chance").forGetter(r -> r.oreDuplicationChance),
            Codec.INT.optionalFieldOf("priority", 0).forGetter(r -> r.priority),
            ExtractorBonusOutput.CODEC.listOf().optionalFieldOf("bonuses", List.of()).forGetter(r -> r.bonuses)
    ).apply(inst, ExtractorRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ExtractorRecipe> STREAM_CODEC = StreamCodec.of(
            (buf, r) -> {
                buf.writeVarInt(r.stage);
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, r.input);
                ItemStackTemplate.STREAM_CODEC.encode(buf, r.output);
                buf.writeBoolean(r.duplicationChance.isPresent());
                r.duplicationChance.ifPresent(buf::writeDouble);
                buf.writeBoolean(r.oreDuplicationChance.isPresent());
                r.oreDuplicationChance.ifPresent(buf::writeDouble);
                buf.writeVarInt(r.priority);
                buf.writeVarInt(r.bonuses.size());
                r.bonuses.forEach(bonus -> ExtractorBonusOutput.STREAM_CODEC.encode(buf, bonus));
            },
            buf -> {
                int stage = buf.readVarInt();
                Ingredient in = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
                ItemStackTemplate out = ItemStackTemplate.STREAM_CODEC.decode(buf);
                Optional<Double> chance = buf.readBoolean() ? Optional.of(buf.readDouble()) : Optional.empty();
                Optional<Double> oreChance = buf.readBoolean() ? Optional.of(buf.readDouble()) : Optional.empty();
                int priority = buf.readVarInt();
                int count = buf.readVarInt();
                if (count < 0 || count > 1024) throw new IllegalArgumentException("Invalid extractor bonus count");
                var bonuses = new java.util.ArrayList<ExtractorBonusOutput>();
                for (int i = 0; i < count; i++) bonuses.add(ExtractorBonusOutput.STREAM_CODEC.decode(buf));
                return new ExtractorRecipe(stage, in, out, chance, oreChance, priority, bonuses);
            }
    );
}
