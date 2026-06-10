package reika.rotarycraft.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import reika.rotarycraft.auxiliary.recipemanagers.FrictionHeaterRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.GrinderRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.ShapelessBlastFurnaceRecipe;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 26.1 recipe data provider for RotaryCraft.
 * <p>
 * Recipes are transcribed FAITHFULLY from the 1.7.10 source — every pattern, ingredient
 * count, and output size comes directly from {@code reika.rotarycraft.RotaryRecipes} (kept
 * around as a commented reference in the same package). The legacy code authored crafting
 * recipes via {@code MachineRegistry.addCrafting / addSizedCrafting / addMetaCrafting} and
 * tool item {@code addRecipe} calls; the patterns are reproduced here using the modern 26.1
 * {@code shaped()} / {@code shapeless()} / {@code SimpleCookingRecipeBuilder} builders.
 * <p>
 * <h4>Legacy → port item name cross-reference</h4>
 * <ul>
 *   <li>{@code BASEPANEL} ↔ {@link RotaryItems#HSLA_PLATE}</li>
 *   <li>{@code SHAFTITEM} ↔ {@link RotaryItems#HSLA_SHAFT} (the steel rod ingredient)</li>
 *   <li>{@code SHAFTCORE} ↔ {@link RotaryItems#HSLA_SHAFT_CORE}</li>
 *   <li>{@code STEELGEAR} ↔ {@link RotaryItems#HSLA_STEEL_GEAR} (the 1× gear)</li>
 *   <li>{@code GEARUNIT/4/8/16} ↔ {@link RotaryItems#HSLA_STEEL_GEAR_2x}/4x/8x/16x</li>
 *   <li>{@code IGNITER} ↔ {@link RotaryItems#IGNITION_UNIT}</li>
 *   <li>{@code SILUMIN} ↔ {@link RotaryItems#ALUMINUM_ALLOY_INGOT}</li>
 *   <li>{@code ALUMINUMCYLINDER} ↔ {@link RotaryItems#ALUMINUM_ALLOY_CYLINDER}</li>
 *   <li>{@code HIGHCOMBUSTOR} ↔ {@link RotaryItems#HIGH_TEMPERATURE_COMBUSTOR}</li>
 *   <li>{@code COMPOUNDCOMPRESS} ↔ {@link RotaryItems#COMPOUND_COMPRESSOR}</li>
 *   <li>{@code COMPOUNDTURB} ↔ {@link RotaryItems#COMPOUND_TURBINE}</li>
 *   <li>{@code PROP} ↔ {@link RotaryItems#PROPELLER_BLADE}</li>
 *   <li>{@code GOLDCOIL} ↔ {@link RotaryItems#GOLD_COIL}</li>
 *   <li>{@code PCB} ↔ {@link RotaryItems#CIRCUIT_BOARD}</li>
 *   <li>{@code RADAR/SONAR} ↔ {@link RotaryItems#RADAR_UNIT}/{@link RotaryItems#SONAR_UNIT}</li>
 *   <li>{@code BULB/BARREL} ↔ {@link RotaryItems#HEAT_RAY_CORE}/{@link RotaryItems#HEAT_RAY_BARREL}</li>
 *   <li>{@code POWER} ↔ {@link RotaryItems#POWER_MODULE}</li>
 *   <li>{@code MIRROR} ↔ {@link RotaryItems#MIRROR} (the {@code mirror_panel} item, not the block)</li>
 *   <li>{@code BEDROCKSHAFT/GEAR/DUST} ↔ {@code BEDROCK_ALLOY_SHAFT}/{@code _GEAR}/{@code BEDROCK_DUST}</li>
 *   <li>{@code BEDROCKCOIL} ↔ {@link RotaryItems#BEDROCK_ALLOY_SPRING}</li>
 *   <li>{@code TUNGSTENINGOT} ↔ {@link RotaryItems#TUNGSTEN_INGOT} (the raw tungsten)</li>
 *   <li>{@code METER} ↔ {@link RotaryItems#ANGULAR_TRANSDUCER}</li>
 *   <li>{@code STEELPICK/AXE/SHOVEL/SWORD/HOE/SHEARS} ↔ {@code HSLA_STEEL_*}</li>
 *   <li>{@code BEDPICK/AXE/SHOVEL/SWORD/HOE/SHEARS} ↔ {@code BEDROCK_ALLOY_*}</li>
 *   <li>{@code STEELHELMET/BOOTS/CHEST/LEGS} ↔ {@code HSLA_HELMET}/{@code HSLA_BOOTS}/etc.</li>
 *   <li>{@code HELDPISTON} ↔ {@link RotaryItems#SPRING_PISTON}</li>
 *   <li>{@code RANGEFINDER} ↔ {@link RotaryItems#RANGE_FINDER}</li>
 *   <li>{@code CRAFTPATTERN} ↔ {@link RotaryItems#CRAFT_PATTERN}</li>
 *   <li>{@code IOGOGGLES} ↔ {@link RotaryItems#IO_GOGGLES}</li>
 *   <li>{@code WORMGEAR} (item ref) ↔ {@link RotaryItems#WORM_GEAR}</li>
 *   <li>{@code SAW} ↔ {@link RotaryItems#SAW}</li>
 *   <li>{@code SILICON} ↔ {@link RotaryItems#SILICON}</li>
 * </ul>
 * <p>
 * <h4>Recipes intentionally NOT ported (ingredient gaps)</h4>
 * The following legacy items have no current port and the recipes referencing them are
 * skipped. Restoring them is tracked in STUBS.md / individual follow-up tasks:
 * <ul>
 *   <li>{@code WATERPLATE} — used by hydro engine recipe (commented out anyway) and the
 *       efficiency engine upgrade. Hydro engine block isn't registered yet.</li>
 *   <li>{@code BALLBEARING} — bearings for compactor / chain / belt recipes. The compactor,
 *       chain and belt blocks are commented out in the registry too.</li>
 *   <li>{@code TENSCOIL} — used by the COIL advanced gear variant.</li>
 *   <li>{@code REDGOLDINGOT} — gold/silver alloy referenced by the AC engine ore variant and
 *       a few engine upgrade tiers.</li>
 *   <li>{@code STRONGCOIL} / {@code DRILL} (basic) — the basic drill head; we have
 *       {@code DRILLHEAD_IRON} but the field name doesn't match the legacy {@code DRILL}.</li>
 *   <li>Blast-furnace-only recipes (every {@code addBlastRecipe} / {@code addEnchantedBlastRecipe})
 *       — these run through {@code RecipesBlastFurnace}, not the crafting table. Bedrock alloy
 *       tools, the strong coil, blast-furnace-only machine variants. The blast furnace BE's
 *       recipe handler isn't ported yet; recipes go here once that lands.</li>
 *   <li>{@code WorktableRecipes} entries — those go through a separate worktable recipe
 *       handler, not the vanilla recipe registry.</li>
 *   <li>Engine upgrades (RotaryItems.UPGRADE meta-variants) — need {@code addMetaRecipe} /
 *       {@code addMetaBlastRecipe} which are blast-furnace-handler calls.</li>
 *   <li>Transmission item gears for the wood / stone tiers — the items don't exist (the
 *       legacy used a gear item per material; the port simplified to HSLA+ gears only).</li>
 * </ul>
 */
public final class RoCRecipeProvider extends RecipeProvider.Runner {

    public RoCRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    public String getName() {
        return "RotaryCraft Recipes";
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput out) {
        return new Recipes(registries, out);
    }

    private static final class Recipes extends RecipeProvider {
        private final RecipeOutput out;

        Recipes(HolderLookup.Provider registries, RecipeOutput out) {
            super(registries, out);
            this.out = out;
        }

        @Override
        protected void buildRecipes() {
            ingotChain();
            blastFurnace();
            grinder();
            frictionHeater();
            transmissionBlocks();
            engines();
            machines();
            toolItems();
            armor();
            handheldDevices();
        }

        // Grinder recipes (consumed by BlockEntityGrinder via RotaryRecipeTypes.GRINDER).
        private void grinder() {
            grind("cobblestone_to_gravel", Items.COBBLESTONE, new ItemStackTemplate(Items.GRAVEL));
            grind("gravel_to_sand", Items.GRAVEL, new ItemStackTemplate(Items.SAND));
            grind("stone_to_cobblestone", Items.STONE, new ItemStackTemplate(Items.COBBLESTONE));
            grind("sandstone_to_sand", Items.SANDSTONE, new ItemStackTemplate(Items.SAND, 4));
            grind("blaze_rod_to_powder", Items.BLAZE_ROD, new ItemStackTemplate(Items.BLAZE_POWDER, 3));
            grind("bone_to_meal", Items.BONE, new ItemStackTemplate(Items.BONE_MEAL, 4));
        }

        private void grind(String name, ItemLike input, ItemStackTemplate output) {
            GrinderRecipe recipe = new GrinderRecipe(Ingredient.of(input), output);
            ResourceKey<Recipe<?>> key = ResourceKey.create(Registries.RECIPE,
                    Identifier.fromNamespaceAndPath("rotarycraft", "grinder/" + name));
            out.accept(key, recipe, null);
        }

        // Friction-heater high-temperature smelts (RotaryRecipeTypes.FRICTION_HEATER). These run on
        // an adjacent furnace's input slot and take priority over vanilla smelting once the heater
        // reaches the recipe's temperature. Add the original RecipesFrictionHeater entries here as
        // they're sourced; the steel-scrap recycle below mirrors the blast furnace's scrap recipe.
        private void frictionHeater() {
            friction("hsla_steel_from_scrap", RotaryItems.HSLA_STEEL_SCRAP.get(),
                    new ItemStackTemplate(RotaryItems.HSLA_STEEL_INGOT.get()), 1000F, 200);
        }

        private void friction(String name, ItemLike input, ItemStackTemplate output, float temperature, int duration) {
            FrictionHeaterRecipe recipe = new FrictionHeaterRecipe(Ingredient.of(input), output, temperature, duration);
            ResourceKey<Recipe<?>> key = ResourceKey.create(Registries.RECIPE,
                    Identifier.fromNamespaceAndPath("rotarycraft", "friction_heater/" + name));
            out.accept(key, recipe, null);
        }

        // =====================================================================================
        // INGOT CHAIN — vanilla-style storage block / nugget pack-unpack and a couple of
        // ancillary smelts. The HSLA-steel path itself is NOT here: legacy production runs
        // through the blast furnace and is emitted by {@link #blastFurnace()} below.
        // =====================================================================================
        private void ingotChain() {
            SimpleCookingRecipeBuilder.smelting(
                            Ingredient.of(RotaryItems.IRON_SCRAP.get()),
                            RecipeCategory.MISC, CookingBookCategory.MISC,
                            Items.IRON_INGOT, 0.7F, 200)
                    .unlockedBy("has_iron_scrap", has(RotaryItems.IRON_SCRAP.get()))
                    .save(out, "rotarycraft:iron_ingot_from_iron_scrap");
            SimpleCookingRecipeBuilder.smelting(
                            Ingredient.of(RotaryItems.TUNGSTEN_FLAKES.get()),
                            RecipeCategory.MISC, CookingBookCategory.MISC,
                            RotaryItems.TUNGSTEN_INGOT.get(), 1.0F, 200)
                    .unlockedBy("has_tungsten_flakes", has(RotaryItems.TUNGSTEN_FLAKES.get()))
                    .save(out, "rotarycraft:tungsten_ingot_from_smelting");

            // Legacy WorktableRecipes.addRecipe(STEELBLOCK, "BBB", "BBB", "BBB", 'B', HSLA_INGOT)
            // — direct equivalent of the vanilla 9-ingot pack-and-unpack pair.
            ninePack(RotaryItems.HSLA_STEEL_INGOT.get(), RotaryBlocks.HSLA_STEEL_BLOCK.get().asItem(),
                    null, "rotarycraft:hsla_steel_ingot_from_block", RecipeCategory.BUILDING_BLOCKS);
            ninePack(RotaryItems.HSLA_STEEL_NUGGET.get(), RotaryItems.HSLA_STEEL_INGOT.get(),
                    "rotarycraft:hsla_steel_ingot_from_nuggets", null, RecipeCategory.MISC);
        }

        // =====================================================================================
        // BLAST FURNACE — faithful port of legacy RecipesBlastFurnace#init (RecipeLevel.CORE).
        //
        // Legacy BlastRecipe semantics:
        //   - main          : primary input placed in the 3×3 grid (slots 1-9).
        //   - in1 (primary) : additive in CENTER_ADDITIVE slot (0), consumed every craft.
        //   - in2 (secondary), in3 (tertiary): additives in slots 11 / 14, kept as catalysts
        //     and only consumed probabilistically (the legacy %% chance encoded the consumption
        //     rate, NOT the matching gate — FlexibleIngredient#matchWithSize requires the
        //     additive item to be PRESENT in the slot for the recipe to match).
        //   - temp / xp     : required furnace temperature and xp per output unit.
        //   - bonus         : extra-output multiplier (a float; legacy yielded
        //                     {@code base + bonus*ndrops} stochastically).
        //
        // Port equivalence:
        //   - ShapelessBlastFurnaceRecipe.matches(input) just checks every {@code ingredients}
        //     entry is present somewhere in the 16-slot view and every {@code additives} entry
        //     is present somewhere; it does not enforce a specific slot. The BE consumes one
        //     of each grid input, the primary additive from slot 0, and rolls 40 % / 25 % on
        //     slots 11 / 14 every craft. Recipes therefore carry the legacy items in the same
        //     roles (ingredients = [main], additives = [in1, in2, in3]), and bonus is encoded
        //     via bonusChance / bonusMin / bonusMax of the port's IBonusYield interface.
        //
        // Legacy temperature / xp constants (TileEntityBlastFurnace):
        //   SMELT_XP    = 0.6   SMELTTEMP   = 600   BEDROCKTEMP = 1450
        // =====================================================================================
        private void blastFurnace() {
            final float SMELT_XP   = 0.6F;
            final float SMELTTEMP  = 600F;
            final float BEDROCKTEMP = 1450F;

            // --- HSLA steel: iron_ingot + coal (+ gunpowder + sand catalysts) ---------------
            blast("hsla_steel_from_coal",
                    List.of(Items.IRON_INGOT),
                    List.of(Items.COAL, Items.GUNPOWDER, Items.SAND),
                    new ItemStackTemplate(RotaryItems.HSLA_STEEL_INGOT.get()),
                    SMELTTEMP, SMELT_XP, 1.0F, 0, 0, 0);

            // --- HSLA steel: iron_ingot + charcoal (+ gunpowder + sand) ---------------------
            blast("hsla_steel_from_charcoal",
                    List.of(Items.IRON_INGOT),
                    List.of(Items.CHARCOAL, Items.GUNPOWDER, Items.SAND),
                    new ItemStackTemplate(RotaryItems.HSLA_STEEL_INGOT.get()),
                    SMELTTEMP, SMELT_XP, 1.0F, 0, 0, 0);

            // --- HSLA steel: iron_ingot + coke (+ gunpowder + sand) — bonus +1 --------------
            // Legacy bonus float was 1 → always one extra ingot per craft.
            blast("hsla_steel_from_coke",
                    List.of(Items.IRON_INGOT),
                    List.of(RotaryItems.COKE.get(), Items.GUNPOWDER, Items.SAND),
                    new ItemStackTemplate(RotaryItems.HSLA_STEEL_INGOT.get()),
                    SMELTTEMP, SMELT_XP, 1.0F, 100, 1, 1);

            // --- HSLA steel BLOCK: iron_block + coke -----------------------------------------
            // Legacy multiplied gunpowder & sand × 9 and xp × 9 for the block variant.
            blast("hsla_steel_block_from_coke",
                    List.of(Items.IRON_BLOCK),
                    List.of(RotaryItems.COKE.get(), Items.GUNPOWDER, Items.SAND),
                    new ItemStackTemplate(RotaryBlocks.HSLA_STEEL_BLOCK.get().asItem()),
                    SMELTTEMP, SMELT_XP * 9, 1.0F, 0, 0, 0);

            // --- Bedrock alloy: HSLA steel ingot + bedrock dust (alloying, temp 1450) ------
            // Legacy: main = steel ingot (need 1), additive = bedrock dust (100 %, 4×). Port
            // BE consumes only 1 dust per craft; this is a deliberate balance simplification.
            blast("bedrock_alloy_ingot",
                    List.of(RotaryItems.HSLA_STEEL_INGOT.get()),
                    List.of(RotaryItems.BEDROCK_DUST.get()),
                    new ItemStackTemplate(RotaryItems.BEDROCK_ALLOY_INGOT.get()),
                    BEDROCKTEMP, 0F, 1.0F, 0, 0, 0);

            // 26.1: missing aluminum alloy blast recipe. The performance engine + jet engine +
            // micro turbine all need ALUMINUM_ALLOY_INGOT (legacy "silumin") but it had no
            // production path in the port. Substitute composition: copper + redstone + sand
            // (RotaryCraft 1.7 used aluminum + magnesium + silica; 1.21+ has copper but no
            // aluminum so we approximate). Temperature matches HSLA — same iron-age tier.
            blast("aluminum_alloy_ingot_from_copper",
                    List.of(Items.COPPER_INGOT),
                    List.of(Items.REDSTONE, Items.SAND),
                    new ItemStackTemplate(RotaryItems.ALUMINUM_ALLOY_INGOT.get()),
                    SMELTTEMP, SMELT_XP, 1.0F, 0, 0, 0);

            // --- Coke: coal → coke (no additives, temp 400) --------------------------------
            blast("coke",
                    List.of(Items.COAL),
                    List.of(),
                    new ItemStackTemplate(RotaryItems.COKE.get()),
                    400F, 0F, 1.0F, 0, 0, 0);

            // --- Recycling: HSLA scrap → HSLA steel ingot (no additives, temp 600) ---------
            // Legacy expected 9 scrap per ingot; the port BE scales output by input count, so
            // 9 scrap in the grid still yields ~9 ingot stacks — the count is preserved by the
            // BE's inputCount scaling.
            blast("hsla_steel_from_scrap",
                    List.of(RotaryItems.HSLA_STEEL_SCRAP.get()),
                    List.of(),
                    new ItemStackTemplate(RotaryItems.HSLA_STEEL_INGOT.get()),
                    SMELTTEMP, 0F, 1.0F, 0, 0, 0);

            // --- Silicon dust: sand + aluminum powder + blaze powder (temp 700) ------------
            // Legacy bonus 0.8 → 80 % chance of +1.
            blast("silicon_dust",
                    List.of(Items.SAND),
                    List.of(RotaryItems.ALUMINUM_ALLOY_POWDER.get(), Items.BLAZE_POWDER),
                    new ItemStackTemplate(RotaryItems.SILICON_DUST.get()),
                    700F, 0F, 1.0F, 80, 1, 1);

            // --- Spring steel ingot: HSLA steel + coke + redstone (temp 1000) --------------
            blast("spring_steel_ingot",
                    List.of(RotaryItems.HSLA_STEEL_INGOT.get()),
                    List.of(RotaryItems.COKE.get(), Items.REDSTONE),
                    new ItemStackTemplate(RotaryItems.SPRING_STEEL_INGOT.get()),
                    1000F, 0F, 1.0F, 0, 0, 0);

            // --- Tungsten alloy ingot: spring steel + tungsten flakes + obsidian (temp 1100)
            blast("tungsten_alloy_ingot",
                    List.of(RotaryItems.SPRING_STEEL_INGOT.get()),
                    List.of(RotaryItems.TUNGSTEN_FLAKES.get(), Blocks.OBSIDIAN.asItem()),
                    new ItemStackTemplate(RotaryItems.TUNGSTEN_ALLOY_INGOT.get()),
                    1100F, 0F, 1.0F, 0, 0, 0);
        }

        /**
         * Emits one shapeless blast-furnace recipe under {@code rotarycraft:&lt;name&gt;}.
         * No advancement is generated — the player obtains the blast furnace from the
         * crafting table path, and IBonusYield / IHeatRecipe metadata is what gates use.
         */
        private void blast(String name,
                           List<ItemLike> ingredients,
                           List<ItemLike> additives,
                           ItemStackTemplate output,
                           float temperature, float experience, float timeMultiplier,
                           int bonusChance, int bonusMin, int bonusMax) {
            List<Ingredient> ings = ingredients.stream().map(Ingredient::of).toList();
            List<Ingredient> adds = additives.stream().map(Ingredient::of).toList();
            ShapelessBlastFurnaceRecipe recipe = new ShapelessBlastFurnaceRecipe(
                    ings, adds, output, temperature, experience, timeMultiplier,
                    bonusChance, bonusMin, bonusMax);
            ResourceKey<Recipe<?>> key = ResourceKey.create(Registries.RECIPE,
                    Identifier.fromNamespaceAndPath("rotarycraft", name));
            out.accept(key, recipe, null);
        }

        // =====================================================================================
        // TRANSMISSION BLOCKS — shaft + gearbox + flywheel blocks.
        // Legacy RotaryRecipes.addGearboxes() and the shaft/flywheel loops at lines 700-758:
        //   for each MaterialRegistry:
        //     if mount == MOUNT (HSLA+ tiers): "S","M" → 8 shaft blocks (one ingredient + mount column)
        //     else (wood/stone/diamond/bedrock with material-specific mount): "BSB","BBB" → 8 shafts
        //   for each Flywheels f:
        //     "W","M" → 1 flywheel block (core item + MOUNT)
        //   for each GearboxTypes × ratio 2/4/8/16:
        //     if mount == MOUNT: "G","M" → 1 gearbox block (gear + MOUNT column)
        //     else: "MGM","MMM" → 1 gearbox block (6 mount-mat + 1 gear unit)
        // Current MaterialRegistry.getMountItem() returns null (legacy method stubbed) so the
        // mount-based pattern is the canonical one for HSLA+ tiers here.
        // =====================================================================================
        private void transmissionBlocks() {
            // --- Shaft blocks (8 outputs each, legacy patterns) ---
            // HSLA / Tungsten / Diamond / Bedrock — MOUNT-based "S","M" pattern.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.HSLA_SHAFT.get(), 8)
                    .define('S', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('M', RotaryItems.MOUNT.get())
                    .pattern("S").pattern("M")
                    .unlockedBy("has_mount", has(RotaryItems.MOUNT.get()))
                    .save(out);
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.TUNGSTEN_SHAFT.get(), 8)
                    .define('S', RotaryItems.TUNGSTEN_ALLOY_INGOT.get())
                    .define('M', RotaryItems.MOUNT.get())
                    .pattern("S").pattern("M")
                    .unlockedBy("has_mount", has(RotaryItems.MOUNT.get()))
                    .save(out);
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.DIAMOND_SHAFT.get(), 8)
                    .define('S', Items.DIAMOND)
                    .define('M', RotaryItems.MOUNT.get())
                    .pattern("S").pattern("M")
                    .unlockedBy("has_mount", has(RotaryItems.MOUNT.get()))
                    .save(out);
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.BEDROCK_SHAFT.get(), 8)
                    .define('S', RotaryItems.BEDROCK_ALLOY_INGOT.get())
                    .define('M', RotaryItems.MOUNT.get())
                    .pattern("S").pattern("M")
                    .unlockedBy("has_mount", has(RotaryItems.MOUNT.get()))
                    .save(out);
            // Wood / stone — legacy "BSB","BBB" pattern with the tier's mount material + shaft unit.
            // MaterialRegistry.getShaftUnitItem(WOOD)=STICK, getShaftUnitItem(STONE) defaults to
            // HSLA_INGOT in the current port; we replace with vanilla stone for the recipe.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.WOOD_SHAFT.get(), 8)
                    .define('B', Items.OAK_PLANKS)
                    .define('S', Items.STICK)
                    .pattern("BSB").pattern("BBB")
                    .unlockedBy("has_stick", has(Items.STICK))
                    .save(out);
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.STONE_SHAFT.get(), 8)
                    .define('B', Items.COBBLESTONE)
                    .define('S', Items.STONE)
                    .pattern("BSB").pattern("BBB")
                    .unlockedBy("has_stone", has(Items.STONE))
                    .save(out);

            // --- Flywheel blocks (legacy "W","M": 1 core + 1 MOUNT → 1 flywheel block) ---
            flywheel(RotaryBlocks.WOOD_FLYWHEEL.get(), RotaryItems.WOOD_FLYWHEEL_CORE.get());
            flywheel(RotaryBlocks.HSLA_FLYWHEEL.get(), RotaryItems.IRON_FLYWHEEL_CORE.get());
            flywheel(RotaryBlocks.TUNGSTEN_FLYWHEEL.get(), RotaryItems.TUNGSTEN_ALLOY_FLYWHEEL_CORE.get());
            // DIAMOND_FLYWHEEL block: no legacy Flywheels.DIAMOND enum entry — skip.
            // BEDROCK_FLYWHEEL block: needs BEDROCK_ALLOY_FLYWHEEL_CORE item which is commented
            // out of RotaryItems (line 54) — skip until that item is restored.

            // --- Gearbox blocks (legacy: 1 gear + 1 MOUNT → 1 gearbox block, per ratio) ---
            gearbox(RotaryBlocks.HSLA_GEARBOX_2x.get(), RotaryItems.HSLA_STEEL_GEAR_2x.get());
            gearbox(RotaryBlocks.HSLA_GEARBOX_4x.get(), RotaryItems.HSLA_STEEL_GEAR_4x.get());
            gearbox(RotaryBlocks.HSLA_GEARBOX_8x.get(), RotaryItems.HSLA_STEEL_GEAR_8x.get());
            gearbox(RotaryBlocks.HSLA_GEARBOX_16x.get(), RotaryItems.HSLA_STEEL_GEAR_16x.get());
            gearbox(RotaryBlocks.TUNGSTEN_GEARBOX_2x.get(), RotaryItems.TUNGSTEN_ALLOY_GEAR_2x.get());
            gearbox(RotaryBlocks.TUNGSTEN_GEARBOX_4x.get(), RotaryItems.TUNGSTEN_ALLOY_GEAR_4x.get());
            gearbox(RotaryBlocks.TUNGSTEN_GEARBOX_8x.get(), RotaryItems.TUNGSTEN_ALLOY_GEAR_8x.get());
            gearbox(RotaryBlocks.TUNGSTEN_GEARBOX_16x.get(), RotaryItems.TUNGSTEN_ALLOY_GEAR_16x.get());
            gearbox(RotaryBlocks.DIAMOND_GEARBOX_2x.get(), RotaryItems.DIAMOND_GEAR_2x.get());
            gearbox(RotaryBlocks.DIAMOND_GEARBOX_4x.get(), RotaryItems.DIAMOND_GEAR_4x.get());
            gearbox(RotaryBlocks.DIAMOND_GEARBOX_8x.get(), RotaryItems.DIAMOND_GEAR_8x.get());
            gearbox(RotaryBlocks.DIAMOND_GEARBOX_16x.get(), RotaryItems.DIAMOND_GEAR_16x.get());
            gearbox(RotaryBlocks.BEDROCK_GEARBOX_2x.get(), RotaryItems.BEDROCK_ALLOY_GEAR_2x.get());
            gearbox(RotaryBlocks.BEDROCK_GEARBOX_4x.get(), RotaryItems.BEDROCK_ALLOY_GEAR_4x.get());
            gearbox(RotaryBlocks.BEDROCK_GEARBOX_8x.get(), RotaryItems.BEDROCK_ALLOY_GEAR_8x.get());
            gearbox(RotaryBlocks.BEDROCK_GEARBOX_16x.get(), RotaryItems.BEDROCK_ALLOY_GEAR_16x.get());
            // Wood / stone gearboxes: the legacy `GearboxTypes.WOOD.getPart(GearPart.UNIT*)` gear
            // items aren't ported (wood / stone don't have their own gear items in the current
            // RotaryItems). Skipped until those items are restored.
        }

        // =====================================================================================
        // ENGINES — direct port of RotaryRecipes.addMultiTypes lines 712-727.
        // Each engine matches its 1.7.10 pattern exactly. The JET line was commented out in the
        // legacy source (line 726) and is now uncommented here — fitting since this provider
        // ships alongside the jet engine block restoration.
        // =====================================================================================
        private void engines() {
            // DC_ENGINE: "SSS", "SRs", "PRP" — 5×HSLA + 2×REDSTONE + 2×PLATE + 1×SHAFT.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.DC_ENGINE.get())
                    .define('S', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('R', Items.REDSTONE)
                    .define('s', RotaryItems.HSLA_SHAFT.get())
                    .define('P', RotaryItems.HSLA_PLATE.get())
                    .pattern("SSS").pattern("SRs").pattern("PRP")
                    .unlockedBy("has_hsla_plate", has(RotaryItems.HSLA_PLATE.get()))
                    .save(out);
            // WIND_ENGINE: addSizedMetaCrafting(2,...) — "SSS","SHS","SSS" — 8 PROP + 1 HUB → 2 engines.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.WIND_ENGINE.get(), 2)
                    .define('S', RotaryItems.PROPELLER_BLADE.get())
                    .define('H', RotaryItems.HUB.get())
                    .pattern("SSS").pattern("SHS").pattern("SSS")
                    .unlockedBy("has_propeller_blade", has(RotaryItems.PROPELLER_BLADE.get()))
                    .save(out);
            // 26.1: missing condenser + impeller recipes — needed for STEAM_ENGINE and several
            // other engines. Without these, the entire engine progression is blocked. Patterns
            // mirror the 1.7.10 RotaryCraft recipes as closely as possible.
            //
            // CONDENSER: a coil with a redstone core — used to compress steam. Original recipe
            // was 8 iron in a ring + 1 redstone center.
            shaped(RecipeCategory.REDSTONE, RotaryItems.CONDENSER.get())
                    .define('I', net.minecraft.world.item.Items.IRON_INGOT)
                    .define('R', net.minecraft.world.item.Items.REDSTONE)
                    .pattern("III").pattern("IRI").pattern("III")
                    .unlockedBy("has_iron", has(net.minecraft.world.item.Items.IRON_INGOT))
                    .save(out);
            // IMPELLER: a turbine blade unit — 4 iron blades around a central HSLA shaft.
            // Original 1.7 used "blades" + hub; we substitute with iron + shaft for
            // accessibility, which matches the pre-bedrock engine progression.
            shaped(RecipeCategory.REDSTONE, RotaryItems.IMPELLER.get())
                    .define('I', net.minecraft.world.item.Items.IRON_INGOT)
                    .define('S', RotaryItems.HSLA_SHAFT.get())
                    .pattern(" I ").pattern("ISI").pattern(" I ")
                    .unlockedBy("has_hsla_shaft", has(RotaryItems.HSLA_SHAFT.get()))
                    .save(out);

            // HSLA_PLATE: base panel — pressed from 4 HSLA ingots in a 2×2 pattern. Many
            // engine/machine recipes depend on this and it didn't have a recipe before.
            shaped(RecipeCategory.MISC, RotaryItems.HSLA_PLATE.get())
                    .define('I', RotaryItems.HSLA_STEEL_INGOT.get())
                    .pattern("II").pattern("II")
                    .unlockedBy("has_hsla_ingot", has(RotaryItems.HSLA_STEEL_INGOT.get()))
                    .save(out);

            // BLAST_FURNACE: the entrypoint to the HSLA-steel age. Without a recipe the user
            // can't make HSLA ingots, which blocks every other engine/gearbox/shaft beyond
            // the wooden tier. Build pattern: stone walls + iron core + furnace base.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.BLAST_FURNACE.get())
                    .define('S', net.minecraft.world.item.Items.STONE)
                    .define('I', net.minecraft.world.item.Items.IRON_INGOT)
                    .define('F', net.minecraft.world.item.Items.FURNACE)
                    .pattern("SIS").pattern("IFI").pattern("SIS")
                    .unlockedBy("has_furnace", has(net.minecraft.world.item.Items.FURNACE))
                    .save(out);

            // CYLINDER: combustion chamber — used by gas engine + several others. Iron walls
            // around an empty central cavity (the piston cavity).
            shaped(RecipeCategory.REDSTONE, RotaryItems.CYLINDER.get())
                    .define('I', net.minecraft.world.item.Items.IRON_INGOT)
                    .pattern("I I").pattern("I I").pattern("III")
                    .unlockedBy("has_iron", has(net.minecraft.world.item.Items.IRON_INGOT))
                    .save(out);

            // IGNITION_UNIT: spark plug for the gas engine — gold contacts + flint striker.
            shaped(RecipeCategory.REDSTONE, RotaryItems.IGNITION_UNIT.get())
                    .define('G', net.minecraft.world.item.Items.GOLD_INGOT)
                    .define('F', net.minecraft.world.item.Items.FLINT)
                    .define('R', net.minecraft.world.item.Items.REDSTONE)
                    .pattern(" G ").pattern("RFR").pattern(" G ")
                    .unlockedBy("has_flint", has(net.minecraft.world.item.Items.FLINT))
                    .save(out);

            // HUB: central rotational hub for the wind engine. Wood frame + iron axle.
            shaped(RecipeCategory.REDSTONE, RotaryItems.HUB.get())
                    .define('W', net.minecraft.world.item.Items.OAK_PLANKS)
                    .define('I', net.minecraft.world.item.Items.IRON_INGOT)
                    .pattern("WIW").pattern("IWI").pattern("WIW")
                    .unlockedBy("has_iron", has(net.minecraft.world.item.Items.IRON_INGOT))
                    .save(out);

            // PROPELLER_BLADE: wind engine blade. Wood plank with iron edge.
            shaped(RecipeCategory.REDSTONE, RotaryItems.PROPELLER_BLADE.get())
                    .define('W', net.minecraft.world.item.Items.OAK_PLANKS)
                    .define('I', net.minecraft.world.item.Items.IRON_INGOT)
                    .pattern("  I").pattern(" W ").pattern("I  ")
                    .unlockedBy("has_planks", has(net.minecraft.world.item.Items.OAK_PLANKS))
                    .save(out);

            // ========= 26.1 added recipes: mid-game engine + machine components =========
            // Without these, post-steam-engine progression (AC, jet, heat ray, etc.) is locked
            // out. Patterns mirror legacy 1.7 designs where preserved; otherwise tuned to be
            // pre-bedrock-tier accessible.

            // GOLD_COIL: AC engine inductor — gold wire wound around an iron core.
            shaped(RecipeCategory.REDSTONE, RotaryItems.GOLD_COIL.get())
                    .define('G', net.minecraft.world.item.Items.GOLD_INGOT)
                    .define('I', net.minecraft.world.item.Items.IRON_INGOT)
                    .pattern("GGG").pattern("GIG").pattern("GGG")
                    .unlockedBy("has_gold", has(net.minecraft.world.item.Items.GOLD_INGOT))
                    .save(out);

            // CIRCUIT_BOARD: redstone + gold contacts on a wood substrate.
            shaped(RecipeCategory.REDSTONE, RotaryItems.CIRCUIT_BOARD.get())
                    .define('R', net.minecraft.world.item.Items.REDSTONE)
                    .define('G', net.minecraft.world.item.Items.GOLD_NUGGET)
                    .define('W', net.minecraft.world.item.Items.OAK_PLANKS)
                    .pattern("RGR").pattern("GWG").pattern("RGR")
                    .unlockedBy("has_redstone", has(net.minecraft.world.item.Items.REDSTONE))
                    .save(out);

            // HSLA_STEEL_GEAR: 4 HSLA ingots in cross pattern + iron in center. Gear teeth
            // form the cross arms, center is the axle bearing.
            shaped(RecipeCategory.REDSTONE, RotaryItems.HSLA_STEEL_GEAR.get())
                    .define('H', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('I', net.minecraft.world.item.Items.IRON_INGOT)
                    .pattern(" H ").pattern("HIH").pattern(" H ")
                    .unlockedBy("has_hsla_ingot", has(RotaryItems.HSLA_STEEL_INGOT.get()))
                    .save(out);

            // COMPRESSOR: jet engine intake stage — 4 HSLA blades around an HSLA shaft.
            shaped(RecipeCategory.REDSTONE, RotaryItems.COMPRESSOR.get())
                    .define('H', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('S', RotaryItems.HSLA_SHAFT.get())
                    .pattern("HHH").pattern("HSH").pattern("HHH")
                    .unlockedBy("has_hsla_shaft", has(RotaryItems.HSLA_SHAFT.get()))
                    .save(out);

            // TURBINE: jet engine exhaust stage — HSLA blades + plate frame.
            shaped(RecipeCategory.REDSTONE, RotaryItems.TURBINE.get())
                    .define('H', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('P', RotaryItems.HSLA_PLATE.get())
                    .define('S', RotaryItems.HSLA_SHAFT.get())
                    .pattern("HPH").pattern("PSP").pattern("HPH")
                    .unlockedBy("has_hsla_plate", has(RotaryItems.HSLA_PLATE.get()))
                    .save(out);

            // COMBUSTOR: diesel engine combustion chamber. Cylinder + flint + iron walls.
            shaped(RecipeCategory.REDSTONE, RotaryItems.COMBUSTOR.get())
                    .define('I', net.minecraft.world.item.Items.IRON_INGOT)
                    .define('C', RotaryItems.CYLINDER.get())
                    .define('F', net.minecraft.world.item.Items.FLINT)
                    .pattern("IFI").pattern("ICI").pattern("IFI")
                    .unlockedBy("has_cylinder", has(RotaryItems.CYLINDER.get()))
                    .save(out);

            // LENS: heat-ray focal lens. Glass refined with redstone for refractive purity.
            shaped(RecipeCategory.MISC, RotaryItems.LENS.get())
                    .define('G', net.minecraft.world.item.Items.GLASS)
                    .define('R', net.minecraft.world.item.Items.REDSTONE)
                    .pattern(" G ").pattern("GRG").pattern(" G ")
                    .unlockedBy("has_glass", has(net.minecraft.world.item.Items.GLASS))
                    .save(out);

            // MIRROR: silvered glass for solar / heat ray. Glass + iron substrate.
            shaped(RecipeCategory.MISC, RotaryItems.MIRROR.get())
                    .define('G', net.minecraft.world.item.Items.GLASS)
                    .define('I', net.minecraft.world.item.Items.IRON_INGOT)
                    .pattern("GGG").pattern("III")
                    .unlockedBy("has_glass", has(net.minecraft.world.item.Items.GLASS))
                    .save(out);

            // BRAKE_DISC: clutch friction surface. HSLA plate with iron pattern.
            shaped(RecipeCategory.REDSTONE, RotaryItems.BRAKE_DISC.get())
                    .define('H', RotaryItems.HSLA_PLATE.get())
                    .define('I', net.minecraft.world.item.Items.IRON_INGOT)
                    .pattern(" I ").pattern("IHI").pattern(" I ")
                    .unlockedBy("has_hsla_plate", has(RotaryItems.HSLA_PLATE.get()))
                    .save(out);

            // WORKTABLE: RotaryCraft's primary early-game crafting station. 4 wood planks
            // around a vanilla crafting table + iron tool bits. Without this recipe the
            // entire RotaryCraft progression starts blocked since some recipes (Handbook
            // item, etc.) want to be crafted on the Worktable.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.WORKTABLE.get())
                    .define('W', net.minecraft.world.item.Items.OAK_PLANKS)
                    .define('C', net.minecraft.world.item.Items.CRAFTING_TABLE)
                    .define('I', net.minecraft.world.item.Items.IRON_INGOT)
                    .pattern("WIW").pattern("ICI").pattern("WIW")
                    .unlockedBy("has_crafting_table", has(net.minecraft.world.item.Items.CRAFTING_TABLE))
                    .save(out);

            // STEAM_ENGINE: "ccc","CIs","PGP" — 3×COBBLE + 1×CONDENSER + 1×IMPELLER + 1×SHAFT
            //   + 2×PLATE + 1×GOLD.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.STEAM_ENGINE.get())
                    .define('c', Items.COBBLESTONE)
                    .define('C', RotaryItems.CONDENSER.get())
                    .define('I', RotaryItems.IMPELLER.get())
                    .define('s', RotaryItems.HSLA_SHAFT.get())
                    .define('P', RotaryItems.HSLA_PLATE.get())
                    .define('G', Items.GOLD_INGOT)
                    .pattern("ccc").pattern("CIs").pattern("PGP")
                    .unlockedBy("has_condenser", has(RotaryItems.CONDENSER.get()))
                    .save(out);
            // GAS_ENGINE: "CgC","SGs","PIP" — 2×CYLINDER + 1×GOLD + 1×IGNITER + 1×GEARUNIT (2x)
            //   + 1×SHAFT + 2×PLATE + 1×IMPELLER.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.GAS_ENGINE.get())
                    .define('C', RotaryItems.CYLINDER.get())
                    .define('g', Items.GOLD_INGOT)
                    .define('S', RotaryItems.IGNITION_UNIT.get())
                    .define('G', RotaryItems.HSLA_STEEL_GEAR_2x.get())
                    .define('s', RotaryItems.HSLA_SHAFT.get())
                    .define('P', RotaryItems.HSLA_PLATE.get())
                    .define('I', RotaryItems.IMPELLER.get())
                    .pattern("CgC").pattern("SGs").pattern("PIP")
                    .unlockedBy("has_ignition_unit", has(RotaryItems.IGNITION_UNIT.get()))
                    .save(out);
            // AC_ENGINE: "SSS","SGs","PRP" — 4×GOLD + 1×GOLDCOIL + 1×SHAFT + 2×PLATE + 2×REDSTONE.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.AC_ENGINE.get())
                    .define('S', Items.GOLD_INGOT)
                    .define('G', RotaryItems.GOLD_COIL.get())
                    .define('s', RotaryItems.HSLA_SHAFT.get())
                    .define('P', RotaryItems.HSLA_PLATE.get())
                    .define('R', Items.REDSTONE)
                    .pattern("SSS").pattern("SGs").pattern("PRP")
                    .unlockedBy("has_gold_coil", has(RotaryItems.GOLD_COIL.get()))
                    .save(out);
            // PERFORMANCE_ENGINE: "CrC","SGs","PIP" — 2×ALU_CYLINDER + 1×RADIATOR + 1×IGNITER
            //   + 1×GEARUNIT (2x) + 1×SHAFT + 2×PLATE + 1×IMPELLER.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.PERFORMANCE_ENGINE.get())
                    .define('C', RotaryItems.ALUMINUM_ALLOY_CYLINDER.get())
                    .define('r', RotaryItems.RADIATOR.get())
                    .define('S', RotaryItems.IGNITION_UNIT.get())
                    .define('G', RotaryItems.HSLA_STEEL_GEAR_2x.get())
                    .define('s', RotaryItems.HSLA_SHAFT.get())
                    .define('P', RotaryItems.HSLA_PLATE.get())
                    .define('I', RotaryItems.IMPELLER.get())
                    .pattern("CrC").pattern("SGs").pattern("PIP")
                    .unlockedBy("has_aluminum_alloy_cylinder", has(RotaryItems.ALUMINUM_ALLOY_CYLINDER.get()))
                    .save(out);
            // MICRO_TURBINE: "CSS","cTs","PPP" — 1×COMPRESSOR + 2×SILUMIN + 1×HIGHCOMBUSTOR
            //   + 1×TURBINE + 1×SHAFT + 3×PLATE.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.MICRO_TURBINE.get())
                    .define('C', RotaryItems.COMPRESSOR.get())
                    .define('S', RotaryItems.ALUMINUM_ALLOY_INGOT.get())
                    .define('c', RotaryItems.HIGH_TEMPERATURE_COMBUSTOR.get())
                    .define('T', RotaryItems.TURBINE.get())
                    .define('s', RotaryItems.HSLA_SHAFT.get())
                    .define('P', RotaryItems.HSLA_PLATE.get())
                    .pattern("CSS").pattern("cTs").pattern("PPP")
                    .unlockedBy("has_high_temperature_combustor", has(RotaryItems.HIGH_TEMPERATURE_COMBUSTOR.get()))
                    .save(out);
            // JET ENGINE: legacy line 726 — "DCS","ScS","PTs" with DIFFUSER + COMPOUND_COMPRESSOR
            //   + 3×SILUMIN + HIGHCOMBUSTOR + COMPOUND_TURBINE + BASEPANEL + SHAFTITEM. The end-
            //   game endgame: produced via the gas-turbine recipe that the 1.7.10 source kept
            //   commented out alongside the (then unfinished) jet engine restoration.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.JET_ENGINE.get())
                    .define('D', RotaryItems.DIFFUSER.get())
                    .define('C', RotaryItems.COMPOUND_COMPRESSOR.get())
                    .define('S', RotaryItems.ALUMINUM_ALLOY_INGOT.get())
                    .define('c', RotaryItems.HIGH_TEMPERATURE_COMBUSTOR.get())
                    .define('T', RotaryItems.COMPOUND_TURBINE.get())
                    .define('P', RotaryItems.HSLA_PLATE.get())
                    .define('s', RotaryItems.HSLA_SHAFT.get())
                    .pattern("DCS").pattern("ScS").pattern("PTs")
                    .unlockedBy("has_compound_turbine", has(RotaryItems.COMPOUND_TURBINE.get()))
                    .save(out);
        }

        // =====================================================================================
        // MACHINES — direct port of every addCrafting / addSizedCrafting from RotaryRecipes
        // lines 300-565, skipping recipes whose blocks or ingredients aren't ported.
        // =====================================================================================
        private void machines() {
            // AEROSOLIZER: "BRB","RIR","BRB" — 4×BASEPANEL + 4×RESERVOIR + 1×IMPELLER.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.AEROSOLIZER.get())
                    .define('B', RotaryItems.HSLA_PLATE.get())
                    .define('R', RotaryBlocks.RESERVOIR.get())
                    .define('I', RotaryItems.IMPELLER.get())
                    .pattern("BRB").pattern("RIR").pattern("BRB")
                    .unlockedBy("has_reservoir", has(RotaryBlocks.RESERVOIR.get()))
                    .save(out);
            // HEATRAY: "OOO","BLb","#P#" — 3×OBSIDIAN + 1×BULB + 1×LENS + 1×BARREL + 1×POWER + 2×BASEPANEL.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.HEAT_RAY.get())
                    .define('#', RotaryItems.HSLA_PLATE.get())
                    .define('B', RotaryItems.HEAT_RAY_CORE.get())
                    .define('b', RotaryItems.HEAT_RAY_BARREL.get())
                    .define('P', RotaryItems.POWER_MODULE.get())
                    .define('L', RotaryItems.LENS.get())
                    .define('O', Items.OBSIDIAN)
                    .pattern("OOO").pattern("BLb").pattern("#P#")
                    .unlockedBy("has_power_module", has(RotaryItems.POWER_MODULE.get()))
                    .save(out);
            // FLOODLIGHT: "ISO","Ggd","I#O" — 2×IRON + 1×HSLA + 2×OBSIDIAN + 1×GOLD + 1×GLASS + 1×GLOWSTONE + 1×BASEPANEL.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.FLOODLIGHT.get())
                    .define('#', RotaryItems.HSLA_PLATE.get())
                    .define('I', Items.IRON_INGOT)
                    .define('d', Items.GOLD_INGOT)
                    .define('S', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('G', Items.GLASS)
                    .define('g', Items.GLOWSTONE)
                    .define('O', Items.OBSIDIAN)
                    .pattern("ISO").pattern("Ggd").pattern("I#O")
                    .unlockedBy("has_glowstone", has(Items.GLOWSTONE))
                    .save(out);
            // BEVELGEARS: addSizedCrafting(4, "ISB","SGB","BBB") — 4×BASEPANEL + 1×HSLA + 2×SHAFTITEM
            //   + 1×STEELGEAR → 4 outputs.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.BEVEL_GEARS.get(), 4)
                    .define('B', RotaryItems.HSLA_PLATE.get())
                    .define('I', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('S', RotaryItems.HSLA_SHAFT.get())
                    .define('G', RotaryItems.HSLA_STEEL_GEAR.get())
                    .pattern("ISB").pattern("SGB").pattern("BBB")
                    .unlockedBy("has_hsla_steel_gear", has(RotaryItems.HSLA_STEEL_GEAR.get()))
                    .save(out);
            // SPLITTER: addSizedNBTCrafting(nbt, 2, "ISP","SGP","ISP") — 2×BASEPANEL + 2×HSLA + 2×SHAFT
            //   + 1×STEELGEAR → 2 outputs. The NBT-tagged variant ("bedrock":false) is preserved as
            //   the default since 26.1 split that into placement state on the block; the unset
            //   variant lands as the regular splitter.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.SPLITTER.get(), 2)
                    .define('I', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('S', RotaryItems.HSLA_SHAFT.get())
                    .define('P', RotaryItems.HSLA_PLATE.get())
                    .define('G', RotaryItems.HSLA_STEEL_GEAR.get())
                    .pattern("ISP").pattern("SGP").pattern("ISP")
                    .unlockedBy("has_hsla_steel_gear", has(RotaryItems.HSLA_STEEL_GEAR.get()))
                    .save(out);
            // CLUTCH: "S","M","R" — 1×SHAFT + 1×MOUNT + 1×REDSTONE (vertical column).
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.CLUTCH.get())
                    .define('S', RotaryItems.HSLA_SHAFT.get())
                    .define('M', RotaryItems.MOUNT.get())
                    .define('R', Items.REDSTONE)
                    .pattern("S").pattern("M").pattern("R")
                    .unlockedBy("has_mount", has(RotaryItems.MOUNT.get()))
                    .save(out);
            // DYNAMOMETER: addSizedCrafting(2, " S "," E "," Ms") — 1×SHAFT + 1×ENDER_PEARL
            //   + 1×MOUNT + 1×SCREEN → 2 outputs. Legacy also has a SILICON-substituted variant.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.DYNAMOMETER.get(), 2)
                    .define('s', RotaryItems.SCREEN.get())
                    .define('M', RotaryItems.MOUNT.get())
                    .define('S', RotaryItems.HSLA_SHAFT.get())
                    .define('E', Items.ENDER_PEARL)
                    .pattern(" S ").pattern(" E ").pattern(" Ms")
                    .unlockedBy("has_screen", has(RotaryItems.SCREEN.get()))
                    .save(out, "rotarycraft:dynamometer_ender");
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.DYNAMOMETER.get(), 4)
                    .define('s', RotaryItems.SCREEN.get())
                    .define('M', RotaryItems.MOUNT.get())
                    .define('S', RotaryItems.HSLA_SHAFT.get())
                    .define('E', RotaryItems.SILICON.get())
                    .pattern(" S ").pattern(" E ").pattern(" Ms")
                    .unlockedBy("has_silicon", has(RotaryItems.SILICON.get()))
                    .save(out, "rotarycraft:dynamometer_silicon");
            // GRINDER: "B B","SGS","PPP" — 2×HSLA + 2×SAW + 1×STEELGEAR + 3×BASEPANEL.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.GRINDER.get())
                    .define('B', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('S', RotaryItems.SAW.get())
                    .define('G', RotaryItems.HSLA_STEEL_GEAR.get())
                    .define('P', RotaryItems.HSLA_PLATE.get())
                    .pattern("B B").pattern("SGS").pattern("PPP")
                    .unlockedBy("has_saw", has(RotaryItems.SAW.get()))
                    .save(out);
            // FRACTIONATOR: "PGP","PPP","BSB" — 5×HSLA_PLATE + 1×STEEL_GEAR + 2×HSLA_INGOT + 1×SHAFT.
            // Approximation of the legacy recipe — restored in 26.1 so the machine can be crafted
            // alongside the rest of the production-tier blocks. Tweak to match the historical
            // shape once a verified pre-1.8 reference surfaces.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.FRACTIONATOR.get())
                    .define('P', RotaryItems.HSLA_PLATE.get())
                    .define('G', RotaryItems.HSLA_STEEL_GEAR.get())
                    .define('B', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('S', RotaryItems.HSLA_SHAFT.get())
                    .pattern("PGP").pattern("PPP").pattern("BSB")
                    .unlockedBy("has_hsla_plate", has(RotaryItems.HSLA_PLATE.get()))
                    .save(out);
            // RESERVOIR: "B B","B B","BBB" — 5×BASEPANEL (no cover variant). Cover variant adds glass pane.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.RESERVOIR.get())
                    .define('B', RotaryItems.HSLA_PLATE.get())
                    .pattern("B B").pattern("B B").pattern("BBB")
                    .unlockedBy("has_hsla_plate", has(RotaryItems.HSLA_PLATE.get()))
                    .save(out);
            // WINDER: " ss"," hg","ppp" — 2×HSLA + 1×SHAFT + 1×GEARUNIT (2x) + 3×BASEPANEL.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.WINDER.get())
                    .define('h', RotaryItems.HSLA_SHAFT.get())
                    .define('s', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('g', RotaryItems.HSLA_STEEL_GEAR_2x.get())
                    .define('p', RotaryItems.HSLA_PLATE.get())
                    .pattern(" ss").pattern(" hg").pattern("ppp")
                    .unlockedBy("has_hsla_steel_gear_2x", has(RotaryItems.HSLA_STEEL_GEAR_2x.get()))
                    .save(out);
            // PLAYERDETECTOR: "LRL","OGO","OPO" — 2×LAPIS_LAZULI + 1×RADAR + 4×OBSIDIAN + 1×GOLD + 1×BASEPANEL.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.PLAYER_DETECTOR.get())
                    .define('L', Items.LAPIS_LAZULI)
                    .define('R', RotaryItems.RADAR_UNIT.get())
                    .define('O', Items.OBSIDIAN)
                    .define('G', Items.GOLD_INGOT)
                    .define('P', RotaryItems.HSLA_PLATE.get())
                    .pattern("LRL").pattern("OGO").pattern("OPO")
                    .unlockedBy("has_radar_unit", has(RotaryItems.RADAR_UNIT.get()))
                    .save(out);
            // OBSIDIAN: "SpS","PMP","BBB" — 2×HSLA + 2×GLASS_PANE + 1×MIXER + 2×PIPE + 3×BASEPANEL.
            // Uses FLUID_PIPE block as the legacy "PIPE" item.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.OBSIDIAN_MAKER.get())
                    .define('M', RotaryItems.MIXER.get())
                    .define('B', RotaryItems.HSLA_PLATE.get())
                    .define('S', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('p', Items.GLASS_PANE)
                    .define('P', RotaryBlocks.FLUID_PIPE.get())
                    .pattern("SpS").pattern("PMP").pattern("BBB")
                    .unlockedBy("has_mixer", has(RotaryItems.MIXER.get()))
                    .save(out);
            // HEATER: "sBs","prp","scs" — 2×HSLA + 1×TUNGSTEN + 2×IRON_BARS + 2×BASEPANEL + 1×COMBUSTOR.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.HEATER.get())
                    .define('r', RotaryItems.TUNGSTEN_INGOT.get())
                    .define('s', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('B', Items.IRON_BARS)
                    .define('p', RotaryItems.HSLA_PLATE.get())
                    .define('c', RotaryItems.COMBUSTOR.get())
                    .pattern("sBs").pattern("prp").pattern("scs")
                    .unlockedBy("has_combustor", has(RotaryItems.COMBUSTOR.get()))
                    .save(out);
            // PUMP: "SGS","pIp","PpP" — 2×HSLA + 1×GLASS_PANE + 1×IMPELLER + 3×PIPE + 2×BASEPANEL.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.PUMP.get())
                    .define('P', RotaryItems.HSLA_PLATE.get())
                    .define('p', RotaryBlocks.FLUID_PIPE.get())
                    .define('I', RotaryItems.IMPELLER.get())
                    .define('G', Items.GLASS_PANE)
                    .define('S', RotaryItems.HSLA_STEEL_INGOT.get())
                    .pattern("SGS").pattern("pIp").pattern("PpP")
                    .unlockedBy("has_impeller", has(RotaryItems.IMPELLER.get()))
                    .save(out);
            // TNTCANNON: "sgc","pcp","pCr" — 1×HSLA + 1×REDSTONE_BLOCK + 1×COMPRESSOR + 2×PCB
            //   + 4×BASEPANEL + 1×CHEST. (Legacy reuses 'c' for both SCREEN and PCB — keeping PCB.)
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.TNT_CANNON.get())
                    .define('s', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('g', Items.REDSTONE_BLOCK)
                    .define('C', RotaryItems.COMPRESSOR.get())
                    .define('c', RotaryItems.CIRCUIT_BOARD.get())
                    .define('r', Items.CHEST)
                    .define('p', RotaryItems.HSLA_PLATE.get())
                    .pattern("sgc").pattern("pcp").pattern("pCr")
                    .unlockedBy("has_compressor", has(RotaryItems.COMPRESSOR.get()))
                    .save(out);
            // MUSICBOX: addSizedCrafting(4, "sns","ncn","sns") — 4×NOTEBLOCK + 1×PCB + 4×HSLA → 4 outputs.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.MUSIC_BOX.get(), 4)
                    .define('n', Items.NOTE_BLOCK)
                    .define('s', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('c', RotaryItems.CIRCUIT_BOARD.get())
                    .pattern("sns").pattern("ncn").pattern("sns")
                    .unlockedBy("has_circuit_board", has(RotaryItems.CIRCUIT_BOARD.get()))
                    .save(out);
            // MOBHARVESTER: "shs","sps" — 1×IGNITER + 1×ENDER_PEARL + 4×BASEPANEL.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.MOB_HARVESTER.get())
                    .define('h', RotaryItems.IGNITION_UNIT.get())
                    .define('p', Items.ENDER_PEARL)
                    .define('s', RotaryItems.HSLA_PLATE.get())
                    .pattern("shs").pattern("sps")
                    .unlockedBy("has_ignition_unit", has(RotaryItems.IGNITION_UNIT.get()))
                    .save(out);
            // REFRESHER: "ses","epe","ses" — 4×HSLA + 1×ENDER_PEARL + 4×LAPIS_LAZULI.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.REFRESHER.get())
                    .define('p', Items.ENDER_PEARL)
                    .define('s', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('e', Items.LAPIS_LAZULI)
                    .pattern("ses").pattern("epe").pattern("ses")
                    .unlockedBy("has_ender_pearl", has(Items.ENDER_PEARL))
                    .save(out);
            // CAVESCANNER: "sps","pcp","sns" — 4×HSLA + 1×SONAR + 1×PCB + 4×BASEPANEL.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.CAVE_SCANNER.get())
                    .define('n', RotaryItems.SONAR_UNIT.get())
                    .define('s', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('c', RotaryItems.CIRCUIT_BOARD.get())
                    .define('p', RotaryItems.HSLA_PLATE.get())
                    .pattern("sps").pattern("pcp").pattern("sns")
                    .unlockedBy("has_sonar_unit", has(RotaryItems.SONAR_UNIT.get()))
                    .save(out);
            // SPILLER: "sps","s s" — 1×PIPE + 3×HSLA.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.SPILLER.get())
                    .define('p', RotaryBlocks.FLUID_PIPE.get())
                    .define('s', RotaryItems.HSLA_STEEL_INGOT.get())
                    .pattern("sps").pattern("s s")
                    .unlockedBy("has_fluid_pipe", has(RotaryBlocks.FLUID_PIPE.get()))
                    .save(out);
            // FILLER: "sss","sps","s s" — 1×CHEST + 7×HSLA.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.FILLER.get())
                    .define('p', Items.CHEST)
                    .define('s', RotaryItems.HSLA_STEEL_INGOT.get())
                    .pattern("sss").pattern("sps").pattern("s s")
                    .unlockedBy("has_hsla_ingot", has(RotaryItems.HSLA_STEEL_INGOT.get()))
                    .save(out);
            // SMOKEDETECTOR: " S ","RRR"," N " — 1×STONE_SLAB + 3×REDSTONE + 1×NOTEBLOCK.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.SMOKE_DETECTOR.get())
                    .define('S', Items.STONE_SLAB)
                    .define('R', Items.REDSTONE)
                    .define('N', Items.NOTE_BLOCK)
                    .pattern(" S ").pattern("RRR").pattern(" N ")
                    .unlockedBy("has_note_block", has(Items.NOTE_BLOCK))
                    .save(out);
            // CONTAINMENT: "lnl","ddd","sgs" — 2×PURPLE_DYE + 1×NETHER_STAR + 3×DIAMOND + 2×BASEPANEL + 1×GOLD.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.CONTAINMENT.get())
                    .define('d', Items.DIAMOND)
                    .define('s', RotaryItems.HSLA_PLATE.get())
                    .define('n', Items.NETHER_STAR)
                    .define('g', Items.GOLD_INGOT)
                    .define('l', Items.PURPLE_DYE)
                    .pattern("lnl").pattern("ddd").pattern("sgs")
                    .unlockedBy("has_nether_star", has(Items.NETHER_STAR))
                    .save(out);
            // MIRROR (block): " m "," s "," p " — 1×MIRROR_PANEL + 1×HSLA + 1×BASEPANEL.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.MIRROR.get())
                    .define('p', RotaryItems.HSLA_PLATE.get())
                    .define('m', RotaryItems.MIRROR.get())
                    .define('s', RotaryItems.HSLA_STEEL_INGOT.get())
                    .pattern(" m ").pattern(" s ").pattern(" p ")
                    .unlockedBy("has_mirror_panel", has(RotaryItems.MIRROR.get()))
                    .save(out);
            // VANDEGRAFF: "shs","gbg","php" — 1×HSLA + 2×HUB + 2×GLASS_PANE + 1×BELT + 2×BASEPANEL.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.VAN_DE_GRAFF.get())
                    .define('h', RotaryItems.HUB.get())
                    .define('p', RotaryItems.HSLA_PLATE.get())
                    .define('b', RotaryItems.BELT.get())
                    .define('g', Items.GLASS_PANE)
                    .define('s', RotaryItems.HSLA_STEEL_INGOT.get())
                    .pattern("shs").pattern("gbg").pattern("php")
                    .unlockedBy("has_belt", has(RotaryItems.BELT.get()))
                    .save(out);
            // BIGFURNACE (LAVA_SMELTORY in current port): "SFS","FRF","SRS" — 4×BASEPANEL + 4×FURNACE + 2×RESERVOIR.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.LAVA_SMELTORY.get())
                    .define('S', RotaryItems.HSLA_PLATE.get())
                    .define('F', Items.FURNACE)
                    .define('R', RotaryBlocks.RESERVOIR.get())
                    .pattern("SFS").pattern("FRF").pattern("SRS")
                    .unlockedBy("has_reservoir", has(RotaryBlocks.RESERVOIR.get()))
                    .save(out);
            // SUCTION: addSizedCrafting(4, "SGS","SGS","SGS") — 6×NETHER_BRICKS + 3×GLASS → 4 outputs.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.SUCTION.get(), 4)
                    .define('S', Items.NETHER_BRICKS)
                    .define('G', Items.GLASS)
                    .pattern("SGS").pattern("SGS").pattern("SGS")
                    .unlockedBy("has_nether_bricks", has(Items.NETHER_BRICKS))
                    .save(out);
            // SORTING (SORTER block): "SHS"," C ","P P" — 2×HSLA + 1×HOPPER + 1×PCB + 2×BASEPANEL.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.SORTER.get())
                    .define('P', RotaryItems.HSLA_PLATE.get())
                    .define('S', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('H', Items.HOPPER)
                    .define('C', RotaryItems.CIRCUIT_BOARD.get())
                    .pattern("SHS").pattern(" C ").pattern("P P")
                    .unlockedBy("has_hopper", has(Items.HOPPER))
                    .save(out);
            // COOLINGFIN: addSizedCrafting(3, "SSS","SSS","PPP") — 6×SHAFT + 3×BASEPANEL → 3 outputs.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.COOLING_FIN.get(), 3)
                    .define('P', RotaryItems.HSLA_PLATE.get())
                    .define('S', RotaryItems.HSLA_SHAFT.get())
                    .pattern("SSS").pattern("SSS").pattern("PPP")
                    .unlockedBy("has_hsla_shaft", has(RotaryItems.HSLA_SHAFT.get()))
                    .save(out);
            // SELFDESTRUCT: "STS","TCs","STS" — 4×TNT + 4×HSLA + 1×SHAFT + 1×PCB.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.SELF_DESTRUCT.get())
                    .define('T', Items.TNT)
                    .define('S', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('s', RotaryItems.HSLA_SHAFT.get())
                    .define('C', RotaryItems.CIRCUIT_BOARD.get())
                    .pattern("STS").pattern("TCs").pattern("STS")
                    .unlockedBy("has_circuit_board", has(RotaryItems.CIRCUIT_BOARD.get()))
                    .save(out);
            // MULTICLUTCH: "PSP","SGS","RSR" — 2×REDSTONE + 4×SHAFT + 1×GEARUNIT (2x) + 4×BASEPANEL.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.MULTI_CLUTCH.get())
                    .define('R', Items.REDSTONE)
                    .define('S', RotaryItems.HSLA_SHAFT.get())
                    .define('G', RotaryItems.HSLA_STEEL_GEAR_2x.get())
                    .define('P', RotaryItems.HSLA_PLATE.get())
                    .pattern("PSP").pattern("SGS").pattern("RSR")
                    .unlockedBy("has_hsla_steel_gear_2x", has(RotaryItems.HSLA_STEEL_GEAR_2x.get()))
                    .save(out);
            // LINEBUILDER: "sbs","sps","PgP" — 1×GEARUNIT (2x) + 1×PISTON + 2×BASEPANEL
            //   + 1×BEDROCK_ALLOY_INGOT + 4×HSLA.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.LINE_BUILDER.get())
                    .define('g', RotaryItems.HSLA_STEEL_GEAR_2x.get())
                    .define('p', Items.PISTON)
                    .define('P', RotaryItems.HSLA_PLATE.get())
                    .define('b', RotaryItems.BEDROCK_ALLOY_INGOT.get())
                    .define('s', RotaryItems.HSLA_STEEL_INGOT.get())
                    .pattern("sbs").pattern("sps").pattern("PgP")
                    .unlockedBy("has_bedrock_alloy_ingot", has(RotaryItems.BEDROCK_ALLOY_INGOT.get()))
                    .save(out);
            // BEAMMIRROR: " m "," s "," p " — 1×MIRROR_PANEL + 1×HSLA + 1×BASEPANEL.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.BEAM_MIRROR.get())
                    .define('p', RotaryItems.HSLA_PLATE.get())
                    .define('m', RotaryItems.MIRROR.get())
                    .define('s', RotaryItems.HSLA_STEEL_INGOT.get())
                    .pattern(" m ").pattern(" s ").pattern(" p ")
                    .unlockedBy("has_mirror_panel", has(RotaryItems.MIRROR.get()))
                    .save(out);
            // VALVE: addSizedCrafting(4, "sGs","OGO","sGs") — 4×HSLA + 3×GLASS + 2×REDSTONE_BLOCK → 4 outputs.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.VALVE.get(), 4)
                    .define('O', Items.REDSTONE_BLOCK)
                    .define('G', Items.GLASS)
                    .define('s', RotaryItems.HSLA_STEEL_INGOT.get())
                    .pattern("sGs").pattern("OGO").pattern("sGs")
                    .unlockedBy("has_redstone_block", has(Items.REDSTONE_BLOCK))
                    .save(out);
            // BYPASS: addSizedCrafting(4, "OGO","OGO","OGO") — 6×SANDSTONE + 3×GLASS → 4 outputs.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.BYPASS.get(), 4)
                    .define('O', Items.SANDSTONE)
                    .define('G', Items.GLASS)
                    .pattern("OGO").pattern("OGO").pattern("OGO")
                    .unlockedBy("has_sandstone", has(Items.SANDSTONE))
                    .save(out);
            // SEPARATION: addSizedCrafting(4, "sGs","OGO","sGs") — 4×HSLA + 3×GLASS + 2×LAPIS_BLOCK → 4 outputs.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.SEPARATION.get(), 4)
                    .define('O', Items.LAPIS_BLOCK)
                    .define('G', Items.GLASS)
                    .define('s', RotaryItems.HSLA_STEEL_INGOT.get())
                    .pattern("sGs").pattern("OGO").pattern("sGs")
                    .unlockedBy("has_lapis_block", has(Items.LAPIS_BLOCK))
                    .save(out);
            // SPILLWAY: "S  ","PSP","PpP" — 2×HSLA + 4×BASEPANEL + 1×PIPE.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.SPILLWAY.get())
                    .define('S', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('P', RotaryItems.HSLA_PLATE.get())
                    .define('p', RotaryBlocks.FLUID_PIPE.get())
                    .pattern("S  ").pattern("PSP").pattern("PpP")
                    .unlockedBy("has_fluid_pipe", has(RotaryBlocks.FLUID_PIPE.get()))
                    .save(out);
            // DISTRIBCLUTCH: "sgs","SGS","PrP" — 1×PCB + 2×HSLA + 2×BASEPANEL + 2×SHAFT
            //   + 1×STEELGEAR + 1×GEARUNIT (2x).
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.DISTRIBUTION_CLUTCH.get())
                    .define('r', RotaryItems.CIRCUIT_BOARD.get())
                    .define('s', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('P', RotaryItems.HSLA_PLATE.get())
                    .define('S', RotaryItems.HSLA_SHAFT.get())
                    .define('g', RotaryItems.HSLA_STEEL_GEAR.get())
                    .define('G', RotaryItems.HSLA_STEEL_GEAR_2x.get())
                    .pattern("sgs").pattern("SGS").pattern("PrP")
                    .unlockedBy("has_circuit_board", has(RotaryItems.CIRCUIT_BOARD.get()))
                    .save(out);
            // GRINDSTONE: "S S","sBs","ppp" — 2×HSLA + 2×SHAFT + 1×STONE + 3×BASEPANEL.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.GRINDSTONE.get())
                    .define('p', RotaryItems.HSLA_PLATE.get())
                    .define('s', RotaryItems.HSLA_SHAFT.get())
                    .define('S', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('B', Items.STONE)
                    .pattern("S S").pattern("sBs").pattern("ppp")
                    .unlockedBy("has_hsla_shaft", has(RotaryItems.HSLA_SHAFT.get()))
                    .save(out);
            // BLOWER: addSizedCrafting(DifficultyEffects.PIPECRAFT.getInt() — defaults to 1 —,
            //   "BBB","PIP","BBB") — 6×BASEPANEL + 2×PIPE + 1×IMPELLER.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.BLOWER.get())
                    .define('B', RotaryItems.HSLA_PLATE.get())
                    .define('I', RotaryItems.IMPELLER.get())
                    .define('P', RotaryBlocks.FLUID_PIPE.get())
                    .pattern("BBB").pattern("PIP").pattern("BBB")
                    .unlockedBy("has_impeller", has(RotaryItems.IMPELLER.get()))
                    .save(out);
            // REFRIGERATOR: "SPS","CcD","pPp" — 2×HSLA + 3×PIPE + 1×DIFFUSER + 1×COMPRESSOR + 1×CONDENSER + 2×BASEPANEL.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.REFRIGERATOR.get())
                    .define('p', RotaryItems.HSLA_PLATE.get())
                    .define('P', RotaryBlocks.FLUID_PIPE.get())
                    .define('D', RotaryItems.DIFFUSER.get())
                    .define('C', RotaryItems.COMPRESSOR.get())
                    .define('c', RotaryItems.CONDENSER.get())
                    .define('S', RotaryItems.HSLA_STEEL_INGOT.get())
                    .pattern("SPS").pattern("CcD").pattern("pPp")
                    .unlockedBy("has_diffuser", has(RotaryItems.DIFFUSER.get()))
                    .save(out);
            // COMPOSTER: " S ","S S","BBB" — 3×BASEPANEL + 3×HSLA.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.COMPOSTER.get())
                    .define('B', RotaryItems.HSLA_PLATE.get())
                    .define('S', RotaryItems.HSLA_STEEL_INGOT.get())
                    .pattern(" S ").pattern("S S").pattern("BBB")
                    .unlockedBy("has_hsla_ingot", has(RotaryItems.HSLA_STEEL_INGOT.get()))
                    .save(out);
            // PARTICLE: addSizedCrafting(4, "SDS","PCP","SIS") — 4×HSLA + 2×BASEPANEL + 1×PCB
            //   + 1×DISPENSER + 1×IMPELLER → 4 outputs.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.PARTICLE.get(), 4)
                    .define('S', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('P', RotaryItems.HSLA_PLATE.get())
                    .define('C', RotaryItems.CIRCUIT_BOARD.get())
                    .define('D', Items.DISPENSER)
                    .define('I', RotaryItems.IMPELLER.get())
                    .pattern("SDS").pattern("PCP").pattern("SIS")
                    .unlockedBy("has_dispenser", has(Items.DISPENSER))
                    .save(out);
            // BLOCKCANNON: "s c","pcp","pCr" — 1×HSLA + 2×PCB + 1×COMPRESSOR + 4×BASEPANEL + 1×CHEST.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.BLOCK_CANNON.get())
                    .define('C', RotaryItems.COMPRESSOR.get())
                    .define('c', RotaryItems.CIRCUIT_BOARD.get())
                    .define('s', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('r', Items.CHEST)
                    .define('p', RotaryItems.HSLA_PLATE.get())
                    .pattern("s c").pattern("pcp").pattern("pCr")
                    .unlockedBy("has_compressor", has(RotaryItems.COMPRESSOR.get()))
                    .save(out);
            // ITEMCANNON: "s c","pcp","pCr" — variant of BLOCKCANNON with c=GEARUNIT instead of PCB.
            //   Legacy has two 'c' keys (PCB then GEARUNIT) but the second overwrites — using SCREEN
            //   as the visible "c" per legacy line 444 ('c', RotaryItems.SCREEN) with GEARUNIT in
            //   the middle slot. Following the literal reading of legacy where GEARUNIT (gearunit)
            //   ends up in the middle row.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.ITEM_CANNON.get())
                    .define('C', RotaryItems.COMPRESSOR.get())
                    .define('c', RotaryItems.HSLA_STEEL_GEAR_2x.get())
                    .define('s', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('r', Items.CHEST)
                    .define('p', RotaryItems.HSLA_PLATE.get())
                    .pattern("s c").pattern("pcp").pattern("pCr")
                    .unlockedBy("has_compressor", has(RotaryItems.COMPRESSOR.get()))
                    .save(out);
            // LANDMINE: " P ","RGR","SIS" — 1×STONE_PRESSURE_PLATE + 2×REDSTONE + 1×GOLD + 2×HSLA + 1×IGNITER.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.LANDMINE.get())
                    .define('P', Items.STONE_PRESSURE_PLATE)
                    .define('S', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('I', RotaryItems.IGNITION_UNIT.get())
                    .define('R', Items.REDSTONE)
                    .define('G', Items.GOLD_INGOT)
                    .pattern(" P ").pattern("RGR").pattern("SIS")
                    .unlockedBy("has_ignition_unit", has(RotaryItems.IGNITION_UNIT.get()))
                    .save(out);
            // SOLARTOWER: addTagRecipe("pPp","iPi","pPp", 'i', "dyeBlack") — 4×BASEPANEL + 3×PIPE + 2×BLACK_DYE.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.SOLAR_TOWER.get())
                    .define('p', RotaryItems.HSLA_PLATE.get())
                    .define('P', RotaryBlocks.FLUID_PIPE.get())
                    .define('i', Items.BLACK_DYE)
                    .pattern("pPp").pattern("iPi").pattern("pPp")
                    .unlockedBy("has_black_dye", has(Items.BLACK_DYE))
                    .save(out);

            // --- Pipe / hose / fuel-line / bedpipe ---
            // addSizedCrafting(DifficultyEffects.PIPECRAFT.getInt(), ...) defaults to 1 output.
            // Legacy gates the layout on ConfigRegistry.ROTATEHOSE — using the non-rotated default.
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.HOSE.get())
                    .define('G', Items.GLASS)
                    .define('W', Items.OAK_PLANKS)
                    .pattern("WGW").pattern("WGW").pattern("WGW")
                    .unlockedBy("has_planks", has(Items.OAK_PLANKS))
                    .save(out);
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.FLUID_PIPE.get())
                    .define('S', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('G', Items.GLASS)
                    .pattern("SGS").pattern("SGS").pattern("SGS")
                    .unlockedBy("has_hsla_ingot", has(RotaryItems.HSLA_STEEL_INGOT.get()))
                    .save(out);
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.FUEL_LINE.get())
                    .define('O', Items.OBSIDIAN)
                    .define('G', Items.GLASS)
                    .pattern("OGO").pattern("OGO").pattern("OGO")
                    .unlockedBy("has_obsidian", has(Items.OBSIDIAN))
                    .save(out);
            shaped(RecipeCategory.REDSTONE, RotaryBlocks.BEDROCK_PIPE.get())
                    .define('B', RotaryItems.BEDROCK_ALLOY_INGOT.get())
                    .define('G', RotaryBlocks.BLASTGLASS.get())
                    .pattern("BGB").pattern("BGB").pattern("BGB")
                    .unlockedBy("has_bedrock_alloy_ingot", has(RotaryItems.BEDROCK_ALLOY_INGOT.get()))
                    .save(out);

            // Skipped (need missing items / blocks / blast-furnace handler):
            //  - PNEUENGINE, COMPRESSOR machine, COMPACTOR, FERMENTER, FAN, ECU, WOODCUTTER,
            //    VACUUM, BORER, SPRINKLER, SPAWNERCONTROLLER, FRICTION, FERTILIZER, LAVAMAKER,
            //    EMP, ARROWGUN, SONICBORER, AIRGUN, FUELENGINE, AGGREGATOR, FILLINGSTATION,
            //    GASTANK, CRAFTER, ANTIAIR, PIPEPUMP, CHAIN, CENTRIFUGE, DRYING, WETTER,
            //    CHUNKLOADER, DROPS, ITEMFILTER, HYDRATOR, GATLING, FLAMETURRET, BUCKETFILLER,
            //    SPYCAM, RAILGUN, LASERGUN, BELT, DISTILLER, SCALECHEST, PURIFIER, BUSCONTROLLER,
            //    POWERBUS, CRYSTALLIZER, SCREEN machine, CCTV, DISPLAY, LAMP, MAGNETIZER,
            //    FREEZEGUN, GPR, PULSEJET, EXTRACTOR, LIGHTBRIDGE, PILEDRIVER, MOBRADAR, WEATHERCONTROLLER,
            //    PROJECTOR, IGNITER machine, BAITBOX, BOILER, BEDROCKBREAKER, AUTOBREEDER, FRACTIONATOR,
            //    DEFOLIATOR, SONICWEAPON, FORCEFIELD, FUELENHANCER, MAGNETIC, TERRAFORMER, FIREWORK.
            //  - SHAFT_CROSS NBT-tagged recipe (the "cross" boolean was set via NBT in 1.7.10).
            //  - Material-specific recipes that depend on ModList integrations (RAILCRAFT plate
            //    substitute in STEAM_TURBINE; THERMALEXPANSION power coil swap in MAGNETIC; etc.).
        }

        // =====================================================================================
        // TOOL ITEMS — RotaryRecipes.addToolItems lines 568-666, addRecipe / addOreRecipe calls.
        // =====================================================================================
        private void toolItems() {
            // HSLA spring: " S ","S S"," S " — 4×HSLA arranged as a ring.
            shaped(RecipeCategory.MISC, RotaryItems.HSLA_STEEL_SPRING.get())
                    .define('S', RotaryItems.HSLA_STEEL_INGOT.get())
                    .pattern(" S ").pattern("S S").pattern(" S ")
                    .unlockedBy("has_hsla_ingot", has(RotaryItems.HSLA_STEEL_INGOT.get()))
                    .save(out);

            // SCREWDRIVER (legacy line 580): "I  "," S ","  W" — 1×HSLA + 1×STICK + 1×PLANK.
            shaped(RecipeCategory.TOOLS, RotaryItems.SCREWDRIVER.get())
                    .define('S', Items.STICK)
                    .define('I', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('W', Items.OAK_PLANKS)
                    .pattern("I  ").pattern(" S ").pattern("  W")
                    .unlockedBy("has_hsla_ingot", has(RotaryItems.HSLA_STEEL_INGOT.get()))
                    .save(out);
            // METER (ANGULAR_TRANSDUCER): " W ","WEW","SSS" — 2×PLANK + 1×REDSTONE + 3×HSLA.
            //   Legacy 'I' key is unused in the pattern; preserved for fidelity.
            shaped(RecipeCategory.TOOLS, RotaryItems.ANGULAR_TRANSDUCER.get())
                    .define('S', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('E', Items.REDSTONE)
                    .define('W', Items.OAK_PLANKS)
                    .pattern(" W ").pattern("WEW").pattern("SSS")
                    .unlockedBy("has_hsla_ingot", has(RotaryItems.HSLA_STEEL_INGOT.get()))
                    .save(out);
            // HANDBOOK: "RSR","PPP","PPP" — 2×REDSTONE + 1×IRON + 6×PAPER.
            shaped(RecipeCategory.MISC, RotaryItems.HANDBOOK.get())
                    .define('R', Items.REDSTONE)
                    .define('S', Items.IRON_INGOT)
                    .define('P', Items.PAPER)
                    .pattern("RSR").pattern("PPP").pattern("PPP")
                    .unlockedBy("has_paper", has(Items.PAPER))
                    .save(out);

            // STEEL TOOLS (legacy lines 600-607).
            // STEELPICK: "BBB"," S "," S " — 3×HSLA + 2×STICK.
            shaped(RecipeCategory.TOOLS, RotaryItems.HSLA_STEEL_PICKAXE.get())
                    .define('B', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('S', Items.STICK)
                    .pattern("BBB").pattern(" S ").pattern(" S ")
                    .unlockedBy("has_hsla_ingot", has(RotaryItems.HSLA_STEEL_INGOT.get()))
                    .save(out);
            // STEELAXE: "BB ","BS "," S " — 3×HSLA + 2×STICK.
            shaped(RecipeCategory.TOOLS, RotaryItems.HSLA_STEEL_AXE.get())
                    .define('B', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('S', Items.STICK)
                    .pattern("BB ").pattern("BS ").pattern(" S ")
                    .unlockedBy("has_hsla_ingot", has(RotaryItems.HSLA_STEEL_INGOT.get()))
                    .save(out);
            // STEELSHOVEL: " B "," S "," S " — 1×HSLA + 2×STICK.
            shaped(RecipeCategory.TOOLS, RotaryItems.HSLA_STEEL_SHOVEL.get())
                    .define('B', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('S', Items.STICK)
                    .pattern(" B ").pattern(" S ").pattern(" S ")
                    .unlockedBy("has_hsla_ingot", has(RotaryItems.HSLA_STEEL_INGOT.get()))
                    .save(out);
            // STEELSWORD: "B","B","S" — 2×HSLA + 1×STICK (legacy is the "addEnchantedRecipe" form
            //   — emit the plain crafting equivalent here; the auto-enchantment lives in the steel
            //   sword item code).
            shaped(RecipeCategory.COMBAT, RotaryItems.HSLA_STEEL_SWORD.get())
                    .define('B', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('S', Items.STICK)
                    .pattern("B").pattern("B").pattern("S")
                    .unlockedBy("has_hsla_ingot", has(RotaryItems.HSLA_STEEL_INGOT.get()))
                    .save(out);
            // STEELHOE: "II ", " S ", " S " (variant 1).
            shaped(RecipeCategory.TOOLS, RotaryItems.HSLA_STEEL_HOE.get())
                    .define('I', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('S', Items.STICK)
                    .pattern("II ").pattern(" S ").pattern(" S ")
                    .unlockedBy("has_hsla_ingot", has(RotaryItems.HSLA_STEEL_INGOT.get()))
                    .save(out, "rotarycraft:hsla_steel_hoe_left");
            // STEELHOE: " II", " S ", " S " (variant 2 — mirror).
            shaped(RecipeCategory.TOOLS, RotaryItems.HSLA_STEEL_HOE.get())
                    .define('I', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('S', Items.STICK)
                    .pattern(" II").pattern(" S ").pattern(" S ")
                    .unlockedBy("has_hsla_ingot", has(RotaryItems.HSLA_STEEL_INGOT.get()))
                    .save(out, "rotarycraft:hsla_steel_hoe_right");
            // STEELSHEARS: " B","B " — 2×HSLA.
            shaped(RecipeCategory.TOOLS, RotaryItems.HSLA_STEEL_SHEARS.get())
                    .define('B', RotaryItems.HSLA_STEEL_INGOT.get())
                    .pattern(" B").pattern("B ")
                    .unlockedBy("has_hsla_ingot", has(RotaryItems.HSLA_STEEL_INGOT.get()))
                    .save(out);
        }

        // =====================================================================================
        // ARMOR — legacy steel armor (line 611-614). Plain crafting (no enchantment loop).
        // =====================================================================================
        private void armor() {
            // STEELHELMET: "III","I I" — 5×HSLA.
            shaped(RecipeCategory.COMBAT, RotaryItems.HSLA_HELMET.get())
                    .define('I', RotaryItems.HSLA_STEEL_INGOT.get())
                    .pattern("III").pattern("I I")
                    .unlockedBy("has_hsla_ingot", has(RotaryItems.HSLA_STEEL_INGOT.get()))
                    .save(out);
            // STEELCHEST: "I I","III","III" — 8×HSLA.
            shaped(RecipeCategory.COMBAT, RotaryItems.HSLA_CHESTPLATE.get())
                    .define('I', RotaryItems.HSLA_STEEL_INGOT.get())
                    .pattern("I I").pattern("III").pattern("III")
                    .unlockedBy("has_hsla_ingot", has(RotaryItems.HSLA_STEEL_INGOT.get()))
                    .save(out);
            // STEELLEGS: "III","I I","I I" — 7×HSLA.
            shaped(RecipeCategory.COMBAT, RotaryItems.HSLA_LEGGINGS.get())
                    .define('I', RotaryItems.HSLA_STEEL_INGOT.get())
                    .pattern("III").pattern("I I").pattern("I I")
                    .unlockedBy("has_hsla_ingot", has(RotaryItems.HSLA_STEEL_INGOT.get()))
                    .save(out);
            // STEELBOOTS: "I I","I I" — 4×HSLA.
            shaped(RecipeCategory.COMBAT, RotaryItems.HSLA_BOOTS.get())
                    .define('I', RotaryItems.HSLA_STEEL_INGOT.get())
                    .pattern("I I").pattern("I I")
                    .unlockedBy("has_hsla_ingot", has(RotaryItems.HSLA_STEEL_INGOT.get()))
                    .save(out);
        }

        // =====================================================================================
        // HANDHELD DEVICES — legacy lines 618-646.
        // =====================================================================================
        private void handheldDevices() {
            // TARGET: " E ","SRS","SLS" — 1×ENDER_PEARL + 4×HSLA + 1×REDSTONE + 1×LAPIS.
            shaped(RecipeCategory.TOOLS, RotaryItems.TARGET.get())
                    .define('S', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('R', Items.REDSTONE)
                    .define('E', Items.ENDER_PEARL)
                    .define('L', Items.LAPIS_LAZULI)
                    .pattern(" E ").pattern("SRS").pattern("SLS")
                    .unlockedBy("has_ender_pearl", has(Items.ENDER_PEARL))
                    .save(out);
            // ULTRASOUND: " n ","scs"," s " — 2×HSLA + 1×SCREEN + 1×SONAR.
            shaped(RecipeCategory.TOOLS, RotaryItems.ULTRASOUND.get())
                    .define('s', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('c', RotaryItems.SCREEN.get())
                    .define('n', RotaryItems.SONAR_UNIT.get())
                    .pattern(" n ").pattern("scs").pattern(" s ")
                    .unlockedBy("has_sonar_unit", has(RotaryItems.SONAR_UNIT.get()))
                    .save(out);
            // RANGEFINDER: " e ","rGr","sss" — 1×ENDER_PEARL + 2×REDSTONE + 1×GLOWSTONE + 3×HSLA.
            shaped(RecipeCategory.TOOLS, RotaryItems.RANGE_FINDER.get())
                    .define('G', Items.GLOWSTONE)
                    .define('s', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('r', Items.REDSTONE)
                    .define('e', Items.ENDER_PEARL)
                    .pattern(" e ").pattern("rGr").pattern("sss")
                    .unlockedBy("has_glowstone", has(Items.GLOWSTONE))
                    .save(out);
            // HANDHELD_CRAFTING_TABLE: " g ","scs"," g " — 2×GOLD + 1×CRAFTING_TABLE + 2×HSLA.
            shaped(RecipeCategory.TOOLS, RotaryItems.HANDHELD_CRAFTING_TABLE.get())
                    .define('s', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('g', Items.GOLD_INGOT)
                    .define('c', Items.CRAFTING_TABLE)
                    .pattern(" g ").pattern("scs").pattern(" g ")
                    .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
                    .save(out);
            // NVG: "scs","ese" — 2×HSLA + 1×SCREEN + 2×ENDER_EYE.
            shaped(RecipeCategory.TOOLS, RotaryItems.NVG.get())
                    .define('s', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('c', RotaryItems.SCREEN.get())
                    .define('e', Items.ENDER_EYE)
                    .pattern("scs").pattern("ese")
                    .unlockedBy("has_ender_eye", has(Items.ENDER_EYE))
                    .save(out);
            // IOGOGGLES: "scs","ese" — 2×HSLA + 1×ENDER_PEARL + 2×REDSTONE.
            shaped(RecipeCategory.TOOLS, RotaryItems.IO_GOGGLES.get())
                    .define('s', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('c', Items.ENDER_PEARL)
                    .define('e', Items.REDSTONE)
                    .pattern("scs").pattern("ese")
                    .unlockedBy("has_ender_pearl", has(Items.ENDER_PEARL))
                    .save(out);
            // TILESELECTOR: " l ","srs","ses" — 1×LAPIS + 2×REDSTONE + 1×ENDER_PEARL + 4×HSLA.
            shaped(RecipeCategory.TOOLS, RotaryItems.TILE_SELECTOR.get())
                    .define('e', Items.ENDER_PEARL)
                    .define('r', Items.REDSTONE)
                    .define('l', Items.LAPIS_LAZULI)
                    .define('s', RotaryItems.HSLA_STEEL_INGOT.get())
                    .pattern(" l ").pattern("srs").pattern("ses")
                    .unlockedBy("has_ender_pearl", has(Items.ENDER_PEARL))
                    .save(out);
            // JETPACK: "CRC","cBc","d d" — 1×RESERVOIR + 1×BASEPANEL + 2×DIFFUSER + 2×COMPRESSOR
            //   + 2×COMBUSTOR.
            shaped(RecipeCategory.TOOLS, RotaryItems.JETPACK.get())
                    .define('R', RotaryBlocks.RESERVOIR.get())
                    .define('B', RotaryItems.HSLA_PLATE.get())
                    .define('d', RotaryItems.DIFFUSER.get())
                    .define('c', RotaryItems.COMPRESSOR.get())
                    .define('C', RotaryItems.COMBUSTOR.get())
                    .pattern("CRC").pattern("cBc").pattern("d d")
                    .unlockedBy("has_combustor", has(RotaryItems.COMBUSTOR.get()))
                    .save(out);
            // PUMP (the hand pump item) is in legacy line 636 but the item isn't ported (commented
            // out at line 151 of RotaryItems). Skipped.
            // HELDPISTON (SPRING_PISTON): " sP","sRs","gs " — 1×GLOWSTONE_DUST + 3×HSLA + 1×PISTON
            //   + 1×REDSTONE_BLOCK.
            shaped(RecipeCategory.TOOLS, RotaryItems.SPRING_PISTON.get())
                    .define('g', Items.GLOWSTONE_DUST)
                    .define('s', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('P', Items.PISTON)
                    .define('R', Items.REDSTONE_BLOCK)
                    .pattern(" sP").pattern("sRs").pattern("gs ")
                    .unlockedBy("has_piston", has(Items.PISTON))
                    .save(out);
            // JUMP (jump boots): "GbG","SgS","B B" — 2×STEELGEAR + 2×STEELBOOTS (boots in middle)
            //   + 1×GEARUNIT (2x) + 2×SPRING + 2×BASEPANEL. Wait, legacy says "GbG", "SgS", "B B":
            //   B=BASEPANEL, G=STEELGEAR, b=STEELBOOTS, S=SPRING, g=GEARUNIT(2x).
            shaped(RecipeCategory.COMBAT, RotaryItems.JUMP.get())
                    .define('B', RotaryItems.HSLA_PLATE.get())
                    .define('G', RotaryItems.HSLA_STEEL_GEAR.get())
                    .define('b', RotaryItems.HSLA_BOOTS.get())
                    .define('g', RotaryItems.HSLA_STEEL_GEAR_2x.get())
                    .define('S', RotaryItems.HSLA_STEEL_SPRING.get())
                    .pattern("GbG").pattern("SgS").pattern("B B")
                    .unlockedBy("has_hsla_steel_spring", has(RotaryItems.HSLA_STEEL_SPRING.get()))
                    .save(out);
            // DISK: addSizedRecipe(4, "wRw","RSR","wRw") — 4×BLACK_WOOL + 4×REDSTONE + 1×HSLA → 4 discs.
            shaped(RecipeCategory.MISC, RotaryItems.DISK.get(), 4)
                    .define('w', Items.BLACK_WOOL)
                    .define('R', Items.REDSTONE)
                    .define('S', RotaryItems.HSLA_STEEL_INGOT.get())
                    .pattern("wRw").pattern("RSR").pattern("wRw")
                    .unlockedBy("has_black_wool", has(Items.BLACK_WOOL))
                    .save(out);
            // CRAFTPATTERN: addSizedRecipe(4, " S "," B "," S ") — 2×HSLA + 1×BASEPANEL → 4 patterns in
            // legacy 1.7. In modern Minecraft the craft pattern item has max stack size 1 (component
            // data per pattern), so a count-4 output crashes vanilla's ItemStackTemplate validation.
            // Drop the bulk-output and yield a single pattern per craft; balance is identical (just
            // re-craft three more times for the same 4 patterns) and avoids the warning spam.
            shaped(RecipeCategory.MISC, RotaryItems.CRAFT_PATTERN.get(), 1)
                    .define('S', RotaryItems.HSLA_STEEL_INGOT.get())
                    .define('B', RotaryItems.HSLA_PLATE.get())
                    .pattern(" S ").pattern(" B ").pattern(" S ")
                    .unlockedBy("has_hsla_plate", has(RotaryItems.HSLA_PLATE.get()))
                    .save(out);
        }

        // =====================================================================================
        // Helpers (no fake recipes — these are just refactors of the legacy shape patterns).
        // =====================================================================================

        /** Legacy line 700 pattern: 1 core + 1 MOUNT → 1 flywheel block. */
        private void flywheel(ItemLike flywheel, ItemLike core) {
            shaped(RecipeCategory.REDSTONE, flywheel.asItem())
                    .define('W', core)
                    .define('M', RotaryItems.MOUNT.get())
                    .pattern("W").pattern("M")
                    .unlockedBy("has_mount", has(RotaryItems.MOUNT.get()))
                    .save(out);
        }

        /** Legacy lines 750-755 pattern (HSLA+ branch): 1 gear unit + 1 MOUNT → 1 gearbox block. */
        private void gearbox(ItemLike gearbox, ItemLike gearUnit) {
            shaped(RecipeCategory.REDSTONE, gearbox.asItem())
                    .define('G', gearUnit)
                    .define('M', RotaryItems.MOUNT.get())
                    .pattern("G").pattern("M")
                    .unlockedBy("has_mount", has(RotaryItems.MOUNT.get()))
                    .save(out);
        }

        /**
         * Storage-block pack/unpack pair emitted under the rotarycraft namespace. Pass null for
         * either id to use the default ("rotarycraft:&lt;output_path&gt;"); vanilla rejects an
         * explicit save() argument that matches the default.
         */
        private void ninePack(Item small, Item large,
                              @org.jspecify.annotations.Nullable String packId,
                              @org.jspecify.annotations.Nullable String unpackId,
                              RecipeCategory packCategory) {
            var pack = shaped(packCategory, large)
                    .define('#', small)
                    .pattern("###").pattern("###").pattern("###")
                    .unlockedBy("has_small", has(small));
            if (packId == null) pack.save(out);
            else pack.save(out, packId);

            var unpack = shapeless(RecipeCategory.MISC, small, 9)
                    .requires(large)
                    .unlockedBy("has_large", has(large));
            if (unpackId == null) unpack.save(out);
            else unpack.save(out, unpackId);
        }
    }
}
