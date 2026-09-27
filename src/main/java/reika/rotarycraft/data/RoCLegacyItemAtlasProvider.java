package reika.rotarycraft.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import java.util.concurrent.CompletableFuture;

/** Maps original 1.7.10 sheet cells to 26.2 item sprites without altering the source art. */
public final class RoCLegacyItemAtlasProvider implements DataProvider {
    private final PackOutput.PathProvider paths;

    public RoCLegacyItemAtlasProvider(PackOutput output) {
        paths = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "atlases");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        JsonObject atlas = new JsonObject();
        JsonArray sources = new JsonArray();
        JsonObject sheet = new JsonObject();
        sheet.addProperty("type", "minecraft:unstitch");
        sheet.addProperty("resource", "rotarycraft:item/legacy_items");
        sheet.addProperty("divisor_x", 16);
        sheet.addProperty("divisor_y", 16);
        JsonArray regions = new JsonArray();
        regions.add(region("red_gold_ingot", 6, 6));
        regions.add(region("red_gold_dust", 12, 8));
        regions.add(region("clean_sludge", 0, 12));
        sheet.add("regions", regions);
        sources.add(sheet);
        atlas.add("sources", sources);
        return DataProvider.saveStable(cache, atlas, paths.json(Identifier.withDefaultNamespace("items")));
    }

    private static JsonObject region(String name, int x, int y) {
        JsonObject result = new JsonObject();
        result.addProperty("sprite", "rotarycraft:item/" + name);
        result.addProperty("x", x);
        result.addProperty("y", y);
        result.addProperty("width", 1);
        result.addProperty("height", 1);
        return result;
    }

    @Override
    public String getName() {
        return "RotaryCraft Legacy Item Atlas";
    }
}
