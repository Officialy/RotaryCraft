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
//import reika.rotarycraft.blockentities.Weaponry.Turret.BlockEntityMultiCannon;
//
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.registry.ReikaParticleHelper;
//import reika.dragonapi.libraries.rendering.ReikaRenderHelper;
//import net.minecraft.client.Minecraft;
//import net.minecraft.core.BlockPos;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.util.Mth;
//import net.minecraft.world.damagesource.DamageSource;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
//import net.minecraft.world.entity.decoration.ItemFrame;
//import net.minecraft.world.entity.decoration.Painting;
//import net.minecraft.world.entity.monster.Slime;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//
//import net.minecraft.world.phys.AABB;
//import net.minecraft.world.phys.HitResult;
//import net.minecraft.world.phys.Vec3;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//
//import java.util.List;
//
//public class EntityGatlingShot extends EntityTurretShot {
//
//    public EntityGatlingShot(final EntityType<? extends EntityTurretShot> entityType, Level world) {
//        super(entityType, world);
//    }
//
//    public EntityGatlingShot(Level world, BlockPos pos, BlockPos vpos, BlockEntityMultiCannon r) {
//        super(world, pos, vpos, r);
//        motionX = vx;
//        motionY = vy;
//        motionZ = vz;
//        if (!world.isClientSide)
//            velocityChanged = true;
//        gun = r;
//    }
//
//    @Override
//    public void tick() {
//        if (gun == null) {
//            this.kill();
//            return;
//        }
//
//        if (level.isClientSide && this.getEntityId() % 10 == 0) { //tracer round
//            this.spawnTracerParticles();
//        }
//
//        tickCount++;
//        boolean hit = false;
//        Level world = level;
//        int x = Mth.floor(getY);
//        int y = Mth.floor(getY());
//        int z = Mth.floor(posZ);
//        Block id = level.getBlock(pos);
//
//        int r = 1;
//        for (int i = -r; i <= r; i++) {
//            for (int j = -r; j <= r; j++) {
//                for (int k = -r; k <= r; k++) {
//                    Block id2 = world.getBlock(x + i, y + j, z + k);
//                    //ReikaJavaLibrary.pConsole(RotaryConfig.COMMON.BLOCKDAMAGE.get()+" with "+power+" on "+FMLCommonHandler.instance().getEffectiveSide());
//                    if (RotaryConfig.COMMON.ATTACKBLOCKS.getState()) {
//                        if (id2.defaultBlockState().getMaterial() == Material.glass || id2.defaultBlockState().getMaterial() == Material.ice) {
//                            ReikaSoundHelper.playBreakSound(world, x + i, y + j, z + k, Blocks.GLASS);
//                            world.setBlock(x + i, y + j, z + k, Blocks.AIR);
//                        }
//                    }
//                }
//            }
//        }
//
//        MachineRegistry m = MachineRegistry.getMachine(level, getY, getY(), posZ);
//        List mobs = level.getEntities(LivingEntity.class, this.getBoundingBox().expand(1, 1, 1));
//        //ReikaJavaLibrary.pConsole("ID: "+id+" and "+mobs.size()+" mobs");
//        hit = (mobs.size() > 0 || (m != MachineRegistry.GATLING && id != Blocks.AIR && !ReikaWorldHelper.softBlocks(level, pos)));
//        //ReikaJavaLibrary.pConsole(hit+"   by "+id+"  or mobs "+mobs.size());
//        if (hit) {
//            //ReikaChatHelper.write("HIT  @  "+tickCount+"  by "+(mobs.size() > 0));
//            this.onHit(null);
//            this.kill();
//            return;
//        }
//        //ReikaChatHelper.write(this.tickCount);
//        if (!level.isClientSide && (shootingEntity != null && shootingEntity.isAlive() || !level.blockExists((int) getY, (int) getY(), (int) posZ)))
//            this.kill();
//        else {
//            this.onEntityUpdate();
//            Vec3 var15 = Vec3.createVectorHelper(getY, getY(), posZ);
//            Vec3 var2 = Vec3.createVectorHelper(getY + motionX, getY() + motionY, posZ + motionZ);
//            HitResult var3 = level.rayTraceBlocks(var15, var2);
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
//                    AABB var11 = var9.boundingBox.expand(var10, var10, var10);
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
//            getY += motionX;
//            getY() += motionY;
//            posZ += motionZ;
//            if (this.isInWater()) {
//                for (int var19 = 0; var19 < 4; ++var19) {
//                    float var18 = 0.25F;
//                    ReikaParticleHelper.BUBBLE.spawnAt(level, getY - motionX * var18, getY() - motionY * var18, posZ - motionZ * var18, motionX, motionY, motionZ);
//                }
//            }
//            this.setPosition(getY, getY(), posZ);
//        }
//    }
//
//    
//    private void spawnTracerParticles() {
//        double l = 0.25;
//        for (double d = -l; d <= l; d += 0.0625) {
//            double px = getY + motionX * d;
//            double py = getY() + motionY * d;
//            double pz = posZ + motionZ * d;
//            float s = 3 * (float) (l - Math.abs(d) / 2D);
//            EntityFX fx = new EntityFluidFX(level, px, py, pz, Fluids.LAVA).setLife(2)/*.setColor(0xffd0a0)*/.setScale(s);
//            Minecraft.getInstance().effectRenderer.addEffect(fx);
//        }
//    }
//
//    @Override
//    public void onHit(HitResult mov) {
//        if (mov != null && MachineRegistry.getMachine(level, new BlockPos(mov.getLocation())) == MachineRegistry.GATLING) {
//            this.kill();
//            return;
//        }
//        if (isAlive())
//            return;
//        Level world = level;
//        double x = getX();
//        double y = getY();
//        double z = getZ();
//        int x0 = (int) Math.floor(x);
//        int y0 = (int) Math.floor(y);
//        int z0 = (int) Math.floor(z);
//        LivingEntity el;
//        Entity ent;
//        int r = 0;
//        for (int i = -r; i <= r; i++) {
//            for (int j = -r; j <= r; j++) {
//                for (int k = -r; k <= r; k++) {
//                    Block id = world.getBlock(x0 + i, y0 + j, z0 + k);
//                    //ReikaJavaLibrary.pConsole(RotaryConfig.COMMON.BLOCKDAMAGE.get()+" with "+power+" on "+FMLCommonHandler.instance().getEffectiveSide());
//                    if (RotaryConfig.COMMON.ATTACKBLOCKS.getState()) {
//                        if (id.defaultBlockState().getMaterial() == Material.GLASS || id.defaultBlockState().getMaterial() == Material.ice) {
//                            ReikaSoundHelper.playBreakSound(world, x0 + i, y0 + j, z0 + k, Blocks.GLASS);
//                            world.setBlock(x0 + i, y0 + j, z0 + k, Blocks.AIR);
//                        }
//                    }
//
//                    if (world.isClientSide && id.defaultBlockState().getMaterial() != Material.AIR && MachineRegistry.getMachine(level, x0 + i, y0 + j, z0 + k) != MachineRegistry.GATLING)
//                        ReikaRenderHelper.spawnDropParticles(world, x0 + i, y0 + j, z0 + k, id, world.getBlockMetadata(x0 + i, y0 + j, z0 + k));
//                }
//            }
//        }
//
//        double d = 1.5;
//        AABB splash = new AABB(x, y, z, x0, y0, z0).expandTowards(d, d, d);
//        //world.explode(this, pos, 3F, false);
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
//        return 1;
//    }
//
//    @Override
//    protected void applyAttackEffectsToEntity(Level world, Entity ent) {
//        if (ent instanceof Slime) {
//            int in = ent.getEntityData().getInt(BlockEntityMultiCannon.SLIME_NBT);
//            ent.getEntityData().putInt(BlockEntityMultiCannon.SLIME_NBT, in + 1);
//            ReikaEntityHelper.knockbackEntityFromPos(gun.getPos().getX() + 0.5, gun.getPos().getY() + 0.5, gun.getPos().getZ() + 0.5, ent, 1);
//            ent.hurt(this.getDamageSource(), this.getAttackDamage() / 8F);
//            ent.invulnerableTime = 0; //hurtResistantTime
//        } else {
//            ent.hurt(this.getDamageSource(), this.getAttackDamage());
//            ReikaEntityHelper.knockbackEntityFromPos(gun.getPos().getX() + 0.5, gun.getPos().getY() + 0.5, gun.getPos().getZ() + 0.5, ent, 0.0625);
//            ent.invulnerableTime = 0; //hurtResistantTime
//        }
//    }
//
//    @Override
//    public void writeSpawnData(FriendlyByteBuf buffer) {
//        buffer.writeInt(gun.getPos().getX());
//        buffer.writeInt(gun.getPos().getY());
//        buffer.writeInt(gun.getPos().getZ());
//    }
//
//    @Override
//    public void readSpawnData(FriendlyByteBuf additionalData) {
//
//    }
//}
