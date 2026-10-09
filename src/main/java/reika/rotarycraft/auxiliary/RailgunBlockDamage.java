package reika.rotarycraft.auxiliary;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import org.jspecify.annotations.Nullable;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.ReikaPlayerAPI;
import reika.rotarycraft.base.blockentity.BlockEntityAimedCannon;
import reika.rotarycraft.data.RoCBlockTagsProvider;
import reika.rotarycraft.registry.RotaryBlocks;

/** V33a terrain thresholds. Fatigue is saved independently of modern block-state variants. */
public final class RailgunBlockDamage extends SavedData {
    private record Damage(BlockPos pos, BlockState state, int hits) {
        private static final Codec<Damage> CODEC = RecordCodecBuilder.create(i -> i.group(
                BlockPos.CODEC.fieldOf("pos").forGetter(Damage::pos),
                BlockState.CODEC.fieldOf("state").forGetter(Damage::state),
                Codec.intRange(0, 4).fieldOf("hits").forGetter(Damage::hits)).apply(i, Damage::new));
    }
    public static final Codec<RailgunBlockDamage> CODEC = Damage.CODEC.listOf().xmap(RailgunBlockDamage::new,
            data -> List.copyOf(data.damage.values()));
    private static final SavedDataType<RailgunBlockDamage> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath("rotarycraft", "railgun_block_damage"), RailgunBlockDamage::new, CODEC);
    private final Map<BlockPos, Damage> damage = new HashMap<>();
    public RailgunBlockDamage() {}
    private RailgunBlockDamage(List<Damage> entries) { entries.forEach(entry -> damage.put(entry.pos, entry)); }
    public static RailgunBlockDamage get(ServerLevel level) { return level.getDataStorage().computeIfAbsent(TYPE); }
    public void forget(BlockPos pos) { if (damage.remove(pos) != null) setDirty(); }
    public void forgetChanged(ServerLevel level, BlockPos pos) {
        Damage prior = damage.get(pos);
        if (prior != null && !prior.state.equals(level.getBlockState(pos))) forget(pos);
    }
    private static ServerPlayer owner(ServerLevel level, @Nullable BlockEntityAimedCannon gun) {
        ServerPlayer player = gun == null ? null : gun.getServerPlacer();
        return player != null ? player : ReikaPlayerAPI.getFakePlayerByNameAndUUID(level, "[RotaryCraft]",
                UUID.nameUUIDFromBytes("[RotaryCraft]".getBytes(java.nio.charset.StandardCharsets.UTF_8)));
    }
    private static boolean available(ServerLevel level, BlockPos pos) {
        return !level.isOutsideBuildHeight(pos) && level.hasChunkAt(pos);
    }
    private static boolean breakable(ServerLevel level, BlockPos pos, BlockState state) {
        return !state.isAir() && !(state.getBlock() instanceof net.minecraft.world.level.block.LiquidBlock) && !state.is(RotaryBlocks.RAILGUN.get())
                && state.getDestroySpeed(level, pos) >= 0;
    }
    private static boolean permitted(ServerLevel level, BlockPos pos, BlockState state, ServerPlayer player) {
        return ReikaPlayerAPI.playerCanBreakAt(level, pos, state, player) && level.getBlockState(pos).equals(state);
    }
    /** Same-block six-neighbour flood, bounded by the original spherical radius and loaded chunks. */
    public void breakConnected(ServerLevel level, BlockPos origin, int radius, @Nullable BlockEntityAimedCannon gun) {
        if (!available(level, origin)) return;
        var block = level.getBlockState(origin).getBlock();
        var player = owner(level, gun);
        Set<BlockPos> visited = new HashSet<>();
        ArrayDeque<BlockPos> pending = new ArrayDeque<>(); pending.add(origin.immutable());
        while (!pending.isEmpty()) {
            BlockPos pos = pending.removeFirst();
            if (!visited.add(pos) || pos.distSqr(origin) > radius * radius || !available(level, pos)) continue;
            BlockState state = level.getBlockState(pos);
            if (!state.is(block) || !breakable(level, pos, state) || !permitted(level, pos, state, player)) continue;
            if (!level.destroyBlock(pos, true, player)) continue;
            forget(pos);
            for (Direction side : Direction.values()) pending.add(pos.relative(side));
        }
    }
    public void impact(ServerLevel level, BlockPos origin, int tier, @Nullable BlockEntityAimedCannon gun) {
        var player = owner(level, gun);
        for (int x = -3; x <= 3; x++) for (int y = -3; y <= 3; y++) for (int z = -3; z <= 3; z++) {
            if (Math.abs(x * y * z) >= 9) continue;
            BlockPos pos = origin.offset(x, y, z);
            if (!available(level, pos)) continue;
            BlockState state = level.getBlockState(pos);
            if (!breakable(level, pos, state)) { forget(pos); continue; }
            if (ReikaWorldHelper.softBlocks(level, pos)) { breakConnected(level, pos, 5, gun); continue; }
            boolean instant = tier >= 1 && state.is(RoCBlockTagsProvider.RAILGUN_PLANTS)
                    || tier >= 2 && state.is(RoCBlockTagsProvider.RAILGUN_FRAGILE)
                    || tier >= 3 && state.is(RoCBlockTagsProvider.RAILGUN_WOOD)
                    || tier >= 4 && state.is(RoCBlockTagsProvider.RAILGUN_SEDIMENT);
            if (instant) { breakConnected(level, pos, 5, gun); continue; }
            if (state.is(Blocks.GRASS_BLOCK) && tier >= 3) {
                if (!permitted(level, pos, state, player)) continue;
                if (tier >= 5) level.destroyBlock(pos, true, player);
                else level.setBlock(pos, Blocks.DIRT.defaultBlockState(), 3);
                forget(pos); continue;
            }
            boolean dirt = state.is(Blocks.DIRT) || state.is(Blocks.COARSE_DIRT) || state.is(Blocks.PODZOL);
            boolean stone = state.is(Blocks.STONE);
            boolean cobble = state.is(Blocks.COBBLESTONE) || state.is(Blocks.MOSSY_COBBLESTONE) || state.is(Blocks.COBBLESTONE_WALL);
            if (!(dirt && tier >= 3 || (stone || cobble) && tier >= 5)) {
                if (tier >= 14) breakConnected(level, pos, tier == 14 ? 3 : 6, gun);
                continue;
            }
            if (!permitted(level, pos, state, player)) continue;
            Damage prior = damage.get(pos);
            int hits = prior != null && prior.state.equals(state) ? prior.hits : 0;
            int threshold = stone ? 2 : 3, accelerator = dirt ? 6 : 8;
            if (hits >= threshold || tier > accelerator && hits > threshold - (tier - accelerator)) {
                if (stone && tier <= 12) level.setBlock(pos, Blocks.COBBLESTONE.defaultBlockState(), 3);
                else level.destroyBlock(pos, true, player);
                forget(pos);
            } else { damage.put(pos.immutable(), new Damage(pos.immutable(), state, hits + 1)); setDirty(); }
        }
    }
}
