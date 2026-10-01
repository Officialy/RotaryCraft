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
                        : prettify(holder.getId().getPath())));

        addAdvancements();
    }

    // Titles + descriptions for the RotaryCraft advancements (see RotaryAdvancements /
    // RoCAdvancementProvider). Keys: advancements.rotarycraft.<enum_lowercase>.{title,description}.
    private void addAdvancements() {
        adv("rcusebook", "The Handbook", "Obtain the RotaryCraft Handbook");
        adv("dumbextractor", "Brute Force", "Build a DC Electric Engine");
        adv("makesteel", "Forging Ahead", "Smelt HSLA Steel in a Blast Furnace");
        adv("failsteel", "Solid Investment", "Compress HSLA Steel into a block");
        adv("makeyeast", "Rise Up", "Produce Yeast");
        adv("pump", "Pumping Iron", "Build a Pump");
        adv("jetfuel", "Jet Set", "Refine a bucket of Jet Fuel");
        adv("recycle", "Scrap Heap", "Recycle metal gear into HSLA Steel Scrap");
        adv("jetengine", "Now We're Flying", "Build a Jet Engine");
        adv("suckedintojet", "Spaghettification", "Get sucked into a running Jet Engine");
        adv("bedrockbreaker", "Breaking Bedrock", "Break bedrock and collect Bedrock Dust");
        adv("steamengine", "Full Steam Ahead", "Run a Steam Engine");
        adv("steelshaft", "Drive Shaft", "Craft an HSLA Steel Shaft");
        adv("bedrockshaft", "Unbreakable", "Craft a Bedrock Alloy Shaft");
        adv("jetchicken", "Chicken Jet-er", "Feed fifty chickens to a Jet Engine");
        adv("jetfail", "Catastrophic Failure", "Cause a violent Jet Engine failure");
        adv("floodlight", "Let There Be Light", "Power a Floodlight to full brightness");
        adv("landmine", "Watch Your Step", "Step on an armed Landmine");
        adv("overpressure", "Keeping Cool", "Cool a Steam Engine with a Cooling Fin");
        adv("gpr", "X-Ray Vision", "Build and run a Ground Penetrating Radar");
        adv("gprspawner", "Something Lurks Below", "Scan a mob spawner with the GPR");
        adv("gprendportal", "The End Is Near", "Scan an End Portal with the GPR");
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
