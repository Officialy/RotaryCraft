package reika.rotarycraft.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import reika.rotarycraft.items.tools.bedrock.*;
import reika.rotarycraft.items.tools.steel.*;

import java.util.function.BiConsumer;

/** Generated item-only poses. World blockstates and pipe shell geometry keep their own models. */
public final class RoCItemDisplayModels {
    private RoCItemDisplayModels() {}

    public static Identifier machine(Item item, BiConsumer<Identifier, ModelInstance> output) {
        Identifier id = ModelLocationUtils.getModelLocation(item);
        output.accept(id, () -> {
            JsonObject json = new JsonObject();
            json.addProperty("parent", "minecraft:block/block");
            JsonObject textures = new JsonObject();
            // The special renderer supplies the machine's actual texture; this is its particle.
            textures.addProperty("particle", "rotarycraft:block/steel");
            json.add("textures", textures);
            json.add("display", displays(false));
            return json;
        });
        return id;
    }

    public static Identifier pipe(Item item, Identifier core, BiConsumer<Identifier, ModelInstance> output) {
        Identifier id = ModelLocationUtils.getModelLocation(item);
        output.accept(id, () -> {
            JsonObject json = new JsonObject();
            json.addProperty("parent", core.toString());
            json.add("display", displays(true));
            return json;
        });
        return id;
    }

    public static JsonObject displays(boolean pipe) {
        JsonObject display = new JsonObject();
        // Original PipeBodyRenderer enlarged its 0.75-block shell by 1.25 in inventory.
        // Keep that silhouette and the isometric view, with a smaller held pipe per user feedback.
        transform(display, "gui", 30, 225, 0, 0, 0, 0, pipe ? 0.78125F : 0.625F);
        transform(display, "ground", 0, 0, 0, 0, 3, 0, pipe ? 0.3125F : 0.25F);
        transform(display, "fixed", 0, 0, 0, 0, 0, 0, 0.5F);
        transform(display, "on_shelf", 0, 180, 0, 0, 0, 0, 1);
        transform(display, "head", 0, 0, 0, 0, 0, 0, 1);
        transform(display, "thirdperson_righthand", 75, 45, 0, 0, 2.5F, 0, pipe ? 0.25F : 0.375F);
        transform(display, "thirdperson_lefthand", 75, 45, 0, 0, 2.5F, 0, pipe ? 0.25F : 0.375F);
        transform(display, "firstperson_righthand", 0, 45, 0, 0, 0, 0, pipe ? 0.25F : 0.4F);
        transform(display, "firstperson_lefthand", 0, 225, 0, 0, 0, 0, pipe ? 0.25F : 0.4F);
        return display;
    }

    private static void transform(JsonObject display, String context, float rx, float ry, float rz,
                                  float tx, float ty, float tz, float scale) {
        JsonObject transform = new JsonObject();
        transform.add("rotation", vector(rx, ry, rz));
        transform.add("translation", vector(tx, ty, tz));
        transform.add("scale", vector(scale, scale, scale));
        display.add(context, transform);
    }

    private static JsonArray vector(float x, float y, float z) {
        JsonArray vector = new JsonArray();
        vector.add(x); vector.add(y); vector.add(z);
        return vector;
    }

    /** These classes replaced vanilla ToolItem/HoeItem/SwordItem, whose held poses were 3D tools. */
    public static boolean isHandheldTool(Item item) {
        return item instanceof ItemSteelAxe || item instanceof ItemSteelPick
                || item instanceof ItemSteelShovel || item instanceof ItemSteelHoe
                || item instanceof ItemSteelSword || item instanceof ItemBedrockAxe
                || item instanceof ItemBedrockPickaxe || item instanceof ItemBedrockShovel
                || item instanceof ItemBedrockHoe || item instanceof ItemBedrockSword;
    }
}
