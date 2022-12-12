/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.base.blockentity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
import reika.rotarycraft.registry.MachineRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

public abstract class BlockEntityLaunchCannon extends InventoriedPowerReceiver implements DiscreteFunction, ConditionalOperation {

    public int velocity;
    public int phi;
    public int theta;

    public boolean targetMode = false;

    public int[] target = new int[3];

    public BlockEntityLaunchCannon(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void updateBlockEntity() {
    }

    protected abstract boolean fire(Level world, BlockPos pos, int slot);

    public abstract int getMaxLaunchVelocity();

    public abstract int getMaxTheta();

    public abstract double getMaxLaunchDistance();

    @Override
    protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag);
        velocity = tag.getInt("svelocity");
        phi = tag.getInt("sphi");
        theta = tag.getInt("stheta");
        targetMode = tag.getBoolean("istarget");
        target = tag.getIntArray("targetxyz");
    }

    @Override
    public MachineRegistry getMachine() {
        return null;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag);
        tag.putInt("svelocity", velocity);
        tag.putInt("sphi", phi);
        tag.putInt("stheta", theta);
        tag.putBoolean("istarget", targetMode);
        tag.putIntArray("targetxyz", target);
    }

    public int getOperationTime() {
        return 10;
    }

    @Override
    public boolean areConditionsMet() {
        //return !ReikaInventoryHelper.isEmpty(inv);
        return false; // TODO: add isEmpty
    }

    @Override
    public String getOperationalStatus() {
        return this.areConditionsMet() ? "Operational" : "No Ammunition";
    }

    @Override
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    public boolean hasATank() {
        return false;
    }
}
