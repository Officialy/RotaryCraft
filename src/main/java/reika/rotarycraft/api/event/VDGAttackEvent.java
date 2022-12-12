package reika.rotarycraft.api.event;

import net.minecraft.world.level.block.entity.BlockEntity;
import reika.dragonapi.instantiable.event.BlockEntityEvent;

public class VDGAttackEvent extends BlockEntityEvent {

    public final int attackDamage;
    public final int tileCharge;

    public VDGAttackEvent(BlockEntity te, int chg, int dmg) {
        super(te);

        attackDamage = dmg;
        tileCharge = chg;
    }
}
