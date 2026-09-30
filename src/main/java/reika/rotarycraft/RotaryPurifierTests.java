package reika.rotarycraft;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.util.ProblemReporter;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import reika.rotarycraft.auxiliary.recipemanagers.PurifierRecipe;
import reika.rotarycraft.blockentities.processing.BlockEntityPurifier;
import reika.rotarycraft.gui.container.machine.inventory.ContainerPurifier;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryItems;
import reika.rotarycraft.registry.RotaryRecipeTypes;

/** Real recipe, world ticks, coils, transactions and survival interactions for the steel purifier. */
final class RotaryPurifierTests {
    private static final BlockPos MACHINE = new BlockPos(3, 1, 4);
    private RotaryPurifierTests() {}

    private static BlockEntityPurifier machine(GameTestHelper helper, boolean powered) {
        if (powered) RotaryPowerTests.coil(helper, 2, 4, 64, 1 << 20);
        helper.setBlock(MACHINE, RotaryBlocks.PURIFIER.get());
        BlockEntityPurifier machine = helper.getBlockEntity(MACHINE, BlockEntityPurifier.class);
        machine.setItem(0, new ItemStack(Items.GUNPOWDER, 64));
        machine.setItem(7, new ItemStack(Items.SAND, 64));
        // Only the GameTest fixture adds this item to c:ingots/steel; release data leaves it empty.
        for (int slot = 1; slot <= 5; slot++) machine.setItem(slot, new ItemStack(Items.IRON_INGOT));
        machine.setTemperature(700);
        helper.assertTrue(machine.isItemValidForSlot(1, new ItemStack(Items.IRON_INGOT)),
                "the separate steel fixture and the real purification recipe must load");
        return machine;
    }

    static void batch(GameTestHelper helper) {
        var machine = machine(helper, true);
        helper.runAfterDelay(10, () -> {
            RotaryPowerTests.assertPower(helper, machine, 64, 1 << 20, "purifier coil");
            helper.assertTrue(machine.getItem(6).is(RotaryItems.HSLA_STEEL_INGOT.get())
                    && machine.getItem(6).getCount() == 5, "five input slots must yield exactly five HSLA ingots");
            for (int slot = 1; slot <= 5; slot++) helper.assertTrue(machine.getItem(slot).isEmpty(), "input must be consumed once");
            helper.assertTrue(machine.getItem(0).getCount() >= 63 && machine.getItem(7).getCount() >= 63,
                    "catalysts may lose at most one item per batch");
            helper.succeed();
        });
    }

    static void partialBatch(GameTestHelper helper) {
        var machine = machine(helper, true);
        machine.setItem(2, ItemStack.EMPTY);
        machine.setItem(4, new ItemStack(Items.DIAMOND));
        machine.setItem(6, new ItemStack(RotaryItems.HSLA_STEEL_INGOT.get(), 61));
        helper.runAfterDelay(10, () -> {
            helper.assertTrue(machine.getItem(6).getCount() == 64, "three valid input slots must top output up exactly to 64");
            helper.assertTrue(machine.getItem(4).is(Items.DIAMOND), "unmatched input must remain intact");
            helper.succeed();
        });
    }

    static void blockedOutput(GameTestHelper helper) {
        var machine = machine(helper, true);
        machine.setItem(6, new ItemStack(RotaryItems.HSLA_STEEL_INGOT.get(), 60));
        helper.runAfterDelay(10, () -> {
            assertUnconsumed(helper, machine);
            helper.assertTrue(machine.getItem(6).getCount() == 60 && machine.cookTime == 0,
                    "insufficient batch capacity must pause without consuming anything");
            machine.setItem(6, new ItemStack(Items.DIAMOND));
            helper.runAfterDelay(10, () -> {
                assertUnconsumed(helper, machine);
                helper.assertTrue(machine.getItem(6).is(Items.DIAMOND), "incompatible output must not be overwritten");
                helper.succeed();
            });
        });
    }

    static void gates(GameTestHelper helper) {
        var machine = machine(helper, false);
        helper.runAfterDelay(10, () -> {
            assertUnconsumed(helper, machine);
            helper.assertTrue(machine.getItem(6).isEmpty() && machine.cookTime == 0, "unpowered purifier must stop");
            RotaryPowerTests.coil(helper, 2, 4, 64, 1 << 20);
            machine.setTemperature(100);
            helper.runAfterDelay(10, () -> {
                assertUnconsumed(helper, machine);
                helper.assertTrue(machine.getItem(6).isEmpty(), "cold purifier must stop despite power");
                machine.setTemperature(700);
                machine.setItem(7, ItemStack.EMPTY);
                helper.runAfterDelay(10, () -> {
                    assertUnconsumed(helper, machine);
                    helper.assertTrue(machine.getItem(6).isEmpty(), "missing sand must prevent processing");
                    machine.setItem(7, new ItemStack(Items.SAND));
                    machine.setItem(0, ItemStack.EMPTY);
                    helper.runAfterDelay(10, () -> {
                        assertUnconsumed(helper, machine);
                        helper.assertTrue(machine.getItem(6).isEmpty(), "missing gunpowder must prevent processing");
                        helper.succeed();
                    });
                });
            });
        });
    }

    private static void assertUnconsumed(GameTestHelper helper, BlockEntityPurifier machine) {
        for (int slot = 1; slot <= 5; slot++) helper.assertTrue(machine.getItem(slot).getCount() == 1, "input slot " + slot + " changed");
    }

    static void transactions(GameTestHelper helper) {
        helper.setBlock(MACHINE, RotaryBlocks.PURIFIER.get());
        var machine = helper.getBlockEntity(MACHINE, BlockEntityPurifier.class);
        for (Direction side : Direction.values()) {
            var handler = helper.getLevel().getCapability(Capabilities.Item.BLOCK, helper.absolutePos(MACHINE), side);
            helper.assertTrue(handler != null, "item capability missing on " + side);
            try (Transaction transaction = Transaction.openRoot()) {
                helper.assertTrue(handler.insert(0, ItemResource.of(Items.GUNPOWDER), 1, transaction) == 1, "gunpowder insertion");
                helper.assertTrue(handler.insert(7, ItemResource.of(Items.SAND), 1, transaction) == 1, "sand insertion");
                helper.assertTrue(handler.insert(1, ItemResource.of(Items.IRON_INGOT), 1, transaction) == 1, "steel insertion");
                helper.assertTrue(handler.insert(6, ItemResource.of(Items.IRON_INGOT), 1, transaction) == 0, "output must reject insertion");
                helper.assertTrue(handler.insert(2, ItemResource.of(Items.DIAMOND), 1, transaction) == 0, "invalid item must be rejected");
                helper.assertTrue(handler.extract(1, ItemResource.of(Items.IRON_INGOT), 1, transaction) == 0, "automation must not extract input");
            }
            helper.assertTrue(machine.isEmpty(), "aborted transaction must restore inventory on " + side);
        }
        machine.setItem(6, new ItemStack(RotaryItems.HSLA_STEEL_INGOT.get(), 5));
        var handler = helper.getLevel().getCapability(Capabilities.Item.BLOCK, helper.absolutePos(MACHINE), Direction.DOWN);
        try (Transaction transaction = Transaction.openRoot()) {
            helper.assertTrue(handler.extract(6, ItemResource.of(RotaryItems.HSLA_STEEL_INGOT.get()), 5, transaction) == 5, "output extraction");
        }
        helper.assertTrue(machine.getItem(6).getCount() == 5, "aborted extraction must preserve output");
        try (Transaction transaction = Transaction.openRoot()) {
            helper.assertTrue(handler.extract(6, ItemResource.of(RotaryItems.HSLA_STEEL_INGOT.get()), 5, transaction) == 5, "output extraction");
            transaction.commit();
        }
        helper.assertTrue(machine.isEmpty(), "committed extraction must remove output");
        helper.succeed();
    }

    static void saveReload(GameTestHelper helper) {
        var machine = machine(helper, false);
        machine.cookTime = 137;
        var saved = machine.saveWithoutMetadata(helper.getLevel().registryAccess());
        machine.clearContent();
        machine.setTemperature(0);
        machine.cookTime = 0;
        machine.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, helper.getLevel().registryAccess(), saved));
        helper.assertTrue(machine.temperature == 700 && machine.cookTime == 137, "temperature and progress must persist");
        assertUnconsumed(helper, machine);
        helper.assertTrue(machine.getItem(0).getCount() == 64 && machine.getItem(7).getCount() == 64, "catalysts must persist");
        // Reload replaces the managed handler; external capabilities must still bind the current inventory.
        transactionsAfterReload(helper, machine);
        helper.succeed();
    }

    private static void transactionsAfterReload(GameTestHelper helper, BlockEntityPurifier machine) {
        machine.setItem(6, new ItemStack(RotaryItems.HSLA_STEEL_INGOT.get(), 3));
        var handler = helper.getLevel().getCapability(Capabilities.Item.BLOCK, helper.absolutePos(MACHINE), Direction.DOWN);
        try (Transaction transaction = Transaction.openRoot()) {
            helper.assertTrue(handler.extract(6, ItemResource.of(RotaryItems.HSLA_STEEL_INGOT.get()), 3, transaction) == 3, "reloaded capability");
            transaction.commit();
        }
        helper.assertTrue(machine.getItem(6).isEmpty(), "reloaded output must be removed exactly once");
    }

    static void environment(GameTestHelper helper) {
        var machine = machine(helper, false);
        machine.setTemperature(0);
        helper.setBlock(MACHINE.east(), Blocks.LAVA);
        machine.updateTemperature(helper.getLevel(), helper.absolutePos(MACHINE));
        helper.assertTrue(machine.temperature == 2, "lava must warm at the original two-degree rate");
        helper.setBlock(MACHINE.east(), Blocks.ICE);
        machine.setTemperature(500);
        machine.updateTemperature(helper.getLevel(), helper.absolutePos(MACHINE));
        helper.assertBlockPresent(Blocks.WATER, MACHINE.east());
        helper.assertTrue(machine.temperature == 498, "ice must cool the purifier and melt into water");
        helper.succeed();
    }

    static void menu(GameTestHelper helper) {
        var machine = machine(helper, false);
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        var menu = (ContainerPurifier) machine.createMenu(1, player.getInventory(), player);
        helper.assertTrue(menu.slots.size() == 44, "original eight machine slots plus player inventory");
        helper.assertTrue(menu.getSlot(0).mayPlace(new ItemStack(Items.GUNPOWDER))
                && !menu.getSlot(0).mayPlace(new ItemStack(Items.DIAMOND)), "gunpowder slot must validate items");
        helper.assertTrue(menu.getSlot(1).mayPlace(new ItemStack(Items.SAND)), "sand slot must validate items");
        helper.assertTrue(!menu.getSlot(7).mayPlace(new ItemStack(RotaryItems.HSLA_STEEL_INGOT.get())), "output slot must reject placement");
        machine.clearContent();
        player.getInventory().setItem(9, new ItemStack(Items.SAND, 12));
        menu.quickMoveStack(player, 8);
        helper.assertTrue(machine.getItem(7).getCount() == 12 && player.getInventory().getItem(9).isEmpty(), "shift-click must route sand to slot 7");
        menu.setData(2, 0xFFFF);
        menu.setData(3, 0x10);
        helper.assertTrue(machine.omega == 0x10FFFF, "speed sync must retain high bits");
        menu.setData(6, 0xFFFF);
        menu.setData(7, 0xFFFF);
        menu.setData(8, 1);
        menu.setData(9, 0);
        helper.assertTrue(machine.power == 0x1FFFFFFFFL, "power sync must retain values beyond 32 bits");
        helper.succeed();
    }

    static void survivalBreak(GameTestHelper helper) {
        var machine = machine(helper, false);
        var player = (net.minecraft.server.level.ServerPlayer) helper.makeMockServerPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_PICKAXE));
        helper.assertTrue(player.gameMode.destroyBlock(helper.absolutePos(MACHINE)), "survival player must break the purifier");
        int steel = 0, powder = 0, sand = 0, blocks = 0;
        for (var drop : helper.getEntities(EntityTypes.ITEM, MACHINE, 3)) {
            ItemStack item = drop.getItem();
            if (item.is(Items.IRON_INGOT)) steel += item.getCount();
            if (item.is(Items.GUNPOWDER)) powder += item.getCount();
            if (item.is(Items.SAND)) sand += item.getCount();
            if (item.is(RotaryBlocks.PURIFIER.get().asItem())) blocks += item.getCount();
        }
        helper.assertTrue(steel == 5 && powder == 64 && sand == 64 && blocks == 1,
                "break must drop inventory and machine exactly once: " + steel + "/" + powder + "/" + sand + "/" + blocks);
        helper.succeed();
    }

    static void recipeNetworkRoundTrip(GameTestHelper helper) {
        var recipe = helper.getLevel().getServer().getRecipeManager().recipeMap()
                .byType(RotaryRecipeTypes.PURIFIER.get()).iterator().next().value();
        var buffer = new RegistryFriendlyByteBuf(io.netty.buffer.Unpooled.buffer(), helper.getLevel().registryAccess());
        try {
            PurifierRecipe.STREAM_CODEC.encode(buffer, recipe);
            var decoded = PurifierRecipe.STREAM_CODEC.decode(buffer);
            helper.assertTrue(decoded.input().test(new ItemStack(Items.IRON_INGOT))
                    && !decoded.input().test(new ItemStack(Items.DIAMOND)), "network recipe must preserve the resolved steel ingredient");
            helper.assertTrue(decoded.gunpowder().test(new ItemStack(Items.GUNPOWDER))
                    && decoded.sand().test(new ItemStack(Items.SAND)), "network recipe must preserve both catalysts");
            helper.assertTrue(decoded.getResult().is(RotaryItems.HSLA_STEEL_INGOT.get())
                    && decoded.temperature() == 600 && decoded.gunpowderConsumption() == 25
                    && decoded.sandConsumption() == 5 && !buffer.isReadable(), "network recipe must preserve every requirement and output");
        } finally {
            buffer.release();
        }
        helper.succeed();
    }
}
