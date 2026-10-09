package reika.rotarycraft;

import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import reika.dragonapi.auxiliary.ProgressiveRecursiveBreaker;
import reika.dragonapi.auxiliary.ProgressiveRecursiveBreaker.BreakerCallback;
import reika.dragonapi.auxiliary.ProgressiveRecursiveBreaker.ProgressiveBreaker;
import reika.dragonapi.auxiliary.trackers.TickRegistry;
import reika.dragonapi.instantiable.data.immutable.BlockBox;
import reika.dragonapi.instantiable.data.immutable.BlockKey;
import reika.dragonapi.interfaces.registry.TreeType;
import reika.dragonapi.modregistry.ModWoodList;

final class DragonAPITreeBreakerTests {
    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> environment) {
        depthTest(event, environment, "ordinary", ModWoodList.CANOPY, 30);
        depthTest(event, environment, "sequoia", ModWoodList.SEQUOIA, 350);
        depthTest(event, environment, "twilight_oak", ModWoodList.TWILIGHTOAK, 200);
        depthTest(event, environment, "darkwood", ModWoodList.DARKWOOD, 32);
        depthTest(event, environment, "giant_pink", ModWoodList.GIANTPINKTREE, 180);
        RotaryGameTests.register(event, environment, "dragonapi_tree_glow_bounds_and_states", 40, DragonAPITreeBreakerTests::glowWood);
    }

    private static void depthTest(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> environment,
            String name, ModWoodList policy, int expected) {
        RotaryGameTests.register(event, environment, "dragonapi_tree_depth_" + name, 40, h -> depth(h, policy, expected));
    }

    private static void depth(GameTestHelper h, ModWoodList policy, int expected) {
        var level = h.getLevel();
        BlockPos column = h.absolutePos(new BlockPos(4, 3, 4));
        BlockPos origin = new BlockPos(column.getX(), level.getMinY() + 8, column.getZ());
        var positions = new ArrayList<BlockPos>();
        ProgressiveBreaker breaker = null;
        try {
            for (int i = 0; i <= expected; i++) {
                var pos = origin.above(i);
                var state = i % 5 == 4 ? Blocks.OAK_LEAVES.defaultBlockState()
                        .setValue(BlockStateProperties.PERSISTENT, i % 2 == 0)
                        .setValue(BlockStateProperties.DISTANCE, 1 + i % 7)
                        : Blocks.OAK_LOG.defaultBlockState().setValue(BlockStateProperties.AXIS, Direction.Axis.values()[i % 3]);
                level.setBlock(pos, state, Block.UPDATE_CLIENTS);
                positions.add(pos);
            }
            BlockPos otherLog = origin.above(4).east(), otherLeaf = origin.above(4).west();
            level.setBlock(otherLog, Blocks.BIRCH_LOG.defaultBlockState(), Block.UPDATE_CLIENTS);
            level.setBlock(otherLeaf, Blocks.BIRCH_LEAVES.defaultBlockState(), Block.UPDATE_CLIENTS);
            positions.add(otherLog); positions.add(otherLeaf);
            // Missing optional mods are not installed or impersonated: this functional descriptor
            // uses registered vanilla states with the real catalogue depth policy and a broad test box.
            breaker = ProgressiveRecursiveBreaker.instance.getTreeBreaker(level, origin, new PolicyTree(policy));
            int[] broken = {0}, finished = {0};
            breaker.call = counting(broken, finished);
            breaker.drops = false;
            breaker.causeUpdates = false;
            h.assertTrue(breaker.extraSpread, "tree breaking retains diagonal spread");
            ProgressiveRecursiveBreaker.instance.addBlockPos(level, breaker);
            for (int i = 0; i <= expected; i++) ProgressiveRecursiveBreaker.instance.tick(TickRegistry.TickType.WORLD, level);
            h.assertTrue(broken[0] == expected && finished[0] == 1, "the tree policy admits exactly its original number of waves");
            for (int i = 0; i < expected; i++) h.assertTrue(level.getBlockState(origin.above(i)).isAir(),
                    "all admitted log axes and leaf distance/persistence states are removed");
            h.assertTrue(!level.getBlockState(origin.above(expected)).isAir(), "the next matching block survives the depth limit");
            h.assertTrue(level.getBlockState(otherLog).is(Blocks.BIRCH_LOG) && level.getBlockState(otherLeaf).is(Blocks.BIRCH_LEAVES),
                    "other log and leaf identities remain outside the declared tree");
            breaker.terminate();
            h.assertTrue(finished[0] == 1, "terminating a completed tree breaker does not repeat its callback");
            h.succeed();
        } finally {
            if (breaker != null) breaker.terminate();
            for (var pos : positions) level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    private static BreakerCallback counting(int[] broken, int[] finished) {
        return new BreakerCallback() {
            @Override public boolean canBreak(ProgressiveBreaker b, Level level, BlockPos pos, Block block) { return true; }
            @Override public void onPreBreak(ProgressiveBreaker b, Level level, BlockPos pos, Block block) { }
            @Override public void onPostBreak(ProgressiveBreaker b, Level level, BlockPos pos, Block block) { broken[0]++; }
            @Override public void onFinish(ProgressiveBreaker b) { finished[0]++; }
        };
    }

    private static void glowWood(GameTestHelper h) {
        var wood = ModWoodList.LIGHTED;
        h.assertTrue(wood.exists(), "the accepted ChromatiCraft glow wood must resolve through its real registry identity");
        var level = h.getLevel();
        BlockPos origin = h.absolutePos(new BlockPos(4, 3, 4));
        var positions = new ArrayList<BlockPos>();
        ProgressiveBreaker breaker = null;
        try {
            int border = wood.getTypicalMaximumSize().maxX;
            for (int i = 0; i <= border + 1; i++) {
                var pos = origin.east(i);
                level.setBlock(pos, wood.getLogID().defaultBlockState().setValue(BlockStateProperties.AXIS,
                        i % 2 == 0 ? Direction.Axis.X : Direction.Axis.Z), Block.UPDATE_CLIENTS);
                positions.add(pos);
            }
            BlockPos diagonal = origin.offset(-1, 1, -1);
            var leafStates = wood.getLeafID().getStateDefinition().getPossibleStates();
            level.setBlock(diagonal, leafStates.getLast(), Block.UPDATE_CLIENTS);
            positions.add(diagonal);
            BlockPos other = origin.above();
            level.setBlock(other, Blocks.BIRCH_LOG.defaultBlockState(), Block.UPDATE_CLIENTS);
            positions.add(other);
            breaker = ProgressiveRecursiveBreaker.instance.getTreeBreaker(level, origin, wood);
            var box = wood.getTypicalMaximumSize().offset(origin);
            h.assertTrue(breaker.bounds.minX == box.minX && breaker.bounds.minY == box.minY && breaker.bounds.minZ == box.minZ
                    && breaker.bounds.maxX == box.maxX && breaker.bounds.maxY == box.maxY && breaker.bounds.maxZ == box.maxZ,
                    "the original tree bounds are offset in all three axes without enlargement");
            breaker.drops = false;
            breaker.causeUpdates = false;
            ProgressiveRecursiveBreaker.instance.addBlockPos(level, breaker);
            for (int i = 0; i <= wood.getRecursiveBreakDepth(); i++) ProgressiveRecursiveBreaker.instance.tick(TickRegistry.TickType.WORLD, level);
            for (int i = 0; i <= border; i++) h.assertTrue(level.getBlockState(origin.east(i)).isAir(), "in-bound glow log axes are admitted");
            h.assertTrue(level.getBlockState(diagonal).isAir(), "corner-connected glow leaves retain extra spread");
            h.assertTrue(level.getBlockState(origin.east(border + 1)).is(wood.getLogID()), "a matching log beyond the original box survives");
            h.assertTrue(level.getBlockState(other).is(Blocks.BIRCH_LOG), "unrelated wood inside the bounds survives");
            h.succeed();
        } finally {
            if (breaker != null) breaker.terminate();
            for (var pos : positions) level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    private record PolicyTree(ModWoodList policy) implements TreeType {
        @Override public BlockKey getItem() { return new BlockKey(Blocks.OAK_LOG); }
        @Override public Block getLogID() { return Blocks.OAK_LOG; }
        @Override public Block getLeafID() { return Blocks.OAK_LEAVES; }
        @Override public Block getSaplingID() { return Blocks.OAK_SAPLING; }
        @Override public Block getPlankID() { return Blocks.OAK_PLANKS; }
        @Override public boolean canBePlacedSideways() { return true; }
        @Override public boolean exists() { return true; }
        @Override public BlockKey getBasicLeaf() { return new BlockKey(Blocks.OAK_LEAVES); }
        @Override public BlockBox getTypicalMaximumSize() { return new BlockBox(-2, -2, -2, 2, 360, 2); }
        @Override public int getRecursiveBreakDepth() { return policy.getRecursiveBreakDepth(); }
    }
}
