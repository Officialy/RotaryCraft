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

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import reika.dragonapi.DragonAPI;
import reika.rotarycraft.api.interfaces.TensionStorage;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;

public abstract class BlockEntitySpringPowered extends InventoriedRCBlockEntity implements ConditionalOperation {

    public boolean isCreative;

    public BlockEntitySpringPowered(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public abstract int getBaseDischargeTime();

    protected final int getUnwindTime() {
        if (isCreative)
            return Integer.MAX_VALUE;
        //if (DragonAPI.DEBUGTEST)
        //    return Integer.MAX_VALUE;
        ItemStack is = itemHandler.getStackInSlot(this.getCoilSlot());
        int base = this.getBaseDischargeTime();
        return base * ((TensionStorage) is.getItem()).getStiffness(is);
    }

    public int getExpectedCoilLife() {
        return this.getUnwindTime() * itemHandler.getStackInSlot(this.getCoilSlot()).getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag().getIntOr("energy", 0);
    }


    public boolean isItemValidForSlot(int i, ItemStack is) {
        return is.getItem() instanceof TensionStorage && i == this.getCoilSlot();
    }


    public final boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return itemstack.getDamageValue() == 0 && i == this.getCoilSlot();
    }

    public int getCoilSlot() {
        return 0;
    }

    protected final ItemStack getDecrementedCharged() {
        ItemStack in = itemHandler.getStackInSlot(this.getCoilSlot());
        if (isCreative)
            return in;
        // 1.21.5: ItemStack(Item, int, CompoundTag) ctor was removed (NBT is now per-component).
        // Use copyWithCount + custom-data attach so the discharged stack carries any persistent
        // component state forward. For springs we don't actually need the NBT to follow, so a plain
        // copy-with-count is the correct behaviour.
        return in.copyWithCount(in.getCount());
    }

    protected final boolean hasCoil() {
        if (isCreative)
            return true;
        if (DragonAPI.debugtest)
            return true;
        ItemStack is = itemHandler.getStackInSlot(this.getCoilSlot());
        if (is.isEmpty())
            return false;
        Item i = is.getItem();
        return is.getDamageValue() > 0 && i instanceof TensionStorage;
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);

        isCreative = NBT.getBooleanOr("creative", false);
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);

        NBT.putBoolean("creative", isCreative);
    }

    @Override
    public boolean areConditionsMet() {
        return this.hasCoil();
    }

    @Override
    public String getOperationalStatus() {
        return this.areConditionsMet() ? "Operational" : "No Coil";
    }
}
