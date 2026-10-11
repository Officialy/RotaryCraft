package reika.rotarycraft;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import reika.dragonapi.auxiliary.ProgressiveRecursiveBreaker;
import reika.dragonapi.auxiliary.trackers.TickRegistry;
import reika.dragonapi.instantiable.data.immutable.BlockBox;
import reika.dragonapi.instantiable.data.immutable.BlockKey;
import reika.dragonapi.interfaces.registry.TreeType;
import reika.dragonapi.modregistry.ModOreList;
import reika.dragonapi.modregistry.ModWoodList;
import reika.dragonapi.modregistry.WoodSpecies;

final class DragonAPICatalogueTests {
    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> environment) {
        RotaryGameTests.register(event, environment, "dragonapi_catalogue_glow_states_and_items", 40, DragonAPICatalogueTests::glow);
        RotaryGameTests.register(event, environment, "dragonapi_catalogue_shared_state_items", 40, DragonAPICatalogueTests::items);
        RotaryGameTests.register(event, environment, "dragonapi_catalogue_species_breaker", 40, DragonAPICatalogueTests::breaker);
        RotaryGameTests.register(event, environment, "dragonapi_catalogue_ore_states_and_copies", 40, DragonAPICatalogueTests::ores);
    }

    private static void glow(GameTestHelper h) {
        var wood = ModWoodList.LIGHTED;
        h.assertTrue(wood.exists(), "the real included glow tree resolves after deferred registration");
        h.assertTrue(wood.getLogStates().size() == 3 && wood.getLeafStates().size() == 28,
                "all real axes, leaf distance, persistence and waterlogging states are associated");
        for (int i = 0; i < wood.getLogStates().size(); i++) {
            var state = wood.getLogStates().get(i);
            h.assertTrue(wood.getLogItemWithOffset(i).blockID == state && ModWoodList.getModWood(state) == wood,
                    "each offset and reverse state lookup retains its actual axis");
        }
        for (var state : wood.getLeafStates()) {
            h.assertTrue(ModWoodList.getModWoodFromLeaf(state) == wood && wood.isNaturalLeaf(state),
                    "the original never-decaying glow leaf is natural regardless of native persistent state");
        }
        h.assertTrue(wood.getAllLeaves().size() == 1 && wood.getAllLogItems().size() == 1
                && wood.getAllSaplings().size() == 1, "world conditions do not create extra obtainable inventory variants");
        h.assertTrue(ModWoodList.getModWoodFromSapling(wood.getCorrespondingSapling()) == wood,
                "the actual dropped sapling has the same species");
        h.assertTrue(ModWoodList.getModWood(wood.getAllLogItems().getFirst()) == wood
                && ModWoodList.getModWoodFromLeaf(wood.getAllLeaves().getFirst()) == wood, "item reverse lookups retain species");
        wood.getAllLeaves().getFirst().setCount(0); wood.getCorrespondingSapling().setCount(0);
        h.assertTrue(wood.getAllLeaves().getFirst().getCount() == 1 && wood.getCorrespondingSapling().getCount() == 1,
                "returned stacks cannot corrupt catalogue associations");
        var tool = new ItemStack(Items.DIAMOND_AXE);
        tool.enchant(h.getLevel().registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT)
                .getOrThrow(net.minecraft.world.item.enchantment.Enchantments.FORTUNE), 10);
        // The original max(1, 50 - fortune*5) clamp gives a guaranteed sapling at Fortune X.
        for (var state : wood.getLeafStates()) {
            var drops = Block.getDrops(state, h.getLevel(), h.absolutePos(new BlockPos(4, 2, 4)), null, null, tool);
            h.assertTrue(drops.size() == 1 && ItemStack.isSameItemSameComponents(drops.getFirst(), wood.getCorrespondingSapling())
                    && ModWoodList.getModWoodFromSapling(drops.getFirst()) == wood,
                    "each declared glow leaf state's loaded native loot drops the catalogue's actual sapling");
        }
        h.assertTrue(!wood.isNaturalLeaf(Blocks.OAK_LEAVES.defaultBlockState()), "foreign persistent leaves are not glow leaves");
        h.assertTrue(ModWoodList.getModWood((ItemStack)null) == null && ModWoodList.getModWood(ItemStack.EMPTY) == null
                && ModWoodList.getModWoodFromSapling(new ItemStack(Items.STICK)) == null, "empty and non-block items are unclassified");
        boolean rejected = false;
        try { wood.getLogItemWithOffset(3); } catch (IndexOutOfBoundsException expected) { rejected = true; }
        h.assertTrue(rejected, "invalid offsets cannot silently alias the default log");
        h.succeed();
    }

    private static void items(GameTestHelper h) {
        var up = Blocks.MUSHROOM_STEM.defaultBlockState().setValue(BlockStateProperties.UP, true);
        var down = up.setValue(BlockStateProperties.UP, false);
        // These existing registered states exercise a shared-block association without inventing
        // an optional mod or replacing the real glow tree's global catalogue entry.
        var species = new WoodSpecies(List.of(up, down),
                List.of(Blocks.OAK_LEAVES.defaultBlockState(), Blocks.AZALEA_LEAVES.defaultBlockState()),
                List.of(Blocks.OAK_SAPLING.defaultBlockState(), Blocks.BIRCH_SAPLING.defaultBlockState()), false);
        h.assertTrue(species.logItems().size() == 2 && WoodSpecies.stateOf(species.logItems().get(0)) == up
                && WoodSpecies.stateOf(species.logItems().get(1)) == down, "shared-block properties survive native item placement components");
        h.assertTrue(species.leafItems().size() == 2 && species.saplingItems().size() == 2,
                "multiple leaf and sapling registry associations remain obtainable");
        species.leafItems().getFirst().setCount(0); species.saplingItems().getFirst().setCount(99);
        h.assertTrue(species.leafItems().getFirst().getCount() == 1 && species.saplingItems().getFirst().getCount() == 1,
                "item snapshots cannot mutate subsequent association results");
        var authored = species.logItems().getFirst();
        authored.set(net.minecraft.core.component.DataComponents.CUSTOM_NAME, net.minecraft.network.chat.Component.literal("authored tree item"));
        var supplied = WoodSpecies.ofItems(authored, new ItemStack(Blocks.OAK_LEAVES), new ItemStack(Blocks.OAK_SAPLING), false);
        authored.setCount(0);
        h.assertTrue(supplied.logs().equals(List.of(up)) && supplied.leaves().size() == 28 && supplied.saplings().size() == 2,
                "supplied item identity properties select the species while native world conditions expand");
        h.assertTrue(supplied.logItems().getFirst().getCount() == 1
                && supplied.logItems().getFirst().has(net.minecraft.core.component.DataComponents.CUSTOM_NAME),
                "integration-provided item components are preserved and detached");
        h.succeed();
    }

    private static void breaker(GameTestHelper h) {
        var level = h.getLevel(); var origin = h.absolutePos(new BlockPos(4, 2, 4));
        var logs = List.of(Blocks.OAK_LOG.defaultBlockState().setValue(BlockStateProperties.AXIS, Direction.Axis.Y),
                Blocks.OAK_LOG.defaultBlockState().setValue(BlockStateProperties.AXIS, Direction.Axis.X));
        var leaves = Blocks.OAK_LEAVES.getStateDefinition().getPossibleStates().stream()
                .filter(s -> !s.getValue(BlockStateProperties.WATERLOGGED)).toList();
        var foreignLog = logs.getFirst().setValue(BlockStateProperties.AXIS, Direction.Axis.Z);
        var foreignLeaf = leaves.getFirst().setValue(BlockStateProperties.WATERLOGGED, true);
        var positions = new ArrayList<BlockPos>();
        var tree = new TreeType() {
            @Override public BlockKey getItem() { return new BlockKey(logs.getFirst()); }
            @Override public Block getLogID() { return Blocks.OAK_LOG; }
            @Override public Block getLeafID() { return Blocks.OAK_LEAVES; }
            @Override public Block getSaplingID() { return Blocks.OAK_SAPLING; }
            @Override public Block getPlankID() { return Blocks.OAK_PLANKS; }
            @Override public boolean canBePlacedSideways() { return true; }
            @Override public boolean exists() { return true; }
            @Override public BlockKey getBasicLeaf() { return new BlockKey(leaves.getFirst()); }
            @Override public List<BlockState> getLogStates() { return logs; }
            @Override public List<BlockState> getLeafStates() { return leaves; }
            @Override public BlockBox getTypicalMaximumSize() { return BlockBox.origin().expand(4, 8, 4); }
        };
        var breaker = ProgressiveRecursiveBreaker.instance.getTreeBreaker(level, origin, tree);
        try {
            var states = List.of(logs.getFirst(), logs.get(1), leaves.getFirst(), foreignLog, foreignLeaf);
            for (int i = 0; i < states.size(); i++) {
                var pos = origin.above(i); positions.add(pos); level.setBlock(pos, states.get(i), Block.UPDATE_CLIENTS);
            }
            breaker.drops = false; breaker.causeUpdates = false;
            ProgressiveRecursiveBreaker.instance.addBlockPos(level, breaker);
            for (int i = 0; i < 5; i++) ProgressiveRecursiveBreaker.instance.tick(TickRegistry.TickType.WORLD, level);
            for (int i = 0; i < 3; i++) h.assertTrue(level.getBlockState(positions.get(i)).isAir(), "declared species states are broken");
            h.assertTrue(level.getBlockState(positions.get(3)) == foreignLog && level.getBlockState(positions.get(4)) == foreignLeaf,
                    "undeclared variants of the very same blocks survive recursive species breaking");
            h.succeed();
        } finally {
            breaker.terminate();
            for (var pos : positions) level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    private static void ores(GameTestHelper h) {
        var ore = ModOreList.COPPER;
        h.assertTrue(ore.getOreStates().contains(Blocks.COPPER_ORE.defaultBlockState())
                && ore.getOreStates().contains(Blocks.DEEPSLATE_COPPER_ORE.defaultBlockState()), "both real tagged copper hosts are enumerated");
        for (var state : ore.getOreStates()) h.assertTrue(ModOreList.getModOreFromOre(state) == ore, "state lookups retain tag classification");
        h.assertTrue(ModOreList.getModOreFromOre((BlockState)null) == null && ModOreList.getModOreFromOre(Blocks.STONE.defaultBlockState()) == null,
                "missing and ordinary block states are unclassified");
        var first = ore.getFirstOreBlock(); first.setCount(0);
        for (var stack : ore.getAllOreBlocks()) stack.setCount(0);
        for (var stack : ModOreList.getAllRegisteredOreBlocks()) stack.setCount(0);
        var random = RandomSource.create(171);
        for (int i = 0; i < 12; i++) {
            var chosen = ore.getRandomOreBlock(random);
            h.assertTrue(chosen.getCount() == 1 && ModOreList.getModOreFromOre(chosen) == ore,
                    "injected native RNG selects a valid detached ore stack");
            chosen.setCount(0);
        }
        h.assertTrue(ore.getFirstOreBlock().getCount() == 1 && ore.getAllOreBlocks().stream().allMatch(s -> s.getCount() == 1),
                "none of the public collection or random APIs expose internal mutable stacks");
        h.succeed();
    }
}
