package reika.rotarycraft.data;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import reika.rotarycraft.RotaryCraft;

/**
 * 1.21.5 datagen entry point for RotaryCraft.
 * <p>
 * NeoForge 26.x split {@link GatherDataEvent} into {@link GatherDataEvent.Client} and
 * {@link GatherDataEvent.Server}. Client-side providers (block-/item-models, lang) attach
 * to the client event; data-side providers (recipes, loot tables, tags) attach to the
 * server event. Mod-level providers register via {@code event.createProvider(...)} / {@code addProvider}.
 * <p>
 * Both handlers must be {@code static} — {@code @EventBusSubscriber} only auto-registers static methods.
 */
@EventBusSubscriber(modid = RotaryCraft.MODID)
public final class RoCDataProviders {

    private RoCDataProviders() {}

    @SubscribeEvent
    public static void onGatherClient(GatherDataEvent.Client event) {
        // Client-side resources: language, block / item models.
        event.createProvider(output -> new RotaryLang(output, "en_us"));
        event.createProvider(RoCModelProvider::new);
        event.createProvider(RoCLegacyItemAtlasProvider::new);
    }

    @SubscribeEvent
    public static void onGatherServer(GatherDataEvent.Server event) {
        // Recipes, loot tables, and advancements are reloadable registries in 26.3.
        // The recipe bootstrap also writes recipe unlock advancements into ADVANCEMENT.
        event.createReloadableRegistryObjects(new RegistrySetBuilder()
                .add(RoCRecipeProvider.bootstrap())
                .add(Registries.LOOT_TABLE, new RoCLootProvider())
                .add(Registries.ADVANCEMENT, new RoCAdvancementProvider()));
        // Empty arena structure template that the in-world game tests run on.
        event.createProvider(RoCTestStructureProvider::new);
        event.createProvider(RoCBlockTagsProvider::new);
        event.createProvider(RoCItemTagsProvider::new);
    }
}
