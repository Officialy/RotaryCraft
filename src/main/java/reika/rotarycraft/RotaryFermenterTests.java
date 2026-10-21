package reika.rotarycraft;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.production.BlockEntityFermenter;
import reika.rotarycraft.blockentities.engine.BlockEntitySteamEngine;
import reika.rotarycraft.gui.container.machine.inventory.ContainerFermenter;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryItems;
import reika.rotarycraft.registry.RotaryRecipeTypes;
import reika.rotarycraft.auxiliary.recipemanagers.FermenterRecipe;

final class RotaryFermenterTests {
    private static final BlockPos POS = new BlockPos(4, 1, 4);
    private RotaryFermenterTests() {}

    private static BlockEntityFermenter machine(GameTestHelper h) {
        h.setBlock(POS, RotaryBlocks.FERMENTER.get().defaultBlockState()
                .setValue(BlockRotaryCraftMachine.FACING, Direction.EAST));
        var tile = h.getBlockEntity(POS, BlockEntityFermenter.class);
        tile.setLiquid(4000);
        tile.setTemperature(25);
        return tile;
    }

    static void yeast(GameTestHelper h) {
        RotaryPowerTests.coil(h, 3, 4, 32, 1 << 20);
        var tile = machine(h);
        tile.itemHandler.setStackInSlot(0, new ItemStack(Items.SUGAR, 64));
        tile.itemHandler.setStackInSlot(1, new ItemStack(Items.DIRT, 64));
        h.runAfterDelay(5, () -> {
            RotaryPowerTests.assertPower(h, tile, 32, 1 << 20, "fermenter input");
            h.assertTrue(tile.itemHandler.getStackInSlot(2).is(RotaryItems.YEAST.get()), "sugar+dirt must produce yeast");
            h.assertTrue(tile.getLiquidLevel() < 4000, "each operation must consume water");
            h.succeed();
        });
    }

    static void steamPower(GameTestHelper h) {
        h.setBlock(POS.west(), RotaryBlocks.STEAM_ENGINE.get().defaultBlockState()
                .setValue(BlockRotaryCraftMachine.FACING, Direction.EAST));
        var engine = h.getBlockEntity(POS.west(), BlockEntitySteamEngine.class);
        engine.addWater(12000);
        engine.setTemperature(110);
        var tile = machine(h);
        tile.itemHandler.setStackInSlot(0, new ItemStack(Items.SUGAR, 64));
        tile.itemHandler.setStackInSlot(1, new ItemStack(Items.DIRT, 64));
        h.succeedWhen(() -> {
            h.assertTrue(engine.power > 0, "steam engine must run");
            h.assertTrue(tile.power == engine.power && tile.omega == engine.omega && tile.torque == engine.torque,
                    "fermenter must read the real steam engine output");
        });
    }

    static void automation(GameTestHelper h) {
        var tile = machine(h);
        var yeast = ItemResource.of(new ItemStack(RotaryItems.YEAST.get()));
        for (Direction side : Direction.values()) {
            var handler = h.getLevel().getCapability(Capabilities.Item.BLOCK, tile.getBlockPos(), side);
            h.assertTrue(handler != null, "every side must expose inventory");
            try (var tx = Transaction.openRoot()) {
                h.assertTrue(handler.insert(2, yeast, 1, tx) == 0, "hopper must never insert output");
                h.assertTrue(handler.insert(0, ItemResource.of(Items.SUGAR), 8, tx) == 8, "unpowered mode accepts sugar");
                h.assertTrue(handler.insert(1, ItemResource.of(Items.DIRT), 8, tx) == 8, "unpowered mode accepts dirt");
                h.assertTrue(handler.extract(0, ItemResource.of(Items.SUGAR), 8, tx) == 0, "hopper must not drain sugar");
                h.assertTrue(handler.extract(1, ItemResource.of(Items.DIRT), 8, tx) == 0, "hopper must not drain dirt");
            }
            h.assertTrue(tile.itemHandler.getStackInSlot(0).isEmpty(), "aborted inserts must roll back");
        }
        var handler = h.getLevel().getCapability(Capabilities.Item.BLOCK, tile.getBlockPos(), Direction.DOWN);
        h.setBlock(POS.east(), Blocks.REDSTONE_BLOCK);
        try (var tx = Transaction.openRoot()) {
            h.assertTrue(handler.insert(0, ItemResource.of(Items.SUGAR), 1, tx) == 0, "live mode change must reject sugar");
            h.assertTrue(handler.insert(0, yeast, 8, tx) == 8, "redstone mode accepts yeast");
            h.assertTrue(handler.insert(1, ItemResource.of(Items.OAK_LEAVES), 8, tx) == 8, "redstone mode accepts mulch plants");
            h.assertTrue(handler.insert(1, ItemResource.of(Items.DIAMOND), 1, tx) == 0, "nonplants must be rejected");
            tx.commit();
        }
        tile.itemHandler.setStackInSlot(2, new ItemStack(RotaryItems.SLUDGE.get(), 2));
        try (var tx = Transaction.openRoot()) {
            h.assertTrue(handler.extract(ItemResource.of(new ItemStack(RotaryItems.SLUDGE.get())), 2, tx) == 2,
                    "hopper must extract output");
            h.assertTrue(handler.extract(yeast, 8, tx) == 0, "bulk extraction must not bypass the input lock");
            tx.commit();
        }
        h.assertTrue(tile.itemHandler.getStackInSlot(0).getCount() == 8 && tile.itemHandler.getStackInSlot(2).isEmpty(),
                "committed output extraction must leave inputs intact");
        h.succeed();
    }

    static void menu(GameTestHelper h) {
        var tile = machine(h);
        var player = h.makeMockPlayer(GameType.SURVIVAL);
        var menu = new ContainerFermenter(1, player.getInventory(), tile);
        player.getInventory().setItem(9, new ItemStack(Items.SUGAR, 12));
        menu.quickMoveStack(player, 3);
        h.assertTrue(tile.itemHandler.getStackInSlot(0).getCount() == 12 && player.getInventory().getItem(9).isEmpty(),
                "shift sugar into catalyst slot");
        player.getInventory().setItem(9, new ItemStack(Items.DIRT, 12));
        menu.quickMoveStack(player, 3);
        h.assertTrue(tile.itemHandler.getStackInSlot(1).getCount() == 12, "shift dirt into feedstock slot");
        menu.quickMoveStack(player, 0);
        menu.quickMoveStack(player, 1);
        h.assertTrue(tile.itemHandler.getStackInSlot(0).isEmpty() && tile.itemHandler.getStackInSlot(1).isEmpty(), "shift inputs back out");
        h.setBlock(POS.east(), Blocks.REDSTONE_BLOCK);
        h.assertTrue(menu.isSludgeMode(), "target must select sludge on redstone");
        player.getInventory().setItem(9, new ItemStack(RotaryItems.YEAST.get(), 8));
        menu.quickMoveStack(player, 3);
        player.getInventory().setItem(9, new ItemStack(Items.OAK_LEAVES, 8));
        menu.quickMoveStack(player, 3);
        h.assertTrue(tile.itemHandler.getStackInSlot(0).is(RotaryItems.YEAST.get())
                && tile.itemHandler.getStackInSlot(1).is(Items.OAK_LEAVES), "mode must route yeast and leaves to correct slots");
        h.assertTrue(!menu.getSlot(2).mayPlace(new ItemStack(Items.DIRT)), "GUI output rejects items too");
        tile.itemHandler.setStackInSlot(2, new ItemStack(RotaryItems.YEAST.get(), 3));
        h.assertTrue(menu.quickMoveStack(player, 2).getCount() == 3 && tile.itemHandler.getStackInSlot(2).isEmpty(),
                "shift-click must extract the output into the player inventory");
        menu.setData(2, 1234);
        menu.setData(5, 0xFFFF); menu.setData(6, 0x10);
        menu.setData(7, 0xFFFF); menu.setData(8, 1);
        menu.setData(9, 0xFFFF); menu.setData(10, 0xFFFF); menu.setData(11, 1); menu.setData(12, 0);
        h.assertTrue(tile.getLiquidLevel() == 1234 && tile.omega == 0x10FFFF && tile.torque == 0x1FFFF
                && tile.power == 0x1FFFFFFFFL, "water and full-width power values must synchronize");
        h.succeed();
    }

    static void plants(GameTestHelper h) {
        var tile = machine(h);
        assertYield(h, new ItemStack(Items.OAK_LEAVES), 2);
        assertYield(h, new ItemStack(Items.OAK_SAPLING), 1);
        var rainbow = BuiltInRegistries.ITEM.getOptional(Identifier.fromNamespaceAndPath("chromaticraft", "rainbow_leaves"));
        if (rainbow.isPresent()) {
            assertYield(h, new ItemStack(rainbow.get()), 16);
            var sapling = BuiltInRegistries.ITEM.getOptional(Identifier.fromNamespaceAndPath("chromaticraft", "rainbow_sapling")).orElseThrow();
            assertYield(h, new ItemStack(sapling), 8);
        }
        RotaryPowerTests.coil(h, 3, 4, 32, 1 << 20);
        h.setBlock(POS.east(), Blocks.REDSTONE_BLOCK);
        tile.itemHandler.setStackInSlot(0, new ItemStack(RotaryItems.YEAST.get(), 64));
        tile.itemHandler.setStackInSlot(1, new ItemStack(rainbow.orElse(Items.OAK_LEAVES)));
        int expectedYield = rainbow.isPresent() ? 16 : 2;
        h.runAfterDelay(5, () -> {
            h.assertTrue(tile.itemHandler.getStackInSlot(2).getCount() == expectedYield
                    && tile.itemHandler.getStackInSlot(1).isEmpty(), "processing must honor plant yield exactly");
            h.succeed();
        });
    }

    private static void assertYield(GameTestHelper h, ItemStack input, int count) {
        var in = new FermenterRecipe.FermenterInput(new ItemStack(RotaryItems.YEAST.get()), input);
        var recipe = h.getLevel().getServer().getRecipeManager().recipeMap().byType(RotaryRecipeTypes.FERMENTER.get()).stream()
                .map(holder -> holder.value()).filter(r -> r.matches(in, h.getLevel())).findFirst().orElseThrow();
        h.assertTrue(recipe.assemble(in).getCount() == count, "plant yield must match source for " + input);
    }
}
