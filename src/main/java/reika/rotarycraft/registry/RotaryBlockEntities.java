package reika.rotarycraft.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.blockentities.*;
import reika.rotarycraft.blockentities.auxiliary.BlockEntityCoolingFin;
import reika.rotarycraft.blockentities.auxiliary.BlockEntityHeater;
import reika.rotarycraft.blockentities.auxiliary.BlockEntityMirror;
import reika.rotarycraft.blockentities.decorative.BlockEntityMusicBox;
import reika.rotarycraft.blockentities.decorative.BlockEntityParticleEmitter;
import reika.rotarycraft.blockentities.engine.*;
import reika.rotarycraft.blockentities.farming.BlockEntityComposter;
import reika.rotarycraft.blockentities.farming.BlockEntityMobHarvester;
import reika.rotarycraft.blockentities.level.*;
import reika.rotarycraft.blockentities.piping.*;
import reika.rotarycraft.blockentities.processing.BlockEntityGrinder;
import reika.rotarycraft.blockentities.processing.BlockEntityLavaSmeltery;
import reika.rotarycraft.blockentities.production.*;
import reika.rotarycraft.blockentities.storage.BlockEntityReservoir;
import reika.rotarycraft.blockentities.surveying.BlockEntityCaveFinder;
import reika.rotarycraft.blockentities.transmission.*;
import reika.rotarycraft.blockentities.weaponry.*;
import reika.rotarycraft.modinterface.conversion.BlockEntityBoiler;
import reika.rotarycraft.modinterface.conversion.BlockEntityMagnetEngine;
import reika.rotarycraft.modinterface.conversion.BlockEntitySteam;

public class RotaryBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, RotaryCraft.MODID);

    public static final RegistryObject<BlockEntityType<BlockEntityShaft>> WOOD_SHAFT = BLOCK_ENTITIES.register("wood_shaft", () ->
            BlockEntityType.Builder.of((pPos, pState) -> new BlockEntityShaft(MaterialRegistry.STEEL, pPos, pState), RotaryBlocks.WOOD_SHAFT.get()).build(null));
    public static final RegistryObject<BlockEntityType<BlockEntityShaft>> STONE_SHAFT = BLOCK_ENTITIES.register("stone_shaft", () ->
            BlockEntityType.Builder.of((pPos, pState) -> new BlockEntityShaft(MaterialRegistry.STONE, pPos, pState), RotaryBlocks.WOOD_SHAFT.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityShaft>> HSLA_STEEL_SHAFT = BLOCK_ENTITIES.register("hsla_shaft", () ->
            BlockEntityType.Builder.of((pPos, pState) -> new BlockEntityShaft(MaterialRegistry.STEEL, pPos, pState), RotaryBlocks.HSLA_SHAFT.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityShaft>> TUNGSTEN_SHAFT = BLOCK_ENTITIES.register("tungsten_shaft", () ->
            BlockEntityType.Builder.of((pPos, pState) -> new BlockEntityShaft(MaterialRegistry.TUNGSTEN, pPos, pState), RotaryBlocks.TUNGSTEN_SHAFT.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityShaft>> DIAMOND_SHAFT = BLOCK_ENTITIES.register("diamond_shaft", () ->
            BlockEntityType.Builder.of((pPos, pState) -> new BlockEntityShaft(MaterialRegistry.DIAMOND, pPos, pState), RotaryBlocks.DIAMOND_SHAFT.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityShaft>> BEDROCK_SHAFT = BLOCK_ENTITIES.register("bedrock_shaft", () ->
            BlockEntityType.Builder.of((pPos, pState) -> new BlockEntityShaft(MaterialRegistry.BEDROCK, pPos, pState), RotaryBlocks.BEDROCK_SHAFT.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityGearbox>> GEARBOX = BLOCK_ENTITIES.register("gearbox", () ->
            BlockEntityType.Builder.of((pPos, pState) -> new BlockEntityGearbox(GearboxTypes.STEEL, pPos, pState), RotaryBlocks.HSLA_GEARBOX_2x.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityMicroturbine>> MICROTURBINE = BLOCK_ENTITIES.register("microturbine", () ->
            BlockEntityType.Builder.of(BlockEntityMicroturbine::new, RotaryBlocks.MICRO_TURBINE.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityPerformanceEngine>> PERFORMANCE_ENGINE = BLOCK_ENTITIES.register("performance_engine", () ->
            BlockEntityType.Builder.of(BlockEntityPerformanceEngine::new, RotaryBlocks.PERFORMANCE_ENGINE.get()).build(null));


    public static final RegistryObject<BlockEntityType<BlockEntitySteamEngine>> STEAM_ENGINE = BLOCK_ENTITIES.register("steam_engine", () ->
            BlockEntityType.Builder.of(BlockEntitySteamEngine::new, RotaryBlocks.STEAM_ENGINE.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityWindEngine>> WIND_ENGINE = BLOCK_ENTITIES.register("wind_engine", () ->
            BlockEntityType.Builder.of(BlockEntityWindEngine::new, RotaryBlocks.WIND_ENGINE.get()).build(null));


    public static final RegistryObject<BlockEntityType<BlockEntityMobHarvester>> MOB_HARVESTER = BLOCK_ENTITIES.register("mob_harvester", () ->
            BlockEntityType.Builder.of(BlockEntityMobHarvester::new, RotaryBlocks.MOB_HARVESTER.get()).build(null));


    public static final RegistryObject<BlockEntityType<BlockEntityDecoTank>> DECO_TANK = BLOCK_ENTITIES.register("deco_tank", () ->
            BlockEntityType.Builder.of(BlockEntityDecoTank::new, RotaryBlocks.DECOTANK.get()).build(null));


    public static final RegistryObject<BlockEntityType<BlockEntitySplitter>> SPLITTER = BLOCK_ENTITIES.register("splitter", () ->
            BlockEntityType.Builder.of(BlockEntitySplitter::new, RotaryBlocks.SPLITTER.get()).build(null));


    public static final RegistryObject<BlockEntityType<BlockEntitySmokeDetector>> SMOKE_DETECTOR = BLOCK_ENTITIES.register("smoke_detector", () ->
            BlockEntityType.Builder.of(BlockEntitySmokeDetector::new, RotaryBlocks.SMOKE_DETECTOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityClutch>> CLUTCH = BLOCK_ENTITIES.register("clutch", () ->
            BlockEntityType.Builder.of(BlockEntityClutch::new, RotaryBlocks.CLUTCH.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityDistributionClutch>> DISTRIBUTION_CLUTCH = BLOCK_ENTITIES.register("distribution_clutch", () ->
            BlockEntityType.Builder.of(BlockEntityDistributionClutch::new, RotaryBlocks.DISTRIBUTION_CLUTCH.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityFlywheel>> FLYWHEEL = BLOCK_ENTITIES.register("flywheel", () ->
            BlockEntityType.Builder.of(BlockEntityFlywheel::new, RotaryBlocks.DISTRIBUTION_CLUTCH.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityReservoir>> RESERVOIR = BLOCK_ENTITIES.register("reservoir", () ->
            BlockEntityType.Builder.of(BlockEntityReservoir::new, RotaryBlocks.RESERVOIR.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityPipe>> PIPE = BLOCK_ENTITIES.register("pipe", () ->
            BlockEntityType.Builder.of((pos, state) -> new BlockEntityPipe(RotaryBlockEntities.PIPE.get(), pos, state), RotaryBlocks.PIPE.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityHose>> HOSE = BLOCK_ENTITIES.register("hose", () ->
            BlockEntityType.Builder.of(BlockEntityHose::new, RotaryBlocks.HOSE.get()).build(null));
    public static final RegistryObject<BlockEntityType<BlockEntitySeparatorPipe>> SEPARATION = BLOCK_ENTITIES.register("separation", () ->
            BlockEntityType.Builder.of(BlockEntitySeparatorPipe::new, RotaryBlocks.SEPARATION.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityValve>> VALVE = BLOCK_ENTITIES.register("valve", () ->
            BlockEntityType.Builder.of(BlockEntityValve::new, RotaryBlocks.VALVE.get()).build(null));
    public static final RegistryObject<BlockEntityType<BlockEntitySuctionPipe>> SUCTION = BLOCK_ENTITIES.register("suction", () ->
            BlockEntityType.Builder.of(BlockEntitySuctionPipe::new, RotaryBlocks.SUCTION.get()).build(null));
    public static final RegistryObject<BlockEntityType<BlockEntityBypass>> BYPASS = BLOCK_ENTITIES.register("bypass", () ->
            BlockEntityType.Builder.of(BlockEntityBypass::new, RotaryBlocks.BYPASS.get()).build(null));
    public static final RegistryObject<BlockEntityType<BlockEntityFuelLine>> FUEL_LINE = BLOCK_ENTITIES.register("fuel_line", () ->
            BlockEntityType.Builder.of(BlockEntityFuelLine::new, RotaryBlocks.FUEL_LINE.get()).build(null));
    public static final RegistryObject<BlockEntityType<BlockEntityBedrockPipe>> BEDROCK_PIPE = BLOCK_ENTITIES.register("bedrock_pipe", () ->
            BlockEntityType.Builder.of(BlockEntityBedrockPipe::new, RotaryBlocks.BEDROCK_PIPE.get()).build(null));


    public static final RegistryObject<BlockEntityType<BlockEntityGasEngine>> GAS_ENGINE = BLOCK_ENTITIES.register("gas_engine", () ->
            BlockEntityType.Builder.of(BlockEntityGasEngine::new, RotaryBlocks.GAS_ENGINE.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityDCEngine>> DC_ENGINE = BLOCK_ENTITIES.register("dc_engine", () ->
            BlockEntityType.Builder.of(BlockEntityDCEngine::new, RotaryBlocks.DC_ENGINE.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityPump>> PUMP = BLOCK_ENTITIES.register("pump", () ->
            BlockEntityType.Builder.of(BlockEntityPump::new, RotaryBlocks.PUMP.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityAerosolizer>> AEROSOLIZER = BLOCK_ENTITIES.register("aerosolizer", () ->
            BlockEntityType.Builder.of(BlockEntityAerosolizer::new, RotaryBlocks.AEROSOLIZER.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityWinder>> WINDER = BLOCK_ENTITIES.register("winder", () ->
            BlockEntityType.Builder.of(BlockEntityWinder::new, RotaryBlocks.WINDER.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityFloodlight>> FLOODLIGHT = BLOCK_ENTITIES.register("floodlight", () ->
            BlockEntityType.Builder.of(BlockEntityFloodlight::new, RotaryBlocks.FLOODLIGHT.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityMonitor>> DYNAMOMETER = BLOCK_ENTITIES.register("dynamometer", () ->
            BlockEntityType.Builder.of(BlockEntityMonitor::new, RotaryBlocks.DYNAMOMETER.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityMirror>> MIRROR = BLOCK_ENTITIES.register("mirror", () ->
            BlockEntityType.Builder.of(BlockEntityMirror::new, RotaryBlocks.MIRROR.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityVanDeGraff>> VAN_DE_GRAFF = BLOCK_ENTITIES.register("van_de_graff", () ->
            BlockEntityType.Builder.of(BlockEntityVanDeGraff::new, RotaryBlocks.VAN_DE_GRAFF.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityLandmine>> LANDMINE = BLOCK_ENTITIES.register("landmine", () ->
            BlockEntityType.Builder.of(BlockEntityLandmine::new, RotaryBlocks.LANDMINE.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityACEngine>> AC_ENGINE = BLOCK_ENTITIES.register("ac_engine", () ->
            BlockEntityType.Builder.of(BlockEntityACEngine::new, RotaryBlocks.AC_ENGINE.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityCoolingFin>> COOLING_FIN = BLOCK_ENTITIES.register("cooling_fin", () ->
            BlockEntityType.Builder.of(BlockEntityCoolingFin::new, RotaryBlocks.COOLING_FIN.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityMusicBox>> MUSIC_BOX = BLOCK_ENTITIES.register("music_box", () ->
            BlockEntityType.Builder.of(BlockEntityMusicBox::new, RotaryBlocks.MUSIC_BOX.get()).build(null));
    public static final RegistryObject<BlockEntityType<BlockEntityMagnetEngine>> MAGNETOSTATIC_ENGINE = BLOCK_ENTITIES.register("magnetostatic_engine", () ->
            BlockEntityType.Builder.of(BlockEntityMagnetEngine::new, RotaryBlocks.MAGNETOSTATIC_ENGINE.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntitySolarTower>> SOLAR_TOWER = BLOCK_ENTITIES.register("solar_tower", () ->
            BlockEntityType.Builder.of(BlockEntitySolarTower::new, RotaryBlocks.SOLAR_TOWER.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityAdvancedGear>> ADVANCED_GEAR = BLOCK_ENTITIES.register("advanced_gear", () ->
            BlockEntityType.Builder.of(BlockEntityAdvancedGear::new, RotaryBlocks.ADVANCED_GEAR.get()).build(null));
    public static final RegistryObject<BlockEntityType<BlockEntityHeatRay>> HEAT_RAY = BLOCK_ENTITIES.register("heat_ray", () ->
            BlockEntityType.Builder.of(BlockEntityHeatRay::new, RotaryBlocks.HEAT_RAY.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityObsidianMaker>> OBSIDIAN_MAKER = BLOCK_ENTITIES.register("obsidian_maker", () ->
            BlockEntityType.Builder.of(BlockEntityObsidianMaker::new, RotaryBlocks.OBSIDIAN_MAKER.get()).build(null));
    public static final RegistryObject<BlockEntityType<BlockEntityPlayerDetector>> PLAYER_DETECTOR = BLOCK_ENTITIES.register("player_detector", () ->
            BlockEntityType.Builder.of(BlockEntityPlayerDetector::new, RotaryBlocks.PLAYER_DETECTOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityHeater>> HEATER = BLOCK_ENTITIES.register("heater", () ->
            BlockEntityType.Builder.of(BlockEntityHeater::new, RotaryBlocks.HEATER.get()).build(null));
    public static final RegistryObject<BlockEntityType<BlockEntityItemCannon>> ITEM_CANNON = BLOCK_ENTITIES.register("item_cannon", () ->
            BlockEntityType.Builder.of(BlockEntityItemCannon::new, RotaryBlocks.ITEM_CANNON.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityCaveFinder>> CAVE_SCANNER = BLOCK_ENTITIES.register("cave_scanner", () ->
            BlockEntityType.Builder.of(BlockEntityCaveFinder::new, RotaryBlocks.CAVE_SCANNER.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityBlockCannon>> BLOCK_CANNON = BLOCK_ENTITIES.register("block_cannon", () ->
            BlockEntityType.Builder.of(BlockEntityBlockCannon::new, RotaryBlocks.BLOCK_CANNON.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityItemRefresher>> REFRESHER = BLOCK_ENTITIES.register("refresher", () ->
            BlockEntityType.Builder.of(BlockEntityItemRefresher::new, RotaryBlocks.REFRESHER.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntitySpiller>> SPILLER = BLOCK_ENTITIES.register("spiller", () ->
            BlockEntityType.Builder.of(BlockEntitySpiller::new, RotaryBlocks.SPILLER.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityTNTCannon>> TNT_CANNON = BLOCK_ENTITIES.register("tnt_cannon", () ->
            BlockEntityType.Builder.of(BlockEntityTNTCannon::new, RotaryBlocks.TNT_CANNON.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityContainment>> CONTAINMENT = BLOCK_ENTITIES.register("containment", () ->
            BlockEntityType.Builder.of(BlockEntityContainment::new, RotaryBlocks.CONTAINMENT.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityBucketFiller>> BUCKET_FILLER = BLOCK_ENTITIES.register("bucket_filler", () ->
            BlockEntityType.Builder.of(BlockEntityBucketFiller::new, RotaryBlocks.BUCKET_FILLER.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntitySelfDestruct>> SELF_DESTRUCT = BLOCK_ENTITIES.register("self_destruct", () ->
            BlockEntityType.Builder.of(BlockEntitySelfDestruct::new, RotaryBlocks.SELF_DESTRUCT.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityLineBuilder>> LINE_BUILDER = BLOCK_ENTITIES.register("line_builder", () ->
            BlockEntityType.Builder.of(BlockEntityLineBuilder::new, RotaryBlocks.LINE_BUILDER.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityBeamMirror>> BEAM_MIRROR = BLOCK_ENTITIES.register("beam_mirror", () ->
            BlockEntityType.Builder.of(BlockEntityBeamMirror::new, RotaryBlocks.BEAM_MIRROR.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityMultiClutch>> MULTI_CLUTCH = BLOCK_ENTITIES.register("multi_clutch", () ->
            BlockEntityType.Builder.of(BlockEntityMultiClutch::new, RotaryBlocks.MULTI_CLUTCH.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityBoiler>> FRICTION_BOILER = BLOCK_ENTITIES.register("friction_boiler", () ->
            BlockEntityType.Builder.of(BlockEntityBoiler::new, RotaryBlocks.FRICTION_BOILER.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntitySteam>> STEAM_TURBINE = BLOCK_ENTITIES.register("steam_turbine", () ->
            BlockEntityType.Builder.of(BlockEntitySteam::new, RotaryBlocks.STEAM_TURBINE.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityLavaSmeltery>> BIG_FURNACE = BLOCK_ENTITIES.register("big_furnace", () ->
            BlockEntityType.Builder.of(BlockEntityLavaSmeltery::new, RotaryBlocks.LAVA_SMELTORY.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityParticleEmitter>> PARTICLE = BLOCK_ENTITIES.register("particle", () ->
            BlockEntityType.Builder.of(BlockEntityParticleEmitter::new, RotaryBlocks.PARTICLE.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityGrindstone>> GRINDSTONE = BLOCK_ENTITIES.register("grindstone", () ->
            BlockEntityType.Builder.of(BlockEntityGrindstone::new, RotaryBlocks.GRINDSTONE.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityBlower>> BLOWER = BLOCK_ENTITIES.register("blower", () ->
            BlockEntityType.Builder.of(BlockEntityBlower::new, RotaryBlocks.BLOWER.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityRefrigerator>> REFRIGERATOR = BLOCK_ENTITIES.register("refrigerator", () ->
            BlockEntityType.Builder.of(BlockEntityRefrigerator::new, RotaryBlocks.REFRIGERATOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityComposter>> COMPOSTER = BLOCK_ENTITIES.register("composter", () ->
            BlockEntityType.Builder.of(BlockEntityComposter::new, RotaryBlocks.COMPOSTER.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityBlockFiller>> FILLER = BLOCK_ENTITIES.register("filler", () ->
            BlockEntityType.Builder.of(BlockEntityBlockFiller::new, RotaryBlocks.FILLER.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntitySpillway>> SPILLWAY = BLOCK_ENTITIES.register("spillway", () ->
            BlockEntityType.Builder.of(BlockEntitySpillway::new, RotaryBlocks.SPILLWAY.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityBevelGear>> BEVEL_GEARS = BLOCK_ENTITIES.register("bevel_gears", () ->
            BlockEntityType.Builder.of(BlockEntityBevelGear::new, RotaryBlocks.BEVEL_GEARS.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntitySorting>> SORTER = BLOCK_ENTITIES.register("sorter", () ->
            BlockEntityType.Builder.of(BlockEntitySorting::new, RotaryBlocks.SORTER.get()).build(null));
/*    public static final RegistryObject<BlockEntityType<BlockEntityFurnaceHeater>> FRICTION_HEATER = BLOCK_ENTITIES.register("friction_heater", () ->
            BlockEntityType.Builder.of(BlockEntityFurnaceHeater::new, RotaryBlocks.FRICTION_HEATER.get()).build(null));*/

//    public static final RegistryObject<BlockEntityType<BlockEntityPulseFurnace>> PULSE_JET_FURNACE = BLOCK_ENTITIES.register("pulse_jet_furnace", () ->
//            BlockEntityType.Builder.of(BlockEntityPulseFurnace::new, RotaryBlocks.PULSE_JET_FURNACE.get()).build(null));

//    public static final RegistryObject<BlockEntityType<BlockEntityFillingStation>> FILLING_STATION = BLOCK_ENTITIES.register("filling_station", () ->
//            BlockEntityType.Builder.of(BlockEntityFillingStation::new, RotaryBlocks.FILLING_STATION.get()).build(null));

    public static final RegistryObject<BlockEntityType<BlockEntityGrinder>> GRINDER = BLOCK_ENTITIES.register("filling_station", () ->
            BlockEntityType.Builder.of(BlockEntityGrinder::new, RotaryBlocks.GRINDER.get()).build(null));


}
