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
//import net.minecraft.ChatFormatting;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.InteractionResultHolder;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.Mob;
//import net.minecraft.world.entity.ai.navigation.PathNavigation;
//import net.minecraft.world.entity.ambient.Bat;
//import net.minecraft.world.entity.animal.Animal;
//import net.minecraft.world.entity.animal.Squid;
//import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
//import net.minecraft.world.entity.boss.wither.WitherBoss;
//import net.minecraft.world.entity.monster.EnderMan;
//import net.minecraft.world.entity.monster.Ghast;
//import net.minecraft.world.entity.monster.Slime;
//import net.minecraft.world.entity.monster.piglin.Piglin;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.pathfinder.Path;
//import net.minecraft.world.phys.AABB;
//import net.minecraft.world.phys.Vec3;
//import reika.dragonapi.libraries.io.ReikaChatHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.rotarycraft.base.ItemChargedTool;
//
//
//import java.util.List;
//
//public class ItemMotionTracker extends ItemChargedTool {
//
//    public int rendertick = 0;
//    public List inrange;
//
//    double lastdist;
//    String lastmobname;
//
//    public ItemMotionTracker() {
//        super();
//    }
//
//    //todo add noCharge();
//    @Override
//    public InteractionResultHolder<ItemStack> use(Level world, Player ep, InteractionHand hand) {
//        this.warnCharge(this.getDefaultInstance());
//        if (RotaryConfig.COMMON.CLEARCHAT.getState())
//            ReikaChatHelper.clearChat(); //clr
//        rendertick = 512;
//        for (float i = 1; i <= 128; i += 0.5) {
//            Vec3 look = ep.getLookAngle();
//            double dx = ep.getY();
//            double dy = ep.getY() + ep.getEyeHeight();
//            double dz = ep.getZ();
//            ReikaChatHelper.writeString(String.format("%.3f", look.x()) + " " + String.format("%.3f", look.y()) + " " + String.format("%.3f", look.z()));
//            look.xCoord *= i;
//            look.yCoord *= i;
//            look.zCoord *= i;
//            AABB fov = new AABB(dx + look.x() - 0.5, dy + look.y() - 0.5, dz + look.z() - 0.5, dx + look.x() + 0.5, dy + look.y() + 0.5, dz + look.z() + 0.5);
//            List infov = world.getEntities(LivingEntity.class, fov);
//            if (infov.size() > 0) {
//                String mob;
//                if (infov.size() > 1)
//                    mob = "Mobs";
//                else
//                    mob = "Mob";
//                //ReikaChatHelper.write(infov.size()+String.format(" %s", mob)+" Detected:");
//                for (int k = 0; k < infov.size(); k++) {
//                    LivingEntity ent = (LivingEntity) infov.get(k);
//                    double dist = ReikaMathLibrary.py3d(dx - ent.getY(), dy - ent.getY() - ent.getEyeHeight(), dz - ent.getZ());
//                    ChatFormatting color;
//                    String mobname = ent.getName().getString();
//                    if (ent instanceof Mob || ent instanceof Slime || ent instanceof Ghast)
//                        color = ChatFormatting.RED;
//                    else if (ent instanceof Animal || ent instanceof Bat || ent instanceof Squid)
//                        color = ChatFormatting.GREEN;
//                    else
//                        color = ChatFormatting.WHITE;
//                    if (ent instanceof EnderMan || ent instanceof Piglin)
//                        color = ChatFormatting.YELLOW;
//                    if (ent instanceof EnderDragon)
//                        color = ChatFormatting.DARK_PURPLE;
//                    if (ent instanceof WitherBoss)
//                        color = ChatFormatting.DARK_GRAY;
//                    if (!(ent instanceof Player) && (dist <= 32 || ent instanceof WitherBoss || ent instanceof EnderDragon) && !(lastmobname == mobname && dist == lastdist)) {
//                        ReikaChatHelper.write(color + mobname + ChatFormatting.WHITE + String.format(" %.2f", dist - 1) + "m away.");
//                        if (ent instanceof EnderDragon) {
//                            EnderDragon ed = (EnderDragon) ent;
//                            if (ReikaMathLibrary.approxr(ed.getTarget().getX(), ep.getY(), 4))
//                                if (ReikaMathLibrary.approxr(ed.getTarget().getY(), ep.getY(), 4))
//                                    if (ReikaMathLibrary.approxr(ed.getTarget().getZ(), ep.getZ(), 4))
//                                        ReikaChatHelper.writeFormattedString("Dragon is Attacking!", ChatFormatting.RED);
//                        } else if (ent instanceof Mob) {
//                            Mob em = (Mob) ent;
//                            Path path = em.getNavigation().getPath();
//                            if (em.getTarget() == ep || em.getTarget() == ep)
//                                ReikaChatHelper.writeFormattedString("Mob is Attacking!", ChatFormatting.RED);
//                        }
//                    }
//					/*double[] vel = ReikaPhysicsHelper.cartesianToPolar(ent.motionX, ent.motionY, ent.motionZ);
//					ReikaChatHelper.write(String.format(" %.2f", vel[0])+"  "+String.format(" %.2f", vel[1])+"  "+String.format(" %.2f", vel[2]));
//					ReikaChatHelper.write("�4"+String.format(" %.2f", ReikaPhysicsHelper.cartesianToPolar(dx, dy, dz)[0])+"  "+String.format(" %.2f", ReikaPhysicsHelper.cartesianToPolar(dx, dy, dz)[1])+"  "+String.format(" %.2f", ReikaPhysicsHelper.cartesianToPolar(dx, dy, dz)[2]));
//					if (vel[1] == ReikaPhysicsHelper.cartesianToPolar(dx, dy, dz)[1]) {
//						if (vel[2] == ReikaPhysicsHelper.cartesianToPolar(dx, dy, dz)[2]) {
//							if (ent instanceof EntityDragon || ent instanceof EntityWither)
//								color = "�4";
//							else if (ent instanceof EntityMob)
//								color = "�e";
//							else
//								color = "�f";
//							ReikaChatHelper.write(String.format("%s", color)+"Mob is approaching you!");
//						}
//					}*/
//
//                    lastmobname = mobname;
//                    lastdist = dist;
//                }
//            }
//        }
//        return InteractionResultHolder.success(this.getDefaultInstance());
//    }
//
//}
