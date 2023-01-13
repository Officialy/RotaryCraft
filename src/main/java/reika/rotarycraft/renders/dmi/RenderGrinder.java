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
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.processing.BlockEntityGrinder;
import reika.rotarycraft.models.animated.GrinderModel;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderGrinder extends RotaryTERenderer<BlockEntityGrinder> {

    private GrinderModel modelGrinder;

    //private ModelGrinderV GrinderModelV = new ModelGrinderV();
    public RenderGrinder(BlockEntityRendererProvider.Context context) {
        modelGrinder = new GrinderModel(context.bakeLayer(RotaryModelLayers.GRINDER));
    }

    /**
     * Renders the BlockEntity for the position.
     */
    public void renderBlockEntityGrinderAt(BlockEntityGrinder tile, PoseStack stack, MultiBufferSource bufferSource, int light) {

        float f = tile.getBlockState().getValue(BlockRotaryCraftMachine.FACING).toYRot();
        stack.translate(0.5F, 1.5F, 0.5F);
        stack.mulPose(Axis.YP.rotationDegrees(-f));
        stack.mulPose(Axis.ZP.rotationDegrees(180));

        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entitySolid(GrinderModel.TEXTURE_LOCATION));
        modelGrinder.renderAll(stack, vertexconsumer, light, tile, null, -tile.phi); //-tile.getUpdateTag().getFloat("phi")
    }

    @Override
    public void render(BlockEntityGrinder tile, float p_112308_, PoseStack stack, MultiBufferSource bufferSource, int p_112311_, int p_112312_) {
        if (this.doRenderModel(stack, tile))
            this.renderBlockEntityGrinderAt(tile, stack, bufferSource, p_112311_);
        if (tile.isInWorld()) {// && MinecraftForgeClient.getRenderPass() == 1) {
            IORenderer.renderIO(stack, bufferSource, tile, tile.getBlockPos());
//            if (tile.getEnchantmentHandler().hasEnchantments())
//                EnchantmentRenderer.renderGlint(tile, modelGrinder, null, par2, par4, par6);
        }
    }
}
