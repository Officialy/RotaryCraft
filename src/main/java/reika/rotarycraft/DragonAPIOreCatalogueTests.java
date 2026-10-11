package reika.rotarycraft;

import java.util.HashMap;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagLoader;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import reika.dragonapi.modregistry.ModOreList;

/** Native tag binding, source products, and real server/remote/integrated event handling. */
final class DragonAPIOreCatalogueTests {
    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> environment) {
        RotaryGameTests.register(event, environment, "dragonapi_ore_catalogue_products", 40, DragonAPIOreCatalogueTests::products);
        RotaryGameTests.register(event, environment, "dragonapi_ore_catalogue_reload", 40, DragonAPIOreCatalogueTests::reload);
    }

    private static void products(GameTestHelper h) {
        ModOreList.initializeAll();
        var copper = new ItemStack(Items.COPPER_INGOT, 32);
        copper.set(DataComponents.CUSTOM_NAME, Component.literal("Detached copper"));
        h.assertTrue(ModOreList.getAllByDrop(copper).equals(List.of(ModOreList.COPPER, ModOreList.NETHERCOPPER)),
                "shared original ingotCopper product retains both catalogue entries despite count/components");
        h.assertTrue(ModOreList.getByDrop(copper) == ModOreList.COPPER, "singular API chooses original enum order");
        h.assertTrue(ModOreList.NETHERCOAL.getProductItems().stream().anyMatch(stack -> stack.is(Items.COAL)),
                "actual loaded source itemCoal product is retained");
        h.assertTrue(ModOreList.getAllByDrop(new ItemStack(Items.LAPIS_LAZULI)).containsAll(List.of(ModOreList.NETHERLAPIS, ModOreList.SODALITE)),
                "historical blue dye includes modern lapis gem membership");
        h.assertTrue(ModOreList.getByDrop(ItemStack.EMPTY) == null && ModOreList.getByDrop(null) == null,
                "empty inputs are unclassified");
        var first = ModOreList.COPPER.getProductItems().getFirst();
        first.setCount(0); first.set(DataComponents.CUSTOM_NAME, Component.literal("Changed"));
        h.assertTrue(ModOreList.COPPER.getProductItems().stream().allMatch(stack -> stack.getCount() == 1 && !stack.has(DataComponents.CUSTOM_NAME)),
                "product query stacks cannot modify the cache");
        h.succeed();
    }

    private static void reload(GameTestHelper h) {
        var registry = BuiltInRegistries.ITEM;
        var original = new HashMap<TagKey<Item>, List<Holder<Item>>>();
        registry.getTags().forEach(tag -> original.put(tag.key(), List.copyOf(tag.contents())));
        var changed = new HashMap<>(original);
        changed.put(tag("ores/tin"), List.of(Items.STONE.builtInRegistryHolder(), Items.APPLE.builtInRegistryHolder()));
        changed.put(tag("ores/copper"), List.of(Items.STONE.builtInRegistryHolder()));
        changed.put(tag("ingots/tin"), List.of(Items.APPLE.builtInRegistryHolder()));
        changed.put(tag("ingots/copper"), List.of(Items.APPLE.builtInRegistryHolder()));
        changed.put(tag("ores/nether_copper"), List.of(Items.ANDESITE.builtInRegistryHolder()));
        changed.put(tag("ores/nethercopper"), List.of(Items.GRANITE.builtInRegistryHolder()));
        var serverEvent = new TagsUpdatedEvent.ServerDataLoad(h.getLevel().getServer().getServerResources().managers(), h.getLevel().registryAccess());
        var stone = new ItemStack(Items.STONE);
        try {
            ModOreList.initializeAll();
            var before = ModOreList.getAllModOresFromOre(stone);
            registry.prepareTagReload(new TagLoader.LoadResult<>(registry.key(), changed)).apply();
            NeoForge.EVENT_BUS.post(new TagsUpdatedEvent.ClientPacketReceived(h.getLevel().registryAccess(), true));
            h.assertTrue(ModOreList.getAllModOresFromOre(stone).equals(before), "integrated client preserves server-owned catalogue");
            // A tag rebind does not prepare unrelated single-use server reload listeners for bus replay.
            reika.dragonapi.DragonAPI.instance.updateOreCatalogue(serverEvent);
            var held = ModOreList.getAllModOresFromOre(stone);
            h.assertTrue(held.containsAll(List.of(ModOreList.TIN, ModOreList.COPPER)), "server tag update preserves overlapping ores");
            h.assertTrue(ModOreList.getAllByOreName("c:ores/tin").equals(List.of(ModOreList.TIN))
                    && ModOreList.getByOreName("oreTin") == ModOreList.TIN, "native tag and retained source names both resolve");
            h.assertTrue(ModOreList.getAllByDrop(new ItemStack(Items.APPLE)).containsAll(List.of(ModOreList.TIN, ModOreList.COPPER, ModOreList.NETHERCOPPER)),
                    "non-block product membership retains every shared declared product");
            h.assertTrue(ModOreList.getAllModOresFromOre(new ItemStack(Items.APPLE)).isEmpty(), "non-block ore tag members are rejected");
            h.assertTrue(ModOreList.NETHERCOPPER.contains(new ItemStack(Items.ANDESITE)) && ModOreList.NETHERCOPPER.contains(new ItemStack(Items.GRANITE)),
                    "modern compound and prior flat tags both resolve");
            stone.set(DataComponents.CUSTOM_NAME, Component.literal("Tagged stone")); stone.setCount(19);
            h.assertTrue(ModOreList.TIN.contains(stone), "tag membership is independent of display components and count");
            changed.put(tag("ores/tin"), List.of()); changed.put(tag("ores/copper"), List.of());
            changed.put(tag("ingots/tin"), List.of()); changed.put(tag("ingots/copper"), List.of());
            changed.put(tag("ores/nether_copper"), List.of()); changed.put(tag("ores/nethercopper"), List.of());
            registry.prepareTagReload(new TagLoader.LoadResult<>(registry.key(), changed)).apply();
            // The legacy individual-entry path must clear stale reverse mappings across the snapshot.
            ModOreList.TIN.initialize();
            h.assertTrue(!ModOreList.getAllModOresFromOre(stone).contains(ModOreList.TIN)
                    && !ModOreList.getAllModOresFromOre(stone).contains(ModOreList.COPPER), "individual reinitialization removes old ore mappings");
            h.assertTrue(!ModOreList.getAllByDrop(new ItemStack(Items.APPLE)).contains(ModOreList.TIN), "individual reinitialization removes old product mappings");
            h.assertTrue(held.containsAll(List.of(ModOreList.TIN, ModOreList.COPPER)), "previous immutable query views survive reload unchanged");
            changed.put(tag("ores/tin"), List.of(Items.ANDESITE.builtInRegistryHolder()));
            changed.put(tag("ingots/tin"), List.of(Items.DIAMOND.builtInRegistryHolder()));
            registry.prepareTagReload(new TagLoader.LoadResult<>(registry.key(), changed)).apply();
            NeoForge.EVENT_BUS.post(new TagsUpdatedEvent.ClientPacketReceived(h.getLevel().registryAccess(), false));
            h.assertTrue(ModOreList.TIN.contains(new ItemStack(Items.ANDESITE)) && !ModOreList.NETHERCOPPER.contains(new ItemStack(Items.ANDESITE)),
                    "remote event replaces associations, rather than merging stale entries");
            h.assertTrue(ModOreList.getAllByDrop(new ItemStack(Items.DIAMOND)).contains(ModOreList.TIN), "remote event adds declared product mappings");
            var names = List.of(ModOreList.TIN.getOreDictNames());
            boolean rejected = false;
            try { ModOreList.TIN.addOreDictName("invalid name"); } catch (IllegalArgumentException expected) { rejected = true; }
            h.assertTrue(rejected && names.equals(List.of(ModOreList.TIN.getOreDictNames())) && ModOreList.TIN.contains(new ItemStack(Items.ANDESITE)),
                    "invalid registration cannot change definition or published snapshot");
            ModOreList.TIN.addOreDictName("oreTin");
            h.assertTrue(names.equals(List.of(ModOreList.TIN.getOreDictNames())), "duplicate registration is idempotent");
        } finally {
            registry.prepareTagReload(new TagLoader.LoadResult<>(registry.key(), original)).apply();
            reika.dragonapi.DragonAPI.instance.updateOreCatalogue(serverEvent);
        }
        h.succeed();
    }

    private static TagKey<Item> tag(String path) { return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", path)); }
}
