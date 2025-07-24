///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.renders.mi;
//
//import reika.rotarycraft.base.BlockEntity.BlockEntityLaunchCannon;
//import reika.rotarycraft.blockentities.Weaponry.BlockEntityBlockCannon;
//import reika.rotarycraft.blockentities.Weaponry.BlockEntityTNTCannon;
//import com.mojang.blaze3d.vertex.BufferBuilder;
//import com.mojang.blaze3d.vertex.Tesselator;
//import net.minecraft.BlockEntity.BlockEntity;
//import net.minecraft.client.renderer.Tesselator;
//import net.neoforged.client.MinecraftForgeClient;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.dragonapi.libraries.rendering.ReikaRenderHelper;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.BlockEntityLaunchCannon;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.blockentities.weaponry.BlockEntityBlockCannon;
//import reika.rotarycraft.models.ModelCannon;
//
//public class RenderCannon extends RotaryTERenderer {
//
//    private ModelCannon CannonModel = new ModelCannon();
//
//    public void renderBlockEntityLaunchCannonAt(BlockEntityLaunchCannon tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int var9;
//
//        if (!tile.isInWorld())
//            var9 = 0;
//        else
//            var9 = tile.getBlockMetadata();
//
//        ModelCannon var14;
//        var14 = CannonModel;
//
//        if (tile instanceof BlockEntityTNTCannon)
//            this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/tntcannontex.png");
//        if (tile instanceof BlockEntityBlockCannon)
//            this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/blockcannontex.png");
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
//        var14.renderAll(stack, tile, null);
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
//            this.renderBlockEntityLaunchCannonAt((BlockEntityLaunchCannon) tile, par2, par4, par6, par8);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1) {
//            IORenderer.renderIO(tile, par2, par4, par6);
//            if (((BlockEntityLaunchCannon) tile).targetMode)
//                this.renderMarker((BlockEntityLaunchCannon) tile, par2, par4, par6);
//        }
//    }
//
//    private void renderMarker(BlockEntityLaunchCannon tile, double par2, double par4, double par6) {
//        ReikaRenderHelper.prepareGeoDraw(true);
//        Tesselator tess = Tesselator.getInstance();
//        BufferBuilder v5 = tess.getBuilder();
//        double dx = tile.target[0] - tile.xCoord;
//        double dy = tile.target[1] - tile.yCoord;
//        double dz = tile.target[2] - tile.zCoord;
//        float i = 0.5F;
//        float j = 0.5F;
//        double length = 25;
//        double width = 4;
//        double height = 4;
//        v5.startDrawing(GL11.GL_LINE_LOOP);
//        v5.color(0, 255, 0, 255);
//        v5.vertex(par2 + dx, par4 + dy + height, par6 + dz);
//        v5.vertex(par2 + dx, par4 + dy + length, par6 + dz);
//        v5.end();
//        v5.startDrawing(GL11.GL_LINE_LOOP);
//        v5.color(0, 255, 0, 255);
//        v5.vertex(par2 + dx + 0.5, par4 + dy + height, par6 + dz);
//        v5.vertex(par2 + dx + 0.5, par4 + dy + length, par6 + dz);
//        v5.end();
//        v5.startDrawing(GL11.GL_LINE_LOOP);
//        v5.color(0, 255, 0, 255);
//        v5.vertex(par2 + dx - 0.5, par4 + dy + height, par6 + dz);
//        v5.vertex(par2 + dx - 0.5, par4 + dy + length, par6 + dz);
//        v5.end();
//        v5.startDrawing(GL11.GL_LINE_LOOP);
//        v5.color(0, 255, 0, 255);
//        v5.vertex(par2 + dx, par4 + dy + height, par6 + dz + 0.5);
//        v5.vertex(par2 + dx, par4 + dy + length, par6 + dz + 0.5);
//        v5.end();
//        v5.startDrawing(GL11.GL_LINE_LOOP);
//        v5.color(0, 255, 0, 255);
//        v5.vertex(par2 + dx, par4 + dy + height, par6 + dz - 0.5);
//        v5.vertex(par2 + dx, par4 + dy + length, par6 + dz - 0.5);
//        v5.end();
//        v5.startDrawing(GL11.GL_LINE_LOOP);
//        v5.color(0, 255, 0, 255);
//        v5.vertex(par2 + dx - width, par4 + dy + height, par6 + dz);
//        v5.vertex(par2 + dx, par4 + dy, par6 + dz);
//        v5.vertex(par2 + dx + width, par4 + dy + height, par6 + dz);
//        v5.end();
//        v5.startDrawing(GL11.GL_LINE_LOOP);
//        v5.color(0, 255, 0, 255);
//        v5.vertex(par2 + dx, par4 + dy + height, par6 + dz - width);
//        v5.vertex(par2 + dx, par4 + dy, par6 + dz);
//        v5.vertex(par2 + dx, par4 + dy + height, par6 + dz + width);
//        v5.end();
//        v5.startDrawing(GL11.GL_LINE_LOOP);
//        v5.color(0, 255, 0, 255);
//        v5.vertex(par2 + dx - width * 0.71, par4 + dy + height, par6 + dz - width * 0.71);
//        v5.vertex(par2 + dx, par4 + dy, par6 + dz);
//        v5.vertex(par2 + dx + width * 0.71, par4 + dy + height, par6 + dz + width * 0.71);
//        v5.end();
//        v5.startDrawing(GL11.GL_LINE_LOOP);
//        v5.color(0, 255, 0, 255);
//        v5.vertex(par2 + dx + width * 0.71, par4 + dy + height, par6 + dz - width * 0.71);
//        v5.vertex(par2 + dx, par4 + dy, par6 + dz);
//        v5.vertex(par2 + dx - width * 0.71, par4 + dy + height, par6 + dz + width * 0.71);
//        v5.end();
//
//        v5.startDrawing(GL11.GL_POLYGON);
//        v5.color(0, 255, 0, 127);
//        v5.vertex(par2 + dx - width, par4 + dy + height, par6 + dz);
//        v5.vertex(par2 + dx, par4 + dy, par6 + dz);
//        v5.vertex(par2 + dx + width, par4 + dy + height, par6 + dz);
//        v5.end();
//        v5.startDrawing(GL11.GL_POLYGON);
//        v5.color(0, 255, 0, 127);
//        v5.vertex(par2 + dx, par4 + dy + height, par6 + dz - width);
//        v5.vertex(par2 + dx, par4 + dy, par6 + dz);
//        v5.vertex(par2 + dx, par4 + dy + height, par6 + dz + width);
//        v5.end();
//        v5.startDrawing(GL11.GL_POLYGON);
//        v5.color(0, 255, 0, 127);
//        v5.vertex(par2 + dx + width, par4 + dy + height, par6 + dz);
//        v5.vertex(par2 + dx, par4 + dy, par6 + dz);
//        v5.vertex(par2 + dx - width, par4 + dy + height, par6 + dz);
//        v5.end();
//        v5.startDrawing(GL11.GL_POLYGON);
//        v5.color(0, 255, 0, 127);
//        v5.vertex(par2 + dx, par4 + dy + height, par6 + dz + width);
//        v5.vertex(par2 + dx, par4 + dy, par6 + dz);
//        v5.vertex(par2 + dx, par4 + dy + height, par6 + dz - width);
//        v5.end();
//
//        v5.startDrawing(GL11.GL_POLYGON);
//        v5.color(0, 255, 0, 127);
//        v5.vertex(par2 + dx - width * 0.71, par4 + dy + height, par6 + dz - width * 0.71);
//        v5.vertex(par2 + dx, par4 + dy, par6 + dz);
//        v5.vertex(par2 + dx + width * 0.71, par4 + dy + height, par6 + dz + width * 0.71);
//        v5.end();
//        v5.startDrawing(GL11.GL_POLYGON);
//        v5.color(0, 255, 0, 127);
//        v5.vertex(par2 + dx + width * 0.71, par4 + dy + height, par6 + dz - width * 0.71);
//        v5.vertex(par2 + dx, par4 + dy, par6 + dz);
//        v5.vertex(par2 + dx - width * 0.71, par4 + dy + height, par6 + dz + width * 0.71);
//        v5.end();
//        v5.startDrawing(GL11.GL_POLYGON);
//        v5.color(0, 255, 0, 127);
//        v5.vertex(par2 + dx - width * 0.71, par4 + dy + height, par6 + dz + width * 0.71);
//        v5.vertex(par2 + dx, par4 + dy, par6 + dz);
//        v5.vertex(par2 + dx + width * 0.71, par4 + dy + height, par6 + dz - width * 0.71);
//        v5.end();
//        v5.startDrawing(GL11.GL_POLYGON);
//        v5.color(0, 255, 0, 127);
//        v5.vertex(par2 + dx + width * 0.71, par4 + dy + height, par6 + dz + width * 0.71);
//        v5.vertex(par2 + dx, par4 + dy, par6 + dz);
//        v5.vertex(par2 + dx - width * 0.71, par4 + dy + height, par6 + dz - width * 0.71);
//        v5.end();
//        ReikaRenderHelper.exitGeoDraw();
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        if (te instanceof BlockEntityTNTCannon)
//            return "tntcannontex.png";
//        if (te instanceof BlockEntityBlockCannon)
//            return "blockcannontex.png";
//        return "";
//    }
//}
