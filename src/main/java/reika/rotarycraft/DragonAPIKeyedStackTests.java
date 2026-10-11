package reika.rotarycraft;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import reika.dragonapi.base.BlockEntityBase;
import reika.dragonapi.instantiable.ItemMatch;
import reika.dragonapi.instantiable.data.KeyedItemStack;
import reika.rotarycraft.items.ItemRailGunAmmo;
import reika.rotarycraft.registry.RotaryDataComponents;
import reika.rotarycraft.registry.RotaryItems;

/** Bound 26.3 stacks and actual family consumers, without a cyclic compile dependency on Chroma/Reactor. */
final class DragonAPIKeyedStackTests {
    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> environment) {
        RotaryGameTests.register(event, environment, "dragonapi_keyed_stack_policies", 40, DragonAPIKeyedStackTests::policies);
        RotaryGameTests.register(event, environment, "dragonapi_keyed_stack_variants_and_save", 40, DragonAPIKeyedStackTests::variants);
        RotaryGameTests.register(event, environment, "dragonapi_keyed_stack_tool_crate", 40, DragonAPIKeyedStackTests::crate);
    }

    private static void policies(GameTestHelper h) {
        var plain = new ItemStack(Items.IRON_SWORD); var worn = plain.copy(); worn.setDamageValue(37);
        var key = new KeyedItemStack(plain);
        h.assertTrue(key.match(worn) && key.equals(new KeyedItemStack(worn)) && key.compareTo(new KeyedItemStack(worn)) == 0,
                "source ordinary tools ignore wear in matching, equality and ordering");
        h.assertTrue(!key.exactMatch(new KeyedItemStack(worn)) && !key.copy().setIgnoreDamage(false).match(worn),
                "exact matching and explicit durability selection distinguish wear");
        worn.set(DataComponents.CUSTOM_NAME, Component.literal("Named worn sword"));
        h.assertTrue(new KeyedItemStack(Items.IRON_SWORD).match(worn) && new ItemMatch(Items.IRON_SWORD).match(worn),
                "source item-only constructors are wildcard predicates, including worn tools");
        h.assertTrue(!key.match(worn), "additional custom data remains significant by default");
        h.assertTrue(key.copy().setIgnoreComponents(true).match(worn) && !key.copy().setIgnoreComponents(false).match(worn),
                "modern whole-component policy controls variant, additional data and durability together");
        var strict = new KeyedItemStack(worn); var fuzzy = strict.copy().setIgnoreAdditionalComponents(true);
        var changed = worn.copy(); changed.set(DataComponents.CUSTOM_NAME, Component.literal("B"));
        h.assertTrue(!strict.equals(fuzzy) && strict.matches(fuzzy) && fuzzy.matches(strict)
                && fuzzy.contains(new KeyedItemStack(changed)) && !strict.contains(fuzzy), "fuzzy matching/containment remain explicit and symmetric where intended");
        var keys = List.of(strict, fuzzy, new KeyedItemStack(changed), strict.copy().setSimpleHash(true), strict.copy().setIgnoreDamage(false));
        h.assertTrue(new HashSet<>(keys).size() == new TreeSet<>(keys).size(), "hash and sorted collections retain the same equality classes");
        for (var a : keys) for (var b : keys)
            h.assertTrue((a.equals(b) == (a.compareTo(b) == 0)) && Integer.signum(a.compareTo(b)) == -Integer.signum(b.compareTo(a))
                    && (!a.equals(b) || a.hashCode() == b.hashCode()), "all selected policies obey equality, hash and order contracts");
        var locked = fuzzy.copy().lock(); locked.setIgnoreComponents(false).setSized(true);
        h.assertTrue(!locked.hasNBT() && !locked.checksDamage() && !locked.isSized(), "copies preserve lock and setters cannot mutate locked keys");
        var returned = strict.getItemStack(); returned.setCount(0);
        h.assertTrue(!strict.getItemStack().isEmpty(), "returned stacks are detached");
        h.assertTrue(!key.match(null) && !key.match(ItemStack.EMPTY) && !key.matches(null) && !key.exactMatch(null) && !key.contains(null), "empty inputs never match");
        var many = plain.copyWithCount(3);
        h.assertTrue(key.match(many) && !key.copy().setSized(true).match(many) && !key.exactMatch(new KeyedItemStack(many)), "count remains an independent criterion");
        var healing = PotionContents.createItemStack(Items.POTION, Potions.HEALING);
        var speed = PotionContents.createItemStack(Items.POTION, Potions.SWIFTNESS);
        var custom = healing.copy(); custom.set(DataComponents.POTION_CONTENTS,
                new PotionContents(Optional.of(Potions.HEALING), Optional.of(0x123456), List.of(), Optional.of("custom")));
        h.assertTrue(new ItemMatch(healing).match(custom) && !new ItemMatch(healing).match(speed) && !new ItemMatch(custom).match(healing),
                "source ingredient policy separates base potion subtype from explicitly supplied custom fields");
        h.assertTrue(new KeyedItemStack(healing).setIgnoreVariantComponents(true).match(speed)
                && !new KeyedItemStack(healing).setIgnoreVariantComponents(true).match(custom), "ignored variant identity cannot leak into additional matching");
        h.succeed();
    }

    private static void variants(GameTestHelper h) {
        for (var item : List.of(RotaryItems.HSLA_STEEL_SPRING.get(), RotaryItems.BEDROCK_ALLOY_SPRING.get(), RotaryItems.MOTION.get())) {
            var chargeA = new ItemStack(item); chargeA.setDamageValue(13);
            var chargeB = chargeA.copy(); chargeB.setDamageValue(27);
            var key = new KeyedItemStack(chargeA).setIgnoreAdditionalComponents(true);
            h.assertTrue(!key.match(chargeB) && key.copy().setIgnoreDamage(false).setIgnoreVariantComponents(true).match(chargeB),
                    "source charged subtype identity remains separate from durability: " + BuiltInRegistries.ITEM.getKey(item));
            h.assertTrue(!new ItemMatch(chargeA).match(chargeB) && new KeyedItemStack(item).match(chargeB),
                    "charged templates preserve exact subtype and item-only templates preserve wildcard behavior");
        }
        var first = ItemRailGunAmmo.stack(5); var second = ItemRailGunAmmo.stack(6);
        var named = first.copy(); named.set(DataComponents.CUSTOM_NAME, Component.literal("Named ammunition"));
        var subtype = new KeyedItemStack(first).setIgnoreAdditionalComponents(true);
        h.assertTrue(subtype.match(named) && !subtype.match(second), "ignoring old NBT preserves the actual railgun subtype component");
        h.assertTrue(new KeyedItemStack(first).setIgnoreVariantComponents(true).match(second), "ignoring old metadata ignores the selected subtype independently");
        h.assertTrue(new ItemMatch(first).match(named) && !new ItemMatch(first).match(second), "variant patches do not force strict additional matching in ItemMatch");
        var legacy = new ItemStack(RotaryItems.RAILGUN_AMMO.get()); legacy.set(DataComponents.DAMAGE, 5);
        h.assertTrue(subtype.exactMatch(new KeyedItemStack(legacy)) && legacy.has(DataComponents.DAMAGE), "legacy railgun damage normalizes on a private key copy without mutating its source");
        var familyA = new ItemStack(Items.STICK); familyA.set(RotaryDataComponents.EXTRACT_FAMILY.get(), Identifier.parse("rotarycraft:alpha"));
        var familyB = familyA.copy(); familyB.set(RotaryDataComponents.EXTRACT_FAMILY.get(), Identifier.parse("rotarycraft:beta"));
        h.assertTrue(!new KeyedItemStack(familyA).setIgnoreNBT(true).match(familyB), "custom extractor families cannot collapse in fuzzy recipe loop keys");
        for (var entry : List.of(new String[]{"fuel", "fuel_burnup"}, new String[]{"magnet", "magnet_charge"})) {
            var item = BuiltInRegistries.ITEM.getValue(Identifier.fromNamespaceAndPath("reactorcraft", entry[0]));
            var type = DragonAPIKeyedStackTests.<Integer>type("reactorcraft:" + entry[1]);
            var a = new ItemStack(item); a.set(type, 2); var b = a.copy(); b.set(type, 3);
            h.assertTrue(!new KeyedItemStack(a).setIgnoreNBT(true).match(b), "source Reactor subtype identity survives ignored extra data: " + entry[0]);
            var old = new ItemStack(item); old.set(DataComponents.DAMAGE, 2);
            h.assertTrue(new KeyedItemStack(a).exactMatch(new KeyedItemStack(old)) && old.has(DataComponents.DAMAGE), "existing Reactor legacy migration is applied privately: " + entry[0]);
            var zero = new ItemStack(item); zero.set(type, 0);
            h.assertTrue(new KeyedItemStack(new ItemStack(item)).equals(new KeyedItemStack(zero)), "absent and explicit source zero subtype are equal: " + entry[0]);
        }
        var wasteItem = BuiltInRegistries.ITEM.getValue(Identifier.parse("reactorcraft:waste"));
        var isotope = DragonAPIKeyedStackTests.<String>type("reactorcraft:waste_identity");
        var waste0 = new ItemStack(wasteItem); waste0.set(isotope, "isotope:" + reika.dragonapi.libraries.mathsci.Isotopes.values()[0].name().toLowerCase(java.util.Locale.ROOT));
        var waste1 = new ItemStack(wasteItem); waste1.set(isotope, "isotope:" + reika.dragonapi.libraries.mathsci.Isotopes.values()[1].name().toLowerCase(java.util.Locale.ROOT));
        var oldWaste = new ItemStack(wasteItem); oldWaste.set(DataComponents.DAMAGE, 1);
        h.assertTrue(!new KeyedItemStack(waste0).setIgnoreNBT(true).match(waste1)
                && new KeyedItemStack(waste1).exactMatch(new KeyedItemStack(oldWaste))
                && new KeyedItemStack(waste0).equals(new KeyedItemStack(new ItemStack(wasteItem))),
                "nuclear-waste isotopes retain their source subtype identity, zero default and legacy conversion");
        var encoded = new CompoundTag(); var saved = new KeyedItemStack(named).setSized(true).setIgnoreDamage(false).setSimpleHash(true);
        saved.saveAdditional(encoded, h.getLevel().registryAccess());
        var loaded = KeyedItemStack.load(encoded, h.getLevel().registryAccess());
        h.assertTrue(saved.equals(loaded) && saved.compareTo(loaded) == 0 && loaded.checksDamage() && loaded.isSized(), "typed stack and independent policy flags round-trip with native registry serialization");
        encoded.remove("ignoredamage");
        h.assertTrue(!KeyedItemStack.load(encoded, h.getLevel().registryAccess()).checksDamage(), "older keys without a durability policy preserve source ignored-wear behavior");
        var potionKey = new KeyedItemStack(PotionContents.createItemStack(Items.POTION, Potions.HEALING)).setIgnoreNBT(true);
        encoded = new CompoundTag(); potionKey.saveAdditional(encoded, h.getLevel().registryAccess());
        h.assertTrue(potionKey.equals(KeyedItemStack.load(encoded, h.getLevel().registryAccess())), "registry-backed potion subtype and flags round-trip");
        h.succeed();
    }

    private static void crate(GameTestHelper h) {
        var pos = new BlockPos(4, 1, 4);
        h.setBlock(pos, BuiltInRegistries.BLOCK.getValue(Identifier.parse("chromaticraft:tool_storage")));
        var entity = (BlockEntityBase)h.getLevel().getBlockEntity(h.absolutePos(pos));
        var container = (Container)entity;
        try {
            select(entity, "SWORD");
            var plain = new ItemStack(Items.IRON_SWORD); var worn = plain.copy(); worn.setDamageValue(37);
            worn.set(DataComponents.CUSTOM_NAME, Component.literal("Named worn sword"));
            insert(h, entity, container, plain); insert(h, entity, container, worn);
            var types = counts(entity);
            h.assertTrue(types.size() == 1 && types.values().iterator().next() == 2, "actual tool crate groups ordinary swords across wear and ignored extra data");
            container.clearContent(); select(entity, "POTION");
            var healing = PotionContents.createItemStack(Items.POTION, Potions.HEALING);
            var custom = healing.copy(); custom.set(DataComponents.POTION_CONTENTS,
                    new PotionContents(Optional.of(Potions.HEALING), Optional.of(0x123456), List.of(), Optional.of("custom")));
            insert(h, entity, container, healing); insert(h, entity, container, custom);
            insert(h, entity, container, PotionContents.createItemStack(Items.POTION, Potions.SWIFTNESS));
            types = counts(entity);
            h.assertTrue(types.size() == 2 && new HashSet<>(types.values()).equals(java.util.Set.of(1, 2)),
                    "actual potion crate keeps base subtypes separate while grouping source-ignored custom potion fields");
        } catch (ReflectiveOperationException failure) { throw new AssertionError("Real accepted tool-crate API", failure); }
        finally { container.clearContent(); }
        h.succeed();
    }

    private static void insert(GameTestHelper h, BlockEntityBase entity, Container container, ItemStack stack) {
        h.assertTrue(container.canPlaceItem(0, stack), "actual crate accepts the selected source tool kind");
        container.setItem(0, stack); entity.updateEntity(h.getLevel(), entity.getBlockPos());
    }
    private static void select(BlockEntityBase entity, String mode) throws ReflectiveOperationException {
        for (int i = 0; i < 24; i++) {
            if (((Enum<?>)entity.getClass().getMethod("getFilter").invoke(entity)).name().equals(mode)) return;
            entity.getClass().getMethod("stepMode").invoke(entity);
        }
        throw new AssertionError("Missing actual crate mode " + mode);
    }
    @SuppressWarnings("unchecked") private static Map<KeyedItemStack, Integer> counts(BlockEntityBase entity) throws ReflectiveOperationException {
        return (Map<KeyedItemStack, Integer>)entity.getClass().getMethod("getItemTypes").invoke(entity);
    }
    @SuppressWarnings("unchecked") private static <T> DataComponentType<T> type(String id) {
        return (DataComponentType<T>)BuiltInRegistries.DATA_COMPONENT_TYPE.getValue(Identifier.parse(id));
    }
}
