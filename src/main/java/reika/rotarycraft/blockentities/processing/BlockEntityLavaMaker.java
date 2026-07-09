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
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;

import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
import reika.rotarycraft.auxiliary.interfaces.TemperatureTE;
import reika.rotarycraft.auxiliary.recipemanagers.LavaMakerRecipe;
import reika.rotarycraft.base.blockentity.InventoriedPowerLiquidProducer;
import reika.rotarycraft.gui.container.machine.inventory.ContainerLavaMaker;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryRecipeTypes;

/**
 * Rock Melter: friction-heats itself from shaft power and, once hot enough, melts stone/cobblestone/
 * netherrack (and other {@link LavaMakerRecipe} inputs) into lava, banking shaft work as {@code energy}
 * until a batch's {@code meltEnergy} is met. Port of the legacy {@code TileEntityLavaMaker}; the
 * mod-heavy environmental temperature bonuses (adjacent lava/ice/water) are dropped for a clean
 * friction-heat + ambient-decay model.
 */
public class BlockEntityLavaMaker extends InventoriedPowerLiquidProducer implements TemperatureTE, ConditionalOperation {

    public static final int CAPACITY = 8000;
    public static final int MAXTEMP = 2000;
    private static final int AMBIENT = 20;

    private int temperature = AMBIENT;
    private long energy;

    public BlockEntityLavaMaker(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.LAVAMAKER.get(), pos, state);
    }

    @Override
    public int getContainerSize() {
        return 9;
    }

    @Override
    public int getCapacity() {
        return CAPACITY;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.LAVAMAKER;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.LAVAMAKER.get();
    }

    @Override
    protected String getTEName() {
        return "lavamaker";
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public boolean canOutputTo(Direction to) {
        return true;
    }

    @Override
    public boolean canConnectToPipe(MachineRegistry m) {
        return m.isStandardPipe() || m == MachineRegistry.FUELLINE;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack is) {
        return this.getRecipe(is) != null;
    }

    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return false;
    }

    /** Exposes the feed inventory for the container (base handler is protected). */
    public reika.dragonapi.instantiable.storage.ManagedItemHandler getFeedHandler() {
        return itemHandler;
    }

    // Producer: drainable from any side (outputs the melted fluid to a pipe).
    @Override
    public FluidStack drainPipe(Direction from, int maxDrain, net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction action) {
        return tank.drain(maxDrain, action);
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
        if (!this.isInWorld()) {
            phi = 0;
            return;
        }
        phi += (float) ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        this.getPowerBelow();

        if (tickcount % 20 == 0)
            this.updateTemperature(world, pos);

        int meltTemp = this.getMeltTemperature();
        if (temperature >= meltTemp)
            energy += power;
        else
            energy = (long) (energy * 0.85);
        tickcount++;

        if (world.isClientSide())
            return;
        for (int i = 0; i < this.getContainerSize(); i++) {
            LavaMakerRecipe r = this.getRecipe(itemHandler.getStackInSlot(i));
            if (r == null)
                continue;
            FluidStack fs = r.getFluid();
            if (this.canMake(fs) && energy >= r.getMeltEnergy() * 20L) {
                tank.addLiquid(fs.getAmount(), fs.getFluid());
                itemHandler.extractItem(i, 1, false);
                energy -= r.getMeltEnergy() * 20L;
                this.setChanged();
                return;
            }
        }
    }

    private LavaMakerRecipe getRecipe(ItemStack in) {
        if (in.isEmpty() || level == null || level.getServer() == null)
            return null;
        return level.getServer().getRecipeManager()
                .getRecipeFor(RotaryRecipeTypes.LAVA_MAKER.get(), new SingleRecipeInput(in), level)
                .map(h -> h.value()).orElse(null);
    }

    /** Lowest melting temperature among the current inputs (what the machine must reach to melt). */
    private int getMeltTemperature() {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < this.getContainerSize(); i++) {
            LavaMakerRecipe r = this.getRecipe(itemHandler.getStackInSlot(i));
            if (r != null)
                min = Math.min(min, r.getMeltTemperature());
        }
        return min;
    }

    private boolean canMake(FluidStack liq) {
        if (tank.isEmpty())
            return true;
        if (!tank.getActualFluid().getFluid().isSame(liq.getFluid()))
            return false;
        return tank.getFluidLevel() + liq.getAmount() <= tank.getCapacity();
    }

    // --- TemperatureTE (friction heat + ambient decay) ----------------------

    @Override
    public void updateTemperature(Level world, BlockPos pos) {
        if (power > 0 && omega > 0) {
            temperature += (int) Math.max(1, ReikaMathLibrary.logbase(torque + 1, 2));
        } else if (temperature > AMBIENT) {
            temperature -= Math.max(1, (temperature - AMBIENT) / 40);
        }
        if (temperature > MAXTEMP) {
            temperature = MAXTEMP;
            this.overheat(world, pos);
        }
    }

    @Override
    public void addTemperature(int temp) {
        temperature += temp;
    }

    @Override
    public int getTemperature() {
        return temperature;
    }

    @Override
    public void setTemperature(int T) {
        temperature = T;
    }

    @Override
    public int getThermalDamage() {
        return temperature >= 600 ? 2 : 0;
    }

    @Override
    public int getMaxTemperature() {
        return MAXTEMP;
    }

    @Override
    public int getAmbientTemperature() {
        return AMBIENT;
    }

    @Override
    public boolean canBeCooledWithFins() {
        return true;
    }

    @Override
    public boolean allowExternalHeating() {
        return true;
    }

    @Override
    public boolean allowHeatExtraction() {
        return false;
    }

    @Override
    public void overheat(Level world, BlockPos pos) {
        // Rock melter has no destructive overheat — it just caps out (legacy behaviour).
    }

    public int getTemperatureScaled(int a) {
        return a * temperature / MAXTEMP;
    }

    // --- GUI / status --------------------------------------------------------

    @Override
    public boolean areConditionsMet() {
        return this.getMeltTemperature() != Integer.MAX_VALUE;
    }

    @Override
    public String getOperationalStatus() {
        return this.areConditionsMet() ? "Operational" : "No Meltable Items";
    }

    @Override
    public int getRedstoneOverride() {
        return tank.isFull() ? 15 : 0;
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        temperature = NBT.getIntOr("temp", AMBIENT);
        energy = NBT.getLongOr("energy", 0L);
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        NBT.putInt("temp", temperature);
        NBT.putLong("energy", energy);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new ContainerLavaMaker(id, inv, this);
    }
}
