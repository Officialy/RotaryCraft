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
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import org.joml.Quaternionf;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.blockentities.auxiliary.BlockEntityMirror;
import reika.rotarycraft.models.animated.MirrorModel;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderMirror extends RotaryTERenderer<BlockEntityMirror> {

    private final MirrorModel mirrorModel;

    public RenderMirror(BlockEntityRendererProvider.Context pContext) {
        mirrorModel = new MirrorModel(pContext.bakeLayer(RotaryModelLayers.MIRROR));
    }

    /**
     * Renders the BlockEntity for the position.
     */
    public void renderBlockEntityMirrorAt(PoseStack stack, BlockEntityMirror tile, VertexConsumer bufferSource, int pPackedLight) {
        stack.pushPose();
        // The legacy line translated to (renderPos + 0, +2, +1). In 1.21.5 the PoseStack is already
        // at the block origin, so only the (0, +2, +1) pivot offset must be re-applied here — it was
        // commented out during the port, which left the net transform at block+(0.5,-0.5,-0.5)
        // (below/behind the block, underground) so the mirror was invisible. With it restored the
        // net origin is block+(0.5,1.5,0.5) — the top-centre, matching every other machine renderer.
        stack.translate(0.0F, 2.0F, 1.0F);
        stack.scale(1.0F, -1.0F, -1.0F);
        stack.translate(0.5F, 0.5F, 0.5F);
        int var11 = 1;     //used to rotate the model about metadata
        int var12 = 0;
        if (!tile.isInWorld()) {
            stack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(-90));
        }
        stack.translate(0, var12, 0);
        stack.scale(1, var11, 1);
        VertexConsumer vertexconsumer = bufferSource;
        mirrorModel.renderToBuffer(stack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        stack.scale(1, var11, 1);
        stack.translate(0, -var12, 0);

//        if (tile.isInWorld())
//            GL11.glDisable(GL12.GL_RESCALE_NORMAL);

        stack.popPose();
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    protected Identifier getSubmitTexture(BlockEntity be) {
        return MirrorModel.TEXTURE_LOCATION;
    }

    @Override
    protected void renderModel(PoseStack stack, BlockEntity be, VertexConsumer mbs, int light) {
        if (be instanceof BlockEntityMirror mirror)
            renderBlockEntityMirrorAt(stack, mirror, mbs, light);
    }
}

