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
//import net.neoforged.client.MinecraftForgeClient;
//
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.dragonapi.libraries.Java.ReikaJavaLibrary;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.blockentities.storage.BlockEntityReservoir;
//import reika.rotarycraft.modinterface.model.ElecMotorModel;
//import reika.rotarycraft.modinterface.model.ElecMotorModel;
//
//public class RenderElecMotor extends RotaryTERenderer {
//
//    private ElecMotorModel elecMotorModel = new ElecMotorModel();
//
//    @Override
//    protected String getTextureSubfolder() {
//        return "Converter/";
//    }
//
//    /**
//     * Renders the BlockEntity for the position.
//     */
//    public void renderTileEntityElectricMotorAt(TileEntityElectricMotor tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int var9;
//
//        if (!tile.isInWorld())
//            var9 = 0;
//        else
//            var9 = tile.getBlockMetadata();
//
//        ElecMotorModel var14;
//        var14 = elecMotorModel;
//
//        this.bindTextureByName("/Reika/RotaryCraft/Textures/TileEntityTex/Converter/elecmotortex.png");
//
//        this.setupGL(tile, par2, par4, par6);
//
//        int var11 = 0;
//        float var13;
//        switch (var9) {
//            case 2:
//                var11 = 0;
//                break;
//            case 0:
//                var11 = 180;
//                break;
//            case 1:
//                var11 = 90;
//                break;
//            case 3:
//                var11 = 270;
//                break;
//        }
//
//        stack.mulPose(var11, 0.0F, 1.0F, 0.0F);
//        var14.renderAll(tile, ReikaJavaLibrary.makeListFrom(5), tile.phi, 0);
//
//        this.closeGL(tile);
//    }
//
//    @Override
//    public void render(BlockEntity tile, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
//        if (this.doRenderModel((RotaryCraftBlockEntity) tile))
//            this.renderTileEntityElectricMotorAt((TileEntityElectricMotor) tile, par2, par4, par6, par8);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1) {
//            IORenderer.renderIO(tile, par2, par4, par6);
//        }
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        return "elecmotortex.png";
//    }
//
//}
