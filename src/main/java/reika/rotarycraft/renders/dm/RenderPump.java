/*******************************************************************************
* @author Reika Kalseki
*
* Copyright 2017
*
* All rights reserved.
* Distribution of the software in any form is only allowed with
* explicit, prior permission from the owner.
******************************************************************************/
package reika.rotarycraft.renders.dm;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.production.BlockEntityPump;
import reika.rotarycraft.models.animated.PumpModel;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderPump extends RotaryTERenderer<BlockEntityPump> {
    private final PumpModel pumpModel;

    public RenderPump(BlockEntityRendererProvider.Context context) {
        pumpModel = new PumpModel(context.bakeLayer(RotaryModelLayers.PUMP));
    }

    public void renderBlockEntityPumpAt(PoseStack stack, BlockEntityPump tile, MultiBufferSource bufferSource, int packedLight) {
        stack.pushPose();
        stack.translate(0.5, 1.5, 0.5);
        // Orientation based on FACING property
        BlockState state = tile.getBlockState();
        Direction facing = state.getValue(BlockRotaryCraftMachine.FACING);
        float yRot = switch (facing) {
            case NORTH -> 180f;
            case SOUTH -> 0f;
            case WEST -> 90f;
            case EAST -> -90f;
            case UP, DOWN -> 0f;
        };
        stack.mulPose(Axis.YP.rotationDegrees(yRot + 90));
        stack.mulPose(Axis.ZP.rotationDegrees(180));
        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderTypes.entityCutout(PumpModel.TEXTURE_LOCATION));
        pumpModel.renderAll(stack, vertexconsumer, packedLight, tile, null, -tile.phi, 0);
        stack.popPose();
    }

    // 1.21.5 NOTE: Tesselator.getBuilder/Vertex.endVertex/RenderSystem.enable* / IClientFluidTypeExtensions.getStillTexture+getTintColor
    // have all been removed. Liquid rendering needs a rewrite against the new MeshData/BufferBuilder pipeline.
    private void renderLiquid(PoseStack stack, BlockEntityPump tile, MultiBufferSource bufferSource, int packedLight) {
    }

    // 1.21.5: BlockEntityRenderer.render → submit(BlockEntityRenderState, PoseStack, SubmitNodeCollector, CameraRenderState).
    // The vanilla draw pipeline drains queued submissions LATER, after submit() returns, so the
    // outer PoseStack may have been popped by then. We snapshot the current pose onto a fresh
    // PoseStack that the lambda captures by reference, and we present a tiny lambda
    // MultiBufferSource that always hands the existing renderBlockEntityPumpAt code the
    // VertexConsumer the collector gave us. The pump only ever requests one RenderType per
    // render call, so the single-RT MultiBufferSource is faithful.
    @Override
    public void submit(BlockEntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;
        BlockEntity be = level.getBlockEntity(state.blockPos);
        if (!(be instanceof BlockEntityPump tile)) return;
        if (!this.doRenderModel(poseStack, tile)) return;

        // Snapshot the pose: the lambda runs deferred, the original poseStack would be gone.
        PoseStack snapped = new PoseStack();
        snapped.last().set(poseStack.last());

        RenderType rt = RenderTypes.entityCutout(PumpModel.TEXTURE_LOCATION);
        int light = state.lightCoords;
        collector.submitCustomGeometry(poseStack, rt, (pose, vc) -> {
            MultiBufferSource oneRT = ignored -> vc;
            renderBlockEntityPumpAt(snapped, tile, oneRT, light);
        });
        // IO arrows (red/green direction overlays). IORenderer routes its own submitCustomGeometry
        // calls for the debugFilledBox quads, so we pass the same outer poseStack — the block's
        // origin is already applied by the dispatcher, and the box helper places each face cube
        // at the per-Direction stepX/Y/Z offset.
        if (tile.isInWorld()) {
            IORenderer.renderIO(poseStack, collector, tile, tile.getBlockPos());
        }
        // renderLiquid still TODO until the fluid quad helper is ported.
    }
}

