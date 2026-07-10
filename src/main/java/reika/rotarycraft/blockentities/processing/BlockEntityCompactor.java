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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
import reika.rotarycraft.auxiliary.interfaces.FrictionHeatable;
import reika.rotarycraft.auxiliary.interfaces.MultiOperational;
import reika.rotarycraft.auxiliary.interfaces.PressureTE;
import reika.rotarycraft.auxiliary.interfaces.TemperatureTE;
import reika.rotarycraft.auxiliary.recipemanagers.CompactorRecipe;
import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
import reika.rotarycraft.registry.DurationRegistry;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryRecipeTypes;

/**
 * Compactor: squeezes 4x of an item (slots 0-3, all matching) into a compressed product (slot 4)
 * once BOTH sufficient pressure and temperature are reached. Pressure builds from input torque;
 * temperature from friction (heatable externally with a friction heater). The flagship chain is
 * coal -> anthracite -> prismane -> lonsdaleite -> diamond (each step needs 550 MPa / 800 C);
 * peripherals: blaze powder -> glowstone, ice -> packed ice (cold!).
 *
 * <p>26.2 port notes: recipes are the data-driven {@link CompactorRecipe} type (legacy
 * RecipesCompactor). The mod-heavy environmental temperature model (biome ambient lookup, adjacent
 * lava/fire/water/ice/snow blocks, nether pressure decay) is reduced to the LavaMaker-style
 * friction-heat + ambient-decay model, with pressure fed by torque exactly as legacy. Overheat
 * and overpressure destroy the machine as upstream.</p>
 */
public class BlockEntityCompactor extends InventoriedPowerReceiver implements TemperatureTE,
        PressureTE, FrictionHeatable, MultiOperational, ConditionalOperation {

    public static final int MAXTEMP = 1000;
    /** All pressures in kPa; steel yield strength = 250 MPa. */
    public static final int MAXPRESSURE = 600000;
    public static final int REQTEMP = 800;
    public static final int REQPRESS = 550000;
    private static final int AMBIENT_TEMP = 20;
    private static final int AMBIENT_PRESS = 101;

    public int compactorCookTime;
    private int pressure = AMBIENT_PRESS;
    public int temperature = AMBIENT_TEMP;
    public boolean idle = false;
    private boolean animdir = false;
    private int envirotick = 0;
    private int tempTick;

    public BlockEntityCompactor(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.COMPACTOR.get(), pos, state);
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        this.getPower(false);

        envirotick++;
        if (envirotick >= 20) {
            this.updatePressure(world, pos);
            if (tempTick == 0)
                this.updateTemperature(world, pos);
            envirotick = 0;
        }
        if (tempTick > 0)
            tempTick--;

        this.testIdle();

        tickcount++;

        if (world.isClientSide())
            return;

        boolean flag1 = false;
        int n = this.getNumberConsecutiveOperations();
        for (int i = 0; i < n; i++)
            flag1 |= this.doOperation(n > 1);

        if (flag1)
            this.setChanged();
    }

    private boolean doOperation(boolean multiple) {
        if (this.canSmelt()) {
            compactorCookTime++;
            if (multiple || compactorCookTime >= this.getOperationTime()) {
                compactorCookTime = 0;
                this.smeltItem();
            }
            return true;
        }
        else {
            compactorCookTime = 0;
            return false;
        }
    }

    public void testIdle() {
        boolean ingred = false;
        boolean invalid = false;
        for (int i = 0; i < 4; i++) {
            if (itemHandler.getStackInSlot(i).isEmpty())
                invalid = true;
        }
        if (!invalid) {
            ItemStack first = itemHandler.getStackInSlot(0);
            for (int i = 1; i < 4; i++) {
                if (!ItemStack.isSameItemSameComponents(first, itemHandler.getStackInSlot(i)))
                    invalid = true;
            }
        }
        if (!invalid && this.getRecipe(itemHandler.getStackInSlot(0)) != null)
            ingred = true;
        ItemStack out = itemHandler.getStackInSlot(4);
        boolean full = !out.isEmpty() && out.getCount() >= out.getMaxStackSize();
        idle = !ingred || full;
    }

    private CompactorRecipe getRecipe(ItemStack in) {
        if (in.isEmpty() || level == null || level.getServer() == null)
            return null;
        return level.getServer().getRecipeManager()
                .getRecipeFor(RotaryRecipeTypes.COMPACTOR.get(), new SingleRecipeInput(in), level)
                .map(h -> h.value()).orElse(null);
    }

    private boolean canSmelt() {
        if (power < MINPOWER || torque < MINTORQUE)
            return false;
        ItemStack first = itemHandler.getStackInSlot(0);
        for (int i = 0; i < 4; i++) {
            if (itemHandler.getStackInSlot(i).isEmpty())
                return false;
        }
        for (int i = 1; i < 4; i++) {
            if (!ItemStack.isSameItemSameComponents(first, itemHandler.getStackInSlot(i)))
                return false;
        }
        CompactorRecipe r = this.getRecipe(first);
        if (r == null)
            return false;
        if (pressure < r.getReqPressure() || temperature < r.getReqTemperature())
            return false;
        ItemStack result = r.getResult();
        ItemStack out = itemHandler.getStackInSlot(4);
        if (out.isEmpty())
            return true;
        return ItemStack.isSameItemSameComponents(out, result)
                && out.getCount() + result.getCount() <= Math.min(this.getInventoryStackLimit(), result.getMaxStackSize());
    }

    private void smeltItem() {
        if (!this.canSmelt())
            return;
        CompactorRecipe r = this.getRecipe(itemHandler.getStackInSlot(0));
        ItemStack result = r.getResult();
        ItemStack out = itemHandler.getStackInSlot(4);
        if (out.isEmpty())
            itemHandler.setStackInSlot(4, result);
        else
            out.grow(result.getCount());
        for (int i = 0; i < 4; i++)
            itemHandler.extractItem(i, 1, false);
    }

    /** 1-indexed stage from the current input (drives the staged operation time). */
    public int getStage() {
        ItemStack in = itemHandler.getStackInSlot(0);
        if (in.isEmpty())
            return 1;
        if (in.getItem() == reika.rotarycraft.registry.RotaryItems.ANTHRACITE.get())
            return 2;
        if (in.getItem() == reika.rotarycraft.registry.RotaryItems.PRISMANE.get())
            return 3;
        if (in.getItem() == reika.rotarycraft.registry.RotaryItems.LONSDALEITE.get())
            return 4;
        return 1;
    }

    // --- Pressure (builds from torque; decays toward ambient) ---------------

    @Override
    public void updatePressure(Level world, BlockPos pos) {
        if (pressure > AMBIENT_PRESS)
            pressure -= Math.max((pressure - AMBIENT_PRESS) / 200, 1);
        if (pressure < AMBIENT_PRESS)
            pressure += Math.max((AMBIENT_PRESS - pressure) / 40, 1);
        if (omega > 0)
            pressure += (int) (128 * ReikaMathLibrary.logbase(torque, 2));
        if (pressure > MAXPRESSURE)
            this.overpressure(world, pos);
    }

    @Override
    public void addPressure(int press) {
        pressure += press;
    }

    @Override
    public int getPressure() {
        return pressure;
    }

    @Override
    public int getMaxPressure() {
        return MAXPRESSURE;
    }

    @Override
    public void overpressure(Level world, BlockPos pos) {
        pressure = MAXPRESSURE;
        world.removeBlock(pos, false);
        world.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3F, Level.ExplosionInteraction.BLOCK);
    }

    // --- Temperature (friction heat + ambient decay, LavaMaker model) -------

    @Override
    public void updateTemperature(Level world, BlockPos pos) {
        if (temperature > AMBIENT_TEMP)
            temperature -= Math.max((temperature - AMBIENT_TEMP) / 200, 1);
        if (temperature < AMBIENT_TEMP)
            temperature += Math.max((AMBIENT_TEMP - temperature) / 40, 1);
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
        return 0;
    }

    @Override
    public int getMaxTemperature() {
        return MAXTEMP;
    }

    @Override
    public int getAmbientTemperature() {
        return AMBIENT_TEMP;
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
        return true;
    }

    @Override
    public void overheat(Level world, BlockPos pos) {
        temperature = MAXTEMP;
        world.removeBlock(pos, false);
        world.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 2F, Level.ExplosionInteraction.BLOCK);
    }

    @Override
    public void resetAmbientTemperatureTimer() {
        tempTick = 20;
    }

    @Override
    public float getMultiplier() {
        return 1F;
    }

    @Override
    public boolean canBeFrictionHeated() {
        return true;
    }

    @Override
    public void onOverheat(Level world, BlockPos pos) {
        this.overheat(world, pos);
    }

    // --- GUI scaling ---------------------------------------------------------

    public int getCookProgressScaled(int par1) {
        int time = this.getOperationTime();
        return time > 0 ? Math.min(par1, compactorCookTime * par1 / time) : 0;
    }

    public int getPressureScaled(int par1) {
        return pressure * par1 / MAXPRESSURE;
    }

    public int getTemperatureScaled(int par1) {
        return temperature * par1 / MAXTEMP;
    }

    // --- Boilerplate ----------------------------------------------------------

    @Override
    public int getContainerSize() {
        return 5;
    }

    public boolean isItemValidForSlot(int slot, ItemStack is) {
        return slot != 4 && this.getRecipe(is) != null;
    }

    public boolean canExtractItem(int i, ItemStack is, int j) {
        return i == 4;
    }

    /** Exposes the inventory for the container (base handler is protected). */
    public reika.dragonapi.instantiable.storage.ManagedItemHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
        // Press piston oscillation between 0.5 and 1.5, mostly resting at the ends (legacy).
        if (phi < 0.5F)
            phi = 1F;
        if (!this.isInWorld())
            return;
        if (power < MINPOWER || torque < MINTORQUE)
            return;
        if (phi >= 1.5F || phi <= 0.5F)
            if (rand.nextInt(40) > 0)
                return;
        if (animdir)
            phi += 0.03125F;
        else
            phi -= 0.03125F;
        if (phi >= 1.5F || phi <= 0.5F)
            animdir = !animdir;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.COMPACTOR;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.COMPACTOR.get();
    }

    @Override
    protected String getTEName() {
        return "compactor";
    }

    @Override
    public boolean hasAnInventory() {
        return true;
    }

    @Override
    public boolean hasATank() {
        return false;
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
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        NBT.putShort("CookTime", (short) compactorCookTime);
        NBT.putInt("temperature", temperature);
        NBT.putInt("pressure", pressure);
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        compactorCookTime = NBT.getShortOr("CookTime", (short) 0);
        temperature = NBT.getIntOr("temperature", AMBIENT_TEMP);
        pressure = NBT.getIntOr("pressure", AMBIENT_PRESS);
    }

    @Override
    public int getOperationTime() {
        return DurationRegistry.COMPACTOR.getOperationTime(omega, this.getStage() - 1);
    }

    @Override
    public int getNumberConsecutiveOperations() {
        return DurationRegistry.COMPACTOR.getNumberOperations(omega, this.getStage() - 1);
    }

    @Override
    public boolean areConditionsMet() {
        return !idle;
    }

    @Override
    public String getOperationalStatus() {
        return this.areConditionsMet() ? "Operational" : "Idle (Check Items)";
    }

}
