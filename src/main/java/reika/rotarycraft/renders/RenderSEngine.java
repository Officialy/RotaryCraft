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

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import reika.dragonapi.libraries.mathsci.ReikaPhysicsHelper;
import reika.dragonapi.libraries.rendering.ReikaColorAPI;
import reika.rotarycraft.registry.EngineType;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.auxiliary.interfaces.AlternatingRedstoneUser;
import reika.rotarycraft.auxiliary.interfaces.RedstoneUpgradeable;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blockentity.BlockEntityEngine;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.engine.BlockEntityJetEngine;
import reika.rotarycraft.models.engine.*;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryModelLayers;

//@SideOnly(Dist.CLIENT)
public class RenderSEngine extends RotaryTERenderer<BlockEntityEngine> {

    private static final Identifier JET_GLOW_MASK = Identifier.fromNamespaceAndPath(
            RotaryCraft.MODID, "textures/blockentitytex/engine/jettex_glow_mask2.png");

    private final DCModel dcModel;
    private final SteamModel steamModel;
    private final CombustionModel combModel;
    private final ACModel acModel;
    private final PerformanceModel perfModel;
    private final MicroTurbineModel microModel;
    private final JetModel jetModel;
    private final HydroModel hydroModel;
    private final WindModel windModel;

//    private static final Glow jetGlow = new Glow(255, 150, 20, 192).setScale(0.4);

    public RenderSEngine(BlockEntityRendererProvider.Context context) {
        dcModel = new DCModel(context.bakeLayer(RotaryModelLayers.DC_ENGINE));
        steamModel = new SteamModel(context.bakeLayer(RotaryModelLayers.STEAM_ENGINE));
        combModel = new CombustionModel(context.bakeLayer(RotaryModelLayers.COMBUSTION_ENGINE));
        acModel = new ACModel(context.bakeLayer(RotaryModelLayers.AC_ENGINE));
        perfModel = new PerformanceModel(context.bakeLayer(RotaryModelLayers.PERFORMANCE_ENGINE));
        microModel = new MicroTurbineModel(context.bakeLayer(RotaryModelLayers.MICRO_ENGINE));
        jetModel = new JetModel(context.bakeLayer(RotaryModelLayers.JET_ENGINE));
        hydroModel = new HydroModel(context.bakeLayer(RotaryModelLayers.HYDRO_ENGINE));
        windModel = new WindModel(context.bakeLayer(RotaryModelLayers.WIND_ENGINE));
    }

    /**
     * Renders the BlockEntity for the position.
     */
    public void renderBlockEntityEngineAt(PoseStack stack, BlockEntityEngine tile, MultiBufferSource bufferSource, int pPackedLight) {
        stack.pushPose();

//        this.setupGL(stack, tile, par2, par4, par6);
        if (tile.isInWorld()) {

            float f = tile.getBlockState().getValue(BlockRotaryCraftMachine.FACING).toYRot();
            if (tile.getEngineType().isJetFueled()) {
                f += 90;
            }
            stack.translate(0.5F, 1.5F, 0.5F);
            stack.mulPose(Axis.YP.rotationDegrees(-f - 90));
            stack.mulPose(Axis.ZP.rotationDegrees(180));
//todo            this.prepareShader(tile);
        }

        switch (tile.getEngineType()) {
            case DC -> {
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderTypes.entitySolid((DCModel.TEXTURE_LOCATION)));
                dcModel.renderAll(stack, vertexconsumer, pPackedLight, tile, null, -tile.phi);
            }
            case WIND -> {
//                stack.mulPose(new Quaternion(90, 0.0F, 1.0F, 0.0F));
                var s = 0.7;
                var d = 0.375;
//                stack.scale((float) s, (float) s, (float) s);
                double d2 = 0.2;
//                stack.translate(0, d, 0);
//                stack.translate(d2, 0, 0);
//                stack.translate(0, -d, 0);
//                stack.translate(-d2, 0, 0);
//                stack.scale((float) (1D / s), (float) (1D / s), (float) (1D / s));
                stack.mulPose(Axis.YP.rotationDegrees(90));
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderTypes.entitySolid((WindModel.TEXTURE_LOCATION)));
                windModel.renderAll(stack, vertexconsumer, pPackedLight, tile, null, -tile.phi, 0);
            }
            case STEAM -> {
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderTypes.entitySolid((SteamModel.TEXTURE_LOCATION)));
//                RotaryCraft.LOGGER.info(tile.phi);
                steamModel.renderAll(stack, vertexconsumer, pPackedLight, tile, null, -tile.phi);
            }
            case GAS -> {
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderTypes.entitySolid((CombustionModel.TEXTURE_LOCATION)));
                combModel.renderAll(stack, vertexconsumer, pPackedLight, tile, null, -tile.phi, 0);
            }
            case AC -> {
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderTypes.entitySolid((ACModel.TEXTURE_LOCATION)));
                acModel.renderAll(stack, vertexconsumer, pPackedLight, tile, null, -tile.phi, 0);
            }
            case SPORT -> {
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderTypes.entitySolid((PerformanceModel.TEXTURE_LOCATION)));
                perfModel.renderAll(stack, vertexconsumer, pPackedLight, tile, null, -tile.phi, 0);
            }
//            case HYDRO:
//                BlockEntityHydroEngine eng = (BlockEntityHydroEngine) tile;
//                var21.renderAll(stack, tile, ReikaJavaLibrary.makeListFrom(eng.failed, eng.isBedrock()), eng.isReversed() ? tile.phi : -tile.phi, 0);
//                break;
            case MICRO -> {
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderTypes.entitySolid((MicroTurbineModel.TEXTURE_LOCATION)));
                microModel.renderAll(stack, vertexconsumer, pPackedLight, tile, null, -tile.phi, 0);
                stack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(90));
            }
            case JET -> {
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderTypes.entitySolid(textureWithSuffix(JetModel.TEXTURE_LOCATION, "jettex.png")));
                jetModel.renderAll(stack, vertexconsumer, pPackedLight, tile, null, -tile.phi, 0);
            }
//            case JET -> {
//                float f = 1;
//				/*
//				ShaderProgram sh = ClientProxy.getHeatGlowShader();
//				sh.setEnabled(f > 0);
//				sh.setIntensity(f);
//				sh.setField("glowRed", ReikaColorAPI.getRed(c));
//				sh.setField("glowGreen", ReikaColorAPI.getGreen(c));
//				sh.setField("glowBlue", ReikaColorAPI.getBlue(c));
//				ShaderRegistry.runShader(sh);
//				 */
//                var20.renderAll(stack, tile, null, -tile.phi);
//                //ShaderRegistry.completeShader();
//                if (f > 0) {
//                    int temp = Math.max(tile.temperature - 600, (tile.temperature - 1000) * 5 / 2);
//                    int c = ReikaPhysicsHelper.getColorForTemperature(temp);
//                    stack.pushPose();
//                    GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
//                    double s = 1.005;
//                    ReikaRenderHelper.disableLighting();
//                    ReikaRenderHelper.disableEntityLighting();
//                    GL11.glColor4f(ReikaColorAPI.getRed(c) / 255F, ReikaColorAPI.getGreen(c) / 255F, ReikaColorAPI.getBlue(c) / 255F, 1);
//                    BlendMode.ADDITIVEDARK.apply();
//                    this.bindTextureByName("/reika/rotarycraft/textures/blockentitytex/Engine/jettex_glow_mask2.png");
//
//                    stack.pushPose();
//                    stack.translate(0, 1 - s, (1 - s) / 4);
//                    stack.scale(s, s, s);
//                    var20.renderAll(stack, tile, null, -tile.phi);
//                    stack.popPose();
//                    stack.pushPose();
//                    stack.translate(0, s - 1, -(1 - s) / 4);
//                    stack.scale(1 / s, 1 / s, 1 / s);
//                    var20.renderAll(stack, tile, null, -tile.phi);
//                    stack.popPose();
//
//                    GL11.glDisable(GL11.GL_TEXTURE_2D);
//                    Tesselator.instance.startDrawing(GL11.GL_TRIANGLE_FAN);
//                    Tesselator.instance.setColorOpaque_I(ReikaColorAPI.getColorWithBrightnessMultiplier(c, 0.65F));
//                    Tesselator.instance.addTranslation(-0.5F, 0.5625F, -0.45F);
//                    Tesselator.instance.vertex(0.5, 0.5, 0);
//                    Tesselator.instance.vertex(0.3, 0.8, 0);
//                    Tesselator.instance.vertex(0.7, 0.8, 0);
//                    Tesselator.instance.vertex(0.8, 0.5, 0);
//                    Tesselator.instance.vertex(0.7, 0.2, 0);
//                    Tesselator.instance.vertex(0.3, 0.2, 0);
//                    Tesselator.instance.vertex(0.2, 0.5, 0);
//                    Tesselator.instance.vertex(0.3, 0.8, 0);
//                    Tesselator.instance.addTranslation(0.5F, -0.5625F, 0.45F);
//                    Tesselator.instance.end();
//
//                    //GL11.glPopAttrib();
//                    stack.popPose();
//                }
//                }
        }

//        this.closeGL(stack, tile);
        stack.popPose();
    }

    private void prepareShader(BlockEntityEngine tile, PoseStack stack) {
       /* todo if (tile.getEngineType() == EngineType.JET) {
            BlockEntityJetEngine te = (BlockEntityJetEngine) tile;
            double dx = 0.625 * tile.getWriteDirection().getStepX();
            double dz = 0.625 * tile.getWriteDirection().getStepZ();
            Player ep = Minecraft.getInstance().player;
            stack.pushPose();
            stack.translate(0, 1, -0.625);
            double dist = ep.distanceToSqr(tile.getBlockPos().getX() + 0.5 + dx, tile.getBlockPos().getY() + 0.5, tile.getBlockPos().getZ() + 0.5 + dz);
            float f = 0;
            if (te.omega > 0) {
                f = Math.max(f, (float) Math.sqrt(te.omega * 0.5F / EngineType.JET.getSpeed()));
            }
            if (te.temperature > 100) {
                f = Math.max(f, Math.min(1, (te.temperature - 100F) / (te.getMaxExhaustTemperature() - 100F)));
            }
            double dd = 0.25;
            float fac = te.isAfterburning() ? 1 : 0.75F;
            for (double d = 0; d <= 3; d += dd) {
                dx += dd * tile.getWriteDirection().getStepX();
                dz += dd * tile.getWriteDirection().getStepZ();
                HeatRippleRenderer.instance.addHeatRippleEffectIfLOS(tile, tile.getBlockPos().getX() + 0.5 + dx, tile.getBlockPos().getY() + 0.5, tile.getBlockPos().getZ() + 0.5 + dz, ep, dist, f, fac, 1, 1);
                stack.translate(0, 0, -dd);
                fac *= te.isAfterburning() ? 0.875 : 0.825;
                if (fac <= 0.01)
                    break;
            }
            stack.popPose();
        }*/
    }

    /**
     * 1.21.5 submit hook. submitCustomGeometry takes a single RenderType, so we pre-compute the
     * one the legacy switch would have requested (engine-type → texture → entitySolid RT) before
     * queueing. Snapshotting the pose into a fresh PoseStack lets renderBlockEntityEngineAt's
     * unchanged transform code run inside the deferred lambda after the outer poseStack has been
     * popped by the dispatcher.
     */
    @Override
    public void submit(BlockEntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;
        BlockEntity be = level.getBlockEntity(state.blockPos);
        if (!(be instanceof BlockEntityEngine engine)) return;
        if (!this.doRenderModel(poseStack, engine)) return;

        Identifier tex = textureForEngine(engine.getEngineType());
        if (tex == null) return;

        PoseStack snapped = new PoseStack();
        snapped.last().set(poseStack.last());

        RenderType rt = RenderTypes.entitySolid(tex);
        int light = state.lightCoords;
        collector.submitCustomGeometry(poseStack, rt, (pose, vc) -> {
            MultiBufferSource oneRT = ignored -> vc;
            renderBlockEntityEngineAt(snapped, engine, oneRT, light);
        });

        if (engine instanceof BlockEntityJetEngine jet && jet.getTemperature() > 600) {
            int temp = Math.max(jet.getTemperature() - 600, (jet.getTemperature() - 1000) * 5 / 2);
            int c = ReikaPhysicsHelper.getColorForTemperature(temp);
            int r = ReikaColorAPI.getRed(c);
            int g = ReikaColorAPI.getGreen(c);
            int b = ReikaColorAPI.getBlue(c);
            int glowColor = (255 << 24) | (r << 16) | (g << 8) | b;

            PoseStack glowSnapped = new PoseStack();
            glowSnapped.last().set(poseStack.last());

            // 26.1: the legacy heat glow used BlendMode.ADDITIVEDARK so the opaque (alpha-less)
            // glow-mask texture's black background added nothing and only the bright nozzle
            // glowed. RenderTypes.eyes() in 1.21.5 switched to BlendFunction.TRANSLUCENT (it used
            // to be additive), so re-rendering the whole jet model through it painted the engine
            // solid black. energySwirl is the additive+emissive pipeline (BlendFunction.ADDITIVE);
            // offsets of 0 disable the UV scroll, giving a static additive glow == ADDITIVEDARK.
            RenderType glowRT = RenderTypes.energySwirl(JET_GLOW_MASK, 0.0F, 0.0F);
            float phi = -engine.phi;
            collector.submitCustomGeometry(poseStack, glowRT, (pose, vc) -> {
                renderJetGlow(glowSnapped, engine, vc, phi, glowColor);
            });
        }

        if (engine.isInWorld()) {
            IORenderer.renderIO(poseStack, collector, engine, engine.getBlockPos());
        }
    }

    private void renderJetGlow(PoseStack stack, BlockEntityEngine engine, VertexConsumer vc, float phi, int color) {
        stack.pushPose();

        if (engine.isInWorld()) {
            float f = engine.getBlockState().getValue(BlockRotaryCraftMachine.FACING).toYRot();
            if (engine.getEngineType().isJetFueled()) {
                f += 90;
            }
            stack.translate(0.5F, 1.5F, 0.5F);
            stack.mulPose(Axis.YP.rotationDegrees(-f - 90));
            stack.mulPose(Axis.ZP.rotationDegrees(180));
        }

        double s = 1.005;
        stack.pushPose();
        stack.translate(0, 1 - s, (1 - s) / 4);
        stack.scale((float) s, (float) s, (float) s);
        jetModel.renderAllColored(stack, vc, 15728880, engine, null, phi, 0, color);
        stack.popPose();

        stack.pushPose();
        stack.translate(0, s - 1, -(1 - s) / 4);
        stack.scale((float) (1 / s), (float) (1 / s), (float) (1 / s));
        jetModel.renderAllColored(stack, vc, 15728880, engine, null, phi, 0, color);
        stack.popPose();

        stack.popPose();
    }

    /**
     * Mirror of the engine-type switch in {@link #renderBlockEntityEngineAt}, only for picking
     * the texture. JET is intentionally null — the JetModel's TEXTURE_LOCATION is a directory
     * prefix that needs a frame-specific suffix appended at render time (animation), and the
     * legacy switch's JET case is commented out anyway. HYDRO isn't in the current EngineType
     * enum.
     */
    private static Identifier textureForEngine(EngineType type) {
        return switch (type) {
            case DC -> DCModel.TEXTURE_LOCATION;
            case STEAM -> SteamModel.TEXTURE_LOCATION;
            case GAS -> CombustionModel.TEXTURE_LOCATION;
            case AC -> ACModel.TEXTURE_LOCATION;
            case SPORT -> PerformanceModel.TEXTURE_LOCATION;
            case MICRO -> MicroTurbineModel.TEXTURE_LOCATION;
            case WIND -> WindModel.TEXTURE_LOCATION;
            case JET -> // JetModel's TEXTURE_LOCATION is a directory prefix the legacy animation
                    // logic appended a suffix to. Until that's ported, point straight at the
                    // existing {@code jettex.png} so the jet engine actually renders in-world.
                    textureWithSuffix(JetModel.TEXTURE_LOCATION, "jettex.png");
        };
    }

    // Legacy 1.7.10 signature retained for reference / item-stack rendering callers; vanilla no
    // longer invokes this — submit() above is the live path.
    public void render(BlockEntityEngine tile, float v, PoseStack stack, MultiBufferSource multiBufferSource, int i, int i1) {
        if (this.doRenderModel(stack, tile))
            this.renderBlockEntityEngineAt(stack, tile, multiBufferSource, i);
        if (tile instanceof RedstoneUpgradeable) {
            if ((tile).isInWorld()) {//&& MinecraftForgeClient.getRenderPass() == 0) {
                this.renderRedstoneFrame(stack, tile, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ(), multiBufferSource, v);
            }
        }
        if (tile.isInWorld()) {//&& MinecraftForgeClient.getRenderPass() == 1) {
            IORenderer.renderIO(stack, multiBufferSource, tile, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ());
            /*
			BlockEntityEngine eng = (BlockEntityEngine)tile;
			if (eng.type == EngineType.JET && eng.power > 0)
				this.renderGlow(tile, par2, par4, par6);
			 */
//            BlockEntityEngine eng = tile;
//            eng.power = 1;
//            if (eng.getEngineType() == EngineType.JET && eng.power > 0) {
//            	jetGlow.setPosition(tile.xCoord+0.5, tile.yCoord+0.5, tile.zCoord+0.5);
//            	jetGlow.render();
//            }
        }
    }

    private void renderRedstoneFrame(PoseStack stack, BlockEntityEngine tile, int x, int y, int z, MultiBufferSource bufferSource, float par2) {
        // TODO: Port to 26.1 rendering API (Tesselator.getBuilder() + vertex().uv().endVertex() + end() all removed)
    }
}

