package reika.rotarycraft.auxiliary.recipemanagers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;

/** Ordered secondary-product alternative. An optional ore tag replaces legacy mod-presence gates. */
public record ExtractorBonusOutput(ItemStackTemplate output, double chance, Optional<TagKey<Item>> requiredOre) {
    public ExtractorBonusOutput {
        if (!Double.isFinite(chance) || chance < 0 || chance > 1)
            throw new IllegalArgumentException("Extractor bonus chance must be between zero and one");
    }
    public boolean isAvailable() {
        return chance > 0 && requiredOre.map(tag -> BuiltInRegistries.ITEM.getTagOrEmpty(tag).iterator().hasNext()).orElse(true);
    }
    public static final Codec<ExtractorBonusOutput> CODEC = RecordCodecBuilder.create(i -> i.group(
            ItemStackTemplate.CODEC.fieldOf("output").forGetter(ExtractorBonusOutput::output),
            Codec.doubleRange(0, 1).fieldOf("chance").forGetter(ExtractorBonusOutput::chance),
            TagKey.codec(Registries.ITEM).optionalFieldOf("required_ore").forGetter(ExtractorBonusOutput::requiredOre)
    ).apply(i, ExtractorBonusOutput::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ExtractorBonusOutput> STREAM_CODEC = StreamCodec.of(
            (buf, bonus) -> {
                ItemStackTemplate.STREAM_CODEC.encode(buf, bonus.output);
                buf.writeDouble(bonus.chance);
                buf.writeBoolean(bonus.requiredOre.isPresent());
                bonus.requiredOre.ifPresent(tag -> buf.writeIdentifier(tag.location()));
            }, buf -> new ExtractorBonusOutput(ItemStackTemplate.STREAM_CODEC.decode(buf), buf.readDouble(),
                    buf.readBoolean() ? Optional.of(TagKey.create(Registries.ITEM, buf.readIdentifier())) : Optional.empty()));
}
