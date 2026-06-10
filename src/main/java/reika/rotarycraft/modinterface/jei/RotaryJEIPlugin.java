/*******************************************************************************
 * @author Reika Kalseki / 26.1 port by OfficialyMax
 *
 * JEI (Just Enough Items) integration entrypoint for RotaryCraft. Registers the mod's
 * blocks/items into JEI's item panel automatically (vanilla item registry handles most of
 * this for free), and reserves the registration hooks for custom recipe categories
 * (Worktable, Blast Furnace, Fractionator, Grinder, etc.) when those provider classes are
 * ported.
 *
 * Discovery: JEI scans for {@code @JeiPlugin}-annotated classes via service loading at
 * mod-load time. This class compiles only when {@code -PenableJEICompat=true} is passed
 * (see RotaryCraft/build.gradle), so the main jar stays free of JEI API references on
 * lifecycles where JEI hasn't been published yet (currently NeoForge 26.1).
 ******************************************************************************/
package reika.rotarycraft.modinterface.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.Identifier;

import reika.rotarycraft.RotaryCraft;

@JeiPlugin
public class RotaryJEIPlugin implements IModPlugin {

    public static final Identifier UID = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "jei_plugin");

    @Override
    public Identifier getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        // Hook for custom recipe categories. Wire up Worktable / BlastFurnace / Grinder /
        // Fractionator categories here when their JEI category classes are ported. Vanilla
        // crafting / smelting / blasting / smoking / campfire are registered by JEI itself —
        // any recipe of those types added by RotaryCraft (Blast Furnace, etc.) shows up
        // automatically.
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        // Per-category recipe registration goes here. Each category needs:
        //   1. A recipe type (UID).
        //   2. A list of recipe instances pulled from the world's RecipeManager.
        //   3. The category class registered above.
        // Most RotaryCraft recipes are vanilla-derived (blast / smelting / crafting), so
        // they pick up the matching JEI category without explicit work here.
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        // Optional: cache the runtime so other RotaryCraft systems can call into JEI
        // (e.g., the Handbook's "show recipe" button).
    }
}
