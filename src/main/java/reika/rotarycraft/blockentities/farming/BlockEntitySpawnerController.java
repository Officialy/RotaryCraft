/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.farming;

import java.lang.reflect.Field;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

/**
 * Spawner Controller: sits directly on top of a vanilla mob spawner and, while powered, overrides
 * its spawn delay -- more shaft speed = faster spawns, down to instant at high speed; it can also
 * disable the spawner entirely (or via redstone on the spawner).
 *
 * <p>26.2 port notes: the legacy version drove an ASM-injected {@code SpawnerControl}/
 * ControllableSpawner hook plus DragonAPI's ReikaSpawnerHelper. Here the vanilla
 * {@link BaseSpawner}'s private {@code spawnDelay} is driven directly through cached reflection
 * (NeoForge runs on official mappings at runtime, so the field name is stable). Writing the delay
 * every tick also overrides the vanilla post-spawn random reset, exactly like the legacy per-tick
 * setDelay. The forced no-player spawnCycle and the nearby-entity spawn cap are not ported (the
 * spawner still requires a player in range, as vanilla).</p>
 */
public class BlockEntitySpawnerController extends BlockEntityPowerReceiver implements ConditionalOperation {

    /** 40 s default max spawner delay. */
    public static final int BASEDELAY = 800;

    public boolean disable;
    private int setDelay = BASEDELAY;
    private int timer;

    private static Field spawnDelayField;

    public BlockEntitySpawnerController(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.SPAWNERCONTROLLER.get(), pos, state);
    }

    public int getOperationTime() {
        int time = BASEDELAY - 40 * (int) ReikaMathLibrary.logbase(omega, 2);
        return Math.max(time, 0);
    }

    public void setDelay(int delay) {
        setDelay = delay;
        if (timer >= setDelay)
            timer = Math.max(0, setDelay - 1);
    }

    public int getDelay() {
        return Math.max(setDelay, this.getOperationTime());
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        if (!this.isValidLocation(world, pos)) {
            disable = false;
            timer = 0;
            omega = torque = 0;
            power = 0;
            return;
        }

        // The spawner block itself is the power input (shafts feed the spawner's position).
        this.getOffsetPower4Sided(0, -1, 0, true);

        if (world.isClientSide())
            return;

        if (power >= MINPOWER && setDelay > 0)
            this.applyToSpawner(world, pos);
    }

    private void applyToSpawner(Level world, BlockPos pos) {
        BaseSpawner spawner = this.getSpawner(world, pos);
        if (spawner == null)
            return;
        if (disable || world.hasNeighborSignal(pos.below())) {
            // Park the delay far in the future; re-parked every tick while disabled.
            this.setSpawnDelay(spawner, Integer.MAX_VALUE / 2);
            this.shutdownParticles(world, pos);
        }
        else {
            int delay = this.getDelay();
            timer++;
            if (timer >= delay)
                timer = 0;
            this.setSpawnDelay(spawner, Math.max(0, delay - timer));
        }
    }

    private BaseSpawner getSpawner(Level world, BlockPos pos) {
        return world.getBlockEntity(pos.below()) instanceof SpawnerBlockEntity sp ? sp.getSpawner() : null;
    }

    private void setSpawnDelay(BaseSpawner spawner, int delay) {
        try {
            if (spawnDelayField == null) {
                spawnDelayField = BaseSpawner.class.getDeclaredField("spawnDelay");
                spawnDelayField.setAccessible(true);
            }
            spawnDelayField.setInt(spawner, delay);
        }
        catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    private void shutdownParticles(Level world, BlockPos pos) {
        if (rand.nextInt(4) > 0)
            return;
        for (int i = 0; i < 4; i++) {
            double px = pos.getX() + rand.nextFloat();
            double py = pos.getY() - 1 + rand.nextFloat();
            double pz = pos.getZ() + rand.nextFloat();
            if (world instanceof net.minecraft.server.level.ServerLevel sl) {
                sl.sendParticles(DustParticleOptions.REDSTONE, pos.getX() - 0.25 + 1.5 * rand.nextFloat(), py, pos.getZ() - 0.25 + 1.5 * rand.nextFloat(), 1, 0, 0, 0, 0);
                sl.sendParticles(ParticleTypes.CRIT, px, py, pz, 1, -0.3 + 0.6 * rand.nextFloat(), 0.4 * rand.nextFloat(), -0.3 + 0.6 * rand.nextFloat(), 0.3);
            }
        }
    }

    public boolean isValidLocation(Level world, BlockPos pos) {
        return world.getBlockState(pos.below()).is(Blocks.SPAWNER);
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        NBT.putInt("setdelay", setDelay);
        NBT.putBoolean("disable", disable);
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        this.setDelay(NBT.getIntOr("setdelay", BASEDELAY));
        disable = NBT.getBooleanOr("disable", false);
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.SPAWNERCONTROLLER;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.SPAWNERCONTROLLER.get();
    }

    @Override
    protected String getTEName() {
        return "spawnercontroller";
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
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    public boolean areConditionsMet() {
        return level != null && this.isValidLocation(level, worldPosition);
    }

    @Override
    public String getOperationalStatus() {
        return this.areConditionsMet() ? "Operational" : "No Spawner Below";
    }

}
