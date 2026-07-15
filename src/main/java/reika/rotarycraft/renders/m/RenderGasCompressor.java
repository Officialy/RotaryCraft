/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.renders.m;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;

import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.blockentities.storage.BlockEntityFluidCompressor;
import reika.rotarycraft.models.GasCompressorModel;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderGasCompressor extends RotaryTERenderer<BlockEntityFluidCompressor> {

    private final GasCompressorModel model;

    public RenderGasCompressor(BlockEntityRendererProvider.Context context) {
        model = new GasCompressorModel(context.bakeLayer(RotaryModelLayers.GASTANK));
    }

    private void renderGasTankAt(BlockEntityFluidCompressor tile, PoseStack stack, VertexConsumer vc, int light) {
        stack.pushPose();
        stack.translate(0.5F, 1.5F, 0.5F);
        stack.mulPose(Axis.ZP.rotationDegrees(180));
        model.renderAll(stack, vc, light, tile, null, tile.phi, 0);
        stack.popPose();
    }

    @Override
    protected Identifier getSubmitTexture(BlockEntity be) {
        return GasCompressorModel.TEXTURE_LOCATION;
    }

    @Override
    protected void renderModel(PoseStack stack, BlockEntity be, VertexConsumer vc, int light) {
        if (be instanceof BlockEntityFluidCompressor tank)
            renderGasTankAt(tank, stack, vc, light);
    }
}
