package reika.rotarycraft;

import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.*;
import net.minecraft.gametest.framework.*;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.*;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.material.*;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.api.interfaces.Fillable;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.auxiliary.BlockEntityFillingStation;
import reika.rotarycraft.blockentities.transmission.BlockEntityCreativeCoil;
import reika.rotarycraft.gui.container.machine.inventory.ContainerFillingStation;
import reika.rotarycraft.items.tools.ItemIntegratedGearbox;
import reika.rotarycraft.items.tools.ItemJetPack;
import reika.rotarycraft.registry.*;

/** Player insertion, real shaft power, conserved fluid transfer, automation and saved inventories. */
final class RotaryFillingStationTests {
    private static final BlockPos POS = new BlockPos(3, 2, 4);
    private RotaryFillingStationTests() {}

    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> env) {
        test(event, env, "menu_click_insert_retrieve", RotaryFillingStationTests::menu);
        test(event, env, "shift_click_inventory", h -> shift(h, false));
        test(event, env, "shift_click_worn_pack", h -> shift(h, true));
        test(event, env, "armor_slots", RotaryFillingStationTests::armor);
        for (Direction facing : Direction.Plane.HORIZONTAL) test(event, env, "shaft_" + facing.getSerializedName(), h -> shaft(h, facing));
        for (int variant = 0; variant < 3; variant++) {
            int index = variant;
            test(event, env, "jetpack_variant_" + variant, h -> topUp(h, index));
        }
        test(event, env, "speed_rate", RotaryFillingStationTests::rate);
        test(event, env, "power_gate", RotaryFillingStationTests::powerGate);
        test(event, env, "last_millibuckets", RotaryFillingStationTests::lastFluid);
        test(event, env, "ethanol_crystals", RotaryFillingStationTests::ethanol);
        test(event, env, "fluid_buckets", RotaryFillingStationTests::buckets);
        test(event, env, "bucket_backpressure", RotaryFillingStationTests::bucketBackpressure);
        test(event, env, "reject_mixed_fuels", RotaryFillingStationTests::mixed);
        test(event, env, "gearbox_lubricant", h -> gearbox(h, RotaryFluids.LUBRICANT.get()));
        test(event, env, "gearbox_nitrogen", h -> gearbox(h, RotaryFluids.LIQUID_NITROGEN.get()));
        test(event, env, "full_pack_empty_tank", RotaryFillingStationTests::full);
        test(event, env, "output_backpressure", RotaryFillingStationTests::backpressure);
        test(event, env, "fluid_ports_transactions", RotaryFillingStationTests::fluids);
        test(event, env, "item_ports_transactions", RotaryFillingStationTests::items);
        test(event, env, "hopper_input", h -> hopper(h, false));
        test(event, env, "hopper_output_empty_tank", h -> hopper(h, true));
        test(event, env, "save_load_detached", h -> save(h, false));
        test(event, env, "save_load_legacy_inventory", h -> save(h, true));
        test(event, env, "legacy_jetpack_fuel", RotaryFillingStationTests::legacy);
        test(event, env, "fuel_consumption_and_upgrades", RotaryFillingStationTests::consume);
        test(event, env, "survival_inventory_drops", RotaryFillingStationTests::drops);
        test(event, env, "command_inventory_drops", h -> drops(h, true));
        test(event, env, "rocket_fuel_tag", RotaryFillingStationTests::rocket);
        test(event, env, "comparator", RotaryFillingStationTests::comparator);
        test(event, env, "gear_output_stack_limit", RotaryFillingStationTests::stacked);
        test(event, env, "item_api_rejects_invalid_amounts", RotaryFillingStationTests::invalid);
    }
    private static void test(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> env, String name, Consumer<GameTestHelper> body) {
        RotaryGameTests.register(event, env, "filling_station_" + name, 100, body);
    }
    private static BlockEntityFillingStation station(GameTestHelper h) {
        h.setBlock(POS, RotaryBlocks.FILLING_STATION.get().defaultBlockState().setValue(BlockRotaryCraftMachine.FACING, Direction.EAST));
        return h.getBlockEntity(POS, BlockEntityFillingStation.class);
    }
    private static BlockEntityCreativeCoil coil(GameTestHelper h, Direction facing, int torque, int speed) {
        BlockPos pos = POS.relative(facing.getOpposite());
        h.setBlock(pos.below(), Blocks.REDSTONE_BLOCK);
        h.setBlock(pos, RotaryBlocks.CREATIVE_COIL.get().defaultBlockState().setValue(BlockRotaryCraftMachine.FACING, facing));
        var coil = h.getBlockEntity(pos, BlockEntityCreativeCoil.class);
        coil.setReleaseTorque(torque); coil.setReleaseOmega(speed);
        coil.updateEntity(h.getLevel(), coil.getBlockPos());
        return coil;
    }
    private static void powered(GameTestHelper h) { coil(h, Direction.EAST, 8, 128); }
    private static void tick(GameTestHelper h, BlockEntityFillingStation station, int count) {
        for (int n = 0; n < count; n++) station.updateEntity(h.getLevel(), station.getBlockPos());
    }
    private static void supply(BlockEntityFillingStation tile, Fluid fluid, int amount) {
        try (var tx = Transaction.openRoot()) { tile.getFluidHandler(null).insert(FluidResource.of(fluid), amount, tx); tx.commit(); }
    }
    private static ItemStack pack(int variant, Fluid fluid, int amount) {
        ItemStack item = new ItemStack(switch (variant) { case 1 -> RotaryItems.HSLA_STEEL_PACK.get(); case 2 -> RotaryItems.BEDROCK_ALLOY_PACK.get(); default -> RotaryItems.JETPACK.get(); });
        ((Fillable)item.getItem()).addFluid(item, new FluidStack(fluid, 1), amount);
        return item;
    }
    private static Player player(GameTestHelper h) {
        Player player = h.makeMockPlayer(GameType.SURVIVAL); player.setPos(Vec3.atCenterOf(h.absolutePos(POS))); return player;
    }
    private static void menu(GameTestHelper h) {
        var tile = station(h); var player = player(h); var menu = new ContainerFillingStation(1, player.getInventory(), tile);
        h.assertTrue(menu.slots.size() == 44, "menu must expose four machine slots, 36 inventory slots and four armor slots");
        menu.setCarried(pack(0, RotaryFluids.JET_FUEL.get(), 0)); menu.clicked(3, 0, ContainerInput.PICKUP, player);
        h.assertTrue(menu.getCarried().isEmpty() && tile.getItem(3).is(RotaryItems.JETPACK.get()), "ordinary mouse insertion must put the jetpack in the input slot");
        menu.clicked(3, 0, ContainerInput.PICKUP, player);
        h.assertTrue(tile.getItem(3).isEmpty() && menu.getCarried().is(RotaryItems.JETPACK.get()), "ordinary mouse retrieval must return the pack");
        h.assertTrue(!menu.slots.get(0).mayPlace(menu.getCarried()) && !menu.slots.get(2).mayPlace(menu.getCarried()), "active and output slots must reject insertion");
        tile.setItem(0, menu.getCarried()); menu.setCarried(ItemStack.EMPTY); menu.clicked(0, 0, ContainerInput.PICKUP, player);
        h.assertTrue(tile.getItem(0).isEmpty() && menu.getCarried().is(RotaryItems.JETPACK.get()), "a partially filled active pack must be manually retrievable"); h.succeed();
    }
    private static void shift(GameTestHelper h, boolean worn) {
        var tile = station(h); var player = player(h); var menu = new ContainerFillingStation(1, player.getInventory(), tile);
        ItemStack pack = pack(0, RotaryFluids.JET_FUEL.get(), 12);
        if (worn) player.setItemSlot(EquipmentSlot.CHEST, pack); else player.getInventory().setItem(9, pack);
        menu.quickMoveStack(player, worn ? 41 : 4);
        h.assertTrue(tile.getItem(3).is(RotaryItems.JETPACK.get()) && (worn ? player.getItemBySlot(EquipmentSlot.CHEST).isEmpty() : player.getInventory().getItem(9).isEmpty()), "shift-click must move inventory or worn jetpacks into input");
        tile.setItem(2, tile.removeItem(3, 1)); menu.quickMoveStack(player, 2);
        h.assertTrue(tile.isEmpty() && player.getInventory().getItem(9).is(RotaryItems.JETPACK.get()), "shift-click output must preserve the retrieved pack"); h.succeed();
    }
    private static void armor(GameTestHelper h) {
        var tile = station(h); var player = player(h); var menu = new ContainerFillingStation(1, player.getInventory(), tile);
        var pack = pack(0, RotaryFluids.JET_FUEL.get(), 10);
        h.assertTrue(menu.slots.get(41).mayPlace(pack) && !menu.slots.get(40).mayPlace(pack) && !menu.slots.get(42).mayPlace(pack) && !menu.slots.get(43).mayPlace(pack), "jetpack must be accepted only by chest armor slot");
        menu.setCarried(pack); menu.clicked(41, 0, ContainerInput.PICKUP, player);
        h.assertTrue(player.getItemBySlot(EquipmentSlot.CHEST).is(RotaryItems.JETPACK.get()) && menu.getCarried().isEmpty(), "station menu must equip a retrieved jetpack"); h.succeed();
    }
    private static void shaft(GameTestHelper h, Direction facing) {
        h.setBlock(POS, RotaryBlocks.FILLING_STATION.get().defaultBlockState().setValue(BlockRotaryCraftMachine.FACING, facing));
        var tile = h.getBlockEntity(POS, BlockEntityFillingStation.class); coil(h, facing, 8, 128);
        supply(tile, RotaryFluids.JET_FUEL.get(), 100); tile.setItem(0, pack(0, RotaryFluids.JET_FUEL.get(), 0)); tick(h, tile, 1);
        h.assertTrue(tile.power == 1024 && tile.getReadDirection() == facing.getOpposite() && tile.getFluidLevel() == 72 && ((Fillable)tile.getItem(0).getItem()).getCurrentFillLevel(tile.getItem(0)) == 28, "each facing must receive real shaft power and fill at V33a speed"); h.succeed();
    }
    private static void topUp(GameTestHelper h, int variant) {
        var tile = station(h); powered(h); supply(tile, RotaryFluids.JET_FUEL.get(), 1000); tile.setItem(3, pack(variant, RotaryFluids.JET_FUEL.get(), 29950)); tick(h, tile, 3);
        var output = tile.getItem(2); var fillable = (Fillable)output.getItem();
        h.assertTrue(tile.getItem(0).isEmpty() && tile.getItem(3).isEmpty() && fillable.isFull(output) && fillable.getCurrentFluid(output) == RotaryFluids.JET_FUEL.get() && tile.getFluidLevel() == 950, "all jetpack variants must top up and output with exactly 50 mB consumed"); h.succeed();
    }
    private static void rate(GameTestHelper h) {
        var tile = station(h); var coil = coil(h, Direction.EAST, 8, 256); supply(tile, RotaryFluids.JET_FUEL.get(), 100); tile.setItem(0, pack(0, RotaryFluids.JET_FUEL.get(), 0)); tick(h, tile, 1);
        h.assertTrue(tile.getFluidLevel() == 68, "256 rad/s must transfer 32 mB per tick");
        coil.setReleaseTorque(1024); coil.setReleaseOmega(1); coil.updateEntity(h.getLevel(), coil.getBlockPos()); tick(h, tile, 1);
        h.assertTrue(tile.power == 1024 && tile.getFluidLevel() == 68, "V33a log2 speed rate is zero at one rad/s even with sufficient torque"); h.succeed();
    }
    private static void powerGate(GameTestHelper h) {
        var tile = station(h); tile.setItem(1, new ItemStack(RotaryItems.JET_FUEL_BUCKET.get())); tile.setItem(3, pack(0, RotaryFluids.JET_FUEL.get(), 0)); tick(h, tile, 4);
        h.assertTrue(tile.getFluidLevel() == 0 && !tile.getItem(1).isEmpty() && !tile.getItem(3).isEmpty(), "unpowered station must neither drain buckets nor shuttle packs");
        coil(h, Direction.EAST, 7, 128); tick(h, tile, 1); h.assertTrue(tile.getFluidLevel() == 0, "896 W is below 1024 W minimum");
        powered(h); tick(h, tile, 2); h.assertTrue(tile.getFluidLevel() == 972 && tile.getItem(1).is(Items.BUCKET), "restored real power must drain bucket and fill pack"); h.succeed();
    }
    private static void lastFluid(GameTestHelper h) {
        var tile = station(h); powered(h); supply(tile, RotaryFluids.JET_FUEL.get(), 7); tile.setItem(0, pack(0, RotaryFluids.JET_FUEL.get(), 23)); tick(h, tile, 3);
        h.assertTrue(tile.getFluidLevel() == 0 && ((Fillable)tile.getItem(0).getItem()).getCurrentFillLevel(tile.getItem(0)) == 30, "last seven mB must transfer exactly once without creating fuel"); h.succeed();
    }
    private static void ethanol(GameTestHelper h) {
        var tile = station(h); powered(h); tile.setItem(1, new ItemStack(RotaryItems.ETHANOL.get(), 2)); tile.setItem(0, pack(0, RotaryFluids.ETHANOL.get(), 0)); tick(h, tile, 2);
        var pack = tile.getItem(0); var fillable = (Fillable)pack.getItem();
        h.assertTrue(tile.getItem(1).isEmpty() && tile.getFluidLevel() == 1944 && fillable.getCurrentFillLevel(pack) == 56 && fillable.getCurrentFluid(pack) == RotaryFluids.ETHANOL.get(), "crystals must provide 1000 mB each and pack must retain ethanol fuel type"); h.succeed();
    }
    private static void buckets(GameTestHelper h) {
        var tile = station(h); powered(h);
        for (Item bucket : List.of(RotaryItems.JET_FUEL_BUCKET.get(), RotaryItems.ETHANOL_BUCKET.get(), RotaryItems.LUBE_BUCKET.get(), Items.WATER_BUCKET)) {
            tile.setItem(1, new ItemStack(bucket)); h.assertTrue(tile.isItemValidForSlot(1, tile.getItem(1)), "fuel slot must accept all filled fluid containers"); tick(h, tile, 1);
            h.assertTrue(tile.getFluidLevel() == 1000 && tile.getItem(1).is(Items.BUCKET), "fluid-container transaction must return empty bucket and conserve all 1000 mB");
            try (var tx = Transaction.openRoot()) { tile.getFluidHandler(null).extract(FluidResource.of(tile.getContainedFluid()), 1000, tx); tx.commit(); }
        } h.succeed();
    }
    private static void bucketBackpressure(GameTestHelper h) {
        var tile = station(h); powered(h); supply(tile, RotaryFluids.JET_FUEL.get(), 31500); tile.setItem(1, new ItemStack(RotaryItems.JET_FUEL_BUCKET.get())); tick(h, tile, 1);
        h.assertTrue(tile.getFluidLevel() == 31500 && tile.getItem(1).is(RotaryItems.JET_FUEL_BUCKET.get()), "bucket must fit completely before draining");
        tile.setItem(1, new ItemStack(RotaryItems.ETHANOL_BUCKET.get())); tick(h, tile, 1);
        h.assertTrue(tile.getFluidLevel() == 31500 && tile.getItem(1).is(RotaryItems.ETHANOL_BUCKET.get()), "incompatible bucket must remain intact"); h.succeed();
    }
    private static void mixed(GameTestHelper h) {
        var tile = station(h); powered(h); supply(tile, RotaryFluids.JET_FUEL.get(), 100); tile.setItem(0, pack(0, RotaryFluids.ETHANOL.get(), 23)); tick(h, tile, 2);
        var pack = tile.getItem(0); var item = (Fillable)pack.getItem();
        h.assertTrue(tile.getFluidLevel() == 100 && item.getCurrentFluid(pack) == RotaryFluids.ETHANOL.get() && item.getCurrentFillLevel(pack) == 23, "different fuel must be rejected without conversion or fuel loss"); h.succeed();
    }
    private static void gearbox(GameTestHelper h, Fluid fluid) {
        var tile = station(h); powered(h); supply(tile, fluid, 1000); var gear = ItemIntegratedGearbox.getIntegratedGearItem(8, null);
        ((Fillable)gear.getItem()).addFluid(gear, new FluidStack(fluid, 1), 480); tile.setItem(3, gear); tick(h, tile, 2);
        var output = tile.getItem(2); var item = (Fillable)output.getItem();
        h.assertTrue(item.isFull(output) && item.getCurrentFluid(output) == fluid && tile.getFluidLevel() == 980 && ItemIntegratedGearbox.getRatioFromIntegratedGearItem(output, true) == (fluid == RotaryFluids.LUBRICANT.get() ? 8 : -8), "station must fill either gearbox fluid and preserve gear ratio and mode"); h.succeed();
    }
    private static void full(GameTestHelper h) {
        var tile = station(h); powered(h); tile.setItem(3, pack(0, RotaryFluids.JET_FUEL.get(), 30000)); tick(h, tile, 2);
        h.assertTrue(tile.getItem(0).isEmpty() && tile.getItem(2).is(RotaryItems.JETPACK.get()) && tile.getFluidLevel() == 0, "already full input must output without requiring tank fluid"); h.succeed();
    }
    private static void backpressure(GameTestHelper h) {
        var tile = station(h); powered(h); tile.setItem(0, pack(0, RotaryFluids.JET_FUEL.get(), 30000)); tile.setItem(2, pack(1, RotaryFluids.JET_FUEL.get(), 30000)); tick(h, tile, 2);
        h.assertTrue(!tile.getItem(0).isEmpty() && !tile.getItem(2).isEmpty(), "occupied output must retain active pack"); tile.removeItem(2, 1); tick(h, tile, 1);
        h.assertTrue(tile.getItem(0).isEmpty() && tile.getItem(2).is(RotaryItems.JETPACK.get()), "clearing output must release blocked full pack even with empty tank"); h.succeed();
    }
    private static void fluids(GameTestHelper h) {
        var tile = station(h); var fuel = FluidResource.of(RotaryFluids.JET_FUEL.get());
        for (Direction side : Direction.values()) {
            var view = h.getLevel().getCapability(Capabilities.Fluid.BLOCK, tile.getBlockPos(), side); h.assertTrue(view != null, "every side must expose the tank");
            try (var tx = Transaction.openRoot()) { h.assertTrue(view.insert(fuel, 100, tx) == (side == Direction.DOWN ? 0 : 100), "bottom is output, other ports are inputs"); }
            h.assertTrue(tile.getFluidLevel() == 0, "aborted fluid insertion must roll back");
        }
        supply(tile, RotaryFluids.JET_FUEL.get(), 100);
        for (Direction side : Direction.values()) try (var tx = Transaction.openRoot()) {
            var view = tile.getFluidHandler(side); h.assertTrue(view.extract(fuel, 7, tx) == (side == Direction.DOWN ? 7 : 0), "only bottom may extract fluid");
        }
        h.assertTrue(tile.getFluidLevel() == 100, "aborted extraction must conserve fuel"); h.succeed();
    }
    private static void items(GameTestHelper h) {
        var tile = station(h); var pack = ItemResource.of(pack(0, RotaryFluids.JET_FUEL.get(), 0));
        for (Direction side : Direction.values()) {
            var view = h.getLevel().getCapability(Capabilities.Item.BLOCK, tile.getBlockPos(), side); h.assertTrue(view != null && view.size() == 4, "all sides must expose four inventory slots");
            try (var tx = Transaction.openRoot()) {
                h.assertTrue(view.insert(3, pack, 1, tx) == 1 && view.insert(0, pack, 1, tx) == 0 && view.insert(2, pack, 1, tx) == 0, "automation may insert pack only in waiting input");
                h.assertTrue(view.extract(3, pack, 1, tx) == 0 && view.extract(pack, 1, tx) == 0, "automation must not steal waiting or active packs");
            }
            h.assertTrue(tile.isEmpty(), "aborted insertion must restore item inventory");
            tile.setItem(2, pack.toStack()); try (var tx = Transaction.openRoot()) { h.assertTrue(view.extract(2, pack, 1, tx) == 1, "automation may take finished output"); tx.commit(); }
            h.assertTrue(tile.isEmpty(), "committed extraction must remove output");
        } h.succeed();
    }
    private static void hopper(GameTestHelper h, boolean output) {
        var tile = station(h); var pos = output ? POS.below() : POS.above(); h.setBlock(pos, Blocks.HOPPER);
        var hopper = h.getBlockEntity(pos, HopperBlockEntity.class); var pack = pack(0, RotaryFluids.JET_FUEL.get(), 30);
        if (output) tile.setItem(2, pack); else hopper.setItem(0, pack);
        h.runAfterDelay(32, () -> { h.assertTrue(output ? tile.getItem(2).isEmpty() && hopper.getItem(0).is(RotaryItems.JETPACK.get()) : tile.getItem(3).is(RotaryItems.JETPACK.get()) && hopper.getItem(0).isEmpty(), "real hopper must move allowed input/output even when tank is empty"); h.succeed(); });
    }
    private static void save(GameTestHelper h, boolean legacy) {
        var tile = station(h); supply(tile, RotaryFluids.ETHANOL.get(), 123); tile.setItem(0, pack(0, RotaryFluids.ETHANOL.get(), 7)); tile.setItem(1, new ItemStack(RotaryItems.ETHANOL.get(), 3)); tile.setItem(2, pack(1, RotaryFluids.JET_FUEL.get(), 30000)); tile.setItem(3, pack(2, RotaryFluids.JET_FUEL.get(), 31));
        var saved = tile.saveWithoutMetadata(h.getLevel().registryAccess());
        if (legacy) { saved.put("ItemsRaw", saved.get("Inventory").copy()); saved.remove("Inventory"); }
        var restored = new BlockEntityFillingStation(tile.getBlockPos(), tile.getBlockState()); var view = restored.getItemHandler();
        restored.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, h.getLevel().registryAccess(), saved));
        h.assertTrue(restored.getLevel() == null && restored.getFluidLevel() == 123 && restored.getContainedFluid() == RotaryFluids.ETHANOL.get() && view == restored.getItemHandler(), "detached loads must retain named tank, caller registry context and handler identity");
        for (int slot = 0; slot < 4; slot++) h.assertTrue(ItemStack.matches(tile.getItem(slot), restored.getItem(slot)), "all four persisted inventory stacks must survive including fuel type and upgrades"); h.succeed();
    }
    private static void legacy(GameTestHelper h) {
        var pack = pack(0, RotaryFluids.JET_FUEL.get(), 0); ReikaItemHelper.updateStackTag(pack, tag -> tag.putInt("fuel", 17)); var item = (ItemJetPack)pack.getItem();
        h.assertTrue(item.getCurrentFluid(pack) == RotaryFluids.JET_FUEL.get() && item.addFluid(pack, new FluidStack(RotaryFluids.JET_FUEL.get(), 1), 7) == 7 && item.getFuel(pack) == 24, "earlier amount-only jetpacks must retain and accept existing fuel"); h.succeed();
    }
    private static void consume(GameTestHelper h) {
        var pack = pack(0, RotaryFluids.ETHANOL.get(), 7); var item = (ItemJetPack)pack.getItem(); ItemJetPack.PackUpgrades.COOLING.enable(pack, true);
        item.use(pack, 3); h.assertTrue(item.getFuel(pack) == 4 && item.getCurrentFluid(pack) == RotaryFluids.ETHANOL.get(), "consumption must retain actual ethanol fuel type");
        item.use(pack, 4); item.use(pack, 1);
        h.assertTrue(item.getFuel(pack) == 0 && item.getCurrentFluid(pack) == null && ItemJetPack.PackUpgrades.COOLING.existsOn(pack), "using the final fuel or an empty pack must be safe and preserve upgrades");
        h.assertTrue(item.addFluid(pack, new FluidStack(RotaryFluids.JET_FUEL.get(), 1), 1) == 1, "drained pack may switch fuel"); h.succeed();
    }
    private static void rocket(GameTestHelper h) {
        var tile = station(h); powered(h); supply(tile, Fluids.WATER, 7); tile.setItem(0, pack(0, Fluids.WATER, 23)); tick(h, tile, 1);
        var pack = tile.getItem(0); var item = (Fillable)pack.getItem();
        h.assertTrue(tile.getFluidLevel() == 0 && item.getCurrentFillLevel(pack) == 30 && item.getCurrentFluid(pack) == Fluids.WATER, "test-only foreign rocket fuel tag must fill and retain the integrated fluid type"); h.succeed();
    }
    private static void comparator(GameTestHelper h) {
        var tile = station(h); h.assertTrue(tile.getRedstoneOverride() == 15, "empty tank must signal blocked station");
        supply(tile, RotaryFluids.JET_FUEL.get(), 100); tile.setItem(0, pack(0, RotaryFluids.JET_FUEL.get(), 23));
        h.assertTrue(tile.getRedstoneOverride() == 0 && tile.areConditionsMet(), "compatible partial pack must signal ready");
        tile.setItem(0, pack(0, RotaryFluids.ETHANOL.get(), 23)); h.assertTrue(tile.getRedstoneOverride() == 15, "incompatible fuel must signal blocked station"); h.succeed();
    }
    private static void stacked(GameTestHelper h) {
        var tile = station(h); powered(h); var gear = ItemIntegratedGearbox.getIntegratedGearItem(8, new FluidStack(RotaryFluids.LUBRICANT.get(), 1));
        int limit = gear.getMaxStackSize(); tile.setItem(2, gear.copyWithCount(limit)); tile.setItem(3, gear); tick(h, tile, 2);
        h.assertTrue(tile.getItem(2).getCount() == limit && tile.getItem(0).getCount() == 1 && tile.getItem(3).isEmpty(), "full output must retain identical gear input rather than exceed its real item stack limit");
        tile.removeItem(2, 1); tick(h, tile, 1);
        h.assertTrue(tile.getItem(2).getCount() == limit && tile.getItem(0).isEmpty(), "freeing one output space must release the retained full gear"); h.succeed();
    }
    private static void invalid(GameTestHelper h) {
        for (ItemStack stack : List.of(pack(0, RotaryFluids.JET_FUEL.get(), 23), ItemIntegratedGearbox.getIntegratedGearItem(8, new FluidStack(RotaryFluids.LUBRICANT.get(), 1)))) {
            var item = (Fillable)stack.getItem(); var before = stack.copy();
            h.assertTrue(item.addFluid(stack, FluidStack.EMPTY, 7) == 0 && item.addFluid(stack, new FluidStack(Fluids.LAVA, 1), 7) == 0 && item.addFluid(stack, new FluidStack(item.getCurrentFluid(stack), 1), -7) == 0 && item.addFluid(stack, new FluidStack(item.getCurrentFluid(stack), 1), 0) == 0, "empty fluid, invalid fluid and nonpositive amounts must be rejected");
            h.assertTrue(ItemStack.matches(before, stack), "rejected filling must preserve all item data");
        } h.succeed();
    }
    private static void drops(GameTestHelper h) { drops(h, false); }
    private static void drops(GameTestHelper h, boolean command) {
        var tile = station(h); tile.setItem(0, pack(0, RotaryFluids.JET_FUEL.get(), 7)); tile.setItem(1, new ItemStack(RotaryItems.ETHANOL.get(), 3)); tile.setItem(2, pack(1, RotaryFluids.JET_FUEL.get(), 30000)); tile.setItem(3, pack(2, RotaryFluids.JET_FUEL.get(), 31));
        if (command) h.setBlock(POS, Blocks.AIR);
        else {
            var player = (net.minecraft.server.level.ServerPlayer)h.makeMockServerPlayer(GameType.SURVIVAL); player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_PICKAXE));
            h.assertTrue(player.gameMode.destroyBlock(tile.getBlockPos()), "station must harvest in survival");
        }
        var drops = h.getEntities(EntityTypes.ITEM, POS, 3);
        for (Item item : List.of(RotaryItems.JETPACK.get(), RotaryItems.HSLA_STEEL_PACK.get(), RotaryItems.BEDROCK_ALLOY_PACK.get())) h.assertTrue(drops.stream().filter(e -> e.getItem().is(item)).mapToInt(e -> e.getItem().getCount()).sum() == 1, "each stored pack must drop exactly once");
        h.assertTrue(drops.stream().filter(e -> e.getItem().is(RotaryItems.ETHANOL.get())).mapToInt(e -> e.getItem().getCount()).sum() == 3, "stored crystals must drop exactly once"); h.succeed();
    }
}
