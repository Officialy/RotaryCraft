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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import reika.dragonapi.instantiable.storage.ManagedItemHandler;


public abstract class InventoriedPowerReceiver extends BlockEntityPowerReceiver {

    public ManagedItemHandler itemHandler = new ManagedItemHandler(getContainerSize()) {
        @Override protected void onContentsChanged(int slot) { setChanged(); }
        @Override public void deserialize(ValueInput input) {
            var loaded = input.read(VALUE_IO_KEY, codec);
            for (int slot = 0; slot < getContainerSize(); slot++)
                stacks.set(slot, loaded.isPresent() && slot < loaded.get().size() ? loaded.get().get(slot) : ItemStack.EMPTY);
        }
    };

    public InventoriedPowerReceiver(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    public void openInventory() {
    }

    public void closeInventory() {
    }

    public int getInventoryStackLimit() {
        return 64;
    }


    public final ItemStack getStackInSlot(int slot) {
        return itemHandler.getStackInSlot(slot);
    }

    public final void setInventorySlotContents(int slot, ItemStack is) {
        itemHandler.setStackInSlot(slot, is);
        this.onInventoryChanged(slot);
    }

    protected void onInventoryChanged(int slot) {
        setChanged();
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

    public boolean isUseableByPlayer(Player var1) {
        return this.isPlayerAccessible(var1);
    }

    // 1.21.5: BlockEntity#saveAdditional/loadAdditional now take ValueOutput/ValueInput.
    // ManagedItemHandler#serialize(ValueOutput)/deserialize(ValueInput) is the new bridge for
    // the inventory contents.
    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        itemHandler.serialize(output.child("ItemsRaw"));
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        // The child retains the loading caller's registry context even before the entity has a level.
        itemHandler.deserialize(input.childOrEmpty("ItemsRaw"));
    }

    public abstract int getContainerSize();
}
