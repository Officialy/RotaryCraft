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
import net.minecraftforge.items.IItemHandler;
import reika.dragonapi.libraries.ReikaInventoryHelper;

public abstract class InventoriedRCBlockEntity extends RotaryCraftBlockEntity implements IItemHandler, Container {

    protected ItemStack[] inv = new ItemStack[this.getContainerSize()];

    public InventoriedRCBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public final int[] getAccessibleSlotsFromSide(int var1) {
//        if (this instanceof InertIInv)
//            return new int[0];
//        return ReikaInventoryHelper.getWholeInventoryForISided(this);
        return new int[]{0};
    }

    public final boolean canInsertItem(int i, ItemStack is, Direction side) {
//        return this.isItemValidForSlot(i, is);
        return true;
    }

    public final ItemStack getStackInSlot(int par1) {
        return inv[par1];
    }

    public final void setInventorySlotContents(int par1, ItemStack is) {
        inv[par1] = is;
    }

//    public boolean isUseableByPlayer(Player var1) {
//        return this.isPlayerAccessible(var1);
//    }

    public final ItemStack decrStackSize(int par1, int par2) {
        return ReikaInventoryHelper.decrStackSize(this, par1, par2);
    }

//    public final ItemStack getStackInSlotOnClosing(int par1) {
//        return ReikaInventoryHelper.getStackInSlotOnClosing(this, par1);
//    }


    public void openInventory() {
    }

    public void closeInventory() {
    }

    @Override
    public CompoundTag serializeNBT() {

        ListTag nbttaglist = new ListTag();
        CompoundTag nbt = new CompoundTag();
        for (int i = 0; i < inv.length; i++) {
            if (inv[i] != null) {
                CompoundTag tag = new CompoundTag();
                tag.putByte("Slot", (byte) i);
                inv[i].serializeNBT();
                nbttaglist.add(tag);
            }
        }

        nbt.put("Items", nbttaglist);
        return nbt;
    }

    @Override
    public void load(CompoundTag nbt) {
        ListTag nbttaglist = nbt.getList("Items", Tag.TAG_COMPOUND);
        inv = new ItemStack[this.getContainerSize()];

        for (int i = 0; i < nbttaglist.size(); i++) {
            CompoundTag CompoundTag = nbttaglist.getCompound(i);
            byte byte0 = CompoundTag.getByte("Slot");

            if (byte0 >= 0 && byte0 < inv.length) {
                inv[byte0] = ItemStack.of(CompoundTag);
            }
        }
    }
}
