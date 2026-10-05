package reika.rotarycraft.gui.container.machine.inventory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import reika.rotarycraft.base.IOMachineContainer;
import reika.rotarycraft.blockentities.level.BlockEntityDefoliator;
import reika.rotarycraft.registry.RotaryMenus;
public final class ContainerDefoliator extends IOMachineContainer<BlockEntityDefoliator> {
    private int fluidLevel;
    public ContainerDefoliator(int id, Inventory inventory, FriendlyByteBuf data) { this(id, inventory, (BlockEntityDefoliator)inventory.player.level().getBlockEntity(data.readBlockPos())); }
    public ContainerDefoliator(int id, Inventory inventory, BlockEntityDefoliator tile) {
        super(RotaryMenus.DEFOLIATOR.get(), id, inventory, tile); fluidLevel = tile.getLiquidLevel();
        addSlot(tile.itemHandler.slot(0, 80, 17)); addSlot(tile.itemHandler.slot(1, 80, 53)); addPlayerInventory(inventory);
        addDataSlots(new ContainerData() {
            @Override public int getCount() { return 5; }
            @Override public int get(int index) { return index == 0 ? tile.getLiquidLevel() : (int)(tile.power >>> ((index - 1) * 16) & 0xFFFF); }
            @Override public void set(int index, int value) {
                if (index == 0) fluidLevel = Math.clamp(value, 0, BlockEntityDefoliator.CAPACITY);
                else { int shift = (index - 1) * 16; tile.power = (tile.power & ~(0xFFFFL << shift)) | ((value & 0xFFFFL) << shift); }
            }
        });
    }
    public int getFluidLevel() { return tile.getLevel() != null && !tile.getLevel().isClientSide() ? tile.getLiquidLevel() : fluidLevel; }
    @Override public boolean stillValid(Player player) { return !tile.isRemoved() && tile.isPlayerAccessible(player); }
    @Override public ItemStack quickMoveStack(Player player, int index) {
        if (index < 0 || index >= slots.size() || !slots.get(index).hasItem()) return ItemStack.EMPTY;
        var slot = slots.get(index); var stack = slot.getItem(); var original = stack.copy();
        if (index < 2) { if (!moveItemStackTo(stack, 2, slots.size(), true)) return ItemStack.EMPTY; }
        else if (!tile.isItemValidForSlot(0, stack) || !moveItemStackTo(stack, 0, 1, false)) return ItemStack.EMPTY;
        if (stack.isEmpty()) slot.set(ItemStack.EMPTY); else slot.setChanged(); slot.onTake(player, stack); return original;
    }
}
