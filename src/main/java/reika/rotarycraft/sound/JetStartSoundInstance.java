package reika.rotarycraft.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;

import reika.rotarycraft.blockentities.engine.BlockEntityJetEngine;
import reika.rotarycraft.registry.SoundRegistry;

/**
 * Client-side spool-up sound for a jet engine. Non-looping: it plays once while the engine
 * spools up and removes itself when the spool completes. Crucially it also stops the instant the
 * engine block is removed or unloaded, which the legacy fire-and-forget server packet could not do
 * (that sound kept playing for the remainder of its ~24s length after the engine was broken).
 */
public class JetStartSoundInstance extends AbstractTickableSoundInstance {

    private final BlockEntityJetEngine engine;

    public JetStartSoundInstance(BlockEntityJetEngine engine, SoundRegistry sound) {
        super(sound.getSoundEvent(), sound.getCategory(), SoundInstance.createUnseededRandom());
        this.engine = engine;
        BlockPos pos = engine.getBlockPos();
        x = pos.getX() + 0.5;
        y = pos.getY() + 0.5;
        z = pos.getZ() + 0.5;
        looping = false;
        delay = 0;
        volume = sound.getModulatedVolume();
        pitch = 1F;
    }

    @Override
    public boolean canStartSilent() {
        // engines enter render distance outside audible range; start silent so it is consistent
        // with the drone and does not get culled before the player is close
        return true;
    }

    @Override
    public void tick() {
        if (engine.isRemoved() || !engine.hasLevel() || !engine.isSpoolingUp())
            this.stop();
    }
}
