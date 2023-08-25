package reika.rotarycraft.api.event;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.dragonapi.instantiable.event.BlockEntityEvent;

public class VacuumItemAbsorbEvent extends BlockEntityEvent {

    private final ItemStack item;

    public VacuumItemAbsorbEvent(BlockEntity te, ItemStack item) {
        super(te);
        this.item = item;
    }

    public final ItemStack getItem() {
        return item != null ? item.copy() : null;
    }

}
