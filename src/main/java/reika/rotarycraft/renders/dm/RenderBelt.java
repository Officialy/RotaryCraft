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
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;

import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.transmission.BlockEntityBeltHub;
import reika.rotarycraft.models.animated.BeltModel;
import reika.rotarycraft.registry.RotaryModelLayers;

/**
 * Belt/chain hub BER: the hub wheel, spun by phi, yawed to the hub facing. 26.2 port of the legacy
 * RenderBelt; the legacy long belt-run geometry drawn between paired hubs (stretched quads over the
 * span) is not yet ported -- hubs render individually (TODO: belt-run strip via the submit
 * pipeline, like the pipe shells).
 */
public class RenderBelt extends RotaryTERenderer<BlockEntityBeltHub> {

    private final BeltModel model;

    public RenderBelt(BlockEntityRendererProvider.Context context) {
        model = new BeltModel(context.bakeLayer(RotaryModelLayers.BELT));
    }

    private void renderBeltHubAt(BlockEntityBeltHub tile, PoseStack stack, VertexConsumer vc, int light) {
        stack.pushPose();
        stack.translate(0.5F, 1.5F, 0.5F);
        stack.mulPose(Axis.ZP.rotationDegrees(180));
        var state = tile.getBlockState();
        var facing = state.hasProperty(BlockRotaryCraftMachine.FACING)
                ? state.getValue(BlockRotaryCraftMachine.FACING) : net.minecraft.core.Direction.NORTH;
        if (facing.getAxis().isHorizontal()) {
            stack.mulPose(Axis.YP.rotationDegrees(facing.toYRot() + 90));
        }
        else {
            // Vertical hubs: legacy meta 4/5 pitched the model over.
            stack.mulPose(Axis.XP.rotationDegrees(facing == net.minecraft.core.Direction.UP ? 90 : -90));
        }
        model.renderAll(stack, vc, light, tile, null, tile.phi, 0);
        stack.popPose();
    }

    @Override
    protected Identifier getSubmitTexture(BlockEntity be) {
        return BeltModel.TEXTURE_LOCATION;
    }

    @Override
    protected void renderModel(PoseStack stack, BlockEntity be, VertexConsumer vc, int light) {
        if (be instanceof BlockEntityBeltHub hub)
            renderBeltHubAt(hub, stack, vc, light);
    }
}
