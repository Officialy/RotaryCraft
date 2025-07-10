package reika.rotarycraft.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.joml.Matrix4f;
import reika.dragonapi.libraries.rendering.ReikaRenderHelper;
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

        stack.translate(0.5, 1.5, 0.5);
        stack.scale(1.0F, -1.0F, -1.0F);

        int rotationY = 0;
        int rotationX = 0;
        int rotationZ = 0;
        int dir = 1;

        switch (tile.direction) {
            case 0 -> rotationY = 90;
            case 1 -> rotationY = 180;
            case 2 -> rotationY = 270;
            case 3 -> rotationY = 0;
            case 4 -> { rotationY = 0; dir = -1; }
            case 5 -> { rotationY = 90; dir = -1; }
            case 6 -> { rotationY = 180; dir = -1; }
            case 7 -> { rotationY = 270; dir = -1; }
            case 8 -> { rotationY = 0; rotationX = 270; stack.translate(0, 1, 1); dir = -1; }
            case 9 -> { rotationY = 90; rotationX = 270; stack.translate(1, 1, 0); dir = -1; }
            case 10 -> { rotationY = 180; rotationX = 270; stack.translate(0, 1, -1); dir = -1; }
            case 11 -> { rotationY = -90; rotationX = 270; stack.translate(-1, 1, 0); dir = -1; }
            case 12 -> { rotationY = 0; rotationX = 90; stack.translate(0, 1, -1); dir = -1; }
            case 13 -> { rotationY = 90; rotationX = 90; stack.translate(-1, 1, 0); }
            case 14 -> { rotationY = 180; rotationX = 90; stack.translate(0, 1, 1); dir = -1; }
            case 15 -> { rotationY = -90; rotationX = 90; stack.translate(1, 1, 0); dir = -1; }
            case 16 -> { rotationY = 0; rotationX = 90; stack.translate(0, 1, -1); dir = -1; }
            case 17 -> { rotationY = 90; rotationX = 90; stack.translate(-1, 1, 0); dir = -1; }
            case 18 -> { rotationY = 180; rotationX = 90; stack.translate(0, 1, 1); dir = -1; }
            case 19 -> { rotationY = -90; rotationX = 90; stack.translate(1, 1, 0); dir = -1; }
            case 20 -> { rotationY = 0; rotationX = 270; stack.translate(0, 1, 1); }
            case 21 -> { rotationY = 90; rotationX = 270; stack.translate(1, 1, 0); }
            case 22 -> { rotationY = 180; rotationX = 270; stack.translate(0, 1, -1); }
            case 23 -> { rotationY = -90; rotationX = 270; stack.translate(-1, 1, 0); }
        }

        if (rotationY != 0)
            stack.mulPose(Axis.YP.rotationDegrees(rotationY));
        if (rotationX != 0)
            stack.mulPose(Axis.XP.rotationDegrees(rotationX));
        if (rotationZ != 0)
            stack.mulPose(Axis.ZP.rotationDegrees(rotationZ));


        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutout(BevelModel.TEXTURE_LOCATION));
        bevelModel.renderAll(stack, vertexconsumer, light, tile, null, tile.phi * dir);
        stack.popPose();
    }

    @Override
    public void render(BlockEntityBevelGear tile, float partialTicks, PoseStack stack, MultiBufferSource bufferSource, int light, int overlay) {
        if (this.doRenderModel(stack, tile))
            this.renderBlockEntityBevelAt(stack, tile, bufferSource, light);

        if (tile.isInWorld()) {
            if (tile.iotick > 64 && ConfigRegistry.COLORBLIND.getState())
                this.renderFaceNumbers(stack, tile, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ(), bufferSource);

            stack.pushPose();
            IORenderer.renderIO(stack, bufferSource, tile, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ());
            stack.popPose();
            renderCompass(tile, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ(), bufferSource, stack);
        }
    }

    private void renderFaceNumbers(PoseStack stack, BlockEntityBevelGear tile, double x, double y, double z, MultiBufferSource bufferSource) {
        stack.pushPose();
        stack.translate(x, y, z);
        ReikaRenderHelper.disableLighting();

        float scale = 0.0625f;
        double d = 0.53;

        for (int i = 0; i < 6; i++) {
            stack.pushPose();

            int rx = 0, ry = 0, rz = 0;
            double dx = 0, dy = 0, dz = 0;
            double l = -0.07;

            switch (i) {
                case 0 -> { rx = 90; l = 0.07; }
                case 1 -> { rx = 90; dy = 1; }
                case 2 -> { rz = 180; dy = 1; dx = 1; }
                case 3 -> { dz = 1; l = 0.07; }
                case 4 -> { ry = 90; rz = 180; dy = 1; }
                case 5 -> { ry = -90; rz = 180; dy = 1; dx = 1; dz = 1; }
            }

            stack.translate(dx, dy, dz);
            stack.mulPose(Axis.XP.rotationDegrees(rx));
            stack.mulPose(Axis.YP.rotationDegrees(ry));
            stack.mulPose(Axis.ZP.rotationDegrees(rz));
            stack.translate(d, 0.28, l);
            stack.scale(scale, scale, scale);

            Minecraft.getInstance().font.drawInBatch(
                    String.valueOf(i),
                    0,
                    0,
                    0xFFFFFF,
                    false,
                    stack.last().pose(),
                    bufferSource,
                    Font.DisplayMode.NORMAL,
                    0,
                    15728880
            );

            stack.popPose();
        }

        ReikaRenderHelper.enableLighting();
        stack.popPose();
    }

    private void renderCompass(BlockEntity tile, double x, double y, double z, MultiBufferSource bufferSource, PoseStack stack) {
        BlockEntityIOMachine io = (BlockEntityIOMachine) tile;
        int[] rgb = {255, 255, 0};
        float alpha = Math.min(1F, io.iotick / 255F);
        float vo = 1.05F;

        Tesselator tess = Tesselator.getInstance();
        BufferBuilder buffer = tess.getBuilder();

        stack.pushPose();
        stack.translate(x, y + vo, z);
        Matrix4f pose = stack.last().pose();

        buffer.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);

        addLine(buffer, pose, -0.5f, 0, 0.5f, 1.5f, 0, 0.5f, rgb, alpha);
        addLine(buffer, pose, 0.5f, 0, -0.5f, 0.5f, 0, 1.5f, rgb, alpha);
        addLine(buffer, pose, 0.35f, 0, -0.75f, 0.35f, 0, -1.25f, rgb, alpha);
        addLine(buffer, pose, 0.35f, 0, -1.25f, 0.65f, 0, -0.75f, rgb, alpha);
        addLine(buffer, pose, 0.65f, 0, -0.75f, 0.65f, 0, -1.25f, rgb, alpha);

        tess.end();
        stack.popPose();
    }

    private void addLine(BufferBuilder buffer, Matrix4f pose, float x1, float y1, float z1, float x2, float y2, float z2, int[] rgb, float alpha) {
        buffer.vertex(pose, x1, y1, z1).color(rgb[0], rgb[1], rgb[2], (int)(alpha * 255)).endVertex();
        buffer.vertex(pose, x2, y2, z2).color(rgb[0], rgb[1], rgb[2], (int)(alpha * 255)).endVertex();
    }

}
