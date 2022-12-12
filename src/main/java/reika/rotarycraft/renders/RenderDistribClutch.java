///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.renders;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.math.Quaternion;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
//import reika.rotarycraft.blockentities.transmission.BlockEntityDistributionClutch;
//import reika.rotarycraft.model.animated.ModelDistribClutch;
//
//public class RenderDistribClutch extends RotaryTERenderer {
//
//    private ModelDistribClutch DistributionClutchModel = new ModelDistribClutch();
//
//    /**
//     * Renders the BlockEntity for the position.
//     */
//    public void renderBlockEntityDistributionClutchAt(PoseStack stack, BlockEntityDistributionClutch tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        ModelDistribClutch var14;
//        var14 = DistributionClutchModel;
//
//        this.bindTextureByName("/reika/rotarycraft/textures/blockentitytex/transmission/shaft/distribclutchtex.png");
//
//        stack.pushPose();
//        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        stack.translate((float) par2, (float) par4 + 2.0F, (float) par6 + 1.0F);
//        stack.scale(1.0F, -1.0F, -1.0F);
//        stack.translate(0.5F, 0.5F, 0.5F);
//        int var11 = 0;
//        float var13;
//
//        if (tile.isInWorld()) {
//
//            switch (tile.getBlockState().getValue(getFacingProperty())) {
//                case EAST:
//                    var11 = 90;
//                    break;
//                case WEST:
//                    var11 = -90;
//                    break;
//                case SOUTH:
//                    var11 = 180;
//                    break;
//                case NORTH:
//                    var11 = 0;
//                    break;
//            }
//
//            stack.mulPose(new Quaternion(var11, 0.0F, 1.0F, 0.0F));
//
//        }
//
//        var14.renderAll(stack, tile, null, tile.phi);
//
//        if (tile.isInWorld())
//            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//        stack.popPose();
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//
//    }
//
//    @Override
//    public void render(BlockEntity tile, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
//        /*todo if (this.doRenderModel((RotaryCraftBlockEntity) tile))
//            this.renderBlockEntityDistributionClutchAt(poseStack,(BlockEntityDistributionClutch) tile, par2, par4, par6, par8);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1) {
//            IORenderer.renderIO(tile, par2, par4, par6);
//            //this.renderFaceColors((BlockEntityDistributionClutch)tile, par2, par4, par6);
//        }*/
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        return "distribclutchtex.png";
//    }
//
//}
