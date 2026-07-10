/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.renders.dmi;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;

import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.processing.BlockEntityCompactor;
import reika.rotarycraft.models.animated.CompactorModel;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderCompactor extends RotaryTERenderer<BlockEntityCompactor> {

    private final CompactorModel model;

    public RenderCompactor(BlockEntityRendererProvider.Context context) {
        model = new CompactorModel(context.bakeLayer(RotaryModelLayers.COMPACTOR));
    }

    private void renderCompactorAt(BlockEntityCompactor tile, PoseStack stack, VertexConsumer vc, int light) {
        stack.pushPose();
        float yaw = tile.getBlockState().hasProperty(BlockRotaryCraftMachine.FACING)
                ? tile.getBlockState().getValue(BlockRotaryCraftMachine.FACING).toYRot() : 0;
        stack.translate(0.5F, 1.5F, 0.5F);
        stack.mulPose(Axis.ZP.rotationDegrees(180));
        stack.mulPose(Axis.YP.rotationDegrees(yaw));
        // phi drives the press piston (oscillates 0.5..1.5 while working).
        model.renderAll(stack, vc, light, tile, null, tile.phi, 0);
        stack.popPose();
    }

    @Override
    protected Identifier getSubmitTexture(BlockEntity be) {
        return CompactorModel.TEXTURE_LOCATION;
    }

    @Override
    protected void renderModel(PoseStack stack, BlockEntity be, VertexConsumer vc, int light) {
        if (be instanceof BlockEntityCompactor c)
            renderCompactorAt(c, stack, vc, light);
    }
}
