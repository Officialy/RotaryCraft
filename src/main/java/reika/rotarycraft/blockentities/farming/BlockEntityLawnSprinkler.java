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

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import reika.dragonapi.instantiable.data.WeightedRandom;
import reika.rotarycraft.auxiliary.interfaces.Wettable;
import reika.rotarycraft.base.blockentity.SprinklerBlock;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

/**
 * Lawn Sprinkler: the ground-mounted rotating sprinkler (pipe feed from BELOW). Pressure-scaled
 * range: growth-ticks crops/plants and hydrates farmland (weighted toward the centre), extinguishes
 * fires, washes {@link Wettable} machines above 3 kPa (un-jams the Woodcutter), and at pressure-
 * washer pressures (>0.8 MPa) blasts mobs in line of sight -- fluid-injection lethal above 2 MPa.
 * All legacy behaviour.
 *
 * <p>26.2 port notes: crop growth is tag/BonemealableBlock-driven via randomTick (the legacy
 * ReikaCropHelper meta-increment and ModCropList registries are gone); machine detection for
 * washing is any Wettable BE (legacy checked MachineRegistry first -- same set). The ReactorCraft
 * radiation-clearing interop stays gated out (wrong-direction dependency; REACTOR-PORT). The
 * rotating water-stream particles use the client phi exactly as legacy.</p>
 */
public class BlockEntityLawnSprinkler extends SprinklerBlock {

    /** Pipes cap at 2.4 MPa = 24 atm, so a modifier keeps the thresholds reachable (legacy). */
    private static final int PRESSURE_MODIFIER = 10;
    /** Typical pressure washers are 10-30 MPa. */
    public static final int PRESSURE_TO_HURT = 8000000 / PRESSURE_MODIFIER;
    /** High enough to cause fluid-injection injuries. */
    public static final int PRESSURE_TO_KILL = 20000000 / PRESSURE_MODIFIER;

    /** Legacy growth-tick radius distribution: strongly weighted toward the centre. */
    private static final int[] GROWTH_PATTERN = {8, 5, 3, 1, 1};
    private static final WeightedRandom<Integer> radiusRandom = new WeightedRandom<>();

    static {
        for (int i = 0; i < GROWTH_PATTERN.length - 1; i++) {
            radiusRandom.addEntry(i + 1, GROWTH_PATTERN[i]);
        }
        radiusRandom.addEntry(-1, GROWTH_PATTERN[GROWTH_PATTERN.length - 1]);
    }

    private int speed;

    public BlockEntityLawnSprinkler(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.LAWNSPRINKLER.get(), pos, state);
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
    }

    @Override
    protected void doAnimations() {
        if (level != null && level.isClientSide()) {
            if (this.canPerformEffects()) {
                if (speed < 24)
                    speed += 1;
            }
            else {
                if (speed > 0)
                    speed--;
            }
        }
        phi += speed;
    }

    @Override
    protected void performEffects(Level world, BlockPos pos) {
        if (!world.isClientSide()) {
            for (int k = 0; k < 3; k++) {
                this.accelerateGrowth(world, pos);
                this.extinguishFire(world, pos);
            }
            if (this.getPressure() > 3000)
                this.washMachines(world, pos);
            // REACTOR-PORT: legacy 1/2400 EntityRadiation clean() sweep when ReactorCraft is loaded.
            if (this.getPressure() > PRESSURE_TO_HURT)
                this.damageMobs(world, pos);
        }
        else {
            this.spreadWater(world, pos);
        }
    }

    /** Random column in range: hydrate farmland, growth-tick crops/plants (1-in-8), legacy scan. */
    private void accelerateGrowth(Level world, BlockPos pos) {
        int r = this.calcRange();
        int rx = pos.getX() + rand.nextInt(2 * r + 1) - r;
        int rz = pos.getZ() + rand.nextInt(2 * r + 1) - r;
        for (int i = pos.getY(); i > pos.getY() - 4; i--) {
            BlockPos p = new BlockPos(rx, i, rz);
            BlockState state = world.getBlockState(p);
            Block b = state.getBlock();
            if (b instanceof FarmlandBlock) {
                if (state.getValue(FarmlandBlock.MOISTURE) < 7)
                    world.setBlockAndUpdate(p, state.setValue(FarmlandBlock.MOISTURE, 7));
                return;
            }
            else if (!state.isAir() && state.canOcclude()) {
                return;
            }
            else if (!state.isAir() && rand.nextInt(8) == 0) {
                if ((state.is(BlockTags.CROPS) || b instanceof BonemealableBlock
                        || b == Blocks.SUGAR_CANE || b == Blocks.CACTUS) && world instanceof ServerLevel sl) {
                    state.randomTick(sl, p, sl.getRandom());
                }
            }
        }
    }

    /** Random column in range: put out fires (not on netherrack -- those relight instantly). */
    private void extinguishFire(Level world, BlockPos pos) {
        int r = this.getRange();
        if (r <= 0)
            return;
        int rx = pos.getX() + rand.nextInt(2 * r + 1) - r;
        int rz = pos.getZ() + rand.nextInt(2 * r + 1) - r;
        for (int i = pos.getY(); i > pos.getY() - 4; i--) {
            BlockPos p = new BlockPos(rx, i, rz);
            BlockState state = world.getBlockState(p);
            if (state.is(Blocks.FIRE)) {
                if (!world.getBlockState(p.below()).is(Blocks.NETHERRACK)) {
                    world.setBlockAndUpdate(p, Blocks.AIR.defaultBlockState());
                    world.playSound(null, p, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.6F, 1F);
                }
            }
            else if (!state.isAir() && state.canOcclude())
                return;
        }
    }

    /** High-pressure spray keeps nearby machines clean (wets Wettables like the Woodcutter). */
    private void washMachines(Level world, BlockPos pos) {
        int r = this.getRange();
        if (r <= 0)
            return;
        for (int c = 0; c < 3; c++) {
            int rx = pos.getX() + rand.nextInt(2 * r + 1) - r;
            int rz = pos.getZ() + rand.nextInt(2 * r + 1) - r;
            for (int i = pos.getY(); i > pos.getY() - 4; i--) {
                BlockPos p = new BlockPos(rx, i, rz);
                BlockEntity te = world.getBlockEntity(p);
                if (te instanceof Wettable w) {
                    w.wet();
                }
                else {
                    BlockState state = world.getBlockState(p);
                    if (!state.isAir() && state.canOcclude())
                        break;
                }
            }
        }
    }

    /** Pressure-washer damage: 0.5 drowning damage to mobs in clear line of sight (legacy). */
    private void damageMobs(Level world, BlockPos pos) {
        int r = this.getRange();
        AABB box = new AABB(pos).move(0, 1, 0).inflate(r, 1, r);
        List<LivingEntity> li = world.getEntitiesOfClass(LivingEntity.class, box);
        Vec3 from = new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        for (LivingEntity e : li) {
            Vec3 to = new Vec3(e.getX(), e.getY() + 0.5, e.getZ());
            HitResult hit = world.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, e));
            if (hit.getType() == HitResult.Type.MISS) {
                e.hurt(world.damageSources().drown(), 0.5F);
            }
        }
    }

    /** Legacy centre-weighted growth radius: mostly 1-4, occasionally full range. */
    private int calcRange() {
        Integer r = radiusRandom.getRandomEntry();
        int v = r == null ? 1 : r;
        if (v == -1)
            v = this.getMaxRange();
        return Math.max(1, Math.min(v, Math.max(1, this.getRange())));
    }

    /** Rotating triple water stream + splashes, driven by the client phi (legacy). */
    private void spreadWater(Level world, BlockPos pos) {
        double py = pos.getY() - 0.1875D + 0.5;
        for (int i = 0; i < rand.nextInt(3); i++) {
            double px = pos.getX() - 1 + 2 * rand.nextFloat();
            double pz = pos.getZ() - 1 + 2 * rand.nextFloat();
            world.addParticle(ParticleTypes.SPLASH, px + 0.5, py, pz + 0.5, 0, 0, 0);
        }
        for (int i = 0; i < 3; i++)
            this.createWaterStream(world, pos, i * 120 + 60);
    }

    private void createWaterStream(Level world, BlockPos pos, float offset) {
        int r = this.getRange();
        double dx = 0.6 * Math.sin(Math.toRadians(phi + offset));
        double dz = 0.6 * Math.cos(Math.toRadians(phi + offset));
        for (int i = 0; i < 12; i++) {
            double v = rand.nextInt((1 + r) * 10) / 72D;
            world.addParticle(ParticleTypes.SPLASH, pos.getX() + 0.5 + dx, pos.getY() + 0.75, pos.getZ() + 0.5 + dz,
                    dx * v - 0.025 + 0.05 * rand.nextDouble(), 0, dz * v - 0.025 + 0.05 * rand.nextDouble());
        }
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        this.tick(world, pos);
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.LAWNSPRINKLER;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.LAWNSPRINKLER.get();
    }

    @Override
    protected String getTEName() {
        return "lawnsprinkler";
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
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
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    public int getCapacity() {
        return 5;
    }

    @Override
    public int getWaterConsumption() {
        return 1;
    }

    @Override
    public Direction getPipeDirection() {
        return Direction.DOWN;
    }

    // Pure consumer: nothing drains OUT of a sprinkler (PipeConnector contract).
    @Override
    public net.neoforged.neoforge.fluids.FluidStack drainPipe(Direction from, int maxDrain, net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction action) {
        return net.neoforged.neoforge.fluids.FluidStack.EMPTY;
    }
}
