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
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import reika.dragonapi.libraries.rendering.ReikaRenderHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.auxiliary.RotaryAux;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.transmission.BlockEntityAdvancedGear;
import reika.rotarycraft.models.CVTModel;
import reika.rotarycraft.models.animated.CoilModel;
import reika.rotarycraft.models.animated.HighGearModel;
import reika.rotarycraft.models.animated.WormModel;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderAdvGear extends RotaryTERenderer<BlockEntityAdvancedGear> {

    private final WormModel wormModel;
    private final CVTModel cvtModel;
    private final CoilModel coilModel;
    private final HighGearModel highGearModel;

    public RenderAdvGear(BlockEntityRendererProvider.Context context) {
        wormModel = new WormModel(context.bakeLayer(RotaryModelLayers.WORM));
        cvtModel = new CVTModel(context.bakeLayer(RotaryModelLayers.CVT));
        coilModel = new CoilModel(context.bakeLayer(RotaryModelLayers.COIL));
        highGearModel = new HighGearModel(context.bakeLayer(RotaryModelLayers.HIGHGEAR));
    }

    public void renderBlockEntityAdvancedGearAt(BlockEntityAdvancedGear tile, PoseStack stack, MultiBufferSource bufferSource, int pPackedLight) {
        float f = tile.getBlockState().getValue(BlockRotaryCraftMachine.FACING).toYRot();
        stack.pushPose();
        stack.translate(0.5F, 1.5F, 0.5F);
        stack.mulPose(Axis.YP.rotationDegrees(-f + 90));
        stack.mulPose(Axis.ZP.rotationDegrees(180));
        switch (tile.getGearType()) {
            case WORM -> {
                VertexConsumer wormTex = bufferSource.getBuffer(RenderType.entityCutout((new ResourceLocation(RotaryCraft.MODID, "textures/blockentitytex/transmission/shaft/shafttex.png"))));
                wormModel.renderAll(stack, wormTex, pPackedLight, tile, null, -tile.phi);
            }
            case CVT -> {
                VertexConsumer cvtTex = bufferSource.getBuffer(RenderType.entityCutout((new ResourceLocation(RotaryCraft.MODID, "textures/blockentitytex/transmission/shaft/cvttex.png"))));
                cvtModel.renderAll(stack, cvtTex, pPackedLight, tile, null, -tile.phi);
                if (tile.isInWorld()) {
//                    GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
//                    GL11.glDisable(GL11.GL_LIGHTING);
                    ReikaRenderHelper.disableEntityLighting();
                    Font fr = this.getFontRenderer();
                    float var10 = 0.6666667F * 0.8F;
                    stack.scale(var10, -var10, -var10);
                    float var112 = 0.016666668F * var10;
                    stack.translate(0.0F, 0.61875F * var10, 0.20625F * var10);
                    stack.mulPose(Axis.XN.rotationDegrees(20F));
                    stack.translate(-0.175F, -0.545F, -0.19F); //was 0.1X
                    stack.scale(var112, -var112, var112);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.depthMask(false);
                    stack.translate(5, -48, 37);
                    stack.scale(2, 2, 2);
                    String var15b;

                    if (tile.getTicksExisted() / 80 % 2 == 0) {
                        var15b = RotaryAux.formatPower(tile.power);
                        fr.drawInBatch(var15b, -18, 70, 0xffffff, false, stack.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);

                        var15b = RotaryAux.formatTorque(tile.torque);
                        fr.drawInBatch(var15b, -18, 82, 0xffffff, false, stack.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);

                        var15b = RotaryAux.formatSpeed(tile.omega);
                        fr.drawInBatch(var15b, -18, 94, 0xffffff, false, stack.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
                    } else {
                        stack.scale(2, 2, 2);
                        stack.translate(0.075F, 0.25F, 0.1F);
                        int ratio = tile.getCVTRatio();
                        if (ratio > 0) {
                            var15b = "1:" + ratio;
                        } else {
                            var15b = -ratio + ":1";
                        }
                        while (var15b.length() < 5) {
                            var15b = " " + var15b;
                        }
                        fr.drawInBatch(var15b, -10, 39, 0xffffff, false, stack.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
                    }
                    //GL11.glPopAttrib();
                }
            }
            case COIL -> {
                if (tile.isBedrockCoil()) {
                    VertexConsumer bedCoilTex = bufferSource.getBuffer(RenderType.entityCutout((new ResourceLocation(RotaryCraft.MODID, "textures/blockentitytex/transmission/shaft/coiltex_bed.png"))));
                    coilModel.renderAll(stack, bedCoilTex, pPackedLight, tile, null, -tile.phi);
                } else {
                    VertexConsumer coilTex = bufferSource.getBuffer(RenderType.entityCutout((new ResourceLocation(RotaryCraft.MODID, "textures/blockentitytex/transmission/shaft/coiltex.png"))));
                    coilModel.renderAll(stack, coilTex, pPackedLight, tile, null, -tile.phi);
                }
            }
            case HIGH -> {
                VertexConsumer highTex = bufferSource.getBuffer(RenderType.entityCutout((new ResourceLocation(RotaryCraft.MODID, "textures/blockentitytex/transmission/shaft/highgeartex.png"))));
                highGearModel.renderAll(stack, highTex, pPackedLight, tile, null, -tile.phi);
            }
        }
        stack.popPose();
    }

    @Override
    public void render(BlockEntityAdvancedGear tile, float e, PoseStack stack, MultiBufferSource bufferSource, int light, int overlay) {
      /*  if (par8 <= -999F) {
            itemMetadata = (int) -par8 / 1000;
            par8 = 0F;
        }*/
        if (this.doRenderModel(stack, tile))
            this.renderBlockEntityAdvancedGearAt(tile, stack, bufferSource, light);
        if (tile.isInWorld())// && MinecraftForgeClient.getRenderPass() == 1)
            IORenderer.renderIO(stack, bufferSource, tile, tile.getBlockPos());
    }

}
