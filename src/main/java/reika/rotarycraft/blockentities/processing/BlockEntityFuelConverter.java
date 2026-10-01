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
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.*;
import reika.rotarycraft.auxiliary.recipemanagers.FuelEnhancerRecipe;
import reika.rotarycraft.base.blockentity.InventoriedPoweredLiquidIO;
import reika.rotarycraft.gui.container.machine.inventory.ContainerFuelEnhancer;
import reika.rotarycraft.modinterface.jei.RotaryRecipeSync;
import reika.rotarycraft.registry.*;

/** V33a fuel enhancer: bottom shaft input, five catalysts and two 5000 mB tanks. */
public class BlockEntityFuelConverter extends InventoriedPoweredLiquidIO {
    public static final int CAPACITY = 5000;
    public BlockEntityFuelConverter(BlockPos pos, BlockState state) { super(RotaryBlockEntities.FUEL_ENHANCER.get(), pos, state); }
    public static List<FuelEnhancerRecipe> getAllRecipes(Level level) {
        var recipes = RotaryRecipeSync.getRecipes(level);
        return recipes == null ? List.of() : recipes.byType(RotaryRecipeTypes.FUEL_ENHANCER.get()).stream().map(holder -> holder.value()).toList();
    }
    public static List<FuelEnhancerRecipe> getByInput(Level level, ItemStack item) { return getAllRecipes(level).stream().filter(r -> r.isValidItem(item)).toList(); }
    public static List<FuelEnhancerRecipe> getByInput(Level level, Fluid fluid) { return getAllRecipes(level).stream().filter(r -> r.accepts(fluid)).toList(); }
    public static List<FuelEnhancerRecipe> getByOutput(Level level, Fluid fluid) { return getAllRecipes(level).stream().filter(r -> r.output().value() == fluid).toList(); }
    public int getSpeedBoost() { return omega < MINSPEED ? 0 : 1 + (31 - Integer.numberOfLeadingZeros(omega / MINSPEED)) / 2; }
    @Override public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        if (world.isClientSide()) return;
        getPowerBelow();
        if (power < MINPOWER || omega < MINSPEED || input.isEmpty()) return;
        var inventory = java.util.stream.IntStream.range(0, getContainerSize()).mapToObj(this::getItem).toList();
        for (var recipe : getByInput(world, input.getActualFluid().getFluid())) {
            long speed = (long)getSpeedBoost() * recipe.speedFactor(), cost = speed * recipe.fluidRatio();
            if (cost > input.getFluidLevel() || speed > CAPACITY || !output.canTakeIn(recipe.output().value(), (int)speed) || !recipe.isUsable(this)) continue;
            int[] slots = recipe.findIngredients(inventory);
            if (slots == null) continue;
            input.removeLiquid((int)cost);
            output.addLiquid((int)speed, recipe.output().value());
            for (int slot : slots) if (world.getRandom().nextDouble() < recipe.itemConsumptionChance()) removeItem(slot, 1);
            setChanged();
            // The base lifecycle sends tank/power deltas every five ticks; menu slots sync separately.
            break;
        }
    }
    @Override protected void animateWithTick(Level world, BlockPos pos) {
        if (!isInWorld()) phi = 0;
        else phi += Math.pow(Math.log(omega + 1D) / Math.log(2), 1.05);
    }
    @Override public int getContainerSize() { return 9; }
    @Override public boolean isItemValidForSlot(int slot, ItemStack item) { return !getByInput(level, item).isEmpty(); }
    @Override public boolean canExtractItem(int slot, ItemStack item) { return false; }
    @Override public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) { return new ContainerFuelEnhancer(id, inventory, this); }
    @Override public boolean isValidFluid(Fluid fluid) { return !getByInput(level, fluid).isEmpty(); }
    @Override public Fluid getInputFluid() { return input.isEmpty() ? Fluids.EMPTY : input.getActualFluid().getFluid(); }
    @Override public boolean canConnectToPipe(MachineRegistry pipe) { return pipe == MachineRegistry.FUELLINE || pipe.isStandardPipe(); }
    @Override public boolean canIntakeFromPipe(MachineRegistry pipe) { return canConnectToPipe(pipe); }
    @Override public boolean canOutputToPipe(MachineRegistry pipe) { return pipe == MachineRegistry.FUELLINE; }
    @Override public boolean canOutputTo(Direction side) { return side.getAxis().isHorizontal(); }
    @Override public boolean canReceiveFrom(Direction side) { return side == Direction.UP; }
    @Override public boolean hasATank() { return true; }
    @Override public int getCapacity() { return CAPACITY; }
    @Override protected String getTEName() { return "Fuel Enhancer"; }
    @Override public net.minecraft.world.level.block.Block getBlockEntityBlockID() { return RotaryBlocks.FUEL_ENHANCER.get(); }
    @Override public MachineRegistry getMachine() { return MachineRegistry.FUELENHANCER; }
    @Override public boolean hasModelTransparency() { return true; }
    @Override public int getRedstoneOverride() { return input.isEmpty() || output.isFull() ? 15 : 0; }
    public double getLiquidModelOffset(boolean in) { return (in ? 10 : 1) / 16D; }
}
