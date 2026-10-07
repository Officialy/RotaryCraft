package reika.rotarycraft.sound;

import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import reika.rotarycraft.auxiliary.RotaryAux;
import reika.rotarycraft.blockentities.production.BlockEntityPump;
import reika.rotarycraft.registry.SoundRegistry;

/** Continuous pump audio, so walking into range never waits for a server re-fire. */
public final class PumpSoundManager {
    private static final HashMap<BlockPos, PumpSound> active = new HashMap<>();
    private static ClientLevel currentLevel;

    private PumpSoundManager() {}

    public static void tick(BlockEntityPump pump) {
        var minecraft = Minecraft.getInstance();
        var manager = minecraft.getSoundManager();
        if (currentLevel != minecraft.level) {
            active.values().forEach(manager::stop);
            active.clear();
            currentLevel = minecraft.level;
        }
        if (currentLevel == null || pump.getLevel() != currentLevel)
            return;
        if (currentLevel.getGameTime() % 600 == 0)
            active.values().removeIf(PumpSound::isStopped);
        BlockPos pos = pump.getBlockPos();
        PumpSound sound = active.get(pos);
        if (!pump.shouldPlayPumpSound()) {
            if (sound != null) {
                manager.stop(sound);
                active.remove(pos);
            }
            return;
        }
        if (sound != null && sound.pump == pump && !sound.isStopped() && manager.isActive(sound))
            return;
        if (sound != null)
            manager.stop(sound);
        sound = new PumpSound(pump);
        active.put(pos.immutable(), sound);
        manager.play(sound);
    }

    private static final class PumpSound extends AbstractTickableSoundInstance {
        private final BlockEntityPump pump;

        private PumpSound(BlockEntityPump pump) {
            super(SoundRegistry.PUMP.getSoundEvent(), SoundRegistry.PUMP.getCategory(),
                    SoundInstance.createUnseededRandom());
            this.pump = pump;
            x = pump.getBlockPos().getX() + 0.5;
            y = pump.getBlockPos().getY() + 0.5;
            z = pump.getBlockPos().getZ() + 0.5;
            looping = true;
            delay = 0;
            pitch = 1;
            updateVolume();
        }

        @Override public boolean canStartSilent() { return true; }

        private void updateVolume() {
            volume = 0.5F * SoundRegistry.PUMP.getModulatedVolume();
            if (RotaryAux.isMuffled(pump))
                volume *= 0.25F;
        }

        @Override public void tick() {
            if (pump.getLevel() != Minecraft.getInstance().level || !pump.shouldPlayPumpSound())
                stop();
            else
                updateVolume();
        }
    }
}
