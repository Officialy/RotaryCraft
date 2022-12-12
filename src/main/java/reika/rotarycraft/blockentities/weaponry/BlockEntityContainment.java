/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.weaponry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import reika.dragonapi.libraries.ReikaEntityHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.base.blockentity.BlockEntityProtectionDome;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;

import java.util.List;

public class BlockEntityContainment extends BlockEntityProtectionDome {

    public static final int DRAGONPOWER = 2097152;
    public static final int WITHERPOWER = 524288;

    public static final int FALLOFF = 8192;

    public BlockEntityContainment(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.CONTAINMENT.get(), pos, state);
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.CONTAINMENT;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return null;
    }

    //    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        this.setColor(120, 0, 150);
        this.getPowerBelow();
        if (power < MINPOWER)
            return;
        this.spawnParticles(world, pos);
        List<LivingEntity> inbox = world.getEntitiesOfClass(LivingEntity.class, this.getRangedBox());
        for (LivingEntity e : inbox) {
            if (this.isGeneralCapturable(e)) {
                this.markNoDespawn(e);
                double dx = e.getY() - pos.getX() - 0.5;
                double dy = e.getY() - pos.getY() - 0.5;
                double dz = e.getZ() - pos.getZ() - 0.5;
                double dd = ReikaMathLibrary.py3d(dx, dy, dz);
                if (dd > this.getRange() - 0.5) {
                    double x = e.getDeltaMovement().x();
                    double y = e.getDeltaMovement().y();
                    double z = e.getDeltaMovement().z();
                    e.setDeltaMovement(x - (dx / dd / 2), y - (dy / dd / 2), z - (dz / dd / 2));
                    if (!world.isClientSide)
                        e.hurtMarked = true;
                }
            }
            if (e instanceof EnderDragon && power >= DRAGONPOWER) {
                double dx = e.getY() - pos.getX() - 0.5;
                double dy = e.getY() - pos.getY() - 0.5;
                double dz = e.getZ() - pos.getZ() - 0.5;
                double dd = ReikaMathLibrary.py3d(dx, dy, dz);
                if (dd > this.getRange() - 2) {
                    double x = e.getDeltaMovement().x();
                    double y = e.getDeltaMovement().y();
                    double z = e.getDeltaMovement().z();
                    e.setDeltaMovement(x - (dx / dd), y - (dy / dd), z - (dz / dd));

                }
            }
            if (e instanceof WitherBoss && power >= WITHERPOWER) {
                double dx = e.getY() - pos.getX() - 0.5;
                double dy = e.getY() - pos.getY() - 0.5;
                double dz = e.getZ() - pos.getZ() - 0.5;
                double dd = ReikaMathLibrary.py3d(dx, dy, dz);
                if (dd > this.getRange() - 2) {
                    double x = e.getDeltaMovement().x();
                    double y = e.getDeltaMovement().y();
                    double z = e.getDeltaMovement().z();
                    e.setDeltaMovement(x - (dx / dd), y - (dy / dd), z - (dz / dd));
                }
                int id = ((WitherBoss) e).getAlternativeTarget(0);
                Entity ent = world.getEntity(id);
                if (ent != null) {
                    double dx2 = ent.getX() - pos.getX() - 0.5;
                    double dy2 = ent.getY() - pos.getY() - 0.5;
                    double dz2 = ent.getZ() - pos.getZ() - 0.5;
                    double dd2 = ReikaMathLibrary.py3d(dx2, dy2, dz2);
                    if (dd2 > this.getRange())
                        ((WitherBoss) e).setAlternativeTarget(0, 0);
                }
            }
        }
    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    @Override
    protected String getTEName() {
        return "containment";
    }

    private boolean isGeneralCapturable(LivingEntity e) {
        if (e instanceof EnderDragon || e instanceof WitherBoss)
            return false;
        if (e.getClass().getSimpleName().equals("EntityClayMan"))
            return true;
        return ReikaEntityHelper.isHostile(e);
    }

    private void markNoDespawn(LivingEntity e) {
        boolean pot = !e.getActiveEffects().contains(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 3600)); //todo check if this works
        if (pot)
            e.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 3600));
        e.hurt(DamageSource.ON_FIRE, 0);
        if (pot)
            e.removeEffect(MobEffects.FIRE_RESISTANCE);
    }

    @Override
    public ParticleOptions getParticleType() {
        return ParticleTypes.PORTAL;
    }

    @Override
    public int getFallOff() {
        return FALLOFF;
    }

    @Override
    public int getRangeBoost() {
        return 0;
    }

    @Override
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    public boolean hasATank() {
        return false;
    }

}
