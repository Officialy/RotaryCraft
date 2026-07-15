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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

import reika.rotarycraft.base.EntityTurretShot;
import reika.rotarycraft.base.blockentity.BlockEntityAimedCannon;
import reika.rotarycraft.registry.RotaryEntities;

/**
 * The anti-aircraft flak round fired by the AA Gun: it bursts into a small, non-terrain-damaging
 * explosion on impact (or after a short flight), shredding whatever flew into it. Flight is handled
 * by the turret-shot base.
 */
public class EntityFlakShot extends EntityTurretShot {

    public EntityFlakShot(EntityType<? extends EntityFlakShot> type, Level world) {
        super(type, world);
    }

    public EntityFlakShot(Level world, BlockPos pos, BlockPos vpos, BlockEntityAimedCannon gun) {
        super(world, pos, vpos, gun);
        this.setDeltaMovement(this.getDeltaMovement().scale(3));
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide() && this.tickCount > 60)
            this.burst();
    }

    @Override
    protected int getAttackDamage() {
        return 6;
    }

    @Override
    protected void applyAttackEffectsToEntity(Level world, Entity el) {
        el.hurt(this.getDamageSource(), this.getAttackDamage());
    }

    @Override
    protected void onHit(HitResult result) {
        this.burst();
    }

    private void burst() {
        if (level().isClientSide())
            return;
        level().explode(this, this.getX(), this.getY(), this.getZ(), 2F, Level.ExplosionInteraction.NONE);
        this.discard();
    }

    @Override
    public EntityType<?> getType() {
        return RotaryEntities.FLAKSHOT.get();
    }
}
