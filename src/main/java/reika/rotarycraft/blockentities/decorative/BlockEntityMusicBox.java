/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.decorative;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.NoteBlockEvent;
import org.jetbrains.annotations.Nullable;
import reika.dragonapi.instantiable.MusicScore;
import reika.dragonapi.instantiable.MusicScore.NoteData;
import reika.dragonapi.instantiable.MusicScore.ScoreTrack;
import reika.dragonapi.instantiable.io.MIDIInterface;
import reika.dragonapi.instantiable.io.PacketTarget;
import reika.dragonapi.interfaces.blockentity.BreakAction;
import reika.dragonapi.interfaces.blockentity.TriggerableAction;
import reika.dragonapi.io.ReikaFileReader;
import reika.dragonapi.libraries.io.ReikaChatHelper;
import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.dragonapi.libraries.java.ReikaJavaLibrary;
import reika.dragonapi.libraries.java.ReikaStringParser;
import reika.dragonapi.libraries.mathsci.ReikaMusicHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
import reika.rotarycraft.gui.container.machine.MusicContainer;
import reika.rotarycraft.registry.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class BlockEntityMusicBox extends BlockEntityPowerReceiver implements BreakAction, TriggerableAction {
    //todo container size is 5 max
    public static final int LOOPPOWER = 1024;
    private static final int[] channelColors = {
            0x3636FF, 0xD336FF, 0xFFACAC, 0xFF3636, 0xFFAC36, 0xD3D336,
            0x65BC8F, 0x36D336, 0x36FFFF, 0x58ABF9, 0x8484FF, 0xFF36FF,
            0x8436FF, 0xB49C8A, 0x8FA9B5, 0x94B581
    };
    /**
     * 16 channels, 7 voices, 64 pitch states
     */
    private final ArrayList<Note>[] musicQueue;
    private final int[] playDelay = new int[16];
    private final int[] playIndex = new int[16];
    private boolean isOneTimePlaying = false;

    public BlockEntityMusicBox(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.MUSIC_BOX.get(), pos, state);

        musicQueue = new ArrayList[16];
        for (int i = 0; i < 16; i++) {
            musicQueue[i] = new ArrayList<>();
        }
    }

    public static int getColorForChannel(int channel) {
        return channelColors[channel];
    }

    public int getMusicLength() {
        int size = -1;
        for (int i = 0; i < 16; i++) {
            size = Math.max(size, musicQueue[i].size());
        }
        return size;
    }

    public int getChannelLength(int channel) {
        return musicQueue[channel].size();
    }

    public ArrayList<Note> getNotesAtIndex(int index) {
        ArrayList<Note> li = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            if (musicQueue[i].size() > index)
                li.add(musicQueue[i].get(index));
        }
        return li;
    }

    public List<Note> getNotesInChannel(int channel) {
        return Collections.unmodifiableList(musicQueue[channel]);
    }

    @Override
    protected void onFirstTick(Level world, BlockPos pos) {
        if (this.hasSavedFile())
            this.read();
    }

    @Override
    public void updateBlockEntity() {
        Level world = this.getLevel();
        super.updateBlockEntity();
        this.getSummativeSidedPower();
        //ReikaJavaLibrary.pConsole(Arrays.toString(musicQueue), Dist.DEDICATED_SERVER);

        if (world.isClientSide()) {
            return;
        }

        if (power < LOOPPOWER) {
            if (!isOneTimePlaying) {
                this.startPlaying();
                return;
            }
        } else {
            isOneTimePlaying = false;
        }

        if (isOneTimePlaying || power >= LOOPPOWER) {
            for (int i = 0; i < 16; i++) {
                if (playDelay[i] > 0)
                    playDelay[i]--;
                if (playDelay[i] == 0) {
                    if (!musicQueue[i].isEmpty()) {
                        if (playIndex[i] < musicQueue[i].size()) {
                            Note n = musicQueue[i].get(playIndex[i]);
                            if (n != null) {
                                this.playNote(i, n);
                            }
                        }
                    } else {
                        playIndex[i] = 0;
                    }
                }
            }
        }
        //ReikaJavaLibrary.pConsole(Arrays.toString(playDelay)+":"+Arrays.toString(playIndex), Dist.DEDICATED_SERVER);
        if (this.isAtEnd() && this.hasNoDelays()) {
            this.resetPlayback();
        }
    }

//    @Override todo redstone stuff
    protected void onPositiveRedstoneEdge() {
        isOneTimePlaying = true;
        this.startPlaying();
    }

    private boolean hasNoDelays() {
        for (int i = 0; i < 16; i++) {
            if (playDelay[i] > 0)
                return false;
        }
        return true;
    }

    private boolean isAtEnd() {
        for (int i = 0; i < 16; i++) {
            if (playIndex[i] < musicQueue[i].size() - 1)
                return false;
        }
        return true;
    }

    private void resetPlayback() {
        for (int i = 0; i < 16; i++) {
            playIndex[i] = 0;
        }
        isOneTimePlaying = false;
    }

    private void startPlaying() {
        for (int i = 0; i < 16; i++) {
            playIndex[i] = 0;
            playDelay[i] = 0;
        }
    }

    private void playNote(int channel, Note n) {
        if (!n.isRest()) {
            for (int i = 0; i < 3; i++)
                n.play(level, worldPosition);
            ReikaPacketHelper.sendUpdatePacket(RotaryCraft.packetChannel, PacketRegistry.MUSICPARTICLE.ordinal(), this, new PacketTarget.RadiusTarget(this, 32));
        }
        playDelay[channel] = n.length.tickLength;
        playIndex[channel]++;
//        NoteBlockEvent.Play e = new NoteBlockEvent.Play(this, n.pitch, n.getTickLength(), channel);
//        MinecraftForge.EVENT_BUS.post(e); //todo noteblock event
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    public void addNote(int channel, Note n) {
        musicQueue[channel].add(n);
    }

    public void addRest(int channel, Note n) {
        n = n.getRest();
        musicQueue[channel].add(n);
    }

    public void backspace(int channel) {
        musicQueue[channel].remove(musicQueue[channel].size() - 1);
    }

    public void clearChannel(int channel) {
        musicQueue[channel].clear();
    }

    public void clearMusic() {
        for (int i = 0; i < 16; i++)
            this.clearChannel(i);
    }

    @Override
    public Block getBlockEntityBlockID() {
        return null;
    }

    @Override
    public void updateEntity(Level level, BlockPos blockPos) {

    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);

        NBT.putBoolean("onetime", isOneTimePlaying);
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);

        isOneTimePlaying = NBT.getBoolean("onetime");
    }

    @Override
    protected String getTEName() {
        return "musicbox";
    }


    public void loadLocalMIDI(String file) {
        File f = new File(file);
        if (!f.exists() || f.isDirectory()) {
            RotaryCraft.LOGGER.error("Could not load local MIDI: file is not a MIDI file!");
            return;
        }
        if (f.length() > 60000) {
            RotaryCraft.LOGGER.error("Could not load local MIDI: file is too large (" + f.length() + " bytes) and cannot be safely used!");
            return;
        }
        try {
            MusicScore mus = new MIDIInterface(f).fillToScore(true).scaleSpeed(11, true);
            this.dispatchTrack(mus);
        } catch (Exception e) {
            RotaryCraft.LOGGER.error(e.toString());
            e.printStackTrace();
        }
    }

    private void dispatchTrack(MusicScore mus) {
        //ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.MUSICCLEAR.ordinal(), this); todo noteblock packets
        for (int i = 0; i < mus.countTracks(); i++) {
            ScoreTrack track = mus.getTrack(i);
            if (track == null)
                continue;
            int lastNoteTime = -1;
            int lastNoteLength = -1;
            for (Entry<Integer, NoteData> e : track.entryView()) {
                int time = e.getKey();
                NoteData c = e.getValue();
                for (MusicScore.Note n : c.notes()) {
                    if (n != null && n.key != null) {
                        if (lastNoteTime >= 0) {
                            int t1 = lastNoteTime + lastNoteLength;
                            int dt = time - t1;
                            while (dt >= NoteLength.SIXTEENTH.tickLength) {
                                NoteLength l = NoteLength.getLargestNotMoreThan(dt);
                                t1 += l.tickLength;
                                dt = time - t1;
                                this.sendNote(0, i, l, Instrument.REST);
                            }
                        }
                        Note n2 = Note.getFromMusicScore(n);
                        this.sendNote(n2.pitch, i, n2.length, n2.voice);//ReikaPacketHelper.sendDataPacket(RotaryCraft.packetChannel, PacketRegistry.FIXEDMUSICNOTE.getMinValue(), this, i, n.key.ordinal(), n.length, n.voice == -1 ? 1 : 0, time);
                        lastNoteTime = time;
                        lastNoteLength = n2.length.tickLength;
                        break;
                    }
                }
            }
        }
    }

    public void save() {
        if (getLevel().isClientSide())
            return;
        try {
            File save = getLevel().getServer().getServerDirectory();//DimensionManager.getCurrentSaveRootDirectory(); //todo check if this is right
            String name = "musicbox@" + String.format("%d,%d,%d", worldPosition) + ".rcmusic";
            File dir = new File(save.getPath() + "/RotaryCraft/");
            if (!dir.exists())
                dir.mkdir();
            File f = new File(save.getPath() + "/RotaryCraft/" + name);
            if (f.exists())
                f.delete();
            f.createNewFile();
            PrintWriter p = new PrintWriter(f);
            f.createNewFile();

            int length = this.getMusicLength();
            for (int i = 0; i < length; i++) {
                for (int k = 0; k < 16; k++) {
                    if (musicQueue[k].size() > i) {
                        Note n = musicQueue[k].get(i);
                        String s = n.toSerialString();
                        p.append(s + ";");
                    } else {
                        p.append("-;");
                    }
                }
                p.append("\n");
            }

            p.close();
        } catch (Exception e) {
            ReikaChatHelper.write(e.getCause() + " caused the save to fail!");
            e.printStackTrace();
        }
    }

    public boolean hasSavedFile() {
        if (level.isClientSide())
            return false;
        File save = getLevel().getServer().getServerDirectory();//DimensionManager.getCurrentSaveRootDirectory(); //todo check if this is right
        String base = save.getPath();
        String name = "musicbox@" + String.format("%d,%d,%d", worldPosition) + ".rcmusic";
        File f = new File(base + "/RotaryCraft/" + name);
        return f.exists();
    }

    public void read() {
        if (level.isClientSide())
            return;
        File save = getLevel().getServer().getServerDirectory();//DimensionManager.getCurrentSaveRootDirectory(); //todo check if this is right
        //ReikaJavaLibrary.pConsole(musicFile);
        String name = "musicbox@" + String.format("%d,%d,%d", worldPosition) + ".rcmusic";
        String path = save.getPath() + "/RotaryCraft/" + name;
        this.readFile(path, false);
    }

    private void readFile(String path, boolean internal) {
        this.clearMusic();
        int linecount = -1;
        try (BufferedReader p = internal ? new BufferedReader(new InputStreamReader(RotaryCraft.class.getResourceAsStream(path))) : ReikaFileReader.getReader(path, Charset.defaultCharset())) {
            String line = p.readLine();
            while (line != null) {
                linecount++;
                String[] pieces = line.split(";");
                for (int i = 0; i < 16; i++) {
                    Note n = Note.getFromSerialString(pieces[i]);
                    if (n != null) {
                        musicQueue[i].add(n);
                        //ReikaJavaLibrary.pConsole(n);
                    }
                }
                line = p.readLine();
            }
        } catch (Exception e) {
            if (linecount >= 0)
                RotaryCraft.LOGGER.info("LINE " + linecount + ":\n");
            e.printStackTrace();
            ReikaChatHelper.write(e.getMessage() + " caused the read to fail!");
        }
    }

    public void loadDemo() {
        if (level.isClientSide())
            return;
        String path = "Resources/demomusic.rcmusic";
        this.readFile(path, true);
        isOneTimePlaying = true;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.MUSICBOX;
    }

    public void setMusicFromDisc(ItemStack is) {
        if (level.isClientSide())
            return;
        if (is.getItem() != RotaryItems.DISK.get())
            return;
        if (is.getTag() == null)
            return;
        this.clearMusic();
        try {
            for (int i = 0; i < 16; i++) {
                if (is.getTag().contains("ch" + i)) {
                    ListTag li = is.getTag().getList("ch" + i, Tag.TAG_COMPOUND);
                    for (int k = 0; k < li.size(); k++) {
                        CompoundTag nbt = li.getCompound(k);
                        //ReikaJavaLibrary.pConsole(i+":"+k+":"+nbt, Dist.DEDICATED_SERVER);
                        Note n = Note.load(nbt);
                        this.addNote(i, n);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveMusicToDisk(ItemStack is) {
        if (level.isClientSide())
            return;
        if (is.getItem() != RotaryItems.DISK.get())
            return;
        is.save(new CompoundTag());
        for (int i = 0; i < 16; i++) {
            ListTag li = new ListTag();
            ArrayList<Note> channel = musicQueue[i];
            for (Note n : channel) {
                CompoundTag nbt = n.saveAdditional();
                ReikaJavaLibrary.pConsole(i + ":" + channel + ":" + nbt, Dist.DEDICATED_SERVER);
                li.add(nbt);
            }
            is.getTag().put("ch" + i, li);
        }
    }

    private void deleteFiles(BlockPos pos) {
        File save = Minecraft.getInstance().gameDirectory;//DimensionManager.getCurrentSaveRootDirectory(); //todo check if this is right
        //ReikaJavaLibrary.pConsole(musicFile);
        String name = "musicbox@" + String.format("%d,%d,%d", pos) + ".rcmusic";
        File f = new File(save.getPath() + "/RotaryCraft/" + name);
        if (f.exists())
            f.delete();
    }

    @Override
    public void breakBlock() {
        this.deleteFiles(worldPosition);
    }

    @Override
    public boolean trigger() {
        this.startPlaying();
        return true;
    }


    public void sendNote(int pitch, int channel, NoteLength len, Instrument voice) {
        ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.MUSICNOTE.ordinal(), this, pitch, channel, len.ordinal(), voice.ordinal());
    }

    /**
     * A more accurate way of checking if the machine has an inventory than just instanceof Container.
     */
    @Override
    public boolean hasAnInventory() {
        return false;
    }

    /**
     * A more accurate way of checking if the machine interfaces with pipes than just instanceof IFluidHandler.
     */
    @Override
    public boolean hasATank() {
        return false;
    }

    public enum NoteLength {
        WHOLE(48),
        HALF(24),
        QUARTER(12),
        EIGHTH(6),
        SIXTEENTH(3);

        private static final HashMap<Integer, NoteLength> lengthMap = new HashMap();

        static {
            for (NoteLength n : values()) {
                lengthMap.put(n.tickLength, n);
            }
        }

        public final int tickLength;

        NoteLength(int length) {
            tickLength = length;
        }

        public static NoteLength getByTickLength(int ticks) {
            NoteLength len = lengthMap.get(ticks);
            if (len == null) {
                len = WHOLE;
                for (NoteLength n : values()) {
                    if (Math.abs(n.tickLength - ticks) < Math.abs(len.tickLength - ticks)) {
                        len = n;
                    }
                }
            }
            return len;
        }

        public static NoteLength getLargestNotMoreThan(int dt) {
            for (NoteLength n : values()) {
                if (n.tickLength <= dt)
                    return n;
            }
            return null;
        }

        @Override
        public String toString() {
            return ReikaStringParser.capFirstChar(this.name());
        }
    }

    public enum Instrument {
        REST(-1),
        GUITAR(18),
        BASS(32),
        PLING(98),
        BASSDRUM(116),
        SNARE(48),
        CLAVE(-1);

        private static final HashMap<Integer, Instrument> MIDIMap = new HashMap();

        static {
            for (Instrument n : values()) {
                MIDIMap.put(n.MIDIvalue, n);
            }
        }

        public final int MIDIvalue;

        Instrument(int mid) {
            MIDIvalue = mid;
        }

        public static Instrument getFromVoiceAndPitch(MusicScore.Note n) {
            if (n.percussion) {
                return CLAVE;
            } else {
                Instrument i = MIDIMap.get(n.voice);
                if (i != null)
                    return i;
                if (n.key.ordinal() < ReikaMusicHelper.MusicKey.F4.ordinal())
                    return BASS;
                if (n.voice >= 81 && n.voice <= 96) //synth leads and pads
                    return PLING;
                return GUITAR;
            }
        }

        public boolean isPitched() {
            return this.ordinal() < 4;
        }
    }

    public static final class Note {

        private static final String[] notes = {"C", "C#", "D", "Eb", "E", "F", "F#", "G", "G#", "A", "Bb", "B"};
        public final NoteLength length;
        /**
         * Standard MC notation
         */
        public final int pitch;
        public final Instrument voice;

        public Note(NoteLength length, int pitch, Instrument voice) {
            this.length = length;
            this.pitch = pitch;
            this.voice = voice;
        }

        public static String getNoteName(int pitch) {
            return notes[pitch % 12];
        }

        private static Note getFromSerialString(String s) {
            if (s.equals("-"))
                return null;
            String[] sgs = s.split(":");
            int l1 = Integer.parseInt(sgs[0]);
            int note = Integer.parseInt(sgs[1]);
            int i1 = Integer.parseInt(sgs[2]);
            return new Note(NoteLength.values()[l1], note, Instrument.values()[i1]);
        }

        public static Note load(CompoundTag NBT) {
            int length = NBT.getInt("len");
            int pitch = NBT.getInt("pch");
            int voice = NBT.getInt("vc");
            return new Note(NoteLength.values()[length], pitch, Instrument.values()[voice]);
        }

        public static int getPitch(ReikaMusicHelper.MusicKey k) {
            return k.ordinal() - ReikaMusicHelper.MusicKey.F2.ordinal();
        }

        public static Note getFromMusicScore(MusicScore.Note n) {
            Instrument i = Instrument.getFromVoiceAndPitch(n);
            ReikaMusicHelper.MusicKey key = n.key;
            key = key.getInterval(-12);
            if (i == Instrument.BASS)
                key = key.getOctave().getOctave().getOctave();
            int pitch = getPitch(key);
            while (pitch > 48)
                pitch -= 12;
            return new Note(NoteLength.getByTickLength(n.length / 8), pitch, i);
        }

        public String getName() {
            return notes[pitch % 12];
        }

        public int getTickLength() {
            return length.tickLength;
        }

        public boolean isRest() {
            return pitch < 0;
        }

        public Note getRest() {
            return new Note(length, -1, voice);
        }

        public void play(BlockEntityMusicBox te) {
            this.play(te.level, te.worldPosition);
        }

        public void play(Level world, BlockPos pos) {
            if (this.isRest())
                return;

            String pit;
            float pitch = (float) Math.pow(2.0D, (this.pitch - 24) / 12.0D);
            float volume = 200 / 100F;
            if (pitch < 0.5F) {
                pitch *= 2F;
                pit = "low";
            } else if (pitch > 2F) {
                pitch *= 0.25F;
                pit = "hi";
            } else
                pit = "";
            switch (voice) {
                case GUITAR -> SoundRegistry.getNoteFromVoiceAndPitch(SoundRegistry.HARP, pit).playSoundAtBlock(world, pos, volume, pitch);
                case BASS -> SoundRegistry.getNoteFromVoiceAndPitch(SoundRegistry.BASS, pit).playSoundAtBlock(world, pos, volume, pitch);
                case PLING -> SoundRegistry.getNoteFromVoiceAndPitch(SoundRegistry.PLING, pit).playSoundAtBlock(world, pos, volume, pitch);
                case BASSDRUM -> world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.NOTE_BLOCK_BASEDRUM.get(), SoundSource.BLOCKS, volume, pitch, false);
                case SNARE -> world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.NOTE_BLOCK_SNARE.get(), SoundSource.BLOCKS, volume, pitch, false);
                case CLAVE -> world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.NOTE_BLOCK_HAT.get(), SoundSource.BLOCKS, volume, pitch, false);
                default -> {
                }
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Note) {
                Note n = (Note) o;
                return n.length == length && n.pitch == pitch && n.voice == voice;
            }
            return false;
        }

        @Override
        public String toString() {
            if (this.isRest()) {
                return ReikaStringParser.capFirstChar(length.name()) + " Rest";
            }
            StringBuilder sb = new StringBuilder();
            sb.append(voice);
            sb.append(" plays ");
            sb.append(pitch);
            sb.append(" for ");
            sb.append(length);
            return sb.toString();
        }

        public String toSerialString() {
            StringBuilder sb = new StringBuilder();
            sb.append(length.ordinal());
            sb.append(":");
            sb.append(pitch);
            sb.append(":");
            sb.append(voice.ordinal());
            return sb.toString();
        }

        public CompoundTag saveAdditional() {
            CompoundTag NBT = new CompoundTag();
            NBT.putInt("len", length.ordinal());
            NBT.putInt("pch", pitch);
            NBT.putInt("vc", voice.ordinal());
            //ReikaJavaLibrary.pConsole(this+":"+NBT, Dist.DEDICATED_SERVER);
            return NBT;
        }

        public ReikaMusicHelper.MusicKey getMusicKey() {
            return ReikaMusicHelper.MusicKey.getByIndex(ReikaMusicHelper.MusicKey.F2.ordinal() + pitch);
        }

    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Music Box");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
        return new MusicContainer(p_39954_, p_39955_, this);
    }
}
