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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;


public abstract class RemoteControlMachine extends BlockEntitySpringPowered {

    public int[] colors = new int[3];

    protected int tickcount2 = 0;
    protected boolean on = false;

    public RemoteControlMachine(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public abstract void activate(Level world, Player ep, BlockPos pos);

    protected void setColors() {
        for (int i = 0; i < 3; i++) {
            if (inv[i + 1] == null)
                colors[i] = -1;
            else if (inv[i + 1].getItem() != Items.BLACK_DYE)
                colors[i] = -1;
            else
                colors[i] = inv[i + 1].getDamageValue();
            if (colors[i] == -1)
                on = false;
        }
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        colors = NBT.getIntArray("color");
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        NBT.putIntArray("color", colors);
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack is) {
        if (i == 0)
            return super.isItemValidForSlot(i, is);
        return is.getItem() == Items.BLACK_DYE;
    }

    /*@Override
    public int getContainerSize() {
        return 4;
    }*/

    @Override
    public int getBaseDischargeTime() {
        return 120;
    }

}
