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

import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class EntityIceBlock extends Entity {

    public double xWidth;
    public double yWidth;
    public double zWidth;

    private LivingEntity target;

    public EntityIceBlock(final EntityType<? extends Entity> entityType, Level world) {
        super(EntityTypes.BAT, world);
    }

    public EntityIceBlock(Level world, LivingEntity t) {
        super(EntityTypes.BAT, world);
        if (t == null) {
            if (world instanceof ServerLevel sl) this.kill(sl);
            return;
        }
        target = t;
        xo = target.getX();
        yo = target.getY();
        zo = target.getZ();
        xWidth = target.getBbWidth() + 0.15;
        zWidth = target.getBbWidth() + 0.15;
        yWidth = target.getBbHeight() + 0.375;
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (target == null)
            return;
        if (!target.isAlive()) {
            if (this.level() instanceof ServerLevel sl) this.kill(sl);
            return;
        }
        xo = target.getX();
        yo = target.getY();
        zo = target.getZ();
        xWidth = target.getBbWidth() + 0.15;
        zWidth = target.getBbWidth() + 0.15;
        yWidth = target.getBbHeight() + 0.375;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    protected AABB makeBoundingBox(Vec3 pos) {
        return new AABB(pos.x, pos.y, pos.z, pos.x + xWidth, pos.y + yWidth, pos.z + zWidth);
    }

    public boolean canBeCollidedWith() {
        return isAlive();
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
        return super.getAddEntityPacket(serverEntity);
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        xWidth = input.getDoubleOr("xw", 0);
        yWidth = input.getDoubleOr("yw", 0);
        zWidth = input.getDoubleOr("zw", 0);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.putDouble("xw", xWidth);
        output.putDouble("yw", yWidth);
        output.putDouble("zw", zWidth);
    }
}
