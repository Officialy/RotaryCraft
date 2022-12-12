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
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//
//import net.minecraft.BlockEntity.BlockEntity;
//import net.minecraftforge.client.MinecraftForgeClient;
//
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.models.Animated.ModelMultiClutch;
//import reika.rotarycraft.tileentities.transmission.BlockEntityMultiClutch;
//
//public class RenderMultiClutch extends RotaryTERenderer
//{
//
//	private ModelMultiClutch MultiClutchModel = new ModelMultiClutch();
//
//	@Override
//	protected String getTextureSubfolder() {
//		return "Transmission/";
//	}
//
//	/**
//	 * Renders the BlockEntity for the position.
//	 */
//	public void renderBlockEntityMultiClutchAt(BlockEntityMultiClutch tile, PoseStack stack, MultiBufferSource bufferSource, int light)
//	{
//		ModelMultiClutch var14;
//		var14 = MultiClutchModel;
//
//		this.bindTextureByName("/reika/rotarycraft/textures/blockentitytex/transmission/shaft/multiclutchtex.png");
//
//		stack.pushPose();
//		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//		stack.translate((float)par2, (float)par4 + 2.0F, (float)par6 + 1.0F);
//		stack.scale(1.0F, -1.0F, -1.0F);
//		stack.translate(0.5F, 0.5F, 0.5F);
//		int var11 = 0;
//		float var13;
//
//		var14.renderAll(stack, tile, null, tile.phi);
//
//		if (tile.isInWorld())
//			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//		stack.popPose();
//		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//
//	}
//
//	@Override
//	public void renderBlockEntityAt(BlockEntity tile, PoseStack stack, MultiBufferSource bufferSource, int light)
//	{
//		if (this.doRenderModel((RotaryCraftBlockEntity)tile))
//			this.renderBlockEntityMultiClutchAt((BlockEntityMultiClutch)tile, par2, par4, par6, par8);
//		if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1) {
//			IORenderer.renderIO(tile, par2, par4, par6);
//			this.renderFaceColors((BlockEntityMultiClutch)tile, par2, par4, par6);
//		}
//	}
//
//	@Override
//	public String getImageFileName(RenderFetcher te) {
//		return "multiclutchtex.png";
//	}
//}
