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
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.material.Fluid;
//import reika.dragonapi.instantiable.StepTimer;
//import reika.dragonapi.libraries.ReikaInventoryHelper;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.rotarycraft.registry.MachineRegistry;
//
//public class BlockEntityDryingBed extends InventoriedRCFluidReceiver {
//
//    public static final int CAPACITY = 2000;
//
//    private final StepTimer timer = new StepTimer(400);
//    public int progress;
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        ItemStack is = tank.isEmpty() ? null : RecipesDryingBed.getRecipes().getDryingResult(tank.getFluid());
//        if (this.canMake(is)) {
//            timer.update();
//            if (timer.checkCap()) {
//                while (this.canMake(is)) {
//                    this.make(is);
//                    is = tank.isEmpty() ? null : RecipesDryingBed.getRecipes().getDryingResult(tank.getFluid());
//                }
//            }
//        } else {
//            timer.reset();
//        }
//        progress = timer.getTick();
//    }
//
//    public void addLiquid(Fluid f, int amt) {
//        tank.addLiquid(amt, f);
//    }
//
//    private boolean canMake(ItemStack is) {
//        return is != null && this.canMakeMore(is);
//    }
//
//    private void make(ItemStack is) {
//        ReikaInventoryHelper.addOrSetStack(is, inv, 0);
//        int amt = RecipesDryingBed.getRecipes().getRecipeConsumption(is);
//        tank.removeLiquid(amt);
//    }
//
//    private boolean canMakeMore(ItemStack is) {
//        if (inv[0] == null)
//            return true;
//        return ReikaItemHelper.matchStacks(is, inv[0]) && inv[0].getCount() + is.getCount() <= inv[0].getMaxStackSize();
//    }
//
//    @Override
//    public boolean canExtractItem(int i, ItemStack is, int s) {
//        return true;
//    }
//
//    @Override
//    public int getContainerSize() {
//        return 1;
//    }
//
//    @Override
//    public int getInventoryStackLimit() {
//        return 64;
//    }
//
//    @Override
//    public boolean isItemValidForSlot(int i, ItemStack is) {
//        return false;
//    }
//
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.DRYING;
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return true;
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        return 0;
//    }
//
//    @Override
//    public boolean canConnectToPipe(MachineRegistry m) {
//        return m.isStandardPipe() || m == MachineRegistry.HOSE || m == MachineRegistry.FUELLINE;
//    }
//
//    @Override
//    public int getCapacity() {
//        return CAPACITY;
//    }
//
//    @Override
//    public Fluid getInputFluid() {
//        return null;
//    }
//
//    @Override
//    public boolean isValidFluid(Fluid f) {
//        return RecipesDryingBed.getRecipes().isValidFluid(f);
//    }
//
//    @Override
//    public boolean canReceiveFrom(Direction from) {
//        return from != Direction.DOWN;
//    }
//
//    public int getLiquidScaled(int a) {
//        return a * tank.getLevel() / tank.getCapacity();
//    }
//
//    public int getProgressScaled(int a) {
//        return a * progress / timer.getCap();
//    }
//
//    public Fluid getFluid() {
//        return tank.getActualFluid();
//    }
//
//    public boolean hasFluid() {
//        return !tank.isEmpty();
//    }
//
//}
