/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.renders.m;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.fluids.FluidStack;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.blockentities.processing.BlockEntityFuelConverter;
import reika.rotarycraft.modinterface.model.FuelConverterModel;
import reika.rotarycraft.registry.RotaryModelLayers;

/** Original two-basin model and fluid volumes, using each fluid's registered 26.3 model and tint. */
public class RenderFuelConverter extends RotaryTERenderer<BlockEntityFuelConverter> {
    private final FuelConverterModel model;
    public RenderFuelConverter(BlockEntityRendererProvider.Context context) { model = new FuelConverterModel(context.bakeLayer(RotaryModelLayers.FUEL_ENHANCER)); }
    @Override protected boolean useEntityCutout() { return true; }
    @Override protected Identifier getSubmitTexture(BlockEntity be) { return FuelConverterModel.TEXTURE_LOCATION; }
    @Override protected void renderModel(PoseStack pose, BlockEntity be, VertexConsumer vertices, int light) {
        pose.pushPose();
        pose.translate(0.5, 1.5, 0.5);
        pose.rotate(Axis.ZP.rotationDegrees(180));
        model.renderAll(pose, vertices, light, be, null, -((BlockEntityFuelConverter)be).phi, 0);
        pose.popPose();
    }

    @Override public void submit(BlockEntityRenderState state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
        super.submit(state, pose, collector, camera);
        var level = Minecraft.getInstance().level;
        if (level != null && level.getBlockEntity(state.blockPos) instanceof BlockEntityFuelConverter tile) {
            submitFluid(tile, tile.getFluidInInput(), false, pose, collector, state.lightCoords);
            submitFluid(tile, tile.getFluidInOutput(), true, pose, collector, state.lightCoords);
        }
    }

    private static void submitFluid(BlockEntityFuelConverter tile, FluidStack fluid, boolean output, PoseStack pose, SubmitNodeCollector collector, int light) {
        if (fluid.isEmpty()) return;
        var state = fluid.getFluid().defaultFluidState();
        var model = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(state);
        var sprite = model.stillMaterial().sprite();
        int color = model.fluidTintSource() == null ? -1 : model.fluidTintSource().colorInWorld(state,
                tile.getBlockState(), Minecraft.getInstance().level, tile.getBlockPos());
        float bottom = (output ? 1 : 10) / 16F + 0.001F;
        float top = bottom + 0.95F / 3 * Math.min(1, fluid.getAmount() / (float) tile.getCapacity());
        int fullLight = fluid.getFluidType().getLightLevel(fluid) > 0 ? 0xf000f0 : light;
        PoseStack snapshot = new PoseStack();
        snapshot.last().set(pose.last());
        collector.submitCustomGeometry(pose, RenderTypes.entityTranslucent(TextureAtlas.LOCATION_BLOCKS), (submittedPose, vertices) -> {
            float low = 0.005F, high = 0.995F;
            face(snapshot, vertices, sprite, color, fullLight, new float[][]{{low, top, low},{low,top,high},{high,top,high},{high,top,low}}, 0, 1, 0);
            face(snapshot, vertices, sprite, color, fullLight, new float[][]{{low,bottom,high},{low,bottom,low},{high,bottom,low},{high,bottom,high}}, 0, -1, 0);
            face(snapshot, vertices, sprite, color, fullLight, new float[][]{{low,bottom,low},{low,top,low},{high,top,low},{high,bottom,low}}, 0, 0, -1);
            face(snapshot, vertices, sprite, color, fullLight, new float[][]{{high,bottom,high},{high,top,high},{low,top,high},{low,bottom,high}}, 0, 0, 1);
            face(snapshot, vertices, sprite, color, fullLight, new float[][]{{low,bottom,high},{low,top,high},{low,top,low},{low,bottom,low}}, -1, 0, 0);
            face(snapshot, vertices, sprite, color, fullLight, new float[][]{{high,bottom,low},{high,top,low},{high,top,high},{high,bottom,high}}, 1, 0, 0);
        });
    }

    private static void face(PoseStack pose, VertexConsumer vertices, TextureAtlasSprite sprite, int color, int light, float[][] points, float nx, float ny, float nz) {
        for (int i = 0; i < 4; i++)
            vertices.addVertex(pose.last(), points[i][0], points[i][1], points[i][2]).setColor(color)
                    .setUv(i < 2 ? sprite.getU0() : sprite.getU1(), i == 0 || i == 3 ? sprite.getV1() : sprite.getV0())
                    .setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose.last(), nx, ny, nz);
    }
}
