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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.base.blockentity.BlockEntityInventoriedCannon;
import reika.rotarycraft.entities.EntityRailGunShot;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryItems;

/**
 * The Rail Gun is an inventoried auto-turret that fires hypervelocity slugs ({@link EntityRailGunShot})
 * at the nearest valid target, consuming a piece of rail-gun ammo per shot. The slug's power (and thus
 * damage + impact blast) scales with the input torque. Built on the shared inventoried-turret base.
 */
public class BlockEntityRailGun extends BlockEntityInventoriedCannon {

    public BlockEntityRailGun(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.RAILGUN.get(), pos, state);
    }

    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity(); // aims via the base
        this.getPowerBelow();
        if (power < MINPOWER)
            return;
        if (!this.hasAmmo())
            return;
        if (!this.isAimingAtTarget(world, pos, target))
            return;
        if (tickcount < this.getOperationTime())
            return;
        tickcount = 0;
        if (target[3] == 1 && !world.isClientSide())
            this.fire(world, target);
    }

    // ==== IItemHandler: the ammo inventory, delegated to itemHandler (only ammo accepted) ====

    @Override
    public int getSlots() {
        return itemHandler.getSlots();
    }

    @Override
    public net.minecraft.world.item.ItemStack insertItem(int slot, net.minecraft.world.item.ItemStack stack, boolean simulate) {
        return this.isItemValid(slot, stack) ? itemHandler.insertItem(slot, stack, simulate) : stack;
    }

    @Override
    public net.minecraft.world.item.ItemStack extractItem(int slot, int amount, boolean simulate) {
        return itemHandler.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return itemHandler.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, net.minecraft.world.item.ItemStack stack) {
        return stack.getItem() == RotaryItems.RAILGUN_AMMO.get();
    }

    // ==== Container ====

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < itemHandler.getSlots(); i++)
            if (!itemHandler.getStackInSlot(i).isEmpty())
                return false;
        return true;
    }

    @Override
    public net.minecraft.world.item.ItemStack getItem(int slot) {
        return itemHandler.getStackInSlot(slot);
    }

    @Override
    public net.minecraft.world.item.ItemStack removeItem(int slot, int amount) {
        return itemHandler.extractItem(slot, amount, false);
    }

    @Override
    public net.minecraft.world.item.ItemStack removeItemNoUpdate(int slot) {
        net.minecraft.world.item.ItemStack s = itemHandler.getStackInSlot(slot);
        itemHandler.setStackInSlot(slot, net.minecraft.world.item.ItemStack.EMPTY);
        return s;
    }

    @Override
    public void setItem(int slot, net.minecraft.world.item.ItemStack stack) {
        itemHandler.setStackInSlot(slot, stack);
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < itemHandler.getSlots(); i++)
            itemHandler.setStackInSlot(i, net.minecraft.world.item.ItemStack.EMPTY);
    }

    @Override
    public boolean stillValid(net.minecraft.world.entity.player.Player player) {
        return !isRemoved() && player.distanceToSqr(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5) <= 64;
    }

    @Override
    public boolean hasAmmo() {
        return ReikaInventoryHelper.locateInInventory(RotaryItems.RAILGUN_AMMO.get(), itemHandler) >= 0;
    }

    @Override
    public void fire(Level world, double[] xyz) {
        int slot = ReikaInventoryHelper.locateInInventory(RotaryItems.RAILGUN_AMMO.get(), itemHandler);
        if (slot < 0)
            return;
        ReikaInventoryHelper.decrStack(slot, itemHandler);
        int pw = Math.max(1, (int) ReikaMathLibrary.logbase2(Math.max(1, torque)));
        BlockPos vpos = BlockPos.containing(xyz[0], xyz[1], xyz[2]);
        world.addFreshEntity(new EntityRailGunShot(world, worldPosition, vpos, this, pw));
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
        return 164;
    }

    @Override
    public int getMaxRange() {
        return 256;
    }

    @Override
    public int getContainerSize() {
        return 54;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.RAILGUN;
    }

    @Override
    protected String getTEName() {
        return "railgun";
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.RAILGUN.get();
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
