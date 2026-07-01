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

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;

import reika.rotarycraft.registry.RotaryEntities;

import java.awt.*;

public class EntityDischarge extends Entity implements IEntityWithComplexSpawn {

    public int charge;
    public double targetX;
    public double targetY;
    public double targetZ;

    public EntityDischarge(final EntityType<? extends Entity> entityType, Level world) {
        super(entityType, world);
        targetX = targetY = targetZ = 0;
        charge = 0;
    }

    public EntityDischarge(Level world, double x, double y, double z, int charge, double tx, double ty, double tz) {
        super(RotaryEntities.DISCHARGE.get(), world);
        this.setPos(x, y, z);
        this.charge = charge;
        targetX = tx;
        targetY = ty;
        targetZ = tz;
    }

    public int getCurrent() {
        return charge * 20 / 1000;
    }

    public Color getColor() {
        int a = this.getCurrent();
        if (a > 120) return new Color(127, 0, 255);
        else if (a > 90) return new Color(0, 192, 255);
        else if (a > 70) return new Color(255, 255, 255);
        else if (a > 50) return new Color(255, 255, 0);
        else return new Color(0, 0, 0);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount > 1)
            this.setRemoved(RemovalReason.KILLED);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
        return super.getAddEntityPacket(serverEntity);
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        buffer.writeDouble(targetX);
        buffer.writeDouble(targetY);
        buffer.writeDouble(targetZ);
        buffer.writeInt(charge);
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf additionalData) {
        targetX = additionalData.readDouble();
        targetY = additionalData.readDouble();
        targetZ = additionalData.readDouble();
        charge = additionalData.readInt();
    }
}
