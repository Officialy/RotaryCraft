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
//import reika.dragonapi.libraries.io.ReikaTextureHelper;
//import reika.dragonapi.libraries.java.ReikaGLHelper.BlendMode;
//import reika.rotarycraft.blockentities.Production.BlockEntityObsidianMaker;
//import net.minecraft.BlockEntity.BlockEntity;
//import net.minecraft.client.renderer.Tesselator;
//
//import net.neoforged.client.MinecraftForgeClient;
//import net.neoforged.fluids.FluidRegistry;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.dragonapi.libraries.rendering.ReikaRenderHelper;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.blockentities.production.BlockEntityObsidianMaker;
//import reika.rotarycraft.models.ModelObsidian;
//
//public class RenderObsidian extends RotaryTERenderer {
//    private ModelObsidian ObsidianModel = new ModelObsidian();
//    //private ModelObsidianV ObsidianModelV = new ModelObsidianV();
//
//    /**
//     * Renders the BlockEntity for the position.
//     */
//    public void renderBlockEntityObsidianAt(BlockEntityObsidianMaker tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int var9;
//
//        if (!tile.isInWorld())
//            var9 = 0;
//        else
//            var9 = tile.getBlockMetadata();
//
//        ModelObsidian var14;
//        var14 = ObsidianModel;
//        //ModelObsidianV var15;
//        //var14 = this.ObsidianModelV;
//        this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/obsidiantex.png");
//
//        stack.pushPose();
//        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//        GL11.glColor4f(1.0F + tile.overred, 1.0F + tile.overgreen, 1.0F + tile.overblue, 1.0F);
//        stack.translate((float) par2, (float) par4 + 2.0F, (float) par6 + 1.0F);
//        stack.scale(1.0F, -1.0F, -1.0F);
//        stack.translate(0.5F, 0.5F, 0.5F);
//
//        float var13;
//
//        var14.renderAll(stack, tile, null);
//
//        if (tile.isInWorld() || MinecraftForgeClient.getRenderPass() == 1)
//            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//        RenderSystem.disableBlend();
//        stack.popPose();
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//    }
//
//    @Override
//    public void renderBlockEntityAt(BlockEntity tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        if (this.doRenderModel((RotaryCraftBlockEntity) tile))
//            this.renderBlockEntityObsidianAt((BlockEntityObsidianMaker) tile, par2, par4, par6, par8);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1) {
//            this.renderInternalTexture((BlockEntityObsidianMaker) tile, par2, par4, par6);
//            IORenderer.renderIO(tile, par2, par4, par6);
//        }
//    }
//
//    private void renderInternalTexture(BlockEntityObsidianMaker te, double par2, double par4, double par6) {
//
//        int i = 0;
//        double h = 0;
//        double maxh = 0.6875;
//        if (te.getWater() > 0)
//            i += 1;
//        if (te.getLava() > 0)
//            i += 2;
//        float u = 0;
//        float v = 0;
//        float du = 0;
//        float dv = 0;
//
//        switch (i) {
//            case 0:
//                return;
//            case 1: {
//                ReikaTextureHelper.bindTerrainTexture();
//                IIcon ico = FluidRegistry.WATER.getIcon();
//                u = ico.getMinU();
//                v = ico.getMinV();
//                du = ico.getMaxU();
//                dv = ico.getMaxV();
//                h = maxh * te.getWater() / te.CAPACITY;
//                break;
//            }
//            case 2: {
//                ReikaTextureHelper.bindTerrainTexture();
//                IIcon ico = FluidRegistry.LAVA.getIcon();
//                u = ico.getMinU();
//                v = ico.getMinV();
//                du = ico.getMaxU();
//                dv = ico.getMaxV();
//                h = maxh * te.getLava() / te.CAPACITY;
//                break;
//            }
//            case 3:
//                this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/obsidiantex.png");
//                u = 78 / 128F;
//                v = 0;
//                du = u + 14 / 128F;
//                dv = v + 14 / 128F;
//                h = maxh;
//                break;
//        }
//
//        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
//        stack.pushPose();
//        stack.translate(par2, par4, par6);
//        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//        GL11.glDisable(GL11.GL_LIGHTING);
//        if ((i & 2) != 0) {
//            ReikaRenderHelper.disableEntityLighting();
//        }
//        GL11.glColor4f(1, 1, 1, 1);
//        RenderSystem.enableBlend();
//        RenderSystem.defaultBlendFunc();
//
//        Tesselator tess = Tesselator.getInstance();
//BufferBuilder v5 = tess.getBuilder();
//
//        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//        v5.setBrightness(te.getBlockType().getMixedBrightnessForBlock(te.level, te.xCoord, te.yCoord, te.zCoord));
//        if ((i & 2) != 0)
//            v5.setBrightness(240);
//        v5.setColorOpaque_I(0xffffff);
//
//        v5.addVertexWithUV(0, h, 1, u, dv);
//        v5.addVertexWithUV(1, h, 1, du, dv);
//        v5.addVertexWithUV(1, h, 0, du, v);
//        v5.addVertexWithUV(0, h, 0, u, v);
//
//        v5.end();
//
//        stack.popPose();
//        //GL11.glPopAttrib();
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        return "obsidiantex.png";
//    }
//}
