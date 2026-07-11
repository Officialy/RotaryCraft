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
import reika.rotarycraft.blockentities.farming.BlockEntityLawnSprinkler;
import reika.rotarycraft.models.animated.LawnSprinklerModel;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderLawnSprinkler extends RotaryTERenderer<BlockEntityLawnSprinkler> {

    private final LawnSprinklerModel model;

    public RenderLawnSprinkler(BlockEntityRendererProvider.Context context) {
        model = new LawnSprinklerModel(context.bakeLayer(RotaryModelLayers.LAWNSPRINKLER));
    }

    private void renderLawnSprinklerAt(BlockEntityLawnSprinkler tile, PoseStack stack, VertexConsumer vc, int light) {
        stack.pushPose();
        stack.translate(0.5F, 1.5F, 0.5F);
        stack.mulPose(Axis.ZP.rotationDegrees(180));
        // The head rotates while spraying (phi accelerates/decays client-side).
        model.renderAll(stack, vc, light, tile, null, tile.phi, 0);
        stack.popPose();
    }

    @Override
    protected Identifier getSubmitTexture(BlockEntity be) {
        return LawnSprinklerModel.TEXTURE_LOCATION;
    }

    @Override
    protected void renderModel(PoseStack stack, BlockEntity be, VertexConsumer vc, int light) {
        if (be instanceof BlockEntityLawnSprinkler s)
            renderLawnSprinklerAt(s, stack, vc, light);
    }
}
