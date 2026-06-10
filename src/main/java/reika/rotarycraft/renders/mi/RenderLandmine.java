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
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.blockentities.weaponry.BlockEntityLandmine;
import reika.rotarycraft.models.LandmineModel;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderLandmine extends RotaryTERenderer<BlockEntityLandmine> {

    private final LandmineModel landmineModel;
    //private LandmineModelV LandmineModelV = new LandmineModelV();

    public RenderLandmine(BlockEntityRendererProvider.Context context) {
        landmineModel = new LandmineModel(context.bakeLayer(RotaryModelLayers.LANDMINE));
    }

    /**
     * Renders the BlockEntity for the position.
     */
    public void renderBlockEntityLandmineAt(PoseStack stack, BlockEntityLandmine tile, MultiBufferSource bufferSource, int pPackedLight) {
        stack.pushPose();
//        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        stack.translate(0.5F, 1.5F, 0.5F);
        stack.mulPose(Axis.ZP.rotationDegrees(180F));
        if (!tile.isInWorld()) {
            stack.scale(1.5F, 1.5F, 1.5F);
            stack.translate(0, -0.6, 0);
        }

        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderTypes.entityCutout(LandmineModel.TEXTURE_LOCATION));
        landmineModel.renderToBuffer(stack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);

//        if (tile.isInWorld())
//            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        stack.popPose();
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    protected Identifier getSubmitTexture(BlockEntity be) {
        return LandmineModel.TEXTURE_LOCATION;
    }

    @Override
    protected boolean useEntityCutout() {
        return true;
    }

    @Override
    protected void renderModel(PoseStack stack, BlockEntity be, MultiBufferSource mbs, int light) {
        if (be instanceof BlockEntityLandmine landmine)
            renderBlockEntityLandmineAt(stack, landmine, mbs, light);
    }
}

