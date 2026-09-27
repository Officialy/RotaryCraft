/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.production;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import reika.dragonapi.instantiable.data.WeightedRandom;
import reika.dragonapi.instantiable.math.MovingAverage;
import reika.dragonapi.instantiable.storage.ManagedItemHandler;
import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.dragonapi.libraries.java.ReikaRandomHelper;
import reika.rotarycraft.base.blockentity.PoweredLiquidIO;
import reika.rotarycraft.auxiliary.recipemanagers.FractionatorRecipe;
import reika.rotarycraft.gui.container.machine.inventory.ContainerFractionator;
import reika.rotarycraft.registry.DifficultyEffects;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryFluids;
import reika.rotarycraft.registry.RotaryRecipeTypes;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Converts ethanol and six ingredients into jet fuel. A ghast tear is required as a solvent,
 * while the ingredient consumption and fuel yield follow the legacy weighted and pressure models.
 */
public class BlockEntityFractionator extends PoweredLiquidIO implements Container {

    public static final int CAPACITY = 240000;
    public static final int OPERATION_TIME = 80;
    public static final int FUEL_PER_OP    = 1000;
    public static final int ETHANOL_PER_OP = 1000;
    public static final long MIN_POWER     = 1024L;
    public static final int  MIN_SPEED     = 32;
    public static final int  MIN_TORQUE    = 32;

    private static final int SLOTS = 7;

    /** Inventory backing for the GUI's 7 slots. Public for {@link ContainerFractionator}. */
    public ManagedItemHandler itemHandler = new ManagedItemHandler(SLOTS) {
        @Override
        protected void onContentsChanged(int slot) { setChanged(); }
    };

    public int mixTime;

    /**
     * Internal pressure tracker (legacy 1.7 mapped 0–1000 to ~0.1–10 atm). Builds up while the
     * machine has torque input, decays toward ambient otherwise. Affects {@link #getYieldRatio}.
     */
    private int pressure;
    private MovingAverage torqueInput = new MovingAverage(20);

    /**
     * Maximum pressure before overpressure failure (kPa-ish, scaled units). 1000 ≈ 10 atm.
     */
    public static final int MAX_PRESSURE = 1000;

    /**
     * 7-point yield curve from the legacy implementation — input is pressure, output is the
     * jet-fuel multiplier at the end of a successful cycle. See the original
     * TileEntityFractionator constructor for the same break-points.
     */
    private static final float[][] YIELD_POINTS = {
            {0,    0.01F},
            {100,  0.05F},
            {180,  0.10F},
            {500,  0.40F},
            {720,  1.00F},
            {850,  1.50F},
            {1000, 2.50F}
    };

    public float getYieldRatio() {
        // Linear interpolation across the 7-point yield curve.
        for (int i = 1; i < YIELD_POINTS.length; i++) {
            if (pressure <= YIELD_POINTS[i][0]) {
                float p0 = YIELD_POINTS[i - 1][0], p1 = YIELD_POINTS[i][0];
                float y0 = YIELD_POINTS[i - 1][1], y1 = YIELD_POINTS[i][1];
                float t = p1 == p0 ? 0 : (pressure - p0) / (p1 - p0);
                return y0 + t * (y1 - y0);
            }
        }
        return YIELD_POINTS[YIELD_POINTS.length - 1][1];
    }

    public int getPressure() { return pressure; }
    public int getPressureScaled(int p) { return pressure * p / MAX_PRESSURE; }

    public BlockEntityFractionator(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.FRACTIONATOR.get(), pos, state);
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.FRACTIONATOR.get();
    }

    @Override
    public int getCapacity() {
        return CAPACITY;
    }

    @Override
    public int getInputCapacity() {
        return 16000;
    }

    @Override
    public Fluid getInputFluid() {
        FractionatorRecipe recipe = this.getConfiguredRecipe();
        return recipe == null ? Fluids.EMPTY : recipe.getInputFluid();
    }

    @Override
    public boolean canReceiveFrom(Direction from) {
        return from.getStepY() == 0;
    }

    @Override
    public boolean canOutputTo(Direction to) {
        return to == Direction.UP;
    }

    @Override
    public boolean canConnectToPipe(MachineRegistry m) {
        return m.isStandardPipe() || m == MachineRegistry.HOSE || m == MachineRegistry.FUELLINE;
    }

    @Override
    public boolean canIntakeFromPipe(MachineRegistry p) {
        return canConnectToPipe(p);
    }

    @Override
    public boolean canOutputToPipe(MachineRegistry p) {
        return canConnectToPipe(p);
    }

    @Override
    public boolean isValidFluid(Fluid f) {
        return f != null && f.equals(getInputFluid());
    }

    public boolean isItemValidForSlot(int slot, ItemStack is) {
        if (is.isEmpty()) return false;
        FractionatorRecipe recipe = this.getConfiguredRecipe();
        if (recipe == null) return false;
        if (slot == 6) return recipe.getSolvent().test(is);
        return slot >= 0 && slot < 6 && recipe.getIngredients().stream()
                .anyMatch(entry -> entry.ingredient().test(is));
    }

    public boolean hasAnInventory() { return true; }
    public boolean hasATank() { return true; }

    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        this.getPowerBelow();
        power = (long) omega * (long) torque;
        torqueInput.addValue(torque);

        if (world.isClientSide()) return;

        tickcount++;
        // The original machine samples a 20-tick torque average before each pressure update.
        if ((tickcount % 20) == 0) updatePressure();

        if (power < MIN_POWER || omega < MIN_SPEED || torque < MIN_TORQUE) {
            mixTime = 0;
            return;
        }
        FractionatorRecipe recipe = this.getRecipe();
        if (recipe == null || !canRunRecipe(recipe)) {
            mixTime = 0;
            return;
        }
        mixTime++;
        if (mixTime >= OPERATION_TIME) {
            mixTime = 0;
            runRecipe(recipe);
            setChanged();
        }
    }

    /** The original 20-tick moving-average pressure model; overpressure caps at 1000. */
    private void updatePressure() {
        int local = pressure;
        int ambient = 100; // ~1 atm in legacy units
        int dp = local - ambient;
        int sub = (int) (Math.signum(dp) * Math.max(1, Math.abs(dp / 16)));
        int averageTorque = (int) torqueInput.getAverage();
        if (averageTorque <= 0) sub *= 8;

        local -= sub;
        if (averageTorque > 0) local += (int) (1.8 * Math.sqrt(averageTorque));

        if (local > MAX_PRESSURE) local = MAX_PRESSURE;
        if (pressure < local) {
            pressure += Math.max(1, Math.min(ReikaRandomHelper.getRandomPlusMinus(6, 13), (local - pressure) / 4));
        } else {
            pressure = local;
        }
    }

    private FractionatorRecipe getConfiguredRecipe() {
        if (level == null || level.getServer() == null) return null;
        return level.getServer().getRecipeManager().recipeMap()
                .byType(RotaryRecipeTypes.FRACTIONATOR.get()).stream()
                .map(holder -> holder.value()).findFirst().orElse(null);
    }

    private FractionatorRecipe getRecipe() {
        if (level == null || level.getServer() == null) return null;
        ArrayList<ItemStack> solids = new ArrayList<>(6);
        for (int i = 0; i < 6; i++) solids.add(itemHandler.getStackInSlot(i));
        FractionatorRecipe.FractionatorInput recipeInput = new FractionatorRecipe.FractionatorInput(
                solids, itemHandler.getStackInSlot(6), input.getFluid());
        return level.getServer().getRecipeManager()
                .getRecipeFor(RotaryRecipeTypes.FRACTIONATOR.get(), recipeInput, level)
                .map(holder -> holder.value()).orElse(null);
    }

    private boolean canRunRecipe(FractionatorRecipe recipe) {
        // Output-space check uses the upper-bound legacy PRODUCEFRAC ceiling so the cycle never
        // fires when the output tank would overflow.
        if (output.getFluidLevel() + (int) Math.ceil(DifficultyEffects.PRODUCEFRAC.getMaxAmount() * 2.5) > CAPACITY)
            return false;
        // Input-fluid check tracks the per-difficulty actual cost so easy-mode players (who pay
        // ~31 mB ethanol per cycle) aren't gated on a full litre being present.
        int ethanolCost = (int) Math.max(1, recipe.getInputAmount()
                * DifficultyEffects.CONSUMEFRAC.getChance());
        if (input.getFluidLevel() < ethanolCost) return false;
        return true;
    }

    private void runRecipe(FractionatorRecipe recipe) {
        // Legacy 1.7 ethanol consumption was {@code 1000 mB × CONSUMEFRAC.getChance()} (0.03 / 0.25
        // / 0.75 for easy / medium / hard), so a medium-difficulty cycle uses only ~250 mB
        // ethanol instead of a flat litre. Match that here so the input tank doesn't drain
        // unrealistically fast and easy-mode players actually get the lighter cost they expect.
        float consumeFrac = DifficultyEffects.CONSUMEFRAC.getChance();
        int ethanolCost = (int) Math.max(1, recipe.getInputAmount() * consumeFrac);
        input.removeLiquid(ethanolCost);
        // Yield = legacy 7-point pressure curve × per-difficulty PRODUCEFRAC roll. The legacy
        // PRODUCEFRAC is a random range (e.g. 1000..2200 mB on medium); {@code getInt()} picks
        // a value within that range each cycle.
        int produceBase = DifficultyEffects.PRODUCEFRAC.getInt();
        int produced = (int) (produceBase * getYieldRatio());
        output.addLiquid(Math.max(1, produced), recipe.getOutputFluid());
        consumeIngredientsWeighted(recipe);
        // The ghast tear is a solvent requirement, not a consumed ingredient in the 1.7 machine.
    }

    /**
     * Legacy 1.7 consumption model: pick a number of ingredient slots equal to the count of
     * ingredients (i.e. 6 with default difficulty) weighted by the per-ingredient yield weight.
     * Each picked slot has one item consumed. Higher-weighted ingredients (e.g. netherrack dust
     * weight=2 vs. pink dye weight=0.5) are statistically more likely to be drained, so a player
     * targeting consistency stocks the heavier-weight slots harder than the light-weight ones.
     *
     * <p>Fractional consume budgets (e.g. 5.6 ingredients with reduced difficulty) round their
     * trailing fractional pick by probability — matches the legacy
     * {@code doWithChance(consume - floor)} behaviour.</p>
     */
    private void consumeIngredientsWeighted(FractionatorRecipe recipe) {
        // Legacy: {@code consume = ingredients.size() × CONSUMEFRAC.getChance()}.
        // On medium difficulty: 6 × 0.25 = 1.5 → typically one slot consumed each cycle, with
        // ~50% chance of a second one (fractional-budget probability check below).
        float consume = recipe.getIngredients().size()
                * DifficultyEffects.CONSUMEFRAC.getChance();

        WeightedRandom<Integer> wr = new WeightedRandom<>();
        for (int i = 0; i < 6; i++) {
            ItemStack is = itemHandler.getStackInSlot(i);
            if (is.isEmpty()) continue;
            float weight = recipe.weightFor(is);
            if (weight <= 0) continue;
            wr.addEntry(i, weight);
        }

        while (consume > 0) {
            Integer slot = wr.getRandomEntry();
            if (slot == null) break;            // ran out of weighted entries
            boolean fire = consume >= 1
                    || ReikaRandomHelper.doWithChance(consume);
            if (fire) {
                ItemStack is = itemHandler.getStackInSlot(slot);
                if (!is.isEmpty()) {
                    is.shrink(1);
                    itemHandler.setStackInSlot(slot, is);
                }
            }
            wr.remove(slot);                    // each slot only picked once per cycle
            consume -= 1;
        }
    }

    public int getFuelScaled(int p)    { return output.getFluidLevel() * p / CAPACITY; }
    public int getEthanolScaled(int p) { return input.getFluidLevel() * p / getInputCapacity(); }
    public int getMixScaled(int p)     { return mixTime * p / OPERATION_TIME; }

    @Override
    protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag);
        tag.putInt("mix", mixTime);
        tag.putInt("press", pressure);
        CompoundTag average = new CompoundTag();
        torqueInput.saveAdditional(average);
        tag.put("torqueAverage", average);
    }

    @Override
    protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag);
        mixTime = tag.getIntOr("mix", 0);
        pressure = tag.getIntOr("press", 0);
        if (tag.contains("torqueAverage"))
            torqueInput = MovingAverage.load(tag.getCompoundOrEmpty("torqueAverage"));
    }

    @Override
    protected String getTEName() { return "fractionator"; }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.FRACTIONATOR;
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) { /* no animated parts */ }

    @Override
    public int getRedstoneOverride() {
        return (int) (15.0 * getYieldRatio() / YIELD_POINTS[YIELD_POINTS.length - 1][1]);
    }

    @Override
    public void onEMP() {}

    @Override
    public boolean hasModelTransparency() { return false; }


    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new ContainerFractionator(id, inv, this);
    }

    // 26.1: NBT save/load using ValueOutput/ValueInput so the inventory survives chunk reload.
    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        TagValueOutput nested = TagValueOutput.createWithContext(
                ProblemReporter.DISCARDING,
                this.level == null ? RegistryAccess.EMPTY : this.level.registryAccess());
        itemHandler.serialize(nested);
        output.store("ItemsRaw", CompoundTag.CODEC, nested.buildResult());
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        itemHandler = new ManagedItemHandler(SLOTS) {
            @Override
            protected void onContentsChanged(int slot) { setChanged(); }
        };
        Optional<CompoundTag> raw = input.read("ItemsRaw", CompoundTag.CODEC);
        if (raw.isPresent()) {
            ValueInput nested = TagValueInput.create(
                    ProblemReporter.DISCARDING,
                    this.level == null ? RegistryAccess.EMPTY : this.level.registryAccess(),
                    raw.get());
            itemHandler.deserialize(nested);
        }
    }

    // --- Container interface ------------------------------------------------------------------
    @Override public int getContainerSize() { return SLOTS; }
    @Override public boolean isEmpty() {
        for (int i = 0; i < SLOTS; i++) if (!itemHandler.getStackInSlot(i).isEmpty()) return false;
        return true;
    }
    @Override public ItemStack getItem(int slot) { return itemHandler.getStackInSlot(slot); }
    @Override public ItemStack removeItem(int slot, int count) { return ReikaInventoryHelper.decrStackSize(itemHandler, slot, count); }
    @Override public ItemStack removeItemNoUpdate(int slot) {
        ItemStack is = itemHandler.getStackInSlot(slot);
        itemHandler.setStackInSlot(slot, ItemStack.EMPTY);
        return is;
    }
    @Override public void setItem(int slot, ItemStack stack) { itemHandler.setStackInSlot(slot, stack); }
    @Override public boolean stillValid(Player player) { return !this.isRemoved(); }
    @Override public void clearContent() { for (int i = 0; i < SLOTS; i++) itemHandler.setStackInSlot(i, ItemStack.EMPTY); }
}
