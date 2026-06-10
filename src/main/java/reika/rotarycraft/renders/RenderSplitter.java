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
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
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

    private final SplitterModel splitterModel;
    private final SplitterModel2 splitterModel2;

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

        if (tile.isInWorld()) {
            // 26.1 facing fix (round 4): the splitter's model rotation needs to follow the
            // BE's WRITE direction (derived from {@code ioside}), NOT the static FACING
            // property. When the player rotates with the screwdriver, {@code ioside} changes
            // and so does the actual output side — but the FACING blockstate doesn't, so a
            // FACING-based rotation went out of sync with the IO arrows. Pull the live
            // write direction off the BE instead. Falls back to SOUTH for item-form rendering.
            Direction writeDir = tile.getWriteDirection();
            if (writeDir == null || writeDir.getAxis() == Direction.Axis.Y) writeDir = Direction.SOUTH;
            float f = writeDir.toYRot();
            // Legacy 1.7 mapping in toYRot() terms (write-direction = the splitter's "front"):
            //   WEST  (90)  → 180°
            //   NORTH (180) → 270°
            //   EAST  (270) → 0°
            //   SOUTH (0)   → 90°
            // which is {@code f + 90} mod 360. The Z-axis 180° flip preserves the engine-
            // style upside-down mounting.
            stack.translate(0.5F, 1.5F, 0.5F);
            stack.mulPose(Axis.YP.rotationDegrees(f + 90));
            stack.mulPose(Axis.ZP.rotationDegrees(180));
        } else {
            // 1.21.5 fix: {@code new Quaternionf(180,0,1,0)} treats args as (x,y,z,w) — a
            // non-normalised quaternion that collapses the item-form model. Use a real Y-axis
            // rotation so the splitter item icon points the right way.
            stack.mulPose(Axis.YP.rotationDegrees(180F));
        }

//        if (meta < 4 || (meta >= 8 && meta < 12))
//            var14.renderAll(stack, tile, ReikaJavaLibrary.makeListFrom(tile.failed), -tile.phi * dir, 0);
//        else
//            var15.renderToBuffer(stack, tile, ReikaJavaLibrary.makeListFrom(tile.failed), -tile.phi * dir, 0);

        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderTypes.entityCutout(SplitterModel.TEXTURE_LOCATION));
        // 26.1 fix: legacy port called {@code renderToBuffer} (root-only static draw) — so the
        // splitter's cross-shafts never spun. Route through {@link SplitterModel#renderAll}
        // which applies the {@code phi} rotation to the rotating sub-parts. Pass {@code -tile.phi}
        // to match the direction convention used by the shaft/gearbox renderers.
        // Pass the {@code failed} flag in the conditions list (legacy: li.get(0)) so the
        // shafts stop spinning when the splitter has been killed by torque overload.
        java.util.ArrayList<Boolean> li = new java.util.ArrayList<>();
        li.add(tile.failed);
        splitterModel.renderAll(stack, vertexconsumer, pPackedLight, tile, li, -tile.phi, 0);

//        this.closeGL(stack, tile);
    }

    // Legacy 1.7 signature retained as dead code; vanilla calls {@link #submit} instead.
    public void render(BlockEntitySplitter tile, float v, PoseStack stack, MultiBufferSource multiBufferSource, int pPackedLight, int i1) {
        if (this.doRenderModel(stack, tile))
            this.renderBlockEntitySplitterAt(stack, tile, multiBufferSource, pPackedLight);
        if ((tile).isInWorld())
            IORenderer.renderIO(stack, multiBufferSource, tile, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ());
    }

    @Override
    public void submit(net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState state,
                       PoseStack poseStack,
                       net.minecraft.client.renderer.SubmitNodeCollector collector,
                       net.minecraft.client.renderer.state.level.CameraRenderState camera) {
        net.minecraft.world.level.Level level = net.minecraft.client.Minecraft.getInstance().level;
        if (level == null) return;
        net.minecraft.world.level.block.entity.BlockEntity be = level.getBlockEntity(state.blockPos);
        if (!(be instanceof BlockEntitySplitter tile)) return;
        if (!this.doRenderModel(poseStack, tile)) return;

        PoseStack snapped = new PoseStack();
        snapped.last().set(poseStack.last());
        RenderType rt = RenderTypes.entityCutout(SplitterModel.TEXTURE_LOCATION);
        int light = state.lightCoords;
        collector.submitCustomGeometry(poseStack, rt, (pose, vc) -> {
            MultiBufferSource oneRT = ignored -> vc;
            renderBlockEntitySplitterAt(snapped, tile, oneRT, light);
        });
        if (tile.isInWorld()) {
            IORenderer.renderIO(poseStack, collector, tile, tile.getBlockPos());
        }
    }

}

