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
//import reika.dragonapi.libraries.java.ReikaJavaLibrary;
//import reika.rotarycraft.blockentities.Production.BlockEntityLavaMaker;
//import net.minecraft.BlockEntity.BlockEntity;
//import net.minecraft.client.renderer.Tesselator;
//
//import net.neoforged.client.MinecraftForgeClient;
//import net.neoforged.fluids.Fluid;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.dragonapi.libraries.rendering.ReikaLiquidRenderer;
//import reika.dragonapi.libraries.rendering.ReikaRenderHelper;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.models.Animated.ModelLavaMaker;
//
//public class RenderRockMelter extends RotaryTERenderer {
//
//    private ModelLavaMaker LavaMakerModel = new ModelLavaMaker();
//
//    public void renderBlockEntityLavaMakerAt(BlockEntityLavaMaker tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int var9;
//
//        if (!tile.isInWorld())
//            var9 = 0;
//        else
//            var9 = tile.getBlockMetadata();
//
//        ModelLavaMaker var14;
//        var14 = LavaMakerModel;
//
//        this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/lavamakertex.png");
//
//        stack.pushPose();
//        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        stack.translate((float) par2, (float) par4 + 2.0F, (float) par6 + 1.0F);
//        stack.scale(1.0F, -1.0F, -1.0F);
//        if (tile.isInWorld() && MinecraftForgeClient.getRenderPass() == 1)
//            RenderSystem.enableBlend();
//        stack.translate(0.5F, 0.5F, 0.5F);
//        int var11 = 0;     //used to rotate the model about metadata
//
//        if (tile.isInWorld()) {
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
//        var14.renderAll(stack, tile, ReikaJavaLibrary.makeListFrom(tile.hasStone()), -tile.phi, 0);
//
//        if (tile.isInWorld())
//            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//        RenderSystem.disableBlend();
//        stack.popPose();
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//    }
//
//    @Override
//    public void renderBlockEntityAt(BlockEntity tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        if (this.doRenderModel((RotaryCraftBlockEntity) tile))
//            this.renderBlockEntityLavaMakerAt((BlockEntityLavaMaker) tile, par2, par4, par6, par8);
//
//        if (MinecraftForgeClient.getRenderPass() == 0) {
//            this.renderLiquid(tile, par2, par4, par6);
//        }
//
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1) {
//            IORenderer.renderIO(tile, par2, par4, par6);
//        }
//    }
//
//    private void renderLiquid(BlockEntity tile, double par2, double par4, double par6) {
//        stack.translate(par2, par4, par6);
//        BlockEntityLavaMaker tr = (BlockEntityLavaMaker) tile;
//        if (!tr.isEmpty() && tr.isInWorld()) {
//            Fluid f = tr.getContainedFluid();
//            ReikaLiquidRenderer.bindFluidTexture(f);
//            IIcon ico = ReikaLiquidRenderer.getFluidIconSafe(f);
//            float u = ico.getMinU();
//            float v = ico.getMinV();
//            float du = ico.getMaxU();
//            float dv = ico.getMaxV();
//            double h = 0.1875 + 12D / 16D * tr.getLevel() / tr.CAPACITY;
//            Tesselator tess = Tesselator.getInstance();
//BufferBuilder v5 = tess.getBuilder();
//            ReikaRenderHelper.disableLighting();
//            v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//            v5.normal(0, 1, 0);
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
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        return "lavamakertex.png";
//    }
//}
