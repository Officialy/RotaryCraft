package reika.rotarycraft.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.joml.Vector3f;
import org.junit.jupiter.api.Test;
import reika.rotarycraft.blockentities.transmission.BlockEntitySplitter;
import reika.rotarycraft.models.animated.SplitterModel;
import reika.rotarycraft.models.animated.SplitterModel2;
import reika.rotarycraft.registry.RotaryBlocks;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/** Checks baked shaft geometry against the real junction IO, without a GPU or client world. */
class SplitterRenderTest {

    @Test
    void allSixteenOrientationsMatchActualPowerConnections() {
        BlockEntitySplitter junction = new BlockEntitySplitter(BlockPos.ZERO, RotaryBlocks.SPLITTER.get().defaultBlockState());
        for (int side = 0; side < 16; side++) {
            junction.setIoside(side);
            boolean mirrored = RenderSplitter.usesMirroredModel(side);
            ModelPart root = (mirrored ? SplitterModel2.createLayer() : SplitterModel.createLayer()).bakeRoot();
            PoseStack pose = new PoseStack();
            RenderSplitter.applyWorldPose(pose, side);
            Direction right = shaftDirection(pose, root.getChild("shape12"), true);
            Direction left = shaftDirection(pose, root.getChild("shape12a"), true);
            Direction bend = shaftDirection(pose, root.getChild("shape14"), false);
            if (junction.isSplitting()) {
                assertEquals(junction.getReadDirection(), right, "split input, ioside=" + side);
                assertEquals(Set.of(junction.getWriteDirection(), junction.getWriteDirection2()), Set.of(left, bend),
                        "split outputs, ioside=" + side);
            } else {
                assertEquals(junction.getWriteDirection(), right, "merge output, ioside=" + side);
                assertEquals(Set.of(junction.getReadDirection(), junction.getReadDirection2()), Set.of(left, bend),
                        "merge inputs, ioside=" + side);
            }
        }
    }

    private static Direction shaftDirection(PoseStack pose, ModelPart shaft, boolean inline) {
        Vector3f v = inline ? new Vector3f((shaft.x + 3) / 16, 0, 0)
                : new Vector3f(0, 0, (shaft.z + 3) / 16);
        pose.last().pose().transformDirection(v);
        assertEquals(0, v.y, 0.00001F, "a horizontal shaft tilted vertically");
        return Math.abs(v.x) > Math.abs(v.z) ? (v.x > 0 ? Direction.EAST : Direction.WEST)
                : (v.z > 0 ? Direction.SOUTH : Direction.NORTH);
    }

    @Test
    void alternateGeometryKeepsItsPartsInBlockSpace() {
        for (ModelPart root : List.of(SplitterModel.createLayer().bakeRoot(), SplitterModel2.createLayer().bakeRoot())) {
            assertEquals(16, root.getChild("shape12").y + 1, 0.00001F);
            assertEquals(15, root.getChild("shape14").y, 0.00001F);
            assertEquals(8, root.getChild("shape3").y, 0.00001F);
        }
    }

    @Test
    void bothBendsAnimateAndPreserveOriginalBrokenFrame() {
        for (SplitterModel model : List.of(new SplitterModel(SplitterModel.createLayer().bakeRoot()),
                new SplitterModel2(SplitterModel2.createLayer().bakeRoot()))) {
            List<Vector3f> stopped = vertices(model, false, 0);
            List<Vector3f> rotating = vertices(model, false, 37);
            List<Vector3f> failed = vertices(model, true, 0);
            assertEquals(stopped.size(), rotating.size());
            assertNotEquals(stopped, rotating, "shaft animation was discarded");
            assertEquals(stopped.subList(0, failed.size()), rotating.subList(0, failed.size()), "housing rotated with the gears");
            assertEquals(failed, vertices(model, true, 37), "broken frame still animates");
            assertTrue(failed.size() > 0 && failed.size() < stopped.size(), "original broken frame should omit the gear groups");
        }
    }

    private static List<Vector3f> vertices(SplitterModel model, boolean failed, float angle) {
        List<Vector3f> vertices = new ArrayList<>();
        VertexConsumer consumer = (VertexConsumer) Proxy.newProxyInstance(VertexConsumer.class.getClassLoader(),
                new Class<?>[]{VertexConsumer.class}, (proxy, method, args) -> {
                    if (method.getName().equals("addVertex"))
                        vertices.add(new Vector3f((float) args[0], (float) args[1], (float) args[2]));
                    return method.getReturnType() == VertexConsumer.class ? proxy : null;
                });
        PoseStack pose = new PoseStack();
        model.renderAll(pose, consumer, 0, null, new ArrayList<>(List.of(failed)), angle, 0);
        assertTrue(pose.last().pose().equals(new org.joml.Matrix4f(), 0.00001F), "model leaked its pose");
        return vertices;
    }
}
