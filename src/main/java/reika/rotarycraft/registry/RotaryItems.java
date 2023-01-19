package reika.rotarycraft.registry;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.ItemBasic;
import reika.rotarycraft.items.*;
import reika.rotarycraft.items.tools.*;
import reika.rotarycraft.items.tools.bedrock.*;
import reika.rotarycraft.items.tools.charged.*;
import reika.rotarycraft.items.tools.steel.*;

public class RotaryItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RotaryCraft.MODID);

    public static final RegistryObject<Item> IRON_SCRAP = ITEMS.register("iron_scrap", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CUSTOMEXTRACT = ITEMS.register("customextract", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ALUMINUM_ALLOY_INGOT = ITEMS.register("aluminum_alloy_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ALUMINUM_ALLOY_CYLINDER = ITEMS.register("aluminum_alloy_cylinder", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WOOD_FLYWHEEL_CORE = ITEMS.register("wood_flywheel_core", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> STONE_FLYWHEEL_CORE = ITEMS.register("stone_flywheel_core", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> IRON_FLYWHEEL_CORE = ITEMS.register("iron_flywheel_core", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GOLD_FLYWHEEL_CORE = ITEMS.register("gold_flywheel_core", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TUNGSTEN_ALLOY_FLYWHEEL_CORE = ITEMS.register("tungsten_alloy_flywheel_core", () -> new Item(new Item.Properties()));
//    public static final RegistryObject<Item> BEDROCK_ALLOY_FLYWHEEL_CORE = ITEMS.register("bedrock_alloy_flywheel_core", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> NETHERRACK_DUST = ITEMS.register("netherrack_dust", () -> new Item(new Item.Properties()));

    public static final RegistryObject<BucketItem> MOLTEN_HSLA_BUCKET = ITEMS.register("molten_hsla_bucket", () -> new BucketItem(RotaryFluids.HSLA_FLUID, new Item.Properties()));

    //Components
    public static final RegistryObject<Item> MOUNT = ITEMS.register("mount", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HUB = ITEMS.register("hub", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> IGNITION_UNIT = ITEMS.register("ignition_unit", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> IMPELLER = ITEMS.register("impeller", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> INDUCTIVE_INGOT = ITEMS.register("inductive_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LENS = ITEMS.register("lens", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LINEAR_INDUCTION_MOTOR = ITEMS.register("linear_induction_motor", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MIRROR = ITEMS.register("mirror_panel", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MIXER = ITEMS.register("mixer", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PADDLE_PANEL = ITEMS.register("paddle_panel", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> POWER_MODULE = ITEMS.register("power_module", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PRESSURE_HEAD = ITEMS.register("pressure_head", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PROPELLER_BLADE = ITEMS.register("propeller_blade", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RADAR_UNIT = ITEMS.register("radar_unit", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RADIATOR = ITEMS.register("radiator", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAILGUN_ACCELERATOR = ITEMS.register("railgun_accelerator", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SCREEN = ITEMS.register("screen", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SONAR_UNIT = ITEMS.register("sonar_unit", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TURBINE = ITEMS.register("turbine", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TURRET_AIMING_UNIT = ITEMS.register("turret_aiming_unit", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TURRET_BASE = ITEMS.register("turret_base", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TUNGSTEN_ALLOY_INGOT = ITEMS.register("tungsten_alloy_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TUNGSTEN_INGOT = ITEMS.register("tungsten_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WORM_GEAR = ITEMS.register("worm_gear", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HSLA_STEEL_SPRING = ITEMS.register("hsla_steel_spring", ItemCoil::new);
    public static final RegistryObject<Item> TUNGSTEN_ALLOY_SPRING = ITEMS.register("tungsten_alloy_spring", ItemCoil::new);
    public static final RegistryObject<Item> BEDROCK_ALLOY_SPRING = ITEMS.register("bedrock_alloy_spring", ItemCoil::new);
    public static final RegistryObject<Item> HEAT_RAY_BARREL = ITEMS.register("heat_ray_barrel", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HEAT_RAY_CORE = ITEMS.register("heat_ray_core", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BELT = ITEMS.register("belt", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BRAKE_DISC = ITEMS.register("brake_disc", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHAIN_LINK = ITEMS.register("chain_link", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CIRCUIT_BOARD = ITEMS.register("circuit_board", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COMBUSTOR = ITEMS.register("combustor", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COMPOUND_COMPRESSOR = ITEMS.register("compound_compressor", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COMPRESSOR = ITEMS.register("compressor", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COMPOUND_TURBINE = ITEMS.register("compound_turbine", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CONDENSER = ITEMS.register("condenser", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CYLINDER = ITEMS.register("cylinder", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DIFFUSER = ITEMS.register("diffuser", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DRILLHEAD_IRON = ITEMS.register("drillhead_iron", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GENERATOR = ITEMS.register("generator", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GOLD_COIL = ITEMS.register("gold_coil", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HIGH_TEMPERATURE_COMBUSTOR = ITEMS.register("high_temperature_combustor", () -> new Item(new Item.Properties()));


    public static final RegistryObject<Item> SPRING_STEEL_INGOT = ITEMS.register("spring_steel_ingot", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> NVG = ITEMS.register("nvg", ItemNightVisionGoggles::new);
    //public static final RegistryObject<Item> NVH = ITEMS.register("nvh", ItemNightVisionHelmet);
    public static final RegistryObject<Item> RAILGUN_AMMO = ITEMS.register("railgun_ammo", ItemRailGunAmmo::new);
    public static final RegistryObject<Item> TARGET = ITEMS.register("target", ItemTarget::new);
    public static final RegistryObject<Item> IO_GOGGLES = ITEMS.register("io_goggles", ItemIOGoggles::new);
    public static final RegistryObject<Item> ANGULAR_TRANSDUCER = ITEMS.register("angular_transducer", ItemMeter::new);

    public static final RegistryObject<Item> SLIDE = ITEMS.register("slide", ItemSlide::new);
    //public static final RegistryObject<Item> KEY = ITEMS.register("key", ItemCannonKey::new);
    public static final RegistryObject<Item> SHELL = ITEMS.register("shell", ItemExplosiveShell::new);
    public static final RegistryObject<Item> ETHANOL_CART = ITEMS.register("ethanol_cart", ItemEthanolMinecart::new);
    public static final RegistryObject<Item> JETPACK = ITEMS.register("ethanol_jetpack", () -> new ItemJetPack(Materials.HSLA_STEEL));
    public static final RegistryObject<Item> JUMP = ITEMS.register("jump_boots", () -> new ItemSpringBoots(Materials.HSLA_STEEL, new Item.Properties()));
    public static final RegistryObject<Item> BEDROCK_ALLOY_JUMP_BOOTS = ITEMS.register("bedrock_alloy_jump_boots", () -> new ItemSpringBoots(Materials.BEDROCK_ALLOY, new Item.Properties()));
    //public static final RegistryObject<Item> FUEL = ITEMS.register("fueltank", ItemFuelTank::new);
    public static final RegistryObject<Item> DISK = ITEMS.register("music_box_disc", ItemDisk::new);
    public static final RegistryObject<Item> UPGRADE = ITEMS.register("engine_upgrade", ItemEngineUpgrade::new);

    //Other Tools
    public static final RegistryObject<Item> RANGE_FINDER = ITEMS.register("range_finder", ItemRangeFinder::new);
    public static final RegistryObject<Item> SPRING_PISTON = ITEMS.register("spring_piston", ItemHandheldPiston::new);
    public static final RegistryObject<Item> FLAMETHROWER = ITEMS.register("flamethrower", ItemFlamethrower::new);
    public static final RegistryObject<Item> TILE_SELECTOR = ITEMS.register("tile_selector", ItemTileSelector::new);
    public static final RegistryObject<Item> HANDHELD_CRAFTING_TABLE = ITEMS.register("handheld_crafting_table", ItemHandheldCrafting::new);
    public static final RegistryObject<Item> ULTRASOUND = ITEMS.register("ultrasound", ItemUltrasound::new);
    public static final RegistryObject<Item> SCREWDRIVER = ITEMS.register("screwdriver", ItemScrewdriver::new);
    public static final RegistryObject<Item> HANDBOOK = ITEMS.register("handbook", ItemHandBook::new);
    public static final RegistryObject<Item> SAW = ITEMS.register("saw", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DRY_ICE = ITEMS.register("dry_ice", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SILICON = ITEMS.register("silicon", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SILICON_DUST = ITEMS.register("silicon_dust", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> INDUCTIVE_BLEND = ITEMS.register("inductive_blend", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ENDERIUM = ITEMS.register("enderium", () -> new Item(new Item.Properties()));

    //public static final RegistryObject<Item> MOTION = ITEMS.register("motion", ItemMotionTracker::new);
    //public static final RegistryObject<Item> VACUUM = ITEMS.register("vacuum", ItemVacuum::new);
    //public static final RegistryObject<Item> STUNGUN = ITEMS.register("stun_gun", ItemStunGun::new);
    //public static final RegistryObject<Item> GRAVELGUN = ITEMS.register("gravel_gun", ItemGravelGun::new);
    //public static final RegistryObject<Item> FIREBALL = ITEMS.register("fire_launcher", ItemFireballLauncher::new);
    //public static final RegistryObject<Item> CHARGED_GRAFTER = ITEMS.register("chargedgrafter", ItemChargedGrafter::new, ModList.FORESTRY);
//    public static final RegistryObject<Item> CRAFTPATTERN = ITEMS.register("craft_pattern", () -> new ItemCraftPattern(new Item.Properties().stacksTo(16)));
    //public static final RegistryObject<Item> METER = ITEMS.register("meter", ItemMeter::new);
    // public static final RegistryObject<Item> DEBUG = ITEMS.register("debug", ItemDebug::new);
    //public static final RegistryObject<Item> PUMP = ITEMS.register("hand_pump", ItemPump::new);

    public static final RegistryObject<Item> INTEGRATED_GEARBOX = ITEMS.register("integrated_gearbox", () -> new ItemIntegratedGearbox(new Item.Properties().stacksTo(16)));
    //public static final RegistryObject<Item> BEDKNIFE = ITEMS.register("bedknife", ItemBedrockKnife(), ModList.APPENG);
    // public static final RegistryObject<Item> MATCHFILTER = ITEMS.register("match_filter", () -> new ItemMatchFilter(new Item.Properties()));
    // public static final RegistryObject<Item> BEDDRILL = ITEMS.register("beddrill", ItemBedrockDrillHead(), ModList.IMMERSIVEENG);
    //public static final RegistryObject<Item> BEDCHISEL = ITEMS.register("bedchisel", ItemBedrockChisel(), ModList.CHISEL);
    public static final RegistryObject<Item> SLUDGE = ITEMS.register("sludge", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SAWDUST = ITEMS.register("sawdust", () -> new Item(new Item.Properties()));
    //public static final RegistryObject<Item> DECOTANK = ITEMS.register("deco_tank", () -> new BlockItemDecoTank(RotaryBlocks.DECOTANK.get(), new Item.Properties()));

    //HSLA Steel Items
    public static final RegistryObject<Item> HSLA_STEEL_GEAR = ITEMS.register("hsla_steel_gear", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HSLA_STEEL_GEAR_2x = ITEMS.register("hsla_steel_gear_2x", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HSLA_STEEL_GEAR_4x = ITEMS.register("hsla_steel_gear_4x", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HSLA_STEEL_GEAR_8x = ITEMS.register("hsla_steel_gear_8x", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HSLA_STEEL_GEAR_16x = ITEMS.register("hsla_steel_gear_16x", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HSLA_SHAFT = ITEMS.register("hsla_steel_rod", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HSLA_SHAFT_CORE = ITEMS.register("hsla_steel_shaft_core", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> HSLA_STEEL_PICKAXE = ITEMS.register("hsla_steel_pickaxe", ItemSteelPick::new);
    public static final RegistryObject<Item> HSLA_STEEL_SHOVEL = ITEMS.register("hsla_steel_shovel", ItemSteelShovel::new);
    public static final RegistryObject<Item> HSLA_STEEL_AXE = ITEMS.register("hsla_steel_axe", ItemSteelAxe::new);
    public static final RegistryObject<Item> HSLA_STEEL_SWORD = ITEMS.register("hsla_steel_sword", ItemSteelSword::new);
    public static final RegistryObject<Item> HSLA_STEEL_HOE = ITEMS.register("hsla_steel_hoe", ItemSteelHoe::new);
    public static final RegistryObject<Item> HSLA_STEEL_SHEARS = ITEMS.register("hsla_steel_shears", ItemSteelShears::new);
//    public static final RegistryObject<Item> HSLA_STEEL_SHIELD = ITEMS.register("hsla_steel_shield", () -> new ShieldItem(new Item.Properties()));

    public static final RegistryObject<Item> HSLA_STEEL_INGOT = ITEMS.register("hsla_steel_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HSLA_STEEL_NUGGET = ITEMS.register("hsla_steel_nugget", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HSLA_HELMET = ITEMS.register("hsla_steel_helmet", () -> new ItemSteelArmor(EquipmentSlot.HEAD, new Item.Properties()));
    public static final RegistryObject<Item> HSLA_CHESTPLATE = ITEMS.register("hsla_steel_chestplate", () -> new ItemSteelArmor(EquipmentSlot.CHEST, new Item.Properties()));
    public static final RegistryObject<Item> HSLA_LEGGINGS = ITEMS.register("hsla_steel_leggings", () -> new ItemSteelArmor(EquipmentSlot.LEGS, new Item.Properties()));
    public static final RegistryObject<Item> HSLA_BOOTS = ITEMS.register("hsla_steel_boots", () -> new ItemSteelArmor(EquipmentSlot.FEET, new Item.Properties()));
    public static final RegistryObject<Item> HSLA_STEEL_SCRAP = ITEMS.register("hsla_steel_scrap", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HSLA_PLATE = ITEMS.register("hsla_steel_plate", () -> new Item(new Item.Properties())); //this is the "base plate"
    public static final RegistryObject<Item> HSLA_DRILL = ITEMS.register("hsla_steel_drill", () -> new Item(new Item.Properties()));
    //public static final RegistryObject<Item> HSLA_STEEL_SICKLE = ITEMS.register("steelsickle", ItemSteelSickle);
    public static final RegistryObject<Item> HSLA_STEEL_PACK = ITEMS.register("hsla_jet_chest", () -> new ItemJetPack(Materials.HSLA_STEEL));

    //Tungsten Alloy Items
    public static final RegistryObject<Item> TUNGSTEN_ALLOY_GEAR = ITEMS.register("tungsten_alloy_gear", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TUNGSTEN_ALLOY_GEAR_2x = ITEMS.register("tungsten_alloy_gear_2x", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TUNGSTEN_ALLOY_GEAR_4x = ITEMS.register("tungsten_alloy_gear_4x", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TUNGSTEN_ALLOY_GEAR_8x = ITEMS.register("tungsten_alloy_gear_8x", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TUNGSTEN_ALLOY_GEAR_16x = ITEMS.register("tungsten_alloy_gear_16x", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TUNGSTEN_ALLOY_SHAFT = ITEMS.register("tungsten_alloy_rod", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TUNGSTEN_ALLOY_SHAFT_CORE = ITEMS.register("tungsten_alloy_shaft_core", () -> new Item(new Item.Properties()));

    //Diamond Items
    public static final RegistryObject<Item> DIAMOND_GEAR = ITEMS.register("diamond_gear", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DIAMOND_GEAR_2x = ITEMS.register("diamond_gear_2x", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DIAMOND_GEAR_4x = ITEMS.register("diamond_gear_4x", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DIAMOND_GEAR_8x = ITEMS.register("diamond_gear_8x", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DIAMOND_GEAR_16x = ITEMS.register("diamond_gear_16x", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DIAMOND_SHAFT = ITEMS.register("diamond_rod", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DIAMOND_SHAFT_CORE = ITEMS.register("diamond_shaft_core", () -> new Item(new Item.Properties()));

    //Bedrock Items
    public static final RegistryObject<Item> BEDROCK_ALLOY_GEAR = ITEMS.register("bedrock_alloy_gear", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BEDROCK_ALLOY_GEAR_2x = ITEMS.register("bedrock_alloy_gear_2x", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BEDROCK_ALLOY_GEAR_4x = ITEMS.register("bedrock_alloy_gear_4x", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BEDROCK_ALLOY_GEAR_8x = ITEMS.register("bedrock_alloy_gear_8x", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BEDROCK_ALLOY_GEAR_16x = ITEMS.register("bedrock_alloy_gear_16x", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BEDROCK_DUST = ITEMS.register("bedrock_dust", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BEDROCK_ALLOY_SHAFT = ITEMS.register("bedrock_alloy_rod", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BEDROCK_ALLOY_SHAFT_CORE = ITEMS.register("bedrock_alloy_shaft_core", () -> new Item(new Item.Properties()));
//    public static final RegistryObject<Item> BEDROCK_ALLOY_SICKLE = ITEMS.register("bedsickle", ItemBedrockSickle::new);
    //public static final RegistryObject<Item> BEDROCK_ALLOY_GRAFTER = ITEMS.register("bedgrafter", ItemBedrockGrafter::new, ModList.FORESTRY);
    //public static final RegistryObject<Item> BEDROCK_ALLOY_SAW = ITEMS.register("bedsaw", ItemBedrockSaw::new, ModList.MULTIPART);
    public static final RegistryObject<Item> BEDROCK_ALLOY_PACK = ITEMS.register("bedrock_alloy_jet_chest", () -> new ItemJetPack(Materials.BEDROCK_ALLOY));

    public static final RegistryObject<Item> BEDROCK_ALLOY_PICK = ITEMS.register("bedrock_alloy_pickaxe", ItemBedrockPickaxe::new);
    public static final RegistryObject<Item> BEDROCK_ALLOY_AXE = ITEMS.register("bedrock_alloy_axe", ItemBedrockAxe::new);
    public static final RegistryObject<Item> BEDROCK_ALLOY_SWORD = ITEMS.register("bedrock_alloy_sword", ItemBedrockSword::new);
    public static final RegistryObject<Item> BEDROCK_ALLOY_HOE = ITEMS.register("bedrock_alloy_hoe", ItemBedrockHoe::new);
    public static final RegistryObject<Item> BEDROCK_ALLOY_SHEARS = ITEMS.register("bedrock_alloy_shears", ItemBedrockShears::new);
    public static final RegistryObject<Item> BEDROCK_ALLOY_SHOVEL = ITEMS.register("bedrock_alloy_shovel", ItemBedrockShovel::new);
//    public static final RegistryObject<Item> BEDROCK_ALLOY_SHIELD = ITEMS.register("bedrock_alloy_shield", () -> new ShieldItem(new Item.Properties()));

    public static final RegistryObject<Item> BEDROCK_ALLOY_INGOT = ITEMS.register("bedrock_alloy_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BEDROCK_ALLOY_HELMET = ITEMS.register("bedrock_alloy_helmet", () -> new ItemBedrockArmor(EquipmentSlot.HEAD, new Item.Properties()));
    public static final RegistryObject<Item> BEDROCK_ALLOY_CHESTPLATE = ITEMS.register("bedrock_alloy_chestplate", () -> new ItemBedrockArmor(EquipmentSlot.CHEST, new Item.Properties()));
    public static final RegistryObject<Item> BEDROCK_ALLOY_LEGGINGS = ITEMS.register("bedrock_alloy_leggings", () -> new ItemBedrockArmor(EquipmentSlot.LEGS, new Item.Properties()));
    public static final RegistryObject<Item> BEDROCK_ALLOY_BOOTS = ITEMS.register("bedrock_alloy_boots", () -> new ItemBedrockArmor(EquipmentSlot.FEET, new Item.Properties()));
    public static final RegistryObject<Item> BEDROCK_DRILL = ITEMS.register("bedrock_alloy_drill", () -> new Item(new Item.Properties()));

    //Flakes
    public static final RegistryObject<Item> TUNGSTEN_FLAKES = ITEMS.register("tungsten_flakes", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DIAMOND_FLAKES = ITEMS.register("diamond_flakes", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> COMPOST = ITEMS.register("compost", () -> new Item(new Item.Properties()));

    //Fluid Buckets
    public static final RegistryObject<Item> LUBE_BUCKET = ITEMS.register("lubricant_bucket", () -> new BucketItem(RotaryFluids.LUBRICANT, new Item.Properties()));
    public static final RegistryObject<Item> JET_FUEL_BUCKET = ITEMS.register("jet_fuel_bucket", () -> new BucketItem(RotaryFluids.JET_FUEL, new Item.Properties()));
    public static final RegistryObject<Item> ETHANOLBUCKET = ITEMS.register("liquid_ethanol_bucket", () -> new BucketItem(RotaryFluids.ETHANOL, new Item.Properties()));
    public static final RegistryObject<Item> NITROGENBUCKET = ITEMS.register("liquid_nitrogen_bucket", () -> new BucketItem(RotaryFluids.LIQUID_NITROGEN, new Item.Properties()));

    //Other Items
    public static final RegistryObject<Item> YEAST = ITEMS.register("yeast", () -> new ItemBasic(new Item.Properties(), 64));
    public static final RegistryObject<Item> ETHANOL = ITEMS.register("ethanol_crystals", () -> new ItemBasic(new Item.Properties(), 64));
    public static final RegistryObject<Item> CANOLA_SEEDS = ITEMS.register("canola_seeds", () -> new ItemNameBlockItem(RotaryBlocks.CANOLA.get(), new Item.Properties()));
    public static final RegistryObject<Item> CANOLA_HUSKS = ITEMS.register("canola_seed_husks", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DENSE_CANOLA_SEEDS = ITEMS.register("dense_canola_seeds", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TAR = ITEMS.register("tar", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ALUMINUM_ALLOY_POWDER = ITEMS.register("aluminum_alloy_powder", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COAL_DUST = ITEMS.register("coal_dust", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FLOUR = ITEMS.register("flour", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CALCULATOR = ITEMS.register("calculator", () -> new ItemCalculator(new Item.Properties()));
    public static final RegistryObject<Item> COKE = ITEMS.register("coke", () -> new ItemCalculator(new Item.Properties()));
    public static final RegistryObject<Item> NITRATE = ITEMS.register("nitrate", () -> new ItemCalculator(new Item.Properties()));
    public static final RegistryObject<Item> SALT = ITEMS.register("salt", () -> new ItemCalculator(new Item.Properties()));
    public static final RegistryObject<Item> SILVERIODIDE = ITEMS.register("silveriodide", () -> new ItemCalculator(new Item.Properties()));




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
        if (is == UPGRADE.get().getDefaultInstance()){
            return true;
        }
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
                return is.getTag() != null;
        }*/
        return false;
    }
}