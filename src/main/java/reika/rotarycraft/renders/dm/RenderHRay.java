///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.renders.dm;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.blaze3d.vertex.VertexConsumer;
//import org.joml.Vector3f;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.client.renderer.RenderType;
//import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
//import net.minecraft.client.renderer.texture.OverlayTexture;
//import org.lwjgl.opengl.GL11;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
//import reika.rotarycraft.blockentities.weaponry.BlockEntityHeatRay;
//import reika.rotarycraft.models.ModelHRay;
//import reika.rotarycraft.registry.RotaryModelLayers;
//
//public class RenderHRay extends RotaryTERenderer<BlockEntityHeatRay> {
//
//    private ModelHRay HRayModel;
//
//    public RenderHRay(BlockEntityRendererProvider.Context context) {
//        HRayModel = new ModelHRay(context.bakeLayer(RotaryModelLayers.FLOODLIGHT));
//    }
//
//    /**
//     * Renders the BlockEntity for the position.
//     */
//    public void renderBlockEntityHeatRayAt(PoseStack stack, BlockEntityHeatRay tile, MultiBufferSource bufferSource, int pPackedLight) {
//        ModelHRay var14 = HRayModel;
//
//        this.setupGL(stack, tile, (float) tile.getBlockPos().getX(), (float) tile.getBlockPos().getY(), (float) tile.getBlockPos().getZ());
//
//        int var11 = 0;     //used to rotate the model about metadata
//
//        if (tile.isInWorld()) {
//            switch (tile.getBlockState().getValue(getFacingProperty())) {
//                case EAST -> var11 = 0;
//                case WEST -> var11 = 180;
//                case SOUTH -> var11 = 270;
//                case NORTH -> var11 = 90;
//            }
//
//            stack.mulPose((float) var11 + 90, 0.0F, 1.0F, 0.0F);
//            stack.mulPose(Axis.YP.rotationDegrees(var11 + 90));
//        } else {
////            GL11.glEnable(GL11.GL_LIGHTING);
//        }
//
//        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutout(ModelHRay.TEXTURE_LOCATION));
//        HRayModel.renderToBuffer(stack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
//
//        this.closeGL(stack, tile);
//    }
//
//    @Override
//    public void render(BlockEntityHeatRay tile, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
//        if (this.doRenderModel(poseStack, tile))
//            this.renderBlockEntityHeatRayAt(poseStack, tile, multiBufferSource, i);
//        if ((tile).isInWorld()) {// && MinecraftForgeClient.getRenderPass() == 1)
//            IORenderer.renderIO(stack, bufferSource, tile, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ());
//        }
//
//    }
//}