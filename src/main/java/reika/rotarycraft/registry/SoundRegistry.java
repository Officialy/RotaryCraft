/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.registries.DeferredRegister;
import net.neoforged.registries.ForgeRegistries;
import net.neoforged.registries.RegistryObject;
import reika.dragonapi.interfaces.registry.CustomDistanceSound;
import reika.dragonapi.interfaces.registry.SoundEnum;
import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.dragonapi.libraries.io.ReikaSoundHelper;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.RotaryCraft;

import java.net.URL;
import java.util.HashMap;

import static net.minecraft.sounds.SoundSource.MASTER;

public enum SoundRegistry implements CustomDistanceSound {

    ELECTRIC("electric"),
    WIND("wind"),
    STEAM("steam"),
    CAR("car"),
    HYDRO("hydro"),
    MICRO("micro"),
    JET("jet"),
    KNOCKBACK("knockback"),
    PULSEJET("pulsejet"),
    PUMP("pump"),
    PILEDRIVER("piledriver"),
    SMOKE("smokealarm"),
    SPRINKLER("sprinkler"),
    FLYWHEEL("flywheel"),
    PROJECTOR("projector"),
    LOWBASS("basslo"),
    BASS("bass"),
    HIBASS("basshi"),
    LOWHARP("harplo"),
    HARP("harp"),
    HIHARP("harphi"),
    LOWPLING("plinglo"),
    PLING("pling"),
    HIPLING("plinghi"),
    FRICTION("friction"),
    CRAFT("craft"),
    AIRCOMP("compress"),
    PNEUMATIC("pneu"),
    LINEBUILDER("linebuild"),
    JETPACK("pack"),
    DIESEL("diesel"),
    BELT("belt"),
    FAN("fan"),
    SPARK("spark"),
    DYNAMO("dynamo"),
    INGESTION("ingest_short"),
    FRIDGE("fridge"),
    JETSTART("jetstart"),
    SONIC("sonic"),
    SHORTJET("shortjet"),
    AFTERBURN("afterburner"),
    RUMBLE("rumble2"),
    COIL("coil"),
    GATLINGRELOAD("gatlingreload"),
    GATLING("gatling"),
    FLAMETURRET("flameturret");

    // DeferredRegister for sound events
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, RotaryCraft.MODID);

    // Static map for sound event registration
    private static final HashMap<SoundRegistry, RegistryObject<SoundEvent>> SOUND_EVENT_MAP = new HashMap<>();

    public static final String SOUND_FOLDER = "sounds/";
    private static final String SOUND_DIR = "sounds/";
    private static final String SOUND_EXT = ".ogg";
    private static final String MUSIC_FOLDER = "music/";
    private static final HashMap<String, SoundRegistry> soundNames = new HashMap<>();

    static {
        // Register all sound events
        for (SoundRegistry sound : values()) {
            SOUND_EVENT_MAP.put(sound, SOUND_EVENTS.register(sound.eventName, 
                () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(RotaryCraft.MODID, sound.eventName))));
        }
        
        // Create name lookup map
        for (SoundRegistry s : values()) {
            soundNames.put(s.name(), s);
        }
    }

    private final ResourceLocation path;
    private final String name;
    private final String eventName;
    private boolean isVolumed = false;

    SoundRegistry(String eventName) {
        this.eventName = eventName;
        this.name = eventName;
        
        // Determine if this is a volumed sound based on common engine sounds
        if (this.name.contains("engine") || this.name.equals("electric") || this.name.equals("wind") || 
            this.name.equals("steam") || this.name.equals("car") || this.name.equals("hydro") || 
            this.name.equals("micro") || this.name.equals("jet") || this.name.equals("pulsejet") ||
            this.name.equals("pump") || this.name.equals("sprinkler") || this.name.equals("flywheel") ||
            this.name.equals("friction") || this.name.equals("craft") || this.name.equals("compress") ||
            this.name.equals("pneu") || this.name.equals("diesel") || this.name.equals("belt") ||
            this.name.equals("fan") || this.name.equals("dynamo") || this.name.equals("fridge") ||
            this.name.equals("jetstart") || this.name.equals("sonic") || this.name.equals("coil")) {
            isVolumed = true;
        }
        
        if (this.isNote())
            path = new ResourceLocation(RotaryCraft.MODID, SOUND_FOLDER + MUSIC_FOLDER + name + SOUND_EXT);
        else
            path = new ResourceLocation(RotaryCraft.MODID, SOUND_FOLDER + name + SOUND_EXT);
    }

    // Get the registered SoundEvent
    public SoundEvent getSoundEvent() {
        return SOUND_EVENT_MAP.get(this).get();
    }

    public static SoundRegistry getNoteFromVoiceAndPitch(SoundRegistry voice, String pitch) {
        return SoundRegistry.getSoundByName(pitch.toUpperCase() + voice.name());
    }

    public static SoundRegistry getSoundByName(String s) {
        return soundNames.get(s);
    }

    public float getSoundVolume() {
        double vol = ConfigRegistry.MACHINEVOLUME.getFloat();
        if (this.isEngineSound()) {
            vol *= ConfigRegistry.ENGINEVOLUME.getFloat();
        }
        if (vol < 0)
            vol = 0;
        if (vol > 1)
            vol = 1F;
        return (float) vol;
    }

    @Override
    public float getModulatedVolume() {
        if (!isVolumed)
            return 1F;
        else
            return this.getSoundVolume();
    }

    public void playSound(Entity e) {
        this.playSound(e, 1, 1);
    }

    public void playSound(Entity e, float vol, float pitch) {
        this.playSound(e.level(), e.getOnPos(), vol, pitch);
    }

    public void playSound(Level world, BlockPos pos, float vol, float pitch) {
        if (world.isClientSide())
            return;
        ReikaSoundHelper.playSound(this, world, pos.getX(), pos.getY(), pos.getZ(), vol * this.getModulatedVolume(), pitch);
    }

    public void playSound(Level world, double x, double y, double z, float vol, float pitch) {
        if (world.isClientSide())
            return;
        ReikaSoundHelper.playSound(this, world, x, y, z, vol * this.getModulatedVolume(), pitch);
    }

    public void playSound(Level world, BlockPos pos, float vol, float pitch, boolean attenuate) {
        if (world.isClientSide())
            return;
        ReikaSoundHelper.playSound(this, world, pos.getX(), pos.getY(), pos.getZ(), vol * this.getModulatedVolume(), pitch, attenuate);
    }

    public void playSoundAtBlock(Level world, BlockPos pos, float vol, float pitch) {
        this.playSound(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, vol, pitch);
    }
    public void playSoundAtBlock(Level world, double x, double y, double z, float vol, float pitch) {
        this.playSound(world, x + 0.5, y + 0.5, x + 0.5, vol, pitch);
    }
    public void playSoundAtBlock(Level world, BlockPos pos) {
        this.playSound(world, pos, 1, 1);
    }

    public void playSoundAtBlock(BlockEntity te) {
        this.playSoundAtBlock(te, 1, 1);
    }

    public void playSoundAtBlock(BlockEntity te, float vol, float pitch) {
        this.playSoundAtBlock(te.getLevel(), te.getBlockPos(), vol, pitch);
    }

    public void playSoundNoAttenuation(Level world, BlockPos pos, float vol, float pitch, int broadcast) {
        if (world.isClientSide())
            return;
        ReikaPacketHelper.sendSoundPacket(this, world, pos.getX(), pos.getY(), pos.getZ(), vol, pitch, false, broadcast);
    }

    public String getName() {
        return this.name();
    }

    public ResourceLocation getPath() {
        return path;
    }

    public URL getURL() {
        if (this.isNote())
            return RotaryCraft.class.getResource(SOUND_DIR + MUSIC_FOLDER + name + SOUND_EXT);
        else
            return RotaryCraft.class.getResource(SOUND_DIR + name + SOUND_EXT);
    }

    public boolean isNote() {
        return (this.name().contains("HARP") || this.name().contains("BASS") || this.name().contains("PLING"));
    }

    @Override
    public SoundSource getCategory() {
        return MASTER;
    }

    @Override
    public boolean canOverlap() {
        return this == JETPACK || this == RUMBLE;
    }

    private boolean isEngineSound() {
        return name.endsWith("engine") || this == ELECTRIC || this == WIND || this == STEAM || this == CAR || 
               this == HYDRO || this == MICRO || this == JET || this == DIESEL;
    }

    @Override
    public boolean attenuate() {
        return true;
    }

    @Override
    public boolean preload() {
        return this == JETSTART;
    }

    @Override
    public float getAudibleDistance() {
        return switch (this) {
            case JET, JETSTART -> 40;
            default -> -1;
        };
    }
}
