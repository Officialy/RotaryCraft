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

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import reika.dragonapi.ModList;
import reika.dragonapi.exception.RegistrationException;
import reika.dragonapi.instantiable.data.immutable.ImmutableArray;
import reika.dragonapi.instantiable.data.maps.BlockMap;
import reika.dragonapi.interfaces.registry.TileEnum;
import reika.dragonapi.modregistry.PowerTypes;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.ModDependency;
import reika.rotarycraft.auxiliary.interfaces.*;
import reika.rotarycraft.base.blockentity.*;
import reika.rotarycraft.blockentities.*;
import reika.rotarycraft.blockentities.auxiliary.BlockEntityCoolingFin;
import reika.rotarycraft.blockentities.auxiliary.BlockEntityPipePump;
import reika.rotarycraft.blockentities.auxiliary.BlockEntityFillingStation;
import reika.rotarycraft.blockentities.auxiliary.BlockEntityFurnaceHeater;
import reika.rotarycraft.blockentities.auxiliary.BlockEntityHeater;
import reika.rotarycraft.blockentities.auxiliary.BlockEntityMirror;
import reika.rotarycraft.blockentities.decorative.BlockEntityMusicBox;
import reika.rotarycraft.blockentities.decorative.BlockEntityParticleEmitter;
import reika.rotarycraft.blockentities.engine.*;
import reika.rotarycraft.blockentities.farming.BlockEntityComposter;
import reika.rotarycraft.blockentities.farming.BlockEntityGroundHydrator;
import reika.rotarycraft.blockentities.farming.BlockEntityFan;
import reika.rotarycraft.blockentities.farming.BlockEntityMobHarvester;
import reika.rotarycraft.blockentities.farming.BlockEntitySprinkler;
import reika.rotarycraft.blockentities.farming.BlockEntityWoodcutter;
import reika.rotarycraft.blockentities.processing.BlockEntityCompactor;
import reika.rotarycraft.blockentities.processing.BlockEntityDropProcessor;
import reika.rotarycraft.blockentities.farming.BlockEntityAutoBreeder;
import reika.rotarycraft.blockentities.processing.BlockEntityCrystallizer;
import reika.rotarycraft.blockentities.farming.BlockEntityFertilizer;
import reika.rotarycraft.blockentities.farming.BlockEntityLawnSprinkler;
import reika.rotarycraft.blockentities.transmission.BlockEntityBeltHub;
import reika.rotarycraft.blockentities.transmission.BlockEntitySplitBelt;
import reika.rotarycraft.blockentities.transmission.BlockEntityChainDrive;
import reika.rotarycraft.blockentities.auxiliary.BlockEntityEngineController;
import reika.rotarycraft.blockentities.farming.BlockEntitySpawnerController;
import reika.rotarycraft.blockentities.processing.BlockEntityWetter;
import reika.rotarycraft.blockentities.processing.BlockEntityDryingBed;
import reika.rotarycraft.blockentities.level.*;
import reika.rotarycraft.blockentities.piping.*;
import reika.rotarycraft.blockentities.processing.BlockEntityCentrifuge;
import reika.rotarycraft.blockentities.processing.BlockEntityLavaMaker;
import reika.rotarycraft.blockentities.processing.BlockEntityExtractor;
import reika.rotarycraft.blockentities.processing.BlockEntityGrinder;
import reika.rotarycraft.blockentities.processing.BlockEntityLavaSmeltery;
import reika.rotarycraft.blockentities.processing.BlockEntityMagnetizer;
import reika.rotarycraft.blockentities.processing.BlockEntityPulseFurnace;
import reika.rotarycraft.blockentities.production.*;
import reika.rotarycraft.blockentities.storage.BlockEntityReservoir;
import reika.rotarycraft.blockentities.storage.BlockEntityScaleableChest;
import reika.rotarycraft.blockentities.surveying.BlockEntityCaveFinder;
import reika.rotarycraft.blockentities.surveying.BlockEntityGPR;
import reika.rotarycraft.blockentities.transmission.*;
import reika.rotarycraft.blockentities.weaponry.*;
import reika.rotarycraft.modinterface.conversion.BlockEntityBoiler;
import reika.rotarycraft.modinterface.conversion.BlockEntityMagnetEngine;
import reika.rotarycraft.modinterface.conversion.BlockEntitySteam;

import java.util.Locale;
import java.util.function.Function;

/**
 * ONLY ADD NEW MACHINES TO THE BOTTOM OF THIS LIST
 */
public enum MachineRegistry implements TileEnum {

    WIND_ENGINE("machine.wind_engine", RotaryBlocks.WIND_ENGINE.get(), BlockEntityWindEngine.class, EngineType.WIND),
    STEAM_ENGINE(true, "machine.steam_engine", RotaryBlocks.STEAM_ENGINE.get(), BlockEntitySteamEngine.class, EngineType.STEAM),
    PERFORMANCE_ENGINE(true, "machine.performance_engine", RotaryBlocks.PERFORMANCE_ENGINE.get(), BlockEntityPerformanceEngine.class, EngineType.SPORT),
    MICRO_TURBINE(true, "machine.micro_turbine", RotaryBlocks.MICRO_TURBINE.get(), BlockEntityMicroturbine.class, EngineType.MICRO),
    GAS_ENGINE(true, "machine.gas_engine", RotaryBlocks.GAS_ENGINE.get(), BlockEntityGasEngine.class, EngineType.GAS),
    DC_ENGINE("machine.dc_engine", RotaryBlocks.DC_ENGINE.get(), BlockEntityDCEngine.class, EngineType.DC),
    AC_ENGINE("machine.ac_engine", RotaryBlocks.AC_ENGINE.get(), BlockEntityACEngine.class, EngineType.AC),
    JET_ENGINE(true, "machine.jet_engine", RotaryBlocks.JET_ENGINE.get(), BlockEntityJetEngine.class, EngineType.JET),

    FLYWHEEL(true, "machine.flywheel", RotaryBlocks.HSLA_FLYWHEEL.get(), BlockEntityFlywheel.class),
    WOOD_SHAFT("machine.shaft", RotaryBlocks.WOOD_SHAFT.get(), BlockEntityShaft.class),
    STONE_SHAFT("machine.shaft", RotaryBlocks.STONE_SHAFT.get(), BlockEntityShaft.class),
    HSLA_SHAFT("machine.shaft", RotaryBlocks.HSLA_SHAFT.get(), BlockEntityShaft.class),
    TUNGSTEN_SHAFT("machine.shaft", RotaryBlocks.TUNGSTEN_SHAFT.get(), BlockEntityShaft.class),
    DIAMOND_SHAFT("machine.shaft", RotaryBlocks.DIAMOND_SHAFT.get(), BlockEntityShaft.class),
    BEDROCK_SHAFT("machine.shaft", RotaryBlocks.BEDROCK_SHAFT.get(), BlockEntityShaft.class),
    SHAFT_CROSS("machine.shaft", RotaryBlocks.SHAFT_CROSS.get(), BlockEntityShaft.class),
    SHAFT_MERGE("machine.shaft", RotaryBlocks.SHAFT_MERGE.get(), BlockEntityShaft.class),

    BEVELGEARS(true, "machine.bevel", RotaryBlocks.BEVEL_GEARS.get(), BlockEntityBevelGear.class),
    GEARBOX(true, "machine.gearbox", RotaryBlocks.HSLA_GEARBOX_2x.get(), BlockEntityGearbox.class),
    SPLITTER(true, "machine.splitter", RotaryBlocks.SPLITTER.get(), BlockEntitySplitter.class),
    //            FERMENTER("machine.fermenter", BlockRotaryCraftMachine.class, BlockEntityFermenter.class),
    FLOODLIGHT("machine.floodlight", RotaryBlocks.FLOODLIGHT.get(), BlockEntityFloodlight.class),
    CLUTCH("machine.clutch", RotaryBlocks.CLUTCH.get(), BlockEntityClutch.class),
    DYNAMOMETER("machine.dyna", RotaryBlocks.DYNAMOMETER.get(), BlockEntityMonitor.class),
    GRINDER(true, "machine.grinder", RotaryBlocks.GRINDER.get(), BlockEntityGrinder.class),
    HEATRAY("machine.heatray", RotaryBlocks.HEAT_RAY.get(), BlockEntityHeatRay.class),

    //                       PIPES
    HOSE("machine.hose", RotaryBlocks.HOSE.get(), BlockEntityHose.class),
    PIPE("machine.pipe", RotaryBlocks.FLUID_PIPE.get(), BlockEntityPipe.class),
    FUELLINE("machine.fuelline", RotaryBlocks.FUEL_LINE.get(), BlockEntityFuelLine.class),
    VALVE("machine.valve", RotaryBlocks.VALVE.get(), BlockEntityValve.class),
    BYPASS("machine.bypass", RotaryBlocks.BYPASS.get(), BlockEntityBypass.class),
    SEPARATION("machine.separation", RotaryBlocks.SEPARATION.get(), BlockEntitySeparatorPipe.class),
    SUCTION("machine.suction", RotaryBlocks.SUCTION.get(), BlockEntitySuctionPipe.class),
    BEDPIPE("machine.bedpipe", RotaryBlocks.BEDROCK_PIPE.get(), BlockEntityBedrockPipe.class),

    BORER(true, "machine.borer", RotaryBlocks.BORER.get(), BlockEntityBorer.class),
    LIGHTBRIDGE("machine.lightbridge", RotaryBlocks.LIGHT_BRIDGE.get(), BlockEntityLightBridge.class),
    PUMP("machine.pump", RotaryBlocks.PUMP.get(), BlockEntityPump.class),
    RESERVOIR(true, "machine.reservoir", RotaryBlocks.RESERVOIR.get(), BlockEntityReservoir.class),
    AEROSOLIZER(true, "machine.aerosolizer", RotaryBlocks.AEROSOLIZER.get(), BlockEntityAerosolizer.class),
    //    EXTRACTOR(true, "machine.extractor", BlockRotaryCraftMachine.class, BlockEntityExtractor.class, "RenderExtractor"),
    PULSEJET(true, "machine.pulsejet", RotaryBlocks.PULSE_JET_FURNACE.get(), BlockEntityPulseFurnace.class),
    COMPACTOR(true, "machine.compactor", RotaryBlocks.COMPACTOR.get(), BlockEntityCompactor.class),
   FAN("machine.fan", RotaryBlocks.FAN.get(), BlockEntityFan.class),
    FRACTIONATOR(true, "machine.fractionator", RotaryBlocks.FRACTIONATOR.get(), BlockEntityFractionator.class),
    GPR(true, "machine.gpr", RotaryBlocks.GPR.get(), BlockEntityGPR.class),
    OBSIDIAN(true, "machine.obsidian", RotaryBlocks.OBSIDIAN_MAKER.get(), BlockEntityObsidianMaker.class),
    //    PILEDRIVER("machine.piledriver", BlockRotaryCraftMachine.class, BlockEntityPileDriver.class, "RenderPileDriver"),
    VACUUM(true, "machine.vacuum", RotaryBlocks.VACUUM.get(), BlockEntityVacuum.class),
    FIREWORK("machine.firework", RotaryBlocks.FIREWORK.get(), reika.rotarycraft.blockentities.decorative.BlockEntityFireworkMachine.class),
    SPRINKLER(true, "machine.sprinkler", RotaryBlocks.SPRINKLER.get(), BlockEntitySprinkler.class),
    WOODCUTTER("machine.woodcutter", RotaryBlocks.WOODCUTTER.get(), BlockEntityWoodcutter.class),
    SPAWNERCONTROLLER("machine.spawnercontroller", RotaryBlocks.SPAWNERCONTROLLER.get(), BlockEntitySpawnerController.class),
    PLAYERDETECTOR(true, "machine.playerdetector", RotaryBlocks.PLAYER_DETECTOR.get(), BlockEntityPlayerDetector.class),
    HEATER(true, "machine.heater", RotaryBlocks.HEATER.get(), BlockEntityHeater.class),
    //    BAITBOX(true, "machine.baitbox", BlockRotaryCraftMachine.class, BlockEntityBaitBox.class, "RenderBaitBox"),
    AUTOBREEDER(true, "machine.breeder", RotaryBlocks.AUTOBREEDER.get(), BlockEntityAutoBreeder.class),
    ECU("machine.ecu", RotaryBlocks.ECU.get(), BlockEntityEngineController.class),
    SMOKEDETECTOR("machine.smokedetector", RotaryBlocks.SMOKE_DETECTOR.get(), BlockEntitySmokeDetector.class),
    //    MOBRADAR("machine.mobradar", BlockRotaryCraftMachine.class, BlockEntityMobRadar.class, "RenderMobRadar"),
    WINDER(true, "machine.winder", RotaryBlocks.WINDER.get(), BlockEntityWinder.class),
    WORMGEAR("machine.advgear", RotaryBlocks.WORMGEAR.get(), BlockEntityAdvancedGear.class),
    CVT(true, "machine.advgear", RotaryBlocks.CVT.get(), BlockEntityAdvancedGear.class),
    HIGHGEAR(true, "machine.advgear", RotaryBlocks.HIGHGEAR.get(), BlockEntityAdvancedGear.class),
    COIL(true, "machine.advgear", RotaryBlocks.COIL.get(), BlockEntityAdvancedGear.class),
    CREATIVE_COIL(true, "machine.creativecoil", RotaryBlocks.CREATIVE_COIL.get(), BlockEntityCreativeCoil.class),

    TNTCANNON(true, "machine.tntcannon", RotaryBlocks.TNT_CANNON.get(), BlockEntityTNTCannon.class),
    //    SONICWEAPON(true, "machine.sonicweapon", BlockRotaryCraftMachine.class, BlockEntitySonicWeapon.class, "RenderSonic"),
    BLASTFURNACE(true, "machine.blastfurnace", RotaryBlocks.BLAST_FURNACE.get(), BlockEntityBlastFurnace.class),
    FORCEFIELD(true, "machine.forcefield", RotaryBlocks.FORCE_FIELD.get(), BlockEntityForceField.class),
    MUSICBOX(true, "machine.musicbox", RotaryBlocks.MUSIC_BOX.get(), BlockEntityMusicBox.class),
    SPILLER(true, "machine.spiller", RotaryBlocks.SPILLER.get(), BlockEntitySpiller.class),
    //    CHUNKLOADER("machine.chunkloader", BlockRotaryCraftMachine.class, BlockEntityChunkLoader.class, "RenderChunkLoader"),
    MOBHARVESTER("machine.mobharvester", RotaryBlocks.MOB_HARVESTER.get(), BlockEntityMobHarvester.class),
    //    CCTV("machine.cctv", BlockRotaryCraftMachine.class, BlockEntityCCTV.class, "RenderCCTV"),
//    PROJECTOR("machine.projector", BlockRotaryCraftMachine.class, BlockEntityProjector.class, "RenderProjector"),
    RAILGUN("machine.railgun", RotaryBlocks.RAILGUN.get(), reika.rotarycraft.blockentities.weaponry.Turret.BlockEntityRailGun.class),
    WEATHERCONTROLLER("machine.weather", RotaryBlocks.WEATHER_CONTROLLER.get(), BlockEntityWeatherController.class),
    REFRESHER("machine.refresher", RotaryBlocks.REFRESHER.get(), BlockEntityItemRefresher.class),
    FREEZEGUN("machine.freezegun", RotaryBlocks.FREEZE_GUN.get(), reika.rotarycraft.blockentities.weaponry.Turret.BlockEntityFreezeGun.class),
    CAVESCANNER("machine.cavescanner", RotaryBlocks.CAVE_SCANNER.get(), BlockEntityCaveFinder.class),
    SCALECHEST(true, "machine.chest", RotaryBlocks.SCALECHEST.get(), BlockEntityScaleableChest.class),
    IGNITER(true, "machine.firestarter", RotaryBlocks.IGNITER.get(), BlockEntityIgniter.class),
    MAGNETIZER("machine.magnetizer", RotaryBlocks.MAGNETIZER.get(), BlockEntityMagnetizer.class),
    CONTAINMENT("machine.containment", RotaryBlocks.CONTAINMENT.get(), BlockEntityContainment.class),
    //    SCREEN("machine.screen", BlockRotaryCraftMachine.class, BlockEntityScreen.class, "RenderCCTVScreen"),
//    PURIFIER("machine.purifier", BlockRotaryCraftMachine.class, BlockEntityPurifier.class),
    LASERGUN("machine.lasergun", RotaryBlocks.LASER_GUN.get(), reika.rotarycraft.blockentities.weaponry.Turret.BlockEntityLaserGun.class),
    ITEMCANNON("machine.itemcannon", RotaryBlocks.ITEM_CANNON.get(), BlockEntityItemCannon.class),
    LANDMINE("machine.landmine", RotaryBlocks.LANDMINE.get(), BlockEntityLandmine.class),
    FRICTION("machine.friction", RotaryBlocks.FRICTION_HEATER.get(), BlockEntityFurnaceHeater.class),
    BLOCKCANNON("machine.blockcannon", RotaryBlocks.BLOCK_CANNON.get(), BlockEntityBlockCannon.class),
    BUCKETFILLER("machine.bucketfiller", RotaryBlocks.BUCKET_FILLER.get(), BlockEntityBucketFiller.class),
    MIRROR("machine.mirror", RotaryBlocks.MIRROR.get(), BlockEntityMirror.class),
    SOLARTOWER("machine.solartower", RotaryBlocks.SOLAR_TOWER.get(), BlockEntitySolarTower.class),
    //    SPYCAM("machine.spycam", BlockRotaryCraftMachine.class, BlockEntitySpyCam.class, "RenderSpyCam"),
    SELFDESTRUCT("machine.selfdestruct", RotaryBlocks.SELF_DESTRUCT.get(), BlockEntitySelfDestruct.class),
    COOLINGFIN("machine.coolingfin", RotaryBlocks.COOLING_FIN.get(), BlockEntityCoolingFin.class),
    WORKTABLE(true, "machine.worktable", RotaryBlocks.WORKTABLE.get(), BlockEntityWorktable.class), // 26.1: hasGui=true so right-click opens GuiWorktable
    //    COMPRESSOR("machine.compressor", BlockModEngine.class, BlockEntityAirCompressor.class, "RenderCompressor", PowerTypes.PNEUMATIC),
    //PNEUENGINE("machine.pneuengine", BlockModEngine.class, BlockEntityPneumaticEngine.class, "RenderPneumatic", PowerTypes.PNEUMATIC),
//    DISPLAY("machine.display", BlockRotaryCraftMachine.class, BlockEntityDisplay.class, "RenderDisplay"),
    LAMP("machine.lamp", RotaryBlocks.LAMP.get(), BlockEntityLamp.class),
    EMP("machine.emp", RotaryBlocks.EMP.get(), reika.rotarycraft.blockentities.weaponry.BlockEntityEMP.class),
    LINEBUILDER("machine.linebuilder", RotaryBlocks.LINE_BUILDER.get(), BlockEntityLineBuilder.class),
    BEAMMIRROR("machine.beammirror", RotaryBlocks.BEAM_MIRROR.get(), BlockEntityBeamMirror.class),
    MULTICLUTCH("machine.multiclutch", RotaryBlocks.MULTI_CLUTCH.get(), BlockEntityMultiClutch.class),
    //    TERRAFORMER("machine.terraformer", BlockRotaryCraftMachine.class, BlockEntityTerraformer.class),
    SORTING("machine.sorting", RotaryBlocks.SORTER.get(), BlockEntitySorting.class),
    //    FUELENHANCER("machine.fuelenhancer", BlockRotaryCraftMachine.class, BlockEntityFuelConverter.class, "RenderFuelConverter"),
    ARROWGUN(true, "machine.arrowgun", RotaryBlocks.MACHINEGUN.get(), BlockEntityMachineGun.class),
    BOILER("machine.frictionboiler", RotaryBlocks.FRICTION_BOILER.get(), BlockEntityBoiler.class, PowerTypes.STEAM),
    STEAMTURBINE(true, "machine.steamturbine", RotaryBlocks.STEAM_TURBINE.get(), BlockEntitySteam.class, PowerTypes.STEAM),
    FERTILIZER(true, "machine.fertilizer", RotaryBlocks.FERTILIZER.get(), BlockEntityFertilizer.class),
    LAVAMAKER(true, "machine.lavamaker", RotaryBlocks.LAVAMAKER.get(), BlockEntityLavaMaker.class),
    //GENERATOR("machine.generator", BlockModEngine.class, BlockEntityGenerator.class, "RenderGenerator", PowerTypes.EU),
    //ELECTRICMOTOR("machine.electricmotor", BlockModEngine.class, BlockEntityElectricMotor.class, "RenderElecMotor", PowerTypes.EU),
    AGGREGATOR("machine.aggregator", RotaryBlocks.AGGREGATOR.get(), reika.rotarycraft.blockentities.production.BlockEntityAggregator.class),
    AIRGUN("machine.airgun", RotaryBlocks.AIRGUN.get(), BlockEntityAirGun.class),
    SONICBORER("machine.sonicborer", RotaryBlocks.SONICBORER.get(), BlockEntitySonicBorer.class),
//    FUELENGINE("machine.fuelengine", BlockModEngine.class, BlockEntityFuelEngine.class, "RenderFuelEngine", ModList.BCENERGY),
    FILLINGSTATION(true, "machine.fillingstation", RotaryBlocks.FILLING_STATION.get(), BlockEntityFillingStation.class),
    BELT("machine.belt", RotaryBlocks.BELT.get(), BlockEntityBeltHub.class),
    SPLITBELT("machine.splitbelt", RotaryBlocks.SPLITBELT.get(), BlockEntitySplitBelt.class),
    VANDEGRAFF("machine.vandegraff", RotaryBlocks.VAN_DE_GRAFF.get(), BlockEntityVanDeGraff.class),
    //    DEFOLIATOR("machine.defoliator", BlockRotaryCraftMachine.class, BlockEntityDefoliator.class, "RenderDefoliator"),
    BIGFURNACE(true, "machine.bigfurnace", RotaryBlocks.LAVA_SMELTORY.get(), BlockEntityLavaSmeltery.class, PowerTypes.RF),
    //    DISTILLER("machine.distiller", BlockRotaryCraftMachine.class, BlockEntityDistillery.class, "RenderDistillery"),
    //    DYNAMO("machine.dynamo", BlockModEngine.class, BlockEntityDynamo.class, "RenderDynamo", PowerTypes.RF),
    MAGNETIC(true, "machine.magnetic", RotaryBlocks.MAGNETOSTATIC_ENGINE.get(), BlockEntityMagnetEngine.class, PowerTypes.RF),
    CRYSTALLIZER("machine.crystal", RotaryBlocks.CRYSTALLIZER.get(), BlockEntityCrystallizer.class),
    BUSCONTROLLER("machine.buscontroller", RotaryBlocks.BUSCONTROLLER.get(), BlockEntityBusController.class),
    POWERBUS("machine.bus", RotaryBlocks.POWERBUS.get(), BlockEntityPowerBus.class),
    PARTICLE(true, "machine.particle", RotaryBlocks.PARTICLE.get(), BlockEntityParticleEmitter.class),
        LAWNSPRINKLER("machine.lawnsprinkler", RotaryBlocks.LAWNSPRINKLER.get(), BlockEntityLawnSprinkler.class),
    GRINDSTONE(true, "machine.grindstone", RotaryBlocks.GRINDSTONE.get(), BlockEntityGrindstone.class),
    BLOWER("machine.blower", RotaryBlocks.BLOWER.get(), BlockEntityBlower.class), // no custom model in original
    //    PORTALSHAFT("machine.portalshaft", BlockRotaryCraftMachine.class, BlockEntityPortalShaft.class, "RenderPortalShaft"),
    REFRIGERATOR(true, "machine.refrigerator", RotaryBlocks.REFRIGERATOR.get(), BlockEntityRefrigerator.class),
    GASTANK("machine.gastank", RotaryBlocks.GASTANK.get(), reika.rotarycraft.blockentities.storage.BlockEntityFluidCompressor.class),
//    CRAFTER("machine.crafter", BlockRotaryCraftMachine.class, BlockEntityAutoCrafter.class),
    COMPOSTER("machine.composter", RotaryBlocks.COMPOSTER.get(), BlockEntityComposter.class),
    ANTIAIR("machine.antiair", RotaryBlocks.AA_GUN.get(), reika.rotarycraft.blockentities.weaponry.Turret.BlockEntityAAGun.class),
    PIPEPUMP("machine.pipepump", RotaryBlocks.PIPEPUMP.get(), BlockEntityPipePump.class),
    CHAIN("machine.chain", RotaryBlocks.CHAIN.get(), BlockEntityChainDrive.class),
    CENTRIFUGE(true, "machine.centrifuge", RotaryBlocks.CENTRIFUGE.get(), BlockEntityCentrifuge.class),
    DRYING("machine.drying", RotaryBlocks.DRYING.get(), BlockEntityDryingBed.class),
    WETTER("machine.wetter", RotaryBlocks.WETTER.get(), BlockEntityWetter.class),
    DROPS("machine.drops", RotaryBlocks.DROPS.get(), BlockEntityDropProcessor.class),
//    ITEMFILTER("machine.itemfilter", BlockRotaryCraftMachine.class, BlockEntityItemFilter.class),
    HYDRATOR("machine.hydrator", RotaryBlocks.HYDRATOR.get(), BlockEntityGroundHydrator.class),
    FILLER("machine.filler", RotaryBlocks.FILLER.get(), BlockEntityBlockFiller.class),
    GATLING("machine.gatling", RotaryBlocks.MULTI_CANNON.get(), reika.rotarycraft.blockentities.weaponry.Turret.BlockEntityMultiCannon.class),
    SPILLWAY("machine.spillway", RotaryBlocks.SPILLWAY.get(), BlockEntitySpillway.class),
    //    FLAMETURRET("machine.flameturret", BlockRotaryCraftMachine.class, BlockEntityFlameTurret.class, "RenderFlameTurret"),
//    BUNDLEDBUS("machine.bundledbus", BlockRotaryCraftMachine.class, BlockEntityBundledBus.class, ModList.APPENG, ModList.PROJRED),
    DISTRIBCLUTCH("machine.distribclutch", RotaryBlocks.DISTRIBUTION_CLUTCH.get(), BlockEntityDistributionClutch.class),
    BEDROCKBREAKER("machine.bedrock", RotaryBlocks.BEDROCK_BREAKER.get(), BlockEntityBedrockBreaker.class),
    HYDRO_ENGINE("machine.hydro_engine", RotaryBlocks.HYDRO_ENGINE.get(), BlockEntityHydroEngine.class, EngineType.HYDRO),
    FERMENTER(true, "machine.fermenter", RotaryBlocks.FERMENTER.get(), BlockEntityFermenter.class),
    EXTRACTOR(true, "machine.extractor", RotaryBlocks.EXTRACTOR.get(), BlockEntityExtractor.class);

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
        // 26.1 PERF: take the {@link BlockPos} directly, avoiding the {@code new BlockPos(x,y,z)}
        // allocation that the int-coord overload's getBlockState call would do otherwise. With
        // ~30 pipes × 20 TPS × ~12 getMachine calls each (intake/dump/recompute) that's ~7200
        // calls/sec — eliminating the inner allocation pulls a measurable allocation-rate
        // reduction (GC pause frequency was tracking the same number in the user's PipeDbg log).
        // The caller may pass a {@link BlockPos.MutableBlockPos}; vanilla's getBlockState only
        // reads the pos's packed-long coordinates, so it's safe.
        Block b = level.getBlockState(pos).getBlock();
        if (b == Blocks.AIR)
            return null;
        return getMachineMapping(b);
    }

    public static MachineRegistry getMachine(BlockGetter world, int x, int y, int z) {
        Block b = world.getBlockState(new BlockPos(x, y, z)).getBlock();
        if (b == Blocks.AIR)
            return null;
        return getMachineMapping(b);
    }

    public static MachineRegistry getMachineMapping(Block id) {
        if (id == RotaryBlocks.GPR.get())
            return GPR;
        if (id == RotaryBlocks.BEDROCK_GEARBOX_2x.get() || id == RotaryBlocks.BEDROCK_GEARBOX_4x.get() ||
                id == RotaryBlocks.BEDROCK_GEARBOX_8x.get() || id == RotaryBlocks.BEDROCK_GEARBOX_16x.get() ||

                id == RotaryBlocks.HSLA_GEARBOX_2x.get() || id == RotaryBlocks.HSLA_GEARBOX_4x.get()
                || id == RotaryBlocks.HSLA_GEARBOX_8x.get() || id == RotaryBlocks.HSLA_GEARBOX_16x.get()

                || id == RotaryBlocks.DIAMOND_GEARBOX_2x.get() || id == RotaryBlocks.DIAMOND_GEARBOX_4x.get() ||
                id == RotaryBlocks.DIAMOND_GEARBOX_8x.get() || id == RotaryBlocks.DIAMOND_GEARBOX_16x.get()

                || id == RotaryBlocks.WOOD_GEARBOX_2x.get() || id == RotaryBlocks.WOOD_GEARBOX_4x.get()
                || id == RotaryBlocks.WOOD_GEARBOX_8x.get() || id == RotaryBlocks.WOOD_GEARBOX_16x.get()

                || id == RotaryBlocks.TUNGSTEN_GEARBOX_2x.get()  || id == RotaryBlocks.TUNGSTEN_GEARBOX_4x.get()
                || id == RotaryBlocks.TUNGSTEN_GEARBOX_8x.get()  || id == RotaryBlocks.TUNGSTEN_GEARBOX_16x.get()

                || id == RotaryBlocks.STONE_GEARBOX_2x.get()  || id == RotaryBlocks.STONE_GEARBOX_4x.get()
                || id == RotaryBlocks.STONE_GEARBOX_8x.get()  || id == RotaryBlocks.STONE_GEARBOX_16x.get() )
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
        if (this == WOODCUTTER)
            return 0.0625F;
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
        if (this == WOODCUTTER)
            return 0.0625F;
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
        if (this == WOODCUTTER)
            return 0.9375F;
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
        if (this == WOODCUTTER)
            return 0.9375F;
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
        // Component.translatable, not the client-only I18n: this is called during registry setup
        // (PowerReceivers.initialize) on a dedicated server, where I18n does not exist. On the client
        // this still resolves through the active language; on the server it yields the key, which is
        // what the server has always been able to say about a translation.
        return net.minecraft.network.chat.Component
                .translatable("rcmachine" + name.toLowerCase(Locale.ROOT)).getString();
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
            case COMPACTOR, HEATER, /*IGNITER,*/ OBSIDIAN /*, PULSEJET, FRICTION*/ -> true;
            default -> false;
        };
    }

    public boolean is4Sided() {
        return switch (this) {
            case DC_ENGINE, /*BORER, LIGHTBRIDGE,*/ FLYWHEEL, GEARBOX, SPLITTER, /*FERMENTER,*/ DYNAMOMETER, GRINDER, HEATRAY, COMPACTOR, WOODCUTTER, WINDER, WORMGEAR, HIGHGEAR, CVT, COIL, /*BLASTFURNACE, PROJECTOR, SCALECHEST, MAGNETIZER, SCREEN, FRICTION, DISPLAY,*/
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

	public boolean hasNBTVariants() {
		return NBTMachine.class.isAssignableFrom(te);
	}

	public boolean hasTemperature() {
		return TemperatureTE.class.isAssignableFrom(te);
	}

	public boolean isTransmissionMachine() {
		return BlockEntityTransmissionMachine.class.isAssignableFrom(te);
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
        return !this.isConfigDisabled();
//        todo if (this == PORTALSHAFT)
//            return false;
    }

    public boolean isDummiedOut() {
//        if (this == CCTV)
//            return true;
        if (requirement != null && !requirement.isLoaded())
            return true;
        return powertype != null && !powertype.isLoaded();
    }

    public boolean hasPrerequisite() {
        return requirement != null || powertype != null;
    }

    public boolean renderInPass1() {
        if (this == COOLINGFIN)
            return true;
//        if (this == DISPLAY)
//            return true;
        return this == PUMP;
//        return this == EMP;
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
        //            case BORER, SONICBORER, EMP, RAILGUN, LASERGUN -> true;
        return false;
    }

    public boolean isUncraftable() {
        return this != MachineRegistry.COOLINGFIN;
    }

    public boolean canDoMultiPerTick() {
//        return this == EXTRACTOR || MultiOperational.class.isAssignableFrom(this.getTEClass());
        return false;
    }

    static {
        for (int i = 0; i < machineList.length; i++) {
            var m = machineList.get(i);
            RotaryCraft.LOGGER.info("M on it's own: " + m);
            RotaryCraft.LOGGER.info("Name: " + m.getName());
            if (m.getBlockState() != null) {
                var id = m.getBlockState().getBlock();
                RotaryCraft.LOGGER.info("ID: " + id);
                if (machineMappings.containsKey(id))
                    throw new RegistrationException(RotaryCraft.getInstance(), "BlockState conflict " + id + ": " + m + " & " + machineMappings.get(id));
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
                 AC_ENGINE, BLOCKCANNON, ITEMCANNON, DYNAMOMETER, HEATRAY, GRINDSTONE, MIRROR, FRICTION, MAGNETIZER, CRYSTALLIZER, SONICBORER, AIRGUN -> true;
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

    /**
     * Is the machine crucial to the mod (i.e. the techtree, realism, usability, or balance is damaged by its removal)
     */
    public boolean isCrucial() {
        if (this.isPipe())
            return true;
        if (this.isCritical())
            return true;
        return switch (this) {
            case BEDROCKBREAKER, STEAM_ENGINE,
                    PERFORMANCE_ENGINE,
                    MICRO_TURBINE,
                    GAS_ENGINE,
                    DC_ENGINE,
                    AC_ENGINE, WOOD_SHAFT,
                    STONE_SHAFT,
                    HSLA_SHAFT,
                    TUNGSTEN_SHAFT,
                    DIAMOND_SHAFT,
                    BEDROCK_SHAFT, BEVELGEARS, SPLITTER, GEARBOX, DYNAMOMETER, FERMENTER, GRINDER, COMPACTOR, BORER, PUMP, EXTRACTOR, FAN, FRACTIONATOR, WOODCUTTER, SPAWNERCONTROLLER, HEATER, HEATRAY, ECU, WINDER, CVT, WORMGEAR, BLASTFURNACE, MOBHARVESTER, MAGNETIZER, FRICTION, MIRROR, SOLARTOWER, COOLINGFIN, WORKTABLE, /*COMPRESSOR, DYNAMO,*/ MULTICLUTCH, SORTING,FERTILIZER, MAGNETIC, LAVAMAKER, AGGREGATOR, FILLINGSTATION, BELT, VANDEGRAFF, BUSCONTROLLER, POWERBUS, BIGFURNACE, CRYSTALLIZER, BLOWER, REFRIGERATOR, /*CRAFTER,*/ COMPOSTER, CENTRIFUGE, PIPEPUMP, DRYING, WETTER ->
                    true;
            default -> false;
        };
    }

    public boolean isCritical() {
        if (this.isPipe())
            return true;
        return switch (this) {
            case BEDROCKBREAKER, /*ENGINE, SHAFT, */
                    BEVELGEARS, SPLITTER, GEARBOX,
                            FERMENTER, GRINDER,/* COMPACTOR,*/ PUMP,
                            EXTRACTOR, FAN, FRACTIONATOR, HEATER,
                            HEATRAY, WINDER, /*ADVANCEDGEARS, */BLASTFURNACE,
                    MAGNETIZER, FRICTION, COOLINGFIN, WORKTABLE,
                            MULTICLUTCH, SORTING, FERTILIZER, AGGREGATOR,
                    FILLINGSTATION, BELT, VANDEGRAFF, BUSCONTROLLER,
                    POWERBUS, BLOWER, REFRIGERATOR, CENTRIFUGE/*, CRAFTER*/,
                    PIPEPUMP, DRYING, WETTER  -> true;
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

    public ItemStack getCraftedProduct() {
        return new ItemStack(block);
    }


}
