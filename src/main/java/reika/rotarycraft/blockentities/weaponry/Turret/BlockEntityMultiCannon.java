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
import reika.rotarycraft.entities.EntityGatlingShot;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

/**
 * The Multi-Cannon is a rapid-fire gatling auto-turret: it pours a stream of low-damage rounds
 * ({@link EntityGatlingShot}) into the nearest valid target, consuming an iron nugget per round.
 * Built on the shared inventoried-turret base; its very short operation time gives the high rate of
 * fire.
 */
public class BlockEntityMultiCannon extends BlockEntityInventoriedCannon {

    public BlockEntityMultiCannon(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.MULTI_CANNON.get(), pos, state);
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
    public int getOperationTime() {
        return 2; //rapid fire
    }

    @Override
    public boolean hasAmmo() {
        return ReikaInventoryHelper.locateInInventory(Items.IRON_NUGGET, itemHandler) >= 0;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return stack.getItem() == Items.IRON_NUGGET;
    }

    @Override
    public void fire(Level world, double[] xyz) {
        int slot = ReikaInventoryHelper.locateInInventory(Items.IRON_NUGGET, itemHandler);
        if (slot < 0)
            return;
        ReikaInventoryHelper.decrStack(slot, itemHandler);
        BlockPos vpos = BlockPos.containing(xyz[0], xyz[1], xyz[2]);
        world.addFreshEntity(new EntityGatlingShot(world, worldPosition, vpos, this));
    }

    @Override
    protected double randomOffset() {
        return (level.getRandom().nextDouble() - 0.5) * 1.5; //spray
    }

    @Override
    protected boolean isValidTarget(Entity ent) {
        return ent instanceof LivingEntity le && this.isMobOrUnlistedPlayer(le);
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
            double dy = -(ent.getY() - pos.getY());
            double reqtheta = -90 + Math.toDegrees(Math.abs(Math.acos(dy / dist)));
            if ((reqtheta <= dir * MAXLOWANGLE && dir == -1) || (reqtheta >= dir * MAXLOWANGLE && dir == 1)) {
                mindist = dist;
                best = ent;
            }
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
        return 96;
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
        return MachineRegistry.GATLING;
    }

    @Override
    protected String getTEName() {
        return "gatling";
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.MULTI_CANNON.get();
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
