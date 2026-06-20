package reika.rotarycraft.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import reika.rotarycraft.base.blockentity.BlockEntityEngine;
import reika.rotarycraft.blockentities.engine.BlockEntityJetEngine;
import reika.rotarycraft.registry.SoundRegistry;

import java.util.HashMap;

/**
 * Tracks one {@link EngineSoundInstance} per running engine. Called every client
 * tick from the engine blocks' client ticker; instances stop themselves when the
 * engine shuts off or unloads, and get recreated here if the sound system dropped
 * them (F3+T, sound reload, volume toggled to zero and back).
 */
public class EngineSoundManager {

    private static final HashMap<BlockPos, EngineSoundInstance> active = new HashMap<>();
    private static final HashMap<BlockPos, JetStartSoundInstance> jetStarts = new HashMap<>();

    public static void tick(BlockEntityEngine engine) {
        Level level = engine.getLevel();
        if (level == null)
            return;
        if (level.getGameTime() % 600 == 0) {
            if (!active.isEmpty())
                active.values().removeIf(EngineSoundInstance::isStopped);
            // A jet broken mid-spool stops its instance but stops ticking, so prune stale entries.
            if (!jetStarts.isEmpty())
                jetStarts.values().removeIf(JetStartSoundInstance::isStopped);
        }
        // Jet spool-up sound runs while the drone is still gated off (shouldPlayEngineSound() is
        // false during spool), so handle it before the drone gate below.
        if (engine instanceof BlockEntityJetEngine jet)
            tickJetStart(jet);
        if (!engine.shouldPlayEngineSound())
            return;
        SoundRegistry sound = engine.getEngineSound();
        if (sound == null)
            return;
        BlockPos key = engine.getBlockPos();
        EngineSoundInstance cur = active.get(key);
        SoundManager sm = Minecraft.getInstance().getSoundManager();
        if (cur != null && !cur.isStopped() && sm.isActive(cur))
            return;
        EngineSoundInstance inst = new EngineSoundInstance(engine, sound);
        active.put(key.immutable(), inst);
        sm.play(inst);
    }

    private static void tickJetStart(BlockEntityJetEngine jet) {
        BlockPos key = jet.getBlockPos();
        JetStartSoundInstance cur = jetStarts.get(key);
        if (!jet.isSpoolingUp()) {
            // Not spooling (off or spool finished). The instance stops itself via its own tick();
            // just drop the stale reference once it has gone away.
            if (cur != null && cur.isStopped())
                jetStarts.remove(key);
            return;
        }
        SoundManager sm = Minecraft.getInstance().getSoundManager();
        if (cur != null && !cur.isStopped() && sm.isActive(cur))
            return;
        // Only (re)start the spool sound near the beginning of the spool-up, so a player arriving
        // late doesn't trigger a full-length replay that would overlap the drone.
        if (jet.getSpoolTick() > 40)
            return;
        JetStartSoundInstance inst = new JetStartSoundInstance(jet, SoundRegistry.JETSTART);
        jetStarts.put(key.immutable(), inst);
        sm.play(inst);
    }
}
