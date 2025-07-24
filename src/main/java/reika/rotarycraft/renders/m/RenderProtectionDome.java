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
//import reika.dragonapi.libraries.io.ReikaTextureHelper;
//import reika.dragonapi.libraries.java.ReikaGLHelper.BlendMode;
//import reika.rotarycraft.base.BlockEntity.BlockEntityProtectionDome;
//import reika.rotarycraft.RotaryCraft;
//import com.mojang.blaze3d.shaders.BlendMode;
//import com.mojang.blaze3d.vertex.BufferBuilder;
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.blaze3d.vertex.Tesselator;
//import net.minecraft.BlockEntity.BlockEntity;
//import net.minecraft.client.renderer.Tesselator;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.neoforged.client.MinecraftForgeClient;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.dragonapi.libraries.io.ReikaTextureHelper;
//import reika.dragonapi.libraries.rendering.ReikaRenderHelper;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.BlockEntityProtectionDome;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.models.ModelDomeEmitter;
//
//public class RenderProtectionDome extends RotaryTERenderer {
//
//    protected final ModelDomeEmitter model = new ModelDomeEmitter();
//
//    public void renderBlockEntityProtectionDomeAt(PoseStack stack, BlockEntityProtectionDome tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int var9;
//
//        if (!tile.isInWorld())
//            var9 = 0;
//        else
//            var9 = tile.getBlockMetadata();
//
//        ModelDomeEmitter var14;
//        var14 = model;
//
//        this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/" + this.getImageFileName(tile));
//
//        stack.pushPose();
//        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        stack.translate((float) par2, (float) par4 + 2.0F, (float) par6 + 1.0F);
//        stack.scale(1.0F, -1.0F, -1.0F);
//        stack.translate(0.5F, 0.5F, 0.5F);
//        int var11 = 0;     //used to rotate the model about metadata
//
//        float var13;
//
//        var14.renderAll(stack, tile, null);
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
//            this.renderBlockEntityProtectionDomeAt((BlockEntityProtectionDome) tile, par2, par4, par6, par8);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1) {
//            IORenderer.renderIO(tile, par2, par4, par6);
//            this.renderField(((BlockEntityProtectionDome) tile), par2 + 0.5, par4 + 0.5, par6 + 0.5);
//        }
//    }
//
//    protected void renderField(BlockEntityProtectionDome te, double x, double y, double z) {
//        if (!te.isInWorld())
//            return;
//        if (te.getRange() <= 0)
//            return;
//        int color = te.getDomeColor();
//        int r = te.getRange();
//
//        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
//        GL11.glEnable(GL11.GL_TEXTURE_2D);
//        GL11.glDisable(GL11.GL_CULL_FACE);
//        GL11.glDisable(GL11.GL_LIGHTING);
//        RenderSystem.enableBlend();
//        ReikaRenderHelper.disableEntityLighting();
//        BlendMode.ADDITIVEDARK.apply();
//        GL11.glDepthMask(false);
//
//        ReikaTextureHelper.bindTexture(RotaryCraft.class, "Textures/forcefield.png");
//        Tesselator tess = Tesselator.getInstance();
//        BufferBuilder var5 = tess.getBuilder();
//        var5.startDrawingQuads();
//        var5.setColorRGBA_I(color, color >> 24 & 255);
//        double dk = 0.5 * r / 16;
//        double di = 10;
//        for (double k = -r; k <= r; k += dk) {
//            double r2 = Math.sqrt(r * r - k * k);
//            double r3 = Math.sqrt(r * r - (k + dk) * (k + dk));
//            for (int i = 0; i < 360; i += di) {
//                double a = Math.toRadians(i);
//                double a2 = Math.toRadians(i + di);
//                double ti = i + (System.currentTimeMillis() / 50D % 360);
//                double tk = k + (System.currentTimeMillis() / 220D % 360);
//                double u = ti / 360D;
//                double du = (ti + di) / 360D;
//                double v = tk / r;
//                double dv = (tk + dk) / r;
//                double s1 = Math.sin(a);
//                double s2 = Math.sin(a2);
//                double c1 = Math.cos(a);
//                double c2 = Math.cos(a2);
//                var5.vertex(x + r2 * c1, y + k, z + r2 * s1).uv((float) u, (float) v);
//                var5.vertex(x + r2 * c2, y + k, z + r2 * s2).uv((float) du, (float) v);
//                var5.vertex(x + r3 * c2, y + k + dk, z + r3 * s2).uv((float) du, (float) dv);
//                var5.vertex(x + r3 * c1, y + k + dk, z + r3 * s1).uv((float) u, (float) dv);
//            }
//        }
//        var5.end();
//
//        var5.startDrawing(GL11.GL_TRIANGLE_FAN);
//        var5.setColorRGBA_I(color, color >> 24 & 255);
//        var5.vertex(x, y + 0.5, z).uv(0.5, 0.5);
//        double dr = 2;
//        for (int i = 0; i < 360; i += 10) {
//            double a = Math.toRadians(i);
//            double a2 = a + Math.toRadians(System.currentTimeMillis() / 20D % 360);
//            double dx = Math.cos(a);
//            double dz = Math.sin(a);
//            double ux = (System.currentTimeMillis() / 3100D) % 10;
//            double uy = (System.currentTimeMillis() / 4700D) % 10;
//            double u = Math.cos(a2) + ux;
//            double v = Math.sin(a2) + uy;
//            u = u * 0.25;
//            v = v * 0.25;
//            var5.vertex(x + dx * dr, y + r - 0.25, z + dz * dr).uv(u, v);
//        }
//        var5.end();
//
//        //GL11.glPopAttrib();
//
//		/*
//		ReikaRenderHelper.prepareGeoDraw(false);
//		for (double k = -r; k <= r; k += 0.5*r/8)
//			ReikaRenderHelper.renderCircle(Math.sqrt(r*r-k*k), x, y+k, z, color, 15);
//		for (int k = 0; k < 360; k += 15)
//			ReikaRenderHelper.renderVCircle(r, x, y, z, color, k, 15);
//
//		double ang = 7;
//		ReikaRenderHelper.renderLine(x, y, z, x, y+r, z, color);
//		for (int k = 0; k < 360; k += 15) {
//			ReikaRenderHelper.renderVCircle(0.125, x, y+0.5, z, color, (System.nanoTime()/7500000)%360+k, 15);
//			ReikaRenderHelper.renderLine(x, y, z, x+r*Math.sin(Math.toRadians(ang)*Math.cos(Math.toRadians(k))), y+r-Math.sin(Math.toRadians(ang)), z+r*Math.sin(Math.toRadians(ang)*Math.sin(Math.toRadians(k))), color);
//		}
//		ReikaRenderHelper.exitGeoDraw();
//		 */
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        return "containtex.png";
//    }
//}
