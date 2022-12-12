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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;

import java.awt.*;

public class EntityDischarge extends Entity implements IEntityAdditionalSpawnData {

    public int charge;
    public double targetX;
    public double targetY;
    public double targetZ;

    public EntityDischarge(final EntityType<? extends Entity> entityType, Level world) {
        super(EntityType.AREA_EFFECT_CLOUD, world);
        targetX = targetY = targetZ = 0;
        charge = 0;
    }

    public EntityDischarge(Level world, BlockPos pos, int charge, double tx, double ty, double tz) {
        super(EntityType.AREA_EFFECT_CLOUD, world);
        this.setPos(pos.getX(), pos.getY(), pos.getZ());
        this.charge = charge;
        targetX = tx;
        targetY = ty;
        targetZ = tz;
    }

    public int getCurrent() {
        return charge * 20 / 1000; //1A = 1C/s, 1t = 1/20 s
    }

    public Color getColor() {
        int a = this.getCurrent();
        if (a > 120) {
            return new Color(127, 0, 255);
        } else if (a > 90) {
            return new Color(0, 192, 255);
        } else if (a > 70) {
            return new Color(255, 255, 255);
        } else if (a > 50) {
            return new Color(255, 255, 0);
        } else {
            return new Color(0, 0, 0);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (tickCount > 1)
            this.setRemoved(RemovalReason.KILLED);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag p_20052_) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag p_20139_) {

    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return super.getAddEntityPacket();
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeDouble(targetX);
        buffer.writeDouble(targetY);
        buffer.writeDouble(targetZ);

        buffer.writeInt(charge);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        targetX = additionalData.readDouble();
        targetY = additionalData.readDouble();
        targetZ = additionalData.readDouble();

        charge = additionalData.readInt();
    }
}
