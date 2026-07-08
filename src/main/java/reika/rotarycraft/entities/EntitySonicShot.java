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

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;

import reika.rotarycraft.blockentities.level.BlockEntitySonicBorer;
import reika.rotarycraft.registry.RotaryEntities;

/**
 * The pressure-wave projectile a {@link BlockEntitySonicBorer} fires down its axis. It flies in a
 * straight line at constant velocity (no gravity) from the borer to the first solid surface the
 * borer detected, then flattens a {@code (2*FOV+1)²} cross-section there — dropping the blocks and
 * bruising any mobs in the blast — and vanishes. Faithful to the legacy {@code EntitySonicShot}: the
 * borer computes the target (borer + step·range) and the shot just carries it there for the visual.
 */
public class EntitySonicShot extends Entity implements IEntityWithComplexSpawn {

    private static final double SPEED = 2.0;

    private int stepX, stepY, stepZ;
    private double targetX, targetY, targetZ;

    public EntitySonicShot(EntityType<? extends EntitySonicShot> type, Level world) {
        super(type, world);
        this.noPhysics = true;
    }

    public EntitySonicShot(Level world, BlockEntitySonicBorer tile) {
        super(RotaryEntities.SHOCKWAVE.get(), world);
        this.noPhysics = true;
        BlockPos pos = tile.getBlockPos();
        stepX = tile.xstep;
        stepY = tile.ystep;
        stepZ = tile.zstep;
        this.setPos(pos.getX() + 0.5 + stepX, pos.getY() + 0.5 + stepY, pos.getZ() + 0.5 + stepZ);
        int[] tg = tile.getTargetPosn();
        targetX = tg[0] + 0.5;
        targetY = tg[1] + 0.5;
        targetZ = tg[2] + 0.5;
        this.setDeltaMovement(stepX * SPEED, stepY * SPEED, stepZ * SPEED);
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount > 1200) {
            this.discard();
            return;
        }
        Vec3 m = this.getDeltaMovement();
        double nx = this.getX() + m.x;
        double ny = this.getY() + m.y;
        double nz = this.getZ() + m.z;

        // Reached (or passed) the target plane along the travel axis?
        double travelled = stepX * (nx - targetX) + stepY * (ny - targetY) + stepZ * (nz - targetZ);
        if (travelled >= 0) {
            this.setPos(targetX, targetY, targetZ);
            if (!this.level().isClientSide())
                this.detonate();
            this.discard();
            return;
        }
        this.setPos(nx, ny, nz);
    }

    private void detonate() {
        Level world = this.level();
        BlockPos center = BlockPos.containing(targetX, targetY, targetZ);
        int k = BlockEntitySonicBorer.FOV;
        // Flatten the plane perpendicular to the travel axis.
        for (int a = -k; a <= k; a++) {
            for (int b = -k; b <= k; b++) {
                BlockPos p;
                if (stepX != 0) p = center.offset(0, a, b);
                else if (stepZ != 0) p = center.offset(a, b, 0);
                else p = center.offset(a, 0, b);
                if (p.getY() <= world.getMinY())
                    continue;
                BlockState bs = world.getBlockState(p);
                if (bs.isAir())
                    continue;
                if (!BlockEntitySonicBorer.canDrop(world, p))
                    continue;
                world.destroyBlock(p, true);
            }
        }
        AABB box = new AABB(center).inflate(3);
        for (LivingEntity e : world.getEntitiesOfClass(LivingEntity.class, box)) {
            e.hurt(world.damageSources().fellOutOfWorld(), 1);
        }
        world.playSound(null, center, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, 1F, 1F);
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        return false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        stepX = input.getIntOr("sx", 0);
        stepY = input.getIntOr("sy", 0);
        stepZ = input.getIntOr("sz", 0);
        targetX = input.getDoubleOr("tx", 0);
        targetY = input.getDoubleOr("ty", 0);
        targetZ = input.getDoubleOr("tz", 0);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.putInt("sx", stepX);
        output.putInt("sy", stepY);
        output.putInt("sz", stepZ);
        output.putDouble("tx", targetX);
        output.putDouble("ty", targetY);
        output.putDouble("tz", targetZ);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
        return super.getAddEntityPacket(serverEntity);
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        buffer.writeInt(stepX);
        buffer.writeInt(stepY);
        buffer.writeInt(stepZ);
        buffer.writeDouble(targetX);
        buffer.writeDouble(targetY);
        buffer.writeDouble(targetZ);
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf buffer) {
        stepX = buffer.readInt();
        stepY = buffer.readInt();
        stepZ = buffer.readInt();
        targetX = buffer.readDouble();
        targetY = buffer.readDouble();
        targetZ = buffer.readDouble();
    }
}
