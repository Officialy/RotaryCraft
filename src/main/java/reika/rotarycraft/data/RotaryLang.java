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
        // Dotted, matching the keys RotaryCraftTabs actually looks up. Three of these were
        // registered with underscores, so the transmission/tools/ores tabs showed their raw
        // translation key in-game; the other two were already dotted.
        add("tab.rotarycraft", "RotaryCraft");
        add("tab.rotarycraft.transmission", "RotaryCraft Transmission");
        add("tab.rotarycraft.tools", "RotaryCraft Tools");
        add("tab.rotarycraft.ores", "RotaryCraft Ore Flakes");
        add("tab.rotarycraft.all", "RotaryCraft (All)");

        // Jade config translations
        add("config.jade.plugin_rotarycraft.reservoir_fluid", "Reservoir Fluid");
        add("config.jade.plugin_rotarycraft.machine_power", "Machine Power");
        add("config.jade.plugin_rotarycraft.engine_extras", "Engine Extras");
        add("config.jade.plugin_rotarycraft.pipe_info", "Pipe Info");

        // Block translations — derived from each block's registry path, with the curated names from the
        // 1.7.10 en_USold.lang for the machines whose prettified path reads wrong (e.g. "Hose" ->
        // "Lubricant Hose", "Fluid Pipe" -> "Liquid Pipe"). Only the names where the original is clearly
        // better are overridden; per-tier blocks (Wood Shaft, HSLA Flywheel, ...) keep their prettified
        // name since the original lumped them under one shared label.
        RotaryBlocks.BLOCKS.getEntries().forEach(holder -> {
            String path = holder.getId().getPath();
            addBlock(holder, NAME_OVERRIDES.getOrDefault(path, prettify(path)));
        });

        // Item translations for the dedicated items register (RotaryBlocks.ITEMS holds the
        // auto-generated BlockItems, which inherit their block's translation key — registering
        // them again would emit a duplicate-key warning, so they're skipped here).
        RotaryItems.ITEMS.getEntries().forEach(holder ->
                addItem(holder, "debug".equals(holder.getId().getPath()) ? "Magic Wand" : prettify(holder.getId().getPath())));

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
     * Convert {@code wood_flywheel} → {@code "Wood Flywheel"}. Acronym whitelist preserves
     * all-caps for tokens like {@code hsla} → "HSLA", {@code dc} → "DC", {@code ac} → "AC",
     * {@code emp} → "EMP", {@code tnt} → "TNT" — vanilla title-casing would render these as
     * "Hsla Steel Plate" / "Dc Engine" which reads poorly for the established acronyms in
     * RotaryCraft's UI.
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
