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
//import reika.rotarycraft.blockentities.Weaponry.Turret.BlockEntityFreezeGun;
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import net.minecraft.core.BlockPos;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.util.Mth;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.phys.AABB;
//import net.minecraft.world.phys.EntityHitResult;
//import net.minecraft.world.phys.HitResult;
//import net.minecraft.world.phys.Vec3;
//
//import java.util.List;
//
//public class EntityFreezeGunShot extends EntityTurretShot {
//
//    public EntityFreezeGunShot(final EntityType<? extends EntityTurretShot> entityType, Level world) {
//        super(entityType, world);
//    }
//
//    public EntityFreezeGunShot(Level world, BlockPos pos, BlockPos vpos, BlockEntityFreezeGun te) {
//        super(world, pos, vpos, te);
//        motionX = vx;
//        motionY = vy;
//        motionZ = vz;
//        if (!world.isClientSide)
//            velocityChanged = true;
//    }
//
//    @Override
//    public void onHit(EntityHitResult mov) {
//        if (mov != null && MachineRegistry.getMachine(level, mov.getEntity().getOnPos())) ==MachineRegistry.FREEZEGUN){
//            this.kill();
//            return;
//        }
//        if (isAlive())
//            return;
//        Level world = level;
//        double x = getOnPos().getX();
//        double y = getOnPos().getY();
//        double z = getOnPos().getZ();
//        int x0 = (int) x;
//        int y0 = (int) y;
//        int z0 = (int) z;
//        LivingEntity el;
//        Entity ent;
//
//        AABB splash = new AABB(position(), position()).expandTowards(2, 2, 2);
//        List dmgd = world.getEntities(this, splash);
//        for (int l = 0; l < dmgd.size(); l++) {
//            ent = (Entity) dmgd.get(l);
//            this.applyAttackEffectsToEntity(world, ent);
//        }
//
//        this.kill();
//    }
//
//    @Override
//    protected void applyAttackEffectsToEntity(Level world, Entity el) {
//        if (el instanceof Player && ((Player) el).capabilities.isCreative())
//            return;
//        if (el instanceof TargetEntity) {
//            ((TargetEntity) el).onFreeze(gun);
//        }
//        if (el instanceof LivingEntity)
//            ((LivingEntity) el).addEffect(BlockEntityFreezeGun.getFreezeEffect(60000));
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
//        hit = (mobs.size() > 0 || (m != MachineRegistry.FREEZEGUN && id != Blocks.AIR && !ReikaWorldHelper.softBlocks(level, pos)));
//        if (hit) {
//            this.onHit(null);
//            return;
//        }
//        if (!level.isClientSide && (shootingEntity != null && shootingEntity.isAlive() || !level.blockExists(pos)))
//            this.kill();
//        else {
//            if (tickCount > 80 && !level.isClientSide)
//                this.onHit(null);
//            this.tick();
//            Vec3 var15 = Vec3.createVectorHelper(getY, getY(), posZ);
//            Vec3 var2 = Vec3.createVectorHelper(getY + motionX, getY() + motionY, posZ + motionZ);
//            EntityHitResult var3 = level.rayTraceBlocks(var15, var2);
//            var15 = Vec3.createVectorHelper(getY, getY(), posZ);
//            var2 = Vec3.createVectorHelper(getY + motionX, getY() + motionY, posZ + motionZ);
//            if (var3 != null)
//                var2 = Vec3.createVectorHelper(var3.getLocation().xCoord, var3.getLocation().yCoord, var3.getLocation().zCoord);
//            Entity var4 = null;
//            List var5 = level.getEntitiesExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
//            double var6 = 0.0D;
//            for (int var8 = 0; var8 < var5.size(); ++var8) {
//                Entity var9 = (Entity) var5.get(var8);
//                if (var9.canBeCollidedWith() && (!var9.isEntityEqual(shootingEntity))) {
//                    float var10 = 0.3F;
//                    AABB var11 = var9.getBoundingBox().expandTowards(var10, var10, var10);
//                    HitResult var12 = var11.intersect(var15, var2);
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
//                var3 = new EntityHitResult(var4);
//            if (var3 != null)
//                this.onHit(var3);
//            getY += motionX;
//            getY() += motionY;
//            posZ += motionZ;
//            if (this.isInWater()) {
//                this.kill();
//            }
//            this.setPosition(getY, getY(), posZ);
//        }
//    }
//
//    @Override
//    public int getAttackDamage() {
//        return 0;
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
