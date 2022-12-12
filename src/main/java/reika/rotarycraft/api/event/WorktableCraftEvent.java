package reika.rotarycraft.api.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.dragonapi.instantiable.event.BlockEntityEvent;

public class WorktableCraftEvent extends BlockEntityEvent {

    public final boolean automated;
    public final Player player;
    private final ItemStack item;

    public WorktableCraftEvent(BlockEntity te, Player ep, boolean wasAuto, ItemStack is) {
        super(te);
        automated = wasAuto;
        player = ep;
        item = is;
    }

    public final ItemStack getItem() {
        return item != null ? item.copy() : null;
    }

}
