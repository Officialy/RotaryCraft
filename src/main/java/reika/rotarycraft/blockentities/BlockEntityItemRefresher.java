/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;

import java.util.List;

public class BlockEntityItemRefresher extends BlockEntityPowerReceiver implements RangedEffect {

    public static final int FALLOFF = 1024;

    public BlockEntityItemRefresher(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.REFRESHER.get(), pos, state);
    }

    @Override
    public Block getBlockEntityBlockID() {
        return null;
    }

    //@Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        this.getSummativeSidedPower();
        if (power < MINPOWER)
            return;
        int range = this.getRange();
        AABB box = new AABB(pos.getX() - range, pos.getY() - range, pos.getZ() - range, pos.getX() + 1 + range, pos.getY() + 1 + range, pos.getZ() + 1 + range);
        List<ItemEntity> items = world.getEntitiesOfClass(ItemEntity.class, box);
        for (ItemEntity item : items) {
            if (item.getAge() > item.lifespan - 20)
                item.getPersistentData().putInt("Age", item.lifespan - 20);//todo check if this works
            if (item.yo == 0)
                item.yo = 0.4;
        }

    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    protected String getTEName() {
        return "itemrefresher";
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public int getRange() {
        return Math.min(this.getMaxRange(), 4 + (int) (power - MINPOWER) / FALLOFF);
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.REFRESHER;
    }

    @Override
    public int getMaxRange() {
        return 128;
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
