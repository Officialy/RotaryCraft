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
import net.neoforged.neoforge.fluids.FluidStack;
import reika.dragonapi.instantiable.storage.ManagedItemHandler;


import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.rotarycraft.registry.MachineRegistry;

public abstract class InventoriedPowerLiquidProducer extends PoweredLiquidProducer {

    protected ManagedItemHandler itemHandler = new ManagedItemHandler(getContainerSize()){
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    public InventoriedPowerLiquidProducer(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
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

    // 1.21.5: BlockEntity#saveAdditional/loadAdditional now take ValueOutput/ValueInput.
    // ManagedItemHandler#serialize(ValueOutput)/deserialize(ValueInput) replaces the old
    // ItemStack.save/of and ListTag round-tripping.
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
        return FluidStack.EMPTY;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return null;
    }

    @Override
    public void updateEntity(Level level, BlockPos blockPos) {
        super.updateBlockEntity();

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
