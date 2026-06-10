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
import reika.dragonapi.instantiable.storage.ManagedItemHandler;
import reika.dragonapi.libraries.ReikaInventoryHelper;

public abstract class InventoriedPowerLiquidInOut extends PoweredLiquidInOut implements Container {

    protected ManagedItemHandler itemHandler = new ManagedItemHandler(getContainerSize()) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    public InventoriedPowerLiquidInOut(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    public final ItemStack getStackInSlot(int slot) {
        return itemHandler.getStackInSlot(slot);
    }

    public final void setInventorySlotContents(int slot, ItemStack is) {
        itemHandler.setStackInSlot(slot, is);
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
        return ReikaInventoryHelper.decrStackSize(itemHandler, par1, par2);
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

    // 1.21.5: serializeNBT/load were replaced by saveAdditional/loadAdditional (ValueOutput/ValueInput).
    @Override
    protected void saveAdditional(net.minecraft.world.level.storage.ValueOutput output) {
        super.saveAdditional(output);
        net.minecraft.world.level.storage.TagValueOutput nested = net.minecraft.world.level.storage.TagValueOutput.createWithContext(net.minecraft.util.ProblemReporter.DISCARDING, this.level == null ? net.minecraft.core.RegistryAccess.EMPTY : this.level.registryAccess());
        itemHandler.serialize(nested);
        output.store("ItemsRaw", CompoundTag.CODEC, nested.buildResult());
    }

    @Override
    protected void loadAdditional(net.minecraft.world.level.storage.ValueInput input) {
        super.loadAdditional(input);
        itemHandler = new ManagedItemHandler(getContainerSize()) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
        java.util.Optional<CompoundTag> raw = input.read("ItemsRaw", CompoundTag.CODEC);
        if (raw.isPresent()) {
            net.minecraft.world.level.storage.ValueInput nested = net.minecraft.world.level.storage.TagValueInput.create(net.minecraft.util.ProblemReporter.DISCARDING, this.level == null ? net.minecraft.core.RegistryAccess.EMPTY : this.level.registryAccess(), raw.get());
            itemHandler.deserialize(nested);
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
