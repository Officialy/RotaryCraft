///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.weaponry;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.entity.projectile.Fireball;
//import net.minecraft.world.item.enchantment.Enchantment;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.phys.AABB;
//import net.minecraft.world.phys.Vec3;
//import net.minecraftforge.common.MinecraftForge;
//import net.minecraftforge.eventbus.api.Event;
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.ModList;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.dragonapi.libraries.mathsci.ReikaVectorHelper;
//import reika.rotarycraft.auxiliary.MachineEnchantmentHandler;
//import reika.rotarycraft.auxiliary.interfaces.EnchantableMachine;
//import reika.rotarycraft.base.blockentity.BlockEntityProtectionDome;
//import reika.rotarycraft.registry.MachineRegistry;
//
//import java.util.Iterator;
//import java.util.List;
//
//public class BlockEntityForceField extends BlockEntityProtectionDome implements EnchantableMachine {
//
//    public static final int FALLOFF = 32768;
//    private final MachineEnchantmentHandler enchantments = new MachineEnchantmentHandler().addFilter(Enchantment.protection);
//
//    public double[] getBoundaryCoord(double x, double y, double z) {
//        double[] xyz = new double[3];
//        Vec3 vec = ReikaVectorHelper.getVec2Pt(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, pos);
//        xyz[0] = vec.normalize().xCoord;
//        xyz[1] = vec.normalize().yCoord;
//        xyz[2] = vec.normalize().zCoord;
//        for (int i = 0; i < 3; i++)
//            xyz[i] *= this.getRange();
//        return xyz;
//    }
//
//    @Override
//    public int getRangeBoost() {
//        return 8 * enchantments.getEnchantment(Enchantment.protection);
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        tickcount++;
//        this.getPowerBelow();
//        if (power < MINPOWER)
//            return;
//        this.spawnParticles(world, pos);
//        this.setColor(64 * 4 / tickcount, 128 + 128 * 4 / tickcount, 255);
//        AABB field = this.getRangedBox();
//        List<Entity> threats = world.getEntities(Entity.class, field);
//        for (Entity e : threats) {
//            this.protect(world, e);
//        }
//    }
//
//    private void protect(Level world, Entity threat) {
//        double x = threat.getY;
//        double y = threat.getY();
//        double z = threat.posZ;
//        if (ModList.METEORCRAFT.isLoaded()) {
//            if (threat instanceof MeteorEntity) {
//                ((MeteorEntity) threat).destroy();
//            }
//        }
//        if (this.isAtBorder(pos) || threat instanceof EntityArrow) {
//            if (threat instanceof EntityWitherSkull) {
//                ((EntityWitherSkull) threat).kill();
//                if (!world.isClientSide)
//                    world.explode(null, pos, 1F, false);
//                tickcount = 0;
//            }
//            if (threat instanceof Fireball) {
//                if (((EntityFireball) threat).shootingEntity instanceof EntityGhast) {
//                    if (!world.isClientSide)
//                        world.explode(null, pos, 2F, true);
//                }
//                if (((Fireball) threat).shootingEntity instanceof EntityBlaze) {
//                    for (int k = 0; k < 6 + DragonAPI.rand.nextInt(7); k++)
//                        world.addParticle("flame", x - 0.2 + 0.4 * DragonAPI.rand.nextFloat(), y - 0.2 + 0.4 * DragonAPI.rand.nextFloat(), z - 0.2 + 0.4 * DragonAPI.rand.nextFloat(), 0, 0, 0);
//                    world.playAuxSFX(1008, (int) x, (int) y, (int) z, 1);
//                }
//                if (((EntityFireball) threat).shootingEntity instanceof Player) {
//                    if (!world.isClientSide)
//                        world.explode(null, pos, 2F, true);
//                }
//                threat.kill();
//                tickcount = 0;
//            }
//            if (threat instanceof EntityRailGunShot) {
//                EntityRailGunShot e = (EntityRailGunShot) threat;
//                e.onHit(null);
//                tickcount = 0;
//            }
//            if (threat instanceof EntityPotion) {
//                if (!threat.isAlive()) {
//                    List var2 = Items.potionitem.getEffects(((EntityPotion) threat).getPotionDamage());
//                    if (var2 != null && !var2.isEmpty()) {
//                        AABB var3 = ((EntityPotion) threat).boundingBox.expand(4.0D, 2.0D, 4.0D);
//                        List<EntityPotion> var4 = ((EntityPotion) threat).level.getEntities(LivingEntity.class, var3);
//                        if (var4 != null && !var4.isEmpty()) {
//                            Iterator var5 = var4.iterator();
//                            while (var5.hasNext()) {
//                                LivingEntity var6 = (LivingEntity) var5.next();
//                                double var7 = ((EntityPotion) threat).getDistanceSqToEntity(var6);
//                                if (var7 < 16.0D) {
//                                    double var9 = 1.0D - Math.sqrt(var7) / 4.0D;
//                                    Iterator var11 = var2.iterator();
//                                    while (var11.hasNext()) {
//                                        MobEffect var12 = (MobEffect) var11.next();
//                                        int var13 = var12.getPotionID();
//                                        if (Potion.potionTypes[var13].isInstant())
//                                            Potion.potionTypes[var13].affectEntity(((EntityPotion) threat).getThrower(), var6, var12.getAmplifier(), var9);
//                                        else {
//                                            int var14 = (int) (var9 * var12.getDuration() + 0.5D);
//                                            if (var14 > 20)
//                                                var6.addMobEffect(new MobEffect(var13, var14, var12.getAmplifier()));
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    ((EntityPotion) threat).level.playAuxSFX(2002, (int) Math.round(((EntityPotion) threat).getY), (int) Math.round(((EntityPotion) threat).getY()), (int) Math.round(((EntityPotion) threat).posZ), ((EntityPotion) threat).getPotionDamage());
//                    ((EntityPotion) threat).kill();
//                }
//                tickcount = 0;
//            }
//            if (threat instanceof EntitySnowball) {
//                threat.kill();
//                for (int j = 0; j < 3 + DragonAPI.rand.nextInt(3); j++)
//                    world.addParticle("snowballpoof", x - 0.2 + 0.4 * DragonAPI.rand.nextFloat(), y - 0.2 + 0.4 * DragonAPI.rand.nextFloat(), z - 0.2 + 0.4 * DragonAPI.rand.nextFloat(), 0, 0, 0);
//                world.playLocalSound(pos, "dig.snow", 1F, 1F);
//                tickcount = 0;
//            }
//            if (threat instanceof EntityEgg) {
//                threat.kill();
//                for (int j = 0; j < 3 + DragonAPI.rand.nextInt(3); j++)
//                    world.addParticle("snowballpoof", x - 0.2 + 0.4 * DragonAPI.rand.nextFloat(), y - 0.2 + 0.4 * DragonAPI.rand.nextFloat(), z - 0.2 + 0.4 * DragonAPI.rand.nextFloat(), 0, 0, 0);
//                world.playLocalSound(pos, "DragonAPI.rand.glass", 1F, 5F);
//                if (!world.isClientSide && DragonAPI.rand.nextInt(8) == 0) {
//                    byte var2 = 1;
//                    if (DragonAPI.rand.nextInt(32) == 0)
//                        var2 = 4;
//                    for (int var3 = 0; var3 < var2; ++var3) {
//                        EntityChicken var4 = new EntityChicken(world);
//                        var4.setGrowingAge(-24000);
//                        var4.setLocationAndAngles(pos, threat.rotationYaw, 0);
//                        level.addFreshEntity(var4);
//                    }
//                }
//                tickcount = 0;
//            }
//            if (threat instanceof Player) {
//                if (!threat.getUniqueID().equals(placerUUID)) {
//                    double dx = threat.getY - xCoord - 0.5;
//                    double dy = threat.getY() - yCoord - 1;
//                    double dz = threat.posZ - zCoord - 0.5;
//                    double dd = ReikaMathLibrary.py3d(dx, dy, dz);
//                    if (dd >= this.getRange() - 1.5) {
//                        double v = 1;
//                        threat.motionX = v * dx / dd;
//                        threat.motionY = v * dy / dd;
//                        threat.motionZ = v * dz / dd;
//                        threat.velocityChanged = true;
//                        tickcount = 0;
//                    }
//                }
//            }
//            if (threat instanceof EntityArrow) {
//                if (threat.motionX != 0 || threat.motionZ != 0)
//                    world.playLocalSound(pos, "DragonAPI.rand.bowhit", 1, 1);
//                threat.rotationPitch = -90;
//                threat.motionX = 0;
//                if (threat.motionY > 0)
//                    threat.motionY = 0;
//                threat.motionZ = 0;
//                tickcount = 0;
//            }
//            if (threat instanceof PrimedTnt) {
//                threat.kill();
//                if (!world.isClientSide)
//                    world.explode(null, pos, 4F, true);
//                tickcount = 0;
//            }
//            if (ModExplosiveHandler.getInstance().isModExplosive(threat)) {
//                threat.kill();
//                if (!world.isClientSide)
//                    world.explode(null, pos, 4F, true);
//                tickcount = 0;
//            }
//            if (InterfaceCache.IMISSILE.instanceOf(threat)) {
//                ((IMissileEntity) threat).destroyMissile(this, null, 1, false, true, true);
//                if (!world.isClientSide)
//                    world.explode(null, pos, 4F, true);
//                tickcount = 0;
//            }
//            if (threat instanceof EntityMob || threat instanceof EntityGhast || threat instanceof Slime) {
//                int mult = 1;
//                //if (this.isInside(pos))
//                //	mult = -1;
//                double dx = x - xCoord;
//                double dy = y - yCoord;
//                double dz = z - zCoord;
//                double dist = ReikaMathLibrary.py3d(dx, dy, dz);
//                threat.motionX = mult * dx / dist / 10;
//                if (threat.onGround)
//                    threat.motionY = mult * dy / dist / 10;
//                threat.motionZ = mult * dz / dist / 10;
//                threat.rotationYaw = threat.rotationYaw - 30 + DragonAPI.rand.nextInt(61);
//                //if (!world.isClientSide)
//                threat.velocityChanged = true;
//            }
//            if (threat instanceof EntityWolf) {
//                if (((EntityWolf) threat).isAngry()) {
//                    int mult = 1;
//                    if (this.isInside(pos))
//                        mult = -1;
//                    double dx = x - xCoord;
//                    double dy = y - yCoord;
//                    double dz = z - zCoord;
//                    double dist = ReikaMathLibrary.py3d(dx, dy, dz);
//                    threat.motionX = mult * dx / dist / 15;
//                    if (threat.onGround)
//                        threat.motionY = mult * dy / dist / 15;
//                    threat.motionZ = mult * dz / dist / 15;
//                    threat.rotationYaw = DragonAPI.rand.nextInt(360);
//                    //if (!world.isClientSide)
//                    threat.velocityChanged = true;
//                }
//            }
//            Event evt = new ForceFieldEvent(this, threat);
//            MinecraftForge.EVENT_BUS.post(evt);
//
//            if (evt.getResult() == Result.ALLOW) {
//                threat.kill();
//                world.explode(null, pos, 4F, true);
//            }
//        }
//    }
//
//    private boolean isAtBorder(double x, double y, double z) {
//        double dx = x - xCoord - 0.5;
//        double dy = y - yCoord - 0.5;
//        double dz = z - zCoord - 0.5;
//        double dist = ReikaMathLibrary.py3d(dx, dy, dz);
//        if (dist > this.getRange())
//            return false;
//        return (ReikaMathLibrary.approxr(dist, this.getRange(), 3));
//    }
//
//    private boolean isInside(double x, double y, double z) {
//        double dx = x - xCoord;
//        double dy = y - yCoord;
//        double dz = z - zCoord;
//        double dist = ReikaMathLibrary.py3d(dx, dy, dz);
//        if (dist > this.getRange())
//            ;//return false;
//        return (ReikaMathLibrary.approxr(dist, this.getRange(), 3));
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//        NBT.putInt("setRange", setRange);
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//        setRange = NBT.getInt("setRange");
//    }
//
//    @Override
//    public void saveAdditional(CompoundTag NBT) {
//        super.saveAdditional(NBT);
//
//        NBT.put("enchants", enchantments.saveAdditional());
//    }
//
//    @Override
//    public void load(CompoundTag NBT) {
//        super.load(NBT);
//
//        enchantments.load(NBT.getTagList("enchants", NBTTypes.COMPOUND.ID));
//    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.FORCEFIELD;
//    }
//
//    @Override
//    public String getParticleType() {
//        return "magicCrit";
//    }
//
//    @Override
//    public int getFallOff() {
//        return FALLOFF;
//    }
//
//    @Override
//    public MachineEnchantmentHandler getEnchantmentHandler() {
//        return enchantments;
//    }
//}
