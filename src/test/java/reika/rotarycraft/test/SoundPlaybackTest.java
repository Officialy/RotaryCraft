package reika.rotarycraft.test;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import reika.dragonapi.interfaces.registry.SoundEnum;
import reika.dragonapi.interfaces.registry.CustomDistanceSound;
import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.rotarycraft.registry.SoundRegistry;

import static org.junit.jupiter.api.Assertions.*;

class SoundPlaybackTest {
    @Test
    void unspecifiedCustomRangeUsesVanillaFadeDistance() throws Exception {
        var sound = (CustomDistanceSound) java.lang.reflect.Proxy.newProxyInstance(
                CustomDistanceSound.class.getClassLoader(), new Class<?>[] {CustomDistanceSound.class},
                (proxy, method, args) -> method.getName().equals("getAudibleDistance") ? -1F : null);
        var distance = ReikaPacketHelper.class.getDeclaredMethod("getSoundDistance", boolean.class, SoundEnum.class);
        distance.setAccessible(true);
        assertTrue((int) distance.invoke(null, true, sound) >= 16,
                "The -1/default sentinel must not truncate vanilla's sixteen-block fade");
    }

    @Test
    void broadcastCoversEveryAssetsFadeDistance() throws Exception {
        var distance = ReikaPacketHelper.class.getDeclaredMethod("getSoundDistance", boolean.class, SoundEnum.class);
        distance.setAccessible(true);
        try (var reader = new InputStreamReader(getClass().getResourceAsStream("/assets/rotarycraft/sounds.json"), StandardCharsets.UTF_8)) {
            var json = JsonParser.parseReader(reader).getAsJsonObject();
            for (var sound : SoundRegistry.values()) {
                var event = json.getAsJsonObject(sound.getSoundEvent().location().getPath());
                assertNotNull(event, "Missing sound event " + sound);
                int range = (int) distance.invoke(null, true, sound);
                for (var entry : event.getAsJsonArray("sounds")) {
                    int fade = entry.isJsonObject() && entry.getAsJsonObject().has("attenuation_distance")
                            ? entry.getAsJsonObject().get("attenuation_distance").getAsInt() : 16;
                    assertTrue(range >= fade, sound + " broadcast " + range + " cuts off fade distance " + fade);
                }
                assertEquals(Integer.MAX_VALUE, distance.invoke(null, false, sound), "Global sound must reach all players");
            }
        }
    }

    @Test
    void spatialSoundAssetsAreMono() throws Exception {
        try (var reader = new InputStreamReader(getClass().getResourceAsStream("/assets/rotarycraft/sounds.json"), StandardCharsets.UTF_8)) {
            var json = JsonParser.parseReader(reader).getAsJsonObject();
            for (var event : json.entrySet()) {
                for (var entry : event.getValue().getAsJsonObject().getAsJsonArray("sounds")) {
                    String name = entry.isJsonPrimitive() ? entry.getAsString() : entry.getAsJsonObject().get("name").getAsString();
                    String path = "/assets/" + name.replace(':', '/') ;
                    int slash = path.indexOf('/', "/assets/".length());
                    path = path.substring(0, slash) + "/sounds" + path.substring(slash) + ".ogg";
                    try (var stream = getClass().getResourceAsStream(path)) {
                        assertNotNull(stream, "Missing audio " + path);
                        byte[] bytes = stream.readNBytes(256);
                        int header = -1;
                        byte[] signature = {1, 'v', 'o', 'r', 'b', 'i', 's'};
                        for (int i = 0; i <= bytes.length - 12; i++) {
                            boolean match = true;
                            for (int j = 0; j < signature.length; j++) match &= bytes[i + j] == signature[j];
                            if (match) { header = i; break; }
                        }
                        assertTrue(header >= 0, "Missing Vorbis header " + path);
                        assertEquals(1, bytes[header + 11], "OpenAL cannot attenuate stereo machine audio: " + path);
                    }
                }
            }
        }
    }
}
