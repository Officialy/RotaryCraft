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
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import reika.dragonapi.libraries.ReikaAABBHelper;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.blockentities.farming.BlockEntityFan;
import reika.rotarycraft.models.animated.FanModel;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderFan extends RotaryTERenderer<BlockEntityFan> {

    private final FanModel fanModel;

    public RenderFan(BlockEntityRendererProvider.Context context) {
        fanModel = new FanModel(context.bakeLayer(RotaryModelLayers.FAN));
    }

    public void renderBlockEntityFanAt(PoseStack stack, BlockEntityFan tile, MultiBufferSource bufferSource, int packedLight) {
        stack.pushPose();
        stack.translate(0.5, 1.5, 0.5);
        // Orientation based on FACING property
        Direction facing = tile.getBlockState().getValue(reika.rotarycraft.base.blocks.BlockRotaryCraftMachine.FACING);
        float yRot = switch (facing) {
            case NORTH -> 180f;
            case SOUTH -> 0f;
            case WEST -> 90f;
            case EAST -> -90f;
            case UP -> 0f;
            case DOWN -> 0f;
        };
        stack.mulPose(Axis.YP.rotationDegrees(yRot));
        stack.mulPose(Axis.ZP.rotationDegrees(180));
        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutout(FanModel.TEXTURE_LOCATION));
        fanModel.renderAll(stack, vertexconsumer, packedLight, tile, null, -tile.phi);
        stack.popPose();
    }

    @Override
    public void render(BlockEntityFan tile, float partialTicks, PoseStack stack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (this.doRenderModel(stack, tile))
            this.renderBlockEntityFanAt(stack, tile, bufferSource, packedLight);
        if (tile.isInWorld()) {
            // Render IO overlays and AABB zones
            IORenderer.renderIO(stack, bufferSource, tile, tile.getBlockPos());
            AABB box = tile.getBlowZone(tile.getRange());
            AABB wide = tile.getWideBlowZone(tile.getRange());
            ReikaAABBHelper.renderAABB(stack, tile.wideAreaBlow ? wide : box, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ(), tile.iotick, 32, 192, 255, true);
            ReikaAABBHelper.renderAABB(stack, tile.wideAreaHarvest ? wide.move(0, 1, 0) : box, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ(), tile.iotick, 255, 255, 255, true);
        }
    }
}
