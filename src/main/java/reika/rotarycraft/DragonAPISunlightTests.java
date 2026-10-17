package reika.rotarycraft;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.world.attribute.EnvironmentAttributeSystem;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.clock.WorldClocks;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.rotarycraft.auxiliary.SolarPlant;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.level.BlockEntityBeamMirror;
import reika.rotarycraft.registry.RotaryBlocks;

final class DragonAPISunlightTests {
    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> environment) {
        RotaryGameTests.register(event, environment, "dragonapi_sunlight_timeline", 40, DragonAPISunlightTests::timeline);
        RotaryGameTests.register(event, environment, "dragonapi_sunlight_climate", 40, DragonAPISunlightTests::climate);
        RotaryGameTests.register(event, environment, "dragonapi_sunlight_solar_consumers", 40, DragonAPISunlightTests::solarConsumers);
    }

    private static void timeline(GameTestHelper h) {
        var level = h.getLevel();
        var clock = level.registryAccess().lookupOrThrow(Registries.WORLD_CLOCK).getOrThrow(WorldClocks.OVERWORLD);
        long time = level.getOverworldClockTime();
        var weather = WeatherState.capture(level);
        BlockPos pos = h.absolutePos(new BlockPos(4, 3, 4));
        try {
            for (int tick = 0; tick < 24000; tick += 250) {
                level.clockManager().setTotalTicks(clock, tick);
                level.environmentAttributes().invalidateTickCache();
                float angle = level.environmentAttributes().getValue(EnvironmentAttributes.SUN_ANGLE, pos);
                double expected = Math.clamp(Math.cos(Math.toRadians(angle)) * 2 + 0.2, 0, 1);
                near(h, ReikaWorldHelper.getSunIntensity(level, pos, false, 0), expected, "native timeline tick " + tick);
                level.setRainLevel(1); level.setThunderLevel(1);
                near(h, ReikaWorldHelper.getSunIntensity(level, pos, true, 0.5F), expected * 0.6875 * 0.6875,
                        "native weighted rain/thunder tick " + tick);
            }
            for (int tick : new int[]{6000, 18000}) {
                level.clockManager().setTotalTicks(clock, tick);
                level.environmentAttributes().invalidateTickCache();
                near(h, ReikaWorldHelper.getSunIntensity(level, pos, false, 1), tick == 6000 ? 1 : 0, "noon/midnight normalization");
            }
            near(h, ReikaWorldHelper.getSunIntensity(level, false, 1),
                    ReikaWorldHelper.getSunIntensity(level, BlockPos.ZERO, false, 1), "position-free origin compatibility");
        } finally {
            level.clockManager().setTotalTicks(clock, time);
            level.environmentAttributes().invalidateTickCache();
            weather.restore(level);
        }
        h.succeed();
    }

    private static void climate(GameTestHelper h) {
        var level = h.getLevel();
        BlockPos pos = h.absolutePos(new BlockPos(4, 4, 4));
        openSky(h, pos);
        float[] angle = {0};
        var attributes = EnvironmentAttributeSystem.builder().addPositionalLayer(EnvironmentAttributes.SUN_ANGLE,
                (base, sample, interpolation) -> sample.distanceToSqr(Vec3.atCenterOf(pos)) < 0.01 ? angle[0] : 180F).build();
        var previous = level.setEnvironmentAttributes(attributes);
        var weather = WeatherState.capture(level);
        try {
            level.setRainLevel(0); level.setThunderLevel(0);
            near(h, ReikaWorldHelper.getSunIntensity(level, pos, false, 0), 1, "local sun angle overrides clock");
            near(h, ReikaWorldHelper.getSunIntensity(level, false, 0), 0, "origin has its own positional angle");
            int day = ReikaWorldHelper.getAmbientTemperatureAt(level, pos, 0);
            angle[0] = 180;
            int night = ReikaWorldHelper.getAmbientTemperatureAt(level, pos, 0);
            h.assertTrue(day - night == 20, "actual exposed climate caller retains original normalized 20-degree day/night delta: " + day + "/" + night);
        } finally {
            level.setEnvironmentAttributes(previous);
            weather.restore(level);
        }
        h.succeed();
    }

    private static void solarConsumers(GameTestHelper h) {
        var level = h.getLevel();
        BlockPos pos = h.absolutePos(new BlockPos(4, 4, 4));
        openSky(h, pos);
        float[] angle = {0};
        var attributes = EnvironmentAttributeSystem.builder().addPositionalLayer(EnvironmentAttributes.SUN_ANGLE,
                (base, sample, interpolation) -> sample.distanceToSqr(Vec3.atCenterOf(pos)) < 0.01 ? angle[0] : 180F).build();
        var previous = level.setEnvironmentAttributes(attributes);
        var clock = level.registryAccess().lookupOrThrow(Registries.WORLD_CLOCK).getOrThrow(WorldClocks.OVERWORLD);
        long time = level.getOverworldClockTime();
        var weather = WeatherState.capture(level);
        try {
            level.clockManager().setTotalTicks(clock, 6000);
            level.setRainLevel(0); level.setThunderLevel(0);
            var plant = SolarPlant.build(level, pos);
            near(h, plant.getLightLevel(level), 15, "solar plant samples its build position and supplies its own ambient floor once");
            level.setRainLevel(1); level.setThunderLevel(1);
            near(h, plant.getLightLevel(level), 8, "solar plant retains original storm daylight curve");
            angle[0] = 180;
            near(h, plant.getLightLevel(level), 3, "full-moon fallback is reachable at zero sunlight");
            level.clockManager().setTotalTicks(clock, 4 * 24000L + 18000);
            near(h, plant.getLightLevel(level), 0.15, "new-moon fallback preserves authored phase strength");
            level.clockManager().setTotalTicks(clock, 6000);
            level.setRainLevel(0); level.setThunderLevel(0);
            angle[0] = 90;
            for (int distance = 1; distance <= 3; distance++) level.setBlockAndUpdate(pos.relative(Direction.EAST, distance), Blocks.AIR.defaultBlockState());
            level.setBlockAndUpdate(pos, RotaryBlocks.BEAM_MIRROR.get().defaultBlockState().setValue(BlockRotaryCraftMachine.FACING, Direction.EAST));
            var mirror = (BlockEntityBeamMirror)level.getBlockEntity(pos);
            mirror.updateEntity(level, pos);
            h.assertTrue(mirror.getRange() == 2, "actual beam mirror uses local normalized sunrise intensity");
            angle[0] = 180;
            h.assertTrue(mirror.getRange() == 1, "zero local sunlight retains the original minimum daytime beam range");
        } finally {
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            level.setEnvironmentAttributes(previous);
            level.clockManager().setTotalTicks(clock, time);
            previous.invalidateTickCache();
            weather.restore(level);
        }
        h.succeed();
    }

    private static void openSky(GameTestHelper h, BlockPos pos) {
        for (int y = pos.getY() + 1; y < h.getLevel().getMaxY(); y++)
            h.getLevel().setBlockAndUpdate(new BlockPos(pos.getX(), y, pos.getZ()), Blocks.AIR.defaultBlockState());
        h.assertTrue(h.getLevel().canSeeSky(pos.above()), "fixture has actual exposed sky");
    }

    /** Preserve both ends of interpolation, including thunder hidden by zero rain. */
    private record WeatherState(float[] values) {
        private static final String[] FIELDS = {"rainLevel", "oRainLevel", "thunderLevel", "oThunderLevel"};

        static WeatherState capture(net.minecraft.world.level.Level level) {
            float[] values = new float[FIELDS.length];
            try {
                for (int i = 0; i < FIELDS.length; i++) values[i] = field(i).getFloat(level);
            } catch (ReflectiveOperationException error) {
                throw new IllegalStateException("Could not capture native test weather", error);
            }
            return new WeatherState(values);
        }

        void restore(net.minecraft.world.level.Level level) {
            try {
                for (int i = 0; i < FIELDS.length; i++) field(i).setFloat(level, values[i]);
            } catch (ReflectiveOperationException error) {
                throw new IllegalStateException("Could not restore native test weather", error);
            }
        }

        private static java.lang.reflect.Field field(int index) throws ReflectiveOperationException {
            var field = net.minecraft.world.level.Level.class.getDeclaredField(FIELDS[index]);
            field.setAccessible(true);
            return field;
        }
    }

    private static void near(GameTestHelper h, double actual, double expected, String context) {
        h.assertTrue(Math.abs(actual - expected) < 0.0003, context + ": expected " + expected + ", got " + actual);
    }
}
