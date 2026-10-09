package reika.rotarycraft;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import reika.dragonapi.instantiable.data.blockstruct.FilledBlockArray;
import reika.dragonapi.instantiable.data.blockstruct.SlicedBlockBlueprint;
import reika.dragonapi.instantiable.data.immutable.BlockKey;
import reika.dragonapi.interfaces.BlockCheck;

final class DragonAPIBlockArrayTests {
    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> environment) {
        RotaryGameTests.register(event, environment, "dragonapi_arrays_rotation", 40, DragonAPIBlockArrayTests::rotation);
        RotaryGameTests.register(event, environment, "dragonapi_arrays_tally", 40, DragonAPIBlockArrayTests::tally);
        RotaryGameTests.register(event, environment, "dragonapi_arrays_blueprint", 40, DragonAPIBlockArrayTests::blueprint);
        RotaryGameTests.register(event, environment, "dragonapi_arrays_entities", 40, DragonAPIBlockArrayTests::entities);
        RotaryGameTests.register(event, environment, "dragonapi_arrays_fluid", 40, DragonAPIBlockArrayTests::fluid);
    }

    private static void rotation(GameTestHelper h) {
        var level = h.getLevel(); var origin = h.absolutePos(new BlockPos(4, 2, 4));
        var source = new FilledBlockArray(level);
        source.setBlock(origin.getX() + 1, origin.getY(), origin.getZ(), Blocks.STONE);
        source.setPlacementOverride(origin.getX() + 1, origin.getY(), origin.getZ(), Blocks.DIRT.defaultBlockState());
        var result = source.rotate90Degrees(origin.getX(), origin.getZ(), true);
        BlockPos moved = origin.north(); result.place();
        h.assertTrue(level.getBlockState(moved).is(Blocks.DIRT) && result.hasBlockAt(moved.getX(), moved.getY(), moved.getZ(), Blocks.STONE.defaultBlockState()),
                "rotation moves placement overrides and matching constraints together");
        var target = new FilledBlockArray(level); target.addAll(result); target.place();
        h.assertTrue(level.getBlockState(moved).is(Blocks.DIRT), "merge retains the source placement override");
        result.remove(moved); result.place();
        h.assertTrue(result.isEmpty() && source.getSize() == 1, "transformed membership is independent");
        h.succeed();
    }

    private static void tally(GameTestHelper h) {
        var source = new FilledBlockArray(null);
        source.setBlock(0, 0, 0, Blocks.OAK_STAIRS.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
        source.setBlock(1, 0, 0, Blocks.OAK_STAIRS.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST));
        source.addBlock(1, 0, 0, Blocks.DIRT);
        source.setEmpty(2, 0, 0, false, false);
        source.setFluid(3, 0, 0, Fluids.WATER);
        var first = new ItemStack(Items.PAPER, 9); first.set(DataComponents.CUSTOM_NAME, Component.literal("First"));
        var second = new ItemStack(Items.PAPER, 7); second.set(DataComponents.CUSTOM_NAME, Component.literal("Second"));
        source.setBlock(4, 0, 0, new DisplayCheck(first)); source.setBlock(5, 0, 0, new DisplayCheck(first));
        source.setBlock(6, 0, 0, new DisplayCheck(second));
        var counts = source.tally();
        h.assertTrue(counts.get(new ItemStack(Items.OAK_STAIRS)) == 2 && counts.get(new ItemStack(Items.WATER_BUCKET)) == 1,
                "material requirements group stair orientation and count a fluid container");
        h.assertTrue(counts.get(first) == 2 && counts.get(second) == 1 && first.getCount() == 9 && second.getCount() == 7,
                "component variants stay distinct; counting never changes shared display stacks");
        h.assertTrue(counts.size() == 4 && source.tally(position -> position.getX() == 1).get(new ItemStack(Items.OAK_STAIRS)) == 1,
                "empty cells are skipped, alternatives count once, and the validity filter is respected");
        h.succeed();
    }

    private static void blueprint(GameTestHelper h) {
        var level = h.getLevel(); var origin = h.absolutePos(new BlockPos(3, 4, 3));
        var blueprint = new SlicedBlockBlueprint(); blueprint.addMapping('s', Blocks.STONE); blueprint.addAntiMapping('d', Blocks.DIRT);
        blueprint.addSlice("s-x", "xds");
        // Checker keeps the original convention: columns across the plane, rows downward.
        level.setBlock(origin, Blocks.STONE.defaultBlockState(), 3);
        level.setBlock(origin.east(), Blocks.AIR.defaultBlockState(), 3);
        level.setBlock(origin.offset(2, 0, 0), Blocks.GOLD_BLOCK.defaultBlockState(), 3);
        level.setBlock(origin.offset(1, -1, 0), Blocks.GOLD_BLOCK.defaultBlockState(), 3);
        level.setBlock(origin.offset(2, -1, 0), Blocks.STONE.defaultBlockState(), 3);
        h.assertTrue(blueprint.checkAgainst(level, origin.getX(), origin.getY(), origin.getZ(), 0, 0, Direction.NORTH, 0, null),
                "rectangular checker handles mapped, empty, ignored and anti-mapped cells without transposing");
        level.setBlock(origin.offset(1, -1, 0), Blocks.DIRT.defaultBlockState(), 3);
        var failure = new java.util.ArrayList<BlockPos>();
        h.assertTrue(!blueprint.checkAgainst(level, origin.getX(), origin.getY(), origin.getZ(), 0, 0, Direction.NORTH, 0,
                (world, x, y, z, expected) -> failure.add(new BlockPos(x, y, z))) && failure.equals(List.of(origin.offset(1, -1, 0))),
                "forbidden-block failures report the actual rectangular cell");
        var imported = new FilledBlockArray(level); imported.fillFrom(blueprint, origin.getX(), origin.getY(), origin.getZ(), Direction.EAST);
        BlockPos anti = origin.north().above();
        level.setBlock(anti, Blocks.GOLD_BLOCK.defaultBlockState(), 3); imported.place();
        h.assertTrue(level.getBlockState(anti).is(Blocks.GOLD_BLOCK), "anti-mapping placement retains permitted blocks");
        level.setBlock(anti, Blocks.DIRT.defaultBlockState(), 3); imported.place();
        h.assertTrue(level.getBlockState(anti).isAir() && imported.matchInWorld(), "anti-mapping import retains its constraint and repairs forbidden blocks");
        h.succeed();
    }

    private static void entities(GameTestHelper h) {
        var level = h.getLevel(); var position = h.absolutePos(new BlockPos(4, 2, 4));
        var state = Blocks.CHEST.defaultBlockState();
        var prototype = new ChestBlockEntity(BlockPos.ZERO, state); prototype.getPersistentData().putString("owner", "first");
        var layout = new FilledBlockArray(level);
        layout.setTile(position.getX(), position.getY(), position.getZ(), state, prototype, level.registryAccess(), "NeoForgeData");
        prototype.getPersistentData().putString("owner", "changed after capture");
        level.setBlock(position, state, 3);
        var chest = (ChestBlockEntity)level.getBlockEntity(position); chest.setItem(0, new ItemStack(Items.DIAMOND, 3));
        chest.getPersistentData().putString("owner", "wrong");
        h.assertTrue(!layout.matchInWorld() && layout.countErrors() == 1, "selected NBT is compared as a snapshot");
        layout.place(); chest = (ChestBlockEntity)level.getBlockEntity(position);
        h.assertTrue(layout.matchInWorld() && chest.getItem(0).is(Items.DIAMOND) && chest.getItem(0).getCount() == 3
                && chest.getPersistentData().getStringOr("owner", "").equals("first"),
                "placement merges selected NBT through native ValueInput without clearing unrelated inventory");
        h.assertTrue(layout.getBlockEntityAt(position.getX(), position.getY(), position.getZ()) == prototype,
                "the original prototype reference remains available");
        var classOnly = new FilledBlockArray.BasicBlockEntityCheck(state, ChestBlockEntity.class);
        var wrongClass = new FilledBlockArray.BasicBlockEntityCheck(state, FurnaceBlockEntity.class);
        h.assertTrue(classOnly.getBlockEntity() == null && classOnly.matchInWorld(level, position) && !wrongClass.matchInWorld(level, position),
                "class-only checks work without a prototype and reject the wrong entity class");
        BlockPos moved = position.offset(2, 0, 0); var shifted = layout.offset(new BlockPos(2, 0, 0)); shifted.place();
        var movedChest = (ChestBlockEntity)level.getBlockEntity(moved);
        h.assertTrue(shifted.matchInWorld() && movedChest.getBlockPos().equals(moved), "selected NBT does not relocate the destination block entity");
        h.succeed();
    }

    private static void fluid(GameTestHelper h) {
        var level = h.getLevel(); var origin = h.absolutePos(new BlockPos(3, 3, 3));
        var flow = ((FlowingFluid)Fluids.WATER).getFlowing(7, false).createLegacyBlock();
        var source = new FilledBlockArray(level); source.setFluid(origin.getX(), origin.getY(), origin.getZ(), Fluids.FLOWING_WATER);
        h.assertTrue(source.hasBlockAt(origin.getX(), origin.getY(), origin.getZ(), Blocks.WATER.defaultBlockState())
                && !source.hasBlockAt(origin.getX(), origin.getY(), origin.getZ(), flow), "source and flowing IDs share a fluid family but source policy is retained");
        var onlyFlow = new FilledBlockArray(level); onlyFlow.setFluid(origin.getX(), origin.getY(), origin.getZ(), Fluids.WATER, true, false);
        h.assertTrue(onlyFlow.hasBlockAt(origin.getX(), origin.getY(), origin.getZ(), flow)
                && !onlyFlow.hasBlockAt(origin.getX(), origin.getY(), origin.getZ(), Blocks.WATER.defaultBlockState()), "disallowing sources retains the original flowing-only rule");
        source.place(); h.assertTrue(source.matchInWorld(), "native source placement satisfies its constraint");
        onlyFlow.place(); h.assertTrue(onlyFlow.matchInWorld(), "native flowing placement satisfies its constraint");
        var any = new FilledBlockArray(level); any.setFluid(origin.getX(), origin.getY(), origin.getZ(), Fluids.WATER, false, true);
        var waterlogged = Blocks.OAK_FENCE.defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, true);
        h.assertTrue(any.hasBlockAt(origin.getX(), origin.getY(), origin.getZ(), flow)
                && !any.hasBlockAt(origin.getX(), origin.getY(), origin.getZ(), waterlogged), "waterlogged solids cannot substitute for standalone fluid cells");
        var empty = new FilledBlockArray(level); BlockPos plant = origin.east(2);
        empty.setEmpty(plant.getX(), plant.getY(), plant.getZ(), true, false, Blocks.STONE);
        level.setBlock(plant, Blocks.WATER.defaultBlockState(), 3);
        h.assertTrue(empty.matchInWorld() && any.isSpaceEmpty(level, true) && !any.isSpaceEmpty(level, false), "soft allowances work for matching and space checks");
        level.setBlock(plant, Blocks.STONE.defaultBlockState(), 3);
        h.assertTrue(!empty.matchInWorld(), "explicit exclusions override soft allowances");
        h.succeed();
    }

    private record DisplayCheck(ItemStack display) implements BlockCheck {
        public boolean match(BlockState state) { return state.is(Blocks.STONE); }
        public boolean matchInWorld(Level world, BlockPos position) { return this.match(world.getBlockState(position)); }
        public boolean match(BlockCheck check) { return check instanceof DisplayCheck other && ItemStack.isSameItemSameComponents(display, other.display); }
        public void place(Level world, BlockPos position, int flags) { world.setBlock(position, Blocks.STONE.defaultBlockState(), flags); }
        public ItemStack asItemStack() { return display; }
        public ItemStack getDisplay() { return display; }
        public BlockKey asBlockKey() { return new BlockKey(Blocks.STONE); }
    }
}
