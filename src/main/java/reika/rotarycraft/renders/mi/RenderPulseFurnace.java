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
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.processing.BlockEntityPulseFurnace;
import reika.rotarycraft.models.PulseFurnaceModel;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderPulseFurnace extends RotaryTERenderer<BlockEntityPulseFurnace> {

    private final PulseFurnaceModel model;

    public RenderPulseFurnace(BlockEntityRendererProvider.Context context) {
        model = new PulseFurnaceModel(context.bakeLayer(RotaryModelLayers.PULSEJET));
    }

    public void renderBlockEntityPulseFurnaceAt(BlockEntityPulseFurnace tile, PoseStack stack, VertexConsumer bufferSource, int light) {
        stack.pushPose();
        float f = tile.getBlockState().getValue(BlockRotaryCraftMachine.FACING).toYRot();
        stack.translate(0.5F, 1.5F, 0.5F);
        stack.mulPose(Axis.YP.rotationDegrees(f + 180));
        stack.mulPose(Axis.ZP.rotationDegrees(180));

        VertexConsumer vertexconsumer = bufferSource;
        model.renderAll(stack, vertexconsumer, light, tile, null, 0, 0);
        stack.popPose();
    }

    @Override
    protected Identifier getSubmitTexture(BlockEntity be) {
        return PulseFurnaceModel.TEXTURE_LOCATION;
    }

    @Override
    protected void renderModel(PoseStack stack, BlockEntity be, VertexConsumer mbs, int light) {
        if (be instanceof BlockEntityPulseFurnace p)
            renderBlockEntityPulseFurnaceAt(p, stack, mbs, light);
    }
}
