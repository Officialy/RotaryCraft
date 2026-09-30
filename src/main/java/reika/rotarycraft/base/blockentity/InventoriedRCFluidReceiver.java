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
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import reika.dragonapi.instantiable.storage.ManagedItemHandler;
import reika.dragonapi.interfaces.blockentity.HasItemHandler;

/**
 * Unpowered fluid receiver with an inventory (the Drying Bed family): {@link RCFluidReceiver}'s
 * tank/pipe plumbing plus the same ManagedItemHandler scaffolding as
 * {@link InventoriedPowerLiquidReceiver}. Inventories persist with the caller's registry context
 * and expose the original sided rules through vanilla containers and NeoForge transactions.
 */
public abstract class InventoriedRCFluidReceiver extends RCFluidReceiver implements HasItemHandler, WorldlyContainer {

    public ManagedItemHandler itemHandler = new ManagedItemHandler(getContainerSize()) {
        @Override
        public boolean isValid(int slot, ItemResource resource) {
            return isItemValidForSlot(slot, resource.toStack(1));
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    public InventoriedRCFluidReceiver(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public final ManagedItemHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        itemHandler.serialize(output.child("ItemsRaw"));
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        clearContent();
        input.child("ItemsRaw").ifPresent(itemHandler::deserialize);
    }

    @Override public boolean isEmpty() {
        for (int slot = 0; slot < getContainerSize(); slot++) if (!getItem(slot).isEmpty()) return false;
        return true;
    }
    @Override public ItemStack getItem(int slot) { return itemHandler.getStackInSlot(slot); }
    @Override public ItemStack removeItem(int slot, int amount) { return itemHandler.extractItem(slot, amount, false); }
    @Override public ItemStack removeItemNoUpdate(int slot) { return removeItem(slot, getItem(slot).getCount()); }
    @Override public void setItem(int slot, ItemStack stack) { itemHandler.setStackInSlot(slot, stack); }
    @Override public boolean stillValid(Player player) { return isPlayerAccessible(player); }
    @Override public void clearContent() {
        for (int slot = 0; slot < getContainerSize(); slot++) setItem(slot, ItemStack.EMPTY);
    }
    @Override public boolean canPlaceItem(int slot, ItemStack stack) { return isItemValidForSlot(slot, stack); }
    @Override public int[] getSlotsForFace(Direction side) {
        return java.util.stream.IntStream.range(0, getContainerSize()).toArray();
    }
    @Override public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction side) { return canPlaceItem(slot, stack); }
    @Override public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) { return canExtractItem(slot, stack, side.ordinal()); }
    public abstract boolean canExtractItem(int slot, ItemStack stack, int side);

    public abstract int getContainerSize();

    public abstract boolean isItemValidForSlot(int slot, ItemStack is);

    public final ItemStack getStackInSlot(int slot) {
        return itemHandler.getStackInSlot(slot);
    }

    public final void setInventorySlotContents(int slot, ItemStack is) {
        itemHandler.setStackInSlot(slot, is);
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean hasAnInventory() {
        return true;
    }

    @Override
    public boolean hasATank() {
        return true;
    }

}
