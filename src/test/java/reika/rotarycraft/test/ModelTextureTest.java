package reika.rotarycraft.test;

import com.google.gson.JsonParser;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.RenderShape;
import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.base.blocks.entity.pipe.BlockPipeShell;
import reika.rotarycraft.registry.RotaryBlocks;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Every model's declared texture must resolve to a resource that actually exists.
 *
 * <p>This is the cheapest guard against the most common defect found in the 2026-08-20/21 render
 * audit: twelve models pointed at files that were not there — six turrets into a {@code turret/}
 * subdirectory that does not exist, the coil / heater / iodide / fuel converter all at the steam
 * engine's {@code engine/steamtex.png}, the electric motor at a {@code modinterface/} that does not
 * exist, and the vertical clutch returning a <em>directory</em> instead of a file. All twelve render
 * untextured in-world and as the in-hand item, and none was reachable from a GameTest, because
 * GameTests run on a server with no client classes.
 *
 * <p>Eight constants are legitimately directory prefixes that the renderer concatenates a
 * per-material suffix onto (the gearbox, shaft, cross and jet families). They are distinguished by
 * ending in {@code /} rather than by an allowlist, so a new one needs no change here — and a
 * <em>non</em>-prefix constant that happens to name a directory, which is what VClutch did, still
 * fails.
 *
 * <p><b>What this does not catch.</b> The VClutch bug was a constant that <em>was</em> a directory
 * and whose {@code getTexture()} returned it verbatim. Once such a constant ends in {@code /} it is
 * indistinguishable here from the eight genuine prefixes, because {@code getTexture()} is an
 * instance method and instantiating a model needs a baked {@code ModelPart}. Verified by
 * deliberately reintroducing both bugs: the {@code turret/} one is caught, the VClutch one is not.
 * Closing that gap would mean each prefix model exposing its concrete fallback as a second static
 * constant; worth doing if the shape recurs, but it has been a single occurrence so far.
 *
 * <p>Discovery note: under NeoForge's {@code TransformingClassLoader} {@code getResource} resolves
 * files but returns null for directories, so neither the class scan nor the prefix check can list a
 * package or asset folder directly. Both anchor on a resource that <em>is</em> resolvable and walk
 * out from its location on disk.
 */
public class ModelTextureTest {

    /** Any class that certainly exists, used to locate the compiled-classes root. */
    private static final String ANCHOR_CLASS = "reika/rotarycraft/RotaryCraft.class";
    /** Any asset that certainly exists, used to locate the asset root. */
    private static final String ANCHOR_ASSET = "assets/rotarycraft/textures/blockentitytex/lamptex.png";
    private static final String ASSET_PREFIX = "assets/rotarycraft/";

    @Test
    void everyBlockModelTextureResolves() throws IOException {
        Path models = assetRoot().resolve("models/block");
        List<String> broken = new ArrayList<>();
        int checked = 0;
        try (Stream<Path> walk = Files.walk(models)) {
            for (Path model : walk.filter(p -> p.toString().endsWith(".json")).toList()) {
                var json = JsonParser.parseString(Files.readString(model)).getAsJsonObject();
                if (!json.has("textures")) continue;
                for (var entry : json.getAsJsonObject("textures").entrySet()) {
                    String sprite = entry.getValue().getAsString();
                    if (!sprite.startsWith("rotarycraft:")) continue;
                    checked++;
                    String resource = ASSET_PREFIX + "textures/" + sprite.substring("rotarycraft:".length()) + ".png";
                    if (getClass().getClassLoader().getResource(resource) == null)
                        broken.add(models.relativize(model) + " (" + entry.getKey() + "): " + sprite);
                }
            }
        }
        assertTrue(checked > 100, "block model texture scan did not include the generated machine models");
        assertTrue(broken.isEmpty(), "Block model textures missing from the atlas:\n  " + String.join("\n  ", broken));
    }

    @Test
    void modelledMachinesUseLegacySteelDebris() throws IOException {
        int checked = 0;
        for (var holder : RotaryBlocks.BLOCKS.getEntries()) {
            var block = holder.get();
            if (!(block instanceof BlockBasicMachine) || block instanceof BlockPipeShell
                    || block.defaultBlockState().getRenderShape() != RenderShape.INVISIBLE) continue;
            String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
            String resource = ASSET_PREFIX + "models/block/" + name + ".json";
            try (var stream = getClass().getClassLoader().getResourceAsStream(resource)) {
                assertNotNull(stream, name + " has no debris model");
                var textures = JsonParser.parseString(new String(stream.readAllBytes(), StandardCharsets.UTF_8))
                        .getAsJsonObject().getAsJsonObject("textures");
                // cube_all inherits particle=#all. A BER texture or block/<registry id> is incorrect.
                assertEquals("rotarycraft:block/steel", textures.get("all").getAsString(), name + " debris sprite");
                checked++;
            }
        }
        assertTrue(checked > 60, "only " + checked + " modelled machine variants were checked");
    }

    @Test
    void everyModelTextureResolves() throws Exception {
        List<Class<?>> models = modelClasses();
        assertFalse(models.isEmpty(),
                "found no *Model classes to check; the scan is broken, not the models");

        Path assetRoot = assetRoot();
        assertNotNull(assetRoot, "could not locate the asset root from " + ANCHOR_ASSET);

        List<String> broken = new ArrayList<>();
        int checked = 0;
        for (Class<?> c : models) {
            Field f;
            try {
                f = c.getDeclaredField("TEXTURE_LOCATION");
            } catch (NoSuchFieldException e) {
                continue; // model inherits or hard-codes its texture
            }
            if (!Modifier.isStatic(f.getModifiers()))
                continue;
            f.setAccessible(true);
            Object id = f.get(null);
            if (id == null) {
                broken.add(c.getSimpleName() + ": TEXTURE_LOCATION is null");
                continue;
            }
            // Identifier's toString is "namespace:path"; avoid importing it so this test does not
            // pull the render stack onto the test classpath.
            String s = id.toString();
            int colon = s.indexOf(':');
            String path = colon >= 0 ? s.substring(colon + 1) : s;
            checked++;

            boolean prefix = path.endsWith("/");
            if (prefix) {
                Path dir = assetRoot.resolve(path);
                if (!Files.isDirectory(dir))
                    broken.add(c.getSimpleName() + ": " + s + " is a declared directory prefix, but"
                            + " no such directory exists");
                else if (isEmptyOfTextures(dir))
                    broken.add(c.getSimpleName() + ": " + s + " is a declared prefix but the"
                            + " directory holds no .png");
            } else if (!path.endsWith(".png")) {
                broken.add(c.getSimpleName() + ": " + s + " is neither a .png file nor a directory"
                        + " prefix ending in '/'");
            } else if (getClass().getClassLoader().getResource(ASSET_PREFIX + path) == null) {
                broken.add(c.getSimpleName() + ": " + s + " does not exist");
            }
        }

        assertTrue(checked > 50,
                "only " + checked + " models declared a TEXTURE_LOCATION; the scan is not working");
        assertTrue(broken.isEmpty(),
                broken.size() + " model texture(s) do not resolve:\n  " + String.join("\n  ", broken));
    }

    private static boolean isEmptyOfTextures(Path dir) throws IOException {
        try (Stream<Path> list = Files.list(dir)) {
            return list.noneMatch(p -> p.getFileName().toString().endsWith(".png"));
        }
    }

    /** {@code .../build/resources/main/assets/rotarycraft/}, derived from a resolvable asset. */
    private static Path assetRoot() {
        URL u = ModelTextureTest.class.getClassLoader().getResource(ANCHOR_ASSET);
        if (u == null || !"file".equals(u.getProtocol()))
            return null;
        Path anchor = Path.of(new File(u.getPath().replace("%20", " ")).toURI());
        // strip the anchor's own relative path back to the resources root, then re-add the prefix
        Path root = anchor;
        for (int i = 0; i < ANCHOR_ASSET.split("/").length; i++)
            root = root.getParent();
        return root.resolve(ASSET_PREFIX);
    }

    /** Every {@code *Model} class under {@code reika.rotarycraft}, walked from the classes root. */
    private static List<Class<?>> modelClasses() throws IOException {
        List<Class<?>> out = new ArrayList<>();
        ClassLoader cl = ModelTextureTest.class.getClassLoader();
        URL u = cl.getResource(ANCHOR_CLASS);
        if (u == null || !"file".equals(u.getProtocol()))
            return out;
        Path anchor = Path.of(new File(u.getPath().replace("%20", " ")).toURI());
        Path root = anchor;
        for (int i = 0; i < ANCHOR_CLASS.split("/").length; i++)
            root = root.getParent();

        Path pkg = root.resolve("reika/rotarycraft");
        if (!Files.isDirectory(pkg))
            return out;
        try (Stream<Path> walk = Files.walk(pkg)) {
            for (Path p : walk.toList()) {
                String name = p.getFileName().toString();
                if (!name.endsWith("Model.class") || name.contains("$"))
                    continue;
                String rel = root.relativize(p).toString().replace(File.separatorChar, '/');
                String cn = rel.substring(0, rel.length() - ".class".length()).replace('/', '.');
                try {
                    out.add(Class.forName(cn, false, cl));
                } catch (Throwable ignored) {
                    // needs a client-only supertype that is absent here; nothing to check
                }
            }
        }
        return out;
    }
}
