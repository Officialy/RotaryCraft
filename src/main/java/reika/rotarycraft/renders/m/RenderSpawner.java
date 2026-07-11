/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.renders.m;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;

import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.blockentities.farming.BlockEntitySpawnerController;
import reika.rotarycraft.models.SpawnerModel;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderSpawner extends RotaryTERenderer<BlockEntitySpawnerController> {

    private final SpawnerModel model;

    public RenderSpawner(BlockEntityRendererProvider.Context context) {
        model = new SpawnerModel(context.bakeLayer(RotaryModelLayers.SPAWNER_CONTROLLER));
    }

    @Override
    protected Identifier getSubmitTexture(BlockEntity be) {
        return SpawnerModel.TEXTURE_LOCATION;
    }

    @Override
    protected void renderModel(PoseStack stack, BlockEntity be, VertexConsumer vc, int light) {
        if (!(be instanceof BlockEntitySpawnerController te))
            return;
        stack.pushPose();
        stack.translate(0.5F, 1.5F, 0.5F);
        stack.mulPose(Axis.ZP.rotationDegrees(180));
        model.renderAll(stack, vc, light, te, null, te.phi, 0);
        stack.popPose();
    }
}
