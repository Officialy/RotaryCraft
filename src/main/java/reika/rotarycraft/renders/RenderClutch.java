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
//import com.mojang.blaze3d.vertex.*;
//import com.mojang.math.Quaternion;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraftforge.client.MinecraftForgeClient;
//import org.lwjgl.opengl.GL11;
//import reika.dragonapi.instantiable.rendering.TessellatorVertexList;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
//import reika.rotarycraft.blockentities.transmission.BlockEntityClutch;
//import reika.rotarycraft.models.animated.shaftonly.ModelClutch;
//import reika.rotarycraft.models.animated.shaftonly.ModelVClutch;
//
//public class RenderClutch extends RotaryTERenderer {
//
//    private ModelClutch ClutchModel = new ModelClutch();
//    private ModelVClutch ClutchModelV = new ModelVClutch();
//
//    @Override
//    protected String getTextureSubfolder() {
//        return "Transmission/shaft";
//    }
//
//    public void renderBlockEntityClutchAt(PoseStack stack, BlockEntityClutch tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        ModelClutch var14;
//        ModelVClutch var15;
//
//        var14 = ClutchModel;
//        var15 = ClutchModelV;
//        this.bindTextureByName("/reika/rotarycraft/textures/blockentitytex/transmission/shaft/shaft/shafttex.png");
//
//        this.setupGL(tile, par2, par4, par6);
//
//        int var11 = 0;     //used to rotate the model about metadata
//
//        if (tile.isInWorld()) {
//
//            switch (tile.getBlockState().getValue(getFacingProperty())) {
//                case 0:
//                    var11 = 0;
//                    break;
//                case 1:
//                    var11 = 180;
//                    break;
//                case 2:
//                    var11 = 90;
//                    break;
//                case 3:
//                    var11 = 270;
//                    break;
//                case 4:
//                case 5:
//                    var11 = 0;
//                    break;
//            }
//
//            stack.mulPose(new Quaternion(var11, 0.0F, 1.0F, 0.0F));
//
//        }
//        if (tile.getBlockMetadata() < 4)
//            var14.renderAll(stack, tile, null, -tile.phi);
//        else {
//            var15.renderAll(stack, tile, null, tile.getBlockMetadata() == 5 ? tile.phi : -tile.phi, 0);
//        }
//
//        this.closeGL(stack, tile);
//    }
//
//    @Override
//    public void render(BlockEntity tile, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
//       /*todo if (this.doRenderModel((RotaryCraftBlockEntity) tile))
//            this.renderBlockEntityClutchAt(poseStack, (BlockEntityClutch) tile, par2, par4, par6, par8);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1) {
//            IORenderer.renderIO(tile, par2, par4, par6);
//            this.renderConnection((BlockEntityClutch) tile, par2, par4, par6, par8);
//        }*/
//    }
//
//    private void renderConnection(PoseStack stack, BlockEntityClutch tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int c = tile.isOutputEnabled() ? 0xff0000 : 0x900000;
//        int c2 = tile.isOutputEnabled() ? 0xffa7a7 : 0xda0000;
//        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
//        stack.pushPose();
//        stack.translate(par2, par4, par6);
//        if (tile.isOutputEnabled())
//            GL11.glDisable(GL11.GL_LIGHTING);
//        RenderSystem.enableBlend();
//        GL11.glDisable(GL11.GL_TEXTURE_2D);
//
//        boolean vert = tile.getBlockMetadata() >= 4;
//        double h = vert ? 0.6 : 0.35;
//        double h2 = vert ? 1 - h : h - 0.125;
//        if (tile.getBlockMetadata() < 4 && tile.isFlipped) {
//            h = 1 - h;
//            h2 = 1 - h2;
//        }
//        double w = 0.225;
//
//        TessellatorVertexList tv5 = new TessellatorVertexList();
//        Tesselator tess = Tesselator.getInstance();
//        BufferBuilder v5 = tess.getBuilder();
//
//        tv5.vertex(0.5 - w, h, 0.5 + w);
//        tv5.vertex(0.5 + w, h, 0.5 + w);
//        tv5.vertex(0.5 + w, h, 0.5 - w);
//        tv5.vertex(0.5 - w, h, 0.5 - w);
//
//        tv5.vertex(0.5 + w, h2, 0.5 - w);
//        tv5.vertex(0.5 - w, h2, 0.5 - w);
//        tv5.vertex(0.5 - w, h, 0.5 - w);
//        tv5.vertex(0.5 + w, h, 0.5 - w);
//
//        tv5.vertex(0.5 + w, h, 0.5 + w);
//        tv5.vertex(0.5 - w, h, 0.5 + w);
//        tv5.vertex(0.5 - w, h2, 0.5 + w);
//        tv5.vertex(0.5 + w, h2, 0.5 + w);
//
//        tv5.vertex(0.5 - w, h, 0.5 + w);
//        tv5.vertex(0.5 - w, h, 0.5 - w);
//        tv5.vertex(0.5 - w, h2, 0.5 - w);
//        tv5.vertex(0.5 - w, h2, 0.5 + w);
//
//        tv5.vertex(0.5 + w, h2, 0.5 + w);
//        tv5.vertex(0.5 + w, h2, 0.5 - w);
//        tv5.vertex(0.5 + w, h, 0.5 - w);
//        tv5.vertex(0.5 + w, h, 0.5 + w);
//
//        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//        v5.setColorRGBA_I(c, 240);
//        v5.setBrightness(240);
//        if (tile.isFlipped)
//            tv5.reverse();
//        tv5.render();
//        v5.end();
//
//        v5.startDrawing(GL11.GL_LINE_LOOP);
//        v5.setBrightness(240);
//        v5.setColorRGBA_I(c2, 240);
//        v5.vertex(0.5 - w, h, 0.5 + w);
//        v5.vertex(0.5 + w, h, 0.5 + w);
//        v5.vertex(0.5 + w, h, 0.5 - w);
//        v5.vertex(0.5 - w, h, 0.5 - w);
//        v5.end();
//
//        v5.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);
//        v5.setBrightness(240);
//        v5.setColorRGBA_I(c2, 240);
//        v5.vertex(0.5 - w, h, 0.5 + w);
//        v5.vertex(0.5 - w, h2, 0.5 + w);
//
//        v5.vertex(0.5 + w, h, 0.5 + w);
//        v5.vertex(0.5 + w, h2, 0.5 + w);
//
//        v5.vertex(0.5 + w, h, 0.5 - w);
//        v5.vertex(0.5 + w, h2, 0.5 - w);
//
//        v5.vertex(0.5 - w, h, 0.5 - w);
//        v5.vertex(0.5 - w, h2, 0.5 - w);
//        v5.end();
//
//        stack.popPose();
//        //GL11.glPopAttrib();
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        return "shafttex.png";
//    }
//}
