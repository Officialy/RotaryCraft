///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.entities;
//
//import reika.rotarycraft.base.EntityTurretShot;
//import reika.rotarycraft.blockentities.weaponry.Turret.BlockEntityFlameTurret;
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import net.minecraft.core.BlockPos;
//import net.minecraft.util.Mth;
//import net.minecraft.world.damagesource.DamageSource;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
//import net.minecraft.world.entity.decoration.ItemFrame;
//import net.minecraft.world.entity.decoration.Painting;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.phys.AABB;
//import net.minecraft.world.phys.HitResult;
//import net.minecraft.world.phys.Vec3;
//
//import java.util.List;
//
//public class EntityFlameTurretShot extends EntityTurretShot {
//
//    private BlockEntityFlameTurret.FlameAttack parameters;
//
//    public EntityFlameTurretShot(final EntityType<? extends EntityTurretShot> entityType, Level world) {
//        super(entityType, world);
//    }
//
//    public EntityFlameTurretShot(Level world, BlockPos pos, BlockPos vpos, BlockEntityFlameTurret r, BlockEntityFlameTurret.FlameAttack f) {
//        super(world, pos, 0, 0, 0, r);
//        motionX = vx;
//        motionY = vy;
//        motionZ = vz;
//        if (!world.isClientSide)
//            velocityChanged = true;
//        gun = r;
//        parameters = f;
//    }
//
//    @Override
//    public void tick() {
//        if (gun == null) {
//            this.kill();
//            return;
//        }
//
//        tickCount++;
//        motionY -= 0.005;
//        velocityChanged = true;
//
//        boolean hit = false;
//        Level world = level;
//        int x = Mth.floor(getY());
//        int y = Mth.floor(getY());
//        int z = Mth.floor(getZ());
//        Block id = level.getBlockState(blockPosition()).getBlock();
//
//        MachineRegistry m = MachineRegistry.getMachine(level, blockPosition());
//        //ReikaJavaLibrary.pConsole("ID: "+id+" and "+mobs.size()+" mobs");
//        hit = tickCount > 5 && m != MachineRegistry.FLAMETURRET && id != Blocks.AIR && !ReikaWorldHelper.softBlocks(level, pos);
//        //ReikaJavaLibrary.pConsole(hit+"   by "+id+"  or mobs "+mobs.size())
//        if (hit) {
//            //ReikaChatHelper.write("HIT  @  "+tickCount+"  by "+(mobs.size() > 0));
//            this.onHit(null);
//            this.kill();
//            return;
//        }
//        //ReikaChatHelper.write(this.tickCount);
//        if (!level.isClientSide && (tickCount > 240 || shootingEntity != null && shootingEntity.isAlive() || !level.blockExists((int) getY(), (int) getY(), (int) getZ()))) {
//            this.kill();
//        } else {
//            this.onEntityUpdate();
//            Vec3 var15 = Vec3.createVectorHelper(getY, getY(), posZ);
//            Vec3 var2 = Vec3.createVectorHelper(getY + motionX, getY() + motionY, posZ + motionZ);
//            HitResult var3 = level.rayTraceBlocks(var15, var2);
//            if (var3 != null && var3.getType() == MovingObjectType.BLOCK && MachineRegistry.getMachine(world, var3.blockX, var3.blockY, var3.blockZ) != MachineRegistry.FLAMETURRET)
//                this.onHit(var3);
//            getX() += motionX;
//            getY() += motionY;
//            getZ() += motionZ;
//            if (this.isInWater()) {
//                if (DragonAPI.rand.nextInt(6) == 0)
//                    ReikaSoundHelper.playSoundAtEntity(world, this, "DragonAPI.rand.fizz", 0.4F, DragonAPI.rand.nextFloat() * 0.5F + 0.5F);
//                this.kill();
//            }
//            this.setPos(getY(), getY(), getZ());
//        }
//    }
//
//    @Override
//    public void onHit(HitResult mov) {
//        if (isAlive())
//            return;
//        Level world = level;
//        if (world.isClientSide)
//            return;
//        double x = getX();
//        double y = getY();
//        double z = getZ();
//        int x0 = (int) Math.floor(x);
//        int y0 = (int) Math.floor(y);
//        int z0 = (int) Math.floor(z);
//        LivingEntity el;
//        Entity ent;
//        int r = 0;
//        if (world.getBlockState(new BlockPos(x0, y0, z0)) == Blocks.AIR.defaultBlockState())
//            world.setBlock(new BlockPos(x0, y0, z0), Blocks.FIRE.defaultBlockState(), parameters.fireBlockLife, 3);
//        ReikaWorldHelper.ignite(world, x0, y0, z0, parameters.fireBlockLife);
//
//        double d = 1.5;
//        AABB splash = AABB.getBoundingBox(pos, pos).expand(d, d, d);
//        List dmgd = world.getEntities(Entity.class, splash);
//        for (int l = 0; l < dmgd.size(); l++) {
//            ent = (Entity) dmgd.get(l);
//            if (ent instanceof LivingEntity) {
//                el = (LivingEntity) ent;
//                this.applyAttackEffectsToEntity(world, el);
//            } else if (ent instanceof EndCrystal || ent instanceof Painting || ent instanceof ItemFrame) //Will not target but will destroy
//                ent.hurt(DamageSource.GENERIC, this.getAttackDamage());
//        }
//
//        this.kill();
//        //ent.hurt(DamageSource.outOfWorld, el.getHealth()*(1+el.getTotalArmorValue()));
//    }
//
//    @Override
//    public int getAttackDamage() {
//        return Math.round(5 * parameters.damageMultiplier);
//    }
//
//    @Override
//    protected void applyAttackEffectsToEntity(Level world, Entity ent) {
//        ent.hurt(this.getDamageSource().setIsFire(), this.getAttackDamage());
//        ent.setSecondsOnFire(parameters.burnTime);
//    }
//
//    @Override
//    public boolean shouldRenderInPass(int pass) {
//        return pass == 1;
//    }
//
//}
