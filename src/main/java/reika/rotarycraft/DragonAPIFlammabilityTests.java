package reika.rotarycraft;

import java.util.HashMap;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagLoader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import reika.dragonapi.extras.BlockProperties;
import reika.dragonapi.instantiable.TemperatureEffect;
import reika.dragonapi.libraries.level.LegacyFlammabilityTags;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.rotarycraft.blockentities.weaponry.Turret.BlockEntityLaserGun;
import reika.rotarycraft.registry.RotaryBlocks;

final class DragonAPIFlammabilityTests {
    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> environment) {
        RotaryGameTests.register(event, environment, "dragonapi_flammability_catalogue", 40, DragonAPIFlammabilityTests::catalogue);
        RotaryGameTests.register(event, environment, "dragonapi_flammability_actual_state", 40, DragonAPIFlammabilityTests::actualState);
        RotaryGameTests.register(event, environment, "dragonapi_flammability_tag_rebind", 40, DragonAPIFlammabilityTests::tagRebind);
        RotaryGameTests.register(event, environment, "dragonapi_flammability_temperature", 40, DragonAPIFlammabilityTests::temperature);
        RotaryGameTests.register(event, environment, "dragonapi_flammability_laser", 40, DragonAPIFlammabilityTests::laser);
    }

    private static void catalogue(GameTestHelper h) {
        var fire = (FireBlock)Blocks.FIRE;
        for (var block : BuiltInRegistries.BLOCK) {
            var state = block.defaultBlockState();
            h.assertTrue(BlockProperties.isFlammable(state) == (fire.getBurnOdds(state) > 0
                    || state.is(LegacyFlammabilityTags.FLAMMABLE)), "catalogue follows native fire data and the bound exception tag: " + block);
        }
        for (var block : new Block[]{Blocks.NOTE_BLOCK, Blocks.JUKEBOX})
            h.assertTrue(fire.getBurnOdds(block.defaultBlockState()) == 0 && BlockProperties.isFlammable(block),
                    "original exception is supplied by generated data: " + block);
        for (var block : new Block[]{Blocks.AIR, Blocks.STONE, Blocks.CRIMSON_PLANKS, Blocks.WARPED_PLANKS,
                Blocks.CRIMSON_SLAB, Blocks.WARPED_STAIRS})
            h.assertTrue(!ReikaWorldHelper.flammable(block), "fireproof native blocks remain excluded: " + block);
        h.succeed();
    }

    private static void actualState(GameTestHelper h) {
        var level = h.getLevel(); var pos = h.absolutePos(new BlockPos(4, 2, 4));
        for (var block : new Block[]{Blocks.OAK_SLAB, Blocks.OAK_STAIRS, Blocks.MANGROVE_ROOTS}) {
            var dry = block.defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, false);
            level.setBlock(pos, dry, Block.UPDATE_CLIENTS);
            h.assertTrue(ReikaWorldHelper.flammable(level, pos), "dry state is flammable: " + block);
            level.setBlock(pos, dry.setValue(BlockStateProperties.WATERLOGGED, true), Block.UPDATE_CLIENTS);
            h.assertTrue(!ReikaWorldHelper.flammable(level, pos), "waterlogged actual state is not replaced by a dry default: " + block);
        }
        for (var block : new Block[]{Blocks.CHEST, Blocks.OAK_TRAPDOOR}) {
            level.setBlock(pos, block.defaultBlockState(), Block.UPDATE_CLIENTS);
            h.assertTrue(!ReikaWorldHelper.flammable(level, pos), "original world helper exception is retained: " + block);
        }
        h.succeed();
    }

    private static void tagRebind(GameTestHelper h) {
        var registry = BuiltInRegistries.BLOCK;
        var original = new HashMap<TagKey<Block>, List<Holder<Block>>>();
        registry.getTags().forEach(tag -> original.put(tag.key(), List.copyOf(tag.contents())));
        var changed = new HashMap<>(original);
        changed.put(LegacyFlammabilityTags.FLAMMABLE, List.of(Blocks.STONE.builtInRegistryHolder(), Blocks.OAK_STAIRS.builtInRegistryHolder()));
        try {
            // Exercise the native tag-reload apply path synchronously, retaining all other tags.
            registry.prepareTagReload(new TagLoader.LoadResult<>(registry.key(), changed)).apply();
            h.assertTrue(BlockProperties.isFlammable(Blocks.STONE) && !BlockProperties.isFlammable(Blocks.NOTE_BLOCK),
                    "additions and removals are observed without rebuilding a cached catalogue");
            h.assertTrue(!BlockProperties.isFlammable(Blocks.OAK_STAIRS.defaultBlockState()
                    .setValue(BlockStateProperties.WATERLOGGED, true)), "tag overrides do not make waterlogged wood dry");
        } finally {
            registry.prepareTagReload(new TagLoader.LoadResult<>(registry.key(), original)).apply();
        }
        h.assertTrue(!BlockProperties.isFlammable(Blocks.STONE) && BlockProperties.isFlammable(Blocks.NOTE_BLOCK),
                "restored tag bindings are observed immediately");
        h.succeed();
    }

    private static void temperature(GameTestHelper h) {
        var level = h.getLevel(); var pos = h.absolutePos(new BlockPos(4, 2, 4));
        level.setBlock(pos.below(), Blocks.STONE.defaultBlockState(), Block.UPDATE_CLIENTS);
        for (var block : new Block[]{Blocks.OAK_PLANKS, Blocks.WOOL.white(), Blocks.NOTE_BLOCK, Blocks.OAK_SLAB}) {
            level.setBlock(pos, block.defaultBlockState(), Block.UPDATE_CLIENTS);
            level.setBlock(pos.above(), Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
            TemperatureEffect.woodIgnition.apply(level, pos, 600, null);
            h.assertTrue(level.getBlockState(pos.above()).is(Blocks.FIRE), "real ignition effect reaches flammable catalogue entries: " + block);
        }
        level.setBlock(pos, Blocks.OAK_SLAB.defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, true), Block.UPDATE_CLIENTS);
        level.setBlock(pos.above(), Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
        TemperatureEffect.woodIgnition.apply(level, pos, 600, null);
        h.assertTrue(level.getBlockState(pos.above()).isAir(), "real ignition effect respects the actual wet state");
        h.succeed();
    }

    private static void laser(GameTestHelper h) {
        var level = h.getLevel(); var origin = h.absolutePos(new BlockPos(4, 2, 2));
        var target = origin.south(2);
        // The public beam entry point uses its stored position and aim; supply a short clear ray.
        var gun = new BlockEntityLaserGun(origin, RotaryBlocks.LASER_GUN.get().defaultBlockState());
        gun.theta = 0;
        for (int i = 0; i <= 3; i++) {
            level.setBlock(origin.south(i), Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
            level.setBlock(origin.south(i).below(), Blocks.STONE.defaultBlockState(), Block.UPDATE_CLIENTS);
        }
        for (var block : new Block[]{Blocks.OAK_PLANKS, Blocks.WOOL.white(), Blocks.NOTE_BLOCK}) {
            level.setBlock(target, block.defaultBlockState(), Block.UPDATE_CLIENTS);
            gun.fire(level, null);
            h.assertTrue(level.getBlockState(target).is(Blocks.FIRE), "real laser beam converts flammable targets: " + block);
        }
        var wet = Blocks.OAK_SLAB.defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, true);
        level.setBlock(target, wet, Block.UPDATE_CLIENTS);
        level.setBlock(target.south(), Blocks.STONE.defaultBlockState(), Block.UPDATE_CLIENTS);
        gun.fire(level, null);
        h.assertTrue(level.getBlockState(target).equals(wet), "real laser beam retains waterlogged wood");
        h.succeed();
    }
}
