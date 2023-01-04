/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.renders;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.joml.Quaternionf;
import org.lwjgl.opengl.GL11;
import reika.dragonapi.libraries.rendering.ReikaGuiAPI;
import reika.dragonapi.libraries.rendering.ReikaRenderHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blockentity.BlockEntityIOMachine;
import reika.rotarycraft.blockentities.transmission.BlockEntityBevelGear;
import reika.rotarycraft.models.animated.BevelModel;
import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderBevel extends RotaryTERenderer<BlockEntityBevelGear> {

    private final BevelModel bevelModel;

    public RenderBevel(BlockEntityRendererProvider.Context context) {
        bevelModel = new BevelModel(context.bakeLayer(RotaryModelLayers.BEVEL));
    }

    public void renderBlockEntityBevelAt(PoseStack stack, BlockEntityBevelGear tile, MultiBufferSource bufferSource, int light) {
        stack.pushPose();
//        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        stack.translate((float) par2, (float) par4 + 2.0F, (float) par6 + 1.0F);
//        stack.scale(1.0F, -1.0F, -1.0F);
        stack.translate(0.5F, 1.5F, 0.5F);
        int var11 = 0;  //used to rotate the model into the X direction
        int var12 = 0;  //used to rotate the model into the Y direction
        int var13 = 0;  //used to rotate the model into the Z direction
        int dir = 1;
//        RotaryCraft.LOGGER.info(tile.direction);
        if (tile.isInWorld()) {
            switch (tile.direction) {
                case 0 -> {
                    var11 = 90;
                    var12 = 0;
                    var13 = 0;
                }
                case 1 -> {
                    var11 = 180;
                    var12 = 0;
                    var13 = 0;
                }
                case 2 -> {
                    var11 = 270;
                    var12 = 0;
                    var13 = 0;
                }
                case 3 -> {
                    var11 = 0;
                    var12 = 0;
                    var13 = 0;
                }
                case 4 -> {
                    var11 = 0;
                    var12 = 0;
                    var13 = 0;
                    dir = -1;
                }
                case 5 -> {
                    var11 = 90;
                    var12 = 0;
                    var13 = 0;
                    dir = -1;
                }
                case 6 -> {
                    var11 = 180;
                    var12 = 0;
                    var13 = 0;
                    dir = -1;
                }
                case 7 -> {
                    var11 = 270;
                    var12 = 0;
                    var13 = 0;
                    dir = -1;
                }
                case 8 -> {
                    var11 = 0;
                    var12 = 270;
                    var13 = 0;
                    stack.translate(0F, 1F, 1F);
                    dir = -1;
                }
                case 9 -> {
                    var11 = 90;
                    var12 = 270;
                    var13 = 0;
                    stack.translate(1F, 1F, -0F);
                    dir = -1;
                }
                case 10 -> {
                    var11 = 180;
                    var12 = 270;
                    var13 = 0;
                    stack.translate(0F, 1F, -1F);
                    dir = -1;
                }
                case 11 -> {
                    var11 = -90;
                    var12 = 270;
                    var13 = 0;
                    stack.translate(-1F, 1F, -0F);
                    dir = -1;
                }
                case 12 -> {
                    var11 = 0;
                    var12 = 90;
                    var13 = 0;
                    stack.translate(0F, 1F, -1F);
                    dir = -1;
                }
                case 13 -> {
                    var11 = 90;
                    var12 = 90;
                    var13 = 0;
                    stack.translate(-1F, 1F, -0F);
                }
                case 14 -> {
                    var11 = 180;
                    var12 = 90;
                    var13 = 0;
                    stack.translate(0F, 1F, 1F);
                    dir = -1;
                }
                case 15 -> {
                    var11 = -90;
                    var12 = 90;
                    var13 = 0;
                    stack.translate(1F, 1F, -0F);
                    dir = -1;
                }
                case 16 -> {
                    var11 = 0;
                    var12 = 90;
                    var13 = 0;
                    stack.translate(0F, 1F, -1F);
                    dir = -1;
                }
                case 17 -> {
                    var11 = 90;
                    var12 = 90;
                    var13 = 0;
                    stack.translate(-1F, 1F, -0F);
                    dir = -1;
                }
                case 18 -> {
                    var11 = 180;
                    var12 = 90;
                    var13 = 0;
                    stack.translate(0F, 1F, 1F);
                    dir = -1;
                }
                case 19 -> {
                    var11 = -90;
                    var12 = 90;
                    var13 = 0;
                    stack.translate(1F, 1F, -0F);
                    dir = -1;
                }
                case 20 -> {
                    var11 = 0;
                    var12 = 270;
                    var13 = 0;
                    stack.translate(0F, 1F, 1F);
                }
                case 21 -> {
                    var11 = 90;
                    var12 = 270;
                    var13 = 0;
                    stack.translate(1F, 1F, -0F);
                }
                case 22 -> {
                    var11 = 180;
                    var12 = 270;
                    var13 = 0;
                    stack.translate(0F, 1F, -1F);
                }
                case 23 -> {
                    var11 = -90;
                    var12 = 270;
                    var13 = 0;
                    stack.translate(-1F, 1F, 0F);
                }
            }

            stack.mulPose(Axis.YP.rotationDegrees(var11));
            stack.mulPose(Axis.XP.rotationDegrees(var12));
            stack.mulPose(Axis.ZP.rotationDegrees(var13));
        } else {
            stack.mulPose(new Quaternionf(Axis.YP.rotationDegrees(90F)));
        }
        stack.mulPose(new Quaternionf(Axis.ZP.rotationDegrees(180F)));
        //stack.translate(-0.5F, -0.5F, -0.5F);
        //float var12 = tile.prevLidAngle + (tile.lidAngle - tile.prevLidAngle) * par8;
        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutout(BevelModel.TEXTURE_LOCATION));
        bevelModel.renderAll(stack, vertexconsumer, light, tile, null, tile.phi * dir);
//        if (tile.isInWorld())
//            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        stack.popPose();
    }

    @Override
    public void render(BlockEntityBevelGear tile, float p_112308_, PoseStack stack, MultiBufferSource bufferSource, int p_112311_, int p_112312_) {
        if (this.doRenderModel(stack, tile))
            this.renderBlockEntityBevelAt(stack, tile, bufferSource, p_112311_);
        if (tile.isInWorld()) {// && MinecraftForgeClient.getRenderPass() == 1) {
//            this.renderCompass(tile, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ());
            if (tile.iotick > 64 && ConfigRegistry.COLORBLIND.getState())
                this.renderFaceNumbers(stack, tile, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ());
//            else
//                this.renderFaceColors(tile, par2, par4, par6);
            IORenderer.renderIO(stack, bufferSource, tile, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ());
        }

    }

    private void renderFaceNumbers(PoseStack stack, BlockEntityBevelGear tile, double par2, double par4, double par6) {
        stack.translate(par2, par4, par6);
        ReikaRenderHelper.disableLighting();
        float s = 0.0625f;
        double d = 0.53;
        for (int i = 0; i < 6; i++) {
            double l = -0.07;
            int rx = 0;
            int ry = 0;
            int rz = 0;
            double dx = 0;
            double dy = 0;
            double dz = 0;
            switch (i) {
                case 0 -> {
                    rx = 90;
                    l = 0.07;
                }
                case 1 -> {
                    rx = 90;
                    dy = 1;
                }
                case 2 -> {
                    rz = 180;
                    dy = 1;
                    dx = 1;
                }
                case 3 -> {
                    dz = 1;
                    l = 0.07;
                }
                case 4 -> {
                    ry = 90;
                    rz = 180;
                    dy = 1;
                }
                case 5 -> {
                    ry = -90;
                    rz = 180;
                    dy = 1;
                    dx = 1;
                    dz = 1;
                }
            }
            stack.translate(dx, 0, 0);
            stack.translate(0, dy, 0);
            stack.translate(0, 0, dz);
            GL11.glRotated(rx, 1, 0, 0);
            GL11.glRotated(ry, 0, 1, 0);
            GL11.glRotated(rz, 0, 0, 1);
            stack.translate(d, 0.28, l);
            stack.scale(s, s, s);
            //Minecraft.getInstance().fontRenderer.draw("0", 0, 0, 0xffffff);
            ReikaGuiAPI.instance.drawCenteredStringNoShadow(stack, this.getFontRenderer(), String.valueOf(i), 0, 0, 0xffffff);
            stack.scale(1 / s, 1 / s, 1 / s);
            stack.translate(-d, -0.28, -l);
            GL11.glRotated(-rz, 0, 0, 1);
            GL11.glRotated(-ry, 0, 1, 0);
            GL11.glRotated(-rx, 1, 0, 0);
            stack.translate(0, 0, -dz);
            stack.translate(0, -dy, 0);
            stack.translate(-dx, 0, 0);
        }
        ReikaRenderHelper.enableLighting();
        stack.translate(-par2, -par4, -par6);
    }

    private void renderCompass(BlockEntity te, double p2, double p4, double p6) {
        BlockEntityIOMachine io = (BlockEntityIOMachine) te;
        ReikaRenderHelper.prepareGeoDraw(false);
        double vo = 1.05;
        int[] rgb = {255, 255, 0};
        //GL11.glDisable(GL11.GL_DEPTH_TEST);
        //RenderSystem.enableBlend();
        Tesselator tess = Tesselator.getInstance();
        BufferBuilder v5 = tess.getBuilder();
        v5.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR); // was GL_LINE_LOOP
        v5.color(rgb[0], rgb[1], rgb[2], io.iotick);
        v5.vertex(p2 - 0.5, p4 + vo, p6 + 0.5);
        v5.vertex(p2 + 1.5, p4 + vo, p6 + 0.5);
        v5.end();
        v5.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR); // was GL_LINE_LOOP
        v5.color(rgb[0], rgb[1], rgb[2], io.iotick);
        v5.vertex(p2 + 0.5, p4 + vo, p6 - 0.5);
        v5.vertex(p2 + 0.5, p4 + vo, p6 + 1.5);
        v5.end();
        v5.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);
        v5.color(rgb[0], rgb[1], rgb[2], io.iotick);
        v5.vertex(p2 + 0.35, p4 + vo, p6 - 0.75);
        v5.vertex(p2 + 0.35, p4 + vo, p6 - 1.25);
        v5.end();
        v5.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);
        v5.color(rgb[0], rgb[1], rgb[2], io.iotick);
        v5.vertex(p2 + 0.35, p4 + vo, p6 - 1.25);
        v5.vertex(p2 + 0.65, p4 + vo, p6 - 0.75);
        v5.end();
        v5.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);
        v5.color(rgb[0], rgb[1], rgb[2], io.iotick);
        v5.vertex(p2 + 0.65, p4 + vo, p6 - 0.75);
        v5.vertex(p2 + 0.65, p4 + vo, p6 - 1.25);
        v5.end();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        ReikaRenderHelper.exitGeoDraw();
    }

}
