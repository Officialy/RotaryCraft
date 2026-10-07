/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.level;

import java.util.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.decoration.BlockAttachedEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.common.NeoForge;
import reika.dragonapi.libraries.ReikaPlayerAPI;
import reika.rotarycraft.api.event.PileDriverImpactEvent;
import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.base.blocks.entity.BlockMiningPipe;
import reika.rotarycraft.data.RoCBlockTagsProvider;
import reika.rotarycraft.items.ItemSpawner;
import reika.rotarycraft.registry.*;

/** V33a retracting hammer, depth-dependent lift power and 21-cell impacts. */
public final class BlockEntityPileDriver extends BlockEntityPowerReceiver {
    public static final int BASEPOWER = 16384, BASESPEED = 300, MINTIME = 1;
    public int step;
    private int step2;
    private boolean climbing, smashed, active;
    private final Map<BlockPos, HitCount> numHits = new HashMap<>();
    private static final class HitCount {
        final BlockState state;
        final int maximum;
        int hits;
        HitCount(BlockState state, int maximum) { this.state = state; this.maximum = maximum; }
    }
    public BlockEntityPileDriver(BlockPos pos, BlockState state) { super(RotaryBlockEntities.PILEDRIVER.get(), pos, state); }
    public void getIOSides(Direction facing) {
        read = facing.getAxis() == Direction.Axis.X ? Direction.EAST : Direction.NORTH; read2 = read.getOpposite();
    }
    public long getRequiredPower() { return (long)BASEPOWER * ((long)step + 1); }
    public int getCycleTime() { return power < getRequiredPower() ? BASESPEED : (int)Math.max(MINTIME, BASESPEED / Math.max(1, power / getRequiredPower())); }
    public int getHammerDepth() { return step2; }
    public boolean isClimbing() { return climbing; }
    public boolean hasSmashed() { return smashed; }
    @Override public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity(); getIOSides(getBlockState().getValue(BlockRotaryCraftMachine.FACING)); getPower(true);
        if (!(world instanceof ServerLevel server) || power < getRequiredPower() || torque < MINTORQUE || (long)pos.getY() - step - 1 < world.getMinY()) return;
        tickcount++;
        if (!drawPile(server, getCycleTime()) && step != 0) return;
        climbing = true; tickcount = 0;
        if (smash(pos.below(step + 1))) step++;
        // Source applies entity effects after advancing the depth, including on cleared impacts.
        applyEntityEffects(pos.below(step + 1)); SoundRegistry.PILEDRIVER.playSoundAtBlock(world, pos, 1, 1); changed();
    }
    private void changed() { setChanged(); if (level != null) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3); }
    private ServerPlayer owner(ServerLevel server) {
        ServerPlayer owner = getServerPlacer();
        return owner != null ? owner : ReikaPlayerAPI.getFakePlayerByNameAndUUID(server, "[RotaryCraft]", UUID.nameUUIDFromBytes("[RotaryCraft]".getBytes(java.nio.charset.StandardCharsets.UTF_8)));
    }
    private boolean available(BlockPos pos) { return level != null && !level.isOutsideBuildHeight(pos) && level.hasChunkAt(pos); }
    private boolean permitted(ServerLevel server, BlockPos pos, BlockState state) {
        return available(pos) && ReikaPlayerAPI.playerCanBreakAt(server, pos, state, owner(server)) && server.getBlockState(pos).equals(state);
    }
    private boolean removeBit(ServerLevel server, BlockPos pos) {
        if (!available(pos)) return false;
        BlockState state = server.getBlockState(pos);
        if (state.isAir()) return true;
        // A replacement block belongs to its placer, not to the retracting hammer.
        return state.is(RotaryBlocks.MININGPIPE.get()) && state.getValue(BlockMiningPipe.SHAPE) == BlockMiningPipe.Shape.PILE_DRIVER
                && permitted(server, pos, state) && server.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
    }
    public boolean drawPile(ServerLevel server, int speed) {
        if (climbing && tickcount > speed) {
            BlockPos lower = worldPosition.below(step2 + 2);
            if (available(lower) && server.getBlockState(lower).is(RotaryBlocks.MININGPIPE.get()) && !removeBit(server, lower)) return false;
            int nextDepth = Math.max(0, step2 - 1);
            if (nextDepth > 0 && !removeBit(server, worldPosition.below(nextDepth + 1))) return false;
            step2 = nextDepth;
            if (step2 == 0) { climbing = false; smashed = false; }
            tickcount = 0;
        }
        if (climbing && tickcount <= speed) {
            int nextDepth = step2 >= step ? Math.max(0, step2 - 1) : step2;
            if (nextDepth > 0 && !removeBit(server, worldPosition.below(nextDepth + 1))) return false;
            step2 = nextDepth;
            if (step2 == 0) { climbing = false; smashed = false; }
        }
        BlockPos next = worldPosition.below(step2 + 1);
        if (!available(next)) return false;
        if (!climbing) {
            BlockState state = server.getBlockState(next);
            if (!state.isAir() && !(state.getBlock() instanceof LiquidBlock) && !(state.is(RotaryBlocks.MININGPIPE.get()) && state.getValue(BlockMiningPipe.SHAPE) == BlockMiningPipe.Shape.PILE_DRIVER)) return false;
            if (!permitted(server, next, state)) return false;
            liquidEffects(server, next, state);
            BlockState bit = RotaryBlocks.MININGPIPE.get().defaultBlockState().setValue(BlockMiningPipe.SHAPE, BlockMiningPipe.Shape.PILE_DRIVER);
            if (!server.setBlock(next, bit, 3) && !server.getBlockState(next).equals(bit)) return false;
            step2++;
        }
        BlockPos below = worldPosition.below(step2 + 1);
        if (available(below) && server.getBlockState(below).isAir()) {
            while (step == step2 && available(worldPosition.below(step2 + 2)) && worldPosition.getY() - step2 - 2 > server.getMinY()
                    && server.getBlockState(worldPosition.below(step2 + 2)).isAir()) { step++; step2 = step; }
        }
        changed(); return step2 == step;
    }
    private void liquidEffects(ServerLevel server, BlockPos target, BlockState state) {
        boolean water = state.getFluidState().is(FluidTags.WATER), lava = state.getFluidState().is(FluidTags.LAVA);
        if (!water && !lava) return;
        var particle = water ? ParticleTypes.SPLASH : ParticleTypes.LAVA; var random = server.getRandom();
        for (int pass = 0; pass < 2; pass++) for (int x = 0; x <= 2; x++) for (int z = 0; z <= 2; z++) {
            if (x == 1 && z == 1) continue;
            double dx = (x - 1) * .2, dz = (z - 1) * .2;
            if (pass == 1) {
                if (dx != 0) dx -= Math.signum(dx) * .4 * random.nextFloat();
                // V33a's x-center/z-positive splash deliberately adds rather than subtracts.
                if (dz != 0) dz += x == 1 && z == 2 ? .4 * random.nextFloat() : -Math.signum(dz) * .4 * random.nextFloat();
            }
            server.sendParticles(particle, target.getX() + x * .5, target.getY() + 2, target.getZ() + z * .5, 0, dx, .4, dz, 1);
        }
        for (int n = 0; n < 8; n++) server.sendParticles(particle, target.getX() + random.nextFloat(), target.getY() + 2,
                target.getZ() + random.nextFloat(), 0, random.nextFloat(), random.nextFloat(), random.nextFloat(), 1);
        server.playSound(null, target.above(), water ? SoundEvents.GENERIC_SPLASH : SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 1, 1);
    }
    public boolean smash(BlockPos center) {
        if (!(level instanceof ServerLevel server) || !available(center)) return false;
        NeoForge.EVENT_BUS.post(new PileDriverImpactEvent(this, center)); smashed = true;
        for (int x = -2; x <= 2; x++) for (int z = -2; z <= 2; z++) {
            if (Math.abs(x * z) == 4) continue;
            BlockPos target = center.offset(x, 0, z);
            if (!available(target)) continue;
            BlockState state = server.getBlockState(target);
            if (state.isAir() || !permitted(server, target, state)) continue;
            for (int depth = 1; depth <= 4; depth++) {
                BlockPos under = target.below(depth);
                if (!available(under)) continue;
                int hits = PileDriverRules.hits(server.getBlockState(under)); if (hits < 0 && -(long)hits >= depth) strike(under);
            }
            if (server.getBlockState(target).equals(state)) strike(target);
        }
        SoundRegistry.PILEDRIVER.playSoundAtBlock(server, center, 1, 1);
        boolean cleared = true;
        for (int x = -2; x <= 2; x++) for (int z = -2; z <= 2; z++) {
            if (Math.abs(x * z) == 4) continue;
            BlockPos pos = center.offset(x, 0, z);
            if (!available(pos)) { cleared = false; continue; }
            BlockState state = server.getBlockState(pos);
            if (!state.isAir() && !(state.getBlock() instanceof LiquidBlock
                    && (state.getFluidState().is(FluidTags.WATER) || state.getFluidState().is(FluidTags.LAVA)))) cleared = false;
        }
        shockwave(center); changed(); return cleared;
    }
    public boolean strike(BlockPos target) {
        if (!(level instanceof ServerLevel server) || !available(target)) return false;
        BlockState state = server.getBlockState(target);
        if (state.isAir() || !permitted(server, target, state)) return false;
        HitCount count = numHits.get(target);
        if (count != null && !count.state.equals(state)) { numHits.remove(target); count = null; }
        if (count == null && PileDriverRules.hits(state) > 0) {
            // Original starts at zero: a rule of five means six impacts (obsidian).
            numHits.put(target.immutable(), new HitCount(state, PileDriverRules.hits(state))); return false;
        }
        if (count != null && ++count.hits < count.maximum) return false;
        numHits.remove(target); BlockState result = PileDriverRules.product(state);
        if (result.equals(state)) return false;
        var drops = result.isAir() ? Block.getDrops(state, server, target, server.getBlockEntity(target), owner(server), ItemStack.EMPTY) : List.<ItemStack>of();
        ItemStack spawner = server.getBlockEntity(target) instanceof SpawnerBlockEntity tile ? ItemSpawner.fromSpawner(tile) : ItemStack.EMPTY;
        if (!server.getBlockState(target).equals(state) || !server.setBlock(target, result, 3)) return false;
        for (ItemStack drop : drops) Block.popResource(server, target, drop);
        if (result.isAir() && !spawner.isEmpty()) Block.popResource(server, target, spawner);
        server.sendBlockUpdated(target, state, result, 3); return true;
    }
    public void shockwave(BlockPos center) {
        if (!(level instanceof ServerLevel server)) return;
        for (BlockPos target : BlockPos.betweenClosed(center.offset(-5, -5, -5), center.offset(5, 5, 5))) {
            if (!available(target)) continue;
            BlockState state = server.getBlockState(target);
            if (state.isAir()) continue;
            if (state.is(RoCBlockTagsProvider.PILE_DRIVER_FRAGILE)) breakFragile(target.immutable(), Blocks.AIR.defaultBlockState(), null);
            else if (state.is(Blocks.ICE)) breakFragile(target.immutable(), Blocks.WATER.defaultBlockState(), new ItemStack(Items.ICE));
            else if (state.is(Blocks.COBWEB)) breakFragile(target.immutable(), Blocks.AIR.defaultBlockState(), new ItemStack(Items.STRING));
            else if (state.getBlock() instanceof FallingBlock && FallingBlock.isFree(server.getBlockState(target.below()))) makeFall(server, target.immutable(), state);
        }
        for (BlockAttachedEntity hanging : server.getEntitiesOfClass(BlockAttachedEntity.class, new AABB(center).inflate(5)))
            if (permitted(server, hanging.getPos(), server.getBlockState(hanging.getPos()))) hanging.hurtServer(server, hanging.damageSources().fellOutOfWorld(), 100);
    }
    private void breakFragile(BlockPos target, BlockState replacement, ItemStack specialDrop) {
        ServerLevel server = (ServerLevel)level; BlockState state = server.getBlockState(target);
        if (!permitted(server, target, state)) return;
        var drops = specialDrop != null ? List.of(specialDrop) : Block.getDrops(state, server, target, server.getBlockEntity(target), owner(server), ItemStack.EMPTY);
        if (!server.getBlockState(target).equals(state) || !server.setBlock(target, replacement, 3)) return;
        for (ItemStack drop : drops) Block.popResource(server, target, drop);
    }
    private void makeFall(ServerLevel server, BlockPos pos, BlockState state) {
        if (!permitted(server, pos, state)) return;
        if (server.hasChunksAt(pos.offset(-32, -32, -32), pos.offset(32, 32, 32))) FallingBlockEntity.fall(server, pos, state);
        else {
            BlockPos landing = pos;
            while (landing.getY() > server.getMinY() && available(landing.below()) && FallingBlock.isFree(server.getBlockState(landing.below()))) landing = landing.below();
            if (!available(landing) || !permitted(server, landing, server.getBlockState(landing))) return;
            if (server.setBlock(pos, Blocks.AIR.defaultBlockState(), 3) && landing.getY() > server.getMinY()) server.setBlock(landing, state, 3);
        }
    }
    public void applyEntityEffects(BlockPos center) {
        if (!(level instanceof ServerLevel server)) return;
        AABB bounce = new AABB(center.getX() - 2, center.getY(), center.getZ() - 2, center.getX() + 3, center.getY() + 1, center.getZ() + 3).inflate(24);
        for (Entity entity : server.getEntitiesOfClass(Entity.class, bounce)) {
            if (entity.onGround()) {
                double distance = entity.position().distanceTo(Vec3.atLowerCornerOf(center));
                entity.setDeltaMovement(entity.getDeltaMovement().add(0, .5 / Math.sqrt(Math.max(distance, .0001)), 0));
            }
            entity.syncVelocity = true;
        }
        for (LivingEntity entity : server.getEntitiesOfClass(LivingEntity.class, new AABB(center).inflate(.5, 2, .5))) {
            float damage = entity.getMaxHealth() * entity.getArmorValue(); entity.hurtServer(server, entity.damageSources().inWall(), damage <= 0 ? Float.MAX_VALUE : damage);
        }
        for (var player : server.getEntitiesOfClass(net.minecraft.world.entity.player.Player.class, new AABB(center).inflate(15)))
            if (!player.isCreative()) player.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 150, 10));
    }
    @Override protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag); tag.putInt("step", step); tag.putInt("step2", step2); tag.putBoolean("active", active); tag.putBoolean("climbing", climbing); tag.putBoolean("smashed", smashed);
    }
    @Override protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag); step = Math.max(0, tag.getIntOr("step", 0)); step2 = Math.max(0, tag.getIntOr("step2", 0));
        active = tag.getBooleanOr("active", false); climbing = tag.getBooleanOr("climbing", false); smashed = tag.getBooleanOr("smashed", false);
    }
    @Override protected void animateWithTick(Level world, BlockPos pos) {
        if (!isInWorld()) phi = 0; else if (power >= getRequiredPower() && torque >= MINTORQUE) phi += Math.pow(Math.log(omega + 1D) / Math.log(2), 1.05);
    }
    @Override public MachineRegistry getMachine() { return MachineRegistry.PILEDRIVER; }
    @Override protected String getTEName() { return "piledriver"; }
    @Override public Block getBlockEntityBlockID() { return RotaryBlocks.PILEDRIVER.get(); }
    @Override public boolean hasAnInventory() { return false; }
    @Override public boolean hasATank() { return false; }
    @Override public boolean hasModelTransparency() { return false; }
    @Override public int getRedstoneOverride() { return 0; }
}
