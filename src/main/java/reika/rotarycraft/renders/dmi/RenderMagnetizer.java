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
//import reika.dragonapi.libraries.java.ReikaJavaLibrary;
//import reika.rotarycraft.blockentities.processing.BlockEntityMagnetizer;
//import net.minecraft.BlockEntity.BlockEntity;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraftforge.client.MinecraftForgeClient;
//import org.lwjgl.opengl.GL11;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.models.Animated.ModelMagnetizer;
//import reika.rotarycraft.models.animated.ModelMagnetizer;
//
//public class RenderMagnetizer extends RotaryTERenderer {
//
//    private ModelMagnetizer MagnetizerModel = new ModelMagnetizer();
//
//    public void renderBlockEntityMagnetizerAt(BlockEntityMagnetizer tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int var9;
//
//        if (!tile.isInWorld())
//            var9 = 0;
//        else
//            var9 = tile.getBlockMetadata();
//
//        ModelMagnetizer var14;
//        var14 = MagnetizerModel;
//
//        this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/magnettex.png");
//
//        this.setupGL(tile, par2, par4, par6);
//
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
//            stack.mulPose((float) var11 - 90, 0.0F, 1.0F, 0.0F);
//
//        }
//
//        float var13;
//
//        boolean hasItem = tile.hasCore();
//        var14.renderAll(stack, tile, ReikaJavaLibrary.makeListFrom(hasItem), -tile.phi, 0);
//
//        this.closeGL(stack, tile);
//    }
//
//    @Override
//    public void renderBlockEntityAt(BlockEntity tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        if (this.doRenderModel((RotaryCraftBlockEntity) tile))
//            this.renderBlockEntityMagnetizerAt((BlockEntityMagnetizer) tile, par2, par4, par6, par8);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1)
//            IORenderer.renderIO(tile, par2, par4, par6);
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        return "magnettex.png";
//    }
//}
