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
import reika.rotarycraft.blockentities.BlockEntityVacuum;
import reika.rotarycraft.models.VacuumModel;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderVacuum extends RotaryTERenderer<BlockEntityVacuum> {

    private final VacuumModel model;

    public RenderVacuum(BlockEntityRendererProvider.Context context) {
        model = new VacuumModel(context.bakeLayer(RotaryModelLayers.VACCUUM));
    }

    private void renderVacuumAt(BlockEntityVacuum tile, PoseStack stack, VertexConsumer vc, int light) {
        stack.pushPose();
        // Standard legacy model frame (y+2/z+1, scale(1,-1,-1), +0.5 recentre) — the vacuum is a
        // static, non-directional machine, so no facing rotation.
        stack.translate(0.5F, 1.5F, 0.5F);
        stack.mulPose(Axis.ZP.rotationDegrees(180));
        model.renderAll(stack, vc, light, tile, null, 0, 0);
        stack.popPose();
    }

    @Override
    protected Identifier getSubmitTexture(BlockEntity be) {
        return VacuumModel.TEXTURE_LOCATION;
    }

    @Override
    protected void renderModel(PoseStack stack, BlockEntity be, VertexConsumer vc, int light) {
        if (be instanceof BlockEntityVacuum vac)
            renderVacuumAt(vac, stack, vc, light);
    }
}
