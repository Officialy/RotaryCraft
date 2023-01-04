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
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.auxiliary.interfaces.AlternatingRedstoneUser;
import reika.rotarycraft.auxiliary.interfaces.RedstoneUpgradeable;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blockentity.BlockEntityEngine;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.models.engine.*;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryModelLayers;

//@SideOnly(Dist.CLIENT)
public class RenderSEngine extends RotaryTERenderer<BlockEntityEngine> {

    private DCModel dcModel;
    private SteamModel steamModel;
    private CombustionModel combModel;
    private ACModel acModel;
    private PerformanceModel perfModel;
    private MicroTurbineModel microModel;
    private JetModel jetModel;
    private HydroModel hydroModel;
    private WindModel windModel;

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

            Level level = tile.getLevel();
            boolean flag = level != null;
            BlockState blockstate = flag ? tile.getBlockState() : RotaryBlocks.ENGINE.get().defaultBlockState().setValue(BlockRotaryCraftMachine.FACING, Direction.SOUTH);

            float f = blockstate.getValue(BlockRotaryCraftMachine.FACING).toYRot();
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
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entitySolid((DCModel.TEXTURE_LOCATION)));
//                RotaryCraft.LOGGER.info(tile.getUpdateTag().getFloat("phi"));
                dcModel.renderAll(stack, vertexconsumer, pPackedLight, tile, null, -tile.getUpdateTag().getFloat("phi"));
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
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entitySolid((WindModel.TEXTURE_LOCATION)));
                windModel.renderToBuffer(stack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            }
            case STEAM -> {
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entitySolid((SteamModel.TEXTURE_LOCATION)));
                steamModel.renderToBuffer(stack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            }
            case GAS -> {
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entitySolid((SteamModel.TEXTURE_LOCATION)));
                combModel.renderToBuffer(stack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            }
            case AC -> {
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entitySolid((ACModel.TEXTURE_LOCATION)));
                acModel.renderToBuffer(stack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            }
            case SPORT -> {
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entitySolid((PerformanceModel.TEXTURE_LOCATION)));
                perfModel.renderToBuffer(stack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            }
//            case HYDRO:
//                BlockEntityHydroEngine eng = (BlockEntityHydroEngine) tile;
//                var21.renderAll(stack, tile, ReikaJavaLibrary.makeListFrom(eng.failed, eng.isBedrock()), eng.isReversed() ? tile.phi : -tile.phi, 0);
//                break;
            case MICRO -> {
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entitySolid((MicroTurbineModel.TEXTURE_LOCATION)));
                microModel.renderToBuffer(stack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
                stack.mulPose(new Quaternionf(90, 0.0F, 1.0F, 0.0F));
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
//                    RenderSystem.enableBlend();
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

    @Override
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
        RedstoneUpgradeable ar = (RedstoneUpgradeable) tile;
        if (!ar.hasRedstoneUpgrade())
            return;
        boolean bright = true;
        if (tile instanceof AlternatingRedstoneUser && !ar.hasRedstoneSignal())
            bright = (tile.getTicksExisted() / 3) % 2 == 0;
        int c = bright ? 0xff0000 : 0x900000;
        int c2 = bright ? 0xffa7a7 : 0xda0000;
//    todo    GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

//        stack.translate(x, y, z);
//        todo if (bright)
//      todo      GL11.glDisable(GL11.GL_LIGHTING);

//        RenderSystem.enableBlend();
        RenderSystem.enableTexture();

        double o = 0.005;
        double t = 0.05;
        double p = 0.125;
        double h = tile.isFlipped ? 1 - p - o : p + o + t;
        double h2 = tile.isFlipped ? 1 - p - o - t : p + o;
        double w = 0.475;

// todo       ReikaTextureHelper.bindTerrainTexture();
//        IIcon ico = Blocks.REDSTONE_BLOCK.blockIcon;
        float u = 5;//ico.getMinU();
        float v = 5;//ico.getMinV();
        float du = 55;// ico.getMaxU();
        float dv = 55;// ico.getMaxV();

        Tesselator tess = Tesselator.getInstance();
        BufferBuilder v5 = tess.getBuilder();

//        v5.setBrightness(240);
//        v5.setColorRGBA_I(c, 255/*240*/);
        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        if (tile.isFlipped) {
            v5.vertex(0.5 - w, h2, 0.5 - w).uv(u, dv).endVertex();
            v5.vertex(0.5 + w, h2, 0.5 - w).uv(du, dv).endVertex();
            v5.vertex(0.5 + w, h2, 0.5 + w).uv(du, v).endVertex();
            v5.vertex(0.5 - w, h2, 0.5 + w).uv(u, v).endVertex();
        } else {
            v5.vertex(0.5 - w, h, 0.5 + w).uv(u, v).endVertex();
            v5.vertex(0.5 + w, h, 0.5 + w).uv(du, v).endVertex();
            v5.vertex(0.5 + w, h, 0.5 - w).uv(du, dv).endVertex();
            v5.vertex(0.5 - w, h, 0.5 - w).uv(u, dv).endVertex();
        }

        v5.vertex(0.5 + w, h2, 0.5 - w).uv(u, v).endVertex();
        v5.vertex(0.5 - w, h2, 0.5 - w).uv(du, v).endVertex();
        v5.vertex(0.5 - w, h, 0.5 - w).uv(du, dv).endVertex();
        v5.vertex(0.5 + w, h, 0.5 - w).uv(u, dv).endVertex();

        v5.vertex(0.5 + w, h, 0.5 + w).uv(u, v).endVertex();
        v5.vertex(0.5 - w, h, 0.5 + w).uv(du, v).endVertex();
        v5.vertex(0.5 - w, h2, 0.5 + w).uv(du, dv).endVertex();
        v5.vertex(0.5 + w, h2, 0.5 + w).uv(u, dv).endVertex();

        v5.vertex(0.5 - w, h, 0.5 + w).uv(u, v).endVertex();
        v5.vertex(0.5 - w, h, 0.5 - w).uv(du, v).endVertex();
        v5.vertex(0.5 - w, h2, 0.5 - w).uv(du, dv).endVertex();
        v5.vertex(0.5 - w, h2, 0.5 + w).uv(u, dv).endVertex();

        v5.vertex(0.5 + w, h2, 0.5 + w).uv(u, v).endVertex();
        v5.vertex(0.5 + w, h2, 0.5 - w).uv(du, v).endVertex();
        v5.vertex(0.5 + w, h, 0.5 - w).uv(du, dv).endVertex();
        v5.vertex(0.5 + w, h, 0.5 + w).uv(u, dv).endVertex();
        v5.end();
//   todo     //GL11.glPopAttrib();
    }
}
