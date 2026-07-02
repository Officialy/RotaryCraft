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

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import reika.dragonapi.libraries.ReikaAABBHelper;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.auxiliary.BlockEntityCoolingFin;
import reika.rotarycraft.models.FinModel;
import reika.rotarycraft.registry.RotaryModelLayers;
import reika.rotarycraft.renders.RotaryRenderPipelines;

public class RenderFin extends RotaryTERenderer<BlockEntityCoolingFin> {

    private final FinModel finModel;

    public RenderFin(BlockEntityRendererProvider.Context context) {
        finModel = new FinModel(context.bakeLayer(RotaryModelLayers.COOLING_FIN));
    }

    public void renderBlockEntityCoolingFinAt(PoseStack stack, BlockEntityCoolingFin tile, VertexConsumer bufferSource, int pPackedLight) {
        stack.pushPose();
        // Original: glTranslatef(par2, par4 + 2.0, par6 + 1.0); — BER PoseStack is at block origin
        stack.translate(0.0, 2.0, 1.0);
        // Original: glScalef(1.0, -1.0, -1.0);
        stack.scale(1.0f, -1.0f, -1.0f);
        // Original: glTranslatef(0.5, 0.5, 0.5);
        stack.translate(0.5, 0.5, 0.5);

        if (tile.isInWorld()) {
            BlockState state = tile.getBlockState();
            if (state != null && state.hasProperty(BlockRotaryCraftMachine.FACING)) {
                Direction facing = state.getValue(BlockRotaryCraftMachine.FACING);
                // Original meta→var11 mapping:
                //   meta 0 (DOWN)=0, meta 1 (UP)=180, meta 2 (NORTH)=0,
                //   meta 3 (WEST)=90, meta 4 (SOUTH)=180, meta 5 (EAST)=270
                int var11;
                boolean isVertical;
                switch (facing) {
                    case DOWN  -> { var11 = 0;   isVertical = true; }
                    case UP    -> { var11 = 180; isVertical = true; }
                    case NORTH -> { var11 = 0;   isVertical = false; }
                    case WEST  -> { var11 = 90;  isVertical = false; }
                    case SOUTH -> { var11 = 180; isVertical = false; }
                    case EAST  -> { var11 = 270; isVertical = false; }
                    default    -> { var11 = 0;   isVertical = true; }
                }

                if (isVertical) {
                    // Original: if (meta < 2) { glRotatef(var11, 0, 0, 1); if (meta == 1) glTranslated(0, -2, 0); }
                    stack.mulPose(Axis.ZP.rotationDegrees(var11));
                    if (facing == Direction.UP) {
                        stack.translate(0, -2, 0);
                    }
                } else {
                    // Original: glRotatef(90, 1, 0, 0); glRotatef(var11, 0, 0, 1); glTranslated(0, -1, -1);
                    stack.mulPose(Axis.XP.rotationDegrees(90));
                    stack.mulPose(Axis.ZP.rotationDegrees(var11));
                    stack.translate(0, -1, -1);
                }
            }
        }

        VertexConsumer vertexconsumer = bufferSource;
        finModel.renderToBuffer(stack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        stack.popPose();
    }

    // Legacy 1.7 signature kept as dead code; vanilla calls {@link #submit} instead.
    public void render(BlockEntityCoolingFin tile, float v, PoseStack stack, VertexConsumer multiBufferSource, int i, int i1) {
        if (this.doRenderModel(stack, tile))
            this.renderBlockEntityCoolingFinAt(stack, tile, multiBufferSource, i);
        if ((tile).isInWorld()) {
            this.renderTarget(stack, tile);
        }
    }

    @Override
    public void submit(BlockEntityRenderState state,
                       PoseStack poseStack,
                       SubmitNodeCollector collector,
                       CameraRenderState camera) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;
        BlockEntity be = level.getBlockEntity(state.blockPos);
        if (!(be instanceof BlockEntityCoolingFin tile)) return;
        if (!this.doRenderModel(poseStack, tile)) return;

        PoseStack snapped = new PoseStack();
        snapped.last().set(poseStack.last());
        RenderType rt = RenderTypes.entityCutout(FinModel.TEXTURE_LOCATION);
        int light = state.lightCoords;
        collector.submitCustomGeometry(poseStack, rt, (pose, vc) -> {
            renderBlockEntityCoolingFinAt(snapped, tile, vc, light);
        });
        if (tile.isInWorld()) {
            IORenderer.renderIO(poseStack, collector, tile, tile.getBlockPos());
            if (tile.ticks > 0) {
                int[] xyz = tile.getTarget();
                AABB box = AABB.of(new BoundingBox(xyz[0], xyz[1], xyz[2], xyz[0] + 1, xyz[1] + 1, xyz[2] + 1))
                        .inflate(0.03125, 0.03125, 0.03125);
                ReikaAABBHelper.renderAABB(poseStack, collector, box,
                        tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ(),
                        tile.ticks, 0, 127, 255, true,
                        RotaryRenderPipelines.NO_DEPTH_FILLED_BOX_TYPE,
                        RotaryRenderPipelines.NO_DEPTH_LINES_TYPE);
            }
        }
    }

    private void renderTarget(PoseStack stack, BlockEntityCoolingFin tile) {
        int[] xyz = tile.getTarget();
        AABB box = AABB.of(new BoundingBox(xyz[0], xyz[1], xyz[2], xyz[0] + 1, xyz[1] + 1, xyz[2] + 1)).inflate(0.03125, 0.03125, 0.03125);
        ReikaAABBHelper.renderAABB(stack, box, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ(), tile.ticks, 0, 127, 255, true);
    }

}
