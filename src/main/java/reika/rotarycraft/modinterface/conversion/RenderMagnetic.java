/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.modinterface.conversion;

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
import org.joml.Vector3f;
import reika.dragonapi.libraries.java.ReikaJavaLibrary;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.modinterface.model.MagneticModel;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderMagnetic extends RotaryTERenderer<BlockEntityMagnetEngine> {

    private MagneticModel dynamoModel;

    public RenderMagnetic(BlockEntityRendererProvider.Context context) {
        dynamoModel = new MagneticModel(context.bakeLayer(RotaryModelLayers.MAGNETIC));
    }

    public void renderBlockEntityDynamoAt(PoseStack stack, BlockEntityMagnetEngine tile, MultiBufferSource buffer, int pPackedLight) {
        stack.pushPose();
        Level level = tile.getLevel();
        boolean flag = level != null;
        BlockState blockstate = flag ? tile.getBlockState() : RotaryBlocks.MAGNETOSTATIC_ENGINE.get().defaultBlockState().setValue(BlockRotaryCraftMachine.FACING, Direction.SOUTH);

        stack.translate(0.5F, 1.5F, 0.5F);
        stack.mulPose(Axis.ZP.rotationDegrees(180));
        stack.mulPose(Axis.YP.rotationDegrees(180));

        if (tile.isInWorld()) {
            float f = blockstate.getValue(BlockRotaryCraftMachine.FACING).toYRot();
            stack.mulPose(Axis.YP.rotationDegrees(f));
        }

        VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityCutout(MagneticModel.TEXTURE_LOCATION));
        dynamoModel.renderAll(stack, vertexconsumer, pPackedLight, tile, null, tile.phi, 0);
        stack.popPose();
    }

    @Override
    public void render(BlockEntityMagnetEngine tile, float p_112308_, PoseStack stack, MultiBufferSource multiBufferSource, int light, int p_112312_) {
        if (this.doRenderModel(stack, tile))
            this.renderBlockEntityDynamoAt(stack, tile, multiBufferSource, light);
        if (tile.isInWorld()) {// && MinecraftForgeClient.getRenderPass() == 1) {
            IORenderer.renderIO(stack, multiBufferSource, tile, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ());
        }
    }

}

