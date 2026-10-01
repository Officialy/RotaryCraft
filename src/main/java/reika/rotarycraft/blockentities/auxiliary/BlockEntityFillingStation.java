/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.auxiliary;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.api.interfaces.Fillable;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import reika.dragonapi.instantiable.storage.ManagedItemHandler;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
import reika.rotarycraft.base.blockentity.BlockEntityPiping.Flow;
import reika.rotarycraft.base.blockentity.InventoriedPowerLiquidInOut;
import reika.rotarycraft.gui.container.machine.inventory.ContainerFillingStation;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryFluids;
import reika.rotarycraft.registry.RotaryItems;

public class BlockEntityFillingStation extends InventoriedPowerLiquidInOut implements ConditionalOperation {

    public static final int CAPACITY = 32000;
    public static final int FUEL_PER_CRYSTAL = 1000;

    public static final int FILLING_SLOT = 0; // the Fillable currently being filled
    public static final int FUEL_SLOT = 1;    // filled fluid containers or ethanol crystals
    public static final int OUTPUT_SLOT = 2;  // filled item output
    public static final int INPUT_SLOT = 3;   // Fillable items waiting to be filled

    public BlockEntityFillingStation(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.FILLING_STATION.get(), pos, state);
    }

    @Override
    public int getContainerSize() {
        return 4;
    }

    @Override
    protected String getTEName() {
        return "fillingstation";
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.FILLING_STATION.get();
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.FILLINGSTATION;
    }

    @Override
    public int getCapacity() {
        return CAPACITY;
    }

    @Override
    public Fluid getInputFluid() {
        return null;
    }

    @Override
    public boolean isValidFluid(Fluid f) {
        return true;
    }

    @Override
    public boolean canReceiveFrom(Direction from) {
        return true;
    }

    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        read = getBlockState().getValue(BlockRotaryCraftMachine.FACING).getOpposite();
        this.getPower(false);
        if (power < MINPOWER)
            return;
        if (world.isClientSide())
            return;

        this.makeFuel();

        if (!this.hasFillable()) {
            // Shuttle a waiting Fillable into the active filling slot.
            ItemStack in = itemHandler.getStackInSlot(INPUT_SLOT);
            if (!in.isEmpty() && in.getItem() instanceof Fillable && itemHandler.getStackInSlot(FILLING_SLOT).isEmpty()) {
                itemHandler.setStackInSlot(FILLING_SLOT, ReikaItemHelper.getSizedItemStack(in, 1));
                itemHandler.extractItem(INPUT_SLOT, 1, false);
            }
        } else {
            if (this.canFill()) this.fill();
            this.shuttleFullItem();
        }
    }

    private void makeFuel() {
        ItemStack item = itemHandler.getStackInSlot(FUEL_SLOT);
        if (item.isEmpty()) return;
        if (item.is(RotaryItems.ETHANOL.get())) {
            if (tank.canTakeIn(FUEL_PER_CRYSTAL) && (tank.isEmpty() || tank.getActualFluid().getFluid() == RotaryFluids.ETHANOL.get())) {
                tank.addLiquid(FUEL_PER_CRYSTAL, RotaryFluids.ETHANOL.get());
                itemHandler.extractItem(FUEL_SLOT, 1, false);
                setChanged();
            }
            return;
        }
        FluidStack contents = FluidUtil.getFirstStackContained(item);
        if (contents.isEmpty() || !tank.canTakeIn(contents.getAmount())
                || !tank.isEmpty() && tank.getActualFluid().getFluid() != contents.getFluid()) return;
        // Container remainders are internal results, not new fuel-slot inputs. A strict scratch
        // slot permits the empty bucket without letting it spill into the pack slots.
        var working = new ManagedItemHandler(1);
        working.setStackInSlot(0, item);
        var container = ItemAccess.forHandlerIndexStrict(working, 0).oneByOne().getCapability(Capabilities.Fluid.ITEM);
        if (container == null) return;
        boolean drained = false;
        try (Transaction transaction = Transaction.openRoot()) {
            var moved = ResourceHandlerUtil.moveFirst(container, getFluidHandler(null), resource -> resource.getFluid() == contents.getFluid(), contents.getAmount(), transaction);
            if (moved != null && moved.amount() == contents.getAmount()) {
                transaction.commit();
                drained = true;
            }
        }
        if (drained) itemHandler.setStackInSlot(FUEL_SLOT, working.getStackInSlot(0));
    }

    private boolean hasFillable() {
        ItemStack is = itemHandler.getStackInSlot(FILLING_SLOT);
        return !is.isEmpty() && is.getItem() instanceof Fillable;
    }

    private boolean canFill() {
        if (tank.isEmpty())
            return false;
        ItemStack is = itemHandler.getStackInSlot(FILLING_SLOT);
        if (is.isEmpty() || !(is.getItem() instanceof Fillable f))
            return false;
        return f.isValidFluid(tank.getActualFluid(), is) && f.getCapacity(is) > f.getCurrentFillLevel(is);
    }

    private void fill() {
        ItemStack item = itemHandler.getStackInSlot(FILLING_SLOT);
        Fillable fillable = (Fillable)item.getItem();
        int rate = omega > 0 ? 4 * (31 - Integer.numberOfLeadingZeros(omega)) : 0;
        int amount = Math.min(rate, tank.getFluidLevel());
        int added = fillable.addFluid(item, tank.getActualFluid(), amount);
        if (added > 0) {
            tank.removeLiquid(added);
            itemHandler.setStackInSlot(FILLING_SLOT, item);
            setChanged();
        }
    }

    private void shuttleFullItem() {
        ItemStack item = itemHandler.getStackInSlot(FILLING_SLOT);
        if (item.isEmpty() || !(item.getItem() instanceof Fillable fillable) || !fillable.isFull(item)) return;
        ItemStack output = itemHandler.getStackInSlot(OUTPUT_SLOT);
        if (!output.isEmpty() && (!ItemStack.isSameItemSameComponents(item, output) || item.getCount() + output.getCount() > output.getMaxStackSize())) return;
        itemHandler.setStackInSlot(OUTPUT_SLOT, item.copyWithCount(item.getCount() + output.getCount()));
        itemHandler.setStackInSlot(FILLING_SLOT, ItemStack.EMPTY);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack) { return slot == OUTPUT_SLOT; }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        if (i == INPUT_SLOT)
            return itemstack.getItem() instanceof Fillable;
        if (i == FUEL_SLOT)
            return itemstack.is(RotaryItems.ETHANOL.get()) || !FluidUtil.getFirstStackContained(itemstack).isEmpty();
        return false;
    }

    @Override
    public boolean canConnectToPipe(MachineRegistry m) {
        return m == MachineRegistry.FUELLINE || m.isStandardPipe() || m == MachineRegistry.HOSE;
    }
    @Override
    public Flow getFlowForSide(Direction side) {
        return side == Direction.DOWN ? Flow.OUTPUT : Flow.INPUT;
    }

    public boolean canDrain(Direction from, Fluid fluid) {
        return from == Direction.DOWN;
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
    }

    @Override
    public boolean hasModelTransparency() {
        return true;
    }

    @Override
    public int getRedstoneOverride() {
        return this.canFill() ? 0 : 15;
    }

    public FluidStack getFluidStack() {
        return tank.getActualFluid();
    }

    public int getLiquidScaled(int i) {
        return tank.getFluidLevel() * i / tank.getCapacity();
    }

    public ItemStack getItemForRender() {
        ItemStack is = itemHandler.getStackInSlot(FILLING_SLOT);
        return !is.isEmpty() ? is.copy() : ItemStack.EMPTY;
    }

    @Override
    public boolean areConditionsMet() {
        return !tank.isEmpty() && this.hasFillable();
    }

    @Override
    public String getOperationalStatus() {
        return tank.isEmpty() ? "No Liquid" : this.areConditionsMet() ? "Operational" : "No Fillable Items";
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Filling Station");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new ContainerFillingStation(id, inv, this);
    }
}
