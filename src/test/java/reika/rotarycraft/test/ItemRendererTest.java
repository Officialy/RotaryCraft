package reika.rotarycraft.test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.junit.jupiter.api.Test;
import reika.rotarycraft.client.MachineModels;
import reika.rotarycraft.models.animated.Gearbox4Model;
import reika.rotarycraft.models.animated.Gearbox8Model;
import reika.rotarycraft.models.animated.Gearbox16Model;
import reika.rotarycraft.models.engine.DCModel;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryItems;
import reika.rotarycraft.registry.RotaryModelLayers;
import reika.rotarycraft.renders.item.MachineItemPose;
import reika.rotarycraft.renders.item.MachineItemRenderer;

import java.io.InputStreamReader;
import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/** Native baked models and packaged item data, without a window or OpenGL context. */
class ItemRendererTest {
    private static final float EPSILON = 0.00001F;

    private static EntityModelSet modelSet() {
        Map<ModelLayerLocation, LayerDefinition> layers = new HashMap<>();
        RotaryModelLayers.registerLayerDefinitions(new EntityRenderersEvent.RegisterLayerDefinitions() {
            @Override
            public void registerLayerDefinition(ModelLayerLocation layer, Supplier<LayerDefinition> supplier) {
                layers.put(layer, supplier.get());
            }
        });
        return new EntityModelSet(layers);
    }

    @Test
    void everyRegisteredMachineItemBakesWithConcreteTextureAndFiniteBounds() {
        EntityModelSet models = modelSet();
        int checked = 0;
        for (var holder : RotaryBlocks.BLOCKS.getEntries()) {
            var block = holder.get();
            MachineRegistry machine = MachineRegistry.getMachineMapping(block);
            if (machine == null || !MachineModels.has(machine)) continue;
            String variant = BuiltInRegistries.BLOCK.getKey(block).getPath();
            var model = MachineItemRenderer.Unbaked.bakeModel(machine, variant, models);
            var texture = MachineItemRenderer.Unbaked.resolveTexture(machine, variant, model);
            assertNotNull(getClass().getClassLoader().getResource("assets/" + texture.getNamespace() + "/" + texture.getPath()), variant + ": " + texture);
            List<Vector3fc> bounds = new ArrayList<>();
            new MachineItemRenderer(machine, model, texture).getExtents(bounds::add);
            assertFalse(bounds.isEmpty(), variant);
            for (Vector3fc point : bounds)
                assertTrue(Float.isFinite(point.x()) && Float.isFinite(point.y()) && Float.isFinite(point.z()), variant);
            if (machine == MachineRegistry.GEARBOX) {
                if (variant.endsWith("_4x")) assertInstanceOf(Gearbox4Model.class, model, variant);
                if (variant.endsWith("_8x")) assertInstanceOf(Gearbox8Model.class, model, variant);
                if (variant.endsWith("_16x")) assertInstanceOf(Gearbox16Model.class, model, variant);
            }
            JsonObject item = resource("items/" + variant + ".json").getAsJsonObject("model");
            assertEquals("rotarycraft:item/" + variant, item.get("base").getAsString(), variant);
            assertEquals(variant, item.getAsJsonObject("model").get("variant").getAsString(), variant);
            checked++;
        }
        assertTrue(checked >= 110, "Only " + checked + " machine item variants were checked");
    }

    @Test
    void reportedExtentsMatchThePoseActuallySubmittedToMinecraft() {
        var model = new DCModel(DCModel.createLayer().bakeRoot());
        var renderer = new MachineItemRenderer(MachineRegistry.DC_ENGINE, model, model.getTexture());
        List<Vector3fc> rendered = new ArrayList<>();
        int[] submissions = {0};
        VertexConsumer buffer = (VertexConsumer)Proxy.newProxyInstance(
                VertexConsumer.class.getClassLoader(), new Class<?>[]{VertexConsumer.class},
                (proxy, method, args) -> {
                    if (method.isDefault()) return InvocationHandler.invokeDefault(proxy, method, args);
                    if (method.getName().equals("addVertex"))
                        rendered.add(new Vector3f((Float)args[0], (Float)args[1], (Float)args[2]));
                    return proxy;
                });
        SubmitNodeCollector collector = (SubmitNodeCollector)Proxy.newProxyInstance(
                SubmitNodeCollector.class.getClassLoader(), new Class<?>[]{SubmitNodeCollector.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("submitCustomGeometry")) {
                        ((SubmitNodeCollector.CustomGeometryRenderer)args[2]).render(((PoseStack)args[0]).last(), buffer);
                        submissions[0]++;
                    }
                    return null;
                });
        PoseStack pose = new PoseStack();
        pose.translate(3, 7, 9);
        Matrix4f original = new Matrix4f(pose.last().pose());
        renderer.submit(MachineItemRenderer.ItemState.NORMAL, pose, collector, 0, 0, false, 0);
        assertEquals(1, submissions[0]);
        assertEquals(original, pose.last().pose(), "Renderer must restore its caller's pose");
        List<Vector3fc> reported = new ArrayList<>();
        renderer.getExtents(reported::add);
        assertEquals(rendered.size(), reported.size());
        for (int i = 0; i < rendered.size(); i++) {
            Vector3f expected = original.transformPosition(reported.get(i), new Vector3f());
            assertTrue(expected.equals(rendered.get(i), EPSILON), "Vertex " + i + " disagrees with the submitted geometry");
        }
    }

    @Test
    void enginePoseMatchesOriginalTechneAxisConversion() {
        PoseStack pose = new PoseStack();
        MachineItemPose.apply(MachineRegistry.DC_ENGINE, pose);
        Vector3f base = pose.last().pose().transformPosition(0, 1.5F, 0, new Vector3f());
        assertTrue(base.equals(new Vector3f(0.5F, 0, 0.5F), EPSILON));
        Vector3f shaft = pose.last().pose().transformPosition(1, 0, 0, new Vector3f());
        assertTrue(shaft.equals(new Vector3f(0.5F, 1.5F, -0.5F), EPSILON));
    }

    @Test
    void upgradedItemDataIsExtractedWithoutReadingAWorld() {
        var models = modelSet();
        for (var holder : List.of(RotaryBlocks.HYDRO_ENGINE, RotaryBlocks.COIL, RotaryBlocks.SPLITTER)) {
            var block = holder.get();
            var machine = MachineRegistry.getMachineMapping(block);
            String variant = BuiltInRegistries.BLOCK.getKey(block).getPath();
            var model = MachineItemRenderer.Unbaked.bakeModel(machine, variant, models);
            var renderer = new MachineItemRenderer(machine, model,
                    MachineItemRenderer.Unbaked.resolveTexture(machine, variant, model));
            // The unit-test bootstrap registers items but does not perform the 26.3 component reload.
            // A native direct holder supplies an explicit component prototype without rebinding globals.
            var stack = new ItemStack(new Holder.Direct<>(block.asItem(), DataComponentMap.EMPTY), 1);
            assertEquals(MachineItemRenderer.ItemState.NORMAL, renderer.extractArgument(stack));
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("bedrock", true);
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            var snapshot = renderer.extractArgument(stack);
            assertEquals(MachineItemRenderer.ItemState.BEDROCK, snapshot);
            stack.remove(DataComponents.CUSTOM_DATA);
            assertEquals(MachineItemRenderer.ItemState.BEDROCK, snapshot, "Deferred state must be immutable");
            if (machine == MachineRegistry.HYDRO_ENGINE) {
                tag = new CompoundTag(); tag.putBoolean("bed", true);
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                assertEquals(MachineItemRenderer.ItemState.BEDROCK, renderer.extractArgument(stack));
            }
        }
    }

    @Test
    void allPipeItemsHaveIsometricIconsAndSmallPosesForBothHands() {
        for (String variant : List.of("fluid_pipe", "hose", "fuel_line", "bedrock_pipe", "separation", "suction")) {
            JsonObject item = resource("items/" + variant + ".json").getAsJsonObject("model");
            assertEquals("rotarycraft:item/" + variant, item.get("model").getAsString(), variant);
            JsonObject display = resource("models/item/" + variant + ".json").getAsJsonObject("display");
            JsonObject gui = display.getAsJsonObject("gui");
            assertEquals(30, gui.getAsJsonArray("rotation").get(0).getAsInt(), variant);
            assertEquals(225, gui.getAsJsonArray("rotation").get(1).getAsInt(), variant);
            assertEquals(0.78125F, gui.getAsJsonArray("scale").get(0).getAsFloat(), EPSILON, variant);
            for (String context : List.of("firstperson_righthand", "firstperson_lefthand",
                    "thirdperson_righthand", "thirdperson_lefthand"))
                assertEquals(0.25F, display.getAsJsonObject(context).getAsJsonArray("scale").get(0).getAsFloat(), EPSILON, variant + "/" + context);
        }
    }

    @Test
    void vanillaStyleToolsKeepHandheldModels() {
        for (var holder : List.of(RotaryItems.HSLA_STEEL_PICKAXE, RotaryItems.HSLA_STEEL_AXE, RotaryItems.HSLA_STEEL_SHOVEL,
                RotaryItems.HSLA_STEEL_HOE, RotaryItems.HSLA_STEEL_SWORD, RotaryItems.BEDROCK_ALLOY_PICK, RotaryItems.BEDROCK_ALLOY_AXE,
                RotaryItems.BEDROCK_ALLOY_SHOVEL, RotaryItems.BEDROCK_ALLOY_HOE, RotaryItems.BEDROCK_ALLOY_SWORD)) {
            String id = BuiltInRegistries.ITEM.getKey(holder.get()).getPath();
            assertEquals("minecraft:item/handheld", resource("models/item/" + id + ".json").get("parent").getAsString(), id);
        }
    }

    private JsonObject resource(String relative) {
        String path = "assets/rotarycraft/" + relative;
        var stream = getClass().getClassLoader().getResourceAsStream(path);
        assertNotNull(stream, path);
        try (var reader = new InputStreamReader(stream, java.nio.charset.StandardCharsets.UTF_8)) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        } catch (java.io.IOException e) {
            throw new java.io.UncheckedIOException(e);
        }
    }
}
