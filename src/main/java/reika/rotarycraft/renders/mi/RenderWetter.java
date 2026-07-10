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
import reika.rotarycraft.blockentities.processing.BlockEntityWetter;
import reika.rotarycraft.models.animated.WetterModel;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderWetter extends RotaryTERenderer<BlockEntityWetter> {

    private final WetterModel model;

    public RenderWetter(BlockEntityRendererProvider.Context context) {
        model = new WetterModel(context.bakeLayer(RotaryModelLayers.WETTER));
    }

    @Override
    protected Identifier getSubmitTexture(BlockEntity be) {
        return WetterModel.TEXTURE_LOCATION;
    }

    @Override
    protected void renderModel(PoseStack stack, BlockEntity be, VertexConsumer vc, int light) {
        if (!(be instanceof BlockEntityWetter te))
            return;
        stack.pushPose();
        stack.translate(0.5F, 1.5F, 0.5F);
        stack.mulPose(Axis.ZP.rotationDegrees(180));
        model.renderAll(stack, vc, light, te, null, te.phi, 0);
        stack.popPose();
    }
}
