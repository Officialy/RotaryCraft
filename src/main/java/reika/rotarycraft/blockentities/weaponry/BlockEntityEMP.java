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

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;

import reika.rotarycraft.api.interfaces.EMPControl;
import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

/**
 * The EMP is a single-use device that slowly charges from shaft power and, once it has stored an
 * enormous energy, discharges an electromagnetic pulse that shuts down every powered machine in a
 * wide radius (RotaryCraft machines via their {@code onEMP} hook, plus any {@link EMPControl} tile)
 * and violently disrupts nearby entities. 1.7.10-faithful core; the cross-mod interception (Forge
 * Energy / IC2 / ChromatiCraft nodes / BuildCraft robots / powered-armour stripping) and the fancy
 * client shockwave render are simplified/gated (MOD-PORT: those APIs are not in this build).
 */
public class BlockEntityEMP extends BlockEntityPowerReceiver implements RangedEffect {

    public static final long BLAST_ENERGY = (long) 4.184e9;
    public static final int MAX_RANGE = 64;

    private long energy;
    private boolean fired;

    public BlockEntityEMP(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.EMP.get(), pos, state);
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        tickcount++;
        this.getPowerBelow();
        if (fired)
            return;
        if (power >= MINPOWER)
            energy += power;
        if (energy / 20L >= BLAST_ENERGY)
            this.fire(world, pos);
    }

    private void fire(Level world, BlockPos pos) {
        fired = true;
        this.setChanged();
        if (world.isClientSide())
            return;

        int r = this.getRange();
        int cr = (r >> 4) + 1;
        int ccx = pos.getX() >> 4;
        int ccz = pos.getZ() >> 4;
        List<BlockEntity> hit = new ArrayList<>();
        for (int cx = ccx - cr; cx <= ccx + cr; cx++) {
            for (int cz = ccz - cr; cz <= ccz + cr; cz++) {
                if (!world.hasChunk(cx, cz))
                    continue;
                LevelChunk ch = world.getChunk(cx, cz);
                for (BlockEntity be : ch.getBlockEntities().values()) {
                    if (be != this && be.getBlockPos().distSqr(pos) <= (long) r * r)
                        hit.add(be);
                }
            }
        }
        for (BlockEntity be : hit)
            this.applyEMP(be);

        this.affectEntities(world, pos);

        // MOD-PORT: legacy fired a SPARKLOC/EMPEFFECT render packet + a full shockwave animation.
        if (world instanceof ServerLevel sl)
            sl.sendParticles(net.minecraft.core.particles.ParticleTypes.PORTAL,
                    pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 200, r / 2.0, 2, r / 2.0, 0.5);
    }

    private void applyEMP(BlockEntity be) {
        if (be instanceof RotaryCraftBlockEntity rc) {
            if (!rc.isShutdown())
                rc.onEMP();
        } else if (be instanceof EMPControl ec) {
            ec.onHitWithEMP(be);
        }
        // MOD-PORT: Forge-Energy tiles, IC2 power tiles, and ChromatiCraft nodes/pylons were also
        // drained/charged here; those capabilities/APIs are not part of this build.
    }

    private void affectEntities(Level world, BlockPos pos) {
        AABB box = new AABB(pos).inflate(128, 64, 128);
        for (Entity e : world.getEntitiesOfClass(LivingEntity.class, box)) {
            // MOD-PORT: legacy specifically stripped + detonated powered armour / BuildCraft robots.
            // Approximation: an EMP shockwave that hurls nearby living things off their feet.
            double dx = e.getX() - (pos.getX() + 0.5);
            double dz = e.getZ() - (pos.getZ() + 0.5);
            double dd = Math.max(0.5, Math.sqrt(dx * dx + dz * dz));
            double v = Math.max(0, 1.0 - dd / 128.0);
            e.setDeltaMovement(e.getDeltaMovement().add(dx / dd * v, 0.4 * v, dz / dd * v));
            e.hurtMarked = true;
        }
    }

    @Override
    public int getRange() {
        return MAX_RANGE;
    }

    @Override
    public int getMaxRange() {
        return MAX_RANGE;
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.EMP;
    }

    @Override
    protected String getTEName() {
        return "emp";
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.EMP.get();
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    public boolean hasATank() {
        return false;
    }

    @Override
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        energy = NBT.getLongOr("energy", 0L);
        fired = NBT.getBooleanOr("fired", false);
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        NBT.putLong("energy", energy);
        NBT.putBoolean("fired", fired);
    }
}
