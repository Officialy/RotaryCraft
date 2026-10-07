package reika.rotarycraft.renders.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import reika.rotarycraft.base.RotaryModelBase;
import reika.rotarycraft.registry.MachineRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Immutable inventory geometry captured from the real model's renderAll path at resource bake.
 * The raw root omits repeated parts, constant gear offsets and inventory-only visibility rules.
 * Capturing once also keeps deferred item submission independent of mutable model/world state.
 */
public final class MachineItemGeometry {
    private final List<Vertex> vertices;

    private MachineItemGeometry(List<Vertex> vertices) {
        this.vertices = List.copyOf(vertices);
    }

    public static MachineItemGeometry bake(MachineRegistry machine, RotaryModelBase model) {
        return bake(machine, model, false);
    }

    public static MachineItemGeometry bake(MachineRegistry machine, RotaryModelBase model, boolean bedrock) {
        Builder builder = new Builder();
        PoseStack pose = new PoseStack();
        MachineItemPose.apply(machine, pose);
        float phi = machine == MachineRegistry.COMPACTOR ? 1
                : machine == MachineRegistry.PERFORMANCE_ENGINE ? Float.MIN_NORMAL : 0;
        model.renderAll(pose, builder, 0, null, conditions(machine, bedrock), phi, 0);
        builder.finish();
        return new MachineItemGeometry(builder.vertices);
    }

    private static ArrayList<?> conditions(MachineRegistry machine, boolean bedrock) {
        // Original TESRs' out-of-world conditions, with no contents or failed moving parts.
        return switch (machine) {
            case AUTOBREEDER -> new ArrayList<>(List.of(false, false, false, false, false));
            case HYDRO_ENGINE -> new ArrayList<>(List.of(false, bedrock));
            case BEDROCKBREAKER -> new ArrayList<>(List.of(0, 0F));
            case SPAWNERCONTROLLER -> new ArrayList<>(List.of(true));
            default -> new ArrayList<>(List.of(false));
        };
    }

    public void getExtents(Consumer<Vector3fc> output) {
        for (Vertex vertex : vertices)
            output.accept(new Vector3f(vertex.x, vertex.y, vertex.z));
    }

    public void draw(PoseStack.Pose pose, VertexConsumer buffer, int light, int overlay, int color) {
        for (Vertex vertex : vertices) {
            buffer.addVertex(pose, vertex.x, vertex.y, vertex.z)
                    .setColor(color == -1 ? vertex.color : color)
                    .setUv(vertex.u, vertex.v)
                    .setOverlay(overlay).setLight(light)
                    .setNormal(pose, vertex.nx, vertex.ny, vertex.nz);
        }
    }

    private record Vertex(float x, float y, float z, int color, float u, float v, float nx, float ny, float nz) {}

    /** Native ModelPart writes position, colour, UV and normal through the ordinary vertex API. */
    private static final class Builder implements VertexConsumer {
        private final List<Vertex> vertices = new ArrayList<>();
        private boolean pending;
        private float x, y, z, u, v, nx, ny, nz;
        private int color = -1;

        private void finish() {
            if (pending) {
                vertices.add(new Vertex(x, y, z, color, u, v, nx, ny, nz));
                pending = false;
            }
        }

        @Override public VertexConsumer addVertex(float x, float y, float z) {
            finish();
            this.x = x; this.y = y; this.z = z;
            pending = true;
            return this;
        }
        @Override public VertexConsumer setColor(int color) { this.color = color; return this; }
        @Override public VertexConsumer setColor(int r, int g, int b, int a) {
            return setColor(a << 24 | r << 16 | g << 8 | b);
        }
        @Override public VertexConsumer setUv(float u, float v) { this.u = u; this.v = v; return this; }
        @Override public VertexConsumer setNormal(float x, float y, float z) { nx = x; ny = y; nz = z; return this; }
        // Light/overlay are supplied by the live item submit, rather than cached at bake.
        @Override public VertexConsumer setUv1(int u, int v) { return this; }
        @Override public VertexConsumer setUv2(int u, int v) { return this; }
        @Override public VertexConsumer setUv3(float u, float v) { return this; }
        @Override public VertexConsumer setLineWidth(float width) { return this; }
    }
}
