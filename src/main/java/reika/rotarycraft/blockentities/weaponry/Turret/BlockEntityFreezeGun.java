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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.base.blockentity.BlockEntityInventoriedCannon;
import reika.rotarycraft.entities.EntityFreezeGunShot;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

/**
 * The Freeze Gun is an auto-turret that fires supercooled snowball slugs which deep-freeze whatever
 * they hit. It also converts fed snow (→ 4 snowballs) and ice (→ 16 snowballs) into ammo. Built on
 * the shared inventoried-turret base.
 */
public class BlockEntityFreezeGun extends BlockEntityInventoriedCannon {

    public BlockEntityFreezeGun(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.FREEZE_GUN.get(), pos, state);
    }

    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        this.getPowerBelow();
        this.convertSnow();
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

    private void convertSnow() {
        int snow = ReikaInventoryHelper.locateInInventory(Blocks.SNOW.asItem(), itemHandler);
        if (snow >= 0) {
            ReikaInventoryHelper.decrStack(snow, itemHandler);
            ReikaInventoryHelper.addToIInv(new ItemStack(Items.SNOWBALL, 4), this);
        }
        int ice = ReikaInventoryHelper.locateInInventory(Blocks.ICE.asItem(), itemHandler);
        if (ice >= 0) {
            ReikaInventoryHelper.decrStack(ice, itemHandler);
            ReikaInventoryHelper.addToIInv(new ItemStack(Items.SNOWBALL, 16), this);
        }
    }

    @Override
    public boolean hasAmmo() {
        return ReikaInventoryHelper.locateInInventory(Items.SNOWBALL, itemHandler) >= 0;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return stack.getItem() == Items.SNOWBALL || stack.getItem() == Blocks.SNOW.asItem() || stack.getItem() == Blocks.ICE.asItem();
    }

    @Override
    public void fire(Level world, double[] xyz) {
        int slot = ReikaInventoryHelper.locateInInventory(Items.SNOWBALL, itemHandler);
        if (slot < 0)
            return;
        ReikaInventoryHelper.decrStack(slot, itemHandler);
        BlockPos vpos = BlockPos.containing(xyz[0], xyz[1], xyz[2]);
        world.addFreshEntity(new EntityFreezeGunShot(world, worldPosition, vpos, this));
    }

    @Override
    protected double randomOffset() {
        return 0;
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
        return 64;
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
        return MachineRegistry.FREEZEGUN;
    }

    @Override
    protected String getTEName() {
        return "freezegun";
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.FREEZE_GUN.get();
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
