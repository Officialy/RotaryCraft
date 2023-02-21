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
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.material.Fluids;
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.libraries.ReikaInventoryHelper;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.rotarycraft.auxiliary.RotaryAux;
//import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
//import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
//import reika.rotarycraft.auxiliary.interfaces.TemperatureTE;
//import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
//import reika.rotarycraft.registry.DurationRegistry;
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.RotaryItems;
//
//import java.util.List;
//
//public class BlockEntityPurifier extends InventoriedPowerReceiver implements TemperatureTE, DiscreteFunction, ConditionalOperation {
//
//    public static final int SMELTTEMP = 600;
//    public static final int MAXTEMP = 1000;
//    public int cookTime = 0;
//    public int temperature;
//
//    @Override
//    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
//        return i == 6;
//    }
//
//    @Override
//    public int getContainerSize() {
//        return 8;
//    }
//
//    @Override
//    public Block getBlockEntityBlockID() {
//        return null;
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        cookTime = tickcount;
//        tickcount++;
//        this.updateTemperature(world, pos);
//        this.getSummativeSidedPower();
//        if (power < MINPOWER) {
//            tickcount = 0;
//            return;
//        }
//        if (!this.canSmelt()) {
//            tickcount = 0;
//            return;
//        }
//        if (tickcount < this.getOperationTime())
//            return;
//        this.smelt();
//    }
//
//    @Override
//    protected void animateWithTick(Level level, BlockPos blockPos) {
//
//    }
//
//    private void smelt() {
//        tickcount = 0;
//        int count = 0;
//        for (int i = 1; i < 6; i++) {
//            if (this.isModSteel(itemHandler.getStackInSlot(i))) {
//                ReikaInventoryHelper.decrStack(i, inv);
//                count++;
//            }
//        }
//        if (count <= 0)
//            return;
//        ReikaInventoryHelper.addOrSetStack(RotaryItems.HSLA_STEEL_INGOT.get(), count, inv, 6);
//        if (DragonAPI.rand.nextInt(25) == 0)
//            ReikaInventoryHelper.decrStack(0, inv);
//        if (DragonAPI.rand.nextInt(5) == 0)
//            ReikaInventoryHelper.decrStack(7, inv);
//    }
//
//    public int getCookScaled(int par1) {
//        return (par1 * cookTime) / this.getOperationTime();
//    }
//
//    private boolean canSmelt() {
//        if (temperature < SMELTTEMP)
//            return false;
//        if (itemHandler.getStackInSlot(0).isEmpty())
//            return false;
//        if (itemHandler.getStackInSlot(0).getItem() != Items.GUNPOWDER)
//            return false;
//        if (inv[7] == null)
//            return false;
//        if (!ReikaItemHelper.matchStackWithBlock(inv[7], Blocks.SAND.defaultBlockState()))
//            return false;
//        for (int i = 1; i < 6; i++) {
//            if (this.isModSteel(itemHandler.getStackInSlot(i)))
//                return true;
//        }
//        return false;
//    }
//
//    private boolean isModSteel(ItemStack is) {
//        if (is == null)
//            return false;
//        List<ItemStack> steel = RotaryItems.getModSteels();
//        for (ItemStack s : steel) {
//            if (is.getItem() == s.getItem() && (is.getItemDamage() == s.getItemDamage() || !s.getHasSubtypes()))
//                return true;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean isItemValidForSlot(int slot, ItemStack is) {
//        if (slot == 0)
//            return is.getItem() == Items.GUNPOWDER;
//        if (slot == 7)
//            return ReikaItemHelper.matchStackWithBlock(is, Blocks.SAND.defaultBlockState());
//        if (slot == 6)
//            return false;
//        return this.isModSteel(is);
//    }
//
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.PURIFIER;
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//        temperature = NBT.getInt("temperature");
//        cookTime = NBT.getInt("time");
//    }
//
//    @Override
//    protected String getTEName() {
//        return null;
//    }
//
//    /**
//     * Writes a tile entity to NBT.
//     */
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//        NBT.putInt("temperature", temperature);
//        NBT.putInt("time", cookTime);
//    }
//
//    public void updateTemperature(Level world, BlockPos pos) {
//        int Tamb = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);
//
//        if (RotaryAux.isNextToWater(world, pos)) {
//            Tamb /= 2;
//        }
//        Direction iceside = ReikaWorldHelper.checkForAdjBlock(world, pos, Blocks.ICE);
//        if (iceside != null) {
//            if (Tamb > 0)
//                Tamb /= 4;
//            ReikaWorldHelper.changeAdjBlock(world, pos, iceside, Fluids.FLOWING_WATER, 0);
//        }
//
//        if (RotaryAux.isNextToFire(world, pos)) {
//            Tamb += 200;
//        }
//
//        if (RotaryAux.isNextToLava(world, pos)) {
//            Tamb += 600;
//        }
//
//        if (temperature > Tamb)
//            temperature--;
//        if (temperature > Tamb * 2)
//            temperature--;
//        if (temperature < Tamb)
//            temperature++;
//        if (temperature * 2 < Tamb)
//            temperature++;
//        if (temperature > MAXTEMP) {
//            temperature = MAXTEMP;
//            this.overheat(world, pos);
//        }
//    }
//
//    public void overheat(Level world, BlockPos pos) {
//        world.setBlockToAir(pos);
//        ReikaWorldHelper.overheat(world, pos, RotaryItems.HSLA_STEEL_SCRAP.get(), 0, 7, false, 1F, false, true, 2F);
//    }
//
//    @Override
//    public int getThermalDamage() {
//        return 0;
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        if (itemHandler.getStackInSlot(0).isEmpty())
//            return 15;
//        if (itemHandler.getStackInSlot(0).getItem() != Items.GUNPOWDER)
//            return 0;
//        boolean hasModSteel = false;
//        for (int i = 1; i < 6; i++) {
//            if (this.isModSteel(itemHandler.getStackInSlot(i)))
//                hasModSteel = true;
//        }
//        if (!hasModSteel)
//            return 15;
//        if (inv[6] == null)
//            return 0;
//        if (ReikaItemHelper.matchStacks(inv[6], RotaryItems.HSLA_STEEL_INGOT))
//            return 15;
//        if (inv[6].getCount() >= RotaryItems.HSLA_STEEL_INGOT.get().getMaxStackSize())
//            return 15;
//        return 0;
//    }
//
//    @Override
//    public void addTemperature(int temp) {
//        temperature += temp;
//    }
//
//    @Override
//    public int getTemperature() {
//        return temperature;
//    }
//
//    public void setTemperature(int temp) {
//        temperature = temp;
//    }
//
//    @Override
//    public void onEMP() {
//    }
//
//    @Override
//    public int getOperationTime() {
//        return DurationRegistry.PURIFIER.getOperationTime(omega);
//    }
//
//    @Override
//    public boolean areConditionsMet() {
//        return this.canSmelt();
//    }
//
//    @Override
//    public String getOperationalStatus() {
//        return this.areConditionsMet() ? "Operational" : "Invalid or Missing Items";
//    }
//
//    @Override
//    public boolean canBeCooledWithFins() {
//        return false;
//    }
//
//    @Override
//    public boolean allowExternalHeating() {
//        return true;
//    }
//
//    @Override
//    public boolean allowHeatExtraction() {
//        return true;
//    }
//
//    @Override
//    public int getAmbientTemperature() {
//        return 0;
//    }
//
//    @Override
//    public int getMaxTemperature() {
//        return MAXTEMP;
//    }
//
//    @Override
//    public int getContainerSize() {
//        return 0;
//    }
//
//    @Override
//    public boolean isEmpty() {
//        return false;
//    }
//
//    @Override
//    public ItemStack getItem(int pIndex) {
//        return null;
//    }
//
//    @Override
//    public ItemStack removeItem(int pIndex, int pCount) {
//        return null;
//    }
//
//    @Override
//    public ItemStack removeItemNoUpdate(int pIndex) {
//        return null;
//    }
//
//    @Override
//    public void setItem(int pIndex, ItemStack pStack) {
//
//    }
//
//    @Override
//    public boolean stillValid(Player pPlayer) {
//        return false;
//    }
//
//    @Override
//    public void clearContent() {
//
//    }
//
//    @Override
//    public boolean hasAnInventory() {
//        return false;
//    }
//
//    @Override
//    public boolean hasATank() {
//        return false;
//    }
//}
