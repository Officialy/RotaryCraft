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

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.blaze3d.framegraph.FramePass;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import com.mojang.blaze3d.resource.RenderTargetDescriptor;
import com.mojang.blaze3d.resource.ResourceHandle;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MappableRingBuffer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import org.joml.Matrix4fc;
import org.joml.Vector4f;
import org.jspecify.annotations.Nullable;

import reika.dragonapi.instantiable.RayTracer;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.renders.RotaryRenderPipelines;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The heat haze over hot machines: engine exhaust, the Friction Heater, the Heater. Renderers post an
 * emitter each frame while their machine is hot, and this warps the screen around them.
 *
 * <h2>How it reaches the GPU</h2>
 * <p>1.7.10 ran the haze shader once per emitter, ping-ponging framebuffers, passing that emitter's
 * values in as uniforms. A modern PostChain compiles its uniforms into an immutable buffer when the
 * chain loads, so a declarative chain cannot carry values that change every frame. Instead the pass
 * is driven by hand ({@link #drawWarp}) with a live UBO holding every emitter, and resolves them all
 * in one fullscreen draw.</p>
 *
 * <p>The pass cannot read and write the main target at once, so it renders into an offscreen target
 * and the {@code rotarycraft:heatripple} chain blits that back over the scene.</p>
 */
@EventBusSubscriber(modid = RotaryCraft.MODID, value = Dist.CLIENT)
public final class HeatRippleRenderer {

    public static final HeatRippleRenderer instance = new HeatRippleRenderer();

    /** The chain: assets/rotarycraft/post_effect/heatripple.json. */
    private static final Identifier EFFECT_ID = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "heatripple");

    /** The warped scene, as named by the chain JSON's input and supplied via a TargetBundle. */
    private static final Identifier WARPED_TARGET_ID = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "warped");

    /** Targets the chain is permitted to reference; enforced by {@code PostChain.load}. */
    private static final Set<Identifier> ALLOWED_TARGETS = Set.of(PostChain.MAIN_TARGET_ID, WARPED_TARGET_ID);

    /** Must match {@code MAX_HEAT_POINTS} in heatripple.fsh. */
    private static final int MAX_HEAT_POINTS = 32;

    /** std140: ivec4 HeatCount, vec4 HeatTime, then vec4 Focus[N] and vec4 Params[N]. */
    private static final int UBO_SIZE = 16 + 16 + MAX_HEAT_POINTS * 16 * 2;

    /** MAP_WRITE | UNIFORM. */
    private static final int UBO_USAGE = GpuBuffer.USAGE_MAP_WRITE | GpuBuffer.USAGE_UNIFORM;

    private final RayTracer.RayTracerWithCache<?> LOS = RayTracer.getVisualLOSForRenderCulling();

    private static final List<HeatPoint> POINTS = new ArrayList<>();

    private static @Nullable MappableRingBuffer heatUbo;

    private HeatRippleRenderer() {}

    /** One emitter for this frame; mirrors the 1.7.10 per-focus uniform set. */
    private record HeatPoint(Vec3 position, double distSq, float intensity, float factor, float scale, float fade) {}

    /**
     * Post an emitter only if the viewer can actually see it, so machines behind walls do not haze
     * the screen. Distance is measured from the viewer, as in 1.7.10.
     */
    public boolean addHeatRippleEffectIfLOS(BlockEntity tile, double x, double y, double z,
                                            float f, float fac, float scale, float centerFade) {
        Player ep = Minecraft.getInstance().player;
        if (ep == null)
            return false;
        return this.addHeatRippleEffectIfLOS(tile, x, y, z, ep, ep.distanceToSqr(x, y, z), f, fac, scale, centerFade);
    }

    public boolean addHeatRippleEffectIfLOS(BlockEntity tile, double x, double y, double z, Player ep,
                                            double distSq, float f, float fac, float scale, float centerFade) {
        LOS.setOrigins(x, y, z, ep.getX(), ep.getY(), ep.getZ());
        if (!LOS.isClearLineOfSight(tile))
            return false;
        this.addHeatRippleEffect(x, y, z, distSq, f, fac, scale, centerFade);
        return true;
    }

    /**
     * @param distSq      squared distance from the viewer; the shader's falloff is written against
     *                    the square, so a nearer machine hazes a wider patch of screen
     * @param f           emitter strength, normally how hot the machine is
     * @param fac         how violently it churns
     * @param scale       how far the haze spreads
     * @param centerFade  hollows out the middle, so exhaust does not smear the machine itself
     */
    public void addHeatRippleEffect(double x, double y, double z, double distSq,
                                    float f, float fac, float scale, float centerFade) {
        // A zero factor or scale divides by zero in the falloff, and a zero strength shows nothing.
        if (f <= 0 || fac <= 0 || scale <= 0)
            return;
        if (POINTS.size() >= MAX_HEAT_POINTS)
            return;
        POINTS.add(new HeatPoint(new Vec3(x, y, z), distSq, f, fac, scale, centerFade));
    }

    /** Runs once the level is fully drawn, so the haze distorts the finished scene. */
    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent.AfterLevel event) {
        if (POINTS.isEmpty())
            return;
        try {
            CameraRenderState camera = event.getLevelRenderState().cameraRenderState;
            render(event.getModelViewMatrix(), camera.projectionMatrix, camera.pos,
                    event.getLevelRenderState().gameTime);
        }
        finally {
            // Emitters are posted per frame by the renderers, so the set always starts empty.
            POINTS.clear();
        }
    }

    private static void render(Matrix4fc modelView, Matrix4fc projection, Vec3 cameraPos, long gameTime) {
        Minecraft mc = Minecraft.getInstance();
        PostChain chain = mc.getShaderManager().getPostChain(EFFECT_ID, ALLOWED_TARGETS);
        if (chain == null)
            return;

        RenderTarget main = mc.gameRenderer.mainRenderTarget();
        int width = main.width;
        int height = main.height;

        List<float[]> projected = project(modelView, projection, cameraPos, width, height);
        if (projected.isEmpty())
            return;

        uploadHeatPoints(projected, gameTime);

        // The warp pass reads the scene and writes it displaced into an offscreen target; the chain
        // then blits that back over main. Reading and writing main in one pass is not allowed.
        FrameGraphBuilder frame = new FrameGraphBuilder();
        ResourceHandle<RenderTarget> mainHandle = frame.importExternal("main", main);
        ResourceHandle<RenderTarget> warpedHandle = frame.createInternal(
                "rotarycraft_warped",
                new RenderTargetDescriptor(width, height, false, new Vector4f(0, 0, 0, 0), GpuFormat.RGBA8_UNORM));

        FramePass warpPass = frame.addPass("rotarycraft_heatripple_warp");
        warpPass.reads(mainHandle);
        ResourceHandle<RenderTarget> warpedOut = warpPass.readsAndWrites(warpedHandle);
        warpPass.executes(() -> drawWarp(mainHandle.get(), warpedOut.get()));

        chain.addToFrame(frame, width, height, new WarpTargetBundle(mainHandle, warpedOut));
        frame.execute(GraphicsResourceAllocator.UNPOOLED);

        if (heatUbo != null)
            heatUbo.rotate();
    }

    /**
     * Project each emitter to screen space. The 1.7.10 shader did this itself (lib_geometry's
     * {@code getScreenPos}) because the emitter arrived as uniforms; here the CPU owns them.
     *
     * @return one {@code {u, v, distSq, intensity, factor, scale, fade}} per visible emitter
     */
    private static List<float[]> project(Matrix4fc modelView, Matrix4fc projection, Vec3 cameraPos, int width, int height) {
        List<float[]> out = new ArrayList<>(POINTS.size());
        for (HeatPoint p : POINTS) {
            Vector4f v = new Vector4f(
                    (float) (p.position().x - cameraPos.x),
                    (float) (p.position().y - cameraPos.y),
                    (float) (p.position().z - cameraPos.z),
                    1);
            v.mul(modelView);
            v.mul(projection);
            if (v.w <= 1.0E-4F) // behind the near plane; no meaningful screen position
                continue;

            // NDC -> the UV convention of core/screenquad's texCoord (origin bottom-left), so no flip.
            float u = (v.x / v.w) * 0.5F + 0.5F;
            float vv = (v.y / v.w) * 0.5F + 0.5F;

            out.add(new float[]{u, vv, (float) Math.max(0.01, p.distSq()), p.intensity(),
                    p.factor(), p.scale(), p.fade()});
        }
        return out;
    }

    private static void uploadHeatPoints(List<float[]> projected, long gameTime) {
        if (heatUbo == null)
            heatUbo = new MappableRingBuffer(() -> "RotaryCraft HeatPoints", UBO_USAGE, UBO_SIZE);

        try (GpuBufferSlice.MappedView view = heatUbo.currentBuffer().map(false, true)) {
            Std140Builder builder = Std140Builder.intoBuffer(view.data());
            builder.putIVec4(projected.size(), 0, 0, 0);
            // Wrapped to keep the ripple's phase in float range over a long-lived world.
            builder.putVec4(gameTime % 24000L, 0, 0, 0);
            for (float[] p : projected)
                builder.putVec4(p[0], p[1], p[2], p[3]);
            for (int i = projected.size(); i < MAX_HEAT_POINTS; i++)
                builder.putVec4(0, 0, 0, 0); // pad: Params starts at a fixed offset
            for (float[] p : projected)
                builder.putVec4(p[4], p[5], p[6], 0);
        }
    }

    private static void drawWarp(RenderTarget source, RenderTarget target) {
        CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
        try (RenderPass pass = encoder.createRenderPass(
                () -> "RotaryCraft heat ripple",
                target.getColorTextureView(),
                Optional.empty())) {
            pass.setPipeline(RotaryRenderPipelines.HEAT_RIPPLE);
            RenderSystem.bindDefaultUniforms(pass); // Globals, for ScreenSize
            pass.setUniform("HeatPoints", heatUbo.currentBuffer());
            // Clamped + linear: the displaced UV lands between texels and can reach off-screen.
            pass.bindTexture("InSampler", source.getColorTextureView(),
                    RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
            pass.draw(3, 1, 0, 0); // core/screenquad builds the fullscreen triangle from gl_VertexID
        }
    }

    /** Supplies the chain with the main target plus our warped scene. */
    private record WarpTargetBundle(ResourceHandle<RenderTarget> main,
                                    ResourceHandle<RenderTarget> warped) implements PostChain.TargetBundle {

        @Override
        public void replace(Identifier id, ResourceHandle<RenderTarget> handle) {
            // The chain writes back the handles it produced. The blit lands in the real main target
            // and the graph is executed immediately, so there is nothing to carry forward.
        }

        @Override
        public @Nullable ResourceHandle<RenderTarget> get(Identifier id) {
            if (id.equals(PostChain.MAIN_TARGET_ID))
                return main;
            return id.equals(WARPED_TARGET_ID) ? warped : null;
        }
    }
}
