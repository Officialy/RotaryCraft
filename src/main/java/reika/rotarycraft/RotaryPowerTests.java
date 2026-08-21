package reika.rotarycraft;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import reika.rotarycraft.base.blockentity.BlockEntityIOMachine;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.transmission.BlockEntityCreativeCoil;
import reika.rotarycraft.blockentities.transmission.BlockEntityGearbox;
import reika.rotarycraft.registry.RotaryBlocks;

/**
 * Shared rig for the power-chain game tests.
 *
 * <p>Every RotaryCraft power link is derived from {@link BlockRotaryCraftMachine#FACING}: a
 * {@code BlockEntity1DTransmitter} facing EAST has {@code read = WEST, write = EAST}, while a
 * receiver such as the bedrock breaker sets {@code read = facing.getOpposite()}. {@code setBlock}
 * places the <em>default</em> state, so every machine in a chain needs its facing set explicitly or
 * the link silently never forms and the test fails with {@code torque == 0} and no explanation.
 * {@link #assertLink} exists so a mis-wired layout says so instead of just reading zero.
 *
 * <p>The arena ({@code rotarycraft:test_arena}) is 9x6x9 with a stone floor at y=0. Chains run
 * along +X at {@link #ROW_Y}; several independent chains fit side by side on different z rows,
 * which is how the per-ratio gearbox tests avoid accidentally chaining into each other.
 */
final class RotaryPowerTests {

    private RotaryPowerTests() {}

    /** Chains run along +X at this height. */
    static final int ROW_Y = 1;

    /** z rows that are far enough apart to hold independent chains. */
    static final int[] ROWS = {1, 3, 5, 7};

    static BlockPos at(int x, int z) {
        return new BlockPos(x, ROW_Y, z);
    }

    /** Places a machine with an explicit facing rather than relying on the default state. */
    static void place(GameTestHelper helper, int x, int z, Block block, Direction facing) {
        BlockState state = block.defaultBlockState();
        if (state.hasProperty(BlockRotaryCraftMachine.FACING))
            state = state.setValue(BlockRotaryCraftMachine.FACING, facing);
        helper.setBlock(at(x, z), state);
    }

    /**
     * A creative coil emitting a fixed torque/speed east, with its redstone source placed
     * <em>first</em> so the BE's tick-0 {@code redstoneInput} read already sees a signal — the coil
     * only releases while {@code enabled && hasRedstoneSignal()}.
     *
     * <p>The coil goes at column {@code x}; the redstone block goes at {@code x - 1}, which is also
     * the coil's read side and therefore harmless.
     */
    static BlockEntityCreativeCoil coil(GameTestHelper helper, int x, int z, int torque, int omega) {
        helper.setBlock(at(x - 1, z), Blocks.REDSTONE_BLOCK);
        place(helper, x, z, RotaryBlocks.CREATIVE_COIL.get(), Direction.EAST);
        BlockEntityCreativeCoil c = helper.getBlockEntity(at(x, z), BlockEntityCreativeCoil.class);
        c.setReleaseTorque(torque);
        c.setReleaseOmega(omega);
        return c;
    }

    /** Asserts {@code from} is actually wired to write into {@code to}, naming both if it is not. */
    static void assertLink(GameTestHelper helper, BlockEntityIOMachine from, BlockEntityIOMachine to, String label) {
        helper.assertTrue(from.getWriteDirection() != null,
                label + ": source at " + from.getBlockPos() + " has no write direction (check its FACING)");
        helper.assertTrue(from.isWritingTo(to),
                label + ": source at " + from.getBlockPos() + " writes " + from.getWriteDirection()
                        + ", which does not reach " + to.getBlockPos());
        helper.assertTrue(to.getReadDirection() != null,
                label + ": receiver at " + to.getBlockPos() + " has no read direction (check its FACING)");
    }

    static void assertPower(GameTestHelper helper, BlockEntityIOMachine te, int torque, int omega, String label) {
        helper.assertTrue(te.torque == torque, label + ": torque was " + te.torque + ", expected " + torque);
        helper.assertTrue(te.omega == omega, label + ": speed was " + te.omega + ", expected " + omega);
        helper.assertTrue(te.power == (long) torque * omega,
                label + ": power was " + te.power + ", expected " + ((long) torque * omega));
    }

    /**
     * Bedrock gearboxes for every ratio: bedrock needs no lubricant and has infinite strength, so a
     * short test cannot be perturbed by wear damage or a failure roll.
     */
    static Block gearbox(int ratio) {
        return switch (ratio) {
            case 4 -> RotaryBlocks.BEDROCK_GEARBOX_4x.get();
            case 8 -> RotaryBlocks.BEDROCK_GEARBOX_8x.get();
            case 16 -> RotaryBlocks.BEDROCK_GEARBOX_16x.get();
            default -> RotaryBlocks.BEDROCK_GEARBOX_2x.get();
        };
    }

    static BlockEntityGearbox gearboxAt(GameTestHelper helper, int x, int z) {
        return helper.getBlockEntity(at(x, z), BlockEntityGearbox.class);
    }
}
