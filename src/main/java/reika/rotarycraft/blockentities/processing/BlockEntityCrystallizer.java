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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import reika.dragonapi.instantiable.StepTimer;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
import reika.rotarycraft.auxiliary.interfaces.MultiOperational;
import reika.rotarycraft.auxiliary.recipemanagers.CrystallizerRecipe;
import reika.rotarycraft.base.blockentity.InventoriedPowerLiquidReceiver;
import reika.rotarycraft.registry.DurationRegistry;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryItems;

/**
 * Crystallizer: freezes the tank fluid into a solid (water -> ice, lava -> stone, ethanol ->
 * ethanol crystals) -- but only while the machine is chilled below the fluid's freezing point
 * (-273 + 0.9x the fluid temperature in K, so ~-3C for water). Cooling comes from the environment
 * (adjacent snow/water/ice) and dry ice fed into slot 1 (-40, consumed); output collects in slot 0.
 *
 * <p>26.2 port notes: recipes are the data-driven {@link CrystallizerRecipe} (legacy
 * RecipesCrystallizer; the ender/redstone mod-fluid entries and Thermal cryotheum coolant are
 * gated out). The legacy biome-ambient lookup is a flat 20C ambient with the same adjacency
 * bonuses. Implements the freezing side of TemperatureTE informally (no overheat).</p>
 */
public class BlockEntityCrystallizer extends InventoriedPowerLiquidReceiver implements MultiOperational, ConditionalOperation {

    public static final int CAPACITY = 8000;
    private static final int AMBIENT = 20;

    private final StepTimer timer = new StepTimer(400);
    private final StepTimer tempTimer = new StepTimer(20);

    private int temperature = AMBIENT;
    public int freezeTick;

    public BlockEntityCrystallizer(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.CRYSTALLIZER.get(), pos, state);
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        this.getPower(false);

        timer.setCap(this.getOperationTime());
        tempTimer.update();
        if (tempTimer.checkCap())
            this.updateTemperature(world, pos);

        if (!world.isClientSide()) {
            int n = this.getNumberConsecutiveOperations();
            for (int i = 0; i < n; i++)
                this.doOperation(n > 1);
            freezeTick = timer.getTick();
        }
    }

    private void doOperation(boolean multiple) {
        CrystallizerRecipe r = tank.isEmpty() ? null : this.getRecipe(tank.getActualFluid().getFluid());
        if (r != null && this.canOperate(r)) {
            timer.update();
            if (multiple || timer.checkCap())
                this.make(r);
        }
        else {
            timer.reset();
        }
    }

    private CrystallizerRecipe getRecipe(Fluid f) {
        if (level == null || level.getServer() == null)
            return null;
        for (var h : level.getServer().getRecipeManager().recipeMap().byType(reika.rotarycraft.registry.RotaryRecipeTypes.CRYSTALLIZER.get())) {
            if (h.value().matchesFluid(f))
                return h.value();
        }
        return null;
    }

    private void make(CrystallizerRecipe r) {
        ItemStack toMake = r.getResult();
        ItemStack slot = itemHandler.getStackInSlot(0);
        if (slot.isEmpty())
            itemHandler.setStackInSlot(0, toMake);
        else
            slot.grow(toMake.getCount());
        tank.removeLiquid(r.getConsumption());
        this.setChanged();
    }

    private boolean canOperate(CrystallizerRecipe r) {
        if (power < MINPOWER || omega < MINSPEED)
            return false;
        if (tank.getFluidLevel() < r.getConsumption())
            return false;
        if (temperature > this.getFreezingPoint())
            return false;
        ItemStack toMake = r.getResult();
        ItemStack slot = itemHandler.getStackInSlot(0);
        if (slot.isEmpty())
            return true;
        return ItemStack.isSameItemSameComponents(slot, toMake)
                && slot.getCount() + toMake.getCount() <= Math.min(this.getInventoryStackLimit(), slot.getMaxStackSize());
    }

    /** The freezing point the machine must be chilled below (legacy formula from the fluid temp). */
    public int getFreezingPoint() {
        if (tank.isEmpty())
            return 0;
        FluidStack fs = tank.getFluid();
        return -273 + (int) (0.9 * fs.getFluid().getFluidType().getTemperature(fs));
    }

    /** Environment + dry-ice cooling toward a (possibly sub-zero) local ambient (legacy model). */
    public void updateTemperature(Level world, BlockPos pos) {
        int Tamb = AMBIENT;
        boolean snow = false, water = false, ice = false;
        for (Direction d : Direction.values()) {
            BlockState s = world.getBlockState(pos.relative(d));
            if (s.is(Blocks.SNOW) || s.is(Blocks.SNOW_BLOCK))
                snow = true;
            if (s.is(Blocks.WATER))
                water = true;
            if (s.is(Blocks.ICE) || s.is(Blocks.PACKED_ICE) || s.is(Blocks.BLUE_ICE))
                ice = true;
        }
        if (snow)
            Tamb -= 5;
        if (water)
            Tamb -= 15;
        if (ice)
            Tamb -= 30;
        ItemStack coolant = itemHandler.getStackInSlot(1);
        if (!coolant.isEmpty() && coolant.getItem() == RotaryItems.DRY_ICE.get()) {
            Tamb -= 40;
            if (temperature > Tamb + 4 || rand.nextInt(20) == 0)
                itemHandler.extractItem(1, 1, false);
        }
        int dT = Tamb - temperature;
        temperature += Math.abs(dT) < 4 ? (int) Math.signum(dT) : dT / 4;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getProgressScaled(int s) {
        return timer.getCap() > 0 ? s * freezeTick / timer.getCap() : 0;
    }

    public int getLiquidScaled(int s) {
        return s * tank.getFluidLevel() / this.getCapacity();
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return i == 0;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack is) {
        return slot == 1 && is.getItem() == RotaryItems.DRY_ICE.get();
    }

    /** Exposes the inventory for the container. */
    public reika.dragonapi.instantiable.storage.ManagedItemHandler getCrystallizerHandler() {
        return itemHandler;
    }

    @Override
    public boolean canConnectToPipe(MachineRegistry m) {
        return true;
    }

    // Multi-fluid receiver: any fluid a freezing recipe uses is acceptable.
    @Override
    public Fluid getInputFluid() {
        return null;
    }

    @Override
    public boolean isValidFluid(Fluid f) {
        return this.getRecipe(f) != null;
    }

    @Override
    public boolean canReceiveFrom(Direction from) {
        return true;
    }

    @Override
    public FluidStack drainPipe(Direction from, int maxDrain, IFluidHandler.FluidAction action) {
        return FluidStack.EMPTY;
    }

    @Override
    public int fillPipe(Direction from, FluidStack resource, IFluidHandler.FluidAction action) {
        if (!this.canReceiveFrom(from) || !this.isValidFluid(resource.getFluid()))
            return 0;
        return tank.fill(resource, action);
    }

    @Override
    public int getCapacity() {
        return CAPACITY;
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
        if (!this.isInWorld()) {
            phi = 0;
            return;
        }
        if (power < MINPOWER || omega < MINSPEED)
            return;
        phi += (float) ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.CRYSTALLIZER;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.CRYSTALLIZER.get();
    }

    @Override
    protected String getTEName() {
        return "crystallizer";
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        temperature = NBT.getIntOr("temp", AMBIENT);
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        NBT.putInt("temp", temperature);
    }

    @Override
    public int getOperationTime() {
        return DurationRegistry.CRYSTALLIZER.getOperationTime(Math.max(0, omega - MINSPEED));
    }

    @Override
    public int getNumberConsecutiveOperations() {
        return DurationRegistry.CRYSTALLIZER.getNumberOperations(Math.max(0, omega - MINSPEED));
    }

    @Override
    public boolean areConditionsMet() {
        return !tank.isEmpty();
    }

    @Override
    public String getOperationalStatus() {
        return this.areConditionsMet() ? "Operational" : "No Liquid";
    }

}
