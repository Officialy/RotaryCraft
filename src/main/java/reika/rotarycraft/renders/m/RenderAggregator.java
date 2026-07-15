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
import reika.rotarycraft.blockentities.production.BlockEntityAggregator;
import reika.rotarycraft.models.animated.AggregatorModel;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderAggregator extends RotaryTERenderer<BlockEntityAggregator> {

    private final AggregatorModel model;

    public RenderAggregator(BlockEntityRendererProvider.Context context) {
        model = new AggregatorModel(context.bakeLayer(RotaryModelLayers.AGGREGATOR));
    }

    private void renderAggregatorAt(BlockEntityAggregator tile, PoseStack stack, VertexConsumer vc, int light) {
        stack.pushPose();
        stack.translate(0.5F, 1.5F, 0.5F);
        stack.mulPose(Axis.ZP.rotationDegrees(180));
        // The condenser drum spins with the input shaft (phi).
        model.renderAll(stack, vc, light, tile, null, -tile.phi, 0);
        stack.popPose();
    }

    @Override
    protected Identifier getSubmitTexture(BlockEntity be) {
        return AggregatorModel.TEXTURE_LOCATION;
    }

    @Override
    protected void renderModel(PoseStack stack, BlockEntity be, VertexConsumer vc, int light) {
        if (be instanceof BlockEntityAggregator agg)
            renderAggregatorAt(agg, stack, vc, light);
    }
}
