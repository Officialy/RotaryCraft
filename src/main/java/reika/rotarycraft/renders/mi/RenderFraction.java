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
import reika.rotarycraft.blockentities.production.BlockEntityFractionator;
import reika.rotarycraft.models.FractionModel;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderFraction extends RotaryTERenderer<BlockEntityFractionator> {

    private final FractionModel model;

    public RenderFraction(BlockEntityRendererProvider.Context ctx) {
        model = new FractionModel(ctx.bakeLayer(RotaryModelLayers.FRACTIONATOR));
    }

    public void renderBlockEntityFractionatorAt(BlockEntityFractionator tile, PoseStack stack, VertexConsumer bufferSource, int light) {
        stack.pushPose();
        stack.translate(0.5F, 1.5F, 0.5F);
        stack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        model.renderAll(stack, bufferSource, light, tile, null, 0, 0);
        stack.popPose();
    }

    @Override
    protected Identifier getSubmitTexture(BlockEntity be) {
        return FractionModel.TEXTURE_LOCATION;
    }

    @Override
    protected void renderModel(PoseStack stack, BlockEntity be, VertexConsumer mbs, int light) {
        if (be instanceof BlockEntityFractionator frac)
            renderBlockEntityFractionatorAt(frac, stack, mbs, light);
    }
}
