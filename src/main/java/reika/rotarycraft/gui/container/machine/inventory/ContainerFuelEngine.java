package reika.rotarycraft.gui.container.machine.inventory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import reika.dragonapi.base.CoreContainer;
import reika.rotarycraft.modinterface.TileEntityFuelEngine;
import reika.rotarycraft.registry.RotaryMenus;

/** V33a has no machine inventory: only the player inventory and four gauges. */
public final class ContainerFuelEngine extends CoreContainer<TileEntityFuelEngine> {
    private final int[] gauges = new int[5];
    public ContainerFuelEngine(int id, Inventory inv, FriendlyByteBuf data) { this(id, inv, (TileEntityFuelEngine)inv.player.level().getBlockEntity(data.readBlockPos())); }
    public ContainerFuelEngine(int id, Inventory inv, TileEntityFuelEngine tile) {
        super(RotaryMenus.FUEL_ENGINE.get(), id, inv, tile);
        addPlayerInventory(inv);
        addDataSlots(new ContainerData() {
            @Override public int get(int index) { return switch (index) {
                case 0 -> tile.getFuelLevel(); case 1 -> tile.getWaterLevel(); case 2 -> tile.getLubeLevel();
                case 3 -> tile.getTemperature(); default -> tile.getFuelInterval();
            }; }
            @Override public void set(int index, int value) { gauges[index] = value; }
            @Override public int getCount() { return 5; }
        });
    }
    public int getGauge(int index) { return tile.getLevel().isClientSide() ? gauges[index] : switch (index) {
        case 0 -> tile.getFuelLevel(); case 1 -> tile.getWaterLevel(); case 2 -> tile.getLubeLevel();
        case 3 -> tile.getTemperature(); default -> tile.getFuelInterval();
    }; }
    @Override public boolean stillValid(Player player) { return !tile.isRemoved() && tile.isPlayerAccessible(player); }
    @Override public ItemStack quickMoveStack(Player player, int index) {
        if (index < 0 || index >= slots.size()) return ItemStack.EMPTY;
        var slot = slots.get(index); if (!slot.hasItem()) return ItemStack.EMPTY;
        var stack = slot.getItem(); var original = stack.copy();
        if (!moveItemStackTo(stack, index < 27 ? 27 : 0, index < 27 ? 36 : 27, false)) return ItemStack.EMPTY;
        slot.set(stack.isEmpty() ? ItemStack.EMPTY : stack); slot.setChanged(); slot.onTake(player, stack);
        return original;
    }
}
