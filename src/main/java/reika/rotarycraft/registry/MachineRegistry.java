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

import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
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

import java.util.Locale;

/**
 * ONLY ADD NEW MACHINES TO THE BOTTOM OF THIS LIST
 */
public enum MachineRegistry implements TileEnum {

    //        BEDROCKBREAKER("machine.bedrock", BlockRotaryCraftMachine.class, BlockEntityBedrockBreaker.class),
    WIND_ENGINE("machine.wind_engine", RotaryBlocks.WIND_ENGINE.get(), BlockEntityWindEngine.class, EngineType.WIND),
    STEAM_ENGINE("machine.steam_engine", RotaryBlocks.STEAM_ENGINE.get(), BlockEntitySteamEngine.class, EngineType.STEAM),
    PERFORMANCE_ENGINE("machine.performance_engine", RotaryBlocks.PERFORMANCE_ENGINE.get(), BlockEntityPerformanceEngine.class, EngineType.SPORT),
    MICRO_TURBINE("machine.micro_turbine", RotaryBlocks.MICRO_TURBINE.get(), BlockEntityMicroturbine.class, EngineType.MICRO),
    GAS_ENGINE("machine.gas_engine", RotaryBlocks.GAS_ENGINE.get(), BlockEntityGasEngine.class, EngineType.GAS),
    DC_ENGINE("machine.dc_engine", RotaryBlocks.DC_ENGINE.get(), BlockEntityDCEngine.class, EngineType.DC),
    AC_ENGINE("machine.ac_engine", RotaryBlocks.AC_ENGINE.get(), BlockEntityACEngine.class, EngineType.AC),

    FLYWHEEL("machine.flywheel", RotaryBlocks.HSLA_FLYWHEEL.get(), BlockEntityFlywheel.class),
    SHAFT("machine.shaft", RotaryBlocks.HSLA_SHAFT.get(), BlockEntityShaft.class),
    BEVELGEARS("machine.bevel", RotaryBlocks.BEVEL_GEARS.get(), BlockEntityBevelGear.class),
    GEARBOX("machine.gearbox", RotaryBlocks.HSLA_GEARBOX_2x.get(), BlockEntityGearbox.class),
    SPLITTER("machine.splitter", RotaryBlocks.SPLITTER.get(), BlockEntitySplitter.class),
    //            FERMENTER("machine.fermenter", BlockRotaryCraftMachine.class, BlockEntityFermenter.class),
    FLOODLIGHT("machine.floodlight", RotaryBlocks.FLOODLIGHT.get(), BlockEntityFloodlight.class),
    CLUTCH("machine.clutch", RotaryBlocks.CLUTCH.get(), BlockEntityClutch.class),
    DYNAMOMETER("machine.dyna", RotaryBlocks.DYNAMOMETER.get(), BlockEntityMonitor.class),
    GRINDER("machine.grinder", RotaryBlocks.GRINDER.get(), BlockEntityGrinder.class),
    HEATRAY("machine.heatray", RotaryBlocks.HEAT_RAY.get(), BlockEntityHeatRay.class),

    //                       PIPES
    HOSE("machine.hose", RotaryBlocks.HOSE.get(), BlockEntityHose.class),
    PIPE("machine.pipe", RotaryBlocks.PIPE.get(), BlockEntityPipe.class),
    FUELLINE("machine.fuelline", RotaryBlocks.FUEL_LINE.get(), BlockEntityFuelLine.class),
    VALVE("machine.valve", RotaryBlocks.VALVE.get(), BlockEntityValve.class),
    BYPASS("machine.bypass", RotaryBlocks.BYPASS.get(), BlockEntityBypass.class),
    SEPARATION("machine.separation", RotaryBlocks.SEPARATION.get(), BlockEntitySeparatorPipe.class),
    SUCTION("machine.suction", RotaryBlocks.SUCTION.get(), BlockEntitySuctionPipe.class),
    BEDPIPE("machine.bedpipe", RotaryBlocks.BEDROCK_PIPE.get(), BlockEntityBedrockPipe.class),

    //    BORER("machine.borer", BlockRotaryCraftMachine.class, BlockEntityBorer.class),
//    LIGHTBRIDGE("machine.lightbridge", BlockRotaryCraftMachine.class, BlockEntityLightBridge.class, "RenderBridge"),
    PUMP("machine.pump", RotaryBlocks.PUMP.get(), BlockEntityPump.class),
    RESERVOIR("machine.reservoir", RotaryBlocks.RESERVOIR.get(), BlockEntityReservoir.class),
    AEROSOLIZER("machine.aerosolizer", RotaryBlocks.AEROSOLIZER.get(), BlockEntityAerosolizer.class),
    //    EXTRACTOR("machine.extractor", BlockRotaryCraftMachine.class, BlockEntityExtractor.class, "RenderExtractor"),
//    PULSEJET("machine.pulsejet", RotaryBlocks.PULSE_JET_FURNACE.get(), BlockEntityPulseFurnace.class),
//    COMPACTOR("machine.compactor", BlockRotaryCraftMachine.class, BlockEntityCompactor.class, "RenderCompactor"),
//    FAN("machine.fan", BlockRotaryCraftMachine.class, BlockEntityFan.class, "RenderFan"),
    //    FRACTIONATOR("machine.fractionator", BlockRotaryCraftMachine.class, BlockEntityFractionator.class, "RenderFraction"),
//    GPR("machine.gpr", BlockGPR.class, BlockEntityGPR.class),
    OBSIDIAN("machine.obsidian", RotaryBlocks.OBSIDIAN_MAKER.get(), BlockEntityObsidianMaker.class),
    //    PILEDRIVER("machine.piledriver", BlockRotaryCraftMachine.class, BlockEntityPileDriver.class, "RenderPileDriver"),
//    VACUUM("machine.vacuum", BlockRotaryCraftMachine.class, BlockEntityVacuum.class, "RenderVacuum"),
//    FIREWORK("machine.firework", BlockRotaryCraftMachine.class, BlockEntityFireworkMachine.class),
//    SPRINKLER("machine.sprinkler", BlockRotaryCraftMachine.class, BlockEntitySprinkler.class, "RenderSprinkler"),
//    WOODCUTTER("machine.woodcutter", BlockRotaryCraftMachine.class, BlockEntityWoodcutter.class, "RenderWoodcutter"),
//    SPAWNERCONTROLLER("machine.spawnercontroller", BlockRotaryCraftMachine.class, BlockEntitySpawnerController.class, "RenderSpawner"),
    PLAYERDETECTOR("machine.playerdetector", RotaryBlocks.PLAYER_DETECTOR.get(), BlockEntityPlayerDetector.class),
    HEATER("machine.heater", RotaryBlocks.HEATER.get(), BlockEntityHeater.class),
    //    BAITBOX("machine.baitbox", BlockRotaryCraftMachine.class, BlockEntityBaitBox.class, "RenderBaitBox"),
//    AUTOBREEDER("machine.breeder", BlockRotaryCraftMachine.class, BlockEntityAutoBreeder.class, "RenderBreeder"),
//    ECU("machine.ecu", BlockRotaryCraftMachine.class, BlockEntityEngineController.class),
    SMOKEDETECTOR("machine.smokedetector", RotaryBlocks.SMOKE_DETECTOR.get(), BlockEntitySmokeDetector.class),
    //    MOBRADAR("machine.mobradar", BlockRotaryCraftMachine.class, BlockEntityMobRadar.class, "RenderMobRadar"),
    WINDER("machine.winder", RotaryBlocks.WINDER.get(), BlockEntityWinder.class),
    WORMGEAR("machine.advgear", RotaryBlocks.WORMGEAR.get(), BlockEntityAdvancedGear.class),
    CVT("machine.advgear", RotaryBlocks.CVT.get(), BlockEntityAdvancedGear.class),
    HIGHGEAR("machine.advgear", RotaryBlocks.HIGHGEAR.get(), BlockEntityAdvancedGear.class),
    COIL("machine.advgear", RotaryBlocks.COIL.get(), BlockEntityAdvancedGear.class),

    TNTCANNON("machine.tntcannon", RotaryBlocks.TNT_CANNON.get(), BlockEntityTNTCannon.class),
    //    SONICWEAPON("machine.sonicweapon", BlockRotaryCraftMachine.class, BlockEntitySonicWeapon.class, "RenderSonic"),
//    BLASTFURNACE("machine.blastfurnace", BlockRotaryCraftMachine.class, BlockEntityBlastFurnace.class),
//    FORCEFIELD("machine.forcefield", BlockRotaryCraftMachine.class, BlockEntityForceField.class, "RenderForceField"),
    MUSICBOX("machine.musicbox", RotaryBlocks.MUSIC_BOX.get(), BlockEntityMusicBox.class),
    SPILLER("machine.spiller", RotaryBlocks.SPILLER.get(), BlockEntitySpiller.class),
    //    CHUNKLOADER("machine.chunkloader", BlockRotaryCraftMachine.class, BlockEntityChunkLoader.class, "RenderChunkLoader"),
    MOBHARVESTER("machine.mobharvester", RotaryBlocks.MOB_HARVESTER.get(), BlockEntityMobHarvester.class),
    //    CCTV("machine.cctv", BlockRotaryCraftMachine.class, BlockEntityCCTV.class, "RenderCCTV"),
//    PROJECTOR("machine.projector", BlockRotaryCraftMachine.class, BlockEntityProjector.class, "RenderProjector"),
//    RAILGUN("machine.railgun", BlockRotaryCraftMachine.class, BlockEntityRailGun.class, "RenderRailGun"),
//    WEATHERCONTROLLER("machine.weather", BlockRotaryCraftMachine.class, BlockEntityWeatherController.class, "RenderIodide"),
    REFRESHER("machine.refresher", RotaryBlocks.REFRESHER.get(), BlockEntityItemRefresher.class),
    //    FREEZEGUN("machine.freezegun", BlockRotaryCraftMachine.class, BlockEntityFreezeGun.class, "RenderFreezeGun"),
    CAVESCANNER("machine.cavescanner", RotaryBlocks.CAVE_SCANNER.get(), BlockEntityCaveFinder.class),
    //    SCALECHEST("machine.chest", BlockRotaryCraftMachine.class, BlockEntityScaleableChest.class, "RenderScaleChest"),
//    IGNITER("machine.firestarter", BlockRotaryCraftMachine.class, BlockEntityIgniter.class),
//    MAGNETIZER("machine.magnetizer", BlockRotaryCraftMachine.class, BlockEntityMagnetizer.class, "RenderMagnetizer"),
    CONTAINMENT("machine.containment", RotaryBlocks.CONTAINMENT.get(), BlockEntityContainment.class),
    //    SCREEN("machine.screen", BlockRotaryCraftMachine.class, BlockEntityScreen.class, "RenderCCTVScreen"),
//    PURIFIER("machine.purifier", BlockRotaryCraftMachine.class, BlockEntityPurifier.class),
//    LASERGUN("machine.lasergun", BlockRotaryCraftMachine.class, BlockEntityLaserGun.class, "RenderLaserGun"),
    ITEMCANNON("machine.itemcannon", RotaryBlocks.ITEM_CANNON.get(), BlockEntityItemCannon.class),
    LANDMINE("machine.landmine", RotaryBlocks.LANDMINE.get(), BlockEntityLandmine.class),
    //    FRICTION("machine.friction", RotaryBlocks.FRICTION_HEATER.get(), BlockEntityFurnaceHeater.class),
    BLOCKCANNON("machine.blockcannon", RotaryBlocks.BLOCK_CANNON.get(), BlockEntityBlockCannon.class),
    BUCKETFILLER("machine.bucketfiller", RotaryBlocks.BUCKET_FILLER.get(), BlockEntityBucketFiller.class),
    MIRROR("machine.mirror", RotaryBlocks.MIRROR.get(), BlockEntityMirror.class),
    SOLARTOWER("machine.solartower", RotaryBlocks.SOLAR_TOWER.get(), BlockEntitySolarTower.class),
    //    SPYCAM("machine.spycam", BlockRotaryCraftMachine.class, BlockEntitySpyCam.class, "RenderSpyCam"),
    SELFDESTRUCT("machine.selfdestruct", RotaryBlocks.SELF_DESTRUCT.get(), BlockEntitySelfDestruct.class),
    COOLINGFIN("machine.coolingfin", RotaryBlocks.COOLING_FIN.get(), BlockEntityCoolingFin.class),
    //        WORKTABLE("machine.worktable", BlockRotaryCraftMachine.class, BlockEntityWorktable.class),
//    COMPRESSOR("machine.compressor", BlockModEngine.class, BlockEntityAirCompressor.class, "RenderCompressor", PowerTypes.PNEUMATIC),
    //PNEUENGINE("machine.pneuengine", BlockModEngine.class, BlockEntityPneumaticEngine.class, "RenderPneumatic", PowerTypes.PNEUMATIC),
//    DISPLAY("machine.display", BlockRotaryCraftMachine.class, BlockEntityDisplay.class, "RenderDisplay"),
//    LAMP("machine.lamp", BlockRotaryCraftMachine.class, BlockEntityLamp.class),
//    EMP("machine.emp", BlockRotaryCraftMachine.class, BlockEntityEMP.class, "RenderEMP"),
    LINEBUILDER("machine.linebuilder", RotaryBlocks.LINE_BUILDER.get(), BlockEntityLineBuilder.class),
    BEAMMIRROR("machine.beammirror", RotaryBlocks.BEAM_MIRROR.get(), BlockEntityBeamMirror.class),
    MULTICLUTCH("machine.multiclutch", RotaryBlocks.MULTI_CLUTCH.get(), BlockEntityMultiClutch.class),
    //    TERRAFORMER("machine.terraformer", BlockRotaryCraftMachine.class, BlockEntityTerraformer.class),
    SORTING("machine.sorting", RotaryBlocks.SORTER.get(), BlockEntitySorting.class),
    //    FUELENHANCER("machine.fuelenhancer", BlockRotaryCraftMachine.class, BlockEntityFuelConverter.class, "RenderFuelConverter"),
//    ARROWGUN("machine.arrowgun", BlockRotaryCraftMachine.class, BlockEntityMachineGun.class),
    BOILER("machine.frictionboiler", RotaryBlocks.FRICTION_BOILER.get(), BlockEntityBoiler.class, PowerTypes.STEAM),
    STEAMTURBINE("machine.steamturbine", RotaryBlocks.STEAM_TURBINE.get(), BlockEntitySteam.class, PowerTypes.STEAM),
    //    FERTILIZER("machine.fertilizer", BlockRotaryCraftMachine.class, BlockEntityFertilizer.class, "RenderFertilizer"),
//    LAVAMAKER("machine.lavamaker", BlockRotaryCraftMachine.class, BlockEntityLavaMaker.class, "RenderRockMelter"),
    //GENERATOR("machine.generator", BlockModEngine.class, BlockEntityGenerator.class, "RenderGenerator", PowerTypes.EU),
    //ELECTRICMOTOR("machine.electricmotor", BlockModEngine.class, BlockEntityElectricMotor.class, "RenderElecMotor", PowerTypes.EU),
    //    AGGREGATOR("machine.aggregator", BlockRotaryCraftMachine.class, BlockEntityAggregator.class, "RenderAggregator"),
//    AIRGUN("machine.airgun", BlockRotaryCraftMachine.class, BlockEntityAirGun.class, "RenderAirGun"),
//    SONICBORER("machine.sonicborer", BlockRotaryCraftMachine.class, BlockEntitySonicBorer.class, "RenderSonicBorer"),
//    FUELENGINE("machine.fuelengine", BlockModEngine.class, BlockEntityFuelEngine.class, "RenderFuelEngine", ModList.BCENERGY),
//    FILLINGSTATION("machine.fillingstation", RotaryBlocks.FILLING_STATION.get(), BlockEntityFillingStation.class),
//    BELT("machine.belt", BlockRotaryCraftMachine.class, BlockEntityBeltHub.class, "RenderBelt"),
    VANDEGRAFF("machine.vandegraff", RotaryBlocks.VAN_DE_GRAFF.get(), BlockEntityVanDeGraff.class),
    //    DEFOLIATOR("machine.defoliator", BlockRotaryCraftMachine.class, BlockEntityDefoliator.class, "RenderDefoliator"),
    BIGFURNACE("machine.bigfurnace", RotaryBlocks.LAVA_SMELTORY.get(), BlockEntityLavaSmeltery.class),
    //    DISTILLER("machine.distiller", BlockRotaryCraftMachine.class, BlockEntityDistillery.class, "RenderDistillery"),
    //    DYNAMO("machine.dynamo", BlockModEngine.class, BlockEntityDynamo.class, "RenderDynamo", PowerTypes.RF),
    MAGNETIC("machine.magnetic", RotaryBlocks.MAGNETOSTATIC_ENGINE.get(), BlockEntityMagnetEngine.class, PowerTypes.RF),
    //    CRYSTALLIZER("machine.crystal", BlockRotaryCraftMachine.class, BlockEntityCrystallizer.class, "RenderCrystal"),
//    BUSCONTROLLER("machine.buscontroller", BlockRotaryCraftMachine.class, BlockEntityBusController.class),
//    POWERBUS("machine.bus", BlockRotaryCraftMachine.class, BlockEntityPowerBus.class),
    PARTICLE("machine.particle", RotaryBlocks.PARTICLE.get(), BlockEntityParticleEmitter.class),
    //    LAWNSPRINKLER("machine.lawnsprinkler", BlockRotaryCraftMachine.class, BlockEntityLawnSprinkler.class, "RenderLawnSprinkler"),
    GRINDSTONE("machine.grindstone", RotaryBlocks.GRINDSTONE.get(), BlockEntityGrindstone.class),
    BLOWER("machine.blower", RotaryBlocks.BLOWER.get(), BlockEntityBlower.class),
    //    PORTALSHAFT("machine.portalshaft", BlockRotaryCraftMachine.class, BlockEntityPortalShaft.class, "RenderPortalShaft"),
    REFRIGERATOR("machine.refrigerator", RotaryBlocks.REFRIGERATOR.get(), BlockEntityRefrigerator.class),
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
    FILLER("machine.filler", RotaryBlocks.FILLER.get(), BlockEntityBlockFiller.class),
    //    GATLING("machine.gatling", BlockRotaryCraftMachine.class, BlockEntityMultiCannon.class, "RenderMultiCannon"),
    SPILLWAY("machine.spillway", RotaryBlocks.SPILLWAY.get(), BlockEntitySpillway.class),
    //    FLAMETURRET("machine.flameturret", BlockRotaryCraftMachine.class, BlockEntityFlameTurret.class, "RenderFlameTurret"),
//    BUNDLEDBUS("machine.bundledbus", BlockRotaryCraftMachine.class, BlockEntityBundledBus.class, ModList.APPENG, ModList.PROJRED),
    DISTRIBCLUTCH("machine.distribclutch", RotaryBlocks.DISTRIBUTION_CLUTCH.get(), BlockEntityDistributionClutch.class);

    public static final ImmutableArray<MachineRegistry> machineList = new ImmutableArray<>(values());
    public static final BlockMap<MachineRegistry> machineMappings = new BlockMap<>();
    private final String name;
    private final Block block;
    private final Class<? extends RotaryCraftBlockEntity> te;
    private EngineType engineType;
    private ModDependency requirement;
    private PowerTypes powertype;
    private PowerReceivers receiver;

    MachineRegistry(String n, Block b, Class<? extends RotaryCraftBlockEntity> tile) {
        name = n;
        block = b;
        te = tile;
        receiver = PowerReceivers.initialize(this);
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
        if (id == RotaryBlocks.WOOD_SHAFT.get() || id == RotaryBlocks.HSLA_SHAFT.get() || id == RotaryBlocks.TUNGSTEN_SHAFT.get() || id == RotaryBlocks.DIAMOND_SHAFT.get() || id == RotaryBlocks.BEDROCK_SHAFT.get())
            return SHAFT;
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
            case SHAFT, /*BEDROCKBREAKER, */FLOODLIGHT, /*FAN,*/ COOLINGFIN, LINEBUILDER,/* SONICBORER, BELT,*/ BLOWER, /*PIPEPUMP, CHAIN,*/ CLUTCH ->
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
                    AC_ENGINE, GEARBOX, SHAFT, WORMGEAR, HIGHGEAR, CVT, COIL, FLYWHEEL -> true;
            default -> false;
        };
    }

    public boolean canBeFrictionHeated() {
        return FrictionHeatable.class.isAssignableFrom(te);
    }

    public boolean canBeBroken() {
        return switch (this) {
            case MIRROR, SHAFT, FLYWHEEL, WIND_ENGINE,
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
        if (this == SHAFT)
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
            case WORMGEAR, HIGHGEAR, CVT, COIL, STEAMTURBINE, TNTCANNON, MAGNETIC, REFRIGERATOR, WINDER, COMPOSTER, SHAFT, CLUTCH, GEARBOX, FLYWHEEL, RESERVOIR, WIND_ENGINE, GRINDER,
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
}
