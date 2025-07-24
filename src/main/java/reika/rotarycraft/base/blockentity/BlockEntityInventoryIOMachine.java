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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.common.capabilities.Capability;
import net.neoforged.common.capabilities.ForgeCapabilities;
import net.neoforged.common.util.LazyOptional;
import net.neoforged.items.IItemHandler;
import net.neoforged.items.ItemStackHandler;
import reika.dragonapi.libraries.ReikaInventoryHelper;

public abstract class BlockEntityInventoryIOMachine extends BlockEntityIOMachine {

  protected ItemStackHandler itemHandler =
      new ItemStackHandler(getContainerSize()) {
        @Override
        protected void onContentsChanged(int slot) {
          setChanged();
        }

        @Override
        public boolean isItemValid(int slot,  ItemStack stack) {
          return canPlaceItem(slot, stack);
        }
      };

  private final LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.of(() -> itemHandler);

  public BlockEntityInventoryIOMachine(BlockEntityType<?> type, BlockPos pos, BlockState state) {
    super(type, pos, state);
  }

  @Override
  
  public <T> LazyOptional<T> getCapability(
       Capability<T> capability,  Direction facing) {
    if (capability == ForgeCapabilities.ITEM_HANDLER) return lazyItemHandler.cast();
    return super.getCapability(capability, facing);
  }

  @Override
  public void invalidateCaps() {
    super.invalidateCaps();
    lazyItemHandler.invalidate();
  }

  //    public final int[] getAccessibleSlotsFromSide(int var1) {
  //        if (this instanceof InertIInv)
  //            return new int[0];
  //        return ReikaInventoryHelper.getWholeInventoryForISided(this);
  //    }

  //    public final boolean canInsertItem(int i, ItemStack is, int side) {
  //        if (this instanceof InertIInv)
  //            return false;
  //        return ((Container) this).isItemValidForSlot(i, is);
  //    }

  public boolean isUseableByPlayer(Player var1) {
    return this.isPlayerAccessible(var1);
  }

  public final ItemStack decrStackSize(int par1, int par2) {
    return ReikaInventoryHelper.decrStackSize(itemHandler, par1, par2);
  }

  //    public final ItemStack getStackInSlotOnClosing(int par1) {
  //        return ReikaInventoryHelper.getStackInSlotOnClosing(this, par1);
  //    }

  public void openInventory() {}

  public void closeInventory() {}

  //    @Override
  //    public final void markDirty() {
  //        blockMetadata = level.getBlockMetadata(pos);
  //        level.markBlockEntityChunkModified(pos, this);
  //
  //        if (this.getBlockType() != Blocks.AIR) {
  //            level.func_147453_f(getPos(), this.getBlockType());
  //        }
  //    }

  @Override
  public void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);

    ListTag nbttaglist = new ListTag();

    for (int i = 0; i < itemHandler.getSlots(); i++) {
      if (itemHandler.getStackInSlot(i).isEmpty()) {
        CompoundTag CompoundTag = new CompoundTag();
        CompoundTag.putByte("Slot", (byte) i);
        itemHandler.getStackInSlot(i).save(CompoundTag);
        nbttaglist.add(CompoundTag);
      }
    }

    tag.put("Items", nbttaglist);
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);

    ListTag nbttaglist = tag.getList("Items", Tag.TAG_COMPOUND);
    itemHandler =
        new ItemStackHandler(getContainerSize()) {
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

  public abstract int getContainerSize();

  public abstract boolean canPlaceItem(int slot, ItemStack is);
}
