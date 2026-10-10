package reika.rotarycraft;

import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import reika.dragonapi.base.DragonAPIMod;
import reika.dragonapi.base.InertEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;

/** Real family loader registrations and failure diagnostics, without constructing synthetic mod instances. */
final class DragonAPIRegistrationAuditTests {
    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> environment) {
        RotaryGameTests.register(event, environment, "dragonapi_registration_all_mod_ids", 40,
                DragonAPIRegistrationAuditTests::registration);
        RotaryGameTests.register(event, environment, "dragonapi_inert_registered_identity", 40,
                DragonAPIRegistrationAuditTests::inertIdentity);
    }

    private static void inertIdentity(GameTestHelper h) {
        for (String id : Set.of("reactorcraft:neutron", "reactorcraft:plasma", "reactorcraft:fusion",
                "reactorcraft:radiation", "chromaticraft:pylon_overload", "chromaticraft:luma_burst",
                "chromaticraft:piston_spline", "chromaticraft:laser_pulse", "chromaticraft:aurora",
                "chromaticraft:death_fog")) {
            var type = BuiltInRegistries.ENTITY_TYPE.getValue(Identifier.parse(id));
            h.assertTrue(type != null, "accepted inert type must be registered: " + id);
            var entity = type.create(h.getLevel(), EntitySpawnReason.COMMAND);
            h.assertTrue(entity instanceof InertEntity && entity.getType() == type,
                    "native factory must retain the registered inert identity: " + id);
            // Loader factories defer gameplay fields (for example radiation intensity) to load.
            // Complete that native lifecycle before asking the entity to serialize itself.
            entity.load(TagValueInput.create(ProblemReporter.DISCARDING, h.getLevel().registryAccess(), new CompoundTag()));
            var output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, h.getLevel().registryAccess());
            h.assertTrue(entity.save(output) && output.buildResult().getString("id").orElse("").equals(id),
                    "native serialization must retain the entity ID rather than arrow: " + id);
        }
        h.succeed();
    }

    private static void registration(GameTestHelper h) {
        Set<String> ids = Set.of("dragonapi", "rotarycraft", "reactorcraft", "electricraft", "geostrata",
                "chromaticraft", "voidmonster", "testinstance");
        for (String id : ids) {
            ModContainer registered = ContainerAccess.probe(id);
            h.assertTrue(registered == ModList.get().getModContainerById(id).orElseThrow()
                    && registered.getModId().equals(id) && registered.getModInfo().getModId().equals(id),
                    "registered family mod identity must match its actual loader container: " + id);
        }
        h.assertTrue(!DragonAPIMod.getAllMods().isEmpty(), "actual DragonAPI mod subclasses must be constructed");
        for (DragonAPIMod mod : DragonAPIMod.getAllMods()) {
            h.assertTrue(ids.contains(mod.getModId()) && mod.getModContainer() == ContainerAccess.probe(mod.getModId()),
                    "base-mod resolution uses its own declared registration: " + mod.getClass().getName());
        }
        String missing = "dragonapi_missing_registration_fixture";
        try {
            ContainerAccess.probe(missing);
            throw new AssertionError("Unknown mod ID must fail at the registration boundary");
        } catch (IllegalStateException expected) {
            h.assertTrue(expected.getMessage().contains(missing)
                    && expected.getMessage().contains(ContainerAccess.class.getName())
                    && expected.getMessage().contains("@Mod") && expected.getMessage().contains("neoforge.mods.toml"),
                    "registration failure names the missing ID, owner class and actual registration sources");
        }
        h.succeed();
    }

    private abstract static class ContainerAccess extends DragonAPIMod {
        static ModContainer probe(String id) { return requireModContainer(id, ContainerAccess.class); }
    }
}
