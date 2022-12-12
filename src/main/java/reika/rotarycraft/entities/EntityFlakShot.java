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
//import reika.rotarycraft.api.Interfaces.TargetEntity;
//import reika.rotarycraft.base.EntityTurretShot;
//import reika.rotarycraft.registry.MachineRegistry;
//import net.minecraft.core.BlockPos;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.sounds.SoundEvent;
//import net.minecraft.util.Mth;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.phys.AABB;
//import net.minecraft.world.phys.HitResult;
//import net.minecraft.world.phys.Vec3;
//
//import java.util.List;
//
//public class EntityFlakShot extends EntityTurretShot {
//
//    public EntityFlakShot(final EntityType<? extends EntityTurretShot> entityType, Level world) {
//        super(entityType, world);
//    }
//
//    public EntityFlakShot(Level world, BlockPos pos, BlockPos vpos, BlockEntityAAGun te) {
//        super(world, pos, vpos, te);
//        motionX = vx;
//        motionY = vy;
//        motionZ = vz;
//        if (!world.isClientSide)
//            velocityChanged = true;
//    }
//
//    @Override
//    protected void onHit(HitResult hit) {
//        if (hit != null && MachineRegistry.getMachine(level, new BlockPos(hit.getLocation().x(), hit.getLocation().y(), hit.getLocation().z())) == MachineRegistry.ANTIAIR) {
//            this.kill();
//            return;
//        }
//        if (isAlive())
//            return;
//        Level world = level;
//        double x = getX();
//        double y = getY();
//        double z = getZ();
//        int x0 = (int) x;
//        int y0 = (int) y;
//        int z0 = (int) z;
//        LivingEntity el;
//        Entity ent;
//
//        AABB splash = new AABB(x, y, z, x0, y0, z0).expandTowards(4, 4, 4);
//        List dmgd = world.getEntities(Entity.class, splash);
//        for (int l = 0; l < dmgd.size(); l++) {
//            ent = (Entity) dmgd.get(l);
//            if (ent instanceof LivingEntity) {
//                el = (LivingEntity) ent;
//                this.applyAttackEffectsToEntity(world, el);
//            }
//        }
//
//        this.kill();
//    }
//
//    @Override
//    protected void applyAttackEffectsToEntity(Level world, Entity el) {
//        if (el instanceof TargetEntity) {
//            ((TargetEntity) el).flakShot(gun);
//        }
//        if (el instanceof LivingEntity) {
//            el.hurt(this.getDamageSource(), this.getAttackDamage());
//            el.playSound(new SoundEvent(new ResourceLocation("damage.hit")), 2, 1);
//        }
//    }
//
//    @Override
//    public void tick() {
//        tickCount++;
//        boolean hit = false;
//        int x = Mth.floor(getX());
//        int y = Mth.floor(getY());
//        int z = Mth.floor(getZ());
//        Block id = level.getBlockState(blockPosition()).getBlock();
//        MachineRegistry m = MachineRegistry.getMachine(level, blockPosition());
//        List mobs = level.getEntities(LivingEntity.class, this.getBoundingBox().expandTowards(1, 1, 1));
//        hit = (mobs.size() > 0 || (m != MachineRegistry.FREEZEGUN && id != Blocks.AIR && !ReikaWorldHelper.softBlocks(level, blockPosition())));
//        if (hit) {
//            this.onHit(null);
//            return;
//        }
//        if (!level.isClientSide && (shootingEntity != null && shootingEntity.isAlive() || !level.blockExists(pos)))
//            this.kill();
//        else {
//            if (tickCount > 80 && !level.isClientSide)
//                this.onHit(null);
//            this.onEntityUpdate();
//            Vec3 var15 = Vec3.createVectorHelper(getX(), getY(), getZ());
//            Vec3 var2 = Vec3.createVectorHelper(getX() + motionX, getY() + motionY, getZ() + motionZ);
//            HitResult var3 = level.rayTraceBlocks(var15, var2);
//            var15 = Vec3.createVectorHelper(getX(), getY(), getZ());
//            var2 = Vec3.createVectorHelper(getY() + motionX, getY() + motionY, getZ() + motionZ);
//            if (var3 != null)
//                var2 = Vec3.createVectorHelper(var3.getLocation().x(), var3.getLocation().y(), var3.getLocation().z());
//            Entity var4 = null;
//            List var5 = level.getEntitiesExcludingEntity(this, getBoundingBox().addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
//            double var6 = 0.0D;
//            for (int var8 = 0; var8 < var5.size(); ++var8) {
//                Entity var9 = (Entity) var5.get(var8);
//                if (var9.canBeCollidedWith() && (!var9.isEntityEqual(shootingEntity))) {
//                    float var10 = 0.3F;
//                    AABB var11 = var9.getBoundingBox().expandTowards(var10, var10, var10);
//                    HitResult var12 = var11.calculateIntercept(var15, var2);
//                    if (var12 != null) {
//                        double var13 = var15.distanceTo(var12.getLocation());
//                        if (var13 < var6 || var6 == 0.0D) {
//                            var4 = var9;
//                            var6 = var13;
//                        }
//                    }
//                }
//            }
//            if (var4 != null)
//                var3 = new HitResult(var4);
//            if (var3 != null)
//                this.onHit(var3);
//            getY() += motionX;
//            getY() += motionY;
//            getZ() += motionZ;
//            if (this.isInWater()) {
//                this.kill();
//            }
//            this.setPos(getX(), getY(), getZ());
//        }
//    }
//
//    @Override
//    public int getAttackDamage() {
//        return 2;
//    }
//
//    @Override
//    public void writeSpawnData(FriendlyByteBuf buffer) {
//
//    }
//
//    @Override
//    public void readSpawnData(FriendlyByteBuf additionalData) {
//
//    }
//}
