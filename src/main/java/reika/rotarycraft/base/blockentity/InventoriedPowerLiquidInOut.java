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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import reika.dragonapi.libraries.ReikaInventoryHelper;

public abstract class InventoriedPowerLiquidInOut extends PoweredLiquidInOut implements Container {

    protected ItemStack[] inv = new ItemStack[this.getContainerSize()];

    public InventoriedPowerLiquidInOut(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public final ItemStack getStackInSlot(int par1) {
        return inv[par1];
    }

    public final void setInventorySlotContents(int par1, ItemStack is) {
        inv[par1] = is;
    }

    public void openInventory() {
    }

    public void closeInventory() {
    }

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

//    public int[] getAccessibleSlotsFromSide(int var1) {
//        if (this instanceof InertIInv)
//            return new int[0];
//        return ReikaInventoryHelper.getWholeInventoryForISided(this);
//    }

    public boolean canInsertItem(int i, ItemStack is, int side) {

        return this.isItemValidForSlot(i, is);
    }

    public boolean isUseableByPlayer(Player var1) {
        //todo return this.isPlayerAccessible(var1);
        return true;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag nbttaglist = new ListTag();
        for (int i = 0; i < inv.length; i++) {
            if (inv[i] != null) {
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.putByte("Slot", (byte) i);
                inv[i].save(compoundTag);
                nbttaglist.add(compoundTag);
            }
        }
        tag.put("Items", nbttaglist);
        return tag;
    }

    @Override
    public void load(CompoundTag NBT) {
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
    public int getCapacity() {
        return 0;
    }

    @Override
    public Fluid getInputFluid() {
        return null;
    }

    @Override
    public boolean canReceiveFrom(Direction from) {
        return false;
    }

    /**
     * A more accurate way of checking if the machine has an inventory than just instanceof Container.
     */
    @Override
    public boolean hasAnInventory() {
        return false;
    }

    /**
     * A more accurate way of checking if the machine interfaces with pipes than just instanceof IFluidHandler.
     */
    @Override
    public boolean hasATank() {
        return false;
    }

}
