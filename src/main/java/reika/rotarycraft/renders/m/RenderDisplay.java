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
//import reika.rotarycraft.blockentities.decorative.BlockEntityDisplay;
//import net.minecraft.BlockEntity.BlockEntity;
//import net.minecraft.client.gui.Font;
//import net.minecraft.client.renderer.Tesselator;
//import net.neoforged.client.MinecraftForgeClient;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.dragonapi.libraries.rendering.ReikaRenderHelper;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.models.ModelDisplay;
//
//import java.util.List;
//
//public class RenderDisplay extends RotaryTERenderer {
//
//    private ModelDisplay displayModel = new ModelDisplay();
//
//    public void renderBlockEntityDisplayAt(BlockEntityDisplay tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int var9;
//
//        if (!tile.isInWorld())
//            var9 = 0;
//        else
//            var9 = tile.getBlockMetadata();
//
//        ModelDisplay var14;
//        var14 = displayModel;
//        this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/displaytex.png");
//
//        stack.pushPose();
//        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        stack.translate((float) par2, (float) par4 + 2.0F, (float) par6 + 1.0F);
//        stack.scale(1.0F, -1.0F, -1.0F);
//        stack.translate(0.5F, 0.5F, 0.5F);
//        int var11 = 1;     //used to rotate the model about metadata
//        switch (var9) {
//            case 0:
//                var11 = 0;
//                break;
//            case 1:
//                var11 = 180;
//                break;
//            case 2:
//                var11 = 90;
//                break;
//            case 3:
//                var11 = 270;
//                break;
//        }
//        stack.mulPose((float) var11 + 90, 0.0F, 1.0F, 0.0F);
//        var14.renderAll(stack, tile, null, -tile.phi);
//        stack.scale(1, var11, 1);
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
//            this.renderBlockEntityDisplayAt((BlockEntityDisplay) tile, par2, par4, par6, par8);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1) {
//            if (((BlockEntityDisplay) tile).canDisplay() && ((BlockEntityDisplay) tile).hasSpace()) {
//                GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
//                stack.pushPose();
//                GL11.glDisable(GL11.GL_LIGHTING);
//                RenderSystem.enableBlend();
//                ReikaRenderHelper.disableEntityLighting();
//                GL11.glDisable(GL11.GL_ALPHA_TEST);
//                GL11.glDepthMask(false);
//                GL11.glDisable(GL11.GL_TEXTURE_2D);
//                ((BlockEntityDisplay) tile).loadColorData();
//                int dir = 0;
//                int dx = 0;
//                int dz = 0;
//                switch (tile.getBlockMetadata()) {
//                    case 0:
//                        dir = 270;
//                        dx = 1;
//                        break;
//                    case 1:
//                        dir = 90;
//                        dz = 1;
//                        break;
//                    case 2:
//                        dir = 180;
//                        dz = 1;
//                        dx = 1;
//                        break;
//                    case 3:
//                        dir = 0;
//                        break;
//                }
//                stack.translate(par2, par4, par6);
//                stack.translate(dx, 0, dz);
//                stack.mulPose(new Quaternion(dir, 0, 1, 0);
//                this.renderScreen((BlockEntityDisplay) tile, par2, par4, par6);
//                this.renderText((BlockEntityDisplay) tile, par2, par4, par6);
//                stack.mulPose(new Quaternion(-dir, 0, 1, 0);
//                stack.translate(-dx, 0, -dz);
//
//                stack.popPose();
//                //GL11.glPopAttrib();
//            }
//        }
//    }
//
//    private void renderScreen(BlockEntityDisplay tile, double par2, double par4, double par6) {
//        if (tile == null)
//            return;
//        stack.translate(0, 0, 0.495);
//        Tesselator tess = Tesselator.getInstance();
//BufferBuilder v5 = tess.getBuilder();
//        int r = tile.getRed();
//        int g = tile.getGreen();
//        int b = tile.getBlue();
//        int br = tile.getBorderRed();
//        int bg = tile.getBorderGreen();
//        int bb = tile.getBorderBlue();
//        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//        v5.color(r, g, b, 96);
//        v5.vertex(-2, 1, 0);
//        v5.vertex(3, 1, 0);
//        v5.vertex(3, 4, 0);
//        v5.vertex(-2, 4, 0);
//        v5.end();
//
//        double dd = 0.03125;
//        double dx = dd;
//        double dy = dd;
//        double dz = 0;
//
//        stack.translate(0, 0, 0.0005);
//
//        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//        v5.color(br, bg, bb, 255);
//        v5.vertex(-2, 4 - dy, 0);
//        v5.vertex(3, 4 - dy, 0);
//        v5.vertex(3, 4, 0);
//        v5.vertex(-2, 4, 0);
//        v5.end();
//
//        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//        v5.color(br, bg, bb, 255);
//        v5.vertex(-2, 1, 0);
//        v5.vertex(3, 1, 0);
//        v5.vertex(3, 1 + dy, 0);
//        v5.vertex(-2, 1 + dy, 0);
//        v5.end();
//
//        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//        v5.color(br, bg, bb, 255);
//        v5.vertex(3 - dx, 4, 0);
//        v5.vertex(3 - dx, 1, 0);
//        v5.vertex(3, 1, 0);
//        v5.vertex(3, 4, 0);
//        v5.end();
//
//        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//        v5.color(br, bg, bb, 255);
//        v5.vertex(-2, 4, 0);
//        v5.vertex(-2, 1, 0);
//        v5.vertex(-2 + dx, 1, 0);
//        v5.vertex(-2 + dx, 4, 0);
//        v5.end();
//
//        v5.startDrawing(GL11.GL_LINES);
//        v5.color(br, bg, bb, 32);
//        float vspacing = 0.0625F;
//        float hspacing = 0.25F;
//        for (float k = 1 + vspacing; k < 4; k += vspacing) {
//            v5.vertex(-2, k, 0);
//            v5.vertex(3, k, 0);
//        }
//        for (float k = -2 + hspacing; k < 3; k += hspacing) {
//            v5.vertex(k, 4, 0);
//            v5.vertex(k, 1, 0);
//        }
//        v5.end();
//
//        stack.translate(0, 0, -0.0005);
//
//        stack.translate(0, 0, -0.495);
//    }
//
//    private void renderText(BlockEntityDisplay tile, double par2, double par4, double par6) {
//        if (tile == null)
//            return;
//        if (!tile.hasList())
//            return;
//        GL11.glEnable(GL11.GL_TEXTURE_2D);
//        Font f = this.getFontRenderer();
//        stack.scale(1, -1, 1);
//        double sc = 0.02;
//        stack.scale(sc, sc, sc);
//        stack.translate(0, -50, 25);
//        stack.translate(0, -tile.displayHeight * tile.lineHeight, 0);
//        int dd = 100 - tile.charWidth / 4;
//        int dx = -dd;
//        int dz = 0;
//        stack.translate(dx, 0, dz);
//
//        List<String> cache = tile.getMessageForDisplay();
//
//        long core = tile.getTick();//System.currentTimeMillis();
//        float scroll = cache.size() > tile.displayHeight ? (core * 4) % (180 * cache.size()) / 180F : 0;
//        int linescroll = scroll - (int) scroll > 0.5F ? (int) scroll + 1 : (int) scroll;//tile.getRoundedScroll();
//        //ReikaJavaLibrary.pConsole(tile.getMessageLine(0));
//        int len = Math.min(cache.size() - 1, tile.displayHeight + linescroll - 1);
//        for (int i = linescroll; i < len + 1; i++) {
//            String s2 = cache.get(i);
//            //ReikaJavaLibrary.pConsole("Printing line "+i+" of "+tile.getMessageLength()+": "+tile.getMessageLine(i));
//            //f.draw(tile.getMessageLine(i), 1, -1+(int)((i-scroll)*tile.lineHeight), tile.getTextColor());
//            //f.drawSplitString(s, 1, -1+(int)((i-scroll)*tile.lineHeight), tile.displayWidth*f.FONT_HEIGHT, tile.getTextColor());
//            f.draw(s2, 1, -1 + (int) ((i - scroll) * tile.lineHeight), tile.getTextColor());
//            stack.translate(0, 0, -0.2875);
//            //f.draw(tile.getMessageLine(i), 1, -1+(int)((i-scroll)*tile.lineHeight), tile.getTextColor());
//            f.draw(s2, 1, -1 + (int) ((i - scroll) * tile.lineHeight), tile.getTextColor());
//            stack.translate(0, 0, 0.2875);
//
//        }
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        return "displaytex.png";
//    }
//}
