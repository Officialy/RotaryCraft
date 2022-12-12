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

import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import reika.dragonapi.instantiable.MusicScore;
import reika.dragonapi.interfaces.item.MusicDataItem;
import reika.rotarycraft.base.ItemRotaryTool;

import java.util.List;

public class ItemDisk extends ItemRotaryTool implements MusicDataItem {

    public ItemDisk() {
        super(new Properties());
    }


    public void addInformation(ItemStack is, Player ep, List li, boolean par4) {
        if (is.getTag() == null)
            return;
        li.add("Contains stored music:");
        for (int i = 0; i < 16; i++) {
            if (is.getTag().contains("ch" + i)) {
                ListTag track = is.getTag().getList("ch" + i, Tag.TAG_COMPOUND);
                if (track.size() > 0)
                    li.add("Track " + i + ": " + track.size() + " entries");
            }
        }
    }

    @Override
    public MusicScore getMusic(ItemStack is) {
        MusicScore mus = new MusicScore(16);
        int[] pos = new int[16];
        for (int i = 0; i < 16; i++) {
            if (is.getTag().contains("ch" + i)) {
                ListTag li = is.getTag().getList("ch" + i, Tag.TAG_COMPOUND);
//                for (int k = 0; k < li.size(); k++) {
//                    CompoundTag nbt = li.getCompound(k);
//                    //ReikaJavaLibrary.pConsole(i+":"+k+":"+nbt, Dist.DEDICATED_SERVER);
//                    BlockEntityMusicBox.Note n = BlockEntityMusicBox.Note.load(nbt);
//                    if (!n.isRest())
//                        mus.addNote(pos[i], i, n.getMusicKey(), n.voice.MIDIvalue, 128, n.getTickLength(), !n.voice.isPitched());
//                    pos[i] += n.getTickLength();
//                }
            }
        }
        return mus;
    }

}
