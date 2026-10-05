package reika.rotarycraft.auxiliary;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import reika.rotarycraft.registry.RotaryAdvancements;

/** Tracks the actual loss of a player's supporting light bridge until that fall ends. */
@EventBusSubscriber(modid = "rotarycraft")
public final class LightBridgeFallTracker {
    private static final String TAG = "RotaryCraft_LightBridgeFall";
    private LightBridgeFallTracker() {}

    public static void supportRemoved(ServerLevel level, BlockPos bridge) {
        var box = new AABB(bridge.getX(), bridge.getY() + .9, bridge.getZ(),
                bridge.getX() + 1, bridge.getY() + 1.2, bridge.getZ() + 1);
        for (ServerPlayer player : level.getEntitiesOfClass(ServerPlayer.class, box)) {
            if (player.isSpectator() || player.getAbilities().flying || player.isFallFlying()
                    || Math.abs(player.getY() - (bridge.getY() + 1)) > .2) continue;
            var fall = new CompoundTag();
            fall.putLong("started", level.getGameTime());
            fall.putDouble("height", player.getY());
            fall.putString("dimension", level.dimension().identifier().toString());
            player.getPersistentData().put(TAG, fall);
        }
    }

    @SubscribeEvent
    public static void tick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer)) return;
        var fall = player.getPersistentData().getCompound(TAG).orElse(null);
        if (fall == null) return;
        long age = player.level().getGameTime() - fall.getLongOr("started", 0);
        // The client's onGround flag can lag behind bridge retraction. Require actual supporting
        // collision before clearing, so a slow connection still receives the authored fall award.
        boolean supported = player.onGround() && player.level().getBlockCollisions(player,
                player.getBoundingBox().move(0, -.05, 0)).iterator().hasNext();
        if (!sameDimension(player, fall) || age < 0 || player.isSpectator()
                || player.getAbilities().flying || player.isFallFlying() || player.isInWater()
                || player.isPassenger() || player.getY() > fall.getDoubleOr("height", player.getY()) + 1
                || supported)
            player.getPersistentData().remove(TAG);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void died(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        var fall = player.getPersistentData().getCompound(TAG).orElse(null);
        if (fall != null && sameDimension(player, fall) && event.getSource().is(DamageTypes.FALL)
                && player.getY() < fall.getDoubleOr("height", player.getY()))
            RotaryAdvancements.LIGHTFALL.triggerAchievement(player);
        player.getPersistentData().remove(TAG);
    }

    private static boolean sameDimension(Player player, CompoundTag fall) {
        return player.level().dimension().identifier().toString().equals(fall.getStringOr("dimension", ""));
    }

    @SubscribeEvent
    public static void login(PlayerEvent.PlayerLoggedInEvent event) { event.getEntity().getPersistentData().remove(TAG); }
    @SubscribeEvent
    public static void logout(PlayerEvent.PlayerLoggedOutEvent event) { event.getEntity().getPersistentData().remove(TAG); }
    @SubscribeEvent
    public static void changedDimension(PlayerEvent.PlayerChangedDimensionEvent event) { event.getEntity().getPersistentData().remove(TAG); }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void teleported(net.neoforged.neoforge.event.entity.EntityTeleportEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) player.getPersistentData().remove(TAG);
    }
}
