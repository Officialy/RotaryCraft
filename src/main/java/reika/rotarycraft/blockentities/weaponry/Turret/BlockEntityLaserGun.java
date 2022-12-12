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
//import net.minecraft.core.particles.ParticleTypes;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.item.PrimedTnt;
//import net.minecraft.world.level.Explosion;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.phys.AABB;
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.api.Interfaces.TargetEntity;
//import reika.rotarycraft.auxiliary.TurretDamage;
//import reika.rotarycraft.base.blockentity.BlockEntityAimedCannon;
//
//import reika.rotarycraft.registry.MachineRegistry;
//
//import java.util.List;
//
//public class BlockEntityLaserGun extends BlockEntityAimedCannon {
//
//    private int range;
//
//    @Override
//    public int getRange() {
//        return range;
//    }
//
//    private void setRange(int i) {
//        range = i;
//    }
//
//    @Override
//    public int getMaxRange() {
//        return 256;
//    }
//
//    @Override
//    public boolean hasAmmo() {
//        return true;
//    }
//
//    @Override
//    protected double[] getTarget(Level world, BlockPos pos) {
//        double[] xyzb = new double[4];
//        int r = this.getRange();
//        AABB box = new AABB(x - r, y - r, z - r, x + 1 + r, y + 1 + r, z + 1 + r);
//        List<Entity> inrange = world.getEntitiesOfClass(Entity.class, box);
//        double mindist = this.getRange() + 2;
//        Entity i_at_min = null;
//        for (Entity ent : inrange) {
//            double dist = ReikaMathLibrary.py3d(ent.getY() - x - 0.5, ent.getY() - y - 0.5, ent.getZ() - z - 0.5);
//            if (this.isValidTarget(ent)) {
//                if (ReikaWorldHelper.canBlockSee(world, pos, ent.getX(), ent.getY(), ent.getZ(), this.getRange())) {
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
//        xyzb[0] = closestMob.getX() + this.randomOffset();
//        xyzb[1] = closestMob.getY() + closestMob.getEyeHeight() * 0.25 + this.randomOffset();
//        xyzb[2] = closestMob.getZ() + this.randomOffset();
//        xyzb[3] = 1;
//        return xyzb;
//    }
//
//    @Override
//    public void fire(Level world, double[] xyz) {
//        if (world.isClientSide)
//            return;
//        for (float i = 0; i <= this.getMaxRange(); i += 0.5F) {
//            double dx = i * Math.cos(Math.toRadians(theta)) * Math.cos(Math.toRadians(-phi + 90));
//            double dy = i * Math.sin(Math.toRadians(theta));
//            double dz = i * Math.cos(Math.toRadians(theta)) * Math.sin(Math.toRadians(-phi + 90));
//            int r = 1;
//            AABB light = new AABB(worldPosition.getX() + dx, worldPosition.getY() + dy, worldPosition.getZ() + dz, worldPosition.getX() + dx, worldPosition.getY() + dy, worldPosition.getZ() + dz).expand(r, r, r);
//            List<Entity> in = world.getEntitiesOfClass(Entity.class, light);
//            for (Entity e : in) {
//                if (e instanceof TargetEntity) {
//                    ((TargetEntity) e).onLaserBeam(this);
//                }
//                if (e instanceof LivingEntity) {
//                    e.hurt(new TurretDamage(this).setIsFire(), 4);
//                    e.setSecondsOnFire(7);
//                    ((LivingEntity) e).removeEffect(RotaryCraft.freeze);
//                }
//            }
//            int x = xCoord + (int) dx;
//            int y = yCoord + (int) dy;
//            int z = zCoord + (int) dz;
//            Block id = world.getBlockState(pos).getBlock();
//            Block id2 = this.getAffectedID(id);
//            int meta2 = this.getAffectedMetadata(id2);
//            //ReikaJavaLibrary.pConsole(id+"  to  "+id2+"  @  "+x+", "+y+", "+z);
//            //ReikaJavaLibrary.pConsole(theta);
//            if (RotaryConfig.COMMON.ATTACKBLOCKS.getState()) {
//                if (id2 != id || meta2 != meta) {
//                    world.setBlock(pos, id2, meta2, 3);
//                    world.markBlockForUpdate(pos);
//                    this.setRange((int) i + 1);
//                    return;
//                }
//                if (id == Blocks.NETHERRACK) {
//                    world.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3F, true, Level.ExplosionInteraction.BLOCK);
//                    world.markBlockForUpdate(pos);
//                    this.setRange((int) i + 1);
//                    return;
//                }
//                if (id == Blocks.TNT) {
//                    world.setBlockToAir(pos);
//                    PrimedTnt var6 = new PrimedTnt(world, x + 0.5D, y + 0.5D, z + 0.5D, null);
//                    world.addFreshEntity(var6);
////                    world.playSound(var6, "DragonAPI.rand.fuse", 1.0F, 1.0F);
//                    world.addParticle(ParticleTypes.LAVA, x + DragonAPI.rand.nextFloat(), y + DragonAPI.rand.nextFloat(), z + DragonAPI.rand.nextFloat(), 0, 0, 0);
//                    this.setRange((int) i + 1);
//                    return;
//                }
//            }
//            if (id != Blocks.AIR && id.isOpaqueCube()) {
//                this.setRange((int) i + 1);
//                return;
//            }
//            if (i == this.getMaxRange()) {
//                this.setRange(this.getMaxRange());
//            }
//        }
//    }
//
//    private Block getAffectedID(Block id) {
//        if (id == Blocks.AIR)
//            return Blocks.AIR;
//        if (id == Blocks.SAND)
//            return Blocks.GLASS;
//        if (id == Blocks.STONE || id == Blocks.STONE_BRICKS || id == Blocks.SANDSTONE)
//            return Blocks.LAVA;
//        if (id == Blocks.GRASS || id == Blocks.MYCELIUM)
//            return Blocks.DIRT;
//        if (id == Blocks.DIRT || id == Blocks.FARMLAND)
//            return Blocks.SAND;
//        if (id == Blocks.COBBLESTONE)
//            return Blocks.LAVA;
//        if (id == Blocks.GRAVEL)
//            return Blocks.COBBLESTONE;
//        if (id == Blocks.TALL_GRASS || id == Blocks.COBWEB || id == Blocks.DANDELION || id == Blocks.SNOW ||
//                id == Blocks.POPPY || id == Blocks.RED_MUSHROOM || id == Blocks.BROWN_MUSHROOM ||
//                id == Blocks.DEAD_BUSH || id == Blocks.WHEAT || id == Blocks.CARROTS || id == Blocks.POTATOES || id == Blocks.VINE ||
//                id == Blocks.MELON_STEM || id == Blocks.PUMPKIN_STEM || id == Blocks.LILY_PAD)
//            return Blocks.AIR;
//        if (ReikaWorldHelper.flammable(id))
//            return Blocks.FIRE;
//        if (id == Blocks.ICE || id == Blocks.SNOW)
//            return Blocks.WATER;
//        return id;
//    }
//
//    @Override
//    protected double randomOffset() {
//        return 0;
//    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.LASERGUN;
//    }
//
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        if (power < MINPOWER) {
//            this.setRange(0);
//            return;
//        }
//        this.setRange(this.getMaxRange());
//        if (tickcount < 0)
//            return;
//        tickcount = 0;
//        this.fire(world, null);
//        //if (!this.isAimingAtTarget(world, pos, target))
//        //	return;
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
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
//}
