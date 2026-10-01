package reika.rotarycraft;

import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.*;
import net.minecraft.gametest.framework.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.*;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.phys.*;
import net.minecraft.util.ProblemReporter;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.neoforged.neoforge.transfer.*;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import reika.dragonapi.modinteract.AtmosphereHandler;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.auxiliary.BlockEntityEngineController;
import reika.rotarycraft.blockentities.transmission.BlockEntityShaft;
import reika.rotarycraft.gui.container.machine.inventory.ContainerFuelEngine;
import reika.rotarycraft.modinterface.TileEntityFuelEngine;
import reika.rotarycraft.registry.*;

/** Petroleum fixtures stay in the test pack. Generation tests use the actual engine, not injected shaft power. */
final class RotaryFuelEngineTests {
    private static final BlockPos POS = new BlockPos(3, 2, 4);
    private RotaryFuelEngineTests() {}
    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> env) {
        for (Direction facing : Direction.Plane.HORIZONTAL) test(event, env, "shaft_" + facing.getSerializedName(), h -> shaft(h, facing));
        test(event, env, "startup_fuel", RotaryFuelEngineTests::startup);
        test(event, env, "fuel_cadence", h -> cadence(h, false));
        test(event, env, "turbofuel_cadence", h -> cadence(h, true));
        for (int setting = 0; setting < 5; setting++) {
            int throttle = setting;
            test(event, env, "ecu_throttle_" + setting, h -> throttle(h, throttle, false));
            test(event, env, "ecu_turbofuel_" + setting, h -> throttle(h, throttle, true));
        }
        test(event, env, "requires_fuel", h -> gates(h, false));
        test(event, env, "requires_lubricant", h -> gates(h, true));
        test(event, env, "coasts_after_fuel_loss", h -> coast(h, false));
        test(event, env, "coasts_after_ecu_shutdown", h -> coast(h, true));
        test(event, env, "vacuum_blocks_combustion", RotaryFuelEngineTests::atmosphere);
        test(event, env, "lubricant_cadence", RotaryFuelEngineTests::lubricant);
        test(event, env, "fluid_ports", h -> fluids(h, false));
        test(event, env, "flipped_fluid_ports", h -> fluids(h, true));
        test(event, env, "ecu_transfer", h -> transfer(h, false, false));
        test(event, env, "flipped_ecu_transfer", h -> transfer(h, true, false));
        test(event, env, "ecu_transfer_backpressure", h -> transfer(h, false, true));
        test(event, env, "ecu_wrong_orientation", RotaryFuelEngineTests::wrongEcu);
        test(event, env, "ecu_rejects_wrong_fuel", RotaryFuelEngineTests::wrongFuel);
        test(event, env, "ecu_redstone_hysteresis", RotaryFuelEngineTests::redstone);
        test(event, env, "piped_resources", RotaryFuelEngineTests::pipes);
        test(event, env, "ceiling_placement", RotaryFuelEngineTests::placement);
        test(event, env, "cooling", RotaryFuelEngineTests::cooling);
        test(event, env, "temperature_speed_limit", RotaryFuelEngineTests::temperature);
        test(event, env, "overheat_explosions", RotaryFuelEngineTests::overheat);
        test(event, env, "save_load_tanks_and_timers", RotaryFuelEngineTests::save);
        test(event, env, "resume_fuel_timer", RotaryFuelEngineTests::resume);
        test(event, env, "menu_and_player_inventory", RotaryFuelEngineTests::menu);
        test(event, env, "comparator", RotaryFuelEngineTests::comparator);
        test(event, env, "survival_buckets", RotaryFuelEngineTests::buckets);
        test(event, env, "survival_harvest", RotaryFuelEngineTests::harvest);
        test(event, env, "crafting_recipe", RotaryFuelEngineTests::crafting);
    }
    private static void test(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> env, String name, Consumer<GameTestHelper> body) {
        RotaryGameTests.register(event, env, "fuel_engine_" + name, 100, body);
    }
    private static TileEntityFuelEngine engine(GameTestHelper h) {
        h.setBlock(POS, RotaryBlocks.FUEL_ENGINE.get());
        return h.getBlockEntity(POS, TileEntityFuelEngine.class);
    }
    private static void supply(TileEntityFuelEngine tile, boolean turbo) { tile.addFuel(10000, turbo ? RotaryFluids.ETHANOL.get() : Fluids.WATER); tile.addLube(1000); }
    private static void tick(GameTestHelper h, TileEntityFuelEngine tile, int count) { for (int i = 0; i < count; i++) tile.updateEntity(h.getLevel(), h.absolutePos(POS)); }
    private static BlockEntityEngineController ecu(GameTestHelper h, boolean flip, int setting) {
        var pos = flip ? POS.above() : POS.below(); h.setBlock(pos, RotaryBlocks.ECU.get());
        var ecu = h.getBlockEntity(pos, BlockEntityEngineController.class); ecu.setSetting(setting); return ecu;
    }
    private static ResourceHandler<FluidResource> capability(GameTestHelper h, Direction side) { return h.getLevel().getCapability(Capabilities.Fluid.BLOCK, h.absolutePos(POS), side); }
    private static void startup(GameTestHelper h) {
        var tile = engine(h); supply(tile, false);
        for (int i = 1; i <= 8; i++) { tick(h, tile, 1); h.assertTrue(tile.omega == 32 * i && tile.torque == 2048 && tile.getFuelLevel() == 10000 - i, "startup must burn 1 mB while gaining 32 rad/s per tick"); }
        h.assertTrue(tile.power == 524288 && tile.getCurrentPower() == 524288 && tile.getMaxPower() == tile.power, "full speed must generate 524288 W through the real generator API"); h.succeed();
    }
    private static void shaft(GameTestHelper h, Direction facing) {
        h.setBlock(POS, RotaryBlocks.FUEL_ENGINE.get().defaultBlockState().setValue(BlockRotaryCraftMachine.FACING, facing));
        var tile = h.getBlockEntity(POS, TileEntityFuelEngine.class); supply(tile, false);
        h.setBlock(POS.relative(facing), RotaryBlocks.HSLA_SHAFT.get().defaultBlockState().setValue(BlockRotaryCraftMachine.FACING, facing));
        var shaft = h.getBlockEntity(POS.relative(facing), BlockEntityShaft.class);
        h.runAfterDelay(16, () -> { h.assertTrue(tile.omega == 256 && shaft.omega == 256 && shaft.torque == 2048 && shaft.power == 524288,
                    "engine must deliver nominal power into the correctly oriented live shaft");
            h.assertTrue(tile.getEmittingPos(tile.getBlockPos()).equals(shaft.getBlockPos()), "generator emitting position must match shaft");
            h.assertTrue(!tile.getPowerSources(shaft, null).isEmpty(), "power tracing must retain the real generator"); h.succeed(); });
    }
    private static void cadence(GameTestHelper h, boolean turbo) {
        var tile = engine(h); supply(tile, turbo); int interval = turbo ? 72 : 36;
        tick(h, tile, interval - 1);
        h.assertTrue(tile.getFuelLevel() == 9992 && tile.getFuelInterval() == interval, "only 8 startup mB burn before the fuel timer expires");
        tick(h, tile, 1); h.assertTrue(tile.getFuelLevel() == 9988, "expiry must burn exactly 4 mB, with V33a's integer turbofuel bonus");
        tick(h, tile, interval); h.assertTrue(tile.getFuelLevel() == 9984, "steady speed must not repeatedly pay startup fuel"); h.succeed();
    }
    private static void throttle(GameTestHelper h, int setting, boolean turbo) {
        var tile = engine(h); supply(tile, turbo); var controller = ecu(h, false, setting);
        tick(h, tile, 10);
        int[] speed = {0, 16, 64, 128, 256}, factor = {1, 64, 8, 2, 1};
        h.assertTrue(tile.getController() == controller && tile.omega == speed[setting] && tile.power == (long)speed[setting] * 2048,
                "all five ECU settings must govern actual engine generation");
        h.assertTrue(tile.getFuelInterval() == 36 * factor[setting] * (turbo ? 2 : 1), "ECU piston efficiency and turbofuel must combine exactly");
        if (setting == 0) h.assertTrue(tile.getFuelLevel() == 10000 && tile.getLubeLevel() == 1000, "shutdown must burn no fuel or lubricant"); h.succeed();
    }
    private static void gates(GameTestHelper h, boolean noLube) {
        var tile = engine(h); if (noLube) tile.addFuel(10000, Fluids.WATER); else tile.addLube(1000);
        tick(h, tile, 40); h.assertTrue(tile.power == 0 && tile.omega == 0 && tile.torque == 0, "fuel and lubricant must both be present before generation starts");
        if (noLube) tile.addLube(1000); else tile.addFuel(10000, Fluids.WATER);
        tick(h, tile, 8); h.assertTrue(tile.power == 524288, "restoring the missing resource must enable generation"); h.succeed();
    }
    private static void coast(GameTestHelper h, boolean shutdown) {
        var tile = engine(h); supply(tile, false); tick(h, tile, 8);
        if (shutdown) ecu(h, false, 0); else tile.removeFuel(24000);
        int fuel = tile.getFuelLevel(); tick(h, tile, 1);
        h.assertTrue(tile.omega == 254 && tile.torque == 2048 && tile.power == 520192 && tile.getFuelLevel() == fuel, "loss of combustion must coast with retained torque and no fuel consumption");
        tick(h, tile, 255); h.assertTrue(tile.omega == 0 && tile.torque == 0 && tile.power == 0, "inertia must eventually decay completely"); h.succeed();
    }
    private static void atmosphere(GameTestHelper h) {
        var tile = engine(h); supply(tile, false);
        Consumer<AtmosphereHandler.AtmosphereQuery> vacuum = event -> { if (event.level == h.getLevel() && event.pos.equals(h.absolutePos(POS).above())) { event.setCombustionAllowed(false); event.setSoundReduction(10); } };
        NeoForge.EVENT_BUS.addListener(vacuum);
        try {
            tick(h, tile, 10); h.assertTrue(tile.power == 0 && tile.getFuelLevel() == 10000, "position-specific atmosphere checks must block combustion and consumption");
            h.assertTrue(AtmosphereHandler.getAtmoDensity(h.getLevel(), h.absolutePos(POS).above()) == .1F, "integration density must preserve sound attenuation");
            h.assertTrue(AtmosphereHandler.isNoAtmo(h.getLevel(), h.absolutePos(POS).above(), tile.getBlockState().getBlock(), false), "non-oxygen queries must detect reduced atmospheric density");
        } finally { NeoForge.EVENT_BUS.unregister(vacuum); }
        tick(h, tile, 8); h.assertTrue(tile.power == 524288, "restored oxygen/atmosphere must permit combustion"); h.succeed();
    }
    private static void lubricant(GameTestHelper h) {
        var tile = engine(h); supply(tile, false); tick(h, tile, 8); int[] before = {tile.getLubeLevel()};
        h.startSequence().thenIdle(32).thenExecute(() -> { h.assertTrue(tile.getLubeLevel() == before[0] - 1, "exactly 1 mB lubricant must burn per 32 powered world ticks"); before[0] = tile.getLubeLevel(); ecu(h, false, 0); })
                .thenIdle(32).thenExecute(() -> h.assertTrue(tile.getLubeLevel() == before[0] - 1, "lubrication must continue during coast-down")).thenSucceed();
    }
    private static void fluids(GameTestHelper h, boolean flip) {
        var tile = engine(h); tile.isFlipped = flip;
        for (Direction side : Direction.values()) {
            var handler = capability(h, side); h.assertTrue(handler != null && handler.size() == 3, "every side must report the three tanks");
            try (var tx = Transaction.openRoot()) {
                h.assertTrue(handler.insert(0, FluidResource.of(Fluids.WATER), 11, tx) == (side == (flip ? Direction.UP : Direction.DOWN) ? 11 : 0), "only the ECU-facing vertical side takes fuel");
                h.assertTrue(handler.insert(1, FluidResource.of(Fluids.WATER), 13, tx) == (side.getAxis().isHorizontal() ? 13 : 0), "only horizontal sides take cooling water");
                h.assertTrue(handler.insert(2, FluidResource.of(RotaryFluids.LUBRICANT.get()), 17, tx) == (side.getAxis().isHorizontal() ? 17 : 0), "only horizontal sides take lubricant");
                h.assertTrue(handler.extract(FluidResource.of(Fluids.WATER), 24, tx) == 0, "all extraction must be denied");
                h.assertTrue(handler.insert(FluidResource.of(Fluids.LAVA), 1, tx) == 0 && handler.insert(FluidResource.of(RotaryFluids.JET_FUEL.get()), 1, tx) == 0, "kerosene fixture and jet fuel must not replace petroleum or turbofuel");
            }
            h.assertTrue(tile.getFuelLevel() + tile.getWaterLevel() + tile.getLubeLevel() == 0, "aborted transactions must restore all tanks");
        }
        try (var tx = Transaction.openRoot()) {
            var handler = capability(h, flip ? Direction.UP : Direction.DOWN);
            h.assertTrue(handler.insert(FluidResource.of(Fluids.WATER), 24001, tx) == 24000, "fuel capacity must clamp to 24000 mB"); tx.commit();
        }
        tile.addWater(24001); tile.addLube(24001);
        h.assertTrue(tile.getFuelLevel() == 24000 && tile.getWaterLevel() == 24000 && tile.getLubeLevel() == 24000, "all three tanks have independent 24000 mB capacities");
        for (Direction side : Direction.values()) h.assertTrue(tile.getFlowForSide(side) == (side == (flip ? Direction.DOWN : Direction.UP) ? reika.rotarycraft.base.blockentity.BlockEntityPiping.Flow.NONE : reika.rotarycraft.base.blockentity.BlockEntityPiping.Flow.INPUT), "pipe flow must agree with flipped ports");
        h.succeed();
    }
    private static void transfer(GameTestHelper h, boolean flip, boolean full) {
        var tile = engine(h); tile.isFlipped = flip; var controller = ecu(h, flip, 0);
        if (full) tile.addFuel(23998, Fluids.WATER);
        try (var tx = Transaction.openRoot()) { h.assertTrue(controller.getFluidHandler(Direction.NORTH).insert(FluidResource.of(Fluids.WATER), 100, tx) == 100, "ECU must admit tagged petroleum through automation"); tx.commit(); }
        controller.updateEntity(h.getLevel(), controller.getBlockPos());
        h.assertTrue(tile.getFuelLevel() == (full ? 24000 : 26), "ECU transfers one quarter plus 1 mB, bounded by engine capacity");
        h.assertTrue(controller.getFluidHandler(null).getAmountAsInt(0) == (full ? 98 : 74), "ECU must remove exactly the amount the engine accepts");
        if (full) { controller.updateEntity(h.getLevel(), controller.getBlockPos()); h.assertTrue(controller.getFluidHandler(null).getAmountAsInt(0) == 98, "full engine must apply backpressure without losing fuel"); }
        h.succeed();
    }
    private static void wrongEcu(GameTestHelper h) {
        var tile = engine(h); tile.isFlipped = true; var controller = ecu(h, false, 0);
        try (var tx = Transaction.openRoot()) { controller.getFluidHandler(null).insert(FluidResource.of(Fluids.WATER), 100, tx); tx.commit(); }
        controller.updateEntity(h.getLevel(), controller.getBlockPos());
        h.assertTrue(tile.getFuelLevel() == 0 && controller.getFluidHandler(null).getAmountAsInt(0) == 100 && tile.getController() == null, "ECU on the wrong side must not control or fuel a flipped engine"); h.succeed();
    }
    private static void wrongFuel(GameTestHelper h) {
        var tile = engine(h); var controller = ecu(h, false, 0);
        try (var tx = Transaction.openRoot()) { h.assertTrue(controller.getFluidHandler(null).insert(FluidResource.of(RotaryFluids.JET_FUEL.get()), 100, tx) == 100, "ECU still accepts turbine fuel for other engines"); tx.commit(); }
        controller.updateEntity(h.getLevel(), controller.getBlockPos());
        h.assertTrue(tile.getFuelLevel() == 0 && controller.getFluidHandler(null).getAmountAsInt(0) == 100, "incompatible ECU fuel must never be discarded or inserted"); h.succeed();
    }
    private static void redstone(GameTestHelper h) {
        var tile = engine(h); supply(tile, false); var controller = ecu(h, false, 4); controller.redstoneMode = true;
        h.setBlock(POS.below().east(), Blocks.REDSTONE_BLOCK); controller.updateEntity(h.getLevel(), controller.getBlockPos());
        tick(h, tile, 8); h.assertTrue(tile.power == 0 && tile.getFuelLevel() == 10000, "redstone 15 must shut off fuel engine via ECU");
        h.setBlock(POS.below().east(), Blocks.AIR);
        for (int i = 0; i < 59; i++) controller.updateEntity(h.getLevel(), controller.getBlockPos());
        h.assertTrue(!controller.canProducePower(), "ECU must hold its previous redstone sample for 60 ticks");
        controller.updateEntity(h.getLevel(), controller.getBlockPos()); tick(h, tile, 8);
        h.assertTrue(tile.power == 524288, "ECU must sample the restored signal and restart the engine after the hold expires"); h.succeed();
    }
    private static void pipes(GameTestHelper h) {
        var tile = engine(h);
        BlockPos[] positions = {POS.below(), POS.west(), POS.east()};
        net.minecraft.world.level.block.Block[] blocks = {RotaryBlocks.FUEL_LINE.get(), RotaryBlocks.FLUID_PIPE.get(), RotaryBlocks.HOSE.get()};
        Fluid[] fluids = {Fluids.WATER, Fluids.WATER, RotaryFluids.LUBRICANT.get()};
        for (int i = 0; i < positions.length; i++) {
            h.setBlock(positions[i], blocks[i]);
            var handler = h.getLevel().getCapability(Capabilities.Fluid.BLOCK, h.absolutePos(positions[i]), null);
            try (var tx = Transaction.openRoot()) { h.assertTrue(handler.insert(FluidResource.of(fluids[i]), 1000, tx) == 1000, "real pipe must admit its integration fluid"); tx.commit(); }
        }
        h.runAfterDelay(24, () -> { h.assertTrue(tile.getFuelLevel() > 0 && tile.getWaterLevel() > 0 && tile.getLubeLevel() > 0 && tile.power > 0, "fuel line, water pipe and hose must supply the actual engine through its sided ports"); h.succeed(); });
    }
    private static void placement(GameTestHelper h) {
        h.setBlock(POS.above(), Blocks.STONE); var tile = engine(h); var player = h.makeMockServerPlayer(GameType.SURVIVAL);
        RotaryBlocks.FUEL_ENGINE.get().setPlacedBy(h.getLevel(), tile.getBlockPos(), tile.getBlockState(), player, new ItemStack(RotaryBlocks.FUEL_ENGINE.get()));
        h.assertTrue(tile.isFlipped, "placement below a solid ceiling with open space beneath must flip the engine");
        var controller = ecu(h, true, 4); h.assertTrue(tile.getController() == controller, "ceiling-mounted engine must find its top ECU"); h.succeed();
    }
    private static void cooling(GameTestHelper h) {
        var tile = engine(h); int ambient = ReikaWorldHelper.getAmbientTemperatureAt(h.getLevel(), tile.getBlockPos()); tile.setTemperature(ambient + 401); tile.addWater(13);
        tile.updateTemperature(h.getLevel(), tile.getBlockPos());
        h.assertTrue(tile.getTemperature() == ambient + 396 && tile.getWaterLevel() == 0, "water cooling subtracts one degree plus the integer temperature excess /100 and consumes up to 20 mB");
        tile.setTemperature(ambient + 1); tile.addWater(20); tile.updateTemperature(h.getLevel(), tile.getBlockPos());
        h.assertTrue(tile.getTemperature() == ambient && tile.getWaterLevel() == 0, "water is still consumed when the extra cooling term rounds to zero");
        h.assertTrue(!tile.canBeCooledWithFins() && !tile.allowExternalHeating() && !tile.allowHeatExtraction(), "external thermal exchange must retain V33a restrictions"); h.succeed();
    }
    private static void temperature(GameTestHelper h) {
        var tile = engine(h); supply(tile, false); tile.setTemperature(450); h.assertTrue(tile.getGenOmega() == 256, "450C has no speed penalty");
        tile.setTemperature(550); tick(h, tile, 8); h.assertTrue(tile.omega == 156, "hot engine must accelerate only to its thermal cap");
        tile.setTemperature(750); h.assertTrue(tile.getGenOmega() == 16, "thermal speed floor must be 16 rad/s");
        int previous = tile.omega; tick(h, tile, 1); h.assertTrue(tile.omega == previous - 1, "a lower thermal cap must coast down rather than instantaneously clamp speed"); h.succeed();
    }
    private static void overheat(GameTestHelper h) {
        var tile = engine(h); var radii = new java.util.ArrayList<Float>();
        Consumer<ExplosionEvent.Start> listener = event -> {
            if (event.getLevel() == h.getLevel() && event.getExplosion().center().equals(Vec3.atCenterOf(tile.getBlockPos()))) {
                radii.add(event.getExplosion().radius()); event.setCanceled(true); // Keep adjacent GameTest structures isolated.
            }
        };
        NeoForge.EVENT_BUS.addListener(listener);
        try { tile.setTemperature(752); tick(h, tile, 20);
            h.assertTrue(h.getBlockState(POS).isAir() && radii.equals(List.of(4F, 8F)), "overheat must remove the engine and dispatch both original explosions");
        } finally { NeoForge.EVENT_BUS.unregister(listener); }
        h.succeed();
    }
    private static void save(GameTestHelper h) {
        var tile = engine(h); supply(tile, true); tile.addWater(357); tile.setTemperature(150); tile.isFlipped = true; tick(h, tile, 11);
        var saved = tile.saveWithoutMetadata(h.getLevel().registryAccess()); var restored = new TileEntityFuelEngine(tile.getBlockPos(), tile.getBlockState()); var view = restored.getFluidHandler(Direction.UP);
        restored.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, h.getLevel().registryAccess(), saved));
        h.assertTrue(restored.getLevel() == null && restored.isFlipped && restored.getFuelLevel() == tile.getFuelLevel() && restored.getWaterLevel() == 357 && restored.getLubeLevel() == tile.getLubeLevel()
                && restored.getTemperature() == 150 && restored.omega == tile.omega && restored.torque == tile.torque && restored.power == tile.power && restored.isUsingTurbofuel(), "detached loads must restore tanks, flipping, temperature and mechanical state");
        var copy = restored.saveWithoutMetadata(h.getLevel().registryAccess());
        for (String name : List.of("fuelTick", "soundTick", "tempTick")) h.assertTrue(saved.getIntOr(name, -1) == copy.getIntOr(name, -2), "timer phase must survive saving and loading");
        try (var tx = Transaction.openRoot()) { h.assertTrue(view.insert(FluidResource.of(RotaryFluids.ETHANOL.get()), 3, tx) == 3, "pre-load capability view must retain restored tanks and flipped permissions"); tx.commit(); }
        h.assertTrue(restored.getFuelLevel() == tile.getFuelLevel() + 3, "committed pre-load view must mutate restored tank"); h.succeed();
    }
    private static void menu(GameTestHelper h) {
        var tile = engine(h); supply(tile, false); tile.addWater(123); tile.setTemperature(620);
        var player = h.makeMockServerPlayer(GameType.SURVIVAL); player.setPos(Vec3.atCenterOf(tile.getBlockPos()));
        var menu = new ContainerFuelEngine(1, player.getInventory(), tile);
        h.assertTrue(menu.slots.size() == 36 && menu.stillValid(player) && menu.getGauge(0) == 10000 && menu.getGauge(1) == 123 && menu.getGauge(2) == 1000 && menu.getGauge(3) == 620 && menu.getGauge(4) == 36, "menu must expose only player inventory and synchronized gauges");
        player.getInventory().setItem(9, new ItemStack(Items.DIAMOND, 5)); menu.quickMoveStack(player, 0);
        h.assertTrue(player.getInventory().getItem(9).isEmpty() && player.getInventory().getItem(0).getCount() == 5, "shift-click must move player inventory to hotbar");
        menu.quickMoveStack(player, 27); h.assertTrue(player.getInventory().getItem(0).isEmpty() && player.getInventory().getItem(9).getCount() == 5, "shift-click must move hotbar back to main inventory");
        player.setPos(player.getX() + 20, player.getY(), player.getZ()); h.assertTrue(!menu.stillValid(player), "distant players must lose menu access"); h.succeed();
    }
    private static void resume(GameTestHelper h) {
        var tile = engine(h); supply(tile, true); tick(h, tile, 11);
        int fuel = tile.getFuelLevel(); var saved = tile.saveWithoutMetadata(h.getLevel().registryAccess());
        h.setBlock(POS, Blocks.AIR); var restored = engine(h);
        restored.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, h.getLevel().registryAccess(), saved));
        tick(h, restored, 60);
        h.assertTrue(restored.omega == 256 && restored.getFuelLevel() == fuel, "reloaded running engine must resume its saved phase without repaying startup or burning early");
        tick(h, restored, 1); h.assertTrue(restored.getFuelLevel() == fuel - 4, "saved fuel timer must expire on the original tick after reloading"); h.succeed();
    }
    private static void comparator(GameTestHelper h) {
        var tile = engine(h); var state = h.getBlockState(POS); h.assertTrue(state.hasAnalogOutputSignal(), "engine must expose comparator signal");
        for (int amount : new int[]{0, 1600, 12000, 24000}) { tile.removeFuel(24000); tile.addFuel(amount, Fluids.WATER);
            h.assertTrue(state.getAnalogOutputSignal(h.getLevel(), tile.getBlockPos(), Direction.NORTH) == amount * 15 / 24000, "comparator must scale actual fuel level"); } h.succeed();
    }
    private static void buckets(GameTestHelper h) {
        var tile = engine(h); var player = h.makeMockPlayer(GameType.SURVIVAL); player.setPos(Vec3.atCenterOf(tile.getBlockPos()));
        for (Item bucket : List.of(RotaryItems.ETHANOL_BUCKET.get(), RotaryItems.LUBE_BUCKET.get(), Items.WATER_BUCKET)) {
            player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(bucket));
            h.getBlockState(POS).useItemOn(player.getMainHandItem(), h.getLevel(), player, InteractionHand.MAIN_HAND, new BlockHitResult(Vec3.atCenterOf(tile.getBlockPos()), Direction.NORTH, tile.getBlockPos(), false));
            h.assertTrue(player.getMainHandItem().is(Items.BUCKET), "survival fluid insertion must return the empty bucket");
        }
        h.assertTrue(tile.getFuelLevel() == 1000 && tile.getWaterLevel() == 1000 && tile.getLubeLevel() == 1000, "manual buckets must reach each of the three independent tanks");
        tile.addFuel(22999, RotaryFluids.ETHANOL.get()); player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(RotaryItems.ETHANOL_BUCKET.get()));
        h.getBlockState(POS).useItemOn(player.getMainHandItem(), h.getLevel(), player, InteractionHand.MAIN_HAND, new BlockHitResult(Vec3.atCenterOf(tile.getBlockPos()), Direction.NORTH, tile.getBlockPos(), false));
        h.assertTrue(tile.getFuelLevel() == 23999 && player.getMainHandItem().is(RotaryItems.ETHANOL_BUCKET.get()), "a bucket must fit completely or roll back without consuming fuel"); h.succeed();
    }
    private static void harvest(GameTestHelper h) {
        engine(h); var player = (net.minecraft.server.level.ServerPlayer)h.makeMockServerPlayer(GameType.SURVIVAL); player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_PICKAXE));
        h.assertTrue(player.gameMode.destroyBlock(h.absolutePos(POS)), "iron pickaxe must harvest the fuel engine");
        h.assertTrue(h.getEntities(EntityTypes.ITEM, POS, 3).stream().filter(e -> e.getItem().is(RotaryBlocks.FUEL_ENGINE.get().asItem())).mapToInt(e -> e.getItem().getCount()).sum() == 1, "survival harvest must drop exactly one engine"); h.succeed();
    }
    private static void crafting(GameTestHelper h) {
        var grid = List.of(new ItemStack(RotaryItems.ALUMINUM_ALLOY_CYLINDER.get()), new ItemStack(RotaryItems.TUNGSTEN_INGOT.get()), new ItemStack(RotaryItems.ALUMINUM_ALLOY_CYLINDER.get()),
                new ItemStack(RotaryItems.HSLA_STEEL_GEAR_2x.get()), new ItemStack(RotaryItems.TUNGSTEN_ALLOY_GEAR_8x.get()), new ItemStack(RotaryItems.HSLA_SHAFT_CORE.get()),
                new ItemStack(RotaryItems.HSLA_PLATE.get()), new ItemStack(RotaryItems.IMPELLER.get()), new ItemStack(RotaryItems.HSLA_PLATE.get()));
        var input = CraftingInput.of(3, 3, grid);
        h.assertTrue(h.getLevel().getServer().getRecipeManager().recipeMap().byType(RecipeType.CRAFTING).stream().anyMatch(r -> r.value().matches(input, h.getLevel()) && r.value().assemble(input).is(RotaryBlocks.FUEL_ENGINE.get().asItem())), "exact V33a recipe must craft the engine from survival parts"); h.succeed();
    }
}
