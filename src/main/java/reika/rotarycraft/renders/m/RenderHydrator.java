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
//import reika.rotarycraft.blockentities.Farming.BlockEntityGroundHydrator;
//import net.minecraft.BlockEntity.BlockEntity;
//import net.minecraft.block.Block;
//import net.minecraft.client.renderer.Tesselator;
//
//import net.neoforged.client.MinecraftForgeClient;
//import net.neoforged.common.util.Direction;
//import net.neoforged.fluids.Fluid;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.dragonapi.libraries.rendering.ReikaLiquidRenderer;
//import reika.dragonapi.libraries.rendering.ReikaRenderHelper;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.models.ModelReservoir;
//
//public class RenderHydrator extends RotaryTERenderer {
//
//    private ModelReservoir ReservoirModel = new ModelReservoir();
//
//    public void renderBlockEntityGroundHydratorAt(BlockEntityGroundHydrator tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int var9;
//
//        if (!tile.isInWorld())
//            var9 = 0;
//        else
//            var9 = tile.getBlockMetadata();
//
//        ModelReservoir var14;
//        var14 = ReservoirModel;
//
//        this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/hydratortex.png");
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
//                    var11 = 270;
//                    break;
//                case 3:
//                    var11 = 90;
//                    break;
//            }
//
//            if (tile.getBlockMetadata() <= 3)
//                stack.mulPose((float) var11 + 90, 0.0F, 1.0F, 0.0F);
//            else {
//                stack.mulPose(new Quaternion(var11, 1F, 0F, 0.0F);
//                stack.translate(0F, -1F, 1F);
//                if (tile.getBlockMetadata() == 5)
//                    stack.translate(0F, 0F, -2F);
//            }
//        }
//
//        float var13;
//
//        if (tile.isInWorld()) {
//            for (int i = 2; i < 6; i++) {
//                var14.renderSide(tile, dirs[i]);
//            }
//            var14.renderSide(tile, Direction.DOWN);
//        } else {
//            var14.renderAll(stack, tile, null);
//        }
//
//        if (tile.isInWorld())
//            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//        stack.popPose();
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//    }
//
//    @Override
//    public void renderBlockEntityAt(BlockEntity tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
//        BlockEntityGroundHydrator tr = (BlockEntityGroundHydrator) tile;
//        if (this.doRenderModel(tr)) {
//            this.renderBlockEntityGroundHydratorAt(tr, par2, par4, par6, par8);
//            RenderSystem.enableBlend();
//            RenderSystem.defaultBlendFunc();
//            this.renderCover(tr, par2, par4, par6);
//        }
//
//        RenderSystem.enableBlend();
//        RenderSystem.defaultBlendFunc();
//        if (MinecraftForgeClient.getRenderPass() == 1) {
//            this.renderLiquid(tr, par2, par4, par6);
//            if (tr.getLevel() > 0)
//                this.renderOverlay(tr, par2, par4, par6);
//        }
//        //GL11.glPopAttrib();
//    }
//
//    private void renderOverlay(BlockEntityGroundHydrator te, double par2, double par4, double par6) {
//        stack.translate(par2, par4, par6);
//        int r = te.getRange();
//        Tesselator tess = Tesselator.getInstance();
//BufferBuilder v5 = tess.getBuilder();
//        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//        v5.normal(0, 1, 0);
//        ReikaTextureHelper.bindTerrainTexture();
//        IIcon ico = RotaryCraft.hydratorOverlay;
//        float u = ico.getMinU();
//        float v = ico.getMinV();
//        float du = ico.getMaxU();
//        float dv = ico.getMaxV();
//        for (int i = -r; i <= r; i++) {
//            for (int k = -r; k <= r; k++) {
//                Block b = te.level.getBlock(te.xCoord + i, te.yCoord, te.zCoord + k);
//                int meta = te.level.getBlockMetadata(te.xCoord + i, te.yCoord, te.zCoord + k);
//                if (te.affectsBlock(b, meta)) {
//                    int a = 192 - 16 * (Math.abs(i) + Math.abs(k));
//                    v5.setColorRGBA_I(0xffffff, a);
//                    double h = b.getBlockBoundsMaxY() + 0.005;
//                    v5.addVertexWithUV(i + 0, h, k + 1, u, dv);
//                    v5.addVertexWithUV(i + 1, h, k + 1, du, dv);
//                    v5.addVertexWithUV(i + 1, h, k + 0, du, v);
//                    v5.addVertexWithUV(i + 0, h, k + 0, u, v);
//                }
//            }
//        }
//        v5.end();
//        stack.translate(-par2, -par4, -par6);
//    }
//
//    private void renderCover(BlockEntityGroundHydrator tr, double par2, double par4, double par6) {
//        stack.translate(par2, par4, par6);
//        ReikaTextureHelper.bindTerrainTexture();
//        IIcon ico = RotaryCraft.woodLattice;
//        float u = ico.getMinU();
//        float v = ico.getMinV();
//        float du = ico.getMaxU();
//        float dv = ico.getMaxV();
//        float h = 0.99F;
//        float dd = 0;//.03125F;
//        Tesselator tess = Tesselator.getInstance();
//BufferBuilder v5 = tess.getBuilder();
//        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//        v5.normal(0, 1, 0);
//        v5.addVertexWithUV(dd, h, 1 - dd, u, dv);
//        v5.addVertexWithUV(1 - dd, h, 1 - dd, du, dv);
//        v5.addVertexWithUV(1 - dd, h, dd, du, v);
//        v5.addVertexWithUV(dd, h, dd, u, v);
//        v5.end();
//        stack.translate(-par2, -par4, -par6);
//    }
//
//    private void renderLiquid(BlockEntityGroundHydrator tr, double par2, double par4, double par6) {
//        stack.translate(par2, par4, par6);
//        Fluid f = tr.getFluid();
//        if (f != null) {
//            ReikaLiquidRenderer.bindFluidTexture(f);
//            IIcon ico = ReikaLiquidRenderer.getFluidIconSafe(f);
//            float u = ico.getMinU();
//            float v = ico.getMinV();
//            float du = ico.getMaxU();
//            float dv = ico.getMaxV();
//            double h = this.getFillAmount(tr);
//            Tesselator tess = Tesselator.getInstance();
//BufferBuilder v5 = tess.getBuilder();
//            v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//            v5.normal(0, 1, 0);
//
//            v5.addVertexWithUV(0, h, 1, u, dv);
//            v5.addVertexWithUV(1, h, 1, du, dv);
//            v5.addVertexWithUV(1, h, 0, du, v);
//            v5.addVertexWithUV(0, h, 0, u, v);
//            v5.end();
//            ReikaRenderHelper.enableLighting();
//        }
//        stack.translate(-par2, -par4, -par6);
//    }
//
//    private double getFillAmount(BlockEntityGroundHydrator tr) {
//        return 0.0625 + 14D / 16D * tr.getLevel() / 1000;
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        return "hydratortex.png";
//    }
//}
