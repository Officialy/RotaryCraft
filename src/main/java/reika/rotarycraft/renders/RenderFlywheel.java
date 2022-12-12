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
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.math.Quaternion;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.dragonapi.libraries.java.ReikaJavaLibrary;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
//import reika.rotarycraft.blockentities.transmission.BlockEntityFlywheel;
//import reika.rotarycraft.model.animated.ModelFlywheel;
//import reika.rotarycraft.registry.Flywheels;
//
//import java.util.Locale;
//
//public class RenderFlywheel extends RotaryTERenderer<BlockEntityFlywheel> {
//
//    private ModelFlywheel FlywheelModel = new ModelFlywheel();
//
//    public RenderFlywheel(BlockEntityRendererProvider.Context pContext) {
//
//    }
//
//    private static String getFlywheelTextureName(Flywheels f) {
//        return f.name().toLowerCase(Locale.ENGLISH) + ".png";
//    }
//
//    @Override
//    protected String getTextureSubfolder() {
//        return "Transmission/Flywheel/";
//    }
//
//    public void renderBlockEntityFlywheelAt(PoseStack stack, BlockEntityFlywheel tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//
//        ModelFlywheel var14;
//        var14 = FlywheelModel;
//        this.bindTextureByName(this.getTextureFolder() + this.getFlywheelTextureName(tile.getTypeOrdinal()));
//
//        this.setupGL(stack, tile, par2, par4, par6);
//
//        //GL11.glDisable(GL11.GL_LIGHTING);
//        int var11 = 0;     //used to rotate the model about metadata
//
//        if (tile.isInWorld()) {
//
//            switch (tile.getBlockState().getValue(getFacingProperty())) {
//                case EAST:
//                    var11 = 180;
//                    break;
//                case WEST:
//                    var11 = 0;
//                    break;
//                case SOUTH:
//                    var11 = 270;
//                    break;
//                case NORTH:
//                    var11 = 90;
//                    break;
//            }
//
//            stack.mulPose(new Quaternion(var11, 0.0F, 1.0F, 0.0F));
//
//        }
//        //stack.translate(-0.5F, -0.5F, -0.5F);
//        //float var12 = tile.prevLidAngle + (tile.lidAngle - tile.prevLidAngle) * par8;
//        float var13;/*
//
//            var12 = 1.0F - var12;
//            var12 = 1.0F - var12 * var12 * var12;*/
//        var14.renderAll(stack, tile, ReikaJavaLibrary.makeListFrom(tile.failed), -tile.phi, 0);
//
//        this.closeGL(stack, tile);
//    }
//
//    @Override
//    public void render(BlockEntityFlywheel tile, float v, PoseStack stack, MultiBufferSource multiBufferSource, int i, int i1) {
//        if (this.doRenderModel(stack, tile))
//            this.renderBlockEntityFlywheelAt(stack, tile, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ(), i);
//        if ((tile).isInWorld())// && MinecraftForgeClient.getRenderPass() == 1)
//            IORenderer.renderIO(stack, bufferSource, tile, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ());
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        BlockEntityFlywheel tile = (BlockEntityFlywheel) te;
//        return this.getFlywheelTextureName(tile.getTypeOrdinal());
//    }
//}
