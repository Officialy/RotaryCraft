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
        ItemStack is = inv[this.getCoilSlot()];
        int base = this.getBaseDischargeTime();
        return base * ((TensionStorage) is.getItem()).getStiffness(is);
    }

    public int getExpectedCoilLife() {
        return this.getUnwindTime() * inv[this.getCoilSlot()].getDamageValue();
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
        ItemStack in = inv[this.getCoilSlot()];
        if (isCreative)
            return in;
        return new ItemStack(in.getItem(), in.getCount(), getUpdateTag()); //todo check if this tag is fucked
    }

    protected final boolean hasCoil() {
        if (isCreative)
            return true;
        if (DragonAPI.debugtest)
            return true;
        ItemStack is = inv[this.getCoilSlot()];
        if (is == null)
            return false;
        Item i = is.getItem();
        return is.getDamageValue() > 0 && i instanceof TensionStorage;
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);

        isCreative = NBT.getBoolean("creative");
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
