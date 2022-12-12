///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.renders.mi;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.blaze3d.vertex.VertexConsumer;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.client.renderer.RenderType;
//import net.minecraft.client.renderer.texture.OverlayTexture;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.blockentities.farming.BlockEntityComposter;
//import reika.rotarycraft.models.ModelComposter;
//import reika.rotarycraft.models.ModelMonitor;
//
//public class RenderComposter extends RotaryTERenderer<BlockEntityComposter> {
//
//    private ModelComposter ComposterModel;
//
//    /**
//     * Renders the BlockEntity for the position.
//     */
//    public void renderBlockEntityFloodlightAt(PoseStack stack, BlockEntityComposter tile, MultiBufferSource bufferSource, int pPackedLight) {
//        ModelComposter var14;
//        var14 = ComposterModel;
//
//        this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/compostertex.png");
//
//        stack.pushPose();
//        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        stack.translate((float) tile.getBlockPos().getX(), (float) tile.getBlockPos().getY() + 2.0F, (float) tile.getBlockPos().getZ() + 1.0F);
//        stack.scale(1.0F, -1.0F, -1.0F);
//        stack.translate(0.5F, 0.5F, 0.5F);
//        int var11 = 0;     //used to rotate the model about metadata
//
//        float var13;
//
//        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutout(ModelMonitor.TEXTURE_LOCATION));
//        ComposterModel.renderToBuffer(stack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
//
//        if (tile.isInWorld())
//            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//        stack.popPose();
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//    }
//
//    @Override
//    public void render(BlockEntityComposter tile, float v, PoseStack stack, MultiBufferSource multiBufferSource, int i, int i1) {
//       /* todo if (this.doRenderModel((RotaryCraftBlockEntity) tile))
//           this.renderBlockEntityComposterAt(stack, (BlockEntityComposter) tile, par2, par4, par6, par8); */
//
//    }
//
//}
