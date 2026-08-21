package reika.rotarycraft;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import reika.rotarycraft.auxiliary.recipemanagers.ShapelessBlastFurnaceRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.ShapedBlastFurnaceRecipe;

/**
 * Progression: the crafting chain from a fresh world to the end-game tiers.
 *
 * <p>This is written as an <em>ordered</em> tech tree rather than a reachability closure over the
 * whole {@code RecipeManager}. A closure sounds more thorough but is not: several RotaryCraft
 * materials are obtained by machine operation rather than crafting (bedrock dust comes out of the
 * bedrock breaker, extractor stages come out of the extractor), so a naive closure reports a wall
 * of false unreachables. An ordered list encodes the intended progression, reads like the tech
 * tree, and when it fails it names the exact rung that broke.
 *
 * <p>For each step the test asserts the recipe is loaded, and that <em>every RotaryCraft ingredient
 * it actually declares</em> was produced by an earlier step or is listed as a machine-obtained
 * prerequisite. Ingredients are read from the live recipe rather than hand-listed, so the ordering
 * claim cannot drift away from the real recipes.
 */
final class RotaryProgressionTests {

    private RotaryProgressionTests() {}

    /**
     * Materials the player gets from machine operation or worldgen rather than a crafting recipe.
     * They seed the "already obtainable" set so steps that consume them are not reported broken.
     */
    private static final String[] MACHINE_OBTAINED = {
            "bedrock_dust",       // bedrock breaker
            "sawdust",            // grinder / woodcutter
            "hsla_steel_scrap",   // gearbox failure
    };

    /**
     * The intended path, earliest first. Each entry is a recipe id in the {@code rotarycraft}
     * namespace. Vanilla ingredients are assumed available from the start.
     */
    private static final String[] CHAIN = {
            // --- Tier 0: get into the mod at all
            "handbook",
            "worktable",
            "blast_furnace",

            // --- Tier 1: steel, the material everything else is built from
            "hsla_steel_from_charcoal",
            "hsla_steel_plate",
            "hsla_steel_rod",

            // --- Tier 2: first power and first transmission
            "dc_engine",
            "mount",
            "hsla_shaft",
            "hsla_steel_gear",
            "hsla_steel_gear_2x",
            "hsla_gearbox_2x",
            "bevel_gears",

            // --- Tier 3: the early processing machines
            "saw",
            "grinder",
            "drillhead_iron",
            "impeller",
            "extractor",
            "friction_heater",
            "fermenter",

            // --- Tier 4: precision parts those machines unlock
            "ball_bearing_block",
            "hsla_steel_bearing",
            "brake_disc",
            "hsla_steel_shaft_core",
            "hsla_steel_spring",
            "tension_coil",
            "coil",

            // --- Tier 5: the bedrock line
            "bedrock_alloy_ingot",
            "bedrock_shaft",

            // --- Tier 6: electronics and the turbine components
            "circuit_board",
            "screen",
            "compressor",
            "propeller_blade_down",
            "turbine",
            "diffuser",
    };

    /**
     * Rungs that are known to be missing, asserted to be <em>still</em> missing so the gap cannot
     * be forgotten and so whoever closes one is told to promote it into {@link #CHAIN}.
     *
     * <p>{@code high_temperature_combustor} is registered as an item but has no recipe: 1.7.10 made
     * it in the blast furnace at 1100 degrees from steel, redstone, an igniter and <em>red gold
     * ingots</em> (RotaryRecipes line 953), and the red gold ingot has not been ported yet. The jet
     * engine consumes the combustor, so the whole jet tier is currently uncraftable.
     */
    private static final String[] KNOWN_GAPS = {
            // Registered as an item with no recipe. 1.7.10 made it in the blast furnace at 1100
            // degrees from steel, redstone, an igniter and *red gold ingots* (RotaryRecipes 953);
            // the red gold ingot has not been ported, so the whole jet-engine tier is uncraftable.
            "high_temperature_combustor",
            // Upstream got aluminium powder from aluminium-ore decomposition, which the port has no
            // source for yet; this gates the aluminium alloy ingot and everything above it.
            "aluminum_alloy_powder",
            // No gear-unit recipes for the bedrock tier, so no bedrock gearbox of any ratio can be
            // crafted even though the blocks and their gearbox recipes exist.
            "bedrock_alloy_gear",
            "bedrock_alloy_gear_16x",
            // No recipe, and between them these block the tungsten gearbox, the CVT, both compound
            // turbine parts and -- via the diamond gear -- the bedrock breaker itself.
            "diamond_gear",
            "tungsten_alloy_spring",
            "tungsten_flakes",
    };

    static void recipeChain(GameTestHelper helper) {
        var manager = helper.getLevel().getServer().getRecipeManager();

        Set<Item> obtainable = new LinkedHashSet<>();
        for (String path : MACHINE_OBTAINED) {
            Item it = item(path);
            if (it != null)
                obtainable.add(it);
        }

        List<String> missing = new ArrayList<>();
        List<String> unreadable = new ArrayList<>();
        Set<String> outOfOrder = new LinkedHashSet<>();

        for (String path : CHAIN) {
            ResourceKey<Recipe<?>> key = ResourceKey.create(Registries.RECIPE,
                    Identifier.fromNamespaceAndPath(RotaryCraft.MODID, path));
            var found = manager.byKey(key);
            if (found.isEmpty()) {
                missing.add(path);
                continue;
            }
            RecipeHolder<?> holder = found.get();
            Recipe<?> recipe = holder.value();

            // Every RotaryCraft ingredient must already be obtainable at this point in the chain.
            for (Ingredient ing : recipe.placementInfo().ingredients()) {
                boolean satisfied = false;
                boolean anyOurs = false;
                for (Holder<Item> h : ing.items().toList()) {
                    Item it = h.value();
                    Identifier id = BuiltInRegistries.ITEM.getKey(it);
                    boolean ours = id != null && RotaryCraft.MODID.equals(id.getNamespace());
                    if (!ours) {
                        // A vanilla (or other-mod) option satisfies this slot outright.
                        satisfied = true;
                        break;
                    }
                    anyOurs = true;
                    if (obtainable.contains(it)) {
                        satisfied = true;
                        break;
                    }
                }
                if (anyOurs && !satisfied) {
                    String names = ing.items().map(h -> String.valueOf(BuiltInRegistries.ITEM.getKey(h.value())))
                            .reduce((a, b) -> a + "/" + b).orElse("?");
                    outOfOrder.add(path + " needs " + names + ", which no earlier step produces");
                }
            }

            // Its output is available to every later step.
            if (!recordResult(recipe, helper, obtainable))
                unreadable.add(path + " (" + recipe.getClass().getSimpleName() + ")");
        }

        // KNOWN_GAPS names items that no recipe produces. Asserting they are *still* unproducible
        // means whoever adds the missing recipe is told to move the entry into CHAIN, so the gap
        // list cannot quietly rot into a list of things that were fixed years ago.
        List<String> unexpectedlyPresent = new ArrayList<>();
        for (String path : KNOWN_GAPS) {
            Item gap = item(path);
            if (gap == null)
                continue;
            boolean produced = manager.getRecipes().stream().anyMatch(h -> {
                Set<Item> results = new LinkedHashSet<>();
                recordResult(h.value(), helper, results);
                return results.contains(gap);
            });
            if (produced)
                unexpectedlyPresent.add(path);
        }

        helper.assertTrue(missing.isEmpty(),
                "progression recipes not loaded: " + String.join(", ", missing));
        helper.assertTrue(unreadable.isEmpty(),
                "could not read the result of these recipes, so the ordering check below is "
                        + "meaningless for anything downstream of them -- teach recordResult about "
                        + "their type: " + String.join(", ", unreadable));
        helper.assertTrue(unexpectedlyPresent.isEmpty(),
                "these were known progression gaps and now have recipes -- move them into CHAIN: "
                        + String.join(", ", unexpectedlyPresent));
        helper.assertTrue(outOfOrder.isEmpty(),
                "progression chain is not self-consistent:\n  " + String.join("\n  ", outOfOrder));
        helper.succeed();
    }

    /**
     * Records what a recipe produces, so later steps can consume it.
     *
     * <p>Vanilla crafting recipes expose their result through {@code display()}, which the recipe
     * book drives. RotaryCraft's own machine recipe types do not override {@code display()} -- it
     * returns empty -- so they are read through their own {@code getOutput()}. A recipe type that
     * matches neither is reported rather than silently contributing nothing: that failure mode
     * shows up as a bogus "no earlier step produces X" much further down the chain, which is
     * exactly the sort of misleading error this test exists to avoid.
     *
     * @return true if the result could be determined
     */
    private static boolean recordResult(Recipe<?> recipe, GameTestHelper helper, Set<Item> obtainable) {
        ContextMap ctx = SlotDisplayContext.fromLevel(helper.getLevel());
        boolean any = false;
        for (RecipeDisplay display : recipe.display()) {
            for (ItemStack stack : display.result().resolveForStacks(ctx)) {
                if (!stack.isEmpty()) {
                    obtainable.add(stack.getItem());
                    any = true;
                }
            }
        }
        if (any)
            return true;

        ItemStack out = switch (recipe) {
            case ShapelessBlastFurnaceRecipe r -> r.getOutput();
            case ShapedBlastFurnaceRecipe r -> r.getOutput();
            default -> ItemStack.EMPTY;
        };
        if (!out.isEmpty()) {
            obtainable.add(out.getItem());
            return true;
        }
        return false;
    }

    private static Item item(String path) {
        return BuiltInRegistries.ITEM.getOptional(
                Identifier.fromNamespaceAndPath(RotaryCraft.MODID, path)).orElse(null);
    }
}
