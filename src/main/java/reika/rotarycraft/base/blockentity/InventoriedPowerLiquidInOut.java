/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.base.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.*;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import reika.dragonapi.instantiable.storage.ManagedItemHandler;
import reika.dragonapi.interfaces.blockentity.HasItemHandler;

/** Inventoried single-tank machine, with separate manual and automation permissions. */
public abstract class InventoriedPowerLiquidInOut extends PoweredLiquidInOut implements WorldlyContainer, HasItemHandler {
    private final class MachineItemHandler extends ManagedItemHandler {
        private MachineItemHandler() { super(getContainerSize()); }
        @Override public boolean isValid(int slot, ItemResource item) { return isItemValidForSlot(slot, item.toStack()); }
        @Override protected int getCapacity(int slot, ItemResource item) { return Math.min(64, super.getCapacity(slot, item)); }
        @Override protected void onContentsChanged(int slot) { setChanged(); }
    }
    private final MachineItemHandler items = new MachineItemHandler();
    protected final ManagedItemHandler itemHandler = items;
    private final ResourceHandler<ItemResource> automation = new DelegatingResourceHandler<>(() -> items) {
        @Override public int extract(int slot, ItemResource item, int amount, TransactionContext transaction) {
            TransferPreconditions.checkNonEmptyNonNegative(item, amount);
            java.util.Objects.checkIndex(slot, size());
            return canExtractItem(slot, item.toStack()) ? super.extract(slot, item, amount, transaction) : 0;
        }
        @Override public int extract(ItemResource item, int amount, TransactionContext transaction) {
            TransferPreconditions.checkNonEmptyNonNegative(item, amount);
            int extracted = 0;
            for (int slot = 0; slot < size() && extracted < amount; slot++) extracted += extract(slot, item, amount - extracted, transaction);
            return extracted;
        }
    };
    protected InventoriedPowerLiquidInOut(BlockEntityType<?> type, BlockPos pos, BlockState state) { super(type, pos, state); }
    public final ItemStack getStackInSlot(int slot) { return items.getStackInSlot(slot); }
    public final void setInventorySlotContents(int slot, ItemStack stack) { items.setStackInSlot(slot, stack); }
    public final ItemStack decrStackSize(int slot, int amount) { return removeItem(slot, amount); }
    public int getInventoryStackLimit() { return 64; }
    @Override public final ManagedItemHandler getItemHandler() { return items; }
    @Override public final ResourceHandler<ItemResource> getAutomationItemHandler() { return automation; }
    // Vanilla Container users (notably hoppers) mutate this stack and then call setChanged().
    // ManagedItemHandler's public snapshot API remains unchanged for transactional clients.
    @Override public final ItemStack getItem(int slot) { return items.getLiveStack(slot); }
    @Override public final ItemStack removeItem(int slot, int amount) { return items.extractItem(slot, amount, false); }
    @Override public final ItemStack removeItemNoUpdate(int slot) { return removeItem(slot, getItem(slot).getCount()); }
    @Override public final void setItem(int slot, ItemStack item) { items.setStackInSlot(slot, item.copyWithCount(Math.min(item.getCount(), Math.min(64, item.getMaxStackSize())))); }
    @Override public final boolean isEmpty() { for (int i = 0; i < getContainerSize(); i++) if (!getItem(i).isEmpty()) return false; return true; }
    @Override public final void clearContent() { for (int i = 0; i < getContainerSize(); i++) items.setStackInSlot(i, ItemStack.EMPTY); }
    @Override public final boolean stillValid(Player player) { return isPlayerAccessible(player); }
    @Override public final boolean canPlaceItem(int slot, ItemStack item) { return isItemValidForSlot(slot, item); }
    @Override public final int[] getSlotsForFace(Direction side) { return java.util.stream.IntStream.range(0, getContainerSize()).toArray(); }
    @Override public final boolean canPlaceItemThroughFace(int slot, ItemStack item, Direction side) { return isItemValidForSlot(slot, item); }
    @Override public final boolean canTakeItemThroughFace(int slot, ItemStack item, Direction side) { return canExtractItem(slot, item); }
    public abstract boolean isItemValidForSlot(int slot, ItemStack item);
    public abstract boolean canExtractItem(int slot, ItemStack item);
    @Override public final boolean hasAnInventory() { return true; }
    @Override public final boolean hasATank() { return true; }
    @Override protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        items.serialize(output.child("Inventory"));
    }
    @Override protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        // Keep the handler identity and the caller's registry context, including detached loads.
        items.deserialize(input.child("Inventory").orElseGet(() -> input.childOrEmpty("ItemsRaw")));
    }
}
