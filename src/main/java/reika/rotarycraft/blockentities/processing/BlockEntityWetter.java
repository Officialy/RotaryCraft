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
//import net.minecraft.world.entity.item.ItemEntity;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.material.Fluid;
//import net.minecraftforge.fluids.FluidStack;
//import reika.dragonapi.base.OneSlotMachine;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.rotarycraft.base.blockentity.InventoriedPowerLiquidReceiver;
//import reika.rotarycraft.registry.MachineRegistry;
//
//public class BlockEntityWetter extends InventoriedPowerLiquidReceiver implements OneSlotMachine {
//
//    private int tick = 0;
//    private ItemEntity item;
//
//    private int extractionCooldown = 0;
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        this.getPowerBelow();
//        this.updateItem();
//
//
//        boolean ticked = false;
//
//        if (power >= MINPOWER && omega >= MINSPEED) {
//            ItemStack is = itemHandler.getStackInSlot(0);
//            if (is != null) {
//                FluidStack fs = tank.getFluid();
//                if (fs != null) {
//                    WettingRecipe wr = RecipesWetter.getRecipes().getRecipe(is, fs);
//                    if (wr != null) {
//                        ticked = true;
//                        if (tick >= this.getDuration(wr)) {
//                            tank.removeLiquid(wr.getFluid().amount);
//                            itemHandler.getStackInSlot(0) = wr.getOutput();
//                            tick = 0;
//                            this.onItemSet(0, itemHandler.getStackInSlot(0));
//                        } else {
//                            tick += 1 + 4 * ReikaMathLibrary.logbase2(omega / MINSPEED);
//                            extractionCooldown = 10;
//                        }
//                    }
//                }
//            }
//        }
//
//        if (ticked) {
//            extractionCooldown = 10;
//        } else {
//            if (extractionCooldown > 0)
//                extractionCooldown--;
//            tick = 0;
//        }
//    }
//
//    public int getDuration(WettingRecipe wr) {
//        return Math.max(1, wr.duration - 5 * ((omega / MINSPEED) - 1));
//    }
//
//    @Override
//    protected void onItemSet(int slot, ItemStack is) {
//        this.updateItem();
//    }
//
//    private void updateItem() {
//        if (item == null && itemHandler.getStackInSlot(0).isEmpty())
//            return;
//        if ((item == null && !itemHandler.getStackInSlot(0).isEmpty()) || (item != null && itemHandler.getStackInSlot(0).isEmpty()) || !ReikaItemHelper.matchStacks(item.getEntityItem(), itemHandler.getStackInSlot(0))) {
//            item = !itemHandler.getStackInSlot(0).isEmpty() ? new InertItem(level, itemHandler.getStackInSlot(0)) : null;
//        }
//        this.syncAllData(true);
//    }
//
//    public ItemEntity getItem() {
//        return item;
//    }
//
//    @Override
//    public boolean canExtractItem(int slot, ItemStack is, int side) {
//        return tick == 0 && extractionCooldown == 0;
//    }
//
//    @Override
//    public int getContainerSize() {
//        return 1;
//    }
//
//    @Override
//    public int getInventoryStackLimit() {
//        return 1;
//    }
//
//    @Override
//    public boolean canConnectToPipe(MachineRegistry m) {
//        return m == MachineRegistry.HOSE || m == MachineRegistry.FUELLINE || m.isStandardPipe();
//    }
//
//    @Override
//    public boolean isItemValidForSlot(int slot, ItemStack is) {
//        if (tank.getActualFluid() != null) {
//            return RecipesWetter.getRecipes().isWettableWith(is, tank.getActualFluid());
//        }
//        return RecipesWetter.getRecipes().isWettable(is);
//    }
//
//    @Override
//    public Fluid getInputFluid() {
//        return null;
//    }
//
//    @Override
//    public boolean isValidFluid(Fluid f) {
//        if (tick > 0)
//            return false;
//        if (!itemHandler.getStackInSlot(0).isEmpty()) {
//            return RecipesWetter.getRecipes().isWettableWith(itemHandler.getStackInSlot(0), f);
//        }
//        return RecipesWetter.getRecipes().isValidFluid(f);
//    }
//
//    @Override
//    public boolean canReceiveFrom(Direction from) {
//        return from.getStepY() == 0;
//    }
//
//    @Override
//    public int getCapacity() {
//        return 1000;
//    }
//
//    @Override
//    protected void animateWithTick(Level world, BlockPos pos) {
//        if (!this.isInWorld()) {
//            phi = 0;
//            return;
//        }
//        if (power < MINPOWER || torque < MINTORQUE)
//            return;
//        phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
//    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.WETTER;
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
//    public void saveAdditional(CompoundTag NBT) {
//        super.saveAdditional(NBT);
//
//        NBT.putInt("extract", extractionCooldown);
//    }
//
//    @Override
//    public void load(CompoundTag NBT) {
//        super.load(NBT);
//
//        extractionCooldown = NBT.getInt("extract");
//    }
//
//}
