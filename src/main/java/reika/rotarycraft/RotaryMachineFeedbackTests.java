package reika.rotarycraft;

import java.util.ArrayList;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.rotarycraft.auxiliary.RotaryAux;
import reika.rotarycraft.blockentities.engine.BlockEntityDCEngine;
import reika.rotarycraft.items.MachineBlockItem;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlocks;

/** Regression contracts for machine sound/thermal feedback and inventory power data. */
final class RotaryMachineFeedbackTests {
    private RotaryMachineFeedbackTests() {}

    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> env) {
        RotaryGameTests.register(event, env, "machine_feedback_wool_heat", 20, RotaryMachineFeedbackTests::woolHeat);
        RotaryGameTests.register(event, env, "machine_feedback_dc_wool", 140, RotaryMachineFeedbackTests::dcWool);
        RotaryGameTests.register(event, env, "machine_feedback_coloured_mufflers", 20, RotaryMachineFeedbackTests::mufflers);
        RotaryGameTests.register(event, env, "machine_feedback_item_power", 20, RotaryMachineFeedbackTests::itemPower);
        RotaryGameTests.register(event, env, "machine_feedback_pump_sound_gate", 20, RotaryMachineFeedbackTests::pumpSoundGate);
    }

    private static void woolHeat(GameTestHelper h) {
        BlockPos origin = new BlockPos(4, 2, 4);
        for (var wool : Blocks.WOOL.asList()) {
            h.setBlock(origin.east(), wool);
            ReikaWorldHelper.temperatureEnvironment(h.getLevel(), h.absolutePos(origin), 29);
            h.assertBlockPresent(wool, origin.east());
        }
        h.setBlock(origin.east(), Blocks.SNOW_BLOCK);
        ReikaWorldHelper.temperatureEnvironment(h.getLevel(), h.absolutePos(origin), 29);
        h.assertBlockPresent(Blocks.AIR, origin.east());
        h.setBlock(origin.east(), Blocks.ICE);
        ReikaWorldHelper.temperatureEnvironment(h.getLevel(), h.absolutePos(origin), 29);
        h.assertBlockPresent(Blocks.WATER, origin.east());
        h.setBlock(origin.east(), Blocks.WOOL.white());
        ReikaWorldHelper.temperatureEnvironment(h.getLevel(), h.absolutePos(origin), 600);
        h.assertBlockPresent(Blocks.FIRE, origin.east().above());
        h.succeed();
    }

    private static void dcWool(GameTestHelper h) {
        BlockPos enginePos = RotaryPowerTests.at(4, 4);
        RotaryPowerTests.place(h, 4, 4, RotaryBlocks.DC_ENGINE.get(), Direction.EAST);
        h.setBlock(enginePos.west(), Blocks.REDSTONE_BLOCK);
        var engine = h.getBlockEntity(enginePos, BlockEntityDCEngine.class);
        engine.setTemperature(29);
        for (Direction side : Direction.values())
            if (side != Direction.WEST)
                h.setBlock(enginePos.relative(side), Blocks.WOOL.white());
        final BlockPos position = enginePos;
        h.runAfterDelay(100, () -> {
            h.assertTrue(engine.power > 0, "DC engine must run while wool is next to it");
            h.assertTrue(!engine.hasTemperature(), "DC engine must not advertise thermal state to Jade");
            for (Direction side : Direction.values())
                if (side != Direction.WEST)
                    h.assertBlockPresent(Blocks.WOOL.white(), position.relative(side));
            h.succeed();
        });
    }

    private static void mufflers(GameTestHelper h) {
        BlockPos pos = new BlockPos(4, 2, 4);
        h.setBlock(pos, RotaryBlocks.DC_ENGINE.get());
        var engine = h.getBlockEntity(pos, BlockEntityDCEngine.class);
        for (var wool : Blocks.WOOL.asList()) {
            h.setBlock(pos.above(), wool);
            h.setBlock(pos.below(), wool);
            h.assertTrue(engine.isMuffled(h.getLevel(), h.absolutePos(pos)), "All wool colours muffle engines");
            h.assertTrue(RotaryAux.isMuffled(engine), "All wool colours muffle machines");
        }
        h.setBlock(pos.above(), Blocks.SNOW_BLOCK);
        h.assertTrue(!RotaryAux.isMuffled(engine), "Snow must not count as wool");
        h.succeed();
    }

    private static void itemPower(GameTestHelper h) {
        for (int i = 0; i < MachineRegistry.machineList.length; i++) {
            var machine = MachineRegistry.machineList.get(i);
            if (!machine.isPowerReceiver() && !machine.isEngine() && machine != MachineRegistry.FUELENGINE)
                continue;
            ItemStack stack = machine.getBlockState().getBlock().asItem().getDefaultInstance();
            h.assertTrue(stack.getItem() instanceof MachineBlockItem, machine + " has no machine tooltip bridge");
            var normal = tooltip(stack, false);
            var shifted = tooltip(stack, true);
            h.assertTrue(shifted.equals(tooltip(stack, false, true)), machine + " must expose the same data to recipe tooltip indexing");
            if (normal.stream().anyMatch(s -> s.contains("Shift"))) {
                h.assertTrue(shifted.stream().noneMatch(s -> s.contains("Shift")), machine + " ignores Shift flag");
                h.assertTrue(shifted.stream().anyMatch(s -> s.contains("Power") || s.contains("Torque") || s.contains("Speed")),
                        machine + " missing power data on the actual registered item");
            }
        }
        var pump = tooltip(RotaryBlocks.PUMP.get().asItem().getDefaultInstance(), true);
        h.assertTrue(pump.stream().anyMatch(s -> s.startsWith("Minimum Power:")), "Pump must show minimum power");
        var dc = tooltip(RotaryBlocks.DC_ENGINE.get().asItem().getDefaultInstance(), true);
        h.assertTrue(dc.contains("Power: 1.024 kW") && dc.contains("Torque: 4.000 Nm") && dc.contains("Speed: 256.000 rad/s"),
                "DC tooltip must show its exact 1024 W / 4 Nm / 256 rad/s output: " + dc);
        h.succeed();
    }

    private static java.util.List<String> tooltip(ItemStack stack, boolean shift) {
        return tooltip(stack, shift, false);
    }

    private static java.util.List<String> tooltip(ItemStack stack, boolean shift, boolean allInformation) {
        var lines = new ArrayList<Component>();
        TooltipFlag flag = new TooltipFlag() {
            @Override public boolean isAdvanced() { return false; }
            @Override public boolean isCreative() { return false; }
            @Override public boolean hasShiftDown() { return shift; }
            @Override public boolean shouldDisplayAllInformation() { return allInformation; }
        };
        stack.getItem().appendHoverText(stack, Item.TooltipContext.EMPTY, TooltipDisplay.DEFAULT, lines::add, flag);
        return lines.stream().map(Component::getString).toList();
    }

    private static void pumpSoundGate(GameTestHelper h) {
        BlockPos pos = new BlockPos(4, 2, 4);
        h.setBlock(pos, RotaryBlocks.PUMP.get());
        var pump = h.getBlockEntity(pos, reika.rotarycraft.blockentities.production.BlockEntityPump.class);
        h.setBlock(pos.below(), Blocks.WATER);
        pump.power = pump.MINPOWER + 1;
        pump.torque = pump.MINTORQUE;
        h.assertTrue(pump.shouldPlayPumpSound(), "Powered pump above fluid must sound");
        pump.power = pump.MINPOWER;
        h.assertTrue(!pump.shouldPlayPumpSound(), "Preserve the original strict power threshold for sound");
        pump.power++;
        pump.torque--;
        h.assertTrue(!pump.shouldPlayPumpSound(), "Pump below required torque must be silent");
        pump.torque++;
        h.setBlock(pos.below(), Blocks.STONE);
        h.assertTrue(!pump.shouldPlayPumpSound(), "Pump without fluid below must be silent");
        h.succeed();
    }
}
