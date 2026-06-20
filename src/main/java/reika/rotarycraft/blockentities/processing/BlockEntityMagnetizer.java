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
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.level.block.Block;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import reika.dragonapi.DragonAPI;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.items.tools.ItemEngineUpgrade.UpgradeType;
import reika.rotarycraft.auxiliary.RedstoneCycleTracker;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
import reika.rotarycraft.auxiliary.interfaces.MagnetizationCore;
import reika.rotarycraft.auxiliary.recipemanagers.RecipesMagnetizer;
import reika.rotarycraft.auxiliary.recipemanagers.RecipesMagnetizer.MagnetizerRecipe;
import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
import reika.rotarycraft.items.tools.ItemEngineUpgrade;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryItems;

public class BlockEntityMagnetizer extends InventoriedPowerReceiver
        implements DiscreteFunction, ConditionalOperation, MagnetizationCore {

    public static final int MIN_DURATION = 2;

    private final RedstoneCycleTracker redstone = new RedstoneCycleTracker(3);
    private boolean hasLodestone = false;

    public BlockEntityMagnetizer(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.MAGNETIZER.get(), pos, state);
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.MAGNETIZER;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        this.getSummativeSidedPower();

        if (power < MINPOWER) {
            tickcount = 0;
            return;
        }
        if (omega < MINSPEED) {
            tickcount = 0;
            return;
        }
        if (world.isClientSide()) return;

        redstone.update(world, pos);
        if (!redstone.isAlternating())
            return;

        tickcount++;
        if (tickcount < this.getOperationTime())
            return;
        tickcount = 0;

        ItemStack is = itemHandler.getStackInSlot(0);
        if (!is.isEmpty()) {
            MagnetizerRecipe r = RecipesMagnetizer.getRecipes().getRecipe(is);
            if (r != null && this.canRunRecipe(r))
                this.magnetize(r);
        }
    }

    private boolean hasRecipe() {
        ItemStack is = itemHandler.getStackInSlot(0);
        return !is.isEmpty() && RecipesMagnetizer.getRecipes().getRecipe(is) != null;
    }

    private boolean canRunRecipe(MagnetizerRecipe r) {
        int ms = r.minSpeed;
        if (hasLodestone) ms /= 2;
        return omega >= ms && (r.allowStacking || itemHandler.getStackInSlot(0).getCount() == 1);
    }

    private void magnetize(MagnetizerRecipe r) {
        if (r.timeFactor > 1 && DragonAPI.rand.nextInt(r.timeFactor) > 0)
            return;
        ItemStack is = itemHandler.getStackInSlot(0);
        // Read current magnet level from custom data NBT
        CompoundTag tag = is.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        int current = tag.getIntOr("magnet", 0);
        int max = r.getMaxCharge(hasLodestone ? omega * 2 : omega);
        if (max <= 0) max = 1;
        if (current < max) {
            tag.putInt("magnet", current + 1);
            is.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, cd -> CustomData.of(tag));
        }
    }

    @Override
    public int getOperationTime() {
        // DurationRegistry.MAGNETIZER is commented out; use a simple formula matching original: 400 base / omega steps
        int base = omega > 0 ? Math.max(MIN_DURATION, 400 / omega) : 400;
        return hasLodestone ? base / 2 : base;
    }

    @Override
    public boolean areConditionsMet() {
        return this.hasRecipe();
    }

    @Override
    public String getOperationalStatus() {
        return this.areConditionsMet() ? "Operational" : "No Shaft Core";
    }

    @Override
    public int getCoreMagnetization() {
        ItemStack is = itemHandler.getStackInSlot(0);
        if (is.isEmpty()) return 0;
        CompoundTag tag = is.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return tag.getIntOr("magnet", 0);
    }

    public void addRedstoneUpgrade() {
        redstone.addIntegrated();
    }

    public boolean hasRedstoneUpgrade() {
        return redstone.hasIntegrated();
    }

    public void addLodestoneUpgrade() {
        hasLodestone = true;
    }

    public boolean hasLodestoneUpgrade() {
        return hasLodestone;
    }

    @Override
    public void breakBlock() {
        if (this.hasRedstoneUpgrade()) {
            ItemStack upgrade = new ItemStack(RotaryItems.UPGRADE.get(), 1);
            ReikaItemHelper.updateStackTag(upgrade, t -> t.putString("upgradeType", UpgradeType.REDSTONE.desc));
            ReikaItemHelper.dropItem(level, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, upgrade);
        }
        if (this.hasLodestoneUpgrade()) {
            ItemStack upgrade = new ItemStack(RotaryItems.UPGRADE.get(), 1);
            ReikaItemHelper.updateStackTag(upgrade, t -> t.putString("upgradeType", UpgradeType.LODESTONE.desc));
            ReikaItemHelper.dropItem(level, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, upgrade);
        }
    }

    @Override
    public boolean canUpgradeWith(ItemStack item) {
        if (!(item.getItem() instanceof ItemEngineUpgrade)) return false;
        String type = item.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getStringOr("upgradeType", "");
        if (UpgradeType.REDSTONE.desc.equals(type)) return !redstone.hasIntegrated();
        if (UpgradeType.LODESTONE.desc.equals(type)) return !hasLodestone;
        return false;
    }

    @Override
    public void upgrade(ItemStack item) {
        String type = item.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getStringOr("upgradeType", "");
        if (UpgradeType.REDSTONE.desc.equals(type)) redstone.addIntegrated();
        else if (UpgradeType.LODESTONE.desc.equals(type)) hasLodestone = true;
    }

    public boolean hasCore() {
        ItemStack is = itemHandler.getStackInSlot(0);
        return ReikaItemHelper.matchStacks(is, RotaryItems.HSLA_SHAFT_CORE)
                || ReikaItemHelper.matchStacks(is, RotaryItems.TUNGSTEN_ALLOY_SHAFT_CORE);
    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {
        if (omega > 0)
            phi += (float) Math.pow(Math.log(omega + 1) / Math.log(2), 1.05);
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        NBT.putBoolean("redstoneUpgrade", redstone.hasIntegrated());
        NBT.putBoolean("lodestoneUpgrade", hasLodestone);
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        redstone.reset();
        if (NBT.getBooleanOr("redstoneUpgrade", false))
            redstone.addIntegrated();
        hasLodestone = NBT.getBooleanOr("lodestoneUpgrade", false);
    }

    @Override
    protected String getTEName() {
        return "magnetizer";
    }

    @Override
    public int getRedstoneOverride() {
        return !this.areConditionsMet() ? 15 : 0;
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
    public Block getBlockEntityBlockID() {
        return null;
    }
}
