package reika.rotarycraft;

import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.*;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.BlockEntityWinder;
import reika.rotarycraft.blockentities.production.BlockEntityWorktable;
import reika.rotarycraft.blockentities.surveying.BlockEntityMobRadar;
import reika.rotarycraft.blockentities.transmission.BlockEntityCreativeCoil;
import reika.rotarycraft.gui.container.machine.inventory.WinderContainer;
import reika.rotarycraft.items.ItemCoil;
import reika.rotarycraft.items.tools.charged.ItemMotionTracker;
import reika.rotarycraft.registry.*;

/** Real V33a scans, registered radar access and spring -> Winder -> Worktable charging. */
final class RotaryMotionTrackerTests {
    private static final BlockPos POS = new BlockPos(4, 2, 4);
    private RotaryMotionTrackerTests() {}
    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> env) {
        test(event, env, "through_walls_and_off_ray", RotaryMotionTrackerTests::ray);
        RotaryGameTests.register(event, env, "motion_tracker_ordinary_and_boss_range", 6000, RotaryMotionTrackerTests::range);
        test(event, env, "distinct_same_name_contacts", RotaryMotionTrackerTests::distinct);
        test(event, env, "independent_viewers", RotaryMotionTrackerTests::viewers);
        test(event, env, "hostile_attack_warnings", RotaryMotionTrackerTests::attacking);
        test(event, env, "original_colors", RotaryMotionTrackerTests::colors);
        for (boolean creative : new boolean[]{false, true}) test(event, env, "scan_charge_" + creative, h -> use(h, creative));
        test(event, env, "depleted_refuses_scan", RotaryMotionTrackerTests::depleted);
        test(event, env, "registered_owner_hud", RotaryMotionTrackerTests::hud);
        test(event, env, "original_recipe", RotaryMotionTrackerTests::recipe);
        test(event, env, "spring_materials", RotaryMotionTrackerTests::springMaterials);
        test(event, env, "legacy_energy_migration", RotaryMotionTrackerTests::legacy);
        test(event, env, "zero_power_no_charge", RotaryMotionTrackerTests::unpowered);
        for (Direction side : Direction.values()) test(event, env, "winder_face_" + side.getName(), h -> winding(h, side));
        test(event, env, "torque_and_technical_caps", RotaryMotionTrackerTests::caps);
        for (boolean bedrock : new boolean[]{false, true}) test(event, env, "unwind_" + bedrock, h -> unwind(h, bedrock));
        test(event, env, "winder_detached_save", RotaryMotionTrackerTests::saveWinder);
        test(event, env, "winder_transaction_ports", RotaryMotionTrackerTests::ports);
        test(event, env, "winder_menu_transfer", RotaryMotionTrackerTests::menu);
        test(event, env, "winder_harvest_inventory", RotaryMotionTrackerTests::harvest);
        for (boolean bedrock : new boolean[]{false, true}) test(event, env, "worktable_swap_" + bedrock, h -> swap(h, bedrock));
        test(event, env, "worktable_backpressure", h -> blocked(h, false));
        test(event, env, "worktable_extra_item", h -> blocked(h, true));
        test(event, env, "worktable_detached_save", RotaryMotionTrackerTests::saveWorktable);
        test(event, env, "survival_charge_chain", RotaryMotionTrackerTests::chain);
        test(event, env, "spring_machine_real_discharge", h -> smoke(h, false));
        test(event, env, "spring_machine_bedrock_lifetime", h -> smoke(h, true));
        test(event, env, "spring_machine_creative_lifetime", RotaryMotionTrackerTests::creativeLife);
        test(event, env, "winder_wrong_power_face", RotaryMotionTrackerTests::wrongFace);
    }
    private static void test(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> env, String name, Consumer<GameTestHelper> body) {
        RotaryGameTests.register(event, env, "motion_tracker_" + name, 100, body);
    }
    private static ItemMotionTracker tracker() { return (ItemMotionTracker)RotaryItems.MOTION.get(); }
    private static Player viewer(GameTestHelper h, boolean creative) {
        var player = h.makeMockPlayer(creative ? GameType.CREATIVE : GameType.SURVIVAL);
        player.setPos(Vec3.atCenterOf(h.absolutePos(POS))); player.setYRot(0); player.setXRot(0); return player;
    }
    private static <T extends LivingEntity> T spawn(GameTestHelper h, Player viewer, net.minecraft.world.entity.EntityType<T> type, double distance, double lateral) {
        T mob = type.create(h.getLevel(), EntitySpawnReason.COMMAND);
        mob.setPos(viewer.getX() + lateral, viewer.getEyeY() - .1, viewer.getZ() + distance);
        if (mob instanceof Mob ai) ai.setNoAi(true); h.getLevel().addFreshEntity(mob); return mob;
    }
    private static boolean contains(List<ItemMotionTracker.Contact> contacts, LivingEntity mob) { return contacts.stream().anyMatch(c -> c.entityId().equals(mob.getUUID())); }
    private static void ray(GameTestHelper h) {
        var player = viewer(h, false); var inside = spawn(h, player, EntityTypes.COW, 5, 0); var outside = spawn(h, player, EntityTypes.COW, 5, 3);
        h.setBlock(POS.offset(0, 2, 2), Blocks.STONE); h.setBlock(POS.offset(0, 1, 2), Blocks.STONE);
        var contacts = tracker().scan(h.getLevel(), player);
        h.assertTrue(contains(contacts, inside) && !contains(contacts, outside), "half-block ray samples must scan through a solid wall and reject off-ray animals"); h.succeed();
    }
    private static void range(GameTestHelper h) {
        var player = viewer(h, false);
        var chunks = java.util.stream.IntStream.of(30, 35, 70, 135).mapToObj(distance -> ChunkPos.containing(BlockPos.containing(player.getX(), player.getY(), player.getZ() + distance))).distinct().toList();
        var loads = chunks.stream().map(chunk -> h.getLevel().getChunkSource().addTicketAndLoadWithRadius(net.minecraft.server.level.TicketType.FORCED, chunk, 2)).toList();
        h.startSequence().thenWaitUntil(() -> {
            for (var load : loads) h.assertTrue(load.isDone() && !load.isCompletedExceptionally(), "distant chunk neighborhood must finish loading");
            for (var chunk : chunks) h.assertTrue(h.getLevel().areEntitiesActuallyLoadedAndTicking(chunk), "distant scan chunk must finish loading entities");
        }).thenExecute(() -> {
            var near = spawn(h, player, EntityTypes.COW, 30, 0); var far = spawn(h, player, EntityTypes.COW, 35, 0);
            var boss = spawn(h, player, EntityTypes.WITHER, 70, 0); var beyond = spawn(h, player, EntityTypes.WITHER, 135, 0);
            try {
                var contacts = tracker().scan(h.getLevel(), player);
                h.assertTrue(contains(contacts, near) && !contains(contacts, far) && contains(contacts, boss) && !contains(contacts, beyond),
                        "ordinary/boss range: near=" + contains(contacts, near) + ", far=" + contains(contacts, far) + ", boss=" + contains(contacts, boss) + ", beyond=" + contains(contacts, beyond) + ", loadedBoss=" + h.getLevel().getEntitiesOfClass(LivingEntity.class, boss.getBoundingBox()).contains(boss) + ", aliveBoss=" + boss.isAlive() + ", bossBox=" + boss.getBoundingBox());
            } finally {
                near.discard(); far.discard(); boss.discard(); beyond.discard();
                for (var chunk : chunks) h.getLevel().getChunkSource().removeTicketWithRadius(net.minecraft.server.level.TicketType.FORCED, chunk, 2);
            }
        }).thenSucceed();
    }
    private static void distinct(GameTestHelper h) {
        var player = viewer(h, false); var a = spawn(h, player, EntityTypes.COW, 4, 0); var b = spawn(h, player, EntityTypes.COW, 4, .15);
        a.setCustomName(Component.literal("Same name")); b.setCustomName(Component.literal("Same name"));
        var contacts = tracker().scan(h.getLevel(), player);
        h.assertTrue(contacts.stream().filter(c -> c.entityId().equals(a.getUUID())).count() == 1 && contacts.stream().filter(c -> c.entityId().equals(b.getUUID())).count() == 1,
                "one large mob must not repeat per sample and two distinct same-name mobs must both appear"); h.succeed();
    }
    private static void viewers(GameTestHelper h) {
        var a = viewer(h, false); var b = viewer(h, false); b.setYRot(180); var cow = spawn(h, a, EntityTypes.COW, 5, 0);
        h.assertTrue(contains(tracker().scan(h.getLevel(), a), cow) && !contains(tracker().scan(h.getLevel(), b), cow)
                && contains(tracker().scan(h.getLevel(), a), cow), "singleton item must retain no cross-player or previous-scan contact cache"); h.succeed();
    }
    private static void attacking(GameTestHelper h) {
        var player = viewer(h, false); var zombie = spawn(h, player, EntityTypes.ZOMBIE, 5, 0);
        zombie.setTarget(player); h.assertTrue(tracker().scan(h.getLevel(), player).stream().anyMatch(c -> c.entityId().equals(zombie.getUUID()) && c.attacking()), "current hostile target must warn the viewer");
        zombie.setTarget(null); zombie.setLastHurtByMob(player);
        h.assertTrue(tracker().scan(h.getLevel(), player).stream().anyMatch(c -> c.entityId().equals(zombie.getUUID()) && c.attacking()), "revenge target must also warn the viewer");
        zombie.setLastHurtByMob(null); h.assertTrue(tracker().scan(h.getLevel(), player).stream().noneMatch(ItemMotionTracker.Contact::attacking), "untargeted hostile must not produce a warning"); h.succeed();
    }
    private static void colors(GameTestHelper h) {
        var types = List.of(EntityTypes.COW, EntityTypes.BAT, EntityTypes.SQUID, EntityTypes.ZOMBIE, EntityTypes.SLIME, EntityTypes.GHAST, EntityTypes.ENDERMAN, EntityTypes.ZOMBIFIED_PIGLIN, EntityTypes.ENDER_DRAGON, EntityTypes.WITHER);
        var expected = List.of(ChatFormatting.GREEN, ChatFormatting.GREEN, ChatFormatting.GREEN, ChatFormatting.RED, ChatFormatting.RED, ChatFormatting.RED, ChatFormatting.YELLOW, ChatFormatting.YELLOW, ChatFormatting.DARK_PURPLE, ChatFormatting.DARK_GRAY);
        for (int i = 0; i < types.size(); i++) h.assertTrue(ItemMotionTracker.getContactColor((LivingEntity)types.get(i).create(h.getLevel(), EntitySpawnReason.COMMAND)) == expected.get(i), "V33a contact color for " + types.get(i)); h.succeed();
    }
    private static void use(GameTestHelper h, boolean creative) {
        var player = viewer(h, creative); var stack = named(new ItemStack(RotaryItems.MOTION.get()), "Tracker"); stack.setDamageValue(3); player.setItemInHand(InteractionHand.MAIN_HAND, stack);
        var result = tracker().use(h.getLevel(), player, InteractionHand.MAIN_HAND);
        h.assertTrue(result.consumesAction() && stack.getDamageValue() == 2 && stack.getHoverName().getString().equals("Tracker") && !tracker().isBarVisible(stack), "one scan consumes one charge, including creative, while retaining all stack components"); h.succeed();
    }
    private static void depleted(GameTestHelper h) {
        var player = viewer(h, false); var stack = new ItemStack(RotaryItems.MOTION.get()); player.setItemInHand(InteractionHand.MAIN_HAND, stack);
        h.assertTrue(!tracker().use(h.getLevel(), player, InteractionHand.MAIN_HAND).consumesAction() && stack.getDamageValue() == 0, "depleted tracker refuses use without negative charge"); h.succeed();
    }
    private static void hud(GameTestHelper h) {
        h.setBlock(POS, RotaryBlocks.MOB_RADAR.get()); var radar = h.getBlockEntity(POS, BlockEntityMobRadar.class); var owner = viewer(h, false); var other = viewer(h, false); radar.setPlacer(owner);
        h.assertTrue(!radar.canShowHud(owner), "tracker is required"); owner.getInventory().setItem(35, new ItemStack(RotaryItems.MOTION.get()));
        h.assertTrue(radar.canShowHud(owner) && !radar.canShowHud(other), "registered tracker in last main inventory slot unlocks only its owner's HUD, even uncharged as in V33a");
        owner.getInventory().setItem(35, ItemStack.EMPTY); owner.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(RotaryItems.MOTION.get()));
        h.assertTrue(!radar.canShowHud(owner), "offhand is outside the original main-inventory HUD gate"); h.succeed();
    }
    private static void recipe(GameTestHelper h) {
        var steel = new ItemStack(RotaryItems.HSLA_STEEL_INGOT.get());
        var input = CraftingInput.of(3, 3, List.of(ItemStack.EMPTY, new ItemStack(RotaryItems.SONAR_UNIT.get()), new ItemStack(RotaryItems.RADAR_UNIT.get()), steel, new ItemStack(RotaryItems.SCREEN.get()), steel, ItemStack.EMPTY, steel, ItemStack.EMPTY));
        h.assertTrue(h.getLevel().getServer().getRecipeManager().recipeMap().byType(RecipeType.CRAFTING).stream().anyMatch(r -> r.value().matches(input, h.getLevel()) && r.value().assemble(input).is(RotaryItems.MOTION.get())), "V33a sonar/radar/screen/HSLA recipe must craft the tracker"); h.succeed();
    }
    private static ItemStack spring(boolean bedrock, int charge) { var stack = new ItemStack((bedrock ? RotaryItems.BEDROCK_ALLOY_SPRING : RotaryItems.HSLA_STEEL_SPRING).get()); ItemCoil.setCharge(stack, charge); return stack; }
    private static ItemStack named(ItemStack stack, String name) { stack.set(DataComponents.CUSTOM_NAME, Component.literal(name)); var tag = new CompoundTag(); tag.putString("test", "preserved"); stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag)); return stack; }
    private static void springMaterials(GameTestHelper h) {
        var a = spring(false, 32); var b = spring(true, 32); var normal = (ItemCoil)a.getItem(); var bedrock = (ItemCoil)b.getItem();
        h.assertTrue(a.getMaxStackSize() == 1 && a.getDamageValue() == 32 && normal.getStiffness(a) == 1 && normal.getPowerScale(a) == 1 && normal.isBreakable(a)
                && bedrock.getStiffness(b) == 16 && bedrock.getPowerScale(b) == 4 && !bedrock.isBreakable(b) && !bedrock.isBarVisible(b), "original spring material values and per-stack charge must be restored"); h.succeed();
    }
    private static void legacy(GameTestHelper h) {
        var stack = named(spring(false, 0), "Legacy"); var tag = stack.get(DataComponents.CUSTOM_DATA).copyTag(); tag.putInt("energy", 51); stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        h.assertTrue(ItemCoil.getCharge(stack) == 51, "pre-slice energy tag must be readable"); ItemCoil.setCharge(stack, 50);
        h.assertTrue(stack.getDamageValue() == 50 && !stack.get(DataComponents.CUSTOM_DATA).contains("energy") && stack.get(DataComponents.CUSTOM_DATA).copyTag().getStringOr("test", "").equals("preserved"), "charge mutation migrates only energy, retaining other components"); h.succeed();
    }
    private static BlockEntityWinder winder(GameTestHelper h, Direction side) {
        h.setBlock(POS, RotaryBlocks.WINDER.get().defaultBlockState().setValue(BlockRotaryCraftMachine.FACING, side.getOpposite())); return h.getBlockEntity(POS, BlockEntityWinder.class);
    }
    private static void tick(GameTestHelper h, BlockEntityWinder tile, int count) { for (int i = 0; i < count; i++) tile.updateEntity(h.getLevel(), tile.getBlockPos()); }
    private static BlockEntityCreativeCoil coil(GameTestHelper h, Direction side, int torque, int speed) {
        var pos = POS.relative(side); h.setBlock(pos.north().equals(POS) ? pos.east() : pos.north(), Blocks.REDSTONE_BLOCK);
        h.setBlock(pos, RotaryBlocks.CREATIVE_COIL.get().defaultBlockState().setValue(BlockRotaryCraftMachine.FACING, side.getAxis().isVertical() ? side : side.getOpposite()));
        var coil = h.getBlockEntity(pos, BlockEntityCreativeCoil.class); coil.setReleaseTorque(torque); coil.setReleaseOmega(speed); coil.updateEntity(h.getLevel(), coil.getBlockPos()); return coil;
    }
    private static void unpowered(GameTestHelper h) {
        var tile = winder(h, Direction.WEST); tile.setItem(0, spring(true, 0)); tick(h, tile, 100);
        h.assertTrue(ItemCoil.getCharge(tile.getItem(0)) == 0 && tile.power == 0, "an unpowered winder must never create charge"); h.succeed();
    }
    private static void winding(GameTestHelper h, Direction side) {
        var tile = winder(h, side); tile.setItem(0, spring(true, 0)); coil(h, side, 32, 1024); tick(h, tile, 5);
        h.assertTrue(tile.torque == 32 && tile.omega == 1024 && ItemCoil.getCharge(tile.getItem(0)) == 2, "actual shaft on " + side + " must wind to torque/stiffness cap"); h.succeed();
    }
    private static void caps(GameTestHelper h) {
        var tile = winder(h, Direction.WEST); tile.setItem(0, spring(true, 32000)); coil(h, Direction.WEST, 1000000, 1024); tick(h, tile, 10);
        h.assertTrue(tile.getMaxWind() == 32000 && ItemCoil.getCharge(tile.getItem(0)) == 32000, "charge is capped at original technical maximum without integer wrap"); h.succeed();
    }
    private static void unwind(GameTestHelper h, boolean bedrock) {
        var tile = winder(h, Direction.WEST); tile.winding = false; tile.setItem(0, spring(bedrock, 1)); int lifetime = bedrock ? 320 : 20; tick(h, tile, lifetime - 1);
        h.assertTrue(ItemCoil.getCharge(tile.getItem(0)) == 1 && tile.torque == (bedrock ? 32 : 8) && tile.omega == (bedrock ? 4096 : 1024), "unwinding must output original rated power for full stiffness-scaled interval");
        tick(h, tile, 1); h.assertTrue(ItemCoil.getCharge(tile.getItem(0)) == 0, "interval completion must write depleted charge back to slot"); tick(h, tile, 1);
        h.assertTrue(tile.power == 0 && tile.omega == 0 && tile.torque == 0, "depleted coil must stop output"); h.succeed();
    }
    private static void saveWinder(GameTestHelper h) {
        var tile = winder(h, Direction.WEST); tile.winding = false; tile.setItem(0, named(spring(true, 117), "Stored spring")); tick(h, tile, 3);
        var saved = tile.saveWithoutMetadata(h.getLevel().registryAccess()); var restored = new BlockEntityWinder(tile.getBlockPos(), tile.getBlockState()); var handler = restored.itemHandler;
        restored.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, h.getLevel().registryAccess(), saved));
        h.assertTrue(restored.getLevel() == null && restored.itemHandler == handler && handler.getSlots() == 1 && !restored.winding && ItemStack.matches(restored.getItem(0), tile.getItem(0)), "detached save/load must retain mode, exact components and fixed handler identity"); h.succeed();
    }
    private static void ports(GameTestHelper h) {
        var tile = winder(h, Direction.WEST); var stack = spring(true, 13); var resource = ItemResource.of(stack);
        for (Direction side : Direction.values()) {
            var handler = h.getLevel().getCapability(Capabilities.Item.BLOCK, tile.getBlockPos(), side);
            h.assertTrue(handler != null && handler.size() == 1, "real item capability exposes exactly one spring slot");
            try (var tx = Transaction.openRoot()) { h.assertTrue(handler.insert(resource, 2, tx) == 1 && handler.insert(ItemResource.of(new ItemStack(Items.DIAMOND)), 1, tx) == 0, "spring-only capacity must be one"); }
            h.assertTrue(tile.isEmpty(), "aborted insertion must roll back"); tile.setItem(0, stack);
            try (var tx = Transaction.openRoot()) { h.assertTrue(handler.extract(resource, 1, tx) == (side == Direction.DOWN ? 1 : 0), "only bottom automation can extract springs"); }
            h.assertTrue(ItemStack.matches(tile.getItem(0), stack), "aborted extraction must restore exact charged stack"); tile.clearContent();
        }
        var bottom = h.getLevel().getCapability(Capabilities.Item.BLOCK, tile.getBlockPos(), Direction.DOWN);
        try (var tx = Transaction.openRoot()) { bottom.insert(resource, 1, tx); tx.commit(); }
        h.assertTrue(ItemStack.matches(tile.getItem(0), stack), "committed insertion retains charge components"); h.succeed();
    }
    private static void menu(GameTestHelper h) {
        var tile = winder(h, Direction.WEST); var player = h.makeMockServerPlayer(GameType.SURVIVAL); player.setPos(Vec3.atCenterOf(tile.getBlockPos())); var menu = new WinderContainer(1, player.getInventory(), tile);
        h.assertTrue(menu.slots.size() == 37 && menu.stillValid(player), "menu must include its missing machine slot and all 36 player slots");
        player.getInventory().setItem(9, spring(true, 12)); menu.quickMoveStack(player, 1);
        h.assertTrue(ItemCoil.getCharge(tile.getItem(0)) == 12 && player.getInventory().getItem(9).isEmpty(), "shift-click must transfer actual spring charge into winder");
        menu.quickMoveStack(player, 0); h.assertTrue(tile.isEmpty() && java.util.stream.IntStream.range(0, 36).anyMatch(i -> player.getInventory().getItem(i).is(RotaryItems.BEDROCK_ALLOY_SPRING.get()) && ItemCoil.getCharge(player.getInventory().getItem(i)) == 12), "shift-click must return spring to main inventory");
        player.getInventory().setItem(9, new ItemStack(Items.DIAMOND)); menu.quickMoveStack(player, 1); h.assertTrue(tile.isEmpty(), "invalid item is refused by manual slot");
        player.setPos(player.getX() + 20, player.getY(), player.getZ()); h.assertTrue(!menu.stillValid(player), "remote access must fail"); h.succeed();
    }
    private static void harvest(GameTestHelper h) {
        var tile = winder(h, Direction.WEST); tile.setItem(0, named(spring(true, 91), "Dropped spring"));
        var player = (net.minecraft.server.level.ServerPlayer)h.makeMockServerPlayer(GameType.SURVIVAL); player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_PICKAXE));
        h.assertTrue(player.gameMode.destroyBlock(tile.getBlockPos()), "survival pickaxe must harvest Winder");
        var drops = h.getEntities(EntityTypes.ITEM, POS, 3);
        h.assertTrue(drops.stream().filter(e -> e.getItem().is(RotaryBlocks.WINDER.get().asItem())).mapToInt(e -> e.getItem().getCount()).sum() == 1
                && drops.stream().filter(e -> e.getItem().is(RotaryItems.BEDROCK_ALLOY_SPRING.get()) && ItemCoil.getCharge(e.getItem()) == 91).mapToInt(e -> e.getItem().getCount()).sum() == 1, "survival harvest drops exactly one machine and one charged spring"); h.succeed();
    }
    private static BlockEntityWorktable table(GameTestHelper h) { h.setBlock(POS, RotaryBlocks.WORKTABLE.get()); return h.getBlockEntity(POS, BlockEntityWorktable.class); }
    private static void tableTick(GameTestHelper h, BlockEntityWorktable tile) { for (int i = 0; i < 4; i++) tile.updateEntity(h.getLevel(), tile.getBlockPos()); }
    private static void swap(GameTestHelper h, boolean bedrock) {
        var tile = table(h); var tool = named(new ItemStack(RotaryItems.MOTION.get()), "Charged tracker"); tool.setDamageValue(7); var coil = named(spring(bedrock, 91), "Charged spring");
        tile.setInventorySlotContents(0, tool); tile.setInventorySlotContents(8, coil); tableTick(h, tile);
        tool.setDamageValue(91); ItemCoil.setCharge(coil, 7);
        h.assertTrue(tile.getItem(0).isEmpty() && tile.getItem(8).isEmpty() && ItemStack.matches(tile.getItem(9), tool) && ItemStack.matches(tile.getItem(10), coil), "Worktable swaps exact original charge in both directions and preserves names and custom data"); h.succeed();
    }
    private static void blocked(GameTestHelper h, boolean extra) {
        var tile = table(h); tile.setInventorySlotContents(0, new ItemStack(RotaryItems.MOTION.get())); tile.setInventorySlotContents(8, spring(true, 91)); tile.setInventorySlotContents(extra ? 4 : 9, new ItemStack(Items.DIAMOND)); tableTick(h, tile);
        h.assertTrue(tile.getItem(0).is(RotaryItems.MOTION.get()) && ItemCoil.getCharge(tile.getItem(8)) == 91 && tile.getItem(extra ? 4 : 9).is(Items.DIAMOND), "extra input or occupied output must preserve all original items and charges"); h.succeed();
    }
    private static void saveWorktable(GameTestHelper h) {
        var tile = table(h); tile.setInventorySlotContents(0, named(new ItemStack(RotaryItems.MOTION.get()), "Saved tracker")); tile.setInventorySlotContents(8, spring(true, 32000));
        var saved = tile.saveWithoutMetadata(h.getLevel().registryAccess()); var restored = new BlockEntityWorktable(tile.getBlockPos(), tile.getBlockState()); var handler = restored.getItemHandler();
        restored.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, h.getLevel().registryAccess(), saved));
        h.assertTrue(restored.getLevel() == null && restored.getItemHandler() == handler && handler.getSlots() == 19 && ItemStack.matches(restored.getItem(0), tile.getItem(0)) && ItemCoil.getCharge(restored.getItem(8)) == 32000, "detached Worktable save restores real registry-backed items through stable 19-slot handler"); h.succeed();
    }
    private static void chain(GameTestHelper h) {
        var winder = winder(h, Direction.WEST); winder.setItem(0, spring(true, 0)); coil(h, Direction.WEST, 64, 1024); tick(h, winder, 4); var wound = winder.removeItem(0, 1);
        h.assertTrue(ItemCoil.getCharge(wound) == 4, "powered Winder must supply real spring charge"); var table = table(h); table.setInventorySlotContents(0, new ItemStack(RotaryItems.MOTION.get())); table.setInventorySlotContents(1, wound); tableTick(h, table);
        var player = viewer(h, false); player.setItemInHand(InteractionHand.MAIN_HAND, table.removeItem(9, 1)); var cow = spawn(h, player, EntityTypes.COW, 4, 0);
        h.assertTrue(contains(tracker().scan(h.getLevel(), player), cow), "charged tool must find live contact"); tracker().use(h.getLevel(), player, InteractionHand.MAIN_HAND);
        h.assertTrue(player.getMainHandItem().getDamageValue() == 3 && ItemCoil.getCharge(table.getItem(10)) == 0, "Winder -> Worktable -> scan must consume exactly one of the four earned charge units"); h.succeed();
    }
    private static void smoke(GameTestHelper h, boolean bedrock) {
        h.setBlock(POS, RotaryBlocks.SMOKE_DETECTOR.get()); var tile = h.getBlockEntity(POS, reika.rotarycraft.blockentities.BlockEntitySmokeDetector.class);
        tile.setItem(0, spring(bedrock, 10)); int interval = bedrock ? 19200 : 1200;
        h.assertTrue(tile.getRange() == 6 && !tile.lowBattery() && tile.getExpectedCoilLife() == interval * 10, "range, warning and lifetime must read actual spring charge");
        var saved = tile.saveWithoutMetadata(h.getLevel().registryAccess()); saved.putInt("unwindTick", interval - 1);
        tile.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, h.getLevel().registryAccess(), saved)); tile.updateEntity(h.getLevel(), tile.getBlockPos());
        h.assertTrue(ItemCoil.getCharge(tile.getItem(0)) == 9, "spring-powered machines must actually discharge at material-scaled interval");
        tile.setItem(0, spring(bedrock, 8)); h.assertTrue(tile.lowBattery(), "eight-charge low battery boundary is inclusive");
        tile.setItem(0, spring(bedrock, 0)); h.assertTrue(!tile.checkValidCoil() && tile.getRange() == 0 && tile.getExpectedCoilLife() == 0, "depletion stops spring-powered operation"); h.succeed();
    }
    private static void creativeLife(GameTestHelper h) {
        h.setBlock(POS, RotaryBlocks.SMOKE_DETECTOR.get()); var tile = h.getBlockEntity(POS, reika.rotarycraft.blockentities.BlockEntitySmokeDetector.class); tile.isCreative = true;
        h.assertTrue(tile.getExpectedCoilLife() == Integer.MAX_VALUE, "creative lifetime must be bounded without overflow or an absent-spring cast"); h.succeed();
    }
    private static void wrongFace(GameTestHelper h) {
        var tile = winder(h, Direction.WEST); tile.setItem(0, spring(true, 0)); coil(h, Direction.EAST, 64, 1024); tick(h, tile, 10);
        h.assertTrue(tile.power == 0 && ItemCoil.getCharge(tile.getItem(0)) == 0, "Winder must read only its configured input face"); h.succeed();
    }
}
