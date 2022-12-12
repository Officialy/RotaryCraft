package reika.rotarycraft.api.event;

import net.minecraft.world.level.block.entity.BlockEntity;
import reika.dragonapi.instantiable.event.BlockEntityEvent;

public class ShaftFailureEvent extends BlockEntityEvent {

    public final boolean isSpeedFailure;
    public final int materialType;

    public ShaftFailureEvent(BlockEntity te, boolean wasSpeed, int type) {
        super(te);

        isSpeedFailure = wasSpeed;
        materialType = type;
    }
}
