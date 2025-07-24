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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.common.capabilities.Capability;
import net.neoforged.common.capabilities.ForgeCapabilities;
import net.neoforged.common.util.LazyOptional;
import net.neoforged.fluids.FluidStack;
import net.neoforged.items.IItemHandler;
import net.neoforged.items.ItemStackHandler;


import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.rotarycraft.registry.MachineRegistry;

public abstract class InventoriedPowerLiquidReceiver extends PoweredLiquidReceiver {

    protected ItemStackHandler itemHandler = new ItemStackHandler(getContainerSize()) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    private final LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.of(() -> itemHandler);

    public InventoriedPowerLiquidReceiver(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    
    public <T> LazyOptional<T> getCapability( Capability<T> capability,  Direction facing) {
        if (capability == ForgeCapabilities.ITEM_HANDLER)
            return lazyItemHandler.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    public final ItemStack getStackInSlot(int slot) {
        return itemHandler.getStackInSlot(slot);
    }

    public final void setInventorySlotContents(int slot, ItemStack is) {
        itemHandler.setStackInSlot(slot, is);

        this.onItemSet(slot, is);
    }

    protected void onItemSet(int slot, ItemStack is) {

    }

    public boolean validatesInputs() {
        return false;
    }

    public void openInventory() {
    }

    public void closeInventory() {
    }

//    @Override
//    public final void markDirty() {
//        level.markBlockEntityChunkModified(worldPosition, this);
//
//        if (this.getBlockType() != Blocks.AIR) {
//            level.func_147453_f(getPos(), this.getBlockType());
//        }
//    }

    public int getInventoryStackLimit() {
        return 64;
    }

    public abstract boolean isItemValidForSlot(int slot, ItemStack is);

    public final ItemStack decrStackSize(int par1, int par2) {
        return ReikaInventoryHelper.decrStackSize(itemHandler, par1, par2);
    }

//    public final ItemStack getStackInSlotOnClosing(int par1) {
//        return ReikaInventoryHelper.getStackInSlotOnClosing(this, par1);
//    }
//
//    public int[] getAccessibleSlotsFromSide(int var1) {
//        if (this instanceof InertIInv)
//            return new int[0];
//        if (this instanceof HiddenInventorySlot)
//            return ReikaArrayHelper.getLinearArrayExceptFor(this.getContainerSize(), ((HiddenInventorySlot) this).getHiddenSlots());
//        return ReikaInventoryHelper.getWholeInventoryForISided(this);
//    }

//    public boolean canInsertItem(int i, ItemStack is, int side) {
//        if (this instanceof InertIInv)
//            return false;
//        return ((Inventory) this).isItemValidForSlot(i, is);
//    }

//    public boolean isUseableByPlayer(Player var1) {
//        return this.isPlayerAccessible(var1);
//    }

    //    @Override
    public void saveAdditional(CompoundTag NBT) {
//        super.saveAdditional(NBT);

        ListTag nbttaglist = new ListTag();

        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (itemHandler.getStackInSlot(i).isEmpty()) {
                CompoundTag tag = new CompoundTag();
                tag.putByte("Slot", (byte) i);
                itemHandler.getStackInSlot(i).save(tag);
                nbttaglist.add(tag);
            }
        }

        NBT.put("Items", nbttaglist);
    }

    //    @Override
    public void load(CompoundTag NBT) {
//        super.load(NBT);

        ListTag nbttaglist = NBT.getList("Items", Tag.TAG_COMPOUND);
        itemHandler = new ItemStackHandler(getContainerSize()) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
        for (int i = 0; i < nbttaglist.size(); i++) {
            CompoundTag tag = nbttaglist.getCompound(i);
            byte byte0 = tag.getByte("Slot");

            if (byte0 >= 0 && byte0 < itemHandler.getSlots()) {
                itemHandler.setStackInSlot(byte0, ItemStack.of(tag));
            }
        }
    }

    @Override
    public Fluid getInputFluid() {
        return null;
    }

    @Override
    public boolean canReceiveFrom(Direction from) {
        return false;
    }

    @Override
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    public boolean hasATank() {
        return false;
    }

    @Override
    public int getCapacity() {
        return 0;
    }

    @Override
    public boolean canConnectToPipe(MachineRegistry m) {
        return false;
    }

    @Override
    public MachineRegistry getMachine() {
        return null;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    public abstract int getContainerSize();
}
