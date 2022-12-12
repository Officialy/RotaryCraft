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
//import net.minecraft.world.item.ItemStack;
//import net.minecraftforge.fluids.FluidStack;
//import org.jetbrains.annotations.NotNull;
//import reika.rotarycraft.api.Interfaces.TargetEntity;
//import reika.rotarycraft.base.blockentity.BlockEntityFluidCannon;
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.SoundRegistry;
//import reika.dragonapi.libraries.java.ReikaRandomHelper;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.dragonapi.libraries.mathsci.ReikaPhysicsHelper;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.material.Fluid;
//import net.minecraft.world.phys.AABB;
//
//import java.util.HashMap;
//import java.util.List;
//
//public class BlockEntityFlameTurret extends BlockEntityFluidCannon {
//
//    private static final HashMap<String, FlameAttack> damageMultipliers = new HashMap<>();
//
//    static {
//        damageMultipliers.put("oil", new FlameAttack(0.75F, 6, 0.4F, 0));
//        damageMultipliers.put("fuel", new FlameAttack(1F, 3));
//        damageMultipliers.put("rc ethanol", new FlameAttack(1.2F, 4, 1, 8));
//        damageMultipliers.put("bioethanol", new FlameAttack(1.35F, 4, 1, 8)); //forestry
//        damageMultipliers.put("rc jet fuel", new FlameAttack(1.8F, 6));
//        damageMultipliers.put("rocket fuel", new FlameAttack(2F, 10));
//    }
//
//    @Override
//    public int getRange() {
//        return tank.isEmpty() ? 0 : (int) (32 * damageMultipliers.get(tank.getActualFluid().getName()).rangeMultiplier);
//    }
//
//    @Override
//    public int getMaxRange() {
//        return 32;
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
//                        if (dist < mindist && dist >= 6) { //has a min range too
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
//        double dist = ReikaMathLibrary.py3d(xyz[0] - xCoord, xyz[1] - yCoord, xyz[2] - zCoord);
//        double speed = 0.25 * (Math.pow(dist, 0.7) / 7D) * ReikaRandomHelper.getRandomPlusMinus(1, 0.125);
//        tank.removeLiquid(1);
//        double[] v = ReikaPhysicsHelper.polarToCartesian(speed, theta + 20, -phi + 90);
//        double dx = v[0];
//        double dy = v[1];
//        double dz = v[2];
//        if (!world.isClientSide) {
//            double y = this.getFiringPositionY(dy);
//            world.addFreshEntity(new EntityFlameTurretShot(world, xCoord + 0.5 + dx, y, zCoord + 0.5 + dz, v[0], v[1], v[2], this, damageMultipliers.get(tank.getActualFluid().getName())));
//        }
//        if (this.tickcount % 34 == 0)
//            SoundRegistry.FLAMETURRET.playSoundAtBlock(this, 1, 1);
//        //ReikaSoundHelper.playSoundAtBlock(world, xCoord, yCoord, zCoord, "mob.blaze.hit", 0.25F, 0.25F);
//    }
//
//    @Override
//    protected double getThetaOffset() {
//        return 20;
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
//        return MachineRegistry.FLAMETURRET;
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
//        if (power < MINPOWER || omega < MINSPEED) {
//            return;
//        }
//
//        //if (!this.isAimingAtTarget(world, pos, target)) //fire constantly
//        //	return;
//
//        if (tickcount < this.getOperationTime())
//            return;
//
//        tickcount = 0;
//        if (!world.isClientSide) {
//            if (target[3] == 1 && this.hasAmmo()) {
//                this.fire(world, target);
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
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//    }
//
//    @Override
//    public boolean canConnectToPipe(MachineRegistry m) {
//        return m == MachineRegistry.FUELLINE || m == MachineRegistry.BEDPIPE;
//    }
//
//    @Override
//    public boolean isValidFluid(Fluid f) {
//        return damageMultipliers.containsKey(f.getName());
//    }
//
//    @Override
//    public boolean canReceiveFrom(Direction from) {
//        return true;
//    }
//
//    @Override
//    public int getTanks() {
//        return 0;
//    }
//
//    @NotNull
//    @Override
//    public FluidStack getFluidInTank(int tank) {
//        return null;
//    }
//
//    @Override
//    public int getTankCapacity(int tank) {
//        return 0;
//    }
//
//    @Override
//    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
//        return false;
//    }
//
//    @Override
//    public int fill(FluidStack resource, FluidAction action) {
//        return 0;
//    }
//
//    @NotNull
//    @Override
//    public FluidStack drain(FluidStack resource, FluidAction action) {
//        return null;
//    }
//
//    @NotNull
//    @Override
//    public FluidStack drain(int maxDrain, FluidAction action) {
//        return null;
//    }
//
//    @Override
//    public int getSlots() {
//        return 0;
//    }
//
//    @NotNull
//    @Override
//    public ItemStack getStackInSlot(int slot) {
//        return null;
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
//        return "flameturret";
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
//    public static class FlameAttack {
//
//        private static final int DEFAULT_FIRE_LIFE = 4; //lower is higher life
//        public final float damageMultiplier;
//        public final int burnTime;
//        public final float rangeMultiplier;
//        public final int fireBlockLife;
//
//        private FlameAttack(float f, int burn) {
//            this(f, burn, 1, DEFAULT_FIRE_LIFE);
//        }
//
//        private FlameAttack(float f, int burn, int life) {
//            this(f, burn, 1, life);
//        }
//
//        private FlameAttack(float f, int burn, float r) {
//            this(f, burn, r, DEFAULT_FIRE_LIFE);
//        }
//
//        private FlameAttack(float f, int burn, float r, int life) {
//            damageMultiplier = f;
//            burnTime = burn;
//            rangeMultiplier = r;
//            fireBlockLife = life;
//        }
//
//    }
//
//}
