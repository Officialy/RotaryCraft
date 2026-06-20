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
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.dragonapi.libraries.java.ReikaJavaLibrary;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.processing.BlockEntityMagnetizer;
import reika.rotarycraft.models.animated.MagnetizerModel;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderMagnetizer extends RotaryTERenderer<BlockEntityMagnetizer> {

    private final MagnetizerModel model;

    public RenderMagnetizer(BlockEntityRendererProvider.Context ctx) {
        model = new MagnetizerModel(ctx.bakeLayer(RotaryModelLayers.MAGNETIZER));
    }

    public void renderBlockEntityMagnetizerAt(BlockEntityMagnetizer tile, PoseStack stack, VertexConsumer bufferSource, int light) {
        stack.pushPose();

        // Facing-based Y rotation (original: meta 0=180°, 1=0°, 2=270°, 3=90°, then -90°)
        float yRot = tile.getBlockState().getValue(BlockRotaryCraftMachine.FACING).toYRot();
        stack.translate(0.5F, 1.5F, 0.5F);
        stack.mulPose(Axis.YP.rotationDegrees(yRot - 90));
        stack.mulPose(Axis.ZP.rotationDegrees(180.0F));

        VertexConsumer vc = bufferSource;
        model.renderAll(stack, vc, light, tile,
                ReikaJavaLibrary.makeListFrom(tile.hasCore()),
                tile.phi, 0);
        stack.popPose();
    }

    @Override
    protected Identifier getSubmitTexture(BlockEntity be) {
        return MagnetizerModel.TEXTURE_LOCATION;
    }

    @Override
    protected void renderModel(PoseStack stack, BlockEntity be, VertexConsumer mbs, int light) {
        if (be instanceof BlockEntityMagnetizer mag)
            renderBlockEntityMagnetizerAt(mag, stack, mbs, light);
    }
}
