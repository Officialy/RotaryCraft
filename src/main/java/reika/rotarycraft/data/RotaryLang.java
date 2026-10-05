package reika.rotarycraft.data;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryItems;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * en_us translations for RotaryCraft.
 * <p>
 * Iterates over the mod's DeferredRegisters and emits a humanised name for each block / item
 * (e.g. {@code wood_flywheel} → {@code "Wood Flywheel"}). Tab labels are added on top.
 * <p>
 * 1.21.5 / NeoForge 26.x: {@code LanguageProvider} now takes a {@link PackOutput} directly
 * (no DataGenerator wrapper) and lives at {@code net.neoforged.neoforge.common.data.LanguageProvider}.
 */
public class RotaryLang extends LanguageProvider {

    public RotaryLang(PackOutput output, String locale) {
        super(output, RotaryCraft.MODID, locale);
    }

    // Curated block-name corrections sourced from assets/rotarycraft/lang/en_USold.lang (1.7.10), for
    // machines whose registry-path prettify reads wrong. Per-tier/variant blocks are intentionally
    // omitted (their prettified per-tier name is more informative than the original shared label).
    private static final Map<String, String> NAME_OVERRIDES = Map.ofEntries(
            Map.entry("borer", "Boring Machine"),
            Map.entry("crafter", "AutoCrafting Unit"),
            Map.entry("scalechest", "Scaleable Chest"),
            Map.entry("aa_gun", "AA Gun"),
            Map.entry("bypass", "Bypass Pipe"),
            Map.entry("containment", "Containment Field"),
            Map.entry("distribution_clutch", "Shaft Distribution Clutch"),
            Map.entry("filler", "Block Filler"),
            Map.entry("fluid_pipe", "Liquid Pipe"),
            Map.entry("fractionator", "Fractionation Unit"),
            Map.entry("hose", "Lubricant Hose"),
            Map.entry("landmine", "Land Mine"),
            Map.entry("lava_smeltory", "Lava Smeltery"),
            Map.entry("line_builder", "Block Ram"),
            Map.entry("magnetizer", "Magnetizing Unit"),
            Map.entry("rotational_dynamo", "Rotational Dynamo"),
            Map.entry("mirror", "Solar Mirror"),
            Map.entry("multi_clutch", "Multi-Directional Clutch"),
            Map.entry("obsidian_maker", "Obsidian Factory"),
            Map.entry("particle", "Particle Display"),
            Map.entry("refresher", "Item Refresher"),
            Map.entry("refrigerator", "Refrigeration Unit"),
            Map.entry("self_destruct", "Self Destruct Mechanism"),
            Map.entry("separation", "Separation Pipe"),
            Map.entry("sorter", "Sorting Machine"),
            Map.entry("spiller", "Liquid Spiller"),
            Map.entry("splitter", "Shaft Junction"),
            Map.entry("suction", "Suction Pipe"),
            Map.entry("tnt_cannon", "TNT Cannon"),
            Map.entry("vacuum", "Item Vacuum"),
            Map.entry("valve", "Valve Pipe"),
            Map.entry("van_de_graff", "Van De Graaff Generator"),
            Map.entry("winder", "Coil Winder"),
            Map.entry("cvt", "CVT"),
            Map.entry("wormgear", "Worm Gear"));

    @Override
    protected void addTranslations() {
        // Creative tabs / categories.
        add("tab.rotarycraft", "RotaryCraft");
        add("tab.rotarycraft.transmission", "RotaryCraft Transmission");
        add("tab.rotarycraft.tools", "RotaryCraft Tools");
        add("tab.rotarycraft.ores", "RotaryCraft Ore Flakes");
        add("tab.rotarycraft.all", "RotaryCraft (All)");

        add("machine.terraformer", "Terraformer");
        add("rcmachinemachine.terraformer", "Terraformer");
        add("gui.rotarycraft.terraformer_status", "%s cells; %s mB water");
        add("item.rotarycraft.tile_selector_linked", "Linked selector to machine at %s");
        add("item.rotarycraft.tile_selector_selected", "Selected biome cell at %s");
        add("gui.rotarycraft.fuel_engine.fuel", "Fuel: %s / 24000 mB");
        add("gui.rotarycraft.mob_radar.range", "%s m");
        add("gui.rotarycraft.sonic_weapon.volume", "Volume:");
        add("gui.rotarycraft.defoliator.poison", "Poison: %s/%s");
        add("message.rotarycraft.motion_tracker.contact", "%s %sm away.");
        add("message.rotarycraft.motion_tracker.mob_attacking", "Mob is Attacking!");
        add("message.rotarycraft.motion_tracker.dragon_attacking", "Dragon is Attacking!");
        add("message.rotarycraft.motion_tracker.low", "Tool charge is low (%s kJ)!");
        add("message.rotarycraft.motion_tracker.very_low", "Tool charge is very low (%s kJ)!");
        add("tooltip.rotarycraft.motion_tracker.charge", "Charge: %s kJ");
        add("tooltip.rotarycraft.spring.charge", "Stored charge: %s");
        add("tooltip.rotarycraft.extractor.duplication", "Duplication chance: %s%% per stage");
        add("tooltip.rotarycraft.extractor.bonus", "Secondary product chance: %s%%");
        add("gui.rotarycraft.fuel_engine.water", "Water: %s / 24000 mB");
        add("gui.rotarycraft.fuel_engine.lubricant", "Lubricant: %s / 24000 mB");
        add("gui.rotarycraft.fuel_engine.temperature", "Temperature: %s°C");
        add("gui.rotarycraft.fuel_engine.duration", "Remaining fuel: %s min %s sec");
        add("tooltip.rotarycraft.fuel_engine.power", "Power: %s %sW");
        add("tooltip.rotarycraft.fuel_engine.torque", "Torque: %s %sNm");
        add("tooltip.rotarycraft.fuel_engine.speed", "Speed: %s %srad/s");
        add("tooltip.rotarycraft.fuel_engine.shift", "Hold Shift for power data");
        add("jei.rotarycraft.fuel_enhancer_power", "Requires 16384 W and 16384 rad/s; rate rises by 1 for each 4x speed");
        add("jei.rotarycraft.fuel_enhancer_consumption", "%s%% chance to consume one of each catalyst per conversion");
        add("gui.rotarycraft.purifier_temperature", "%s \u00b0C");
        add("jei.rotarycraft.purifier_requirements", "Requires %s \u00b0C; up to 5 ingots per batch");

        // Jade config translations
        add("config.jade.plugin_rotarycraft.reservoir_fluid", "Reservoir Fluid");
        add("config.jade.plugin_rotarycraft.machine_power", "Machine Power");
        add("config.jade.plugin_rotarycraft.engine_extras", "Engine Extras");
        add("config.jade.plugin_rotarycraft.pipe_info", "Pipe Info");
        add("config.jade.plugin_rotarycraft.gearbox_lubricant", "Gearbox Lubricant");
        add("config.jade.plugin_rotarycraft.machine_state", "Machine State");
        add("jade.rotarycraft.shutdown", "Shut down by EMP");
        add("jade.rotarycraft.power_required", "Requires %s W");
        add("jade.rotarycraft.status", "Status: %s");
        add("jade.rotarycraft.temperature", "Temperature: %s / %s °C");
        add("jade.rotarycraft.range", "Range: %s / %s blocks");
        add("jade.rotarycraft.fluid", "%s: %s / %s mB");
        add("jade.rotarycraft.comparator", "Comparator: %s / 15");
        add("jei.rotarycraft.lava_requirements", "Requires %s °C and %s J");
        add("jei.rotarycraft.compactor_input", "4 items consumed");
        add("jei.rotarycraft.compactor_requirements", "Requires %s kPa and %s °C");
        add("jei.rotarycraft.wetter_duration", "%s base ticks");
        add("jei.rotarycraft.magnetizer_input", "Minimum speed: %s rad/s; alternating redstone required");
        add("jei.rotarycraft.magnetizer_output", "Magnetic charge increases over time; %s rad/s per µT");
        add("jei.rotarycraft.composter_yeast", "Yeast catalyst; consumed occasionally");
        add("jei.rotarycraft.composter_temperature", "Requires 40–70 °C");
        add("jei.rotarycraft.refrigerator_yield", "100–2000 mB per ice; yield scales with torque");
        add("jei.rotarycraft.refrigerator_byproduct", "25%% chance; quantity varies");
        add("jei.rotarycraft.obsidian_temperature", "100%% obsidian at 550–750 °C; output varies outside this range");
        add("jei.rotarycraft.cobblestone_temperature", "Cobblestone outside the obsidian temperature range; chance varies");
        add("jei.rotarycraft.cobblestone_lava", "50 mB must be present; lava is not consumed for cobblestone");

        RotaryBlocks.BLOCKS.getEntries().forEach(holder -> {
            String path = holder.getId().getPath();
            addBlock(holder, NAME_OVERRIDES.getOrDefault(path, prettify(path)));
        });

        // Item translations for the dedicated items register (RotaryBlocks.ITEMS holds the
        // auto-generated BlockItems, which inherit their block's translation key — registering
        // them again would emit a duplicate-key warning, so they're skipped here).
        RotaryItems.ITEMS.getEntries().forEach(holder ->
                addItem(holder, "debug".equals(holder.getId().getPath()) ? "Magic Wand"
                        : "craft_pattern".equals(holder.getId().getPath()) ? "Crafting Pattern" //V33a item.craftpattern
                          : reika.rotarycraft.registry.ModExtractOres.itemName(holder.get()) != null
                          ? reika.rotarycraft.registry.ModExtractOres.itemName(holder.get()) : prettify(holder.getId().getPath())));

        addAdvancements();
    }

    // Titles + descriptions for the RotaryCraft advancements (see RotaryAdvancements /
    // RoCAdvancementProvider). Keys: advancements.rotarycraft.<enum_lowercase>.{title,description}.
    private void addAdvancements() {
        add("message.rotarycraft.tool_charge_depleted", "Tool charge is depleted!");
        add("tooltip.rotarycraft.gravel_gun.damage", "Dealing %s hearts of damage per shot");
        add("tooltip.rotarycraft.gravel_gun.uncharged", "Unable to fire - requires charging");
        add("advancements.rotarycraft.rcusebook.title", "Actually Doing The Research");
        add("advancements.rotarycraft.rcusebook.description", "Read the handbook instead of making basic mistakes and calling it a bug");
        add("advancements.rotarycraft.dumbextractor.title", "Not How It Works");
        add("advancements.rotarycraft.dumbextractor.description", "Completely fail to grasp power requirements and distribution");
        add("advancements.rotarycraft.makesteel.title", "Steelmaker");
        add("advancements.rotarycraft.makesteel.description", "Make a steel ingot in the blast furnace");
        add("advancements.rotarycraft.failsteel.title", "Remedial Materials Science");
        add("advancements.rotarycraft.failsteel.description", "Waste lots of iron in making steel");
        add("advancements.rotarycraft.worktable.title", "Constructive");
        add("advancements.rotarycraft.worktable.description", "Build the worktable");
        add("advancements.rotarycraft.makeyeast.title", "Fermenter");
        add("advancements.rotarycraft.makeyeast.description", "Make yeast in the fermenter");
        add("advancements.rotarycraft.extractor.title", "Multiplier");
        add("advancements.rotarycraft.extractor.description", "Multiply ores in the extractor");
        add("advancements.rotarycraft.pcb.title", "Advanced Control");
        add("advancements.rotarycraft.pcb.description", "Make a circuit board");
        add("advancements.rotarycraft.pump.title", "Pumped");
        add("advancements.rotarycraft.pump.description", "Pump up water");
        add("advancements.rotarycraft.gpr.title", "Sneak Peek");
        add("advancements.rotarycraft.gpr.description", "Use a GPR");
        add("advancements.rotarycraft.borer.title", "Getting Bored");
        add("advancements.rotarycraft.borer.description", "Activate the Borer");
        add("advancements.rotarycraft.jetfuel.title", "Liquid Power");
        add("advancements.rotarycraft.jetfuel.description", "Make jet fuel");
        add("advancements.rotarycraft.recycle.title", "Yay for Recycling");
        add("advancements.rotarycraft.recycle.description", "Melt scrap back into ingots");
        add("advancements.rotarycraft.jetengine.title", "A Matter of Thrust");
        add("advancements.rotarycraft.jetengine.description", "Fire up a jet engine");
        add("advancements.rotarycraft.makerailgun.title", "Overkill");
        add("advancements.rotarycraft.makerailgun.description", "Make a railgun");
        add("advancements.rotarycraft.suckedintojet.title", "This Really Sucks");
        add("advancements.rotarycraft.suckedintojet.description", "Get sucked into a jet engine");
        add("advancements.rotarycraft.bedrockbreaker.title", "Unbreakable? Really?");
        add("advancements.rotarycraft.bedrockbreaker.description", "Make bedrock dust");
        add("advancements.rotarycraft.steamengine.title", "I Think I Can");
        add("advancements.rotarycraft.steamengine.description", "Activate a steam engine");
        add("advancements.rotarycraft.steelshaft.title", "Engaged");
        add("advancements.rotarycraft.steelshaft.description", "Make a steel shaft");
        add("advancements.rotarycraft.cvt.title", "Versatility");
        add("advancements.rotarycraft.cvt.description", "Make a CVT");
        add("advancements.rotarycraft.bedrockshaft.title", "Now This Is Unbreakable");
        add("advancements.rotarycraft.bedrockshaft.description", "Make a bedrock shaft");
        add("advancements.rotarycraft.bedrocktools.title", "No More Tool Replacement!");
        add("advancements.rotarycraft.bedrocktools.description", "Make a bedrock tool");
        add("advancements.rotarycraft.jetchicken.title", "Doing It Wrong");
        add("advancements.rotarycraft.jetchicken.description", "Suck 50 chickens into a jet engine");
        add("advancements.rotarycraft.jetfail.title", "...Oops");
        add("advancements.rotarycraft.jetfail.description", "Cause a jet engine's violent failure");
        add("advancements.rotarycraft.lightfall.title", "Long Way Down");
        add("advancements.rotarycraft.lightfall.description", "Die due to a light bridge deactivation");
        add("advancements.rotarycraft.sprinkler.title", "Green Thumb");
        add("advancements.rotarycraft.sprinkler.description", "Turn on a sprinkler");
        add("advancements.rotarycraft.floodlight.title", "Illuminating");
        add("advancements.rotarycraft.floodlight.description", "Turn on a floodlight");
        add("advancements.rotarycraft.damagegears.title", "Grind My Gears");
        add("advancements.rotarycraft.damagegears.description", "Damage a gearbox");
        add("advancements.rotarycraft.diamondgears.title", "Efficiency");
        add("advancements.rotarycraft.diamondgears.description", "Make a diamond gearbox");
        add("advancements.rotarycraft.mrads32.title", "Overspeed");
        add("advancements.rotarycraft.mrads32.description", "Send power at 32000000 rad/s without breaking the shaft");
        add("advancements.rotarycraft.gigawatt.title", "Overpowered");
        add("advancements.rotarycraft.gigawatt.description", "Send 1GW of power down a shaft without breaking it");
        add("advancements.rotarycraft.raildragon.title", "Blown Out Of The Sky");
        add("advancements.rotarycraft.raildragon.description", "Kill an EnderDragon with the railgun");
        add("advancements.rotarycraft.railkilled.title", "Too Close For Comfort");
        add("advancements.rotarycraft.railkilled.description", "Get killed by a railgun");
        add("advancements.rotarycraft.gravelgun.title", "Sniped");
        add("advancements.rotarycraft.gravelgun.description", "One-Hit kill a mob at 80m with the gravel gun");
        add("advancements.rotarycraft.landmine.title", "Watch Your Step");
        add("advancements.rotarycraft.landmine.description", "Step on a land mine");
        add("advancements.rotarycraft.netherheatray.title", "Boom Miner");
        add("advancements.rotarycraft.netherheatray.description", "Dig 500m with the Heat Ray in the Nether");
        add("advancements.rotarycraft.gprspawner.title", "Buried Treasure");
        add("advancements.rotarycraft.gprspawner.description", "Locate a Monster Spawner with the GPR");
        add("advancements.rotarycraft.gprendportal.title", "Who Needs Ender Eyes?");
        add("advancements.rotarycraft.gprendportal.description", "Locate an End Portal with a GPR");
        add("advancements.rotarycraft.cutknot.title", "Cutting the Knot");
        add("advancements.rotarycraft.cutknot.description", "Dig through a Twilight Forest labyrinth with the Borer");
        add("advancements.rotarycraft.rareextract.title", "Fortune XVIII");
        add("advancements.rotarycraft.rareextract.description", "Process a rare ore in the Extractor");
        add("advancements.rotarycraft.massivehit.title", "Hand Cannon");
        add("advancements.rotarycraft.massivehit.description", "Deal 250 hearts of damage with the gravel gun");
        add("advancements.rotarycraft.overpressure.title", "Into The Red");
        add("advancements.rotarycraft.overpressure.description", "Cause a steam engine to overheat and explode");
        add("advancements.rotarycraft.doublekill.title", "Two birds, one stone");
        add("advancements.rotarycraft.doublekill.description", "Kill two mobs at once with one gravel gun shot");
        add("advancements.rotarycraft.insanity.title", "Insanity");
        add("advancements.rotarycraft.insanity.description", "Run an extractor at 1-tick operation times for all four stages");
        add("advancements.rotarycraft.instantbed.title", "Insanity II");
        add("advancements.rotarycraft.instantbed.description", "Run a bedrock breaker at 1-tick operation times");
        add("advancements.rotarycraft.pulsefire.title", "Pulse Jet Arsonist");
        add("advancements.rotarycraft.pulsefire.description", "Set your house on fire with a Pulse Jet Furnace");
    }

    private void adv(String name, String title, String description) {
        add("advancements.rotarycraft." + name + ".title", title);
        add("advancements.rotarycraft." + name + ".description", description);
    }

    /**
     * Acronym whitelist to prevent prettify() from lowercasing certain registry-path segments. For example:
     * {@code hsla} → "HSLA", {@code dc} → "DC",
     * {@code ac} → "AC",
     * {@code emp} → "EMP", {@code tnt} → "TNT"
     */
    private static final Set<String> ACRONYMS = Set.of(
            "hsla", "dc", "ac", "emp", "tnt", "cvt", "io", "cctv", "gpr", "rc", "ic"
    );

    private static String prettify(String path) {
        String[] parts = path.split("_");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) sb.append(' ');
            String p = parts[i];
            if (p.isEmpty()) continue;
            if (ACRONYMS.contains(p)) {
                sb.append(p.toUpperCase(Locale.ROOT));
            } else {
                sb.append(Character.toUpperCase(p.charAt(0)));
                if (p.length() > 1) sb.append(p.substring(1).toLowerCase(Locale.ROOT));
            }
        }
        return sb.toString();
    }
}
