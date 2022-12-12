///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.processing;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.fluids.FluidStack;
//import net.minecraftforge.fluids.capability.IFluidHandler;
//import reika.dragonapi.instantiable.data.Collections.ChancedOutputList.ItemWithChance;
//import reika.dragonapi.instantiable.HybridTank;
//import reika.dragonapi.instantiable.TemporaryInventory;
//import reika.dragonapi.libraries.ReikaInventoryHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
//import reika.rotarycraft.auxiliary.interfaces.MultiOperational;
//import reika.rotarycraft.auxiliary.interfaces.PipeConnector;
//import reika.rotarycraft.base.blockentity.BlockEntityPiping.Flow;
//import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
//import reika.rotarycraft.registry.DurationRegistry;
//import reika.rotarycraft.registry.MachineRegistry;
//
//import java.util.Collection;
//
//
//public class BlockEntityCentrifuge extends InventoriedPowerReceiver implements MultiOperational, ConditionalOperation, IFluidHandler, PipeConnector {
//
//    private int progressTime;
//    public static final int CAPACITY = 10000;
//    private final HybridTank tank = new HybridTank("centrifuge", CAPACITY);
//
//    public int getProgressScaled(int l) {
//        return l * progressTime / this.getOperationTime();
//    }
//
//    @Override
//    protected void animateWithTick(Level world, BlockPos pos) {
//        phi += ReikaMathLibrary.logbase2(omega);
//    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.CENTRIFUGE;
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        if (isFlipped)
//            this.getPowerAbove();
//        else
//            this.getPowerBelow();
//
//        if (power >= MINPOWER && omega >= MINSPEED) {
//            int n = this.getNumberConsecutiveOperations();
//            for (int i = 0; i < n; i++)
//                this.doOperation(n > 1);
//        } else {
//            progressTime = 0;
//        }
//    }
//
//    private void doOperation(boolean multiple) {
//        ItemStack in = inv[0];
//        if (in != null && RecipesCentrifuge.getRecipes().isCentrifugable(in)) {
//            progressTime++;
//
//            if (multiple || progressTime >= this.getOperationTime()) {
//                CentrifugeRecipe out = RecipesCentrifuge.getRecipes().getRecipeResult(in);
//                Collection<ItemWithChance> items = out.getItems();
//                for (int i = 0; i < out.maxStack && inv[0] != null; i++) {
//                    if (this.canMakeAllOf(items)) {
//                        FluidStack fs = out.getFluid();
//                        if (fs == null || tank.canTakeIn(fs)) {
//                            Collection<ItemStack> c = out.rollItems();
//                            for (ItemStack is : c) {
//                                ReikaInventoryHelper.addToIInv(is, this, true, 1, this.getContainerSize());
//                            }
//                            fs = out.rollFluid();
//                            if (fs != null) {
//                                tank.addLiquid(fs.amount, fs.getFluid());
//                            }
//                            ReikaInventoryHelper.decrStack(0, inv);
//                        } else {
//                            break;
//                        }
//                    } else {
//                        break;
//                    }
//                }
//                progressTime = 0;
//            }
//        } else {
//            progressTime = 0;
//        }
//    }
//
//    private boolean canMakeAllOf(Collection<ItemWithChance> out) {
//        Container temp = new TemporaryInventory(9);
//        for (int i = 0; i < 9; i++) {
//            ItemStack in = inv[i + 1];
//            if (in != null)
//                temp.setInventorySlotContents(i, in.copy());
//        }
//        for (ItemWithChance is : out) {
//            if (!ReikaInventoryHelper.addToIInv(is.getItem(), temp))
//                return false;
//        }
//        return true;
//    }
//
//    public int getLevel() {
//        return tank.getLevel();
//    }
//
//    public Fluid getFluid() {
//        return tank.getActualFluid();
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        return 0;
//    }
//
//    @Override
//    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
//        return i != 0;
//    }
//
//    @Override
//    public int getContainerSize() {
//        return 10;
//    }
//
//    @Override
//    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
//        return i == 0 && RecipesCentrifuge.getRecipes().isCentrifugable(itemstack);
//    }
//
//    @Override
//    public boolean areConditionsMet() {
//        return inv[0] != null && RecipesCentrifuge.getRecipes().isCentrifugable(inv[0]);
//    }
//
//    @Override
//    public String getOperationalStatus() {
//        return this.areConditionsMet() ? "Operational" : "Missing Items";
//    }
//
//    @Override
//    public int getOperationTime() {
//        return DurationRegistry.CENTRIFUGE.getOperationTime(omega);
//    }
//
//    @Override
//    public int getNumberConsecutiveOperations() {
//        return DurationRegistry.CENTRIFUGE.getNumberOperations(omega);
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//
//        progressTime = NBT.getInt("CookTime");
//        tank.load(NBT);
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//        NBT.putInt("CookTime", progressTime);
//        tank.saveAdditional(NBT);
//    }
//
//    @Override
//    public boolean canConnectToPipe(MachineRegistry m) {
//        return m.isStandardPipe() || m == MachineRegistry.HOSE;
//    }
//
//    @Override
//    public boolean canConnectToPipeOnSide(MachineRegistry p, Direction side) {
//        return this.canConnectToPipe(p) && side.getStepY() == 0;
//    }
//
//    @Override
//    public Flow getFlowForSide(Direction side) {
//        return side.getStepY() == 0 ? Flow.OUTPUT : Flow.NONE;
//    }
//
//    @Override
//    public int fill(Direction from, FluidStack resource, boolean doFill) {
//        return 0;
//    }
//
//    @Override
//    public FluidStack drain(Direction from, FluidStack resource, boolean doDrain) {
//        return this.canDrain(from, resource.getFluid()) ? tank.drain(resource.amount, doDrain) : null;
//    }
//
//    @Override
//    public FluidStack drain(Direction from, int maxDrain, boolean doDrain) {
//        return this.canDrain(from, null) ? tank.drain(maxDrain, doDrain) : null;
//    }
//
//    @Override
//    public boolean canFill(Direction from, Fluid fluid) {
//        return false;
//    }
//
//    @Override
//    public boolean canDrain(Direction from, Fluid fluid) {
//        return from.getStepY() == 0 && ReikaFluidHelper.isFluidDrainableFromTank(fluid, tank);
//    }
//
//    @Override
//    public FluidTankInfo[] getTankInfo(Direction from) {
//        return new FluidTankInfo[]{tank.getInfo()};
//    }
//
//    public int getLiquidScaled(int a) {
//        return a * tank.getLevel() / tank.getCapacity();
//    }
//
//    public int getProgress() {
//        return progressTime;
//    }
//
//
//    public void syncProgress(int time) {
//        progressTime = time;
//    }
//
//}
