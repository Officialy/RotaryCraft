package reika.rotarycraft.gui.container.machine;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import reika.rotarycraft.base.IOMachineContainer;
import reika.rotarycraft.blockentities.transmission.BlockEntityBevelGear;
import reika.rotarycraft.registry.RotaryMenus;

public class BevelContainer extends IOMachineContainer<BlockEntityBevelGear> {

    public final BlockEntityBevelGear bevel;

    //Client
    public BevelContainer(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityBevelGear) inv.player.level.getBlockEntity(data.readBlockPos()));
    }

    public BevelContainer(int id, Inventory inv, BlockEntityBevelGear te) {
        super(RotaryMenus.BEVEL.get(), id, inv, te);
        bevel = te;
    }

}
