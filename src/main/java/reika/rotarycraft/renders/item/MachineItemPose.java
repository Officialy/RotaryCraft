package reika.rotarycraft.renders.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import reika.rotarycraft.registry.MachineRegistry;

/**
 * Original 1.7.10 out-of-world TESR poses, after ItemMachineRenderer's placement offset.
 * These convert Techne coordinates into block-item coordinates; GUI/hand transforms belong in data.
 * Used by both submission and extent extraction so Minecraft measures exactly what is drawn.
 */
public final class MachineItemPose {
    private MachineItemPose() {}

    public static void apply(MachineRegistry machine, PoseStack pose) {
        // ItemMachineRenderer supplied y=0 for engines, shafts, flywheels and gearboxes;
        // generic machines and advanced gears used y=-0.1. setupGL then added (0.5,1.5,0.5).
        float y = switch (machine) {
            case DC_ENGINE, AC_ENGINE, WIND_ENGINE, STEAM_ENGINE, GAS_ENGINE, PERFORMANCE_ENGINE,
                    HYDRO_ENGINE, MICRO_TURBINE, JET_ENGINE, FLYWHEEL, GEARBOX,
                    WOOD_SHAFT, STONE_SHAFT, HSLA_SHAFT, TUNGSTEN_SHAFT, DIAMOND_SHAFT,
                    BEDROCK_SHAFT, SHAFT_CROSS, SHAFT_MERGE -> 1.5F;
            default -> 1.4F;
        };
        pose.translate(0.5F, y, 0.5F);
        // Original scale(1,-1,-1), expressed as a rotation to preserve a proper normal matrix.
        pose.rotate(Axis.XP.rotationDegrees(180));

        switch (machine) {
            case DC_ENGINE, AC_ENGINE, STEAM_ENGINE, GAS_ENGINE, PERFORMANCE_ENGINE,
                    GEARBOX, WORMGEAR, CVT, COIL, CREATIVE_COIL, HIGHGEAR, MIRROR -> yaw(pose, -90);
            case BEVELGEARS, FRICTION -> yaw(pose, 90);
            case SPLITTER -> yaw(pose, 180);
            case FUELENGINE -> yaw(pose, 270); // original metadata=0 -> var11=180, plus 90
            case WIND_ENGINE -> {
                pose.scale(0.7F, 0.7F, 0.7F);
                pose.translate(0.2F, 0.375F, 0);
            }
            case HYDRO_ENGINE -> {
                yaw(pose, -90);
                pose.translate(0, 0.375F, 0);
                pose.scale(0.7F, 0.7F, 0.7F);
            }
            case SPRINKLER -> {
                pose.scale(1.75F, 1.75F, 1.75F);
                pose.translate(0, -0.225F, 0);
                yaw(pose, -90);
            }
            case LANDMINE -> {
                pose.scale(1.5F, 1.5F, 1.5F);
                pose.translate(0, -0.6F, 0);
            }
            case CHUNKLOADER -> {
                pose.scale(1.125F, 1.125F, 1.125F);
                pose.translate(0, -0.25F, 0);
            }
            case SMOKEDETECTOR -> {
                pose.scale(2, 2, 2);
                yaw(pose, -90);
            }
            case SPAWNERCONTROLLER -> pose.translate(0, -0.4F, 0);
            default -> { } // Original inventory path applies no additional machine rotation.
        }
    }

    private static void yaw(PoseStack pose, float degrees) {
        pose.rotate(Axis.YP.rotationDegrees(degrees));
    }
}
