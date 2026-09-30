package reika.rotarycraft;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.util.ProblemReporter;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import reika.dragonapi.instantiable.HybridTank;
import reika.rotarycraft.auxiliary.recipemanagers.DistilleryRecipe;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.processing.BlockEntityDistillery;
import reika.rotarycraft.blockentities.storage.BlockEntityReservoir;
import reika.rotarycraft.blockentities.transmission.BlockEntityCreativeCoil;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryFluids;
import reika.rotarycraft.registry.RotaryRecipeTypes;

/** Foreign-fluid tag fixtures are loaded only by GameTest runs, never included in release jars. */
final class RotaryDistilleryTests {
    private static final BlockPos MACHINE = new BlockPos(3, 2, 4);
    private RotaryDistilleryTests() {}

    private static BlockEntityCreativeCoil coil(GameTestHelper h, int torque, int omega) {
        h.setBlock(MACHINE.below().north(), Blocks.REDSTONE_BLOCK);
        h.setBlock(MACHINE.below(), RotaryBlocks.CREATIVE_COIL.get().defaultBlockState().setValue(BlockRotaryCraftMachine.FACING, Direction.DOWN));
        var coil = h.getBlockEntity(MACHINE.below(), BlockEntityCreativeCoil.class);
        coil.setReleaseTorque(torque);
        coil.setReleaseOmega(omega);
        return coil;
    }

    private static BlockEntityDistillery machine(GameTestHelper h) {
        h.setBlock(MACHINE, RotaryBlocks.DISTILLER.get());
        return h.getBlockEntity(MACHINE, BlockEntityDistillery.class);
    }

    private static ResourceHandler<FluidResource> handler(GameTestHelper h, Direction side) {
        var handler = h.getLevel().getCapability(Capabilities.Fluid.BLOCK, h.absolutePos(MACHINE), side);
        h.assertTrue(handler != null, "distiller fluid capability missing on " + side);
        return handler;
    }

    private static void fill(GameTestHelper h, Fluid fluid, int amount) {
        try (var transaction = Transaction.openRoot()) {
            h.assertTrue(handler(h, Direction.NORTH).insert(FluidResource.of(fluid), amount, transaction) == amount, "distiller must accept the fixture fluid");
            transaction.commit();
        }
    }

    private static void output(GameTestHelper h, BlockEntityDistillery machine, Fluid fluid, int amount) {
        var saved = machine.saveWithoutMetadata(h.getLevel().registryAccess());
        var tank = new HybridTank("distillerout", 6000);
        tank.setContents(amount, fluid);
        tank.writeToNBT(saved);
        machine.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, h.getLevel().registryAccess(), saved));
    }

    static void conversion(GameTestHelper h, int type) {
        boolean oil = type == 0;
        coil(h, oil ? 2048 : 512, oil ? 4 : 256);
        var machine = machine(h);
        Fluid input = oil ? Fluids.LAVA : type == 1 ? Fluids.WATER : RotaryFluids.ETHANOL.get();
        Fluid result = oil ? RotaryFluids.LUBRICANT.get() : RotaryFluids.ETHANOL.get();
        fill(h, input, 12);
        h.runAfterDelay(90, () -> {
            h.assertTrue(machine.power >= (oil ? 8192 : 131072) && machine.torque == (oil ? 2048 : 512), "creative coil must reach the recipe's real bottom power input");
            h.assertTrue(machine.getInputLevel() == 0 && machine.getOutputLevel() == (oil ? 72 : type == 1 ? 12 : 6)
                    && machine.getFluidInOutput().getFluid() == result, "original conversion ratio must be exact, input=" + machine.getInputLevel() + ", output=" + machine.getOutputLevel());
            h.succeed();
        });
    }

    static void gates(GameTestHelper h) {
        var coil = coil(h, 1024, 128);
        var machine = machine(h);
        fill(h, Fluids.LAVA, 1);
        h.startSequence().thenIdle(12).thenExecute(() -> {
            h.assertTrue(machine.getInputLevel() == 1 && machine.getOutputLevel() == 0, "oil conversion must require 2048 Nm even with sufficient power");
            coil.setReleaseTorque(2048);
            coil.setReleaseOmega(2);
        }).thenIdle(12).thenExecute(() -> {
            h.assertTrue(machine.getInputLevel() == 1 && machine.getOutputLevel() == 0, "oil conversion must require 8192 W even with sufficient torque");
            coil.setReleaseOmega(4);
        }).thenIdle(12).thenExecute(() -> {
            h.assertTrue(machine.getInputLevel() == 0 && machine.getOutputLevel() == 6, "conversion must resume when both requirements are met");
        }).thenSucceed();
    }

    static void noBottomPower(GameTestHelper h) {
        var coil = RotaryPowerTests.coil(h, 2, 4, 2048, 1024);
        // Move the distiller beside the coil at the same Y: side power must not be accepted.
        h.setBlock(new BlockPos(3, 1, 4), RotaryBlocks.DISTILLER.get());
        var machine = h.getBlockEntity(new BlockPos(3, 1, 4), BlockEntityDistillery.class);
        try (var tx = Transaction.openRoot()) { machine.getFluidHandler(Direction.NORTH).insert(FluidResource.of(Fluids.LAVA), 1, tx); tx.commit(); }
        h.runAfterDelay(12, () -> {
            h.assertTrue(coil.power > 0 && machine.power == 0 && machine.getInputLevel() == 1 && machine.getOutputLevel() == 0, "side power must not run a bottom-powered distiller");
            h.succeed();
        });
    }

    static void backpressure(GameTestHelper h, boolean incompatible) {
        coil(h, 2048, 4);
        var machine = machine(h);
        fill(h, Fluids.LAVA, 2);
        output(h, machine, incompatible ? RotaryFluids.ETHANOL.get() : RotaryFluids.LUBRICANT.get(), incompatible ? 1 : 5998);
        h.startSequence().thenIdle(12).thenExecute(() -> {
            h.assertTrue(machine.getInputLevel() == 2 && machine.getOutputLevel() == (incompatible ? 1 : 5998), "blocked output must preserve the whole input batch");
            output(h, machine, RotaryFluids.LUBRICANT.get(), 5994);
        }).thenIdle(12).thenExecute(() -> {
            h.assertTrue(machine.getInputLevel() == 1 && machine.getOutputLevel() == 6000, "exactly one six-mB batch must fit at the output capacity boundary");
        }).thenSucceed();
    }

    static void transactions(GameTestHelper h) {
        var machine = machine(h);
        for (Direction side : Direction.values()) {
            var handler = handler(h, side);
            try (var tx = Transaction.openRoot()) {
                h.assertTrue(handler.insert(FluidResource.of(Fluids.LAVA), 17, tx) == (side == Direction.UP ? 0 : 17), "top is output-only, all other faces are input");
                h.assertTrue(handler.insert(FluidResource.of(RotaryFluids.JET_FUEL.get()), 10, tx) == 0, "unknown fluids must be rejected");
                h.assertTrue(handler.extract(FluidResource.of(Fluids.LAVA), 17, tx) == 0, "input fluid must not be extractable");
            }
            h.assertTrue(machine.getInputLevel() == 0, "aborted insertion must restore the tank on " + side);
        }
        fill(h, Fluids.LAVA, 6000);
        try (var tx = Transaction.openRoot()) { h.assertTrue(handler(h, Direction.DOWN).insert(FluidResource.of(Fluids.LAVA), 1, tx) == 0, "input capacity is exactly 6000 mB"); }
        output(h, machine, RotaryFluids.LUBRICANT.get(), 18);
        try (var tx = Transaction.openRoot()) {
            h.assertTrue(handler(h, Direction.NORTH).extract(FluidResource.of(RotaryFluids.LUBRICANT.get()), 6, tx) == 0, "non-top sides must hide the output tank");
            h.assertTrue(handler(h, Direction.UP).extract(FluidResource.of(RotaryFluids.LUBRICANT.get()), 6, tx) == 6, "top extraction must access output");
        }
        h.assertTrue(machine.getOutputLevel() == 18, "aborted output extraction must roll back");
        try (var tx = Transaction.openRoot()) { handler(h, Direction.UP).extract(FluidResource.of(RotaryFluids.LUBRICANT.get()), 6, tx); tx.commit(); }
        h.assertTrue(machine.getOutputLevel() == 12 && machine.getInputLevel() == 6000, "committed extraction must remove only output");
        h.succeed();
    }

    static void saveReload(GameTestHelper h) {
        var machine = machine(h);
        fill(h, Fluids.LAVA, 500);
        output(h, machine, RotaryFluids.LUBRICANT.get(), 123);
        var saved = machine.saveWithoutMetadata(h.getLevel().registryAccess());
        saved.putInt("conversionTicks", 5);
        var reloaded = new BlockEntityDistillery(machine.getBlockPos(), machine.getBlockState());
        reloaded.setLevel(h.getLevel());
        reloaded.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, h.getLevel().registryAccess(), saved));
        h.assertTrue(reloaded.getInputLevel() == 500 && reloaded.getFluidInInput().getFluid() == Fluids.LAVA
                && reloaded.getOutputLevel() == 123 && reloaded.getFluidInOutput().getFluid() == RotaryFluids.LUBRICANT.get(), "both tank identities and amounts must survive reload");
        h.assertTrue(reloaded.saveWithoutMetadata(h.getLevel().registryAccess()).getIntOr("conversionTicks", -1) == 5, "conversion cadence must survive reload");
        try (var tx = Transaction.openRoot()) { reloaded.getFluidHandler(Direction.UP).extract(FluidResource.of(RotaryFluids.LUBRICANT.get()), 3, tx); tx.commit(); }
        h.assertTrue(reloaded.getOutputLevel() == 120, "reloaded capability must bind the restored tank");
        h.succeed();
    }

    static void pipes(GameTestHelper h) {
        var machine = machine(h);
        output(h, machine, RotaryFluids.LUBRICANT.get(), 1000);
        h.setBlock(MACHINE.above(), RotaryBlocks.HOSE.get());
        h.setBlock(MACHINE.above().east(), RotaryBlocks.RESERVOIR.get());
        var reservoir = h.getBlockEntity(MACHINE.above().east(), BlockEntityReservoir.class);
        h.runAfterDelay(30, () -> {
            h.assertTrue(machine.getOutputLevel() < 1000 && reservoir.getFluidLevel() > 0 && reservoir.getFluid().getFluid() == RotaryFluids.LUBRICANT.get(), "the real hose must extract top output into a reservoir");
            h.succeed();
        });
    }

    static void survivalBreak(GameTestHelper h) {
        machine(h);
        var player = (net.minecraft.server.level.ServerPlayer) h.makeMockServerPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_PICKAXE));
        h.assertTrue(player.gameMode.destroyBlock(h.absolutePos(MACHINE)), "survival pickaxes must harvest the distiller");
        h.assertTrue(h.getEntities(net.minecraft.world.entity.EntityTypes.ITEM, MACHINE, 2).stream()
                .filter(drop -> drop.getItem().is(RotaryBlocks.DISTILLER.get().asItem())).mapToInt(drop -> drop.getItem().getCount()).sum() == 1, "survival harvest must return exactly one distiller");
        h.succeed();
    }

    static void recipeRoundTrip(GameTestHelper h) {
        var recipes = h.getLevel().getServer().getRecipeManager().recipeMap().byType(RotaryRecipeTypes.DISTILLER.get());
        h.assertTrue(recipes.size() == 3, "all three original conversions must load");
        var buffer = new RegistryFriendlyByteBuf(io.netty.buffer.Unpooled.buffer(), h.getLevel().registryAccess());
        try {
            for (var holder : recipes) {
                var recipe = holder.value();
                buffer.clear();
                DistilleryRecipe.STREAM_CODEC.encode(buffer, recipe);
                var copy = DistilleryRecipe.STREAM_CODEC.decode(buffer);
                h.assertTrue(recipe.equals(copy) && !buffer.isReadable(), "network codec must preserve fluid tags, ratios and power requirements");
            }
        } finally { buffer.release(); }
        h.succeed();
    }
}
