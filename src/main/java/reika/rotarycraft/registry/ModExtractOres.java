package reika.rotarycraft.registry;

import java.util.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredItem;

/** V33a ModOreList catalog; ore dictionary aliases are now reloadable common item tags. */
public enum ModExtractOres {
    TIN("tin", "Tin", 1, "COMMON", "ingots/tin", "ores/tin", "ores/cassiterite"),
    COPPER("copper", "Copper", 1, "EVERYWHERE", "ingots/copper", "ores/copper", "ores/tetrahedrite"),
    LEAD("lead", "Lead", 1, "AVERAGE", "ingots/lead", "ores/lead"),
    NICKEL("nickel", "Nickel", 1, "SCATTERED", "ingots/nickel", "ores/nickel", "ores/pentlandite"),
    SILVER("silver", "Silver", 1, "AVERAGE", "ingots/silver", "ores/silver"),
    GALENA("galena", "Galena", 1, "SCATTERED", "dusts/galena", "ores/galena"),
    ALUMINUM("aluminum", "Aluminum", 1, "COMMON", "ingots/aluminum", "ores/aluminum", "ores/aluminium", "ores/natural_aluminum"),
    IRIDIUM("iridium", "Iridium", 1, "RARE", "ingots/iridium", "ores/iridium"),
    FIRESTONE("firestone", "Firestone", 1, "RARE", "shards/firestone", "ores/firestone"),
    CERTUSQUARTZ("certus_quartz", "Certus Quartz", 2, "AVERAGE", "gems/certus_quartz", "ores/certus_quartz", "ores/certus_quartz_charged", "ores/charged_certus_quartz"),
    URANIUM("uranium", "Uranium", 1, "SCATTERED", "ingots/uranium", "ores/uranium", "ores/yellorite", "ores/uraninite"),
    CINNABAR("cinnabar", "Mercury", 1, "SCATTERED", "items/quicksilver", "ores/cinnabar"),
    AMBER("amber", "Amber", 1, "SCATTERED", "gems/amber", "ores/amber"),
    INFUSEDAIR("infused_air", "Air Infused", 2, "SCATTERED", "shards/air", "ores/infused_air"),
    INFUSEDFIRE("infused_fire", "Fire Infused", 2, "SCATTERED", "shards/fire", "ores/infused_fire"),
    INFUSEDWATER("infused_water", "Water Infused", 2, "AVERAGE", "shards/water", "ores/infused_water"),
    INFUSEDEARTH("infused_earth", "Earth Infused", 2, "SCATTERED", "shards/earth", "ores/infused_earth"),
    INFUSEDENTROPY("infused_entropy", "Entropy Infused", 2, "SCARCE", "shards/entropy", "ores/infused_entropy"),
    INFUSEDORDER("infused_order", "Order Infused", 2, "SCARCE", "shards/order", "ores/infused_order"),
    APATITE("apatite", "Apatite", 3, "COMMON", "gems/apatite", "ores/apatite"),
    SALTPETER("saltpeter", "Saltpeter", 2, "AVERAGE", "dusts/saltpeter", "ores/saltpeter"),
    TUNGSTEN("tungsten", "Tungsten", 1, "COMMON", "dusts/tungsten", "ores/tungsten", "ores/tungstate"),
    NIKOLITE("nikolite", "Nikolite", 5, "COMMON", "dusts/electrotine", "ores/nikolite", "ores/electrotine"),
    PERIDOT("peridot", "Peridot", 1, "SCARCE", "gems/peridot", "ores/peridot"),
    RUBY("ruby", "Ruby", 1, "SCARCE", "gems/ruby", "ores/ruby"),
    SAPPHIRE("sapphire", "Sapphire", 1, "SCARCE", "gems/sapphire", "ores/sapphire"),
    MONAZIT("monazit_ore", "Monazit", 4, "AVERAGE", "items/forcicium_item", "ores/monazit", "ores/monazite", "ores/monazit_ore"),
    FORCE("force", "Force", 3, "AVERAGE", "gems/force", "ores/force"),
    NETHERCOAL("nether_coal", "Nether Coal", 1, "EVERYWHERE", "items/coal", "ores/nether_coal"),
    NETHERIRON("nether_iron", "Nether Iron", 1, "COMMON", "ingots/iron", "ores/nether_iron"),
    NETHERGOLD("nether_gold", "Nether Gold", 1, "AVERAGE", "ingots/gold", "ores/nether_gold"),
    NETHERREDSTONE("nether_redstone", "Nether Redstone", 4, "COMMON", "dusts/redstone", "ores/nether_redstone"),
    NETHERLAPIS("nether_lapis", "Nether Lapis", 6, "AVERAGE", "dyes/blue", "ores/nether_lapis"),
    NETHERDIAMOND("nether_diamond", "Nether Diamond", 1, "SCARCE", "gems/diamond", "ores/nether_diamond"),
    NETHEREMERALD("nether_emerald", "Nether Emerald", 1, "SCARCE", "gems/emerald", "ores/nether_emerald"),
    NETHERTIN("nether_tin", "Nether Tin", 1, "COMMON", "ingots/tin", "ores/nether_tin"),
    NETHERCOPPER("nether_copper", "Nether Copper", 1, "COMMON", "ingots/copper", "ores/nether_copper"),
    NETHERLEAD("nether_lead", "Nether Lead", 1, "AVERAGE", "ingots/lead", "ores/nether_lead"),
    NETHERNICKEL("nether_nickel", "Nether Nickel", 1, "AVERAGE", "ingots/nickel", "ores/nether_nickel"),
    NETHERSILVER("nether_silver", "Nether Silver", 1, "AVERAGE", "ingots/silver", "ores/nether_silver"),
    NETHERNIKOLITE("nether_nikolite", "Nether Nikolite", 5, "AVERAGE", "dusts/nikolite", "ores/nether_nikolite"),
    COBALT("cobalt", "Cobalt", 1, "SCARCE", "ingots/cobalt", "ores/cobalt"),
    ARDITE("ardite", "Ardite", 1, "SCARCE", "ingots/ardite", "ores/ardite"),
    PLATINUM("platinum", "Platinum", 1, "RARE", "ingots/platinum", "ores/platinum", "ores/cooperite"),
    NETHERPLATINUM("nether_platinum", "Nether Platinum", 1, "RARE", "ingots/platinum", "ores/nether_platinum"),
    ZINC("zinc", "Zinc", 1, "COMMON", "ingots/zinc", "ores/zinc", "ores/sphalerite"),
    OSMIUM("osmium", "Osmium", 1, "COMMON", "ingots/osmium", "ores/osmium"),
    NETHERPIGIRON("nether_pig_iron", "Pig Iron", 1, "SCATTERED", "ingots/steel", "ores/nether_pig_iron", "ores/nether_steel"),
    SULFUR("sulfur", "Sulfur", 3, "COMMON", "dusts/sulfur", "ores/sulfur"),
    PITCHBLENDE("pitchblende", "Pitchblende", 1, "AVERAGE", "ingots/uranium", "ores/pitchblende"),
    CADMIUM("cadmium", "Cadmium", 1, "AVERAGE", "ingots/cadmium", "ores/cadmium"),
    INDIUM("indium", "Indium", 1, "SCATTERED", "ingots/indium", "ores/indium"),
    FLUORITE("fluorite", "Fluorite", 6, "EVERYWHERE", "gems/fluorite", "ores/fluorite"),
    BAUXITE("bauxite", "Bauxite", 1, "AVERAGE", "dusts/bauxite", "ores/bauxite"),
    SODALITE("sodalite", "Sodalite", 1, "EVERYWHERE", "dyes/blue", "ores/sodalite"),
    PYRITE("pyrite", "Pyrite", 1, "COMMON", "dusts/pyrite", "ores/pyrite"),
    AMMONIUM("ammonium", "Ammonium Chloride", 1, "SCATTERED", "dusts/ammonium", "ores/ammonium"),
    CALCITE("calcite", "Calcite", 1, "SCATTERED", "gems/calcite", "ores/calcite"),
    CHIMERITE("chimerite", "Chimerite", 2, "SCATTERED", "gems/chimerite", "ores/chimerite"),
    VINTEUM("vinteum", "Vinteum", 1, "SCATTERED", "dusts/vinteum", "ores/vinteum"),
    BLUETOPAZ("blue_topaz", "Blue Topaz", 1, "SCATTERED", "gems/blue_topaz", "ores/blue_topaz"),
    MOONSTONE("moonstone", "Moonstone", 1, "RARE", "gems/moonstone", "ores/moonstone"),
    SUNSTONE("sunstone", "Sunstone", 1, "RARE", "gems/sunstone", "ores/sunstone"),
    TITANIUM("titanium", "Titanium", 1, "SCARCE", "ingots/titanium", "ores/titanium"),
    MAGMANITE("magmanite", "Magmanite", 1, "SCATTERED", "drops/magma", "ores/magmanite"),
    MAGNETITE("magnetite", "Magnetite", 1, "AVERAGE", "gems/magnetite", "ores/magnetite"),
    ESSENCE("essence", "Essence", 4, "AVERAGE", "items/essence", "ores/essence", "ores/nether_essence", "ores/end_essence"),
    MIMICHITE("mimichite", "Mimichite", 1, "SCATTERED", "gems/mimichite", "ores/mimichite"),
    NETHERURANIUM("nether_uranium", "Nether Uranium", 1, "SCARCE", "ingots/uranium", "ores/nether_uranium"),
    QUANTUM("quantum", "Quantum", 2, "SCATTERED", "dusts/quantum", "ores/quantum"),
    NETHERIRIDIUM("nether_iridium", "Nether Iridium", 1, "RARE", "ingots/iridium", "ores/nether_iridium"),
    DARKIRON("fz_dark_iron", "Dark Iron", 1, "AVERAGE", "ingots/fz_dark_iron", "ores/fz_dark_iron", "ores/dark_iron"),
    CHROMITE("chromite", "Chromite", 1, "AVERAGE", "ingots/chrome", "ores/chromite"),
    NETHERSAPPHIRE("nether_sapphire", "Nether Sapphire", 1, "SCARCE", "gems/sapphire", "ores/nether_sapphire"),
    NETHERPERIDOT("nether_green_sapphire", "Nether Peridot", 1, "SCARCE", "gems/peridot", "ores/nether_green_sapphire", "ores/nether_peridot"),
    NETHERTITANIUM("nether_titanium", "Nether Titanium", 1, "SCATTERED", "ingots/titanium", "ores/nether_titanium"),
    NETHERSULFUR("nether_sulfur", "Nether Sulfur", 3, "COMMON", "dusts/sulfur", "ores/nether_sulfur"),
    NETHEROSMIUM("nether_osmium", "Nether Osmium", 1, "AVERAGE", "ingots/osmium", "ores/nether_osmium"),
    SILICON("silicon", "Silicon", 1, "AVERAGE", "items/silicon", "ores/silicon"),
    RUTILE("rutile", "Rutile", 1, "SCATTERED", "ingots/rutile", "ores/rutile"),
    AMETHYST("amethyst", "Amethyst", 1, "RARE", "gems/amethyst", "ores/amethyst"),
    TESLATITE("teslatite", "Teslatite", 3, "COMMON", "dusts/teslatite", "ores/teslatite"),
    MANA("mana", "Mana", 1, "SCARCE", "ingots/mana", "ores/mana"),
    NETHERSALTPETER("nether_saltpeter", "Nether Saltpeter", 4, "SCARCE", "dusts/saltpeter", "ores/nether_saltpeter"),
    THORIUM("thorium", "Thorium", 1, "SCARCE", "dusts/thorium", "ores/thorium", "ores/thorite", "ores/thorianite"),
    DRACONIUM("draconium", "Draconium", 2, "SCATTERED", "dusts/draconium", "ores/draconium"),
    ENDIUM("endium", "Endium", 1, "SCATTERED", "ingots/endium", "ores/endium", "ores/hee_endium"),
    DILITHIUM("dilithium", "Dilithium", 1, "SCATTERED", "dusts/dilithium", "ores/dilithium"),
    EXIMITE("eximite", "Eximite", 1, "SCARCE", "ingots/eximite", "ores/eximite"),
    MEUTOITE("meutoite", "Meutoite", 1, "SCARCE", "ingots/meutoite", "ores/meutoite");

    public final String path, displayName, rarity, productTag;
    public final int dropCount;
    private final List<TagKey<Item>> oreTags;
    private final List<DeferredItem<Item>> stages = new ArrayList<>();
    private DeferredItem<Item> product;

    ModExtractOres(String path, String display, int count, String rarity, String productTag, String... tags) {
        this.path = path; displayName = display; dropCount = count;
        this.rarity = rarity; this.productTag = productTag;
        oreTags = Arrays.stream(tags).map(ModExtractOres::common).toList();
    }

    public static TagKey<Item> common(String path) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", path));
    }
    public TagKey<Item> inputTag() {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("rotarycraft", "extractor/ores/" + path));
    }
    public List<TagKey<Item>> oreTags() { return oreTags; }
    public boolean isNether() { return name().startsWith("NETHER"); }
    public double duplicationChance() { return rarity.equals("RARE") ? .9 : isNether() ? .8 : .5; }
    public float experience() { return rarity.equals("RARE") ? 1 : rarity.equals("EVERYWHERE") ? .5F : .7F; }
    public Item stage(int stage) {
        return this == COPPER ? ExtractOres.COPPER.getStageItem(stage) : stages.get(stage).get();
    }
    public Item product() {
        return switch (this) {
            case COPPER, NETHERCOPPER -> Items.COPPER_INGOT;
            case NETHERCOAL -> Items.COAL;
            case NETHERIRON -> Items.IRON_INGOT;
            case NETHERGOLD -> Items.GOLD_INGOT;
            case NETHERREDSTONE -> Items.REDSTONE;
            case NETHERLAPIS -> Items.LAPIS_LAZULI;
            case NETHERDIAMOND -> Items.DIAMOND;
            case NETHEREMERALD -> Items.EMERALD;
            case SILVER, NETHERSILVER -> RotaryItems.SILVER_INGOT.get();
            case NETHERTIN -> TIN.product();
            case NETHERLEAD -> LEAD.product();
            case NETHERNICKEL -> NICKEL.product();
            case NETHERNIKOLITE -> NIKOLITE.product();
            case NETHERPLATINUM -> PLATINUM.product();
            case NETHERURANIUM -> URANIUM.product();
            case NETHERIRIDIUM -> IRIDIUM.product();
            case NETHERSULFUR -> SULFUR.product();
            case NETHERTITANIUM -> TITANIUM.product();
            case NETHEROSMIUM -> OSMIUM.product();
            case NETHERSALTPETER -> SALTPETER.product();
            default -> product.get();
        };
    }
    /** Called once at the end of RotaryItems initialization, before its DeferredRegister is attached. */
    static void registerItems() {
        for (ModExtractOres ore : values()) {
            if (ore == COPPER) continue;
            for (String stage : List.of("dust", "slurry", "solution", "flakes"))
                ore.stages.add(RotaryItems.ITEMS.registerSimpleItem("mod_" + ore.path + "_" + stage));
            // Redirected products share the original base family or modern vanilla item.
            if (!ore.isNether() && ore != SILVER || List.of(NETHERPIGIRON, NETHERSAPPHIRE, NETHERPERIDOT).contains(ore))
                ore.product = RotaryItems.ITEMS.registerSimpleItem("mod_" + ore.path + "_product");
        }
    }
    public static ModExtractOres forStage(Item item) {
        for (ModExtractOres ore : values())
            if (ore != COPPER)
                for (int stage = 0; stage < 4; stage++) if (ore.stage(stage) == item) return ore;
        return null;
    }
    public static String itemName(Item item) {
        for (ModExtractOres ore : values()) {
            if (ore == COPPER) continue;
            for (int stage = 0; stage < 4; stage++) {
                if (ore.stage(stage) == item)
                    return stage == 0 ? "Powdered " + ore.displayName : ore.displayName + " " + List.of("", "Slurry", "Solution", "Flakes").get(stage);
            }
            if (ore.product != null && ore.product.get() == item)
                return ore.displayName + " " + switch (ore.productTag.split("/")[0]) {
                    case "ingots" -> "Ingot"; case "dusts" -> "Dust"; case "gems" -> "Gem";
                    case "shards" -> "Shard"; default -> "Item";
                };
        }
        return null;
    }
}
