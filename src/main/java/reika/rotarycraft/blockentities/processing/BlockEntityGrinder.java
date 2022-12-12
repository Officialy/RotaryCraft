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
//import net.minecraft.util.Mth;
//import net.minecraft.world.damagesource.DamageSource;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.enchantment.Enchantment;
//import net.minecraft.world.level.Level;
//import net.minecraftforge.fluids.FluidStack;
//import net.minecraftforge.fluids.capability.IFluidHandler;
//import reika.dragonapi.instantiable.data.Collections.OneWayCollections.OneWaySet;
//import reika.dragonapi.instantiable.HybridTank;
//import reika.dragonapi.instantiable.data.KeyedItemStack;
//import reika.dragonapi.instantiable.data.maps.ItemHashMap;
//import reika.dragonapi.libraries.java.ReikaRandomHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.auxiliary.RotaryItems;
//import reika.rotarycraft.auxiliary.interfaces.*;
//import reika.rotarycraft.base.blockentity.BlockEntityPiping.Flow;
//import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
//import reika.rotarycraft.registry.DifficultyEffects;
//import reika.rotarycraft.registry.DurationRegistry;
//import reika.rotarycraft.registry.MachineRegistry;
//
//import java.util.Collection;
//
//public class BlockEntityGrinder extends InventoriedPowerReceiver implements PipeConnector, IFluidHandler, MultiOperational,
//        ConditionalOperation, DamagingContact, EnchantableMachine {
//
//    public static final int MAXLUBE = 4000;
//    private static final int MIN_LUBE_PRODUCTION = DifficultyEffects.CANOLA.getAverageAmount();
//    private static final ItemHashMap<Float> grindableSeeds = new ItemHashMap();
//    private static final OneWaySet<KeyedItemStack> lockedSeeds = new OneWaySet();
//
//    static {
//        //addGrindableSeed(RotaryItems.CANOLA.get(), 1F);
//        grindableSeeds.put(RotaryItems.canolaSeeds, 1F);
//        lockedSeeds.add(new KeyedItemStack(RotaryItems.CANOLA.get()).lock());
//        //addGrindableSeed(RotaryItems.CANOLA.getStackOfMetadata(2), 0.65F);
//    }
//
//    private final MachineEnchantmentHandler enchantments = new MachineEnchantmentHandler().addFilter(Enchantment.looting).addFilter(Enchantment.knockback).addFilter(Enchantment.flame).addFilter(Enchantment.fortune);
//    private final HybridTank tank = new HybridTank("grinder", MAXLUBE);
//    public int grinderCookTime;
//    public boolean idle = false;
//
//    public static void addGrindableSeed(ItemStack seed, float factor) {
//        if (!lockedSeeds.contains(seed))
//            grindableSeeds.put(seed, Mth.clamp_float(factor, 0, 0.75F));
//    }
//
//    public static boolean isGrindableSeed(ItemStack seed) {
//        return grindableSeeds.containsKey(seed);
//    }
//
//    public static Collection<ItemStack> getGrindableSeeds() {
//        return grindableSeeds.keySet();
//    }
//
//    public static void removeGrindableSeed(ItemStack seed) {
//        if (!lockedSeeds.contains(seed))
//            grindableSeeds.remove(seed);
//    }
//
//    @Override
//    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
//        return i == 1;
//    }
//
//    public void testIdle() {
//        idle = !this.canGrind();
//    }
//
//    public boolean getReceptor(Level world, BlockPos pos, int metadata) {
//        if (y == 0)
//            return false;
//        switch (metadata) {
//            case 0:
//                read = Direction.EAST;
//                break;
//            case 1:
//                read = Direction.WEST;
//                break;
//            case 2:
//                read = Direction.SOUTH;
//                break;
//            case 3:
//                read = Direction.NORTH;
//                break;
//        }
//        //ReikaWorldHelper.legacySetBlockWithNotify(world, readx, ready+3, readz, 4);
//        return true;
//    }
//
//    public void readPower() {
//        if (!this.getReceptor(level, xCoord, yCoord, zCoord, this.getBlockMetadata())) {
//            return;
//        }
//        //ReikaJavaLibrary.pConsole(readx+", "+ready+", "+readz, power > 0);
//        this.getPower(false);
//        //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d", ReikaMathLibrary.extrema(2, 1200-this.omega, "max")));
//    }
//
//    public int getContainerSize() {
//        return 3;
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//
//        grinderCookTime = NBT.getShort("CookTime");
//
//        tank.load(NBT);
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//        NBT.putShort("CookTime", (short) grinderCookTime);
//
//        tank.saveAdditional(NBT);
//    }
//
//    public int getCookProgressScaled(int par1) {
//        //ReikaChatHelper.writeInt(this.tickcount);
//        return (grinderCookTime * par1) / 2 / this.getOperationTime();
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        this.testIdle();
//        boolean flag1 = false;
//        tickcount++;
//
//        this.readPower();
//        if (power < MINPOWER || torque < MINTORQUE) {
//            grinderCookTime = 0;
//            return;
//        }
//
//        int n = this.getNumberConsecutiveOperations();
//        for (int i = 0; i < n; i++)
//            flag1 |= this.doOperation(n > 1);
//
//        if (flag1)
//            this.markDirty();
//        if (inv[2] != null && tank.getLevel() >= 1000 && !world.isClientSide) {
//            if (inv[2].getItem() == Items.bucket && inv[2].getCount() == 1) {
//                inv[2] = RotaryItems.lubebucket.copy();
//                tank.removeLiquid(1000);
//            }
//        }
//    }
//
//    private boolean doOperation(boolean multiple) {
//        if (this.canGrind()) {
//            grinderCookTime++;
//            if (multiple || grinderCookTime >= this.getOperationTime()) {
//                grinderCookTime = 0;
//                tickcount = 0;
//                this.grind();
//            }
//            return true;
//        } else {
//            grinderCookTime = 0;
//            return false;
//        }
//    }
//
//    private boolean canGrind() {
//        if (inv[0] == null)
//            return false;
//
//        boolean flag = false;
//        if (isGrindableSeed(inv[0])) {
//            flag = true;
//            if (tank.getRemainingSpace() < MIN_LUBE_PRODUCTION) {
//                return false;
//            }
//        }
//
//        ItemStack out = RecipesGrinder.getRecipes().getGrindingResult(inv[0]);
//
//        if (flag && out == null)
//            return true;
//        if (out == null)
//            return false;
//
//        if (inv[1] == null)
//            return true;
//
//        if (!inv[1].isItemEqual(out))
//            return false;
//
//        return inv[1].getCount() + out.getCount() <= Math.min(this.getInventoryStackLimit(), out.getMaxStackSize());
//    }
//
//    public int getLubricantScaled(int par1) {
//        return tank.getLevel() * par1 / MAXLUBE;
//    }
//
//    private void grind() {
//        ItemStack is = inv[0];
//
//        if (is != null && isGrindableSeed(is)) {
//            float num = grindableSeeds.get(is);
//            tank.addLiquid((int) (DifficultyEffects.CANOLA.getInt() * this.getFortuneLubricantFactor() * num), Fluids.getFluid("rc lubricant"));
//        }
//
//        ItemStack out = RecipesGrinder.getRecipes().getGrindingResult(is);
//        if (out != null) {
//            if (inv[1] == null)
//                inv[1] = out.copy();
//            else if (inv[1].getItem() == out.getItem())
//                inv[1].getCount() += out.getCount();
//        }
//
//        is.setCount(is.getCount() - 1);
//
//        if (is.getCount() <= 0)
//            inv[0] = null;
//    }
//
//    private float getFortuneLubricantFactor() {
//        return 1F + (float) (enchantments.getEnchantment(Enchantment.fortune) * ReikaRandomHelper.getRandomBetween(0.1, 0.2));
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
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
//        phi += 0.85F * ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
//    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.GRINDER;
//    }
//
//    @Override
//    public boolean isItemValidForSlot(int slot, ItemStack is) {
//        if (slot == 1)
//            return false;
//        if (slot == 2)
//            return is.getItem() == Items.bucket;
//        return is.getItem() == RotaryItems.CANOLA.get() || RecipesGrinder.getRecipes().isGrindable(is);
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        if (!this.canGrind())
//            return 15;
//        return 0;
//    }
//
//    @Override
//    public boolean canConnectToPipe(MachineRegistry m) {
//        return m == MachineRegistry.HOSE || m == MachineRegistry.BEDPIPE;
//    }
//
//    @Override
//    public boolean canConnectToPipeOnSide(MachineRegistry p, Direction side) {
//        return side != Direction.DOWN;
//    }
//
//    @Override
//    public void onEMP() {
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
//        if (this.canDrain(from, null))
//            return tank.drain(maxDrain, doDrain);
//        return null;
//    }
//
//    @Override
//    public boolean canFill(Direction from, Fluid fluid) {
//        return false;
//    }
//
//    @Override
//    public boolean canDrain(Direction from, Fluid fluid) {
//        return from != Direction.UP && ReikaFluidHelper.isFluidDrainableFromTank(fluid, tank);
//    }
//
//    @Override
//    public FluidTankInfo[] getTankInfo(Direction from) {
//        return new FluidTankInfo[]{tank.getInfo()};
//    }
//
//    public int getLevel() {
//        return tank.getLevel();
//    }
//
//    public void setLevel(int amt) {
//        tank.setContents(amt, Fluids.getFluid("rc lubricant"));
//    }
//
//    public void removeLiquid(int amt) {
//        tank.removeLiquid(amt);
//    }
//
//    @Override
//    public Flow getFlowForSide(Direction side) {
//        return side != Direction.UP ? Flow.OUTPUT : Flow.NONE;
//    }
//
//    @Override
//    public int getOperationTime() {
//        return DurationRegistry.GRINDER.getOperationTime(omega);
//    }
//
//    @Override
//    public int getNumberConsecutiveOperations() {
//        return DurationRegistry.GRINDER.getNumberOperations(omega);
//    }
//
//    @Override
//    public boolean areConditionsMet() {
//        return this.canGrind();
//    }
//
//    @Override
//    public String getOperationalStatus() {
//        return this.areConditionsMet() ? "Operational" : "Invalid or Missing Items";
//    }
//
//    @Override
//    public int getContactDamage() {
//        return 3;
//    }
//
//    public boolean canDealDamage() {
//        return power >= MINPOWER && torque >= MINTORQUE;
//    }
//
//    @Override
//    public DamageSource getDamageType() {
//        return RotaryCraft.grind;
//    }
//
//    @Override
//    public MachineEnchantmentHandler getEnchantmentHandler() {
//        return enchantments;
//    }
//}
