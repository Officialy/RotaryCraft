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
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.phys.AABB;
//
//import reika.dragonapi.libraries.ReikaInventoryHelper;
//import reika.dragonapi.libraries.java.ReikaArrayHelper;
//import reika.dragonapi.libraries.java.ReikaRandomHelper;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.dragonapi.libraries.mathsci.ReikaPhysicsHelper;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.rotarycraft.api.Interfaces.TargetEntity;
//
//import reika.rotarycraft.base.blockentity.BlockEntityInventoriedCannon;
//
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.SoundRegistry;
//
//import java.util.List;
//
//public class BlockEntityMultiCannon extends BlockEntityInventoriedCannon {
//
//    public static final int LOAD_SLOT = 0;
//    public static final int CLIP_SLOT = 36;
//    public static final String SLIME_NBT = "embedded_rounds";
//    private static final int FIRE_RATE = 1; //ticks per shot
//    private static final int AMMO_RATE = 10; //ticks per unit ammo
//    public static final double AMMO_PER_SHOT = FIRE_RATE / (double) AMMO_RATE;
//    private static final int RELOAD_TIME = 80; //ticks
//    private static final int FEED_TIME = 4;
//    private static final double SPIN_RATE = 25;
//    private static final double SPIN_DELTA = 5;
//    private static final double JITTER = 0.625;
//    private double spinAngle;
//    private double spinSpeed;
//
//    private int reloadTimer;
//    private int feedTick;
//
//    public double getSpinAngle() {
//        return spinAngle;
//    }
//
//    private void startReload() {
//        reloadTimer = RELOAD_TIME;
//        if (inv[CLIP_SLOT - 1] != null)
//            SoundRegistry.GATLINGRELOAD.playSoundAtBlock(this, 0.75F, 0.95F);
//    }
//
//    private void doReload() {
//        reloadTimer--;
//        if (reloadTimer == 0) {
//            ReikaArrayHelper.cycleArray(inv, null);
//            SoundRegistry.PROJECTOR.playSoundAtBlock(this, 2, 0.75F);
//        }
//    }
//
//    @Override
//    public int getContainerSize() {
//        return CLIP_SLOT - LOAD_SLOT + 1;
//    }
//
//    @Override
//    public boolean isItemValidForSlot(int slot, ItemStack is) {
//        return slot == LOAD_SLOT && ReikaItemHelper.matchStacks(is, RotaryItems.ballbearing);
//    }
//
//    @Override
//    public int getRange() {
//        return 120;
//    }
//
//    @Override
//    public int getMaxRange() {
//        return 120;
//    }
//
//    @Override
//    public boolean hasAmmo() {
//        return ReikaItemHelper.matchStacks(RotaryItems.ballbearing, inv[CLIP_SLOT]);
//    }
//
//    @Override
//    protected double[] getTarget(Level world, BlockPos pos) {
//        return new double[0];
//    }
//
//    @Override
//    protected double[] getTarget(Level world, BlockPos pos) {
//        double[] xyzb = new double[4];
//        int r = this.getRange();
//        AABB range = AABB.getBoundingBox(x - r, y - r, z - r, x + 1 + r, y + 1 + r, z + 1 + r);
//        List<Entity> inrange = world.getEntities(Entity.class, range);
//        double mindist = this.getRange() + 2;
//        Entity i_at_min = null;
//        for (Entity ent : inrange) {
//            double dist = ReikaMathLibrary.py3d(ent.getY - x - 0.5, ent.getY() - y - 0.5, ent.posZ - z - 0.5);
//            if (this.isValidTarget(ent)) {
//                if (ReikaWorldHelper.canBlockSee(world, pos, ent.getY, ent.getY(), ent.posZ, this.getRange())) {
//                    double dy = -(ent.getY() - y);
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
//        xyzb[0] = closestMob.getY + this.randomOffset();
//        xyzb[1] = closestMob.getY() + closestMob.getEyeHeight() * 0.25 + this.randomOffset();
//        xyzb[2] = closestMob.posZ + this.randomOffset();
//        xyzb[3] = 1;
//        return xyzb;
//    }
//
//    @Override
//    public void fire(Level world, double[] xyz) {
//        double speed = 1.5;
//        if (ReikaRandomHelper.doWithChance(AMMO_PER_SHOT)) {
//            ReikaInventoryHelper.decrStack(CLIP_SLOT, inv);
//            if (inv[CLIP_SLOT] == null) {
//                this.startReload();
//            }
//        }
//        double[] v = ReikaPhysicsHelper.polarToCartesian(speed, theta, -phi + 90);
//        double dx = v[0];
//        double dy = v[1];
//        double dz = v[2];
//        if (!world.isClientSide) {
//            double y = this.getFiringPositionY(dy);
//            world.addFreshEntity(new EntityGatlingShot(world, xCoord + 0.5 + dx, y, zCoord + 0.5 + dz, v[0], v[1], v[2], this));
//        }
//        SoundRegistry.GATLING.playSoundAtBlock(this, 0.25F, 1.125F);
//        //ReikaSoundHelper.playSoundAtBlock(world, xCoord, yCoord, zCoord, "mob.blaze.hit", 0.25F, 0.25F);
//    }
//
//    @Override
//    protected double randomOffset() {
//        return JITTER;
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
//    public MachineRegistry getMachine() {
//        return MachineRegistry.GATLING;
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//
//        spinAngle += spinSpeed;
//
//        if (power < MINPOWER || omega < MINSPEED) {
//            spinSpeed = Math.max(spinSpeed - SPIN_DELTA, 0);
//            return;
//        }
//
//        if (!world.isClientSide) {
//            if (feedTick > 0)
//                feedTick--;
//            else
//                this.continuousFeed();
//
//            if (reloadTimer == 0) {
//                if (inv[CLIP_SLOT] == null) {
//                    this.startReload();
//                }
//            }
//
//            if (reloadTimer > 0) {
//                this.doReload();
//                return;
//            }
//        }
//
//        if (!this.hasAmmo()) {
//            spinSpeed = Math.max(spinSpeed - SPIN_DELTA, 0);
//            return;
//        }
//        //if (!this.isAimingAtTarget(world, pos, target)) //fire constantly
//        //	return;
//
//        if (tickcount < this.getOperationTime())
//            return;
//
//        tickcount = 0;
//        if (!world.isClientSide) {
//            if (target[3] == 1) {
//                if (spinSpeed < SPIN_RATE)
//                    spinSpeed = Math.min(spinSpeed + SPIN_DELTA, SPIN_RATE);
//                else
//                    this.fire(world, target);
//            } else {
//                spinSpeed = Math.max(spinSpeed - SPIN_DELTA, 0);
//            }
//        }
//    }
//
//    private void continuousFeed() {
//        for (int i = CLIP_SLOT - 1; i > LOAD_SLOT; i--) {
//            if (inv[i - 1] != null) {
//                boolean flag = false;
//                if (itemHandler.getStackInSlot(i) == null) {
//                    itemHandler.getStackInSlot(i) = inv[i - 1];
//                    inv[i - 1] = null;
//                    flag = true;
//                } else if (itemHandler.getStackInSlot(i).getCount() < 64) {
//                    int amt = Math.min(64 - itemHandler.getStackInSlot(i).getCount(), inv[i - 1].getCount());
//                    itemHandler.getStackInSlot(i).getCount() += amt;
//                    ReikaInventoryHelper.decrStack(i - 1, this, amt);
//                    flag = true;
//                }
//                if (flag) {
//                    feedTick = FEED_TIME;
//                    return;
//                }
//            }
//        }
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        return 0;
//    }
//
//    @Override
//    public int getOperationTime() {
//        return FIRE_RATE;
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//        NBT.putDouble("spin", spinAngle);
//        NBT.putInt("reload", reloadTimer);
//        NBT.putInt("feed", feedTick);
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//        spinAngle = NBT.getDouble("spin");
//        feedTick = NBT.getInt("feed");
//        reloadTimer = NBT.getInt("reload");
//    }
//
//    @Override
//    protected boolean isValidTarget(Entity ent) {
//        return false;
//    }
//
//    public boolean isReloading() {
//        return reloadTimer > 0;
//    }
//
//    @Override
//    public int getContainerSize() {
//        return 0;
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
//    
//    @Override
//    public ItemStack insertItem(int slot,  ItemStack stack, boolean simulate) {
//        return null;
//    }
//
//    
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
//    public boolean isItemValid(int slot,  ItemStack stack) {
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
