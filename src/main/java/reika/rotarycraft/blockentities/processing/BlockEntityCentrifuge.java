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

import java.util.List;

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
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import reika.dragonapi.instantiable.HybridTank;
import reika.dragonapi.libraries.ReikaFluidHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
import reika.rotarycraft.auxiliary.interfaces.MultiOperational;
import reika.rotarycraft.auxiliary.interfaces.PipeConnector;
import reika.rotarycraft.auxiliary.recipemanagers.CentrifugeRecipe;
import reika.rotarycraft.base.blockentity.BlockEntityPiping.Flow;
import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
import reika.rotarycraft.gui.container.machine.inventory.ContainerCentrifuge;
import reika.rotarycraft.registry.DurationRegistry;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryRecipeTypes;

/**
 * Vertical spin separator: slot 0 is the input, slots 1-9 hold the chanced outputs, and the
 * tank collects any fluid byproduct (lubricant from canola husks and the like). Power comes in
 * along the vertical axis (from above when flipped). Recipes are the data-driven
 * {@link CentrifugeRecipe}s (legacy {@code RecipesCentrifuge}).
 */
public class BlockEntityCentrifuge extends InventoriedPowerReceiver implements MultiOperational, ConditionalOperation, PipeConnector {

    public static final int CAPACITY = 10000;

    private final HybridTank tank = new HybridTank("centrifuge", CAPACITY) {
        @Override
        protected void onContentsChanged() {
            setChanged();
        }
    };

    private int progressTime;

    public BlockEntityCentrifuge(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.CENTRIFUGE.get(), pos, state);
    }

    public int getProgressScaled(int l) {
        return l * progressTime / this.getOperationTime();
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
        if (!this.isInWorld()) {
            phi = 0;
            return;
        }
        if (omega <= 0)
            return;
        phi += ReikaMathLibrary.logbase2(omega);
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.CENTRIFUGE;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.CENTRIFUGE.get();
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    protected String getTEName() {
        return "Centrifuge";
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        if (isFlipped)
            this.getPowerAbove();
        else
            this.getPowerBelow();

        if (power >= MINPOWER && omega >= MINSPEED) {
            int n = this.getNumberConsecutiveOperations();
            for (int i = 0; i < n; i++)
                this.doOperation(n > 1);
        } else {
            progressTime = 0;
        }
    }

    private void doOperation(boolean multiple) {
        CentrifugeRecipe recipe = this.getRecipe(itemHandler.getStackInSlot(0));
        if (recipe == null) {
            progressTime = 0;
            return;
        }
        progressTime++;
        if (multiple || progressTime >= this.getOperationTime()) {
            progressTime = 0;
            this.process(recipe);
        }
    }

    private void process(CentrifugeRecipe recipe) {
        if (level == null || level.isClientSide())
            return;
        List<ItemStack> items = recipe.rollItems(level.getRandom());
        FluidStack fs = recipe.rollFluid(level.getRandom());
        if (!fs.isEmpty() && !tank.canTakeIn(fs))
            return;

        // Simulate distribution into the nine output slots; only commit when everything fits,
        // matching the legacy canMakeAllOf gate (which refused to run rather than void outputs).
        ItemStack[] virt = new ItemStack[9];
        for (int i = 0; i < 9; i++)
            virt[i] = itemHandler.getStackInSlot(i + 1).copy();
        for (ItemStack is : items) {
            if (!insertVirtual(virt, is.copy()))
                return;
        }
        for (int i = 0; i < 9; i++)
            itemHandler.setStackInSlot(i + 1, virt[i]);
        if (!fs.isEmpty())
            tank.addLiquid(fs.getAmount(), fs.getFluid());
        itemHandler.extractItem(0, 1, false);
        this.setChanged();
    }

    private static boolean insertVirtual(ItemStack[] virt, ItemStack is) {
        for (int i = 0; i < virt.length && !is.isEmpty(); i++) {
            if (virt[i].isEmpty()) {
                virt[i] = is.copy();
                is.setCount(0);
            } else if (ItemStack.isSameItemSameComponents(virt[i], is)) {
                int room = virt[i].getMaxStackSize() - virt[i].getCount();
                int mv = Math.min(room, is.getCount());
                virt[i].grow(mv);
                is.shrink(mv);
            }
        }
        return is.isEmpty();
    }

    private CentrifugeRecipe getRecipe(ItemStack in) {
        if (in.isEmpty() || level == null || level.getServer() == null)
            return null;
        SingleRecipeInput input = new SingleRecipeInput(in);
        return level.getServer().getRecipeManager()
                .getRecipeFor(RotaryRecipeTypes.CENTRIFUGE.get(), input, level)
                .map(h -> h.value())
                .orElse(null);
    }

    public int getContainerSize() {
        return 10;
    }

    public boolean isItemValidForSlot(int slot, ItemStack is) {
        return slot == 0 && this.getRecipe(is) != null;
    }

    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return i != 0;
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    public boolean areConditionsMet() {
        return this.getRecipe(itemHandler.getStackInSlot(0)) != null;
    }

    @Override
    public String getOperationalStatus() {
        return this.areConditionsMet() ? "Operational" : "Invalid or Missing Items";
    }

    @Override
    public int getOperationTime() {
        return DurationRegistry.CENTRIFUGE.getOperationTime(omega);
    }

    @Override
    public int getNumberConsecutiveOperations() {
        return DurationRegistry.CENTRIFUGE.getNumberOperations(omega);
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        progressTime = NBT.getIntOr("CookTime", 0);
        tank.readFromNBT(NBT);
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        NBT.putInt("CookTime", progressTime);
        tank.writeToNBT(NBT);
    }

    // ---- fluid byproduct output (drain-only, horizontal sides — the legacy IO layout) ----

    @Override
    public boolean canConnectToPipe(MachineRegistry m) {
        return m.isStandardPipe() || m == MachineRegistry.HOSE;
    }

    @Override
    public boolean canConnectToPipeOnSide(MachineRegistry p, Direction side) {
        return this.canConnectToPipe(p) && side.getStepY() == 0;
    }

    @Override
    public Flow getFlowForSide(Direction side) {
        return side.getStepY() == 0 ? Flow.OUTPUT : Flow.NONE;
    }

    @Override
    public int fillPipe(Direction from, FluidStack resource, IFluidHandler.FluidAction action) {
        return 0;
    }

    @Override
    public FluidStack drainPipe(Direction from, int maxDrain, IFluidHandler.FluidAction doDrain) {
        if (this.canDrain(from, null))
            return tank.drain(maxDrain, doDrain);
        return FluidStack.EMPTY;
    }

    public boolean canDrain(Direction from, FluidStack fluid) {
        return from.getStepY() == 0 && ReikaFluidHelper.isFluidDrainableFromTank(fluid, tank);
    }

    @Override
    public void onEMP() {
    }

    // ---- GUI accessors ----

    public int getFluidLevel() {
        return tank.getFluidLevel();
    }

    public Fluid getFluid() {
        return tank.getActualFluid().getFluid();
    }

    public int getLiquidScaled(int a) {
        return a * tank.getFluidLevel() / tank.getCapacity();
    }

    public int getProgress() {
        return progressTime;
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
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new ContainerCentrifuge(id, inv, this);
    }
}
