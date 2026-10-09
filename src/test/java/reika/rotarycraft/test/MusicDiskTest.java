package reika.rotarycraft.test;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.junit.jupiter.api.Test;
import reika.rotarycraft.blockentities.decorative.BlockEntityMusicBox;
import reika.rotarycraft.registry.RotaryItems;
import reika.rotarycraft.items.ItemDisk;
import static org.junit.jupiter.api.Assertions.*;

class MusicDiskTest {
    @Test void originalDiskNotesRestsAndChannelsReachMusicScore() {
        var data = new CompoundTag(); var notes = new ListTag();
        notes.add(note(2,-1,1)); notes.add(note(1,12,1)); notes.add(note(4,14,4));
        data.put("ch3", notes);
        var stack = new ItemStack(new Holder.Direct<>(RotaryItems.DISK.get(),DataComponentMap.EMPTY),1);
        stack.set(DataComponents.CUSTOM_DATA,CustomData.of(data));
        var score = ((ItemDisk) stack.getItem()).getMusic(stack);
        assertEquals(2,score.noteCount()); assertEquals(12,score.getTrack(3).firstNoteTime());
        assertFalse(score.getNotes(3,12).iterator().next().percussion);
        assertTrue(score.getNotes(3,36).iterator().next().percussion);
        assertEquals(3,score.getNotes(3,36).iterator().next().length);
        assertEquals(data,stack.get(DataComponents.CUSTOM_DATA).copyTag());
        assertEquals(3,BlockEntityMusicBox.Note.getFromMusicScore(score.getNotes(3,12).iterator().next()).length().tickLength);
    }
    private static CompoundTag note(int length,int pitch,int voice) {
        var tag=new CompoundTag(); tag.putInt("len",length);tag.putInt("pch",pitch);tag.putInt("vc",voice);return tag;
    }
}
