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
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.interfaces.RedstoneUpgradeable;
import reika.rotarycraft.base.blockentity.BlockEntityEngine;
import reika.rotarycraft.items.tools.ItemEngineUpgrade;
import reika.rotarycraft.registry.*;

public class BlockEntityDCEngine extends BlockEntityEngine implements RedstoneUpgradeable {

    private boolean hasUpgrade;

    public BlockEntityDCEngine(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.DC_ENGINE.get(), pos, state, false, false, false, false);
        type = EngineType.DC;
    }
    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.DC_ENGINE;
    }
    @Override
    protected void consumeFuel() {

    }

    @Override
    protected void internalizeFuel() {

    }

    @Override
    protected boolean getRequirements(Level world, BlockPos pos) {
//        RotaryCraft.LOGGER.info("Checking requirements for DC Engine at " + pos + "Redstone signal is:" + this.hasRedstoneSignal());
        return hasUpgrade || this.hasRedstoneSignal();
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

    @Override
    public int getFuelLevel() {
        return 0;
    }

    @Override
    protected void affectSurroundings(Level world, BlockPos pos) {
        if (!world.isClientSide) {
//            if (getAdjacentBlockEntity(write) instanceof BlockEntityExtractor) {
//                RotaryAdvancements.DUMBEXTRACTOR.triggerAchievement(this.getPlacer());
//            }
        }
    }

    @Override
    public void upgrade(ItemStack is) {
        this.addRedstoneUpgrade();
    }

    @Override
    public boolean canUpgradeWith(ItemStack item) {
        return !this.hasRedstoneUpgrade() && RotaryItems.UPGRADE.get() == item.getItem() && item.getTag().getString("upgradeType").equals(ItemEngineUpgrade.UpgradeType.REDSTONE.name());
    }

    @Override
    public void addRedstoneUpgrade() {
        hasUpgrade = true;
    }

    @Override
    public boolean hasRedstoneUpgrade() {
        return hasUpgrade;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.DC_ENGINE.get();
    }

    @Override
    protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag);
        tag.putBoolean("redstoneUpgrade", hasUpgrade);
    }

    @Override
    protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag);
        hasUpgrade = tag.getBoolean("redstoneUpgrade");
    }

    @Override
    protected String getTEName() {
        return "dcengine";
    }

    @Override
    public void breakBlock() {
        super.breakBlock();
        if (this.hasRedstoneUpgrade()) {
            ReikaItemHelper.dropItem(level, worldPosition.offset(0.5, 0.5, 0.5), new ItemStack(RotaryItems.UPGRADE.get(), 1, new CompoundTag() {{
                putString("upgrade", ItemEngineUpgrade.UpgradeType.REDSTONE.name());
            }}));
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
    public int fillPipe(Direction from, FluidStack resource, IFluidHandler.FluidAction action) {
        return 0;
    }

    @Override
    public FluidStack drainPipe(Direction from, int maxDrain, IFluidHandler.FluidAction doDrain) {
        return null;
    }
}
