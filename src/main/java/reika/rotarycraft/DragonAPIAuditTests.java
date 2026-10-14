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
        RotaryGameTests.register(event, environment, "dragonapi_audit_chat_formatting", 40, DragonAPIAuditTests::chatFormatting);
        RotaryGameTests.register(event, environment, "dragonapi_audit_event_profiler", 40, DragonAPIAuditTests::eventProfiler);
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

    private static void eventProfiler(GameTestHelper h) {
        var bus = net.neoforged.neoforge.common.NeoForge.EVENT_BUS;
        h.assertTrue(bus instanceof reika.dragonapi.auxiliary.trackers.ProfilingEventBus,
                "the actual NeoForge game bus must install native listener instrumentation");
        var calls = new java.util.concurrent.atomic.AtomicInteger();
        java.util.function.Consumer<reika.dragonapi.instantiable.event.SetBlockEvent.Post> listener = event -> calls.incrementAndGet();
        bus.addListener(reika.dragonapi.instantiable.event.SetBlockEvent.Post.class, listener);
        var server = h.getLevel().getServer();
        var dispatcher = server.getCommands().getDispatcher();
        try {
            reika.dragonapi.auxiliary.trackers.EventProfiler.finishProfiling();
            dispatcher.execute("profileevent enable reika.dragonapi.instantiable.event.SetBlockEvent$Post", server.createCommandSourceStack());
            h.setBlock(POS, Blocks.GOLD_BLOCK);
            var report = reika.dragonapi.auxiliary.trackers.EventProfiler.snapshot();
            h.assertTrue(report.running() && report.eventFires() > 0 && calls.get() > 0,
                    "real chunk mutation must be measured through the game bus without explicit timing hooks");
            h.assertTrue(report.handlers().stream().mapToLong(reika.dragonapi.auxiliary.trackers.EventProfiler.EventProfile::getFireCount).sum() >= calls.get()
                    && report.totalHandlerTime() > 0, "the actual callback must have nonzero elapsed time and calls");
            dispatcher.execute("profileevent display", server.createCommandSourceStack());
            dispatcher.execute("profileevent disable", server.createCommandSourceStack());
            long finishedFires = reika.dragonapi.auxiliary.trackers.EventProfiler.snapshot().eventFires();
            bus.unregister(listener); int finishedCalls = calls.get(); h.setBlock(POS, Blocks.IRON_BLOCK);
            h.assertTrue(calls.get() == finishedCalls && reika.dragonapi.auxiliary.trackers.EventProfiler.snapshot().eventFires() == finishedFires,
                    "stopping must restore native listener identity for unregister and retain the completed report");
        } catch (com.mojang.brigadier.exceptions.CommandSyntaxException error) { throw new IllegalStateException(error); }
        finally { reika.dragonapi.auxiliary.trackers.EventProfiler.finishProfiling(); bus.unregister(listener); }
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
            h.assertTrue(dispatcher.execute("guide", server.createCommandSourceStack().withEntity(player)) == 1, "guide stack must be created after registry binding");
            boolean translatedGuide = false;
            for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
                var name = player.getInventory().getItem(slot).get(net.minecraft.core.component.DataComponents.CUSTOM_NAME);
                if (name != null && name.getContents() instanceof net.minecraft.network.chat.contents.TranslatableContents contents
                        && contents.getKey().equals("item.dragonapi.guide.name")) translatedGuide = true;
            }
            h.assertTrue(translatedGuide, "guide name must retain its translation key for the receiving client");
            h.assertTrue(dispatcher.execute("biomepng seed \"-1,0,1\" 2 1 0 false", server.createCommandSourceStack().withEntity(player)) == 3, "seed/list and actual range/resolution must reach map generation");
            h.assertTrue(BiomeMapCommand.processCommand(server.createCommandSourceStack(),
                    new String[]{player.getScoreboardName(), "1", "1"}) == 1,
                    "console map target must consume the player argument before range/resolution");
            h.assertTrue(dispatcher.execute("entitylist dedicated_server", server.createCommandSourceStack()) == 1, "entity registry dump must support console");
            h.assertTrue(dispatcher.execute("entitylist invalid", server.createCommandSourceStack()) == 0, "invalid side must report command failure");
            h.assertTrue(dispatcher.execute("entitylist client", server.createCommandSourceStack()) == 0, "client dump must reject a console source");
            h.assertTrue(dispatcher.getRoot().getChild("checker").getChild("enable").getChild("mod") != null, "checker requires a real mod argument");
        } catch (com.mojang.brigadier.exceptions.CommandSyntaxException error) { throw new IllegalStateException(error); }
        h.assertTrue(BiomeMapCommand.parseSeeds("-3--1,5").equals(List.of(-3L, -2L, -1L, 5L)), "negative seed ranges must parse correctly");
        h.succeed();
    }

    private static void chatFormatting(GameTestHelper h) {
        // These helpers format before their client-side delivery guard. The old specifiers
        // throw for a nonempty stack, integer coordinates, and block output respectively.
        var level = h.getLevel();
        reika.dragonapi.libraries.io.ReikaChatHelper.writeItemStack(level, new ItemStack(Items.STONE, 3));
        reika.dragonapi.libraries.io.ReikaChatHelper.writeItemStack(level, null);
        reika.dragonapi.libraries.io.ReikaChatHelper.writeCoords(level, h.absolutePos(POS));
        reika.dragonapi.libraries.io.ReikaChatHelper.writeBlockAtCoords(level, h.absolutePos(POS));
        reika.dragonapi.libraries.io.ReikaChatHelper.writeLocalString("command.dragonapi.entitylist.header");
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
