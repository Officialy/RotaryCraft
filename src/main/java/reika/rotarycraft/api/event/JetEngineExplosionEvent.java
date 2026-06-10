package reika.rotarycraft.api.event;

import net.minecraft.world.level.block.entity.BlockEntity;
import reika.dragonapi.instantiable.event.BlockEntityEvent;

/**
 * Fired on the NeoForge event bus when a {@code BlockEntityJetEngine} undergoes catastrophic
 * failure ({@code temperature > 1000} after entering failure mode) and is about to detonate.
 * Subscribers can read state but cannot cancel the explosion — the engine is past recovery
 * by the time this fires.
 */
public class JetEngineExplosionEvent extends BlockEntityEvent {

    public JetEngineExplosionEvent(BlockEntity te) {
        super(te);
    }
}
