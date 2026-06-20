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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
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
    public int getFuelLevel() {
        return 0;
    }

    @Override
    protected void affectSurroundings(Level world, BlockPos pos) {
        if (!world.isClientSide()) {
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
        return !this.hasRedstoneUpgrade() && RotaryItems.UPGRADE.get() == item.getItem() && item.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag().getStringOr("upgradeType", "").equals(ItemEngineUpgrade.UpgradeType.REDSTONE.name());
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
        hasUpgrade = tag.getBooleanOr("redstoneUpgrade", false);
    }

    @Override
    protected String getTEName() {
        return "dcengine";
    }

    @Override
    public void breakBlock() {
        super.breakBlock();
        if (this.hasRedstoneUpgrade()) {
            // 1.21.5: ItemStack(Item, int, CompoundTag) constructor removed and CompoundTag became final.
            // Attach the upgrade type via the CUSTOM_DATA component instead.
            ItemStack upgrade = new ItemStack(RotaryItems.UPGRADE.get(), 1);
            reika.dragonapi.libraries.registry.ReikaItemHelper.updateStackTag(upgrade, __T__ -> __T__.putString("upgrade", ItemEngineUpgrade.UpgradeType.REDSTONE.name()));
            ReikaItemHelper.dropItem(level, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, upgrade);
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
