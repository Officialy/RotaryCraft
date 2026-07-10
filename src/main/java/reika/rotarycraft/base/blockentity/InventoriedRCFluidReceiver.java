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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import reika.dragonapi.instantiable.storage.ManagedItemHandler;
import reika.dragonapi.interfaces.blockentity.HasItemHandler;

/**
 * Unpowered fluid receiver with an inventory (the Drying Bed family): {@link RCFluidReceiver}'s
 * tank/pipe plumbing plus the same ManagedItemHandler scaffolding as
 * {@link InventoriedPowerLiquidReceiver}. 26.2 port of the legacy base of the same name.
 */
public abstract class InventoriedRCFluidReceiver extends RCFluidReceiver implements HasItemHandler {

    public ManagedItemHandler itemHandler = new ManagedItemHandler(getContainerSize()) {
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
