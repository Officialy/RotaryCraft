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

import com.mojang.blaze3d.pipeline.BindGroupLayout;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import reika.rotarycraft.RotaryCraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.LayeringTransform;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;

/**
 * 26.1: registers custom {@link RenderPipeline}s used by RotaryCraft renderers.
 *
 * <p>Currently exposes {@link #NO_DEPTH_FILLED_BOX}, a clone of vanilla
 * {@code DEBUG_FILLED_BOX} with the depth test relaxed to {@link CompareOp#ALWAYS}.
 * The IO renderer uses it so the input/output overlay cubes stay visible even when the
 * neighbour they sit on top of is occluded by the machine block itself — that's why the
 * user couldn't see N/E IO faces from typical camera angles before this was added.</p>
 */
public final class RotaryRenderPipelines {

    private RotaryRenderPipelines() {}

    /**
     * Same builder as vanilla {@code DEBUG_FILLED_BOX} (translucent blend, position+color
     * quads pipeline, depth WRITE disabled), but with depth TEST set to {@link CompareOp#ALWAYS}
     * so the cube draws regardless of what's already in the depth buffer.
     */
    public static final RenderPipeline NO_DEPTH_FILLED_BOX = RenderPipeline.builder()
            .withLocation("pipeline/rotarycraft_no_depth_filled_box")
            .withBindGroupLayout(BindGroupLayouts.GLOBALS)
            .withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
            .withVertexShader("core/position_color")
            .withFragmentShader("core/position_color")
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, false))
            .withCull(false)
            .build();

    /** RenderType wrapping {@link #NO_DEPTH_FILLED_BOX}; matches the {@code debugFilledBox} setup. */
    public static final RenderType NO_DEPTH_FILLED_BOX_TYPE = RenderType.create(
            "rotarycraft_no_depth_filled_box",
            RenderSetup.builder(NO_DEPTH_FILLED_BOX)
                    .sortOnUpload()
                    .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
                    .createRenderSetup()
    );

    /**
     * Companion no-depth-test pipeline for line geometry. Vanilla's {@code RenderTypes.lines}
     * uses {@code LESS_THAN_OR_EQUAL} depth state, so the IO renderer's wireframe edges were
     * still being occluded by the host block even after we switched the fill body to the
     * no-depth filled box. Mirrors vanilla {@code LINES_SNIPPET} (POSITION_COLOR_NORMAL_LINE_WIDTH
     * vertex format, translucent blend, cull off) with depth test {@code ALWAYS_PASS}.
     */
    public static final RenderPipeline NO_DEPTH_LINES = RenderPipeline.builder()
            .withLocation("pipeline/rotarycraft_no_depth_lines")
            .withBindGroupLayout(BindGroupLayouts.GLOBALS)
            .withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
            .withBindGroupLayout(BindGroupLayouts.FOG)
            .withVertexShader("core/rendertype_lines")
            .withFragmentShader("core/rendertype_lines")
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withCull(false)
            .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR_NORMAL_LINE_WIDTH)
            .withPrimitiveTopology(PrimitiveTopology.LINES)
            .withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, false))
            .build();

    public static final RenderType NO_DEPTH_LINES_TYPE = RenderType.create(
            "rotarycraft_no_depth_lines",
            RenderSetup.builder(NO_DEPTH_LINES).createRenderSetup()
    );

    /**
     * The heat-haze pass, driven by {@link reika.rotarycraft.auxiliary.HeatRippleRenderer}: reads the
     * scene and writes it back displaced around each hot machine.
     *
     * <p>Built the way {@code PostChain} builds its own passes (on {@code POST_PROCESSING_SNIPPET},
     * with vanilla's {@code core/screenquad} vertex shader, which generates the fullscreen triangle
     * from {@code gl_VertexID} and so needs no vertex buffer). It is driven by hand rather than
     * declared in the chain JSON because a PostChain compiles its uniforms into an immutable buffer,
     * and the emitters move every frame; running the pass ourselves lets it take a live UBO.</p>
     *
     * <p>{@code POST_PROCESSING_SNIPPET} builds on {@code GLOBALS_SNIPPET}, so the shader also gets
     * the {@code Globals} block (it reads {@code ScreenSize} for the aspect correction).</p>
     */
    public static final RenderPipeline HEAT_RIPPLE = RenderPipeline.builder(RenderPipelines.POST_PROCESSING_SNIPPET)
            .withLocation(Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "pipeline/heatripple"))
            // The String overloads take a bare path and assume the minecraft namespace, so ours has
            // to be an explicit Identifier or it ends up as "minecraft:rotarycraft:post/...".
            .withVertexShader("core/screenquad")
            .withFragmentShader(Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "post/heatripple"))
            .withBindGroupLayout(BindGroupLayout.builder()
                    .withSampler("InSampler")
                    .withUniform("HeatPoints", UniformType.UNIFORM_BUFFER)
                    .build())
            .build();

    /** Subscribed on the mod event bus by {@code RotaryCraft#RotaryCraft(IEventBus, ...)}. */
    public static void register(IEventBus modBus) {
        modBus.addListener(RotaryRenderPipelines::onRegisterPipelines);
    }

    private static void onRegisterPipelines(RegisterRenderPipelinesEvent event) {
        event.registerPipeline(NO_DEPTH_FILLED_BOX);
        event.registerPipeline(NO_DEPTH_LINES);
        event.registerPipeline(HEAT_RIPPLE);
    }

    /** Touching this class loads the static initializers (RT registration); call from client setup. */
    public static void init() { /* class-load trigger */ }
}
