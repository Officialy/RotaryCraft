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

/**
 * Every live V33a RotaryChests entry, with its original tier, count and weight.
 *
 * <p>In 1.7.10 these entries were merged into the location's single {@code ChestGenHooks} pool, and the
 * chest drew {@code getCount()} items from the merged list. Each subtable reproduces that exactly for
 * RotaryCraft's items: it rolls the 1.7.10 chest's own count, and its empty entry weighs what the
 * 1.7.10 vanilla pool did, so every item keeps its original per-roll chance of
 * {@code weight / (vanilla + RotaryCraft)} and its original expected count per chest.
 *
 * <p>Both columns are read from Forge 10.13.4.1614: the rolls are {@code addInfo}'s min/max as
 * {@code getCount} uses them ({@code min + nextInt(max - min)}, so the max is exclusive), and the
 * vanilla weight is the sum of the vanilla content array plus the enchanted book {@code init()} adds
 * (weight 1, or 2 in the library). Do not swap these for the 26.x vanilla roll counts and a flat
 * filler: that made the bonus-chest steel ten times rarer and mineshaft tools about five times rarer.
 */
public record RoCChestLoot(LootTableSubProvider.Context context) implements LootTableSubProvider {
    public enum Location {
        /** BONUS_CHEST: WorldServer.bonusChestContent, count 10. */
        BONUS(BuiltInLootTables.SPAWN_BONUS_CHEST, 10, 10, 64),
        /** DUNGEON_CHEST: WorldGenDungeons contents + book, count 8. */
        DUNGEON(BuiltInLootTables.SIMPLE_DUNGEON, 8, 8, 120),
        /** MINESHAFT_CORRIDOR: mineshaftChestContents + book, addInfo 3..7. */
        MINESHAFT(BuiltInLootTables.ABANDONED_MINESHAFT, 3, 6, 80),
        /** STRONGHOLD_CORRIDOR: ChestCorridor.strongholdChestContents + book, addInfo 2..4. */
        STRONGHOLD_HALLWAY(BuiltInLootTables.STRONGHOLD_CORRIDOR, 2, 3, 99),
        /** STRONGHOLD_CROSSING: RoomCrossing contents + book, addInfo 1..5. */
        STRONGHOLD_CROSSING(BuiltInLootTables.STRONGHOLD_CROSSING, 1, 4, 62),
        /** STRONGHOLD_LIBRARY: Library contents + book (weight 2), addInfo 1..5. */
        STRONGHOLD_LIBRARY(BuiltInLootTables.STRONGHOLD_LIBRARY, 1, 4, 44),
        /** VILLAGE_BLACKSMITH: House2 contents, addInfo 3..9. */
        VILLAGE(BuiltInLootTables.VILLAGE_WEAPONSMITH, 3, 8, 94);

        public final ResourceKey<LootTable> target;
        public final ResourceKey<LootTable> subtable;
        private final int minRolls;
        private final int maxRolls;
        private final int vanillaWeight;

        Location(ResourceKey<LootTable> target, int minRolls, int maxRolls, int vanillaWeight) {
            this.target = target;
            this.minRolls = minRolls;
            this.maxRolls = maxRolls;
            this.vanillaWeight = vanillaWeight;
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
            // Stands in for the 1.7.10 vanilla entries these weights were drawn against; see the class doc.
            pool.add(EmptyLootItem.emptyItem().setWeight(location.vanillaWeight));
            context.accept(location.subtable, LootTable.lootTable().withPool(pool));
        }
    }

    private static void add(LootPool.Builder pool, int tier, ItemLike item, int min, int max, int weight) {
        pool.add(LootItem.lootTableItem(item).setWeight(weight)
                .when(() -> new RoCChestTierCondition(tier))
                .apply(SetItemCountFunction.setCount(ContextIntProviders.between(min, max))));
    }
}
