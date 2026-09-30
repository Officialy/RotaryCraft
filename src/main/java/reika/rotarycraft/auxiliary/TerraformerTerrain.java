package reika.rotarycraft.auxiliary;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.FeaturePlacer;
import net.minecraft.world.level.material.Fluids;
import reika.dragonapi.instantiable.event.IceFreezeEvent;
import reika.dragonapi.libraries.ReikaPlayerAPI;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.recipemanagers.TerraformingRecipe;
import reika.rotarycraft.blockentities.level.BlockEntityTerraformer;

/** Soil, climate and real registered biome features; decoration cannot leave the selected area or load chunks. */
public final class TerraformerTerrain {
    private TerraformerTerrain() {}
    public static boolean mayEdit(ServerLevel level, BlockPos pos, ServerPlayer owner) {
        return !level.isOutsideBuildHeight(pos) && level.hasChunkAt(pos) && level.getWorldBorder().isWithinBounds(pos)
                && level.getBlockEntity(pos) == null && (owner == null
                || ReikaPlayerAPI.playerCanBreakAt(level, pos, level.getBlockState(pos), owner));
    }
    public static void apply(ServerLevel level, BlockPos cell, TerraformingRecipe recipe, Biome biome,
            boolean terrain, ServerPlayer owner, Set<BlockPos> selectedArea) {
        for (int x = cell.getX(); x < cell.getX() + 4; x++) for (int z = cell.getZ(); z < cell.getZ() + 4; z++) {
            int surface = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z) - 1;
            for (int y = level.getMinY(); y < level.getMaxY(); y++) {
                BlockPos pos = new BlockPos(x, y, z);
                if (!mayEdit(level, pos, owner)) continue;
                var state = level.getBlockState(pos);
                boolean cold = !biome.warmEnoughToRain(pos, level.getSeaLevel());
                // Legacy applyEnvironment=true affects freezing/melting even without the diamond.
                if (cold && state.getFluidState().isSource() && state.getFluidState().getType() == Fluids.WATER
                        && state.getBlock() == Blocks.WATER && IceFreezeEvent.fire_IgnoreVanilla(level, pos))
                    level.setBlockAndUpdate(pos, Blocks.ICE.defaultBlockState());
                else if (!cold && state.is(Blocks.ICE)) level.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
                else if (!cold && state.is(Blocks.SNOW)) level.removeBlock(pos, false);
                if (!terrain || y < 30) continue; // Original soil replacement starts at Y=30.
                if (state.is(recipe.sourceFiller())) level.setBlockAndUpdate(pos, recipe.targetFiller().defaultBlockState());
                if (y == surface && state.is(recipe.sourceTop())) level.setBlockAndUpdate(pos, recipe.targetTop().defaultBlockState());
            }
            if (terrain) {
                BlockPos top = new BlockPos(x, surface + 1, z);
                if (mayEdit(level, top, owner) && biome.shouldSnow(level, top))
                    level.setBlockAndUpdate(top, Blocks.SNOW.defaultBlockState());
            }
        }
        if (terrain) decorate(level, cell, biome, owner, selectedArea);
    }
    private static void decorate(ServerLevel level, BlockPos cell, Biome biome, ServerPlayer owner, Set<BlockPos> area) {
        WorldGenLevel bounded = boundedLevel(level, owner, area);
        var generator = level.getChunkSource().getGenerator();
        // Modern placed features replace BiomeDecorator and its individual tree/grass/flower generators.
        // Placement density is the biome's real datapack density; the bounded world admits only selected columns.
        for (var step : biome.getGenerationSettings().features()) for (var feature : step) {
            try {
                new FeaturePlacer(bounded, generator).placeWithBiomeCheck(feature.value(), level.getRandom(), new BlockPos(cell.getX(), 0, cell.getZ()));
            } catch (IllegalArgumentException exception) {
                RotaryCraft.LOGGER.warn("Terraformer feature {} could not place in loaded selected terrain: {}", feature, exception.getMessage());
            }
        }
    }
    /** Default interface methods execute against this view, so their reads/writes also pass through its guards. */
    public static WorldGenLevel boundedLevel(ServerLevel level, ServerPlayer owner, Set<BlockPos> selectedArea) {
        Set<BlockPos> area = Set.copyOf(selectedArea);
        return (WorldGenLevel)Proxy.newProxyInstance(WorldGenLevel.class.getClassLoader(), new Class<?>[] {WorldGenLevel.class}, (proxy, method, args) -> {
            String name = method.getName();
            if (args != null && args.length > 0 && args[0] instanceof BlockPos pos) {
                boolean selected = area.contains(BlockEntityTerraformer.cell(pos));
                boolean readable = selected && !level.isOutsideBuildHeight(pos) && level.hasChunkAt(pos);
                if (name.equals("getBlockState") && !readable) return Blocks.BEDROCK.defaultBlockState();
                if (name.equals("getFluidState") && !readable) return Fluids.EMPTY.defaultFluidState();
                if (name.equals("getBlockEntity") && !readable) return null;
                if (name.equals("isEmptyBlock") && !readable) return false;
                if (name.equals("ensureCanWrite")) return selected && mayEdit(level, pos, owner);
                if (name.equals("setBlock") || name.equals("setBlockAndUpdate") || name.equals("removeBlock") || name.equals("destroyBlock")) {
                    if (!selected || !mayEdit(level, pos, owner)) return false;
                }
                if (name.equals("scheduleTick") && !readable) return null;
            }
            if (name.equals("getHeight") && args != null && args.length == 3 && args[1] instanceof Integer x && args[2] instanceof Integer z) {
                if (!area.contains(BlockEntityTerraformer.cell(new BlockPos(x, 0, z))) || !level.hasChunkAt(new BlockPos(x, 0, z))) return level.getMinY();
            }
            if (name.equals("getChunk") && args != null && args.length >= 2 && args[0] instanceof Integer x && args[1] instanceof Integer z) {
                var chunk = level.getChunk(x, z, ChunkStatus.FULL, false);
                if (chunk == null) throw new IllegalArgumentException("Unloaded chunk " + x + "," + z);
                return chunk;
            }
            if (method.isDefault()) return InvocationHandler.invokeDefault(proxy, method, args == null ? new Object[0] : args);
            try { return method.invoke(level, args); }
            catch (InvocationTargetException exception) { throw exception.getCause(); }
        });
    }
}
