package reika.rotarycraft.data;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import reika.rotarycraft.RotaryCraft;

/**
 * Minecraft 26.3 datagen entry point for RotaryCraft.
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
        event.getDefaultPackGenerator().createWorldRegistryObjects(RoCWorldGenProvider.buildRegistrySet().add(Registries.DAMAGE_TYPE, context ->
                context.register(reika.rotarycraft.registry.RotaryDamageTypes.TURRET,
                        new net.minecraft.world.damagesource.DamageType("turret", net.minecraft.world.damagesource.DamageScaling.NEVER, .1F))));
        event.createProvider((output, _) -> new net.minecraft.data.tags.DamageTypeTagsProvider(
                output, event.getWorldLookupProvider(), RotaryCraft.MODID) {
            @Override protected void addTags(net.minecraft.core.HolderLookup.Provider registries) {
                var type = reika.rotarycraft.registry.RotaryDamageTypes.TURRET;
                tag(net.minecraft.tags.DamageTypeTags.BYPASSES_ARMOR).add(type);
                tag(net.minecraft.tags.DamageTypeTags.BYPASSES_SHIELD).add(type);
                tag(net.minecraft.tags.DamageTypeTags.IS_PROJECTILE).add(type);
            }
        });
        event.createProvider(RoCWorldGenProvider::new);
        event.createProvider(RoCLootModifierProvider::new);
        // Recipes, loot tables, and advancements are reloadable registries in 26.3.
        // The recipe bootstrap also writes recipe unlock advancements into ADVANCEMENT.
        event.getDefaultPackGenerator().createReloadableRegistryObjects(new RegistrySetBuilder()
                .add(RoCRecipeProvider.bootstrap())
                .add(Registries.LOOT_TABLE, new RoCLootProvider())
                .add(Registries.ADVANCEMENT, new RoCAdvancementProvider()));
        // Empty arena structure template that the in-world game tests run on.
        event.createProvider(RoCTestStructureProvider::new);
        event.createProvider(RoCBlockTagsProvider::new);
        event.createProvider(RoCItemTagsProvider::new);
        event.createProvider(RoCFluidTagsProvider::new);
        event.createProvider(PileDriverDataProvider::new);
        event.createProvider(TerraformerBiomeTagsProvider::new);
    }
}
