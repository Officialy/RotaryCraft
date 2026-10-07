package reika.rotarycraft;

import java.util.*;
import java.util.function.Consumer;
import net.minecraft.core.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;
import reika.rotarycraft.api.event.PileDriverImpactEvent;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.base.blocks.entity.BlockMiningPipe;
import reika.rotarycraft.blockentities.level.BlockEntityPileDriver;
import reika.rotarycraft.blockentities.transmission.BlockEntityCreativeCoil;
import reika.rotarycraft.items.ItemSpawner;
import reika.rotarycraft.registry.*;

/** Native terrain, loot, protection, mechanics and recovered spawner regression coverage. */
final class RotaryPileDriverTests {
    private static final BlockPos POS = new BlockPos(4, 7, 4), TARGET = new BlockPos(4, 3, 4);
    static void register(RegisterGameTestsEvent e, Holder<TestEnvironmentDefinition<?>> env) {
        for (Direction side : Direction.values()) test(e, env, "power_" + side.getName(), h -> power(h, side));
        test(e, env, "strongest_opposing_input", RotaryPileDriverTests::sum);
        test(e, env, "power_thresholds_and_long_depth", RotaryPileDriverTests::thresholds);
        test(e, env, "original_footprint_and_corners", RotaryPileDriverTests::footprint);
        test(e, env, "stone_conversion_no_drop", h -> conversion(h, Blocks.STONE, Blocks.COBBLESTONE));
        test(e, env, "stone_bricks_conversion", h -> conversion(h, Blocks.STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS));
        for (Block block : new Block[]{Blocks.BEDROCK, Blocks.WATER, Blocks.LAVA}) test(e, env, "keep_" + BuiltInRegistries.BLOCK.getKey(block).getPath(), h -> keep(h, block));
        test(e, env, "keep_shield_and_junction", RotaryPileDriverTests::keepSpecial);
        test(e, env, "obsidian_six_strikes", RotaryPileDriverTests::obsidian);
        test(e, env, "hit_counter_partial_sync", RotaryPileDriverTests::hitSync);
        test(e, env, "protected_waterlogged_plane", RotaryPileDriverTests::waterlogged);
        test(e, env, "default_spawner_pig", RotaryPileDriverTests::defaultSpawner);
        test(e, env, "hanging_frame_loot", h -> frame(h, false));
        test(e, env, "hanging_frame_protection", h -> frame(h, true));
        test(e, env, "hit_counter_replacement", RotaryPileDriverTests::replacement);
        for (int depth = 1; depth <= 4; depth++) { final int n = depth; test(e, env, "weak_glass_layer_" + depth, h -> weak(h, Blocks.GLASS, n, true)); }
        test(e, env, "weak_netherrack_two_layers", h -> weak(h, Blocks.NETHERRACK, 2, true));
        test(e, env, "weak_netherrack_third_layer_preserved", h -> weak(h, Blocks.NETHERRACK, 3, false));
        test(e, env, "weak_glowstone_third_layer", h -> weak(h, Blocks.GLOWSTONE, 3, true));
        test(e, env, "weak_wool_first_layer", h -> weak(h, Blocks.WOOL.red(), 1, true));
        test(e, env, "impact_event_before_changes", RotaryPileDriverTests::event);
        test(e, env, "owner_protection", h -> protection(h, 0));
        test(e, env, "weak_layer_protection", h -> protection(h, 1));
        test(e, env, "shockwave_protection", h -> protection(h, 2));
        test(e, env, "protection_state_change", h -> protection(h, 3));
        for (Block block : new Block[]{Blocks.GLASS, Blocks.GLASS_PANE, Blocks.GLOWSTONE, Blocks.CACTUS, Blocks.FERN, Blocks.OAK_SAPLING,
                Blocks.FLOWER_POT, Blocks.SKELETON_SKULL, Blocks.ICE, Blocks.COBWEB, Blocks.STAINED_GLASS.red(), Blocks.TNT})
            test(e, env, "shockwave_" + BuiltInRegistries.BLOCK.getKey(block).getPath(), h -> fragile(h, block));
        test(e, env, "falling_preserves_state", RotaryPileDriverTests::falling);
        test(e, env, "hammer_tip_and_borer_sentinel", RotaryPileDriverTests::bit);
        test(e, env, "hammer_cycle_live", RotaryPileDriverTests::live);
        test(e, env, "hammer_protected_start", RotaryPileDriverTests::blocked);
        test(e, env, "deep_hammer_descent", h -> phase(h, 0));
        test(e, env, "deep_hammer_lift_timer", h -> phase(h, 1));
        test(e, env, "retraction_preserves_replacement", h -> phase(h, 2));
        test(e, env, "retraction_permission_preserves_phase", h -> phase(h, 3));
        test(e, env, "air_column_skips_depth", RotaryPileDriverTests::airColumn);
        test(e, env, "bottom_world_bound", RotaryPileDriverTests::bottom);
        test(e, env, "liquid_hammer_water", h -> liquid(h, Blocks.WATER));
        test(e, env, "liquid_hammer_lava", h -> liquid(h, Blocks.LAVA));
        test(e, env, "mining_pipe_horizontal_cleanup", RotaryPileDriverTests::cleanup);
        test(e, env, "spawner_place_protection_rollback", RotaryPileDriverTests::placeProtection);
        for (Block soft : new Block[]{Blocks.WATER, Blocks.LAVA, Blocks.VINE, Blocks.SNOW})
            test(e, env, "spawner_soft_" + BuiltInRegistries.BLOCK.getKey(soft).getPath(), h -> softSpawner(h, soft));
        test(e, env, "spawner_no_type", RotaryPileDriverTests::untyped);
        test(e, env, "native_save_and_sync", RotaryPileDriverTests::save);
        test(e, env, "grounded_bounce_and_crushing", RotaryPileDriverTests::effects);
        test(e, env, "survival_player_nausea", RotaryPileDriverTests::nausea);
        test(e, env, "spawner_recovery_type_only", RotaryPileDriverTests::spawner);
        test(e, env, "spawner_last_item_survival_place", h -> place(h, false, false));
        test(e, env, "spawner_custom_parameters", h -> place(h, true, false));
        test(e, env, "spawner_creative_place", h -> place(h, false, true));
        test(e, env, "spawner_dimension_and_variants", RotaryPileDriverTests::dimension);
        test(e, env, "spawner_living_collision", RotaryPileDriverTests::collision);
        if (net.neoforged.fml.ModList.get().isLoaded("geostrata")) {
            for (String rock : new String[]{"granite", "basalt", "marble", "limestone", "shale", "sandstone", "pumice", "slate", "gneiss", "peridotite", "quartz", "granulite", "hornfel", "migmatite", "schist", "onyx", "opal"})
                test(e, env, "geostrata_" + rock, h -> rock(h, rock));
        }
        test(e, env, "original_recipe", RotaryPileDriverTests::recipe);
        test(e, env, "survival_machine_harvest", RotaryPileDriverTests::harvest);
    }
    private static void test(RegisterGameTestsEvent e, Holder<TestEnvironmentDefinition<?>> env, String name, Consumer<GameTestHelper> body) { RotaryGameTests.register(e, env, "piledriver_" + name, 150, body); }
    private static BlockEntityPileDriver machine(GameTestHelper h) { h.setBlock(POS, RotaryBlocks.PILEDRIVER.get()); return h.getBlockEntity(POS, BlockEntityPileDriver.class); }
    private static void coil(GameTestHelper h, Direction side, int torque, int speed) {
        BlockPos pos = POS.relative(side); h.setBlock(pos.relative(side), Blocks.REDSTONE_BLOCK);
        h.setBlock(pos, RotaryBlocks.CREATIVE_COIL.get().defaultBlockState().setValue(BlockRotaryCraftMachine.FACING, side.getAxis().isVertical() ? side : side.getOpposite()));
        var coil = h.getBlockEntity(pos, BlockEntityCreativeCoil.class); coil.setReleaseTorque(torque); coil.setReleaseOmega(speed); coil.updateEntity(h.getLevel(), coil.getBlockPos());
    }
    private static void power(GameTestHelper h, Direction side) {
        var tile = machine(h); coil(h, side, 80000, 1); tile.updateEntity(h.getLevel(), tile.getBlockPos());
        h.assertTrue(tile.MINTORQUE == 80000 && tile.MINPOWER == 16384 && tile.power == (side.getAxis() == Direction.Axis.Z ? 80000 : 0), "default north/south shafts only"); h.succeed();
    }
    private static void sum(GameTestHelper h) {
        var tile = machine(h); coil(h, Direction.NORTH, 40000, 1); coil(h, Direction.SOUTH, 40000, 1); tile.updateEntity(h.getLevel(), tile.getBlockPos());
        h.assertTrue(tile.power == 40000 && tile.torque == 40000 && tile.step == 0, "opposite shaft torques are not summed in V33a");
        coil(h, Direction.SOUTH, 80000, 1); tile.updateEntity(h.getLevel(), tile.getBlockPos());
        h.assertTrue(tile.power == 80000 && tile.torque == 80000 && tile.step == 1, "stronger opposing input supplies the hammer");
        h.setBlock(POS, tile.getBlockState().setValue(BlockRotaryCraftMachine.FACING, Direction.EAST)); coil(h, Direction.EAST, 80000, 1); tile.updateEntity(h.getLevel(), tile.getBlockPos());
        h.assertTrue(tile.getReadDirection() == Direction.EAST && tile.getReadDirection2() == Direction.WEST && tile.power == 80000, "rotated machine reads east/west shafts"); h.succeed();
    }
    private static void thresholds(GameTestHelper h) {
        var tile = machine(h); tile.step = 3; tile.power = 65536;
        h.assertTrue(tile.getRequiredPower() == 65536 && tile.getCycleTime() == 300, "16 kW for each lifted meter"); tile.power *= 4;
        h.assertTrue(tile.getCycleTime() == 75, "power multiplier reduces lift time"); tile.power = Long.MAX_VALUE;
        h.assertTrue(tile.getCycleTime() == 1, "very high power never overflows the one-tick floor"); tile.step = Integer.MAX_VALUE;
        h.assertTrue(tile.getRequiredPower() == 35184372088832L, "depth multiplication uses long arithmetic"); h.succeed();
    }
    private static void footprint(GameTestHelper h) {
        var tile = machine(h);
        for (int x = -2; x <= 2; x++) for (int z = -2; z <= 2; z++) h.setBlock(TARGET.offset(x, 0, z), Blocks.DIRT);
        h.assertTrue(tile.smash(h.absolutePos(TARGET)), "all twenty-one targets clear");
        for (int x = -2; x <= 2; x++) for (int z = -2; z <= 2; z++) h.assertTrue(h.getBlockState(TARGET.offset(x, 0, z)).is(Math.abs(x * z) == 4 ? Blocks.DIRT : Blocks.AIR), "source corners are excluded");
        h.assertTrue(h.getEntities(EntityTypes.ITEM, TARGET, 5).stream().filter(i -> i.getItem().is(Items.DIRT)).mapToInt(i -> i.getItem().getCount()).sum() == 21, "fortune-zero drops exactly once per target"); h.succeed();
    }
    private static void conversion(GameTestHelper h, Block from, Block to) {
        var tile = machine(h); h.setBlock(TARGET, from); h.assertTrue(!tile.smash(h.absolutePos(TARGET)) && h.getBlockState(TARGET).is(to), "conversion prevents advance and emits no loot");
        h.assertTrue(h.getEntities(EntityTypes.ITEM, TARGET, 2).isEmpty(), "conversion does not duplicate block"); tile.smash(h.absolutePos(TARGET)); h.assertTrue(h.getBlockState(TARGET).isAir(), "second strike breaks product"); h.succeed();
    }
    private static void keep(GameTestHelper h, Block block) {
        var tile = machine(h); h.setBlock(TARGET, block); var state = h.getBlockState(TARGET); tile.strike(h.absolutePos(TARGET));
        h.assertTrue(h.getBlockState(TARGET).equals(state) && h.getEntities(EntityTypes.ITEM, TARGET, 2).isEmpty(), "bedrock and liquids retain exact state"); h.succeed();
    }
    private static void keepSpecial(GameTestHelper h) {
        var tile = machine(h); h.setBlock(TARGET, RotaryBlocks.SHIELD.get()); tile.strike(h.absolutePos(TARGET)); h.assertTrue(h.getBlockState(TARGET).is(RotaryBlocks.SHIELD.get()), "shield remains");
        h.setBlock(TARGET, RotaryBlocks.MININGPIPE.get().defaultBlockState().setValue(BlockMiningPipe.SHAPE, BlockMiningPipe.Shape.JUNCTION)); tile.strike(h.absolutePos(TARGET));
        h.assertTrue(h.getBlockState(TARGET).getValue(BlockMiningPipe.SHAPE).isJunction(), "borer junction protected"); h.succeed();
    }
    private static void obsidian(GameTestHelper h) {
        var tile = machine(h); h.setBlock(TARGET, Blocks.OBSIDIAN);
        for (int hit = 1; hit <= 5; hit++) { h.assertTrue(!tile.strike(h.absolutePos(TARGET)) && h.getBlockState(TARGET).is(Blocks.OBSIDIAN), "strike " + hit + " retains obsidian"); }
        h.assertTrue(tile.strike(h.absolutePos(TARGET)) && h.getBlockState(TARGET).isAir() && h.getEntities(EntityTypes.ITEM, TARGET, 2).stream().anyMatch(i -> i.getItem().is(Items.OBSIDIAN)), "sixth strike breaks and drops obsidian"); h.succeed();
    }
    private static void hitSync(GameTestHelper h) {
        var tile = machine(h); h.setBlock(TARGET, Blocks.OBSIDIAN); for (int n = 0; n < 5; n++) tile.strike(h.absolutePos(TARGET));
        CompoundTag partial = new CompoundTag(); partial.putBoolean("active", true); tile.applySyncTag(partial);
        h.assertTrue(tile.strike(h.absolutePos(TARGET)) && h.getBlockState(TARGET).isAir(), "partial machine sync cannot erase accumulated impacts"); h.succeed();
    }
    private static void waterlogged(GameTestHelper h) {
        var tile = machine(h); h.setBlock(TARGET, Blocks.OAK_STAIRS.defaultBlockState().setValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED, true));
        Consumer<BreakBlockEvent> listener = e -> { if (e.getPos().equals(h.absolutePos(TARGET))) e.setCanceled(true); };
        NeoForge.EVENT_BUS.addListener(listener); try { h.assertTrue(!tile.smash(h.absolutePos(TARGET)) && h.getBlockState(TARGET).is(Blocks.OAK_STAIRS), "protected solid with water in it is not a cleared liquid plane"); } finally { NeoForge.EVENT_BUS.unregister(listener); } h.succeed();
    }
    private static void defaultSpawner(GameTestHelper h) {
        h.setBlock(TARGET, Blocks.SPAWNER); h.assertTrue(ItemSpawner.type(ItemSpawner.fromSpawner(h.getBlockEntity(TARGET, SpawnerBlockEntity.class))) == EntityTypes.PIG, "native default spawner initializes and recovers its pig type"); h.succeed();
    }
    private static void frame(GameTestHelper h, boolean protectedFrame) {
        var tile = machine(h); var pos = TARGET.offset(3, 0, 0); h.setBlock(pos.relative(Direction.SOUTH), Blocks.STONE);
        var frame = new net.minecraft.world.entity.decoration.ItemFrame(h.getLevel(), h.absolutePos(pos), Direction.NORTH); frame.setItem(new ItemStack(Items.DIAMOND), false); h.getLevel().addFreshEntity(frame);
        Consumer<BreakBlockEvent> listener = e -> { if (protectedFrame && e.getPos().equals(frame.getPos())) e.setCanceled(true); };
        NeoForge.EVENT_BUS.addListener(listener); try {
            tile.shockwave(h.absolutePos(TARGET));
            h.assertTrue(!frame.isRemoved() && frame.getItem().isEmpty() != protectedFrame, "native first frame hit ejects displayed item and honors owner permission");
            if (!protectedFrame) {
                h.assertTrue(h.getEntities(EntityTypes.ITEM, pos, 3).stream().anyMatch(i -> i.getItem().is(Items.DIAMOND)), "native frame hit preserves displayed item loot");
                tile.shockwave(h.absolutePos(TARGET)); h.assertTrue(frame.isRemoved() && h.getEntities(EntityTypes.ITEM, pos, 3).stream().anyMatch(i -> i.getItem().is(Items.ITEM_FRAME)), "second impact breaks the now-empty frame with its own loot");
            }
        } finally { NeoForge.EVENT_BUS.unregister(listener); } h.succeed();
    }
    private static void replacement(GameTestHelper h) {
        var tile = machine(h); h.setBlock(TARGET, Blocks.OBSIDIAN); tile.strike(h.absolutePos(TARGET)); h.setBlock(TARGET, Blocks.STONE); tile.strike(h.absolutePos(TARGET));
        h.assertTrue(h.getBlockState(TARGET).is(Blocks.COBBLESTONE), "a changed block cannot inherit stale positive hit count"); h.succeed();
    }
    private static void weak(GameTestHelper h, Block block, int depth, boolean broken) {
        var tile = machine(h); BlockPos top = new BlockPos(4, 6, 4), under = top.below(depth); h.setBlock(top, Blocks.DIRT); h.setBlock(under, block);
        tile.smash(h.absolutePos(top)); h.assertTrue(h.getBlockState(under).isAir() == broken, "negative hit count controls the original weak-layer depth"); h.succeed();
    }
    private static void event(GameTestHelper h) {
        var tile = machine(h); h.setBlock(TARGET, Blocks.DIRT); boolean[] called = {false}; Consumer<PileDriverImpactEvent> listener = e -> {
            if (e.getWorld() == h.getLevel() && e.getTileX() == tile.getBlockPos().getX()) called[0] = e.centerY == h.absolutePos(TARGET).getY() && h.getBlockState(TARGET).is(Blocks.DIRT);
        };
        NeoForge.EVENT_BUS.addListener(listener); try { tile.smash(h.absolutePos(TARGET)); h.assertTrue(called[0], "non-cancellable event precedes terrain mutation with exact center"); } finally { NeoForge.EVENT_BUS.unregister(listener); } h.succeed();
    }
    private static void protection(GameTestHelper h, int mode) {
        var tile = machine(h); var player = h.makeMockServerPlayer(GameType.SURVIVAL); tile.setPlacer(player);
        BlockPos target = mode == 1 ? TARGET.below() : mode == 2 ? TARGET.offset(3, 0, 0) : TARGET;
        h.setBlock(TARGET, Blocks.DIRT); h.setBlock(target, mode == 0 || mode == 3 ? Blocks.DIRT : mode == 1 ? Blocks.NETHERRACK : Blocks.GLOWSTONE);
        boolean[] called = {false}; Consumer<BreakBlockEvent> listener = e -> { if (e.getLevel() == h.getLevel() && e.getPos().equals(h.absolutePos(target))) {
            called[0] = e.getPlayer().getUUID().equals(player.getUUID()); if (mode == 3) h.setBlock(target, Blocks.STONE); else e.setCanceled(true);
        }};
        NeoForge.EVENT_BUS.addListener(listener); try {
            tile.smash(h.absolutePos(TARGET)); h.assertTrue(called[0] && h.getBlockState(target).is(mode == 3 ? Blocks.STONE : mode == 0 ? Blocks.DIRT : mode == 1 ? Blocks.NETHERRACK : Blocks.GLOWSTONE), "owner event protects every layer and rechecks callback replacements");
        } finally { NeoForge.EVENT_BUS.unregister(listener); } h.succeed();
    }
    private static void fragile(GameTestHelper h, Block block) {
        var tile = machine(h); BlockPos target = TARGET.offset(3, 0, 0); h.setBlock(target, block); tile.shockwave(h.absolutePos(TARGET));
        if (block == Blocks.ICE) h.assertTrue(h.getBlockState(target).is(Blocks.WATER) && h.getEntities(EntityTypes.ITEM, target, 2).stream().anyMatch(i -> i.getItem().is(Items.ICE)), "ice produces water and one ice item");
        else if (block == Blocks.TNT || block == Blocks.STAINED_GLASS.red()) h.assertTrue(h.getBlockState(target).is(block), "source does not target stained glass or ignite TNT");
        else { h.assertTrue(h.getBlockState(target).isAir(), "source fragile target removed"); if (block == Blocks.COBWEB) h.assertTrue(h.getEntities(EntityTypes.ITEM, target, 2).stream().anyMatch(i -> i.getItem().is(Items.STRING)), "web drops string"); }
        h.succeed();
    }
    private static void falling(GameTestHelper h) {
        var tile = machine(h); var pos = TARGET.offset(3, 2, 0); h.setBlock(pos, Blocks.RED_SAND); tile.shockwave(h.absolutePos(TARGET));
        h.assertTrue(h.getBlockState(pos).isAir(), "shockwave initiates unsupported falling block");
        h.assertTrue(!h.getLevel().getEntitiesOfClass(net.minecraft.world.entity.item.FallingBlockEntity.class, new AABB(h.absolutePos(pos)).inflate(3)).isEmpty()
                || h.getBlockState(new BlockPos(pos.getX(), 1, pos.getZ())).is(Blocks.RED_SAND), "native falling entity or instant landing preserves red sand state"); h.succeed();
    }
    private static void bit(GameTestHelper h) {
        var tile = machine(h); h.setBlock(TARGET, RotaryBlocks.MININGPIPE.get().defaultBlockState().setValue(BlockMiningPipe.SHAPE, BlockMiningPipe.Shape.PILE_DRIVER));
        h.assertTrue(!h.getBlockState(TARGET).getValue(BlockMiningPipe.SHAPE).isJunction() && h.getBlockState(TARGET).getShape(h.getLevel(), h.absolutePos(TARGET)).bounds().getYsize() == 1, "hammer tip is full cube and distinct from borer junction");
        h.assertTrue(tile.strike(h.absolutePos(TARGET)) && h.getEntities(EntityTypes.ITEM, TARGET, 2).isEmpty(), "hammer tip breaks without dropping tunnel lining"); h.succeed();
    }
    private static void live(GameTestHelper h) {
        var tile = machine(h); h.setBlock(POS.below(2), Blocks.STONE); coil(h, Direction.NORTH, 80000, 64);
        tile.updateEntity(h.getLevel(), tile.getBlockPos()); h.assertTrue(tile.step == 1 && tile.isClimbing(), "first stroke clears the fresh bit");
        tile.updateEntity(h.getLevel(), tile.getBlockPos()); h.assertTrue(h.getBlockState(POS.below(2)).is(Blocks.COBBLESTONE) && tile.step == 1, "second stroke converts stone at depth one");
        tile.updateEntity(h.getLevel(), tile.getBlockPos()); h.assertTrue(h.getBlockState(POS.below(2)).isAir() && tile.step == 2, "next stroke breaks converted cobble and advances"); h.succeed();
    }
    private static void blocked(GameTestHelper h) {
        var tile = machine(h); BlockPos below = POS.below(); boolean[] called = {false}; Consumer<BreakBlockEvent> listener = e -> { if (e.getPos().equals(h.absolutePos(below))) { called[0] = true; e.setCanceled(true); }};
        NeoForge.EVENT_BUS.addListener(listener); try { tile.drawPile(h.getLevel(), 1); h.assertTrue(called[0] && h.getBlockState(below).isAir() && tile.getHammerDepth() == 0, "protected hammer placement cannot overwrite or advance"); } finally { NeoForge.EVENT_BUS.unregister(listener); } h.succeed();
    }
    private static void phase(GameTestHelper h, int mode) {
        var tile = machine(h); var saved = tile.saveWithoutMetadata(h.getLevel().registryAccess());
        saved.putInt("step", 4); saved.putInt("step2", 3); saved.putBoolean("climbing", mode != 0); saved.putInt("tick", mode >= 2 ? 301 : 0);
        tile.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, h.getLevel().registryAccess(), saved));
        var bit = RotaryBlocks.MININGPIPE.get().defaultBlockState().setValue(BlockMiningPipe.SHAPE, BlockMiningPipe.Shape.PILE_DRIVER);
        h.setBlock(POS.below(4), bit); h.setBlock(POS.below(5), Blocks.STONE);
        if (mode == 0) {
            tile.drawPile(h.getLevel(), 300); h.assertTrue(tile.getHammerDepth() == 4 && h.getBlockState(POS.below(4)).equals(bit), "downstroke places the next tip and reaches impact depth");
        } else if (mode == 1) {
            coil(h, Direction.NORTH, 80000, 2); tile.updateEntity(h.getLevel(), tile.getBlockPos());
            h.assertTrue(tile.getHammerDepth() == 3 && tile.isClimbing() && h.getBlockState(POS.below(4)).isAir(), "lift stays at depth until depth-dependent timer clears");
            for (int n = 0; n < 300; n++) tile.updateEntity(h.getLevel(), tile.getBlockPos());
            h.assertTrue(tile.getHammerDepth() == 2 && tile.isClimbing(), "source uses strict greater-than lift timer");
        } else {
            h.setBlock(POS.below(3), mode == 2 ? Blocks.OBSIDIAN.defaultBlockState() : bit);
            Consumer<BreakBlockEvent> listener = e -> { if (mode == 3 && e.getPos().equals(h.absolutePos(POS.below(3)))) e.setCanceled(true); };
            NeoForge.EVENT_BUS.addListener(listener); try {
                tile.drawPile(h.getLevel(), 300); h.assertTrue(tile.getHammerDepth() == 3 && tile.isClimbing() && h.getBlockState(POS.below(3)).is(mode == 2 ? Blocks.OBSIDIAN : RotaryBlocks.MININGPIPE.get()), "retraction cannot delete a replacement or advance through protected tip");
            } finally { NeoForge.EVENT_BUS.unregister(listener); }
        }
        h.succeed();
    }
    private static void airColumn(GameTestHelper h) {
        var tile = machine(h); tile.step = 1; h.setBlock(POS.below(6), Blocks.DIRT); tile.drawPile(h.getLevel(), 1);
        h.assertTrue(tile.step == 4 && tile.getHammerDepth() == 4 && h.getBlockState(POS.below()).is(RotaryBlocks.MININGPIPE.get()), "source skips empty depth without generating chunks or adding ghost shaft blocks"); h.succeed();
    }
    private static void bottom(GameTestHelper h) {
        var tile = machine(h); var pos = new BlockPos(tile.getBlockPos().getX(), h.getLevel().getMinY(), tile.getBlockPos().getZ());
        var detached = new BlockEntityPileDriver(pos, tile.getBlockState()); detached.setLevel(h.getLevel());
        h.assertTrue(!detached.drawPile(h.getLevel(), 1) && detached.getHammerDepth() == 0 && detached.step == 0, "hammer never places beyond native minimum build height"); h.succeed();
    }
    private static void liquid(GameTestHelper h, Block liquid) {
        var tile = machine(h); h.setBlock(POS.below(), liquid); tile.drawPile(h.getLevel(), 1);
        h.assertTrue(h.getBlockState(POS.below()).is(RotaryBlocks.MININGPIPE.get()) && h.getBlockState(POS.below()).getValue(BlockMiningPipe.SHAPE) == BlockMiningPipe.Shape.PILE_DRIVER, "liquid downstroke places the source hammer tip and emits native effect burst"); h.succeed();
    }
    private static void cleanup(GameTestHelper h) {
        machine(h); var state = RotaryBlocks.MININGPIPE.get().defaultBlockState().setValue(BlockMiningPipe.SHAPE, BlockMiningPipe.Shape.AXIS_X);
        for (int n = -3; n <= 3; n++) h.setBlock(TARGET.offset(n, 0, 0), state);
        h.setBlock(TARGET.offset(0, 0, 2), state); var player = (ServerPlayer)h.makeMockServerPlayer(GameType.SURVIVAL);
        h.assertTrue(player.gameMode.destroyBlock(h.absolutePos(TARGET)), "zero-hardness original mining pipe breaks by hand");
        for (int n = -3; n <= 3; n++) h.assertTrue(h.getBlockState(TARGET.offset(n, 0, 0)).isAir(), "hand break clears same-axis tunnel lining");
        h.assertTrue(h.getBlockState(TARGET.offset(0, 0, 2)).equals(state) && h.getEntities(EntityTypes.ITEM, TARGET, 5).isEmpty(), "off-axis lining stays and generated loot drops nothing"); h.succeed();
    }
    private static void placeProtection(GameTestHelper h) {
        var player = player(h); player.setPos(Vec3.atCenterOf(h.absolutePos(TARGET)).add(3, 0, 0)); var stack = ItemSpawner.typed(EntityTypes.BLAZE); player.setItemInHand(InteractionHand.MAIN_HAND, stack); h.setBlock(TARGET.below(), Blocks.STONE);
        boolean[] called = {false}; Consumer<net.neoforged.neoforge.event.level.BlockEvent.EntityPlaceEvent> listener = e -> {
            if (e.getLevel() == h.getLevel() && e.getPos().equals(h.absolutePos(TARGET))) { called[0] = e.getEntity() == player; e.setCanceled(true); }
        };
        NeoForge.EVENT_BUS.addListener(listener); try {
            var hit = new BlockHitResult(Vec3.atCenterOf(h.absolutePos(TARGET.below())), Direction.UP, h.absolutePos(TARGET.below()), false);
            var result = player.gameMode.useItemOn(player, h.getLevel(), stack, InteractionHand.MAIN_HAND, hit);
            h.assertTrue(called[0] && !result.consumesAction() && h.getBlockState(TARGET).isAir() && stack.getCount() == 1 && ItemSpawner.type(stack) == EntityTypes.BLAZE, "native placement event rolls back block and typed last item");
        } finally { NeoForge.EVENT_BUS.unregister(listener); h.getLevel().getServer().getPlayerList().remove(player); } h.succeed();
    }
    private static void softSpawner(GameTestHelper h, Block block) {
        var player = player(h); player.setPos(Vec3.atCenterOf(h.absolutePos(TARGET)).add(3, 0, 0)); var stack = ItemSpawner.typed(EntityTypes.COW); player.setItemInHand(InteractionHand.MAIN_HAND, stack);
        h.setBlock(TARGET, block == Blocks.SNOW ? block.defaultBlockState().setValue(SnowLayerBlock.LAYERS, 8) : block.defaultBlockState());
        var hit = new BlockHitResult(Vec3.atCenterOf(h.absolutePos(TARGET)), Direction.UP, h.absolutePos(TARGET), false);
        var result = player.gameMode.useItemOn(player, h.getLevel(), stack, InteractionHand.MAIN_HAND, hit);
        h.assertTrue(result.consumesAction() && h.getBlockState(TARGET).is(Blocks.SPAWNER) && stack.isEmpty(), "source soft clicked target is replaced in place without shifting the placement above it");
        h.getLevel().getServer().getPlayerList().remove(player); h.succeed();
    }
    private static void untyped(GameTestHelper h) {
        var player = player(h); player.setPos(Vec3.atCenterOf(h.absolutePos(TARGET)).add(3, 0, 0)); var stack = new ItemStack(RotaryItems.SPAWNER.get()); player.setItemInHand(InteractionHand.MAIN_HAND, stack);
        var context = new UseOnContext(player, InteractionHand.MAIN_HAND, new BlockHitResult(Vec3.atCenterOf(h.absolutePos(TARGET)), Direction.UP, h.absolutePos(TARGET), false));
        h.assertTrue(!RotaryItems.SPAWNER.get().useOn(context).consumesAction() && stack.getCount() == 1 && h.getBlockState(TARGET).isAir() && Blocks.SPAWNER.asItem() == Items.SPAWNER, "untyped spawner rejects without consuming or overriding the native spawner item");
        h.getLevel().getServer().getPlayerList().remove(player); h.succeed();
    }
    private static void save(GameTestHelper h) {
        var tile = machine(h); coil(h, Direction.NORTH, 80000, 1); tile.updateEntity(h.getLevel(), tile.getBlockPos());
        var restored = new BlockEntityPileDriver(tile.getBlockPos(), tile.getBlockState()); restored.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, h.getLevel().registryAccess(), tile.saveWithoutMetadata(h.getLevel().registryAccess())));
        h.assertTrue(restored.step == tile.step && restored.getHammerDepth() == tile.getHammerDepth() && restored.isClimbing() == tile.isClimbing() && restored.hasSmashed() == tile.hasSmashed(), "detached registry-aware NBT restores the full hammer phase"); h.succeed();
    }
    private static void effects(GameTestHelper h) {
        var tile = machine(h); var near = EntityTypes.COW.create(h.getLevel(), EntitySpawnReason.COMMAND); near.setNoAi(true); near.setPos(Vec3.atCenterOf(h.absolutePos(TARGET))); h.getLevel().addFreshEntity(near);
        var far = EntityTypes.COW.create(h.getLevel(), EntitySpawnReason.COMMAND); far.setNoAi(true); far.setPos(Vec3.atLowerCornerOf(h.absolutePos(TARGET)).add(5, 0, 0)); far.setOnGround(true); h.getLevel().addFreshEntity(far);
        tile.applyEntityEffects(h.absolutePos(TARGET)); h.assertTrue(!near.isAlive() && far.isAlive() && far.getDeltaMovement().y > 0 && far.syncVelocity, "near unarmored mob crushed, grounded distant mob bounced and synchronized"); h.succeed();
    }
    private static ServerPlayer player(GameTestHelper h) {
        var player = (ServerPlayer)h.makeMockServerPlayer(GameType.SURVIVAL); var connection = new net.minecraft.network.Connection(net.minecraft.network.protocol.PacketFlow.SERVERBOUND);
        new io.netty.channel.embedded.EmbeddedChannel(connection); net.neoforged.neoforge.network.registration.NetworkRegistry.configureMockConnection(connection);
        h.getLevel().getServer().getPlayerList().placeNewPlayer(connection, player, net.minecraft.server.network.CommonListenerCookie.createInitial(player.getGameProfile(), false));
        player.setGameMode(GameType.SURVIVAL); player.connection.markClientLoaded(); return player;
    }
    private static void nausea(GameTestHelper h) {
        var tile = machine(h); var player = player(h); player.setPos(Vec3.atCenterOf(h.absolutePos(TARGET)).add(5, 0, 0)); tile.applyEntityEffects(h.absolutePos(TARGET));
        h.assertTrue(player.hasEffect(MobEffects.NAUSEA) && player.getEffect(MobEffects.NAUSEA).getDuration() == 150 && player.getEffect(MobEffects.NAUSEA).getAmplifier() == 10, "survival player receives original nausea duration and amplifier"); h.getLevel().getServer().getPlayerList().remove(player); h.succeed();
    }
    private static void spawner(GameTestHelper h) {
        var tile = machine(h); h.setBlock(TARGET, Blocks.SPAWNER); var spawner = h.getBlockEntity(TARGET, SpawnerBlockEntity.class); spawner.setEntityId(EntityTypes.BLAZE, h.getLevel().getRandom());
        h.assertTrue(tile.strike(h.absolutePos(TARGET)), "spawner broken"); var drops = h.getEntities(EntityTypes.ITEM, TARGET, 2).stream().filter(i -> i.getItem().is(RotaryItems.SPAWNER.get())).toList();
        h.assertTrue(drops.size() == 1 && ItemSpawner.type(drops.getFirst().getItem()) == EntityTypes.BLAZE && !drops.getFirst().getItem().get(DataComponents.CUSTOM_DATA).copyTag().contains("logic"), "recovered spawner contains only original mob type"); h.succeed();
    }
    private static void place(GameTestHelper h, boolean custom, boolean creative) {
        var player = player(h); if (creative) player.setGameMode(GameType.CREATIVE); player.setPos(Vec3.atCenterOf(h.absolutePos(TARGET)).add(3, 0, 0));
        var stack = ItemSpawner.typed(EntityTypes.BLAZE);
        if (custom) { var tag = stack.get(DataComponents.CUSTOM_DATA).copyTag(); var logic = new CompoundTag(); logic.putShort("MinSpawnDelay", (short)45); logic.putShort("MaxSpawnDelay", (short)70); logic.putShort("SpawnCount", (short)2); logic.putShort("RequiredPlayerRange", (short)9); tag.put("logic", logic); stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag)); }
        player.setItemInHand(InteractionHand.MAIN_HAND, stack); h.setBlock(TARGET.below(), Blocks.STONE);
        var hit = new BlockHitResult(Vec3.atCenterOf(h.absolutePos(TARGET.below())), Direction.UP, h.absolutePos(TARGET.below()), false);
        var result = player.gameMode.useItemOn(player, h.getLevel(), stack, InteractionHand.MAIN_HAND, hit);
        h.assertTrue(result.consumesAction() && h.getBlockState(TARGET).is(Blocks.SPAWNER), "native survival/creative item interaction places recovered spawner");
        var placed = h.getBlockEntity(TARGET, SpawnerBlockEntity.class); var data = ItemSpawner.spawnerData(placed);
        h.assertTrue(ItemSpawner.type(ItemSpawner.fromSpawner(placed)) == EntityTypes.BLAZE && stack.getCount() == (creative ? 1 : 0), "last stack retains mob type before survival consumption");
        h.assertTrue(data.getIntOr("Delay", -1) >= (custom ? 45 : 200) && data.getIntOr("Delay", 999) <= (custom ? 70 : 800), "original randomized spawn delay");
        if (custom) h.assertTrue(data.getIntOr("SpawnCount", 0) == 2 && data.getIntOr("RequiredPlayerRange", 0) == 9 && data.getIntOr("MaxSpawnDelay", 0) == 70, "custom spawner parameters survive placement");
        h.getLevel().getServer().getPlayerList().remove(player); h.succeed();
    }
    private static void dimension(GameTestHelper h) {
        h.assertTrue(ItemSpawner.creativeTypes().size() == 28 && new HashSet<>(ItemSpawner.creativeTypes()).size() == 28 && !ItemSpawner.validDimension(EntityTypes.ENDER_DRAGON, h.getLevel()) && ItemSpawner.validDimension(EntityTypes.BLAZE, h.getLevel()), "original twenty-eight creative variants and end-only dragon restriction"); h.succeed();
    }
    private static void collision(GameTestHelper h) {
        var player = (ServerPlayer)h.makeMockServerPlayer(GameType.SURVIVAL); player.setPos(Vec3.atCenterOf(h.absolutePos(TARGET)).add(3, 0, 0)); var stack = ItemSpawner.typed(EntityTypes.COW); player.setItemInHand(InteractionHand.MAIN_HAND, stack);
        var cow = EntityTypes.COW.create(h.getLevel(), EntitySpawnReason.COMMAND); cow.setPos(Vec3.atCenterOf(h.absolutePos(TARGET))); h.getLevel().addFreshEntity(cow);
        var context = new UseOnContext(player, InteractionHand.MAIN_HAND, new BlockHitResult(Vec3.atCenterOf(h.absolutePos(TARGET)), Direction.UP, h.absolutePos(TARGET), false));
        h.assertTrue(!RotaryItems.SPAWNER.get().useOn(context).consumesAction() && stack.getCount() == 1 && h.getBlockState(TARGET).isAir(), "living collision rejects placement without consumption"); h.succeed();
    }
    private static void rock(GameTestHelper h, String rock) {
        var tile = machine(h); int hits = switch (rock) { case "granite", "hornfel" -> 3; case "peridotite", "gneiss", "schist" -> 2; default -> 0; };
        Block cobble = BuiltInRegistries.BLOCK.getOptional(net.minecraft.resources.Identifier.fromNamespaceAndPath("geostrata", rock + "_cobble")).orElseThrow();
        for (String shape : new String[]{"smooth", "cobble", "brick", "round", "fitted", "tile", "engraved", "inscribed", "cubed", "lined", "embossed", "centered", "raised", "etched", "spiral", "fan", "mossy", "connected", "connected2", "pillar"}) {
            String suffix = shape.equals("connected") || shape.equals("connected2") ? "_connected" : "";
            var id = net.minecraft.resources.Identifier.fromNamespaceAndPath("geostrata", rock + "_" + shape + suffix);
            Block block = BuiltInRegistries.BLOCK.getOptional(id).orElseThrow(); h.setBlock(TARGET, block);
            h.assertTrue(PileDriverRules.hits(h.getBlockState(TARGET)) == (rock.equals("shale") || rock.equals("limestone") ? -1 : hits), "original GeoStrata weak-layer profile: " + id);
            for (int n = 0; n < hits; n++) h.assertTrue(!tile.strike(h.absolutePos(TARGET)) && h.getBlockState(TARGET).is(block), "original rock hit threshold: " + id);
            h.assertTrue(tile.strike(h.absolutePos(TARGET)) && h.getBlockState(TARGET).is(shape.equals("cobble") ? Blocks.AIR : cobble), "native conditional rock conversion: " + id);
            if (!shape.equals("cobble")) {
                for (int n = 0; n < hits; n++) h.assertTrue(!tile.strike(h.absolutePos(TARGET)), "rock cobble repeats its original positive hits");
                h.assertTrue(tile.strike(h.absolutePos(TARGET)) && h.getBlockState(TARGET).isAir(), "rock cobble finally breaks");
            }
        }
        h.succeed();
    }
    private static void recipe(GameTestHelper h) {
        var p = new ItemStack(RotaryItems.HSLA_PLATE.get()); var g = new ItemStack(RotaryItems.HSLA_STEEL_GEAR_8x.get()); var s = new ItemStack(RotaryItems.HSLA_SHAFT.get());
        var input = CraftingInput.of(3, 3, List.of(p,g,p,s,new ItemStack(RotaryItems.TUNGSTEN_ALLOY_FLYWHEEL_CORE.get()),s,p,new ItemStack(RotaryItems.DRILLHEAD_IRON.get()),p));
        h.assertTrue(h.getLevel().getServer().getRecipeManager().recipeMap().byType(RecipeType.CRAFTING).stream().anyMatch(r -> r.value().matches(input, h.getLevel()) && r.value().assemble(input).is(RotaryBlocks.PILEDRIVER.get().asItem())), "exact PGP/gFg/PDP source recipe with tungsten core and iron drill"); h.succeed();
    }
    private static void harvest(GameTestHelper h) {
        var tile = machine(h); var player = (ServerPlayer)h.makeMockServerPlayer(GameType.SURVIVAL); player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_PICKAXE));
        h.assertTrue(player.gameMode.destroyBlock(tile.getBlockPos()) && h.getEntities(EntityTypes.ITEM, POS, 3).stream().filter(i -> i.getItem().is(RotaryBlocks.PILEDRIVER.get().asItem())).mapToInt(i -> i.getItem().getCount()).sum() == 1, "survival harvesting drops one machine"); h.succeed();
    }
}
