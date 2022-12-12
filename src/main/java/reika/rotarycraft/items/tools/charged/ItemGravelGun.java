///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.items.tools.charged;
//
//import net.minecraft.world.damagesource.DamageSource;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.item.ItemEntity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.phys.AABB;
//import net.minecraft.world.phys.Vec3;
//import reika.dragonapi.instantiable.data.immutable.DecimalPosition;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.dragonapi.libraries.mathsci.ReikaVectorHelper;
//import reika.rotarycraft.auxiliary.GravelGunDamage;
//import reika.rotarycraft.base.ItemChargedTool;
//
//import reika.rotarycraft.registry.RotaryItems;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ItemGravelGun extends ItemChargedTool {
//
//    public ItemGravelGun() {
//        super();
//    }
//
//    @Override
//    public ItemStack onItemRightClick(ItemStack is, Level world, Player ep) {
//        if (is.getItemDamage() <= 0) {
//            this.noCharge();
//            return is;
//        }
//        this.warnCharge(is);
//        if (!ReikaPlayerAPI.playerHasOrIsCreative(ep, Blocks.GRAVEL, -1)) {
//            if (!world.isClientSide)
//                world.playAuxSFX(1001, (int) ep.getY, (int) ep.getY(), (int) ep.posZ, 1);
//            return is;
//        }
//        Vec3 look = ep.getLookVec();
//        for (float i = 1; i <= 128; i += 0.5) {
//            DecimalPosition looks = ReikaVectorHelper.getPlayerLookCoords(ep, i);
//            AABB fov = looks.getAABB(0.5);
//            List<Entity> li = world.getEntities(Entity.class, fov);
//            ArrayList<Entity> infov = new ArrayList<>();
//            for (Entity e : li) {
//                if (!this.isFiringPlayer(e, ep)) {
//                    if (!ep.equals(e) && this.isEntityAttackable(e) && ReikaWorldHelper.lineOfSight(world, ep, e)) {
//                        infov.add(e);
//                    }
//                }
//            }
//            for (Entity ent : infov) {
//                double dist = ReikaMathLibrary.py3d(ep.getY - ent.getY, ep.getY() - ent.getY(), ep.posZ - ent.posZ);
//                double x = ep.getY + look.xCoord;
//                double y = ep.getY() + ep.getEyeHeight() + look.yCoord;
//                double z = ep.posZ + look.zCoord;
//                double dx = ent.getY - ep.getY;
//                double dy = ent.getY() - ep.getY();
//                double dz = ent.posZ - ep.posZ;
//                if (!world.isClientSide) {
//                    ItemStack fl = new ItemStack(Items.flint);
//                    ItemEntity ei = new ItemEntity(world, look.xCoord / look.lengthVector() + ep.getY, look.yCoord / look.lengthVector() + ep.getY(), look.zCoord / look.lengthVector() + ep.posZ, fl);
//                    ei.delayBeforeCanPickup = 100;
//                    ei.motionX = dx;
//                    ei.motionY = dy + 1;
//                    ei.motionZ = dz;
//                    //ReikaChatHelper.writeCoords(world, ei.getY, ei.getY(), ei.posZ);
//                    ei.velocityChanged = true;
//                    world.playSoundAtEntity(ep, "dig.gravel", 1.5F, 2F);
//                    ei.lifespan = 5;
//                    world.addFreshEntity(ei);
//
//                    if (is.getItemDamage() > 4096) { //approx the 1-hit kill of a 10-heart mob
//                        //ReikaPacketHelper.sendUpdatePacket(RotaryCraft.packetChannel, PacketRegistry.GRAVELGUN.getMinValue(), world, (int)ent.getY, (int)ent.getY(), (int)ent.posZ);
//                        //world.playSoundAtEntity(ep, "DragonAPI.rand.explode", 0.25F, 1F);
//                    }
//                    if (ent instanceof EntityDragon) {
//                        EntityDragon ed = (EntityDragon) ent;
//                        ed.attackEntityFromPart(ed.dragonPartBody, DamageSource.causePlayerDamage(ep), this.getAttackDamage(is.getItemDamage()));
//                    } else if (ent instanceof EntityEnderCrystal) {
//                        ent.hurt(DamageSource.causePlayerDamage(ep), this.getAttackDamage(is.getItemDamage()));
//                    } else {
//                        LivingEntity e = (LivingEntity) ent;
//                        float dmg = this.getAttackDamage(is.getItemDamage());
//                        if (ent instanceof Player) {
//                            for (int n = 1; n < 5; n++) {
//                                RotaryItems ir = RotaryItems.getEntry(e.getEquipmentInSlot(n));
//                                if (ir != null) {
//                                    if (ir.isBedrockArmor())
//                                        dmg *= 0.75;
//                                }
//                            }
//                        }
//                        float pre = e.getHealth();
//                        ent.hurt(new GravelGunDamage(ep), dmg);
//                        float post = e.getHealth();
//                        if (post > 0) {
//                            float newh = Math.min(post, pre - Math.min(10, dmg));
//                            if (newh <= 0) {
//                                e.setHealth(0.01F);
//                                ent.hurt(new GravelGunDamage(ep), dmg);
//                            } else {
//                                e.setHealth(newh);
//                            }
//                        }
//                        if (dmg >= 500)
//                            RotaryAdvancements.MASSIVEHIT.triggerAchievement(ep);
//                    }
//                    if (ent instanceof EntityMob && (ent.isAlive() || ((LivingEntity) ent).getHealth() <= 0) && ReikaMathLibrary.py3d(ep.getY - ent.getY, ep.getY() - ent.getY(), ep.posZ - ent.posZ) >= 80)
//                        RotaryAdvancements.GRAVELGUN.triggerAchievement(ep);
//                }
//                //ReikaWorldHelper.spawnParticleLine(world, pos, ent.getY, ent.getY()+ent.height/2, ent.posZ, "crit", 0, 0, 0, 60);
//                for (float t = 0; t < 2; t += 0.05F)
//                    world.addParticle("crit", pos, dx / dist * t, dy / dist * t, dz / dist * t);
//            }
//            if (infov.size() > 1) {
//                RotaryAdvancements.DOUBLEKILL.triggerAchievement(ep);
//            }
//            if (infov.size() > 0) {
//                if (!ep.capabilities.isCreative())
//                    ReikaInventoryHelper.findAndDecrStack(Blocks.GRAVEL, -1, ep.inventory.mainInventory);
//                is.setItemDamage(is.getItemDamage() - this.getChargeConsumed(is.getItemDamage()));
//                return is;
//            }
//        }
//        return is;
//    }
//
//    private boolean isFiringPlayer(Entity e, Player ep) {
//        return e instanceof Player && (e.getCommandSenderName().equals(ep.getCommandSenderName()));
//    }
//
//    private boolean isEntityAttackable(Entity ent) {
//        if (ent instanceof Player && ReikaPlayerAPI.isReika((Player) ent))
//            return false;
//        if (!(ent instanceof LivingEntity))
//            return ent instanceof EntityEnderCrystal;
//        return RotaryConfig.COMMON.GRAVELPLAYER.get() || !(ent instanceof Player);
//    }
//
//    private int getChargeConsumed(int charge) {
//        return Math.max(1, ReikaMathLibrary.logbase2(1 + charge));
//    }
//
//    private float getAttackDamage(int charge) {
//        if (charge <= 0)
//            return 0;
//        if (charge == 1)
//            return 1;
//        long pow = ReikaMathLibrary.longpow(charge / 2, 3); //fits in long (^6 does not)
//        double base = this.getExpBase() + Math.pow(charge, this.getPowR()) / 150000D;
//        return (float) (1 + (ReikaMathLibrary.logbase(pow, 2) / 2) * ReikaMathLibrary.doubpow(base, charge));
//    }
//
//    private double getPowR() {
//        return RotaryConfig.COMMON.HARDGRAVELGUN.get() ? 0.15 : 0.1875;
//    }
//
//    private double getExpBase() {
//        return RotaryConfig.COMMON.HARDGRAVELGUN.get() ? 1.00005 : 1.0001;
//    }
//
//    @Override
//    public void addInformation(ItemStack is, Player ep, List li, boolean par4) {
//        float dmg = this.getAttackDamage(is.getItemDamage());
//        String s = dmg > 0 ? String.format("Dealing %.1f hearts of damage per shot", dmg / 2F) : "Unable to fire - requires charging";
//        li.add(s);
//    }
//
//}
