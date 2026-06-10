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
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import reika.rotarycraft.models.animated.shaftonly.ShaftModel;
import reika.dragonapi.libraries.java.ReikaJavaLibrary;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.transmission.BlockEntityShaft;
import reika.rotarycraft.models.animated.shaftonly.CrossModel;
import reika.rotarycraft.models.animated.shaftonly.ShaftModel;
import reika.rotarycraft.models.animated.shaftonly.ShaftVModel;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderShaft extends RotaryTERenderer<BlockEntityShaft> {
    private final ShaftModel shaftModel;
    private final ShaftVModel VShaftModelt;
    private final CrossModel crossModel;

    public RenderShaft(BlockEntityRendererProvider.Context context) {
        shaftModel = new ShaftModel(context.bakeLayer(RotaryModelLayers.SHAFT));
        VShaftModelt = new ShaftVModel(context.bakeLayer(RotaryModelLayers.SHAFT_VERTICAL));
        crossModel = new CrossModel(context.bakeLayer(RotaryModelLayers.SHAFT_CROSS));
    }

    public void renderBlockEntityShaftAt(PoseStack stack, BlockEntityShaft tile, MultiBufferSource bufferSource, int pPackedLight) {
        stack.pushPose();
        if (tile.isInWorld()) {
            var failed = tile.failed();

            BlockState blockstate = tile.getLevel() != null ? tile.getBlockState() : RotaryBlocks.DYNAMOMETER.get().defaultBlockState().setValue(BlockRotaryCraftMachine.FACING, Direction.SOUTH);
            stack.translate(0.5F, 1.5F, 0.5F);
            stack.mulPose(Axis.ZP.rotationDegrees(180)); // Initial rotation to correct model orientation

            Direction facing = blockstate.getValue(BlockRotaryCraftMachine.FACING);
            if (facing.getAxis().isVertical()) {
                // No additional rotation needed for vertical shafts as they align with the Y-axis by default
            } else {
                float f = facing.toYRot();
                stack.mulPose(Axis.YP.rotationDegrees(-f + 90));
            }

            if (tile.isCross()) {
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderTypes.entityCutout(Identifier.parse(ShaftVModel.TEXTURE_LOCATION + getImageFileName(tile))));
                crossModel.renderAll(stack, vertexconsumer, pPackedLight, tile, ReikaJavaLibrary.makeListFrom(failed), tile.crossphi1, 0);
            } else if (tile.isVertical()) {
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderTypes.entityCutout(Identifier.parse(ShaftVModel.TEXTURE_LOCATION + getImageFileName(tile))));
                VShaftModelt.renderAll(stack, vertexconsumer, pPackedLight, tile, ReikaJavaLibrary.makeListFrom(failed), -tile.phi, 0);
            } else {
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderTypes.entityCutout(Identifier.parse(ShaftModel.TEXTURE_LOCATION + getImageFileName(tile))));
                shaftModel.renderAll(stack, vertexconsumer, pPackedLight, tile, ReikaJavaLibrary.makeListFrom(failed), -tile.phi, 0);
//                RotaryCraft.LOGGER.info("phi" + tile.phi);
            }
        }
        stack.popPose();
    }

    // 1.21.5: render -> submit. The shaft's three variants (straight / vertical / cross) all use
    // entityCutout with the same shaft-type texture, so a single RenderType per render call is
    // sufficient. We pre-compute the texture identifier outside the submitCustomGeometry callback
    // because submit() runs during the gather phase but the lambda fires later, after the outer
    // PoseStack would have been popped — snapshotting the pose into a fresh PoseStack keeps the
    // legacy renderBlockEntityShaftAt code (which does its own translates / rotations on top of
    // the world position the dispatcher already applied) working unmodified.
    @Override
    public void submit(BlockEntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;
        BlockEntity be = level.getBlockEntity(state.blockPos);
        if (!(be instanceof BlockEntityShaft tile)) return;
        if (!this.doRenderModel(poseStack, tile)) return;

        PoseStack snapped = new PoseStack();
        snapped.last().set(poseStack.last());

        // ShaftModel.TEXTURE_LOCATION is the directory part; append the per-material file name.
        RenderType rt = RenderTypes.entityCutout(textureWithSuffix(ShaftModel.TEXTURE_LOCATION, getImageFileName(tile)));
        int light = state.lightCoords;
        collector.submitCustomGeometry(poseStack, rt, (pose, vc) -> {
            MultiBufferSource oneRT = ignored -> vc;
            renderBlockEntityShaftAt(snapped, tile, oneRT, light);
        });
        // IO arrows for the shaft's read/write directions (debugFilledBox quads).
        if (tile.isInWorld()) {
            IORenderer.renderIO(poseStack, collector, tile, tile.getBlockPos());
        }
    }

    public static String getImageFileName(BlockEntity te) {
        BlockEntityShaft tile = (BlockEntityShaft) te;
        String tex = tile.getShaftType().getBaseShaftTexture();
        if (tile.isCross()) {
            tex = "crosstex.png";
        } else if (tile.isVertical()) {
            tex = "v" + tex;
        }
        return tex;
    }

}

