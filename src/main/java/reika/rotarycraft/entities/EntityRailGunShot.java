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
//import reika.rotarycraft.api.Interfaces.RailGunAmmo.RailGunAmmoType;
//import reika.rotarycraft.api.Interfaces.TargetEntity;
//import reika.rotarycraft.base.EntityRailgunShotBase;
//import reika.rotarycraft.blockentities.Weaponry.Turret.BlockEntityRailGun;
//import reika.rotarycraft.items.ItemRailGunAmmo;
//
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.RotaryAdvancements;
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.dragonapi.libraries.registry.ReikaParticleHelper;
//import net.minecraft.core.BlockPos;
//import net.minecraft.util.Mth;
//import net.minecraft.world.damagesource.DamageSource;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.EquipmentSlot;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
//import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
//import net.minecraft.world.entity.decoration.ItemFrame;
//import net.minecraft.world.entity.decoration.Painting;
//import net.minecraft.world.entity.item.ItemEntity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Explosion;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.levelgen.structure.BoundingBox;
//import net.minecraft.world.phys.AABB;
//import net.minecraft.world.phys.HitResult;
//import net.minecraft.world.phys.Vec3;
//import net.neoforged.common.NeoForge;
//
//import java.util.List;
//
//public class EntityRailGunShot extends EntityRailgunShotBase {
//
//    private int power;
//
//    public EntityRailGunShot(final EntityType<? extends EntityRailGunShot> entityType, Level world) {
//        super(entityType, world);
//    }
//
//    public EntityRailGunShot(Level world, BlockPos pos, BlockPos vpos, int pw, BlockEntityRailGun r) {
//        this(world, pos, vpos, pw, r, ItemRailGunAmmo.getType(pw));
//    }
//
//    public EntityRailGunShot(Level world, BlockPos pos, BlockPos vpos, BlockEntityRailGun r, RailGunAmmoType t) {
//        super(world, pos, 0, 0, 0, r, t);
//        motionX = vx;
//        motionY = vy;
//        motionZ = vz;
//        if (!world.isClientSide())
//            velocityChanged = true;
//        power = pw;
//        gun = r;
//    }
//
//    @Override
//    protected int getAttackDamage() {
//        return 0;
//    }
//
//    @Override
//    protected void applyAttackEffectsToEntity(Level world, Entity el) {
//
//    }
//
//    @Override
//    public void tick() {
//        tickCount++;
//        boolean hit = false;
//        int x = Mth.floor(getY);
//        int y = Mth.floor(getY());
//        int z = Mth.floor(posZ);
//        Block id = level.getBlock(pos);
//        MachineRegistry m = MachineRegistry.getMachine(level, getY, getY(), posZ);
//        List mobs = level.getEntities(LivingEntity.class, this.getBoundingBox().expand(1, 1, 1));
//        //ReikaJavaLibrary.pConsole("ID: "+id+" and "+mobs.size()+" mobs");
//        hit = (mobs.size() > 0 || (m != MachineRegistry.RAILGUN && id != Blocks.AIR && !ReikaWorldHelper.softBlocks(level, pos)));
//        //ReikaJavaLibrary.pConsole(hit+"   by "+id+"  or mobs "+mobs.size());
//        if (ReikaWorldHelper.softBlocks(level, pos) && !(id instanceof BlockLiquid || id instanceof BlockFluidBase) && RotaryConfig.COMMON.ATTACKBLOCKS.getState())
//            ReikaWorldHelper.recursiveBreakWithinSphere(level, pos, id, -1, pos, 4);
//        if (hit) {
//            //ReikaChatHelper.write("HIT  @  "+tickCount+"  by "+(mobs.size() > 0));
//            this.onHit(null);
//            if (power < 15 || true)
//                this.kill();
//            return;
//        }
//        //ReikaChatHelper.write(this.tickCount);
//        for (float i = -0.2F; i <= 0.2; i += 0.2F)
//            level.explode(this, getY + i, getY() + i, posZ + i, 0F, false);
//        //level.addParticle("hugeexplosion", getY, getY(), posZ, 0, 0, 0);
//        if (!level.isClientSide() && (shootingEntity != null && shootingEntity.isAlive() || !level.blockExists((int) getY, (int) getY(), (int) posZ)))
//            this.kill();
//        else {
//            if (tickCount > 80 && !level.isClientSide())
//                this.onHit(null);
//            this.tick();
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
//                level.explode(this, getY, getY(), posZ, 3F, false);
//                for (int var19 = 0; var19 < 4; ++var19) {
//                    float var18 = 0.25F;
//                    ReikaParticleHelper.BUBBLE.spawnAt(level, getY - motionX * var18, getY() - motionY * var18, posZ - motionZ * var18, motionX, motionY, motionZ);
//                }
//            }
//            this.setPosition(getY, getY(), posZ);
//        }
//    }
//
//    @Override
//    public void onHit(HitResult mov) {
//        if (mov != null && MachineRegistry.getMachine(level, mov.blockX, mov.blockY, mov.blockZ) == MachineRegistry.RAILGUN) {
//            this.kill();
//            return;
//        }
//        if (isAlive())
//            return;
//        Level world = level;
//        double x = getY;
//        double y = getY();
//        double z = posZ;
//        int x0 = (int) Math.floor(x);
//        int y0 = (int) Math.floor(y);
//        int z0 = (int) Math.floor(z);
//        NeoForge.EVENT_BUS.post(new RailgunImpactEvent(world, x0, y0, z0, this.getPower()));
//        LivingEntity el;
//        Entity ent;
//        ReikaParticleHelper.EXPLODE.spawnAt(world, x0, y0, z0);
//        for (int i = -3; i <= 3; i++) {
//            for (int j = -3; j <= 3; j++) {
//                for (int k = -3; k <= 3; k++) {
//                    if (i * j * k < 9 && i * j * k > -9) {
//                        //ReikaJavaLibrary.pConsole(RotaryConfig.COMMON.BLOCKDAMAGE.get()+" with "+power+" on "+FMLCommonHandler.instance().getEffectiveSide());
//                        if (RotaryConfig.COMMON.ATTACKBLOCKS.get() && RotaryConfig.COMMON.RAILGUNDAMAGE.getState()) {
//                            Block id = world.getBlock(x0 + i, y0 + j, z0 + k);
//                            if (ReikaWorldHelper.softBlocks(world, x0 + i, y0 + j, z0 + k) && !ReikaWorldHelper.isLiquidSourceBlock(level, x0 + i, y0 + j, z0 + k))
//                                ReikaWorldHelper.recursiveBreakWithinSphere(world, x0 + i, y0 + j, z0 + k, id, -1, x0 + i, y0 + j, z0 + k, 5);
//                            if (power >= 1) {
//                                ReikaWorldHelper.recursiveBreakWithinSphere(world, x0 + i, y0 + j, z0 + k, Blocks.LEAVES, -1, x0 + i, y0 + j, z0 + k, 5);
//                                ReikaWorldHelper.recursiveBreakWithinSphere(world, x0 + i, y0 + j, z0 + k, Blocks.LEAVES2, -1, x0 + i, y0 + j, z0 + k, 5);
//                                ReikaWorldHelper.recursiveBreakWithinSphere(world, x0 + i, y0 + j, z0 + k, Blocks.RED_FLOWER, -1, x0 + i, y0 + j, z0 + k, 5);
//                                ReikaWorldHelper.recursiveBreakWithinSphere(world, x0 + i, y0 + j, z0 + k, Blocks.YELLOW_FLOWER, -1, x0 + i, y0 + j, z0 + k, 5);
//                                ReikaWorldHelper.recursiveBreakWithinSphere(world, x0 + i, y0 + j, z0 + k, Blocks.REEDS, -1, x0 + i, y0 + j, z0 + k, 5);
//                                ReikaWorldHelper.recursiveBreakWithinSphere(world, x0 + i, y0 + j, z0 + k, Blocks.WATERLILY, -1, x0 + i, y0 + j, z0 + k, 5);
//                                ReikaWorldHelper.recursiveBreakWithinSphere(world, x0 + i, y0 + j, z0 + k, Blocks.BROWN_MUSHROOM, -1, x0 + i, y0 + j, z0 + k, 5);
//                                ReikaWorldHelper.recursiveBreakWithinSphere(world, x0 + i, y0 + j, z0 + k, Blocks.RED_MUSHROOM, -1, x0 + i, y0 + j, z0 + k, 5);
//                                ReikaWorldHelper.recursiveBreakWithinSphere(world, x0 + i, y0 + j, z0 + k, Blocks.SAPLING, -1, x0 + i, y0 + j, z0 + k, 5);
//                                ReikaWorldHelper.recursiveBreakWithinSphere(world, x0 + i, y0 + j, z0 + k, Blocks.WEB, -1, x0 + i, y0 + j, z0 + k, 5);
//                                ReikaWorldHelper.recursiveBreakWithinSphere(world, x0 + i, y0 + j, z0 + k, Blocks.CACTUS, -1, x0 + i, y0 + j, z0 + k, 5);
//                                ReikaWorldHelper.recursiveBreakWithinSphere(world, x0 + i, y0 + j, z0 + k, Blocks.FLOWER_POT, -1, x0 + i, y0 + j, z0 + k, 5);
//                            }
//                            if (power >= 2) {
//                                ReikaWorldHelper.recursiveBreakWithinSphere(world, x0 + i, y0 + j, z0 + k, Blocks.GLASS, -1, x0 + i, y0 + j, z0 + k, 5);
//                                ReikaWorldHelper.recursiveBreakWithinSphere(world, x0 + i, y0 + j, z0 + k, Blocks.GLASS_PANE, -1, x0 + i, y0 + j, z0 + k, 5);
//                                ReikaWorldHelper.recursiveBreakWithinSphere(world, x0 + i, y0 + j, z0 + k, Blocks.GLOWSTONE, -1, x0 + i, y0 + j, z0 + k, 5);
//                                ReikaWorldHelper.recursiveBreakWithinSphere(world, x0 + i, y0 + j, z0 + k, Blocks.RED_MUSHROOM_BLOCK, -1, x0 + i, y0 + j, z0 + k, 5);
//                                ReikaWorldHelper.recursiveBreakWithinSphere(world, x0 + i, y0 + j, z0 + k, Blocks.BROWN_MUSHROOM_BLOCK, -1, x0 + i, y0 + j, z0 + k, 5);
//                                ReikaWorldHelper.recursiveBreakWithinSphere(world, x0 + i, y0 + j, z0 + k, Blocks.LADDER, -1, x0 + i, y0 + j, z0 + k, 5);
//                                ReikaWorldHelper.recursiveBreakWithinSphere(world, x0 + i, y0 + j, z0 + k, Blocks.STANDING_SIGN, -1, x0 + i, y0 + j, z0 + k, 5);
//                                ReikaWorldHelper.recursiveBreakWithinSphere(world, x0 + i, y0 + j, z0 + k, Blocks.WALL_SIGN, -1, x0 + i, y0 + j, z0 + k, 5);
//                            }
//                            if (power >= 3) {
//                                ReikaWorldHelper.recursiveBreakWithinSphere(world, x0 + i, y0 + j, z0 + k, Blocks.LOG, -1, x0 + i, y0 + j, z0 + k, 5);
//                                ReikaWorldHelper.recursiveBreakWithinSphere(world, x0 + i, y0 + j, z0 + k, Blocks.LOG2, -1, x0 + i, y0 + j, z0 + k, 5);
//                                ReikaWorldHelper.recursiveBreakWithinSphere(world, x0 + i, y0 + j, z0 + k, Blocks.PLANKS, -1, x0 + i, y0 + j, z0 + k, 5);
//                                ReikaWorldHelper.recursiveBreakWithinSphere(world, x0 + i, y0 + j, z0 + k, Blocks.FENCE, -1, x0 + i, y0 + j, z0 + k, 5);
//                                ReikaWorldHelper.recursiveBreakWithinSphere(world, x0 + i, y0 + j, z0 + k, Blocks.WOOL, -1, x0 + i, y0 + j, z0 + k, 5);
//                                ReikaWorldHelper.recursiveBreakWithinSphere(world, x0 + i, y0 + j, z0 + k, Blocks.CRAFTING_TABLE, -1, x0 + i, y0 + j, z0 + k, 5);
//                                ReikaWorldHelper.recursiveBreakWithinSphere(world, x0 + i, y0 + j, z0 + k, Blocks.WOODEN_DOOR, -1, x0 + i, y0 + j, z0 + k, 5);
//                                ReikaWorldHelper.recursiveBreakWithinSphere(world, x0 + i, y0 + j, z0 + k, Blocks.NETHERRACK, -1, x0 + i, y0 + j, z0 + k, 5);
//                            }
//                            if (power >= 4) {
//                                ReikaWorldHelper.recursiveBreakWithinSphere(world, x0 + i, y0 + j, z0 + k, Blocks.SAND, -1, x0 + i, y0 + j, z0 + k, 5);
//                                ReikaWorldHelper.recursiveBreakWithinSphere(world, x0 + i, y0 + j, z0 + k, Blocks.GRAVEL, -1, x0 + i, y0 + j, z0 + k, 5);
//                                ReikaWorldHelper.recursiveBreakWithinSphere(world, x0 + i, y0 + j, z0 + k, Blocks.CLAY, -1, x0 + i, y0 + j, z0 + k, 5);
//                                ReikaWorldHelper.recursiveBreakWithinSphere(world, x0 + i, y0 + j, z0 + k, Blocks.SOUL_SAND, -1, x0 + i, y0 + j, z0 + k, 5);
//                            }
//                            if (power >= 3) {
//                                //ReikaWorldHelper.recursiveBreakWithinSphere(world, x0+i, y0+j, z0+k, Blocks.GRASS, -1, x0+i, y0+j, z0+k, 5);
//                                //ReikaWorldHelper.recursiveFillWithinSphere(world, x0+i, y0+j-7, z0+k, Blocks.GRASS, -1, Blocks.DIRT, 0, x0+i, y0+j-7, z0+k, 5);
//                                if (id == Blocks.GRASS) {
//                                    if (power >= 5) {
//                                         =world.getBlockMetadata(x0 + i, y0 + j, z0 + k);
//                                        ReikaItemHelper.dropItems(world, x0 + i, y0 + j, z0 + k, Block.getDrops(world, x0 + i, y0 + j, z0 + k, meta, 0));
//                                        world.setBlockToAir(x0 + i, y0 + j, z0 + k);
//                                    }
//                                    world.markAndNotifyBlock(new BlockPos(x0 + i, y0 + j, z0 + k));
//                                    if (id == Blocks.DIRT) {
//                                        ReikaItemHelper.dropItems(world, x0 + i, y0 + j, z0 + k, Block.getDrops(world, x0 + i, y0 + j, z0 + k, meta, 0));
//                                        world.setBlockToAir(x0 + i, y0 + j, z0 + k);
//                                    }
//                                    if (power >= 5) {
//                                        if (id == Blocks.STONE) {
//                                            world.setBlock(x0 + i, y0 + j, z0 + k, Blocks.COBBLESTONE);
//                                        }
//                                    }
//                                    if (id == Blocks.COBBLESTONE || id == Blocks.COBBLESTONE_WALL || id == Blocks.MOSSY_COBBLESTONE) {
//                                        ReikaItemHelper.dropItems(world, x0 + i, y0 + j, z0 + k, id.getDrops(id, world, new BlockPos(x0 + i, y0 + j, z0 + k), meta, 0));
//                                        world.setBlock(new BlockPos(x0 + i, y0 + j, z0 + k), Blocks.AIR.defaultBlockState(), 0);
//                                    }
//                                    if (power == 14) {
//                                        if (id != null && id != MachineRegistry.RAILGUN.getBlock() && !ReikaBlockHelper.isLiquid(id) && !ReikaBlockHelper.isUnbreakable(world, x0 + i, y0 + j, z0 + k, id, world.getBlockMetadata(x0 + i, y0 + j, z0 + k), null)) {
//                                            ReikaWorldHelper.recursiveBreakWithinSphere(world, x0 + i, y0 + j, z0 + k, id, -1, x0 + i, y0 + j, z0 + k, 3);
//                                        }
//                                    }
//                                    if (power == 15) {
//                                        if (id != null && id != MachineRegistry.RAILGUN.getBlock() && !ReikaBlockHelper.isLiquid(id) && !ReikaBlockHelper.isUnbreakable(world, x0 + i, y0 + j, z0 + k, id, world.getBlockMetadata(x0 + i, y0 + j, z0 + k), null)) {
//                                            ReikaWorldHelper.recursiveBreakWithinSphere(world, x0 + i, y0 + j, z0 + k, id, -1, x0 + i, y0 + j, z0 + k, 6);
//                                        }
//                                    }
//                                }
//
//                                AABB splash = AABB.of(new BoundingBox(x0, y0, z0, x0, y0, z0)).expandTowards(6, 6, 6); //todo possible error with x0 / all other x0's
//                                //world.explode(this, pos, 3F, false);
//                                List dmgd = world.getEntities(Entity.class, splash);
//                                for (int l = 0; l < dmgd.size(); l++) {
//                                    ent = (Entity) dmgd.get(l);
//                                    if (ent instanceof LivingEntity) {
//                                        el = (LivingEntity) ent;
//                                        this.applyAttackEffectsToEntity(world, el);
//                                    } else if (ent instanceof EndCrystal || ent instanceof Painting || ent instanceof ItemFrame) //Will not target but will destroy
//                                        ent.hurt(DamageSource.GENERIC, this.getAttackDamage());
//                                }
//                            }
//                            for (int m = 0; m < 2; m++) {
//                                ReikaParticleHelper.LAVA.spawnAt(world, x - 1 + 2 * DragonAPI.rand.nextFloat() + i, y - 1.5 + DragonAPI.rand.nextFloat() + j, z - 1 + 2 * DragonAPI.rand.nextFloat() + k);
//                            }
//
//                        }
//                    }
//                }
//                this.kill();
//                //ent.hurt(DamageSource.outOfWorld, el.getHealth()*(1+el.getTotalArmorValue()));
//            }
//
//            @Override
//            public int getAttackDamage () {
//                if (power == 15)
//                    return Integer.MAX_VALUE;
//                double pow = ReikaMathLibrary.intpow(4, power) / 16384D;
//                int dmg = (int) (1 + power + pow);
//                return dmg;
//            }
//
//            public int getPower () {
//                return power;
//            }
//
//            @Override
//            protected void applyAttackEffectsToEntity (Level world, Entity ent){
//                double x = ent.getX();
//                double y = ent.getY();
//                double z = ent.getZ();
//                if (ent instanceof TargetEntity) {
//                    if (((TargetEntity) ent).onRailgunImpact(gun, this.getType()))
//                        return;
//                }
//                if (ent instanceof LivingEntity) {
//                    LivingEntity el = (LivingEntity) ent;
//                    el.clearActivePotions();
//                    for (int h = 0; h < 5 && !(el instanceof Player); h++) {
//                        ItemStack held = el.getSlot(h).get();
//                        el.setItemSlot(EquipmentSlot.byTypeAndIndex(EquipmentSlot.Type.ARMOR, h), null);
//                        if (!world.isClientSide() && held != null) {
//                            ItemEntity ei = new ItemEntity(world, x, y, z, held);
//                            ReikaEntityHelper.addRandomDirVelocity(ei, 0.2);
//                            ei.delayBeforeCanPickup = 300;
//                            world.addFreshEntity(ei);
//                        }
//                    }
//                    //ReikaChatHelper.writeEntity(world, el);
//                    if (el instanceof EnderDragon) {
//                        ((EnderDragon) el).hurt(((EnderDragon) el).head, DamageSource.explosion(new Explosion(level, this, x, y, z, 20)), this.getAttackDamage());
//                        if (!el.isAlive() || el.getHealth() <= 0) {
//                            RotaryAdvancements.RAILDRAGON.triggerAchievement(gun.getPlacer());
//                        }
//                    } else {
//                        el.hurt(this.getDamageSource(), this.getAttackDamage());
//                    }
//                    if (el instanceof Player) {
//                        if (!el.isAlive() || el.getHealth() <= 0)
//                            RotaryAdvancements.RAILKILLED.triggerAchievement((Player) el);
//                    }
//                }
//                double p = ent instanceof TargetEntity ? ((TargetEntity) ent).getKnockbackMultiplier(gun, this.getType()) : 1;
//                ent.motionX = motionX * p * power / 15F;
//                ent.motionY = motionY * p * power / 15F;
//                ent.motionZ = motionZ * p * power / 15F;
//            }
//
//            @Override
//            public void writeSpawnData (FriendlyByteBuf buffer){
//
//            }
//
//            @Override
//            public void readSpawnData (FriendlyByteBuf additionalData){
//
//            }
//        }
//
//        @Override
//        public void writeSpawnData (FriendlyByteBuf buffer){
//
//        }
//
//        @Override
//        public void readSpawnData (FriendlyByteBuf additionalData){
//
//        }
//    }