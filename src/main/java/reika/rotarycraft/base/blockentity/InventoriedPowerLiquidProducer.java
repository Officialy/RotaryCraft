package reika.rotarycraft.base.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.common.capabilities.Capability;
import net.neoforged.common.capabilities.ForgeCapabilities;
import net.neoforged.common.util.LazyOptional;
import net.neoforged.fluids.FluidStack;
import net.neoforged.items.IItemHandler;
import net.neoforged.items.ItemStackHandler;


import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.rotarycraft.registry.MachineRegistry;

public abstract class InventoriedPowerLiquidProducer extends PoweredLiquidProducer {

    protected ItemStackHandler itemHandler = new ItemStackHandler(getContainerSize()){
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    private final LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.of(() -> itemHandler);
    public InventoriedPowerLiquidProducer(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    
    public <T> LazyOptional<T> getCapability( Capability<T> capability,  Direction facing) {
        if (capability == ForgeCapabilities.ITEM_HANDLER)
            return lazyItemHandler.cast();
        return super.getCapability(capability, facing);
    }
    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }
    @Override
    public MachineRegistry getMachine() {
        return null;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public int getCapacity() {
        return 0;
    }

    @Override
    public boolean canOutputTo(Direction to) {
        return false;
    }

    public final ItemStack getStackInSlot(int par1) {
        return itemHandler.getStackInSlot(par1);
    }

    public final void setInventorySlotContents(int par1, ItemStack is) {
        itemHandler.setStackInSlot(par1, is);
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

    public final ItemStack getStackInSlotOnClosing(int par1) {
//        return ReikaInventoryHelper.getStackInSlotOnClosing(this, par1);
        return null;
    }

/*    public int[] getAccessibleSlotsFromSide(Direction dir) {
        if (this instanceof InertIInv)
            return new int[0];
        return ReikaInventoryHelper.getWholeInventoryForISided(this);
    }

    public boolean canInsertItem(int i, ItemStack is, int side) {
        if (this instanceof InertIInv)
            return false;
        return ((Container) this).isItemValidForSlot(i, is);
    }*/

    public boolean isUseableByPlayer(Player var1) {
        return this.isPlayerAccessible(var1);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {


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
    public CompoundTag serializeNBT() {
        super.serializeNBT();
        CompoundTag nbt = new CompoundTag();
        ListTag nbttaglist = new ListTag();
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (itemHandler.getStackInSlot(i).isEmpty()) {
                nbt.putByte("Slot", (byte) i);
                itemHandler.getStackInSlot(i).save(nbt);
                nbttaglist.add(nbt);
            }
        }
        nbt.put("Items", nbttaglist);
        return nbt;
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);

        ListTag nbttaglist = nbt.getList("Items", Tag.TAG_COMPOUND);
        itemHandler = new ItemStackHandler(getContainerSize()){
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

    @Override
    public int getTanks() {
        return 0;
    }

    @Override
    public  FluidStack getFluidInTank(int tank) {
        return null;
    }

    @Override
    public int getTankCapacity(int tank) {
        return 0;
    }

    @Override
    public boolean isFluidValid(int tank,  FluidStack stack) {
        return false;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return 0;
    }

    @Override
    public  FluidStack drain(int maxDrain, FluidAction action) {
        return null;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return null;
    }

    @Override
    public void updateEntity(Level level, BlockPos blockPos) {

    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    protected String getTEName() {
        return null;
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
    public boolean canConnectToPipe(MachineRegistry m) {
        return false;
    }

    @Override
    public int fillPipe(Direction from, FluidStack resource, FluidAction action) {
        return 0;
    }
}
