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
//import net.minecraft.world.level.block.entity.BlockEntity;import net.minecraftforge.client.MinecraftForgeClient;
//
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.modinterface.model.GeneratorModel;
//import reika.rotarycraft.modinterface.model.GeneratorModel;
//
//public class RenderGenerator extends RotaryTERenderer {
//
//	private GeneratorModel generatorModel = new GeneratorModel();
//
//	@Override
//	protected String getTextureSubfolder() {
//		return "Converter/";
//	}
//
//	/**
//	 * Renders the BlockEntity for the position.
//	 */
//	public void renderBlockEntityGeneratorAt(TileEntityGenerator tile, PoseStack stack, MultiBufferSource bufferSource, int light)
//	{
//		int var9;
//
//		if (!tile.isInWorld())
//			var9 = 0;
//		else
//			var9 = tile.getBlockMetadata();
//
//		GeneratorModel var14;
//		var14 = generatorModel;
//
//		this.bindTextureByName("/Reika/RotaryCraft/Textures/TileEntityTex/Converter/generatortex.png");
//
//		this.setupGL(tile, par2, par4, par6);
//
//		int var11 = 0;
//		float var13;
//		switch (var9) {
//			case 2 -> var11 = 0;
//			case 0 -> var11 = 180;
//			case 1 -> var11 = 90;
//			case 3 -> var11 = 270;
//		}
//
//		GL11.glRotatef(var11+180, 0.0F, 1.0F, 0.0F);
//		var14.renderAll(tile, null, 0, 0);
//
//		this.closeGL(tile);
//	}
//
//	@Override
//	public void render(BlockEntity tile, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
//	{
//		if (this.doRenderModel((RotaryCraftBlockEntity)tile))
//			this.renderTileEntityGeneratorAt((TileEntityGenerator)tile, par2, par4, par6, par8);
//		if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1) {
//			IORenderer.renderIO(tile, par2, par4, par6);
//		}
//	}
//
//}