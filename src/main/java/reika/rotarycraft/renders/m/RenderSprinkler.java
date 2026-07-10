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
import reika.rotarycraft.blockentities.farming.BlockEntitySprinkler;
import reika.rotarycraft.models.SprinklerModel;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderSprinkler extends RotaryTERenderer<BlockEntitySprinkler> {

    private final SprinklerModel model;

    public RenderSprinkler(BlockEntityRendererProvider.Context context) {
        model = new SprinklerModel(context.bakeLayer(RotaryModelLayers.SPRINKLER));
    }

    private void renderSprinklerAt(BlockEntitySprinkler tile, PoseStack stack, VertexConsumer vc, int light) {
        stack.pushPose();
        stack.translate(0.5F, 1.5F, 0.5F);
        stack.mulPose(Axis.ZP.rotationDegrees(180));
        // The head spins while spraying.
        model.renderAll(stack, vc, light, tile, null, tile.phi, 0);
        stack.popPose();
    }

    @Override
    protected Identifier getSubmitTexture(BlockEntity be) {
        return SprinklerModel.TEXTURE_LOCATION;
    }

    @Override
    protected void renderModel(PoseStack stack, BlockEntity be, VertexConsumer vc, int light) {
        if (be instanceof BlockEntitySprinkler s)
            renderSprinklerAt(s, stack, vc, light);
    }
}
