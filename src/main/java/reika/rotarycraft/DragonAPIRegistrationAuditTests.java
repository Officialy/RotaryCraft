package reika.rotarycraft;

import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import reika.dragonapi.base.DragonAPIMod;

/** Real family loader registrations and failure diagnostics, without constructing synthetic mod instances. */
final class DragonAPIRegistrationAuditTests {
    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> environment) {
        RotaryGameTests.register(event, environment, "dragonapi_registration_all_mod_ids", 40,
                DragonAPIRegistrationAuditTests::registration);
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
