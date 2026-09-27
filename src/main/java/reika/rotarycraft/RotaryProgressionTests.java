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
import reika.rotarycraft.auxiliary.recipemanagers.PulseFurnaceRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.ShapedBlastFurnaceRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.CentrifugeRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.FermenterRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.GrinderRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.LavaMakerRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.ExtractorRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.FractionatorRecipe;
import reika.rotarycraft.registry.ExtractOres;
import reika.rotarycraft.registry.ExtractorBonus;
import reika.rotarycraft.registry.RotaryFluids;
import reika.rotarycraft.registry.RotaryItems;

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
            "gold_flakes",        // gold ore extractor
            "tungsten_flakes",    // iron solution extractor bonus
            "aluminum_alloy_powder", // lapis/redstone solution extractor bonus
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
            "reservoir",

            // --- Tier 2: first power and first transmission
            "dc_engine",
            "mount",
            "hsla_shaft",
            "hsla_steel_gear",
            "hsla_steel_gear_2x",
            "hsla_steel_gear_4x",
            "hsla_gearbox_2x",
            "bevel_gears",

            // --- Tier 3: the early processing machines
            "saw",
            "grinder",
            "grinder/coal_to_dust",
            "grinder/netherrack_to_dust",
            "grinder/soul_sand_to_tar",
            "drillhead_iron",
            "impeller",
            "extractor",
            "friction_heater",
            "fermenter",
            "centrifuge",
            "fermenter/yeast",
            "fermenter/sludge",
            "centrifuge/clean_sludge",
            "ethanol_crystals_from_clean_sludge",
            "rock_melter",
            "fuel_line",
            "mixer",
            "fractionator",
            "fluid_pipe",

            // --- Tier 4: precision parts those machines unlock
            "ball_bearing_block",
            "hsla_steel_bearing",
            "brake_disc",
            "hsla_steel_shaft_core",
            "hsla_steel_spring",
            "tension_coil",
            "coil",

            // --- Tier 5: the bedrock line
            "tungsten_ingot_from_smelting",
            "bedrock_breaker",
            "bedrock_alloy_ingot",
            "bedrock_shaft",

            // --- Tier 6: electronics and the turbine components
            "circuit_board",
            "screen",
            "compressor",
            "propeller_blade_down",
            "turbine",
            "diffuser",
            "ignition_unit",
            "combustor",
            "pulse_jet_furnace",
            "red_gold_dust",
            "pulse_furnace/red_gold_dust_to_ingot",
            "ignition_unit",
            "high_temperature_combustor",
            "spring_steel_ingot",
            "tungsten_alloy_ingot",
            "tungsten_alloy_rod",
            "tungsten_alloy_shaft_core",
            "compound_compressor",
            "compound_turbine",
            "silicon_dust",
            "aluminum_alloy_ingot",
            "microturbine",
            "jet_engine",
            "tungsten_alloy_gear",
            "diamond_gear",
            "bedrock_alloy_rod",
            "bedrock_alloy_gear",
            "bedrock_alloy_gear_2x",
            "bedrock_alloy_gear_4x",
            "bedrock_alloy_gear_8x",
            "bedrock_alloy_gear_16x",
            "bedrock_gearbox_4x",
    };

    /** Items with no crafting recipe and no known machine or world source yet. */
    private static final String[] KNOWN_GAPS = {
            "tungsten_alloy_spring",
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

            // The Fractionator's six solid inputs live in its machine logic rather than in a
            // crafting recipe. Check them at this rung, before later steps can mask a gap.
            if (path.equals("fractionator")) {
                ResourceKey<Recipe<?>> fuelKey = ResourceKey.create(Registries.RECIPE,
                        Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "fractionator/jet_fuel"));
                Recipe<?> fuelRecipe = manager.byKey(fuelKey).map(RecipeHolder::value).orElse(null);
                if (!(fuelRecipe instanceof FractionatorRecipe fuel)) {
                    missing.add("fractionator/jet_fuel");
                } else {
                    for (FractionatorRecipe.WeightedIngredient entry : fuel.getIngredients()) {
                        boolean available = entry.ingredient().items().anyMatch(itemHolder -> {
                            Item ingredient = itemHolder.value();
                            Identifier id = BuiltInRegistries.ITEM.getKey(ingredient);
                            return !RotaryCraft.MODID.equals(id.getNamespace()) || obtainable.contains(ingredient);
                        });
                        if (!available)
                            outOfOrder.add("fractionator solid has no earlier source: " + entry.ingredient());
                    }
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
        for (String path : new String[]{"lava_maker/ethanol_crystals", "lava_maker/clean_sludge"}) {
            ResourceKey<Recipe<?>> key = ResourceKey.create(Registries.RECIPE,
                    Identifier.fromNamespaceAndPath(RotaryCraft.MODID, path));
            Recipe<?> recipe = manager.byKey(key).orElseThrow().value();
            helper.assertTrue(recipe instanceof LavaMakerRecipe melt
                            && melt.getFluid().getFluid().isSame(RotaryFluids.ETHANOL.get())
                            && melt.getFluid().getAmount() == 1000,
                    path + " must melt to one bucket of ethanol for the fractionator");
        }
        for (ExtractOres ore : new ExtractOres[]{ExtractOres.IRON, ExtractOres.GOLD,
                ExtractOres.LAPIS, ExtractOres.REDSTONE}) {
            String name = ore.name().toLowerCase(java.util.Locale.ROOT);
            String[] stages = {"ore_to_dust", "dust_to_slurry", "slurry_to_solution", "solution_to_flakes"};
            for (int stage = 0; stage < stages.length; stage++) {
                String path = "extractor/" + name + "_" + stages[stage];
                ResourceKey<Recipe<?>> key = ResourceKey.create(Registries.RECIPE,
                        Identifier.fromNamespaceAndPath(RotaryCraft.MODID, path));
                Recipe<?> recipe = manager.byKey(key).orElseThrow().value();
                int expectedStage = stage;
                helper.assertTrue(recipe instanceof ExtractorRecipe extract
                                && extract.getStage() == expectedStage
                                && extract.getOutput().is(ore.getStageItem(expectedStage)),
                        path + " must produce its next material stage");
            }
        }
        for (ExtractorBonus bonus : new ExtractorBonus[]{ExtractorBonus.IRON, ExtractorBonus.LAPIS,
                ExtractorBonus.REDSTONE}) {
            Item solution = switch (bonus) {
                case IRON -> ExtractOres.IRON.getSolution();
                case LAPIS -> ExtractOres.LAPIS.getSolution();
                case REDSTONE -> ExtractOres.REDSTONE.getSolution();
                default -> throw new IllegalStateException();
            };
            Item expected = bonus == ExtractorBonus.IRON ? RotaryItems.TUNGSTEN_FLAKES.get()
                    : RotaryItems.ALUMINUM_ALLOY_POWDER.get();
            helper.assertTrue(ExtractorBonus.getBonusForIngredient(new ItemStack(solution)) == bonus
                            && bonus.getBonusItem().is(expected),
                    bonus + " extractor bonus must supply " + BuiltInRegistries.ITEM.getKey(expected));
        }
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
            case PulseFurnaceRecipe r -> r.getOutput();
            case FermenterRecipe r -> r.getOutput();
            case GrinderRecipe r -> r.getOutput();
            default -> ItemStack.EMPTY;
        };
        if (!out.isEmpty()) {
            obtainable.add(out.getItem());
            return true;
        }
        if (recipe instanceof CentrifugeRecipe r) {
            for (CentrifugeRecipe.ChancedOutput output : r.getOutputs())
                obtainable.add(output.stack().create().getItem());
            return !r.getOutputs().isEmpty() || r.getFluidOutput().isPresent();
        }
        return false;
    }

    private static Item item(String path) {
        return BuiltInRegistries.ITEM.getOptional(
                Identifier.fromNamespaceAndPath(RotaryCraft.MODID, path)).orElse(null);
    }
}
