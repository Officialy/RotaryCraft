/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.auxiliary;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.api.power.ShaftPowerEmitter;
import reika.rotarycraft.api.power.ShaftPowerReceiver;
import reika.rotarycraft.base.blockentity.BlockEntityIOMachine;
import reika.rotarycraft.blockentities.BlockEntityWinder;
import reika.rotarycraft.blockentities.transmission.BlockEntityDistributionClutch;
import reika.rotarycraft.blockentities.transmission.BlockEntityShaft;
import reika.rotarycraft.blockentities.transmission.BlockEntitySplitter;
import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.RotaryItems;

/**
 * 1.21.x port: was a stub for the whole refactor and rendered nothing. Now ported against the
 * {@link SubmitNodeCollector} pipeline used by {@link net.minecraft.client.renderer.blockentity.BlockEntityRenderer}.
 *
 * <p>{@link #renderIO} is the single entry point; each block's {@code submit} hook calls it with
 * the {@link PoseStack} (already translated to the block's local origin by the dispatcher) and
 * the collector. We emit one translucent filled cube per face-direction the BE wants to flag,
 * coloured green for input and red for output (legacy semantics). All the IO-arrow geometry
 * uses {@link RenderTypes#debugFilledBox}, which is a quads pipeline with vertex colours and
 * no texture — exactly the right primitive for solid translucent box overlays.
 */
public abstract class IORenderer {

    private static final Direction[] dirs = Direction.values();

    /* ----------------------------------------------------------------------- */
    /*  Public entry points                                                    */
    /* ----------------------------------------------------------------------- */

    public static void renderIO(PoseStack matrixStack, SubmitNodeCollector collector, BlockEntity teb, BlockPos pos) {
        renderIO(matrixStack, collector, teb, pos.getX(), pos.getY(), pos.getZ());
    }

    /* ----------------------------------------------------------------------- */
    /*  Legacy MultiBufferSource overloads (no-op).                            */
    /*                                                                         */
    /*  Several BlockEntityRenderers still hold their pre-26.1 {@code render}  */
    /*  method (with {@code MultiBufferSource bufferSource}) as dead code —    */
    /*  vanilla no longer calls those signatures, but the call to              */
    /*  {@code IORenderer.renderIO(stack, bufferSource, te, ...)} inside them  */
    /*  still has to compile. The {@link SubmitNodeCollector}-based path above */
    /*  is the live one; these overloads short-circuit so the legacy code is   */
    /*  harmless until each renderer's {@code submit(...)} hook is rewritten.  */
    /* ----------------------------------------------------------------------- */
    // 26.2: MultiBufferSource no longer exists. Legacy overloads retained as no-ops but must not mention the removed type.
    @SuppressWarnings("unused")
    public static void renderIO(PoseStack matrixStack, Object legacyBufferSource, BlockEntity teb, BlockPos pos) { /* no-op; legacy path */ }
    @SuppressWarnings("unused")
    public static void renderIO(PoseStack matrixStack, Object legacyBufferSource, BlockEntity teb, double x, double y, double z) { /* no-op; legacy path */ }

    public static void renderIO(PoseStack matrixStack, SubmitNodeCollector collector, BlockEntity teb, double x, double y, double z) {
        ItemStack is = Minecraft.getInstance().player.getItemBySlot(EquipmentSlot.HEAD);
        boolean flag = is.is(RotaryItems.IO_GOGGLES.get());
        int par4 = 0;
        if (teb instanceof BlockEntityIOMachine te) {
            if (flag) te.iotick = 512;
            if (te.iotick <= 0) return;

            if (teb instanceof BlockEntitySplitter ts) {
                if (ts.isSplitting()) {
                    renderOut(matrixStack, collector, ts.getWriteDirection().getStepX(),  par4, ts.getWriteDirection().getStepZ(),  ts.iotick);
                    renderOut(matrixStack, collector, ts.getWriteDirection2().getStepX(), par4, ts.getWriteDirection2().getStepZ(), ts.iotick);
                    if (ts.getReadDirection() != null)
                        renderIn(matrixStack, collector, ts.getReadDirection().getStepX(), par4, ts.getReadDirection().getStepZ(), ts.iotick);
                } else {
                    if (ts.getWriteDirection() != null)
                        renderOut(matrixStack, collector, ts.getWriteDirection().getStepX(), par4, ts.getWriteDirection().getStepZ(), ts.iotick);
                    if (ts.getReadDirection() != null)
                        renderIn(matrixStack, collector, ts.getReadDirection().getStepX(), par4, ts.getReadDirection().getStepZ(), ts.iotick);
                    if (ts.getReadDirection2() != null)
                        renderIn(matrixStack, collector, ts.getReadDirection2().getStepX(), par4, ts.getReadDirection2().getStepZ(), ts.iotick);
                }
                return;
            }
            if (teb instanceof BlockEntityShaft ts && ts.isCross()) {
                if (ts.getWriteDirection() != null)
                    renderOut(matrixStack, collector, ts.getWriteDirection().getStepX(), par4, ts.getWriteDirection().getStepZ(), ts.iotick);
                if (ts.getWriteDirection2() != null)
                    renderOut(matrixStack, collector, ts.getWriteDirection2().getStepX(), par4, ts.getWriteDirection2().getStepZ(), ts.iotick);
                if (ts.getReadDirection() != null)
                    renderIn(matrixStack, collector, ts.getReadDirection().getStepX(), par4, ts.getReadDirection().getStepZ(), ts.iotick);
                if (ts.getReadDirection2() != null)
                    renderIn(matrixStack, collector, ts.getReadDirection2().getStepX(), par4, ts.getReadDirection2().getStepZ(), ts.iotick);
                return;
            }
            if (teb instanceof BlockEntityDistributionClutch td) {
                Direction read = td.getInputDirection();
                if (read != null) renderIn(matrixStack, collector, read.getStepX(), read.getStepY(), read.getStepZ(), te.iotick);
                for (int i = 0; i < 4; i++) {
                    Direction dir = Direction.values()[i + 2];
                    if (td.isOutputtingToSide(dir))
                        renderOut(matrixStack, collector, dir.getStepX(), dir.getStepY(), dir.getStepZ(), te.iotick);
                }
                return;
            }
            if (teb instanceof BlockEntityWinder ts) {
                if (ts.winding && ts.getReadDirection() != null)
                    renderIn(matrixStack, collector, ts.getReadDirection().getStepX(), ts.getReadDirection().getStepY(), ts.getReadDirection().getStepZ(), ts.iotick);
                else if (ts.getWriteDirection() != null)
                    renderOut(matrixStack, collector, ts.getWriteDirection().getStepX(), ts.getWriteDirection().getStepY(), ts.getWriteDirection().getStepZ(), ts.iotick);
                return;
            }
            if (te.isOmniSided) {
                if (te.getMachine().getMaxY(te) == 1) renderIn(matrixStack, collector, te.getPointingOffsetX(),     te.getPointingOffsetY() + 1, te.getPointingOffsetZ(),     te.iotick);
                if (te.getMachine().getMinY(te) == 0) renderIn(matrixStack, collector, te.getPointingOffsetX(),     te.getPointingOffsetY() - 1, te.getPointingOffsetZ(),     te.iotick);
                if (te.getMachine().getMaxX(te) == 1) renderIn(matrixStack, collector, te.getPointingOffsetX() + 1, te.getPointingOffsetY(),     te.getPointingOffsetZ(),     te.iotick);
                if (te.getMachine().getMinX(te) == 0) renderIn(matrixStack, collector, te.getPointingOffsetX() - 1, te.getPointingOffsetY(),     te.getPointingOffsetZ(),     te.iotick);
                if (te.getMachine().getMaxZ(te) == 1) renderIn(matrixStack, collector, te.getPointingOffsetX(),     te.getPointingOffsetY(),     te.getPointingOffsetZ() + 1, te.iotick);
                if (te.getMachine().getMinZ(te) == 0) renderIn(matrixStack, collector, te.getPointingOffsetX(),     te.getPointingOffsetY(),     te.getPointingOffsetZ() - 1, te.iotick);
                return;
            }
            if (te.getWriteDirection() != null) {
                Direction d = te.getWriteDirection();
                renderOut(matrixStack, collector, d.getStepX(), d.getStepY(), d.getStepZ(), te.iotick);
            }
            if (te.getWriteDirection2() != null) {
                Direction d = te.getWriteDirection2();
                renderOut(matrixStack, collector, d.getStepX(), d.getStepY(), d.getStepZ(), te.iotick);
            }
            if (te.getReadDirection() != null) {
                Direction d = te.getReadDirection();
                renderIn(matrixStack, collector, d.getStepX() + te.getPointingOffsetX(), d.getStepY() + te.getPointingOffsetY(), d.getStepZ() + te.getPointingOffsetZ(), te.iotick);
            }
            if (te.getReadDirection2() != null) {
                Direction d = te.getReadDirection2();
                renderIn(matrixStack, collector, d.getStepX() + te.getPointingOffsetX(), d.getStepY() + te.getPointingOffsetY(), d.getStepZ() + te.getPointingOffsetZ(), te.iotick);
            }
            if (te.getReadDirection3() != null) {
                Direction d = te.getReadDirection3();
                renderIn(matrixStack, collector, d.getStepX() + te.getPointingOffsetX(), d.getStepY() + te.getPointingOffsetY(), d.getStepZ() + te.getPointingOffsetZ(), te.iotick);
            }
            if (te.getReadDirection4() != null) {
                Direction d = te.getReadDirection4();
                renderIn(matrixStack, collector, d.getStepX() + te.getPointingOffsetX(), d.getStepY() + te.getPointingOffsetY(), d.getStepZ() + te.getPointingOffsetZ(), te.iotick);
            }
        } else {
            // Plain power emitters / receivers (engines, shaft consumers) flagging which sides
            // they can accept or output along.
            if (teb instanceof ShaftPowerReceiver sr) {
                int io = sr.getIORenderAlpha();
                if (flag) io = 255;
                if (io <= 0) return;
                for (int i = 0; i < 6; i++) {
                    Direction dir = dirs[i];
                    if (sr.canReadFrom(dir))
                        renderIn(matrixStack, collector, dir.getStepX(), dir.getStepY(), dir.getStepZ(), io);
                }
            }
            if (teb instanceof ShaftPowerEmitter se) {
                int io = se.getIORenderAlpha();
                if (flag) io = 255;
                if (io <= 0) return;
                for (int i = 0; i < 6; i++) {
                    Direction dir = dirs[i];
                    if (se.canWriteTo(dir))
                        renderOut(matrixStack, collector, dir.getStepX(), dir.getStepY(), dir.getStepZ(), io);
                }
            }
        }
        // 26.1: cooling fin isn't an IOMachine — it's a TemperatureTE. The original 1.7
        // renderer drew a cyan (0, 127, 255) bounding box around the target block via
        // ReikaAABBHelper.renderAABB. Since that API isn't ported yet, we render a cyan IO
        // cube on the FACING face instead — same colour as the original target indicator.
        if (teb instanceof reika.rotarycraft.blockentities.auxiliary.BlockEntityCoolingFin fin) {
            int io = fin.ticks; // same per-tick decay timer the IOMachine path uses
            if (flag) io = 255;
            if (io <= 0) return;
            net.minecraft.world.level.block.state.BlockState st = fin.getBlockState();
            if (st != null && st.hasProperty(reika.rotarycraft.base.blocks.BlockRotaryCraftMachine.FACING)) {
                Direction dir = st.getValue(reika.rotarycraft.base.blocks.BlockRotaryCraftMachine.FACING);
                // Cyan (0, 127, 255) — matches original 1.7 target bounding box colour
                int[] color = {0, 127, 255, io};
                renderBox(matrixStack, collector, dir.getStepX(), dir.getStepY(), dir.getStepZ(), color);
            }
        }
    }

    /* ----------------------------------------------------------------------- */
    /*  Per-direction helpers (color and dispatch)                             */
    /* ----------------------------------------------------------------------- */

    private static void renderOut(PoseStack matrixStack, SubmitNodeCollector collector, float x, float y, float z, int a) {
        int[] color = {255, 0, 0, a};
        if (ConfigRegistry.COLORBLIND.getState()) color[0] = 0;
        renderBox(matrixStack, collector, x, y, z, color);
    }

    private static void renderIn(PoseStack matrixStack, SubmitNodeCollector collector, float x, float y, float z, int a) {
        int[] color = {0, 255, 0, a};
        if (ConfigRegistry.COLORBLIND.getState()) {
            color[0] = 255;
            color[2] = 255;
        }
        renderBox(matrixStack, collector, x, y, z, color);
    }

    /* ----------------------------------------------------------------------- */
    /*  Geometry submission                                                    */
    /* ----------------------------------------------------------------------- */

    /**
     * Emits one translucent unit cube at local offset {@code (ox, oy, oz)} (in block coordinates,
     * relative to the BE's origin) with the given RGBA. Uses {@link RenderTypes#debugFilledBox}
     * which is a quads-mode RenderType taking position + color and no texture — exactly the
     * right primitive for a flat translucent overlay box.
     *
     * <p>The 6 faces are emitted in QUADS order so {@link SubmitNodeCollector#submitCustomGeometry}
     * batches them into a single draw call.
     */
    private static void renderBox(PoseStack stack, SubmitNodeCollector collector, float ox, float oy, float oz, int[] color) {
        if (color[3] <= 0) return;
        // Legacy 1.7 renderBox set quad alpha to {@code colorAlpha * 0.375} (translucent fill)
        // and drew the 12 cube edges at FULL alpha (opaque cell-shaded outline). Reproduce both:
        // a debugFilledBox draw for the body, then a debugLineStrip-ish draw for the wireframe.
        int rawAlpha = Math.min(255, color[3]);
        int fillAlpha = Math.max(0, Math.min(96, (int) (rawAlpha * 0.375f)));
        int outlineAlpha = rawAlpha; // edges stay opaque so the box has a clear silhouette
        int fillRgba    = (fillAlpha    << 24) | ((color[0] & 0xFF) << 16) | ((color[1] & 0xFF) << 8) | (color[2] & 0xFF);
        int outlineRgba = (outlineAlpha << 24) | ((color[0] & 0xFF) << 16) | ((color[1] & 0xFF) << 8) | (color[2] & 0xFF);

        final float x0 = ox, y0 = oy, z0 = oz;
        final float x1 = ox + 1, y1 = oy + 1, z1 = oz + 1;
        // Filled translucent body.
        // 26.1 fix: was using {@code RenderTypes.debugFilledBox()} which has
        // {@code CompareOp.LESS_THAN_OR_EQUAL} depth state — so when an IO cube sat behind the
        // host block from the camera's perspective (most commonly the W/N neighbour cubes when
        // the player is on the E/S side), the block model occluded it and the user couldn't
        // see those faces. Switch to our {@link reika.rotarycraft.renders.RotaryRenderPipelines#NO_DEPTH_FILLED_BOX_TYPE}
        // which is the same pipeline with the depth test relaxed to {@code ALWAYS_PASS}, so
        // the box draws regardless of what's in front of it.
        collector.submitCustomGeometry(stack, reika.rotarycraft.renders.RotaryRenderPipelines.NO_DEPTH_FILLED_BOX_TYPE,
                (pose, buffer) -> emitCube(pose, buffer, x0, y0, z0, x1, y1, z1, fillRgba));
        // Opaque wireframe — 12 edges. Use our {@code NO_DEPTH_LINES_TYPE} so the wireframe
        // mirrors the fill in being visible regardless of camera angle (vanilla
        // {@code RenderTypes.lines} is still depth-tested and would cull edges that sit
        // behind the host block from the camera's perspective).
        collector.submitCustomGeometry(stack, reika.rotarycraft.renders.RotaryRenderPipelines.NO_DEPTH_LINES_TYPE,
                (pose, buffer) -> emitCubeEdges(pose, buffer, x0, y0, z0, x1, y1, z1, outlineRgba));
    }

    /** Emits the 12 edges of an axis-aligned cube as line vertices (24 verts, 12 pairs). */
    private static void emitCubeEdges(PoseStack.Pose pose, VertexConsumer b, float x0, float y0, float z0, float x1, float y1, float z1, int rgba) {
        // bottom rectangle
        line(pose, b, x0, y0, z0, x1, y0, z0, rgba);
        line(pose, b, x1, y0, z0, x1, y0, z1, rgba);
        line(pose, b, x1, y0, z1, x0, y0, z1, rgba);
        line(pose, b, x0, y0, z1, x0, y0, z0, rgba);
        // top rectangle
        line(pose, b, x0, y1, z0, x1, y1, z0, rgba);
        line(pose, b, x1, y1, z0, x1, y1, z1, rgba);
        line(pose, b, x1, y1, z1, x0, y1, z1, rgba);
        line(pose, b, x0, y1, z1, x0, y1, z0, rgba);
        // vertical struts
        line(pose, b, x0, y0, z0, x0, y1, z0, rgba);
        line(pose, b, x1, y0, z0, x1, y1, z0, rgba);
        line(pose, b, x1, y0, z1, x1, y1, z1, rgba);
        line(pose, b, x0, y0, z1, x0, y1, z1, rgba);
    }

    /**
     * One line segment. {@link RenderTypes#lines} uses the POSITION_COLOR_NORMAL_LINE_WIDTH
     * vertex format — every vertex must carry a {@code setLineWidth} call or
     * {@code BufferBuilder.endLastVertex} crashes with "Missing elements in vertex: LineWidth".
     */
    private static void line(PoseStack.Pose pose, VertexConsumer b, float x1, float y1, float z1, float x2, float y2, float z2, int rgba) {
        float nx = x2 - x1, ny = y2 - y1, nz = z2 - z1;
        float len = (float) Math.sqrt(nx * nx + ny * ny + nz * nz);
        if (len > 0) { nx /= len; ny /= len; nz /= len; }
        b.addVertex(pose, x1, y1, z1).setColor(rgba).setNormal(pose, nx, ny, nz).setLineWidth(2.0F);
        b.addVertex(pose, x2, y2, z2).setColor(rgba).setNormal(pose, nx, ny, nz).setLineWidth(2.0F);
    }

    private static void emitCube(PoseStack.Pose pose, VertexConsumer b, float x0, float y0, float z0, float x1, float y1, float z1, int rgba) {
        // 6 faces, each 4 vertices in CCW winding for back-face culling friendliness.
        // -Y (bottom)
        b.addVertex(pose, x0, y0, z0).setColor(rgba);
        b.addVertex(pose, x1, y0, z0).setColor(rgba);
        b.addVertex(pose, x1, y0, z1).setColor(rgba);
        b.addVertex(pose, x0, y0, z1).setColor(rgba);
        // +Y (top)
        b.addVertex(pose, x0, y1, z1).setColor(rgba);
        b.addVertex(pose, x1, y1, z1).setColor(rgba);
        b.addVertex(pose, x1, y1, z0).setColor(rgba);
        b.addVertex(pose, x0, y1, z0).setColor(rgba);
        // -Z
        b.addVertex(pose, x0, y0, z0).setColor(rgba);
        b.addVertex(pose, x0, y1, z0).setColor(rgba);
        b.addVertex(pose, x1, y1, z0).setColor(rgba);
        b.addVertex(pose, x1, y0, z0).setColor(rgba);
        // +Z
        b.addVertex(pose, x1, y0, z1).setColor(rgba);
        b.addVertex(pose, x1, y1, z1).setColor(rgba);
        b.addVertex(pose, x0, y1, z1).setColor(rgba);
        b.addVertex(pose, x0, y0, z1).setColor(rgba);
        // -X
        b.addVertex(pose, x0, y0, z1).setColor(rgba);
        b.addVertex(pose, x0, y1, z1).setColor(rgba);
        b.addVertex(pose, x0, y1, z0).setColor(rgba);
        b.addVertex(pose, x0, y0, z0).setColor(rgba);
        // +X
        b.addVertex(pose, x1, y0, z0).setColor(rgba);
        b.addVertex(pose, x1, y1, z0).setColor(rgba);
        b.addVertex(pose, x1, y1, z1).setColor(rgba);
        b.addVertex(pose, x1, y0, z1).setColor(rgba);
    }
}
