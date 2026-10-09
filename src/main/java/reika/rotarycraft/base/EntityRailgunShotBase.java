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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.*;
import org.jspecify.annotations.Nullable;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.rotarycraft.api.interfaces.RailGunAmmo;
import reika.rotarycraft.blockentities.weaponry.Turret.BlockEntityRailGun;
import reika.rotarycraft.registry.RotaryBlocks;

/** V33a constant-velocity flight and full ammo identity, shared by kinetic slugs and explosive shells. */
public abstract class EntityRailgunShotBase extends EntityTurretShot {
    private ItemStack ammo = ItemStack.EMPTY;
    private @Nullable BlockPos gunPos;
    private int age;

    protected EntityRailgunShotBase(EntityType<? extends EntityRailgunShotBase> type, Level level) { super(type, level); }
    protected EntityRailgunShotBase(EntityType<? extends EntityRailgunShotBase> type, Level level, Vec3 position,
            Vec3 velocity, BlockEntityRailGun cannon, RailGunAmmo.RailGunAmmoType ammoType) {
        this(type, level);
        setPos(position); setDeltaMovement(velocity);
        gun = cannon; gunPos = cannon.getBlockPos(); ammo = ammoType.getItem().copyWithCount(1);
        if (cannon.getPlacer() != null) setOwner(cannon.getPlacer());
    }
    public final RailGunAmmo.@Nullable RailGunAmmoType getAmmoType() {
        return ammo.getItem() instanceof RailGunAmmo item ? item.getAmmo(ammo) : null;
    }
    public final ItemStack ammunition() { return ammo.copy(); }
    protected final void setAmmunition(ItemStack stack) { ammo = stack.copyWithCount(1); }
    @Override protected net.minecraft.world.damagesource.DamageSource getDamageSource() {
        // Projectile ownership survives save/load and an unloaded or removed firing machine.
        return new reika.rotarycraft.auxiliary.TurretDamage(level(), getOwner() instanceof net.minecraft.world.entity.player.Player player ? player : null);
    }
    private void resolveGun() {
        if (gun == null && gunPos != null && level().hasChunkAt(gunPos)
                && level().getBlockEntity(gunPos) instanceof BlockEntityRailGun cannon) gun = cannon;
    }
    @Override public final void tick() {
        baseTick(); age++; resolveGun();
        if (isRemoved()) return;
        if (level() instanceof ServerLevel && (!level().hasChunkAt(blockPosition()) || getOwner() != null && !getOwner().isAlive())) {
            discard(); return;
        }
        BlockPos current = blockPosition();
        boolean soft = ReikaWorldHelper.softBlocks(level(), current);
        if (this instanceof reika.rotarycraft.entities.EntityRailGunShot slug && soft && level() instanceof ServerLevel server
                && server.getBlockState(current).getFluidState().isEmpty() && reika.rotarycraft.registry.ConfigRegistry.ATTACKBLOCKS.getState())
            slug.breakSoftFlightBlock(server, current);
        if (age > 80 || !level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(1),
                entity -> entity != getOwner() && entity.isAlive()).isEmpty()
                || !soft && !level().getBlockState(current).is(RotaryBlocks.RAILGUN.get())) {
            onHit(null); return;
        }
        Vec3 start = position(), end = start.add(getDeltaMovement());
        if (level() instanceof ServerLevel && !level().hasChunkAt(BlockPos.containing(end))) { discard(); return; }
        var block = level().clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        Vec3 limit = block.getType() == HitResult.Type.MISS ? end : block.getLocation();
        HitResult hit = block.getType() == HitResult.Type.MISS ? null : block;
        double nearest = start.distanceToSqr(limit);
        for (Entity entity : level().getEntities(this, getBoundingBox().expandTowards(getDeltaMovement()).inflate(1),
                entity -> entity.isPickable() && entity != getOwner() && entity.isAlive())) {
            var intercept = entity.getBoundingBox().inflate(.3).clip(start, limit);
            if (intercept.isPresent() && start.distanceToSqr(intercept.get()) <= nearest) {
                nearest = start.distanceToSqr(intercept.get()); hit = new EntityHitResult(entity, intercept.get());
            }
        }
        if (hit != null) { setPos(hit.getLocation()); onHit(hit); return; }
        setPos(end);
        if (level() instanceof ServerLevel server) {
            if (this instanceof reika.rotarycraft.entities.EntityRailGunShot) {
                for (int i = -1; i <= 1; i++) {
                    server.sendParticles(net.minecraft.core.particles.ParticleTypes.EXPLOSION,
                            getX() + i * .2, getY() + i * .2, getZ() + i * .2, 1, 0, 0, 0, 0);
                    // V33a emitted zero-strength explosions here: preserve their sound without damage or terrain hooks.
                    server.playSound(null, getX() + i * .2, getY() + i * .2, getZ() + i * .2,
                            net.minecraft.sounds.SoundEvents.GENERIC_EXPLODE, net.minecraft.sounds.SoundSource.BLOCKS,
                            4, (1 + (random.nextFloat() - random.nextFloat()) * .2F) * .7F);
                }
            }
            if (isInWater()) {
                server.explode(this, getX(), getY(), getZ(), 3, Level.ExplosionInteraction.NONE);
                Vec3 bubble = position().subtract(getDeltaMovement().scale(.25));
                server.sendParticles(net.minecraft.core.particles.ParticleTypes.BUBBLE, bubble.x, bubble.y, bubble.z,
                        4, 0, 0, 0, 0);
            }
        }
    }
    /** Both concrete types must implement their original impact contract. */
    @Override protected abstract void onHit(@Nullable HitResult hit);

    @Override public void writeSpawnData(RegistryFriendlyByteBuf data) {
        data.writeBoolean(gunPos != null); if (gunPos != null) data.writeBlockPos(gunPos);
        ItemStack.STREAM_CODEC.encode(data, ammo);
    }
    @Override public void readSpawnData(RegistryFriendlyByteBuf data) {
        gunPos = data.readBoolean() ? data.readBlockPos() : null;
        ammo = ItemStack.STREAM_CODEC.decode(data); resolveGun();
    }
    @Override protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.store("Ammo", ItemStack.CODEC, ammo);
        if (gunPos != null) output.putLong("GunPos", gunPos.asLong());
        output.putInt("FlightAge", age);
    }
    @Override protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        ammo = input.read("Ammo", ItemStack.CODEC).orElseGet(() -> getType() == reika.rotarycraft.registry.RotaryEntities.EXPLOSIVE_SHELL.get()
                ? reika.rotarycraft.registry.RotaryItems.SHELL.get().getDefaultInstance() : reika.rotarycraft.items.ItemRailGunAmmo.stack(0));
        gunPos = input.getLong("GunPos").map(BlockPos::of).orElse(null);
        age = Math.max(0, input.getIntOr("FlightAge", 0)); resolveGun();
    }
}
