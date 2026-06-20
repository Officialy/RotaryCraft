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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.api.interfaces.Fillable;
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

    public static final int FILLING_SLOT = 0; // the Fillable currently being filled
    public static final int FUEL_SLOT = 1;    // a fuel bucket draining into the tank
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
        } else if (this.canFill()) {
            this.fill();
        }
    }

    // Drain a jet-fuel bucket in the fuel slot into the tank (the tank can also be piped in).
    private void makeFuel() {
        ItemStack is = itemHandler.getStackInSlot(FUEL_SLOT);
        if (is.isEmpty())
            return;
        boolean roomForJetFuel = tank.canTakeIn(1000)
                && (tank.isEmpty() || tank.getActualFluid().getFluid().equals(RotaryFluids.JET_FUEL.get()));
        if (roomForJetFuel && ReikaItemHelper.matchStacks(is, RotaryItems.JET_FUEL_BUCKET.get())) {
            tank.addLiquid(1000, RotaryFluids.JET_FUEL.get());
            itemHandler.setStackInSlot(FUEL_SLOT, new ItemStack(Items.BUCKET));
        }
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
        ItemStack is = itemHandler.getStackInSlot(FILLING_SLOT);
        if (is.isEmpty() || !(is.getItem() instanceof Fillable f))
            return;
        int added = f.addFluid(is); // the item adds its own fuel type and reports the amount
        if (added > 0) {
            tank.removeLiquid(added);
            itemHandler.setStackInSlot(FILLING_SLOT, is); // commit the item's NBT change
        }
        // Once full, shuttle the item to the output slot.
        if (f.isFull(is) && itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty()) {
            itemHandler.setStackInSlot(OUTPUT_SLOT, is.copy());
            itemHandler.setStackInSlot(FILLING_SLOT, ItemStack.EMPTY);
        }
    }

    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        if (i == INPUT_SLOT)
            return itemstack.getItem() instanceof Fillable;
        if (i == FUEL_SLOT)
            return ReikaItemHelper.matchStacks(itemstack, RotaryItems.JET_FUEL_BUCKET.get());
        return false;
    }

    @Override
    public boolean canConnectToPipe(MachineRegistry m) {
        return m == MachineRegistry.FUELLINE || m.isStandardPipe() || m == MachineRegistry.HOSE;
    }

    @Override
    public int fillPipe(Direction from, FluidStack resource, IFluidHandler.FluidAction doFill) {
        return resource.isEmpty() ? 0 : tank.fill(resource, doFill);
    }

    @Override
    public FluidStack drainPipe(Direction from, int maxDrain, IFluidHandler.FluidAction doDrain) {
        return FluidStack.EMPTY;
    }

    @Override
    public void onEMP() {
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
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    public boolean hasATank() {
        return false;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Filling Station");
    }

    // Container contract (the menu drives the inventory through itemHandler slots directly).
    @Override
    public ItemStack getItem(int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
    }

    @Override
    public boolean stillValid(Player player) {
        return this.isPlayerAccessible(player);
    }

    @Override
    public void clearContent() {
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new ContainerFillingStation(id, inv, this);
    }
}
