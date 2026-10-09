package reika.rotarycraft;

import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.ReferenceCountUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.event.PlayLevelSoundEvent;
import net.neoforged.neoforge.common.NeoForge;
import reika.dragonapi.auxiliary.ProgressiveRecursiveBreaker;
import reika.dragonapi.auxiliary.ProgressiveRecursiveBreaker.BreakerCallback;
import reika.dragonapi.auxiliary.ProgressiveRecursiveBreaker.ProgressiveBreaker;
import reika.dragonapi.auxiliary.trackers.TickRegistry;
import reika.dragonapi.instantiable.data.immutable.BlockKey;
import reika.dragonapi.libraries.io.ReikaSoundHelper;

/** Real server-tick regressions, including sound packets sent through native player connections. */
final class DragonAPIRecursiveBreakerTests {
    private static final BlockPos POS = new BlockPos(4, 3, 4);

    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> environment) {
        soundTest(event, environment, "water_source", Blocks.WATER.defaultBlockState(), SoundEvents.GENERIC_SWIM);
        soundTest(event, environment, "water_flowing", Fluids.FLOWING_WATER.getFlowing(5, false).createLegacyBlock(), SoundEvents.GENERIC_SWIM);
        soundTest(event, environment, "lava_source", Blocks.LAVA.defaultBlockState(), SoundEvents.GHAST_SHOOT);
        soundTest(event, environment, "lava_flowing", Fluids.FLOWING_LAVA.getFlowing(5, false).createLegacyBlock(), SoundEvents.GHAST_SHOOT);
        soundTest(event, environment, "solid", Blocks.STONE.defaultBlockState(), SoundEvents.STONE_BREAK);
        soundTest(event, environment, "waterlogged_solid", Blocks.OAK_STAIRS.defaultBlockState()
                .setValue(BlockStateProperties.WATERLOGGED, true), SoundEvents.WOOD_BREAK);
        RotaryGameTests.register(event, environment, "dragonapi_breaker_no_break_no_sound", 40, DragonAPIRecursiveBreakerTests::quiet);
        RotaryGameTests.register(event, environment, "dragonapi_breaker_shared_sound_helpers", 40, DragonAPIRecursiveBreakerTests::helpers);
        RotaryGameTests.register(event, environment, "dragonapi_breaker_sound_hooks", 40, DragonAPIRecursiveBreakerTests::hooks);
    }

    private static void soundTest(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> environment,
            String name, BlockState state, SoundEvent expected) {
        RotaryGameTests.register(event, environment, "dragonapi_breaker_" + name, 40, h -> sound(h, state, expected));
    }

    private static void sound(GameTestHelper h, BlockState state, SoundEvent expected) {
        var level = h.getLevel();
        BlockPos pos = h.absolutePos(POS);
        var miner = DragonAPIAuditTests.player(h);
        var nearby = DragonAPIAuditTests.player(h);
        var distant = DragonAPIAuditTests.player(h);
        var players = List.of(miner, nearby, distant);
        ProgressiveBreaker breaker = null;
        try {
            miner.setPos(Vec3.atCenterOf(pos.above()));
            nearby.setPos(Vec3.atCenterOf(pos.above().east()));
            distant.setPos(Vec3.atCenterOf(pos.offset(128, 0, 0)));
            level.setBlock(pos, state, Block.UPDATE_CLIENTS);
            for (var player : players) sounds(player);
            breaker = ProgressiveRecursiveBreaker.instance.addBlockPosWithReturn(level, pos, 1);
            breaker.drops = false;
            breaker.causeUpdates = false;
            breaker.player = miner;
            var type = state.getSoundType(level, pos, miner);
            float volume = expected == SoundEvents.GENERIC_SWIM || expected == SoundEvents.GHAST_SHOOT ? 1 : type.getVolume();
            float pitch = expected == SoundEvents.GENERIC_SWIM || expected == SoundEvents.GHAST_SHOOT ? 1 : type.getPitch();
            ProgressiveRecursiveBreaker.instance.tick(TickRegistry.TickType.WORLD, level);
            h.assertTrue(level.getBlockState(pos).isAir(), "the server wave must actually remove its admitted block");
            assertSound(h, sounds(miner), pos, expected, volume, pitch);
            assertSound(h, sounds(nearby), pos, expected, volume, pitch);
            h.assertTrue(sounds(distant).isEmpty(), "native sound range must exclude a distant player");
            h.succeed();
        } finally {
            if (breaker != null) breaker.terminate();
            for (var player : players) level.getServer().getPlayerList().remove(player);
        }
    }

    private static void assertSound(GameTestHelper h, List<ClientboundSoundPacket> packets,
            BlockPos pos, SoundEvent expected, float volume, float pitch) {
        assertSound(h, packets, Vec3.atCenterOf(pos), expected, SoundSource.BLOCKS, volume, pitch);
    }

    private static void assertSound(GameTestHelper h, List<ClientboundSoundPacket> packets,
            Vec3 pos, SoundEvent expected, SoundSource source, float volume, float pitch) {
        h.assertTrue(packets.size() == 1, "each nearby connection receives exactly one native break sound");
        var packet = packets.getFirst();
        h.assertTrue(packet.getSound().value() == expected && packet.getSource() == source,
                "the actual block/fluid family selects the expected sound and category");
        // 26.3 packet getters divide their eighth-block integers by 8.0F. At distant test
        // origins the resulting float cannot retain the half-block centre's double precision.
        h.assertTrue(packet.getX() == (double)(float)pos.x && packet.getY() == (double)(float)pos.y
                && packet.getZ() == (double)(float)pos.z, "sound retains the requested position at native packet precision");
        h.assertTrue(packet.getVolume() == volume && packet.getPitch() == pitch,
                "block sound volume and pitch reach the connection unchanged");
    }

    private static List<ClientboundSoundPacket> sounds(ServerPlayer player) {
        var channel = (EmbeddedChannel) player.connection.getConnection().channel();
        channel.runPendingTasks();
        List<ClientboundSoundPacket> result = new ArrayList<>();
        Object outbound;
        while ((outbound = channel.readOutbound()) != null) {
            if (outbound instanceof ClientboundSoundPacket sound) result.add(sound);
            ReferenceCountUtil.release(outbound);
        }
        channel.checkException();
        return result;
    }

    private static void quiet(GameTestHelper h) {
        var level = h.getLevel();
        BlockPos pos = h.absolutePos(POS);
        var player = DragonAPIAuditTests.player(h);
        player.setPos(Vec3.atCenterOf(pos.above()));
        try {
            for (int mode = 0; mode < 3; mode++) {
                level.setBlock(pos, Blocks.WATER.defaultBlockState(), Block.UPDATE_CLIENTS);
                sounds(player);
                var breaker = ProgressiveRecursiveBreaker.instance.addBlockPosWithReturn(level, pos, 1);
                try {
                    breaker.drops = false;
                    if (mode == 0) breaker.doBreak = false;
                    if (mode == 1) breaker.passThrough.add(new BlockKey(Blocks.WATER));
                    if (mode == 2) breaker.call = new BreakerCallback() {
                        @Override public boolean canBreak(ProgressiveBreaker b, Level world, BlockPos at, Block block) { return false; }
                        @Override public void onPreBreak(ProgressiveBreaker b, Level world, BlockPos at, Block block) { }
                        @Override public void onPostBreak(ProgressiveBreaker b, Level world, BlockPos at, Block block) { }
                        @Override public void onFinish(ProgressiveBreaker b) { }
                    };
                    ProgressiveRecursiveBreaker.instance.tick(TickRegistry.TickType.WORLD, level);
                    h.assertTrue(level.getBlockState(pos).is(Blocks.WATER) && sounds(player).isEmpty(),
                            "nonbreaking traversal, pass-through and denied callbacks must stay silent");
                } finally {
                    breaker.terminate();
                }
            }
            h.succeed();
        } finally {
            level.getServer().getPlayerList().remove(player);
        }
    }

    private record SoundCall(Runnable emit, SoundEvent sound, SoundSource source, Vec3 pos, float volume, float pitch) { }

    private static void helpers(GameTestHelper h) {
        var level = h.getLevel();
        BlockPos pos = h.absolutePos(POS);
        Vec3 centre = Vec3.atCenterOf(pos);
        int x = pos.getX(), y = pos.getY(), z = pos.getZ();
        var player = DragonAPIAuditTests.player(h);
        player.setPos(centre.add(0, 1, 0));
        try {
            // The named block is no longer present: removal/placement callers still name its sound.
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
            var calls = List.of(
                    new SoundCall(() -> ReikaSoundHelper.playBreakSound(level, pos, Blocks.STONE), SoundEvents.STONE_BREAK, SoundSource.BLOCKS, centre, 1, 1),
                    new SoundCall(() -> ReikaSoundHelper.playBreakSound(level, x, y, z, Blocks.STONE), SoundEvents.STONE_BREAK, SoundSource.BLOCKS, centre, 1, 1),
                    new SoundCall(() -> ReikaSoundHelper.playStepSound(level, x, y, z, Blocks.STONE), SoundEvents.STONE_STEP, SoundSource.BLOCKS, centre, 1, 1),
                    new SoundCall(() -> ReikaSoundHelper.playPlaceSound(level, x, y, z, Blocks.STONE), SoundEvents.STONE_PLACE, SoundSource.BLOCKS, centre, 1, 1),
                    new SoundCall(() -> ReikaSoundHelper.playBreakSound(level, x, y, z, Blocks.STONE, .5F, 1.5F), SoundEvents.STONE_BREAK, SoundSource.BLOCKS, centre, .5F, 1.5F),
                    new SoundCall(() -> ReikaSoundHelper.playStepSound(level, x, y, z, Blocks.STONE, .5F, 1.5F), SoundEvents.STONE_STEP, SoundSource.BLOCKS, centre, .5F, 1.5F),
                    new SoundCall(() -> ReikaSoundHelper.playPlaceSound(level, x, y, z, Blocks.STONE, .5F, 1.5F), SoundEvents.STONE_PLACE, SoundSource.BLOCKS, centre, .5F, 1.5F),
                    new SoundCall(() -> ReikaSoundHelper.playSoundAtBlock(level, x, y, z, SoundEvents.FIRE_EXTINGUISH, .5F, 1.5F), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, centre, .5F, 1.5F),
                    new SoundCall(() -> ReikaSoundHelper.playSoundAtBlock(level, pos, SoundEvents.FIRE_EXTINGUISH, .5F, 1.5F), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, centre, .5F, 1.5F),
                    new SoundCall(() -> ReikaSoundHelper.playSoundAtBlock(level, x, y, z, SoundEvents.FIRE_EXTINGUISH), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, centre, 1, 1),
                    new SoundCall(() -> ReikaSoundHelper.playSoundAtBlock(level, pos, SoundEvents.FIRE_EXTINGUISH), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, centre, 1, 1),
                    new SoundCall(() -> ReikaSoundHelper.playNormalClientSound(level, centre.x, centre.y, centre.z, SoundEvents.FIRE_EXTINGUISH, .5F, 1.5F, true), SoundEvents.FIRE_EXTINGUISH, SoundSource.AMBIENT, centre, .5F, 1.5F),
                    new SoundCall(() -> ReikaSoundHelper.playSoundAtEntity(level, player, SoundEvents.FIRE_EXTINGUISH), SoundEvents.FIRE_EXTINGUISH, SoundSource.AMBIENT, player.position(), 1, 1),
                    new SoundCall(() -> ReikaSoundHelper.playSoundAtEntity(level, player, SoundEvents.FIRE_EXTINGUISH, .5F, 1.5F), SoundEvents.FIRE_EXTINGUISH, SoundSource.AMBIENT, player.position(), .5F, 1.5F));
            for (var call : calls) {
                sounds(player);
                call.emit.run();
                assertSound(h, sounds(player), call.pos, call.sound, call.source, call.volume, call.pitch);
            }
            h.succeed();
        } finally {
            level.getServer().getPlayerList().remove(player);
        }
    }

    private static void hooks(GameTestHelper h) {
        var level = h.getLevel();
        BlockPos pos = h.absolutePos(POS);
        Vec3 centre = Vec3.atCenterOf(pos);
        var player = DragonAPIAuditTests.player(h);
        player.setPos(centre.add(0, 1, 0));
        boolean[] cancel = {true};
        int[] events = {0};
        Consumer<PlayLevelSoundEvent.AtPosition> listener = event -> {
            if (event.getLevel() != level || !event.getPosition().equals(centre)) return;
            events[0]++;
            if (cancel[0]) event.setCanceled(true);
            else {
                event.setSound(net.minecraft.core.registries.BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.WOOD_BREAK));
                event.setSource(SoundSource.AMBIENT);
                event.setNewVolume(.5F);
                event.setNewPitch(1.5F);
            }
        };
        NeoForge.EVENT_BUS.addListener(listener);
        try {
            sounds(player);
            ReikaSoundHelper.playBreakSound(level, pos, Blocks.STONE);
            h.assertTrue(events[0] == 1 && sounds(player).isEmpty(), "cancelled native sound hooks must suppress packets");
            cancel[0] = false;
            ReikaSoundHelper.playBreakSound(level, pos, Blocks.STONE);
            h.assertTrue(events[0] == 2, "each server play must reach the native sound hook exactly once");
            assertSound(h, sounds(player), centre, SoundEvents.WOOD_BREAK, SoundSource.AMBIENT, .5F, 1.5F);
            h.succeed();
        } finally {
            NeoForge.EVENT_BUS.unregister(listener);
            level.getServer().getPlayerList().remove(player);
        }
    }
}
