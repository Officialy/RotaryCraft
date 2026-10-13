package reika.rotarycraft;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import reika.dragonapi.auxiliary.trackers.PlayerChunkTracker;
import reika.dragonapi.auxiliary.trackers.TickRegistry;

/** Real player ownership, native ticket membership and view updates, including asynchronous requests. */
final class DragonAPIChunkTrackerTests {
    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> env) {
        RotaryGameTests.register(event,env,"dragonapi_chunk_tracker_timeout_tickets",40,DragonAPIChunkTrackerTests::tickets);
        RotaryGameTests.register(event,env,"dragonapi_chunk_tracker_callbacks",40,DragonAPIChunkTrackerTests::callbacks);
        RotaryGameTests.register(event,env,"dragonapi_chunk_tracker_lifecycle",40,DragonAPIChunkTrackerTests::lifecycle);
        RotaryGameTests.register(event,env,"dragonapi_chunk_tracker_workers",40,DragonAPIChunkTrackerTests::workers);
    }
    private static class Condition implements PlayerChunkTracker.TrackingCondition {
        boolean keep = true; int calls, removals;
        public boolean shouldBeTracked(Player player) {
            if (!((ServerPlayer)player).level().getServer().isSameThread()) throw new AssertionError("Callback off server thread");
            calls++; return keep;
        }
        public void onUntrack(Player player) { removals++; }
    }
    private static void ticks(GameTestHelper h,int count) {
        for(int i=0;i<count;i++) PlayerChunkTracker.instance.tick(TickRegistry.TickType.SERVER,TickRegistry.Phase.END,h.getLevel().getServer());
    }
    private static boolean hasTicket(ServerPlayer player) {
        try {
            var manager=player.level().getChunkSource().chunkMap.getDistanceManager();
            var field=net.minecraft.server.level.DistanceManager.class.getDeclaredField("playersPerChunk"); field.setAccessible(true);
            var entries=(it.unimi.dsi.fastutil.longs.Long2ObjectMap<?>)field.get(manager);
            return entries.values().stream().anyMatch(value->value instanceof Set<?> set && set.contains(player));
        } catch(ReflectiveOperationException error) { throw new IllegalStateException(error); }
    }
    private static void remove(ServerPlayer player) {
        PlayerChunkTracker.stopTrackingPlayer(player);
        player.level().getServer().getPlayerList().remove(player);
    }
    private static void tickets(GameTestHelper h) {
        var player=DragonAPIAuditTests.player(h); var other=DragonAPIAuditTests.player(h); var condition=new Condition();
        try {
            h.assertTrue(hasTicket(player) && hasTicket(other),"native players must initially own loading tickets");
            var view=player.getChunkTrackingView(); double oldX=player.xo,oldZ=player.zo;
            PlayerChunkTracker.startTrackingPlayer(player,condition);
            h.assertTrue(!hasTicket(player) && hasTicket(other),"suspension must remove only that player's loading participation");
            player.setPos(player.getX()+64,player.getY(),player.getZ()+64);
            PlayerChunkTracker.tryUpdatePlayerPertinentChunks(player.level().getChunkSource().chunkMap,player);
            h.assertTrue(!hasTicket(player) && player.getChunkTrackingView()==view,"moving a suspended player must neither add tickets nor change its view");
            condition.keep=false; ticks(h,39);
            h.assertTrue(PlayerChunkTracker.shouldStopChunkloadingFor(player) && condition.removals==0,"false conditions retain the original 40-tick grace");
            condition.keep=true; ticks(h,1); condition.keep=false; ticks(h,39);
            h.assertTrue(PlayerChunkTracker.shouldStopChunkloadingFor(player),"a true condition resets the full grace period");
            ticks(h,1);
            h.assertTrue(!PlayerChunkTracker.shouldStopChunkloadingFor(player) && condition.removals==1 && hasTicket(player),"expiry must call once and restore native loading tickets");
            h.assertTrue(player.getChunkTrackingView() instanceof net.minecraft.server.level.ChunkTrackingView.Positioned next
                    && next.center().equals(player.chunkPosition()),"expiry must immediately restore the current view without another movement");
            h.assertTrue(player.xo==oldX && player.zo==oldZ,"expiry must not corrupt interpolation fields");
            ticks(h,2); h.assertTrue(condition.removals==1,"retired callbacks must not repeat");
            var dead=new Condition(); PlayerChunkTracker.startTrackingPlayer(player,dead); player.setHealth(0); ticks(h,40);
            h.assertTrue(dead.calls==0 && dead.removals==1 && !PlayerChunkTracker.shouldStopChunkloadingFor(player),"death retains the grace without invoking a live condition");
            player.setHealth(20);
        } finally { remove(player); remove(other); }
        h.succeed();
    }
    private static void callbacks(GameTestHelper h) {
        var player=DragonAPIAuditTests.player(h); var renewed=new Condition(); var replaced=new AtomicInteger();
        try {
            PlayerChunkTracker.startTrackingPlayer(player,new Condition(){
                public boolean shouldBeTracked(Player p) { replaced.incrementAndGet(); PlayerChunkTracker.startTrackingPlayer(p,renewed); return false; }
            });
            ticks(h,1); ticks(h,40);
            h.assertTrue(replaced.get()==1 && renewed.calls==40 && PlayerChunkTracker.shouldStopChunkloadingFor(player),"reentrant replacement must survive the stale iteration result");
            PlayerChunkTracker.stopTrackingPlayer(player); h.assertTrue(renewed.removals==1,"explicit stop must notify once");
            var retry=new Condition(); var callbackRemovals=new AtomicInteger();
            PlayerChunkTracker.startTrackingPlayer(player,new Condition(){
                public boolean shouldBeTracked(Player p) { return false; }
                public void onUntrack(Player p) { callbackRemovals.incrementAndGet(); PlayerChunkTracker.startTrackingPlayer(p,retry); }
            });
            ticks(h,40);
            h.assertTrue(callbackRemovals.get()==1 && PlayerChunkTracker.shouldStopChunkloadingFor(player) && !hasTicket(player),"an expiry callback may register a fresh suspension");
            PlayerChunkTracker.stopTrackingPlayer(player);
            var failed=new AtomicInteger();
            PlayerChunkTracker.startTrackingPlayer(player,new Condition(){
                public boolean shouldBeTracked(Player p) { throw new IllegalStateException("intentional D49 condition failure"); }
                public void onUntrack(Player p) { failed.incrementAndGet(); throw new IllegalStateException("intentional D49 untrack failure"); }
            });
            ticks(h,1);
            h.assertTrue(!PlayerChunkTracker.shouldStopChunkloadingFor(player) && hasTicket(player) && failed.get()==1,"failed callbacks must release suspension and restore native state");
        } finally { remove(player); }
        h.succeed();
    }
    private static void lifecycle(GameTestHelper h) {
        var player=DragonAPIAuditTests.player(h); var old=player; var condition=new Condition();
        try {
            PlayerChunkTracker.startTrackingPlayer(player,condition);
            var stale=new Condition(); worker(()->PlayerChunkTracker.startTrackingPlayer(old,stale));
            var nether=h.getLevel().getServer().getLevel(Level.NETHER);
            player=player.teleport(new TeleportTransition(nether,new Vec3(0,100,0),Vec3.ZERO,0,0,TeleportTransition.DO_NOTHING));
            h.assertTrue(player!=null && !PlayerChunkTracker.shouldStopChunkloadingFor(player) && condition.removals==1,"real dimension change must retire the old world binding");
            var beforeRespawn=new Condition(); PlayerChunkTracker.startTrackingPlayer(player,beforeRespawn);
            var previous=player;
            player=h.getLevel().getServer().getPlayerList().respawn(player,false,Entity.RemovalReason.KILLED);
            h.assertTrue(player!=previous && !PlayerChunkTracker.shouldStopChunkloadingFor(player) && beforeRespawn.removals==1,"respawn must not transfer suspension by UUID to the new player entity");
            var current=player; var logout=new Condition(){ public void onUntrack(Player p){ super.onUntrack(p); PlayerChunkTracker.startTrackingPlayer(p,this); } };
            PlayerChunkTracker.startTrackingPlayer(current,logout);
            NeoForge.EVENT_BUS.post(new PlayerEvent.PlayerLoggedOutEvent(previous));
            h.assertTrue(PlayerChunkTracker.shouldStopChunkloadingFor(current),"a stale logout must not clear the new owner with the same UUID");
            h.getLevel().getServer().getPlayerList().remove(current);
            h.assertTrue(!PlayerChunkTracker.shouldStopChunkloadingFor(current) && logout.removals==1,"real logout must clear immediately and prevent callback resurrection");
            h.runAfterDelay(2,()->{
                h.assertTrue(!PlayerChunkTracker.shouldStopChunkloadingFor(old) && stale.calls==0 && stale.removals==0,"a queued request from an old dimension/entity must not resurrect a session");
                h.succeed();
            });
        } finally { PlayerChunkTracker.stopTrackingPlayer(player); }
    }
    private static void worker(Runnable action) {
        var failed=new AtomicReference<Throwable>(); var thread=new Thread(()->{try{action.run();}catch(Throwable e){failed.set(e);}},"dragonapi-chunk-tracker-worker");
        thread.start();
        try { thread.join(2000); } catch(InterruptedException error) { Thread.currentThread().interrupt(); throw new IllegalStateException(error); }
        if(thread.isAlive()) throw new AssertionError("worker blocked on the server callback");
        if(failed.get()!=null) throw new AssertionError("worker failed",failed.get());
    }
    private static void workers(GameTestHelper h) {
        var player=DragonAPIAuditTests.player(h); var condition=new Condition(); var applied=new AtomicBoolean();
        worker(()->{
            for(int i=0;i<2000;i++) PlayerChunkTracker.shouldStopChunkloadingFor(player);
            PlayerChunkTracker.startTrackingPlayer(player,condition);
        });
        h.assertTrue(!PlayerChunkTracker.shouldStopChunkloadingFor(player),"worker requests must wait for the owning server");
        h.runAfterDelay(3,()->{
            h.assertTrue(PlayerChunkTracker.shouldStopChunkloadingFor(player) && condition.calls>0 && !hasTicket(player),"queued start must execute callbacks and change tickets on the server");
            worker(()->PlayerChunkTracker.stopTrackingPlayer(player)); applied.set(true);
        });
        h.runAfterDelay(6,()->{
            try { h.assertTrue(applied.get() && !PlayerChunkTracker.shouldStopChunkloadingFor(player) && condition.removals==1 && hasTicket(player),"queued stop must notify exactly once and restore native tickets"); }
            finally { remove(player); }
            h.succeed();
        });
    }
}
