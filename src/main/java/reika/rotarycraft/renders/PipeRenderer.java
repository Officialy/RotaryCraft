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

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blockentity.BlockEntityPiping;
import reika.rotarycraft.registry.RotaryFluids;

/**
 * 26.1 port of the 1.7 {@code Reika.RotaryCraft.Renders.PipeRenderer}.
 *
 * <p>Faithful reproduction of the legacy {@code renderLiquid} routine — for every side of the
 * pipe, draws the visible fluid surface (either a cap at the pipe inner edge if the side is
 * not connected, or four "tube wall" quads extending from the core to the block face if the
 * side is connected to a neighbour). Uses the legacy half-width / window-width constants and
 * vertex coordinates verbatim, so the visual matches the original pixel-for-pixel.</p>
 *
 * <p>The iron shell of the pipe (12-px cross-frame) is still drawn via the multipart blockstate
 * JSON — only the fluid is BER-rendered, because the fluid surface needs to update every tick
 * based on the BE's {@code liquid} type and {@code liquidLevel}, which is exactly what a BER
 * is for. The fluid sprite comes from the vanilla block-atlas lookup of each fluid's still
 * texture; for fluids whose still texture isn't on the atlas yet (legacy custom-fluid case),
 * we fall back to a tinted quad using the same per-fluid colour palette as the reservoir.</p>
 */
public class PipeRenderer extends RotaryTERenderer<BlockEntityPiping> {

    // Legacy 1.7 constants — see Reika.RotaryCraft.Renders.PipeRenderer.
    private static final float SIZE   = 0.75F / 2F;          // 0.375 — half-width of the pipe
    private static final double IN    = 0.5 + SIZE - 0.01;   // 0.865 — inner-far edge
    private static final double IN2   = 0.5 - SIZE + 0.01;   // 0.135 — inner-near edge
    private static final double DD2   = IN - IN2;            // 0.730 — inner span

    // Legacy "glass window" pane — see PipeBodyRenderer.doRenderFace. Spans only the central
    // window, not the full pipe cross-section, leaving the surrounding shell frame (drawn by the
    // static blockstate model) visible as a border.
    private static final float  WINDOW = 0.5F / 2F;           // 0.25 — half-width of the window pane
    private static final double INW    = 0.5 + WINDOW - 0.01; // 0.74
    private static final double INW2   = 0.5 - WINDOW + 0.01; // 0.26

    // The static core.json model's solid faces sit at the SIZE-based box boundary (2px..14px,
    // i.e. 0.125/0.875) — NOT at IN/IN2 (which are inset further toward the centre, for the fluid
    // cap). A glass quad drawn at IN/IN2 would sit INSIDE that opaque box and never be visible;
    // the window must be flush with (or just outside) the core's true outer face.
    private static final double DD     = 0.5 - SIZE;       // 0.125 — core box inset
    private static final double GOUT   = 1.0 - DD + 0.001; // 0.876 — just outside the +face
    private static final double GOUT2  = DD - 0.001;       // 0.124 — just outside the -face

    private final SpriteGetter sprites;

    public PipeRenderer(BlockEntityRendererProvider.Context context) {
        this.sprites = context.sprites();
    }

    @Override
    public void submit(BlockEntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;
        BlockEntity be = level.getBlockEntity(state.blockPos);
        if (!(be instanceof BlockEntityPiping tile)) return;

        Fluid fluid = tile.getAttributes();
        boolean hasFluid = fluid != null && fluid != Fluids.EMPTY && tile.getFluidLevel() > 0;

        // Each pipe type now has its own static multipart shell (BlockPipeShell + the hand-authored
        // blockstate/models under assets/rotarycraft/blockstates/<type>.json) drawing the visible frame
        // (steel / planks / obsidian / lapis / nether-brick / bedrock, matching 1.7.10's per-material
        // pipe icons). The BER draws two things on top of that frame: the legacy glass "window" pane
        // on every open (unconnected) face — see {@code PipeBodyRenderer.doRenderFace} — and, when
        // there is fluid, the fluid surface through the open core (cap on closed ends, tube walls on
        // connected ends), matching the legacy {@code renderLiquid} routine.
        TextureAtlasSprite glass = glassSprite();
        TextureAtlasSprite fluidSprite = hasFluid ? stillSpriteFor(fluid) : null;
        int tint = hasFluid ? fluidTint(fluid) : 0;

        Matrix4f pose = poseStack.last().pose();
        int light = state.lightCoords;
        int overlay = OverlayTexture.NO_OVERLAY;

        RenderType rt = RenderTypes.entityTranslucent(TextureAtlas.LOCATION_BLOCKS);
        PoseStack snapped = new PoseStack();
        snapped.last().set(poseStack.last());

        collector.submitCustomGeometry(poseStack, rt, (pose2, vc) -> {
            Matrix4f m = snapped.last().pose();

            float gu  = glass.getU0();
            float gv  = glass.getV0();
            float gu2 = glass.getU1();
            float gv2 = glass.getV1();

            float u = 0, v = 0, u2 = 0, v2 = 0;
            double du = 0;
            if (hasFluid) {
                u  = fluidSprite.getU0();
                v  = fluidSprite.getV0();
                u2 = fluidSprite.getU1();
                v2 = fluidSprite.getV1();
                // Arm-extension texture coordinate offset along the flow axis (matches legacy
                // {@code double du = dd2*(u2-u)/4D}).
                du = DD2 * (u2 - u) / 4D;
            }

            boolean[] connectedDirs = new boolean[6];
            for (Direction d : Direction.values()) connectedDirs[d.ordinal()] = isConnected(tile, d);

            for (Direction dir : Direction.values()) {
                boolean connected = connectedDirs[dir.ordinal()];
                if (!connected) {
                    emitGlassWindow(m, vc, dir, gu, gv, gu2, gv2, light, overlay);
                    // Legacy doRenderFace: if a perpendicular neighbour IS connected, also draw a
                    // copy of this window translated half a block toward it. That half-block strip
                    // fills the gap between this window and the neighbour's own (mirrored) window,
                    // so a run of connected pipes reads as one continuous open trough instead of
                    // isolated per-block squares.
                    for (Direction perp : Direction.values()) {
                        if (perp.getAxis() == dir.getAxis()) continue;
                        if (connectedDirs[perp.ordinal()]) {
                            Matrix4f shifted = new Matrix4f(m).translate(
                                    perp.getStepX() * 0.5F, perp.getStepY() * 0.5F, perp.getStepZ() * 0.5F);
                            emitGlassWindow(shifted, vc, dir, gu, gv, gu2, gv2, light, overlay);
                        }
                    }
                }
                if (!hasFluid) continue;
                if (connected) emitConnectedFluid(m, vc, dir, (float) u, (float) v, (float) u2, (float) v2, (float) du, tint, light, overlay);
                else            emitCap(m, vc, dir, (float) u, (float) v, (float) u2, (float) v2, tint, light, overlay);
            }
        });
    }

    /**
     * Reflectively asks the BE whether it's connected on the given side. Uses
     * {@link BlockEntityPiping#isConnectedDirectly} because the legacy
     * {@code isConnectionValidForSide} applied a 1.7-only render-pass swap that's no longer
     * relevant in 26.1.
     */
    private static boolean isConnected(BlockEntityPiping tile, Direction dir) {
        return tile.isConnectedDirectly(dir);
    }

    /** Tint for the glass window pane — plain white, matching legacy {@code glColor4f(1,1,1,1)}. */
    private static final int GLASS_TINT = 0xFFFFFFFF;

    /**
     * Translucent glass "window" pane on a face with no neighbour pipe / connector — the legacy
     * {@code iconBlocks[meta][1] = Blocks.glass} overlay. Sits just OUTSIDE the static blockstate
     * model's solid core box ({@link #GOUT}/{@link #GOUT2}, NOT the inset {@link #IN}/{@link #IN2}
     * fluid-cap depth — that plane is hidden inside the opaque core and never visible), spanning
     * only the central {@link #WINDOW} extent so the core's own face shows through as a border.
     */
    private static void emitGlassWindow(Matrix4f m, VertexConsumer vc, Direction dir,
                                         float u, float v, float u2, float v2, int light, int overlay) {
        switch (dir) {
            case UP -> {
                quad(m, vc, INW2, GOUT, INW,  u,  v2, INW,  GOUT, INW,  u2, v2, INW,  GOUT, INW2, u2, v,  INW2, GOUT, INW2, u,  v,  GLASS_TINT, light, overlay, 0, 1, 0);
            }
            case DOWN -> {
                quad(m, vc, INW2, GOUT2, INW2, u,  v,  INW,  GOUT2, INW2, u2, v,  INW,  GOUT2, INW,  u2, v2, INW2, GOUT2, INW,  u,  v2, GLASS_TINT, light, overlay, 0, -1, 0);
            }
            case SOUTH -> {
                quad(m, vc, INW, INW, GOUT,  u,  v,  INW2, INW, GOUT,  u2, v,  INW2, INW2, GOUT,  u2, v2, INW, INW2, GOUT,  u,  v2, GLASS_TINT, light, overlay, 0, 0, 1);
            }
            case NORTH -> {
                quad(m, vc, INW,  INW2, GOUT2, u,  v2, INW2, INW2, GOUT2, u2, v2, INW2, INW, GOUT2, u2, v,  INW,  INW, GOUT2, u,  v,  GLASS_TINT, light, overlay, 0, 0, -1);
            }
            case EAST -> {
                quad(m, vc, GOUT, INW2, INW,  u,  v2, GOUT, INW2, INW2, u2, v2, GOUT, INW, INW2, u2, v,  GOUT, INW, INW,  u,  v,  GLASS_TINT, light, overlay, 1, 0, 0);
            }
            case WEST -> {
                quad(m, vc, GOUT2, INW, INW,  u,  v,  GOUT2, INW, INW2, u2, v,  GOUT2, INW2, INW2, u2, v2, GOUT2, INW2, INW,  u,  v2, GLASS_TINT, light, overlay, -1, 0, 0);
            }
        }
    }

    /**
     * Fluid cap drawn at the pipe's inner edge facing {@code dir} when there's NO neighbour
     * pipe / connector on that side — i.e. a sealed end. Mirrors the legacy switch over
     * {@code dir} in {@code renderLiquid}.
     */
    private static void emitCap(Matrix4f m, VertexConsumer vc, Direction dir,
                                 float u, float v, float u2, float v2, int tint, int light, int overlay) {
        switch (dir) {
            case UP -> {
                quad(m, vc, IN2, IN, IN,  u,  v2, IN,  IN, IN,  u2, v2, IN,  IN, IN2, u2, v,  IN2, IN, IN2, u,  v,  tint, light, overlay, 0, 1, 0);
            }
            case DOWN -> {
                quad(m, vc, IN2, IN2, IN2, u,  v,  IN,  IN2, IN2, u2, v,  IN,  IN2, IN,  u2, v2, IN2, IN2, IN,  u,  v2, tint, light, overlay, 0, -1, 0);
            }
            case SOUTH -> {
                quad(m, vc, IN, IN, IN,  u,  v,  IN2, IN, IN,  u2, v,  IN2, IN2, IN,  u2, v2, IN, IN2, IN,  u,  v2, tint, light, overlay, 0, 0, 1);
            }
            case NORTH -> {
                quad(m, vc, IN,  IN2, IN2, u,  v2, IN2, IN2, IN2, u2, v2, IN2, IN, IN2, u2, v,  IN,  IN, IN2, u,  v,  tint, light, overlay, 0, 0, -1);
            }
            case EAST -> {
                quad(m, vc, IN, IN2, IN,  u,  v2, IN, IN2, IN2, u2, v2, IN, IN, IN2, u2, v,  IN, IN, IN,  u,  v,  tint, light, overlay, 1, 0, 0);
            }
            case WEST -> {
                quad(m, vc, IN2, IN, IN,  u,  v,  IN2, IN, IN2, u2, v,  IN2, IN2, IN2, u2, v2, IN2, IN2, IN,  u,  v2, tint, light, overlay, -1, 0, 0);
            }
        }
    }

    /**
     * Four "tube wall" quads extending from the pipe core to the block face, drawn when the
     * pipe IS connected on {@code dir}. Visually represents fluid flowing through the arm
     * into the neighbour pipe.
     */
    private static void emitConnectedFluid(Matrix4f m, VertexConsumer vc, Direction dir,
                                            float u, float v, float u2, float v2, float du,
                                            int tint, int light, int overlay) {
        switch (dir) {
            case DOWN -> {
                quad(m, vc, IN2, IN2, IN,  u,  v,    IN2, IN2, IN2, u2, v,    IN2, 0,  IN2, u2, v + du, IN2, 0,  IN,  u,  v + du, tint, light, overlay, -1, 0, 0);
                quad(m, vc, IN,  0,   IN,  u,  v + du, IN,  0,   IN2, u2, v + du, IN,  IN2, IN2, u2, v,    IN,  IN2, IN,  u,  v,    tint, light, overlay, 1, 0, 0);
                quad(m, vc, IN,  0,   IN2, u,  v + du, IN2, 0,   IN2, u2, v + du, IN2, IN2, IN2, u2, v,    IN,  IN2, IN2, u,  v,    tint, light, overlay, 0, 0, -1);
                quad(m, vc, IN,  IN2, IN,  u,  v,    IN2, IN2, IN,  u2, v,    IN2, 0,   IN,  u2, v + du, IN,  0,   IN,  u,  v + du, tint, light, overlay, 0, 0, 1);
            }
            case UP -> {
                quad(m, vc, IN2, 1,  IN,  u,  v + du, IN2, 1,  IN2, u2, v + du, IN2, IN, IN2, u2, v,    IN2, IN, IN,  u,  v,    tint, light, overlay, -1, 0, 0);
                quad(m, vc, IN,  IN, IN,  u,  v,    IN,  IN, IN2, u2, v,    IN,  1,  IN2, u2, v + du, IN,  1,  IN,  u,  v + du, tint, light, overlay, 1, 0, 0);
                quad(m, vc, IN,  IN, IN2, u,  v,    IN2, IN, IN2, u2, v,    IN2, 1,  IN2, u2, v + du, IN,  1,  IN2, u,  v + du, tint, light, overlay, 0, 0, -1);
                quad(m, vc, IN,  1,  IN,  u,  v + du, IN2, 1,  IN,  u2, v + du, IN2, IN, IN,  u2, v,    IN,  IN, IN,  u,  v,    tint, light, overlay, 0, 0, 1);
            }
            case NORTH -> {
                quad(m, vc, IN2, IN2, 0,   u, v2,    IN2, IN2, IN2, u + du, v2,    IN2, IN, IN2, u + du, v,    IN2, IN, 0,   u, v,    tint, light, overlay, -1, 0, 0);
                quad(m, vc, IN,  IN, 0,   u, v,    IN,  IN, IN2, u + du, v,    IN,  IN2, IN2, u + du, v2,    IN,  IN2, 0,   u, v2,    tint, light, overlay, 1, 0, 0);
                quad(m, vc, IN2, IN, 0,   u, v2,    IN2, IN, IN2, u + du, v2,    IN,  IN, IN2, u + du, v,    IN,  IN, 0,   u, v,    tint, light, overlay, 0, 1, 0);
                quad(m, vc, IN,  IN2, 0,   u, v,    IN,  IN2, IN2, u + du, v,    IN2, IN2, IN2, u + du, v2,    IN2, IN2, 0,   u, v2,    tint, light, overlay, 0, -1, 0);
            }
            case SOUTH -> {
                quad(m, vc, IN2, IN, 1,   u, v,    IN2, IN, IN,  u + du, v,    IN2, IN2, IN,  u + du, v2,    IN2, IN2, 1,   u, v2,    tint, light, overlay, -1, 0, 0);
                quad(m, vc, IN,  IN2, 1,   u, v2,    IN,  IN2, IN,  u + du, v2,    IN,  IN, IN,  u + du, v,    IN,  IN, 1,   u, v,    tint, light, overlay, 1, 0, 0);
                quad(m, vc, IN,  IN, 1,   u, v,    IN,  IN, IN,  u + du, v,    IN2, IN, IN,  u + du, v2,    IN2, IN, 1,   u, v2,    tint, light, overlay, 0, 1, 0);
                quad(m, vc, IN2, IN2, 1,   u, v2,    IN2, IN2, IN,  u + du, v2,    IN,  IN2, IN,  u + du, v,    IN,  IN2, 1,   u, v,    tint, light, overlay, 0, -1, 0);
            }
            case EAST -> {
                quad(m, vc, 1,   IN, IN,  u, v,    IN,  IN, IN,  u + du, v,    IN,  IN2, IN,  u + du, v2,    1,   IN2, IN,  u, v2,    tint, light, overlay, 0, 0, 1);
                quad(m, vc, 1,   IN2, IN2, u, v2,    IN,  IN2, IN2, u + du, v2,    IN,  IN, IN2, u + du, v,    1,   IN, IN2, u, v,    tint, light, overlay, 0, 0, -1);
                quad(m, vc, 1,   IN, IN2, u, v2,    IN,  IN, IN2, u + du, v2,    IN,  IN, IN,  u + du, v,    1,   IN, IN,  u, v,    tint, light, overlay, 0, 1, 0);
                quad(m, vc, 1,   IN2, IN,  u, v,    IN,  IN2, IN,  u + du, v,    IN,  IN2, IN2, u + du, v2,    1,   IN2, IN2, u, v2,    tint, light, overlay, 0, -1, 0);
            }
            case WEST -> {
                quad(m, vc, 0,   IN2, IN,  u, v2,    IN2, IN2, IN,  u + du, v2,    IN2, IN, IN,  u + du, v,    0,   IN, IN,  u, v,    tint, light, overlay, 0, 0, 1);
                quad(m, vc, 0,   IN, IN2, u, v,    IN2, IN, IN2, u + du, v,    IN2, IN2, IN2, u + du, v2,    0,   IN2, IN2, u, v2,    tint, light, overlay, 0, 0, -1);
                quad(m, vc, 0,   IN, IN,  u, v,    IN2, IN, IN,  u + du, v,    IN2, IN, IN2, u + du, v2,    0,   IN, IN2, u, v2,    tint, light, overlay, 0, 1, 0);
                quad(m, vc, 0,   IN2, IN2, u, v2,    IN2, IN2, IN2, u + du, v2,    IN2, IN2, IN,  u + du, v,    0,   IN2, IN,  u, v,    tint, light, overlay, 0, -1, 0);
            }
        }
    }

    /** Emit one textured quad (4 vertices) with the same colour and shared light/overlay/normal. */
    private static void quad(Matrix4f pose, VertexConsumer vc,
                             double x1, double y1, double z1, float u1, float v1,
                             double x2, double y2, double z2, float u2_, float v2_,
                             double x3, double y3, double z3, float u3, float v3,
                             double x4, double y4, double z4, float u4, float v4,
                             int rgba, int light, int overlay,
                             float nx, float ny, float nz) {
        vc.addVertex(pose, (float) x1, (float) y1, (float) z1).setColor(rgba).setUv(u1, v1).setOverlay(overlay).setLight(light).setNormal(nx, ny, nz);
        vc.addVertex(pose, (float) x2, (float) y2, (float) z2).setColor(rgba).setUv(u2_, v2_).setOverlay(overlay).setLight(light).setNormal(nx, ny, nz);
        vc.addVertex(pose, (float) x3, (float) y3, (float) z3).setColor(rgba).setUv(u3, v3).setOverlay(overlay).setLight(light).setNormal(nx, ny, nz);
        vc.addVertex(pose, (float) x4, (float) y4, (float) z4).setColor(rgba).setUv(u4, v4).setOverlay(overlay).setLight(light).setNormal(nx, ny, nz);
    }

    /**
     * Resolves the still-texture sprite for a fluid on the block atlas. Vanilla water/lava
     * have well-known atlas locations; for RotaryCraft fluids we look up the standard "rc"
     * texture paths. Unknown fluids return the missing-texture sprite, which is fine — the
     * tint kicks in regardless and the user still sees a clearly-coloured surface.
     */
    private TextureAtlasSprite stillSpriteFor(Fluid fluid) {
        Identifier id = stillTextureId(fluid);
        return sprites.get(new SpriteId(TextureAtlas.LOCATION_BLOCKS, id));
    }

    /** The legacy {@code Blocks.glass.getIcon(0,0)} window-pane texture. */
    private TextureAtlasSprite glassSprite() {
        return sprites.get(new SpriteId(TextureAtlas.LOCATION_BLOCKS, Identifier.withDefaultNamespace("block/glass")));
    }

    private static Identifier stillTextureId(Fluid fluid) {
        if (fluid == Fluids.WATER || fluid == Fluids.FLOWING_WATER)
            return Identifier.withDefaultNamespace("block/water_still");
        if (fluid == Fluids.LAVA || fluid == Fluids.FLOWING_LAVA)
            return Identifier.withDefaultNamespace("block/lava_still");
        if (fluid == RotaryFluids.JET_FUEL.get())
            return Identifier.fromNamespaceAndPath("rotarycraft", "block/fluid/jetfuel");
        if (fluid == RotaryFluids.ETHANOL.get())
            return Identifier.fromNamespaceAndPath("rotarycraft", "block/fluid/ethanol");
        if (fluid == RotaryFluids.LUBRICANT.get())
            return Identifier.fromNamespaceAndPath("rotarycraft", "block/fluid/lubricant");
        if (fluid == RotaryFluids.HSLA_FLUID.get())
            return Identifier.fromNamespaceAndPath("rotarycraft", "block/fluid/hsla_still");
        // Generic fallback: use lava_still for hot fluids, water_still for cold.
        return fluid.getFluidType().getTemperature() > 500
                ? Identifier.withDefaultNamespace("block/lava_still")
                : Identifier.withDefaultNamespace("block/water_still");
    }

    /**
     * Approximate ARGB tint per fluid (full alpha; the translucent RenderType handles blend).
     * Matches the {@code RenderReservoir.fluidTint} palette so the visual identity of each
     * fluid stays consistent across all three (pipe, reservoir, GUI tooltips).
     */
    private static int fluidTint(Fluid f) {
        if (f == Fluids.WATER || f == Fluids.FLOWING_WATER) return 0xFF3050E0;
        if (f == Fluids.LAVA  || f == Fluids.FLOWING_LAVA)  return 0xFFE04010;
        if (f == RotaryFluids.JET_FUEL.get())               return 0xFF60A000;
        if (f == RotaryFluids.ETHANOL.get())                return 0xFFF0F050;
        if (f == RotaryFluids.LUBRICANT.get())              return 0xFFC08020;
        if (f == RotaryFluids.HSLA_FLUID.get())             return 0xFFE04010;
        if (f == RotaryFluids.LIQUID_NITROGEN.get())        return 0xFFA0E0F0;
        if (f == RotaryFluids.POISON.get())                 return 0xFFA030F0;
        if (f == RotaryFluids.STEAM.get())                  return 0xC0E0E0E0;
        if (f == RotaryFluids.SODIUM.get())                 return 0xFFD0D050;
        if (f == RotaryFluids.CHLORINE.get())               return 0xFFC0E060;
        if (f == RotaryFluids.OXYGEN.get())                 return 0xFFE0F0FF;
        return 0xFF20A0C0;
    }
}
