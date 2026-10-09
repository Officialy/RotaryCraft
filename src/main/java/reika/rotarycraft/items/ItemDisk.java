/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.items;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import reika.rotarycraft.blockentities.decorative.BlockEntityMusicBox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import reika.dragonapi.instantiable.MusicScore;
import reika.dragonapi.interfaces.item.MusicDataItem;
import reika.rotarycraft.base.ItemRotaryTool;
import reika.rotarycraft.registry.RotaryItems;

import java.util.List;

public class ItemDisk extends ItemRotaryTool implements MusicDataItem {

    public ItemDisk() {
        super(RotaryItems.itemProperties());
    }


    public void addInformation(ItemStack is, Player ep, List li, boolean par4) {
        if (is.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag() == null)
            return;
        li.add("Contains stored music:");
        for (int i = 0; i < 16; i++) {
            if (is.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().contains("ch" + i)) {
                ListTag track = is.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getListOrEmpty("ch" + i);
                if (track.size() > 0)
                    li.add("Track " + i + ": " + track.size() + " entries");
            }
        }
    }

    @Override
    public MusicScore getMusic(ItemStack stack) {
        MusicScore score = new MusicScore(16);
        CompoundTag data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        for (int channel = 0; channel < score.countTracks(); channel++) {
            ListTag track = data.getListOrEmpty("ch" + channel);
            int time = 0;
            for (int index = 0; index < track.size(); index++) {
                BlockEntityMusicBox.Note note = BlockEntityMusicBox.Note.load(track.getCompoundOrEmpty(index));
                if (!note.isRest()) {
                    score.addNote(time, channel, note.getMusicKey(), note.voice().MIDIvalue,
                            128, note.getTickLength(), !note.voice().isPitched());
                }
                time += note.getTickLength();
            }
        }
        return score;
    }
}
