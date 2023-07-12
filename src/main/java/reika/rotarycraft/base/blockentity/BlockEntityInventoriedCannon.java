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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reika.dragonapi.libraries.ReikaInventoryHelper;

public abstract class BlockEntityInventoriedCannon extends BlockEntityAimedCannon implements IItemHandler, Container {

    protected ItemStackHandler itemHandler = new ItemStackHandler(getContainerSize()) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    private final LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.of(() -> itemHandler);

    public BlockEntityInventoriedCannon(BlockEntityType<?> type, BlockPos pos, BlockState state) {
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
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }
//        @Override
    public final void setInventorySlotContents(int i, ItemStack itemstack) {
        itemHandler.setStackInSlot(i, itemstack);
    }

    @Override
    public final ItemStack getStackInSlot(int i) {
        return itemHandler.getStackInSlot(i);
    }

    public final ItemStack decrStackSize(int par1, int par2) {
        return ReikaInventoryHelper.decrStackSize(itemHandler, par1, par2);
    }


    public void openInventory() {
    }

    public void closeInventory() {
    }

    public void saveAdditional(CompoundTag NBT) {
        ListTag nbttaglist = new ListTag();

        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (itemHandler.getStackInSlot(i).isEmpty()) {
                CompoundTag CompoundTag = new CompoundTag();
                CompoundTag.putByte("Slot", (byte) i);
                itemHandler.getStackInSlot(i).save(CompoundTag);
                nbttaglist.add(CompoundTag);
            }
        }

        NBT.put("Items", nbttaglist);
    }

    public void load(CompoundTag NBT) {
        ListTag nbttaglist = NBT.getList("Items", Tag.TAG_COMPOUND);
        itemHandler = new ItemStackHandler(getContainerSize()) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
        for (int i = 0; i < nbttaglist.size(); i++) {
            CompoundTag CompoundTag = nbttaglist.getCompound(i);
            byte byte0 = CompoundTag.getByte("Slot");

            if (byte0 >= 0 && byte0 < itemHandler.getSlots()) {
                itemHandler.setStackInSlot(byte0, ItemStack.of(CompoundTag));
            }
        }
    }

}
