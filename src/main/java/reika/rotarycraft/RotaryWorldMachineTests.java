package reika.rotarycraft;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.decorative.BlockEntityMusicBox;
import reika.rotarycraft.blockentities.level.BlockEntityBeamMirror;
import reika.rotarycraft.registry.RotaryBlocks;

/** Regressions for machines that act on the world or persist a separate world-owned file. */
final class RotaryWorldMachineTests {
    private RotaryWorldMachineTests() {}

    static void mirrorClearsBeam(GameTestHelper helper) {
        var world = helper.getLevel();
        var clock = world.registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.WORLD_CLOCK)
                .getOrThrow(net.minecraft.world.clock.WorldClocks.OVERWORLD);
        long previousTime = world.clockManager().getInstance(clock).totalTicks();
        world.clockManager().setTotalTicks(clock, 6000);
        BlockPos pos = new BlockPos(4, 1, 4);
        helper.setBlock(pos, RotaryBlocks.BEAM_MIRROR.get().defaultBlockState()
                .setValue(BlockRotaryCraftMachine.FACING, Direction.EAST));
        helper.setBlock(pos.relative(Direction.EAST, 3), Blocks.STONE);
        var mirror = helper.getBlockEntity(pos, BlockEntityBeamMirror.class);
        helper.runAfterDelay(5, () -> {
            helper.assertBlockPresent(Blocks.LIGHT, pos.east());
            helper.assertTrue(mirror.getRange() == 3, "solid stone must limit the beam range");
            helper.setBlock(pos.east(), Blocks.STONE);
            helper.runAfterDelay(5, () -> {
                helper.assertTrue(helper.getBlockEntity(pos, BlockEntityBeamMirror.class) == mirror,
                        "clearing an old beam must preserve its mirror");
                helper.assertTrue(mirror.getRange() == 1, "new obstruction must shorten the beam");
                helper.assertBlockPresent(Blocks.STONE, pos.east());
                helper.assertBlockPresent(Blocks.AIR, pos.relative(Direction.EAST, 2));
                helper.assertBlockPresent(Blocks.STONE, pos.relative(Direction.EAST, 3));
                world.clockManager().setTotalTicks(clock, previousTime);
                helper.succeed();
            });
        });
    }

    static void musicFileSaveReadBreak(GameTestHelper helper) {
        BlockPos pos = new BlockPos(4, 1, 4);
        helper.setBlock(pos, RotaryBlocks.MUSIC_BOX.get());
        var machine = helper.getBlockEntity(pos, BlockEntityMusicBox.class);
        var note = new BlockEntityMusicBox.Note(BlockEntityMusicBox.NoteLength.values()[0], 12,
                BlockEntityMusicBox.Instrument.values()[0]);
        machine.addNote(0, note);
        machine.save();
        helper.assertTrue(machine.hasSavedFile(), "music score must save under the server's world directory");
        machine.clearMusic();
        machine.read();
        helper.assertTrue(machine.getNotesInChannel(0).size() == 1, "saved channel must reload");
        var restored = machine.getNotesInChannel(0).get(0);
        helper.assertTrue(restored.pitch == note.pitch && restored.length == note.length
                && restored.voice == note.voice, "saved note must retain pitch, length and instrument");
        helper.getLevel().destroyBlock(helper.absolutePos(pos), true);
        helper.assertTrue(!machine.hasSavedFile(), "breaking a music box on a dedicated server must remove its score file");
        helper.succeed();
    }
}
