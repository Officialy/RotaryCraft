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
//import com.mojang.blaze3d.vertex.PoseStack;
//import net.minecraft.client.renderer.MultiBufferSource;
//import org.lwjgl.opengl.GL11;
//
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraftforge.client.MinecraftForgeClient;
//
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.modinterface.model.ModelPneumatic;
//
//public class RenderPneumatic extends RotaryTERenderer {
//
//    private ModelPneumatic PneumaticModel = new ModelPneumatic();
//    //private ModelPneumaticV PneumaticModelV = new ModelPneumaticV();
//
//    @Override
//    protected String getTextureSubfolder() {
//        return "Converter/";
//    }
//
//    /**
//     * Renders the BlockEntity for the position.
//     */
//    public void renderBlockEntityPneumaticEngineAt(TileEntityPneumaticEngine tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int var9;
//
//        if (!tile.isInWorld())
//            var9 = 0;
//        else
//            var9 = tile.getBlockMetadata();
//
//        ModelPneumatic var14;
//        var14 = PneumaticModel;
//
//        this.bindTextureByName("/Reika/RotaryCraft/Textures/TileEntityTex/Converter/pneutex.png");
//
//        this.setupGL(tile, par2, par4, par6);
//
//        int var11 = 0;
//        float var13;
//		switch (var9) {
//			case 2 -> var11 = 0;
//			case 0 -> var11 = 180;
//			case 1 -> var11 = 90;
//			case 3 -> var11 = 270;
//		}
//
//        stack.mulPose((float) var11 + 90, 0.0F, 1.0F, 0.0F);
//        var14.renderAll(tile, null, -tile.phi, 0);
//
//        this.closeGL(tile);
//    }
//
//    @Override
//    public void render(BlockEntityReservoir tile, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
//        if (this.doRenderModel((RotaryCraftBlockEntity) tile))
//            this.renderTileEntityPneumaticEngineAt((TileEntityPneumaticEngine) tile, par2, par4, par6, par8);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1) {
//            IORenderer.renderIO(tile, par2, par4, par6);
//        }
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        return "pneutex.png";
//    }
//
//	@Override
//	public void render(BlockEntity p_112307_, float p_112308_, PoseStack p_112309_, MultiBufferSource p_112310_, int p_112311_, int p_112312_) {
//
//	}
//}
