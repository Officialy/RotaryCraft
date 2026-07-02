/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.base;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.core.Registry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.hurtingprojectile.Fireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import net.minecraft.core.registries.BuiltInRegistries;
import reika.rotarycraft.auxiliary.TurretDamage;
import reika.rotarycraft.base.blockentity.BlockEntityAimedCannon;

// 1.21.5: IEntityAdditionalSpawnData → IEntityWithComplexSpawn; Fireball constructor now
// expects a Vec3 movement instead of three doubles; Entity#makeBoundingBox takes a Vec3.
public abstract class EntityTurretShot extends Fireball implements IEntityWithComplexSpawn {

    protected BlockEntityAimedCannon gun;

    public EntityTurretShot(final EntityType<? extends Fireball> entityType, Level world) {
        super(entityType, world);
    }

    public EntityTurretShot(Level world, BlockPos pos, BlockPos vpos, BlockEntityAimedCannon te) {
        super(EntityTypes.FIREBALL, world);
        this.setPos(pos.getX(), pos.getY(), pos.getZ());
        Vec3 direction = new Vec3(vpos.getX() - pos.getX(), vpos.getY() - pos.getY(), vpos.getZ() - pos.getZ()).normalize().scale(0.1);
        this.setDeltaMovement(direction);
        gun = te;
    }

    @Override
    protected void onHit(HitResult p_37260_) {
    }

    @Override
    public boolean displayFireAnimation() {
        return false;
    }

    @Override
    protected AABB makeBoundingBox(Vec3 pos) {
        return new AABB(pos.x + 0.4, pos.y + 0.4, pos.z + 0.4, pos.x + 0.6, pos.y + 0.6, pos.z + 0.6);
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf data) {
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf data) {
    }

    protected abstract int getAttackDamage();

    protected abstract void applyAttackEffectsToEntity(Level world, Entity el);


    protected final DamageSource getDamageSource() {
        if (gun == null || !gun.hasLevel())
            return damageSources().generic();
        return new TurretDamage(gun);
    }

}
