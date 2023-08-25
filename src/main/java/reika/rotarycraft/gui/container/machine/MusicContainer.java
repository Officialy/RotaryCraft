package reika.rotarycraft.gui.container.machine;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import reika.rotarycraft.base.IOMachineContainer;
import reika.rotarycraft.blockentities.decorative.BlockEntityMusicBox;
import reika.rotarycraft.registry.RotaryMenus;

public class MusicContainer extends IOMachineContainer<BlockEntityMusicBox> {

    public final BlockEntityMusicBox bevel;

    //Client
    public MusicContainer(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityMusicBox) inv.player.level().getBlockEntity(data.readBlockPos()));
    }

    public MusicContainer(int id, Inventory inv, BlockEntityMusicBox te) {
        super(RotaryMenus.MUSIC.get(), id, inv, te);
        bevel = te;
    }
}
