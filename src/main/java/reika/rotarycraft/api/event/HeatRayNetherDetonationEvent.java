package reika.rotarycraft.api.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import reika.dragonapi.instantiable.event.base.WorldPositionEvent;

public class HeatRayNetherDetonationEvent extends WorldPositionEvent {

    public HeatRayNetherDetonationEvent(Level world, BlockPos pos) {
        super(world, pos);
    }

}
