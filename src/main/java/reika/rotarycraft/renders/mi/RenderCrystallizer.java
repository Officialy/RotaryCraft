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
import reika.rotarycraft.blockentities.processing.BlockEntityCrystallizer;
import reika.rotarycraft.models.animated.CrystallizerModel;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderCrystallizer extends RotaryTERenderer<BlockEntityCrystallizer> {

    private final CrystallizerModel model;

    public RenderCrystallizer(BlockEntityRendererProvider.Context context) {
        model = new CrystallizerModel(context.bakeLayer(RotaryModelLayers.CRYSTALLIZER));
    }

    @Override
    protected Identifier getSubmitTexture(BlockEntity be) {
        return CrystallizerModel.TEXTURE_LOCATION;
    }

    @Override
    protected void renderModel(PoseStack stack, BlockEntity be, VertexConsumer vc, int light) {
        if (!(be instanceof BlockEntityCrystallizer te))
            return;
        stack.pushPose();
        stack.translate(0.5F, 1.5F, 0.5F);
        stack.mulPose(Axis.ZP.rotationDegrees(180));
        // The chiller fan spins with the input shaft.
        model.renderAll(stack, vc, light, te, null, te.phi, 0);
        stack.popPose();
    }
}
