package reika.rotarycraft.api.event;

import net.minecraft.world.level.block.entity.BlockEntity;
import reika.dragonapi.instantiable.event.BlockEntityEvent;

public class VacuumXPAbsorbEvent extends BlockEntityEvent {

    public final int amount;

    public VacuumXPAbsorbEvent(BlockEntity te, int xp) {
        super(te);
        amount = xp;
    }
}
