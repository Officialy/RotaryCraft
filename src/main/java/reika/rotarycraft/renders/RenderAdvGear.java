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
import net.minecraft.world.level.block.state.BlockState;
import reika.dragonapi.libraries.java.ReikaJavaLibrary;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.base.RotaryModelBase;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.transmission.BlockEntityAdvancedGear;
import reika.rotarycraft.blockentities.transmission.BlockEntityAdvancedGear.GearType;
import reika.rotarycraft.models.CVTModel;
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
    private final CVTModel cvtModel;

    public RenderAdvGear(BlockEntityRendererProvider.Context context) {
        wormModel = new WormModel(context.bakeLayer(RotaryModelLayers.WORM));
        cvtModel = new CVTModel(context.bakeLayer(RotaryModelLayers.CVT));
    }

    /**
     * The advanced-gear family (worm / CVT / high / coil) shares one BER and one BE class; the
     * drawn model is chosen per {@link GearType}. Previously every type drew the worm model, so a
     * CVT in-world showed worm-gear geometry. HIGH/COIL still fall back to the worm model until
     * their models are wired through here.
     */
    private RotaryModelBase selectModel(BlockEntityAdvancedGear tile) {
        if (tile.getGearType() == GearType.CVT)
            return cvtModel;
        return wormModel;
    }

    private void renderAt(PoseStack stack, BlockEntityAdvancedGear tile, VertexConsumer bufferSource, int packedLight, RotaryModelBase model) {
        stack.pushPose();
        stack.translate(0.5, 1.5, 0.5);
        stack.mulPose(Axis.ZP.rotationDegrees(180));
        // Rotate around Y so the gear's axis aligns with the block's FACING. Matches
        // RenderShaft's facing→yRot mapping. Shared by all advanced-gear types (the legacy
        // renderer applied the same facing transform regardless of model).
        if (tile.isInWorld()) {
            BlockState st = tile.getBlockState();
            if (st != null && st.hasProperty(BlockRotaryCraftMachine.FACING)) {
                Direction facing = st.getValue(BlockRotaryCraftMachine.FACING);
                if (!facing.getAxis().isVertical()) {
                    float yRot = facing.toYRot();
                    stack.mulPose(Axis.YP.rotationDegrees(-yRot + 90));
                }
            }
        }
        VertexConsumer vc = bufferSource;
        model.renderAll(stack, vc, packedLight, tile, ReikaJavaLibrary.makeListFrom(false), -tile.phi, 0);
        stack.popPose();
    }

    @Override
    public void submit(BlockEntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;
        BlockEntity be = level.getBlockEntity(state.blockPos);
        if (!(be instanceof BlockEntityAdvancedGear tile)) return;
        if (!this.doRenderModel(poseStack, tile)) return;

        RotaryModelBase model = this.selectModel(tile);
        PoseStack snapped = new PoseStack();
        snapped.last().set(poseStack.last());
        // The render type must be bound to the same texture the model draws with, or the CVT would
        // be drawn into the worm's shaft-texture layer.
        RenderType rt = RenderTypes.entityCutout(model.getTexture());
        int light = state.lightCoords;
        collector.submitCustomGeometry(poseStack, rt, (pose, vc) -> {
            renderAt(snapped, tile, vc, light, model);
        });
        if (tile.isInWorld()) {
            IORenderer.renderIO(poseStack, collector, tile, tile.getBlockPos());
        }
    }
}
