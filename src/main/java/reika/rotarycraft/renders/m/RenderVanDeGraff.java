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

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.blockentities.weaponry.BlockEntityVanDeGraff;
import reika.rotarycraft.models.VanDeGraffModel;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderVanDeGraff extends RotaryTERenderer<BlockEntityVanDeGraff> {
    private VanDeGraffModel vanDeGraffModel;

    public RenderVanDeGraff(BlockEntityRendererProvider.Context context) {
        vanDeGraffModel = new VanDeGraffModel(context.bakeLayer(RotaryModelLayers.VAN_DE_GRAFF));
    }

    /**
     * Renders the BlockEntity for the position.
     */
    public void renderBlockEntityVanDeGraffAt(PoseStack stack, BlockEntityVanDeGraff tile, MultiBufferSource bufferSource, int pPackedLight) {
        stack.pushPose();
//        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        stack.scale(1.0F, -1.0F, -1.0F);
        stack.translate(0.5F, -1.5F, -0.5F);

        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entitySolid(VanDeGraffModel.TEXTURE_LOCATION));
        vanDeGraffModel.renderToBuffer(stack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);

//        if (tile.isInWorld())
//            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        stack.popPose();
    }

    @Override
    public void render(BlockEntityVanDeGraff tile, float v, PoseStack stack, MultiBufferSource multiBufferSource, int i, int i1) {
        if (this.doRenderModel(stack, tile)) {
            this.renderBlockEntityVanDeGraffAt(stack, tile, multiBufferSource, i);
        }
        if ((tile).isInWorld())// && MinecraftForgeClient.getRenderPass() == 1)
            IORenderer.renderIO(stack, multiBufferSource, tile, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ());
    }


}
