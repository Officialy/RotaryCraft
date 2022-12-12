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

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class EntityIceBlock extends Entity {

    public double xWidth;
    public double yWidth;
    public double zWidth;

    private LivingEntity target;

    public EntityIceBlock(final EntityType<? extends Entity> entityType, Level world) {
        super(EntityType.BAT, world);
    }


    public EntityIceBlock(Level world, LivingEntity t) {
        super(EntityType.BAT, world);
        if (t == null) {
            this.kill();
            return;
        }
        target = t;
        xo = target.getX();
        yo = target.getY();
        zo = target.getZ();
        xWidth = target.getBbWidth() + 0.15;
        zWidth = target.getBbWidth() + 0.15;
        yWidth = target.getBbHeight() + 0.375;
        //rotationPitch = target.rotationPitch;
        //rotationYaw = -target.rotationYawHead;
    }

    @Override
    public void tick() {
        super.tick();
        if (target == null)
            return;
        if (!target.isAlive()) {
            this.kill();
            return;
        }
        xo = target.getX();
        yo = target.getY();
        zo = target.getZ();
        xWidth = target.getBbWidth() + 0.15;
        zWidth = target.getBbWidth() + 0.15;
        yWidth = target.getBbHeight() + 0.375;
        //height = (float) yWidth; todo fix width and height
        //width = (float) xWidth;
        //rotationPitch = target.rotationPitch;
        //rotationYaw = -target.rotationYawHead;
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void load(CompoundTag nbt) {
        xWidth = nbt.getDouble("xw");
        yWidth = nbt.getDouble("yw");
        zWidth = nbt.getDouble("zw");
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putDouble("xw", xWidth);
        nbt.putDouble("yw", yWidth);
        nbt.putDouble("zw", zWidth);
        return nbt;
    }

    @Override
    protected AABB makeBoundingBox() {
        return new AABB(getX(), getY(), getZ(), getX() + xWidth, getY() + yWidth, getZ() + zWidth);
    }

    @Override
    public boolean canBeCollidedWith() {
        return isAlive();
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return super.getAddEntityPacket();
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {

    }
}
