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

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.auxiliary.RotaryAux;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.transmission.BlockEntityMonitor;
import reika.rotarycraft.models.MonitorModel;
import reika.rotarycraft.registry.RotaryModelLayers;

/**
 * 1.21.5 port of the dynamometer renderer. Same pattern as the engine / shaft renderers —
 * single render type per submit, snapshot pose into a fresh PoseStack so the legacy in-lambda
 * code stays valid after the dispatcher has popped the outer stack.
 */
public class RenderMonitor extends RotaryTERenderer<BlockEntityMonitor> {

    private final MonitorModel monitorModel;

    public RenderMonitor(BlockEntityRendererProvider.Context context) {
        monitorModel = new MonitorModel(context.bakeLayer(RotaryModelLayers.DYNOMONITOR));
    }

    private void renderBlockEntityMonitorAt(PoseStack stack, BlockEntityMonitor tile, VertexConsumer bufferSource, int light) {
        stack.pushPose();
        BlockState state = tile.getBlockState();
        float yaw = state.getValue(BlockRotaryCraftMachine.FACING).toYRot();
        stack.translate(0.5F, 1.5F, 0.5F);
        stack.mulPose(Axis.YP.rotationDegrees(-yaw - 90));
        stack.mulPose(Axis.ZP.rotationDegrees(180));

        VertexConsumer vc = bufferSource;
        monitorModel.renderAll(stack, vc, light, tile, null, -tile.phi);
        stack.popPose();
    }

    @Override
    public void submit(BlockEntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;
        BlockEntity be = level.getBlockEntity(state.blockPos);
        if (!(be instanceof BlockEntityMonitor tile)) return;
        if (!this.doRenderModel(poseStack, tile)) return;

        PoseStack snapped = new PoseStack();
        snapped.last().set(poseStack.last());
        int light = state.lightCoords;

        RenderType rt = RenderTypes.entityCutout(MonitorModel.TEXTURE_LOCATION);
        collector.submitCustomGeometry(poseStack, rt, (pose, vc) -> {
            renderBlockEntityMonitorAt(snapped, tile, vc, light);
        });

        if (tile.isInWorld()) {
            renderReadout(poseStack, tile, collector, state.blockPos);
            // IO arrows for the dynamometer's read direction.
            IORenderer.renderIO(poseStack, collector, tile, tile.getBlockPos());
        }
    }

    private void renderReadout(PoseStack poseStack, BlockEntityMonitor tile, SubmitNodeCollector collector, BlockPos blockPos) {
        Font font = Minecraft.getInstance().font;
        String powerStr  = "Power: "  + RotaryAux.formatPower(tile.power);
        String torqueStr = "Torque: " + RotaryAux.formatTorque(tile.torque);
        String speedStr  = "Speed: "  + RotaryAux.formatSpeed(tile.omega);

        Direction facing = tile.getBlockState()
                .getValue(BlockRotaryCraftMachine.FACING);
        float facingYaw = facing.toYRot();

        FormattedCharSequence powerLine  =
                Component.literal(powerStr).getVisualOrderText();
        FormattedCharSequence torqueLine =
                Component.literal(torqueStr).getVisualOrderText();
        FormattedCharSequence speedLine  =
                Component.literal(speedStr).getVisualOrderText();

        int colour    = 0xFFFFFFFF;
        int light     = 15728880;
        var mode      = Font.DisplayMode.NORMAL;
        float scale   = 0.0125F;


        for (int side = 0; side < 2; side++) {
            PoseStack textStack = new PoseStack();
            textStack.last().set(poseStack.last());

            textStack.pushPose();

            textStack.translate(0.5F, 0.7F, 0.5F);
            float yaw = (side == 0) ? facingYaw : (facingYaw + 180F);
            textStack.mulPose(Axis.YP.rotationDegrees(yaw - 90F));
//            poseStack.mulPose(Axis.YP.rotationDegrees(-facingYaw - 90F));

            textStack.translate(0F, 0F, -0.51F);
            textStack.mulPose(Axis.ZP.rotationDegrees(180F));
            textStack.scale(scale, scale, scale);

            int lineSpacing = font.lineHeight + 2;
            float xPow  = -font.width(powerLine)  / 2F;
            float xTrq  = -font.width(torqueLine) / 2F;
            float xSpd  = -font.width(speedLine)  / 2F;

            collector.submitText(textStack, xPow, -lineSpacing,     powerLine,  false, mode, light, colour, 0, 0);
            collector.submitText(textStack, xTrq,  0,               torqueLine, false, mode, light, colour, 0, 0);
            collector.submitText(textStack, xSpd,  lineSpacing,     speedLine,  false, mode, light, colour, 0, 0);

            textStack.popPose();
        }
    }
}
