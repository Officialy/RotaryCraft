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
import org.joml.Quaternionf;
import org.joml.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import org.joml.Vector3f;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.transmission.BlockEntitySplitter;
import reika.rotarycraft.models.animated.SplitterModel;
import reika.rotarycraft.models.animated.SplitterModel2;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderSplitter extends RotaryTERenderer<BlockEntitySplitter> {

    private SplitterModel splitterModel;
    private SplitterModel2 splitterModel2;

    public RenderSplitter(BlockEntityRendererProvider.Context context) {
        splitterModel = new SplitterModel(context.bakeLayer(RotaryModelLayers.SPLITTER));
        splitterModel2 = new SplitterModel2(context.bakeLayer(RotaryModelLayers.SPLITTER_2));
    }

    /**
     * Renders the BlockEntity for the position.
     */
    public void renderBlockEntitySplitterAt(PoseStack stack, BlockEntitySplitter tile, MultiBufferSource bufferSource, int pPackedLight) {

        String s = tile.isBedrock() ? "bedsplittertex" : "splittertex";
//        this.bindTextureByName("/reika/rotarycraft/textures/blockentitytex/transmission/shaft/" + s + ".png");

//        this.setupGL(stack, tile, par2, par4, par6);
        if (tile.isInWorld()) {

            var level = tile.getLevel();
            var flag = level != null;
            var blockstate = flag ? tile.getBlockState() : RotaryBlocks.SPLITTER.get().defaultBlockState().setValue(BlockRotaryCraftMachine.FACING, Direction.SOUTH);

            float f = blockstate.getValue(BlockRotaryCraftMachine.FACING).toYRot();
            stack.mulPose(Axis.YP.rotationDegrees(-f));

//            glRotatef((float) var11 - 90, 0.0F, 1.0F, 0.0F);
        } else {
            stack.mulPose(new Quaternionf(180F, 0.0F, 1.0F, 0.0F));
        }

//        if (meta < 4 || (meta >= 8 && meta < 12))
//            var14.renderAll(stack, tile, ReikaJavaLibrary.makeListFrom(tile.failed), -tile.phi * dir, 0);
//        else
//            var15.renderToBuffer(stack, tile, ReikaJavaLibrary.makeListFrom(tile.failed), -tile.phi * dir, 0);

        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutout(SplitterModel.TEXTURE_LOCATION));
        splitterModel.renderToBuffer(stack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);

//        this.closeGL(stack, tile);
    }

    @Override
    public void render(BlockEntitySplitter tile, float v, PoseStack stack, MultiBufferSource multiBufferSource, int pPackedLight, int i1) {
        if (this.doRenderModel(stack, tile))
            this.renderBlockEntitySplitterAt(stack, tile, multiBufferSource, pPackedLight);
        if ((tile).isInWorld())// && MinecraftForgeClient.getRenderPass() == 1)
            IORenderer.renderIO(stack, multiBufferSource, tile, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ());
    }

}
