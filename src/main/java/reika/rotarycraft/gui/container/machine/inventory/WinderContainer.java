package reika.rotarycraft.gui.container.machine.inventory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import reika.dragonapi.base.CoreContainer;
import reika.rotarycraft.blockentities.BlockEntityWinder;
import reika.rotarycraft.registry.RotaryMenus;
public final class WinderContainer extends CoreContainer<BlockEntityWinder> {
    public WinderContainer(int id, Inventory inventory, FriendlyByteBuf data) { this(id, inventory, (BlockEntityWinder)inventory.player.level().getBlockEntity(data.readBlockPos())); }
    public WinderContainer(int id, Inventory inventory, BlockEntityWinder tile) {
        super(RotaryMenus.WINDER.get(), id, inventory, tile);
        addSlot(new Slot(tile, 0, 80, 35)); addPlayerInventoryWithOffset(inventory, 0, 0);
    }
    @Override public boolean stillValid(Player player) { return tile.stillValid(player); }
    @Override public ItemStack quickMoveStack(Player player, int index) {
        if (index < 0 || index >= slots.size()) return ItemStack.EMPTY;
        Slot slot = slots.get(index); if (!slot.hasItem()) return ItemStack.EMPTY;
        ItemStack stack = slot.getItem(), original = stack.copy();
        if (index == 0) { if (!moveItemStackTo(stack, 1, slots.size(), true)) return ItemStack.EMPTY; }
        else if (!tile.canPlaceItem(0, stack) || !moveItemStackTo(stack, 0, 1, false)) return ItemStack.EMPTY;
        if (stack.isEmpty()) slot.set(ItemStack.EMPTY); else slot.setChanged();
        slot.onTake(player, stack); return original;
    }
}
