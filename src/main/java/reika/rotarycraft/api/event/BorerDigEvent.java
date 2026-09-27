package reika.rotarycraft.api.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.dragonapi.instantiable.event.BlockEntityEvent;

/** Emitted after the Boring Machine attempts one tunnel slice. */
public class BorerDigEvent extends BlockEntityEvent {

    public final int range;
    public final int centerX;
    public final int centerY;
    public final int centerZ;
    public final boolean isSilkTouch;

    public BorerDigEvent(BlockEntity machine, int range, BlockPos center, boolean silkTouch) {
        super(machine);
        this.range = range;
        centerX = center.getX();
        centerY = center.getY();
        centerZ = center.getZ();
        isSilkTouch = silkTouch;
    }
}
