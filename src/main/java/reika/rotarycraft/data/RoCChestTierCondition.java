package reika.rotarycraft.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import reika.rotarycraft.registry.ConfigRegistry;

/** Keep V33a's chest tiers configurable while the entries themselves live in generated data. */
public record RoCChestTierCondition(int tier) implements LootItemCondition {
    public static final MapCodec<RoCChestTierCondition> CODEC =
            Codec.intRange(1, 4).fieldOf("tier").xmap(RoCChestTierCondition::new, RoCChestTierCondition::tier);

    @Override
    public boolean test(LootContext context) {
        return ConfigRegistry.CHESTGEN.getValue() >= tier;
    }

    @Override
    public MapCodec<? extends LootItemCondition> codec() {
        return CODEC;
    }
}
