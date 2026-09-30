package reika.rotarycraft.modinterface.jei;

import net.minecraft.world.item.crafting.RecipeMap;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import reika.dragonapi.ModList;
import reika.rotarycraft.registry.RotaryRecipeTypes;

/** Sends every RotaryCraft datapack recipe type through NeoForge's recipe sync channel. */
public final class RotaryRecipeSync {
    private RotaryRecipeSync() {}

    /** The same datapack recipes back server processing and client inventory validation. */
    public static RecipeMap getRecipes(net.minecraft.world.level.Level level) {
        if (level == null) return null;
        return level.isClientSide() ? Client.getCurrentRecipes() : level.getServer().getRecipeManager().recipeMap();
    }

    public static void register() {
        NeoForge.EVENT_BUS.addListener(RotaryRecipeSync::onDatapackSync);
        if (FMLEnvironment.getDist() == Dist.CLIENT)
            Client.register();
    }

    private static void onDatapackSync(OnDatapackSyncEvent event) {
        event.sendRecipes(
                RotaryRecipeTypes.BLAST_FURNACE_SHAPED.get(),
                RotaryRecipeTypes.BLAST_FURNACE_SHAPELESS.get(),
                RotaryRecipeTypes.PULSE_FURNACE.get(),
                RotaryRecipeTypes.GRINDER.get(),
                RotaryRecipeTypes.CENTRIFUGE.get(),
                RotaryRecipeTypes.FRICTION_HEATER.get(),
                RotaryRecipeTypes.EXTRACTOR.get(),
                RotaryRecipeTypes.FERMENTER.get(),
                RotaryRecipeTypes.FRACTIONATOR.get(),
                RotaryRecipeTypes.LAVA_MAKER.get(),
                RotaryRecipeTypes.COMPACTOR.get(),
                RotaryRecipeTypes.PURIFIER.get(),
                RotaryRecipeTypes.WETTER.get(),
                RotaryRecipeTypes.DRYING_BED.get(),
                RotaryRecipeTypes.CRYSTALLIZER.get());
    }

    public static final class Client {
        private static RecipeMap currentRecipes;

        private Client() {}

        private static void register() {
            NeoForge.EVENT_BUS.addListener(Client::onRecipesReceived);
        }

        private static void onRecipesReceived(net.neoforged.neoforge.client.event.RecipesReceivedEvent event) {
            currentRecipes = event.getRecipeMap();
            if (ModList.JEI.isLoaded())
                RotaryJEIPlugin.onRecipeMapReceived(currentRecipes);
        }

        public static RecipeMap getCurrentRecipes() {
            return currentRecipes;
        }
    }
}
