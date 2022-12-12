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
//import reika.dragonapi.libraries.java.ReikaJavaLibrary;
//import reika.rotarycraft.blockentities.Production.BlockEntityBedrockBreaker;
//import net.minecraft.BlockEntity.BlockEntity;
//import net.minecraftforge.client.MinecraftForgeClient;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.models.Animated.ModelBedrockBreaker;
//import reika.rotarycraft.models.Animated.ModelBedrockBreakerV;
//import reika.rotarycraft.models.animated.ModelBedrockBreaker;
//import reika.rotarycraft.models.animated.ModelBedrockBreakerV;
//
//import java.util.ArrayList;
//
//public class RenderBedrockBreaker extends RotaryTERenderer {
//    private ModelBedrockBreaker bbm = new ModelBedrockBreaker();
//    private ModelBedrockBreakerV bbmV = new ModelBedrockBreakerV();
//
//    public void renderBlockEntityBedrockBreakerAt(BlockEntityBedrockBreaker tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int var9;
//
//        if (!tile.isInWorld())
//            var9 = 0;
//        else
//            var9 = tile.getBlockMetadata();
//
//        ModelBedrockBreaker var14;
//        ModelBedrockBreakerV var15;
//
//        var14 = bbm;
//        var15 = bbmV;
//
//        this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/bedrocktex.png");
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
//
//            switch (tile.getBlockMetadata()) {
//                case 0:
//                    var11 = 0;
//                    break;
//                case 1:
//                    var11 = 180;
//                    break;
//                case 2:
//                    var11 = -270;
//                    break;
//                case 3:
//                    var11 = -90;
//                    break;
//                case 4:
//                    var11 = 270;
//                    break;
//                case 5:
//                    var11 = 90;
//                    break;
//            }
//
//            if (tile.getBlockMetadata() <= 3)
//                stack.mulPose(new Quaternion(var11, 0.0F, 1.0F, 0.0F);
//            else {
//                if (tile.getBlockMetadata() == 4)
//                    stack.mulPose(new Quaternion(180, 0.0F, 1.0F, 0.0F);
//                stack.mulPose(new Quaternion(var11, 0F, 0F, 1F);
//                stack.translate(-1F, -1F, 0F);
//                if (tile.getBlockMetadata() == 5)
//                    stack.translate(2F, 0F, 0F);
//            }
//        } else {
//            GL11.glEnable(GL11.GL_LIGHTING);
//        }
//
//        float var13;
//
//        float f = tile.isInWorld() ? tile.getGrindFraction() : 0;
//        ArrayList li = ReikaJavaLibrary.makeListFrom(tile.getStep(), f);
//        if (tile.isInWorld() && tile.getBlockMetadata() > 3) {
//            this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/bedrockvtex.png");
//            var15.renderAll(stack, tile, li, tile.phi, 0);
//        } else
//            var14.renderAll(stack, tile, li, tile.phi, 0);
//        if (tile.isInWorld())
//            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//        stack.popPose();
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//    }
//
//    @Override
//    public void renderBlockEntityAt(BlockEntity tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        if (this.doRenderModel((RotaryCraftBlockEntity) tile))
//            this.renderBlockEntityBedrockBreakerAt((BlockEntityBedrockBreaker) tile, par2, par4, par6, par8);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1)
//            IORenderer.renderIO(tile, par2, par4, par6);
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        BlockEntityBedrockBreaker tb = (BlockEntityBedrockBreaker) te;
//        if (tb.isInWorld() && tb.getBlockMetadata() > 3)
//            return "bedrockvtex.png";
//        return "bedrocktex.png";
//    }
//}
