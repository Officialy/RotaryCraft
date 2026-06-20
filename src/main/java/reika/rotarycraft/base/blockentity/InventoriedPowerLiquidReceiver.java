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
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import reika.dragonapi.instantiable.storage.ManagedItemHandler;
import reika.dragonapi.interfaces.blockentity.HasItemHandler;
import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.rotarycraft.registry.MachineRegistry;

import java.util.Optional;

public abstract class InventoriedPowerLiquidReceiver extends PoweredLiquidReceiver implements HasItemHandler {

    public ManagedItemHandler itemHandler = new ManagedItemHandler(getContainerSize()) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    @Override
    public final ManagedItemHandler getItemHandler() {
        return itemHandler;
    }

    public InventoriedPowerLiquidReceiver(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
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

    // 1.21.5: ValueOutput/ValueInput pattern. ManagedItemHandler#serialize/deserialize handles the
    // per-slot stack codec; we just wrap a CompoundTag through TagValueOutput/TagValueInput so we
    // can write it as a single "ItemsRaw" entry on the modern BE save API.
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
        };
        Optional<CompoundTag> raw = input.read("ItemsRaw", CompoundTag.CODEC);
        if (raw.isPresent()) {
            ValueInput nested = TagValueInput.create(ProblemReporter.DISCARDING,
                    this.level == null ? RegistryAccess.EMPTY : this.level.registryAccess(), raw.get());
            itemHandler.deserialize(nested);
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
