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

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.processing.BlockEntityGrinder;
import reika.rotarycraft.models.animated.GrinderModel;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderGrinder extends RotaryTERenderer<BlockEntityGrinder> {

    private final GrinderModel modelGrinder;

    //private ModelGrinderV GrinderModelV = new ModelGrinderV();
    public RenderGrinder(BlockEntityRendererProvider.Context context) {
        modelGrinder = new GrinderModel(context.bakeLayer(RotaryModelLayers.GRINDER));
    }

    /**
     * Renders the BlockEntity for the position.
     */
    public void renderBlockEntityGrinderAt(BlockEntityGrinder tile, PoseStack stack, VertexConsumer bufferSource, int light) {
        stack.pushPose();
        float f = tile.getBlockState().getValue(BlockRotaryCraftMachine.FACING).toYRot();
        stack.translate(0.5F, 1.5F, 0.5F);
        stack.mulPose(Axis.YP.rotationDegrees(f + 180));
        stack.mulPose(Axis.ZP.rotationDegrees(180));

        VertexConsumer vertexconsumer = bufferSource;
        modelGrinder.renderAll(stack, vertexconsumer, light, tile, null, -tile.phi, 0); //
        stack.popPose();
    }

    @Override
    protected Identifier getSubmitTexture(BlockEntity be) {
        return GrinderModel.TEXTURE_LOCATION;
    }

    @Override
    protected void renderModel(PoseStack stack, BlockEntity be, VertexConsumer mbs, int light) {
        if (be instanceof BlockEntityGrinder grinder)
            renderBlockEntityGrinderAt(grinder, stack, mbs, light);
    }
}

