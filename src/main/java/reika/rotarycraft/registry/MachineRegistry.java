/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.registry;

import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import reika.dragonapi.ModList;
import reika.dragonapi.exception.RegistrationException;
import reika.dragonapi.instantiable.data.immutable.ImmutableArray;
import reika.dragonapi.instantiable.data.maps.BlockMap;
import reika.dragonapi.interfaces.IReikaRecipe;
import reika.dragonapi.interfaces.registry.TileEnum;
import reika.dragonapi.modregistry.PowerTypes;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.ModDependency;
import reika.rotarycraft.auxiliary.interfaces.*;
import reika.rotarycraft.auxiliary.recipemanagers.RecipeHandler;
import reika.rotarycraft.auxiliary.recipemanagers.WorktableRecipes;
import reika.rotarycraft.base.RotaryModelBase;
import reika.rotarycraft.base.blockentity.*;
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
import reika.rotarycraft.models.animated.*;
import reika.rotarycraft.models.animated.shaftonly.ClutchModel;
import reika.rotarycraft.models.animated.shaftonly.ShaftModel;
import reika.rotarycraft.models.engine.*;
import reika.rotarycraft.models.*;
import reika.rotarycraft.modinterface.conversion.BlockEntityBoiler;
import reika.rotarycraft.modinterface.conversion.BlockEntityMagnetEngine;
import reika.rotarycraft.modinterface.conversion.BlockEntitySteam;
import reika.rotarycraft.modinterface.model.BoilerModel;
import reika.rotarycraft.modinterface.model.DynamoModel;
import reika.rotarycraft.modinterface.model.MagneticModel;
import reika.rotarycraft.modinterface.model.SteamTurbineModel;

import java.util.Locale;
import java.util.function.Function;

/**
 * ONLY ADD NEW MACHINES TO THE BOTTOM OF THIS LIST
 */
public enum MachineRegistry implements TileEnum {

    //        BEDROCKBREAKER("machine.bedrock", BlockRotaryCraftMachine.class, BlockEntityBedrockBreaker.class),
    WIND_ENGINE("machine.wind_engine", RotaryBlocks.WIND_ENGINE.get(), BlockEntityWindEngine.class, EngineType.WIND, (modelSet) -> new WindModel(modelSet.bakeLayer(RotaryModelLayers.WIND_ENGINE))),
    STEAM_ENGINE(true, "machine.steam_engine", RotaryBlocks.STEAM_ENGINE.get(), BlockEntitySteamEngine.class, EngineType.STEAM, (modelSet) -> new SteamModel(modelSet.bakeLayer(RotaryModelLayers.STEAM_ENGINE))),
    PERFORMANCE_ENGINE(true, "machine.performance_engine", RotaryBlocks.PERFORMANCE_ENGINE.get(), BlockEntityPerformanceEngine.class, EngineType.SPORT, (modelSet) -> new PerformanceModel(modelSet.bakeLayer(RotaryModelLayers.PERFORMANCE_ENGINE))),
    MICRO_TURBINE(true, "machine.micro_turbine", RotaryBlocks.MICRO_TURBINE.get(), BlockEntityMicroturbine.class, EngineType.MICRO, (modelSet) -> new MicroTurbineModel(modelSet.bakeLayer(RotaryModelLayers.MICRO_ENGINE))),
    GAS_ENGINE(true, "machine.gas_engine", RotaryBlocks.GAS_ENGINE.get(), BlockEntityGasEngine.class, EngineType.GAS, (modelSet) -> new CombustionModel(modelSet.bakeLayer(RotaryModelLayers.COMBUSTION_ENGINE))),
    DC_ENGINE("machine.dc_engine", RotaryBlocks.DC_ENGINE.get(), BlockEntityDCEngine.class, EngineType.DC, (modelSet) -> new DCModel(modelSet.bakeLayer(RotaryModelLayers.DC_ENGINE))),
    AC_ENGINE("machine.ac_engine", RotaryBlocks.AC_ENGINE.get(), BlockEntityACEngine.class, EngineType.AC, (modelSet) -> new ACModel(modelSet.bakeLayer(RotaryModelLayers.AC_ENGINE))),

    FLYWHEEL(true, "machine.flywheel", RotaryBlocks.HSLA_FLYWHEEL.get(), BlockEntityFlywheel.class, (modelSet) -> new FlywheelModel(modelSet.bakeLayer(RotaryModelLayers.FLYWHEEL))),
    WOOD_SHAFT("machine.shaft", RotaryBlocks.WOOD_SHAFT.get(), BlockEntityShaft.class, (modelSet) -> new ShaftModel(modelSet.bakeLayer(RotaryModelLayers.SHAFT))),
    STONE_SHAFT("machine.shaft", RotaryBlocks.STONE_SHAFT.get(), BlockEntityShaft.class, (modelSet) -> new ShaftModel(modelSet.bakeLayer(RotaryModelLayers.SHAFT))),
    HSLA_SHAFT("machine.shaft", RotaryBlocks.HSLA_SHAFT.get(), BlockEntityShaft.class, (modelSet) -> new ShaftModel(modelSet.bakeLayer(RotaryModelLayers.SHAFT))),
    TUNGSTEN_SHAFT("machine.shaft", RotaryBlocks.TUNGSTEN_SHAFT.get(), BlockEntityShaft.class, (modelSet) -> new ShaftModel(modelSet.bakeLayer(RotaryModelLayers.SHAFT))),
    DIAMOND_SHAFT("machine.shaft", RotaryBlocks.DIAMOND_SHAFT.get(), BlockEntityShaft.class, (modelSet) -> new ShaftModel(modelSet.bakeLayer(RotaryModelLayers.SHAFT))),
    BEDROCK_SHAFT("machine.shaft", RotaryBlocks.BEDROCK_SHAFT.get(), BlockEntityShaft.class, (modelset) -> new ShaftModel(modelset.bakeLayer(RotaryModelLayers.SHAFT))),

    BEVELGEARS(true, "machine.bevel", RotaryBlocks.BEVEL_GEARS.get(), BlockEntityBevelGear.class, (modelSet) -> new BevelModel(modelSet.bakeLayer(RotaryModelLayers.BEVEL))),
    GEARBOX(true, "machine.gearbox", RotaryBlocks.HSLA_GEARBOX_2x.get(), BlockEntityGearbox.class, (modelSet) -> new GearboxModel(modelSet.bakeLayer(RotaryModelLayers.GEARBOX))),
    SPLITTER(true, "machine.splitter", RotaryBlocks.SPLITTER.get(), BlockEntitySplitter.class, (modelSet) -> new SplitterModel(modelSet.bakeLayer(RotaryModelLayers.SPLITTER))),
    //            FERMENTER("machine.fermenter", BlockRotaryCraftMachine.class, BlockEntityFermenter.class),
    FLOODLIGHT("machine.floodlight", RotaryBlocks.FLOODLIGHT.get(), BlockEntityFloodlight.class, (modelSet) -> new LampModel(modelSet.bakeLayer(RotaryModelLayers.FLOODLIGHT))),
    CLUTCH("machine.clutch", RotaryBlocks.CLUTCH.get(), BlockEntityClutch.class, (modelSet) -> new ClutchModel(modelSet.bakeLayer(RotaryModelLayers.CLUTCH))),
    DYNAMOMETER("machine.dyna", RotaryBlocks.DYNAMOMETER.get(), BlockEntityMonitor.class, (modelSet) -> new MonitorModel(modelSet.bakeLayer(RotaryModelLayers.DYNOMONITOR))),
    GRINDER(true, "machine.grinder", RotaryBlocks.GRINDER.get(), BlockEntityGrinder.class, (modelSet) -> new GrinderModel(modelSet.bakeLayer(RotaryModelLayers.GRINDER))),
    HEATRAY("machine.heatray", RotaryBlocks.HEAT_RAY.get(), BlockEntityHeatRay.class/*, (modelSet) -> new HeatRayModel(modelSet.bakeLayer(RotaryModelLayers.HEAT_RAY))*/),

    //                       PIPES
    HOSE("machine.hose", RotaryBlocks.HOSE.get(), BlockEntityHose.class/*, (modelSet) -> new HoseModel(modelSet.bakeLayer(RotaryModelLayers.HOSE))*/),
    PIPE("machine.pipe", RotaryBlocks.PIPE.get(), BlockEntityPipe.class/*, (modelSet) -> new PipeModel(modelSet.bakeLayer(RotaryModelLayers.PIPE))*/),
    FUELLINE("machine.fuelline", RotaryBlocks.FUEL_LINE.get(), BlockEntityFuelLine.class/*, (modelSet) -> new FuelLineModel(modelSet.bakeLayer(RotaryModelLayers.FUEL_LINE))*/),
    VALVE("machine.valve", RotaryBlocks.VALVE.get(), BlockEntityValve.class/*, (modelSet) -> new ValveModel(modelSet.bakeLayer(RotaryModelLayers.VALVE))*/),
    BYPASS("machine.bypass", RotaryBlocks.BYPASS.get(), BlockEntityBypass.class/*, (modelSet) -> new BypassModel(modelSet.bakeLayer(RotaryModelLayers.BYPASS))*/),
    SEPARATION("machine.separation", RotaryBlocks.SEPARATION.get(), BlockEntitySeparatorPipe.class/*, (modelSet) -> new SeparationModel(modelSet.bakeLayer(RotaryModelLayers.SEPARATION))*/),
    SUCTION("machine.suction", RotaryBlocks.SUCTION.get(), BlockEntitySuctionPipe.class/*, (modelSet) -> new SuctionModel(modelSet.bakeLayer(RotaryModelLayers.SUCTION))*/),
    BEDPIPE("machine.bedpipe", RotaryBlocks.BEDROCK_PIPE.get(), BlockEntityBedrockPipe.class/*, (modelSet) -> new BedPipeModel(modelSet.bakeLayer(RotaryModelLayers.BEDROCK_PIPE))*/),

    //    BORER("machine.borer", BlockRotaryCraftMachine.class, BlockEntityBorer.class),
//    LIGHTBRIDGE("machine.lightbridge", BlockRotaryCraftMachine.class, BlockEntityLightBridge.class, "RenderBridge"),
    PUMP("machine.pump", RotaryBlocks.PUMP.get(), BlockEntityPump.class, (modelSet) -> new PumpModel(modelSet.bakeLayer(RotaryModelLayers.PUMP))),
    RESERVOIR(true, "machine.reservoir", RotaryBlocks.RESERVOIR.get(), BlockEntityReservoir.class, (modelSet) -> new ReservoirModel(modelSet.bakeLayer(RotaryModelLayers.RESERVOIR))),
    AEROSOLIZER(true, "machine.aerosolizer", RotaryBlocks.AEROSOLIZER.get(), BlockEntityAerosolizer.class, (modelSet) -> new AerosolizerModel(modelSet.bakeLayer(RotaryModelLayers.AEROSOLIZER))),
    //    EXTRACTOR(true, "machine.extractor", BlockRotaryCraftMachine.class, BlockEntityExtractor.class, "RenderExtractor"),
//    PULSEJET(true, "machine.pulsejet", RotaryBlocks.PULSE_JET_FURNACE.get(), BlockEntityPulseFurnace.class),
//    COMPACTOR(true, "machine.compactor", BlockRotaryCraftMachine.class, BlockEntityCompactor.class, "RenderCompactor"),
//    FAN("machine.fan", BlockRotaryCraftMachine.class, BlockEntityFan.class, "RenderFan"),
    //    FRACTIONATOR(true, "machine.fractionator", BlockRotaryCraftMachine.class, BlockEntityFractionator.class, "RenderFraction"),
//    GPR(true, "machine.gpr", BlockGPR.class, BlockEntityGPR.class),
    OBSIDIAN(true, "machine.obsidian", RotaryBlocks.OBSIDIAN_MAKER.get(), BlockEntityObsidianMaker.class/*, (modelSet) -> new ObsidianMakerModel(modelSet.bakeLayer(RotaryModelLayers.OBSIDIAN_MAKER))*/),
    //    PILEDRIVER("machine.piledriver", BlockRotaryCraftMachine.class, BlockEntityPileDriver.class, "RenderPileDriver"),
//    VACUUM(true, "machine.vacuum", BlockRotaryCraftMachine.class, BlockEntityVacuum.class, "RenderVacuum"),
//    FIREWORK(true, "machine.firework", BlockRotaryCraftMachine.class, BlockEntityFireworkMachine.class),
//    SPRINKLER(true, "machine.sprinkler", BlockRotaryCraftMachine.class, BlockEntitySprinkler.class, "RenderSprinkler"),
//    WOODCUTTER("machine.woodcutter", BlockRotaryCraftMachine.class, BlockEntityWoodcutter.class, "RenderWoodcutter"),
//    SPAWNERCONTROLLER("machine.spawnercontroller", BlockRotaryCraftMachine.class, BlockEntitySpawnerController.class, "RenderSpawner"),
    PLAYERDETECTOR(true, "machine.playerdetector", RotaryBlocks.PLAYER_DETECTOR.get(), BlockEntityPlayerDetector.class/*, (modelSet) -> new PlayerDetectorModel(modelSet.bakeLayer(RotaryModelLayers.PLAYER_DETECTOR))*/),
    HEATER(true, "machine.heater", RotaryBlocks.HEATER.get(), BlockEntityHeater.class, (modelSet) -> new HeaterModel(modelSet.bakeLayer(RotaryModelLayers.HEATER))),
    //    BAITBOX(true, "machine.baitbox", BlockRotaryCraftMachine.class, BlockEntityBaitBox.class, "RenderBaitBox"),
//    AUTOBREEDER(true, "machine.breeder", BlockRotaryCraftMachine.class, BlockEntityAutoBreeder.class, "RenderBreeder"),
//    ECU("machine.ecu", BlockRotaryCraftMachine.class, BlockEntityEngineController.class),
    SMOKEDETECTOR("machine.smokedetector", RotaryBlocks.SMOKE_DETECTOR.get(), BlockEntitySmokeDetector.class, (modelSet) -> new SmokeDetectorModel(modelSet.bakeLayer(RotaryModelLayers.SMOKE_DETECTOR))),
    //    MOBRADAR("machine.mobradar", BlockRotaryCraftMachine.class, BlockEntityMobRadar.class, "RenderMobRadar"),
    WINDER(true, "machine.winder", RotaryBlocks.WINDER.get(), BlockEntityWinder.class, (modelSet) -> new WinderModel(modelSet.bakeLayer(RotaryModelLayers.WINDER))),
    WORMGEAR("machine.advgear", RotaryBlocks.WORMGEAR.get(), BlockEntityAdvancedGear.class, (modelSet) -> new WormModel(modelSet.bakeLayer(RotaryModelLayers.WORM))),
    CVT(true, "machine.advgear", RotaryBlocks.CVT.get(), BlockEntityAdvancedGear.class, (modelSet) -> new CVTModel(modelSet.bakeLayer(RotaryModelLayers.CVT))),
    HIGHGEAR(true, "machine.advgear", RotaryBlocks.HIGHGEAR.get(), BlockEntityAdvancedGear.class, (modelSet) -> new HighGearModel(modelSet.bakeLayer(RotaryModelLayers.HIGHGEAR))),
    COIL(true, "machine.advgear", RotaryBlocks.COIL.get(), BlockEntityAdvancedGear.class, (modelSet) -> new CoilModel(modelSet.bakeLayer(RotaryModelLayers.COIL))),

    TNTCANNON(true, "machine.tntcannon", RotaryBlocks.TNT_CANNON.get(), BlockEntityTNTCannon.class/*, (modelSet) -> new TNTCannonModel(modelSet.bakeLayer(RotaryModelLayers.TNT_CANNON))*/),
    //    SONICWEAPON(true, "machine.sonicweapon", BlockRotaryCraftMachine.class, BlockEntitySonicWeapon.class, "RenderSonic"),
//    BLASTFURNACE(true, "machine.blastfurnace", BlockRotaryCraftMachine.class, BlockEntityBlastFurnace.class),
//    FORCEFIELD(true, "machine.forcefield", BlockRotaryCraftMachine.class, BlockEntityForceField.class, "RenderForceField"),
    MUSICBOX(true, "machine.musicbox", RotaryBlocks.MUSIC_BOX.get(), BlockEntityMusicBox.class/*, (modelSet) -> new MusicBoxModel(modelSet.bakeLayer(RotaryModelLayers.MUSIC_BOX))*/),
    SPILLER(true, "machine.spiller", RotaryBlocks.SPILLER.get(), BlockEntitySpiller.class/*, (modelSet) -> new SpillerModel(modelSet.bakeLayer(RotaryModelLayers.SPILLER))*/),
    //    CHUNKLOADER("machine.chunkloader", BlockRotaryCraftMachine.class, BlockEntityChunkLoader.class, "RenderChunkLoader"),
    MOBHARVESTER("machine.mobharvester", RotaryBlocks.MOB_HARVESTER.get(), BlockEntityMobHarvester.class/*, (modelSet) -> new MobHarvesterModel(modelSet.bakeLayer(RotaryModelLayers.MOB_HARVESTER))*/),
    //    CCTV("machine.cctv", BlockRotaryCraftMachine.class, BlockEntityCCTV.class, "RenderCCTV"),
//    PROJECTOR("machine.projector", BlockRotaryCraftMachine.class, BlockEntityProjector.class, "RenderProjector"),
//    RAILGUN("machine.railgun", BlockRotaryCraftMachine.class, BlockEntityRailGun.class, "RenderRailGun"),
//    WEATHERCONTROLLER("machine.weather", BlockRotaryCraftMachine.class, BlockEntityWeatherController.class, "RenderIodide"),
    REFRESHER("machine.refresher", RotaryBlocks.REFRESHER.get(), BlockEntityItemRefresher.class/*, (modelSet) -> new RefresherModel(modelSet.bakeLayer(RotaryModelLayers.REFRESHER))*/),
    //    FREEZEGUN("machine.freezegun", BlockRotaryCraftMachine.class, BlockEntityFreezeGun.class, "RenderFreezeGun"),
    CAVESCANNER("machine.cavescanner", RotaryBlocks.CAVE_SCANNER.get(), BlockEntityCaveFinder.class/*, (modelSet) -> new CaveScannerModel(modelSet.bakeLayer(RotaryModelLayers.CAVE_SCANNER))*/),
    //    SCALECHEST("machine.chest", BlockRotaryCraftMachine.class, BlockEntityScaleableChest.class, "RenderScaleChest"),
//    IGNITER("machine.firestarter", BlockRotaryCraftMachine.class, BlockEntityIgniter.class),
//    MAGNETIZER("machine.magnetizer", BlockRotaryCraftMachine.class, BlockEntityMagnetizer.class, "RenderMagnetizer"),
    CONTAINMENT("machine.containment", RotaryBlocks.CONTAINMENT.get(), BlockEntityContainment.class/*, (modelSet) -> new ContainmentModel(modelSet.bakeLayer(RotaryModelLayers.CONTAINMENT))*/),
    //    SCREEN("machine.screen", BlockRotaryCraftMachine.class, BlockEntityScreen.class, "RenderCCTVScreen"),
//    PURIFIER("machine.purifier", BlockRotaryCraftMachine.class, BlockEntityPurifier.class),
//    LASERGUN("machine.lasergun", BlockRotaryCraftMachine.class, BlockEntityLaserGun.class, "RenderLaserGun"),
    ITEMCANNON("machine.itemcannon", RotaryBlocks.ITEM_CANNON.get(), BlockEntityItemCannon.class, (modelSet) -> new ItemCannonModel(modelSet.bakeLayer(RotaryModelLayers.ITEM_CANNON))),
    LANDMINE("machine.landmine", RotaryBlocks.LANDMINE.get(), BlockEntityLandmine.class, (modelSet) -> new LandmineModel(modelSet.bakeLayer(RotaryModelLayers.LANDMINE))),
    //    FRICTION("machine.friction", RotaryBlocks.FRICTION_HEATER.get(), BlockEntityFurnaceHeater.class),
    BLOCKCANNON("machine.blockcannon", RotaryBlocks.BLOCK_CANNON.get(), BlockEntityBlockCannon.class/*, (modelSet) -> new BlockCannonModel(modelSet.bakeLayer(RotaryModelLayers.BLOCK_CANNON))*/),
    BUCKETFILLER("machine.bucketfiller", RotaryBlocks.BUCKET_FILLER.get(), BlockEntityBucketFiller.class),
    MIRROR("machine.mirror", RotaryBlocks.MIRROR.get(), BlockEntityMirror.class, (modelSet) -> new MirrorModel(modelSet.bakeLayer(RotaryModelLayers.MIRROR))),
    SOLARTOWER("machine.solartower", RotaryBlocks.SOLAR_TOWER.get(), BlockEntitySolarTower.class, (modelSet) -> new SolarTowerModel(modelSet.bakeLayer(RotaryModelLayers.SOLAR_TOWER))),
    //    SPYCAM("machine.spycam", BlockRotaryCraftMachine.class, BlockEntitySpyCam.class, "RenderSpyCam"),
    SELFDESTRUCT("machine.selfdestruct", RotaryBlocks.SELF_DESTRUCT.get(), BlockEntitySelfDestruct.class/*, (modelSet) -> new SelfDestructModel(modelSet.bakeLayer(RotaryModelLayers.SELF_DESTRUCT))*/),
    COOLINGFIN("machine.coolingfin", RotaryBlocks.COOLING_FIN.get(), BlockEntityCoolingFin.class, (modelSet) -> new FinModel(modelSet.bakeLayer(RotaryModelLayers.COOLING_FIN))),
    //        WORKTABLE("machine.worktable", BlockRotaryCraftMachine.class, BlockEntityWorktable.class),
//    COMPRESSOR("machine.compressor", BlockModEngine.class, BlockEntityAirCompressor.class, "RenderCompressor", PowerTypes.PNEUMATIC),
    //PNEUENGINE("machine.pneuengine", BlockModEngine.class, BlockEntityPneumaticEngine.class, "RenderPneumatic", PowerTypes.PNEUMATIC),
//    DISPLAY("machine.display", BlockRotaryCraftMachine.class, BlockEntityDisplay.class, "RenderDisplay"),
//    LAMP("machine.lamp", BlockRotaryCraftMachine.class, BlockEntityLamp.class),
//    EMP("machine.emp", BlockRotaryCraftMachine.class, BlockEntityEMP.class, "RenderEMP"),
    LINEBUILDER("machine.linebuilder", RotaryBlocks.LINE_BUILDER.get(), BlockEntityLineBuilder.class/*, (modelSet) -> new LineBuilderModel(modelSet.bakeLayer(RotaryModelLayers.LINE_BUILDER))*/),
    BEAMMIRROR("machine.beammirror", RotaryBlocks.BEAM_MIRROR.get(), BlockEntityBeamMirror.class, (modelSet) -> new BeamMirrorModel(modelSet.bakeLayer(RotaryModelLayers.BEAM_MIRROR))),
    MULTICLUTCH("machine.multiclutch", RotaryBlocks.MULTI_CLUTCH.get(), BlockEntityMultiClutch.class, (modelSet) -> new MultiClutchModel(modelSet.bakeLayer(RotaryModelLayers.MULTI_CLUTCH))),
    //    TERRAFORMER("machine.terraformer", BlockRotaryCraftMachine.class, BlockEntityTerraformer.class),
    SORTING("machine.sorting", RotaryBlocks.SORTER.get(), BlockEntitySorting.class),
    //    FUELENHANCER("machine.fuelenhancer", BlockRotaryCraftMachine.class, BlockEntityFuelConverter.class, "RenderFuelConverter"),
//    ARROWGUN("machine.arrowgun", BlockRotaryCraftMachine.class, BlockEntityMachineGun.class),
    BOILER("machine.frictionboiler", RotaryBlocks.FRICTION_BOILER.get(), BlockEntityBoiler.class, PowerTypes.STEAM, (modelSet) -> new BoilerModel(modelSet.bakeLayer(RotaryModelLayers.BOILER))),
    STEAMTURBINE(true, "machine.steamturbine", RotaryBlocks.STEAM_TURBINE.get(), BlockEntitySteam.class, PowerTypes.STEAM, (modelSet) -> new SteamTurbineModel(modelSet.bakeLayer(RotaryModelLayers.STEAM_TURBINE))),
    //    FERTILIZER(true, "machine.fertilizer", BlockRotaryCraftMachine.class, BlockEntityFertilizer.class, "RenderFertilizer"),
//    LAVAMAKER(true, "machine.lavamaker", BlockRotaryCraftMachine.class, BlockEntityLavaMaker.class, "RenderRockMelter"),
    //GENERATOR("machine.generator", BlockModEngine.class, BlockEntityGenerator.class, "RenderGenerator", PowerTypes.EU),
    //ELECTRICMOTOR("machine.electricmotor", BlockModEngine.class, BlockEntityElectricMotor.class, "RenderElecMotor", PowerTypes.EU),
    //    AGGREGATOR("machine.aggregator", BlockRotaryCraftMachine.class, BlockEntityAggregator.class, "RenderAggregator"),
//    AIRGUN("machine.airgun", BlockRotaryCraftMachine.class, BlockEntityAirGun.class, "RenderAirGun"),
//    SONICBORER("machine.sonicborer", BlockRotaryCraftMachine.class, BlockEntitySonicBorer.class, "RenderSonicBorer"),
//    FUELENGINE("machine.fuelengine", BlockModEngine.class, BlockEntityFuelEngine.class, "RenderFuelEngine", ModList.BCENERGY),
//    FILLINGSTATION(true, "machine.fillingstation", RotaryBlocks.FILLING_STATION.get(), BlockEntityFillingStation.class),
//    BELT("machine.belt", BlockRotaryCraftMachine.class, BlockEntityBeltHub.class, "RenderBelt"),
    VANDEGRAFF("machine.vandegraff", RotaryBlocks.VAN_DE_GRAFF.get(), BlockEntityVanDeGraff.class, (modelSet) -> new VanDeGraffModel(modelSet.bakeLayer(RotaryModelLayers.VAN_DE_GRAFF))),
    //    DEFOLIATOR("machine.defoliator", BlockRotaryCraftMachine.class, BlockEntityDefoliator.class, "RenderDefoliator"),
    BIGFURNACE(true, "machine.bigfurnace", RotaryBlocks.LAVA_SMELTORY.get(), BlockEntityLavaSmeltery.class, PowerTypes.RF, (modelSet) -> new BigFurnaceModel(modelSet.bakeLayer(RotaryModelLayers.BIG_FURNACE))),
    //    DISTILLER("machine.distiller", BlockRotaryCraftMachine.class, BlockEntityDistillery.class, "RenderDistillery"),
    //    DYNAMO("machine.dynamo", BlockModEngine.class, BlockEntityDynamo.class, "RenderDynamo", PowerTypes.RF),
    MAGNETIC(true, "machine.magnetic", RotaryBlocks.MAGNETOSTATIC_ENGINE.get(), BlockEntityMagnetEngine.class, PowerTypes.RF, (modelSet) -> new MagneticModel(modelSet.bakeLayer(RotaryModelLayers.MAGNETIC))),
    //    CRYSTALLIZER("machine.crystal", BlockRotaryCraftMachine.class, BlockEntityCrystallizer.class, "RenderCrystal"),
//    BUSCONTROLLER("machine.buscontroller", BlockRotaryCraftMachine.class, BlockEntityBusController.class),
//    POWERBUS("machine.bus", BlockRotaryCraftMachine.class, BlockEntityPowerBus.class),
    PARTICLE(true, "machine.particle", RotaryBlocks.PARTICLE.get(), BlockEntityParticleEmitter.class/*, (modelSet) -> new ParticleModel(modelSet.bakeLayer(RotaryModelLayers.PARTICLE))*/),
    //    LAWNSPRINKLER("machine.lawnsprinkler", BlockRotaryCraftMachine.class, BlockEntityLawnSprinkler.class, "RenderLawnSprinkler"),
    GRINDSTONE(true, "machine.grindstone", RotaryBlocks.GRINDSTONE.get(), BlockEntityGrindstone.class, (modelSet) -> new GrindstoneModel(modelSet.bakeLayer(RotaryModelLayers.GRINDSTONE))),
    BLOWER("machine.blower", RotaryBlocks.BLOWER.get(), BlockEntityBlower.class/*, (modelSet) -> new BlowerModel(modelSet.bakeLayer(RotaryModelLayers.BLOWER))*/),
    //    PORTALSHAFT("machine.portalshaft", BlockRotaryCraftMachine.class, BlockEntityPortalShaft.class, "RenderPortalShaft"),
    REFRIGERATOR(true, "machine.refrigerator", RotaryBlocks.REFRIGERATOR.get(), BlockEntityRefrigerator.class/*, (modelSet) -> new RefrigeratorModel(modelSet.bakeLayer(RotaryModelLayers.REFRIGERATOR))*/),
    //    GASTANK("machine.gastank", BlockRotaryCraftMachine.class, BlockEntityFluidCompressor.class, "RenderGasCompressor"),
//    CRAFTER("machine.crafter", BlockRotaryCraftMachine.class, BlockEntityAutoCrafter.class),
    COMPOSTER("machine.composter", RotaryBlocks.COMPOSTER.get(), BlockEntityComposter.class),
    //    ANTIAIR("machine.antiair", BlockRotaryCraftMachine.class, BlockEntityAAGun.class, "RenderAAGun"),
//    PIPEPUMP("machine.pipepump", BlockRotaryCraftMachine.class, BlockEntityPipePump.class, "RenderPipePump"),
//    CHAIN("machine.chain", BlockRotaryCraftMachine.class, BlockEntityChainDrive.class, "RenderBelt"),
//    CENTRIFUGE("machine.centrifuge", BlockRotaryCraftMachine.class, BlockEntityCentrifuge.class, "RenderCentrifuge"),
    //    DRYING("machine.drying", BlockRotaryCraftMachine.class, BlockEntityDryingBed.class, "RenderDryingBed"),
//    WETTER("machine.wetter", BlockRotaryCraftMachine.class, BlockEntityWetter.class, "RenderWetter"),
//    DROPS("machine.drops", BlockRotaryCraftMachine.class, BlockEntityDropProcessor.class),
//    ITEMFILTER("machine.itemfilter", BlockRotaryCraftMachine.class, BlockEntityItemFilter.class),
//    HYDRATOR("machine.hydrator", BlockRotaryCraftMachine.class, BlockEntityGroundHydrator.class, "RenderHydrator"),
    FILLER("machine.filler", RotaryBlocks.FILLER.get(), BlockEntityBlockFiller.class/*, (modelSet) -> new FillerModel(modelSet.bakeLayer(RotaryModelLayers.FILLER))*/),
    //    GATLING("machine.gatling", BlockRotaryCraftMachine.class, BlockEntityMultiCannon.class, "RenderMultiCannon"),
    SPILLWAY("machine.spillway", RotaryBlocks.SPILLWAY.get(), BlockEntitySpillway.class, (modelSet) -> new SpillwayModel(modelSet.bakeLayer(RotaryModelLayers.SPILLWAY))),
    //    FLAMETURRET("machine.flameturret", BlockRotaryCraftMachine.class, BlockEntityFlameTurret.class, "RenderFlameTurret"),
//    BUNDLEDBUS("machine.bundledbus", BlockRotaryCraftMachine.class, BlockEntityBundledBus.class, ModList.APPENG, ModList.PROJRED),
    DISTRIBCLUTCH("machine.distribclutch", RotaryBlocks.DISTRIBUTION_CLUTCH.get(), BlockEntityDistributionClutch.class, (modelSet) -> new DistribClutchModel(modelSet.bakeLayer(RotaryModelLayers.DISTRIB_CLUTCH)));

    public static final ImmutableArray<MachineRegistry> machineList = new ImmutableArray<>(values());
    public static final BlockMap<MachineRegistry> machineMappings = new BlockMap<>();
    private final String name;
    private final Block block;
    private final Class<? extends RotaryCraftBlockEntity> te;
    private EngineType engineType;
    private ModDependency requirement;
    private PowerTypes powertype;
    private PowerReceivers receiver;
    private boolean hasGui;

    private boolean hasModel = false;
    private Function<EntityModelSet, ? extends RotaryModelBase> model;

    MachineRegistry(String n, Block b, Class<? extends RotaryCraftBlockEntity> tile) {
        name = n;
        block = b;
        te = tile;
        receiver = PowerReceivers.initialize(this);
    }

    MachineRegistry(boolean hasGui, String n, Block b, Class<? extends RotaryCraftBlockEntity> tile) {
        this(n, b, tile);
        this.hasGui = hasGui;
    }

    MachineRegistry(boolean hasGui, String n, Block b, Class<? extends RotaryCraftBlockEntity> tile, EngineType e) {
        this(n, b, tile);
        engineType = e;
        receiver = PowerReceivers.initialize(this);
        this.hasGui = hasGui;
    }

    MachineRegistry(boolean hasGui, String n, Block b, Class<? extends RotaryCraftBlockEntity> tile, PowerTypes e) {
        this(n, b, tile);
        powertype = e;
        receiver = PowerReceivers.initialize(this);
        this.hasGui = hasGui;
    }

    MachineRegistry(boolean hasGui, String n, Block b, Class<? extends RotaryCraftBlockEntity> tile, Function<EntityModelSet, ? extends RotaryModelBase> model) {
        this(n, b, tile);
        this.hasGui = hasGui;
        this.model = model;
        hasModel = true;
    }

    MachineRegistry(String n, Block b, Class<? extends RotaryCraftBlockEntity> tile, Function<EntityModelSet, ? extends RotaryModelBase> model) {
        this(n, b, tile);
        this.model = model;
        hasModel = true;
    }

    MachineRegistry(boolean hasGui, String n, Block b, Class<? extends RotaryCraftBlockEntity> tile, EngineType e, Function<EntityModelSet, ? extends RotaryModelBase> model) {
        this(n, b, tile);
        engineType = e;
        receiver = PowerReceivers.initialize(this);
        this.hasGui = hasGui;
        this.model = model;
        hasModel = true;
    }

    MachineRegistry(String n, Block b, Class<? extends RotaryCraftBlockEntity> tile, EngineType e, Function<EntityModelSet, ? extends RotaryModelBase> model) {
        this(n, b, tile);
        engineType = e;
        receiver = PowerReceivers.initialize(this);
        this.model = model;
        hasModel = true;
    }

    MachineRegistry(boolean hasGui, String n, Block b, Class<? extends RotaryCraftBlockEntity> tile, PowerTypes e, Function<EntityModelSet, ? extends RotaryModelBase> model) {
        this(n, b, tile);
        powertype = e;
        receiver = PowerReceivers.initialize(this);
        this.hasGui = hasGui;
        this.model = model;
        hasModel = true;
    }

    MachineRegistry(String n, Block b, Class<? extends RotaryCraftBlockEntity> tile, PowerTypes e, Function<EntityModelSet, ? extends RotaryModelBase> model) {
        this(n, b, tile);
        powertype = e;
        receiver = PowerReceivers.initialize(this);
        this.model = model;
        hasModel = true;
    }

    MachineRegistry(String n, Block b, Class<? extends RotaryCraftBlockEntity> tile, ModList... a) {
        this(n, b, tile);
        requirement = a.length > 0 ? new ModDependency(a) : null;
        receiver = PowerReceivers.initialize(this);
    }

    MachineRegistry(String n, Block b, Class<? extends RotaryCraftBlockEntity> tile, PowerTypes p) {
        this(n, b, tile);
        powertype = p;
        receiver = PowerReceivers.initialize(this);
    }

    MachineRegistry(String n, Block b, Class<? extends RotaryCraftBlockEntity> tile, EngineType type) {
        name = n;
        block = b;
        te = tile;
        engineType = type;
    }

    public boolean hasGui() {
        return hasGui;
    }

/*  todo  public String getRenderPackage() {
        if (this.hasPrerequisite()) {//todo|| BlockModEngine.class.isAssignableFrom(blockClass)) {
            if (EnergyToPowerBase.class.isAssignableFrom(te) || RCToModConverter.class.isAssignableFrom(te))
                return "reika.rotarycraft.modinterface.conversion";
            return "reika.rotarycraft.modinterface";
        }

//        if (blockClass == BlockTrans.class)
//            return "reika.rotarycraft.renders";
        if (block == BlockEngine.class)
            return "reika.rotarycraft.renders";
//        if (blockClass == BlockSolar.class)
//            return "reika.rotarycraft.renders";
        if (block == BlockFlywheel.class)
            return "reika.rotarycraft.renders";
        if (block == BlockGearbox.class)
            return "reika.rotarycraft.renders";
        if (block == BlockShaft.class)
            return "reika.rotarycraft.renders";
        if (block == BlockAdvGear.class)
            return "reika.rotarycraft.renders";
        if (block == BlockPiping.class)
            return "reika.rotarycraft.renders";

        String base = "reika.rotarycraft.renders";
        String app = ".";
        app += this.getBlockType();
        return base + app;
    }

    public String getBlockType() {
        return block.getDescriptionId().replaceAll("Block", "").replaceAll("Machine", "");
    }*/

    /**
     * A convenience feature
     */
    public static MachineRegistry getMachine(Level level, BlockPos pos) {
        return getMachine(level, pos.getX(), pos.getY(), pos.getZ());
    }

    public static MachineRegistry getMachine(BlockGetter world, int x, int y, int z) {
        Block b = world.getBlockState(new BlockPos(x, y, z)).getBlock();
//        RotaryCraft.LOGGER.info("Block at " + x + "," + y + "," + z + " is " + b);
        if (b == Blocks.AIR)
            return null;
        return getMachineMapping(b);
    }

    public static MachineRegistry getMachineMapping(Block id) {
//        if (id == RotaryBlocks.GPR.get())
//            return GPR;
        if (id == RotaryBlocks.BEDROCK_GEARBOX_2x.get() || id == RotaryBlocks.BEDROCK_GEARBOX_4x.get() ||
                id == RotaryBlocks.BEDROCK_GEARBOX_8x.get() || id == RotaryBlocks.BEDROCK_GEARBOX_16x.get() ||
                id == RotaryBlocks.HSLA_GEARBOX_2x.get() || id == RotaryBlocks.HSLA_GEARBOX_4x.get() || id == RotaryBlocks.HSLA_GEARBOX_8x.get() ||
                id == RotaryBlocks.HSLA_GEARBOX_16x.get() || id == RotaryBlocks.DIAMOND_GEARBOX_2x.get() || id == RotaryBlocks.DIAMOND_GEARBOX_4x.get() ||
                id == RotaryBlocks.DIAMOND_GEARBOX_8x.get() || id == RotaryBlocks.DIAMOND_GEARBOX_16x.get())
            return GEARBOX;
        if (id == RotaryBlocks.HSLA_FLYWHEEL.get() || id == RotaryBlocks.BEDROCK_FLYWHEEL.get() || id == RotaryBlocks.DIAMOND_FLYWHEEL.get()
                || id == RotaryBlocks.TUNGSTEN_FLYWHEEL.get() || id == RotaryBlocks.WOOD_FLYWHEEL.get())
            return FLYWHEEL;
        return machineMappings.get(id);
    }

    /*    public static MultiMap<MachineRegistry, Enchantment> getEnchantableMachineList() {
            MultiMap<MachineRegistry, Enchantment> li = new MultiMap<>().setNullEmpty().setOrdered(new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    return 1; //todo enchantment comparing
                }

    //            @Override
    //            public int compare(Enchantment o1, Enchantment o2) {
    //                //return Integer.compare(o1, o2);
    //            }
            });
            for (int i = 0; i < MachineRegistry.machineList.length; i++) {
                MachineRegistry m = MachineRegistry.machineList.get(i);
                if (m.isEnchantable()) {
                    for (Enchantment e : ((EnchantableMachine) (m).getEnchantmentHandler().getValidEnchantments())) {
                        li.addValue(m, e);
                    }
                }
            }
            return li;
        }*/
    public boolean hasModel() {
        return hasModel;
    }

    public int getNumberDirections() {
        if (this.is2Sided())
            return 2;
        if (this.is4Sided())
            return 4;
        if (this.is6Sided())
            return 6;
        return 1;
    }

    public PowerReceivers getPowerReceiverEntry() {
        return receiver;
    }

    public EngineType getEngineType() {
        if (this.isEngine())
            return engineType;
        return EngineType.DC; //return DC in worst case to prevent crashes
    }

    public String getDefaultName() {
        return this.getName();
    }


    public boolean isPipe() {
        return BlockEntityPiping.class.isAssignableFrom(te);
    }

    public float getMinX(RotaryCraftBlockEntity tile) {
//        if (this == SPRINKLER)
//            return 0.3125F;
//        if (this == WOODCUTTER)
//            return 0.0625F;
        if (this == SMOKEDETECTOR)
            return 0.25F;
//        if (this == CCTV)
//            return 0.25F;
//        if (this == SCALECHEST)
//            return 0.0625F;
        return 0;
    }

    public float getMinY(RotaryCraftBlockEntity tile) {
//        if (this == SPRINKLER)
//            return 0.4375F;
        if (this == SMOKEDETECTOR)
            return 0.875F;
//        if (this == SPYCAM)
//            return 0.375F;
//        if (this == CCTV)
//            return 0.5F - 0.5F * (float) Math.sin(Math.toRadians(((BlockEntityCCTV) tile).theta));
        return 0;
    }

    public float getMinZ(RotaryCraftBlockEntity tile) {
//        if (this == SPRINKLER)
//            return 0.3125F;
//        if (this == WOODCUTTER)
//            return 0.0625F;
        if (this == SMOKEDETECTOR)
            return 0.25F;
//        if (this == CCTV)
//            return 0.25F;
//        if (this == SCALECHEST)
//            return 0.0625F;
        return 0;
    }

    public float getMaxX(RotaryCraftBlockEntity tile) {
//        if (this == SPRINKLER)
//            return 0.6875F;
        if (this == SMOKEDETECTOR)
            return 0.75F;
//        if (this == CCTV)
//            return 0.75F;
//        if (this == WOODCUTTER)
//            return 0.9375F;
//        if (this == SCALECHEST)
//            return 0.9375F;
        return 1;
    }

    public float getMaxY(RotaryCraftBlockEntity tile) {
        if (this == FLOODLIGHT) {
            if (((BlockEntityFloodlight) tile).beammode)
                return 1;
            return 0.875F;
        }
//        if (this == CCTV)
//            return 0.5F - 0.5F * (float) Math.sin(Math.toRadians(((BlockEntityCCTV) tile).theta));
        if (this == GRINDER)
            return 0.8125F;
        if (this == HEATRAY)
            return 0.6875F;
//        if (this == LIGHTBRIDGE)
//            return 0.6875F;
        if (this == PUMP)
            return 0.75F;
        if (this == AEROSOLIZER)
            return 0.875F;
//        if (this == PULSEJET)
//            return 0.5625F;
        if (this == HEATER)
            return 0.5F;
//        if (this == AUTOBREEDER)
//            return 0.5F;
        if (this == OBSIDIAN)
            return 0.75F;
//        if (this == WOODCUTTER)
//            return 0.875F;
//        if (this == SPAWNERCONTROLLER)
//            return 0.375F;
        if (this == PLAYERDETECTOR)
            return 0.6875F;
//        if (this == MOBRADAR)
//            return 0.75F;
        if (this == WINDER)
            return 0.8125F;
        if (this == TNTCANNON)
            return 0.9375F;
        if (this == MOBHARVESTER)
            return 0.999F;
//        if (this == PROJECTOR)
//            return 0.8125F;
//        if (this == WEATHERCONTROLLER)
//            return 0.675F;
//        if (this == MAGNETIZER)
//            return 0.9375F;
//        if (this == SCALECHEST)
//            return 0.875F;
        if (this == LANDMINE)
            return 0.4375F;
        if (this == BLOCKCANNON)
            return 0.9375F;
//        if (this == EMP)
//            return 0.5F;
//        if (this == FERTILIZER)
//            return 0.875F;
//        if (this == DEFOLIATOR)
//            return 0.625F;
//        if (this == LAWNSPRINKLER)
//            return 0.75F;
        if (this == GRINDSTONE)
            return 0.9375F;
        if (this == COMPOSTER)
            return 0.75F;
//        if (this == CENTRIFUGE)
//            return 0.375F;
//        if (this == FRICTION)
//            return 0.9375F;
        return 1;
    }

    public float getMaxZ(RotaryCraftBlockEntity tile) {
//        if (this == SPRINKLER)
//            return 0.6875F;
        if (this == SMOKEDETECTOR)
            return 0.75F;
//        if (this == CCTV)
//            return 0.75F;
//        if (this == WOODCUTTER)
//            return 0.9375F;
//        if (this == SCALECHEST)
//            return 0.9375F;
        return 1;
    }

    public boolean hasSneakActions() {
        return switch (this) {
            case CAVESCANNER, /*SCREEN, GPR,*/ RESERVOIR -> true;
            default -> false;
        };
    }

    public boolean isXFlipped() {
//        return this == BEDROCKBREAKER;
        return false;
    }

    public boolean isZFlipped() {
        return switch (this) {
            case /*BEDROCKBREAKER,*/ FLOODLIGHT, /*LIGHTBRIDGE,*/ HEATRAY, /*FAN, PROJECTOR, SCALECHEST,*/ SPILLWAY ->
                    true;
            default -> false;
        };
    }

    public boolean isStandardPipe() {
        return this == PIPE || this == BEDPIPE;
    }

    public Class<? extends RotaryCraftBlockEntity> getTEClass() {
        return te;
    }

    @Override
    public String getName() {
//        return LanguageRegistry.instance().getStringLocalization("rcmachine."+this.name().toLowerCase());
        return I18n.get("rcmachine" + name.toLowerCase(Locale.ROOT));
//        return name;
    }

    /**
     * Gets the default {@link BlockState} for the machines/BlockEntity's parent {@link Block}.
     * Returning null shouldn't happen, if it does the 2nd argument {@link Block} is likely wrong
     */
    public BlockState getBlockState() {
        try {
            return block.defaultBlockState();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets the {@link RotaryModelBase} for the machine / BlockEntity.
     * Returning null shouldn't happen, if it does you're trying to access a machine that doesnt have a model, or something went wrong
     */
    public Function<EntityModelSet, ? extends RotaryModelBase> getModel() {
        return model;
    }

    public boolean isPowerReceiver() {
        return BlockEntityPowerReceiver.class.isAssignableFrom(te);
    }

    public boolean dealsContactDamage() {
        return DamagingContact.class.isAssignableFrom(te);
    }

    public boolean dealsHeatDamage(Entity e) {
        if (e instanceof ItemEntity || e instanceof ExperienceOrb)
            return false;
        return switch (this) {
            case /*COMPACTOR, */HEATER, /*IGNITER,*/ OBSIDIAN /*, PULSEJET, FRICTION*/ -> true;
            default -> false;
        };
    }

    public boolean is4Sided() {
        return switch (this) {
            case DC_ENGINE, /*BORER, LIGHTBRIDGE,*/ FLYWHEEL, GEARBOX, SPLITTER, /*FERMENTER,*/ DYNAMOMETER, GRINDER, HEATRAY, /*COMPACTOR, WOODCUTTER,*/ WINDER, WORMGEAR, HIGHGEAR, CVT, COIL, /*BLASTFURNACE, PROJECTOR, SCALECHEST, MAGNETIZER, SCREEN, FRICTION, DISPLAY,*/
                    MULTICLUTCH, /*ARROWGUN,*/ BEAMMIRROR, /*AIRGUN, SORTING, FILLINGSTATION, DISTILLER, CRYSTALLIZER, BUSCONTROLLER, REFRIGERATOR, DROPS,*/ SPILLWAY ->
                    true;
            default -> false;
        };
    }

    public boolean is6Sided() {
        return switch (this) {
            case WOOD_SHAFT,
                    STONE_SHAFT,
                    HSLA_SHAFT,
                    TUNGSTEN_SHAFT,
                    DIAMOND_SHAFT,
                    BEDROCK_SHAFT, /*BEDROCKBREAKER, */FLOODLIGHT, /*FAN,*/ COOLINGFIN, LINEBUILDER,/* SONICBORER, BELT,*/ BLOWER, /*PIPEPUMP, CHAIN,*/ CLUTCH ->
                    true;
            default -> false;
        };
    }

    public boolean isEnchantable() {
        return EnchantableMachine.class.isAssignableFrom(te);
    }

    public boolean isModConversionEngine() {
        return switch (this) {
            case /*DYNAMO, COMPRESSOR,*/ BOILER /*,GENERATOR*/ -> true;
            default -> false;
        };
    }

    public boolean isEnergyToPower() {
        return EnergyToPowerBase.class.isAssignableFrom(te);
    }

    public boolean isPoweredTransmissionMachine() {
        return TransmissionReceiver.class.isAssignableFrom(te);
    }

    public boolean cachesConnections() {
        return CachedConnection.class.isAssignableFrom(te);
    }

    public static boolean isShaft(Item item) {
        return item == WOOD_SHAFT.block.asItem() || item ==
                STONE_SHAFT.block.asItem() || item ==
                HSLA_SHAFT.block.asItem() || item ==
                TUNGSTEN_SHAFT.block.asItem() || item ==
                DIAMOND_SHAFT.block.asItem() || item ==
                BEDROCK_SHAFT.block.asItem();
    }

    public static boolean isShaft(Block item) {

        return false;
    }

    public boolean is2Sided() {
        return switch (this) {
            case /*PILEDRIVER, GPR, */PUMP, GRINDSTONE -> true;
            default -> false;
        };
    }

    public boolean hasSubdivisions() {
        return switch (this) {
            case WIND_ENGINE,
                    STEAM_ENGINE,
                    PERFORMANCE_ENGINE,
                    MICRO_TURBINE,
                    GAS_ENGINE,
                    DC_ENGINE,
                    AC_ENGINE, GEARBOX, WOOD_SHAFT,
                    STONE_SHAFT,
                    HSLA_SHAFT,
                    TUNGSTEN_SHAFT,
                    DIAMOND_SHAFT,
                    BEDROCK_SHAFT, WORMGEAR, HIGHGEAR, CVT, COIL, FLYWHEEL -> true;
            default -> false;
        };
    }

    public boolean canBeFrictionHeated() {
        return FrictionHeatable.class.isAssignableFrom(te);
    }

    public boolean canBeBroken() {
        return switch (this) {
            case MIRROR, WOOD_SHAFT,
                    STONE_SHAFT,
                    HSLA_SHAFT,
                    TUNGSTEN_SHAFT,
                    DIAMOND_SHAFT,
                    BEDROCK_SHAFT, FLYWHEEL, WIND_ENGINE,
                    STEAM_ENGINE,
                    PERFORMANCE_ENGINE,
                    MICRO_TURBINE,
                    GAS_ENGINE,
                    DC_ENGINE,
                    AC_ENGINE -> true;
            default -> false;
        };
    }

    public boolean isEngine() {
        return BlockEntityEngine.class.isAssignableFrom(te);
    }

    public boolean isBroken(RotaryCraftBlockEntity tile) {
        if (!this.canBeBroken())
            return false;
        if (this == WOOD_SHAFT || this ==
                STONE_SHAFT || this ==
                HSLA_SHAFT || this ==
                TUNGSTEN_SHAFT || this ==
                DIAMOND_SHAFT || this ==
                BEDROCK_SHAFT)
            return ((BlockEntityShaft) tile).failed();
        if (this == FLYWHEEL)
            return ((BlockEntityFlywheel) tile).failed;
        if (this == WIND_ENGINE || this == STEAM_ENGINE || this == PERFORMANCE_ENGINE || this == MICRO_TURBINE || this == GAS_ENGINE || this == DC_ENGINE || this == AC_ENGINE)
            return (((BlockEntityEngine) tile).isBroken());
        if (this == MIRROR)
            return ((BlockEntityMirror) tile).broken;
        return false;
    }

    public boolean isAvailableInCreativeInventory() {
        if (this.isDummiedOut())
            return false;
//    todo    if (this.isIncomplete() && !(DragonAPI.isReikasComputer() || DragonOptions.DEBUGMODE.getState()))
//            return false;
        if (this.isConfigDisabled())
            return false;
//        todo if (this == PORTALSHAFT)
//            return false;
        return true;
    }

    public boolean isDummiedOut() {
//        if (this == CCTV)
//            return true;
        if (requirement != null && !requirement.isLoaded())
            return true;
        if (powertype != null && !powertype.isLoaded())
            return true;
        return false;
    }

    public boolean hasPrerequisite() {
        return requirement != null || powertype != null;
    }

    public boolean renderInPass1() {
        if (this == COOLINGFIN)
            return true;
//        if (this == DISPLAY)
//            return true;
        if (this == PUMP)
            return true;
//        return this == EMP;
        return false;
    }

    public boolean isSidePlaced() {
        if (this == COOLINGFIN)
            return true;
//        if (this == COMPRESSOR)
        return true;
//        return this == DYNAMO;
    }

    public boolean allowsAcceleration() {
        return switch (this) {
            case /*BLASTFURNACE, DRYING,*/ COMPOSTER/*, HYDRATOR*/ -> true;
            default -> false;
        };
    }

    public boolean matches(MachineRegistry m) {
        return this == m;
    }

    public boolean isTechnical() {
        return false; //todo crc or shaft into netherportal? this == PORTALSHAFT;
    }

    public boolean isConfigDisabled() {
//        if (this == BORER || this == SONICBORER)
//            return ConfigRegistry.NOMINERS.get();
//        if (this == TNTCANNON)
//            return !ConfigRegistry.ALLOWTNTCANNON.get();
        if (this == ITEMCANNON)
            return !ConfigRegistry.ALLOWITEMCANNON.getState();
//        if (this == EMP)
//            return !RotaryConfig.COMMON.ALLOWEMP.get();
//        if (this == LIGHTBRIDGE)
//            return !RotaryConfig.COMMON.ALLOWLIGHTBRIDGE.get();
//        if (this == CHUNKLOADER)
//            return !RotaryConfig.COMMON.ALLOWCHUNKLOADER.get();
        if (this == SPILLER)
            return ConfigRegistry.SPILLERRANGE.getValue() == 0;
        if (this.isModConversionEngine())
            return !ConfigRegistry.enableConverters();
        return false;
    }

    public boolean isAdvancedTransmission() {
        return switch (this) {
            case WORMGEAR, HIGHGEAR, CVT, COIL, GEARBOX, SPLITTER, DYNAMOMETER -> true;
            default -> false;
        };
    }

    public boolean canFlip() {
        return switch (this) {
            case SPLITTER, SMOKEDETECTOR, /*SPRINKLER, PULSEJET,*/ PUMP -> false;
            default -> true;
        };
    }

    public boolean canBeDisabledInOverworld() {
        return switch (this) {
//            case BORER, SONICBORER, EMP, RAILGUN, LASERGUN -> true;
            default -> false;
        };
    }

    public boolean isUncraftable() {
        if (this == MachineRegistry.COOLINGFIN) {
            return false;
        }
        return true;
    }

    public boolean canDoMultiPerTick() {
//        return this == EXTRACTOR || MultiOperational.class.isAssignableFrom(this.getTEClass());
        return false;
    }

    static {
        for (int i = 0; i < machineList.length; i++) {
            var m = machineList.get(i);
//            RotaryCraft.LOGGER.info("M on it's own: " + m);
//            RotaryCraft.LOGGER.info("Name: " + m.getName());
            if (m.getBlockState() != null) {
                var id = m.getBlockState().getBlock();
//                RotaryCraft.LOGGER.info("ID: " + id);
//           todo     if (machineMappings.containsKey(id))
//                    throw new RegistrationException(RotaryCraft.getInstance(), "BlockState conflict " + id + ": " + m + " & " + machineMappings.get(id));
                machineMappings.put(id, m);
            } else {
                throw new RegistrationException(RotaryCraft.getInstance(), "BlockState is null for " + m);
            }
        }
    }

    public boolean isSolidBottom() {
        return switch (this) {
//            FRICTION, MAGNETIZER, CRYSTALLIZER, DISPLAY, SONICBORER, PROJECTOR, ELECTRICMOTOR, GENERATOR, AIRGUN,
            case WORMGEAR, HIGHGEAR, CVT, COIL, STEAMTURBINE, TNTCANNON, MAGNETIC, REFRIGERATOR, WINDER, COMPOSTER, WOOD_SHAFT,
                    STONE_SHAFT,
                    HSLA_SHAFT,
                    TUNGSTEN_SHAFT,
                    DIAMOND_SHAFT,
                    BEDROCK_SHAFT, CLUTCH, GEARBOX, FLYWHEEL, RESERVOIR, WIND_ENGINE, GRINDER,
                    STEAM_ENGINE,
                    PERFORMANCE_ENGINE,
                    MICRO_TURBINE,
                    GAS_ENGINE,
                    DC_ENGINE,
                    AC_ENGINE, BLOCKCANNON, ITEMCANNON, DYNAMOMETER, HEATRAY, GRINDSTONE, MIRROR -> true;
            default -> false;
        };
    }

    public boolean isOpaque() {
        return this.isSolidBottom();
    }

    /*    public boolean isCreativeTabValid(CreativeModeTab tab) {
    //      todo  if (this == BELT || this == CHAIN || this == POWERBUS || this == BUSCONTROLLER || BlockEntityTransmissionMachine.class.isAssignableFrom(te))
    //            return tab == RotaryCraft.ROTARY_POWER;
            return tab == RotaryCraft.ROTARY;
        }*/
    /** Is the machine crucial to the mod (i.e. the techtree, realism, usability, or balance is damaged by its removal) */
    public boolean isCrucial() {
        if (this.isPipe())
            return true;
        if (this.isCritical())
            return true;
        return switch (this) {
            case /*BEDROCKBREAKER,*/ STEAM_ENGINE,
                    PERFORMANCE_ENGINE,
                    MICRO_TURBINE,
                    GAS_ENGINE,
                    DC_ENGINE,
                    AC_ENGINE, WOOD_SHAFT,
                    STONE_SHAFT,
                    HSLA_SHAFT,
                    TUNGSTEN_SHAFT,
                    DIAMOND_SHAFT,
                    BEDROCK_SHAFT, BEVELGEARS, SPLITTER, GEARBOX, DYNAMOMETER, /*FERMENTER,*/ GRINDER, /*COMPACTOR, BORER,*/ PUMP, /*EXTRACTOR, FAN, FRACTIONATOR, WOODCUTTER, SPAWNERCONTROLLER,*/ HEATER, HEATRAY, /*ECU,*/ WINDER, CVT, WORMGEAR, /*BLASTFURNACE,*/ MOBHARVESTER, /*MAGNETIZER, FRICTION,*/ MIRROR, SOLARTOWER, COOLINGFIN, /*WORKTABLE, COMPRESSOR, DYNAMO,*/ MULTICLUTCH, SORTING,/* FERTILIZER,*/ MAGNETIC, /*LAVAMAKER, AGGREGATOR, FILLINGSTATION, BELT,*/ VANDEGRAFF, /*BUSCONTROLLER, POWERBUS,*/ BIGFURNACE, /*CRYSTALLIZER,*/ BLOWER, REFRIGERATOR, /*CRAFTER,*/ COMPOSTER/*, PIPEPUMP, CENTRIFUGE, DRYING, WETTER*/ ->
                    true;
            default -> false;
        };
    }

    public boolean isCritical() {
        if (this.isPipe())
            return true;
        return switch (this) {
            case /*BEDROCKBREAKER, ENGINE, SHAFT, */
                    BEVELGEARS, SPLITTER, GEARBOX,
                    /*FERMENTER,*/ GRINDER,/* COMPACTOR,*/ PUMP,
                    /*EXTRACTOR, FAN, FRACTIONATOR,*/ HEATER,
                    HEATRAY, WINDER, /*ADVANCEDGEARS, BLASTFURNACE,
                    MAGNETIZER, FRICTION,*/ COOLINGFIN, /*WORKTABLE, */
                    MULTICLUTCH, SORTING, /*FERTILIZER, AGGREGATOR,
                    FILLINGSTATION, BELT,*/ VANDEGRAFF,/* BUSCONTROLLER,
                    POWERBUS,*/ BLOWER, REFRIGERATOR/*, CRAFTER,
                    PIPEPUMP, CENTRIFUGE, DRYING, WETTER */->
                    true;
            default -> false;
        };
    }
    public boolean isCraftable() {
        if (requirement != null && !requirement.isLoaded())
            return false;
        if (powertype != null && !powertype.isLoaded())
            return false;
        return !this.isDummiedOut() && !this.isTechnical() && !this.isConfigDisabled();
    }

    public void addRecipe(IReikaRecipe ir) {
        if (this.isCraftable()) {
            WorktableRecipes.getInstance().addRecipe(ir, this.isCrucial() ? RecipeHandler.RecipeLevel.CORE : RecipeHandler.RecipeLevel.PROTECTED);
            if (ConfigRegistry.TABLEMACHINES.getState()) {
                GameRegistry.addRecipe(ir);
            }
        }
    }
}
