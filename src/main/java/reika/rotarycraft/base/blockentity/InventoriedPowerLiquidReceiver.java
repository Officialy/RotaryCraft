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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.rotarycraft.registry.MachineRegistry;

public abstract class InventoriedPowerLiquidReceiver extends PoweredLiquidReceiver implements IItemHandler, Container {

    protected ItemStack[] inv = new ItemStack[this.getContainerSize()];

    public InventoriedPowerLiquidReceiver(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public int getSlots() {
        return 0;
    }

    public final ItemStack getStackInSlot(int par1) {
        return inv[par1];
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return null;
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return null;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 0;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return false;
    }

    public final void setInventorySlotContents(int par1, ItemStack is) {
        inv[par1] = is;

        this.onItemSet(par1, is);
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
        return ReikaInventoryHelper.decrStackSize(this, par1, par2);
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

        for (int i = 0; i < inv.length; i++) {
            if (inv[i] != null) {
                CompoundTag tag = new CompoundTag();
                tag.putByte("Slot", (byte) i);
                inv[i].save(tag);
                nbttaglist.add(tag);
            }
        }

        NBT.put("Items", nbttaglist);
    }

    //    @Override
    public void load(CompoundTag NBT) {
//        super.load(NBT);

        ListTag nbttaglist = NBT.getList("Items", Tag.TAG_COMPOUND);
        inv = new ItemStack[this.getContainerSize()];

        for (int i = 0; i < nbttaglist.size(); i++) {
            CompoundTag tag = nbttaglist.getCompound(i);
            byte byte0 = tag.getByte("Slot");

            if (byte0 >= 0 && byte0 < inv.length) {
                inv[byte0] = ItemStack.of(tag);
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
}
