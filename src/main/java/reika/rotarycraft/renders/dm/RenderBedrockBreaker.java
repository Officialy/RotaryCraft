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
// 26.2: VertexConsumer removed; renderModel now takes VertexConsumer.
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.dragonapi.libraries.java.ReikaJavaLibrary;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.production.BlockEntityBedrockBreaker;
import reika.rotarycraft.models.animated.BedrockBreakerModel;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderBedrockBreaker extends RotaryTERenderer<BlockEntityBedrockBreaker> {

    private final BedrockBreakerModel model;

    public RenderBedrockBreaker(BlockEntityRendererProvider.Context context) {
        model = new BedrockBreakerModel(context.bakeLayer(RotaryModelLayers.BEDROCK_BREAKER));
    }

    public void renderBlockEntityBedrockBreakerAt(BlockEntityBedrockBreaker tile, PoseStack stack, VertexConsumer bufferSource, int light) {
        stack.pushPose();
        float f = tile.getBlockState().getValue(BlockRotaryCraftMachine.FACING).toYRot();
        stack.translate(0.5F, 1.5F, 0.5F);
        stack.mulPose(Axis.YP.rotationDegrees(f + 90));
        stack.mulPose(Axis.ZP.rotationDegrees(180));

        float grind = tile.isInWorld() ? tile.getGrindFraction() : 0;
        VertexConsumer vertexconsumer = bufferSource;
        model.renderAll(stack, vertexconsumer, light, tile, ReikaJavaLibrary.makeListFrom(tile.getStep(), grind), -tile.phi, 0);
        stack.popPose();
    }

    @Override
    protected Identifier getSubmitTexture(BlockEntity be) {
        return BedrockBreakerModel.TEXTURE_LOCATION;
    }

    @Override
    protected void renderModel(PoseStack stack, BlockEntity be, VertexConsumer vc, int light) {
        if (be instanceof BlockEntityBedrockBreaker breaker)
            renderBlockEntityBedrockBreakerAt(breaker, stack, vc, light);
    }

}
