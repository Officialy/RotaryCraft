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
//import reika.rotarycraft.blockentities.Weaponry.Turret.BlockEntityFreezeGun;
//import net.minecraft.BlockEntity.BlockEntity;
//import net.neoforged.client.MinecraftForgeClient;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.models.Turret.ModelFreezeGun;
//
//public class RenderFreezeGun extends RotaryTERenderer {
//
//    private ModelFreezeGun freezegunModel = new ModelFreezeGun();
//
//    /**
//     * Renders the BlockEntity for the position.
//     */
//    public void renderBlockEntityFreezeGunAt(BlockEntityFreezeGun tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int var9;
//
//        if (!tile.isInWorld())
//            var9 = 0;
//        else
//            var9 = tile.getBlockMetadata();
//
//        ModelFreezeGun var14;
//        var14 = freezegunModel;
//
//        this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/freezeguntex.png");
//
//        stack.pushPose();
//        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        stack.translate((float) par2, (float) par4 + 2.0F, (float) par6 + 1.0F);
//        stack.scale(1.0F, -1.0F, -1.0F);
//        stack.translate(0.5F, 0.5F, 0.5F);
//        int var11 = 1;     //used to rotate the model about metadata
//        int var12 = 0;
//        if (tile.isInWorld()) {
//            if (tile.getBlockMetadata() == 1) {
//                var11 = -1;
//                var12 = 2;
//                GL11.glFrontFace(GL11.GL_CW);
//            }
//        }
//        stack.translate(0, var12, 0);
//        stack.scale(1, var11, 1);
//        int a = tile.getBlockMetadata() == 0 ? -1 : 1;
//        var14.renderAll(stack, tile, null, -tile.phi, a * tile.theta);
//        stack.scale(1, var11, 1);
//        stack.translate(0, -var12, 0);
//        GL11.glFrontFace(GL11.GL_CCW);
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
//            this.renderBlockEntityFreezeGunAt((BlockEntityFreezeGun) tile, par2, par4, par6, par8);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1) {
//            IORenderer.renderIO(tile, par2, par4, par6);
//            //this.renderIce((BlockEntityFreezeGun)tile, par2, par4, par6);
//        }
//    }
//
//	/*
//
//	private void renderIce(BlockEntityFreezeGun tile, double par2, double par4, double par6) {
//		if (tile == null)
//			return;
//		if (!tile.isInWorld())
//			return;
//		if (tile.frozen == null)
//			return;
//		//ReikaJavaLibrary.pConsole(tile.frozen.size());
//		ReikaRenderHelper.prepareGeoDraw(true);
//		Tesselator v5 = new Tesselator();
//		int[] rgb = {255,255,255};
//		for (int i = 0; i < tile.frozen.size(); i++) {
//			LivingEntity e = tile.frozen.get(i);
//			ReikaJavaLibrary.pConsole(e.getCommandSenderName());
//			double dx = e.posX-tile.xCoord;
//			double dy = e.posY-tile.yCoord;
//			double dz = e.posZ-tile.zCoord;
//			v5.startDrawing(GL11.GL_LINE_LOOP);
//			v5.color(rgb[0], rgb[1], rgb[2], 255);
//			v5.vertex(par2+dx, par4+dy, par6+dz);
//			v5.vertex(dx, dy+10, dz);
//			v5.vertex(dx+10, dy+10, dz);
//			v5.vertex(dx+10, dy, dz);
//			v5.end();
//		}
//		ReikaRenderHelper.exitGeoDraw();
//	}*/
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        return "freezeguntex.png";
//    }
//}
