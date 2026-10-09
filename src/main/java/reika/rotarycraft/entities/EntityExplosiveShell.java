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

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.jspecify.annotations.Nullable;
import reika.rotarycraft.api.interfaces.TargetEntity;
import reika.rotarycraft.base.EntityRailgunShotBase;
import reika.rotarycraft.blockentities.weaponry.Turret.BlockEntityRailGun;
import reika.rotarycraft.items.ItemExplosiveShell;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryEntities;

/** V33a's eight-radius incendiary shell, with the same flight and target-extension contract as slugs. */
public final class EntityExplosiveShell extends EntityRailgunShotBase {
    public static final float EXPLOSION = 8;
    public EntityExplosiveShell(EntityType<? extends EntityExplosiveShell> type, Level level) { super(type, level); setAmmunition(reika.rotarycraft.registry.RotaryItems.SHELL.get().getDefaultInstance()); }
    public EntityExplosiveShell(Level level, Vec3 position, Vec3 velocity, BlockEntityRailGun gun) {
        super(RotaryEntities.EXPLOSIVE_SHELL.get(), level, position, velocity, gun, ItemExplosiveShell.ExplosiveRailGunAmmo.instance);
    }
    @Override public void onHit(@Nullable HitResult hit) {
        if (!(level() instanceof ServerLevel server) || isRemoved()) return;
        if (hit instanceof BlockHitResult block && server.getBlockState(block.getBlockPos()).is(RotaryBlocks.RAILGUN.get())) { discard(); return; }
        server.explode(this, getX(), getY(), getZ(), EXPLOSION, true, Level.ExplosionInteraction.TNT);
        for (Entity entity : server.getEntities(this, new AABB(position(), position()).inflate(EXPLOSION))) applyAttackEffectsToEntity(server, entity);
        discard();
    }
    @Override protected int getAttackDamage() { return 0; } // Native explosion applies the damage, as in V33a.
    @Override protected void applyAttackEffectsToEntity(Level level, Entity entity) {
        if (entity instanceof TargetEntity target) target.onRailgunImpact(gun, getAmmoType());
    }
}
