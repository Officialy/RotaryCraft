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
//import reika.rotarycraft.blockentities.auxiliary.BlockEntityPipePump;
//import net.minecraft.BlockEntity.BlockEntity;
//import net.minecraftforge.client.MinecraftForgeClient;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.models.ModelPipePump;
//
//public class RenderPipePump extends RotaryTERenderer {
//
//    private ModelPipePump PipePumpModel = new ModelPipePump();
//
//    public void renderBlockEntityPipePumpAt(BlockEntityPipePump tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int var9;
//
//        if (!tile.isInWorld())
//            var9 = 0;
//        else
//            var9 = tile.getBlockMetadata();
//
//        ModelPipePump var14;
//        var14 = PipePumpModel;
//
//        this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/pipepumptex.png");
//
//        stack.pushPose();
//        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        stack.translate((float) par2, (float) par4 + 2.0F, (float) par6 + 1.0F);
//        stack.scale(1.0F, -1.0F, -1.0F);
//        stack.translate(0.5F, 0.5F, 0.5F);
//        int var11 = 0;     //used to rotate the model about metadata
//
//        if (tile.isInWorld()) {
//            switch (tile.getBlockMetadata()) {
//                case 0:
//                    var11 = 270;
//                    break;
//                case 1:
//                    var11 = 90;
//                    break;
//                case 2:
//                    var11 = 0;
//                    break;
//                case 3:
//                    var11 = 180;
//                    break;
//                case 4:
//                    var11 = 90;
//                    break;
//                case 5:
//                    var11 = 270;
//                    break;
//            }
//
//            if (tile.getBlockMetadata() <= 3)
//                GL11.glRotatef((float) var11 + 90, 0.0F, 1.0F, 0.0F);
//            else {
//                stack.mulPose(new Quaternion(var11, 0F, 0F, 1F);
//                stack.translate(tile.getBlockMetadata() == 5 ? -1 : 1, -1F, 0);
//            }
//        }
//
//        float var13;
//
//
//        var14.renderAll(stack, tile, null, -tile.phi);
//
//        if (tile.isInWorld())
//            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//        stack.popPose();
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//    }
//
//    @Override
//    public void renderBlockEntityAt(BlockEntity tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        if (this.doRenderModel((RotaryCraftBlockEntity) tile))
//            this.renderBlockEntityPipePumpAt((BlockEntityPipePump) tile, par2, par4, par6, par8);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1)
//            IORenderer.renderIO(tile, par2, par4, par6);
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        return "pipepumptex.png";
//    }
//}
