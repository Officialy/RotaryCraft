/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.Quaternionf;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.blockentities.auxiliary.BlockEntityMirror;
import reika.rotarycraft.models.animated.MirrorModel;
import reika.rotarycraft.models.engine.WindModel;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderMirror extends RotaryTERenderer<BlockEntityMirror> {

    private MirrorModel mirrorModel;

    public RenderMirror(BlockEntityRendererProvider.Context pContext) {
        mirrorModel = new MirrorModel(pContext.bakeLayer(RotaryModelLayers.MIRROR));
    }

    /**
     * Renders the BlockEntity for the position.
     */
    public void renderBlockEntityMirrorAt(PoseStack stack, BlockEntityMirror tile, MultiBufferSource bufferSource, int pPackedLight) {
        stack.pushPose();
//        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        stack.translate((float) par2, (float) par4 + 2.0F, (float) par6 + 1.0F);
        stack.scale(1.0F, -1.0F, -1.0F);
        stack.translate(0.5F, 0.5F, 0.5F);
        int var11 = 1;     //used to rotate the model about metadata
        int var12 = 0;
        if (!tile.isInWorld()) {
            stack.mulPose(new Quaternionf(-90, 0, 1, 0));
        }
        stack.translate(0, var12, 0);
        stack.scale(1, var11, 1);
        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entitySolid((WindModel.TEXTURE_LOCATION)));
        mirrorModel.renderToBuffer(stack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        stack.scale(1, var11, 1);
        stack.translate(0, -var12, 0);

//        if (tile.isInWorld())
//            GL11.glDisable(GL12.GL_RESCALE_NORMAL);

        stack.popPose();
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void render(BlockEntityMirror tile, float v, PoseStack stack, MultiBufferSource multiBufferSource, int i, int i1) {
        if (this.doRenderModel(stack, tile))
            this.renderBlockEntityMirrorAt(stack, tile, multiBufferSource, i);
        if ((tile).isInWorld())// && MinecraftForge.getRenderType() == RenderType.solid())
            IORenderer.renderIO(stack, multiBufferSource, tile, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ());
    }

}
