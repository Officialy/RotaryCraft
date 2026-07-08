/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.renders;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

import org.joml.Matrix4f;

/**
 * Shared flat pipe/duct shell geometry — the code equivalent of the 1.7.10
 * {@code PipeBodyRenderer.doRenderFace} pass. The shell is NOT a 3D box: it is a hollow frame of
 * FLAT quads at the six shell planes (2px and 14px). Each unconnected face is a 12x12 plate made
 * of 2px strips of the pipe's base texture around an 8x8 glass window; a frame strip along an
 * edge is only drawn when BOTH faces meeting at that edge are unconnected. Connected sides open
 * into a flat-walled arm: 2px corner strips running to the block face with glass continuing over
 * the middle, so the glass reads as one continuous window down a pipe run.
 *
 * <p>Emitted at runtime into a {@link VertexConsumer} (rather than baked JSON models) so both
 * RotaryCraft pipes and ReactorCraft ducts share one implementation, and so the geometry stays
 * available for dynamic effects (charge glow textures now; pressure-driven deformation later).
 * Frame strips use position-matched 1:1 UVs; glass uses the original 1/8-inset UV mapping
 * ([2..14] over the 8px cap window, halves over the arm panes).</p>
 */
public final class PipeShellRenderer {

    private PipeShellRenderer() {
    }

    /** One flat axis-aligned quad: the face it renders toward, its bounds and UVs, all in px (0-16). */
    private record Rect(Direction face, float x1, float y1, float z1, float x2, float y2, float z2,
                        float u1, float v1, float u2, float v2) {
    }

    /** Frame strip with position-matched UVs (u/v track the quad's own in-plane coordinates). */
    private static Rect strip(Direction face, float x1, float y1, float z1, float x2, float y2, float z2) {
        return switch (face.getAxis()) {
            case Y -> new Rect(face, x1, y1, z1, x2, y2, z2, x1, z1, x2, z2);
            case Z -> new Rect(face, x1, y1, z1, x2, y2, z2, x1, 16 - y2, x2, 16 - y1);
            case X -> new Rect(face, x1, y1, z1, x2, y2, z2, z1, 16 - y2, z2, 16 - y1);
        };
    }

    /**
     * The 12 frame edges: a pair of perpendicular flat strips meeting at the shared shell edge,
     * drawn only when both adjoining faces are unconnected. Horizontal strips span the full 2..14;
     * strips along the vertical edges span only 4..12 (the corners belong to the horizontal pair),
     * so a closed face tiles with no overlap and no holes.
     */
    private record Edge(Direction a, Direction b, Rect r1, Rect r2) {
    }

    private static final Edge[] EDGES = {
            new Edge(Direction.UP, Direction.NORTH, strip(Direction.UP, 2, 14, 2, 14, 14, 4), strip(Direction.NORTH, 2, 12, 2, 14, 14, 2)),
            new Edge(Direction.UP, Direction.SOUTH, strip(Direction.UP, 2, 14, 12, 14, 14, 14), strip(Direction.SOUTH, 2, 12, 14, 14, 14, 14)),
            new Edge(Direction.UP, Direction.WEST, strip(Direction.UP, 2, 14, 4, 4, 14, 12), strip(Direction.WEST, 2, 12, 2, 2, 14, 14)),
            new Edge(Direction.UP, Direction.EAST, strip(Direction.UP, 12, 14, 4, 14, 14, 12), strip(Direction.EAST, 14, 12, 2, 14, 14, 14)),
            new Edge(Direction.DOWN, Direction.NORTH, strip(Direction.DOWN, 2, 2, 2, 14, 2, 4), strip(Direction.NORTH, 2, 2, 2, 14, 4, 2)),
            new Edge(Direction.DOWN, Direction.SOUTH, strip(Direction.DOWN, 2, 2, 12, 14, 2, 14), strip(Direction.SOUTH, 2, 2, 14, 14, 4, 14)),
            new Edge(Direction.DOWN, Direction.WEST, strip(Direction.DOWN, 2, 2, 4, 4, 2, 12), strip(Direction.WEST, 2, 2, 2, 2, 4, 14)),
            new Edge(Direction.DOWN, Direction.EAST, strip(Direction.DOWN, 12, 2, 4, 14, 2, 12), strip(Direction.EAST, 14, 2, 2, 14, 4, 14)),
            new Edge(Direction.NORTH, Direction.WEST, strip(Direction.NORTH, 2, 4, 2, 4, 12, 2), strip(Direction.WEST, 2, 4, 2, 2, 12, 4)),
            new Edge(Direction.NORTH, Direction.EAST, strip(Direction.NORTH, 12, 4, 2, 14, 12, 2), strip(Direction.EAST, 14, 4, 2, 14, 12, 4)),
            new Edge(Direction.SOUTH, Direction.WEST, strip(Direction.SOUTH, 2, 4, 14, 4, 12, 14), strip(Direction.WEST, 2, 4, 12, 2, 12, 14)),
            new Edge(Direction.SOUTH, Direction.EAST, strip(Direction.SOUTH, 12, 4, 14, 14, 12, 14), strip(Direction.EAST, 14, 4, 12, 14, 12, 14)),
    };

    /** Arm corner strips per connected direction: 8 quads running from the shell edge to the block face. */
    private static final Map<Direction, Rect[]> ARMS = new EnumMap<>(Direction.class);

    /** Glass cap window per unconnected direction: 8x8 pane at the shell plane, 1/8-inset UVs. */
    private static final Map<Direction, Rect> CAPS = new EnumMap<>(Direction.class);

    /** Glass arm panes per connected direction: 4 panes running from the cap edge to the block face. */
    private static final Map<Direction, Rect[]> ARM_GLASS = new EnumMap<>(Direction.class);

    static {
        for (Direction d : Direction.values()) {
            ARMS.put(d, armStrips(d));
            ARM_GLASS.put(d, armGlass(d));
        }
        CAPS.put(Direction.UP, new Rect(Direction.UP, 4, 14, 4, 12, 14, 12, 2, 2, 14, 14));
        CAPS.put(Direction.DOWN, new Rect(Direction.DOWN, 4, 2, 4, 12, 2, 12, 2, 2, 14, 14));
        CAPS.put(Direction.NORTH, new Rect(Direction.NORTH, 4, 4, 2, 12, 12, 2, 2, 2, 14, 14));
        CAPS.put(Direction.SOUTH, new Rect(Direction.SOUTH, 4, 4, 14, 12, 12, 14, 2, 2, 14, 14));
        CAPS.put(Direction.WEST, new Rect(Direction.WEST, 2, 4, 4, 2, 12, 12, 2, 2, 14, 14));
        CAPS.put(Direction.EAST, new Rect(Direction.EAST, 14, 4, 4, 14, 12, 12, 2, 2, 14, 14));
    }

    private static Rect[] armStrips(Direction d) {
        float a = d.getAxisDirection() == Direction.AxisDirection.NEGATIVE ? 0 : 14;
        float b = a + 2;
        List<Rect> q = new ArrayList<>(8);
        switch (d.getAxis()) {
            case X -> {
                for (float z0 : new float[]{2, 12}) {
                    q.add(strip(Direction.UP, a, 14, z0, b, 14, z0 + 2));
                    q.add(strip(Direction.DOWN, a, 2, z0, b, 2, z0 + 2));
                }
                for (float y0 : new float[]{2, 12}) {
                    q.add(strip(Direction.NORTH, a, y0, 2, b, y0 + 2, 2));
                    q.add(strip(Direction.SOUTH, a, y0, 14, b, y0 + 2, 14));
                }
            }
            case Y -> {
                for (float z0 : new float[]{2, 12}) {
                    q.add(strip(Direction.WEST, 2, a, z0, 2, b, z0 + 2));
                    q.add(strip(Direction.EAST, 14, a, z0, 14, b, z0 + 2));
                }
                for (float x0 : new float[]{2, 12}) {
                    q.add(strip(Direction.NORTH, x0, a, 2, x0 + 2, b, 2));
                    q.add(strip(Direction.SOUTH, x0, a, 14, x0 + 2, b, 14));
                }
            }
            case Z -> {
                for (float x0 : new float[]{2, 12}) {
                    q.add(strip(Direction.UP, x0, 14, a, x0 + 2, 14, b));
                    q.add(strip(Direction.DOWN, x0, 2, a, x0 + 2, 2, b));
                }
                for (float y0 : new float[]{2, 12}) {
                    q.add(strip(Direction.WEST, 2, y0, a, 2, y0 + 2, b));
                    q.add(strip(Direction.EAST, 14, y0, a, 14, y0 + 2, b));
                }
            }
        }
        return q.toArray(new Rect[0]);
    }

    private static Rect[] armGlass(Direction d) {
        boolean positive = d.getAxisDirection() == Direction.AxisDirection.POSITIVE;
        float a = positive ? 12 : 0;
        float b = positive ? 16 : 4;
        // Each block draws its half of the pane bridging the joint, sampling the matching half of
        // the inset cap texture so the glass tiles seamlessly into the neighbour's own pane.
        float r0 = positive ? 2 : 8;
        float r1 = positive ? 8 : 14;
        return switch (d.getAxis()) {
            case X -> new Rect[]{ // run axis is x = u on all four faces
                    new Rect(Direction.UP, a, 14, 4, b, 14, 12, r0, 2, r1, 14),
                    new Rect(Direction.DOWN, a, 2, 4, b, 2, 12, r0, 2, r1, 14),
                    new Rect(Direction.NORTH, a, 4, 2, b, 12, 2, r0, 2, r1, 14),
                    new Rect(Direction.SOUTH, a, 4, 14, b, 12, 14, r0, 2, r1, 14),
            };
            case Y -> new Rect[]{ // run axis is y = v on all four faces
                    new Rect(Direction.WEST, 2, a, 4, 2, b, 12, 2, r0, 14, r1),
                    new Rect(Direction.EAST, 14, a, 4, 14, b, 12, 2, r0, 14, r1),
                    new Rect(Direction.NORTH, 4, a, 2, 12, b, 2, 2, r0, 14, r1),
                    new Rect(Direction.SOUTH, 4, a, 14, 12, b, 14, 2, r0, 14, r1),
            };
            case Z -> new Rect[]{ // run axis is z: u on the side faces, v on top/bottom
                    new Rect(Direction.UP, 4, 14, a, 12, 14, b, 2, r0, 14, r1),
                    new Rect(Direction.DOWN, 4, 2, a, 12, 2, b, 2, r0, 14, r1),
                    new Rect(Direction.WEST, 2, 4, a, 2, 12, b, r0, 2, r1, 14),
                    new Rect(Direction.EAST, 14, 4, a, 14, 12, b, r0, 2, r1, 14),
            };
        };
    }

    private static final int GLASS_TINT = 0xFFFFFFFF;

    /**
     * Emit the full shell for one pipe block: frame strips of {@code shell} plus {@code glass}
     * windows, driven by the per-side connection flags (indexed by {@link Direction#ordinal()}).
     */
    public static void emitShell(Matrix4f m, VertexConsumer vc, boolean[] conn,
                                 TextureAtlasSprite shell, TextureAtlasSprite glass,
                                 int shellTint, int light, int overlay) {
        for (Edge e : EDGES) {
            if (!conn[e.a().ordinal()] && !conn[e.b().ordinal()]) {
                rect(m, vc, e.r1(), shell, shellTint, light, overlay);
                rect(m, vc, e.r2(), shell, shellTint, light, overlay);
            }
        }
        for (Direction d : Direction.values()) {
            if (conn[d.ordinal()]) {
                for (Rect r : ARMS.get(d))
                    rect(m, vc, r, shell, shellTint, light, overlay);
                for (Rect r : ARM_GLASS.get(d))
                    rect(m, vc, r, glass, GLASS_TINT, light, overlay);
            } else {
                rect(m, vc, CAPS.get(d), glass, GLASS_TINT, light, overlay);
            }
        }
    }

    private static void rect(Matrix4f m, VertexConsumer vc, Rect r, TextureAtlasSprite spr,
                             int tint, int light, int overlay) {
        float x1 = r.x1() / 16F, y1 = r.y1() / 16F, z1 = r.z1() / 16F;
        float x2 = r.x2() / 16F, y2 = r.y2() / 16F, z2 = r.z2() / 16F;
        float u1 = spr.getU0() + (spr.getU1() - spr.getU0()) * r.u1() / 16F;
        float u2 = spr.getU0() + (spr.getU1() - spr.getU0()) * r.u2() / 16F;
        float v1 = spr.getV0() + (spr.getV1() - spr.getV0()) * r.v1() / 16F;
        float v2 = spr.getV0() + (spr.getV1() - spr.getV0()) * r.v2() / 16F;
        Direction f = r.face();
        float nx = f.getStepX(), ny = f.getStepY(), nz = f.getStepZ();
        // u/v axis mapping per plane: Y planes u->x, v->z; Z planes u->x, v->-y; X planes u->z, v->-y.
        switch (f.getAxis()) {
            case Y -> {
                vert(m, vc, x1, y1, z1, u1, v1, tint, light, overlay, nx, ny, nz);
                vert(m, vc, x1, y1, z2, u1, v2, tint, light, overlay, nx, ny, nz);
                vert(m, vc, x2, y2, z2, u2, v2, tint, light, overlay, nx, ny, nz);
                vert(m, vc, x2, y2, z1, u2, v1, tint, light, overlay, nx, ny, nz);
            }
            case Z -> {
                vert(m, vc, x1, y2, z1, u1, v1, tint, light, overlay, nx, ny, nz);
                vert(m, vc, x1, y1, z1, u1, v2, tint, light, overlay, nx, ny, nz);
                vert(m, vc, x2, y1, z2, u2, v2, tint, light, overlay, nx, ny, nz);
                vert(m, vc, x2, y2, z2, u2, v1, tint, light, overlay, nx, ny, nz);
            }
            case X -> {
                vert(m, vc, x1, y2, z1, u1, v1, tint, light, overlay, nx, ny, nz);
                vert(m, vc, x1, y1, z1, u1, v2, tint, light, overlay, nx, ny, nz);
                vert(m, vc, x2, y1, z2, u2, v2, tint, light, overlay, nx, ny, nz);
                vert(m, vc, x2, y2, z2, u2, v1, tint, light, overlay, nx, ny, nz);
            }
        }
    }

    private static void vert(Matrix4f m, VertexConsumer vc, float x, float y, float z, float u, float v,
                             int tint, int light, int overlay, float nx, float ny, float nz) {
        vc.addVertex(m, x, y, z).setColor(tint).setUv(u, v).setOverlay(overlay).setLight(light).setNormal(nx, ny, nz);
    }
}
