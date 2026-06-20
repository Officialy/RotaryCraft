package reika.rotarycraft.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

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

    public void renderBlockEntityBevelAt(PoseStack stack, BlockEntityBevelGear tile, VertexConsumer bufferSource, int light) {
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


        VertexConsumer vertexconsumer = bufferSource;
        bevelModel.renderAll(stack, vertexconsumer, light, tile, null, tile.phi * dir);
        stack.popPose();
    }

    // 1.21.5: render -> submit. Bevel gears use exactly one RenderType (entityCutout with the
    // bevel texture) regardless of which of the 24 mounting orientations is active, so one
    // submitCustomGeometry call covers the model. Pose is snapshotted because the lambda runs
    // after the outer PoseStack may have been popped.
    @Override
    public void submit(BlockEntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;
        BlockEntity be = level.getBlockEntity(state.blockPos);
        if (!(be instanceof BlockEntityBevelGear tile)) return;
        if (!this.doRenderModel(poseStack, tile)) return;

        PoseStack snapped = new PoseStack();
        snapped.last().set(poseStack.last());

        RenderType rt = RenderTypes.entityCutout(BevelModel.TEXTURE_LOCATION);
        int light = state.lightCoords;
        collector.submitCustomGeometry(poseStack, rt, (pose, vc) -> {
            renderBlockEntityBevelAt(snapped, tile, vc, light);
        });
        // IO arrows for the bevel's read/write directions (debugFilledBox quads).
        if (tile.isInWorld()) {
            IORenderer.renderIO(poseStack, collector, tile, tile.getBlockPos());
        }
        // Face numbers / compass still TODO (need separate per-RT submissions).
    }

    private void renderFaceNumbers(PoseStack stack, BlockEntityBevelGear tile, double x, double y, double z, net.minecraft.client.renderer.SubmitNodeCollector collector) {
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

            // 26.2: Font.drawInBatch removed; in-world text is submitted via the feature pipeline.
            collector.submitText(stack, 0, 0,
                    net.minecraft.util.FormattedCharSequence.forward(String.valueOf(i), net.minecraft.network.chat.Style.EMPTY),
                    false, Font.DisplayMode.NORMAL, 15728880, 0xFFFFFF, 0, 0);

            stack.popPose();
        }

        ReikaRenderHelper.enableLighting();
        stack.popPose();
    }

    private void renderCompass(BlockEntity tile, double x, double y, double z, VertexConsumer bufferSource, PoseStack stack) {
        // TODO: Port to 26.1 rendering API (Tesselator.getBuilder() + begin() + vertex().color().endVertex() + end() all removed)
    }

    private void addLine(Object buffer, Object pose, float x1, float y1, float z1, float x2, float y2, float z2, int[] rgb, float alpha) {
        // TODO: Port to 26.1 rendering API
    }

}

