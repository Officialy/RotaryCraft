/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.renders.mi;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;

import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.blockentities.processing.BlockEntityCentrifuge;
import reika.rotarycraft.models.animated.CentrifugeModel;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderCentrifuge extends RotaryTERenderer<BlockEntityCentrifuge> {

    private final CentrifugeModel model;

    public RenderCentrifuge(BlockEntityRendererProvider.Context context) {
        model = new CentrifugeModel(context.bakeLayer(RotaryModelLayers.CENTRIFUGE));
    }

    private void renderCentrifugeAt(BlockEntityCentrifuge tile, PoseStack stack, VertexConsumer vc, int light) {
        stack.pushPose();
        // Standard legacy model frame (translate y+2/z+1, scale(1,-1,-1), +0.5 recentre) — same
        // net transform as the grinder, minus the facing rotation (the centrifuge is vertical).
        stack.translate(0.5F, 1.5F, 0.5F);
        stack.mulPose(Axis.ZP.rotationDegrees(180));
        if (tile.isFlipped) {
            stack.mulPose(Axis.XP.rotationDegrees(180));
            stack.translate(0, -2, 0);
        }
        model.renderAll(stack, vc, light, tile, null, tile.phi, 0);
        stack.popPose();
    }

    @Override
    protected Identifier getSubmitTexture(BlockEntity be) {
        return CentrifugeModel.TEXTURE_LOCATION;
    }

    @Override
    protected void renderModel(PoseStack stack, BlockEntity be, VertexConsumer vc, int light) {
        if (be instanceof BlockEntityCentrifuge cent)
            renderCentrifugeAt(cent, stack, vc, light);
    }
}
