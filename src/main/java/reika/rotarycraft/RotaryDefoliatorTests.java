package reika.rotarycraft;

import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.*;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.material.*;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.level.BlockEntityDefoliator;
import reika.rotarycraft.blockentities.piping.BlockEntityPipe;
import reika.rotarycraft.blockentities.transmission.BlockEntityCreativeCoil;
import reika.rotarycraft.data.RoCBlockTagsProvider;
import reika.rotarycraft.gui.container.machine.inventory.ContainerDefoliator;
import reika.rotarycraft.registry.*;

/** V33a behavior exercised against native 26.3 blocks, loot, transactions and automation. */
final class RotaryDefoliatorTests {
    private static final BlockPos POS = new BlockPos(4, 3, 4), TARGET = new BlockPos(6, 3, 4);
    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> env) {
        for (Direction side : Direction.values()) {
            test(event, env, "power_" + side.getName(), h -> power(h, side));
            test(event, env, "fluid_" + side.getName(), h -> fluid(h, side));
            test(event, env, "pipe_" + side.getName(), h -> pipe(h, side));
        }
        test(event, env, "cadence_and_range", RotaryDefoliatorTests::cadence);
        test(event, env, "potion_identity", RotaryDefoliatorTests::identity);
        test(event, env, "potion_conversion_unpowered", h -> potion(h, 0, 0, Items.GLASS_BOTTLE, true));
        test(event, env, "potion_exact_capacity", h -> potion(h, 3000, 63, Items.GLASS_BOTTLE, true));
        test(event, env, "potion_partial_room", h -> potion(h, 3001, 0, Items.GLASS_BOTTLE, false));
        test(event, env, "potion_full_tank", h -> potion(h, 4000, 0, Items.GLASS_BOTTLE, false));
        test(event, env, "potion_full_bottles", h -> potion(h, 0, 64, Items.GLASS_BOTTLE, false));
        test(event, env, "potion_wrong_output", h -> potion(h, 0, 1, Items.STONE, false));
        test(event, env, "chlorine_and_mixing", RotaryDefoliatorTests::mixing);
        test(event, env, "item_transactions", RotaryDefoliatorTests::items);
        test(event, env, "hopper_input", h -> hopper(h, true));
        test(event, env, "hopper_output", h -> hopper(h, false));
        test(event, env, "hopper_potion_to_bottle", RotaryDefoliatorTests::hopperConversion);
        for (Block block : new Block[]{Blocks.OAK_LOG, Blocks.OAK_LEAVES, Blocks.OAK_SAPLING, Blocks.DANDELION,
                Blocks.VINE, Blocks.CACTUS, Blocks.WHEAT, Blocks.RED_MUSHROOM, Blocks.SUGAR_CANE, Blocks.BAMBOO})
            test(event, env, "decay_" + BuiltInRegistries.BLOCK.getKey(block).getPath(), h -> vegetation(h, block));
        for (Block block : new Block[]{Blocks.STONE, Blocks.GLASS, Blocks.WATER, Blocks.AIR})
            test(event, env, "reject_" + BuiltInRegistries.BLOCK.getKey(block).getPath(), h -> reject(h, block));
        test(event, env, "poison_effect_and_damage", RotaryDefoliatorTests::effects);
        test(event, env, "protection_cancellation", h -> protection(h, false));
        test(event, env, "protection_state_change", h -> protection(h, true));
        test(event, env, "protection_owner_identity", RotaryDefoliatorTests::owner);
        test(event, env, "empty_tank", RotaryDefoliatorTests::empty);
        test(event, env, "detached_save_and_capabilities", RotaryDefoliatorTests::save);
        test(event, env, "menu_and_sync", RotaryDefoliatorTests::menu);
        test(event, env, "menu_open_from_survival", RotaryDefoliatorTests::open);
        test(event, env, "client_menu_fluid_sync", RotaryDefoliatorTests::sync);
        test(event, env, "player_poison", RotaryDefoliatorTests::playerEffect);
        test(event, env, "effect_packet_round_trip", RotaryDefoliatorTests::packet);
        test(event, env, "original_recipe", RotaryDefoliatorTests::recipe);
        test(event, env, "survival_harvest_and_inventory", RotaryDefoliatorTests::harvest);
        test(event, env, "any_removal_inventory", RotaryDefoliatorTests::removal);
        test(event, env, "live_powered_decay", RotaryDefoliatorTests::live);
    }
    private static void test(RegisterGameTestsEvent e, Holder<TestEnvironmentDefinition<?>> env, String name, Consumer<GameTestHelper> body) {
        RotaryGameTests.register(e, env, "defoliator_" + name, 100, body);
    }
    private static BlockEntityDefoliator machine(GameTestHelper h) {
        h.setBlock(POS, RotaryBlocks.DEFOLIATOR.get()); return h.getBlockEntity(POS, BlockEntityDefoliator.class);
    }
    private static ItemStack poison() {
        var stack = new ItemStack(Items.POTION); stack.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.POISON)); return stack;
    }
    private static void fill(BlockEntityDefoliator tile, Fluid fluid, int amount) {
        try (var tx = Transaction.openRoot()) {
            int filled = tile.getFluidHandler(Direction.NORTH).insert(FluidResource.of(fluid), amount, tx);
            if (filled != amount) throw new IllegalStateException("chemical fill: " + filled + "/" + amount);
            tx.commit();
        }
    }
    private static BlockEntityCreativeCoil coil(GameTestHelper h, Direction side, int torque, int speed) {
        BlockPos pos = POS.relative(side); h.setBlock(pos.relative(side), Blocks.REDSTONE_BLOCK);
        h.setBlock(pos, RotaryBlocks.CREATIVE_COIL.get().defaultBlockState().setValue(BlockRotaryCraftMachine.FACING, side.getAxis().isVertical() ? side : side.getOpposite()));
        var coil = h.getBlockEntity(pos, BlockEntityCreativeCoil.class); coil.setReleaseTorque(torque); coil.setReleaseOmega(speed); coil.updateEntity(h.getLevel(), coil.getBlockPos()); return coil;
    }
    private static void power(GameTestHelper h, Direction side) {
        var tile = machine(h); coil(h, side, 128, 128); tile.updateEntity(h.getLevel(), tile.getBlockPos());
        h.assertTrue(tile.MINPOWER == 16384 && (side == Direction.DOWN ? tile.power == 16384 && tile.torque == 128 && tile.omega == 128 : tile.power == 0), "only the bottom shaft supplies the original 16384 W threshold"); h.succeed();
    }
    private static void cadence(GameTestHelper h) {
        var tile = machine(h); tile.power = 16383; tile.omega = 256;
        h.assertTrue(tile.getNumberPasses() == 0, "below minimum power suppresses all probes"); tile.power = 16384;
        h.assertTrue(tile.getNumberPasses() == 32, "two times truncated square root of angular speed"); tile.omega = Integer.MAX_VALUE;
        h.assertTrue(tile.getNumberPasses() == 92680, "original high-speed probing must not be arbitrarily capped");
        int[] torques = {0, 1, 2, 256, 65536, Integer.MAX_VALUE}, ranges = {0, 0, 8, 64, 128, 128};
        for (int n = 0; n < torques.length; n++) { tile.torque = torques[n]; h.assertTrue(tile.getRange() == ranges[n], "logarithmic capped torque range: " + torques[n]); }
        h.succeed();
    }
    private static void identity(GameTestHelper h) {
        var tile = machine(h); h.assertTrue(tile.isItemValidForSlot(0, poison()) && tile.isItemValidForSlot(1, poison()), "source accepts base poison in either slot, while conversion reads slot zero");
        var custom = poison(); custom.set(DataComponents.POTION_CONTENTS, new PotionContents(java.util.Optional.of(Potions.POISON), java.util.Optional.empty(),
                List.of(new net.minecraft.world.effect.MobEffectInstance(MobEffects.SLOWNESS, 100, 2)), java.util.Optional.empty()));
        h.assertTrue(tile.isItemValidForSlot(0, custom), "custom potion effects do not change base metadata eligibility");
        for (var potion : List.of(Potions.LONG_POISON, Potions.STRONG_POISON, Potions.WATER, Potions.HARMING)) {
            var stack = poison(); stack.set(DataComponents.POTION_CONTENTS, new PotionContents(potion));
            h.assertTrue(!tile.isItemValidForSlot(0, stack), "non-base potion rejected: " + potion);
        }
        for (Item item : new Item[]{Items.SPLASH_POTION, Items.LINGERING_POTION, Items.GLASS_BOTTLE, Items.STONE}) {
            var stack = new ItemStack(item); stack.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.POISON)); h.assertTrue(!tile.isItemValidForSlot(0, stack), "wrong vessel rejected");
        }
        h.succeed();
    }
    private static void potion(GameTestHelper h, int chemical, int bottles, Item output, boolean expected) {
        var tile = machine(h); fill(tile, RotaryFluids.POISON.get(), chemical); var input = poison(); input.set(DataComponents.CUSTOM_NAME, Component.literal("Chemical"));
        tile.setInventorySlotContents(0, input); tile.setInventorySlotContents(1, new ItemStack(output, bottles));
        h.assertTrue(tile.consumePotions() == expected && tile.getLiquidLevel() == chemical + (expected ? 1000 : 0), "conversion reserves 1000 mB and bottle room together");
        h.assertTrue(expected ? tile.getStackInSlot(0).isEmpty() && tile.getStackInSlot(1).is(Items.GLASS_BOTTLE) && tile.getStackInSlot(1).getCount() == bottles + 1
                : ItemStack.matches(tile.getStackInSlot(0), input) && tile.getStackInSlot(1).getCount() == bottles, "blocked output must preserve input and both inventories");
        h.assertTrue(tile.power == 0, "potions do not require mechanical input"); h.succeed();
    }
    private static void mixing(GameTestHelper h) {
        var tile = machine(h); fill(tile, RotaryFluids.CHLORINE.get(), 1000); tile.setInventorySlotContents(0, poison());
        h.assertTrue(!tile.consumePotions() && tile.getLiquidLevel() == 1000 && tile.getContainedFluid().is(RotaryFluids.CHLORINE.get()), "poison potion cannot overwrite chlorine");
        h.setBlock(TARGET, Blocks.OAK_LOG); h.assertTrue(tile.decay(h.absolutePos(TARGET)) && tile.getLiquidLevel() == 999, "chlorine powers the same decay operation");
        try (var tx = Transaction.openRoot()) { h.assertTrue(tile.getFluidHandler(Direction.EAST).insert(FluidResource.of(RotaryFluids.POISON.get()), 1000, tx) == 0, "chemicals never mix"); }
        h.succeed();
    }
    private static void fluid(GameTestHelper h, Direction side) {
        var tile = machine(h); var handler = h.getLevel().getCapability(Capabilities.Fluid.BLOCK, tile.getBlockPos(), side);
        if (!side.getAxis().isHorizontal()) { h.assertTrue(handler == null, "vertical intake has no capability"); h.succeed(); return; }
        h.assertTrue(handler != null, "horizontal fluid capability");
        try (var tx = Transaction.openRoot()) { h.assertTrue(handler.insert(FluidResource.of(RotaryFluids.POISON.get()), 4500, tx) == 4000, "capacity four buckets"); }
        h.assertTrue(tile.getLiquidLevel() == 0, "aborted fill rolls back");
        try (var tx = Transaction.openRoot()) { handler.insert(FluidResource.of(RotaryFluids.CHLORINE.get()), 4000, tx); tx.commit(); }
        try (var tx = Transaction.openRoot()) {
            h.assertTrue(handler.extract(FluidResource.of(RotaryFluids.CHLORINE.get()), 1, tx) == 0 && handler.extract(0, FluidResource.of(RotaryFluids.CHLORINE.get()), 1, tx) == 0, "no fluid output, in bulk or by slot");
            h.assertTrue(handler.insert(FluidResource.of(Fluids.WATER), 1, tx) == 0 && handler.insert(FluidResource.of(RotaryFluids.POISON.get()), 1, tx) == 0, "invalid/mixed liquids rejected");
        }
        h.assertTrue(tile.getLiquidLevel() == 4000, "chemical is preserved after rejected transfer"); h.succeed();
    }
    private static void items(GameTestHelper h) {
        var tile = machine(h); var handler = h.getLevel().getCapability(Capabilities.Item.BLOCK, tile.getBlockPos(), Direction.DOWN);
        var input = poison(); input.set(DataComponents.CUSTOM_NAME, Component.literal("Named poison")); var resource = ItemResource.of(input);
        h.assertTrue(handler != null && handler.size() == 2, "two native resource slots exposed");
        try (var tx = Transaction.openRoot()) { h.assertTrue(handler.insert(resource, 1, tx) == 1 && handler.insert(ItemResource.of(Items.STONE), 1, tx) == 0 && handler.insert(1, resource, 1, tx) == 1, "source allows compatible potion insertion into both slots"); }
        h.assertTrue(tile.getStackInSlot(0).isEmpty(), "item insertion rolls back");
        try (var tx = Transaction.openRoot()) { handler.insert(resource, 1, tx); tx.commit(); }
        tile.setInventorySlotContents(1, new ItemStack(Items.GLASS_BOTTLE, 3));
        try (var tx = Transaction.openRoot()) { h.assertTrue(handler.extract(resource, 1, tx) == 0 && handler.extract(0, resource, 1, tx) == 0 && handler.extract(ItemResource.of(Items.GLASS_BOTTLE), 2, tx) == 2, "automation returns bottles but preserves potion input"); }
        h.assertTrue(tile.getStackInSlot(1).getCount() == 3 && ItemStack.matches(input, tile.getStackInSlot(0)), "rollback retains bottle counts and potion components");
        try (var tx = Transaction.openRoot()) { handler.extract(ItemResource.of(Items.GLASS_BOTTLE), 2, tx); tx.commit(); }
        h.assertTrue(tile.getStackInSlot(1).getCount() == 1 && tile.decrStackSize(0, 1).getHoverName().getString().equals("Named poison"), "manual extraction remains possible");
        tile.setInventorySlotContents(0, new ItemStack(Items.GLASS_BOTTLE, 3));
        try (var tx = Transaction.openRoot()) { h.assertTrue(handler.extract(0, ItemResource.of(Items.GLASS_BOTTLE), 1, tx) == 1, "source permits returning a misplaced bottle from either slot"); tx.commit(); }
        h.assertTrue(tile.getStackInSlot(0).getCount() == 2, "indexed bottle extraction commits"); h.succeed();
    }
    private static void hopper(GameTestHelper h, boolean input) {
        var tile = machine(h); fill(tile, RotaryFluids.CHLORINE.get(), 1); // prevent automatic potion conversion
        tile.setInventorySlotContents(0, input ? ItemStack.EMPTY : poison());
        tile.setInventorySlotContents(1, input ? ItemStack.EMPTY : new ItemStack(Items.GLASS_BOTTLE, 3));
        var pos = input ? POS.above() : POS.below(); h.setBlock(pos, Blocks.HOPPER); var hopper = h.getBlockEntity(pos, HopperBlockEntity.class);
        if (input) hopper.setItem(0, poison());
        h.runAfterDelay(32, () -> {
            h.assertTrue(input ? tile.getStackInSlot(0).is(Items.POTION) && hopper.isEmpty()
                    : tile.getStackInSlot(1).isEmpty() && tile.getStackInSlot(0).is(Items.POTION) && hopper.getItem(0).is(Items.GLASS_BOTTLE) && hopper.getItem(0).getCount() == 3,
                    "actual hopper uses insertion/extraction restrictions"); h.succeed();
        });
    }
    private static void hopperConversion(GameTestHelper h) {
        var tile = machine(h); h.setBlock(POS.above(), Blocks.HOPPER); h.setBlock(POS.below(), Blocks.HOPPER);
        var input = h.getBlockEntity(POS.above(), HopperBlockEntity.class); var output = h.getBlockEntity(POS.below(), HopperBlockEntity.class); input.setItem(0, poison());
        h.runAfterDelay(32, () -> {
            h.assertTrue(input.isEmpty() && tile.getStackInSlot(0).isEmpty() && tile.getStackInSlot(1).isEmpty()
                    && tile.power == 0 && tile.getLiquidLevel() == 1000 && output.getItem(0).is(Items.GLASS_BOTTLE) && output.getItem(0).getCount() == 1,
                    "unpowered server ticks turn real hopper input into chemical and hopper bottle output"); h.succeed();
        });
    }
    private static void pipe(GameTestHelper h, Direction side) {
        var tile = machine(h); var pos = POS.relative(side); h.setBlock(pos, RotaryBlocks.FLUID_PIPE.get()); var pipe = h.getBlockEntity(pos, BlockEntityPipe.class);
        try (var tx = Transaction.openRoot()) { h.assertTrue(pipe.getFluidHandler(null).insert(FluidResource.of(RotaryFluids.POISON.get()), 2000, tx) == 2000, "standard pipe accepts poison"); tx.commit(); }
        for (int tick = 0; tick < 8; tick++) pipe.updateEntity(h.getLevel(), pipe.getBlockPos());
        h.assertTrue(side.getAxis().isHorizontal() ? tile.getLiquidLevel() > 0 : tile.getLiquidLevel() == 0, "standard pipe respects side intake: " + side); h.succeed();
    }
    private static void vegetation(GameTestHelper h, Block block) {
        var tile = machine(h); fill(tile, RotaryFluids.POISON.get(), 1); h.setBlock(TARGET.below(), block == Blocks.WHEAT ? Blocks.FARMLAND : Blocks.DIRT); h.setBlock(TARGET, block);
        h.assertTrue(h.getBlockState(TARGET).is(RoCBlockTagsProvider.DEFOLIATOR_TARGETS) && tile.decay(h.absolutePos(TARGET)) && h.getBlockState(TARGET).isAir() && tile.getLiquidLevel() == 0, "eligible vegetation consumes exactly 1 mB: " + block);
        if (block == Blocks.OAK_LOG || block == Blocks.CACTUS) h.assertTrue(h.getEntities(EntityTypes.ITEM, TARGET, 2).stream().anyMatch(e -> e.getItem().is(block.asItem())), "native fortune-zero loot preserved"); h.succeed();
    }
    private static void reject(GameTestHelper h, Block block) {
        var tile = machine(h); fill(tile, RotaryFluids.POISON.get(), 17); h.setBlock(TARGET, block);
        h.assertTrue(!tile.decay(h.absolutePos(TARGET)) && tile.getLiquidLevel() == 17 && h.getBlockState(TARGET).is(block), "ineligible blocks leave chemical and world intact"); h.succeed();
    }
    private static void empty(GameTestHelper h) {
        var tile = machine(h); h.setBlock(TARGET, Blocks.OAK_LOG); h.assertTrue(!tile.decay(h.absolutePos(TARGET)) && h.getBlockState(TARGET).is(Blocks.OAK_LOG), "no operation with an empty tank");
        fill(tile, RotaryFluids.POISON.get(), 1); h.assertTrue(tile.decay(h.absolutePos(TARGET)), "last mB pays one operation"); h.setBlock(TARGET, Blocks.OAK_LOG);
        h.assertTrue(!tile.decay(h.absolutePos(TARGET)), "last mB cannot pay twice"); h.succeed();
    }
    private static void effects(GameTestHelper h) {
        var tile = machine(h); fill(tile, RotaryFluids.POISON.get(), 1); h.setBlock(TARGET, Blocks.OAK_LOG);
        var cow = EntityTypes.COW.create(h.getLevel(), EntitySpawnReason.COMMAND); cow.setNoAi(true); cow.setPos(Vec3.atCenterOf(h.absolutePos(TARGET))); h.getLevel().addFreshEntity(cow);
        var far = EntityTypes.COW.create(h.getLevel(), EntitySpawnReason.COMMAND); far.setNoAi(true); far.setPos(Vec3.atCenterOf(h.absolutePos(TARGET)).add(5, 0, 0)); h.getLevel().addFreshEntity(far);
        float health = cow.getHealth(); h.assertTrue(tile.decay(h.absolutePos(TARGET)), "eligible decay occurs");
        h.assertTrue(cow.hasEffect(MobEffects.POISON) && cow.getEffect(MobEffects.POISON).getDuration() == 50 && cow.getEffect(MobEffects.POISON).getAmplifier() == 3 && cow.getHealth() == health - .5F && !far.hasEffect(MobEffects.POISON), "three-block cube gives Poison IV for 50 ticks and half-heart-unit damage"); h.succeed();
    }
    private static void protection(GameTestHelper h, boolean mutate) {
        var tile = machine(h); fill(tile, RotaryFluids.POISON.get(), 13); h.setBlock(TARGET, Blocks.OAK_LOG); boolean[] called = {false};
        Consumer<BreakBlockEvent> listener = event -> { if (event.getLevel() == h.getLevel() && event.getPos().equals(h.absolutePos(TARGET))) { called[0] = true; if (mutate) h.setBlock(TARGET, Blocks.STONE); else event.setCanceled(true); } };
        NeoForge.EVENT_BUS.addListener(listener);
        try { h.assertTrue(!tile.decay(h.absolutePos(TARGET)) && called[0] && tile.getLiquidLevel() == 13 && h.getBlockState(TARGET).is(mutate ? Blocks.STONE : Blocks.OAK_LOG), "cancellation or event replacement prevents stale loot and chemical consumption"); }
        finally { NeoForge.EVENT_BUS.unregister(listener); }
        h.succeed();
    }
    private static void owner(GameTestHelper h) {
        var tile = machine(h); var player = h.makeMockServerPlayer(GameType.SURVIVAL); tile.setPlacer(player); fill(tile, RotaryFluids.POISON.get(), 1); h.setBlock(TARGET, Blocks.OAK_LOG);
        boolean[] called = {false}; Consumer<BreakBlockEvent> listener = event -> {
            if (event.getLevel() == h.getLevel() && event.getPos().equals(h.absolutePos(TARGET))) {
                called[0] = event.getPlayer().getUUID().equals(player.getUUID()) && event.getPlayer().getName().getString().equals(player.getName().getString()); event.setCanceled(true);
            }
        };
        NeoForge.EVENT_BUS.addListener(listener);
        try { h.assertTrue(!tile.decay(h.absolutePos(TARGET)) && called[0] && tile.getLiquidLevel() == 1, "offline placer identity reaches the protection event"); }
        finally { NeoForge.EVENT_BUS.unregister(listener); }
        h.succeed();
    }
    private static void save(GameTestHelper h) {
        var tile = machine(h); fill(tile, RotaryFluids.CHLORINE.get(), 2789); var input = poison(); input.set(DataComponents.CUSTOM_NAME, Component.literal("Saved potion")); tile.setInventorySlotContents(0, input); tile.setInventorySlotContents(1, new ItemStack(Items.GLASS_BOTTLE, 27));
        var saved = tile.saveWithoutMetadata(h.getLevel().registryAccess()); var restored = new BlockEntityDefoliator(tile.getBlockPos(), tile.getBlockState()); var items = restored.getItemHandler(); var fluid = restored.getFluidHandler(Direction.NORTH); var automation = restored.getAutomationItemHandler();
        restored.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, h.getLevel().registryAccess(), saved));
        h.assertTrue(restored.getLevel() == null && ItemStack.matches(input, restored.getStackInSlot(0)) && restored.getStackInSlot(1).getCount() == 27 && restored.getLiquidLevel() == 2789 && restored.getContainedFluid().is(RotaryFluids.CHLORINE.get()), "registry-aware detached load restores both slots and chemical");
        try (var tx = Transaction.openRoot()) { h.assertTrue(fluid.insert(FluidResource.of(RotaryFluids.CHLORINE.get()), 1, tx) == 1 && automation.extract(ItemResource.of(Items.GLASS_BOTTLE), 1, tx) == 1, "previously obtained capabilities bind restored state"); tx.commit(); }
        h.assertTrue(items == restored.getItemHandler() && restored.getLiquidLevel() == 2790 && restored.getStackInSlot(1).getCount() == 26, "handler identity survives reload"); h.succeed();
    }
    private static void menu(GameTestHelper h) {
        var tile = machine(h); var player = h.makeMockServerPlayer(GameType.SURVIVAL); player.setPos(Vec3.atCenterOf(tile.getBlockPos())); var menu = new ContainerDefoliator(1, player.getInventory(), tile);
        h.assertTrue(menu.slots.size() == 38 && menu.stillValid(player) && MachineRegistry.DEFOLIATOR.hasGui(), "two plus thirty-six usable inventory slots");
        player.getInventory().setItem(9, poison()); menu.quickMoveStack(player, 2); h.assertTrue(tile.getStackInSlot(0).is(Items.POTION) && player.getInventory().getItem(9).isEmpty(), "shift-click poison into input");
        menu.quickMoveStack(player, 0); h.assertTrue(tile.getStackInSlot(0).isEmpty(), "manual shift-click extracts potion"); player.getInventory().setItem(9, new ItemStack(Items.STONE)); h.assertTrue(menu.quickMoveStack(player, 2).isEmpty(), "wrong input cannot shift-click");
        long power = 0x123456789ABCDEFL; for (int i = 0; i < 4; i++) menu.setData(i + 1, (int)(power >>> (i * 16)) & 65535);
        h.assertTrue(tile.power == power, "all sixty-four power bits synchronize"); player.setPos(Vec3.atCenterOf(tile.getBlockPos()).add(20, 0, 0)); h.assertTrue(!menu.stillValid(player), "distant menu becomes inaccessible"); h.succeed();
    }
    private static net.minecraft.server.level.ServerPlayer connectedPlayer(GameTestHelper h) {
        // The deprecated helper hardcodes gameMode() to creative even after setGameMode().
        var player = (net.minecraft.server.level.ServerPlayer)h.makeMockServerPlayer(GameType.SURVIVAL);
        var cookie = net.minecraft.server.network.CommonListenerCookie.createInitial(player.getGameProfile(), false);
        var connection = new net.minecraft.network.Connection(net.minecraft.network.protocol.PacketFlow.SERVERBOUND);
        new io.netty.channel.embedded.EmbeddedChannel(connection);
        net.neoforged.neoforge.network.registration.NetworkRegistry.configureMockConnection(connection);
        h.getLevel().getServer().getPlayerList().placeNewPlayer(connection, player, cookie);
        player.setGameMode(GameType.SURVIVAL); player.connection.markClientLoaded(); return player;
    }
    private static void open(GameTestHelper h) {
        var tile = machine(h); var player = connectedPlayer(h);
        player.setPos(Vec3.atCenterOf(tile.getBlockPos()));
        player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        var hit = new net.minecraft.world.phys.BlockHitResult(Vec3.atCenterOf(tile.getBlockPos()), Direction.UP, tile.getBlockPos(), false);
        player.gameMode.useItemOn(player, h.getLevel(), ItemStack.EMPTY, InteractionHand.MAIN_HAND, hit);
        h.assertTrue(player.containerMenu instanceof ContainerDefoliator, "survival right-click opens the registered screen menu"); player.closeContainer(); h.getLevel().getServer().getPlayerList().remove(player); h.succeed();
    }
    private static void sync(GameTestHelper h) {
        var tile = machine(h); var player = h.makeMockServerPlayer(GameType.SURVIVAL);
        var detached = new BlockEntityDefoliator(tile.getBlockPos(), tile.getBlockState()); var menu = new ContainerDefoliator(1, player.getInventory(), detached);
        menu.setData(0, 2345); h.assertTrue(menu.getFluidLevel() == 2345, "client cache receives actual chemical level");
        menu.setData(0, 4001); h.assertTrue(menu.getFluidLevel() == 4000, "client chemical cache caps at tank capacity");
        menu.setData(0, -1); h.assertTrue(menu.getFluidLevel() == 0, "invalid negative cache clamps to zero"); h.succeed();
    }
    private static void playerEffect(GameTestHelper h) {
        var tile = machine(h); fill(tile, RotaryFluids.POISON.get(), 1); h.setBlock(TARGET, Blocks.OAK_LOG);
        var player = connectedPlayer(h); player.setPos(Vec3.atCenterOf(h.absolutePos(TARGET))); float health = player.getHealth();
        h.assertTrue(tile.decay(h.absolutePos(TARGET)) && player.hasEffect(MobEffects.POISON) && player.getHealth() == health - .5F, "original poison exposure includes nearby survival players"); h.getLevel().getServer().getPlayerList().remove(player); h.succeed();
    }
    private static void packet(GameTestHelper h) {
        var tile = machine(h); fill(tile, RotaryFluids.POISON.get(), 1); h.setBlock(TARGET, Blocks.OAK_LOG); var player = h.makeMockServerPlayer(GameType.SURVIVAL);
        try {
            var bytes = new java.io.ByteArrayOutputStream(); var out = new java.io.DataOutputStream(bytes); out.writeInt(PacketRegistry.DEFOLIATOR.ordinal());
            for (var pos : List.of(h.absolutePos(TARGET), h.absolutePos(TARGET))) { out.writeInt(pos.getX()); out.writeInt(pos.getY()); out.writeInt(pos.getZ()); }
            var packet = new reika.dragonapi.libraries.io.ReikaPacketHelper.DataPacket(bytes.toByteArray()); packet.init(reika.dragonapi.auxiliary.PacketTypes.DATA, reika.dragonapi.libraries.io.ReikaPacketHelper.getPipeline(RotaryCraft.packetChannel));
            var buffer = new net.minecraft.network.FriendlyByteBuf(io.netty.buffer.Unpooled.buffer());
            try { packet.encode(buffer); var decoded = reika.dragonapi.libraries.io.ReikaPacketHelper.DataPacket.decode(buffer); new PacketHandlerCore().handleData(decoded, h.getLevel(), player); }
            finally { buffer.release(); }
            h.assertTrue(tile.getLiquidLevel() == 1 && h.getBlockState(TARGET).is(Blocks.OAK_LOG), "client effect packet cannot trigger server decay or spend chemical");
        } catch (java.io.IOException error) { throw new IllegalStateException(error); }
        h.succeed();
    }
    private static void recipe(GameTestHelper h) {
        var pipe = new ItemStack(RotaryBlocks.FLUID_PIPE.get()); var steel = new ItemStack(RotaryItems.HSLA_STEEL_INGOT.get()); var plate = new ItemStack(RotaryItems.HSLA_PLATE.get());
        var input = CraftingInput.of(3, 3, List.of(pipe, ItemStack.EMPTY, pipe, steel, pipe, steel, plate, new ItemStack(RotaryItems.IMPELLER.get()), plate));
        h.assertTrue(h.getLevel().getServer().getRecipeManager().recipeMap().byType(RecipeType.CRAFTING).stream().anyMatch(r -> r.value().matches(input, h.getLevel()) && r.value().assemble(input).is(RotaryBlocks.DEFOLIATOR.get().asItem())), "exact V33a P P/SPS/BIB recipe"); h.succeed();
    }
    private static void harvest(GameTestHelper h) {
        var tile = machine(h); tile.setInventorySlotContents(0, poison()); tile.setInventorySlotContents(1, new ItemStack(Items.GLASS_BOTTLE, 3)); var player = (net.minecraft.server.level.ServerPlayer)h.makeMockServerPlayer(GameType.SURVIVAL); player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_PICKAXE));
        h.assertTrue(player.gameMode.destroyBlock(tile.getBlockPos()), "survival iron pickaxe harvests machine");
        var drops = h.getEntities(EntityTypes.ITEM, POS, 3); h.assertTrue(drops.stream().filter(e -> e.getItem().is(RotaryBlocks.DEFOLIATOR.get().asItem())).mapToInt(e -> e.getItem().getCount()).sum() == 1
                && drops.stream().anyMatch(e -> e.getItem().is(Items.POTION)) && drops.stream().filter(e -> e.getItem().is(Items.GLASS_BOTTLE)).mapToInt(e -> e.getItem().getCount()).sum() == 3, "machine and both inventory slots drop exactly once"); h.succeed();
    }
    private static void removal(GameTestHelper h) {
        var tile = machine(h); tile.setInventorySlotContents(0, poison()); h.setBlock(POS, Blocks.AIR);
        h.assertTrue(h.getEntities(EntityTypes.ITEM, POS, 3).stream().filter(e -> e.getItem().is(Items.POTION)).mapToInt(e -> e.getItem().getCount()).sum() == 1 && tile.getStackInSlot(0).isEmpty(), "any removal spills and clears inventory once"); h.succeed();
    }
    private static void live(GameTestHelper h) {
        var tile = machine(h); coil(h, Direction.DOWN, 2, 8192); fill(tile, RotaryFluids.POISON.get(), 2000);
        for (int x = 0; x < 9; x++) for (int y = 1; y < 9; y++) for (int z = 0; z < 9; z++) {
            var pos = h.absolutePos(new BlockPos(x, y, z)); if (pos.equals(tile.getBlockPos()) || pos.equals(tile.getBlockPos().below()) || pos.equals(tile.getBlockPos().below(2))) continue;
            h.getLevel().setBlock(pos, Blocks.OAK_LOG.defaultBlockState(), 2);
        }
        h.getLevel().getRandom().setSeed(0xDEF011A7L);
        tile.updateEntity(h.getLevel(), tile.getBlockPos()); h.assertTrue(tile.power == 16384 && tile.getRange() == 8 && tile.getLiquidLevel() < 2000, "actual minimum-power shaft tick scans and decays vegetation"); h.succeed();
    }
}
