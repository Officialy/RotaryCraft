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
import com.mojang.math.Axis;
import org.joml.Vector3f;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.level.BlockEntityFloodlight;
import reika.rotarycraft.models.LampModel;
import reika.rotarycraft.models.VLampModel;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderLamp extends RotaryTERenderer<BlockEntityFloodlight> {

    private final LampModel lampModel;
    private final VLampModel lampModelV;

    public RenderLamp(BlockEntityRendererProvider.Context context) {
        lampModel = new LampModel(context.bakeLayer(RotaryModelLayers.FLOODLIGHT));
        lampModelV = new VLampModel(context.bakeLayer(RotaryModelLayers.FLOODLIGHT_VERTICAL));
    }

    /**
     * Renders the BlockEntity for the position.
     */
    public void renderBlockEntityFloodlightAt(PoseStack stack, BlockEntityFloodlight tile, VertexConsumer bufferSource, int pPackedLight) {
        stack.pushPose();
//        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        stack.translate(0.5F, 1.5F, 0.5F);
        stack.mulPose(Axis.ZP.rotationDegrees(180));
        stack.mulPose(Axis.YP.rotationDegrees(180));
        Level level = tile.getLevel();
        boolean flag = level != null;
        BlockState blockstate = flag ? tile.getBlockState() : RotaryBlocks.FLOODLIGHT.get().defaultBlockState().setValue(BlockRotaryCraftMachine.FACING, Direction.SOUTH);

        if (tile.isInWorld() && !tile.beammode) {
            float f = blockstate.getValue(BlockRotaryCraftMachine.FACING).toYRot();
            stack.mulPose(Axis.YP.rotationDegrees(-f));
        }

        Direction facing = blockstate.getValue(BlockRotaryCraftMachine.FACING);
        if (tile.isInWorld() && (facing == Direction.DOWN || facing == Direction.UP)) {
            VertexConsumer vertexconsumer = bufferSource;
            lampModelV.renderToBuffer(stack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        } else {
            VertexConsumer vertexconsumer = bufferSource;
            lampModel.renderToBuffer(stack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        }

        stack.popPose();
    }

    @Override
    protected Identifier getSubmitTexture(BlockEntity be) {
        return LampModel.TEXTURE_LOCATION;
    }

    @Override
    protected boolean useEntityCutout() {
        return true;
    }

    @Override
    protected void renderModel(PoseStack stack, BlockEntity be, VertexConsumer mbs, int light) {
        if (be instanceof BlockEntityFloodlight lamp)
            renderBlockEntityFloodlightAt(stack, lamp, mbs, light);
    }
}

