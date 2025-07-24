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
//import net.neoforged.client.MinecraftForgeClient;
//import net.neoforged.fluids.FluidStack;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.dragonapi.libraries.rendering.ReikaLiquidRenderer;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.models.Animated.ModelWetter;
//
//public class RenderWetter extends RotaryTERenderer {
//
//    private final ModelWetter wetterModel = new ModelWetter();
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        return "wettertex.png";
//    }
//
//    public void renderBlockEntityWetterAt(BlockEntityWetter tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int var9;
//
//        if (!tile.isInWorld())
//            var9 = 0;
//        else
//            var9 = tile.getBlockMetadata();
//
//        ModelWetter var14;
//        var14 = wetterModel;
//
//        this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/wettertex.png");
//
//        stack.pushPose();
//        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        stack.translate((float) par2, (float) par4 + 2.0F, (float) par6 + 1.0F);
//        stack.scale(1.0F, -1.0F, -1.0F);
//        stack.translate(0.5F, 0.5F, 0.5F);
//        var14.renderAll(stack, tile, null, tile.phi);
//
//        if (tile.isInWorld())
//            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//        stack.popPose();
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//    }
//
//    @Override
//    public void renderBlockEntityAt(BlockEntity tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        if (this.doRenderModel((RotaryCraftBlockEntity) tile)) {
//            this.renderBlockEntityWetterAt((BlockEntityWetter) tile, par2, par4, par6, par8);
//            this.renderItem((BlockEntityWetter) tile, par2, par4, par6, par8);
//        }
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1) {
//            IORenderer.renderIO(tile, par2, par4, par6);
//            this.renderLiquid((BlockEntityWetter) tile, par2, par4, par6, par8);
//        }
//    }
//
//    private void renderItem(BlockEntityWetter te, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        EntityItem ei = te.getItem();
//        if (ei != null) {
//            ei.age = 0;
//            ei.hoverStart = 0;
//            ei.setAngles(0, 0);
//            stack.pushPose();
//            double ang = 0;//((te.getTicksExisted()+par8)*3D)%360;
//            double dy = -0.3125;//0.0625*Math.sin(Math.toRadians(ang*2));
//            stack.translate(0.5, 0.625 + dy, 0.5);
//            stack.translate(par2, par4, par6);
//            GL11.glRotated(ang, 0, 1, 0);
//            double s = 3;
//            stack.scale(s, s, s);
//            stack.translate(-par2, -par4, -par6);
//
//            Render r = RenderManager.instance.getEntityClassRenderObject(EntityItem.class);
//            r.doRender(ei, par2, par4, par6, 0, 0);
//
//            stack.popPose();
//        }
//    }
//
//    private void renderLiquid(BlockEntityWetter te, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        if (te.getContainedFluid() != null) {
//
//            FluidStack liquid = new FluidStack(te.getContainedFluid(), 1);
//            int amount = te.getLevel();
//            if (amount == 0)
//                return;
//            if (amount > te.getCapacity())
//                amount = te.getCapacity();
//
//            int[] displayList = ReikaLiquidRenderer.getGLLists(liquid, te.level, false);
//
//            if (displayList == null) {
//                return;
//            }
//
//            stack.pushPose();
//            GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
//            GL11.glEnable(GL11.GL_CULL_FACE);
//            GL11.glDisable(GL11.GL_LIGHTING);
//            RenderSystem.enableBlend();
//            RenderSystem.defaultBlendFunc();
//
//            ReikaLiquidRenderer.bindFluidTexture(te.getContainedFluid());
//            ReikaLiquidRenderer.setFluidColor(liquid);
//
//            stack.translate(par2, par4, par6);
//
//            stack.translate(0, 0.125, 0);
//
//            stack.translate(0, 0.001, 0);
//            stack.scale(1, 0.92, 1);
//            stack.scale(0.99, 0.875, 0.99);
//
//            GL11.glCallList(displayList[(int) (amount / ((double) te.getCapacity()) * (ReikaLiquidRenderer.LEVELS - 1))]);
//
//            //GL11.glPopAttrib();
//            stack.popPose();
//
//
//			/*
//			stack.pushPose();
//			stack.translate(par2, par4, par6);
//			GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
//			GL11.glEnable(GL11.GL_CULL_FACE);
//			GL11.glDisable(GL11.GL_LIGHTING);
//			RenderSystem.enableBlend();
//			RenderSystem.defaultBlendFunc();
//
//			Tesselator tess = Tesselator.getInstance();
//BufferBuilder v5 = tess.getBuilder();
//			IIcon ico = te.getContainedFluid().getStillIcon();
//			float u = ico.getMinU();
//			float v = ico.getMinV();
//			float du = ico.getMaxU();
//			float dv = ico.getMaxV();
//			double h = te.getLevel()/(double)te.getCapacity();
//			GL11.glColor4f(1, 1, 1, 1);
//			ReikaTextureHelper.bindTerrainTexture();
//			v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//			v5.setColorOpaque_I(0xffffff);
//			int b = te.getContainedFluid().getLuminosity() >= 12 ? 240 : te.getBlockType().getMixedBrightnessForBlock(te.level, te.xCoord, te.yCoord, te.zCoord);
//			v5.setBrightness(b);
//			v5.addVertexWithUV(0, h, 1, u, dv);
//			v5.addVertexWithUV(1, h, 1, du, dv);
//			v5.addVertexWithUV(1, h, 0, du, v);
//			v5.addVertexWithUV(0, h, 0, u, v);
//			v5.end();
//
//			//GL11.glPopAttrib();
//			stack.popPose();*/
//        }
//    }
//
//}
