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

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import reika.rotarycraft.auxiliary.interfaces.Wettable;
import reika.rotarycraft.base.blockentity.SprinklerBlock;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

/**
 * Sprinkler: sprays a pressure-scaled area, hydrating farmland, growth-ticking crops/plants/
 * saplings, extinguishing fires, and wetting {@link Wettable} machines -- effect rate is 2x at the
 * centre falling to 0.5x at the rim, exactly the legacy falloff.
 *
 * <p>26.2 port notes: the legacy FieldCache/FieldColumn machinery was a scan cache (with its own
 * NBT + sync traffic); this port runs the same per-column top-down scan directly each effect tick
 * -- same blocks hit, same per-effect odds, no cache state. The GrowthCraft apple special cases and
 * ModCropList registries are gone (crops are tag/BonemealableBlock-driven); the ReactorCraft
 * radiation-clearing interop is gated out (would invert the RotaryCraft<-ReactorCraft dependency;
 * REACTOR-PORT). Growth ticks use randomTick on a ServerLevel.</p>
 */
public class BlockEntitySprinkler extends SprinklerBlock {

    public BlockEntitySprinkler(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.SPRINKLER.get(), pos, state);
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        this.tick(world, pos);
    }

    @Override
    protected void performEffects(Level world, BlockPos pos) {
        if (!world.isClientSide()) {
            this.hydrate(world, pos);
            // REACTOR-PORT: legacy 1/2400 EntityRadiation clean() sweep when ReactorCraft is loaded.
        }
        else {
            this.spawnParticles(world, pos);
        }
    }

    private void hydrate(Level world, BlockPos pos) {
        int range = this.getRange();
        for (int i = -range; i <= range; i++) {
            for (int k = -range; k <= range; k++) {
                // Legacy falloff: 2x rate at centre, 0.5x at the rim (factor < 1 = higher rate).
                float f = 0.5F + 1.5F * (i * i + k * k) / (float) (range * range);
                this.tickColumn(world, pos.getX() + i, pos.getZ() + k, pos.getY(), f);
            }
        }
    }

    /** Top-down column scan (legacy FieldColumn.recalulateLevel + tick, fused). */
    private void tickColumn(Level world, int x, int z, int sprinklerY, float chanceFactor) {
        int dy = sprinklerY;
        while (dy > 0 && sprinklerY - dy < 12) {
            BlockPos p = new BlockPos(x, dy, z);
            BlockState state = world.getBlockState(p);
            Block b = state.getBlock();
            if (!state.isAir()) {
                boolean stop = false;
                if (b == Blocks.FIRE) {
                    if (chance(20, chanceFactor)) {
                        world.playSound(null, p, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS,
                                0.6F + 0.4F * rand.nextFloat(), 0.5F + 0.5F * rand.nextFloat());
                        world.setBlockAndUpdate(p, Blocks.AIR.defaultBlockState());
                    }
                    stop = true;
                }
                else if (b instanceof FarmlandBlock) {
                    if (chance(15, chanceFactor) && state.getValue(FarmlandBlock.MOISTURE) < 7)
                        world.setBlockAndUpdate(p, state.setValue(FarmlandBlock.MOISTURE, 7));
                    stop = true;
                }
                // 26.2 removed the SAPLINGS block tag; BonemealableBlock covers saplings/crops/grass.
                else if (state.is(BlockTags.CROPS) || b instanceof BonemealableBlock
                        || b == Blocks.SUGAR_CANE || b == Blocks.CACTUS) {
                    if (chance(80, chanceFactor) && world instanceof ServerLevel sl)
                        state.randomTick(sl, p, sl.getRandom());
                    stop = b == Blocks.SUGAR_CANE || b == Blocks.CACTUS;
                }
                else {
                    BlockEntity te = world.getBlockEntity(p);
                    if (te instanceof Wettable w)
                        w.wet();
                }
                if (stop || state.canOcclude())
                    return;
            }
            dy--;
        }
    }

    private static boolean chance(int base, float factor) {
        return rand.nextInt(Math.max(1, (int) (base * factor))) == 0;
    }

    private void spawnParticles(Level world, BlockPos pos) {
        double r = this.getRange() / 10D;
        double py = pos.getY() - 0.1875D + 0.5;
        int n = (rand.nextInt(2) == 0 ? 1 : 0) + rand.nextInt(2);
        for (int i = 0; i < n; i++) {
            double px = pos.getX() - 1 + 2 * rand.nextFloat();
            double pz = pos.getZ() - 1 + 2 * rand.nextFloat();
            world.addParticle(ParticleTypes.SPLASH, px + 0.5, py, pz + 0.5, 0, 0, 0);
        }
        for (double vel = 0; vel < r; vel += 0.1) {
            n = (rand.nextInt(2) == 0 ? 1 : 0) + rand.nextInt(5);
            for (int i = 0; i < n; i++) {
                double vx = vel * (-1 + rand.nextFloat() * 2) * 1.05;
                double vz = vel * (-1 + rand.nextFloat() * 2) * 1.05;
                double px = pos.getX() - 1 + 2 * rand.nextFloat();
                double pz = pos.getZ() - 1 + 2 * rand.nextFloat();
                world.addParticle(ParticleTypes.SPLASH, px + 0.5, py, pz + 0.5, vx, 0, vz);
            }
        }
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.SPRINKLER;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.SPRINKLER.get();
    }

    @Override
    protected String getTEName() {
        return "sprinkler";
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
        return 180;
    }

    @Override
    public int getWaterConsumption() {
        return 3;
    }

    @Override
    public Direction getPipeDirection() {
        return Direction.UP;
    }

    // Pure consumer: nothing drains OUT of a sprinkler (PipeConnector contract).
    @Override
    public net.neoforged.neoforge.fluids.FluidStack drainPipe(Direction from, int maxDrain, net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction action) {
        return net.neoforged.neoforge.fluids.FluidStack.EMPTY;
    }
}
