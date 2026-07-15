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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import reika.rotarycraft.base.EntityTurretShot;
import reika.rotarycraft.base.blockentity.BlockEntityAimedCannon;
import reika.rotarycraft.registry.RotaryEntities;

/**
 * The snowball-derived slug fired by the Freeze Gun: on hit it deep-freezes the target — slowness,
 * mining fatigue and weakness for several seconds (the port's stand-in for the legacy custom freeze
 * potion, which is not a registered effect in this build). Flight is handled by the turret-shot base.
 */
public class EntityFreezeGunShot extends EntityTurretShot {

    public EntityFreezeGunShot(EntityType<? extends EntityFreezeGunShot> type, Level world) {
        super(type, world);
    }

    public EntityFreezeGunShot(Level world, BlockPos pos, BlockPos vpos, BlockEntityAimedCannon gun) {
        super(world, pos, vpos, gun);
        this.setDeltaMovement(this.getDeltaMovement().scale(3));
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide() && this.tickCount > 80)
            this.discard();
    }

    @Override
    protected int getAttackDamage() {
        return 2;
    }

    @Override
    protected void applyAttackEffectsToEntity(Level world, Entity el) {
        if (el instanceof LivingEntity le) {
            le.hurt(this.getDamageSource(), 2);
            le.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 200, 6));
            le.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 2));
            le.addEffect(new MobEffectInstance(MobEffects.MINING_FATIGUE, 200, 3));
        }
    }

    @Override
    protected void onHit(HitResult result) {
        if (level().isClientSide())
            return;
        if (result instanceof EntityHitResult ehr && ehr.getEntity() instanceof LivingEntity le)
            this.applyAttackEffectsToEntity(level(), le);
        this.discard();
    }

    @Override
    public EntityType<?> getType() {
        return RotaryEntities.FREEZEGUN.get();
    }
}
