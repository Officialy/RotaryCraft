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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
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
        ItemStack is = inv[0];
        if (is == null)
            return false;
        if (!ReikaItemHelper.matchStacks(is, RotaryItems.HSLA_SHAFT_CORE) && !ReikaItemHelper.matchStacks(is, RotaryItems.TUNGSTEN_ALLOY_SHAFT_CORE))
            return false;
        if (is.getTag() == null)
            return false;
        if (!is.getTag().contains("magnet"))
            return false;
        if (is.getTag().getInt("magnet") <= 0)
            return false;

        redstone.update(world, pos);
        boolean ac = redstone.isAlternating();

        if (!world.isClientSide && ac && timer.checkCap("fuel")) {
            if (ReikaItemHelper.matchStacks(is, RotaryItems.HSLA_SHAFT_CORE) || DragonAPI.rand.nextBoolean()) {
                int m = is.getTag().getInt("magnet");
                this.magnetize(is, m - 1);
            }
        }

        return ac;
    }

    private void magnetize(ItemStack is, int amt) {
        if (amt > 0)
            is.getTag().putInt("magnet", amt);
        else {
            is.getTag().remove("magnet");
            if (is.getTag().isEmpty())
                is.save(null);
        }
    }

    @Override
    protected void playSounds(Level world, BlockPos pos, float pitchMultiplier, float volume) {
        soundtick++;
        if (this.isMuffled(world, pos)) {
            volume *= 0.3125F;
        }

        if (soundtick < this.getSoundLength(1F / pitchMultiplier) && soundtick < 2000)
            return;
        soundtick = 0;

        SoundRegistry.ELECTRIC.playSoundAtBlock(world, pos, 0.125F * volume, 1F * pitchMultiplier);
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
        return inv[0] != null && inv[0].getTag() != null ? inv[0].getTag().getInt("magnet") : 0;
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
        if (NBT.getBoolean("redstoneUpgrade"))
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
            ReikaItemHelper.dropItem(level, new BlockPos(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5), RotaryItems.UPGRADE.get().getDefaultInstance());//.getUpgradeType(Upgrades.REDSTONE.ordinal()));
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

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getItem(int pIndex) {
        return null;
    }

    @Override
    public ItemStack removeItem(int pIndex, int pCount) {
        return null;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pIndex) {
        return null;
    }

    @Override
    public void setItem(int pIndex, ItemStack pStack) {

    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return false;
    }

    @Override
    public void clearContent() {

    }

    @Override
    public int fillPipe(Direction from, FluidStack resource, IFluidHandler.FluidAction action) {
        return 0;
    }

    @Override
    public FluidStack drainPipe(Direction from, int maxDrain, IFluidHandler.FluidAction doDrain) {
        return null;
    }
}
