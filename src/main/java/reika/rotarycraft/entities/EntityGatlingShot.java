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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import reika.rotarycraft.base.EntityTurretShot;
import reika.rotarycraft.base.blockentity.BlockEntityAimedCannon;
import reika.rotarycraft.registry.RotaryEntities;

/**
 * A single high-velocity round from the Multi-Cannon (gatling gun): light damage per round, but the
 * cannon fires them in rapid bursts. Flight is handled by the turret-shot base; the round expires on
 * any impact or after a short flight.
 */
public class EntityGatlingShot extends EntityTurretShot {

    public EntityGatlingShot(EntityType<? extends EntityGatlingShot> type, Level world) {
        super(type, world);
    }

    public EntityGatlingShot(Level world, BlockPos pos, BlockPos vpos, BlockEntityAimedCannon gun) {
        super(world, pos, vpos, gun);
        this.setDeltaMovement(this.getDeltaMovement().scale(5));
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide() && this.tickCount > 60)
            this.discard();
    }

    @Override
    protected int getAttackDamage() {
        return 4;
    }

    @Override
    protected void applyAttackEffectsToEntity(Level world, Entity el) {
        el.hurt(this.getDamageSource(), this.getAttackDamage());
    }

    @Override
    protected void onHit(HitResult result) {
        if (level().isClientSide())
            return;
        if (result instanceof EntityHitResult ehr)
            this.applyAttackEffectsToEntity(level(), ehr.getEntity());
        this.discard();
    }

    @Override
    public EntityType<?> getType() {
        return RotaryEntities.GATLING.get();
    }
}
