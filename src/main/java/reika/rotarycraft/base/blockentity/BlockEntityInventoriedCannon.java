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
import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;
import reika.dragonapi.instantiable.storage.ManagedItemHandler;
import reika.dragonapi.interfaces.blockentity.InertIInv;
import reika.dragonapi.libraries.ReikaInventoryHelper;

import java.util.Optional;

/**
 * Ammo-fed cannon inventory. Like 1.7.10's {@code ISidedInventory}, it is a {@link WorldlyContainer}:
 * automation may insert only what {@link #isItemValid} accepts (nothing for an {@link InertIInv}
 * cannon), through any face, and may never extract. {@code RotaryBlockEntities} exposes it as an
 * item capability through NeoForge's {@code WorldlyContainerWrapper}.
 */
public abstract class BlockEntityInventoriedCannon extends BlockEntityAimedCannon implements WorldlyContainer {

    public ManagedItemHandler itemHandler = new ManagedItemHandler(getContainerSize()) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    public BlockEntityInventoriedCannon(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    //        @Override
    public final void setInventorySlotContents(int i, ItemStack itemstack) {
        itemHandler.setStackInSlot(i, itemstack);
    }

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

    // ==== Slot access delegated to itemHandler (subclasses override isItemValid to filter ammo) ====

    public int getSlots() {
        return itemHandler.getSlots();
    }

    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return this.isItemValid(slot, stack) ? itemHandler.insertItem(slot, stack, simulate) : stack;
    }

    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return itemHandler.extractItem(slot, amount, simulate);
    }

    public int getSlotLimit(int slot) {
        return itemHandler.getSlotLimit(slot);
    }

    public boolean isItemValid(int slot, ItemStack stack) {
        return true;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (this instanceof InertIInv)
            return new int[0];
        int[] slots = new int[this.getContainerSize()];
        for (int i = 0; i < slots.length; i++)
            slots[i] = i;
        return slots;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side) {
        return !(this instanceof InertIInv) && this.isItemValid(slot, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        return false;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return this.isItemValid(slot, stack);
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < itemHandler.getSlots(); i++)
            if (!itemHandler.getStackInSlot(i).isEmpty())
                return false;
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return itemHandler.getStackInSlot(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return itemHandler.extractItem(slot, amount, false);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack s = itemHandler.getStackInSlot(slot);
        itemHandler.setStackInSlot(slot, ItemStack.EMPTY);
        return s;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        itemHandler.setStackInSlot(slot, stack);
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < itemHandler.getSlots(); i++)
            itemHandler.setStackInSlot(i, ItemStack.EMPTY);
    }

    @Override
    public boolean stillValid(net.minecraft.world.entity.player.Player player) {
        return !isRemoved() && player.distanceToSqr(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5) <= 64;
    }

}
