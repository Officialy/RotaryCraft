///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.weaponry.Turret;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.phys.AABB;
//import org.jetbrains.annotations.NotNull;
//import reika.dragonapi.libraries.ReikaInventoryHelper;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.rotarycraft.api.Interfaces.RailGunAmmo;
//import reika.rotarycraft.api.Interfaces.RailGunAmmo.RailGunAmmoType;
//import reika.rotarycraft.api.Interfaces.TargetEntity;
//import reika.rotarycraft.base.blockentity.BlockEntityInventoriedCannon;
//import reika.rotarycraft.registry.MachineRegistry;
//
//import java.util.List;
//
//public class BlockEntityRailGun extends BlockEntityInventoriedCannon {
//
//    private RailGunAmmoType ammoType;
//
//    private static RailGunAmmoType getAmmo(ItemStack is) {
//        if (is == null)
//            return null;
//        if (is.getItem() instanceof RailGunAmmo) {
//            return ((RailGunAmmo) is.getItem()).getAmmo(is);
//        }
//        return null;
//    }
//
//    @Override
//    public boolean hasAmmo() {
//        ammoType = null;
//        this.checkAmmo();
//        return ammoType != null;
//    }
//
//    private void checkAmmo() {
//        for (int i = 0; i < inv.length; i++) {
//            RailGunAmmoType rg = getAmmo(inv[i]);
//            if (rg != null) {
//                if (torque >= rg.getRequiredTorque()) {
//                    if (ammoType == null || rg.compareTo(ammoType) > 0)
//                        ammoType = rg;
//                }
//            }
//        }
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        if (power < MINPOWER)
//            return;
//        if (!this.hasAmmo())
//            return;
//        if (!this.isAimingAtTarget(world, pos, target))
//            return;
//        if (tickcount < this.getOperationTime())
//            return;
//        tickcount = 0;
//        if (target[3] == 1) {
//            if (!world.isClientSide)
//                this.fire(world, target);
//        }
//    }
//
//    @Override
//    protected double[] getTarget(Level world, BlockPos pos) {
//        double[] xyzb = new double[4];
//        int r = this.getRange();
//        AABB range = new AABB(pos.getX() - r, pos.getY() - r, pos.getZ() - r, pos.getX() + 1 + r, pos.getY() + 1 + r, pos.getZ() + 1 + r);
//        List<Entity> inrange = world.getEntitiesOfClass(Entity.class, range);
//        double mindist = this.getRange() + 2;
//        Entity i_at_min = null;
//        for (Entity ent : inrange) {
//            double dist = ReikaMathLibrary.py3d(ent.getX() - pos.getX() - 0.5, ent.getY() - pos.getY() - 0.5, ent.getZ() - pos.getZ() - 0.5);
//            if (this.isValidTarget(ent)) {
//                if (ReikaWorldHelper.canBlockSee(world, pos, ent.getY(), ent.getY(), ent.getZ(), this.getRange())) {
//                    double dy = -(ent.getY() - pos.getY());
//                    double reqtheta = -90 + Math.toDegrees(Math.abs(Math.acos(dy / dist)));
//                    if ((reqtheta <= dir * MAXLOWANGLE && dir == -1) || (reqtheta >= dir * MAXLOWANGLE && dir == 1)) {
//                        if (dist < mindist) {
//                            mindist = dist;
//                            i_at_min = ent;
//                        }
//                    }
//                }
//            }
//        }
//        if (i_at_min == null)
//            return xyzb;
//        closestMob = i_at_min;
//        xyzb[0] = closestMob.getX() + this.randomOffset();
//        xyzb[1] = closestMob.getY() + closestMob.getEyeHeight() * 0.25 + this.randomOffset();
//        xyzb[2] = closestMob.getZ() + this.randomOffset();
//        xyzb[3] = 1;
//        return xyzb;
//    }
//
//    @Override
//    public void fire(Level world, double[] xyz) {
//        double speed = 4;
//        ItemStack is = ammoType.getItem();
//        int slot = ReikaInventoryHelper.locateInInventory(is.getItem(), inv);
//        ReikaInventoryHelper.decrStack(slot, inv);
//        double[] v = new double[3];
//        v[0] = xyz[0] - worldPosition.getX();
//        v[1] = xyz[1] - worldPosition.getY();
//        v[2] = xyz[2] - worldPosition.getZ();
//        double dd = ReikaMathLibrary.py3d(v[0], v[1], v[2]);
//        for (int i = 0; i < 3; i++)
//            v[i] /= dd;
//        for (int i = 0; i < 3; i++)
//            v[i] *= speed;
//        dd = ReikaMathLibrary.py3d(v[0], v[1], v[2]);
//        double dx = v[0] / dd;
//        double dy = v[1] / dd;
//        double dz = v[2] / dd;
//        //ReikaJavaLibrary.pConsole(dx+"  "+dy+"  "+dz);
//        if (!world.isClientSide) {
//            double y = this.getFiringPositionY(dy);
//            Entity e = ammoType.getProjecBlockEntity(world, new BlockPos(worldPosition.getX() + 0.5 + dx, y, worldPosition.getZ() + 0.5 + dz), new BlockPos(v[0], v[1], v[2]), this);
//            if (e != null) {
//                world.addFreshEntity(e);
//            }
//        }
//    }
//
//    public int getRange() {
//        return 164;
//    }
//
//	/*
//    public AABB getRenderBoundingBox() {
//        return INFINITE_EXTENT_AABB;
//    }*/
//
//    public Entity getClosestMob() {
//        return closestMob;
//    }
//
//    @Override
//    protected double randomOffset() {
//        //return -0.5+par5Random.nextFloat();
//        return 0;
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.RAILGUN;
//    }
//
//
//    public boolean isItemValidForSlot(int slot, ItemStack is) {
//        return is.getItem() instanceof RailGunAmmo;
//    }
//
//    @Override
//    public int getMaxRange() {
//        return 256;
//    }
//
//    @Override
//    protected boolean isValidTarget(Entity ent) {
//        if (ent.isAlive())
//            return false;
//        if (ent instanceof TargetEntity)
//            return ((TargetEntity) ent).shouldTarget(this, placerUUID);
//        if (!(ent instanceof LivingEntity))
//            return false;
//        LivingEntity elb = (LivingEntity) ent;
//        return elb.getHealth() > 0 && this.isMobOrUnlistedPlayer(elb);
//    }
//
//    @Override
//    public int getContainerSize() {
//        return 54;
//    }
//
//    @Override
//    public boolean isEmpty() {
//        return false;
//    }
//
//    @Override
//    public ItemStack getItem(int pIndex) {
//        return null;
//    }
//
//    @Override
//    public ItemStack removeItem(int pIndex, int pCount) {
//        return null;
//    }
//
//    @Override
//    public ItemStack removeItemNoUpdate(int pIndex) {
//        return null;
//    }
//
//    @Override
//    public void setItem(int pIndex, ItemStack pStack) {
//
//    }
//
//    @Override
//    public boolean stillValid(Player pPlayer) {
//        return false;
//    }
//
//    @Override
//    public void clearContent() {
//
//    }
//
//    @Override
//    public int getSlots() {
//        return 0;
//    }
//
//    @NotNull
//    @Override
//    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
//        return null;
//    }
//
//    @NotNull
//    @Override
//    public ItemStack extractItem(int slot, int amount, boolean simulate) {
//        return null;
//    }
//
//    @Override
//    public int getSlotLimit(int slot) {
//        return 0;
//    }
//
//    @Override
//    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
//        return false;
//    }
//
//    @Override
//    public String getName() {
//        return null;
//    }
//
//    @Override
//    public boolean hasAnInventory() {
//        return false;
//    }
//
//    @Override
//    public boolean hasATank() {
//        return false;
//    }
//}
