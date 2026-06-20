package reika.rotarycraft.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import reika.dragonapi.interfaces.blockentity.HasItemHandler;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.blocks.entity.transmission.BlockGearbox;
import reika.rotarycraft.blockentities.*;
import reika.rotarycraft.blockentities.auxiliary.BlockEntityCoolingFin;
import reika.rotarycraft.blockentities.auxiliary.BlockEntityFurnaceHeater;
import reika.rotarycraft.blockentities.auxiliary.BlockEntityHeater;
import reika.rotarycraft.blockentities.auxiliary.BlockEntityMirror;
import reika.rotarycraft.blockentities.decorative.BlockEntityMusicBox;
import reika.rotarycraft.blockentities.decorative.BlockEntityParticleEmitter;
import reika.rotarycraft.blockentities.engine.*;
import reika.rotarycraft.blockentities.farming.BlockEntityComposter;
import reika.rotarycraft.blockentities.farming.BlockEntityFan;
import reika.rotarycraft.blockentities.farming.BlockEntityMobHarvester;
import reika.rotarycraft.blockentities.level.*;
import reika.rotarycraft.blockentities.piping.*;
import reika.rotarycraft.blockentities.auxiliary.BlockEntityFillingStation;
import reika.rotarycraft.blockentities.processing.BlockEntityGrinder;
import reika.rotarycraft.blockentities.processing.BlockEntityLavaSmeltery;
import reika.rotarycraft.blockentities.processing.BlockEntityPulseFurnace;
import reika.rotarycraft.blockentities.production.*;
import reika.rotarycraft.blockentities.storage.BlockEntityReservoir;
import reika.rotarycraft.blockentities.surveying.BlockEntityCaveFinder;
import reika.rotarycraft.blockentities.transmission.*;
import reika.rotarycraft.blockentities.weaponry.*;
import reika.rotarycraft.modinterface.conversion.BlockEntityBoiler;
import reika.rotarycraft.modinterface.conversion.BlockEntityMagnetEngine;
import reika.rotarycraft.modinterface.conversion.BlockEntitySteam;

public class RotaryBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, RotaryCraft.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityShaft>> WOOD_SHAFT = BLOCK_ENTITIES.register("wood_shaft", () ->
            new BlockEntityType<>((pPos, pState) -> new BlockEntityShaft(MaterialRegistry.WOOD, pPos, pState), RotaryBlocks.WOOD_SHAFT.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityShaft>> STONE_SHAFT = BLOCK_ENTITIES.register("stone_shaft", () ->
            new BlockEntityType<>((pPos, pState) -> new BlockEntityShaft(MaterialRegistry.STONE, pPos, pState), RotaryBlocks.STONE_SHAFT.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityShaft>> HSLA_STEEL_SHAFT = BLOCK_ENTITIES.register("hsla_shaft", () ->
            new BlockEntityType<>((pPos, pState) -> new BlockEntityShaft(MaterialRegistry.STEEL, pPos, pState), RotaryBlocks.HSLA_SHAFT.get(), RotaryBlocks.SHAFT_CROSS.get(), RotaryBlocks.SHAFT_MERGE.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityShaft>> TUNGSTEN_SHAFT = BLOCK_ENTITIES.register("tungsten_shaft", () ->
            new BlockEntityType<>((pPos, pState) -> new BlockEntityShaft(MaterialRegistry.TUNGSTEN, pPos, pState), RotaryBlocks.TUNGSTEN_SHAFT.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityShaft>> DIAMOND_SHAFT = BLOCK_ENTITIES.register("diamond_shaft", () ->
            new BlockEntityType<>((pPos, pState) -> new BlockEntityShaft(MaterialRegistry.DIAMOND, pPos, pState), RotaryBlocks.DIAMOND_SHAFT.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityShaft>> BEDROCK_SHAFT = BLOCK_ENTITIES.register("bedrock_shaft", () ->
            new BlockEntityType<>((pPos, pState) -> new BlockEntityShaft(MaterialRegistry.BEDROCK, pPos, pState), RotaryBlocks.BEDROCK_SHAFT.get()));

    // 1.21.5: register the gearbox BlockEntityType against EVERY gearbox variant block (24 of them),
    // and derive the GearboxType from the placed block at construction time so the BE matches the
    // block's material tier. Previously this only accepted HSLA_GEARBOX_2x and crashed validateBlockState
    // when you placed anything else (e.g. diamond_gearbox_8x).
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityGearbox>> GEARBOX = BLOCK_ENTITIES.register("gearbox", () ->
            new BlockEntityType<>((pos, state) -> {
                GearboxTypes type = state.getBlock() instanceof BlockGearbox bg ? bg.type : GearboxTypes.STEEL;
                return new BlockEntityGearbox(type, pos, state);
            },
            RotaryBlocks.HSLA_GEARBOX_2x.get(), RotaryBlocks.HSLA_GEARBOX_4x.get(), RotaryBlocks.HSLA_GEARBOX_8x.get(), RotaryBlocks.HSLA_GEARBOX_16x.get(),
            RotaryBlocks.WOOD_GEARBOX_2x.get(), RotaryBlocks.WOOD_GEARBOX_4x.get(), RotaryBlocks.WOOD_GEARBOX_8x.get(), RotaryBlocks.WOOD_GEARBOX_16x.get(),
            RotaryBlocks.STONE_GEARBOX_2x.get(), RotaryBlocks.STONE_GEARBOX_4x.get(), RotaryBlocks.STONE_GEARBOX_8x.get(), RotaryBlocks.STONE_GEARBOX_16x.get(),
            RotaryBlocks.TUNGSTEN_GEARBOX_2x.get(), RotaryBlocks.TUNGSTEN_GEARBOX_4x.get(), RotaryBlocks.TUNGSTEN_GEARBOX_8x.get(), RotaryBlocks.TUNGSTEN_GEARBOX_16x.get(),
            RotaryBlocks.DIAMOND_GEARBOX_2x.get(), RotaryBlocks.DIAMOND_GEARBOX_4x.get(), RotaryBlocks.DIAMOND_GEARBOX_8x.get(), RotaryBlocks.DIAMOND_GEARBOX_16x.get(),
            RotaryBlocks.BEDROCK_GEARBOX_2x.get(), RotaryBlocks.BEDROCK_GEARBOX_4x.get(), RotaryBlocks.BEDROCK_GEARBOX_8x.get(), RotaryBlocks.BEDROCK_GEARBOX_16x.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityMicroturbine>> MICROTURBINE = BLOCK_ENTITIES.register("microturbine", () ->
            new BlockEntityType<>(BlockEntityMicroturbine::new, RotaryBlocks.MICRO_TURBINE.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityWorktable>> WORKTABLE = BLOCK_ENTITIES.register("worktable", () ->
            new BlockEntityType<>(BlockEntityWorktable::new, RotaryBlocks.WORKTABLE.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityPerformanceEngine>> PERFORMANCE_ENGINE = BLOCK_ENTITIES.register("performance_engine", () ->
            new BlockEntityType<>(BlockEntityPerformanceEngine::new, RotaryBlocks.PERFORMANCE_ENGINE.get()));


    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntitySteamEngine>> STEAM_ENGINE = BLOCK_ENTITIES.register("steam_engine", () ->
            new BlockEntityType<>(BlockEntitySteamEngine::new, RotaryBlocks.STEAM_ENGINE.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityWindEngine>> WIND_ENGINE = BLOCK_ENTITIES.register("wind_engine", () ->
            new BlockEntityType<>(BlockEntityWindEngine::new, RotaryBlocks.WIND_ENGINE.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityHydroEngine>> HYDRO_ENGINE = BLOCK_ENTITIES.register("hydro_engine", () ->
            new BlockEntityType<>(BlockEntityHydroEngine::new, RotaryBlocks.HYDRO_ENGINE.get()));


    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityMobHarvester>> MOB_HARVESTER = BLOCK_ENTITIES.register("mob_harvester", () ->
            new BlockEntityType<>(BlockEntityMobHarvester::new, RotaryBlocks.MOB_HARVESTER.get()));


    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityDecoTank>> DECO_TANK = BLOCK_ENTITIES.register("deco_tank", () ->
            new BlockEntityType<>(BlockEntityDecoTank::new, RotaryBlocks.DECOTANK.get()));


    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntitySplitter>> SPLITTER = BLOCK_ENTITIES.register("splitter", () ->
            new BlockEntityType<>(BlockEntitySplitter::new, RotaryBlocks.SPLITTER.get()));


    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntitySmokeDetector>> SMOKE_DETECTOR = BLOCK_ENTITIES.register("smoke_detector", () ->
            new BlockEntityType<>(BlockEntitySmokeDetector::new, RotaryBlocks.SMOKE_DETECTOR.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityClutch>> CLUTCH = BLOCK_ENTITIES.register("clutch", () ->
            new BlockEntityType<>(BlockEntityClutch::new, RotaryBlocks.CLUTCH.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityVacuum>> VACUUM = BLOCK_ENTITIES.register("vacuum", () ->
            new BlockEntityType<>(BlockEntityVacuum::new, RotaryBlocks.VACUUM.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityDistributionClutch>> DISTRIBUTION_CLUTCH = BLOCK_ENTITIES.register("distribution_clutch", () ->
            new BlockEntityType<>(BlockEntityDistributionClutch::new, RotaryBlocks.DISTRIBUTION_CLUTCH.get()));

    // 1.21.5: previously incorrectly pointed at DISTRIBUTION_CLUTCH; flywheels need their own
    // five-variant set of blocks. The factory still ignores the type since BlockEntityFlywheel
    // looks it up from the block when first ticked.
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityFlywheel>> FLYWHEEL = BLOCK_ENTITIES.register("flywheel", () ->
            new BlockEntityType<>(BlockEntityFlywheel::new,
                    RotaryBlocks.WOOD_FLYWHEEL.get(), RotaryBlocks.HSLA_FLYWHEEL.get(),
                    RotaryBlocks.TUNGSTEN_FLYWHEEL.get(), RotaryBlocks.DIAMOND_FLYWHEEL.get(),
                    RotaryBlocks.BEDROCK_FLYWHEEL.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityReservoir>> RESERVOIR = BLOCK_ENTITIES.register("reservoir", () ->
            new BlockEntityType<>(BlockEntityReservoir::new, RotaryBlocks.RESERVOIR.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityPipe>> FLUID_PIPE = BLOCK_ENTITIES.register("fluid_pipe", () ->
            new BlockEntityType<>(BlockEntityPipe::new, RotaryBlocks.FLUID_PIPE.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityHose>> HOSE = BLOCK_ENTITIES.register("hose", () ->
            new BlockEntityType<>(BlockEntityHose::new, RotaryBlocks.HOSE.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntitySeparatorPipe>> SEPARATION = BLOCK_ENTITIES.register("separation", () ->
            new BlockEntityType<>(BlockEntitySeparatorPipe::new, RotaryBlocks.SEPARATION.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityValve>> VALVE = BLOCK_ENTITIES.register("valve", () ->
            new BlockEntityType<>(BlockEntityValve::new, RotaryBlocks.VALVE.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntitySuctionPipe>> SUCTION = BLOCK_ENTITIES.register("suction", () ->
            new BlockEntityType<>(BlockEntitySuctionPipe::new, RotaryBlocks.SUCTION.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityBypass>> BYPASS = BLOCK_ENTITIES.register("bypass", () ->
            new BlockEntityType<>(BlockEntityBypass::new, RotaryBlocks.BYPASS.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityFuelLine>> FUEL_LINE = BLOCK_ENTITIES.register("fuel_line", () ->
            new BlockEntityType<>(BlockEntityFuelLine::new, RotaryBlocks.FUEL_LINE.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityBedrockPipe>> BEDROCK_PIPE = BLOCK_ENTITIES.register("bedrock_pipe", () ->
            new BlockEntityType<>(BlockEntityBedrockPipe::new, RotaryBlocks.BEDROCK_PIPE.get()));


    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityGasEngine>> GAS_ENGINE = BLOCK_ENTITIES.register("gas_engine", () ->
            new BlockEntityType<>(BlockEntityGasEngine::new, RotaryBlocks.GAS_ENGINE.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityDCEngine>> DC_ENGINE = BLOCK_ENTITIES.register("dc_engine", () ->
            new BlockEntityType<>(BlockEntityDCEngine::new, RotaryBlocks.DC_ENGINE.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<reika.rotarycraft.blockentities.engine.BlockEntityJetEngine>> JET_ENGINE = BLOCK_ENTITIES.register("jet_engine", () ->
            new BlockEntityType<>(reika.rotarycraft.blockentities.engine.BlockEntityJetEngine::new, RotaryBlocks.JET_ENGINE.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityPump>> PUMP = BLOCK_ENTITIES.register("pump", () ->
            new BlockEntityType<>(BlockEntityPump::new, RotaryBlocks.PUMP.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityAerosolizer>> AEROSOLIZER = BLOCK_ENTITIES.register("aerosolizer", () ->
            new BlockEntityType<>(BlockEntityAerosolizer::new, RotaryBlocks.AEROSOLIZER.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityWinder>> WINDER = BLOCK_ENTITIES.register("winder", () ->
            new BlockEntityType<>(BlockEntityWinder::new, RotaryBlocks.WINDER.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityFloodlight>> FLOODLIGHT = BLOCK_ENTITIES.register("floodlight", () ->
            new BlockEntityType<>(BlockEntityFloodlight::new, RotaryBlocks.FLOODLIGHT.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityMonitor>> DYNAMOMETER = BLOCK_ENTITIES.register("dynamometer", () ->
            new BlockEntityType<>(BlockEntityMonitor::new, RotaryBlocks.DYNAMOMETER.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityMirror>> MIRROR = BLOCK_ENTITIES.register("mirror", () ->
            new BlockEntityType<>(BlockEntityMirror::new, RotaryBlocks.MIRROR.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityVanDeGraff>> VAN_DE_GRAFF = BLOCK_ENTITIES.register("van_de_graff", () ->
            new BlockEntityType<>(BlockEntityVanDeGraff::new, RotaryBlocks.VAN_DE_GRAFF.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityLandmine>> LANDMINE = BLOCK_ENTITIES.register("landmine", () ->
            new BlockEntityType<>(BlockEntityLandmine::new, RotaryBlocks.LANDMINE.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityACEngine>> AC_ENGINE = BLOCK_ENTITIES.register("ac_engine", () ->
            new BlockEntityType<>(BlockEntityACEngine::new, RotaryBlocks.AC_ENGINE.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityCoolingFin>> COOLING_FIN = BLOCK_ENTITIES.register("cooling_fin", () ->
            new BlockEntityType<>(BlockEntityCoolingFin::new, RotaryBlocks.COOLING_FIN.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityMusicBox>> MUSIC_BOX = BLOCK_ENTITIES.register("music_box", () ->
            new BlockEntityType<>(BlockEntityMusicBox::new, RotaryBlocks.MUSIC_BOX.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityMagnetEngine>> MAGNETOSTATIC_ENGINE = BLOCK_ENTITIES.register("magnetostatic_engine", () ->
            new BlockEntityType<>(BlockEntityMagnetEngine::new, RotaryBlocks.MAGNETOSTATIC_ENGINE.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntitySolarTower>> SOLAR_TOWER = BLOCK_ENTITIES.register("solar_tower", () ->
            new BlockEntityType<>(BlockEntitySolarTower::new, RotaryBlocks.SOLAR_TOWER.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityAdvancedGear>> CVT = BLOCK_ENTITIES.register("cvt", () ->
            new BlockEntityType<>((pos, block) -> new BlockEntityAdvancedGear(BlockEntityAdvancedGear.GearType.CVT, pos, block), RotaryBlocks.CVT.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityAdvancedGear>> WORMGEAR = BLOCK_ENTITIES.register("wormgear", () ->
            new BlockEntityType<>((pos, block) -> new BlockEntityAdvancedGear(BlockEntityAdvancedGear.GearType.WORM, pos, block), RotaryBlocks.WORMGEAR.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityAdvancedGear>> COIL = BLOCK_ENTITIES.register("coil", () ->
            new BlockEntityType<>((pos, block) -> new BlockEntityAdvancedGear(BlockEntityAdvancedGear.GearType.COIL, pos, block), RotaryBlocks.COIL.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityAdvancedGear>> HIGHGEAR = BLOCK_ENTITIES.register("highgear", () ->
            new BlockEntityType<>((pos, block) -> new BlockEntityAdvancedGear(BlockEntityAdvancedGear.GearType.HIGH, pos, block), RotaryBlocks.HIGHGEAR.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityHeatRay>> HEAT_RAY = BLOCK_ENTITIES.register("heat_ray", () ->
            new BlockEntityType<>(BlockEntityHeatRay::new, RotaryBlocks.HEAT_RAY.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityObsidianMaker>> OBSIDIAN_MAKER = BLOCK_ENTITIES.register("obsidian_maker", () ->
            new BlockEntityType<>(BlockEntityObsidianMaker::new, RotaryBlocks.OBSIDIAN_MAKER.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityPlayerDetector>> PLAYER_DETECTOR = BLOCK_ENTITIES.register("player_detector", () ->
            new BlockEntityType<>(BlockEntityPlayerDetector::new, RotaryBlocks.PLAYER_DETECTOR.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityHeater>> HEATER = BLOCK_ENTITIES.register("heater", () ->
            new BlockEntityType<>(BlockEntityHeater::new, RotaryBlocks.HEATER.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityItemCannon>> ITEM_CANNON = BLOCK_ENTITIES.register("item_cannon", () ->
            new BlockEntityType<>(BlockEntityItemCannon::new, RotaryBlocks.ITEM_CANNON.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityCaveFinder>> CAVE_SCANNER = BLOCK_ENTITIES.register("cave_scanner", () ->
            new BlockEntityType<>(BlockEntityCaveFinder::new, RotaryBlocks.CAVE_SCANNER.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityBlockCannon>> BLOCK_CANNON = BLOCK_ENTITIES.register("block_cannon", () ->
            new BlockEntityType<>(BlockEntityBlockCannon::new, RotaryBlocks.BLOCK_CANNON.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityItemRefresher>> REFRESHER = BLOCK_ENTITIES.register("refresher", () ->
            new BlockEntityType<>(BlockEntityItemRefresher::new, RotaryBlocks.REFRESHER.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntitySpiller>> SPILLER = BLOCK_ENTITIES.register("spiller", () ->
            new BlockEntityType<>(BlockEntitySpiller::new, RotaryBlocks.SPILLER.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityTNTCannon>> TNT_CANNON = BLOCK_ENTITIES.register("tnt_cannon", () ->
            new BlockEntityType<>(BlockEntityTNTCannon::new, RotaryBlocks.TNT_CANNON.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityContainment>> CONTAINMENT = BLOCK_ENTITIES.register("containment", () ->
            new BlockEntityType<>(BlockEntityContainment::new, RotaryBlocks.CONTAINMENT.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityBucketFiller>> BUCKET_FILLER = BLOCK_ENTITIES.register("bucket_filler", () ->
            new BlockEntityType<>(BlockEntityBucketFiller::new, RotaryBlocks.BUCKET_FILLER.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntitySelfDestruct>> SELF_DESTRUCT = BLOCK_ENTITIES.register("self_destruct", () ->
            new BlockEntityType<>(BlockEntitySelfDestruct::new, RotaryBlocks.SELF_DESTRUCT.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityLineBuilder>> LINE_BUILDER = BLOCK_ENTITIES.register("line_builder", () ->
            new BlockEntityType<>(BlockEntityLineBuilder::new, RotaryBlocks.LINE_BUILDER.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityBeamMirror>> BEAM_MIRROR = BLOCK_ENTITIES.register("beam_mirror", () ->
            new BlockEntityType<>(BlockEntityBeamMirror::new, RotaryBlocks.BEAM_MIRROR.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityMultiClutch>> MULTI_CLUTCH = BLOCK_ENTITIES.register("multi_clutch", () ->
            new BlockEntityType<>(BlockEntityMultiClutch::new, RotaryBlocks.MULTI_CLUTCH.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityBoiler>> FRICTION_BOILER = BLOCK_ENTITIES.register("friction_boiler", () ->
            new BlockEntityType<>(BlockEntityBoiler::new, RotaryBlocks.FRICTION_BOILER.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntitySteam>> STEAM_TURBINE = BLOCK_ENTITIES.register("steam_turbine", () ->
            new BlockEntityType<>(BlockEntitySteam::new, RotaryBlocks.STEAM_TURBINE.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityLavaSmeltery>> BIG_FURNACE = BLOCK_ENTITIES.register("big_furnace", () ->
            new BlockEntityType<>(BlockEntityLavaSmeltery::new, RotaryBlocks.LAVA_SMELTORY.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityParticleEmitter>> PARTICLE = BLOCK_ENTITIES.register("particle", () ->
            new BlockEntityType<>(BlockEntityParticleEmitter::new, RotaryBlocks.PARTICLE.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityGrindstone>> GRINDSTONE = BLOCK_ENTITIES.register("grindstone", () ->
            new BlockEntityType<>(BlockEntityGrindstone::new, RotaryBlocks.GRINDSTONE.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityBlower>> BLOWER = BLOCK_ENTITIES.register("blower", () ->
            new BlockEntityType<>(BlockEntityBlower::new, RotaryBlocks.BLOWER.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityRefrigerator>> REFRIGERATOR = BLOCK_ENTITIES.register("refrigerator", () ->
            new BlockEntityType<>(BlockEntityRefrigerator::new, RotaryBlocks.REFRIGERATOR.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityComposter>> COMPOSTER = BLOCK_ENTITIES.register("composter", () ->
            new BlockEntityType<>(BlockEntityComposter::new, RotaryBlocks.COMPOSTER.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityBlockFiller>> FILLER = BLOCK_ENTITIES.register("filler", () ->
            new BlockEntityType<>(BlockEntityBlockFiller::new, RotaryBlocks.FILLER.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntitySpillway>> SPILLWAY = BLOCK_ENTITIES.register("spillway", () ->
            new BlockEntityType<>(BlockEntitySpillway::new, RotaryBlocks.SPILLWAY.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityBevelGear>> BEVEL_GEARS = BLOCK_ENTITIES.register("bevel_gears", () ->
            new BlockEntityType<>(BlockEntityBevelGear::new, RotaryBlocks.BEVEL_GEARS.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntitySorting>> SORTER = BLOCK_ENTITIES.register("sorter", () ->
            new BlockEntityType<>(BlockEntitySorting::new, RotaryBlocks.SORTER.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityFurnaceHeater>> FRICTION_HEATER = BLOCK_ENTITIES.register("friction_heater", () ->
            new BlockEntityType<>(BlockEntityFurnaceHeater::new, RotaryBlocks.FRICTION_HEATER.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityPulseFurnace>> PULSE_JET_FURNACE = BLOCK_ENTITIES.register("pulse_jet_furnace", () ->
            new BlockEntityType<>(BlockEntityPulseFurnace::new, RotaryBlocks.PULSE_JET_FURNACE.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityFillingStation>> FILLING_STATION = BLOCK_ENTITIES.register("filling_station", () ->
            new BlockEntityType<>(reika.rotarycraft.blockentities.auxiliary.BlockEntityFillingStation::new, RotaryBlocks.FILLING_STATION.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityFan>> FAN = BLOCK_ENTITIES.register("fan", () ->
            new BlockEntityType<>(BlockEntityFan::new, RotaryBlocks.FAN.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityGrinder>> GRINDER = BLOCK_ENTITIES.register("grinder", () ->
            new BlockEntityType<>(BlockEntityGrinder::new, RotaryBlocks.GRINDER.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<reika.rotarycraft.blockentities.production.BlockEntityFractionator>> FRACTIONATOR = BLOCK_ENTITIES.register("fractionator", () ->
            new BlockEntityType<>(reika.rotarycraft.blockentities.production.BlockEntityFractionator::new, RotaryBlocks.FRACTIONATOR.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<reika.rotarycraft.blockentities.processing.BlockEntityMagnetizer>> MAGNETIZER = BLOCK_ENTITIES.register("magnetizer", () ->
            new BlockEntityType<>(reika.rotarycraft.blockentities.processing.BlockEntityMagnetizer::new, RotaryBlocks.MAGNETIZER.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityBlastFurnace>> BLAST_FURNACE = BLOCK_ENTITIES.register("blast_furnace", () ->
            new BlockEntityType<>(BlockEntityBlastFurnace::new, RotaryBlocks.BLAST_FURNACE.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<reika.rotarycraft.blockentities.production.BlockEntityFermenter>> FERMENTER = BLOCK_ENTITIES.register("fermenter", () ->
            new BlockEntityType<>(reika.rotarycraft.blockentities.production.BlockEntityFermenter::new, RotaryBlocks.FERMENTER.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<reika.rotarycraft.blockentities.processing.BlockEntityExtractor>> EXTRACTOR = BLOCK_ENTITIES.register("extractor", () ->
            new BlockEntityType<>(reika.rotarycraft.blockentities.processing.BlockEntityExtractor::new, RotaryBlocks.EXTRACTOR.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<reika.rotarycraft.blockentities.production.BlockEntityBedrockBreaker>> BEDROCK_BREAKER = BLOCK_ENTITIES.register("bedrock_breaker", () ->
            new BlockEntityType<>(reika.rotarycraft.blockentities.production.BlockEntityBedrockBreaker::new, RotaryBlocks.BEDROCK_BREAKER.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<reika.rotarycraft.blockentities.production.BlockEntityBedrockSlice>> BEDROCK_SLICE = BLOCK_ENTITIES.register("bedrock_slice", () ->
            new BlockEntityType<>(reika.rotarycraft.blockentities.production.BlockEntityBedrockSlice::new, RotaryBlocks.BEDROCKSLICE.get()));


    /**
     * Expose machine inventories as the standard NeoForge item capability. Reika BEs carry their
     * inventory as a {@link reika.dragonapi.instantiable.resources.ManagedItemHandler} (already a
     * {@code ResourceHandler<ItemResource>}) reachable via {@link HasItemHandler#getItemHandler()},
     * but that wasn't registered as {@code Capabilities.Item.BLOCK} — so hoppers/pipes and
     * code-driven testing couldn't reach it. Registered for every RotaryCraft BE type; the
     * provider returns the handler only for those that actually carry one.
     */
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (DeferredHolder<BlockEntityType<?>, ? extends BlockEntityType<?>> holder : BLOCK_ENTITIES.getEntries()) {
            registerItemCap(event, holder.get());
        }
    }

    private static <T extends BlockEntity> void registerItemCap(
            RegisterCapabilitiesEvent event, BlockEntityType<T> type) {
        event.registerBlockEntity(
                Capabilities.Item.BLOCK, type,
                (be, ctx) -> be instanceof HasItemHandler h ? h.getItemHandler() : null);
    }
}
