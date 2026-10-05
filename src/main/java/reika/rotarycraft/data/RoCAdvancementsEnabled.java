package reika.rotarycraft.data;

import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.core.Holder;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import reika.rotarycraft.registry.ConfigRegistry;

/** The original shared achievements option also gates automatic inventory criteria. */
public record RoCAdvancementsEnabled() implements LootItemCondition {
    public static final MapCodec<RoCAdvancementsEnabled> CODEC = MapCodec.unit(new RoCAdvancementsEnabled());

    @Override public boolean test(LootContext context) { return ConfigRegistry.ACHIEVEMENTS.getState(); }
    @Override public MapCodec<? extends LootItemCondition> codec() { return CODEC; }

    public static Criterion<InventoryChangeTrigger.TriggerInstance> hasItem(ItemLike item) {
        var original = InventoryChangeTrigger.TriggerInstance.hasItems(item).triggerInstance();
        return net.minecraft.advancements.triggers.CriteriaTriggers.INVENTORY_CHANGED.createCriterion(
                new InventoryChangeTrigger.TriggerInstance(java.util.Optional.of(Holder.direct(new RoCAdvancementsEnabled())),
                        original.slots(), original.items()));
    }
}
