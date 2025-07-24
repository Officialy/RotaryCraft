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
//import reika.dragonapi.libraries.java.ReikaGLHelper.BlendMode;
//import reika.rotarycraft.blockentities.World.BlockEntityAerosolizer;
//import com.mojang.blaze3d.shaders.BlendMode;
//import com.mojang.blaze3d.vertex.BufferBuilder;
//import com.mojang.blaze3d.vertex.Tesselator;
//import net.minecraft.BlockEntity.BlockEntity;
//import net.minecraft.client.renderer.Tesselator;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.neoforged.client.MinecraftForgeClient;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.blockentities.level.BlockEntityAerosolizer;
//import reika.rotarycraft.models.ModelAerosolizer;
//
//public class RenderAerosolizer extends RotaryTERenderer {
//
//    private ModelAerosolizer AerosolizerModel = new ModelAerosolizer();
//    //private ModelAerosolizerV AerosolizerModelV = new ModelAerosolizerV();
//
//    /**
//     * Renders the BlockEntity for the position.
//     */
//    public void renderBlockEntityAerosolizerAt(BlockEntityAerosolizer tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int var9;
//
//        if (!tile.isInWorld()) {
//            var9 = 0;
//        } else {
//
//            var9 = tile.getBlockMetadata();
//
//
//            {
//                //((BlockAerosolizerBlock1)var10).unifyAdjacentChests(tile.level, tile.xCoord, tile.yCoord, tile.zCoord);
//                var9 = tile.getBlockMetadata();
//            }
//        }
//
//        if (true) {
//            ModelAerosolizer var14;
//            var14 = AerosolizerModel;
//            //ModelAerosolizerV var15;
//            //var14 = this.AerosolizerModelV;
//            this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/aerotex.png");
//
//            stack.pushPose();
//            // if (!tile.isInWorld())
//            //GL11.glDisable(GL11.GL_LIGHTING);
//            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//            stack.translate((float) par2, (float) par4 + 2.0F, (float) par6 + 1.0F);
//            stack.scale(1.0F, -1.0F, -1.0F);
//            stack.translate(0.5F, 0.5F, 0.5F);
//            int var11 = 0;     //used to rotate the model about metadata
//
//            //float var12 = tile.prevLidAngle + (tile.lidAngle - tile.prevLidAngle) * par8;
//            float var13;/*
//
//            var12 = 1.0F - var12;
//            var12 = 1.0F - var12 * var12 * var12;*/
//            // if (tile.getBlockMetadata() < 4)
//            var14.renderAll(stack, tile, /*ReikaJavaLibrary.makeListFrom(/*liqlevel > 0*//*false)*/null);
//            // else
//            //var15.renderAll(stack, tile, );
//            //if (!tile.isInWorld())
//            //GL11.glEnable(GL11.GL_LIGHTING);
//            if (tile.isInWorld())
//                GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//            stack.popPose();
//            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        }
//    }
//
//    @Override
//    public void renderBlockEntityAt(BlockEntity tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        if (this.doRenderModel((RotaryCraftBlockEntity) tile))
//            this.renderBlockEntityAerosolizerAt((BlockEntityAerosolizer) tile, par2, par4, par6, par8);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1) {
//            IORenderer.renderIO(tile, par2, par4, par6);
//            this.drawPotionLevels((BlockEntityAerosolizer) tile, par2, par4, par6);
//        }
//    }
//
//    private void drawPotionLevels(BlockEntityAerosolizer tile, double par2, double par4, double par6) {
//        stack.pushPose();
//
//        Tesselator tess = Tesselator.getInstance();
//        BufferBuilder v5 = tess.getBuilder();
//        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
//        GL11.glDisable(GL11.GL_TEXTURE_2D);
//        GL11.glDisable(GL11.GL_LIGHTING);
//        RenderSystem.enableBlend();
//        RenderSystem.defaultBlendFunc();
//
//        stack.translate(par2, par4, par6);
//
//        for (int i = 0; i < 9; i++) {
//            int lvl = tile.getPotionLevel(i);
//            if (lvl > 0) {
//
//                double h = 0.785 + (0.075 * lvl / tile.CAPACITY);
//                double w = 0.25;
//
//                double dx = 0.0625 + (i % 3) * (5 / 16D);
//                double dy = 0.0625 + (i / 3) * (5 / 16D);
//                v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//
//                v5.setColorRGBA_I(tile.getPotionColor(i), 192);
//
//                v5.vertex(dx, h, dy + w);
//                v5.vertex(dx + w, h, dy + w);
//                v5.vertex(dx + w, h, dy);
//                v5.vertex(dx, h, dy);
//
//                v5.end();
//            }
//        }
//
//        //GL11.glPopAttrib();
//        stack.popPose();
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        return "aerotex.png";
//    }
//}
