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

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.cubemob.Slime;
import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
import net.minecraft.world.entity.projectile.hurtingprojectile.SmallFireball;
import net.minecraft.world.entity.projectile.hurtingprojectile.WitherSkull;
import net.minecraft.world.entity.projectile.throwableitemprojectile.AbstractThrownPotion;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import reika.rotarycraft.auxiliary.MachineEnchantmentHandler;
import reika.rotarycraft.auxiliary.interfaces.EnchantableMachine;
import reika.rotarycraft.base.blockentity.BlockEntityProtectionDome;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

/**
 * The Force Field projects a spherical shield that neutralises incoming threats: it stops arrows,
 * detonates wither skulls / ghast fireballs / TNT at the boundary, dissipates rail-gun shots and
 * splash potions, and shoves hostile mobs (and angry wolves) away. Its radius scales with power and
 * is boosted by the Protection enchantment. Built on the same protection-dome base as the live
 * Containment field. 1.7.10-faithful; the MeteorCraft / mod-explosive / mod-missile interception
 * hooks are gated out (MOD-PORT: those mods and their APIs are not in this build).
 */
public class BlockEntityForceField extends BlockEntityProtectionDome implements EnchantableMachine {

    public static final int FALLOFF = 32768;

    private final MachineEnchantmentHandler enchantments = new MachineEnchantmentHandler().addFilter(Enchantments.PROTECTION);

    public BlockEntityForceField(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.FORCE_FIELD.get(), pos, state);
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        tickcount++;
        this.getPowerBelow();
        if (power < MINPOWER)
            return;
        this.setColor(80, 160, 255);
        this.spawnParticles(world, pos);
        AABB field = this.getRangedBox();
        for (Entity e : world.getEntitiesOfClass(Entity.class, field))
            this.protect(world, pos, e);
    }

    private void protect(Level world, BlockPos center, Entity threat) {
        // MOD-PORT: legacy also destroyed MeteorCraft meteors, mod explosives (ModExplosiveHandler)
        // and mod missiles (InterfaceCache.IMISSILE) here — those mods are not in this build.
        Vec3 c = Vec3.atCenterOf(center);
        double dx = threat.getX() - c.x;
        double dy = threat.getY() - c.y;
        double dz = threat.getZ() - c.z;
        double dd = Math.sqrt(dx * dx + dy * dy + dz * dz);
        double cx = center.getX() + 0.5, cy = center.getY() + 0.5, cz = center.getZ() + 0.5;

        // Arrows are stopped anywhere inside the field, not just at the border.
        if (threat instanceof AbstractArrow arrow) {
            world.playSound(null, center, SoundEvents.ARROW_HIT, SoundSource.BLOCKS, 1, 1);
            arrow.setXRot(-90);
            Vec3 m = arrow.getDeltaMovement();
            arrow.setDeltaMovement(0, Math.min(0, m.y), 0);
            arrow.hurtMarked = true;
            tickcount = 0;
            return;
        }

        boolean atBorder = dd >= this.getRange() - 1.5;
        if (atBorder) {
            if (threat instanceof WitherSkull) {
                threat.discard();
                if (!world.isClientSide())
                    world.explode(null, cx, cy, cz, 1F, Level.ExplosionInteraction.MOB);
                tickcount = 0;
                return;
            }
            if (threat instanceof LargeFireball) {
                threat.discard();
                if (!world.isClientSide())
                    world.explode(null, cx, cy, cz, 2F, Level.ExplosionInteraction.MOB);
                tickcount = 0;
                return;
            }
            if (threat instanceof SmallFireball) { // blaze fireball
                threat.discard();
                tickcount = 0;
                return;
            }
            // MOD-PORT: legacy also caught EntityRailGunShot here — the rail-gun entity is not
            // ported (its source file is still stale/commented), so nothing to intercept yet.
            if (threat instanceof AbstractThrownPotion) { // splash potion: neutralised at the boundary
                threat.discard();
                tickcount = 0;
                return;
            }
            if (threat instanceof PrimedTnt) {
                threat.discard();
                if (!world.isClientSide())
                    world.explode(null, cx, cy, cz, 4F, Level.ExplosionInteraction.TNT);
                tickcount = 0;
                return;
            }
        }

        // Hostile mobs (and angry wolves) are shoved outward.
        this.repel(threat, dx, dy, dz, dd);
    }

    private void repel(Entity threat, double dx, double dy, double dz, double dd) {
        if (dd < 1.0E-4)
            return;
        boolean hostile = threat instanceof Monster || threat instanceof Ghast || threat instanceof Slime
                || (threat instanceof Wolf w && w.isAngry());
        if (!hostile)
            return;
        double v = 0.1;
        double my = threat.onGround() ? v * dy / dd : threat.getDeltaMovement().y;
        threat.setDeltaMovement(v * dx / dd, my, v * dz / dd);
        threat.hurtMarked = true;
    }

    @Override
    public MachineEnchantmentHandler getEnchantmentHandler() {
        return enchantments;
    }

    @Override
    public int getRangeBoost() {
        return 8 * enchantments.getEnchantment(Enchantments.PROTECTION);
    }

    @Override
    public int getFallOff() {
        return FALLOFF;
    }

    @Override
    public ParticleOptions getParticleType() {
        return ParticleTypes.ELECTRIC_SPARK;
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.FORCEFIELD;
    }

    @Override
    protected String getTEName() {
        return "forcefield";
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.FORCE_FIELD.get();
    }

    @Override
    public int getRedstoneOverride() {
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

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        enchantments.load(NBT.getListOrEmpty("enchantments"));
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        NBT.put("enchantments", enchantments.saveAdditional());
    }
}
