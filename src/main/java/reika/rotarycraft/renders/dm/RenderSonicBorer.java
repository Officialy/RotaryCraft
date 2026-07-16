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
import reika.rotarycraft.blockentities.level.BlockEntitySonicBorer;
import reika.rotarycraft.models.SonicBorerModel;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderSonicBorer extends RotaryTERenderer<BlockEntitySonicBorer> {

    private final SonicBorerModel model;

    public RenderSonicBorer(BlockEntityRendererProvider.Context context) {
        model = new SonicBorerModel(context.bakeLayer(RotaryModelLayers.SONIC_BORER));
    }

    private void renderSonicBorerAt(BlockEntitySonicBorer tile, PoseStack stack, VertexConsumer vc, int light) {
        stack.pushPose();
        stack.translate(0.5F, 1.5F, 0.5F);
        // The model is built upside down; 1.7.10 righted it with glScalef(1, -1, -1), which is a
        // rotation about X, not Z. Rz(180) is Rx(180) times Ry(180), so flipping about Z here spun
        // the model an extra half-turn and pointed the barrel back down its own power input.
        stack.mulPose(Axis.XP.rotationDegrees(180));
        // Orient the barrel toward the machine's facing (the borer fires along FACING and takes
        // power from the opposite side). toYRot() reproduces the legacy metadata table exactly:
        // firing south/west/north/east -> 0/90/180/270.
        float yaw = tile.getBlockState().getValue(BlockRotaryCraftMachine.FACING).toYRot();
        stack.mulPose(Axis.YP.rotationDegrees(yaw));
        model.renderAll(stack, vc, light, tile, null, 0, 0);
        stack.popPose();
    }

    @Override
    protected Identifier getSubmitTexture(BlockEntity be) {
        return SonicBorerModel.TEXTURE_LOCATION;
    }

    @Override
    protected void renderModel(PoseStack stack, BlockEntity be, VertexConsumer vc, int light) {
        if (be instanceof BlockEntitySonicBorer borer)
            renderSonicBorerAt(borer, stack, vc, light);
    }
}
