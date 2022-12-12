///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.renders.dmi;
//
//import reika.rotarycraft.blockentities.processing.BlockEntityGrinder;
//import net.minecraft.BlockEntity.BlockEntity;
//import net.minecraftforge.client.MinecraftForgeClient;
//import org.lwjgl.opengl.GL11;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.rotarycraft.auxiliary.EnchantmentRenderer;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.models.Animated.ModelGrinder;
//
//public class RenderGrinder extends RotaryTERenderer {
//
//    private ModelGrinder GrinderModel = new ModelGrinder();
//    //private ModelGrinderV GrinderModelV = new ModelGrinderV();
//
//    /**
//     * Renders the BlockEntity for the position.
//     */
//    public void renderBlockEntityGrinderAt(BlockEntityGrinder tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int var9;
//
//        if (!tile.isInWorld())
//            var9 = 0;
//        else
//            var9 = tile.getBlockMetadata();
//
//        ModelGrinder var14;
//        var14 = GrinderModel;
//        //ModelGrinderV var15;
//        //var14 = this.GrinderModelV;
//        this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/grindertex.png");
//
//        this.setupGL(tile, par2, par4, par6);
//
//        //GL11.glDisable(GL11.GL_LIGHTING);
//        int var11 = 0;     //used to rotate the model about metadata
//
//        if (tile.isInWorld()) {
//
//            switch (tile.getBlockMetadata()) {
//                case 0:
//                    var11 = 180;
//                    break;
//                case 1:
//                    var11 = 0;
//                    break;
//                case 2:
//                    var11 = 270;
//                    break;
//                case 3:
//                    var11 = 90;
//                    break;
//            }
//
//            GL11.glRotatef((float) var11 - 90, 0.0F, 1.0F, 0.0F);
//
//        }
//        //float var12 = tile.prevLidAngle + (tile.lidAngle - tile.prevLidAngle) * par8;
//        float var13;/*
//
//            var12 = 1.0F - var12;
//            var12 = 1.0F - var12 * var12 * var12;*/
//        // if (tile.getBlockMetadata() < 4)
//
//
//        var14.renderAll(stack, tile, null, -tile.phi);
//        // else
//        //var15.renderAll(stack, tile, );
//        //GL11.glEnable(GL11.GL_LIGHTING);
//
//        this.closeGL(stack, tile);
//    }
//
//    @Override
//    public void renderBlockEntityAt(BlockEntity tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        if (this.doRenderModel((RotaryCraftBlockEntity) tile))
//            this.renderBlockEntityGrinderAt((BlockEntityGrinder) tile, par2, par4, par6, par8);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1) {
//            IORenderer.renderIO(tile, par2, par4, par6);
//            if (((BlockEntityGrinder) tile).getEnchantmentHandler().hasEnchantments())
//                EnchantmentRenderer.renderGlint(tile, GrinderModel, null, par2, par4, par6);
//        } else if (!tile.hasLevel()) {
//            int var11 = 0;
//            switch (tile.getBlockMetadata()) {
//                case 0:
//                    var11 = 180;
//                    break;
//                case 1:
//                    var11 = 0;
//                    break;
//                case 2:
//                    var11 = 270;
//                    break;
//                case 3:
//                    var11 = 90;
//                    break;
//            }
//            GL11.glRotatef((float) var11 - 90, 0.0F, 1.0F, 0.0F);
//            if (((BlockEntityGrinder) tile).getEnchantmentHandler().hasEnchantments())
//                EnchantmentRenderer.renderGlint(tile, GrinderModel, null, par2, par4, par6);
//        }
//
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        return "grindertex.png";
//    }
//}
