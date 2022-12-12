package reika.rotarycraft.gui.container.machine;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.IOMachineMenu;
import reika.rotarycraft.blockentities.engine.BlockEntitySteamEngine;
import reika.rotarycraft.blockentities.transmission.BlockEntityBevelGear;
import reika.rotarycraft.registry.RotaryMenus;

public class BevelMenu extends IOMachineMenu<BlockEntityBevelGear> {

    public final BlockEntityBevelGear steam;

    //Client
    public BevelMenu(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityBevelGear) inv.player.level.getBlockEntity(data.readBlockPos()));
    }

    public BevelMenu(int id, Inventory inv, BlockEntityBevelGear te) {
        super(RotaryMenus.BEVEL.get(), id, inv, te);
        steam = te;
    }

}
