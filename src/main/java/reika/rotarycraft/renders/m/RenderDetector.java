///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.renders.m;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.blaze3d.vertex.VertexConsumer;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.client.renderer.RenderType;
//import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
//import net.minecraft.client.renderer.texture.OverlayTexture;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.blockentities.BlockPlayerDetector;
//import reika.rotarycraft.models.ModelDetector;
//import reika.rotarycraft.registry.RotaryModelLayers;
//
//public class RenderDetector extends RotaryTERenderer<BlockPlayerDetector> {
//
//    private ModelDetector DetectorModel;
//    //private ModelDetectorV DetectorModelV = new ModelDetectorV();
//
//    public RenderDetector(BlockEntityRendererProvider.Context context) {
//        DetectorModel = new ModelDetector(context.bakeLayer(RotaryModelLayers.FLOODLIGHT));
//    }
//
//    /**
//     * Renders the BlockEntity for the position.
//     */
//    public void renderBlockPlayerDetectorAt(PoseStack stack, BlockPlayerDetector tile, MultiBufferSource bufferSource, int pPackedLight) {
//        ModelDetector var14;
//        var14 = DetectorModel;
//        //ModelDetectorV var15;
//        //var14 = this.DetectorModelV;
//        stack.pushPose();
////        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
////        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        stack.translate((float) tile.getBlockPos().getX(), (float) tile.getBlockPos().getY() + 2.0F, (float) tile.getBlockPos().getZ() + 1.0F);
//        stack.scale(1.0F, -1.0F, -1.0F);
//        stack.translate(0.5F, 0.5F, 0.5F);
//        int var11 = 0;     //used to rotate the model about metadata
//
//
//        //float var12 = tile.prevLidAngle + (tile.lidAngle - tile.prevLidAngle) * par8;
//        float var13;/*
//
//            var12 = 1.0F - var12;
//            var12 = 1.0F - var12 * var12 * var12;*/
//        // if (tile.getBlockMetadata() < 4)
//
//        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutout(ModelDetector.TEXTURE_LOCATION));
//        DetectorModel.renderToBuffer(stack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
//
////        if (tile.isInWorld())
////            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//        stack.popPose();
////        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//    }
//
//    @Override
//    public void render(BlockPlayerDetector tile, float v, PoseStack stack, MultiBufferSource multiBufferSource, int i, int i1) {
//        if (this.doRenderModel(stack, tile)) {
//            this.renderBlockPlayerDetectorAt(stack, tile, multiBufferSource, i);
//        }
//        if ((tile).isInWorld())// && MinecraftForgeClient.getRenderPass() == 1)
//            IORenderer.renderIO(stack, bufferSource, tile, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ());
//    }
//
//}
