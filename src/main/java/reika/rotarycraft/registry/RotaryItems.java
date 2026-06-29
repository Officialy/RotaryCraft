package reika.rotarycraft.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.ItemBasic;
import reika.rotarycraft.items.*;
import reika.rotarycraft.items.tools.*;
import reika.rotarycraft.items.tools.bedrock.*;
import reika.rotarycraft.items.tools.charged.*;
import reika.rotarycraft.items.tools.steel.*;

import java.util.function.Supplier;

public class RotaryItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RotaryCraft.MODID);

    // 1.21.5: Item.Properties requires setId() before Item.<init> (it dereferences props.id via
    // effectiveDescriptionId()). We stash the ResourceKey in a ThreadLocal while each entry's
    // factory runs, and replace `itemProperties()` with `itemProperties()` which reads it.
    private static final ThreadLocal<ResourceKey<Item>> CURRENT_ITEM_KEY = new ThreadLocal<>();

    public static Item.Properties itemProperties() {
        Item.Properties p = new Item.Properties();
        ResourceKey<Item> k = CURRENT_ITEM_KEY.get();
        if (k != null) p.setId(k);
        return p;
    }

    private static <I extends Item> DeferredItem<I> reg(String name, Supplier<I> factory) {
        return ITEMS.register(name, rl -> {
            CURRENT_ITEM_KEY.set(ResourceKey.create(Registries.ITEM, rl));
            try {
                return factory.get();
            } finally {
                CURRENT_ITEM_KEY.remove();
            }
        });
    }

    public static final DeferredItem<Item> IRON_SCRAP = reg("iron_scrap", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> ALUMINUM_ALLOY_INGOT = reg("aluminum_alloy_ingot", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> ALUMINUM_ALLOY_CYLINDER = reg("aluminum_alloy_cylinder", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> WOOD_FLYWHEEL_CORE = reg("wood_flywheel_core", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> STONE_FLYWHEEL_CORE = reg("stone_flywheel_core", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> IRON_FLYWHEEL_CORE = reg("iron_flywheel_core", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> GOLD_FLYWHEEL_CORE = reg("gold_flywheel_core", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> TUNGSTEN_ALLOY_FLYWHEEL_CORE = reg("tungsten_alloy_flywheel_core", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> BEDROCK_ALLOY_FLYWHEEL_CORE = reg("bedrock_alloy_flywheel_core", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> NETHERRACK_DUST = reg("netherrack_dust", () -> new Item(itemProperties()));

    public static final DeferredItem<BucketItem> MOLTEN_HSLA_BUCKET = reg("molten_hsla_bucket", () -> new BucketItem(RotaryFluids.HSLA_FLUID.get(), itemProperties()));

    //Components
    public static final DeferredItem<Item> MOUNT = reg("mount", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> HUB = reg("hub", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> IGNITION_UNIT = reg("ignition_unit", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> IMPELLER = reg("impeller", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> INDUCTIVE_INGOT = reg("inductive_ingot", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> LENS = reg("lens", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> LINEAR_INDUCTION_MOTOR = reg("linear_induction_motor", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> MIRROR = reg("mirror_panel", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> MIXER = reg("mixer", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> PADDLE_PANEL = reg("paddle_panel", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> POWER_MODULE = reg("power_module", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> PRESSURE_HEAD = reg("pressure_head", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> PROPELLER_BLADE = reg("propeller_blade", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> RADAR_UNIT = reg("radar_unit", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> RADIATOR = reg("radiator", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> RAILGUN_ACCELERATOR = reg("railgun_accelerator", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> SCREEN = reg("screen", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> SONAR_UNIT = reg("sonar_unit", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> TURBINE = reg("turbine", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> TURRET_AIMING_UNIT = reg("turret_aiming_unit", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> TURRET_BASE = reg("turret_base", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> TUNGSTEN_ALLOY_INGOT = reg("tungsten_alloy_ingot", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> TUNGSTEN_INGOT = reg("tungsten_ingot", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> WORM_GEAR = reg("worm_gear", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> HSLA_STEEL_SPRING = reg("hsla_steel_spring", ItemCoil::new);
    public static final DeferredItem<Item> TUNGSTEN_ALLOY_SPRING = reg("tungsten_alloy_spring", ItemCoil::new);
    public static final DeferredItem<Item> BEDROCK_ALLOY_SPRING = reg("bedrock_alloy_spring", ItemCoil::new);
    public static final DeferredItem<Item> HEAT_RAY_BARREL = reg("heat_ray_barrel", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> HEAT_RAY_CORE = reg("heat_ray_core", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> BELT = reg("belt", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> BRAKE_DISC = reg("brake_disc", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> TENSION_COIL = reg("tension_coil", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> CHAIN_LINK = reg("chain_link", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> CIRCUIT_BOARD = reg("circuit_board", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> COMBUSTOR = reg("combustor", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> COMPOUND_COMPRESSOR = reg("compound_compressor", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> COMPRESSOR = reg("compressor", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> COMPOUND_TURBINE = reg("compound_turbine", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> CONDENSER = reg("condenser", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> CYLINDER = reg("cylinder", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> DIFFUSER = reg("diffuser", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> DRILLHEAD_IRON = reg("drillhead_iron", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> GENERATOR = reg("generator", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> GOLD_COIL = reg("gold_coil", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> HIGH_TEMPERATURE_COMBUSTOR = reg("high_temperature_combustor", () -> new Item(itemProperties()));


    public static final DeferredItem<Item> SPRING_STEEL_INGOT = reg("spring_steel_ingot", () -> new Item(itemProperties()));

    public static final DeferredItem<Item> NVG = reg("nvg", ItemNightVisionGoggles::new);
    //public static final DeferredItem<Item> NVH = reg("nvh", ItemNightVisionHelmet);
    public static final DeferredItem<Item> RAILGUN_AMMO = reg("railgun_ammo", ItemRailGunAmmo::new);
    public static final DeferredItem<Item> TARGET = reg("target", ItemTarget::new);
    public static final DeferredItem<Item> IO_GOGGLES = reg("io_goggles", ItemIOGoggles::new);
    public static final DeferredItem<Item> ANGULAR_TRANSDUCER = reg("angular_transducer", ItemMeter::new);

    public static final DeferredItem<Item> SLIDE = reg("slide", ItemSlide::new);
    //public static final DeferredItem<Item> KEY = reg("key", ItemCannonKey::new);
    public static final DeferredItem<Item> SHELL = reg("shell", ItemExplosiveShell::new);
    public static final DeferredItem<Item> ETHANOL_CART = reg("ethanol_cart", ItemEthanolMinecart::new);
    public static final DeferredItem<Item> JETPACK = reg("ethanol_jetpack", () -> new ItemJetPack(Materials.HSLA_STEEL));
    public static final DeferredItem<Item> JUMP = reg("jump_boots", () -> new ItemSpringBoots(Materials.HSLA_STEEL, itemProperties()));
    public static final DeferredItem<Item> BEDROCK_ALLOY_JUMP_BOOTS = reg("bedrock_alloy_jump_boots", () -> new ItemSpringBoots(Materials.BEDROCK_ALLOY, itemProperties()));
    //public static final DeferredItem<Item> FUEL = reg("fueltank", ItemFuelTank::new);
    public static final DeferredItem<Item> DISK = reg("music_box_disc", ItemDisk::new);
    public static final DeferredItem<Item> UPGRADE = reg("engine_upgrade", ItemEngineUpgrade::new);

    //Other Tools
    public static final DeferredItem<Item> RANGE_FINDER = reg("range_finder", ItemRangeFinder::new);
    public static final DeferredItem<Item> SPRING_PISTON = reg("spring_piston", ItemHandheldPiston::new);
    public static final DeferredItem<Item> FLAMETHROWER = reg("flamethrower", ItemFlamethrower::new);
    public static final DeferredItem<Item> TILE_SELECTOR = reg("tile_selector", ItemTileSelector::new);
    public static final DeferredItem<Item> HANDHELD_CRAFTING_TABLE = reg("handheld_crafting_table", ItemHandheldCrafting::new);
    public static final DeferredItem<Item> ULTRASOUND = reg("ultrasound", ItemUltrasound::new);
    public static final DeferredItem<Item> SCREWDRIVER = reg("screwdriver", ItemScrewdriver::new);
    public static final DeferredItem<Item> HANDBOOK = reg("handbook", ItemHandBook::new);
    public static final DeferredItem<Item> SAW = reg("saw", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> DRY_ICE = reg("dry_ice", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> SILICON = reg("silicon", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> SILICON_DUST = reg("silicon_dust", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> INDUCTIVE_BLEND = reg("inductive_blend", () -> new Item(itemProperties()));
//    public static final DeferredItem<Item> ENDERIUM = reg("enderium", () -> new Item(itemProperties()));

    //public static final DeferredItem<Item> MOTION = reg("motion", ItemMotionTracker::new);
    //public static final DeferredItem<Item> VACUUM = reg("vacuum", ItemVacuum::new);
    //public static final DeferredItem<Item> STUNGUN = reg("stun_gun", ItemStunGun::new);
    //public static final DeferredItem<Item> GRAVELGUN = reg("gravel_gun", ItemGravelGun::new);
    //public static final DeferredItem<Item> FIREBALL = reg("fire_launcher", ItemFireballLauncher::new);
    //public static final DeferredItem<Item> CHARGED_GRAFTER = reg("chargedgrafter", ItemChargedGrafter::new, ModList.FORESTRY);
    public static final DeferredItem<Item> CRAFT_PATTERN = reg("craft_pattern", () -> new ItemCraftPattern(itemProperties().stacksTo(16)));
    //public static final DeferredItem<Item> METER = reg("meter", ItemMeter::new);
    public static final DeferredItem<Item> DEBUG = reg("debug", ItemDebug::new);
    //public static final DeferredItem<Item> PUMP = reg("hand_pump", ItemPump::new);

    public static final DeferredItem<Item> INTEGRATED_GEARBOX = reg("integrated_gearbox", () -> new ItemIntegratedGearbox(itemProperties().stacksTo(16)));
    //public static final DeferredItem<Item> BEDKNIFE = reg("bedknife", ItemBedrockKnife(), ModList.APPENG);
    // public static final DeferredItem<Item> MATCHFILTER = reg("match_filter", () -> new ItemMatchFilter(itemProperties()));
    // public static final DeferredItem<Item> BEDDRILL = reg("beddrill", ItemBedrockDrillHead(), ModList.IMMERSIVEENG);
    //public static final DeferredItem<Item> BEDCHISEL = reg("bedchisel", ItemBedrockChisel(), ModList.CHISEL);
    public static final DeferredItem<Item> SLUDGE = reg("sludge", () -> new Item(itemProperties()));

    public static final DeferredItem<Item> SAWDUST = reg("sawdust", () -> new Item(itemProperties()));
    //public static final DeferredItem<Item> DECOTANK = reg("deco_tank", () -> new BlockItemDecoTank(RotaryBlocks.DECOTANK.get(), itemProperties()));

    //HSLA Steel Items
    public static final DeferredItem<Item> HSLA_STEEL_GEAR = reg("hsla_steel_gear", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> HSLA_STEEL_GEAR_2x = reg("hsla_steel_gear_2x", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> HSLA_STEEL_GEAR_4x = reg("hsla_steel_gear_4x", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> HSLA_STEEL_GEAR_8x = reg("hsla_steel_gear_8x", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> HSLA_STEEL_GEAR_16x = reg("hsla_steel_gear_16x", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> HSLA_SHAFT = reg("hsla_steel_rod", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> HSLA_SHAFT_CORE = reg("hsla_steel_shaft_core", () -> new Item(itemProperties()));

    public static final DeferredItem<Item> HSLA_STEEL_PICKAXE = reg("hsla_steel_pickaxe", ItemSteelPick::new);
    public static final DeferredItem<Item> HSLA_STEEL_SHOVEL = reg("hsla_steel_shovel", ItemSteelShovel::new);
    public static final DeferredItem<Item> HSLA_STEEL_AXE = reg("hsla_steel_axe", ItemSteelAxe::new);
    public static final DeferredItem<Item> HSLA_STEEL_SWORD = reg("hsla_steel_sword", ItemSteelSword::new);
    public static final DeferredItem<Item> HSLA_STEEL_HOE = reg("hsla_steel_hoe", ItemSteelHoe::new);
    public static final DeferredItem<Item> HSLA_STEEL_SHEARS = reg("hsla_steel_shears", ItemSteelShears::new);
//    public static final DeferredItem<Item> HSLA_STEEL_SHIELD = reg("hsla_steel_shield", () -> new ShieldItem(itemProperties()));

    public static final DeferredItem<Item> HSLA_STEEL_INGOT = reg("hsla_steel_ingot", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> HSLA_STEEL_NUGGET = reg("hsla_steel_nugget", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> HSLA_HELMET = reg("hsla_steel_helmet", () -> new ItemSteelArmor(ArmorType.HELMET, itemProperties()));
    public static final DeferredItem<Item> HSLA_CHESTPLATE = reg("hsla_steel_chestplate", () -> new ItemSteelArmor(ArmorType.CHESTPLATE, itemProperties()));
    public static final DeferredItem<Item> HSLA_LEGGINGS = reg("hsla_steel_leggings", () -> new ItemSteelArmor(ArmorType.LEGGINGS, itemProperties()));
    public static final DeferredItem<Item> HSLA_BOOTS = reg("hsla_steel_boots", () -> new ItemSteelArmor(ArmorType.BOOTS, itemProperties()));
    public static final DeferredItem<Item> HSLA_STEEL_SCRAP = reg("hsla_steel_scrap", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> HSLA_PLATE = reg("hsla_steel_plate", () -> new Item(itemProperties())); //this is the "base plate"
    public static final DeferredItem<Item> HSLA_DRILL = reg("hsla_steel_drill", () -> new Item(itemProperties()));
    //public static final DeferredItem<Item> HSLA_STEEL_SICKLE = reg("steelsickle", ItemSteelSickle);
    public static final DeferredItem<Item> HSLA_STEEL_PACK = reg("hsla_jet_chest", () -> new ItemJetPack(Materials.HSLA_STEEL));

    //Tungsten Alloy Items
    public static final DeferredItem<Item> TUNGSTEN_ALLOY_GEAR = reg("tungsten_alloy_gear", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> TUNGSTEN_ALLOY_GEAR_2x = reg("tungsten_alloy_gear_2x", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> TUNGSTEN_ALLOY_GEAR_4x = reg("tungsten_alloy_gear_4x", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> TUNGSTEN_ALLOY_GEAR_8x = reg("tungsten_alloy_gear_8x", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> TUNGSTEN_ALLOY_GEAR_16x = reg("tungsten_alloy_gear_16x", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> TUNGSTEN_ALLOY_SHAFT = reg("tungsten_alloy_rod", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> TUNGSTEN_ALLOY_SHAFT_CORE = reg("tungsten_alloy_shaft_core", () -> new Item(itemProperties()));

    //Diamond Items
    public static final DeferredItem<Item> DIAMOND_GEAR = reg("diamond_gear", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> DIAMOND_GEAR_2x = reg("diamond_gear_2x", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> DIAMOND_GEAR_4x = reg("diamond_gear_4x", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> DIAMOND_GEAR_8x = reg("diamond_gear_8x", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> DIAMOND_GEAR_16x = reg("diamond_gear_16x", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> DIAMOND_SHAFT = reg("diamond_rod", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> DIAMOND_SHAFT_CORE = reg("diamond_shaft_core", () -> new Item(itemProperties()));

    //Bedrock Items
    public static final DeferredItem<Item> BEDROCK_ALLOY_GEAR = reg("bedrock_alloy_gear", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> BEDROCK_ALLOY_GEAR_2x = reg("bedrock_alloy_gear_2x", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> BEDROCK_ALLOY_GEAR_4x = reg("bedrock_alloy_gear_4x", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> BEDROCK_ALLOY_GEAR_8x = reg("bedrock_alloy_gear_8x", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> BEDROCK_ALLOY_GEAR_16x = reg("bedrock_alloy_gear_16x", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> BEDROCK_DUST = reg("bedrock_dust", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> BEDROCK_ALLOY_SHAFT = reg("bedrock_alloy_rod", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> BEDROCK_ALLOY_SHAFT_CORE = reg("bedrock_alloy_shaft_core", () -> new Item(itemProperties()));

    // Gear-crafting parts: ball bearings, the per-material bearings (used by the advanced gears
    // and gearboxes), and the stone gear. The textures already shipped in resources; the items
    // were simply never registered, which left e.g. the CVT/256x-gear recipes uncraftable.
    public static final DeferredItem<Item> BALL_BEARING = reg("ball_bearing", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> STONE_GEAR = reg("stone_gear", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> STONE_BEARING = reg("stone_bearing", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> HSLA_STEEL_BEARING = reg("hsla_steel_bearing", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> TUNGSTEN_ALLOY_BEARING = reg("tungsten_alloy_bearing", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> DIAMOND_BEARING = reg("diamond_bearing", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> BEDROCK_ALLOY_BEARING = reg("bedrock_alloy_bearing", () -> new Item(itemProperties()));
//    public static final DeferredItem<Item> BEDROCK_ALLOY_SICKLE = reg("bedsickle", ItemBedrockSickle::new);
    //public static final DeferredItem<Item> BEDROCK_ALLOY_GRAFTER = reg("bedgrafter", ItemBedrockGrafter::new, ModList.FORESTRY);
    //public static final DeferredItem<Item> BEDROCK_ALLOY_SAW = reg("bedsaw", ItemBedrockSaw::new, ModList.MULTIPART);
    public static final DeferredItem<Item> BEDROCK_ALLOY_PACK = reg("bedrock_alloy_jet_chest", () -> new ItemJetPack(Materials.BEDROCK_ALLOY));

    public static final DeferredItem<Item> BEDROCK_ALLOY_PICK = reg("bedrock_alloy_pickaxe", ItemBedrockPickaxe::new);
    public static final DeferredItem<Item> BEDROCK_ALLOY_AXE = reg("bedrock_alloy_axe", ItemBedrockAxe::new);
    public static final DeferredItem<Item> BEDROCK_ALLOY_SWORD = reg("bedrock_alloy_sword", ItemBedrockSword::new);
    public static final DeferredItem<Item> BEDROCK_ALLOY_HOE = reg("bedrock_alloy_hoe", ItemBedrockHoe::new);
    public static final DeferredItem<Item> BEDROCK_ALLOY_SHEARS = reg("bedrock_alloy_shears", ItemBedrockShears::new);
    public static final DeferredItem<Item> BEDROCK_ALLOY_SHOVEL = reg("bedrock_alloy_shovel", ItemBedrockShovel::new);
//    public static final DeferredItem<Item> BEDROCK_ALLOY_SHIELD = reg("bedrock_alloy_shield", () -> new ShieldItem(itemProperties()));

    public static final DeferredItem<Item> BEDROCK_ALLOY_INGOT = reg("bedrock_alloy_ingot", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> BEDROCK_ALLOY_HELMET = reg("bedrock_alloy_helmet", () -> new ItemBedrockArmor(ArmorType.HELMET, itemProperties()));
    public static final DeferredItem<Item> BEDROCK_ALLOY_CHESTPLATE = reg("bedrock_alloy_chestplate", () -> new ItemBedrockArmor(ArmorType.CHESTPLATE, itemProperties()));
    public static final DeferredItem<Item> BEDROCK_ALLOY_LEGGINGS = reg("bedrock_alloy_leggings", () -> new ItemBedrockArmor(ArmorType.LEGGINGS, itemProperties()));
    public static final DeferredItem<Item> BEDROCK_ALLOY_BOOTS = reg("bedrock_alloy_boots", () -> new ItemBedrockArmor(ArmorType.BOOTS, itemProperties()));
    public static final DeferredItem<Item> BEDROCK_DRILL = reg("bedrock_alloy_drill", () -> new Item(itemProperties()));

    //Flakes
    public static final DeferredItem<Item> TUNGSTEN_FLAKES = reg("tungsten_flakes", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> DIAMOND_FLAKES = reg("diamond_flakes", () -> new Item(itemProperties()));

    //Extractor ore-processing chain (dust -> slurry -> solution -> flakes per ore; see ExtractOres)
    public static final DeferredItem<Item> IRON_DUST = reg("iron_dust", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> GOLD_DUST = reg("gold_dust", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> REDSTONE_DUST = reg("redstone_dust", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> LAPIS_DUST = reg("lapis_dust", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> DIAMOND_DUST = reg("diamond_dust", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> EMERALD_DUST = reg("emerald_dust", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> QUARTZ_DUST = reg("quartz_dust", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> COPPER_DUST = reg("copper_dust", () -> new Item(itemProperties()));

    public static final DeferredItem<Item> COAL_SLURRY = reg("coal_slurry", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> IRON_SLURRY = reg("iron_slurry", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> GOLD_SLURRY = reg("gold_slurry", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> REDSTONE_SLURRY = reg("redstone_slurry", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> LAPIS_SLURRY = reg("lapis_slurry", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> DIAMOND_SLURRY = reg("diamond_slurry", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> EMERALD_SLURRY = reg("emerald_slurry", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> QUARTZ_SLURRY = reg("quartz_slurry", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> COPPER_SLURRY = reg("copper_slurry", () -> new Item(itemProperties()));

    public static final DeferredItem<Item> COAL_SOLUTION = reg("coal_solution", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> IRON_SOLUTION = reg("iron_solution", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> GOLD_SOLUTION = reg("gold_solution", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> REDSTONE_SOLUTION = reg("redstone_solution", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> LAPIS_SOLUTION = reg("lapis_solution", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> DIAMOND_SOLUTION = reg("diamond_solution", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> EMERALD_SOLUTION = reg("emerald_solution", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> QUARTZ_SOLUTION = reg("quartz_solution", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> COPPER_SOLUTION = reg("copper_solution", () -> new Item(itemProperties()));

    public static final DeferredItem<Item> COAL_FLAKES = reg("coal_flakes", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> IRON_FLAKES = reg("iron_flakes", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> GOLD_FLAKES = reg("gold_flakes", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> REDSTONE_FLAKES = reg("redstone_flakes", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> LAPIS_FLAKES = reg("lapis_flakes", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> EMERALD_FLAKES = reg("emerald_flakes", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> QUARTZ_FLAKES = reg("quartz_flakes", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> COPPER_FLAKES = reg("copper_flakes", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> SILVER_FLAKES = reg("silver_flakes", () -> new Item(itemProperties()));

    public static final DeferredItem<Item> SILVER_INGOT = reg("silver_ingot", () -> new Item(itemProperties()));

    public static final DeferredItem<Item> COMPOST = reg("compost", () -> new Item(itemProperties()));

    //Fluid Buckets
    public static final DeferredItem<Item> LUBE_BUCKET = reg("lubricant_bucket", () -> new BucketItem(RotaryFluids.LUBRICANT.get(), itemProperties()));
    public static final DeferredItem<Item> JET_FUEL_BUCKET = reg("jet_fuel_bucket", () -> new BucketItem(RotaryFluids.JET_FUEL.get(), itemProperties()));
    public static final DeferredItem<Item> ETHANOL_BUCKET = reg("liquid_ethanol_bucket", () -> new BucketItem(RotaryFluids.ETHANOL.get(), itemProperties()));
    public static final DeferredItem<Item> NITROGEN_BUCKET = reg("liquid_nitrogen_bucket", () -> new BucketItem(RotaryFluids.LIQUID_NITROGEN.get(), itemProperties()));

    //Other Items
    public static final DeferredItem<Item> YEAST = reg("yeast", () -> new ItemBasic(itemProperties(), 64));
    public static final DeferredItem<Item> ETHANOL = reg("ethanol_crystals", () -> new ItemBasic(itemProperties(), 64));
    // 26.1: plain BlockItem is correct for crop seeds (ItemNameBlockItem was removed in
    // 1.21+; vanilla wheat seeds use BlockItem + useItemDescriptionPrefix). The crop block's
    // canSurvive override handles "must be on farmland".
    public static final DeferredItem<Item> CANOLA_SEEDS = reg("canola_seeds", () -> new BlockItem(RotaryBlocks.CANOLA.get(), itemProperties().useItemDescriptionPrefix()));
    // GuiMusic icon-button list referenced Blocks.GRASS (removed in 1.20+); use GRASS_BLOCK instead
    public static final DeferredItem<Item> CANOLA_HUSKS = reg("canola_seed_husks", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> DENSE_CANOLA_SEEDS = reg("dense_canola_seeds", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> TAR = reg("tar", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> ALUMINUM_ALLOY_POWDER = reg("aluminum_alloy_powder", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> COAL_DUST = reg("coal_dust", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> FLOUR = reg("flour", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> CALCULATOR = reg("calculator", () -> new ItemCalculator(itemProperties()));
    public static final DeferredItem<Item> COKE = reg("coke", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> NITRATE = reg("nitrate", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> SALT = reg("salt", () -> new Item(itemProperties()));
    public static final DeferredItem<Item> SILVERIODIDE = reg("silveriodide", () -> new Item(itemProperties()));




//    public boolean isJetpack() {
//        if (this == JETPACK || this == BEDROCK_JETPACK || this == HSLA_JETPACK)
//            return true;
//        return false;
//    }

    public static int getArmorType(Item i) {
        if (i == BEDROCK_ALLOY_BOOTS.get()) {
            return 3;
        }
        if (i == HSLA_BOOTS.get()) {
            return 3;
        }
        if (i == JUMP.get()) {
            return 3;
        }
        if (i == BEDROCK_ALLOY_JUMP_BOOTS.get()) {
            return 3;
        }
        if (i == BEDROCK_ALLOY_LEGGINGS.get()) {
            return 2;
        }
        if (i == HSLA_LEGGINGS.get()) {
            return 2;
        }

        if (i == BEDROCK_ALLOY_CHESTPLATE.get()) {
            return 1;
        }
        if (i == HSLA_CHESTPLATE.get()) {
            return 1;

        }
//        if (i == JETPACK.get()) {
//            return 1;
//        } if (i == BEDROCK_JETPACK.get()) {
//            return 1;
//        } if (i == HSLA_JETPACK.get()) {
//            return 1;
//        }
        if (i == BEDROCK_ALLOY_HELMET.get()) {
            return 0;
        }
        if (i == HSLA_HELMET.get()) {
            return 0;
        }
//        if (i == BEDREVEAL.get()) {
//            return 0;
//        }
        return 0;
    }
    public boolean overridesRightClick(ItemStack is) {
        if (is == SCREWDRIVER.get().getDefaultInstance()){
            return true;
        }
        return is == UPGRADE.get().getDefaultInstance();
        /*if (is == DEBUG.get().getDefaultInstance()){
            return true;
        }
        if (is == METER.get().getDefaultInstance()){
            return true;
        }
        if (is == KEY.get().getDefaultInstance()){
            return true;
        }
        if (is == TILESELECTOR.get().getDefaultInstance()){
            return true;
        }
        if (is == GEARUPGRADE.get().getDefaultInstance()){
            return true;
        }
        if (is == PUMP.get().getDefaultInstance()){
                return is.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag() != null;
        }*/
    }
}