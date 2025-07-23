package reika.rotarycraft.gui.container.machine;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.dragonapi.base.CoreContainer;
import net.minecraft.world.item.ItemStack;
import reika.dragonapi.base.BlockEntityBase;

public class BlankContainer<T extends BlockEntityBase> extends CoreContainer<T> {
    public final T tile;

    // Client-side constructor
    public BlankContainer(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf data) {
        this(type, id, inv, (T) inv.player.level().getBlockEntity(data.readBlockPos()));
    }

    // Server-side constructor
    public BlankContainer(MenuType<?> type, int id, Inventory inv, T tile) {
        super(type, id, inv, tile);
        this.tile = tile;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.distanceToSqr(tile.getBlockPos().getCenter()) <= 16;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }


}
