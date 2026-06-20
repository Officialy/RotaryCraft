package reika.rotarycraft.data;

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
    }

    @SubscribeEvent
    public static void onGatherServer(GatherDataEvent.Server event) {
        // Recipe provider: emits crafting / smelting JSONs under data/rotarycraft/recipe/.
        // Starter coverage is the HSLA steel base chain + transmission components; extend
        // RoCRecipeProvider#buildRecipes incrementally.
        event.createProvider(RoCRecipeProvider::new);
        // Block loot tables: every machine block drops itself; canola uses crop drops.
        event.createProvider(RoCLootProvider::new);
        // Advancements — the modern (data-driven) form of RotaryCraft's achievements.
        event.createProvider(RoCAdvancementProvider::new);
        // Empty arena structure template that the in-world game tests run on.
        event.createProvider(RoCTestStructureProvider::new);
        // Tag providers will plug in here too as they're built.
    }
}
