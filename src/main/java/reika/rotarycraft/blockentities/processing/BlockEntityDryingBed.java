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

import reika.rotarycraft.auxiliary.recipemanagers.DryingBedRecipe;
import reika.rotarycraft.base.blockentity.InventoriedRCFluidReceiver;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryRecipeTypes;

/**
 * Drying Bed: unpowered evaporation pan -- fluid piped in slowly dries into an item every 400
 * ticks (water -> salt, lava -> gold nugget). Runs on the new unpowered
 * {@link InventoriedRCFluidReceiver} base.
 *
 * <p>26.2 port notes: recipes are the data-driven {@link DryingBedRecipe} (legacy RecipesDryingBed;
 * the oil/honey/chroma mod-fluid entries are gated out).</p>
 */
public class BlockEntityDryingBed extends InventoriedRCFluidReceiver {

    public static final int CAPACITY = 2000;
    private static final int DRY_TIME = 400;

    private int timer;
    public int progress;

    public BlockEntityDryingBed(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.DRYING.get(), pos, state);
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        if (world.isClientSide())
            return;
        DryingBedRecipe r = tank.isEmpty() ? null : this.getRecipe(tank.getActualFluid().getFluid());
        if (this.canMake(r)) {
            timer++;
            if (timer >= DRY_TIME) {
                timer = 0;
                while (this.canMake(r)) {
                    this.make(r);
                    r = tank.isEmpty() ? null : this.getRecipe(tank.getActualFluid().getFluid());
                }
                this.setChanged();
            }
        }
        else {
            timer = 0;
        }
        progress = timer;
    }

    private DryingBedRecipe getRecipe(Fluid f) {
        if (level == null || level.getServer() == null)
            return null;
        for (var h : level.getServer().getRecipeManager().recipeMap().byType(RotaryRecipeTypes.DRYING_BED.get())) {
            if (h.value().matchesFluid(f))
                return h.value();
        }
        return null;
    }

    private boolean canMake(DryingBedRecipe r) {
        if (r == null || tank.getFluidLevel() < r.getConsumption())
            return false;
        ItemStack is = r.getResult();
        ItemStack slot = itemHandler.getStackInSlot(0);
        if (slot.isEmpty())
            return true;
        return ItemStack.isSameItemSameComponents(slot, is) && slot.getCount() + is.getCount() <= slot.getMaxStackSize();
    }

    private void make(DryingBedRecipe r) {
        ItemStack is = r.getResult();
        ItemStack slot = itemHandler.getStackInSlot(0);
        if (slot.isEmpty())
            itemHandler.setStackInSlot(0, is);
        else
            slot.grow(is.getCount());
        tank.removeLiquid(r.getConsumption());
    }

    public boolean canExtractItem(int i, ItemStack is, int s) {
        return true;
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack is) {
        return false;
    }

    // Multi-fluid receiver: any fluid a drying recipe uses is acceptable.
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
    public boolean canConnectToPipe(MachineRegistry m) {
        return m == MachineRegistry.HOSE || m == MachineRegistry.FUELLINE || m.isStandardPipe();
    }

    @Override
    public FluidStack drainPipe(Direction from, int maxDrain, IFluidHandler.FluidAction action) {
        return FluidStack.EMPTY;
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
        return MachineRegistry.DRYING;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.DRYING.get();
    }

    @Override
    protected String getTEName() {
        return "dryingbed";
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
        NBT.putInt("timer", timer);
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        timer = NBT.getIntOr("timer", 0);
    }

}
