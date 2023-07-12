/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.processing;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reika.dragonapi.instantiable.data.collections.OneWayCollections.OneWaySet;
import reika.dragonapi.instantiable.HybridTank;
import reika.dragonapi.instantiable.data.KeyedItemStack;
import reika.dragonapi.instantiable.data.maps.ItemHashMap;
import reika.dragonapi.libraries.ReikaFluidHelper;
import reika.dragonapi.libraries.java.ReikaRandomHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.MachineEnchantmentHandler;
import reika.rotarycraft.auxiliary.interfaces.*;
import reika.rotarycraft.base.blockentity.BlockEntityPiping.Flow;
import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.gui.container.machine.inventory.ContainerGrinder;
import reika.rotarycraft.registry.*;

import java.util.Collection;

public class BlockEntityGrinder extends InventoriedPowerReceiver implements PipeConnector, MultiOperational, ConditionalOperation, DamagingContact, EnchantableMachine {

    public static final int MAXLUBE = 4000;
    private static final int MIN_LUBE_PRODUCTION = DifficultyEffects.CANOLA.getAverageAmount();
    private static final ItemHashMap<Float> grindableSeeds = new ItemHashMap<>();
    private static final OneWaySet<KeyedItemStack> lockedSeeds = new OneWaySet<>();

    static {
//        addGrindableSeed(RotaryItems.CANOLA.get(), 1F);
        grindableSeeds.put(RotaryItems.CANOLA_SEEDS.get(), 1F);
        lockedSeeds.add(new KeyedItemStack(RotaryItems.CANOLA_SEEDS.get()).lock());
//        addGrindableSeed(RotaryItems.CANOLA.getStackOfMetadata(2), 0.65F);
    }

    private final MachineEnchantmentHandler enchantments = new MachineEnchantmentHandler().addFilter(Enchantments.MOB_LOOTING).addFilter(Enchantments.KNOCKBACK).addFilter(Enchantments.FLAMING_ARROWS /*flaming arrows or flame aspect?!*/).addFilter(Enchantments.BLOCK_FORTUNE);
    private final HybridTank tank = new HybridTank("grinder", MAXLUBE) {
        @Override
        protected void onContentsChanged() {
            setChanged();
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid().isSame(RotaryFluids.LUBRICANT.get());
        }
    };
    private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();

    public int grinderCookTime;
    public boolean idle = false;

    @Override
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
        if (capability == ForgeCapabilities.FLUID_HANDLER)
            return lazyFluidHandler.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyFluidHandler = LazyOptional.of(() -> tank);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyFluidHandler.invalidate();
    }

    public BlockEntityGrinder(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.GRINDER.get(), pos, state);
    }

    public static void addGrindableSeed(ItemStack seed, float factor) {
        if (!lockedSeeds.contains(seed))
            grindableSeeds.put(seed, Mth.clamp(factor, 0, 0.75F));
    }

    public static boolean isGrindableSeed(ItemStack seed) {
        return grindableSeeds.containsKey(seed);
    }

    public static Collection<ItemStack> getGrindableSeeds() {
        return grindableSeeds.keySet();
    }

    public static void removeGrindableSeed(ItemStack seed) {
        if (!lockedSeeds.contains(seed))
            grindableSeeds.remove(seed);
    }

    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return i == 1;
    }

    public void testIdle() {
        idle = !this.canGrind();
    }

    public boolean getReceptor(Level world, BlockPos pos, Direction side) {
        if (pos.getY() == 0)
            return false;
        switch (side) {
            case EAST -> read = Direction.WEST;
            case WEST -> read = Direction.EAST;
            case SOUTH -> read = Direction.NORTH;
            case NORTH -> read = Direction.SOUTH;
        }
        //ReikaWorldHelper.legacySetBlockWithNotify(world, readx, ready+3, readz, 4);
        return true;
    }

    public void readPower() {
        if (!this.getReceptor(level, worldPosition, this.getBlockState().getValue(BlockRotaryCraftMachine.FACING))) {
            return;
        }
        //ReikaJavaLibrary.pConsole(readx+", "+ready+", "+readz, power > 0);
        this.getPower(false);
        //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d", ReikaMathLibrary.extrema(2, 1200-this.omega, "max")));
    }

    public int getContainerSize() {
        return 3;
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);

        grinderCookTime = NBT.getShort("CookTime");

        tank.readFromNBT(NBT);
    }

    @Override
    protected String getTEName() {
        return "Grinder";
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        NBT.putShort("CookTime", (short) grinderCookTime);

        tank.writeToNBT(NBT);
    }

    public int getCookProgressScaled(int par1) {
        //ReikaChatHelper.writeInt(this.tickcount);
        return (grinderCookTime * par1) / 2 / this.getOperationTime();
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.GRINDER.get();
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        this.testIdle();
        boolean flag1 = false;
        tickcount++;

        this.readPower();
        if (power < MINPOWER || torque < MINTORQUE) {
            grinderCookTime = 0;
            return;
        }

        int n = this.getNumberConsecutiveOperations();
        for (int i = 0; i < n; i++)
            flag1 |= this.doOperation(n > 1);

        if (flag1)
            this.setChanged();
        if (!itemHandler.getStackInSlot(2).isEmpty() && tank.getFluidLevel() >= 1000 && !world.isClientSide) {
            if (itemHandler.getStackInSlot(2).getItem() == Items.BUCKET && itemHandler.getStackInSlot(2).getCount() == 1) {
                itemHandler.setStackInSlot(2, RotaryItems.LUBE_BUCKET.get().getDefaultInstance());
                tank.removeLiquid(1000);
            }
        }
    }

    private boolean doOperation(boolean multiple) {
        if (this.canGrind()) {
            grinderCookTime++;
            if (multiple || grinderCookTime >= this.getOperationTime()) {
                grinderCookTime = 0;
                tickcount = 0;
                this.grind();
            }
            return true;
        } else {
            grinderCookTime = 0;
            return false;
        }
    }

    private boolean canGrind() {
        if (itemHandler.getStackInSlot(0).isEmpty())
            return false;

        boolean flag = false;
        if (isGrindableSeed(itemHandler.getStackInSlot(0))) {
            flag = true;
            if (tank.getRemainingSpace() < MIN_LUBE_PRODUCTION) {
                return false;
            }
        }

        ItemStack out = ItemStack.EMPTY;//RecipesGrinder.grinderRecipes.getGrindingResult(itemHandler.getStackInSlot(0));

        if (flag && out.isEmpty())
            return true;
        if (out.isEmpty())
            return false;

        if (itemHandler.getStackInSlot(1).isEmpty())
            return true;

        if (!itemHandler.getStackInSlot(1).equals(out))
            return false;

        return itemHandler.getStackInSlot(1).getCount() + out.getCount() <= Math.min(this.getInventoryStackLimit(), out.getMaxStackSize());
    }

    public int getLubricantScaled(int par1) {
        return tank.getFluidLevel() * par1 / MAXLUBE;
    }

    private void grind() {
        ItemStack is = itemHandler.getStackInSlot(0);

        if (!is.isEmpty() && isGrindableSeed(is)) {
            float num = grindableSeeds.get(is);
            tank.addLiquid((int) (DifficultyEffects.CANOLA.getInt() * this.getFortuneLubricantFactor() * num), RotaryFluids.LUBRICANT.get());
        }

        ItemStack out = ItemStack.EMPTY;//todo RecipesGrinder.grinderRecipes.getGrindingResult(is);
        if (!out.isEmpty()) {
            if (itemHandler.getStackInSlot(1).isEmpty())
                itemHandler.setStackInSlot(1, out.copy());
            else if (itemHandler.getStackInSlot(1).getItem() == out.getItem())
                itemHandler.getStackInSlot(1).setCount(out.getCount());
        }

        is.setCount(is.getCount() - 1);

        if (is.getCount() <= 0)
            itemHandler.setStackInSlot(0, ItemStack.EMPTY);
    }

    private float getFortuneLubricantFactor() {
        return 1F + (float) (enchantments.getEnchantment(Enchantments.BLOCK_FORTUNE) * ReikaRandomHelper.getRandomBetween(0.1, 0.2));
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
        if (!this.isInWorld()) {
            phi = 0;
            return;
        }
        if (power < MINPOWER || torque < MINTORQUE)
            return;
        phi += 0.85F * ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
    }

    @Override
    public void saveAdditional(CompoundTag NBT) {
        super.saveAdditional(NBT);
        NBT.putFloat("phi", phi);
    }

    @Override
    public void load(CompoundTag NBT) {
        super.load(NBT);
        phi = NBT.getFloat("phi");
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.GRINDER;
    }

    public boolean isItemValidForSlot(int slot, ItemStack is) {
        if (slot == 1)
            return false;
        if (slot == 2) {
            return is.getItem() == Items.BUCKET;
        }
        return is.getItem() == Items.BUCKET;//todo || RecipesGrinder.grinderRecipes.isGrindable(is);
    }

    @Override
    public int getRedstoneOverride() {
        if (!this.canGrind())
            return 15;
        return 0;
    }

    @Override
    public boolean canConnectToPipe(MachineRegistry m) {
        return m == MachineRegistry.HOSE || m == MachineRegistry.BEDPIPE;
    }

    @Override
    public boolean canConnectToPipeOnSide(MachineRegistry p, Direction side) {
        return side != Direction.DOWN;
    }

    @Override
    public int fillPipe(Direction from, FluidStack resource, IFluidHandler.FluidAction action) {
        return 0;
    }

    @Override
    public void onEMP() {
    }

    @Override
    public FluidStack drainPipe(Direction from, int maxDrain, IFluidHandler.FluidAction doDrain) {
        if (this.canDrain(from, null))
            return tank.drain(maxDrain, doDrain);
        return null;
    }

    public boolean canDrain(Direction from, FluidStack fluid) {
        return from != Direction.UP && ReikaFluidHelper.isFluidDrainableFromTank(fluid, tank);
    }

    public int getFluidLevel() {
        return tank.getFluidLevel();
    }

    public void setLevel(int amt) {
        tank.setContents(amt, RotaryFluids.LUBRICANT.get());
    }

    public void removeLiquid(int amt) {
        tank.removeLiquid(amt);
    }

    @Override
    public Flow getFlowForSide(Direction side) {
        return side != Direction.UP ? Flow.OUTPUT : Flow.NONE;
    }

    @Override
    public int getOperationTime() {
        return DurationRegistry.GRINDER.getOperationTime(omega);
    }

    @Override
    public int getNumberConsecutiveOperations() {
        return DurationRegistry.GRINDER.getNumberOperations(omega);
    }

    @Override
    public boolean areConditionsMet() {
        return this.canGrind();
    }

    @Override
    public String getOperationalStatus() {
        return this.areConditionsMet() ? "Operational" : "Invalid or Missing Items";
    }

    @Override
    public int getContactDamage() {
        return 3;
    }

    public boolean canDealDamage() {
        return power >= MINPOWER && torque >= MINTORQUE;
    }

    @Override
    public DamageSource getDamageType() {
        return RotaryCraft.grind;
    }

    @Override
    public MachineEnchantmentHandler getEnchantmentHandler() {
        return enchantments;
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
    public @Nullable AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
        return new ContainerGrinder(p_39954_, p_39955_, this);
    }
}