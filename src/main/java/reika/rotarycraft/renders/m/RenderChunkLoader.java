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

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.blockentities.BlockEntityChunkLoader;
import reika.rotarycraft.models.animated.ChunkLoaderModel;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderChunkLoader extends RotaryTERenderer<BlockEntityChunkLoader> {
    private final ChunkLoaderModel model;
    public RenderChunkLoader(BlockEntityRendererProvider.Context context) { model = new ChunkLoaderModel(context.bakeLayer(RotaryModelLayers.CHUNK_LOADER)); }
    @Override protected Identifier getSubmitTexture(BlockEntity be) { return ChunkLoaderModel.TEXTURE_LOCATION; }
    @Override protected void renderModel(PoseStack pose, BlockEntity be, VertexConsumer vertices, int light) {
        pose.pushPose();
        pose.translate(0.5, 1.5, 0.5);
        pose.rotate(Axis.ZP.rotationDegrees(180));
        model.renderAll(pose, vertices, light, be, null, ((BlockEntityChunkLoader)be).phi, 0);
        pose.popPose();
    }
}
