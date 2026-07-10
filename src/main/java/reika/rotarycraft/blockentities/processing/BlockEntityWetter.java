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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.auxiliary.recipemanagers.WetterRecipe;
import reika.rotarycraft.base.blockentity.InventoriedPowerLiquidReceiver;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryRecipeTypes;

/**
 * Wetter: soaks the single held item in the tank fluid, converting it after the recipe's duration
 * (faster with more speed) -- sand + lubricant -> soul sand, cobblestone + jet fuel -> netherrack.
 * Powered from below.
 *
 * <p>26.2 port notes: recipes are the data-driven {@link WetterRecipe} (legacy RecipesWetter; the
 * oil/ender mod-fluid entries are gated out). The legacy in-world floating InertItem display is
 * not ported (item is visible in the GUI); extraction is locked while soaking exactly as legacy.</p>
 */
public class BlockEntityWetter extends InventoriedPowerLiquidReceiver {

    public static final int CAPACITY = 4000;

    private int tick = 0;
    private int extractionCooldown = 0;

    public BlockEntityWetter(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.WETTER.get(), pos, state);
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        this.getPowerBelow();

        if (world.isClientSide())
            return;

        boolean ticked = false;
        if (power >= MINPOWER && omega >= MINSPEED) {
            ItemStack is = itemHandler.getStackInSlot(0);
            if (!is.isEmpty() && !tank.isEmpty()) {
                WetterRecipe wr = this.getRecipe(is, tank.getActualFluid().getFluid());
                if (wr != null) {
                    ticked = true;
                    if (tick >= this.getDuration(wr)) {
                        tank.removeLiquid(wr.getAmount());
                        itemHandler.setStackInSlot(0, wr.getResult());
                        tick = 0;
                        this.setChanged();
                    }
                    else {
                        tick += 1 + 4 * ReikaMathLibrary.logbase2(Math.max(1, omega / Math.max(1, MINSPEED)));
                    }
                }
            }
        }
        if (ticked) {
            extractionCooldown = 10;
        }
        else {
            if (extractionCooldown > 0)
                extractionCooldown--;
            tick = 0;
        }
    }

    private WetterRecipe getRecipe(ItemStack is, Fluid f) {
        if (level == null || level.getServer() == null)
            return null;
        for (var h : level.getServer().getRecipeManager().recipeMap().byType(RotaryRecipeTypes.WETTER.get())) {
            if (h.value().matchesWith(is, f))
                return h.value();
        }
        return null;
    }

    public int getDuration(WetterRecipe wr) {
        return Math.max(1, wr.getDuration() - 5 * ((omega / Math.max(1, MINSPEED)) - 1));
    }

    public int getTick() {
        return tick;
    }

    /** Public tank level for the GUI (base tank field is protected). */
    public int getTankLevel() {
        return tank.getFluidLevel();
    }

    public boolean canExtractItem(int slot, ItemStack is, int side) {
        return tick == 0 && extractionCooldown == 0;
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean canConnectToPipe(MachineRegistry m) {
        return m == MachineRegistry.HOSE || m == MachineRegistry.FUELLINE || m.isStandardPipe();
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack is) {
        if (level == null || level.getServer() == null)
            return false;
        if (!tank.isEmpty())
            return this.getRecipe(is, tank.getActualFluid().getFluid()) != null;
        for (var h : level.getServer().getRecipeManager().recipeMap().byType(RotaryRecipeTypes.WETTER.get())) {
            if (h.value().getInput().test(is))
                return true;
        }
        return false;
    }

    // Multi-fluid receiver: any fluid a wetting recipe uses is acceptable.
    @Override
    public Fluid getInputFluid() {
        return null;
    }

    @Override
    public boolean isValidFluid(Fluid f) {
        if (level == null || level.getServer() == null)
            return false;
        for (var h : level.getServer().getRecipeManager().recipeMap().byType(RotaryRecipeTypes.WETTER.get())) {
            if (h.value().getFluid().isSame(f))
                return true;
        }
        return false;
    }

    @Override
    public boolean canReceiveFrom(Direction from) {
        return from.getAxis().isHorizontal();
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
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.WETTER;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.WETTER.get();
    }

    @Override
    protected String getTEName() {
        return "wetter";
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
        NBT.putInt("tick", tick);
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        tick = NBT.getIntOr("tick", 0);
    }

}
