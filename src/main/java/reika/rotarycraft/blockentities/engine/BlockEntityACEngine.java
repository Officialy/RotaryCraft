/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.engine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import reika.dragonapi.DragonAPI;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.auxiliary.RedstoneCycleTracker;
import reika.rotarycraft.auxiliary.interfaces.MagnetizationCore;
import reika.rotarycraft.base.blockentity.BlockEntityEngine;
import reika.rotarycraft.registry.*;

public class BlockEntityACEngine extends BlockEntityEngine implements MagnetizationCore {

    private final RedstoneCycleTracker redstone = new RedstoneCycleTracker(3);

    public BlockEntityACEngine(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.AC_ENGINE.get(), pos, state, false, false, false, false);
        // Without this the engine inherits the base default (EngineType.DC) until setType() runs,
        // so a freshly-placed AC engine renders the DC model for a frame before correcting itself.
        type = EngineType.AC;
    }
    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.AC_ENGINE;
    }
    @Override
    protected void consumeFuel() {

    }

    @Override
    protected void internalizeFuel() {

    }

    @Override
    protected boolean getRequirements(Level world, BlockPos pos) {
        ItemStack is = itemHandler.getStackInSlot(0);
        if (is.isEmpty())
            return false;
        if (!ReikaItemHelper.matchStacks(is, RotaryItems.HSLA_SHAFT_CORE) && !ReikaItemHelper.matchStacks(is, RotaryItems.TUNGSTEN_ALLOY_SHAFT_CORE))
            return false;
        if (is.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag() == null)
            return false;
        if (!is.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().contains("magnet"))
            return false;
        if (is.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getIntOr("magnet", 0) <= 0)
            return false;

        redstone.update(world, pos);
        boolean ac = redstone.isAlternating();

        if (!world.isClientSide() && ac && timer.checkCap("fuel")) {
            if (ReikaItemHelper.matchStacks(is, RotaryItems.HSLA_SHAFT_CORE) || DragonAPI.rand.nextBoolean()) {
                int m = is.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getIntOr("magnet", 0);
                this.magnetize(is, m - 1);
            }
        }

        return ac;
    }

    private void magnetize(ItemStack is, int amt) {
        if (amt > 0) {
            ReikaItemHelper.updateStackTag(is, __T__ -> __T__.putInt("magnet", amt));
        } else {
            ReikaItemHelper.updateStackTag(is, __T__ -> __T__.remove("magnet"));
            // 1.21.5: if the custom-data tag is now empty, drop the component entirely so the
            // stack doesn't serialise an empty CUSTOM_DATA on save (legacy behaviour: setTag(null)).
            if (is.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().isEmpty()) {
                is.remove(DataComponents.CUSTOM_DATA);
            }
        }
    }

    public void magneticInterference(int mag, double dd) {
        torque = (int) (0.0625 * ReikaMathLibrary.logbase(mag, 2) * this.getEngineType().getTorque() / dd);
        omega = (int) (0.0625 * ReikaMathLibrary.logbase(mag, 2) * this.getEngineType().getSpeed() / dd / 4D);
        power = (long) omega * (long) torque;
    }

    @Override
    public int getFuelLevel() {
        return 0;
    }

    @Override
    protected void affectSurroundings(Level world, BlockPos pos) {

    }

    @Override
    public int getCoreMagnetization() {
        return !itemHandler.getStackInSlot(0).isEmpty() && itemHandler.getStackInSlot(0).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag() != null ? itemHandler.getStackInSlot(0).getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getIntOr("magnet", 0) : 0;
    }

    @Override
    public void addRedstoneUpgrade() {
        redstone.addIntegrated();
    }

    @Override
    public boolean hasRedstoneUpgrade() {
        return redstone.hasIntegrated();
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.AC_ENGINE.get();
    }

    @Override
    public void upgrade(ItemStack is) {
        this.addRedstoneUpgrade();
    }

    @Override
    public boolean canUpgradeWith(ItemStack item) {
        return !this.hasRedstoneUpgrade() && RotaryItems.UPGRADE.get() == item.getItem();// && UpgradeItem item.getItemDamage() == Upgrades.REDSTONE.ordinal();
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);

        NBT.putBoolean("redstoneUpgrade", redstone.hasIntegrated());
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);

        redstone.reset();
        if (NBT.getBooleanOr("redstoneUpgrade", false))
            redstone.addIntegrated();
    }

    @Override
    protected String getTEName() {
        return "ac_engine";
    }

    @Override
    public void breakBlock() {
        super.breakBlock();
        if (this.hasRedstoneUpgrade()) {
            ReikaItemHelper.dropItem(level,worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, RotaryItems.UPGRADE.get().getDefaultInstance());//.getUpgradeType(Upgrades.REDSTONE.ordinal()));
        }
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
    public int getAmbientTemperature() {
        return 0;
    }
}
