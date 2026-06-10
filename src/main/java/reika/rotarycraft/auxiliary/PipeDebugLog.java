/*******************************************************************************
 * @author OfficialyMax (port debug helper)
 *
 * Targeted timing/event logger for the pipe + sync hot paths. The user has reported
 * persistent lag on pipe placement / attach / break despite a stack of theoretical fixes
 * (recompute two-phase, shouldDoInitialFullSync opt-out, state-tracker tightening,
 * writeSyncTag has-fluid byte, BE_NBT_SYNC routing, removed redundant markAndNotifyBlock /
 * setBlocksDirty calls). Without runtime measurements we're guessing. This class lets us
 * see exactly what's firing how often and how long each thing takes.
 *
 * <p>Usage: wrap a code block in {@link #time(String, Runnable)}, or use {@link #event}
 * for a fire-and-forget event tag. Logs are gated by {@link #ENABLED}; flip that to
 * {@code false} once we've diagnosed the lag.
 *
 * <p>Output goes through the standard RotaryCraft logger with a {@code [PipeDbg]} prefix
 * so grep-ability is easy. Per-event-tag stats accumulate in {@link #stats} so a periodic
 * dump shows aggregate cost (e.g. "recomputeConnections fired 230× in last minute, total
 * 45ms"). Dump is triggered by {@link #maybeDumpStats} which any tick handler can call.
 ******************************************************************************/
package reika.rotarycraft.auxiliary;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public final class PipeDebugLog {

    /** Master toggle. Spark profiler diagnosed the lag (VanillaRegistries.createLookup
     *  per fluid sync — fixed). Disabled by default now; flip to {@code true} for any future
     *  pipe / sync investigation. The instrumentation points are all preserved. */
    public static final boolean ENABLED = false;

    /** Per-event aggregate stats so we don't drown the log file in per-call entries. */
    public static final boolean LOG_EACH_CALL = false;

    private static final Logger LOG = LogManager.getLogger("RotaryCraft.PipeDbg");

    /** Counter-and-nanos per event tag. */
    private static final ConcurrentHashMap<String, EventStats> stats = new ConcurrentHashMap<>();

    private static final AtomicLong lastDumpNanos = new AtomicLong(0L);
    private static final long DUMP_INTERVAL_NANOS = 5_000_000_000L; // 5 seconds

    private PipeDebugLog() {}

    /**
     * Records a fire-and-forget event. No timing — useful for "this thing happened, how
     * often?" questions. Aggregated into {@link #stats} for the periodic dump.
     */
    public static void event(String tag) {
        if (!ENABLED) return;
        stats.computeIfAbsent(tag, k -> new EventStats()).count.incrementAndGet();
        if (LOG_EACH_CALL) LOG.info("[event] {}", tag);
    }

    /**
     * Times a {@code Runnable} and accumulates the result. Returns the runnable's value
     * if non-void; void variant is below.
     */
    public static void time(String tag, Runnable r) {
        if (!ENABLED) {
            r.run();
            return;
        }
        long t0 = System.nanoTime();
        try {
            r.run();
        } finally {
            long dt = System.nanoTime() - t0;
            EventStats s = stats.computeIfAbsent(tag, k -> new EventStats());
            s.count.incrementAndGet();
            s.totalNanos.addAndGet(dt);
            long max;
            do {
                max = s.maxNanos.get();
                if (dt <= max) break;
            } while (!s.maxNanos.compareAndSet(max, dt));
            if (LOG_EACH_CALL) LOG.info("[time] {} = {}μs", tag, dt / 1000);
            // Always log unusually long single calls — anything >50ms is potentially the lag.
            if (dt > 50_000_000L) {
                LOG.warn("[time] {} took {}ms (potential lag source)", tag, dt / 1_000_000);
            }
        }
    }

    /**
     * Call from any frequently-firing tick handler. Dumps aggregate stats every 5s and
     * resets the counters. Safe to call thousands of times per second — the actual dump
     * is gated by the elapsed-time check.
     */
    public static void maybeDumpStats() {
        if (!ENABLED) return;
        long now = System.nanoTime();
        long last = lastDumpNanos.get();
        if (now - last < DUMP_INTERVAL_NANOS) return;
        if (!lastDumpNanos.compareAndSet(last, now)) return; // someone else is dumping
        if (stats.isEmpty()) return;

        StringBuilder sb = new StringBuilder("Pipe/sync stats over last ~5s:\n");
        stats.forEach((tag, s) -> {
            long c = s.count.getAndSet(0);
            long tot = s.totalNanos.getAndSet(0);
            long max = s.maxNanos.getAndSet(0);
            if (c == 0) return;
            if (tot == 0) {
                sb.append(String.format("  %-40s  %6d events%n", tag, c));
            } else {
                sb.append(String.format(
                        "  %-40s  %6d calls   total %7.2fms   avg %6.1fμs   max %7.2fms%n",
                        tag, c, tot / 1_000_000.0, (tot / (double) c) / 1000.0, max / 1_000_000.0));
            }
        });
        LOG.info(sb.toString());
    }

    private static final class EventStats {
        final AtomicLong count = new AtomicLong();
        final AtomicLong totalNanos = new AtomicLong();
        final AtomicLong maxNanos = new AtomicLong();
    }
}
