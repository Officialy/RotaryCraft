package reika.rotarycraft.api.event;

import net.minecraft.world.level.block.entity.BlockEntity;
import reika.dragonapi.instantiable.event.BlockEntityEvent;

public class FlywheelFailureEvent extends BlockEntityEvent {

    public final float explosivePower;

    public FlywheelFailureEvent(BlockEntity te, float power) {
        super(te);

        explosivePower = power;
    }

}
