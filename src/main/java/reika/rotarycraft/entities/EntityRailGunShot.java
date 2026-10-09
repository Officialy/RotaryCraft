/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.decoration.BlockAttachedEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.jspecify.annotations.Nullable;
import reika.rotarycraft.api.event.RailgunImpactEvent;
import reika.rotarycraft.api.interfaces.TargetEntity;
import reika.rotarycraft.base.EntityRailgunShotBase;
import reika.rotarycraft.blockentities.weaponry.Turret.BlockEntityRailGun;
import reika.rotarycraft.items.ItemRailGunAmmo;
import reika.rotarycraft.registry.*;
import reika.rotarycraft.auxiliary.RailgunBlockDamage;

/** V33a kinetic impact: tiered terrain damage, disarming, potion stripping and momentum transfer. */
public final class EntityRailGunShot extends EntityRailgunShotBase {
    public EntityRailGunShot(EntityType<? extends EntityRailGunShot> type, Level level) { super(type, level); setAmmunition(ItemRailGunAmmo.stack(0)); }
    public EntityRailGunShot(Level level, Vec3 position, Vec3 velocity, BlockEntityRailGun gun, ItemRailGunAmmo.BasicRailGunAmmo type) {
        super(RotaryEntities.RAILGUN.get(), level, position, velocity, gun, type);
    }
    public int getPower() { return ItemRailGunAmmo.power(ammunition()); }
    public static int damageForTier(int power) {
        if (power == 15) return Integer.MAX_VALUE;
        return (int)(1 + power + Math.pow(4, power) / 16384D);
    }
    @Override public int getAttackDamage() { return damageForTier(getPower()); }
    public void breakSoftFlightBlock(ServerLevel server, BlockPos pos) { RailgunBlockDamage.get(server).breakConnected(server, pos, 4, gun); }
    @Override public void onHit(@Nullable HitResult hit) {
        if (!(level() instanceof ServerLevel server) || isRemoved()) return;
        if (hit instanceof BlockHitResult block && server.getBlockState(block.getBlockPos()).is(RotaryBlocks.RAILGUN.get())) {
            discard(); return;
        }
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.post(new RailgunImpactEvent(server, blockPosition(), getPower()));
        if (ConfigRegistry.ATTACKBLOCKS.getState() && ConfigRegistry.RAILGUNDAMAGE.getState())
            RailgunBlockDamage.get(server).impact(server, blockPosition(), getPower(), gun);
        // The old nested terrain loop visited this same entity box hundreds of times. Process each entity once.
        for (Entity entity : server.getEntities(this, new AABB(position(), position()).inflate(6))) {
            if (entity instanceof LivingEntity || entity instanceof TargetEntity) applyAttackEffectsToEntity(server, entity);
            else if (entity instanceof EndCrystal || entity instanceof BlockAttachedEntity)
                entity.hurtServer(server, entity.damageSources().generic(), getAttackDamage());
        }
        server.sendParticles(net.minecraft.core.particles.ParticleTypes.EXPLOSION, getX(), getY(), getZ(), 1, 0, 0, 0, 0);
        server.sendParticles(net.minecraft.core.particles.ParticleTypes.LAVA, getX(), getY(), getZ(), 686, 3, 3, 3, 0);
        discard();
    }
    @Override protected void applyAttackEffectsToEntity(Level level, Entity entity) {
        if (!(level instanceof ServerLevel server)) return;
        if (entity instanceof TargetEntity target && target.onRailgunImpact(gun, getAmmoType())) return;
        if (entity instanceof LivingEntity living) {
            living.removeAllEffects();
            if (!(living instanceof Player)) {
                for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND, EquipmentSlot.FEET,
                        EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD}) {
                    ItemStack held = living.getItemBySlot(slot);
                    if (held.isEmpty()) continue;
                    living.setItemSlot(slot, ItemStack.EMPTY);
                    var drop = new ItemEntity(server, living.getX(), living.getY(), living.getZ(), held.copy());
                    double angle = random.nextDouble() * Math.PI * 2;
                    drop.setDeltaMovement(Math.cos(angle) * .2, .2, Math.sin(angle) * .2);
                    drop.setPickUpDelay(300); server.addFreshEntity(drop);
                }
            }
            if (living instanceof EnderDragon dragon) {
                dragon.hurt(server, dragon.head, damageSources().explosion(this, getOwner()), getAttackDamage());
                if (!dragon.isAlive() && gun != null && gun.getPlacer() != null) RotaryAdvancements.RAILDRAGON.triggerAchievement(gun.getPlacer());
            } else living.hurtServer(server, getDamageSource(), getAttackDamage());
            if (living instanceof Player player && !player.isAlive()) RotaryAdvancements.RAILKILLED.triggerAchievement(player);
        }
        double multiplier = entity instanceof TargetEntity target ? target.getKnockbackMultiplier(gun, getAmmoType()) : 1;
        if (Double.isFinite(multiplier) && multiplier >= 0) {
            entity.setDeltaMovement(getDeltaMovement().scale(multiplier * getPower() / 15D));
            entity.syncVelocity = true;
        }
    }
}
