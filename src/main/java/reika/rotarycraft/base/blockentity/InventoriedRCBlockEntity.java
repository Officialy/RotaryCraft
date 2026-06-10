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
import reika.dragonapi.instantiable.storage.ManagedItemHandler;
import reika.dragonapi.interfaces.blockentity.HasItemHandler;
import reika.dragonapi.libraries.ReikaInventoryHelper;

public abstract class InventoriedRCBlockEntity extends RotaryCraftBlockEntity implements Container, HasItemHandler {

    protected ManagedItemHandler itemHandler = new ManagedItemHandler(getContainerSize()){
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    public InventoriedRCBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    // -----------------------------------------------------------------------
    // 1.21.9: previously this class delegated every IItemHandlerModifiable method onto
    // `itemHandler` so that CoreContainer's `te instanceof IItemHandler` resolution would
    // find it and slot rendering worked. With IItemHandler being removed and CoreContainer
    // now routing via {@link HasItemHandler#getItemHandler}, we expose the handler through
    // that single getter and let the handler itself be the {@code ResourceHandler<ItemResource>}
    // that the new {@code ResourceHandlerSlot} (and {@code SlotItemHandler}'s replacement)
    // bind to.
    // -----------------------------------------------------------------------
    @Override
    public final ManagedItemHandler getItemHandler() {
        return itemHandler;
    }

    public final int[] getAccessibleSlotsFromSide(int var1) {
//        if (this instanceof InertIInv)
//            return new int[0];
//        return ReikaInventoryHelper.getWholeInventoryForISided(this);
        return new int[]{0};
    }

    public final ItemStack getStackInSlot(int slot) {
        return itemHandler.getStackInSlot(slot);
    }

    public final void setInventorySlotContents(int slot, ItemStack is) {
        itemHandler.setStackInSlot(slot, is);
    }
//    public boolean isUseableByPlayer(Player var1) {
//        return this.isPlayerAccessible(var1);
//    }

    public final ItemStack decrStackSize(int par1, int par2) {
        return ReikaInventoryHelper.decrStackSize(itemHandler, par1, par2);
    }

//    public final ItemStack getStackInSlotOnClosing(int par1) {
//        return ReikaInventoryHelper.getStackInSlotOnClosing(this, par1);
//    }

    public void openInventory() {
    }

    public void closeInventory() {
    }

    // 1.21.5: BlockEntity#serializeNBT/load were replaced by saveAdditional/loadAdditional
    // (ValueOutput/ValueInput). The inventory contents are bridged via ManagedItemHandler.serialize.
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
        itemHandler = new ManagedItemHandler(getContainerSize()){
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

    public abstract int getContainerSize();

}
