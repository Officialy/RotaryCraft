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

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import reika.dragonapi.libraries.rendering.ReikaRenderHelper;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.auxiliary.RotaryAux;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.transmission.BlockEntityMonitor;
import reika.rotarycraft.models.MonitorModel;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderMonitor extends RotaryTERenderer<BlockEntityMonitor> {

    private final MonitorModel monitorModel;

    public RenderMonitor(BlockEntityRendererProvider.Context context) {
        monitorModel = new MonitorModel(context.bakeLayer(RotaryModelLayers.DYNOMONITOR));
    }

    public void renderBlockEntityMonitorAt(PoseStack stack, BlockEntityMonitor tile, MultiBufferSource bufferSource, int pPackedLight) {
        stack.pushPose();

        BlockState blockstate = tile.getLevel() != null ? tile.getBlockState() : RotaryBlocks.DYNAMOMETER.get().defaultBlockState().setValue(BlockRotaryCraftMachine.FACING, Direction.SOUTH);
        stack.translate(0.5F, -0.5F, 0.5F);
        float f = blockstate.getValue(BlockRotaryCraftMachine.FACING).toYRot();

        if (tile.isInWorld()) {
            stack.mulPose(Axis.XP.rotationDegrees(-f + 90));
        }
        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutout(MonitorModel.TEXTURE_LOCATION));
        monitorModel.renderAll(stack, vertexconsumer, pPackedLight, tile, null, -tile.phi, 0);
        stack.popPose();

        if (tile.isInWorld())
            this.renderText(tile, stack, bufferSource, blockstate);
    }

    private void renderText(BlockEntityMonitor tile, PoseStack stack, MultiBufferSource bufferSource, BlockState blockstate) {
        Direction dir = blockstate.getValue(BlockRotaryCraftMachine.FACING);
        ReikaRenderHelper.disableEntityLighting();
        stack.pushPose();
        stack.translate(0.5F, -0.5F, 0.5F);
        Font fontRenderer = this.getFontRenderer();
        float var10 = 0.8f;
        stack.scale(var10, var10, var10);
        float var112 = 0.016666668F * var10;
        stack.translate(0.0F, 3.65F * var10, 0.07F * var10);
        stack.scale(var112, -var112, var112);
        if (dir == Direction.EAST || dir == Direction.WEST) {
            stack.translate(0.5F, 0, 0.65F);
        }
        if (dir == Direction.NORTH || dir == Direction.SOUTH) {
            stack.mulPose(Axis.YP.rotationDegrees(90));
            stack.translate(2.5F, 0, 4.1F);
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//        RenderSystem.depthMask(false);
        stack.translate(5, -48, 37);
        String displayText;

        for (int i = 0; i < 2; i++) {
            stack.translate(-10 * i, 0, -37 * 2 * i - 9 * i);
            if (i == 1)
                stack.scale(-1, 1, 1);
            fontRenderer.drawInBatch("Power:", -37, 140, 0xffffff, false, stack.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
            displayText = RotaryAux.formatPower(tile.power);
            fontRenderer.drawInBatch(displayText, -28, 148, 0xffffff, false, stack.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);

            fontRenderer.drawInBatch("Torque:", -37, 164, 0xffffff, false, stack.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
            displayText = RotaryAux.formatTorque(tile.torque);
            fontRenderer.drawInBatch(displayText, -28, 172, 0xffffff, false, stack.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);

            fontRenderer.drawInBatch("Speed:", -37, 188, 0xffffff, false, stack.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
            displayText = RotaryAux.formatSpeed(tile.omega);
            fontRenderer.drawInBatch(displayText, -28, 196, 0xffffff, false, stack.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
        }
//        RenderSystem.depthMask(true);
        stack.popPose();
    }

    @Override
    public void render(BlockEntityMonitor tile, float v, PoseStack stack, MultiBufferSource bufferSource, int pPackedLight, int i1) {
        if (this.doRenderModel(stack, tile)) {
            this.renderBlockEntityMonitorAt(stack, tile, bufferSource, pPackedLight);
        }
        if ((tile).isInWorld())// && MinecraftForgeClient.getRenderPass() == 1)
            IORenderer.renderIO(stack, bufferSource, tile, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ());
    }
}
