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
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.blockentities.Weaponry.BlockEntityEMP;
//import net.minecraft.BlockEntity.BlockEntity;
//import net.minecraft.client.renderer.Tesselator;
//import net.minecraftforge.client.MinecraftForgeClient;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.dragonapi.libraries.rendering.ReikaColorAPI;
//import reika.dragonapi.libraries.rendering.ReikaRenderHelper;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.models.ModelEMP;
//
//public class RenderEMP extends RotaryTERenderer {
//
//    private ModelEMP EMPModel = new ModelEMP();
//    //private ModelEMPV EMPModelV = new ModelEMPV();
//
//    /**
//     * Renders the BlockEntity for the position.
//     */
//    public void renderBlockEntityEMPAt(BlockEntityEMP tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int var9;
//
//        if (!tile.isInWorld())
//            var9 = 0;
//        else
//            var9 = tile.getBlockMetadata();
//
//        ModelEMP var14;
//        var14 = EMPModel;
//        //ModelEMPV var15;
//        //var14 = this.EMPModelV;
//        if (tile.isLoading())
//            this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/emptex2.png");
//        else
//            this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/emptex.png");
//
//        stack.pushPose();
//        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        stack.translate((float) par2, (float) par4 + 2.0F, (float) par6 + 1.0F);
//        stack.scale(1.0F, -1.0F, -1.0F);
//        stack.translate(0.5F, 0.5F, 0.5F);
//        if (!tile.isInWorld()) {
//            stack.scale(1.125, 1.125, 1.125);
//            stack.translate(0, -0.25F, 0);
//            stack.mulPose(new Quaternion(-90, 0, 1, 0);
//        }
//        int var11 = 0;     //used to rotate the model about metadata
//
//
//        //float var12 = tile.prevLidAngle + (tile.lidAngle - tile.prevLidAngle) * par8;
//        float var13;/*
//
//	            var12 = 1.0F - var12;
//	            var12 = 1.0F - var12 * var12 * var12;*/
//        // if (tile.getBlockMetadata() < 4)
//
//        var14.renderAll(stack, tile, null);
//        if (tile.isInWorld())
//            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//        stack.popPose();
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//    }
//
//    protected void renderEffect(BlockEntityEMP te, double x, double y, double z, float ptick) {
//        double r = te.effectRender.getRadius(ptick);
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
//        ReikaTextureHelper.bindTexture(RotaryCraft.class, "Textures/emp1.png");
//        this.renderSphere(te, x, y, z, r, te.effectRender.getColor2());
//        ReikaTextureHelper.bindTexture(RotaryCraft.class, "Textures/emp2.png");
//        this.renderSphere(te, x, y, z, r, te.effectRender.getColor1());
//
//        //GL11.glPopAttrib();
//    }
//
//    private void renderSphere(BlockEntityEMP te, double x, double y, double z, double r, int color) {
//        Tesselator var5 = Tesselator.instance;
//        var5.startDrawingQuads();
//        color = ReikaColorAPI.getModifiedSat(color, 1.5F);
//        var5.setColorOpaque_I(color);
//        double dk = 0.5 * r / 4;
//        double di = 20;
//        for (double k = -r; k <= r; k += dk) {
//            double r2 = Math.sqrt(r * r - k * k);
//            double r3 = Math.sqrt(r * r - (k + dk) * (k + dk));
//            for (int i = 0; i < 360; i += di) {
//                double a = Math.toRadians(i);
//                double a2 = Math.toRadians(i + di);
//                double ti = /*i*/0 + (System.currentTimeMillis() / 2000D % 360);
//                double tk = /*k/(r*2)*/0 + (System.currentTimeMillis() / 3000D % 360);
//                double u = ti;//ti/360D;
//                double du = ti + 1;//(ti+di)/360D;
//                double v = tk;//tk/r;
//                double dv = tk + 1;//(tk+dk)/r;
//
//                double s1 = Math.sin(a);
//                double s2 = Math.sin(a2);
//                double c1 = Math.cos(a);
//                double c2 = Math.cos(a2);
//                var5.addVertexWithUV(x + r2 * c1, y + k, z + r2 * s1, u, v);
//                var5.addVertexWithUV(x + r2 * c2, y + k, z + r2 * s2, du, v);
//                var5.addVertexWithUV(x + r3 * c2, y + k + dk, z + r3 * s2, du, dv);
//                var5.addVertexWithUV(x + r3 * c1, y + k + dk, z + r3 * s1, u, dv);
//            }
//        }
//        var5.end();
//    }
//
//    @Override
//    public void renderBlockEntityAt(BlockEntity tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        if (this.doRenderModel((RotaryCraftBlockEntity) tile))
//            this.renderBlockEntityEMPAt((BlockEntityEMP) tile, par2, par4, par6, par8);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1) {
//            IORenderer.renderIO(tile, par2, par4, par6);
//            if (((BlockEntityEMP) tile).effectRender != null)
//                this.renderEffect((BlockEntityEMP) tile, par2 + 0.5, par4 + 0.5, par6 + 0.5, par8);
//            if (((BlockEntityEMP) tile).isLoading())
//                this.renderCharging((BlockEntityEMP) tile, par2, par4, par6);
//        }
//    }
//
//    private void renderCharging(BlockEntityEMP te, double par2, double par4, double par6) {
//
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        return "emptex.png";
//    }
//}
