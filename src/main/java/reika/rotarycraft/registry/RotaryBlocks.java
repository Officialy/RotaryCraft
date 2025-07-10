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
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.api.RoCWoodTypes;
import reika.rotarycraft.base.blocks.CanolaBlock;
import reika.rotarycraft.base.blocks.entity.*;
import reika.rotarycraft.base.blocks.entity.engine.*;
import reika.rotarycraft.base.blocks.entity.pipe.*;
import reika.rotarycraft.base.blocks.entity.transmission.*;
import reika.rotarycraft.renders.item.MachineItemRenderer;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class RotaryBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, RotaryCraft.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RotaryCraft.MODID);

    public static final RegistryObject<Block> GPR = registerMachineBlock("gpr", () -> new Block(BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> WOOD_FLYWHEEL = registerMachineBlock("wood_flywheel", () -> new BlockGearbox(GearboxTypes.WOOD, BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> HSLA_FLYWHEEL = registerMachineBlock("hsla_flywheel", () -> new BlockGearbox(GearboxTypes.STEEL, BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> TUNGSTEN_FLYWHEEL = registerMachineBlock("tungsten_flywheel", () -> new BlockGearbox(GearboxTypes.TUNGSTEN, BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> DIAMOND_FLYWHEEL = registerMachineBlock("diamond_flywheel", () -> new BlockGearbox(GearboxTypes.DIAMOND, BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> BEDROCK_FLYWHEEL = registerMachineBlock("bedrock_flywheel", () -> new BlockGearbox(GearboxTypes.BEDROCK, BlockBehaviour.Properties.of().strength(20)));

    public static final RegistryObject<Block> HSLA_GEARBOX_2x = registerMachineBlock("hsla_gearbox_2x", () -> new BlockGearbox(GearboxTypes.STEEL, BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> HSLA_GEARBOX_4x = registerMachineBlock("hsla_gearbox_4x", () -> new BlockGearbox(GearboxTypes.STEEL, BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> HSLA_GEARBOX_8x = registerMachineBlock("hsla_gearbox_8x", () -> new BlockGearbox(GearboxTypes.STEEL, BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> HSLA_GEARBOX_16x = registerMachineBlock("hsla_gearbox_16x", () -> new BlockGearbox(GearboxTypes.STEEL, BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> WOOD_GEARBOX_2x = registerMachineBlock("wood_gearbox_2x", () -> new BlockGearbox(GearboxTypes.STEEL, BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> WOOD_GEARBOX_4x = registerMachineBlock("wood_gearbox_4x", () -> new BlockGearbox(GearboxTypes.STEEL, BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> WOOD_GEARBOX_8x = registerMachineBlock("wood_gearbox_8x", () -> new BlockGearbox(GearboxTypes.STEEL, BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> WOOD_GEARBOX_16x = registerMachineBlock("wood_gearbox_16x", () -> new BlockGearbox(GearboxTypes.STEEL, BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> STONE_GEARBOX_2x = registerMachineBlock("stone_gearbox_2x", () -> new BlockGearbox(GearboxTypes.STEEL, BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> STONE_GEARBOX_4x = registerMachineBlock("stone_gearbox_4x", () -> new BlockGearbox(GearboxTypes.STEEL, BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> STONE_GEARBOX_8x = registerMachineBlock("stone_gearbox_8x", () -> new BlockGearbox(GearboxTypes.STEEL, BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> STONE_GEARBOX_16x = registerMachineBlock("stone_gearbox_16x", () -> new BlockGearbox(GearboxTypes.STEEL, BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> TUNGSTEN_GEARBOX_2x = registerMachineBlock("tungsten_gearbox_2x", () -> new BlockGearbox(GearboxTypes.TUNGSTEN, BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> TUNGSTEN_GEARBOX_4x = registerMachineBlock("tungsten_gearbox_4x", () -> new BlockGearbox(GearboxTypes.TUNGSTEN, BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> TUNGSTEN_GEARBOX_8x = registerMachineBlock("tungsten_gearbox_8x", () -> new BlockGearbox(GearboxTypes.TUNGSTEN, BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> TUNGSTEN_GEARBOX_16x = registerMachineBlock("tungsten_gearbox_16x", () -> new BlockGearbox(GearboxTypes.TUNGSTEN, BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> DIAMOND_GEARBOX_2x = registerMachineBlock("diamond_gearbox_2x", () -> new BlockGearbox(GearboxTypes.DIAMOND, BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> DIAMOND_GEARBOX_4x = registerMachineBlock("diamond_gearbox_4x", () -> new BlockGearbox(GearboxTypes.DIAMOND, BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> DIAMOND_GEARBOX_8x = registerMachineBlock("diamond_gearbox_8x", () -> new BlockGearbox(GearboxTypes.DIAMOND, BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> DIAMOND_GEARBOX_16x = registerMachineBlock("diamond_gearbox_16x", () -> new BlockGearbox(GearboxTypes.DIAMOND, BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> BEDROCK_GEARBOX_2x = registerMachineBlock("bedrock_gearbox_2x", () -> new BlockGearbox(GearboxTypes.BEDROCK, BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> BEDROCK_GEARBOX_4x = registerMachineBlock("bedrock_gearbox_4x", () -> new BlockGearbox(GearboxTypes.BEDROCK, BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> BEDROCK_GEARBOX_8x = registerMachineBlock("bedrock_gearbox_8x", () -> new BlockGearbox(GearboxTypes.BEDROCK, BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> BEDROCK_GEARBOX_16x = registerMachineBlock("bedrock_gearbox_16x", () -> new BlockGearbox(GearboxTypes.BEDROCK, BlockBehaviour.Properties.of().strength(20)));
    //    public static final RegistryObject<Block> FILLING_STATION = register("filling_station", () -> new BlockFillingStation(BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> WOOD_SHAFT = registerMachineBlock("wood_shaft", () -> new BlockShaft(MaterialRegistry.WOOD, BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> STONE_SHAFT = registerMachineBlock("stone_shaft", () -> new BlockShaft(MaterialRegistry.STONE, BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> HSLA_SHAFT = registerMachineBlock("hsla_shaft", () -> new BlockShaft(MaterialRegistry.STEEL, BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> HSLA_STEEL_BLOCK = register("hsla_steel_block", () -> new Block(BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> TUNGSTEN_SHAFT = registerMachineBlock("tungsten_shaft", () -> new BlockShaft(MaterialRegistry.TUNGSTEN, BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> DIAMOND_SHAFT = registerMachineBlock("diamond_shaft", () -> new BlockShaft(MaterialRegistry.DIAMOND, BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> BEDROCK_SHAFT = registerMachineBlock("bedrock_shaft", () -> new BlockShaft(MaterialRegistry.BEDROCK, BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> FAN = registerMachineBlock("fan", () -> new BlockFan(BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> TRANS = register("trans", () -> new Block(BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> SOLAR_TOWER = registerMachineBlock("solar_tower", () -> new BlockSolarTower(BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> BCENGINE = register("bcengine", () -> new Block(BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> DECO = register("deco", () -> new Block(BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> CANOLA = BLOCKS.register("canola", () -> new CanolaBlock(BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.CROP)));
    public static final RegistryObject<Block> BEAM = register("beam", () -> new Block(BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> BRIDGE = register("bridge", () -> new Block(BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> MININGPIPE = register("miningpipe", () -> new Block(BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> BLASTGLASS = register("blastglass", () -> new GlassBlock(BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> BLASTPANE = register("blastpane", () -> new StainedGlassPaneBlock(DyeColor.BLACK, BlockBehaviour.Properties.of().strength(20)));
    public static final RegistryObject<Block> BEDROCKSLICE = BLOCKS.register("bedrockslice", () -> new Block(BlockBehaviour.Properties.of().strength(-1)));
    public static final RegistryObject<Block> DECOTANK = register("decotank", () -> new Block(BlockBehaviour.Properties.of().strength(20)));

    public static final RegistryObject<Block> ANTHRA = register("anthra", () -> new Block(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> LONS = register("lons", () -> new Block(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> SHIELD = register("shield", () -> new Block(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> BEDROCK = BLOCKS.register("bedrock", () -> new Block(BlockBehaviour.Properties.of().strength(-1)));
    public static final RegistryObject<Block> COKE = register("coke_block", () -> new Block(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> SHAFT_CROSS = registerMachineBlock("cross", () -> new BlockShaft(MaterialRegistry.STEEL, BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> SHAFT_MERGE = registerMachineBlock("merge", () -> new BlockShaft(MaterialRegistry.STEEL, BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> SPLITTER = registerMachineBlock("splitter", () -> new BlockSplitter(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> SMOKE_DETECTOR = registerMachineBlock("smoke_detector", () -> new BlockSmokeDetector(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> DISTRIBUTION_CLUTCH = registerMachineBlock("distribution_clutch", () -> new BlockDistributionClutch(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> CLUTCH = registerMachineBlock("clutch", () -> new BlockClutch(BlockBehaviour.Properties.of().strength(5)));

    public static final RegistryObject<Block> MOB_HARVESTER = registerMachineBlock("mob_harvester", () -> new BlockSmokeDetector(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> RESERVOIR = registerMachineBlock("reservoir", () -> new BlockReservoir(BlockBehaviour.Properties.of().strength(5)));
    //Engines
    public static final RegistryObject<Block> WIND_ENGINE = registerMachineBlock("wind_engine", () -> new BlockWindEngine(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> STEAM_ENGINE = registerMachineBlock("steam_engine", () -> new BlockSteamEngine(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> PERFORMANCE_ENGINE = registerMachineBlock("performance_engine", () -> new BlockPerformanceEngine(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> MICRO_TURBINE = registerMachineBlock("microturbine", () -> new BlockMicroturbine(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> GAS_ENGINE = registerMachineBlock("gas_engine", () -> new BlockGasEngine(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> DC_ENGINE = registerMachineBlock("dc_engine", () -> new BlockDCEngine(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> MAGNETOSTATIC_ENGINE = registerMachineBlock("magnetostatic_engine", () -> new BlockMagnetEngine(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> AC_ENGINE = registerMachineBlock("ac_engine", () -> new BlockAcEngine(BlockBehaviour.Properties.of().strength(5)));


    public static final RegistryObject<Block> PUMP = registerMachineBlock("pump", () -> new BlockPump(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> AEROSOLIZER = registerMachineBlock("aerosolizer", () -> new BlockAerosolizer(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> WINDER = registerMachineBlock("winder", () -> new BlockAerosolizer(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> FLOODLIGHT = registerMachineBlock("floodlight", () -> new BlockFloodlight(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> DYNAMOMETER = registerMachineBlock("dynamometer", () -> new BlockDynamometer(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> MIRROR = registerMachineBlock("mirror", () -> new BlockMirror(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> VAN_DE_GRAFF = registerMachineBlock("van_de_graff", () -> new BlockVanDeGraff(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> LANDMINE = registerMachineBlock("landmine", () -> new BlockLandmine(BlockBehaviour.Properties.of().strength(5)));

    public static final RegistryObject<Block> COOLING_FIN = registerMachineBlock("cooling_fin", () -> new BlockCoolingFin(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> MUSIC_BOX = registerMachineBlock("music_box", () -> new BlockMusicBox(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> HEAT_RAY = registerMachineBlock("heat_ray", () -> new BlockHeatRay(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> OBSIDIAN_MAKER = registerMachineBlock("obsidian_maker", () -> new BlockObsidianMaker(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> PLAYER_DETECTOR = registerMachineBlock("player_detector", () -> new BlockPlayerDetector(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> HEATER = registerMachineBlock("heater", () -> new BlockHeater(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> ITEM_CANNON = registerMachineBlock("item_cannon", () -> new BlockItemCannon(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> CAVE_SCANNER = registerMachineBlock("cave_scanner", () -> new BlockCaveScanner(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> BLOCK_CANNON = registerMachineBlock("block_cannon", () -> new BlockBlockCannon(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> REFRESHER = registerMachineBlock("refresher", () -> new BlockRefresher(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> SPILLER = registerMachineBlock("spiller", () -> new BlockSpiller(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> TNT_CANNON = registerMachineBlock("tnt_cannon", () -> new BlockTntCannon(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> CONTAINMENT = registerMachineBlock("containment", () -> new BlockContainment(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> BUCKET_FILLER = registerMachineBlock("bucket_filler", () -> new BlockBucketFiller(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> SELF_DESTRUCT = registerMachineBlock("self_destruct", () -> new BlockSelfDestruct(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> LINE_BUILDER = registerMachineBlock("line_builder", () -> new BlockLineBuilder(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> BEAM_MIRROR = registerMachineBlock("beam_mirror", () -> new BlockBeamMirror(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> MULTI_CLUTCH = registerMachineBlock("multi_clutch", () -> new BlockMultiClutch(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> FRICTION_BOILER = registerMachineBlock("friction_boiler", () -> new BlockFrictionBoiler(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> STEAM_TURBINE = registerMachineBlock("steam_turbine", () -> new BlockSteamTurbine(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> LAVA_SMELTORY = registerMachineBlock("lava_smeltory", () -> new BlockBigFurnace(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> PARTICLE = registerMachineBlock("particle", () -> new BlockParticle(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> GRINDSTONE = registerMachineBlock("grindstone", () -> new BlockGrindstone(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> BLOWER = registerMachineBlock("blower", () -> new BlockBlower(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> REFRIGERATOR = registerMachineBlock("refrigerator", () -> new BlockRefrigerator(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> COMPOSTER = registerMachineBlock("composter", () -> new BlockComposter(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> FILLER = registerMachineBlock("filler", () -> new BlockFiller(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> SPILLWAY = registerMachineBlock("spillway", () -> new BlockSpillway(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> BEVEL_GEARS = registerMachineBlock("bevel_gears", () -> new BlockBevelGears(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> SORTER = registerMachineBlock("sorter", () -> new BlockSorter(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> FRICTION_HEATER = registerMachineBlock("friction_heater", () -> new BlockFrictionHeater(BlockBehaviour.Properties.of().strength(5)));

//    public static final RegistryObject<Block> PULSE_JET_FURNACE = registerMachineBlock("pulse_jet_furnace", () -> new BlockPulseJetFurnace(BlockBehaviour.Properties.of().strength(5)));

    public static final RegistryObject<Block> COIL = registerMachineBlock("coil", () -> new BlockCoil(BlockBehaviour.Properties.of().strength(5))); //todo blocks for these 4
    public static final RegistryObject<Block> CVT = registerMachineBlock("cvt", () -> new BlockCVT(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> WORMGEAR = registerMachineBlock("wormgear", () -> new BlockWormGear(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> HIGHGEAR = registerMachineBlock("highgear", () -> new BlockHighGear(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> GRINDER = registerMachineBlock("grinder", () -> new BlockGrinder(BlockBehaviour.Properties.of().strength(5)));


    public static final RegistryObject<Block> HOSE = registerMachineBlock("hose", () -> new BlockHose(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> PIPE = registerMachineBlock("pipe", () -> new BlockPipe(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> FUEL_LINE = registerMachineBlock("fuel_line", () -> new BlockFuelLine(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> VALVE = registerMachineBlock("valve", () -> new BlockValve(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> BYPASS = registerMachineBlock("bypass", () -> new BlockBypass(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> SEPARATION = registerMachineBlock("separation", () -> new BlockSeperation(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> SUCTION = registerMachineBlock("suction", () -> new BlockSuction(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> BEDROCK_PIPE = registerMachineBlock("bedrock_pipe", () -> new BlockBedrockPipe(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> WORKTABLE = registerMachineBlock("worktable", () -> new BlockWorktable(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> VACUUM = registerMachineBlock("vacuum", () -> new BlockVacuum(BlockBehaviour.Properties.of().strength(5)));
    public static final RegistryObject<Block> BLAST_FURNACE = registerMachineBlock("blast_furnace", () -> new BlockBlastFurnace(BlockBehaviour.Properties.of().strength(5)));


    private static final Block.Properties WOOD_PROPERTIES = Block.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD);

    private static <BLOCK extends Block> RegistryObject<BLOCK> register(final String name, final Supplier<BLOCK> blockFactory) {
        return register(name, blockFactory, block -> new BlockItem(block, new Item.Properties()));
    }

    private static <BLOCK extends Block> RegistryObject<BLOCK> register(final String name, final Supplier<BLOCK> blockFactory, final IBlockItemFactory<BLOCK> itemFactory) {
        final RegistryObject<BLOCK> block = BLOCKS.register(name, blockFactory);
        ITEMS.register(name, () -> itemFactory.create(block.get()));

        return block;
    }

    private static <BLOCK extends Block> RegistryObject<BLOCK> registerMachineBlock(final String name, final Supplier<BLOCK> blockFactory) {
        return register(name, blockFactory, block -> new BlockItem(block, new Item.Properties()){
            @Override
            public void initializeClient(Consumer<IClientItemExtensions> consumer) {
                consumer.accept(new IClientItemExtensions() {
                    @Override
                    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                        return new MachineItemRenderer(null, null);
                    }
                });
            }
        });
    }

    @FunctionalInterface
    private interface IBlockItemFactory<BLOCK extends Block> {
        Item create(BLOCK block);
    }
}