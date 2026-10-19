package reika.rotarycraft;

import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.network.registration.NetworkRegistry;
import reika.dragonapi.test.GameTestPlayers;

/** Real login with optional mod payloads and a mutable game mode, rather than vanilla's pinned mock. */
final class DragonAPIMockPlayerTests {
    static void register(RegisterGameTestsEvent event,Holder<TestEnvironmentDefinition<?>> environment) {
        RotaryGameTests.register(event,environment,"dragonapi_mock_player_modes_and_payloads",40,DragonAPIMockPlayerTests::check);
    }
    private static void check(GameTestHelper helper) {
        var player=GameTestPlayers.joined(helper,GameType.SURVIVAL);
        try {
            helper.assertTrue(player.gameMode()==GameType.SURVIVAL && !player.getAbilities().instabuild,
                    "joined mock must use the requested survival mode instead of the server default");
            if(net.neoforged.fml.ModList.get().isLoaded("curios"))
                helper.assertTrue(NetworkRegistry.hasChannel(player.connection,Identifier.parse("curios:sync_data")),
                        "mock login must negotiate the real installed Curios payload");
            player.setGameMode(GameType.CREATIVE);
            helper.assertTrue(player.gameMode()==GameType.CREATIVE && player.getAbilities().instabuild,
                    "real mock game mode and abilities must remain changeable");
            player.setGameMode(GameType.SURVIVAL);
            helper.assertTrue(player.gameMode()==GameType.SURVIVAL && !player.getAbilities().instabuild,
                    "returning to survival must update both the queried mode and abilities");
        } finally { helper.getLevel().getServer().getPlayerList().remove(player); }
        helper.succeed();
    }
}
