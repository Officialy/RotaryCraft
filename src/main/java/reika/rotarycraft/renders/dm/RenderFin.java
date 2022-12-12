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
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.auxiliary.BlockEntityCoolingFin;
import reika.rotarycraft.models.FinModel;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderFin extends RotaryTERenderer<BlockEntityCoolingFin> {

    private FinModel finModel;

    public RenderFin(BlockEntityRendererProvider.Context context) {
        finModel = new FinModel(context.bakeLayer(RotaryModelLayers.COOLING_FIN));
    }

    /**
     * Renders the BlockEntity for the position.
     */
    public void renderBlockEntityCoolingFinAt(PoseStack stack, BlockEntityCoolingFin tile, MultiBufferSource bufferSource, int pPackedLight) {
        stack.pushPose();
//        stack.scale(1.0F, -1.0F, -1.0F);


//        var14.renderAll(stack, tile, ReikaJavaLibrary.makeListFrom(tile.isInWorld(), tile.setting), -tile.phi, 0);
        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutout(FinModel.TEXTURE_LOCATION));
        finModel.renderToBuffer(stack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);

        stack.popPose();
    }

    @Override
    public void render(BlockEntityCoolingFin tile, float v, PoseStack stack, MultiBufferSource multiBufferSource, int i, int i1) {
        if (this.doRenderModel(stack, tile))
            this.renderBlockEntityCoolingFinAt(stack, tile, multiBufferSource, i1);
        if ((tile).isInWorld()) {// && MinecraftForgeClient.getRenderPass() == 1) {
            this.renderTarget(tile, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ());
        }

    }

    private void renderTarget(BlockEntityCoolingFin tile, double par2, double par4, double par6) {
        int[] xyz = tile.getTarget();
        AABB box = AABB.of(new BoundingBox(xyz[0], xyz[1], xyz[2], xyz[0] + 1, xyz[1] + 1, xyz[2] + 1)).expandTowards(0.03125, 0.03125, 0.03125);
//   todo     ReikaAABBHelper.renderAABB(box, par2, par4, par6, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ(), tile.ticks, 0, 127, 255, true);
    }

}