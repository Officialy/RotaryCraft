package reika.rotarycraft.gui.container.machine.inventory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import reika.dragonapi.base.OneSlotContainer;
import reika.rotarycraft.blockentities.BlockEntityWinder;
import reika.rotarycraft.registry.RotaryMenus;

public class WinderContainer extends OneSlotContainer<BlockEntityWinder> {

    //Client
    public WinderContainer(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityWinder) inv.player.level.getBlockEntity(data.readBlockPos()));
    }

    public WinderContainer(int id, Inventory inv, BlockEntityWinder te) {
        super(RotaryMenus.WINDER.get(), id, inv, te, 0);
    }

}
