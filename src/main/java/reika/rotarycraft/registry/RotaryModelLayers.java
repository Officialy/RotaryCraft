package reika.rotarycraft.registry;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.model.GearboxBaseModel;
import reika.rotarycraft.models.*;
import reika.rotarycraft.models.animated.*;
import reika.rotarycraft.models.animated.shaftonly.CrossModel;
import reika.rotarycraft.models.animated.shaftonly.ShaftModel;
import reika.rotarycraft.models.animated.shaftonly.ShaftVModel;
import reika.rotarycraft.models.engine.*;
import reika.rotarycraft.modinterface.conversion.RenderMagnetic;
import reika.rotarycraft.modinterface.model.MagneticModel;
import reika.rotarycraft.renders.*;
import reika.rotarycraft.renders.dm.RenderFin;
import reika.rotarycraft.renders.dmi.RenderGrinder;
import reika.rotarycraft.renders.m.RenderReservoir;
import reika.rotarycraft.renders.m.RenderVanDeGraff;
import reika.rotarycraft.renders.mi.RenderBigFurnace;
import reika.rotarycraft.renders.mi.RenderLandmine;
import reika.rotarycraft.renders.mi.RenderSmokeDetector;

public class RotaryModelLayers {

    public static final ModelLayerLocation FLOODLIGHT = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "lamp"), "main");
    public static final ModelLayerLocation FLOODLIGHT_VERTICAL = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "lamp_vertical"), "main");

    public static final ModelLayerLocation SHAFT = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "shaft"), "main");
    public static final ModelLayerLocation SHAFT_VERTICAL = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "shaft_vertical"), "main");
    public static final ModelLayerLocation SHAFT_CROSS = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "shaft_cross"), "main");
    public static final ModelLayerLocation BEVEL = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "bevel"), "main");
    public static final ModelLayerLocation SMOKE_DETECTOR = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "smoke_detector"), "main");
    public static final ModelLayerLocation PLAYER_DETECTOR = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "player_detector"), "main");
    public static final ModelLayerLocation VAN_DE_GRAFF = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "van_de_graff"), "main");
    public static final ModelLayerLocation HEAT_RAY = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "heat_ray"), "main");
    public static final ModelLayerLocation COOLING_FIN = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "cooling_fin"), "main");
    public static final ModelLayerLocation MONITOR = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "monitor"), "main");
    public static final ModelLayerLocation LANDMINE = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "landmine"), "main");
    public static final ModelLayerLocation DC_ENGINE = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "dc_engine"), "main");
    public static final ModelLayerLocation VACCUUM = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "vacuum"), "main");
    public static final ModelLayerLocation RESERVOIR = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "reservoir"), "main");
    public static final ModelLayerLocation SPLITTER = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "splitter"), "main");
    public static final ModelLayerLocation SPLITTER_2 = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "splitter_2"), "main");

    public static final ModelLayerLocation STEAM_ENGINE = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "steam_engine"), "main");
    public static final ModelLayerLocation COMBUSTION_ENGINE = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "combustion_engine"), "main");
    public static final ModelLayerLocation AC_ENGINE = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "ac_engine"), "main");
    public static final ModelLayerLocation PERFORMANCE_ENGINE = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "performance_engine"), "main");
    public static final ModelLayerLocation MICRO_ENGINE = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "micro_engine"), "main");
    public static final ModelLayerLocation JET_ENGINE = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "jet_engine"), "main");
    public static final ModelLayerLocation HYDRO_ENGINE = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "hydro_engine"), "main");
    public static final ModelLayerLocation WIND_ENGINE = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "wind_engine"), "main");
    public static final ModelLayerLocation MIRROR = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "mirror"), "main");
    public static final ModelLayerLocation GEARBOX = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "gearbox"), "main");
    public static final ModelLayerLocation GEARBOX_4 = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "gearbox_4"), "main");
    public static final ModelLayerLocation GEARBOX_8 = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "gearbox_8"), "main");
    public static final ModelLayerLocation GEARBOX_16 = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "gearbox_16"), "main");
    public static final ModelLayerLocation GEARBOX_BASE = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "gearbox_base"), "main");
    public static final ModelLayerLocation MAGNETIC = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "magnetic"), "main");
    public static final ModelLayerLocation SOLAR_TOWER = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "solar_tower"), "main");
    public static final ModelLayerLocation BIGFURNACE = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "big_furnace"), "main");
//    public static final ModelLayerLocation WORMMODEL = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "worm_model"), "main");
//    public static final ModelLayerLocation CVTMODEL = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "cvt_model"), "main");
//    public static final ModelLayerLocation COILMODEL = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "coil_model"), "main");
//    public static final ModelLayerLocation HIGHGEARMODEL = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "high_gear_model"), "main");
    public static final ModelLayerLocation GRINDER = new ModelLayerLocation(new ResourceLocation(RotaryCraft.MODID, "grinder"), "main");


    public static void init(IEventBus bus) {
        bus.addListener(RotaryModelLayers::registerEntityRenderers);
        bus.addListener(RotaryModelLayers::registerLayerDefinitions);
    }

    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(RotaryBlockEntities.FLOODLIGHT.get(), RenderLamp::new);
        event.registerBlockEntityRenderer(RotaryBlockEntities.SMOKE_DETECTOR.get(), RenderSmokeDetector::new);
        event.registerBlockEntityRenderer(RotaryBlockEntities.RESERVOIR.get(), RenderReservoir::new);
        event.registerBlockEntityRenderer(RotaryBlockEntities.VAN_DE_GRAFF.get(), RenderVanDeGraff::new);
        event.registerBlockEntityRenderer(RotaryBlockEntities.DC_ENGINE.get(), RenderSEngine::new);
        event.registerBlockEntityRenderer(RotaryBlockEntities.LANDMINE.get(), RenderLandmine::new);
        event.registerBlockEntityRenderer(RotaryBlockEntities.SPLITTER.get(), RenderSplitter::new);
        event.registerBlockEntityRenderer(RotaryBlockEntities.BEVEL_GEARS.get(), RenderBevel::new);
        event.registerBlockEntityRenderer(RotaryBlockEntities.MIRROR.get(), RenderMirror::new);
        event.registerBlockEntityRenderer(RotaryBlockEntities.GAS_ENGINE.get(), RenderSEngine::new);
        event.registerBlockEntityRenderer(RotaryBlockEntities.MICROTURBINE.get(), RenderSEngine::new);
        event.registerBlockEntityRenderer(RotaryBlockEntities.PERFORMANCE_ENGINE.get(), RenderSEngine::new);
        event.registerBlockEntityRenderer(RotaryBlockEntities.WIND_ENGINE.get(), RenderSEngine::new);
        event.registerBlockEntityRenderer(RotaryBlockEntities.STEAM_ENGINE.get(), RenderSEngine::new);
        event.registerBlockEntityRenderer(RotaryBlockEntities.AC_ENGINE.get(), RenderSEngine::new);
        event.registerBlockEntityRenderer(RotaryBlockEntities.WOOD_SHAFT.get(), RenderShaft::new);
        event.registerBlockEntityRenderer(RotaryBlockEntities.HSLA_STEEL_SHAFT.get(), RenderShaft::new);
        event.registerBlockEntityRenderer(RotaryBlockEntities.STONE_SHAFT.get(), RenderShaft::new);
        event.registerBlockEntityRenderer(RotaryBlockEntities.TUNGSTEN_SHAFT.get(), RenderShaft::new);
        event.registerBlockEntityRenderer(RotaryBlockEntities.DIAMOND_SHAFT.get(), RenderShaft::new);
        event.registerBlockEntityRenderer(RotaryBlockEntities.BEDROCK_SHAFT.get(), RenderShaft::new);
        event.registerBlockEntityRenderer(RotaryBlockEntities.MAGNETOSTATIC_ENGINE.get(), RenderMagnetic::new);
        event.registerBlockEntityRenderer(RotaryBlockEntities.SOLAR_TOWER.get(), RenderSolarTower::new);
//        event.registerBlockEntityRenderer(RotaryBlockEntities.VACCUUM.get(), RenderVacuum::new);
        event.registerBlockEntityRenderer(RotaryBlockEntities.COOLING_FIN.get(), RenderFin::new);
//        event.registerBlockEntityRenderer(RotaryBlockEntities.HEAT_RAY.get(), RenderHRay::new);
//        event.registerBlockEntityRenderer(RotaryBlockEntities.PLAYER_DETECTOR.get(), RenderDetector::new);
        event.registerBlockEntityRenderer(RotaryBlockEntities.DYNAMOMETER.get(), RenderMonitor::new);
//        event.registerBlockEntityRenderer(RotaryBlockEntities.JET_ENGINE.get(), RenderSEngine::new);
//        event.registerBlockEntityRenderer(RotaryBlockEntities.HYDRO_ENGINE.get(), RenderSEngine::new);
        event.registerBlockEntityRenderer(RotaryBlockEntities.GEARBOX.get(), RenderGearbox::new);
        event.registerBlockEntityRenderer(RotaryBlockEntities.BIG_FURNACE.get(), RenderBigFurnace::new);
        event.registerBlockEntityRenderer(RotaryBlockEntities.GRINDER.get(), RenderGrinder::new);

    }

    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(FLOODLIGHT, LampModel::createLayer);
        event.registerLayerDefinition(FLOODLIGHT_VERTICAL, VLampModel::createLayer);
        event.registerLayerDefinition(SMOKE_DETECTOR, SmokeDetectorModel::createLayer);
        event.registerLayerDefinition(VACCUUM, VacuumModel::createLayer);
        event.registerLayerDefinition(RESERVOIR, ReservoirModel::createLayer);
        event.registerLayerDefinition(DC_ENGINE, DCModel::createLayer);
        event.registerLayerDefinition(MONITOR, MonitorModel::createLayer);
        event.registerLayerDefinition(PLAYER_DETECTOR, DetectorModel::createLayer);
        event.registerLayerDefinition(VAN_DE_GRAFF, VanDeGraffModel::createLayer);
        event.registerLayerDefinition(HEAT_RAY, HRayModel::createLayer);
        event.registerLayerDefinition(COOLING_FIN, FinModel::createLayer);
        event.registerLayerDefinition(MONITOR, MonitorModel::createLayer);
        event.registerLayerDefinition(LANDMINE, LandmineModel::createLayer);
        event.registerLayerDefinition(SPLITTER, SplitterModel::createLayer);
        event.registerLayerDefinition(SPLITTER_2, SplitterModel2::createLayer);
        event.registerLayerDefinition(SHAFT, ShaftModel::createLayer);
        event.registerLayerDefinition(BEVEL, BevelModel::createLayer);
        event.registerLayerDefinition(SHAFT_VERTICAL, ShaftVModel::createLayer);
        event.registerLayerDefinition(SHAFT_CROSS, CrossModel::createLayer);
        event.registerLayerDefinition(STEAM_ENGINE, SteamModel::createLayer);
        event.registerLayerDefinition(COMBUSTION_ENGINE, CombustionModel::createLayer);
        event.registerLayerDefinition(AC_ENGINE, ACModel::createLayer);
        event.registerLayerDefinition(PERFORMANCE_ENGINE, PerformanceModel::createLayer);
        event.registerLayerDefinition(MICRO_ENGINE, MicroTurbineModel::createLayer);
        event.registerLayerDefinition(JET_ENGINE, JetModel::createLayer);
        event.registerLayerDefinition(HYDRO_ENGINE, HydroModel::createLayer);
        event.registerLayerDefinition(WIND_ENGINE, WindModel::createLayer);
        event.registerLayerDefinition(MIRROR, MirrorModel::createLayer);
        event.registerLayerDefinition(GEARBOX_BASE, GearboxBaseModel::createLayer);
        event.registerLayerDefinition(GEARBOX, GearboxModel::createLayer);
        event.registerLayerDefinition(GEARBOX_4, Gearbox4Model::createLayer);
        event.registerLayerDefinition(GEARBOX_8, Gearbox8Model::createLayer);
        event.registerLayerDefinition(GEARBOX_16, Gearbox16Model::createLayer);
        event.registerLayerDefinition(MAGNETIC, MagneticModel::createLayer);
        event.registerLayerDefinition(SOLAR_TOWER, SolarTowerModel::createLayer);
        event.registerLayerDefinition(BIGFURNACE, BigFurnaceModel::createLayer);
        event.registerLayerDefinition(GRINDER, GrinderModel::createLayer);
    }
}
