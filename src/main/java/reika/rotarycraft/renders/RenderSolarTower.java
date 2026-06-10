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
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.production.BlockEntitySolarTower;
import reika.rotarycraft.models.SolarTowerModel;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderSolarTower extends RotaryTERenderer<BlockEntitySolarTower> {

    private final SolarTowerModel modelSolar;

    public RenderSolarTower(BlockEntityRendererProvider.Context context) {
        modelSolar = new SolarTowerModel(context.bakeLayer(RotaryModelLayers.SOLAR_TOWER));
    }
    /**
     * Renders the BlockEntity for the position.
     */
    public void renderBlockEntitySolarAt(BlockEntitySolarTower tile, PoseStack stack, MultiBufferSource bufferSource, int packetLight) {
        BlockState blockstate = tile.getLevel() != null ? tile.getBlockState() : RotaryBlocks.SOLAR_TOWER.get().defaultBlockState().setValue(BlockRotaryCraftMachine.FACING, Direction.SOUTH);
        float f = blockstate.getValue(BlockRotaryCraftMachine.FACING).toYRot();
        stack.pushPose();
        stack.translate(0.5F, -0.5F, 0.5F);
        stack.mulPose(Axis.YP.rotationDegrees(f));
        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderTypes.entityCutout(SolarTowerModel.TEXTURE_LOCATION));
        modelSolar.renderToBuffer(stack, vertexconsumer, packetLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        stack.popPose();
    }

    @Override
    protected Identifier getSubmitTexture(BlockEntity be) {
        return SolarTowerModel.TEXTURE_LOCATION;
    }

    @Override
    protected boolean useEntityCutout() {
        return true;
    }

    @Override
    protected void renderModel(PoseStack stack, BlockEntity be, MultiBufferSource mbs, int light) {
        if (be instanceof BlockEntitySolarTower solar)
            renderBlockEntitySolarAt(solar, stack, mbs, light);
    }
}
