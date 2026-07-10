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

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;

import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.farming.BlockEntityWoodcutter;
import reika.rotarycraft.models.animated.WoodcutterModel;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderWoodcutter extends RotaryTERenderer<BlockEntityWoodcutter> {

    private final WoodcutterModel model;

    public RenderWoodcutter(BlockEntityRendererProvider.Context context) {
        model = new WoodcutterModel(context.bakeLayer(RotaryModelLayers.WOODCUTTER));
    }

    private void renderWoodcutterAt(BlockEntityWoodcutter tile, PoseStack stack, VertexConsumer vc, int light) {
        stack.pushPose();
        float yaw = tile.getBlockState().getValue(BlockRotaryCraftMachine.FACING).toYRot();
        stack.translate(0.5F, 1.5F, 0.5F);
        stack.mulPose(Axis.ZP.rotationDegrees(180));
        stack.mulPose(Axis.YP.rotationDegrees(yaw));
        // The saw blade spins with the input shaft.
        model.renderAll(stack, vc, light, tile, null, tile.phi, 0);
        stack.popPose();
    }

    @Override
    protected Identifier getSubmitTexture(BlockEntity be) {
        return WoodcutterModel.TEXTURE_LOCATION;
    }

    @Override
    protected void renderModel(PoseStack stack, BlockEntity be, VertexConsumer vc, int light) {
        if (be instanceof BlockEntityWoodcutter wc)
            renderWoodcutterAt(wc, stack, vc, light);
    }
}
