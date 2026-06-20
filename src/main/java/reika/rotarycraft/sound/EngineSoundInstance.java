package reika.rotarycraft.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;

import reika.rotarycraft.base.blockentity.BlockEntityEngine;
import reika.rotarycraft.registry.SoundRegistry;

/**
 * Client-side looping drone for a running engine. Replaces the legacy server-side
 * periodic one-shot re-fire, which had an audible seam every loop and left the
 * engine silent for up to a full loop length when the player came into range.
 */
public class EngineSoundInstance extends AbstractTickableSoundInstance {

    private final BlockEntityEngine engine;
    private final SoundRegistry soundEntry;

    public EngineSoundInstance(BlockEntityEngine engine, SoundRegistry sound) {
        super(sound.getSoundEvent(), sound.getCategory(), SoundInstance.createUnseededRandom());
        this.engine = engine;
        soundEntry = sound;
        BlockPos pos = engine.getBlockPos();
        x = pos.getX() + 0.5;
        y = pos.getY() + 0.5;
        z = pos.getZ() + 0.5;
        looping = true;
        delay = 0;
        this.updateVolume();
    }

    @Override
    public boolean canStartSilent() {
        // engines enter render distance well outside audible range; the loop must start
        // silently so it fades in as the player approaches instead of never starting
        return true;
    }

    private void updateVolume() {
        float vol = engine.getEngineSoundVolume() * soundEntry.getModulatedVolume();
        if (engine.hasLevel() && engine.isMuffled(engine.getLevel(), engine.getBlockPos()))
            vol *= 0.3125F;
        volume = vol;
        pitch = engine.getEngineSoundPitch();
    }

    @Override
    public void tick() {
        if (engine.isRemoved() || !engine.hasLevel() || !engine.shouldPlayEngineSound()) {
            this.stop();
            return;
        }
        this.updateVolume();
    }
}
