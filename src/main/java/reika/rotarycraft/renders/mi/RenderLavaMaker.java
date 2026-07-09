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
import reika.rotarycraft.blockentities.processing.BlockEntityLavaMaker;
import reika.rotarycraft.models.animated.LavaMakerModel;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderLavaMaker extends RotaryTERenderer<BlockEntityLavaMaker> {

    private final LavaMakerModel model;

    public RenderLavaMaker(BlockEntityRendererProvider.Context context) {
        model = new LavaMakerModel(context.bakeLayer(RotaryModelLayers.LAVA_MAKER));
    }

    private void renderLavaMakerAt(BlockEntityLavaMaker tile, PoseStack stack, VertexConsumer vc, int light) {
        stack.pushPose();
        stack.translate(0.5F, 1.5F, 0.5F);
        stack.mulPose(Axis.ZP.rotationDegrees(180));
        model.renderAll(stack, vc, light, tile, null, tile.phi, 0);
        stack.popPose();
    }

    @Override
    protected Identifier getSubmitTexture(BlockEntity be) {
        return LavaMakerModel.TEXTURE_LOCATION;
    }

    @Override
    protected void renderModel(PoseStack stack, BlockEntity be, VertexConsumer vc, int light) {
        if (be instanceof BlockEntityLavaMaker lava)
            renderLavaMakerAt(lava, stack, vc, light);
    }
}
