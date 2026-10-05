package reika.rotarycraft.data;

import java.util.Locale;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import reika.rotarycraft.registry.RotaryItems;

/** Every live V33a RotaryChests entry, with its original tier, count and weight. */
public record RoCChestLoot(LootTableSubProvider.Context context) implements LootTableSubProvider {
    public enum Location {
        BONUS(BuiltInLootTables.SPAWN_BONUS_CHEST, 1, 1),
        DUNGEON(BuiltInLootTables.SIMPLE_DUNGEON, 1, 3),
        MINESHAFT(BuiltInLootTables.ABANDONED_MINESHAFT, 1, 1),
        STRONGHOLD_HALLWAY(BuiltInLootTables.STRONGHOLD_CORRIDOR, 2, 3),
        STRONGHOLD_CROSSING(BuiltInLootTables.STRONGHOLD_CROSSING, 1, 4),
        STRONGHOLD_LIBRARY(BuiltInLootTables.STRONGHOLD_LIBRARY, 2, 10),
        VILLAGE(BuiltInLootTables.VILLAGE_WEAPONSMITH, 3, 8);

        public final ResourceKey<LootTable> target;
        public final ResourceKey<LootTable> subtable;
        private final int minRolls;
        private final int maxRolls;

        Location(ResourceKey<LootTable> target, int minRolls, int maxRolls) {
            this.target = target;
            this.minRolls = minRolls;
            this.maxRolls = maxRolls;
            subtable = ResourceKey.create(Registries.LOOT_TABLE,
                    Identifier.fromNamespaceAndPath("rotarycraft", "chests/injected/" + name().toLowerCase(Locale.ROOT)));
        }
    }

    @Override
    public void run() {
        for (Location location : Location.values()) {
            var pool = LootPool.lootPool().setRolls(ContextIntProviders.between(location.minRolls, location.maxRolls));
            switch (location) {
                case BONUS -> add(pool, 1, RotaryItems.HSLA_STEEL_INGOT.get(), 1, 5, 6);
                case DUNGEON -> {
                    add(pool, 2, RotaryItems.HSLA_STEEL_SCRAP.get(), 6, 18, 20);
                    add(pool, 2, RotaryItems.IRON_SCRAP.get(), 1, 12, 40);
                    add(pool, 1, RotaryItems.CANOLA_SEEDS.get(), 1, 12, 40);
                }
                case MINESHAFT -> {
                    add(pool, 2, RotaryItems.HSLA_PLATE.get(), 1, 3, 1);
                    add(pool, 1, RotaryItems.HSLA_STEEL_INGOT.get(), 1, 8, 12);
                    add(pool, 2, RotaryItems.HSLA_SHAFT_CORE.get(), 1, 4, 10);
                    add(pool, 2, RotaryItems.MOUNT.get(), 1, 1, 2);
                    add(pool, 2, RotaryItems.HSLA_STEEL_GEAR.get(), 1, 3, 9);
                    add(pool, 3, RotaryItems.HSLA_STEEL_GEAR_2x.get(), 1, 2, 5);
                    add(pool, 2, RotaryItems.HSLA_DRILL.get(), 1, 2, 3);
                    add(pool, 3, RotaryItems.GOLD_COIL.get(), 1, 1, 1);
                    add(pool, 2, RotaryItems.HSLA_STEEL_SCRAP.get(), 12, 36, 12);
                    add(pool, 2, RotaryItems.IRON_SCRAP.get(), 1, 12, 12);
                    add(pool, 1, RotaryItems.CANOLA_SEEDS.get(), 1, 12, 10);
                    add(pool, 2, RotaryItems.SCREWDRIVER.get(), 1, 1, 1);
                    add(pool, 3, RotaryItems.ANGULAR_TRANSDUCER.get(), 1, 1, 1);
                    add(pool, 2, RotaryItems.SAWDUST.get(), 1, 10, 7);
                }
                case STRONGHOLD_HALLWAY, STRONGHOLD_CROSSING -> {
                    add(pool, 2, RotaryItems.HSLA_STEEL_SCRAP.get(),
                            location == Location.STRONGHOLD_HALLWAY ? 4 : 8,
                            location == Location.STRONGHOLD_HALLWAY ? 16 : 24, 20);
                    add(pool, 2, RotaryItems.IRON_SCRAP.get(), 1, 8, 20);
                    add(pool, 3, RotaryItems.LONSDALEITE.get(), 1, 2, 10);
                }
                case STRONGHOLD_LIBRARY -> add(pool, 3, RotaryItems.ANGULAR_TRANSDUCER.get(), 1, 1, 2);
                case VILLAGE -> {
                    add(pool, 1, RotaryItems.HSLA_STEEL_INGOT.get(), 1, 3, 10);
                    add(pool, 2, RotaryItems.HSLA_STEEL_SCRAP.get(), 4, 18, 15);
                    add(pool, 2, RotaryItems.IRON_SCRAP.get(), 1, 8, 30);
                }
            }
            // Same modern additive-pool convention as ChromaChestLoot: weight 100 for no extra loot.
            // Original relative weights/counts survive; the old flat vanilla denominator no longer exists.
            pool.add(EmptyLootItem.emptyItem().setWeight(100));
            context.accept(location.subtable, LootTable.lootTable().withPool(pool));
        }
    }

    private static void add(LootPool.Builder pool, int tier, ItemLike item, int min, int max, int weight) {
        pool.add(LootItem.lootTableItem(item).setWeight(weight)
                .when(() -> new RoCChestTierCondition(tier))
                .apply(SetItemCountFunction.setCount(ContextIntProviders.between(min, max))));
    }
}
