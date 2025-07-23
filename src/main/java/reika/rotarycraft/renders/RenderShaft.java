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
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import reika.dragonapi.libraries.java.ReikaJavaLibrary;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.transmission.BlockEntityShaft;
import reika.rotarycraft.models.animated.shaftonly.CrossModel;
import reika.rotarycraft.models.animated.shaftonly.ShaftModel;
import reika.rotarycraft.models.animated.shaftonly.ShaftVModel;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderShaft extends RotaryTERenderer<BlockEntityShaft> {
    private final ShaftModel shaftModel;
    private final ShaftVModel VShaftModelt;
    private final CrossModel crossModel;

    public RenderShaft(BlockEntityRendererProvider.Context context) {
        shaftModel = new ShaftModel(context.bakeLayer(RotaryModelLayers.SHAFT));
        VShaftModelt = new ShaftVModel(context.bakeLayer(RotaryModelLayers.SHAFT_VERTICAL));
        crossModel = new CrossModel(context.bakeLayer(RotaryModelLayers.SHAFT_CROSS));
    }

    public void renderBlockEntityShaftAt(PoseStack stack, BlockEntityShaft tile, MultiBufferSource bufferSource, int pPackedLight) {
        stack.pushPose();
        if (tile.isInWorld()) {
            var failed = tile.failed();

            BlockState blockstate = tile.getLevel() != null ? tile.getBlockState() : RotaryBlocks.DYNAMOMETER.get().defaultBlockState().setValue(BlockRotaryCraftMachine.FACING, Direction.SOUTH);
            stack.translate(0.5F, 1.5F, 0.5F);
            stack.mulPose(Axis.ZP.rotationDegrees(180)); // Initial rotation to correct model orientation

            Direction facing = blockstate.getValue(BlockRotaryCraftMachine.FACING);
            if (facing.getAxis().isVertical()) {
                // No additional rotation needed for vertical shafts as they align with the Y-axis by default
            } else {
                float f = facing.toYRot();
                stack.mulPose(Axis.YP.rotationDegrees(-f + 90));
            }

            if (tile.isCross()) {
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutout(ResourceLocation.parse(ShaftVModel.TEXTURE_LOCATION + getImageFileName(tile))));
                crossModel.renderAll(stack, vertexconsumer, pPackedLight, tile, ReikaJavaLibrary.makeListFrom(failed), tile.crossphi1, 0);
            } else if (tile.isVertical()) {
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutout(ResourceLocation.parse(ShaftVModel.TEXTURE_LOCATION + getImageFileName(tile))));
                VShaftModelt.renderAll(stack, vertexconsumer, pPackedLight, tile, ReikaJavaLibrary.makeListFrom(failed), -tile.phi, 0);
            } else {
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutout(ResourceLocation.parse(ShaftModel.TEXTURE_LOCATION + getImageFileName(tile))));
                shaftModel.renderAll(stack, vertexconsumer, pPackedLight, tile, ReikaJavaLibrary.makeListFrom(failed), -tile.phi, 0);
//                RotaryCraft.LOGGER.info("phi" + tile.phi);
            }
        }
        stack.popPose();
    }

    @Override
    public void render(BlockEntityShaft tile, float pPartialTick, PoseStack stack, MultiBufferSource multiBufferSource, int pPackedLight, int pPackedOverlay) {
        if (this.doRenderModel(stack, tile))
            this.renderBlockEntityShaftAt(stack, tile, multiBufferSource, pPackedLight);
        if (tile.isInWorld())// && MinecraftForgeClient.getRenderPass() == 1)
            IORenderer.renderIO(stack, multiBufferSource, tile, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ());
    }

    public static String getImageFileName(BlockEntity te) {
        BlockEntityShaft tile = (BlockEntityShaft) te;
        String tex = tile.getShaftType().getBaseShaftTexture();
        if (tile.isCross()) {
            tex = "crosstex.png";
        } else if (tile.isVertical()) {
            tex = "v" + tex;
        }
        return tex;
    }

}
