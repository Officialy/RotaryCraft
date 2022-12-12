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
//import reika.rotarycraft.blockentities.level.BlockEntitySonicBorer;
//import reika.rotarycraft.registry.RotaryItems;
//import reika.dragonapi.libraries.registry.ReikaParticleHelper;
//import reika.rotarycraft.blockentities.Level.BlockEntitySonicBorer;
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.damagesource.DamageSource;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.projectile.Fireball;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.phys.AABB;
//import net.minecraft.world.phys.HitResult;
//import net.minecraft.world.phys.Vec3;
//
//import java.util.List;
//
//public class EntitySonicShot extends Fireball {
//
//    private final BlockEntitySonicBorer te;
//
//    public EntitySonicShot(final EntityType<? extends Fireball> entityType, Level par1World) {
//        super(entityType, par1World);
//        te = null;
//    }
//
//    public EntitySonicShot(Level world, BlockEntitySonicBorer tile, String player) {
//        super(world, tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), 0, 0, 0);
//        te = tile;
//        this.setPosition(tile.getPos().getX() + 0.5 + tile.xstep, tile.getPos().getY() + 0.5 + tile.ystep, tile.getPos().getZ() + 0.5 + tile.zstep);
//
//        double dd = 2;
//        motionX = tile.xstep * dd;
//        motionY = tile.ystep * dd;
//        motionZ = tile.zstep * dd;
//        accelerationX = motionX;
//        accelerationY = motionY;
//        accelerationZ = motionZ;
//
//        if (!world.isClientSide)
//            velocityChanged = true;
//    }
//
//    public int[] getSteps() {
//        int[] steps = new int[3];
//        if (motionX != 0) {
//            steps[0] = motionX > 0 ? 1 : -1;
//        }
//        if (motionY != 0) {
//            steps[1] = motionY > 0 ? 1 : -1;
//        }
//        if (motionZ != 0) {
//            steps[2] = motionZ > 0 ? 1 : -1;
//        }
//        return steps;
//    }
//
//    @Override
//    protected void onHit(HitResult mov) {
//        Level world = level;
//        //ReikaJavaLibrary.pConsole(mov != null ? (mov.blockX+","+mov.blockY+","+mov.blockZ) : "null");
//        if (mov != null) {
//            int x = mov.blockX;
//            int y = mov.blockY;
//            int z = mov.blockZ;
//            this.breakBlocks(world, pos);
//            this.hurtMobs(world, pos);
//            this.kill();
//        }
//    }
//
//    private void hurtMobs(Level world, BlockPos pos) {
//        AABB box = ReikaAABBHelper.getBlockAABB(pos).expand(3, 3, 3);
//        List<LivingEntity> li = world.getEntities(LivingEntity.class, box);
//        for (LivingEntity e : li) {
//            e.hurt(DamageSource.inWall, 1);
//        }
//    }
//
//    @Override
//    public void tick() {
//        this.onEntityUpdate();
//        tickCount++;
//
//        if (tickCount > 1200) {
//            this.kill();
//            return;
//        }
//
//        if (te != null) {
//            int x = (int) Math.floor(getY);
//            int y = (int) Math.floor(getY());
//            int z = (int) Math.floor(posZ);
//            int d = te.xstep * (x - te.xCoord) + te.ystep * (y - te.yCoord) + te.zstep * (z - te.zCoord);
//            int r = this.getRange();
//            if (d >= r) {
//                Vec3 vec = Vec3.createVectorHelper(0, 0, 0);
//                int[] tg = te.getTargetPosn();
//                HitResult mov = new HitResult(tg[0], tg[1], tg[2], -1, vec);
//                this.onHit(mov);
//            }
//        }
//        this.setPosition(getY + motionX, getY() + motionY, posZ + motionZ);
//        int dx;
//        int dy;
//        int dz;
//        if (te != null) {
//            int r = 3;
//            dx = te.xstep == 0 ? r : 0;
//            dy = te.ystep == 0 ? r : 0;
//            dz = te.zstep == 0 ? r : 0;
//        } else {
//            dx = dy = dz = 2;
//            AABB box = this.getBoundingBox().expand(dx, dy, dz);
//            List<Entity> li = level.getEntities(Entity.class, box);
//            for (int i = 0; i < li.size(); i++) {
//                Entity e = li.get(i);
//                this.applyEntityCollision(e);
//            }
//        }
//    }
//
//    private int getRange() {
//        return te.getDistanceToSurface();
//    }
//
//    @Override
//    public final boolean canRenderOnFire() {
//        return false;
//    }
//
//    @Override
//    public final AABB getBoundingBox() {
//        return AABB.getBoundingBox(getY + 0.4, getY() + 0.4, posZ + 0.4, getY + 0.6, getY() + 0.6, posZ + 0.6);
//    }
//
//    private void breakBlocks(Level world, BlockPos pos) {
//        int k = BlockEntitySonicBorer.FOV;
//        if (!world.isClientSide) {
//            if (te.xstep != 0) {
//                for (int i = z - k; i <= z + k; i++) {
//                    for (int j = y - k; j <= y + k; j++) {
//                        this.dropBlockAt(world, x, j, i);
//                    }
//                }
//            } else if (te.zstep != 0) {
//                for (int i = x - k; i <= x + k; i++) {
//                    for (int j = y - k; j <= y + k; j++) {
//                        this.dropBlockAt(world, i, j, z);
//                    }
//                }
//            } else if (te.ystep != 0) {
//                for (int i = x - k; i <= x + k; i++) {
//                    for (int j = z - k; j <= z + k; j++) {
//                        this.dropBlockAt(world, i, y, j);
//                    }
//                }
//            }
//        }
//        ReikaSoundHelper.playSoundAtBlock(world, pos, "DragonAPI.rand.explode");
//        ReikaParticleHelper.EXPLODE.spawnAt(world, pos);
//    }
//
//    private void dropBlockAt(Level world, BlockPos pos) {
//        if (y == 0)
//            return;
//        Block b = world.getBlock(pos);
//        if (b == Blocks.AIR)
//            return;
//         =world.getBlockMetadata(pos);
//        if (!world.isClientSide && !ReikaPlayerAPI.playerCanBreakAt((WorldServer) world, pos, b, meta, te.getServerPlacer()))
//            return;
//        if (!BlockEntitySonicBorer.canDrop(world, pos) && !(b instanceof BlockLiquid))
//            return;
//        List<ItemStack> li = b.getDrops(world, pos, meta, 0);
//        if (b == Blocks.mob_spawner) {
//            ItemStack is = RotaryItems.SPAWNER.get();
//            BlockEntityMobSpawner te = (BlockEntityMobSpawner) world.getBlockEntity(pos);
//            ReikaSpawnerHelper.addMobNBTToItem(is, te);
//            li.add(is);
//        }
//        ReikaItemHelper.dropItems(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, li);
//        world.setBlockToAir(pos);
//    }
//
//    @Override
//    public boolean canBeCollidedWith() {
//        return true;
//    }
//
//    @Override
//    public void applyEntityCollision(Entity e) {
//        e.motionX = motionX;
//        e.motionY = motionY;
//        e.motionZ = motionZ;
//    }
//
//    @Override
//    public boolean shouldRenderInPass(int pass) {
//        return pass == 1;
//    }
//}
