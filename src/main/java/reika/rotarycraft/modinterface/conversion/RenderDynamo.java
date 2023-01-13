///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.modinterface.conversion;
//
//import org.lwjgl.opengl.GL11;
//
//import net.minecraft.world.level.block.entity.BlockEntity;
//
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.modinterface.model.Dynamo2Model;
//
//public class RenderDynamo extends RotaryTERenderer
//{
//
//	private Dynamo2Model StaticModel = new Dynamo2Model();
//
//	@Override
//	protected String getTextureSubfolder() {
//		return "Converter/";
//	}
//
//	/**
//	 * Renders the BlockEntity for the position.
//	 */
//	public void renderBlockEntityStaticAt(BlockEntityDynamo tile, PoseStack stack, MultiBufferSource bufferSource, int light)
//	{
//		int var9;
//
//		if (!tile.isInWorld())
//			var9 = 2;
//		else
//			var9 = tile.getBlockMetadata();
//
//		Dynamo2Model var14;
//		var14 = StaticModel;
//
//		String s = "/Reika/RotaryCraft/Textures/TileEntityTex/Converter/dynamotex";
//
//		if (tile.isInWorld() && tile.power > 0)
//			s = s+"2";
//		if (tile.isUpgraded())
//			s = s+"_u";
//		s = s+".png";
//
//		ReikaTextureHelper.bindTexture(RotaryCraft.class, s);
//
//		this.setupGL(tile, par2, par4, par6);
//
//		int var11 = 0;
//
//		if (tile.isInWorld()) {
//
//			switch(tile.getBlockMetadata()) {
//				case 0:
//					var11 = 0;
//					break;
//				case 1:
//					var11 = 180;
//					break;
//				case 2:
//					var11 = 0;
//					break;
//				case 3:
//					var11 = 90;
//					break;
//				case 4:
//					var11 = 180;
//					break;
//				case 5:
//					var11 = 270;
//					break;
//			}
//
//			if (tile.getBlockMetadata() < 2) {
//				stack.mulPose(var11, 0, 0, 1);
//				if (tile.getBlockMetadata() == 1)
//					stack.translate(0, -2, 0);
//			}
//			else {
//				stack.mulPose(90, 1, 0, 0);
//				stack.mulPose(var11, 0, 0, 1);
//				stack.translate(0, -1, -1);
//			}
//		}
//
//		var14.renderAll(tile, null, -tile.phi, 0);
//
//		this.closeGL(tile);
//	}
//
//	@Override
//	public void render(BlockEntity tile, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
//	{
//		if (this.doRenderModel((RotaryCraftBlockEntity)tile))
//			this.renderTileEntityStaticAt((TileEntityDynamo)tile, par2, par4, par6, par8);
//		if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1) {
//			IORenderer.renderIO(tile, par2, par4, par6);
//		}
//	}
//
//	@Override
//	public String getImageFileName(RenderFetcher te) {
//		return "dynamotex.png";
//	}
//}
