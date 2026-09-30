package reika.rotarycraft;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.TagValueInput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import reika.rotarycraft.blockentities.farming.BlockEntityGroundHydrator;
import reika.rotarycraft.blockentities.processing.BlockEntityDryingBed;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryItems;

/** Unpowered production, fluid conservation, automation and survival persistence. */
final class RotaryUnpoweredTests {
    private static final BlockPos POS = new BlockPos(4, 1, 4);
    private RotaryUnpoweredTests() {}

    private static void water(GameTestHelper helper, int amount) {
        var handler = helper.getLevel().getCapability(Capabilities.Fluid.BLOCK, helper.absolutePos(POS), Direction.WEST);
        helper.assertTrue(handler != null, "fluid receiver must expose a filling capability");
        try (Transaction transaction = Transaction.openRoot()) {
            helper.assertTrue(handler.insert(FluidResource.of(Fluids.WATER), amount, transaction) == amount, "must accept the expected water amount");
            transaction.commit();
        }
    }

    private static BlockEntityDryingBed drying(GameTestHelper helper, int salt, int fluid) {
        helper.setBlock(POS, RotaryBlocks.DRYING.get());
        var machine = helper.getBlockEntity(POS, BlockEntityDryingBed.class);
        if (salt > 0) machine.itemHandler.setStackInSlot(0, new ItemStack(RotaryItems.SALT.get(), salt));
        water(helper, fluid);
        return machine;
    }

    static void dryingStacksOutput(GameTestHelper helper) {
        var machine = drying(helper, 2, 2000);
        helper.runAfterDelay(410, () -> {
            helper.assertTrue(machine.getStackInSlot(0).is(RotaryItems.SALT.get()) && machine.getStackInSlot(0).getCount() == 10,
                    "eight water batches must add eight salt to the existing two, got " + machine.getStackInSlot(0));
            helper.assertTrue(machine.getFluidLevel() == 0, "eight salt must consume exactly 2000 mB");
            helper.succeed();
        });
    }

    static void dryingBackpressure(GameTestHelper helper) {
        var machine = drying(helper, 63, 2000);
        helper.runAfterDelay(410, () -> {
            helper.assertTrue(machine.getStackInSlot(0).getCount() == 64 && machine.getFluidLevel() == 1750,
                    "one free output space must consume exactly one 250 mB batch");
            helper.succeed();
        });
    }

    static void dryingSaveReload(GameTestHelper helper) {
        var machine = drying(helper, 5, 1000);
        helper.runAfterDelay(50, () -> {
            var saved = machine.saveWithoutMetadata(helper.getLevel().registryAccess());
            // Chunk loading restores a newly constructed entity before attaching its level.
            var detached = new BlockEntityDryingBed(helper.absolutePos(POS), machine.getBlockState());
            detached.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, helper.getLevel().registryAccess(), saved));
            helper.assertTrue(detached.getStackInSlot(0).getCount() == 5, "detached chunk loading must preserve registry-backed items");
            var existingHandler = machine.getItemHandler();
            machine.itemHandler.setStackInSlot(0, ItemStack.EMPTY);
            machine.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, helper.getLevel().registryAccess(), saved));
            helper.assertTrue(machine.getStackInSlot(0).getCount() == 5 && machine.getFluidLevel() == 1000,
                    "both dried output and remaining fluid must survive reload");
            helper.assertTrue(existingHandler == machine.getItemHandler(), "reload must preserve the live automation handler");
            helper.runAfterDelay(360, () -> {
                helper.assertTrue(machine.getStackInSlot(0).getCount() == 9 && machine.getFluidLevel() == 0,
                        "saved drying progress must resume rather than start again");
                helper.succeed();
            });
        });
    }

    static void dryingAutomation(GameTestHelper helper) {
        var machine = drying(helper, 5, 1000);
        helper.assertTrue(helper.getLevel().getCapability(Capabilities.Fluid.BLOCK, helper.absolutePos(POS), Direction.DOWN) == null,
                "V33a drying beds must reject bottom filling");
        for (Direction side : Direction.values()) {
            var items = helper.getLevel().getCapability(Capabilities.Item.BLOCK, helper.absolutePos(POS), side);
            helper.assertTrue(items != null, "dried output must be accessible from every side");
            try (Transaction transaction = Transaction.openRoot()) {
                helper.assertTrue(items.insert(ItemResource.of(Items.SAND), 1, transaction) == 0, "automation cannot insert items into the output pan");
                helper.assertTrue(items.extract(ItemResource.of(RotaryItems.SALT.get()), 5, transaction) == 5, "output extraction must be available");
            }
            helper.assertTrue(machine.getStackInSlot(0).getCount() == 5, "aborted extraction must restore the pan");
        }
        var items = helper.getLevel().getCapability(Capabilities.Item.BLOCK, helper.absolutePos(POS), Direction.UP);
        try (Transaction transaction = Transaction.openRoot()) {
            items.extract(ItemResource.of(RotaryItems.SALT.get()), 5, transaction);
            transaction.commit();
        }
        helper.assertTrue(machine.getStackInSlot(0).isEmpty(), "committed output extraction must empty the pan");
        helper.succeed();
    }

    static void dryingSurvivalBreak(GameTestHelper helper) {
        drying(helper, 5, 1000);
        var world = helper.getLevel();
        var absolute = helper.absolutePos(POS);
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        var state = world.getBlockState(absolute);
        state.getBlock().onDestroyedByPlayer(state, world, absolute, player, new ItemStack(Items.DIAMOND_PICKAXE), true, state.getFluidState());
        int salt = world.getEntitiesOfClass(ItemEntity.class, new net.minecraft.world.phys.AABB(absolute).inflate(2)).stream()
                .filter(entity -> entity.getItem().is(RotaryItems.SALT.get())).mapToInt(entity -> entity.getItem().getCount()).sum();
        helper.assertTrue(salt == 5, "survival breaking must drop all five dried items exactly once");
        helper.succeed();
    }

    private static BlockEntityGroundHydrator hydrator(GameTestHelper helper, int fluid) {
        for (int x = 1; x <= 7; x++) for (int z = 1; z <= 7; z++) {
            if (x == 4 && z == 4) continue;
            helper.setBlock(new BlockPos(x, 1, z), Blocks.FARMLAND.defaultBlockState().setValue(FarmlandBlock.MOISTURE, 0));
            helper.setBlock(new BlockPos(x, 2, z), Blocks.WHEAT);
            helper.setBlock(new BlockPos(x, 3, z), Blocks.STONE);
        }
        helper.setBlock(POS, RotaryBlocks.HYDRATOR.get());
        var machine = helper.getBlockEntity(POS, BlockEntityGroundHydrator.class);
        water(helper, fluid);
        return machine;
    }

    private static int wetFarmland(GameTestHelper helper) {
        int wet = 0;
        for (int x = 1; x <= 7; x++) for (int z = 1; z <= 7; z++) {
            var state = helper.getBlockState(new BlockPos(x, 1, z));
            if (state.is(Blocks.FARMLAND) && state.getValue(FarmlandBlock.MOISTURE) > 0) wet++;
        }
        return wet;
    }

    static void hydratorWatersFarmland(GameTestHelper helper) {
        var machine = hydrator(helper, 1000);
        helper.runAfterDelay(120, () -> {
            int used = 1000 - machine.getFluidLevel();
            helper.assertTrue(wetFarmland(helper) > 0, "hydrator must actually moisten surrounding farmland");
            helper.assertTrue(used > 0 && used % BlockEntityGroundHydrator.FLUID_PER_BLOCK == 0,
                    "hydrating must consume complete 25 mB doses");
            helper.succeed();
        });
    }

    static void hydratorInsufficientWater(GameTestHelper helper) {
        var machine = hydrator(helper, 24);
        helper.runAfterDelay(120, () -> {
            helper.assertTrue(machine.getFluidLevel() == 24 && wetFarmland(helper) == 0,
                    "less than one hydration dose must preserve water and dry farmland");
            helper.succeed();
        });
    }
}
