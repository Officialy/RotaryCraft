package reika.rotarycraft;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.processing.BlockEntityCompactor;
import reika.rotarycraft.blockentities.processing.BlockEntityCentrifuge;
import reika.rotarycraft.blockentities.processing.BlockEntityGrinder;
import reika.rotarycraft.blockentities.transmission.BlockEntityCreativeCoil;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryFluids;
import reika.rotarycraft.registry.RotaryItems;

/** Functional checks for established processing machines, powered through real creative coils. */
final class RotaryProcessingTests {
    private RotaryProcessingTests() {}

    static void compactorStacksOutput(GameTestHelper helper) {
        int z = 4;
        RotaryPowerTests.coil(helper, 2, z, 4096, 1 << 20);
        RotaryPowerTests.place(helper, 3, z, RotaryBlocks.COMPACTOR.get(), Direction.EAST);
        var machine = helper.getBlockEntity(RotaryPowerTests.at(3, z), BlockEntityCompactor.class);
        machine.setTemperature(850);
        machine.addPressure(560000 - machine.getPressure());
        for (int slot = 0; slot < 4; slot++) machine.itemHandler.setStackInSlot(slot, new ItemStack(Items.COAL, 2));
        machine.itemHandler.setStackInSlot(4, new ItemStack(RotaryItems.ANTHRACITE.get(), 2));
        helper.runAfterDelay(10, () -> {
            RotaryPowerTests.assertPower(helper, machine, 4096, 1 << 20, "compactor coil");
            helper.assertTrue(machine.itemHandler.getStackInSlot(4).getCount() == 6,
                    "two four-coal batches must add four anthracite to the existing two, got " + machine.itemHandler.getStackInSlot(4));
            for (int slot = 0; slot < 4; slot++) helper.assertTrue(machine.itemHandler.getStackInSlot(slot).isEmpty(), "compactor must consume one coal from each slot per batch");
            helper.succeed();
        });
    }

    static void compactorBackpressure(GameTestHelper helper) {
        RotaryPowerTests.coil(helper, 2, 4, 4096, 1 << 20);
        RotaryPowerTests.place(helper, 3, 4, RotaryBlocks.COMPACTOR.get(), Direction.EAST);
        var machine = helper.getBlockEntity(RotaryPowerTests.at(3, 4), BlockEntityCompactor.class);
        machine.setTemperature(850);
        machine.addPressure(560000 - machine.getPressure());
        for (int slot = 0; slot < 4; slot++) machine.itemHandler.setStackInSlot(slot, new ItemStack(Items.COAL));
        machine.itemHandler.setStackInSlot(4, new ItemStack(RotaryItems.ANTHRACITE.get(), 63));
        helper.runAfterDelay(10, () -> {
            RotaryPowerTests.assertPower(helper, machine, 4096, 1 << 20, "compactor coil");
            helper.assertTrue(machine.itemHandler.getStackInSlot(4).getCount() == 63, "two-item result must not overflow a 63-item stack");
            for (int slot = 0; slot < 4; slot++) helper.assertTrue(machine.itemHandler.getStackInSlot(slot).getCount() == 1, "blocked compactor must preserve every input");
            helper.succeed();
        });
    }

    static void grinderStacksOutput(GameTestHelper helper) {
        RotaryPowerTests.coil(helper, 2, 4, 128, 1 << 20);
        RotaryPowerTests.place(helper, 3, 4, RotaryBlocks.GRINDER.get(), Direction.EAST);
        var machine = helper.getBlockEntity(RotaryPowerTests.at(3, 4), BlockEntityGrinder.class);
        machine.itemHandler.setStackInSlot(0, new ItemStack(Items.BLAZE_ROD, 3));
        machine.itemHandler.setStackInSlot(1, new ItemStack(Items.BLAZE_POWDER));
        helper.runAfterDelay(10, () -> {
            helper.assertTrue(machine.itemHandler.getStackInSlot(0).isEmpty(), "grinder must consume all three rods");
            helper.assertTrue(machine.itemHandler.getStackInSlot(1).is(Items.BLAZE_POWDER)
                    && machine.itemHandler.getStackInSlot(1).getCount() == 10, "three rods yield nine powder plus the existing one");
            helper.succeed();
        });
    }

    static void compactorGates(GameTestHelper helper) {
        RotaryPowerTests.place(helper, 3, 4, RotaryBlocks.COMPACTOR.get(), Direction.EAST);
        var machine = helper.getBlockEntity(RotaryPowerTests.at(3, 4), BlockEntityCompactor.class);
        for (int slot = 0; slot < 4; slot++) machine.itemHandler.setStackInSlot(slot, new ItemStack(Items.COAL));
        machine.setTemperature(850);
        machine.addPressure(560000 - machine.getPressure());
        helper.runAfterDelay(5, () -> {
            helper.assertTrue(machine.itemHandler.getStackInSlot(4).isEmpty(), "unpowered compactor must stop");
            RotaryPowerTests.coil(helper, 2, 4, 4096, 1 << 20);
            machine.setTemperature(20);
            helper.runAfterDelay(5, () -> {
                helper.assertTrue(machine.itemHandler.getStackInSlot(4).isEmpty(), "cold compactor must stop");
                machine.setTemperature(850);
                machine.addPressure(100 - machine.getPressure());
                helper.runAfterDelay(5, () -> {
                    helper.assertTrue(machine.itemHandler.getStackInSlot(4).isEmpty(), "low-pressure compactor must stop");
                    machine.addPressure(560000 - machine.getPressure());
                    machine.itemHandler.setStackInSlot(2, new ItemStack(Items.DIAMOND));
                    helper.runAfterDelay(5, () -> {
                        helper.assertTrue(machine.itemHandler.getStackInSlot(4).isEmpty(), "mixed inputs must stop");
                        for (int slot = 0; slot < 4; slot++) helper.assertTrue(machine.itemHandler.getStackInSlot(slot).getCount() == 1, "every rejected batch must preserve its inputs");
                        machine.itemHandler.setStackInSlot(2, new ItemStack(Items.COAL));
                        helper.runAfterDelay(5, () -> {
                            helper.assertTrue(machine.itemHandler.getStackInSlot(4).is(RotaryItems.ANTHRACITE.get())
                                    && machine.itemHandler.getStackInSlot(4).getCount() == 2, "restoring all gates must produce a complete batch");
                            helper.succeed();
                        });
                    });
                });
            });
        });
    }

    static void compactorEnvironment(GameTestHelper helper) {
        BlockPos pos = RotaryPowerTests.at(3, 4);
        helper.setBlock(pos, RotaryBlocks.COMPACTOR.get());
        var machine = helper.getBlockEntity(pos, BlockEntityCompactor.class);
        var world = helper.getLevel();
        int ambient = reika.dragonapi.libraries.level.ReikaWorldHelper.getAmbientTemperatureAt(world, helper.absolutePos(pos));
        machine.setTemperature(ambient);
        helper.setBlock(pos.east(), Blocks.LAVA);
        machine.updateTemperature(world, helper.absolutePos(pos));
        helper.assertTrue(machine.getTemperature() == ambient + 4, "adjacent lava must add the original four degrees");
        helper.setBlock(pos.east(), Blocks.AIR);
        machine.setTemperature(ambient);
        helper.setBlock(pos.east(), Blocks.ICE);
        machine.updateTemperature(world, helper.absolutePos(pos));
        helper.assertTrue(machine.getTemperature() == ambient - 2, "adjacent ice must cool by two degrees");
        machine.setTemperature(850);
        helper.assertTrue(machine.getThermalDamage() == 8 && machine.getMultiplier() == 0.75F
                && !machine.allowHeatExtraction(), "legacy thermal damage and friction-heater settings");
        machine.addPressure(560000 - machine.getPressure());
        machine.omega = 0;
        int before = machine.getPressure();
        machine.updatePressure(world, helper.absolutePos(pos));
        helper.assertTrue(machine.getPressure() < before, "unpowered pressure must decay toward the world ambient pressure");
        helper.succeed();
    }

    /** A Craft Pattern programmed with {@code grid} (row-major 3x3, null = empty) exactly as the pattern GUI would. */
    static ItemStack programmedPattern(GameTestHelper helper, net.minecraft.world.item.Item... grid) {
        var dummy = new net.minecraft.world.inventory.AbstractContainerMenu(null, 0) {
            @Override public ItemStack quickMoveStack(net.minecraft.world.entity.player.Player p, int i) { return ItemStack.EMPTY; }
            @Override public boolean stillValid(net.minecraft.world.entity.player.Player p) { return true; }
        };
        var ic = new net.minecraft.world.inventory.TransientCraftingContainer(dummy, 3, 3);
        for (int i = 0; i < grid.length; i++)
            if (grid[i] != null) ic.setItem(i, new ItemStack(grid[i]));
        ItemStack pattern = new ItemStack(RotaryItems.CRAFT_PATTERN.get());
        reika.rotarycraft.items.tools.ItemCraftPattern.setRecipe(pattern, ic, helper.getLevel());
        return pattern;
    }

    /** V33a AutoCrafter, request mode: a pattern crafts from the inventory above into its output slot; automation can't take the pattern. */
    static void autoCrafterCraftsFromInventoryAbove(GameTestHelper helper) {
        int z = 4;
        RotaryPowerTests.coil(helper, 2, z, 128, 1 << 20);
        RotaryPowerTests.place(helper, 3, z, RotaryBlocks.CRAFTER.get(), Direction.EAST);
        BlockPos chestPos = RotaryPowerTests.at(3, z).above();
        helper.setBlock(chestPos, Blocks.CHEST);
        var chest = helper.getBlockEntity(chestPos, net.minecraft.world.level.block.entity.ChestBlockEntity.class);
        chest.setItem(0, new ItemStack(Items.OAK_PLANKS, 3));
        var crafter = helper.getBlockEntity(RotaryPowerTests.at(3, z), reika.rotarycraft.blockentities.processing.BlockEntityAutoCrafter.class);

        ItemStack pattern = programmedPattern(helper, null, Items.OAK_PLANKS, null, null, Items.OAK_PLANKS, null, null, null, null);
        helper.assertTrue(reika.rotarycraft.items.tools.ItemCraftPattern.getResult(pattern).is(Items.STICK)
                && reika.rotarycraft.items.tools.ItemCraftPattern.getResult(pattern).getCount() == 4, "the programmed pattern must decode its stick output, got " + reika.rotarycraft.items.tools.ItemCraftPattern.getResult(pattern));
        helper.assertTrue(crafter.isItemValidForSlot(0, pattern), "a programmed crafting pattern must be accepted");
        helper.assertTrue(!crafter.isItemValidForSlot(0, new ItemStack(RotaryItems.CRAFT_PATTERN.get())), "a blank pattern must be rejected");
        crafter.itemHandler.setStackInSlot(0, pattern);

        helper.runAfterDelay(60, () -> { //the ingredient source is re-read every 50 ticks
            helper.assertTrue(crafter.power >= crafter.MINPOWER, "crafter must be powered, has " + crafter.power);
            crafter.triggerCraftingCycle(0);
            ItemStack out = crafter.itemHandler.getStackInSlot(reika.rotarycraft.blockentities.processing.BlockEntityAutoCrafter.SIZE);
            helper.assertTrue(out.is(Items.STICK) && out.getCount() == 4, "one request must craft four sticks, got " + out);
            helper.assertTrue(chest.getItem(0).getCount() == 1, "two planks must be drawn from the chest above, left " + chest.getItem(0));
            crafter.triggerCraftingCycle(0);
            helper.assertTrue(crafter.itemHandler.getStackInSlot(reika.rotarycraft.blockentities.processing.BlockEntityAutoCrafter.SIZE).getCount() == 4,
                    "a request with one plank left must not craft");
            var auto = crafter.getAutomationItemHandler();
            try (Transaction tx = Transaction.openRoot()) {
                helper.assertTrue(auto.extract(0, auto.getResource(0), 1, tx) == 0, "automation must not pull the pattern");
                helper.assertTrue(auto.extract(reika.rotarycraft.blockentities.processing.BlockEntityAutoCrafter.SIZE,
                        auto.getResource(reika.rotarycraft.blockentities.processing.BlockEntityAutoCrafter.SIZE), 4, tx) == 4, "automation must pull the output");
            }
            helper.succeed();
        });
    }

    /** V33a Item Filter: automation may only insert template matches (not blacklisted) into the filtered slot, and only empty that slot. */
    static void itemFilterGatesAutomation(GameTestHelper helper) {
        int z = 4;
        RotaryPowerTests.coil(helper, 2, z, 128, 1 << 20);
        RotaryPowerTests.place(helper, 3, z, RotaryBlocks.ITEMFILTER.get(), Direction.EAST);
        var filter = helper.getBlockEntity(RotaryPowerTests.at(3, z), reika.rotarycraft.blockentities.BlockEntityItemFilter.class);
        filter.itemHandler.setStackInSlot(0, new ItemStack(Items.IRON_INGOT));
        helper.runAfterDelay(10, () -> {
            helper.assertTrue(filter.power >= filter.MINPOWER, "filter must be powered, has " + filter.power);
            helper.assertTrue(filter.getData() != null, "the template must build match data");
            var auto = filter.getAutomationItemHandler();
            try (Transaction tx = Transaction.openRoot()) {
                helper.assertTrue(auto.insert(net.neoforged.neoforge.transfer.item.ItemResource.of(Items.GOLD_INGOT), 4, tx) == 0, "a non-matching item must be refused");
                helper.assertTrue(auto.insert(0, net.neoforged.neoforge.transfer.item.ItemResource.of(Items.IRON_INGOT), 1, tx) == 0, "automation must not reach the template slot");
                helper.assertTrue(auto.insert(net.neoforged.neoforge.transfer.item.ItemResource.of(Items.IRON_INGOT), 4, tx) == 4, "a matching item must enter the filtered slot");
                helper.assertTrue(auto.extract(0, auto.getResource(0), 1, tx) == 0, "automation must not take the template");
            }
            filter.itemHandler.setStackInSlot(2, new ItemStack(Items.IRON_INGOT));
            helper.runAfterDelay(2, () -> {
                try (Transaction tx = Transaction.openRoot()) {
                    helper.assertTrue(auto.insert(net.neoforged.neoforge.transfer.item.ItemResource.of(Items.IRON_INGOT), 4, tx) == 0, "a blacklisted item must be refused");
                }
                helper.succeed();
            });
        });
    }

    private static BlockEntityCentrifuge centrifuge(GameTestHelper helper) {
        BlockPos coilPos = new BlockPos(3, 1, 4);
        helper.setBlock(coilPos.west(), Blocks.REDSTONE_BLOCK);
        // Vertical transmitter FACING identifies the read side; DOWN therefore writes UP.
        helper.setBlock(coilPos, RotaryBlocks.CREATIVE_COIL.get().defaultBlockState().setValue(BlockRotaryCraftMachine.FACING, Direction.DOWN));
        var coil = helper.getBlockEntity(coilPos, BlockEntityCreativeCoil.class);
        coil.setReleaseTorque(64);
        coil.setReleaseOmega(1 << 20);
        helper.setBlock(coilPos.above(), RotaryBlocks.CENTRIFUGE.get());
        return helper.getBlockEntity(coilPos.above(), BlockEntityCentrifuge.class);
    }

    static void centrifugeSeparatesItems(GameTestHelper helper) {
        var machine = centrifuge(helper);
        machine.itemHandler.setStackInSlot(0, new ItemStack(Items.WHEAT, 5));
        machine.itemHandler.setStackInSlot(1, new ItemStack(Items.WHEAT_SEEDS, 10));
        helper.runAfterDelay(10, () -> {
            RotaryPowerTests.assertPower(helper, machine, 64, 1 << 20, "centrifuge coil");
            helper.assertTrue(machine.itemHandler.getStackInSlot(0).isEmpty(), "centrifuge must consume five wheat");
            helper.assertTrue(machine.itemHandler.getStackInSlot(1).getCount() == 30, "five wheat yield twenty seeds plus the existing ten");
            helper.succeed();
        });
    }

    static void centrifugeFluidTransactions(GameTestHelper helper) {
        var machine = centrifuge(helper);
        machine.itemHandler.setStackInSlot(0, new ItemStack(RotaryItems.CANOLA_HUSKS.get(), 5));
        helper.runAfterDelay(10, () -> {
            RotaryPowerTests.assertPower(helper, machine, 64, 1 << 20, "centrifuge coil");
            helper.assertTrue(machine.itemHandler.getStackInSlot(0).isEmpty(), "centrifuge must consume five husks");
            var handler = helper.getLevel().getCapability(Capabilities.Fluid.BLOCK, machine.getBlockPos(), Direction.EAST);
            var lubricant = FluidResource.of(RotaryFluids.LUBRICANT.get());
            helper.assertTrue(handler != null && handler.getAmountAsInt(0) == 150, "five husks must yield 150 mB lubricant");
            try (Transaction transaction = Transaction.openRoot()) {
                helper.assertTrue(handler.extract(lubricant, 150, transaction) == 150, "lubricant output extraction");
            }
            helper.assertTrue(handler.getAmountAsInt(0) == 150, "aborted extraction must preserve lubricant");
            try (Transaction transaction = Transaction.openRoot()) {
                helper.assertTrue(handler.extract(lubricant, 150, transaction) == 150, "lubricant output extraction");
                transaction.commit();
            }
            helper.assertTrue(handler.getAmountAsInt(0) == 0, "committed extraction must empty lubricant");
            helper.succeed();
        });
    }
}
