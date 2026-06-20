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
// 26.2: VertexConsumer removed from BER path; using VertexConsumer via submitCustomGeometry.
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
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

    public void renderBlockEntityFanAt(PoseStack stack, BlockEntityFan tile, VertexConsumer bufferSource, int packedLight) {
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
        VertexConsumer vertexconsumer = bufferSource;
        fanModel.renderAll(stack, vertexconsumer, packedLight, tile, null, -tile.phi);
        stack.popPose();
    }

    @Override
    protected Identifier getSubmitTexture(BlockEntity be) {
        return FanModel.TEXTURE_LOCATION;
    }

    @Override
    protected boolean useEntityCutout() {
        return true;
    }

    @Override
    protected void renderModel(PoseStack stack, BlockEntity be, VertexConsumer vc, int light) {
        if (be instanceof BlockEntityFan fan)
            renderBlockEntityFanAt(stack, fan, vc, light);
    }

}

