package reika.rotarycraft;

import java.lang.reflect.Method;
import java.util.List;
import net.minecraft.ReportedException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.common.config.NeoForgeSyncedConfig;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import reika.dragonapi.DragonAPI;
import reika.rotarycraft.blockentities.storage.BlockEntityReservoir;
import reika.rotarycraft.registry.RotaryBlocks;

/** Controlled faults in a real registered reservoir type, through its production ticker. */
final class DragonAPITickFailureAuditTests {
    private static final BlockPos POS = new BlockPos(4, 1, 4);

    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> environment) {
        RotaryGameTests.register(event, environment, "dragonapi_tick_failure_propagation", 40,
                DragonAPITickFailureAuditTests::propagation);
        RotaryGameTests.register(event, environment, "dragonapi_tick_failure_native_policy", 40,
                DragonAPITickFailureAuditTests::nativePolicy);
        RotaryGameTests.register(event, environment, "dragonapi_tick_failure_handled", 40,
                DragonAPITickFailureAuditTests::handled);
    }

    private static void propagation(GameTestHelper h) {
        List<Throwable> faults = List.of(new IndexOutOfBoundsException("tick bounds"),
                new NullPointerException("tick null"), new ClassCastException("tick cast"),
                new ArithmeticException("tick arithmetic"), new IllegalArgumentException("tick argument"),
                new IllegalStateException("tick state"), new LinkageError("tick linkage"));
        for (Throwable fault : faults) {
            for (boolean animation : List.of(false, true)) {
                Probe probe = place(h, fault, animation, false);
                try {
                    try {
                        ticker(probe).tick(h.getLevel(), probe.getBlockPos(), probe.getBlockState(), probe);
                        h.fail("unexpected tick failure was swallowed: " + fault.getClass());
                    } catch (RuntimeException | Error actual) {
                        h.assertTrue(actual == fault, "the original failure must reach the native ticker unchanged");
                    }
                    assertAborted(h, probe);
                    h.assertTrue(probe.animationCalls == 1 && probe.initializationCalls == (animation ? 0 : 1),
                            "failure stops at its lifecycle hook");
                } finally {
                    h.setBlock(POS, Blocks.AIR);
                }
            }
        }
        h.succeed();
    }

    private static void nativePolicy(GameTestHelper h) {
        // The actual chunk ticker requires an entity-ticking chunk; the GameTest arena holds it loaded.
        h.runAfterDelay(2, () -> {
            var policy = NeoForgeSyncedConfig.INSTANCE.removeErroringBlockEntities;
            boolean previous = policy.getRaw();
            try {
                policy.set(false);
                policy.clearCache(); // worldRestart settings retain their old cache after set().
                Throwable fault = new NullPointerException("native report cause");
                Probe probe = place(h, fault, false, false);
                try {
                    nativeTicker(h, probe).tick();
                    h.fail("the native default policy must produce a crash report");
                } catch (ReportedException report) {
                    h.assertTrue(report.getCause() == fault && report.getReport().getTitle().equals("Ticking block entity"),
                            "native report retains the complete original failure");
                    String details = report.getReport().getDetails();
                    h.assertTrue(details.contains("Block entity being ticked") && details.contains("rotarycraft:reservoir")
                            && details.contains(probe.getBlockPos().getX() + "," + probe.getBlockPos().getY() + "," + probe.getBlockPos().getZ()),
                            "native report identifies the registry type and exact world position");
                    assertAborted(h, probe);
                    h.assertTrue(!probe.isRemoved(), "the default policy must not silently delete the block entity");
                } finally {
                    h.setBlock(POS, Blocks.AIR);
                }

                policy.set(true);
                policy.clearCache();
                Probe removable = place(h, new ArithmeticException("native removal cause"), true, false);
                TickingBlockEntity nativeTicker = nativeTicker(h, removable);
                nativeTicker.tick();
                assertAborted(h, removable);
                h.assertTrue(removable.isRemoved() && !h.getLevel().getChunkAt(removable.getBlockPos())
                        .getBlockEntities().containsKey(removable.getBlockPos()),
                        "opt-in native removal unregisters the failing entity from the chunk");
                nativeTicker.tick();
                h.assertTrue(removable.animationCalls == 1, "a removed failing entity must not be retried");
                h.assertTrue(h.getBlockState(POS).is(RotaryBlocks.RESERVOIR.get()),
                        "failure recovery follows native removal semantics for the block state");
            } finally {
                policy.set(previous); // In-memory only: never save or alter the user's configuration.
                policy.clearCache();
                h.setBlock(POS, Blocks.AIR);
            }
            h.setBlock(POS, RotaryBlocks.RESERVOIR.get());
            BlockEntityReservoir healthy = h.getBlockEntity(POS, BlockEntityReservoir.class);
            h.runAfterDelay(3, () -> {
                h.assertTrue(h.getBlockEntity(POS, BlockEntityReservoir.class) == healthy
                        && healthy.getBlockEntityAge() > 0, "a fresh real reservoir resumes normal world ticking");
                h.succeed();
            });
        });
    }

    private static void handled(GameTestHelper h) {
        Throwable fault = new IllegalArgumentException("explicitly handled operation",
                new IllegalStateException("original nested cause"));
        LogEvent[] captured = {null};
        AbstractAppender capture = new AbstractAppender("DragonAPITickFailureCapture", null, null, true, Property.EMPTY_ARRAY) {
            @Override public void append(LogEvent event) {
                if (event.getThrown() == fault) captured[0] = event.toImmutable();
            }
        };
        Logger logger = (Logger) DragonAPI.LOGGER;
        capture.start();
        logger.addAppender(capture);
        try {
            Probe probe = place(h, fault, false, true);
            ticker(probe).tick(h.getLevel(), probe.getBlockPos(), probe.getBlockState(), probe);
            h.assertTrue(probe.handledCalls == 1 && probe.bodyCalls == 1 && probe.callbackCalls == 1
                    && probe.syncStageCalls == 2
                    && probe.getBlockEntityAge() == 1 && probe.getTicksExisted() == 0 && !probe.isRemoved(),
                    "caller-handled failures still report and complete the first natural tick");
            h.assertTrue(captured[0] != null && captured[0].getThrown() == fault
                    && captured[0].getThrown().getCause() == fault.getCause(),
                    "handled failure log retains the Throwable and complete nested cause");
            String message = captured[0].getMessage().getFormattedMessage();
            h.assertTrue(message.contains("rotarycraft:reservoir") && message.contains(probe.getClass().getName())
                    && message.contains(probe.getBlockPos().toString())
                    && message.contains(h.getLevel().dimension().identifier().toString()) && message.contains("[server]"),
                    "handled error reports registry type, implementation, position, dimension and logical side");
        } finally {
            logger.removeAppender(capture);
            capture.stop();
            h.setBlock(POS, Blocks.AIR);
        }
        h.succeed();
    }

    private static Probe place(GameTestHelper h, Throwable fault, boolean animation, boolean handle) {
        h.setBlock(POS, Blocks.AIR);
        h.setBlock(POS, RotaryBlocks.RESERVOIR.get());
        Probe probe = new Probe(h.absolutePos(POS), h.getBlockState(POS), fault, animation, handle);
        h.getLevel().setBlockEntity(probe);
        probe.scheduleCallback(() -> probe.callbackCalls++, 0);
        h.assertTrue(h.getBlockEntity(POS, BlockEntityReservoir.class) == probe
                && probe.getType().isValid(probe.getBlockState()), "fault probe retains the production registry type");
        return probe;
    }

    @SuppressWarnings("unchecked")
    private static BlockEntityTicker<Probe> ticker(Probe probe) {
        // Probe is a reservoir subclass and uses the same registered type and production ticker.
        return (BlockEntityTicker<Probe>) probe.getBlockState().getTicker(probe.getLevel(), probe.getType());
    }

    private static TickingBlockEntity nativeTicker(GameTestHelper h, Probe probe) {
        try {
            // Access only the native wrapper factory: no duplicated crash-report/removal implementation.
            Method factory = LevelChunk.class.getDeclaredMethod("createTicker", BlockEntity.class, BlockEntityTicker.class);
            factory.setAccessible(true);
            return (TickingBlockEntity) factory.invoke(h.getLevel().getChunkAt(probe.getBlockPos()), probe, ticker(probe));
        } catch (ReflectiveOperationException e) {
            throw new AssertionError("26.3 native block entity ticker factory changed", e);
        }
    }

    private static void assertAborted(GameTestHelper h, Probe probe) {
        h.assertTrue(probe.bodyCalls == 0 && probe.callbackCalls == 0 && probe.syncStageCalls == 0
                && probe.getBlockEntityAge() == 0,
                "failed lifecycle must abort synchronization, machine logic, callbacks and completed age advancement");
    }

    private static final class Probe extends BlockEntityReservoir {
        private final Throwable fault;
        private final boolean failAnimation;
        private final boolean handle;
        private int animationCalls, initializationCalls, bodyCalls, callbackCalls, handledCalls, syncStageCalls;

        private Probe(BlockPos pos, BlockState state, Throwable fault, boolean animation, boolean handle) {
            super(pos, state);
            this.fault = fault;
            this.failAnimation = animation;
            this.handle = handle;
        }

        @Override public void updateEntity(Level level, BlockPos pos) {
            super.updateEntity();
            bodyCalls++;
        }

        @Override protected void animateWithTick(Level level, BlockPos pos) {
            animationCalls++;
            if (failAnimation) fail();
        }

        @Override protected void onFirstTick(Level level, BlockPos pos) {
            initializationCalls++;
            if (handle) {
                try { fail(); }
                catch (IllegalArgumentException expected) { writeError(expected); handledCalls++; }
            } else if (!failAnimation) fail();
        }

        @Override protected boolean shouldDoInitialFullSync() { syncStageCalls++; return false; }

        @Override protected boolean shouldSendSyncPackets() { syncStageCalls++; return false; }

        private void fail() {
            if (fault instanceof Error error) throw error;
            throw (RuntimeException) fault;
        }
    }
}
