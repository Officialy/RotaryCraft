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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import reika.rotarycraft.auxiliary.recipemanagers.PurifierRecipe;
import reika.rotarycraft.gui.container.machine.inventory.ContainerPurifier;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryRecipeTypes;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.rotarycraft.auxiliary.RotaryAux;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
import reika.rotarycraft.auxiliary.interfaces.TemperatureTE;
import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
import reika.rotarycraft.registry.DurationRegistry;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryItems;


public class BlockEntityPurifier extends InventoriedPowerReceiver implements TemperatureTE, DiscreteFunction, ConditionalOperation, WorldlyContainer {

    public static final int SMELTTEMP = 600;
    public static final int MAXTEMP = 1000;
    public int cookTime = 0;
    public int temperature;

    public BlockEntityPurifier(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.PURIFIER.get(), pos, state);
    }

    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return i == 6;
    }

    @Override
    public int getContainerSize() {
        return 8;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.PURIFIER.get();
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        if (world.isClientSide()) return;
        this.updateTemperature(world, pos);
        if (isRemoved()) return;
        this.getSummativeSidedPower();
        PurifierRecipe recipe = getBatchRecipe();
        if (power < MINPOWER || !canSmelt(recipe)) {
            cookTime = 0;
        } else if (++cookTime >= getOperationTime()) {
            smelt(recipe);
            cookTime = 0;
        }
        setChanged();
    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    private net.minecraft.world.item.crafting.RecipeMap recipes() {
        return reika.rotarycraft.modinterface.jei.RotaryRecipeSync.getRecipes(level);
    }

    private PurifierRecipe getRecipe(ItemStack stack) {
        var recipes = recipes();
        if (recipes == null || stack.isEmpty()) return null;
        return recipes.byType(RotaryRecipeTypes.PURIFIER.get()).stream().map(holder -> holder.value())
                .filter(recipe -> recipe.matches(new SingleRecipeInput(stack), level)).findFirst().orElse(null);
    }

    private PurifierRecipe getBatchRecipe() {
        for (int slot = 1; slot <= 5; slot++) {
            PurifierRecipe recipe = getRecipe(getItem(slot));
            if (recipe != null) return recipe;
        }
        return null;
    }

    private int getBatchSize(PurifierRecipe recipe) {
        if (recipe == null) return 0;
        int count = 0;
        for (int slot = 1; slot <= 5; slot++)
            if (recipe.matches(new SingleRecipeInput(getItem(slot)), level)) count++;
        return count;
    }

    private boolean canSmelt(PurifierRecipe recipe) {
        if (recipe == null || temperature < recipe.temperature()
                || !recipe.gunpowder().test(getItem(0)) || !recipe.sand().test(getItem(7))) return false;
        ItemStack result = recipe.getResult();
        ItemStack stored = getItem(6);
        int count = getBatchSize(recipe);
        return count > 0 && (stored.isEmpty() || ItemStack.isSameItemSameComponents(stored, result))
                && stored.getCount() + count <= result.getMaxStackSize();
    }

    private void smelt(PurifierRecipe recipe) {
        int count = getBatchSize(recipe);
        for (int slot = 1; slot <= 5; slot++)
            if (recipe.matches(new SingleRecipeInput(getItem(slot)), level)) removeItem(slot, 1);
        ItemStack result = recipe.getResult();
        result.setCount(getItem(6).getCount() + count);
        setItem(6, result);
        if (rand.nextInt(recipe.gunpowderConsumption()) == 0) removeItem(0, 1);
        if (rand.nextInt(recipe.sandConsumption()) == 0) removeItem(7, 1);
    }

    public int getCookScaled(int size) {
        return Math.min(size, size * cookTime / getOperationTime());
    }

    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if (stack.isEmpty() || slot == 6 || recipes() == null) return false;
        if (slot >= 1 && slot <= 5) return getRecipe(stack) != null;
        return recipes().byType(RotaryRecipeTypes.PURIFIER.get()).stream()
                .anyMatch(holder -> slot == 0 ? holder.value().gunpowder().test(stack)
                        : slot == 7 && holder.value().sand().test(stack));
    }


    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.PURIFIER;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        temperature = NBT.getIntOr("temperature", 0);
        cookTime = NBT.getIntOr("time", 0);
    }

    @Override
    protected String getTEName() {
        return "purifier";
    }

    /**
     * Writes a tile entity to NBT.
     */
    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        NBT.putInt("temperature", temperature);
        NBT.putInt("time", cookTime);
    }

    public void updateTemperature(Level world, BlockPos pos) {
        int Tamb = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);

        if (RotaryAux.isNextToWater(world, pos)) {
            Tamb /= 2;
        }
        Direction iceside = ReikaWorldHelper.checkForAdjBlock(world, pos, Blocks.ICE);
        if (iceside != null) {
            if (Tamb > 0)
                Tamb /= 4;
            world.setBlockAndUpdate(pos.relative(iceside), Blocks.WATER.defaultBlockState());
        }

        if (RotaryAux.isNextToFire(world, pos)) {
            Tamb += 200;
        }

        if (RotaryAux.isNextToLava(world, pos)) {
            Tamb += 600;
        }

        if (temperature > Tamb)
            temperature--;
        if (temperature > Tamb * 2)
            temperature--;
        if (temperature < Tamb)
            temperature++;
        if (temperature * 2 < Tamb)
            temperature++;
        if (temperature > MAXTEMP) {
            temperature = MAXTEMP;
            this.overheat(world, pos);
        }
    }

    public void overheat(Level world, BlockPos pos) {
        ReikaWorldHelper.overheat(world, pos.getX(), pos.getY(), pos.getZ(),
                RotaryItems.HSLA_STEEL_SCRAP.get().getDefaultInstance(), 0, 7, false, 1F, false, true, 2F);
    }

    @Override
    public int getThermalDamage() {
        return 0;
    }

    @Override
    public int getRedstoneOverride() {
        if (getItem(0).isEmpty()) return 15;
        if (!getItem(0).is(Items.GUNPOWDER)) return 0;
        if (getBatchRecipe() == null) return 15;
        ItemStack output = getItem(6);
        return !output.isEmpty() && (output.is(RotaryItems.HSLA_STEEL_INGOT.get())
                || output.getCount() >= output.getMaxStackSize()) ? 15 : 0;
    }

    @Override
    public void addTemperature(int temp) {
        temperature += temp;
        setChanged();
    }

    @Override
    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temp) {
        temperature = temp;
        setChanged();
    }

    @Override
    public void onEMP() {
    }

    @Override
    public int getOperationTime() {
        return DurationRegistry.PURIFIER.getOperationTime(omega);
    }

    @Override
    public boolean areConditionsMet() {
        return canSmelt(getBatchRecipe());
    }

    @Override
    public String getOperationalStatus() {
        return this.areConditionsMet() ? "Operational" : "Invalid or Missing Items";
    }

    @Override
    public boolean canBeCooledWithFins() {
        return false;
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
    public int getAmbientTemperature() {
        return 0;
    }

    @Override
    public int getMaxTemperature() {
        return MAXTEMP;
    }

    @Override public boolean canPlaceItem(int slot, ItemStack stack) { return isItemValidForSlot(slot, stack); }
    @Override public int[] getSlotsForFace(Direction side) { return new int[] {0, 1, 2, 3, 4, 5, 6, 7}; }
    @Override public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction side) { return canPlaceItem(slot, stack); }
    @Override public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) { return slot == 6; }
    @Override public boolean isEmpty() {
        for (int slot = 0; slot < getContainerSize(); slot++) if (!getItem(slot).isEmpty()) return false;
        return true;
    }
    @Override public ItemStack getItem(int slot) { return itemHandler.getStackInSlot(slot); }
    @Override public ItemStack removeItem(int slot, int count) { return itemHandler.extractItem(slot, count, false); }
    @Override public ItemStack removeItemNoUpdate(int slot) { return removeItem(slot, getItem(slot).getCount()); }
    @Override public void setItem(int slot, ItemStack stack) { itemHandler.setStackInSlot(slot, stack); }
    @Override public boolean stillValid(Player player) { return isPlayerAccessible(player); }
    @Override public void clearContent() {
        for (int slot = 0; slot < getContainerSize(); slot++) setItem(slot, ItemStack.EMPTY);
    }
    @Override public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new ContainerPurifier(id, inv, this);
    }
    @Override public boolean hasAnInventory() { return true; }
    @Override public boolean hasATank() { return false; }
}
