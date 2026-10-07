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
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.Identifier;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.blockentities.transmission.BlockEntitySplitter;
import reika.rotarycraft.models.animated.SplitterModel;
import reika.rotarycraft.models.animated.SplitterModel2;
import reika.rotarycraft.registry.RotaryModelLayers;

import java.util.ArrayList;

public class RenderSplitter extends RotaryTERenderer<BlockEntitySplitter> {

    private final SplitterModel splitterModel;
    private final SplitterModel2 splitterModel2;
    private static final Identifier BEDROCK_TEXTURE = Identifier.fromNamespaceAndPath("rotarycraft",
            "textures/blockentitytex/transmission/bedsplittertex.png");

    public RenderSplitter(BlockEntityRendererProvider.Context context) {
        splitterModel = new SplitterModel(context.bakeLayer(RotaryModelLayers.SPLITTER));
        splitterModel2 = new SplitterModel2(context.bakeLayer(RotaryModelLayers.SPLITTER_2));
    }

    /**
     * Renders the BlockEntity for the position.
     */
    public void renderBlockEntitySplitterAt(PoseStack stack, BlockEntitySplitter tile, VertexConsumer bufferSource, int pPackedLight) {

        if (tile.isInWorld()) {
            applyWorldPose(stack, tile.getIoside());
        } else {
            // Keep the original out-of-world yaw; machine items supply their own outer pose.
            stack.rotate(Axis.YP.rotationDegrees(180F));
        }

        // Both V33a bend geometries animate their shafts and omit the gears after failure.
        ArrayList<Boolean> li = new ArrayList<>();
        li.add(tile.failed);
        reika.rotarycraft.base.RotaryModelBase model = tile.isInWorld() && usesMirroredModel(tile.getIoside())
                ? splitterModel2 : splitterModel;
        model.renderAll(stack, bufferSource, pPackedLight, tile, li, -tile.phi, 0);
    }

    /** V33a Techne axis conversion followed by the live junction orientation, in that order. */
    static void applyWorldPose(PoseStack stack, int ioside) {
        stack.translate(0.5F, 1.5F, 0.5F);
        stack.rotate(Axis.XP.rotationDegrees(180));
        stack.rotate(Axis.YP.rotationDegrees((ioside & 3) * 90 - 180));
    }

    static boolean usesMirroredModel(int ioside) {
        return (ioside & 7) >= 4;
    }

    // Legacy 1.7 signature retained as dead code; vanilla calls {@link #submit} instead.
    public void render(BlockEntitySplitter tile, float v, PoseStack stack, VertexConsumer multiBufferSource, int pPackedLight, int i1) {
        if (this.doRenderModel(stack, tile))
            this.renderBlockEntitySplitterAt(stack, tile, multiBufferSource, pPackedLight);
        if ((tile).isInWorld())
            IORenderer.renderIO(stack, multiBufferSource, tile, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ());
    }

    @Override
    public void submit(BlockEntityRenderState state,
                       PoseStack poseStack,
                       SubmitNodeCollector collector,
                       CameraRenderState camera) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;
        BlockEntity be = level.getBlockEntity(state.blockPos);
        if (!(be instanceof BlockEntitySplitter tile)) return;
        if (!this.doRenderModel(poseStack, tile)) return;

        PoseStack snapped = new PoseStack();
        snapped.last().set(poseStack.last());
        RenderType rt = RenderTypes.entityCutout(tile.isBedrock() ? BEDROCK_TEXTURE : SplitterModel.TEXTURE_LOCATION);
        int light = state.lightCoords;
        collector.submitCustomGeometry(poseStack, rt, (pose, vc) -> {
            renderBlockEntitySplitterAt(snapped, tile, vc, light);
        });
        if (tile.isInWorld()) {
            IORenderer.renderIO(poseStack, collector, tile, tile.getBlockPos());
        }
    }

}

