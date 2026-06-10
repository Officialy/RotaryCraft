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
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.dragonapi.libraries.java.ReikaJavaLibrary;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.transmission.BlockEntityAdvancedGear;
import reika.rotarycraft.models.animated.WormModel;
import reika.rotarycraft.registry.RotaryModelLayers;

/**
 * 26.1 BER for the advanced-gear family (worm gear is the only block currently wired through
 * here; high-gear / CVT / coil use their own renderers). Was a no-op stub — the worm gear
 * never drew anything in-world. Mirrors the orient-by-FACING + animate-by-phi pattern from
 * {@link RenderShaft} / {@link reika.rotarycraft.renders.dm.RenderPump}: the WormModel's
 * {@code renderAll} already applies the X/Z-axis crank rotations from {@code phi}, so all the
 * BER needs to do is set up the right block-origin transform and submit.
 */
public class RenderAdvGear extends RotaryTERenderer<BlockEntityAdvancedGear> {

    private final WormModel wormModel;

    public RenderAdvGear(BlockEntityRendererProvider.Context context) {
        wormModel = new WormModel(context.bakeLayer(RotaryModelLayers.WORM));
    }

    private void renderAt(PoseStack stack, BlockEntityAdvancedGear tile, MultiBufferSource bufferSource, int packedLight) {
        stack.pushPose();
        stack.translate(0.5, 1.5, 0.5);
        stack.mulPose(Axis.ZP.rotationDegrees(180));
        // Rotate around Y so the worm's screw-axis aligns with the block's FACING. Matches
        // RenderShaft's facing→yRot mapping.
        if (tile.isInWorld()) {
            net.minecraft.world.level.block.state.BlockState st = tile.getBlockState();
            if (st != null && st.hasProperty(BlockRotaryCraftMachine.FACING)) {
                Direction facing = st.getValue(BlockRotaryCraftMachine.FACING);
                if (!facing.getAxis().isVertical()) {
                    float yRot = facing.toYRot();
                    stack.mulPose(Axis.YP.rotationDegrees(-yRot + 90));
                }
            }
        }
        VertexConsumer vc = bufferSource.getBuffer(RenderTypes.entityCutout(WormModel.TEXTURE_LOCATION));
        wormModel.renderAll(stack, vc, packedLight, tile, ReikaJavaLibrary.makeListFrom(false), -tile.phi, 0);
        stack.popPose();
    }

    @Override
    public void submit(BlockEntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;
        BlockEntity be = level.getBlockEntity(state.blockPos);
        if (!(be instanceof BlockEntityAdvancedGear tile)) return;
        if (!this.doRenderModel(poseStack, tile)) return;

        PoseStack snapped = new PoseStack();
        snapped.last().set(poseStack.last());
        RenderType rt = RenderTypes.entityCutout(WormModel.TEXTURE_LOCATION);
        int light = state.lightCoords;
        collector.submitCustomGeometry(poseStack, rt, (pose, vc) -> {
            MultiBufferSource oneRT = ignored -> vc;
            renderAt(snapped, tile, oneRT, light);
        });
        if (tile.isInWorld()) {
            IORenderer.renderIO(poseStack, collector, tile, tile.getBlockPos());
        }
    }
}
