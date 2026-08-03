package reika.rotarycraft.client;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

import net.minecraft.client.model.geom.EntityModelSet;
import reika.rotarycraft.base.RotaryModelBase;
import reika.rotarycraft.models.*;
import reika.rotarycraft.models.BreederModel;
import reika.rotarycraft.models.DryingBedModel;
import reika.rotarycraft.models.SpawnerModel;
import reika.rotarycraft.models.SprinklerModel;
import reika.rotarycraft.models.animated.*;
import reika.rotarycraft.models.animated.BeltModel;
import reika.rotarycraft.models.animated.CompactorModel;
import reika.rotarycraft.models.animated.CrystallizerModel;
import reika.rotarycraft.models.animated.FertilizerModel;
import reika.rotarycraft.models.animated.LawnSprinklerModel;
import reika.rotarycraft.models.animated.WetterModel;
import reika.rotarycraft.models.animated.shaftonly.ClutchModel;
import reika.rotarycraft.models.animated.shaftonly.CrossModel;
import reika.rotarycraft.models.animated.shaftonly.ShaftModel;
import reika.rotarycraft.models.engine.*;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryModelLayers;
import reika.rotarycraft.modinterface.model.*;

/**
 * Maps each machine to the model it renders with.
 *
 * <p>This used to be a field on {@link MachineRegistry} itself, with the factory passed to every
 * enum constant. That made the enum unloadable on a dedicated server: the constructor signature, the
 * field type and all 101 constant lambdas name {@code EntityModelSet} and {@code RotaryModelBase},
 * so simply initialising the enum -- which {@code RotaryCraft.commonSetup} does when it iterates the
 * machine list -- resolved client-only types and aborted mod loading.
 *
 * <p>Its only consumers were the two client renderers and the datagen model provider, so the mapping
 * belongs on the client side. Registry data that the server needs stays in the enum; how a machine
 * looks does not.
 */
public final class MachineModels {

    private static final Map<MachineRegistry, Function<EntityModelSet, ? extends RotaryModelBase>> MODELS =
            new EnumMap<>(MachineRegistry.class);

    private MachineModels() {}

    static {
        MODELS.put(MachineRegistry.WIND_ENGINE, (modelSet) -> new WindModel(modelSet.bakeLayer(RotaryModelLayers.WIND_ENGINE)));
        MODELS.put(MachineRegistry.STEAM_ENGINE, (modelSet) -> new SteamModel(modelSet.bakeLayer(RotaryModelLayers.STEAM_ENGINE)));
        MODELS.put(MachineRegistry.PERFORMANCE_ENGINE, (modelSet) -> new PerformanceModel(modelSet.bakeLayer(RotaryModelLayers.PERFORMANCE_ENGINE)));
        MODELS.put(MachineRegistry.MICRO_TURBINE, (modelSet) -> new MicroTurbineModel(modelSet.bakeLayer(RotaryModelLayers.MICRO_ENGINE)));
        MODELS.put(MachineRegistry.GAS_ENGINE, (modelSet) -> new CombustionModel(modelSet.bakeLayer(RotaryModelLayers.COMBUSTION_ENGINE)));
        MODELS.put(MachineRegistry.DC_ENGINE, (modelSet) -> new DCModel(modelSet.bakeLayer(RotaryModelLayers.DC_ENGINE)));
        MODELS.put(MachineRegistry.AC_ENGINE, (modelSet) -> new ACModel(modelSet.bakeLayer(RotaryModelLayers.AC_ENGINE)));
        MODELS.put(MachineRegistry.JET_ENGINE, (modelSet) -> new JetModel(modelSet.bakeLayer(RotaryModelLayers.JET_ENGINE)));
        MODELS.put(MachineRegistry.FLYWHEEL, (modelSet) -> new FlywheelModel(modelSet.bakeLayer(RotaryModelLayers.FLYWHEEL)));
        MODELS.put(MachineRegistry.WOOD_SHAFT, (modelSet) -> new ShaftModel(modelSet.bakeLayer(RotaryModelLayers.SHAFT)));
        MODELS.put(MachineRegistry.STONE_SHAFT, (modelSet) -> new ShaftModel(modelSet.bakeLayer(RotaryModelLayers.SHAFT)));
        MODELS.put(MachineRegistry.HSLA_SHAFT, (modelSet) -> new ShaftModel(modelSet.bakeLayer(RotaryModelLayers.SHAFT)));
        MODELS.put(MachineRegistry.TUNGSTEN_SHAFT, (modelSet) -> new ShaftModel(modelSet.bakeLayer(RotaryModelLayers.SHAFT)));
        MODELS.put(MachineRegistry.DIAMOND_SHAFT, (modelSet) -> new ShaftModel(modelSet.bakeLayer(RotaryModelLayers.SHAFT)));
        MODELS.put(MachineRegistry.BEVELGEARS, (modelSet) -> new BevelModel(modelSet.bakeLayer(RotaryModelLayers.BEVEL)));
        MODELS.put(MachineRegistry.GEARBOX, (modelSet) -> new GearboxModel(modelSet.bakeLayer(RotaryModelLayers.GEARBOX)));
        MODELS.put(MachineRegistry.SPLITTER, (modelSet) -> new SplitterModel(modelSet.bakeLayer(RotaryModelLayers.SPLITTER)));
        MODELS.put(MachineRegistry.FLOODLIGHT, (modelSet) -> new LampModel(modelSet.bakeLayer(RotaryModelLayers.FLOODLIGHT)));
        MODELS.put(MachineRegistry.CLUTCH, (modelSet) -> new ClutchModel(modelSet.bakeLayer(RotaryModelLayers.CLUTCH)));
        MODELS.put(MachineRegistry.DYNAMOMETER, (modelSet) -> new MonitorModel(modelSet.bakeLayer(RotaryModelLayers.DYNOMONITOR)));
        MODELS.put(MachineRegistry.GRINDER, (modelSet) -> new GrinderModel(modelSet.bakeLayer(RotaryModelLayers.GRINDER)));
        MODELS.put(MachineRegistry.PUMP, (modelSet) -> new PumpModel(modelSet.bakeLayer(RotaryModelLayers.PUMP)));
        MODELS.put(MachineRegistry.RESERVOIR, (modelSet) -> new ReservoirModel(modelSet.bakeLayer(RotaryModelLayers.RESERVOIR)));
        MODELS.put(MachineRegistry.AEROSOLIZER, (modelSet) -> new AerosolizerModel(modelSet.bakeLayer(RotaryModelLayers.AEROSOLIZER)));
        MODELS.put(MachineRegistry.PULSEJET, (modelSet) -> new PulseFurnaceModel(modelSet.bakeLayer(RotaryModelLayers.PULSEJET)));
        MODELS.put(MachineRegistry.COMPACTOR, (modelSet) -> new CompactorModel(modelSet.bakeLayer(RotaryModelLayers.COMPACTOR)));
        MODELS.put(MachineRegistry.FAN, (modelSet) -> new FanModel(modelSet.bakeLayer(RotaryModelLayers.FAN)));
        MODELS.put(MachineRegistry.FRACTIONATOR, (modelSet) -> new FractionModel(modelSet.bakeLayer(RotaryModelLayers.FRACTIONATOR)));
        MODELS.put(MachineRegistry.VACUUM, (modelSet) -> new VacuumModel(modelSet.bakeLayer(RotaryModelLayers.VACCUUM)));
        MODELS.put(MachineRegistry.SPRINKLER, (modelSet) -> new SprinklerModel(modelSet.bakeLayer(RotaryModelLayers.SPRINKLER)));
        MODELS.put(MachineRegistry.WOODCUTTER, (modelSet) -> new WoodcutterModel(modelSet.bakeLayer(RotaryModelLayers.WOODCUTTER)));
        MODELS.put(MachineRegistry.SPAWNERCONTROLLER, (modelSet) -> new SpawnerModel(modelSet.bakeLayer(RotaryModelLayers.SPAWNER_CONTROLLER)));
        MODELS.put(MachineRegistry.HEATER, (modelSet) -> new HeaterModel(modelSet.bakeLayer(RotaryModelLayers.HEATER)));
        MODELS.put(MachineRegistry.AUTOBREEDER, (modelSet) -> new BreederModel(modelSet.bakeLayer(RotaryModelLayers.AUTOBREEDER)));
        MODELS.put(MachineRegistry.SMOKEDETECTOR, (modelSet) -> new SmokeDetectorModel(modelSet.bakeLayer(RotaryModelLayers.SMOKE_DETECTOR)));
        MODELS.put(MachineRegistry.WINDER, (modelSet) -> new WinderModel(modelSet.bakeLayer(RotaryModelLayers.WINDER)));
        MODELS.put(MachineRegistry.WORMGEAR, (modelSet) -> new WormModel(modelSet.bakeLayer(RotaryModelLayers.WORM)));
        MODELS.put(MachineRegistry.CVT, (modelSet) -> new CVTModel(modelSet.bakeLayer(RotaryModelLayers.CVT)));
        MODELS.put(MachineRegistry.HIGHGEAR, (modelSet) -> new HighGearModel(modelSet.bakeLayer(RotaryModelLayers.HIGHGEAR)));
        MODELS.put(MachineRegistry.COIL, (modelSet) -> new CoilModel(modelSet.bakeLayer(RotaryModelLayers.COIL)));
        MODELS.put(MachineRegistry.CREATIVE_COIL, (modelSet) -> new CoilModel(modelSet.bakeLayer(RotaryModelLayers.COIL)));
        MODELS.put(MachineRegistry.MAGNETIZER, (modelSet) -> new MagnetizerModel(modelSet.bakeLayer(RotaryModelLayers.MAGNETIZER)));
        MODELS.put(MachineRegistry.ITEMCANNON, (modelSet) -> new ItemCannonModel(modelSet.bakeLayer(RotaryModelLayers.ITEM_CANNON)));
        MODELS.put(MachineRegistry.LANDMINE, (modelSet) -> new LandmineModel(modelSet.bakeLayer(RotaryModelLayers.LANDMINE)));
        MODELS.put(MachineRegistry.FRICTION, (modelSet) -> new FrictionModel(modelSet.bakeLayer(RotaryModelLayers.FRICTION_HEATER)));
        MODELS.put(MachineRegistry.MIRROR, (modelSet) -> new MirrorModel(modelSet.bakeLayer(RotaryModelLayers.MIRROR)));
        MODELS.put(MachineRegistry.SOLARTOWER, (modelSet) -> new SolarTowerModel(modelSet.bakeLayer(RotaryModelLayers.SOLAR_TOWER)));
        MODELS.put(MachineRegistry.COOLINGFIN, (modelSet) -> new FinModel(modelSet.bakeLayer(RotaryModelLayers.COOLING_FIN)));
        MODELS.put(MachineRegistry.BEAMMIRROR, (modelSet) -> new BeamMirrorModel(modelSet.bakeLayer(RotaryModelLayers.BEAM_MIRROR)));
        MODELS.put(MachineRegistry.MULTICLUTCH, (modelSet) -> new MultiClutchModel(modelSet.bakeLayer(RotaryModelLayers.MULTI_CLUTCH)));
        MODELS.put(MachineRegistry.BOILER, (modelSet) -> new BoilerModel(modelSet.bakeLayer(RotaryModelLayers.BOILER)));
        MODELS.put(MachineRegistry.STEAMTURBINE, (modelSet) -> new SteamTurbineModel(modelSet.bakeLayer(RotaryModelLayers.STEAM_TURBINE)));
        MODELS.put(MachineRegistry.FERTILIZER, (modelSet) -> new FertilizerModel(modelSet.bakeLayer(RotaryModelLayers.FERTILIZER)));
        MODELS.put(MachineRegistry.LAVAMAKER, (modelSet) -> new LavaMakerModel(modelSet.bakeLayer(RotaryModelLayers.LAVA_MAKER)));
        MODELS.put(MachineRegistry.AGGREGATOR, (modelSet) -> new reika.rotarycraft.models.animated.AggregatorModel(modelSet.bakeLayer(RotaryModelLayers.AGGREGATOR)));
        MODELS.put(MachineRegistry.AIRGUN, (modelSet) -> new AirGunModel(modelSet.bakeLayer(RotaryModelLayers.AIR_GUN)));
        MODELS.put(MachineRegistry.SONICBORER, (modelSet) -> new SonicBorerModel(modelSet.bakeLayer(RotaryModelLayers.SONIC_BORER)));
        MODELS.put(MachineRegistry.FILLINGSTATION, (modelSet) -> new FillingStationModel(modelSet.bakeLayer(RotaryModelLayers.FILLING_STATION)));
        MODELS.put(MachineRegistry.BELT, (modelSet) -> new BeltModel(modelSet.bakeLayer(RotaryModelLayers.BELT)));
        MODELS.put(MachineRegistry.SPLITBELT, (modelSet) -> new BeltModel(modelSet.bakeLayer(RotaryModelLayers.BELT)));
        MODELS.put(MachineRegistry.VANDEGRAFF, (modelSet) -> new VanDeGraffModel(modelSet.bakeLayer(RotaryModelLayers.VAN_DE_GRAFF)));
        MODELS.put(MachineRegistry.BIGFURNACE, (modelSet) -> new BigFurnaceModel(modelSet.bakeLayer(RotaryModelLayers.BIG_FURNACE)));
        MODELS.put(MachineRegistry.MAGNETIC, (modelSet) -> new MagneticModel(modelSet.bakeLayer(RotaryModelLayers.MAGNETIC)));
        MODELS.put(MachineRegistry.CRYSTALLIZER, (modelSet) -> new CrystallizerModel(modelSet.bakeLayer(RotaryModelLayers.CRYSTALLIZER)));
        MODELS.put(MachineRegistry.LAWNSPRINKLER, (modelSet) -> new LawnSprinklerModel(modelSet.bakeLayer(RotaryModelLayers.LAWNSPRINKLER)));
        MODELS.put(MachineRegistry.GRINDSTONE, (modelSet) -> new GrindstoneModel(modelSet.bakeLayer(RotaryModelLayers.GRINDSTONE)));
        MODELS.put(MachineRegistry.GASTANK, (modelSet) -> new reika.rotarycraft.models.GasCompressorModel(modelSet.bakeLayer(RotaryModelLayers.GASTANK)));
        MODELS.put(MachineRegistry.PIPEPUMP, (modelSet) -> new PipePumpModel(modelSet.bakeLayer(RotaryModelLayers.PIPE_PUMP)));
        MODELS.put(MachineRegistry.CHAIN, (modelSet) -> new BeltModel(modelSet.bakeLayer(RotaryModelLayers.BELT)));
        MODELS.put(MachineRegistry.CENTRIFUGE, (modelSet) -> new CentrifugeModel(modelSet.bakeLayer(RotaryModelLayers.CENTRIFUGE)));
        MODELS.put(MachineRegistry.DRYING, (modelSet) -> new DryingBedModel(modelSet.bakeLayer(RotaryModelLayers.DRYING_BED)));
        MODELS.put(MachineRegistry.WETTER, (modelSet) -> new WetterModel(modelSet.bakeLayer(RotaryModelLayers.WETTER)));
        MODELS.put(MachineRegistry.SPILLWAY, (modelSet) -> new SpillwayModel(modelSet.bakeLayer(RotaryModelLayers.SPILLWAY)));
        MODELS.put(MachineRegistry.DISTRIBCLUTCH, (modelSet) -> new DistribClutchModel(modelSet.bakeLayer(RotaryModelLayers.DISTRIB_CLUTCH)));
        MODELS.put(MachineRegistry.BEDROCKBREAKER, (modelSet) -> new BedrockBreakerModel(modelSet.bakeLayer(RotaryModelLayers.BEDROCK_BREAKER)));
        MODELS.put(MachineRegistry.HYDRO_ENGINE, (modelSet) -> new HydroModel(modelSet.bakeLayer(RotaryModelLayers.HYDRO_ENGINE)));
        MODELS.put(MachineRegistry.EXTRACTOR, (modelSet) -> new ExtractorModel(modelSet.bakeLayer(RotaryModelLayers.EXTRACTOR)));
        MODELS.put(MachineRegistry.BEDROCK_SHAFT, (modelset) -> new ShaftModel(modelset.bakeLayer(RotaryModelLayers.SHAFT)));
        MODELS.put(MachineRegistry.SHAFT_CROSS, (modelset) -> new CrossModel(modelset.bakeLayer(RotaryModelLayers.SHAFT_CROSS)));
        MODELS.put(MachineRegistry.SHAFT_MERGE, (modelset) -> new CrossModel(modelset.bakeLayer(RotaryModelLayers.SHAFT_CROSS)));
    }

    public static boolean has(MachineRegistry machine) {
        return MODELS.containsKey(machine);
    }

    public static Function<EntityModelSet, ? extends RotaryModelBase> get(MachineRegistry machine) {
        return MODELS.get(machine);
    }
}
