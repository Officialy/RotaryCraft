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

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.fluids.FluidStack;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.production.BlockEntityPump;
import reika.rotarycraft.models.animated.PumpModel;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderPump extends RotaryTERenderer<BlockEntityPump> {
    private final PumpModel pumpModel;

    public RenderPump(BlockEntityRendererProvider.Context context) {
        pumpModel = new PumpModel(context.bakeLayer(RotaryModelLayers.PUMP));
    }

    public void renderBlockEntityPumpAt(PoseStack stack, BlockEntityPump tile, MultiBufferSource bufferSource, int packedLight) {
        stack.pushPose();
        stack.translate(0.5, 1.5, 0.5);
        // Orientation based on FACING property
        BlockState state = tile.getBlockState();
        Direction facing = state.getValue(BlockRotaryCraftMachine.FACING);
        float yRot = switch (facing) {
            case NORTH -> 180f;
            case SOUTH -> 0f;
            case WEST -> 90f;
            case EAST -> -90f;
            case UP, DOWN -> 0f;
        };
        stack.mulPose(Axis.YP.rotationDegrees(yRot + 90));
        stack.mulPose(Axis.ZP.rotationDegrees(180));
        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutout(PumpModel.TEXTURE_LOCATION));
        pumpModel.renderAll(stack, vertexconsumer, packedLight, tile, null, -tile.phi, 0);
        stack.popPose();
    }

    private void renderLiquid(PoseStack stack, BlockEntityPump tile, MultiBufferSource bufferSource, int packedLight) {
        FluidStack fs = tile.getLiquid();
        if (fs == null || fs.isEmpty()) return;
        var fluid = fs.getFluid();
        IClientFluidTypeExtensions props = IClientFluidTypeExtensions.of(fluid.getFluidType());
        double h = 0.3125 + 0.375 * tile.getFluidLevel() / BlockEntityPump.CAPACITY;
        stack.pushPose();
        RenderSystem.enableBlend();
        Minecraft.getInstance().textureManager.bindForSetup(props.getStillTexture());
        var m = stack.last().pose();
        var tesselator = com.mojang.blaze3d.vertex.Tesselator.getInstance();
        var v5 = tesselator.getBuilder();
        float u = 0;
        float v = 0;
        float du = 1;
        float dv = 1;
        v5.begin(com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS, com.mojang.blaze3d.vertex.DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL);
        int color = props.getTintColor(fs);
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;
        float a = 1f;
        float hf = (float) h;
        v5.vertex(m, 0.125f, hf, 0.875f).uv(u, dv).color(r, g, b, a).normal(0, 1, 0).endVertex();
        v5.vertex(m, 0.875f, hf, 0.875f).uv(du, dv).color(r, g, b, a).normal(0, 1, 0).endVertex();
        v5.vertex(m, 0.875f, hf, 0.125f).uv(du, v).color(r, g, b, a).normal(0, 1, 0).endVertex();
        v5.vertex(m, 0.125f, hf, 0.125f).uv(u, v).color(r, g, b, a).normal(0, 1, 0).endVertex();
        tesselator.end();
        RenderSystem.disableBlend();
        stack.popPose();
    }

    @Override
    public void render(BlockEntityPump tile, float partialTicks, PoseStack stack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (this.doRenderModel(stack, tile)) {
            this.renderBlockEntityPumpAt(stack, tile, bufferSource, packedLight);
        }
        if (tile.isInWorld()) {
            IORenderer.renderIO(stack, bufferSource, tile, tile.getBlockPos());
            this.renderLiquid(stack, tile, bufferSource, packedLight);
        }
    }
}
