package reika.rotarycraft;

import java.awt.image.BufferedImage;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import reika.dragonapi.DragonAPI;
import reika.dragonapi.auxiliary.FileInputThread;

/** Real mod resources decoded on an owned worker in the native dedicated-server runtime. */
final class DragonAPIFileInputTests {
    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> environment) {
        RotaryGameTests.register(event, environment, "dragonapi_file_input_sources", 80, DragonAPIFileInputTests::resources);
    }

    private static void resources(GameTestHelper helper) {
        Thread owner = Thread.currentThread();
        var executor = Executors.newSingleThreadExecutor();
        try {
            var image = new FileInputThread().setFiletype(FileInputThread.FileType.IMAGE).setSource(() -> {
                if (Thread.currentThread() == owner) throw new AssertionError("File reading must run on the selected worker");
                return DragonAPI.class.getResourceAsStream("/assets/dragonapi/textures/gui/warning.png");
            });
            var decoded = (BufferedImage)image.submit(executor).toCompletableFuture().get(20, TimeUnit.SECONDS);
            helper.assertTrue(decoded.getWidth() == 48 && decoded.getHeight() == 48 && image.getState() == FileInputThread.State.SUCCEEDED,
                    "native headless image decoding preserves the original icon without a texture manager");
            var text = new FileInputThread().setFiletype(FileInputThread.FileType.TEXT)
                    .setFilepath(DragonAPI.class, "/assets/dragonapi/lang/en_us.json");
            helper.assertTrue(((String)text.submit(executor).toCompletableFuture().get(20, TimeUnit.SECONDS)).contains("tooltip.dragonapi.temperature"),
                    "mod classpath text loads without the client resource manager");
            var sound = new FileInputThread().setFiletype(FileInputThread.FileType.SOUND)
                    .setFilepath(RotaryCraft.class, "/assets/rotarycraft/sounds/spark.ogg");
            byte[] bytes = (byte[])sound.submit(executor).toCompletableFuture().get(20, TimeUnit.SECONDS);
            helper.assertTrue(bytes.length > 4 && bytes[0] == 'O' && bytes[1] == 'g' && bytes[2] == 'g' && bytes[3] == 'S',
                    "encoded original OGG data loads intact with no client/native audio object");
            var xml = new FileInputThread().setFiletype(FileInputThread.FileType.XML)
                    .setData("<root><value>native</value></root>".getBytes(java.nio.charset.StandardCharsets.UTF_8));
            var doc = (org.w3c.dom.Document)xml.submit(executor).toCompletableFuture().get(20, TimeUnit.SECONDS);
            helper.assertTrue(doc.getDocumentElement().getTextContent().equals("native"), "native common XML decoding remains available");
            helper.assertTrue(FileInputThread.class.getDeclaredMethods().length > 0, "common loader metadata loads on a dedicated server");
            helper.succeed();
        } catch (Exception failure) { throw new IllegalStateException("Native file input regression failed", failure); }
        finally {
            executor.shutdownNow();
            try { if (!executor.awaitTermination(10, TimeUnit.SECONDS)) throw new IllegalStateException("Owned file worker did not stop"); }
            catch (InterruptedException failure) { Thread.currentThread().interrupt(); throw new IllegalStateException(failure); }
        }
    }
}
