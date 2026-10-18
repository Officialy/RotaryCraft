package reika.rotarycraft;

import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import reika.dragonapi.instantiable.data.Proportionality;
import reika.dragonapi.instantiable.math.Spline;

/** Common algorithms and their public reflection surface in an actual dedicated-server JVM. */
final class DragonAPISideAuditTests {
    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> environment) {
        RotaryGameTests.register(event, environment, "dragonapi_common_data_distribution", 40, DragonAPISideAuditTests::commonData);
    }

    private static void commonData(GameTestHelper helper) {
        var proportions = new Proportionality<String>();
        proportions.addValue("first", 3); proportions.addValue("second", 1);
        helper.assertTrue(proportions.getFraction("first") == 0.75 && proportions.hasMajority("first"), "server fractions must remain usable");
        proportions.setGeometry(0, 0, 20, 0);
        helper.assertTrue(proportions.getClickedSection(30, 0) == null && proportions.getClickedSection(1, 0) != null,
                "common category geometry must not require a renderer");
        var converter = new reika.dragonapi.libraries.ReikaNBTHelper.NBTIO<String>() {
            public net.minecraft.nbt.Tag convertToNBT(String value) { return net.minecraft.nbt.StringTag.valueOf(value); }
            public String createFromNBT(net.minecraft.nbt.Tag tag) { return tag.asString().orElseThrow(); }
        };
        var tag = new net.minecraft.nbt.CompoundTag(); proportions.drawSeparationLines = true; proportions.save(tag, converter);
        var loaded = new Proportionality<String>(); loaded.load(tag, converter);
        helper.assertTrue(loaded.getFraction("first") == 0.75 && loaded.getValue("second") == 1 && loaded.drawSeparationLines,
                "common NBT persists original weights/total and presentation flag without invoking drawing");
        for (Spline.SplineType kind : Spline.SplineType.values()) {
            var spline = new Spline(kind);
            spline.addPoint(new Spline.BasicSplinePoint(0, 0, 0));
            spline.addPoint(new Spline.BasicSplinePoint(2, 3, 4));
            spline.addPoint(new Spline.BasicSplinePoint(4, 1, 6));
            var points = spline.get(8, false);
            helper.assertTrue(points.size() > 3 && points.getFirst().xCoord() == 0 && points.getLast().xCoord() == 4,
                    "server spline interpolation must remain usable");
            helper.assertTrue(points.stream().allMatch(p -> Double.isFinite(p.xCoord()) && Double.isFinite(p.yCoord()) && Double.isFinite(p.zCoord())),
                    "every spline interpolation kind must remain finite on server");
            helper.assertTrue(spline.get(8, true).getFirst().equals(spline.get(8, true).getLast()), "closed common spline must retain closure");
        }
        var variable = new reika.dragonapi.instantiable.math.VariableEndpointSpline(0, 0, 0, 4, 0, 0,
                Spline.SplineType.CENTRIPETAL, 3, 0.1, 0.01);
        variable.setEndpoint(5, 2, 1); variable.tick();
        helper.assertTrue(variable.getPoints(8).size() > 3, "variable endpoint curves update without native rendering state");
        var location = new reika.dragonapi.instantiable.data.immutable.WorldLocation(helper.getLevel(), new net.minecraft.core.BlockPos(1, 2, 3));
        helper.assertTrue(location.getWorld(helper.getLevel()) == helper.getLevel()
                && location.getWorld(helper.getLevel().getServer()) == helper.getLevel(), "world locations use authoritative common context");
        reika.dragonapi.libraries.io.ReikaChatHelper.clearChat();
        reika.dragonapi.libraries.io.ReikaChatHelper.writeComponent(net.minecraft.network.chat.Component.literal("D14 dedicated guard"));
        reika.dragonapi.libraries.io.ReikaSoundHelper.playClientSound(net.minecraft.sounds.SoundEvents.FIRE_EXTINGUISH, 0, 0, 0, 1, 1, true);
        for (Class<?> type : java.util.List.of(Proportionality.class, Spline.class,
                reika.dragonapi.instantiable.data.CircularDivisionRenderer.class,
                reika.dragonapi.instantiable.io.SyncPacket.class,
                reika.dragonapi.instantiable.io.ControlledConfig.class,
                reika.dragonapi.libraries.io.ReikaSoundHelper.class,
                reika.dragonapi.libraries.io.ReikaChatHelper.class,
                reika.dragonapi.instantiable.data.immutable.WorldLocation.class,
                reika.dragonapi.instantiable.io.RemoteSourcedAsset.class,
                reika.dragonapi.libraries.io.ReikaKeyHelper.class)) {
            try {
                helper.assertTrue(type.getDeclaredMethods().length > 0 && type.getMethods().length > 0,
                        "common public API metadata must be available on dedicated servers: " + type.getName());
            } catch (LinkageError | RuntimeException error) {
                throw new IllegalStateException("Common API resolves a client type during native server reflection: " + type.getName(), error);
            }
        }
        try {
            var hash = reika.dragonapi.instantiable.io.ControlledConfig.class.getDeclaredMethod("genUserHash"); hash.setAccessible(true);
            helper.assertTrue((int)hash.invoke(null) == 0, "dedicated config hash must bypass the physical-client environment bridge");
            var constructor = reika.dragonapi.instantiable.io.RemoteSourcedAsset.class.getDeclaredConstructor(String.class, String.class, String.class, String.class);
            constructor.setAccessible(true);
            var asset = constructor.newInstance("dragonapi", "textures/gui/warning.png", "", "");
            try (var stream = asset.getData()) {
                helper.assertTrue(stream != null && stream.read() == 0x89 && stream.read() == 'P',
                        "dedicated assets use the classpath without a client resource manager or download");
            }
        } catch (ReflectiveOperationException | java.io.IOException error) { throw new IllegalStateException(error); }
        helper.succeed();
    }
}
