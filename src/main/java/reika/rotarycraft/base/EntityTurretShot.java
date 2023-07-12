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
import net.minecraft.core.Registry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryManager;
import reika.rotarycraft.auxiliary.TurretDamage;
import reika.rotarycraft.base.blockentity.BlockEntityAimedCannon;

public abstract class EntityTurretShot extends Fireball implements IEntityAdditionalSpawnData {

    protected BlockEntityAimedCannon gun;

    public EntityTurretShot(final EntityType<? extends Fireball> entityType, Level world) {
        super(entityType, world);
    }

    public EntityTurretShot(Level world, BlockPos pos, BlockPos vpos, BlockEntityAimedCannon te) {
        super(EntityType.FIREBALL, pos.getX(), pos.getY(), pos.getZ(), vpos.getX(), vpos.getY(), vpos.getZ(), world);
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
    protected AABB makeBoundingBox() {
        return new AABB(blockPosition().getX() + 0.4, blockPosition().getY() + 0.4, blockPosition().getZ() + 0.4, blockPosition().getX() + 0.6, blockPosition().getY() + 0.6, blockPosition().getZ() + 0.6);
    }

    protected abstract int getAttackDamage();

    protected abstract void applyAttackEffectsToEntity(Level world, Entity el);


    protected final DamageSource getDamageSource() {
        if (gun == null || !gun.hasLevel())
            return damageSources().generic();
        return new TurretDamage(gun);
    }

}
