package reika.rotarycraft.registry;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.blocks.CanolaBlock;
import reika.rotarycraft.base.blocks.entity.*;
import reika.rotarycraft.base.blocks.entity.engine.*;
import reika.rotarycraft.base.blocks.entity.pipe.*;
import reika.rotarycraft.base.blocks.entity.transmission.*;
import reika.rotarycraft.renders.item.ShaftItemRenderer;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class RotaryBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, RotaryCraft.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RotaryCraft.MODID);


//    public static final RegistryObject<Block> ENGINE = register("engine", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(20)));

    public static final RegistryObject<Block> GPR = register("gpr", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(20)));
    public static final RegistryObject<Block> WOOD_FLYWHEEL = register("wood_flywheel", () -> new BlockGearbox(GearboxTypes.WOOD, BlockBehaviour.Properties.of(Material.WOOD).strength(20)));
    public static final RegistryObject<Block> HSLA_FLYWHEEL = register("hsla_flywheel", () -> new BlockGearbox(GearboxTypes.STEEL, BlockBehaviour.Properties.of(Material.METAL).strength(20)));
    public static final RegistryObject<Block> TUNGSTEN_FLYWHEEL = register("tungsten_flywheel", () -> new BlockGearbox(GearboxTypes.TUNGSTEN, BlockBehaviour.Properties.of(Material.METAL).strength(20)));
    public static final RegistryObject<Block> DIAMOND_FLYWHEEL = register("diamond_flywheel", () -> new BlockGearbox(GearboxTypes.DIAMOND, BlockBehaviour.Properties.of(Material.METAL).strength(20)));
    public static final RegistryObject<Block> BEDROCK_FLYWHEEL = register("bedrock_flywheel", () -> new BlockGearbox(GearboxTypes.BEDROCK, BlockBehaviour.Properties.of(Material.METAL).strength(20)));

    public static final RegistryObject<Block> HSLA_GEARBOX_2x = register("hsla_gearbox_2x", () -> new BlockGearbox(GearboxTypes.STEEL, BlockBehaviour.Properties.of(Material.METAL).strength(20)));
    public static final RegistryObject<Block> HSLA_GEARBOX_4x = register("hsla_gearbox_4x", () -> new BlockGearbox(GearboxTypes.STEEL, BlockBehaviour.Properties.of(Material.METAL).strength(20)));
    public static final RegistryObject<Block> HSLA_GEARBOX_8x = register("hsla_gearbox_8x", () -> new BlockGearbox(GearboxTypes.STEEL, BlockBehaviour.Properties.of(Material.METAL).strength(20)));
    public static final RegistryObject<Block> HSLA_GEARBOX_16x = register("hsla_gearbox_16x", () -> new BlockGearbox(GearboxTypes.STEEL, BlockBehaviour.Properties.of(Material.METAL).strength(20)));
    public static final RegistryObject<Block> TUNGSTEN_GEARBOX_2x = register("tungsten_gearbox_2x", () -> new BlockGearbox(GearboxTypes.TUNGSTEN, BlockBehaviour.Properties.of(Material.METAL).strength(20)));
    public static final RegistryObject<Block> TUNGSTEN_GEARBOX_4x = register("tungsten_gearbox_4x", () -> new BlockGearbox(GearboxTypes.TUNGSTEN, BlockBehaviour.Properties.of(Material.METAL).strength(20)));
    public static final RegistryObject<Block> TUNGSTEN_GEARBOX_8x = register("tungsten_gearbox_8x", () -> new BlockGearbox(GearboxTypes.TUNGSTEN, BlockBehaviour.Properties.of(Material.METAL).strength(20)));
    public static final RegistryObject<Block> TUNGSTEN_GEARBOX_16x = register("tungsten_gearbox_16x", () -> new BlockGearbox(GearboxTypes.TUNGSTEN, BlockBehaviour.Properties.of(Material.METAL).strength(20)));
    public static final RegistryObject<Block> DIAMOND_GEARBOX_2x = register("diamond_gearbox_2x", () -> new BlockGearbox(GearboxTypes.DIAMOND, BlockBehaviour.Properties.of(Material.METAL).strength(20)));
    public static final RegistryObject<Block> DIAMOND_GEARBOX_4x = register("diamond_gearbox_4x", () -> new BlockGearbox(GearboxTypes.DIAMOND, BlockBehaviour.Properties.of(Material.METAL).strength(20)));
    public static final RegistryObject<Block> DIAMOND_GEARBOX_8x = register("diamond_gearbox_8x", () -> new BlockGearbox(GearboxTypes.DIAMOND, BlockBehaviour.Properties.of(Material.METAL).strength(20)));
    public static final RegistryObject<Block> DIAMOND_GEARBOX_16x = register("diamond_gearbox_16x", () -> new BlockGearbox(GearboxTypes.DIAMOND, BlockBehaviour.Properties.of(Material.METAL).strength(20)));
    public static final RegistryObject<Block> BEDROCK_GEARBOX_2x = register("bedrock_gearbox_2x", () -> new BlockGearbox(GearboxTypes.BEDROCK, BlockBehaviour.Properties.of(Material.METAL).strength(20)));
    public static final RegistryObject<Block> BEDROCK_GEARBOX_4x = register("bedrock_gearbox_4x", () -> new BlockGearbox(GearboxTypes.BEDROCK, BlockBehaviour.Properties.of(Material.METAL).strength(20)));
    public static final RegistryObject<Block> BEDROCK_GEARBOX_8x = register("bedrock_gearbox_8x", () -> new BlockGearbox(GearboxTypes.BEDROCK, BlockBehaviour.Properties.of(Material.METAL).strength(20)));
    public static final RegistryObject<Block> BEDROCK_GEARBOX_16x = register("bedrock_gearbox_16x", () -> new BlockGearbox(GearboxTypes.BEDROCK, BlockBehaviour.Properties.of(Material.METAL).strength(20)));
    //    public static final RegistryObject<Block> FILLING_STATION = register("filling_station", () -> new BlockFillingStation(BlockBehaviour.Properties.of(Material.METAL).strength(20)));
    public static final RegistryObject<Block> WOOD_SHAFT = register("wood_shaft", () -> new BlockShaft(MaterialRegistry.WOOD, BlockBehaviour.Properties.of(Material.WOOD).strength(20)), block -> new BlockItem(block, new Item.Properties()) {
        @Override
        public void initializeClient(Consumer<IClientItemExtensions> consumer) {
            consumer.accept(new IClientItemExtensions() {
                @Override
                public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    return new ShaftItemRenderer(null, null);
                }
            });
        }
    });
    public static final RegistryObject<Block> STONE_SHAFT = register("stone_shaft", () -> new BlockShaft(MaterialRegistry.STONE, BlockBehaviour.Properties.of(Material.WOOD).strength(20)), block -> new BlockItem(block, new Item.Properties()) {
        @Override
        public void initializeClient(Consumer<IClientItemExtensions> consumer) {
            consumer.accept(new IClientItemExtensions() {
                @Override
                public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    return new ShaftItemRenderer(null, null);
                }
            });
        }
    });
    public static final RegistryObject<Block> HSLA_SHAFT = register("hsla_shaft", () -> new BlockShaft(MaterialRegistry.STEEL, BlockBehaviour.Properties.of(Material.METAL).strength(20)), block -> new BlockItem(block, new Item.Properties()) {
        @Override
        public void initializeClient(Consumer<IClientItemExtensions> consumer) {
            consumer.accept(new IClientItemExtensions() {
                @Override
                public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    return new ShaftItemRenderer(null, null);
                }
            });
        }
    });
    public static final RegistryObject<Block> HSLA_STEEL_BLOCK = register("hsla_steel_block", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(20)));
    public static final RegistryObject<Block> TUNGSTEN_SHAFT = register("tungsten_shaft", () -> new BlockShaft(MaterialRegistry.TUNGSTEN, BlockBehaviour.Properties.of(Material.METAL).strength(20)), block -> new BlockItem(block, new Item.Properties()) {
        @Override
        public void initializeClient(Consumer<IClientItemExtensions> consumer) {
            consumer.accept(new IClientItemExtensions() {
                @Override
                public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    return new ShaftItemRenderer(null, null);
                }
            });
        }
    });
    public static final RegistryObject<Block> DIAMOND_SHAFT = register("diamond_shaft", () -> new BlockShaft(MaterialRegistry.DIAMOND, BlockBehaviour.Properties.of(Material.METAL).strength(20)), block -> new BlockItem(block, new Item.Properties()) {
        @Override
        public void initializeClient(Consumer<IClientItemExtensions> consumer) {
            consumer.accept(new IClientItemExtensions() {
                @Override
                public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    return new ShaftItemRenderer(null, null);
                }
            });
        }
    });
    public static final RegistryObject<Block> BEDROCK_SHAFT = register("bedrock_shaft", () -> new BlockShaft(MaterialRegistry.BEDROCK, BlockBehaviour.Properties.of(Material.METAL).strength(20)), block -> new BlockItem(block, new Item.Properties()) {
        @Override
        public void initializeClient(Consumer<IClientItemExtensions> consumer) {
            consumer.accept(new IClientItemExtensions() {
                @Override
                public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    return new ShaftItemRenderer(null, null);
                }
            });
        }
    });

    public static final RegistryObject<Block> TRANS = register("trans", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(20)));
    public static final RegistryObject<Block> SOLAR_TOWER = register("solar_tower", () -> new BlockSolarTower(BlockBehaviour.Properties.of(Material.METAL).strength(20)));
    public static final RegistryObject<Block> BCENGINE = register("bcengine", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(20)));
    public static final RegistryObject<Block> DECO = register("deco", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(20)));
    public static final RegistryObject<Block> CANOLA = BLOCKS.register("canola", () -> new CanolaBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.CROP)));//, block -> new BlockItem(block, new Item.Properties()));
    public static final RegistryObject<Block> BEAM = register("beam", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(20)));
    public static final RegistryObject<Block> BRIDGE = register("bridge", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(20)));
    public static final RegistryObject<Block> MININGPIPE = register("miningpipe", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(20)));
    public static final RegistryObject<Block> BLASTGLASS = register("blastglass", () -> new GlassBlock(BlockBehaviour.Properties.of(Material.METAL).strength(20)));
    public static final RegistryObject<Block> BLASTPANE = register("blastpane", () -> new StainedGlassPaneBlock(DyeColor.BLACK, BlockBehaviour.Properties.of(Material.GLASS).strength(20)));
    public static final RegistryObject<Block> BEDROCKSLICE = register("bedrockslice", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(-1)));
    public static final RegistryObject<Block> DECOTANK = register("decotank", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(20)));

    public static final RegistryObject<Block> ANTHRA = register("anthra", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> LONS = register("lons", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> SHIELD = register("shield", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> BEDROCK = register("bedrock", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(-1)));
    public static final RegistryObject<Block> COKE = register("coke_block", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> SHAFT_CROSS = register("cross", () -> new BlockShaft(MaterialRegistry.STEEL, BlockBehaviour.Properties.of(Material.METAL).strength(5)), block -> new BlockItem(block, new Item.Properties()) {
        @Override
        public void initializeClient(Consumer<IClientItemExtensions> consumer) {
            consumer.accept(new IClientItemExtensions() {
                @Override
                public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    return new ShaftItemRenderer(null, null);
                }
            });
        }
    });
    public static final RegistryObject<Block> SHAFT_MERGE = register("merge", () -> new BlockShaft(MaterialRegistry.STEEL, BlockBehaviour.Properties.of(Material.METAL).strength(5)), block -> new BlockItem(block, new Item.Properties()) {
        @Override
        public void initializeClient(Consumer<IClientItemExtensions> consumer) {
            consumer.accept(new IClientItemExtensions() {
                @Override
                public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    return new ShaftItemRenderer(null, null);
                }
            });
        }
    });
    public static final RegistryObject<Block> SPLITTER = register("splitter", () -> new BlockSplitter(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> SMOKE_DETECTOR = register("smoke_detector", () -> new BlockSmokeDetector(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> DISTRIBUTION_CLUTCH = register("distribution_clutch", () -> new BlockDistributionClutch(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> CLUTCH = register("clutch", () -> new BlockClutch(BlockBehaviour.Properties.of(Material.METAL).strength(5)));

    public static final RegistryObject<Block> MOB_HARVESTER = register("mob_harvester", () -> new BlockSmokeDetector(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> RESERVOIR = register("reservoir", () -> new BlockReservoir(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    //Engines
    public static final RegistryObject<Block> WIND_ENGINE = register("wind_engine", () -> new BlockWindEngine(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> STEAM_ENGINE = register("steam_engine", () -> new BlockSteamEngine(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> PERFORMANCE_ENGINE = register("performance_engine", () -> new BlockPerformanceEngine(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> MICRO_TURBINE = register("microturbine", () -> new BlockMicroturbine(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> GAS_ENGINE = register("gas_engine", () -> new BlockGasEngine(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> DC_ENGINE = register("dc_engine", () -> new BlockDCEngine(BlockBehaviour.Properties.of(Material.STONE).strength(5)));
    public static final RegistryObject<Block> MAGNETOSTATIC_ENGINE = register("magnetostatic_engine", () -> new BlockMagnetEngine(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> AC_ENGINE = register("ac_engine", () -> new BlockAcEngine(BlockBehaviour.Properties.of(Material.METAL).strength(5)));


    public static final RegistryObject<Block> PUMP = register("pump", () -> new BlockPump(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> AEROSOLIZER = register("aerosolizer", () -> new BlockAerosolizer(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> WINDER = register("winder", () -> new BlockAerosolizer(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> FLOODLIGHT = register("floodlight", () -> new BlockFloodlight(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> DYNAMOMETER = register("dynamometer", () -> new BlockDynamometer(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> MIRROR = register("mirror", () -> new BlockMirror(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> VAN_DE_GRAFF = register("van_de_graff", () -> new BlockVanDeGraff(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> LANDMINE = register("landmine", () -> new BlockLandmine(BlockBehaviour.Properties.of(Material.METAL).strength(5)));

    public static final RegistryObject<Block> COOLING_FIN = register("cooling_fin", () -> new BlockCoolingFin(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> MUSIC_BOX = register("music_box", () -> new BlockMusicBox(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> HEAT_RAY = register("heat_ray", () -> new BlockHeatRay(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> OBSIDIAN_MAKER = register("obsidian_maker", () -> new BlockObsidianMaker(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> PLAYER_DETECTOR = register("player_detector", () -> new BlockPlayerDetector(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> HEATER = register("heater", () -> new BlockHeater(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> ITEM_CANNON = register("item_cannon", () -> new BlockItemCannon(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> CAVE_SCANNER = register("cave_scanner", () -> new BlockCaveScanner(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> BLOCK_CANNON = register("block_cannon", () -> new BlockBlockCannon(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> REFRESHER = register("refresher", () -> new BlockRefresher(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> SPILLER = register("spiller", () -> new BlockSpiller(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> TNT_CANNON = register("tnt_cannon", () -> new BlockTntCannon(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> CONTAINMENT = register("containment", () -> new BlockContainment(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> BUCKET_FILLER = register("bucket_filler", () -> new BlockBucketFiller(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> SELF_DESTRUCT = register("self_destruct", () -> new BlockSelfDestruct(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> LINE_BUILDER = register("line_builder", () -> new BlockLineBuilder(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> BEAM_MIRROR = register("beam_mirror", () -> new BlockBeamMirror(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> MULTI_CLUTCH = register("multi_clutch", () -> new BlockMultiClutch(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> FRICTION_BOILER = register("friction_boiler", () -> new BlockFrictionBoiler(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> STEAM_TURBINE = register("steam_turbine", () -> new BlockSteamTurbine(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> LAVA_SMELTORY = register("lava_smeltory", () -> new BlockBigFurnace(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> PARTICLE = register("particle", () -> new BlockParticle(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> GRINDSTONE = register("grindstone", () -> new BlockGrindstone(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> BLOWER = register("blower", () -> new BlockBlower(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> REFRIGERATOR = register("refrigerator", () -> new BlockRefrigerator(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> COMPOSTER = register("composter", () -> new BlockComposter(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> FILLER = register("filler", () -> new BlockFiller(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> SPILLWAY = register("spillway", () -> new BlockSpillway(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> BEVEL_GEARS = register("bevel_gears", () -> new BlockBevelGears(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> SORTER = register("sorter", () -> new BlockSorter(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> FRICTION_HEATER = register("friction_heater", () -> new BlockFrictionHeater(BlockBehaviour.Properties.of(Material.METAL).strength(5)));

//    public static final RegistryObject<Block> PULSE_JET_FURNACE = register("pulse_jet_furnace", () -> new BlockPulseJetFurnace(BlockBehaviour.Properties.of(Material.METAL).strength(5)));

    public static final RegistryObject<Block> COIL = register("coil", () -> new BlockCoil(BlockBehaviour.Properties.of(Material.METAL).strength(5))); //todo blocks for these 4
    public static final RegistryObject<Block> CVT = register("cvt", () -> new BlockCVT(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> WORMGEAR = register("wormgear", () -> new BlockWormGear(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> HIGHGEAR = register("highgear", () -> new BlockHighGear(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> GRINDER = register("grinder", () -> new BlockGrinder(BlockBehaviour.Properties.of(Material.METAL).strength(5)));


    public static final RegistryObject<Block> HOSE = register("hose", () -> new BlockHose(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> PIPE = register("pipe", () -> new BlockPipe(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> FUEL_LINE = register("fuel_line", () -> new BlockFuelLine(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> VALVE = register("valve", () -> new BlockValve(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> BYPASS = register("bypass", () -> new BlockBypass(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> SEPARATION = register("separation", () -> new BlockSeperation(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> SUCTION = register("suction", () -> new BlockSuction(BlockBehaviour.Properties.of(Material.METAL).strength(5)));
    public static final RegistryObject<Block> BEDROCK_PIPE = register("bedrock_pipe", () -> new BlockBedrockPipe(BlockBehaviour.Properties.of(Material.METAL).strength(5)));

    private static final Block.Properties WOOD_PROPERTIES = Block.Properties.of(Material.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD);

    static {
//        for (RoCWoodTypes type : RoCWoodTypes.VALUES) {
//            register(type.toString() + "_gearbox_2x", () -> new BlockGearbox(GearboxTypes.WOOD, WOOD_PROPERTIES));
//            register(type + "_gearbox_4x", () -> new BlockGearbox(GearboxTypes.WOOD, WOOD_PROPERTIES));
//            register(type + "_gearbox_8x", () -> new BlockGearbox(GearboxTypes.WOOD, WOOD_PROPERTIES));
//            register(type + "_gearbox_16x", () -> new BlockGearbox(GearboxTypes.WOOD, WOOD_PROPERTIES));
//        }
    }

    private static <BLOCK extends Block> RegistryObject<BLOCK> register(final String name, final Supplier<BLOCK> blockFactory) {
        return register(name, blockFactory, block -> new BlockItem(block, new Item.Properties()));
    }

    private static <BLOCK extends Block> RegistryObject<BLOCK> register(final String name, final Supplier<BLOCK> blockFactory, final IBlockItemFactory<BLOCK> itemFactory) {
        final RegistryObject<BLOCK> block = BLOCKS.register(name, blockFactory);
        ITEMS.register(name, () -> itemFactory.create(block.get()));

        return block;
    }

    @FunctionalInterface
    private interface IBlockItemFactory<BLOCK extends Block> {
        Item create(BLOCK block);
    }
}