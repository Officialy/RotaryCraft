///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.modinterface;
//
//import net.minecraft.world.level.block.entity.BlockEntity;
//import org.lwjgl.opengl.GL11;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//
//public class RenderFuelEngine extends RotaryTERenderer
//{
//
//	private ModelFuelEngine FuelModel = new ModelFuelEngine();
//	//private ModelFuelEngineV FuelModelV = new ModelFuelEngineV();
//
//	@Override
//	protected String getTextureSubfolder() {
//		return "Converter/";
//	}
//
//	/**
//	 * Renders the BlockEntity for the position.
//	 */
//	public void renderBlockEntityFuelEngineAt(BlockEntityFuelEngine tile, PoseStack stack, MultiBufferSource bufferSource, int light)
//	{
//		int var9;
//
//		if (!tile.isInWorld())
//			var9 = 0;
//		else
//			var9 = tile.getBlockMetadata();
//
//		ModelFuelEngine var14;
//		var14 = FuelModel;
//
//		this.bindTextureByName("/Reika/RotaryCraft/Textures/BlockEntityTex/Converter/fuelenginetex.png");
//
//		this.setupGL(tile, par2, par4, par6);
//
//		int var11 = 0;
//		float var13;
//		switch(var9) {
//			case 2:
//				var11 = -90;
//				break;
//			case 0:
//				var11 = 180;
//				break;
//			case 1:
//				var11 = 0;
//				break;
//			case 3:
//				var11 = 90;
//				break;
//		}
//
//		stack.mulPose((float)var11+90, 0.0F, 1.0F, 0.0F);
//		var14.renderAll(tile, null, tile.phi, 0);
//
//		this.closeGL(tile);
//	}
//
//	@Override
//	public void renderBlockEntityAt(BlockEntity tile, PoseStack stack, MultiBufferSource bufferSource, int light)
//	{
//		if (this.doRenderModel((RotaryCraftBlockEntity)tile))
//			this.renderBlockEntityFuelEngineAt((BlockEntityFuelEngine)tile, par2, par4, par6, par8);
//		if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1) {
//			IORenderer.renderIO(tile, par2, par4, par6);
//		}
//	}
//
//	@Override
//	public String getImageFileName(RenderFetcher te) {
//		return "fuelenginetex.png";
//	}
//}
