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
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.BlockEntitySmokeDetector;
import reika.rotarycraft.models.SmokeDetectorModel;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderSmokeDetector extends RotaryTERenderer<BlockEntitySmokeDetector> {

    private final SmokeDetectorModel smokeDetectorModel;
    //private ModelSmokeDetectorV SmokeDetectorModelV = new ModelSmokeDetectorV();

    public RenderSmokeDetector(BlockEntityRendererProvider.Context context) {
        smokeDetectorModel = new SmokeDetectorModel(context.bakeLayer(RotaryModelLayers.SMOKE_DETECTOR));
    }

    /**
     * Renders the BlockEntity for the position.
     */
    public void renderBlockEntitySmokeDetectorAt(PoseStack stack, BlockEntitySmokeDetector tile, MultiBufferSource bufferSource, int pPackedLight) {
        stack.pushPose();

//        todo GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//     todo   GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        stack.scale(1.0F, -1.0F, -1.0F);
        stack.translate(0.5F, -1.5F, -0.5F);

        Level level = tile.getLevel();
        boolean flag = level != null;
        BlockState blockstate = flag ? tile.getBlockState() : RotaryBlocks.FLOODLIGHT.get().defaultBlockState().setValue(BlockRotaryCraftMachine.FACING, Direction.SOUTH);

        if (!tile.isInWorld()) {
//            stack.scale(2, 2, 2);
//            stack.translate(0, 0, 0);

            float f = blockstate.getValue(BlockRotaryCraftMachine.FACING).toYRot();
            stack.mulPose(Axis.YP.rotationDegrees(-f));
        }

        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutout(SmokeDetectorModel.TEXTURE_LOCATION));
        smokeDetectorModel.renderToBuffer(stack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);

//     todo       if (tile.isInWorld())
//                GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        stack.popPose();
//    todo        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void render(BlockEntitySmokeDetector tile, float pPartialTick, PoseStack stack, MultiBufferSource multiBufferSource, int pPackedLight, int pPackedOverlay) {
        if (this.doRenderModel(stack, tile)) {
            this.renderBlockEntitySmokeDetectorAt(stack, tile, multiBufferSource, pPackedLight);
//            RotaryCraft.LOGGER.info("Rendering Smoke Detector");
        }
        if (tile.hasLevel())
            IORenderer.renderIO(stack, multiBufferSource, tile, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ());
    }

}
