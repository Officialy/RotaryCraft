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
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
//import net.minecraft.world.entity.boss.wither.WitherBoss;
//import net.minecraft.world.entity.monster.Blaze;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.phys.AABB;
//import net.neoforged.items.IItemHandler;
//
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.libraries.ReikaEntityHelper;
//import reika.dragonapi.libraries.ReikaInventoryHelper;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.rotarycraft.api.Interfaces.FlyingMob;
//import reika.rotarycraft.api.Interfaces.TargetEntity;
//import reika.rotarycraft.base.blockentity.BlockEntityInventoriedCannon;
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.RotaryItems;
//
//import java.util.List;
//
//public class BlockEntityAAGun extends BlockEntityInventoriedCannon implements IItemHandler {
//
//    @Override
//    public int getRange() {
//        return 128;
//    }
//
//    @Override
//    public int getMaxRange() {
//        return 128;
//    }
//
//    @Override
//    public boolean hasAmmo() {
//        return ReikaInventoryHelper.checkForItemStack(RotaryItems.HSLA_STEEL_SCRAP, inv, false);
//    }
//
//    @Override
//    protected float getAimingSpeed() {
//        return 2;
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        if (power < MINPOWER || torque < MINTORQUE)
//            return;
//        if (!this.hasAmmo())
//            return;
//        if (!this.isAimingAtTarget(world, pos, target))
//            return;
//        if (tickcount < this.getOperationTime())
//            return;
//        //ReikaJavaLibrary.pConsole(Arrays.toString(target), Dist.DEDICATED_SERVER);
//        tickcount = 0;
//        if (target[3] == 1) {
//            this.fire(world, target);
//        }
//    }
//
//    @Override
//    public int getOperationTime() {
//        return 6;
//    }
//
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
//        ReikaSoundHelper.playSoundAtBlock(world, worldPosition, "DragonAPI.rand.explode", 1F, 1.3F);
//        ReikaSoundHelper.playSoundAtBlock(world, worldPosition, "DragonAPI.rand.explode", 1F, 0.5F);
//
//        if (DragonAPI.rand.nextBoolean()) {
//            int slot = ReikaInventoryHelper.locateInInventory(RotaryItems.HSLA_STEEL_SCRAP, inv, false);
//            ReikaInventoryHelper.decrStack(slot, inv);
//        }
//
//        double speed = 2;
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
//
//        //ReikaJavaLibrary.pConsole(dx+"  "+dy+"  "+dz);
//        if (!world.isClientSide) {
//            double y = this.getFiringPositionY(dy);
//            EntityFlakShot flak = new EntityFlakShot(world, worldPosition.getX() + 0.5 + dx, y, worldPosition.getZ() + 0.5 + dz, 3 * v[0], 3 * v[1], 3 * v[2], this);
//            world.addFreshEntity(flak);
//        }
//    }
//
//    @Override
//    protected double randomOffset() {
//        return -1;
//    }
//
//    @Override
//    protected boolean isValidTarget(Entity ent) {
//        if (ent instanceof TargetEntity)
//            return ((TargetEntity) ent).shouldTarget(this, placerUUID);
//        if (!(ent instanceof LivingEntity))
//            return false;
//        LivingEntity elb = (LivingEntity) ent;
//        if (elb.isAlive() || elb.getHealth() <= 0)
//            return false;
//        if (ent.onGround() || ent.isInWater() || ent.isInLava())
//            return false;
//        if (elb instanceof net.minecraft.world.entity.FlyingMob && ReikaEntityHelper.isHostile(elb)) {
//            return ReikaMathLibrary.py3d(ent.getY() - worldPosition.getX() - 0.5, ent.getY() - worldPosition.getY() - 0.5, ent.getZ() - worldPosition.getZ() - 0.5) > 2;
//        }
//        if (ent instanceof Blaze || ent instanceof WitherBoss || ent instanceof EnderDragon) {
//            return ReikaMathLibrary.py3d(ent.getY() - worldPosition.getX() - 0.5, ent.getY() - worldPosition.getY() - 0.5, ent.getZ() - worldPosition.getZ() - 0.5) > 2;
//        }
//        if (ent instanceof FlyingMob) {
//            FlyingMob fm = (FlyingMob) ent;
//            return fm.isCurrentlyFlying() && fm.isHostile() && ReikaMathLibrary.py3d(ent.getY() - worldPosition.getY() - 0.5, ent.getY() - worldPosition.getY() - 0.5, ent.getZ() - worldPosition.getZ() - 0.5) > 2;
//        }
//        //return InterfaceCache.BCROBOT.instanceOf(ent);
//        return false;
//    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.ANTIAIR;
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//    @Override
//    public int getSlots() {
//        return 27;
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
//        return ReikaItemHelper.matchStacks(stack, RotaryItems.HSLA_STEEL_SCRAP);
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
