package reika.rotarycraft;

import java.util.Arrays;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaTask;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.lua.ObjectArguments;
import dan200.computercraft.api.peripheral.IDynamicPeripheral;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.PeripheralCapability;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.transmission.BlockEntityCreativeCoil;
import reika.rotarycraft.blockentities.transmission.BlockEntityShaft;
import reika.rotarycraft.registry.RotaryBlocks;

/**
 * GameTests for the CC: Tweaked integration (DragonAPI CCHooks): machines are computer peripherals whose methods are
 * the 1.7.10 LuaMethods. Only loaded and registered when CC: Tweaked is present, which the family GameTest run
 * ({@code :TestInstance:runGameTest}) provides from TestInstance/run/mods.
 */
final class RotaryCCTests {
    private RotaryCCTests() {}

    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> env) {
        RotaryGameTests.register(event, env, "cc_machine_peripheral_methods", 60, RotaryCCTests::machinePeripheralMethods);
        RotaryGameTests.register(event, env, "cc_ignores_non_reika_blocks", 20, RotaryCCTests::ignoresOtherBlocks);
    }

    /** Runs main-thread tasks inline; a real computer yields to the server thread for them. */
    private static final ILuaContext CONTEXT = new ILuaContext() {
        @Override
        public long issueMainThreadTask(LuaTask task) {
            throw new UnsupportedOperationException("tests run tasks inline");
        }

        @Override
        public MethodResult executeMainThreadTask(LuaTask task) throws LuaException {
            return MethodResult.of(task.execute());
        }
    };

    private static IDynamicPeripheral peripheral(GameTestHelper h, BlockPos rel) {
        IPeripheral p = h.getLevel().getCapability(PeripheralCapability.get(), h.absolutePos(rel), Direction.UP);
        h.assertTrue(p instanceof IDynamicPeripheral, "the machine at " + rel + " must be a computer peripheral, was " + p);
        return (IDynamicPeripheral)p;
    }

    private static Object[] call(GameTestHelper h, IDynamicPeripheral p, String method, Object... args) throws LuaException {
        int idx = Arrays.asList(p.getMethodNames()).indexOf(method);
        h.assertTrue(idx >= 0, p.getType() + " must offer " + method + "(), offers " + Arrays.toString(p.getMethodNames()));
        return p.callMethod(null, CONTEXT, idx, new ObjectArguments(args)).getResult();
    }

    /** A coil driving a shaft: read the shaft's power, retune the coil, and see a bad call come back as a Lua error. */
    static void machinePeripheralMethods(GameTestHelper h) {
        int z = 4;
        BlockEntityCreativeCoil coil = RotaryPowerTests.coil(h, 2, z, 256, 64);
        BlockPos shaftPos = RotaryPowerTests.at(3, z);
        h.setBlock(shaftPos, RotaryBlocks.HSLA_SHAFT.get().defaultBlockState().setValue(BlockRotaryCraftMachine.FACING, Direction.EAST));
        BlockEntityShaft shaft = h.getBlockEntity(shaftPos, BlockEntityShaft.class);
        h.runAfterDelay(20, () -> {
            try {
                IDynamicPeripheral shaftP = peripheral(h, shaftPos);
                h.assertTrue("Shaft".equals(shaftP.getType()), "the 1.7.10 type name (class minus its affix) must be kept, got " + shaftP.getType());
                h.assertTrue(List.of(shaftP.getMethodNames()).contains("getCoords"), "DragonAPI's generic methods must be offered too");
                Object[] power = call(h, shaftP, "getPower");
                h.assertTrue(power.length == 3 && ((Number)power[2]).intValue() == shaft.omega && shaft.omega == 64
                        && ((Number)power[1]).intValue() == shaft.torque && ((Number)power[0]).longValue() == shaft.power,
                        "getPower must return [power, torque, speed] of the live shaft, got " + Arrays.toString(power));

                IDynamicPeripheral coilP = peripheral(h, RotaryPowerTests.at(2, z));
                h.assertTrue(coilP.equals(peripheral(h, RotaryPowerTests.at(2, z))), "repeat lookups must give an equal peripheral so computers do not re-attach");
                call(h, coilP, "setSpeed", 128.0);
                h.assertTrue(coil.getReleaseOmega() == 128, "setSpeed must retune the coil, release speed is " + coil.getReleaseOmega());

                try {
                    call(h, coilP, "setSpeed", "fast");
                    h.fail("a string speed must raise a Lua error");
                }
                catch (LuaException e) {
                    h.assertTrue(e.getMessage().contains("setSpeed"), "the Lua error must name the method, was: " + e.getMessage());
                }
                h.succeed();
            }
            catch (LuaException e) {
                h.fail("peripheral call failed: " + e.getMessage());
            }
        });
    }

    /** Only DragonAPI-based block entities answer; vanilla blocks keep whatever CC itself offers. */
    static void ignoresOtherBlocks(GameTestHelper h) {
        BlockPos pos = RotaryPowerTests.at(2, 2);
        h.setBlock(pos, Blocks.STONE);
        IPeripheral p = h.getLevel().getCapability(PeripheralCapability.get(), h.absolutePos(pos), Direction.UP);
        h.assertTrue(p == null, "stone must not be a peripheral, was " + p);
        h.succeed();
    }

}
