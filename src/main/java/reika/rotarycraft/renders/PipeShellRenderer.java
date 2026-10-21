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
import java.util.List;

import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

import org.joml.Matrix4f;

/**
 * Shared flat pipe/duct shell geometry — the code equivalent of the 1.7.10
 * {@code PipeBodyRenderer.doRenderFace} pass. The shell is NOT a 3D box: it is a hollow frame of
 * FLAT quads at the six shell planes (2px and 14px). Each unconnected face is a 12x12 plate made
 * of 2px strips of the pipe's base texture around an 8x8 glass window; a frame strip along an
 * edge opens into glass when the perpendicular side is connected. Connected sides open
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
    record Rect(Direction face, float x1, float y1, float z1, float x2, float y2, float z2,
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

    /** Immutable geometry for each of the 64 connection masks; never allocate it per frame. */
    record Geometry(List<Rect> frame, List<Rect> glass) {
    }

    private static final Geometry[] GEOMETRY = new Geometry[64];

    static {
        for (int mask = 0; mask < GEOMETRY.length; mask++)
            GEOMETRY[mask] = buildGeometry(mask);
    }

    static Geometry geometry(int mask) {
        return GEOMETRY[mask];
    }

    private static boolean connected(int mask, Direction d) {
        return (mask & (1 << d.ordinal())) != 0;
    }

    private static Geometry buildGeometry(int mask) {
        List<Rect> frame = new ArrayList<>();
        List<Rect> glass = new ArrayList<>();
        for (Direction face : Direction.values()) {
            if (connected(mask, face)) {
                frame.addAll(List.of(armStrips(face)));
                glass.addAll(List.of(armGlass(face)));
                continue;
            }
            // Tile the core face once. A closed perpendicular side contributes a frame border;
            // a connected one extends the window instead. In particular, a corner belongs to
            // either remaining border, or to glass when both perpendicular sides are connected.
            // The old fixed edge strips lost corners when a neighbouring border disappeared;
            // drawing every border instead put steel across the glass connection panes.
            Direction lowU = face.getAxis() == Direction.Axis.X ? Direction.NORTH : Direction.WEST;
            Direction highU = lowU.getOpposite();
            Direction lowV = face.getAxis() == Direction.Axis.Y ? Direction.NORTH : Direction.DOWN;
            Direction highV = lowV.getOpposite();
            float[] edges = {2, 4, 12, 14};
            for (int u = 0; u < 3; u++) {
                for (int v = 0; v < 3; v++) {
                    Rect r = faceRect(face, edges[u], edges[v], edges[u + 1], edges[v + 1]);
                    boolean border = u == 0 && !connected(mask, lowU)
                            || u == 2 && !connected(mask, highU)
                            || v == 0 && !connected(mask, lowV)
                            || v == 2 && !connected(mask, highV);
                    if (border) {
                        frame.add(strip(face, r.x1(), r.y1(), r.z1(), r.x2(), r.y2(), r.z2()));
                    } else {
                        float u1 = glassStart(u), u2 = glassEnd(u);
                        float v1 = glassStart(v), v2 = glassEnd(v);
                        // Side-face texture v runs down, opposite to the in-plane y coordinate.
                        if (face.getAxis() != Direction.Axis.Y) {
                            v1 = v == 0 ? 8 : v == 1 ? 2 : 5;
                            v2 = v == 0 ? 11 : v == 1 ? 14 : 8;
                        }
                        glass.add(new Rect(face, r.x1(), r.y1(), r.z1(), r.x2(), r.y2(), r.z2(),
                                u1, v1, u2, v2));
                    }
                }
            }
        }
        return new Geometry(List.copyOf(frame), List.copyOf(glass));
    }

    private static float glassStart(int segment) {
        return segment == 0 ? 11 : 2;
    }

    private static float glassEnd(int segment) {
        return segment == 2 ? 5 : 14;
    }

    /** In-plane coordinates are x/z on Y faces, x/y on Z faces, and z/y on X faces. */
    private static Rect faceRect(Direction face, float a, float b, float c, float d) {
        float plane = face.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 14 : 2;
        return switch (face.getAxis()) {
            case Y -> new Rect(face, a, plane, b, c, plane, d, 0, 0, 0, 0);
            case Z -> new Rect(face, a, b, plane, c, d, plane, 0, 0, 0, 0);
            case X -> new Rect(face, plane, b, a, plane, d, c, 0, 0, 0, 0);
        };
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
        float a = positive ? 14 : 0;
        float b = positive ? 16 : 2;
        // Only the portion outside the core belongs to the arm. The core face tiles cover
        // 2..14 already; drawing 0..4 / 12..16 here would overlap them at every connection.
        // Keep the corresponding subrange of the legacy half-pane UVs.
        float r0 = positive ? 5 : 8;
        float r1 = positive ? 8 : 11;
        float verticalV1 = positive ? 2 : 11;
        float verticalV2 = positive ? 5 : 14;
        return switch (d.getAxis()) {
            case X -> new Rect[]{ // run axis is x = u on all four faces
                    new Rect(Direction.UP, a, 14, 4, b, 14, 12, r0, 2, r1, 14),
                    new Rect(Direction.DOWN, a, 2, 4, b, 2, 12, r0, 2, r1, 14),
                    new Rect(Direction.NORTH, a, 4, 2, b, 12, 2, r0, 2, r1, 14),
                    new Rect(Direction.SOUTH, a, 4, 14, b, 12, 14, r0, 2, r1, 14),
            };
            case Y -> new Rect[]{ // run axis is y = v on all four faces
                    new Rect(Direction.WEST, 2, a, 4, 2, b, 12, 2, verticalV1, 14, verticalV2),
                    new Rect(Direction.EAST, 14, a, 4, 14, b, 12, 2, verticalV1, 14, verticalV2),
                    new Rect(Direction.NORTH, 4, a, 2, 12, b, 2, 2, verticalV1, 14, verticalV2),
                    new Rect(Direction.SOUTH, 4, a, 14, 12, b, 14, 2, verticalV1, 14, verticalV2),
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
        int mask = 0;
        for (Direction d : Direction.values()) {
            if (conn[d.ordinal()]) mask |= 1 << d.ordinal();
        }
        Geometry geometry = geometry(mask);
        for (Rect r : geometry.frame())
            rect(m, vc, r, shell, shellTint, light, overlay);
        for (Rect r : geometry.glass())
            rect(m, vc, r, glass, GLASS_TINT, light, overlay);
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
