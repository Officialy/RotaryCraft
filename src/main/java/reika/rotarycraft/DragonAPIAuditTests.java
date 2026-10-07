package reika.rotarycraft;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import reika.dragonapi.auxiliary.trackers.KeyWatcher;
import reika.dragonapi.command.BiomeMapCommand;
import reika.dragonapi.libraries.level.ReikaBlockHelper;
import reika.dragonapi.modinteract.lua.LuaMethod;
import reika.rotarycraft.registry.RotaryBlocks;

/** Shared API regressions requiring real registries, block entities, commands and a player connection. */
final class DragonAPIAuditTests {
    private static final BlockPos POS = new BlockPos(4, 1, 4);
    private DragonAPIAuditTests() {}

    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> environment) {
        RotaryGameTests.register(event, environment, "dragonapi_audit_lua_inventory", 40, DragonAPIAuditTests::inventory);
        RotaryGameTests.register(event, environment, "dragonapi_audit_lua_placer", 40, DragonAPIAuditTests::placer);
        RotaryGameTests.register(event, environment, "dragonapi_audit_keys_breakability", 40, DragonAPIAuditTests::keys);
        RotaryGameTests.register(event, environment, "dragonapi_audit_commands", 40, DragonAPIAuditTests::commands);
        RotaryGameTests.register(event, environment, "dragonapi_audit_protection", 40, DragonAPIAuditTests::protection);
        RotaryGameTests.register(event, environment, "dragonapi_audit_components", 40, DragonAPIAuditTests::components);
        RotaryGameTests.register(event, environment, "dragonapi_audit_disconnect", 40, DragonAPIAuditTests::disconnect);
    }

    static net.minecraft.server.level.ServerPlayer player(GameTestHelper h) {
        var profile = new com.mojang.authlib.GameProfile(java.util.UUID.randomUUID(), "dragonapi-audit-player");
        var cookie = net.minecraft.server.network.CommonListenerCookie.createInitial(profile, false);
        var player = new net.minecraft.server.level.ServerPlayer(h.getLevel().getServer(), h.getLevel(), profile, cookie.clientInformation());
        var connection = new net.minecraft.network.Connection(net.minecraft.network.protocol.PacketFlow.SERVERBOUND);
        new io.netty.channel.embedded.EmbeddedChannel(connection);
        h.getLevel().getServer().getPlayerList().placeNewPlayer(connection, player, cookie);
        return player;
    }

    private static void inventory(GameTestHelper h) {
        h.setBlock(POS, Blocks.CHEST);
        var chest = h.getBlockEntity(POS, ChestBlockEntity.class);
        var hasItem = LuaMethod.getMethod("hasItem", Container.class);
        var getSlot = LuaMethod.getMethod("getSlot", Container.class);
        ItemStack sword = new ItemStack(Items.IRON_SWORD);
        sword.setDamageValue(7);
        chest.setItem(0, sword);
        try {
            h.assertTrue(Boolean.TRUE.equals(LuaMethod.call(hasItem, chest, new Object[]{"minecraft:iron_sword"})[0]), "one-argument lookup must find the item");
            h.assertTrue(Boolean.TRUE.equals(LuaMethod.call(hasItem, chest, new Object[]{"minecraft:iron_sword", 7D})[0]), "damage must match");
            h.assertTrue(Boolean.FALSE.equals(LuaMethod.call(hasItem, chest, new Object[]{"minecraft:iron_sword", 8D})[0]), "different damage must not match");
            h.assertTrue(Boolean.TRUE.equals(LuaMethod.call(hasItem, chest, new Object[]{"minecraft:iron_sword", 7D, 1D})[0]), "three-argument lookup must match");
            var slot = LuaMethod.call(getSlot, chest, new Object[]{0D});
            h.assertTrue("minecraft:iron_sword".equals(slot[0]) && slot[2] instanceof String, "item names and display names must be Lua strings");
            h.assertTrue("Empty".equals(LuaMethod.call(getSlot, chest, new Object[]{1D})[0]), "normal EMPTY must return the documented sentinel");
            try {
                LuaMethod.call(getSlot, chest, new Object[]{-1D});
                h.fail("negative slots must fail as LuaMethodException");
            } catch (LuaMethod.LuaMethodException expected) { }
        } catch (LuaMethod.LuaMethodException | InterruptedException error) { throw new IllegalStateException(error); }
        h.succeed();
    }

    private static void placer(GameTestHelper h) {
        h.setBlock(POS, RotaryBlocks.DC_ENGINE.get());
        var tile = (reika.dragonapi.base.BlockEntityBase)h.getLevel().getBlockEntity(h.absolutePos(POS));
        var method = LuaMethod.getMethod("getPlacer", reika.dragonapi.base.BlockEntityBase.class);
        try {
            var absent = LuaMethod.call(method, tile, new Object[0]);
            h.assertTrue(absent[0] == null && absent[1] == null, "unset placers must return nil without dereferencing a player");
            var player = player(h);
            tile.setPlacer(player);
            var found = LuaMethod.call(method, tile, new Object[0]);
            h.assertTrue(player.getName().getString().equals(found[0]) && player.getUUID().toString().equals(found[1]), "saved placer name and UUID must be strings");
        } catch (LuaMethod.LuaMethodException | InterruptedException error) { throw new IllegalStateException(error); }
        h.succeed();
    }

    private static void keys(GameTestHelper h) {
        var player = player(h);
        KeyWatcher.instance.setKey(player, KeyWatcher.Key.LALT, true);
        KeyWatcher.instance.setKey(player, KeyWatcher.Key.PGUP, false);
        h.assertTrue(KeyWatcher.instance.isKeyDown(player, KeyWatcher.Key.LALT) && !KeyWatcher.instance.isKeyDown(player, KeyWatcher.Key.PGUP), "Alt and Page Up must be independent");
        reika.dragonapi.auxiliary.SessionLifecycle.logout(new net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent(player));
        h.assertTrue(!KeyWatcher.instance.isKeyDown(player, KeyWatcher.Key.LALT), "logout must clear pressed keys");
        h.setBlock(POS, Blocks.BEDROCK);
        h.assertTrue(ReikaBlockHelper.isUnbreakable(h.getLevel(), h.absolutePos(POS), Blocks.BEDROCK, player), "ordinary bedrock must be unbreakable");
        h.setBlock(POS, Blocks.STONE);
        h.assertTrue(!ReikaBlockHelper.isUnbreakable(h.getLevel(), h.absolutePos(POS), Blocks.STONE, player), "ordinary stone must remain breakable");
        h.succeed();
    }

    private static void commands(GameTestHelper h) {
        var server = h.getLevel().getServer();
        var dispatcher = server.getCommands().getDispatcher();
        try {
            h.assertTrue(dispatcher.execute("profileevent display", server.createCommandSourceStack()) == 1, "profiling display must support console before profiling starts");
            h.assertTrue(dispatcher.execute("profileevent enable java.lang.String", server.createCommandSourceStack()) == 1, "invalid event classes must report a status without a raw exception");
            h.assertTrue(dispatcher.execute("profileevent disable", server.createCommandSourceStack()) == 1, "disable must be reachable");
            var player = player(h);
            h.assertTrue(dispatcher.execute("biomepng seed \"-1,0,1\" 2 1 0 false", server.createCommandSourceStack().withEntity(player)) == 3, "seed/list and actual range/resolution must reach map generation");
            h.assertTrue(dispatcher.execute("entitylist dedicated_server", server.createCommandSourceStack()) == 1, "entity registry dump must support console");
            h.assertTrue(dispatcher.getRoot().getChild("checker").getChild("enable").getChild("mod") != null, "checker requires a real mod argument");
        } catch (com.mojang.brigadier.exceptions.CommandSyntaxException error) { throw new IllegalStateException(error); }
        h.assertTrue(BiomeMapCommand.parseSeeds("-3--1,5").equals(List.of(-3L, -2L, -1L, 5L)), "negative seed ranges must parse correctly");
        h.succeed();
    }

    private static void protection(GameTestHelper h) {
        var player = player(h);
        h.setBlock(POS, Blocks.STONE);
        var pos = h.absolutePos(POS);
        java.util.function.Consumer<net.neoforged.neoforge.event.level.block.BreakBlockEvent> deny = event -> {
            if (event.getPos().equals(pos)) event.setCanceled(true);
        };
        var bus = net.neoforged.neoforge.common.NeoForge.EVENT_BUS;
        h.assertTrue(reika.dragonapi.libraries.ReikaPlayerAPI.playerCanBreakAt(h.getLevel(), pos, player), "unprotected stone must allow the owner");
        bus.addListener(deny);
        try {
            h.assertTrue(!reika.dragonapi.libraries.ReikaPlayerAPI.playerCanBreakAt(h.getLevel(), pos, player), "a current BreakBlockEvent protection listener must be respected");
        } finally { bus.unregister(deny); }
        h.succeed();
    }

    private static void components(GameTestHelper h) {
        var first = net.neoforged.neoforge.common.crafting.DataComponentIngredient.of(net.minecraft.core.component.DataComponents.DAMAGE, 7, Items.IRON_SWORD);
        var same = net.neoforged.neoforge.common.crafting.DataComponentIngredient.of(net.minecraft.core.component.DataComponents.DAMAGE, 7, Items.IRON_SWORD);
        var other = net.neoforged.neoforge.common.crafting.DataComponentIngredient.of(net.minecraft.core.component.DataComponents.DAMAGE, 8, Items.IRON_SWORD);
        h.assertTrue(reika.dragonapi.libraries.ReikaIngredientHelper.ingredientsEquivalentDeep(first, same, h.getLevel().registryAccess()), "equivalent component predicates must compare equal");
        h.assertTrue(!reika.dragonapi.libraries.ReikaIngredientHelper.ingredientsEquivalentDeep(first, other, h.getLevel().registryAccess()), "different damage predicates must not compare equal from empty samples");
        h.succeed();
    }

    private static void disconnect(GameTestHelper h) {
        var player = player(h);
        h.assertTrue(player.connection.getConnection().isConnected(), "mock player must have a live connection");
        reika.dragonapi.libraries.ReikaPlayerAPI.kickPlayer(player, "DragonAPI audit disconnect test");
        h.assertTrue(!player.connection.getConnection().isConnected(), "kick must close the connection");
        h.succeed();
    }
}
