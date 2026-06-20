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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import reika.dragonapi.instantiable.storage.ManagedItemHandler;
import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.rotarycraft.base.blockentity.PoweredLiquidIO;
import reika.rotarycraft.gui.container.machine.inventory.ContainerFractionator;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryFluids;
import reika.rotarycraft.registry.RotaryItems;

import java.util.LinkedHashMap;

/**
 * 26.1 port of the 1.7 Fractionator. Converts liquid ethanol + a fixed set of ingredient
 * items + a ghast tear catalyst into liquid jet fuel. Simpler than the legacy implementation
 * (no pressure or yield curve) — fires whenever all six ingredients are present, the
 * ghast-tear catalyst is filled, and the input tank holds ≥1000 mB of ethanol; consumes one
 * of each ingredient + 1000 mB ethanol per cycle and emits 1000 mB jet fuel.
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

    private static final LinkedHashMap<Item, Float> INGREDIENTS = new LinkedHashMap<>();

    public static void registerIngredients() {
        if (!INGREDIENTS.isEmpty()) return;
        INGREDIENTS.put(Items.BLAZE_POWDER, 1.5F);
        INGREDIENTS.put(RotaryItems.COAL_DUST.get(), 1F);
        INGREDIENTS.put(Items.MAGMA_CREAM, 0.75F);
        INGREDIENTS.put(Items.DYE.pink(), 0.5F);
        INGREDIENTS.put(RotaryItems.NETHERRACK_DUST.get(), 2F);
        INGREDIENTS.put(RotaryItems.TAR.get(), 1.5F);
    }

    public static boolean isIngredient(ItemStack is) {
        registerIngredients();
        return !is.isEmpty() && INGREDIENTS.containsKey(is.getItem());
    }

    public static Item ingredientForSlot(int slot) {
        registerIngredients();
        int i = 0;
        for (Item k : INGREDIENTS.keySet()) {
            if (i++ == slot) return k;
        }
        return null;
    }

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
        registerIngredients();
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
        return RotaryFluids.ETHANOL.get();
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
        if (slot == 6) return is.getItem() == Items.GHAST_TEAR;
        Item needed = ingredientForSlot(slot);
        return needed != null && is.getItem() == needed;
    }

    public boolean hasAnInventory() { return true; }
    public boolean hasATank() { return true; }

    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        this.getPowerBelow();
        power = (long) omega * (long) torque;

        if (world.isClientSide()) return;

        tickcount++;
        // Pressure dynamics every 20 ticks (~1 s). Builds up while the machine has torque,
        // bleeds off toward ambient otherwise. Triggers overpressure failure at the cap.
        if ((tickcount % 20) == 0) updatePressure();

        if (power < MIN_POWER || omega < MIN_SPEED || torque < MIN_TORQUE) {
            mixTime = 0;
            return;
        }
        if (!canRunRecipe()) {
            mixTime = 0;
            return;
        }
        mixTime++;
        if (mixTime >= OPERATION_TIME) {
            mixTime = 0;
            runRecipe();
            setChanged();
        }
    }

    /**
     * Simplified port of the legacy pressure model. The 1.7 BE used a {@code MovingAverage} of
     * torque to drive both rise and decay; we use the current torque directly. Behaviourally:
     *   - {@code torque > 0} → pressure rises by ~√torque per cycle
     *   - {@code torque ≤ 0} → pressure decays toward ambient (much faster)
     *   - pressure > MAX_PRESSURE → triggers overpressure (set to AIR + small effect)
     */
    private void updatePressure() {
        int local = pressure;
        int ambient = 100; // ~1 atm in legacy units
        int dp = local - ambient;
        int sub = (int) (Math.signum(dp) * Math.max(1, Math.abs(dp / 16)));
        if (torque <= 0) sub *= 8;

        local -= sub;
        if (torque > 0) local += (int) (1.8 * Math.sqrt(torque));

        if (local > MAX_PRESSURE) {
            overpressure();
            local = MAX_PRESSURE;
        }
        if (pressure < local) {
            pressure += Math.max(1, Math.min(10, (local - pressure) / 4));
        } else {
            pressure = local;
        }
    }

    private void overpressure() {
        // Simple overpressure: drop a small amount of jet fuel to the world (legacy explosively
        // destroyed the BE). Keep the user's machine alive so they can recover from a runaway.
        // The legacy explosion behaviour can be restored once block-explosion drops are wired.
        if (level != null && !level.isClientSide()) {
            level.playSound(null, worldPosition,
                    net.minecraft.sounds.SoundEvents.GENERIC_EXPLODE.value(),
                    net.minecraft.sounds.SoundSource.BLOCKS, 0.6F, 1.5F);
            output.removeLiquid(Math.min(2000, output.getFluidLevel()));
        }
    }

    private boolean canRunRecipe() {
        // Output-space check uses the upper-bound legacy PRODUCEFRAC ceiling so the cycle never
        // fires when the output tank would overflow.
        if (output.getFluidLevel() + reika.rotarycraft.registry.DifficultyEffects.PRODUCEFRAC.getMaxAmount() > CAPACITY)
            return false;
        // Input-fluid check tracks the per-difficulty actual cost so easy-mode players (who pay
        // ~31 mB ethanol per cycle) aren't gated on a full litre being present.
        int ethanolCost = (int) Math.max(1, ETHANOL_PER_OP
                * reika.rotarycraft.registry.DifficultyEffects.CONSUMEFRAC.getChance());
        if (input.getFluidLevel() < ethanolCost) return false;
        if (itemHandler.getStackInSlot(6).getItem() != Items.GHAST_TEAR) return false;
        for (int i = 0; i < 6; i++) {
            if (itemHandler.getStackInSlot(i).isEmpty()) return false;
            Item required = ingredientForSlot(i);
            if (required == null || itemHandler.getStackInSlot(i).getItem() != required) return false;
        }
        return true;
    }

    private void runRecipe() {
        // Legacy 1.7 ethanol consumption was {@code 1000 mB × CONSUMEFRAC.getChance()} (0.03 / 0.25
        // / 0.75 for easy / medium / hard), so a medium-difficulty cycle uses only ~250 mB
        // ethanol instead of a flat litre. Match that here so the input tank doesn't drain
        // unrealistically fast and easy-mode players actually get the lighter cost they expect.
        float consumeFrac = reika.rotarycraft.registry.DifficultyEffects.CONSUMEFRAC.getChance();
        int ethanolCost = (int) Math.max(1, ETHANOL_PER_OP * consumeFrac);
        input.removeLiquid(ethanolCost);
        // Yield = legacy 7-point pressure curve × per-difficulty PRODUCEFRAC roll. The legacy
        // PRODUCEFRAC is a random range (e.g. 1000..2200 mB on medium); {@code getInt()} picks
        // a value within that range each cycle.
        int produceBase = reika.rotarycraft.registry.DifficultyEffects.PRODUCEFRAC.getInt();
        int produced = (int) (produceBase * getYieldRatio());
        output.addLiquid(Math.max(1, produced), RotaryFluids.JET_FUEL.get());
        consumeIngredientsWeighted();
        // Ghast tear is a catalyst — consumed every 4 cycles only.
        if ((mixTime & 3) == 0) {
            ItemStack tear = itemHandler.getStackInSlot(6);
            tear.shrink(1);
            itemHandler.setStackInSlot(6, tear);
        }
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
    private void consumeIngredientsWeighted() {
        // Legacy: {@code consume = ingredients.size() × CONSUMEFRAC.getChance()}.
        // On medium difficulty: 6 × 0.25 = 1.5 → typically one slot consumed each cycle, with
        // ~50% chance of a second one (fractional-budget probability check below).
        float consume = INGREDIENTS.size()
                * reika.rotarycraft.registry.DifficultyEffects.CONSUMEFRAC.getChance();

        reika.dragonapi.instantiable.data.WeightedRandom<Integer> wr = new reika.dragonapi.instantiable.data.WeightedRandom<>();
        for (int i = 0; i < 6; i++) {
            ItemStack is = itemHandler.getStackInSlot(i);
            if (is.isEmpty()) continue;
            Float weight = INGREDIENTS.get(is.getItem());
            if (weight == null || weight <= 0) continue;
            wr.addEntry(i, weight);
        }

        while (consume > 0) {
            Integer slot = wr.getRandomEntry();
            if (slot == null) break;            // ran out of weighted entries
            boolean fire = consume >= 1
                    || reika.dragonapi.libraries.java.ReikaRandomHelper.doWithChance(consume);
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
    }

    @Override
    protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag);
        mixTime = tag.getIntOr("mix", 0);
        pressure = tag.getIntOr("press", 0);
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
        return (int) (15.0 * output.getFluidLevel() / CAPACITY);
    }

    @Override
    public void onEMP() {}

    @Override
    public boolean hasModelTransparency() { return false; }

    @Override
    public int fillPipe(Direction from, net.neoforged.neoforge.fluids.FluidStack resource,
                        net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction action) {
        // Ethanol can be pushed into the input tank from any horizontal side.
        if (!canReceiveFrom(from)) return 0;
        if (!isValidFluid(resource.getFluid())) return 0;
        return input.fill(resource, action);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new ContainerFractionator(id, inv, this);
    }

    // 26.1: NBT save/load using ValueOutput/ValueInput so the inventory survives chunk reload.
    @Override
    protected void saveAdditional(net.minecraft.world.level.storage.ValueOutput output) {
        super.saveAdditional(output);
        net.minecraft.world.level.storage.TagValueOutput nested = net.minecraft.world.level.storage.TagValueOutput.createWithContext(
                net.minecraft.util.ProblemReporter.DISCARDING,
                this.level == null ? net.minecraft.core.RegistryAccess.EMPTY : this.level.registryAccess());
        itemHandler.serialize(nested);
        output.store("ItemsRaw", CompoundTag.CODEC, nested.buildResult());
    }

    @Override
    protected void loadAdditional(net.minecraft.world.level.storage.ValueInput input) {
        super.loadAdditional(input);
        itemHandler = new ManagedItemHandler(SLOTS) {
            @Override
            protected void onContentsChanged(int slot) { setChanged(); }
        };
        java.util.Optional<CompoundTag> raw = input.read("ItemsRaw", CompoundTag.CODEC);
        if (raw.isPresent()) {
            net.minecraft.world.level.storage.ValueInput nested = net.minecraft.world.level.storage.TagValueInput.create(
                    net.minecraft.util.ProblemReporter.DISCARDING,
                    this.level == null ? net.minecraft.core.RegistryAccess.EMPTY : this.level.registryAccess(),
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
