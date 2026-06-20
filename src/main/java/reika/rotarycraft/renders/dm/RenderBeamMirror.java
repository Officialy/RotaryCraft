/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.renders.dm;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import reika.dragonapi.libraries.java.ReikaJavaLibrary;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.level.BlockEntityBeamMirror;
import reika.rotarycraft.models.animated.BeamMirrorModel;
import reika.rotarycraft.registry.RotaryModelLayers;

/**
 * 26.1 BER for the beam mirror. The legacy port left this entirely commented out (and unregistered),
 * so the beam mirror drew nothing in-world. Mirrors {@link reika.rotarycraft.renders.RenderMirror}:
 * goes through the base {@link RotaryTERenderer} submit pipeline via {@link #getSubmitTexture} /
 * {@link #renderModel}, restores the legacy {@code (0,+2,+1)} pivot offset (without it the model
 * lands below/behind the block, underground), and applies the facing-based yaw the heliostat needs.
 */
public class RenderBeamMirror extends RotaryTERenderer<BlockEntityBeamMirror> {

    private final BeamMirrorModel beamMirrorModel;

    public RenderBeamMirror(BlockEntityRendererProvider.Context context) {
        beamMirrorModel = new BeamMirrorModel(context.bakeLayer(RotaryModelLayers.BEAM_MIRROR));
    }

    private void renderAt(PoseStack stack, BlockEntityBeamMirror tile, VertexConsumer bufferSource, int light) {
        stack.pushPose();
        // (0,+2,+1) pivot offset + flip, matching the legacy transform; the block-origin part of the
        // legacy translate is now handled by the PoseStack. Net origin = block+(0.5,1.5,0.5).
        stack.translate(0.0F, 2.0F, 1.0F);
        stack.scale(1.0F, -1.0F, -1.0F);
        stack.translate(0.5F, 0.5F, 0.5F);
        if (tile.isInWorld()) {
            BlockState st = tile.getBlockState();
            if (st != null && st.hasProperty(BlockRotaryCraftMachine.FACING)) {
                int yaw = switch (st.getValue(BlockRotaryCraftMachine.FACING)) {
                    case EAST -> 180;
                    case SOUTH -> 270;
                    case NORTH -> 90;
                    default -> 0; // WEST and any vertical
                };
                stack.mulPose(Axis.YP.rotationDegrees(yaw + 90));
            }
        }
        VertexConsumer vc = bufferSource;
        beamMirrorModel.renderAll(stack, vc, light, tile, ReikaJavaLibrary.makeListFrom(false), -tile.phi, 0);
        stack.popPose();
    }

    @Override
    protected Identifier getSubmitTexture(BlockEntity be) {
        return BeamMirrorModel.TEXTURE_LOCATION;
    }

    @Override
    protected void renderModel(PoseStack stack, BlockEntity be, VertexConsumer mbs, int light) {
        if (be instanceof BlockEntityBeamMirror mirror)
            renderAt(stack, mirror, mbs, light);
    }
}
