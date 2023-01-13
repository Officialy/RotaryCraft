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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.rotarycraft.RotaryCraft;

import javax.annotation.Nonnull;

public abstract class InventoriedPowerReceiver extends BlockEntityPowerReceiver implements Container {

    protected ItemStack[] inv = new ItemStack[this.getContainerSize()];
    private ItemStackHandler itemHandler = createHandler();
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    public InventoriedPowerReceiver(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    @Override
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
        if (capability == ForgeCapabilities.ITEM_HANDLER)
            return lazyItemHandler.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    protected void onInventoryChanged(int slot) {

    }

    public final ItemStack getStackInSlot(int par1) {
        return inv[par1];
    }

    public final void setInventorySlotContents(int par1, ItemStack is) {
        inv[par1] = is;
        this.onInventoryChanged(par1);
    }

    public void openInventory() {
    }

    public void closeInventory() {
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    public final ItemStack decrStackSize(int par1, int par2) {
        ItemStack ret = ReikaInventoryHelper.decrStackSize(this, par1, par2);
        this.onInventoryChanged(par1);
        return ret;
    }

//    public final ItemStack getStackInSlotOnClosing(int par1) {
//        ItemStack ret = ReikaInventoryHelper.getStackInSlotOnClosing(this, par1);
//        this.onInventoryChanged(par1);
//        return ret;
//    }

//    public final int[] getAccessibleSlotsFromSide(int var1) {
//        if (this instanceof InertIInv)
//            return new int[0];
//        return ReikaInventoryHelper.getWholeInventoryForISided(this);
//    }

    public final boolean canInsertItem(int i, ItemStack is, int side) {
//        if (this instanceof InertIInv)
//            return false;
        return this.canPlaceItem(i, is);
    }

    public boolean isUseableByPlayer(Player var1) {
//        return this.isPlayerAccessible(var1);
        return true;
    }

    //    @Override
    public void saveAdditional(CompoundTag NBT) {

        ListTag nbttaglist = new ListTag();

        for (int i = 0; i < inv.length; i++) {
            if (inv[i] != null) {
                CompoundTag CompoundTag = new CompoundTag();
                CompoundTag.putShort("Slot", (short) i);
                inv[i].save(CompoundTag);
                nbttaglist.add(CompoundTag);
                //ReikaJavaLibrary.pConsole(i+":"+inv[i]);
            }
        }

        NBT.put("Items", nbttaglist);
    }
    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
    }

    @Override
    public CompoundTag serializeNBT() {
        return super.serializeNBT();
    }
    //    @Override
    public void load(CompoundTag NBT) {

        ListTag nbttaglist = NBT.getList("Items", Tag.TAG_COMPOUND);
        inv = new ItemStack[this.getContainerSize()];

        for (int i = 0; i < nbttaglist.size(); i++) {
            CompoundTag CompoundTag = nbttaglist.getCompound(i);
            short byte0 = CompoundTag.getShort("Slot");

            if (byte0 >= 0 && byte0 < inv.length) {
                inv[byte0] = ItemStack.of(CompoundTag);
                //ReikaJavaLibrary.pConsole(byte0+":"+inv[byte0]);
            } else {
                RotaryCraft.LOGGER.error(this + " tried to load an inventory slot " + byte0 + " from NBT!");
                //Thread.dumpStack();
            }
        }
    }


    private ItemStackHandler createHandler() {
        return new ItemStackHandler(getContainerSize()) {

            @Override
            protected void onContentsChanged(int slot) {
                // To make sure the BE persists when the chunk is saved later we need to
                // mark it changed every time the item handler changes
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return stack.getItem() == Items.DIAMOND;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (stack.getItem() != Items.DIAMOND) {
                    return stack;
                }
                return super.insertItem(slot, stack, simulate);
            }
        };
    }
}
