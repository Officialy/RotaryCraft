package reika.rotarycraft;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import reika.dragonapi.DragonAPI;
import reika.dragonapi.DragonOptions;
import reika.dragonapi.auxiliary.trackers.ModFileVersionChecker;
import reika.dragonapi.auxiliary.trackers.ModFileVersionChecker.HashDecision;
import reika.dragonapi.base.DragonAPIMod;

/** Opt-in loopback server fixture; actual client JVMs send actual packaged jar checksums. */
@EventBusSubscriber(modid = RotaryCraft.MODID)
public final class DragonAPIPackagedHashTests {
    private static final Map<String,ModFileVersionChecker.HashCheckEvent> checks = new HashMap<>();
    private static int stage;
    private static GameTestHelper helper;
    private static Path state;
    private static long started;
    private static boolean cancelReady;
    private DragonAPIPackagedHashTests() {}
    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> env) {
        if (Boolean.getBoolean("testinstance.hashAuditServer"))
            RotaryGameTests.register(event,env,"dragonapi_packaged_hash_wait",60000,DragonAPIPackagedHashTests::run);
    }
    @SubscribeEvent public static void observe(ModFileVersionChecker.HashCheckEvent event) {
        if (!Boolean.getBoolean("testinstance.hashAuditServer") || !event.mod.equals("dragonapi")) return;
        checks.put(event.getEntity().getName().getString(),event);
        DragonAPI.LOGGER.info("PACKAGED_HASH_AUDIT_CHECK: {} {} client={} server={}",event.getEntity().getName().getString(),event.decision,event.clientHash,event.serverHash);
    }
    @SubscribeEvent public static void cancellation(ModFileVersionChecker.HashKickEvent event) {
        if (Boolean.getBoolean("testinstance.hashAuditServer") && stage == 3 && event.mod.equals("dragonapi")) event.setCanceled(true);
    }
    private static void write(String value) {
        try { Files.writeString(state,value); } catch(java.io.IOException error) { throw new IllegalStateException(error); }
    }
    @SuppressWarnings("unchecked") private static void option(boolean enabled) {
        try {
            var field=DragonAPI.config.getClass().getDeclaredField("modernValues"); field.setAccessible(true);
            var values=(Map<Integer,net.neoforged.neoforge.common.ModConfigSpec.ConfigValue<?>>)field.get(DragonAPI.config);
            var value=(net.neoforged.neoforge.common.ModConfigSpec.ConfigValue<Boolean>)values.get(DragonOptions.FILEHASH.ordinal());
            value.set(enabled);
            value.save(); // NeoForge serializes the saved SYNCED config for subsequent connections.
            if(DragonOptions.FILEHASH.getState()!=enabled) throw new AssertionError("Actual FILEHASH option did not change");
        } catch(ReflectiveOperationException error) { throw new IllegalStateException(error); }
    }
    private static void run(GameTestHelper h) {
        helper=h; started=System.nanoTime(); state=Path.of(System.getProperty("testinstance.hashAuditRoot"),"state.txt");
        String hash=DragonAPIMod.getByName("DRAGONAPI").getFileHash();
        h.assertTrue(hash!=null && hash.matches("[0-9a-fA-F]{64}"),"server must compute an actual packaged SHA-256, not Source");
        option(true);
        var server=h.getLevel().getServer(); server.setUsesAuthentication(false);
        try {
            server.getConnection().startTcpServerListener(java.net.InetAddress.getByName("127.0.0.1"),0);
            var field=server.getConnection().getClass().getDeclaredField("channels"); field.setAccessible(true);
            var channels=(java.util.List<?>)field.get(server.getConnection());
            int port=((java.net.InetSocketAddress)((io.netty.channel.ChannelFuture)channels.getFirst()).channel().localAddress()).getPort();
            Files.writeString(state.getParent().resolve("port.txt"),Integer.toString(port));
        } catch(Exception error) { throw new IllegalStateException(error); }
        write("MATCH_READY"); DragonAPI.LOGGER.info("PACKAGED_HASH_AUDIT_SERVER_READY hash={}",hash);
        h.succeedWhen(DragonAPIPackagedHashTests::poll);
    }
    private static void poll() {
        if(System.nanoTime()-started>600000000000L) throw new IllegalStateException("Packaged remote clients timed out");
        var server=helper.getLevel().getServer(); var match=checks.get("HashMatch"); var different=checks.get("HashDifferent");
        if(stage==0 && match!=null) {
            helper.assertTrue(match.decision==HashDecision.MATCH && match.clientHash.equals(match.serverHash),"matching packaged jars must pass actual enforcement");
            stage=1; write("MATCH_PASS");
        } else if(stage==1 && different!=null) {
            helper.assertTrue(different.decision==HashDecision.KICKED && !different.clientHash.equals(different.serverHash),"enabled mismatch must use differing real jar hashes and kick");
            checks.remove("HashDifferent"); option(false); stage=2; write("MISMATCH_KICKED");
        } else if(stage==2 && different!=null) {
            helper.assertTrue(different.decision==HashDecision.DISABLED && !different.clientHash.equals(different.serverHash),"disabled option must ignore a real mismatching frame");
            if(server.getPlayerList().getPlayer(different.getEntity().getUUID())==different.getEntity()) {
                stage=3; checks.remove("HashDifferent"); write("DISABLED_PASS");
            }
        } else if(stage==3 && different==null) {
            if(!cancelReady && server.getPlayerList().getPlayers().stream().noneMatch(p->p.getName().getString().equals("HashDifferent"))) {
                option(true); write("CANCEL_READY");
                cancelReady=true;
            }
        } else if(stage==3 && different!=null) {
            helper.assertTrue(different.decision==HashDecision.CANCELLED && !different.clientHash.equals(different.serverHash),"original HashKickEvent must cancel actual mismatch enforcement");
            if(server.getPlayerList().getPlayer(different.getEntity().getUUID())==different.getEntity()) {
                write("ALL_PASS"); DragonAPI.LOGGER.info("PACKAGED_HASH_AUDIT_SERVER_PASS: match, enabled kick, disabled accept and original cancellation");
                stage=4;
            }
        } else if(stage==4 && Files.isRegularFile(state.getParent().resolve("client-done.txt"))) {
            return;
        }
        try { Thread.sleep(25); } catch(InterruptedException error) { Thread.currentThread().interrupt(); throw new IllegalStateException(error); }
        helper.assertTrue(false,"Waiting for packaged client stages");
    }
}
