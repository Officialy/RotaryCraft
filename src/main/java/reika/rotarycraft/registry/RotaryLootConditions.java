package reika.rotarycraft.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.registries.DeferredRegister;
import reika.rotarycraft.data.RoCChestTierCondition;

public final class RotaryLootConditions {
    public static final DeferredRegister<MapCodec<? extends LootItemCondition>> CONDITIONS =
            DeferredRegister.create(BuiltInRegistries.LOOT_CONDITION_TYPE, "rotarycraft");
    static {
        CONDITIONS.register("chest_tier", () -> RoCChestTierCondition.CODEC);
        CONDITIONS.register("advancements_enabled", () -> reika.rotarycraft.data.RoCAdvancementsEnabled.CODEC);
    }
    private RotaryLootConditions() {}
}
