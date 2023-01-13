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
        int var9;

       /*if (!tile.isInWorld())
            var9 = 0;
        else
            var9 = tile.getBlockMetadata();*/

//        this.bindTextureByName("/Reika/RotaryCraft/Textures/TileEntityTex/Converter/magneticmotortex.png");

//        this.setupGL(tile, par2, par4, par6);

        /*int var11 = 0;
        float var13;
        switch (var9) {
            case 2:
                var11 = 0;
                break;
            case 0:
                var11 = 180;
                break;
            case 1:
                var11 = 90;
                break;
            case 3:
                var11 = 270;
                break;
        }*/

        Level level = tile.getLevel();
        boolean flag = level != null;
        BlockState blockstate = flag ? tile.getBlockState() : RotaryBlocks.ENGINE.get().defaultBlockState().setValue(BlockRotaryCraftMachine.FACING, Direction.SOUTH);

        float f = blockstate.getValue(BlockRotaryCraftMachine.FACING).toYRot();

        stack.translate(0.5F, 1.5F, 0.5F);
        stack.scale(-1.0F, -1.0F, -1.0F);
        stack.mulPose(Axis.YP.rotationDegrees(-f));
        stack.scale(1.0F, 1.0F, 1.0F);

        VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityCutout(MagneticModel.TEXTURE_LOCATION));
        dynamoModel.renderToBuffer(stack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
//        stack.mulPose(var11 + 180, 0.0F, 1.0F, 0.0F);
//        var14.renderAll(tile, null, -tile.phi, 0);
//        this.closeGL(tile);
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

