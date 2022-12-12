///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.auxiliary;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.material.Fluid;
//import net.minecraftforge.fluids.FluidStack;
//import org.jetbrains.annotations.NotNull;
//import reika.dragonapi.libraries.ReikaInventoryHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.rotarycraft.api.Interfaces.Fillable;
//import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
//import reika.rotarycraft.base.blockentity.BlockEntityPiping.Flow;
//import reika.rotarycraft.base.blockentity.InventoriedPowerLiquidInOut;
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.RotaryItems;
//
//public class BlockEntityFillingStation extends InventoriedPowerLiquidInOut implements ConditionalOperation {
//
//    public static final int CAPACITY = 32000;
//
//    public static final int FUEL_PER_CRYSTAL = 1000;
//
//    public static final int INPUT_SLOT = 3;
//    public static final int FUEL_SLOT = 1;
//    public static final int OUTPUT_SLOT = 2;
//    public static final int FILLING_SLOT = 0;
//
//    //@Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        //this.getIOSidesDefault(world, pos); todo getIOSidesDefault
//        this.getPower(false);
//        if (power < MINPOWER)
//            return;
//
//        if (world.isClientSide)
//            return;
//
//        if (this.canMakeFuel()) {
//            this.makeFuel();
//        }
//
//        if (this.hasFillable()) {
//            if (this.canFill()) {
//                this.fill();
//            }
//        } else {
//            ItemStack is = inv[INPUT_SLOT];
//            if (is != null && is.getItem() instanceof Fillable) {
//                ReikaInventoryHelper.addOrSetStack(ReikaItemHelper.getSizedItemStack(is, 1), inv, FILLING_SLOT);
//                ReikaInventoryHelper.decrStack(INPUT_SLOT, inv);
//            }
//        }
//    }
//
//    private boolean hasContainer() {
//        return inv[FILLING_SLOT] != null && FluidContainerRegistry.isContainer(inv[FILLING_SLOT]);
//    }
//
//    private void fillContainer() {
//        int maxadd = this.getFluidToAdd() * 128;
//        ItemStack filled = FluidContainerRegistry.fillFluidContainer(new FluidStack(tank.getActualFluid(), maxadd), inv[FILLING_SLOT]);
//        if (filled != null) {
//            FluidStack fs = FluidContainerRegistry.getFluidForFilledItem(filled);
//            if (fs != null && fs.getAmount() > 0) {
//                int added = fs.getAmount();
//                tank.removeLiquid(added);
//                inv[FILLING_SLOT] = filled;
//            }
//        }
//    }
//
//    public boolean canMakeFuel() {
//        if (inv[FUEL_SLOT] == null)
//            return false;
//        FluidStack fs = FluidContainerRegistry.getFluidForFilledItem(inv[FUEL_SLOT]);
//        if (fs == null) {
//            boolean item = inv[FUEL_SLOT].getItem() == RotaryItems.ETHANOL.get();
//            boolean space = tank.canTakeIn(FUEL_PER_CRYSTAL) && (tank.isEmpty() || tank.getActualFluid().equals(RotaryFluids.ETHANOL));
//            return item && space;
//        } else {
//            if (tank.isEmpty())
//                return true;
//            return tank.canTakeIn(fs.getAmount()) && tank.getActualFluid().equals(fs.getFluid());
//        }
//    }
//
//    public void makeFuel() {
//        if (inv[FUEL_SLOT].getItem() == RotaryItems.ETHANOL.get()) {
//            tank.addLiquid(FUEL_PER_CRYSTAL, RotaryFluids.ETHANOL);
//            ReikaInventoryHelper.decrStack(1, inv);
//            return;
//        }
//        FluidStack fs = FluidContainerRegistry.getFluidForFilledItem(inv[FUEL_SLOT]);
//        tank.addLiquid(fs.getAmount(), fs.getFluid());
//        inv[FUEL_SLOT] = FluidContainerRegistry.drainFluidContainer(inv[FUEL_SLOT]);
//    }
//
//    private void fill() {
//        ItemStack is = inv[FILLING_SLOT];
//        Fillable i = (Fillable) is.getItem();
//        int added = i.addFluid(is, tank.getActualFluid(), this.getFluidToAdd());
//        if (added > 0)
//            tank.removeLiquid(added);
//        if (this.canShuttleItem()) {
//            if (ReikaInventoryHelper.addOrSetStack(is, inv, 2))
//                inv[FILLING_SLOT] = null;
//        }
//    }
//
//    private boolean canShuttleItem() {
//        ItemStack is = inv[FILLING_SLOT];
//        Fillable f = (Fillable) is.getItem();
//        ItemStack is2 = inv[OUTPUT_SLOT];
//        if (is2 == null)
//            return f.isFull(is);
//        if (!ReikaItemHelper.matchStacks(is, is2))
//            return false;
//        if (!f.isFull(is) || !f.isFull(is2))
//            return false;
//        return is.getCount() + is2.getCount() <= is.getMaxStackSize();
//    }
//
//    private int getFluidToAdd() {
//        int toadd = 4 * (int) ReikaMathLibrary.logbase(omega, 2);
//        return Math.min(toadd, tank.getLevel());
//    }
//
//    private boolean hasFillable() {
//        return inv[0] != null && inv[0].getItem() instanceof Fillable;
//    }
//
//    private boolean canFill() {
//        if (tank.isEmpty())
//            return false;
//        ItemStack is = inv[0];
//        if (is == null)
//            return false;
//        Fillable i = (Fillable) is.getItem();
//        int current = i.getCurrentFillLevel(is);
//        int max = i.getCapacity(is);
//        return i.isValidFluid(tank.getActualFluid(), is) && max > current;
//    }
//
//    private boolean canIntakeFluid(Fluid f) {
//        return tank.isEmpty() || tank.getActualFluid().equals(f);
//    }
//
//    //    @Override
//    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
//        return i == OUTPUT_SLOT;
//    }
//
//    @Override
//    public int getInventoryStackLimit() {
//        return 64;
//    }
//
//    @Override
//    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
//        if (i == INPUT_SLOT)
//            return itemstack.getItem() instanceof Fillable;
//        if (i == FUEL_SLOT) {
////            boolean container = FluidContainerRegistry.isFilledContainer(itemstack); todo fluidcontainer
////            return container || itemstack.getItem() == RotaryItems.ETHANOL.get();
//            return true;
//        }
//        return false;
//    }
//
//    //    @Override
//    public boolean canConnectToPipe(MachineRegistry m) {
//        return m == MachineRegistry.FUELLINE || m.isStandardPipe() || m == MachineRegistry.HOSE;
//    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.FILLINGSTATION;
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return true;
//    }
//
//    //    @Override
//    public int getRedstoneOverride() {
//        return this.canFill() ? 0 : 15;
//    }
//
//    public FluidStack getFluidStack() {
//        return tank.getActualFluid();
//    }
//
//    public int getLiquidScaled(int i) {
//        return tank.getLevel() * i / tank.getCapacity();
//    }
//
//    @Override
//    public Fluid getInputFluid() {
//        return null;
//    }
//
//    @Override
//    public boolean canReceiveFrom(Direction from) {
//        return true;
//    }
//
//    @Override
//    public int getCapacity() {
//        return CAPACITY;
//    }
//
//    @Override
//    public boolean isValidFluid(Fluid f) {
//        return true;
//    }
//
//    public ItemStack getItemForRender() {
//        return inv[0] != null ? inv[0].copy() : null;
//    }
//
//    @Override
//    public boolean areConditionsMet() {
//        return !tank.isEmpty() && inv[0] != null && inv[0].getItem() instanceof Fillable;
//    }
//
//    @Override
//    public String getOperationalStatus() {
//        return tank.isEmpty() ? "No Liquid" : this.areConditionsMet() ? "Operational" : "No Fillable Items";
//    }
//
//    //    @Override
//    public boolean canDrain(Direction from, Fluid fluid) {
//        return from == Direction.DOWN;
//    }
//
//    //    @Override
//    public Flow getFlowForSide(Direction side) {
//        return side == Direction.DOWN ? Flow.OUTPUT : Flow.INPUT;
//    }
//
//    @Override
//    public int getContainerSize() {
//        return 4;
//    }
//
//    @Override
//    public ItemStack getItem(int p_18941_) {
//        return null;
//    }
//
//    @Override
//    public ItemStack removeItem(int p_18942_, int p_18943_) {
//        return null;
//    }
//
//    @Override
//    public ItemStack removeItemNoUpdate(int p_18951_) {
//        return null;
//    }
//
//    @Override
//    public void setItem(int p_18944_, ItemStack p_18945_) {
//
//    }
//
//    @Override
//    public boolean stillValid(Player p_18946_) {
//        return false;
//    }
//
//    @Override
//    public void clearContent() {
//
//    }
//
//    /**
//     * For display purposes
//     */
//    @Override
//    public String getName() {
//        return "Filling Station";
//    }
//
//    @Override
//    public int getSlots() {
//        return 4;
//    }
//
//    @NotNull
//    @Override
//    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
//        return null;
//    }
//
//    @NotNull
//    @Override
//    public ItemStack extractItem(int slot, int amount, boolean simulate) {
//        return null;
//    }
//
//    @Override
//    public int getSlotLimit(int slot) {
//        return 0;
//    }
//
//    @Override
//    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
//        return false;
//    }
//}
