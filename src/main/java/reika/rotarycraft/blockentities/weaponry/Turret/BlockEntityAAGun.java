/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.weaponry.Turret;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.base.blockentity.BlockEntityInventoriedCannon;
import reika.rotarycraft.entities.EntityFlakShot;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

/**
 * The AA Gun is an anti-aircraft auto-turret: it only targets airborne mobs/players and fires flak
 * rounds ({@link EntityFlakShot}) that burst near them, consuming gunpowder per shot. Built on the
 * shared inventoried-turret base.
 */
public class BlockEntityAAGun extends BlockEntityInventoriedCannon {

    public BlockEntityAAGun(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.AA_GUN.get(), pos, state);
    }

    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        this.getPowerBelow();
        if (power < MINPOWER || !this.hasAmmo())
            return;
        if (!this.isAimingAtTarget(world, pos, target))
            return;
        if (tickcount < this.getOperationTime())
            return;
        tickcount = 0;
        if (target[3] == 1 && !world.isClientSide())
            this.fire(world, target);
    }

    @Override
    public boolean hasAmmo() {
        return ReikaInventoryHelper.locateInInventory(Items.GUNPOWDER, itemHandler) >= 0;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return stack.getItem() == Items.GUNPOWDER;
    }

    @Override
    public void fire(Level world, double[] xyz) {
        int slot = ReikaInventoryHelper.locateInInventory(Items.GUNPOWDER, itemHandler);
        if (slot < 0)
            return;
        ReikaInventoryHelper.decrStack(slot, itemHandler);
        BlockPos vpos = BlockPos.containing(xyz[0], xyz[1], xyz[2]);
        world.addFreshEntity(new EntityFlakShot(world, worldPosition, vpos, this));
    }

    @Override
    protected double randomOffset() {
        return level.getRandom().nextDouble() - 0.5;
    }

    @Override
    protected boolean isValidTarget(Entity ent) {
        // Anti-air: only things off the ground (flying mobs, players).
        return ent instanceof LivingEntity le && this.isMobOrUnlistedPlayer(le) && !ent.onGround();
    }

    @Override
    protected double[] getTarget(Level world, BlockPos pos) {
        double[] xyzb = new double[4];
        int r = this.getRange();
        AABB box = new AABB(pos.getX() - r, pos.getY() - r, pos.getZ() - r, pos.getX() + 1 + r, pos.getY() + 1 + r, pos.getZ() + 1 + r);
        double mindist = r + 2;
        Entity best = null;
        for (Entity ent : world.getEntitiesOfClass(Entity.class, box)) {
            if (!this.isValidTarget(ent))
                continue;
            double dist = ReikaMathLibrary.py3d(ent.getX() - pos.getX() - 0.5, ent.getY() - pos.getY() - 0.5, ent.getZ() - pos.getZ() - 0.5);
            if (dist >= mindist)
                continue;
            if (!ReikaWorldHelper.canBlockSee(world, pos.getX(), pos.getY(), pos.getZ(), ent.getX(), ent.getY(), ent.getZ(), r))
                continue;
            mindist = dist;
            best = ent;
        }
        if (best == null)
            return xyzb;
        closestMob = best;
        xyzb[0] = best.getX() + this.randomOffset();
        xyzb[1] = best.getY() + best.getEyeHeight() * 0.25 + this.randomOffset();
        xyzb[2] = best.getZ() + this.randomOffset();
        xyzb[3] = 1;
        return xyzb;
    }

    @Override
    public int getRange() {
        return 128;
    }

    @Override
    public int getMaxRange() {
        return 256;
    }

    @Override
    public int getContainerSize() {
        return 27;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.ANTIAIR;
    }

    @Override
    protected String getTEName() {
        return "antiair";
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.AA_GUN.get();
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
    }

    @Override
    public boolean hasATank() {
        return false;
    }

    @Override
    public boolean hasAnInventory() {
        return true;
    }
}
