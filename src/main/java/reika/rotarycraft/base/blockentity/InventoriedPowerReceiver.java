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
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import reika.dragonapi.instantiable.storage.ManagedItemHandler;
import reika.dragonapi.interfaces.blockentity.InertIInv;


/**
 * 1.7.10's base was an {@code ISidedInventory}; as a {@link WorldlyContainer} the machine again
 * takes GUI shift-clicks ({@code CoreContainer.quickMoveStack} only moves into Containers), feeds
 * hoppers by its slot rules, and spills its inventory when broken (vanilla's Container branch of
 * {@code preRemoveSideEffects}, as {@code BlockTEBase.breakBlock} did). Machines that must not spill
 * (the scale chest) override {@code preRemoveSideEffects}.
 */
public abstract class InventoriedPowerReceiver extends BlockEntityPowerReceiver implements WorldlyContainer {

    public ManagedItemHandler itemHandler = new ManagedItemHandler(getContainerSize()) {
        @Override protected void onContentsChanged(int slot) { setChanged(); }
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

    /** 1.7.10 {@code IInventory.isItemValidForSlot}: what a player or automation may put in {@code slot}. */
    public abstract boolean isItemValidForSlot(int slot, ItemStack is);

    /** 1.7.10 {@code ISidedInventory.canExtractItem}; {@code side} is the ForgeDirection/Direction ordinal. */
    public abstract boolean canExtractItem(int slot, ItemStack is, int side);

    private int[] allSlots;

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (this instanceof InertIInv)
            return new int[0];
        if (allSlots == null || allSlots.length != this.getContainerSize()) {
            allSlots = new int[this.getContainerSize()];
            for (int i = 0; i < allSlots.length; i++)
                allSlots[i] = i;
        }
        return allSlots;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack is) {
        return this.isItemValidForSlot(slot, is);
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack is, Direction side) {
        return !(this instanceof InertIInv) && this.isItemValidForSlot(slot, is);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack is, Direction side) {
        return !(this instanceof InertIInv) && this.canExtractItem(slot, is, side.ordinal());
    }

    @Override
    public int getMaxStackSize() {
        return this.getInventoryStackLimit();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < this.getContainerSize(); i++)
            if (!itemHandler.getStackInSlot(i).isEmpty())
                return false;
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return itemHandler.getStackInSlot(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        ItemStack ret = itemHandler.extractItem(slot, count, false);
        this.onInventoryChanged(slot);
        return ret;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack is = itemHandler.getStackInSlot(slot);
        itemHandler.setStackInSlot(slot, ItemStack.EMPTY);
        return is;
    }

    @Override
    public void setItem(int slot, ItemStack is) {
        this.setInventorySlotContents(slot, is);
    }

    @Override
    public boolean stillValid(Player player) {
        return this.isUseableByPlayer(player);
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < this.getContainerSize(); i++)
            itemHandler.setStackInSlot(i, ItemStack.EMPTY);
    }
}
