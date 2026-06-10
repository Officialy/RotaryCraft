package reika.rotarycraft.api.event;

import net.minecraft.world.level.block.entity.BlockEntity;
import reika.dragonapi.instantiable.event.BlockEntityEvent;

/**
 * Fired on the NeoForge event bus when a {@code BlockEntityJetEngine} transitions into the
 * "is jet failing" state — either through FOD accumulation, an ingested screwdriver/wrench,
 * or fluid ingest of a fuel-class fluid. This fires once at the transition; the subsequent
 * detonation chain that follows fires {@link JetEngineExplosionEvent} when temperatures climb
 * past the explosion threshold.
 */
public class JetEngineEnterFailureEvent extends BlockEntityEvent {

    public JetEngineEnterFailureEvent(BlockEntity te) {
        super(te);
    }
}
