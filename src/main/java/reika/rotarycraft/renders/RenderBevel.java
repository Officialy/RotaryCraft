package reika.rotarycraft.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.phys.Vec3;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import reika.rotarycraft.registry.RotaryItems;
import java.util.List;
import java.util.ArrayList;

import reika.rotarycraft.auxiliary.MachineFaceOverlay;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.blockentities.transmission.BlockEntityBevelGear;
import reika.rotarycraft.models.animated.BevelModel;
import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderBevel extends RotaryTERenderer<BlockEntityBevelGear> {

    private final BevelModel bevelModel;

    public RenderBevel(BlockEntityRendererProvider.Context context) {
        bevelModel = new BevelModel(context.bakeLayer(RotaryModelLayers.BEVEL));
    }

    public static final class BevelRenderState extends BlockEntityRenderState {
        public boolean inWorld, modelAllowed, faceNumbers;
        public int direction, faceAlpha;
        public float phi;
        public List<IORenderer.Connection> connections = List.of();
    }
    @Override public BevelRenderState createRenderState() { return new BevelRenderState(); }

    @Override public void extractRenderState(BlockEntityBevelGear tile, BlockEntityRenderState base,
            float partialTick, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        super.extractRenderState(tile, base, partialTick, cameraPosition, breakProgress);
        var state = (BevelRenderState)base;
        state.inWorld = tile.isInWorld();
        state.modelAllowed = !state.inWorld || doRenderModel(new PoseStack(), tile);
        state.direction = tile.direction;
        state.phi = tile.phi;
        state.faceAlpha = state.inWorld ? tile.iotick : 0;
        state.faceNumbers = state.faceAlpha > 64 && ConfigRegistry.COLORBLIND.getState();
        var connections = new ArrayList<IORenderer.Connection>();
        if (state.inWorld) {
            var player = Minecraft.getInstance().player;
            // Preserve the original goggles refresh and face-marker-before-IO ordering.
            if (player != null && player.getItemBySlot(EquipmentSlot.HEAD).is(RotaryItems.IO_GOGGLES.get())) tile.iotick = 512;
            if (tile.iotick > 0) {
                for (var d : new net.minecraft.core.Direction[]{tile.getWriteDirection(), tile.getWriteDirection2()})
                    if (d != null) connections.add(IORenderer.Connection.capture(d.getStepX(), d.getStepY(), d.getStepZ(), tile.iotick, false));
                for (var d : new net.minecraft.core.Direction[]{tile.getReadDirection(), tile.getReadDirection2(), tile.getReadDirection3(), tile.getReadDirection4()})
                    if (d != null) connections.add(IORenderer.Connection.capture(d.getStepX()+tile.getPointingOffsetX(),
                            d.getStepY()+tile.getPointingOffsetY(), d.getStepZ()+tile.getPointingOffsetZ(), tile.iotick, true));
            }
        }
        state.connections = List.copyOf(connections);
    }

    public void renderBlockEntityBevelAt(PoseStack stack, BlockEntityBevelGear tile, VertexConsumer vertices, int light) {
        renderModel(stack, vertices, light, tile.direction, tile.phi, tile.isInWorld());
    }
    public void renderModel(PoseStack stack, VertexConsumer bufferSource, int light, int direction, float phi, boolean inWorld) {
        stack.pushPose();

        stack.translate(0.5, 1.5, 0.5);
        stack.scale(1.0F, -1.0F, -1.0F);

        int rotationY = 0;
        int rotationX = 0;
        int rotationZ = 0;
        int dir = 1;

        if (!inWorld) rotationY = 90;
        else switch (direction) {
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
            stack.rotate(Axis.YP.rotationDegrees(rotationY));
        if (rotationX != 0)
            stack.rotate(Axis.XP.rotationDegrees(rotationX));
        if (rotationZ != 0)
            stack.rotate(Axis.ZP.rotationDegrees(rotationZ));


        VertexConsumer vertexconsumer = bufferSource;
        bevelModel.renderParts(stack, vertexconsumer, light, phi * dir);
        stack.popPose();
    }

    @Override
    public void submit(BlockEntityRenderState base, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        var state = (BevelRenderState)base;
        int direction = state.direction, light = state.lightCoords;
        float phi = state.phi;
        boolean inWorld = state.inWorld;
        if (state.modelAllowed) collector.submitCustomGeometry(poseStack, RenderTypes.entityCutout(BevelModel.TEXTURE_LOCATION), (pose, vertices) -> {
            var captured = new PoseStack(); captured.last().set(pose);
            renderModel(captured, vertices, light, direction, phi, inWorld);
        });
        if (inWorld) {
            // The source's compass call was disabled; retain its geometry without enabling it.
            if (state.faceNumbers) renderFaceNumbers(poseStack, collector);
            else MachineFaceOverlay.submit(poseStack, collector, state.faceAlpha);
            for (var connection : state.connections) connection.submit(poseStack, collector);
        }
    }

    private void renderFaceNumbers(PoseStack stack, SubmitNodeCollector collector) {
        stack.pushPose();

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
            stack.rotate(Axis.XP.rotationDegrees(rx));
            stack.rotate(Axis.YP.rotationDegrees(ry));
            stack.rotate(Axis.ZP.rotationDegrees(rz));
            stack.translate(d, 0.28, l);
            stack.scale(scale, scale, scale);

            collector.submitText(stack, -Minecraft.getInstance().font.width(String.valueOf(i))/2.0F, 0,
                    FormattedCharSequence.forward(String.valueOf(i), Style.EMPTY),
                    false, Font.DisplayMode.NORMAL, 15728880, 0xFFFFFFFF, 0, 0);

            stack.popPose();
        }

        stack.popPose();
    }

    private void renderCompass(PoseStack stack, SubmitNodeCollector collector, int alpha) {
        MachineFaceOverlay.submitCompass(stack, collector, alpha);
    }
}
