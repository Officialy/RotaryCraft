package reika.rotarycraft;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import reika.dragonapi.auxiliary.BlockArrayComputer;
import reika.dragonapi.instantiable.data.blockstruct.BlockArray;

final class DragonAPIBlockArrayComputerTests {
    private static final BlockPos POS = new BlockPos(4, 2, 4);

    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> environment) {
        RotaryGameTests.register(event, environment, "dragonapi_computer_threads", 40, DragonAPIBlockArrayComputerTests::threads);
        RotaryGameTests.register(event, environment, "dragonapi_computer_fill", 40, DragonAPIBlockArrayComputerTests::fill);
        RotaryGameTests.register(event, environment, "dragonapi_computer_queue", 60, DragonAPIBlockArrayComputerTests::queue);
        RotaryGameTests.register(event, environment, "dragonapi_computer_bounds", 40, DragonAPIBlockArrayComputerTests::bounds);
        RotaryGameTests.register(event, environment, "dragonapi_computer_neighbors", 40, DragonAPIBlockArrayComputerTests::neighbors);
    }

    private static void threads(GameTestHelper h) {
        h.setBlock(POS, Blocks.STONE);
        var level = h.getLevel(); var position = h.absolutePos(POS);
        BlockArray source = new BlockArray(List.of(position)).setWorld(level);
        AtomicInteger authorizationCalls = new AtomicInteger();
        BlockArrayComputer task = source.getComputer().setFill(level, Blocks.DIRT.defaultBlockState(),
                (world, pos, previous, replacement) -> { authorizationCalls.incrementAndGet(); return true; });
        boolean rejected = CompletableFuture.supplyAsync(() -> {
            boolean capture = rejectsThread(source::getComputer);
            boolean configuration = rejectsThread(() -> task.setFill(level, Blocks.DIRT.defaultBlockState(),
                    (world, pos, previous, replacement) -> true));
            return capture && configuration && rejectsThread(task);
        }).join();
        h.assertTrue(rejected && authorizationCalls.get() == 0 && level.getBlockState(position).is(Blocks.STONE),
                "off-thread world capture, fill configuration and execution fail before native access/authorization");
        AtomicInteger visited = new AtomicInteger();
        task.setIteration(pos -> visited.incrementAndGet());
        CompletableFuture.runAsync(task).join();
        h.assertTrue(visited.get() == 1 && task.getResult().positions().equals(List.of(position)),
                "the detached snapshot of a server-attached array supports pure worker iteration");
        h.succeed();
    }

    private static boolean rejectsThread(Runnable action) {
        try { action.run(); return false; }
        catch (IllegalStateException expected) { return expected.getMessage().contains("owning server thread"); }
    }

    private static void fill(GameTestHelper h) {
        var level = h.getLevel(); var first = h.absolutePos(POS); var second = first.east();
        h.setBlock(POS, Blocks.STONE); h.setBlock(POS.east(), Blocks.DIRT); h.setBlock(POS.north(), Blocks.GOLD_BLOCK);
        var target = Blocks.OAK_STAIRS.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST)
                .setValue(BlockStateProperties.HALF, Half.TOP);
        BlockArray source = new BlockArray(List.of(first, second)).setWorld(level);
        BlockArrayComputer task = source.getComputer().setFill(level, target,
                (world, pos, previous, replacement) -> !pos.equals(second));
        try { task.run(); h.fail("denied fill succeeded"); }
        catch (SecurityException expected) {
            h.assertTrue(level.getBlockState(first).is(Blocks.STONE) && level.getBlockState(second).is(Blocks.DIRT),
                    "all target permissions are checked before the first native write");
        }
        IllegalArgumentException authorizationFailure = new IllegalArgumentException("authorization service failed");
        task.setFill(level, target, (world, pos, previous, replacement) -> {
            if (pos.equals(second)) throw authorizationFailure;
            return true;
        });
        try { task.run(); h.fail("failed authorization service allowed a fill"); }
        catch (IllegalArgumentException expected) {
            h.assertTrue(expected == authorizationFailure && level.getBlockState(first).is(Blocks.STONE)
                    && level.getBlockState(second).is(Blocks.DIRT), "authorization exceptions propagate before any native write");
        }
        task.setFill(level, target, (world, pos, previous, replacement) -> true);
        level.setBlock(second, Blocks.GOLD_BLOCK.defaultBlockState(), 3);
        try { task.run(); h.fail("stale fill succeeded"); }
        catch (IllegalStateException expected) {
            h.assertTrue(expected.getMessage().contains("Stale") && level.getBlockState(first).is(Blocks.STONE)
                    && level.getBlockState(second).is(Blocks.GOLD_BLOCK), "queued stale work changes no target");
        }
        AtomicInteger checked = new AtomicInteger();
        task.setFill(level, target, (world, pos, previous, replacement) -> {
            h.assertTrue(world == level && world.getServer().isSameThread() && replacement == target
                    && previous.equals(world.getBlockState(pos)), "authorization receives exact native states on the server thread");
            checked.incrementAndGet(); return true;
        });
        source.clear(); // Task membership was detached before the source changed.
        task.run();
        h.assertTrue(checked.get() == 2 && task.getResult().changedBlocks() == 2
                && level.getBlockState(first).equals(target) && level.getBlockState(second).equals(target)
                && h.getBlockState(POS.north()).is(Blocks.GOLD_BLOCK) && source.isEmpty(),
                "authorized fill preserves complete states, reports successful writes and affects only its snapshot");
        task.setFill(level, target, (world, pos, previous, replacement) -> true).run();
        h.assertTrue(task.getResult().changedBlocks() == 0, "already equal states do not count as block changes");
        h.succeed();
    }

    private static void queue(GameTestHelper h) {
        var level = h.getLevel(); var position = h.absolutePos(POS);
        h.setBlock(POS, Blocks.STONE);
        BlockArrayComputer task = new BlockArray(List.of(position)).getComputer()
                .setFill(level, Blocks.DIRT.defaultBlockState(), (world, pos, previous, replacement) -> world.getServer().isSameThread());
        CompletableFuture<BlockArrayComputer.Result> completion = new CompletableFuture<>();
        // The worker explicitly requests server execution and never waits for it from the server thread.
        CompletableFuture.runAsync(() -> level.getServer().execute(() -> {
            try { task.run(); completion.complete(task.getResult()); }
            catch (Throwable failure) { completion.completeExceptionally(failure); }
        }));
        h.succeedWhen(() -> {
            h.assertTrue(completion.isDone(), "explicit server submission must finish");
            h.assertTrue(completion.join().changedBlocks() == 1 && level.getBlockState(position).is(Blocks.DIRT),
                    "worker-submitted fill executes its real native mutation on the server thread");
        });
    }

    private static void bounds(GameTestHelper h) {
        var level = h.getLevel(); var valid = h.absolutePos(POS);
        h.setBlock(POS, Blocks.STONE);
        var outside = new BlockPos(valid.getX(), level.getMaxY() + 1, valid.getZ());
        var task = new BlockArray(List.of(valid, outside)).getComputer();
        try { task.setFill(level, Blocks.DIRT.defaultBlockState(), (world, pos, previous, replacement) -> true);
            h.fail("out-of-height fill capture succeeded"); }
        catch (IllegalArgumentException expected) {
            h.assertTrue(level.getBlockState(valid).is(Blocks.STONE), "invalid target preflight leaves valid targets unchanged");
        }
        var unloaded = valid.offset(4096, 0, 4096);
        h.assertTrue(level.getChunkSource().getChunkNow(unloaded.getX() >> 4, unloaded.getZ() >> 4) == null,
                "fixture must start outside loaded chunks");
        try { new BlockArray(List.of(valid, unloaded)).getComputer().setFill(level, Blocks.DIRT.defaultBlockState(),
                (world, pos, previous, replacement) -> true); h.fail("unloaded fill capture succeeded"); }
        catch (IllegalStateException expected) {
            h.assertTrue(expected.getMessage().contains("not loaded")
                    && level.getChunkSource().getChunkNow(unloaded.getX() >> 4, unloaded.getZ() >> 4) == null
                    && level.getBlockState(valid).is(Blocks.STONE), "preflight never forces a chunk load or writes another target");
        }
        h.succeed();
    }

    private static void neighbors(GameTestHelper h) {
        var level = h.getLevel(); var support = h.absolutePos(POS); var torch = support.above();
        h.setBlock(POS, Blocks.STONE); h.setBlock(POS.above(), Blocks.TORCH);
        var task = new BlockArray(List.of(support, torch)).getComputer().setFill(level, Blocks.AIR.defaultBlockState(),
                (world, pos, previous, replacement) -> true);
        try { task.run(); h.fail("native neighbor change must invalidate the later captured target"); }
        catch (IllegalStateException expected) {
            h.assertTrue(expected.getMessage().contains("after 1 changes") && level.getBlockState(support).isAir()
                    && level.getBlockState(torch).isAir(), "native neighbor updates run; stale later targets are not overwritten");
            try { task.getResult(); h.fail("partially failed native operation advertised success"); }
            catch (IllegalStateException noResult) { /* expected: native world updates are not transactional */ }
        }
        h.succeed();
    }
}
