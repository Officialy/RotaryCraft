package reika.rotarycraft.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.blocks.CanolaBlock;
import reika.rotarycraft.base.blocks.entity.*;
import reika.rotarycraft.base.blocks.entity.engine.*;
import reika.rotarycraft.base.blocks.entity.pipe.*;
import reika.rotarycraft.base.blocks.entity.transmission.*;
import reika.rotarycraft.blockentities.transmission.BlockEntityShaft;

import java.util.function.Supplier;

public class RotaryBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(RotaryCraft.MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RotaryCraft.MODID);

    public static final DeferredBlock<Block> GPR = registerMachineBlock("gpr", () -> new Block(blockProperties().strength(20)));
    public static final DeferredBlock<Block> WOOD_FLYWHEEL = registerMachineBlock("wood_flywheel", () -> new BlockGearbox(GearboxTypes.WOOD, blockProperties().strength(20)));
    public static final DeferredBlock<Block> HSLA_FLYWHEEL = registerMachineBlock("hsla_flywheel", () -> new BlockGearbox(GearboxTypes.STEEL, blockProperties().strength(20)));
    public static final DeferredBlock<Block> TUNGSTEN_FLYWHEEL = registerMachineBlock("tungsten_flywheel", () -> new BlockGearbox(GearboxTypes.TUNGSTEN, blockProperties().strength(20)));
    public static final DeferredBlock<Block> DIAMOND_FLYWHEEL = registerMachineBlock("diamond_flywheel", () -> new BlockGearbox(GearboxTypes.DIAMOND, blockProperties().strength(20)));
    public static final DeferredBlock<Block> BEDROCK_FLYWHEEL = registerMachineBlock("bedrock_flywheel", () -> new BlockGearbox(GearboxTypes.BEDROCK, blockProperties().strength(20)));

    public static final DeferredBlock<Block> HSLA_GEARBOX_2x = registerMachineBlock("hsla_gearbox_2x", () -> new BlockGearbox(GearboxTypes.STEEL, blockProperties().strength(20)));
    public static final DeferredBlock<Block> HSLA_GEARBOX_4x = registerMachineBlock("hsla_gearbox_4x", () -> new BlockGearbox(GearboxTypes.STEEL, blockProperties().strength(20)));
    public static final DeferredBlock<Block> HSLA_GEARBOX_8x = registerMachineBlock("hsla_gearbox_8x", () -> new BlockGearbox(GearboxTypes.STEEL, blockProperties().strength(20)));
    public static final DeferredBlock<Block> HSLA_GEARBOX_16x = registerMachineBlock("hsla_gearbox_16x", () -> new BlockGearbox(GearboxTypes.STEEL, blockProperties().strength(20)));
    public static final DeferredBlock<Block> WOOD_GEARBOX_2x = registerMachineBlock("wood_gearbox_2x", () -> new BlockGearbox(GearboxTypes.STEEL, blockProperties().strength(20)));
    public static final DeferredBlock<Block> WOOD_GEARBOX_4x = registerMachineBlock("wood_gearbox_4x", () -> new BlockGearbox(GearboxTypes.STEEL, blockProperties().strength(20)));
    public static final DeferredBlock<Block> WOOD_GEARBOX_8x = registerMachineBlock("wood_gearbox_8x", () -> new BlockGearbox(GearboxTypes.STEEL, blockProperties().strength(20)));
    public static final DeferredBlock<Block> WOOD_GEARBOX_16x = registerMachineBlock("wood_gearbox_16x", () -> new BlockGearbox(GearboxTypes.STEEL, blockProperties().strength(20)));
    public static final DeferredBlock<Block> STONE_GEARBOX_2x = registerMachineBlock("stone_gearbox_2x", () -> new BlockGearbox(GearboxTypes.STEEL, blockProperties().strength(20)));
    public static final DeferredBlock<Block> STONE_GEARBOX_4x = registerMachineBlock("stone_gearbox_4x", () -> new BlockGearbox(GearboxTypes.STEEL, blockProperties().strength(20)));
    public static final DeferredBlock<Block> STONE_GEARBOX_8x = registerMachineBlock("stone_gearbox_8x", () -> new BlockGearbox(GearboxTypes.STEEL, blockProperties().strength(20)));
    public static final DeferredBlock<Block> STONE_GEARBOX_16x = registerMachineBlock("stone_gearbox_16x", () -> new BlockGearbox(GearboxTypes.STEEL, blockProperties().strength(20)));
    public static final DeferredBlock<Block> TUNGSTEN_GEARBOX_2x = registerMachineBlock("tungsten_gearbox_2x", () -> new BlockGearbox(GearboxTypes.TUNGSTEN, blockProperties().strength(20)));
    public static final DeferredBlock<Block> TUNGSTEN_GEARBOX_4x = registerMachineBlock("tungsten_gearbox_4x", () -> new BlockGearbox(GearboxTypes.TUNGSTEN, blockProperties().strength(20)));
    public static final DeferredBlock<Block> TUNGSTEN_GEARBOX_8x = registerMachineBlock("tungsten_gearbox_8x", () -> new BlockGearbox(GearboxTypes.TUNGSTEN, blockProperties().strength(20)));
    public static final DeferredBlock<Block> TUNGSTEN_GEARBOX_16x = registerMachineBlock("tungsten_gearbox_16x", () -> new BlockGearbox(GearboxTypes.TUNGSTEN, blockProperties().strength(20)));
    public static final DeferredBlock<Block> DIAMOND_GEARBOX_2x = registerMachineBlock("diamond_gearbox_2x", () -> new BlockGearbox(GearboxTypes.DIAMOND, blockProperties().strength(20)));
    public static final DeferredBlock<Block> DIAMOND_GEARBOX_4x = registerMachineBlock("diamond_gearbox_4x", () -> new BlockGearbox(GearboxTypes.DIAMOND, blockProperties().strength(20)));
    public static final DeferredBlock<Block> DIAMOND_GEARBOX_8x = registerMachineBlock("diamond_gearbox_8x", () -> new BlockGearbox(GearboxTypes.DIAMOND, blockProperties().strength(20)));
    public static final DeferredBlock<Block> DIAMOND_GEARBOX_16x = registerMachineBlock("diamond_gearbox_16x", () -> new BlockGearbox(GearboxTypes.DIAMOND, blockProperties().strength(20)));
    public static final DeferredBlock<Block> BEDROCK_GEARBOX_2x = registerMachineBlock("bedrock_gearbox_2x", () -> new BlockGearbox(GearboxTypes.BEDROCK, blockProperties().strength(20)));
    public static final DeferredBlock<Block> BEDROCK_GEARBOX_4x = registerMachineBlock("bedrock_gearbox_4x", () -> new BlockGearbox(GearboxTypes.BEDROCK, blockProperties().strength(20)));
    public static final DeferredBlock<Block> BEDROCK_GEARBOX_8x = registerMachineBlock("bedrock_gearbox_8x", () -> new BlockGearbox(GearboxTypes.BEDROCK, blockProperties().strength(20)));
    public static final DeferredBlock<Block> BEDROCK_GEARBOX_16x = registerMachineBlock("bedrock_gearbox_16x", () -> new BlockGearbox(GearboxTypes.BEDROCK, blockProperties().strength(20)));
    //    public static final DeferredBlock<Block> FILLING_STATION = register("filling_station", () -> new BlockFillingStation(blockProperties().strength(20)));
    public static final DeferredBlock<Block> WOOD_SHAFT = registerMachineBlock("wood_shaft", () -> new BlockShaft(MaterialRegistry.WOOD, blockProperties().strength(20)));
    public static final DeferredBlock<Block> STONE_SHAFT = registerMachineBlock("stone_shaft", () -> new BlockShaft(MaterialRegistry.STONE, blockProperties().strength(20)));
    public static final DeferredBlock<Block> HSLA_SHAFT = registerMachineBlock("hsla_shaft", () -> new BlockShaft(MaterialRegistry.STEEL, blockProperties().strength(20)));
    public static final DeferredBlock<Block> HSLA_STEEL_BLOCK = register("hsla_steel_block", () -> new Block(blockProperties().strength(20)));
    public static final DeferredBlock<Block> TUNGSTEN_SHAFT = registerMachineBlock("tungsten_shaft", () -> new BlockShaft(MaterialRegistry.TUNGSTEN, blockProperties().strength(20)));
    public static final DeferredBlock<Block> DIAMOND_SHAFT = registerMachineBlock("diamond_shaft", () -> new BlockShaft(MaterialRegistry.DIAMOND, blockProperties().strength(20)));
    public static final DeferredBlock<Block> BEDROCK_SHAFT = registerMachineBlock("bedrock_shaft", () -> new BlockShaft(MaterialRegistry.BEDROCK, blockProperties().strength(20)));
    public static final DeferredBlock<Block> FAN = registerMachineBlock("fan", () -> new BlockFan(blockProperties().strength(20)));
    public static final DeferredBlock<Block> TRANS = register("trans", () -> new Block(blockProperties().strength(20)));
    public static final DeferredBlock<Block> SOLAR_TOWER = registerMachineBlock("solar_tower", () -> new BlockSolarTower(blockProperties().strength(20)));
    public static final DeferredBlock<Block> BCENGINE = register("bcengine", () -> new Block(blockProperties().strength(20)));
    public static final DeferredBlock<Block> DECO = register("deco", () -> new Block(blockProperties().strength(20)));
    public static final DeferredBlock<Block> CANOLA = registerBlockOnly("canola", () -> new CanolaBlock(blockProperties().noCollision().randomTicks().instabreak().sound(SoundType.CROP)));
    public static final DeferredBlock<Block> BEAM = register("beam", () -> new Block(blockProperties().strength(20)));
    public static final DeferredBlock<Block> BRIDGE = register("bridge", () -> new Block(blockProperties().strength(20)));
    public static final DeferredBlock<Block> MININGPIPE = register("miningpipe", () -> new Block(blockProperties().strength(20)));
    public static final DeferredBlock<Block> BLASTGLASS = register("blastglass", () -> new HalfTransparentBlock(blockProperties().strength(20)));
    public static final DeferredBlock<Block> BLASTPANE = register("blastpane", () -> new StainedGlassPaneBlock(DyeColor.BLACK, blockProperties().strength(20)));
    public static final DeferredBlock<Block> BEDROCKSLICE = registerBlockOnly("bedrockslice", () -> new reika.rotarycraft.base.blocks.entity.BlockBedrockSlice(blockProperties().strength(-1, 3600000)));
    public static final DeferredBlock<Block> BEDROCK_BREAKER = registerMachineBlock("bedrock_breaker", () -> new reika.rotarycraft.base.blocks.entity.BlockBedrockBreaker(blockProperties().strength(20)));
    public static final DeferredBlock<Block> DECOTANK = register("decotank", () -> new Block(blockProperties().strength(20)));

    public static final DeferredBlock<Block> ANTHRA = register("anthra", () -> new Block(blockProperties().strength(5)));
    public static final DeferredBlock<Block> LONS = register("lons", () -> new Block(blockProperties().strength(5)));
    public static final DeferredBlock<Block> SHIELD = register("shield", () -> new Block(blockProperties().strength(5)));
    public static final DeferredBlock<Block> BEDROCK = registerBlockOnly("bedrock", () -> new Block(blockProperties().strength(-1)));
    public static final DeferredBlock<Block> COKE = register("coke_block", () -> new Block(blockProperties().strength(5)));
    public static final DeferredBlock<Block> SHAFT_CROSS = registerMachineBlock("cross", () -> new BlockShaftSpecial(MaterialRegistry.STEEL, blockProperties().strength(5), BlockEntityShaft.ShaftType.CROSS));
    public static final DeferredBlock<Block> SHAFT_MERGE = registerMachineBlock("merge", () -> new BlockShaftSpecial(MaterialRegistry.STEEL, blockProperties().strength(5), BlockEntityShaft.ShaftType.MERGE));
    public static final DeferredBlock<Block> SPLITTER = registerMachineBlock("splitter", () -> new BlockSplitter(blockProperties().strength(5)));
    public static final DeferredBlock<Block> SMOKE_DETECTOR = registerMachineBlock("smoke_detector", () -> new BlockSmokeDetector(blockProperties().strength(5)));
    public static final DeferredBlock<Block> DISTRIBUTION_CLUTCH = registerMachineBlock("distribution_clutch", () -> new BlockDistributionClutch(blockProperties().strength(5)));
    public static final DeferredBlock<Block> CLUTCH = registerMachineBlock("clutch", () -> new BlockClutch(blockProperties().strength(5)));

    public static final DeferredBlock<Block> MOB_HARVESTER = registerMachineBlock("mob_harvester", () -> new BlockSmokeDetector(blockProperties().strength(5)));
    public static final DeferredBlock<Block> RESERVOIR = registerMachineBlock("reservoir", () -> new BlockReservoir(blockProperties().strength(5)));
    //Engines
    public static final DeferredBlock<Block> WIND_ENGINE = registerMachineBlock("wind_engine", () -> new BlockWindEngine(blockProperties().strength(5)));
    public static final DeferredBlock<Block> STEAM_ENGINE = registerMachineBlock("steam_engine", () -> new BlockSteamEngine(blockProperties().strength(5)));
    public static final DeferredBlock<Block> PERFORMANCE_ENGINE = registerMachineBlock("performance_engine", () -> new BlockPerformanceEngine(blockProperties().strength(5)));
    public static final DeferredBlock<Block> MICRO_TURBINE = registerMachineBlock("microturbine", () -> new BlockMicroturbine(blockProperties().strength(5)));
    public static final DeferredBlock<Block> GAS_ENGINE = registerMachineBlock("gas_engine", () -> new BlockGasEngine(blockProperties().strength(5)));
    public static final DeferredBlock<Block> DC_ENGINE = registerMachineBlock("dc_engine", () -> new BlockDCEngine(blockProperties().strength(5)));
    public static final DeferredBlock<Block> MAGNETOSTATIC_ENGINE = registerMachineBlock("magnetostatic_engine", () -> new BlockMagnetEngine(blockProperties().strength(5)));
    public static final DeferredBlock<Block> AC_ENGINE = registerMachineBlock("ac_engine", () -> new BlockAcEngine(blockProperties().strength(5)));
    public static final DeferredBlock<Block> JET_ENGINE = registerMachineBlock("jet_engine", () -> new reika.rotarycraft.base.blocks.entity.engine.BlockJetEngine(blockProperties().strength(5)));


    public static final DeferredBlock<Block> PUMP = registerMachineBlock("pump", () -> new BlockPump(blockProperties().strength(5)));
    public static final DeferredBlock<Block> AEROSOLIZER = registerMachineBlock("aerosolizer", () -> new BlockAerosolizer(blockProperties().strength(5)));
    public static final DeferredBlock<Block> WINDER = registerMachineBlock("winder", () -> new BlockAerosolizer(blockProperties().strength(5)));
    public static final DeferredBlock<Block> FLOODLIGHT = registerMachineBlock("floodlight", () -> new BlockFloodlight(blockProperties().strength(5)));
    public static final DeferredBlock<Block> DYNAMOMETER = registerMachineBlock("dynamometer", () -> new BlockDynamometer(blockProperties().strength(5)));
    public static final DeferredBlock<Block> MIRROR = registerMachineBlock("mirror", () -> new BlockMirror(blockProperties().strength(5)));
    public static final DeferredBlock<Block> VAN_DE_GRAFF = registerMachineBlock("van_de_graff", () -> new BlockVanDeGraff(blockProperties().strength(5)));
    public static final DeferredBlock<Block> LANDMINE = registerMachineBlock("landmine", () -> new BlockLandmine(blockProperties().strength(5)));

    public static final DeferredBlock<Block> COOLING_FIN = registerMachineBlock("cooling_fin", () -> new BlockCoolingFin(blockProperties().strength(5)));
    public static final DeferredBlock<Block> MUSIC_BOX = registerMachineBlock("music_box", () -> new BlockMusicBox(blockProperties().strength(5)));
    public static final DeferredBlock<Block> HEAT_RAY = registerMachineBlock("heat_ray", () -> new BlockHeatRay(blockProperties().strength(5)));
    public static final DeferredBlock<Block> OBSIDIAN_MAKER = registerMachineBlock("obsidian_maker", () -> new BlockObsidianMaker(blockProperties().strength(5)));
    public static final DeferredBlock<Block> PLAYER_DETECTOR = registerMachineBlock("player_detector", () -> new BlockPlayerDetector(blockProperties().strength(5)));
    public static final DeferredBlock<Block> HEATER = registerMachineBlock("heater", () -> new BlockHeater(blockProperties().strength(5)));
    public static final DeferredBlock<Block> ITEM_CANNON = registerMachineBlock("item_cannon", () -> new BlockItemCannon(blockProperties().strength(5)));
    public static final DeferredBlock<Block> CAVE_SCANNER = registerMachineBlock("cave_scanner", () -> new BlockCaveScanner(blockProperties().strength(5)));
    public static final DeferredBlock<Block> BLOCK_CANNON = registerMachineBlock("block_cannon", () -> new BlockBlockCannon(blockProperties().strength(5)));
    public static final DeferredBlock<Block> REFRESHER = registerMachineBlock("refresher", () -> new BlockRefresher(blockProperties().strength(5)));
    public static final DeferredBlock<Block> SPILLER = registerMachineBlock("spiller", () -> new BlockSpiller(blockProperties().strength(5)));
    public static final DeferredBlock<Block> TNT_CANNON = registerMachineBlock("tnt_cannon", () -> new BlockTntCannon(blockProperties().strength(5)));
    public static final DeferredBlock<Block> CONTAINMENT = registerMachineBlock("containment", () -> new BlockContainment(blockProperties().strength(5)));
    public static final DeferredBlock<Block> BUCKET_FILLER = registerMachineBlock("bucket_filler", () -> new BlockBucketFiller(blockProperties().strength(5)));
    public static final DeferredBlock<Block> SELF_DESTRUCT = registerMachineBlock("self_destruct", () -> new BlockSelfDestruct(blockProperties().strength(5)));
    public static final DeferredBlock<Block> LINE_BUILDER = registerMachineBlock("line_builder", () -> new BlockLineBuilder(blockProperties().strength(5)));
    public static final DeferredBlock<Block> BEAM_MIRROR = registerMachineBlock("beam_mirror", () -> new BlockBeamMirror(blockProperties().strength(5)));
    public static final DeferredBlock<Block> MULTI_CLUTCH = registerMachineBlock("multi_clutch", () -> new BlockMultiClutch(blockProperties().strength(5)));
    public static final DeferredBlock<Block> FRICTION_BOILER = registerMachineBlock("friction_boiler", () -> new BlockFrictionBoiler(blockProperties().strength(5)));
    public static final DeferredBlock<Block> STEAM_TURBINE = registerMachineBlock("steam_turbine", () -> new BlockSteamTurbine(blockProperties().strength(5)));
    public static final DeferredBlock<Block> LAVA_SMELTORY = registerMachineBlock("lava_smeltory", () -> new BlockBigFurnace(blockProperties().strength(5)));
    public static final DeferredBlock<Block> PARTICLE = registerMachineBlock("particle", () -> new BlockParticle(blockProperties().strength(5)));
    public static final DeferredBlock<Block> GRINDSTONE = registerMachineBlock("grindstone", () -> new BlockGrindstone(blockProperties().strength(5)));
    public static final DeferredBlock<Block> BLOWER = registerMachineBlock("blower", () -> new BlockBlower(blockProperties().strength(5)));
    public static final DeferredBlock<Block> REFRIGERATOR = registerMachineBlock("refrigerator", () -> new BlockRefrigerator(blockProperties().strength(5)));
    public static final DeferredBlock<Block> COMPOSTER = registerMachineBlock("composter", () -> new BlockComposter(blockProperties().strength(5)));
    public static final DeferredBlock<Block> FILLER = registerMachineBlock("filler", () -> new BlockFiller(blockProperties().strength(5)));
    public static final DeferredBlock<Block> SPILLWAY = registerMachineBlock("spillway", () -> new BlockSpillway(blockProperties().strength(5)));
    public static final DeferredBlock<Block> BEVEL_GEARS = registerMachineBlock("bevel_gears", () -> new BlockBevelGears(blockProperties().strength(5)));
    public static final DeferredBlock<Block> SORTER = registerMachineBlock("sorter", () -> new BlockSorter(blockProperties().strength(5)));
    public static final DeferredBlock<Block> FRICTION_HEATER = registerMachineBlock("friction_heater", () -> new BlockFrictionHeater(blockProperties().strength(5)));

//    public static final DeferredBlock<Block> PULSE_JET_FURNACE = registerMachineBlock("pulse_jet_furnace", () -> new BlockPulseJetFurnace(blockProperties().strength(5)));

    public static final DeferredBlock<Block> COIL = registerMachineBlock("coil", () -> new BlockCoil(blockProperties().strength(5))); //todo blocks for these 4
    public static final DeferredBlock<Block> CVT = registerMachineBlock("cvt", () -> new BlockCVT(blockProperties().strength(5)));
    public static final DeferredBlock<Block> WORMGEAR = registerMachineBlock("wormgear", () -> new BlockWormGear(blockProperties().strength(5)));
    public static final DeferredBlock<Block> HIGHGEAR = registerMachineBlock("highgear", () -> new BlockHighGear(blockProperties().strength(5)));
    public static final DeferredBlock<Block> GRINDER = registerMachineBlock("grinder", () -> new BlockGrinder(blockProperties().strength(5)));
    public static final DeferredBlock<Block> FRACTIONATOR = registerMachineBlock("fractionator", () -> new reika.rotarycraft.base.blocks.entity.BlockFractionator(blockProperties().strength(5)));


    public static final DeferredBlock<Block> HOSE = registerMachineBlock("hose", () -> new BlockHose(blockProperties().strength(5)));
    public static final DeferredBlock<Block> FLUID_PIPE = registerMachineBlock("fluid_pipe", () -> new BlockPipe(blockProperties().strength(5)));
    public static final DeferredBlock<Block> FUEL_LINE = registerMachineBlock("fuel_line", () -> new BlockFuelLine(blockProperties().strength(5)));
    public static final DeferredBlock<Block> VALVE = registerMachineBlock("valve", () -> new BlockValve(blockProperties().strength(5)));
    public static final DeferredBlock<Block> BYPASS = registerMachineBlock("bypass", () -> new BlockBypass(blockProperties().strength(5)));
    public static final DeferredBlock<Block> SEPARATION = registerMachineBlock("separation", () -> new BlockSeperation(blockProperties().strength(5)));
    public static final DeferredBlock<Block> SUCTION = registerMachineBlock("suction", () -> new BlockSuction(blockProperties().strength(5)));
    public static final DeferredBlock<Block> BEDROCK_PIPE = registerMachineBlock("bedrock_pipe", () -> new BlockBedrockPipe(blockProperties().strength(5)));
    public static final DeferredBlock<Block> WORKTABLE = registerMachineBlock("worktable", () -> new BlockWorktable(blockProperties().strength(5)));
    public static final DeferredBlock<Block> VACUUM = registerMachineBlock("vacuum", () -> new BlockVacuum(blockProperties().strength(5)));
    public static final DeferredBlock<Block> BLAST_FURNACE = registerMachineBlock("blast_furnace", () -> new BlockBlastFurnace(blockProperties().strength(5)));

    // 1.21.5: Block.Properties / Item.Properties must have setId() called before the Block/Item
    // constructor runs (BlockBehaviour.<init> dereferences props.id in effectiveDrops()). To avoid
    // rewriting 100+ entry lambdas, we stash the ResourceKey in a ThreadLocal while running each
    // entry's factory, and a helper blockProperties() reads it.
    private static final ThreadLocal<ResourceKey<Block>> CURRENT_BLOCK_KEY = new ThreadLocal<>();

    /** Replacement for {@code BlockBehaviour.Properties.of()} inside the entry factories above. */
    public static BlockBehaviour.Properties blockProperties() {
        BlockBehaviour.Properties p = BlockBehaviour.Properties.of();
        ResourceKey<Block> k = CURRENT_BLOCK_KEY.get();
        if (k != null) p.setId(k);
        return p;
    }

    private static final Block.Properties WOOD_PROPERTIES = blockProperties().strength(2.0F, 3.0F).sound(SoundType.WOOD);

    private static <BLOCK extends Block> DeferredBlock<BLOCK> register(final String name, final Supplier<BLOCK> blockFactory) {
        DeferredBlock<BLOCK> block = registerBlockOnly(name, blockFactory);
        ITEMS.registerSimpleBlockItem(block); // sets the BlockItem's id automatically
        return block;
    }

    /**
     * Block-only registration (no auto BlockItem). Sets the ResourceKey threadlocal so
     * {@link #blockProperties()} can populate setId. Package-private so sibling registry
     * classes (e.g. RotaryFluids) can route their fluid-block factories through it.
     */
    static <BLOCK extends Block> DeferredBlock<BLOCK> registerBlockOnly(final String name, final Supplier<BLOCK> blockFactory) {
        return BLOCKS.register(name, rl -> {
            CURRENT_BLOCK_KEY.set(ResourceKey.create(Registries.BLOCK, rl));
            try {
                return blockFactory.get();
            } finally {
                CURRENT_BLOCK_KEY.remove();
            }
        });
    }

    private static <BLOCK extends Block> DeferredBlock<BLOCK> registerMachineBlock(final String name, final Supplier<BLOCK> blockFactory) {
        return register(name, blockFactory);
    }
}