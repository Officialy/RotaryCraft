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
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import reika.rotarycraft.base.EntityTurretShot;
import reika.rotarycraft.base.blockentity.BlockEntityAimedCannon;
import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.RotaryEntities;

/**
 * The hypervelocity slug fired by the Rail Gun. Flight and collision are handled by the shared
 * {@link EntityTurretShot} (Fireball) base; this shot carries a power tier that scales its damage
 * and the size of the explosion it makes on impact (block-shredding when the config allows). It
 * self-destructs after a short life so a missed shot does not linger.
 */
public class EntityRailGunShot extends EntityTurretShot {

    private static final int LIFESPAN = 80;

    private int power;

    public EntityRailGunShot(EntityType<? extends EntityRailGunShot> type, Level world) {
        super(type, world);
    }

    public EntityRailGunShot(Level world, BlockPos pos, BlockPos vpos, BlockEntityAimedCannon gun, int power) {
        super(world, pos, vpos, gun);
        this.power = power;
        this.setDeltaMovement(this.getDeltaMovement().scale(4 + power)); //hypervelocity
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide() && this.tickCount > LIFESPAN)
            this.discard();
    }

    @Override
    protected int getAttackDamage() {
        return 10 + power * 8;
    }

    @Override
    protected void applyAttackEffectsToEntity(Level world, Entity el) {
        el.hurt(this.getDamageSource(), this.getAttackDamage());
    }

    @Override
    protected void onHit(HitResult result) {
        if (level().isClientSide())
            return;
        if (result instanceof EntityHitResult ehr && ehr.getEntity() instanceof LivingEntity le)
            this.applyAttackEffectsToEntity(level(), le);
        double r = 2 + power;
        boolean breakBlocks = ConfigRegistry.ATTACKBLOCKS.getState();
        level().explode(this, this.getX(), this.getY(), this.getZ(), (float) r,
                breakBlocks ? Level.ExplosionInteraction.MOB : Level.ExplosionInteraction.NONE);
        this.discard();
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf data) {
        data.writeInt(power);
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf data) {
        power = data.readInt();
    }

    @Override
    public EntityType<?> getType() {
        return RotaryEntities.RAILGUN.get();
    }

    public int getPower() {
        return power;
    }
}
