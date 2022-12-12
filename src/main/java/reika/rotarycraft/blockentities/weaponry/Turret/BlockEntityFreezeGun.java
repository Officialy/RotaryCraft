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
//import net.minecraft.world.effect.MobEffectInstance;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.phys.AABB;
//import org.jetbrains.annotations.NotNull;
//import reika.dragonapi.libraries.ReikaInventoryHelper;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.api.Interfaces.TargetEntity;
//import reika.rotarycraft.base.blockentity.BlockEntityInventoriedCannon;
//import reika.rotarycraft.registry.MachineRegistry;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class BlockEntityFreezeGun extends BlockEntityInventoriedCannon {
//
//    public static MobEffectInstance getFreezeEffect(int duration) {
//        MobEffectInstance pot = new MobEffectInstance(RotaryCraft.freeze.id, duration, 0);
//        pot.setCurativeItems(new ArrayList<>());
//        return pot;
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        if (!this.isAimingAtTarget(world, pos, target))
//            return;
//        this.convertSnow();
//        if (!this.hasAmmo())
//            return;
//        if (tickcount < this.getOperationTime())
//            return;
//        if (power < MINPOWER)
//            return;
//        tickcount = 0;
//        if (target[3] == 1) {
//            this.fire(world, target);
//        }
//    }
//
//    private void convertSnow() {
//        int slot = ReikaInventoryHelper.locateInInventory(Blocks.SNOW, inv);
//        if (slot != -1 && ReikaInventoryHelper.canAcceptMoreOf(Items.SNOWBALL, 0, 4, this)) {
//            ReikaInventoryHelper.decrStack(slot, inv);
//            ReikaInventoryHelper.addToIInv(new ItemStack(Items.SNOWBALL, 4, 0), this);
//        }
//        slot = ReikaInventoryHelper.locateInInventory(Blocks.ICE, inv);
//        if (slot != -1 && ReikaInventoryHelper.canAcceptMoreOf(Items.SNOWBALL, 0, 16, this)) {
//            ReikaInventoryHelper.decrStack(slot, inv);
//            ReikaInventoryHelper.addToIInv(new ItemStack(Items.SNOWBALL, 16, 0), this);
//        }
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//    @Override
//    public int getRange() {
//        return 64;
//    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.FREEZEGUN;
//    }
//
//    @Override
//    public int getMaxRange() {
//        return 256;
//    }
//
//    @Override
//    public int getContainerSize() {
//        return 27;
//    }
//
//    @Override
//    public boolean hasAmmo() {
//        return ReikaInventoryHelper.checkForItem(Blocks.ICE, inv) || ReikaInventoryHelper.checkForItem(Items.snowball, inv);
//    }
//
//    @Override
//    protected double[] getTarget(Level world, BlockPos pos) {
//        double[] xyzb = new double[4];
//        int r = this.getRange();
//        AABB range = AABB.getBoundingBox(pos.getX() - r, pos.getY() - r, pos.getZ() - r, pos.getX() + 1 + r, pos.getY() + 1 + r, pos.getZ() + 1 + r);
//        List<Entity> inrange = world.getEntities(Entity.class, range);
//        double mindist = this.getRange() + 2;
//        Entity i_at_min = null;
//        for (Entity ent : inrange) {
//            double dist = ReikaMathLibrary.py3d(ent.getY() - pos.getX() - 0.5, ent.getY() - pos.getY() - 0.5, ent.getZ() - pos.getZ() - 0.5);
//            if (this.isValidTarget(ent)) {
//                if (ReikaWorldHelper.canBlockSee(world, pos, ent.getY(), ent.getY(), ent.getZ(), this.getRange())) {
//                    //ReikaJavaLibrary.pConsole(ent);
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
//        double speed = 1;
//        int slot = ReikaInventoryHelper.locateInInventory(Items.SNOWBALL, inv);
//        ReikaInventoryHelper.decrStack(slot, inv);
//        double[] v = new double[3];
//        v[0] = xyz[0] - xCoord;
//        v[1] = xyz[1] - yCoord;
//        v[2] = xyz[2] - zCoord;
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
//            EntityFreezeGunShot snow = new EntityFreezeGunShot(world, xCoord + 0.5 + dx, y, zCoord + 0.5 + dz, 3 * v[0], 3 * v[1], 3 * v[2], this);
//            world.addFreshEntity(snow);
//        }
//    }
//
//    @Override
//    public boolean isItemValidForSlot(int slot, ItemStack is) {
//        return ReikaItemHelper.matchStackWithBlock(is, Blocks.ICE.defaultBlockState()) || ReikaItemHelper.matchStackWithBlock(is, Blocks.SNOW.defaultBlockState()) || is.getItem() == Items.SNOWBALL;
//    }
//
//    @Override
//    protected double randomOffset() {
//        return 0;
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        if (!this.hasAmmo())
//            return 15;
//        return 0;
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
//        return elb.getHealth() > 0 && this.isMobOrUnlistedPlayer(elb) && elb.getActiveEffects().contains(RotaryCraft.freeze) == null;
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
//
//    @Override
//    public Level getWorld() {
//        return null;
//    }
//}
