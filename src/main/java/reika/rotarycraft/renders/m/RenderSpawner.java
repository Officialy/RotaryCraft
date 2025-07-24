///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.renders.m;
//
//import reika.dragonapi.libraries.java.ReikaJavaLibrary;
//import reika.rotarycraft.blockentities.Farming.BlockEntitySpawnerController;
//import net.minecraft.BlockEntity.BlockEntity;
//import net.neoforged.client.MinecraftForgeClient;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.models.ModelSpawner;
//
//public class RenderSpawner extends RotaryTERenderer {
//
//    private ModelSpawner SpawnerModel = new ModelSpawner();
//
//    /**
//     * Renders the BlockEntity for the position.
//     */
//    public void renderBlockEntitySpawnerControllerAt(BlockEntitySpawnerController tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int var9;
//
//        if (!tile.isInWorld())
//            var9 = 0;
//        else
//            var9 = tile.getBlockMetadata();
//
//        ModelSpawner var14;
//        var14 = SpawnerModel;
//
//        this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/spawnertex.png");
//
//        stack.pushPose();
//        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        stack.translate((float) par2, (float) par4 + 2.0F, (float) par6 + 1.0F);
//        stack.scale(1.0F, -1.0F, -1.0F);
//        stack.translate(0.5F, 0.5F, 0.5F);
//        int var11 = 0;     //used to rotate the model about metadata
//
//        float var13;
//
//        if (!tile.isInWorld()) {
//            stack.translate(0, -0.4, 0);
//            var14.renderAll(stack, tile, ReikaJavaLibrary.makeListFrom(true), 0, 0);
//        } else
//            var14.renderAll(stack, tile, ReikaJavaLibrary.makeListFrom(tile.isValidLocation(tile.level, tile.xCoord, tile.yCoord, tile.zCoord)), 0, 0);
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
//            this.renderBlockEntitySpawnerControllerAt((BlockEntitySpawnerController) tile, par2, par4, par6, par8);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1)
//            IORenderer.renderIO(tile, par2, par4, par6);
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        return "spawnertex.png";
//    }
//}
