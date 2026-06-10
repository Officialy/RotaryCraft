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
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import reika.dragonapi.instantiable.storage.ManagedItemHandler;
import reika.dragonapi.libraries.ReikaInventoryHelper;

import java.util.Optional;

public abstract class BlockEntityInventoryIOMachine extends BlockEntityIOMachine {

  public ManagedItemHandler itemHandler =
      new ManagedItemHandler(getContainerSize()) {
        @Override
        protected void onContentsChanged(int slot) {
          setChanged();
        }

        @Override
        public boolean isItemValid(int slot,  ItemStack stack) {
          return canPlaceItem(slot, stack);
        }
      };


  public BlockEntityInventoryIOMachine(BlockEntityType<?> type, BlockPos pos, BlockState state) {
    super(type, pos, state);
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

  // 1.21.5: BlockEntity#saveAdditional/loadAdditional now take ValueOutput/ValueInput.
  // The old manual ListTag/per-slot ItemStack.save loop is replaced by ManagedItemHandler's
  // built-in serialize/deserialize, wrapped through TagValueOutput so we can store one
  // CompoundTag under "ItemsRaw" on the modern ValueOutput API.
  @Override
  protected void saveAdditional(ValueOutput output) {
    super.saveAdditional(output);
    TagValueOutput nested = TagValueOutput.createWithContext(ProblemReporter.DISCARDING,
            this.level == null ? RegistryAccess.EMPTY : this.level.registryAccess());
    itemHandler.serialize(nested);
    output.store("ItemsRaw", CompoundTag.CODEC, nested.buildResult());
  }

  @Override
  protected void loadAdditional(ValueInput input) {
    super.loadAdditional(input);
    itemHandler = new ManagedItemHandler(getContainerSize()) {
      @Override
      protected void onContentsChanged(int slot) {
        setChanged();
      }

      @Override
      public boolean isItemValid(int slot, ItemStack stack) {
        return canPlaceItem(slot, stack);
      }
    };
    Optional<CompoundTag> raw = input.read("ItemsRaw", CompoundTag.CODEC);
    if (raw.isPresent()) {
      ValueInput nested = TagValueInput.create(ProblemReporter.DISCARDING,
              this.level == null ? RegistryAccess.EMPTY : this.level.registryAccess(), raw.get());
      itemHandler.deserialize(nested);
    }
  }

  public abstract int getContainerSize();

  public abstract boolean canPlaceItem(int slot, ItemStack is);
}
